package com.idega.block.messenger.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idega.block.messenger.business.ClientManager;
import com.idega.block.messenger.business.MessageManager;
import com.idega.block.messenger.data.Packet;
import com.idega.servlet.IWCoreServlet;

public class ClientServer extends IWCoreServlet{
  public static String MESSENGER_JAR_FILE = "messenger.jar";
  public static String MESSENGER_APPLET_CLASS = "com.idega.block.messenger.presentation.MessengerApplet";
  public static String SERVLET_URL = "servlet/ClientServer";

    /**
     *  This method is used for applets.
     *
     *  Receives and sends the data using object serialization.
     *
     *  Gets an input stream from the applet and reads a Packet object.  Then
     *  registers the Packet to that users session.  Finally, sends a confirmation
     *  message back to the applet.
     */

    public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException{
      Packet packet = receivePacket(request);
      ClientManager cManager = (ClientManager) getApplication().getAttribute("ClientManager");

      if( cManager == null){
        cManager = new ClientManager();
        getApplication().setAttribute("ClientManager",cManager);
      }

      cManager.processPacket(packet);
     //System.out.println("Client Manager is done");
      MessageManager mManager = (MessageManager) getApplication().getAttribute("MessageManager");

      if( mManager == null){
        mManager = new MessageManager();
        getApplication().setAttribute("MessageManager",mManager);
      }

      mManager.processPacket(packet);
      //System.out.println("Message Manager is done");

      sendPacket(response, packet);
    }

	/**
	 *  calls doPost
	 */
    public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        //System.out.println("ClientServer : in doGet ");
        doPost(request,response);
    }


    private void sendPacket(HttpServletResponse response, Packet packet){
        ObjectOutputStream outputToApplet;

        try{
          response.setContentType("application/octet-stream");

          outputToApplet = new ObjectOutputStream(response.getOutputStream());

          //System.out.println("sendPacket : Sending Packet to applet...");
          outputToApplet.writeObject(packet);
          outputToApplet.flush();

          outputToApplet.close();
          //System.out.println("sendPacket : Data transmission complete.");
        }
        catch (IOException e){
          e.printStackTrace(System.err);
        }
    }

    private Packet receivePacket(HttpServletRequest request){
      ObjectInputStream inputFromApplet = null;
      Packet packet = null;

      try{
        // get an input stream from the applet
        inputFromApplet = new ObjectInputStream(request.getInputStream());
        //System.out.println("receivePacket : Connected");

        // read the serialized Packet data from applet
        //System.out.println("receivePacket : Reading data...");
        packet = (Packet) inputFromApplet.readObject();
        //System.out.println("receivePacket : Finished reading.");

        inputFromApplet.close();

        //System.out.println("receivePacket : Complete.");

      }
      catch (Exception e){
        e.printStackTrace(System.err);
      }

      return packet;
    }

}