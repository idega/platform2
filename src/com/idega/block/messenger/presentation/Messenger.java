package com.idega.block.messenger.presentation;

import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.Applet;
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

public class Messenger extends JModuleObject {
  private static Applet messenger;
  private IWBundle iwb;
  private IWResourceBundle iwrb;
  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.messenger";
  private static String SESSION_ID = "session_id";
  private static String USER_ID = "user_id";
  private static String SERVLET_URL = "servlet_url";
  private static String SERVER_ROOT_URL = "server_root_url";


  public Messenger() {
  }

  public void main(ModuleInfo modinfo){
    //isAdmin = AccessControl.hasEditPermission(this,modinfo);
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);

    if( com.idega.block.login.business.LoginBusiness.isLoggedOn(modinfo) ){
      if(messenger==null){
        messenger = new Applet();
        messenger.setCodeArchive(com.idega.block.messenger.servlet.ClientServer.MESSENGER_JAR_FILE);
        messenger.setAppletClass(com.idega.block.messenger.servlet.ClientServer.MESSENGER_APPLET_CLASS);
        messenger.setCodebase(iwb.getResourcesVirtualPath()+"/");
        messenger.setParam(SESSION_ID,modinfo.getSession().getId());
        messenger.setParam(USER_ID,Integer.toString(com.idega.block.login.business.LoginBusiness.getUser(modinfo).getID()));
        messenger.setParam(SERVLET_URL,com.idega.block.messenger.servlet.ClientServer.SERVLET_URL);
        messenger.setParam(SERVER_ROOT_URL,modinfo.getServerName());
      }
      add(messenger);
    }
   // else add("You are not logged on");
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  }