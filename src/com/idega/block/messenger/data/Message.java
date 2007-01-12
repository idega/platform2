package com.idega.block.messenger.data;

import java.io.Serializable;



public class Message implements Serializable{

  private String theMessage;

  private String theSender;

  private String theRecipient;

  private String theSenderName;

  private String theRecipientName;

  private int hashId = 0;



  public Message (){

  }



  public Message (String message, String senderId){

    this();

    this.theSender = senderId;

    this.theMessage = message;

  }



  public Message (String message, String senderId, String recipientId){

    this(message, senderId);

    this.theRecipient = recipientId;



  }



  public Message (String message, String senderId, String recipientId, String senderName){

    this(message,senderId,recipientId);

    this.theSenderName = senderName;

  }





  public void setMessage(String theMessage){

   this.theMessage = theMessage;

  }



  public String getMessage(){

   return this.theMessage;

  }



  public void setSender(String senderId){

   this.theSender = senderId;

  }



  public String getSender(){

   return this.theSender;

  }



  public void setRecipient(String recipientId){

   this.theRecipient = recipientId;

  }



  public String getRecipient(){

   return this.theRecipient;

  }



  public void setSenderName(String senderName){

   this.theSenderName = senderName;

  }



  public String getSenderName(){

   return this.theSenderName;

  }



  public void setRecipientName(String recipientName){

   this.theRecipientName = recipientName;

  }



  public String getRecipientName(){

   return this.theRecipientName;

  }



  public void setId(int hashId){

   this.hashId = hashId;

  }



  public int getId(){

    return this.hashId;

  }

}
