package se.idega.idegaweb.commune.message.business;

import se.idega.idegaweb.commune.message.data.*;

import com.idega.business.IBOServiceBean;
import com.idega.data.*;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class MessageBusinessBean extends com.idega.block.process.business.CaseBusinessBean implements MessageBusiness{

  private String TYPE_USER_MESSAGE="SYMEDAN";
  private String TYPE_SYSTEM_PRINT_MAIL_MESSAGE="SYMEBRV";
  private String TYPE_SYSTEM_PRINT_ARCHIVATION_MESSAGE="SYMEARK";

  public MessageBusinessBean() {
  }

  private MessageHome getMessageHome(String messageType) throws java.rmi.RemoteException{
    if(messageType.equals(TYPE_USER_MESSAGE)){
      return (MessageHome) com.idega.data.IDOLookup.getHome(UserMessage.class);
    }
    else{
      throw new java.lang.UnsupportedOperationException("MessageType "+messageType+" not yet implemented");
    }
  }

  private String getTypeUserMessage(){return TYPE_USER_MESSAGE;}
  private String getTypeMailMessage(){return TYPE_SYSTEM_PRINT_MAIL_MESSAGE;}
  private String getTypeArchivationMessage(){return TYPE_SYSTEM_PRINT_ARCHIVATION_MESSAGE;}

  public Message getMessage(String messageType,int messageId) throws Exception {
    return getMessageHome(messageType).findByPrimaryKey(new Integer(messageId));
  }

  public Collection findMessages(int userId) throws Exception {
    //return getMessageHome().findMessages(userId);
    return null;
  }

  public Message createUserMessage(int userID,String subject,String body)throws CreateException,RemoteException{
    Message message = createMessage(getTypeUserMessage(),userID,subject,body);
    return message;
  }

  public Message createPrintedLetterMessage(int userID,String subject,String body)throws CreateException,RemoteException{
    Message message = createMessage(getTypeMailMessage(),userID,subject,body);
    return message;
  }

  public Message createPrintArchivationMessage(int userID,String subject,String body)throws CreateException,RemoteException{
    Message message = createMessage(getTypeArchivationMessage(),userID,subject,body);
    return message;
  }

  private Message createMessage(String messageType,int userID,String subject,String body)throws CreateException,RemoteException{
    MessageHome home = this.getMessageHome(messageType);
    Message message = home.create();
    try{
      message.setOwner(getUser(userID));
    }
    catch(FinderException fex){
      throw new IDOCreateException(fex);
    }
    message.setSubject(subject);
    message.setBody(body);
    try{
      message.store();
    }
    catch(IDOStoreException idos){
      throw new IDOCreateException(idos);
    }
    return message;
  }
}
