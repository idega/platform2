package com.idega.block.mailinglist.business;

import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.mail.Folder;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.Session;
import javax.mail.NoSuchProviderException;
import javax.mail.MessagingException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  Bjarni
 * @version 1.0
 */

public class InboxManager implements HttpSessionBindingListener{

  //hvers konar session er ég með??
  static private Session session = Session.getDefaultInstance(System.getProperties(),null);
  private Store store;
  private Folder inbox;

  public InboxManager(URLName url) throws NoSuchProviderException, MessagingException{
    store = session.getStore(url);
    store.connect();
    inbox = store.getFolder("INBOX");
    inbox.open(Folder.READ_WRITE);
  }

  public Folder getInbox(){
    return inbox;
  }

  public void valueBound(HttpSessionBindingEvent event){}

  public void valueUnbound(HttpSessionBindingEvent event){
    try{
      inbox.close(true);
      store.close();
    }
    catch(MessagingException e){}
  }
}
