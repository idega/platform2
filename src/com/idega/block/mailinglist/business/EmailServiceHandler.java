package com.idega.block.mailinglist.business;

import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.idega.presentation.IWContext;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

/**
 * Impliments the POP3 protocall to retrieve messages from mail servers.
 * Impliments STMP/MIME protocall for sending messages.
 */

public class EmailServiceHandler /*implements ServiceInterface*/{

  private String server = "default_server";
  private String serverType = "default_server_type";
  private String userName = "default_server_username";
  private String password = "default_server_user_passw";

  private String sendServer = "default_server";
  private String sendUserName = "default_send_server_username";
  private String sendPassword = "default_send_server_user_passw";

  public EmailServiceHandler() {
  }

  public void setUser(String userName){
    this.userName = userName;
  }

  public String getUser(){
    return this.userName;
  }

  public void setUserPassword(String password){
    this.password = password;
  }

  public String getUserPassword(){
    return this.password;
  }

  public void setServer(String server){
    this.server = server;
  }

  public String getServer(){
    return this.server;
  }

  public void setServerType(String serviceType){
    this.serverType = serviceType;
  }

  public String getServerType(){
    return this.serverType;
  }

  public void connectToServer(){}

  public void disconnectFromServer(){}

  public Message[] getServicesAndCloseConnection() throws MessagingException{

    String host = this.server;
    String username = userName;

    // Create empty properties
    Properties properties = new Properties();
    // Get session
    Session session = Session.getDefaultInstance(properties, null);

    // Get the store
    Store store = session.getStore(serverType);
    store.connect(server, userName, password);

    // Get folder
    Folder folder = store.getFolder("INBOX");
    folder.open(Folder.READ_ONLY);

    // Get directory
    Message message[] = folder.getMessages();

    /*
    for (int i=0, n=message.length; i<n; i++) {
       System.out.println(i + ": " + message[i].getFrom()[0]
         + "\t" + message[i].getSubject());
    }
    */

    // Close connection
    folder.close(false);
    store.close();

    return message;
  }


  public void sendService(Message[] messages){}

  public void getProtocall(){}

  /*public void getAllInboxHeadersAndCloseConnection(String server, String protcol, String userName, String password) throws MessagingException{
    Message[] messages = getServicesAndCloseConnection();
    InboxData letter;
    for (int i = 0; i < messages.length; i++) {
      letter = new InboxData();
      letter.setHeader(messages[i]);
    }
  }

  private InboxData setContent(InboxData letter, Message message)throws MessagingException, IOException{
  //Deals only with simple text messages
    if (message.isMimeType("text/plain")){

       return letter;
    }

    //Deals with messages that could withhold attachments (
    else if(message.isMimeType("multipart/*")){

      String bodyString = "";
      Multipart multiPart = (Multipart) message.getContent();
//      letter.setBody(); KLÁRA!!!

      for (int j=0, n=multiPart.getCount(); j<n; j++) {

        Part part = multiPart.getBodyPart(j);
        String disposition = part.getDisposition();

        if (disposition == null) {

          // Handle plain and html
          if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
            bodyString += part.getContent();
          }

          //Handle gif-images
          else if (part.isMimeType("image/*")){
          }
          //... and other specific MIME types could be also handled specially

        }
        else if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) ||
          (disposition.equals(Part.INLINE)))) {
          ICFile file;
          file = ((com.idega.core.data.ICFileHome)com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).createLegacy();
          file.setDescription(part.getDescription());
          file.setFileValue(part.getInputStream());
          file.setFileSize(part.getSize());
//          file.setLanguage();   language is missing??
//          file.setMimeType();
//          file.setModificationDate(); date is missing??
          if ((part.getFileName() == null) || (part.getFileName().equalsIgnoreCase(""))) {
            file.setName("unknownFileName"+j);
          }
          else file.setName(part.getFileName());

          letter.setAttachments(file);
        }


        This condition should never be possible!!!?? At least not according
        to the MIME standards used here.

        else {
          //I have no idea what I can do here??
        }

      }
      //Body of a message set in multipart messages
      letter.setBody(bodyString);
      return letter;
    }
    return letter;
  }

  public void getAllInboxMessagesAndCloseConnection(String server, String protcol, String userName, String password) throws MessagingException, IOException{
    Message[] messages = getServicesAndCloseConnection();
    InboxData letter;
    for (int i = 0; i < messages.length; i++) {
      letter = new InboxData();

      //Downloading the whole message with attachments and all.
      letter.setIsContentSet(true);
      letter.setHeader(messages[i]);

      letter = setContent(letter, messages[i]);
    }
  }*/

