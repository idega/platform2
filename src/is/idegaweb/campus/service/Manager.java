/*
 * $Id: Manager.java,v 1.3 2001/11/08 15:40:40 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;


import com.idega.presentation.*;
import com.idega.presentation.IWContext;
import com.idega.jmodule.login.business.AccessControl;
import com.idega.data.genericentity.*;
import com.idega.data.genericentity.Group;
import java.util.Hashtable;
import java.sql.SQLException;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class Manager extends Block {

  private String LightColor,MiddleColor,DarkColor;
  private String action;
  private static final String strAction = "manager_action";
  private Member eMember;
  private PresentationObject Tabs;
  private CampusObject CampObj;
  private Hashtable PermissionHash;
  private boolean isAdmin;

  public Manager(){
    MiddleColor = "#9FA9B3";
    LightColor = "#D7DADF";
    DarkColor = "#27324B";
  }

  public PresentationObject getTabs(){
    return Tabs;
  }

  private void control(IWContext iwc){

    try{
      eMember = AccessControl.getMember(iwc);
      if(eMember !=null && getPermissionHash(iwc,eMember.getID())){

        if(PermissionHash.containsValue("administrator") )
          CampObj = new Adminer();
        else if(PermissionHash.containsValue("staff") )
          CampObj = new Staffer();
        else if(PermissionHash.containsValue("tenant"))
          CampObj = new Tenanter();
        else if(PermissionHash.containsValue("applicant"))
          CampObj = new Applicant();
        else
          CampObj = new Guest();

        add(CampObj);

      }
    }
    catch(Exception S){	S.printStackTrace();	}
    }

  private boolean getMemberAccessGroups(int iMemberId)throws SQLException{
    Group[] group = (Group[])eMember.getGenericGroups();
    int iGroupLen = group.length;
    PermissionHash = new Hashtable(iGroupLen);
    for(int i = 0; i < iGroupLen ; i++){
      PermissionHash.put(new Integer(group[i].getID()),group[i].getName() );
      return true;
    }
    return false;
  }

  private boolean getPermissionHash(IWContext iwc,int iMemberId)throws SQLException{
    if(iwc.getSession().getAttribute("man_perm_hash") != null){
      PermissionHash = (Hashtable) iwc.getSession().getAttribute("man_perm_hash");
      return true;
    }
    else{
      return getMemberAccessGroups(iMemberId);
    }
    //return false;
  }

  public void main(IWContext iwc)  {
    try{
    isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(iwc);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo fixa Admin*/
    control(iwc);
  }
}// class PriceCatalogueMaker


