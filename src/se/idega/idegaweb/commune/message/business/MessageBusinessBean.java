package se.idega.idegaweb.commune.message.business;

import se.idega.idegaweb.commune.message.data.*;

import com.idega.business.IBOServiceBean;

import java.util.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class MessageBusinessBean extends IBOServiceBean implements MessageBusiness{

  public MessageBusinessBean() {
  }

  private MessageHome getMessageHome() throws java.rmi.RemoteException{
    return (MessageHome) com.idega.data.IDOLookup.getHome(Message.class);
  }

  public Message getMessage(int messageId) throws Exception {
    return getMessageHome().findByPrimaryKey(new Integer(messageId));
  }

  public Collection findMessages(int userId) throws Exception {
    return getMessageHome().findMessages(userId);
  }
}
