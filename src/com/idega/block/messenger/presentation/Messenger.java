package com.idega.block.messenger.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Applet;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;

/**
 * Title: Messenger
 * Description: A wrapper for the messenger applet
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author Eirikur S. Hrafnsson eiki@idega.is
 * @version 1.0
 */

public class Messenger extends Block {
  private Applet messenger;
  private IWBundle iwb;
  private IWResourceBundle iwrb;
  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.messenger";
  private static String SESSION_ID = "session_id";
  private static String USER_ID = "user_id";
  private static String USER_NAME = "user_name";
  private static String SERVLET_URL = "servlet_url";
  private static String SERVER_ROOT_URL = "server_root_url";
  private static String RESOURCE_URL = "resource_url";

  private String width="0";
  private String height="0";

  public Messenger() {
  }

  public void main(IWContext iwc){
    //isAdmin = AccessControl.hasEditPermission(this,iwc);
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
//test delete this
    add(iwc.getApplication().getImageFactory().createButton("RAPPSON",iwb));

    add(new com.idega.presentation.ui.SubmitButton(iwc.getApplication().getImageFactory().createButton("RAPPSON",iwb)));

    if( com.idega.block.login.business.LoginBusiness.isLoggedOn(iwc) ){
      if(messenger==null){
        messenger = new Applet();
        messenger.setCodeArchive(com.idega.block.messenger.servlet.ClientServer.MESSENGER_JAR_FILE);
        messenger.setAppletClass(com.idega.block.messenger.servlet.ClientServer.MESSENGER_APPLET_CLASS);
        messenger.setCodebase(iwb.getResourcesVirtualPath()+"/");
        messenger.setParam(SERVLET_URL,com.idega.block.messenger.servlet.ClientServer.SERVLET_URL);
        messenger.setParam(SERVER_ROOT_URL,"http://"+iwc.getServerName());
        messenger.setParam(RESOURCE_URL,iwb.getResourcesVirtualPath()+"/");
      }

      Applet myMessenger = (Applet) messenger.clone();

      myMessenger.setParam(SESSION_ID,iwc.getSession().getId());
      myMessenger.setParam(USER_ID,Integer.toString(com.idega.block.login.business.LoginBusiness.getUser(iwc).getID()));
      myMessenger.setParam(USER_NAME,com.idega.block.login.business.LoginBusiness.getUser(iwc).getName());
      myMessenger.setWidth(width);
      myMessenger.setHeight(height);

      add(myMessenger);
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

  }