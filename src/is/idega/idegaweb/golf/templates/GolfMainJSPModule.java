/*

 * $Id: GolfMainJSPModule.java,v 1.2 2002/04/06 19:11:21 tryggvil Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.golf.templates;



import is.idega.idegaweb.golf.templates.page.GolfMainJSPModulePage;

import com.idega.presentation.IWContext;

import com.idega.jmodule.*;

import com.idega.presentation.PresentationObject;

import is.idega.idegaweb.golf.entity.Member;

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



  public void add(PresentationObject objectToAdd) {

    ((GolfMainJSPModulePage)getPage()).add(objectToAdd);

  }





	public Member getMember(){

		return (Member)getIWContext().getSession().getAttribute("member_login");

	}



  public boolean isAdmin() {

    try {

      return com.idega.jmodule.login.business.AccessControl.isAdmin(getIWContext());

    }

    catch(SQLException E) {

    }

    return false;

  }



  public boolean isDeveloper() {

    return com.idega.jmodule.login.business.AccessControl.isDeveloper(getIWContext());

  }



  public boolean isClubAdmin() {

    return com.idega.jmodule.login.business.AccessControl.isClubAdmin(getIWContext());

  }



  public boolean isClubWorker() {

    boolean ret;



    try {

      ret = com.idega.jmodule.login.business.AccessControl.isClubWorker(getIWContext());

    }

    catch(java.sql.SQLException e) {

      e.printStackTrace();

      ret = false;

    }



    return(ret);

  }



  public boolean isUser() {

    return com.idega.jmodule.login.business.AccessControl.isUser(getIWContext());

  }



    public void removeUnionIdSessionAttribute(IWContext iwc){

    iwc.removeSessionAttribute("golf_union_id");

  }



  public String getUnionID(IWContext iwc){

    return (String)iwc.getSessionAttribute("golf_union_id");

  }



  public void setUnionID(IWContext iwc, String union_id){

    iwc.setSessionAttribute("golf_union_id", union_id);

  }





  public IWResourceBundle getResourceBundle(){

     return getResourceBundle(getIWContext());

  }



  public IWBundle getBundle(){

    return getBundle(getIWContext());

  }



  public String getBundleIdentifier(){

    return IW_BUNDLE_IDENTIFIER;

  }











}

