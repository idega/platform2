/*
 * $Id: GolfMainJSPModule.java,v 1.6 2004/04/01 20:11:21 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.golf.templates;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.templates.page.GolfMainJSPModulePage;

import java.sql.SQLException;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;

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

  public void add(PresentationObject objectToAdd) {
    ((GolfMainJSPModulePage)getPage()).add(objectToAdd);
  }


	public Member getMember(){
		return (Member)getModuleInfo().getSession().getAttribute("member_login");
	}

  public boolean isAdmin() {
    try {
      return AccessControl.isAdmin(getModuleInfo());
    }
    catch(SQLException E) {
    }
    return false;
  }

  public boolean isDeveloper() {
    return AccessControl.isDeveloper(getModuleInfo());
  }

  public boolean isClubAdmin() {
    return AccessControl.isClubAdmin(getModuleInfo());
  }

  public boolean isClubWorker() {
    boolean ret;

    try {
      ret = AccessControl.isClubWorker(getModuleInfo());
    }
    catch(java.sql.SQLException e) {
      e.printStackTrace();
      ret = false;
    }

    return(ret);
  }

  public boolean isUser() {
    return AccessControl.isUser(getModuleInfo());
  }

    public void removeUnionIdSessionAttribute(IWContext modinfo){
    modinfo.removeSessionAttribute("golf_union_id");
  }

  public String getUnionID(IWContext modinfo){
    return (String)modinfo.getSessionAttribute("golf_union_id");
  }

  public void setUnionID(IWContext modinfo, String union_id){
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
