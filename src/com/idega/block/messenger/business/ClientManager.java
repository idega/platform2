package com.idega.block.messenger.business;

import java.util.Hashtable;
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

  public static void clientCheckIn(String clientId, String memberId){
    try{
      User user = new User(Integer.parseInt(memberId));
      clients.put(clientId,user);
    }
    catch(SQLException e){
      e.printStackTrace(System.err);
      System.err.println("ClientManager : new User() failed!");
    }
  }

  public static void clientCheckOut(String clientId, String memberId){

  }

  public static String getClientName(String clientId){
    User user = (User) clients.get(clientId);
    if( user!=null ){
      return user.getName();
    }
    else return null;
  }

  public void processPacket(Packet packet){
  System.out.println("ClientManager : process packet");

    if( packet!=null ){
      Vector props = packet.getProperties();
      if( props!=null ){
        int length = props.size();
        //System.out.println("ClientManager : PropSize is: "+length);

        for (int i = 0; i < length; i++) {
          Property prop = (Property) props.elementAt(i);
          String key = prop.getKey();
          String value = prop.getValue();
          //System.out.println("ClientManager : Property key: "+key+" ; value: "+value);

          User clientId = (User) ClientManager.clients.get(key);
          //System.out.println("ClientManager : After clients.get(key)");

          if( clientId == null ) clientCheckIn(key,value);//register this client

         // else System.out.println("ClientManager : clientId != null "+clientId);

        }
      }
    }
    else  System.out.println("ClientManager : client sending. no packet");

  }


  public static Vector getConnectedClients(){
    Vector connClients=new Vector();
    Enumeration enum = ClientManager.clients.keys();

    while (enum.hasMoreElements()){
        connClients.add( (User) ClientManager.clients.get( (String)enum.nextElement()));
    }

    return connClients;
  }




}