/*
 * $Id: Manager.java,v 1.1 2001/06/06 11:29:36 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.ModuleInfo;
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
public class Manager extends JModuleObject {

  private String LightColor,MiddleColor,DarkColor;
  private String action;
  private static final String strAction = "manager_action";
  private Member eMember;
  private ModuleObject Tabs;
  private CampusObject CampObj;
  private Hashtable PermissionHash;
  private boolean isAdmin;

  public Manager(){
    MiddleColor = "#9FA9B3";
    LightColor = "#D7DADF";
    DarkColor = "#27324B";
  }

  public ModuleObject getTabs(){
    return Tabs;
  }

  private void control(ModuleInfo modinfo){

    try{
      eMember = AccessControl.getMember(modinfo);
      if(eMember !=null && getPermissionHash(modinfo,eMember.getID())){

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

  private boolean getPermissionHash(ModuleInfo modinfo,int iMemberId)throws SQLException{
    if(modinfo.getSession().getAttribute("man_perm_hash") != null){
      PermissionHash = (Hashtable) modinfo.getSession().getAttribute("man_perm_hash");
      return true;
    }
    else{
      return getMemberAccessGroups(iMemberId);
    }
    //return false;
  }

  public void main(ModuleInfo modinfo)  {
    try{
    isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo fixa Admin*/
    control(modinfo);
  }
}// class PriceCatalogueMaker


