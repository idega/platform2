/*
 * $Id: Tabber.java,v 1.8 2001/07/13 16:49:05 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;

import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.core.user.data.User;
import com.idega.core.data.GenericGroup;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.login.business.LoginBusiness;
import java.util.Hashtable;
import java.sql.SQLException;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class Tabber extends JModuleObject {

  private String LightColor,MiddleColor,DarkColor;
  private String action;
  private String strAction = TabAction.sAction;
  private User eUser;
  private ModuleObject Tabs;
  private CampusObject CampObj;
  private Hashtable PermissionHash;
  private boolean isAdmin;
  private String sAct;
  private int iAct;
  private final int ACT20 = 20, ACT21 = 21, ACT22 = 22, ACT23 = 23, ACT24 = 24,ACT25 = 25;
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
      eUser = LoginBusiness.getUser(modinfo);

     if(modinfo.getParameter(strAction) == null){
        if ( modinfo.getServletContext().getAttribute(strAction) != null ) {
          sAct = (String) modinfo.getServletContext().getAttribute(strAction);
          iAct = Integer.parseInt(sAct);
        }
        else {
          iAct = NOACT;
        }
      }
    if(modinfo.getParameter(strAction) != null){
        sAct = modinfo.getParameter(strAction);
        iAct = Integer.parseInt(sAct);
        if ( ((String) modinfo.getServletContext().getAttribute(strAction)) != (sAct) ) {
          modinfo.getServletContext().setAttribute(strAction,sAct);
        }
      }
      if(eUser !=null && getPermissionHash(modinfo,eUser.getID())){

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

      Link Link1 = new Link(new Image(iAct == ACT20?"/pics/tabs/"+lang+"/financial.gif":"/pics/tabs/"+lang+"/financial1.gif"),"/finance/index.jsp");
      Link1.addParameter(strAction,ACT20);

      Link Link2 = new Link(new Image(iAct == ACT21?"/pics/tabs/"+lang+"/residents.gif":"/pics/tabs/"+lang+"/residents1.gif"),"/main/manager.jsp");
      Link2.addParameter(strAction,ACT21);

      Link Link3 = new Link(new Image(iAct == ACT22?"/pics/tabs/"+lang+"/allocation.gif":"/pics/tabs/"+lang+"/allocation1.gif"),"/allocation/index.jsp");
      Link3.addParameter(strAction,ACT22);

      Link Link4 = new Link(new Image(iAct == ACT23?"/pics/tabs/"+lang+"/apartment.gif":"/pics/tabs/"+lang+"/apartment1.gif"),"/building/index.jsp");
      Link4.addParameter(strAction,ACT23);

      Link Link5 = new Link(new Image(iAct == ACT24?"/pics/tabs/"+lang+"/announce.gif":"/pics/tabs/"+lang+"/announce1.gif"),"/main/announcements.jsp");
      Link5.addParameter(strAction,ACT24);

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
      LinkTable.setAlignment("right");

      String lang = "IS";

      Link Link1 = new Link(new Image(iAct == ACT20?"/pics/tabs/"+lang+"/financial.gif":"/pics/tabs/"+lang+"/financial1.gif"),"/finance/index.jsp");
      Link Link2 = new Link(new Image(iAct == ACT21?"/pics/tabs/"+lang+"/residents.gif":"/pics/tabs/"+lang+"/residents1.gif"),"/main/manager.jsp");
      Link Link3 = new Link(new Image(iAct == ACT22?"/pics/tabs/"+lang+"/allocation.gif":"/pics/tabs/"+lang+"/allocation1.gif"),"/allocation/index.jsp");
      Link Link4 = new Link(new Image(iAct == ACT23?"/pics/tabs/"+lang+"/apartment.gif":"/pics/tabs/"+lang+"/apartment1.gif"),"/building/index.jsp");

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

      Link Link1 = new Link(new Image(iAct == ACT20?"/pics/tabs/"+lang+"/apartment.gif":"/pics/tabs/"+lang+"/apartment1.gif"),"/index2.jsp");
      Link1.addParameter(strAction,ACT20);

      Link Link2 = new Link(new Image(iAct == ACT21?"/pics/tabs/"+lang+"/financial.gif":"/pics/tabs/"+lang+"/financial1.gif"),"/index2.jsp");
      Link2.addParameter(strAction,ACT21);

      Link Link3 = new Link(new Image(iAct == ACT22?"/pics/tabs/"+lang+"/residents.gif":"/pics/tabs/"+lang+"/residents1.gif"),"/index2.jsp");
      Link3.addParameter(strAction,ACT22);

      Link Link4 = new Link(new Image(iAct == ACT23?"/pics/tabs/"+lang+"/announce.gif":"/pics/tabs/"+lang+"/announce1.gif"),"/main/announcements.jsp");
      Link4.addParameter(strAction,ACT23);


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

      Link Link1 = new Link(new Image(iAct == ACT20?"/pics/tabs/"+lang+"/waitinglist.gif":"/pics/tabs/"+lang+"/waitinglist1.gif"),"/index2.jsp");
      Link1.addParameter(strAction,ACT20);

      Link Link2 = new Link(new Image(iAct == ACT21?"/pics/tabs/"+lang+"/apply.gif":"/pics/tabs/"+lang+"/apply1.gif"),"/index2.jsp");
      Link2.addParameter(strAction,ACT21);

      /*Link Link3 = new Link(new Image(iAct == ACT3?"/pics/tabs/"+lang+"/allocation.gif":"/pics/tabs/"+lang+"/allocation1.gif"));
      Link3.addParameter(strAction,ACT3);
      Link Link4 = new Link(new Image(iAct == ACT4?"/pics/tabs/"+lang+"/financial.gif":"/pics/tabs/"+lang+"/financial1.gif"));
      Link4.addParameter(strAction,ACT4);
      */
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,1,1);


      return LinkTable;
  }



  private boolean getUserAccessGroups(int iUserId)throws SQLException{
    GenericGroup[] group = (GenericGroup[])eUser.getGenericGroups();
    int iGroupLen = group.length;
    PermissionHash = new Hashtable(iGroupLen);
    for(int i = 0; i < iGroupLen ; i++){
      PermissionHash.put(new Integer(group[i].getID()),group[i].getName() );
      return true;
    }
    return false;
  }

  private boolean getPermissionHash(ModuleInfo modinfo,int iUserId)throws SQLException{
    if(modinfo.getParameter("man_perm_hash") != null){
      PermissionHash = (Hashtable) modinfo.getSession().getAttribute("man_perm_hash");
      return true;
    }
    else{
      boolean returner = getUserAccessGroups(iUserId);
      modinfo.getSession().setAttribute("man_perm_hash",PermissionHash);
      return returner;
    }
    //return false;
  }

  public void main(ModuleInfo modinfo)  {
    try{
      isAdmin = com.idega.core.accesscontrol.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo fixa Admin*/
    control(modinfo);
  }
}// class PriceCatalogueMaker


