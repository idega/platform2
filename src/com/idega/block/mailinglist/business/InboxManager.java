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

  //hvers konar session er �g me�??
  static private Session session = Session.getDefaultInstance(System.getProperties(),null);
  private Store store;
  private Folder inbox;

  public InboxManager(URLName url) throws NoSuchProviderException, MessagingException{
    this.store = session.getStore(url);
    this.store.connect();
    this.inbox = this.store.getFolder("INBOX");
    this.inbox.open(Folder.READ_WRITE);
  }

  public Folder getInbox(){
    return this.inbox;
  }

  public void valueBound(HttpSessionBindingEvent event){}

  public void valueUnbound(HttpSessionBindingEvent event){
    try{
      this.inbox.close(true);
      this.store.close();
    }
    catch(MessagingException e){}
  }
}
