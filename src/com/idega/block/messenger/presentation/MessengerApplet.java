package com.idega.block.messenger.presentation;

import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.awt.Component;
import java.awt.Label;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import com.idega.block.messenger.data.Message;
import com.idega.block.messenger.data.Packet;
import com.idega.block.messenger.data.Property;
import com.idega.block.messenger.business.MessageListener;
import com.idega.presentation.awt.ImageLabel;
import com.idega.presentation.awt.SingleLineItem;

/**
 * Title:        MessengerApplet
 * Description:  Simple client sceleton
 * Copyright:    Copyright (c) 2001
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class MessengerApplet extends Applet implements  ActionListener{
  private static String FRAME_NAME= "IdegaWeb Messenger";
  private static int FRAME_WIDTH = 295;
  private static int FRAME_HEIGHT = 310;
  private static String SESSION_ID = "session_id";
  private static String USER_ID = "user_id";
  private static String USER_NAME = "user_name";
  private static String USER_LIST = "user_list";
  private static String USER_LIST_VERSION = "user_list_version";

  private static String SERVLET_URL = "servlet_url";
  private static String SERVER_ROOT_URL = "server_root_url";
  private static String RESOURCE_URL = "resource_url";
  private static String LOG_OUT = "log_out";
  private boolean loggingOff = false;


  private String sessionId;
  private String userId;
  private String userName;
  private String userListVersion = "v.0";
  private String servletURL;
  private URL hostURL;
  private String resourceURL;

  private Hashtable dialogs = new Hashtable();
  private MessageListener cycler;

  private AudioClip alertSound;

  private String keyPressed=null;
  //private Image offscreenImage;
  //private Graphics offscr;

  private long checkTimer = 3000;

  private Packet packetToServlet;
  private Packet packetFromServlet;


  /**Construct the applet*/
  public MessengerApplet() {
    /**@todo make this a parameter*/
  }


  /**Initialize the applet*/
  public void init() {
    try {
      sessionId = this.getParameter(SESSION_ID, "noId");
      userId = this.getParameter(USER_ID, "-1");
      userName = this.getParameter(USER_NAME, "Anonymous");
      servletURL = this.getParameter(SERVLET_URL, "servlet/ClientServer");
      hostURL = new URL(this.getParameter(SERVER_ROOT_URL, getCodeBase().getProtocol()+"://"+getCodeBase().getHost()+":"+getCodeBase().getPort()));
      resourceURL = this.getParameter(RESOURCE_URL,"/idegaweb/bundles/com.idega.block.messenger.bundle/resources/");

      if(cycler==null){
        cycler = new MessageListener(checkTimer);
        cycler.addActionListener(this);
        cycler.start();
      }

    }
    catch(MalformedURLException e) {
      System.out.println("MessageApplet: error in init getting parameters!");
      e.printStackTrace(System.err);
    }

    setBackground(Color.white);
    alertSound = getAudioClip(getCodeBase(),"notify.au");

  }

    /**
   * Display the list of Messages <br>
   *
   * Iterate over the vector of Messages and display
   */
  private void dispatchMessagesToDialogs(Vector MessageVector){
    Enumeration enumer = MessageVector.elements();
    Message aMessage = null;

    while (enumer.hasMoreElements()){
      aMessage = (Message) enumer.nextElement();
      MessageDialog messageDialog = (MessageDialog) dialogs.get(Integer.toString(aMessage.getId()));
      if( messageDialog == null ) { //create a new dialog
        messageDialog = createAMessageDialog(false,aMessage);
      }

      messageDialog.addMessage(aMessage);
      messageDialog.setVisible(true);
    }
  }

  private MessageDialog createAMessageDialog(boolean newId, Message aMessage){
    MessageDialog messageDialog;
    messageDialog = new MessageDialog(FRAME_NAME,aMessage,new ImageLabel(getImage(getCodeBase(),"idegalogo.gif")));

    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    messageDialog.setLocation((d.width - messageDialog.getSize().width) / 2, (d.height - messageDialog.getSize().height) / 2);
    messageDialog.setSize(FRAME_WIDTH,FRAME_HEIGHT);
    messageDialog.addActionListener(this);
    if( alertSound!=null ) messageDialog.setAudioClip(alertSound);
    else System.out.println("alert is null");

    if( newId ) dialogs.put(Integer.toString(messageDialog.hashCode()),messageDialog);
    else dialogs.put(Integer.toString(aMessage.getId()),messageDialog);

    return messageDialog;
  }


  private synchronized void getMessagesFromDialog(MessageDialog dialog){//gets called on and iw-send event action
    if( packetToServlet == null ){
      packetToServlet = new Packet();
    }
    packetToServlet.setSender(sessionId);

    Vector msg = dialog.getMessages();
    int length = msg.size();
    for (int i = 0; i < length; i++) {
      ((Message)msg.elementAt(i)).setSender(sessionId);
    }


    /**@todo make this work for many dialogs..*/
    packetToServlet.addMessages(msg);
    dialog.clearMessageVector();
    cycle();
  }

  private URLConnection getURLConnection(){
    System.out.println("GETTING URLConnection");

    URLConnection servletConnection = null;

    try{
        // connect to the servlet
        System.out.println("Connecting to servlet...");
        URL servlet = new URL(hostURL,servletURL);

        servletConnection = servlet.openConnection();
        System.out.println("Connected");

        // inform the connection that we will send output and accept input
        servletConnection.setDoInput(true);
        servletConnection.setDoOutput(true);

        // Don't used a cached version of URL connection.
        servletConnection.setUseCaches(false);

        // Specify the content type that we will send binary data
        servletConnection.setRequestProperty("Content-Type", "application/octet-stream");
       //servletConnection.setRequestProperty("Connection", "Keep-Alive");

      }
      catch (Exception e){
          System.out.println("MessengerApplet : error in getURLConnection"+e.toString());
          e.printStackTrace(System.err);
      }
      return servletConnection;
    }


  /**
  *  Sends the message object to a servlet. It is serialized over the URL connection
  */
  private void sendPacket(URLConnection conn){
    ObjectOutputStream outputToServlet = null;
    try{
        if( packetToServlet == null) packetToServlet = new Packet();

        if( !loggingOff ){
          packetToServlet.addProperty(new Property(SESSION_ID,sessionId));
          packetToServlet.addProperty(new Property(USER_ID,userId));
          packetToServlet.addProperty(new Property(USER_LIST_VERSION,userListVersion));
          packetToServlet.setSender(sessionId);
        }

        System.out.println("sending packets");
        outputToServlet = new ObjectOutputStream(conn.getOutputStream());
        // serialize the object
        outputToServlet.writeObject(packetToServlet);

        outputToServlet.flush();
        outputToServlet.close();

        System.out.println("Sending Complete.");
    }
    catch (IOException e){
        System.out.println(e.getMessage());
        e.printStackTrace(System.err);
    }
  }

    /**
     * Read the input from the servlet.  <b>
     *
     * The servlet will return a serialized Packet ( with messages, processes and/or property changes )
     *
     */
    private void receivePacket(URLConnection conn){
      packetFromServlet = null;
      ObjectInputStream theInputFromServlet = null;
      try{
        System.out.println("receiving packets");

          theInputFromServlet = new ObjectInputStream(conn.getInputStream());
            // read the serialized Packet from the servlet
          System.out.println("Reading data...");
          packetFromServlet = (Packet) theInputFromServlet.readObject();
          theInputFromServlet.close();
          System.out.println("Finished reading data.");

        }
        catch (IOException e){
          System.out.println(e.getMessage());
          e.printStackTrace(System.err);
        }
        catch (ClassNotFoundException e){
          System.out.println(e.getMessage());
          e.printStackTrace(System.err);
        }

    }

  private void processPacket(){

    System.out.println("processing the packet ...");

    if( packetFromServlet!=null ){
      packetFromServlet.process(this);

      //Message stuff
      Vector messages = packetFromServlet.getMessages();
      if( messages!= null) dispatchMessagesToDialogs(messages);

      //Property stuff userlists etc.
      Vector props = packetFromServlet.getProperties();
      Vector userlist = null;

      if( props!=null ){
        int length = props.size();
        for (int i = 0; i < length; i++) {
          if( ((Property)props.elementAt(i)).getKey().equals(USER_LIST) ){
            userlist = (Vector)((Property)props.elementAt(i)).getValue();
            //update the userlist
            if(userlist!=null){
              syncUserList(userlist);
            }
          }
          else if (((Property)props.elementAt(i)).getKey().equals(USER_LIST_VERSION) ){
           userListVersion = (String)((Property)props.elementAt(i)).getValue();
          }
        }
      }

    }else{
     System.err.println("MessengerApplet : packetFromServlet == null !!");
    }
    System.out.println("DONE! processing the packet");

    refresh();

    packetToServlet = null;


  }

  private void syncUserList(Vector userlist){
   this.removeAll();
   int length = userlist.size();

   for (int k = 0; k < length; k++) {
    Property user = (Property)userlist.elementAt(k);
    String id = user.getKey();
    if( !id.equalsIgnoreCase(sessionId) ){
         addToUserList( id , (String)user.getValue() );//new user
    }
       refresh();

   }




    System.out.println("MessengerApplet: userListVersion : "+userListVersion);
  }

  private void addToUserList(String sendToId, String name){

    System.out.println("MessengerApplet: Adding to userlist! id: "+sendToId+" name: "+name);
      Message msg = new Message();
      msg.setSender(sendToId);
      msg.setSenderName(name);
      msg.setRecipientName(userName);

      MessageDialog dialog = createAMessageDialog(true,msg);

      SingleLineItem item = new SingleLineItem(this);
      item.setId(sendToId);
      item.setWindowToOpen(dialog);
      item.addActionListener(this);

      item.add(new ImageLabel(getImage(getCodeBase(),"face_in.gif")));
      item.setNextToFillRight(true);
      item.add(new Label(name));

      add(item);


  }

  private void refresh(){
   doLayout();
   Component[] comps = getComponents();
   for (int i = 0; i < comps.length; i++) {
    comps[i].doLayout();
    comps[i].repaint();
    comps[i].paintAll(getGraphics());
   }


   repaint();
  }

  public synchronized void cycle(){
    URLConnection conn = getURLConnection();
    // send the Packet object to the servlet using serialization
    sendPacket(conn);

    // now, let's read the packet from the servlet.
    receivePacket(conn);

    // get messages, perform processes change properties
    processPacket();

    conn = null;
  }

  private Packet getPacketToServlet(){
    return this.packetToServlet;
  }

  private Packet getPacketFromServlet(){
    return this.packetFromServlet;
  }

  public synchronized void actionPerformed(ActionEvent e){
    String action = e.getActionCommand();

    if(action.equalsIgnoreCase("iw-send")){
      MessageDialog d = (MessageDialog) e.getSource();
      getMessagesFromDialog(d);
    }
    else if(action.equalsIgnoreCase("iw-cycle")){
     cycle();
    }

    System.out.println("MessengerApplet: action command was :"+action);
    refresh();
  }

 public void update(Graphics g){
  paint(g);
 }

  /**Start the applet*/
  public void start() {
  }

  /**Stop the applet*/
  public void stop() {
    logOff();

    if(cycler!=null){
     cycler.stop();
    }
  }

  protected void logOff(){
    packetToServlet = new Packet();
    packetToServlet.addProperty(new Property(LOG_OUT,sessionId));
    loggingOff = true;
    sendPacket(getURLConnection());
  }

  /**Destroy the applet*/
  public void destroy() {
    logOff();

    Graphics g = getGraphics();
    if(g != null) {
        g.dispose(); // crucial
        g = null;
    }

    if(cycler!=null){
     cycler.destroy();
    }

    dialogs.clear();
    dialogs=null;
/**@todo travers through hashtable and do this
      messageDialog.setVisible(false);
      messageDialog.cancel();
      messageDialog.dispose();
      messageDialog = null;
*/
  }

  /**Get Applet information*/
  public String getAppletInfo() {
    return FRAME_NAME;
  }


  /**Get a parameter value*/
  public String getParameter(String key, String def) {
    return (getParameter(key) != null ? getParameter(key) : def);
  }

  /**Get parameter info*/
  public String[][] getParameterInfo() {
    String[][] pinfo =
      {
      {"session_id", "String", "The users sessionId"},
      {"user_id", "String", "The users memberId"},
      {"servlet_url", "String", "The clientServers url"},
      };
    return pinfo;
  }

}