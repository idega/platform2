package com.idega.projects.campus.service;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.login.business.AccessControl;
import com.idega.data.genericentity.Member;
import com.idega.data.genericentity.Group;
import java.util.Hashtable;
import java.sql.SQLException;
import java.io.IOException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class Tabber extends JModuleObject {

  private String LightColor,MiddleColor,DarkColor;
  private String action;
  public static final String strAction = "manager_action";
  private Member eMember;
  private ModuleObject Tabs;
  private CampusObject CampObj;
  private Hashtable PermissionHash;
  private boolean isAdmin;
  private String sAct;
  private int iAct;
  private final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4;
  private final int ACT5 = 5, ACT6 = 6, ACT7 = 7, ACT8 = 8;
  private final int NOACT = 0;


  public Tabber(){
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

      if(modinfo.getParameter(strAction) == null){
        iAct = NOACT;
      }
      else {
        sAct = modinfo.getParameter(strAction);
        iAct = Integer.parseInt(sAct);
      }
      if(eMember !=null && getPermissionHash(modinfo,eMember.getID())){

        if(PermissionHash.containsValue("administrator") )
          Tabs = AdminTabs();
        else if(PermissionHash.containsValue("staff") )
          Tabs = StaffTabs();
        else if(PermissionHash.containsValue("tenant"))
          Tabs = TenantTabs();
        else if(PermissionHash.containsValue("applicant"))
          Tabs = ApplicantTabs();
        else return;

        add(Tabs);

      }
    }
    catch(Exception S){	S.printStackTrace();	}
    }

    private void setDim(Image image,boolean high){
      if(high){
        image.setHeight(21);
        image.setWidth(77);
      }
      else{
        image.setHeight(16);
        image.setWidth(76);
      }
    }

   private Table AdminTabs(){
      Table LinkTable = new Table(1,1);
      LinkTable.setBorder(0);
      LinkTable.setCellpadding(0);
      LinkTable.setCellspacing(0);
      //LinkTable.setWidth("100%");
      LinkTable.setAlignment("right");

      String lang = "IS";

      Link Link1 = new Link(new Image(iAct == ACT1?"/pics/tabs/"+lang+"/financial.gif":"/pics/tabs/"+lang+"/financial1.gif"),"/manager.jsp");
      Link1.addParameter(strAction,ACT1);
      Link1.addParameter(Action.sAdminAction,ACT1);
      Link Link2 = new Link(new Image(iAct == ACT2?"/pics/tabs/"+lang+"/residents.gif":"/pics/tabs/"+lang+"/residents1.gif"),"/manager.jsp");
      Link2.addParameter(strAction,ACT2);
      Link2.addParameter(Action.sAdminAction,ACT2);
      Link Link3 = new Link(new Image(iAct == ACT3?"/pics/tabs/"+lang+"/allocation.gif":"/pics/tabs/"+lang+"/allocation1.gif"),"/manager.jsp");
      Link3.addParameter(strAction,ACT3);
      Link3.addParameter(Action.sAdminAction,ACT3);
      Link Link4 = new Link(new Image(iAct == ACT4?"/pics/tabs/"+lang+"/apartment.gif":"/pics/tabs/"+lang+"/apartment1.gif"),"/manager.jsp");
      Link4.addParameter(strAction,ACT4);
      Link4.addParameter(Action.sAdminAction,ACT4);
      Link Link5 = new Link(new Image(iAct == ACT5?"/pics/tabs/"+lang+"/allocation.gif":"/pics/tabs/"+lang+"/allocation1.gif"),"/manager.jsp");
      Link5.addParameter(strAction,ACT5);
      Link5.addParameter(Action.sAdminAction,ACT5);
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,1,1);
      LinkTable.add(Link3,1,1);
      LinkTable.add(Link4,1,1);
      LinkTable.add(Link5,1,1);

      return LinkTable;
  }

  private Table StaffTabs(){
      Table LinkTable = new Table(1,1);
      LinkTable.setBorder(0);
      LinkTable.setCellpadding(0);
      LinkTable.setCellspacing(0);
      //LinkTable.setWidth("100%");
      LinkTable.setAlignment("right");

      String lang = "IS";

      Link Link1 = new Link(new Image(iAct == ACT1?"/pics/tabs/"+lang+"/threads.gif":"/pics/tabs/"+lang+"/threads1.gif"),"/index2.jsp");
      Link1.addParameter(strAction,ACT1);
      Link1.addParameter(Action.sStaffAction,ACT1);
      Link Link2 = new Link(new Image(iAct == ACT2?"/pics/tabs/"+lang+"/apartment.gif":"/pics/tabs/"+lang+"/apartment1.gif"),"/index2.jsp");
      Link2.addParameter(strAction,ACT2);
      Link2.addParameter(Action.sStaffAction,ACT2);
      Link Link3 = new Link(new Image(iAct == ACT3?"/pics/tabs/"+lang+"/allocation.gif":"/pics/tabs/"+lang+"/allocation1.gif"),"/index2.jsp");
      Link3.addParameter(strAction,ACT3);
      Link3.addParameter(Action.sStaffAction,ACT3);
      Link Link4 = new Link(new Image(iAct == ACT4?"/pics/tabs/"+lang+"/financial.gif":"/pics/tabs/"+lang+"/financial1.gif"),"/index2.jsp");
      Link4.addParameter(strAction,ACT4);
      Link4.addParameter(Action.sStaffAction,ACT4);
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,1,1);
      LinkTable.add(Link3,1,1);
      LinkTable.add(Link4,1,1);

      return LinkTable;
  }

  private Table TenantTabs(){
      Table LinkTable = new Table(1,1);
      LinkTable.setBorder(0);
      LinkTable.setCellpadding(0);
      LinkTable.setCellspacing(0);
      //LinkTable.setWidth("100%");
      LinkTable.setAlignment("right");

      String lang = "IS";

      Link Link1 = new Link(new Image(iAct == ACT1?"/pics/tabs/"+lang+"/apartment.gif":"/pics/tabs/"+lang+"/apartment1.gif"),"/index2.jsp");
      Link1.addParameter(strAction,ACT1);
      Link1.addParameter(Action.sTenantAction,ACT1);
      Link Link2 = new Link(new Image(iAct == ACT2?"/pics/tabs/"+lang+"/financial.gif":"/pics/tabs/"+lang+"/financial1.gif"),"/index2.jsp");
      Link2.addParameter(strAction,ACT2);
      Link2.addParameter(Action.sTenantAction,ACT2);
      Link Link3 = new Link(new Image(iAct == ACT3?"/pics/tabs/"+lang+"/residents.gif":"/pics/tabs/"+lang+"/residents1.gif"),"/index2.jsp");
      Link3.addParameter(strAction,ACT3);
      Link3.addParameter(Action.sTenantAction,ACT3);
      Link Link4 = new Link(new Image(iAct == ACT4?"/pics/tabs/"+lang+"/announce.gif":"/pics/tabs/"+lang+"/announce1.gif"),"/index2.jsp");
      Link4.addParameter(strAction,ACT4);
      Link4.addParameter(Action.sTenantAction,ACT4);

      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,1,1);
      LinkTable.add(Link3,1,1);
      LinkTable.add(Link4,1,1);


      return LinkTable;
  }

  private Table ApplicantTabs(){
      Table LinkTable = new Table(1,1);
      LinkTable.setBorder(0);
      LinkTable.setCellpadding(0);
      LinkTable.setCellspacing(0);
      //LinkTable.setWidth("100%");
      LinkTable.setAlignment("right");

      String lang = "IS";

      Link Link1 = new Link(new Image(iAct == ACT1?"/pics/tabs/"+lang+"/waitinglist.gif":"/pics/tabs/"+lang+"/waitinglist1.gif"),"/index2.jsp");
      Link1.addParameter(strAction,ACT1);
      Link1.addParameter(Action.sApplicantAction,ACT1);
      Link Link2 = new Link(new Image(iAct == ACT2?"/pics/tabs/"+lang+"/apply.gif":"/pics/tabs/"+lang+"/apply1.gif"),"/index2.jsp");
      Link2.addParameter(strAction,ACT2);
      Link2.addParameter(Action.sApplicantAction,ACT2);
      /*Link Link3 = new Link(new Image(iAct == ACT3?"/pics/tabs/"+lang+"/allocation.gif":"/pics/tabs/"+lang+"/allocation1.gif"));
      Link3.addParameter(strAction,ACT3);
      Link Link4 = new Link(new Image(iAct == ACT4?"/pics/tabs/"+lang+"/financial.gif":"/pics/tabs/"+lang+"/financial1.gif"));
      Link4.addParameter(strAction,ACT4);
      */
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,1,1);


      return LinkTable;
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
    if(modinfo.getParameter("man_perm_hash") != null){
      PermissionHash = (Hashtable) modinfo.getApplicationAttribute("man_perm_hash");
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


