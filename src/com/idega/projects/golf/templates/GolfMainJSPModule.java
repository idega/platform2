/*
 * $Id: GolfMainJSPModule.java,v 1.17 2001/05/24 23:15:55 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.projects.golf.templates;

import com.idega.projects.golf.templates.page.GolfMainJSPModulePage;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.*;
import com.idega.jmodule.object.ModuleObject;
import com.idega.projects.golf.entity.Member;
import java.sql.SQLException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

/**
 * @author
 */
public class GolfMainJSPModule extends MainSideJSPModule {

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";

  public void initializePage() {
    this.setPage(new GolfMainJSPModulePage());
  }


  public void setVerticalAlignment(String alignment) {
    ((GolfMainJSPModulePage)getPage()).setVerticalAlignment(alignment);
  }

  public void add(ModuleObject objectToAdd) {
    ((GolfMainJSPModulePage)getPage()).add(objectToAdd);
  }


	public Member getMember(){
		return (Member)getModuleInfo().getSession().getAttribute("member_login");
	}

  public boolean isAdmin() {
    try {
      return com.idega.jmodule.login.business.AccessControl.isAdmin(getModuleInfo());
    }
    catch(SQLException E) {
    }
    return false;
  }

  public boolean isDeveloper() {
    return com.idega.jmodule.login.business.AccessControl.isDeveloper(getModuleInfo());
  }

  public boolean isClubAdmin() {
    return com.idega.jmodule.login.business.AccessControl.isClubAdmin(getModuleInfo());
  }

  public boolean isClubWorker() {
    boolean ret;

    try {
      ret = com.idega.jmodule.login.business.AccessControl.isClubWorker(getModuleInfo());
    }
    catch(java.sql.SQLException e) {
      e.printStackTrace();
      ret = false;
    }

    return(ret);
  }

  public boolean isUser() {
    return com.idega.jmodule.login.business.AccessControl.isUser(getModuleInfo());
  }

    public void removeUnionIdSessionAttribute(ModuleInfo modinfo){
    modinfo.removeSessionAttribute("golf_union_id");
  }

  public String getUnionID(ModuleInfo modinfo){
    return (String)modinfo.getSessionAttribute("golf_union_id");
  }

  public void setUnionID(ModuleInfo modinfo, String union_id){
    modinfo.setSessionAttribute("golf_union_id", union_id);
  }


  public IWResourceBundle getResourceBundle(){
     return getResourceBundle(getModuleInfo());
  }

  public IWBundle getBundle(){
    return getBundle(getModuleInfo());
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }





}
