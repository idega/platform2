/*
 * $Id: Tabber.java,v 1.20 2001/10/01 13:07:28 aron Exp $
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
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.login.business.LoginBusiness;
import java.util.Hashtable;
import java.sql.SQLException;
import java.io.IOException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import is.idegaweb.campus.allocation.CampusAllocation;
import is.idegaweb.campus.tariffs.CampusFinance;
import is.idegaweb.campus.templates.CampusPage;
import com.idega.idegaweb.IWMainApplication;
import java.util.List;
import java.util.Iterator;

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
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus";


  public Tabber(){
    MiddleColor = "#9FA9B3";
    LightColor = "#D7DADF";
    DarkColor = "#27324B";
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public ModuleObject getTabs(){
    return Tabs;
  }

  private void control(ModuleInfo modinfo){

    try{
      eUser = LoginBusiness.getUser(modinfo);

     if(modinfo.getParameter(strAction) == null){
        if ( modinfo.getSessionAttribute(strAction) != null ) {
          sAct = (String) modinfo.getSessionAttribute(strAction);
          iAct = Integer.parseInt(sAct);
        }
        else {
          iAct = NOACT;
        }
      }
    if(modinfo.getParameter(strAction) != null){
        sAct = modinfo.getParameter(strAction);
        iAct = Integer.parseInt(sAct);
        if ( ((String) modinfo.getSessionAttribute(strAction)) != (sAct) ) {
          modinfo.setSessionAttribute(strAction,sAct);
        }
      }
      if(eUser !=null && getPermissionHash(modinfo)){

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

      Link Link1 = new Link(iwrb.getImage(iAct == ACT20?"/tabs/finance.gif":"/tabs/finance1.gif"),"/finance/index.jsp");
      Link1.addParameter(strAction,ACT20);

      Link Link2 = new Link(iwrb.getImage(iAct == ACT21?"/tabs/habitants.gif":"/tabs/habitants1.gif"),"/tenant/habitants.jsp");
      Link2.addParameter(strAction,ACT21);

      Link Link3 = new Link(iwrb.getImage(iAct == ACT22?"/tabs/allocate.gif":"/tabs/allocate1.gif"),"/allocation/index.jsp");
      Link3.addParameter(strAction,ACT22);

      Link Link4 = new Link(iwrb.getImage(iAct == ACT23?"/tabs/apartments.gif":"/tabs/apartments1.gif"),"/building/index.jsp");
      Link4.addParameter(strAction,ACT23);

      Link Link5 = new Link(iwrb.getImage(iAct == ACT24?"/tabs/announcements.gif":"/tabs/announcements1.gif"),"/main/announcements.jsp");
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


      Link Link1 = new Link(iwrb.getImage(iAct == ACT20?"/tabs/finance.gif":"/tabs/finance1.gif"),"/finance/index.jsp");
      Link Link2 = new Link(iwrb.getImage(iAct == ACT21?"/tabs/habitants.gif":"/tabs/habitants.gif"),"/main/manager.jsp");
      Link Link3 = new Link(iwrb.getImage(iAct == ACT22?"/tabs/allocate.gif":"/tabs/allocate.gif"),"/allocation/index.jsp");
      Link Link4 = new Link(iwrb.getImage(iAct == ACT23?"/tabs/apartments.gif":"/tabs/apartments1.gif"),"/building/index.jsp");

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

      Link Link1 = new Link(iwrb.getImage(iAct == ACT20?"/tabs/my_profile.gif":"/tabs/my_profile1.gif"),"/tenant/index.jsp");
      Link1.addParameter(strAction,ACT20);

      Link Link2 = new Link(iwrb.getImage(iAct == ACT21?"/tabs/finance.gif":"/tabs/finance1.gif"),"/tenant/accountview.jsp");
      Link2.addParameter(strAction,ACT21);

      Link Link3 = new Link(iwrb.getImage(iAct == ACT22?"/tabs/habitants.gif":"/tabs/habitants1.gif"),"/index2.jsp");
      Link3.addParameter(strAction,ACT22);

      Link Link4 = new Link(iwrb.getImage(iAct == ACT23?"/tabs/announcements.gif":"/tabs/announcements1.gif"),"/main/announcements.jsp");
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

      Link Link1 = new Link(iwrb.getImage(iAct == ACT20?"/tabs/waitinglist.gif":"/tabs/waitinglist1.gif"),"/index2.jsp");
      Link1.addParameter(strAction,ACT20);

      Link Link2 = new Link(iwrb.getImage(iAct == ACT21?"/tabs/apply.gif":"/tabs/apply1.gif"),"/index2.jsp");
      Link2.addParameter(strAction,ACT21);

      /*Link Link3 = new Link(iwrb.getImage(iAct == ACT3?"/tabs/allocation.gif":"/tabs/allocation1.gif"));
      Link3.addParameter(strAction,ACT3);
      Link Link4 = new Link(iwrb.getImage(iAct == ACT4?"/tabs/financial.gif":"/tabs/financial1.gif"));
      Link4.addParameter(strAction,ACT4);
      */
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,1,1);


      return LinkTable;
  }

  private boolean getUserAccessGroups(ModuleInfo modinfo)throws SQLException{
    List group = com.idega.block.login.business.LoginBusiness.getPermissionGroups(modinfo);
    PermissionHash = new Hashtable();
    System.err.println("getUserAccessGroups in Tabber");
    if(group != null){
      Iterator iter = group.iterator();
      while (iter.hasNext()) {
        com.idega.core.data.GenericGroup item = (com.idega.core.data.GenericGroup)iter.next();
        System.err.println(item.getName());
        PermissionHash.put(new Integer(item.getID()),item.getName() );
        return true;
      }
    }
    return false;
  }

  private boolean getPermissionHash(ModuleInfo modinfo)throws SQLException{
    if(modinfo.getParameter("man_perm_hash") != null){
      PermissionHash = (Hashtable) modinfo.getSession().getAttribute("man_perm_hash");
      return true;
    }
    else{
      boolean returner = getUserAccessGroups(modinfo);
      modinfo.getSession().setAttribute("man_perm_hash",PermissionHash);
      return returner;
    }
    //return false;
  }

  public void main(ModuleInfo modinfo)  {
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    try{
      isAdmin = com.idega.core.accesscontrol.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo fixa Admin*/
    control(modinfo);
  }
}// class PriceCatalogueMaker


