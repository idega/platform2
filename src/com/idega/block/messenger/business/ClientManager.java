package com.idega.block.messenger.business;

import java.util.Hashtable;
import java.util.Map;/**@todo use this instead of Hashtable directly*/
import java.util.Vector;
import java.util.Enumeration;
import com.idega.core.user.data.User;
import java.sql.SQLException;
import com.idega.block.messenger.data.Property;
import com.idega.block.messenger.data.Packet;

/**
 * Title:        com.idega.block.messenger.business
 * Description:  idega classes
 * Copyright:    Copyright (c) 2001
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class ClientManager implements PacketManager{

  private static Hashtable clients = new Hashtable();
  private static Hashtable reverseClients = new Hashtable();//for double loggin check


  private static String SESSION_ID = "session_id";
  private static String USER_ID = "user_id";
  private static String USER_LIST = "user_list";
  private static String USER_LIST_VERSION = "user_list_version";
  private static String LOG_OUT = "log_out";

  private static String PREFIX = "v.";
  private int version = 0;

  public void clientCheckIn(String sessionId, String userId){
    try{
      User user = new User(Integer.parseInt(userId));
      clients.put(sessionId,user);
      removeDoubleRegistry(sessionId,userId);
      reverseClients.put(userId,sessionId);
      version++;
    }
    catch(SQLException e){
      e.printStackTrace(System.err);
      System.err.println("ClientManager : new User() failed!");
    }
  }

  public void clientCheckOut(String sessionId){//debug here is the place if you want to store offline messages
    System.out.println("ClientManager:LOGGIN OFF USER : "+this.getClientName(sessionId)+" sessionid "+sessionId);
    User user = (User) clients.get(sessionId);
    reverseClients.remove(Integer.toString(user.getID()));
    clients.remove(sessionId);
    version++;
  }

  public synchronized static String getClientName(String sessionId){
    User user = (User) clients.get(sessionId);
    if( user!=null ){
      return user.getName();
    }
    else return null;
  }

  public synchronized void processPacket(Packet packet){
 // System.out.println("ClientManager : process packet");
  String sessionId = null;
  String userId = null;
  String packetUserListVersion = null;

    if( packet!=null ){
      Vector props = packet.getProperties();
      if( props!=null ){
        int length = props.size();
        //System.out.println("ClientManager : PropSize is: "+length);

        for (int i = 0; i < length; i++) {
          Property prop = (Property) props.elementAt(i);
          String key = prop.getKey();
          if( key.equalsIgnoreCase(SESSION_ID) ){
            sessionId = (String) prop.getValue();
            System.out.println("ClientManager: session id "+sessionId);
          }
          else if( key.equalsIgnoreCase(USER_ID) ){
            userId = (String) prop.getValue();
            System.out.println("ClientManager: user id "+userId);
          }
          else if( key.equalsIgnoreCase(USER_LIST_VERSION) ){
            packetUserListVersion = (String) prop.getValue();
            System.out.println("ClientManager: user list version "+packetUserListVersion);
          }
          else if( key.equalsIgnoreCase(LOG_OUT) ){
            clientCheckOut((String) prop.getValue());
          }

        }
      }

      if( sessionId!=null ){
        User user = (User) ClientManager.clients.get(sessionId);//already logged on
        if( user == null ){
          clientCheckIn(sessionId,userId);//register this client and check that he's not double logged on
        }
      }

      //userlist stuff
      if( !getUserListVersion().equalsIgnoreCase(packetUserListVersion) ){
       //list changed update it..without self
        /**@todo make a removeProperty method in packet*/
        packet.clearProperties();
        packet.addProperty(new Property(USER_LIST, getConnectedClients(sessionId)));
        packet.addProperty(new Property(USER_LIST_VERSION, getUserListVersion()) );
      }

          //System.out.println("ClientManager : Property key: "+key+" ; value: "+value);
          //System.out.println("ClientManager : After clients.get(key)");
    }
    else  System.out.println("ClientManager : client sending. no packet");

  }

  public String getUserListVersion(){
   return (PREFIX+version);
  }

  public static Vector getConnectedClients(String sessionId){
    /**@todo: don't make a new instance everytime*/
    Vector connClients=new Vector();
    Enumeration enum = ClientManager.clients.keys();

    while (enum.hasMoreElements()){
      String id = (String)enum.nextElement();
        connClients.add( new Property(id,((User)ClientManager.clients.get(id)).getName()) );
    }

    return connClients;
  }

  private boolean removeDoubleRegistry(String sessionId, String userId){
    System.out.println("ClientManager: removeDoubleRegistry new sessionId : "+sessionId);

    boolean existed = false;
    String fromId = (String) reverseClients.get(userId);
    if( fromId!=null){
      System.out.println("ClientManager: removeDoubleRegistry old sessionId : "+fromId);
      MessageManager.moveMessages(fromId,sessionId);

      clients.remove(fromId);
      reverseClients.remove(userId);//old reference

      existed = true;
    }

    return existed;
  }




}