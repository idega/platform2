package com.idega.block.messenger.presentation;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import java.io.*;
import java.net.*;

import com.idega.block.messenger.data.Message;
import com.idega.block.messenger.data.Packet;
import com.idega.block.messenger.data.Property;
import com.idega.block.messenger.business.MessageListener;

import com.idega.presentation.awt.SingleLineItem;

/**
 * Title:        MessengerApplet
 * Description:  Simple client sceleton
 * Copyright:    Copyright (c) 2001
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class MessengerApplet extends Applet implements Runnable{
  public boolean isStandalone = false;
  private boolean runThread = true;
  private boolean isfirstRun = true;
  private static String FRAME_NAME= "IdegaWeb Messenger";
  private static int FRAME_WIDTH = 295;
  private static int FRAME_HEIGHT = 310;
  private static String SESSION_ID = "session_id";
  private static String USER_ID = "user_id";
  private static String SERVLET_URL = "servlet_url";
  private static String SERVER_ROOT_URL = "server_root_url";
  private static String RESOURCE_URL = "resource_url";


  private Thread t;

  private String keyPressed=null;
  private Image offscreenImage;
  private Graphics offscr;
  private int checkTimer = 15;
  private long tm;
  private long threadSleep;

  private String sessionId;
  private String userId;
  private String servletURL;
  private URL hostURL;
  private String resourceURL;

  private Packet packetToServlet;
  private Packet packetFromServlet;

  private MessageDialog messageDialog;
  private MessageListener listener = new MessageListener(this);//should listen on a per window basis


  /**Get a parameter value*/
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  /**Construct the applet*/
  public MessengerApplet() {
  }


  /**Initialize the applet*/
  public void init() {
    try {
      sessionId = this.getParameter(SESSION_ID, "noId");
      userId = this.getParameter(USER_ID, "-1");
      servletURL = this.getParameter(SERVLET_URL, "servlet/ClientServer");
      hostURL = new URL(this.getParameter(SERVER_ROOT_URL, "http://iw.idega.is"));
      resourceURL = this.getParameter(SERVER_ROOT_URL, "http://iw.idega.is");

    java.net.URL url;
    Image img;

    try {
      url = SingleLineItem.class.getResource("face_in.gif");
      img=Toolkit.getDefaultToolkit().getImage(url);
    }
    catch (Exception ex) {
      img = getImage(new URL(hostURL+resourceURL),"face_in.gif");
      System.out.println("AAAAARRGG:"+hostURL+resourceURL);
    }



      SingleLineItem test = new SingleLineItem();
      test.setWindowToOpen(new Dialog(new Frame(),"test1"));
      test.add(img);
      add(test);

      SingleLineItem test2 = new SingleLineItem();
      test2.setWindowToOpen(new Dialog(new Frame(),"test2"));
      add(test2);

      SingleLineItem test3 = new SingleLineItem();
      test3.setWindowToOpen(new Dialog(new Frame(),"test3"));
      add(test3);


    }
    catch(Exception e) {
      e.printStackTrace();
    }
    try {
      initializeEngine();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**Component initialization*/
  private void initializeEngine() throws Exception {
    threadSleep = 1000*checkTimer;
  }

  public void run(){
    tm = System.currentTimeMillis();

    while(runThread){
      repaint();

      cycle();//get and send packets

      try {//keep the wait insync with the performance of the machine it is on
      	tm += threadSleep;
        t.sleep(Math.max(0, tm - System.currentTimeMillis()));
      }
      catch (InterruptedException e) { ; }
    }
  }

    /**
   * Display the list of Messages <br>
   *
   * Iterate over the vector of Messages and display
   */
  private void displayMessages(Vector MessageVector){
      Enumeration enum = MessageVector.elements();

      Message aMessage = null;

      while (enum.hasMoreElements()){

        aMessage = (Message) enum.nextElement();
        if( messageDialog == null ) {//debug this should be one window per chat
          messageDialog = new MessageDialog(new Frame(),FRAME_NAME,false,aMessage);
          Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
          messageDialog.setLocation((d.width - messageDialog.getSize().width) / 2, (d.height - messageDialog.getSize().height) / 2);
          messageDialog.setSize(FRAME_WIDTH,FRAME_HEIGHT);
          messageDialog.setVisible(true);
          listener.start();
        }

        messageDialog.setVisible(true);
        messageDialog.addMessage(aMessage);

      }
  }

  public void getMessagesFromDialog(){
    if( packetToServlet == null ){
      packetToServlet = new Packet();
      packetToServlet.setSender(sessionId);
    }
    System.out.println("In getMessagesFromDialog()");

    Vector msg = messageDialog.getMessages();
    if( msg!=null ) {
      packetToServlet.clearMessages();
      packetToServlet.setMessages(msg);
      messageDialog.clearMessageVector();
      cycle();
    }
  }

  private URLConnection getURLConnection(){
    PrintWriter outTest = null;
    BufferedReader inTest = null;
    Packet thePacket = null;
    URLConnection servletConnection = null;

    try{
        // connect to the servlet
        System.out.println("Connecting to servlet...");
        URL servlet = new URL(hostURL,servletURL);
        // System.out.println("SERVLET URL! :"+hostURL+servletURL);


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
          System.out.println(e.toString());
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
      System.out.println("sending packets");
      if( isfirstRun ){
        packetToServlet = new Packet();
        packetToServlet.addProperty(new Property(sessionId,userId));
        packetToServlet.setSender(sessionId);
        Message msg = new Message("TEST FROM MYSELF!",sessionId,sessionId);
        packetToServlet.addMessage(msg);
        isfirstRun = false;
      }

      if( packetToServlet != null ){
        outputToServlet = new ObjectOutputStream(conn.getOutputStream());

        // serialize the object
        outputToServlet.writeObject(packetToServlet);

        outputToServlet.flush();
        outputToServlet.close();
      }

      packetToServlet = null;
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
          conn=null;

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
    //packet.getProperties(); //get properties to change
    if( packetFromServlet!=null ){
      packetFromServlet.process(this);
      Vector messages = packetFromServlet.getMessages();
      if( messages!= null) displayMessages(messages);

      getMessagesFromDialog();//check dialog for new messages and send them

    }else{
     System.err.println("MessengerApplet : packetFromServlet == null !!");
    }

    System.out.println("DONE! processing the packet");


  }

  private void cycle(){
    URLConnection conn = getURLConnection();
    // send the Packet object to the servlet using serialization
    sendPacket(conn);

    // now, let's read the packet from the servlet.
    receivePacket(conn);

    // get messages, perform processes change properties
    processPacket();
  }

  public Packet getPacketToServlet(){
    return this.packetToServlet;
  }

  public Packet getPacketFromServlet(){
    return this.packetFromServlet;
  }

  public boolean keyDown(Event e, int key){
    /*
    switch (key) {
      case 1004://arrow up
        bally+=0.5f;
        break;
      case 1005://arrow down
        bally-=0.5f;
        break;
      case 1006://arrow left
        ballx-=0.5f;
        break;
      case 1007://arrow right
        ballx+=0.5f;
        break;
      case 32://spacebar
        break;
      case 1002 ://Page Up
        scene.defaultCamera.shift(0f,0f,0.2f);
        break;
      case 1003 ://Page Down
        scene.defaultCamera.shift(0f,0f,-0.2f);
        break;
      case 52 ://KeyPad 4
        scene.defaultCamera.shift(0.2f,0f,0f);
        break;
      case 53 ://KeyPad 5
        scene.defaultCamera.shift(0f,0.2f,0f);
        break;
      case 54 ://KeyPad 6
        scene.defaultCamera.shift(-0.2f,0f,0f);
        break;
      case 56 ://KeyPad 8
        scene.defaultCamera.shift(0f,-0.2f,0f);
        break;
      case 43 ://KeyPad +
        scene.scale(1.2f);
        break;
      case 45 ://KeyPad -
        scene.scale(0.8f);
        break;

      case 97 ://a
        antialias=!antialias; scene.setAntialias(antialias);
        break;
      case 65 ://A
        antialias=!antialias; scene.setAntialias(antialias);
        break;
      case 109 ://m
        for (int i=0;i<scene.objects;i++) scene.object[i].meshSmooth();
        break;
      case 77 ://M
        for (int i=0;i<scene.objects;i++) scene.object[i].meshSmooth();
        break;
      case 110 ://n
        scene.normalize();
        break;
      case 78 ://N
        scene.normalize();
        break;
      case 105 ://i
        idx3d.debug.Inspector.inspect(scene);
        break;
      case 73 ://I
        idx3d.debug.Inspector.inspect(scene);
        break;


      default:
        keyPressed = Integer.toString(key);
        break;
    }
*/
    return true;
  }

  public boolean keyUp(Event e, int key){
    //message = "value = " + key;
    return true;
  }

  public boolean mouseEnter(Event e, int x, int y){
    return true;
  }
  public boolean mouseExit(Event e, int x, int y){
    return true;
  }
  public boolean mouseDown(Event e, int x, int y){
    return true;
  }
  public boolean mouseUp(Event e, int x, int y){
    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    return true;
  }
  public boolean mouseMove(Event e, int x, int y){
    return true;
  }

  public boolean mouseDrag(Event e, int x, int y){
   /*
    repaint();*/
    return true;
  }

  /**
   *  Reads a text response from the servlet.
   */
  protected void readServletResponse(URLConnection servletConnection)
  {
      BufferedReader inFromServlet = null;

      try
      {
              // now, let's read the response from the servlet.
              // this is simply a confirmation string
              inFromServlet = new BufferedReader(new InputStreamReader(servletConnection.getInputStream()));

          String str;
          while (null != ((str = inFromServlet.readLine())))
          {
              System.out.println("Reading servlet response: " + str);
          }

          inFromServlet.close();
      }
      catch (IOException e)
      {
        System.out.println(e.toString());
      }
  }


/**Main method*/
  public static void main(String[] args) {
    if( args.length > 0 ) System.setProperty("resourceBase",args[0]);

    MessengerApplet applet=null;
    applet = new MessengerApplet();
    applet.isStandalone = true;

    Frame frame;
    frame = new Frame() {
      protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
          System.exit(0);
        }
      }
      public synchronized void setTitle(String title) {
        super.setTitle(title);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
      }
    };
    frame.setTitle(FRAME_NAME);
    //frame.getContentPane().add(applet, BorderLayout.CENTER);
    frame.add(applet, BorderLayout.CENTER);
    applet.init();
    applet.start();
    frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
    frame.setVisible(true);
  }

  public void update(Graphics g){
    super.paint(g);
  }

  public void paint(Graphics g){
  //use the update method
  }

  /**Start the applet*/
  public void start() {
    if ( t == null ){
      t = new Thread(this);
      t.start();
      runThread = true;
    }
  }
  /**Stop the applet*/
  public void stop() {
    if ( t != null ){
     // if(t.isAlive())
     // t.yield();
      runThread = false;
    }
    if ( messageDialog !=null ){
      messageDialog.setVisible(false);
      messageDialog.cancel();
      messageDialog.dispose();
      messageDialog = null;
    }

    if( listener!=null) listener.stop();

  }
  /**Destroy the applet*/
  public void destroy() {
    stop();
    Graphics g = getGraphics();
    if(g != null) {
        g.dispose(); // crucial
        g = null;
    }
    if ( t!=null ){
      t=null;
    }

    if ( messageDialog !=null ){
      messageDialog.setVisible(false);
      messageDialog.cancel();
      messageDialog.dispose();
      messageDialog = null;
    }

    listener = null;

  }
  /**Get Applet information*/
  public String getAppletInfo() {
    return FRAME_NAME;
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