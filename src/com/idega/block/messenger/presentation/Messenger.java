package com.idega.block.messenger.presentation;

import com.idega.block.messenger.business.ClientSessionBinder;
import com.idega.builder.presentation.InvisibleInBuilder;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Applet;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;

/**
 * Title: Messenger
 * Description: A wrapper for the messenger applet
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author Eirikur S. Hrafnsson eiki@idega.is
 * @version 1.0
 */

public class Messenger extends Block implements InvisibleInBuilder {
  private IWBundle iwb;
  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.messenger";
  private static String SESSION_ID = "session_id";
  private static String USER_ID = "user_id";
  private static String USER_NAME = "user_name";
  private static String SERVLET_URL = "servlet_url";
  private static String SERVER_ROOT_URL = "server_root_url";
  private static String RESOURCE_URL = "resource_url";

  private String width="145";
  private String height="145";

  public Messenger() {
  }

  public void main(IWContext iwc){

    if( com.idega.core.accesscontrol.business.LoginBusinessBean.isLoggedOn(iwc) ){

      Applet messenger = new Applet();
      iwb = getBundle(iwc);
      messenger.setCodeArchive(com.idega.block.messenger.servlet.ClientServer.MESSENGER_JAR_FILE);
      messenger.setAppletClass(com.idega.block.messenger.servlet.ClientServer.MESSENGER_APPLET_CLASS);
      messenger.setCodebase(iwb.getResourcesVirtualPath()+"/");
      messenger.setParam(SERVLET_URL,com.idega.block.messenger.servlet.ClientServer.SERVLET_URL);
      messenger.setParam(SERVER_ROOT_URL,"http://"+iwc.getServerName()+":"+iwc.getServerPort());
      messenger.setParam(RESOURCE_URL,iwb.getResourcesVirtualPath()+"/");

      //user specific
      messenger.setParam(SESSION_ID,iwc.getSession().getId());
      messenger.setParam(USER_ID,Integer.toString(com.idega.core.accesscontrol.business.LoginBusinessBean.getUser(iwc).getID()));
      messenger.setParam(USER_NAME,com.idega.core.accesscontrol.business.LoginBusinessBean.getUser(iwc).getName());
      messenger.setWidth(width);
      messenger.setHeight(height);

      add(messenger);

      if( iwc.getSessionAttribute("messenger_logoff_thingy")==null ){
        iwc.setSessionAttribute("messenger_logoff_thingy",new ClientSessionBinder(iwc.getIWMainApplication()) );
      }

    }
   // else add("You are not logged on");
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void setWidth(String width){
    this.width = width;
  }

  public void setHeigth(String height){
    this.height = height;
  }

  public synchronized Object clone() {
    Messenger obj = null;
    try {
      obj = (Messenger)super.clone();

      obj.width = this.width;
      obj.height = this.height;

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }

    return obj;
  }

  }
