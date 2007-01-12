package com.idega.block.messenger.business;



import java.util.HashMap;

import java.util.Vector;

import com.idega.block.messenger.data.Packet;

import com.idega.block.messenger.data.Message;





/**

 * Title:        com.idega.block.messenger.business

 * Description:  idega classes

 * Copyright:    Copyright (c) 2001

 * Company:      Idega Software

 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>

 * @version 1.0

 */



public class MessageManager implements PacketManager{

  public static HashMap inbox = new HashMap();



  public static void sendToInbox(Message message, String recipientId){

   //System.out.println("MessageManager : before inbox get");

    Vector messageVector = (Vector) inbox.get(recipientId);

     //System.out.println("MessageManager : after inbox get");

    if( messageVector == null ){

      messageVector = new Vector();

    }

    messageVector.add(message);

    inbox.put(recipientId,messageVector);

  }



  public void processPacket(Packet packet){

  //get the stored messages from the client packet

    if(packet!=null){

      Vector messages = packet.getMessages();

      if( messages!=null ){

        int length = messages.size();

        String senderName;

        String recipientName;

        //System.out.println("MessageManager : "+length+" Messages in packet.");

        for (int i = 0; i < length; i++) {

          Message msg = (Message) messages.elementAt(i);

          //System.out.println("MessageManager : getting sender name from ClientManager");



          //System.out.println("MessageManager : msg.getsender"+msg.getSender());



          senderName = ClientManager.getClientName(msg.getSender());



          if ( senderName != null ){

             msg.setSenderName(senderName);

            //System.out.println("MessageManager : "+senderName);

          }

          else {

            msg.setSenderName("Unknown sender");

            //System.out.println("MessageManager : Unknown sender");

          }



           //System.out.println("MessageManager : msg.getRecipient"+msg.getRecipient());



          recipientName = ClientManager.getClientName(msg.getRecipient());

          if ( recipientName != null ) {
			msg.setRecipientName(recipientName);
		}
		else {

            msg.setRecipientName("Unknown recipient");

            //System.out.println("MessageManager : Unknown recipient");

          }



          MessageManager.sendToInbox( msg, msg.getRecipient() );



        }

      }

      //else System.out.println("MessageManager : No messages in packet!");





    //set the stored messages to the client packet

     packet.clearMessages();

     //System.out.println("MessageManager : getting stored messages");



      Vector storedMessages = (Vector) MessageManager.inbox.get(packet.getSender());

      if( storedMessages!=null ){

        packet.setMessages(storedMessages);

      }

      //else  System.out.println("MessageManager : No stored messages!");



      inbox.remove(packet.getSender());

      //this ignores if the messages got to the client or not. That requers an Observer pattern

      //System.out.println("MessageManager : done getting stored messages");

    }

    //else  System.out.println("MessageManager : packet is NULL!");

  }



  public static void moveMessages(String fromId, String toId){



    Vector messageFrom = (Vector) inbox.get(fromId);

    Vector messageTo = (Vector) inbox.get(toId);



    if( messageFrom != null ){

      if(messageTo==null){

        messageTo = new Vector();

      }

      int length = messageFrom.size();

      Message msg;

      for (int i = 0; i < length; i++) {

        msg = (Message)messageFrom.elementAt(i);

        msg.setRecipient(toId);

        messageTo.addElement(msg);

      }

      inbox.remove(fromId);

      inbox.put(toId,messageTo);

    }



  }



}
