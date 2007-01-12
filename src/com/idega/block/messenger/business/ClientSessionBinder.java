package com.idega.block.messenger.business;

import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSession;
import com.idega.idegaweb.IWMainApplication;

/**
 * Title: com.idega.block.messenger.business.ClientSessionBinder
 * Description: this class is used to log out a client when the <br>
 * session times out. Registered to the session with <br>
 * iwc.setSessionAttribute("some_name",new ClientSessionBinder());<br>
 * When the session starts valueBound is called.<br>
 * When the session runs out valueUnbound is called.
 * Copyright:    Copyright (c) 2002
 * Company:      Idega Software
 * @author <a href="eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */

public class ClientSessionBinder implements HttpSessionBindingListener {
  private IWMainApplication app;

  public ClientSessionBinder(IWMainApplication app) {
    this.app = app;
  }

  public void valueBound(HttpSessionBindingEvent e) {
    //e.getSession().setMaxInactiveInterval(30);
    //HttpSession session = e.getSession();
  }

  /**
   * Logs out the client
   */
  public void valueUnbound(HttpSessionBindingEvent e) {
    HttpSession session = e.getSession();
    ClientManager cManager = (ClientManager) this.app.getAttribute("ClientManager");
    if( cManager!=null ){
      System.out.println("XXXXXXXXXXXXXXXXXXXXXXX");
      System.out.println("ClientSessionBinder : logging off client :"+ClientManager.getClientName(session.getId()));
      System.out.println("XXXXXXXXXXXXXXXXXXXXXXX");
      cManager.clientCheckOut(session.getId());
    }
  }
}