  /*A simple recursive bottom-up algorithm that flattens/destroys tree
    structure of parts into a vector of parts.*/
  private static Vector getInnerParts(Part part) throws MessagingException, IOException{
    Vector parts = new Vector();
    if (part.isMimeType("multipart/*")) {
      Multipart multiPart = (Multipart) part.getContent();
      for (int i = 0; i < multiPart.getCount(); i++) {
        Part innerPart = multiPart.getBodyPart(i);
        getInnerParts(innerPart);
        Vector innerVector = getInnerParts(innerPart);
        for (int j = 0; j < innerVector.size(); j++) {
          parts.add(innerVector.get(j));
        }
      }
    }
    else {
      parts.add(part);
    }
    return parts;
  }

  //Returns an array of (non-multiparts) parts, which contains all the attachment files in the message
  public static Part[] getAttachments(Message message) throws MessagingException, IOException{
    Vector partsVector = new Vector();
    if(message.isMimeType("multipart/*")){
      partsVector = getInnerParts(message);
      for (int j=0, n=partsVector.size(); j<n; j++) {
        String disposition = ((Part) partsVector.get(j)).getDisposition();
        if ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE))) {
          partsVector.removeElementAt(j);
        }
      }
    }
    return (Part[]) partsVector.toArray(new Part[0]);
  }

  public static boolean hasAttachments(Message message) throws MessagingException, IOException{
    boolean hasAttachments = false;
    if (message.isMimeType("multipart/*")){
      Multipart multiPart = (Multipart) message.getContent();
      for (int i=0, n=multiPart.getCount(); i<n; i++) {
        Part part = multiPart.getBodyPart(i);
        String disposition = part.getDisposition();
        if ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE))) {
          return true;
        }
      }
    }
    return hasAttachments;
  }

  public static String getStringBody(Message message) throws MessagingException, IOException{
    String bodyString = "";
    //Deals only with simple text messages
    if (message.isMimeType("text/plain")){
       bodyString = bodyString + (String) message.getContent();
       return bodyString;
    }

    //Deals with messages that could withhold attachments (
    else if(message.isMimeType("multipart/*")){

      Multipart multiPart = (Multipart) message.getContent();
      for (int j=0, n=multiPart.getCount(); j<n; j++) {

        Part part = multiPart.getBodyPart(j);
        String disposition = part.getDisposition();

        if (disposition == null) {
          // Handle plain and html
          if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
            //Á að setja eitthvert bil á milli strengjanna??
            bodyString = bodyString + (String) part.getContent();
          }
        }
      }
    }
    return bodyString;
  }

  public static void removeMessages(Message[] messages) throws MessagingException{
    if (messages != null) {
      for (int i = 0; i < messages.length; i++) {
        messages[i].setFlag(Flags.Flag.DELETED, true);
      }
    }
  }

  /*Sets the flag of the messages which are to be deleted, but dosn´t expunge until the user
    is disconnected from the server.  That is when his session outdates (, or he logs out).*/
  public static void removeMessages(IWContext iwc, String[] checkedMessages) throws MessagingException{
    if (checkedMessages != null) {
      int messageID;
      Folder inbox = null;
      inbox = getInbox(iwc);
      for (int i = 0; i < checkedMessages.length; i++) {
        messageID = Integer.parseInt(checkedMessages[i]);
        inbox.getMessage(messageID).setFlag(Flags.Flag.DELETED, true);
      }
    }
  }

  public static Message[] getMessages(IWContext iwc) throws MessagingException{
    Folder inbox = getInbox(iwc);
    return inbox.getMessages();
  }

  public static String getStringAddresses(Address[] addresses){
    String sAddresses = "";
    for (int i = 0; i < addresses.length; i++) {
      sAddresses += ", "+addresses[i].toString();
    }
    return sAddresses;
  }

  //Counts the messages that have not been deleted
  public static int countMessages(IWContext iwc) throws MessagingException{
    Message[] messages = getMessages(iwc);
    int count = 0;
    for (int i = 0; i < messages.length; i++) {
      if (!messages[i].getFlags().contains(Flags.Flag.DELETED)) count++;
    }
    return count;
  }

  public static Folder getInbox(IWContext iwc/*, String host , User user*/) throws MessagingException{
    InboxManager inboxManager;

    //The inbox has not been opened in this session.
    if (iwc.getSessionAttribute("inboxmanager") == null){
      //temporarily
      String host = "mail.idega.is";
      String user = "eiki";
      String password = "p1par";
      int post = 110;
      URLName url = new URLName("pop3", host, 110, "", user, password);
      inboxManager = new InboxManager(url);
      iwc.setSessionAttribute("inboxmanager",inboxManager);
    }

    //The inbox has been opened, and is about to be used again.
    else{
      inboxManager = (InboxManager) iwc.getSessionAttribute("inboxmanager");
    }
    return inboxManager.getInbox();
  }
}
