package com.idega.block.messenger.data;


public class Message extends Packet{
  private String theMessage;
  private String theSender;
  private String theRecipient;
  private String theSenderName;
  private String theRecipientName;
  private int hashId = 0;

  public Message (String message, String senderId, String recipientId, String senderName){
    this.theSender = senderId;
    this.theRecipient = recipientId;
    this.theMessage = message;
    this.theSenderName = senderName;
  }

  public Message (String message, String senderId, String recipientId){
    this(message, senderId, recipientId,null);
  }

  public Message (String message, String senderId){
    this(message, senderId, senderId,null);
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
    return hashId;
  }
}