/*

 * $Id: CampusTabber.java,v 1.7 2004/07/12 11:52:23 aron Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.campus.presentation;



import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Parameter;
import com.idega.user.data.Group;





/**

 * Title:   idegaclasses

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author  <a href="mailto:aron@idega.is">aron@idega.is

 * @version 1.0

 */



public class CampusTabber extends Block implements Campus {



  private String action;

  private String strAction = TabAction.sAction;

  private User eUser;

  private PresentationObject Tabs;

  private Hashtable PermissionHash;

  private boolean isAdmin;

  private String sAct;

  private int iAct;

  private final int ACT20 = 20, ACT21 = 21, ACT22 = 22, ACT23 = 23, ACT24 = 24,ACT25 = 25;

  private final int ACT5 = 5, ACT6 = 6, ACT7 = 7, ACT8 = 8;

  private final int NOACT = 0;

  protected IWResourceBundle iwrb;

  protected IWBundle iwb;

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";





  public CampusTabber(){



  }



  public String getBundleIdentifier(){

    return IW_BUNDLE_IDENTIFIER;

  }



  public PresentationObject getTabs(){

    return Tabs;

  }



  private void control(IWContext iwc){



    try{

      eUser = LoginBusinessBean.getUser(iwc);



     if(iwc.getParameter(strAction) == null){

        if ( iwc.getSessionAttribute(strAction) != null ) {

          sAct = (String) iwc.getSessionAttribute(strAction);

          iAct = Integer.parseInt(sAct);

        }

        else {

          iAct = NOACT;

        }

      }

    if(iwc.getParameter(strAction) != null){

        sAct = iwc.getParameter(strAction);

        iAct = Integer.parseInt(sAct);

        if ( ((String) iwc.getSessionAttribute(strAction)) != (sAct) ) {

          iwc.setSessionAttribute(strAction,sAct);

        }

      }



      if(eUser !=null && getPermissionHash(iwc)){



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

      LinkTable.setHorizontalAlignment(Table.HORIZONTAL_ALIGN_RIGHT);









      Image finance = iwrb.getImage(iAct == ACT20?"/tabs/finance.gif":"/tabs/finance1.gif");

      Link Link1 = new Link(finance);

      Link1.addParameter(getParameter(ADM_FINANCE));

      Link1.addParameter(strAction,ACT20);



      Image habitants = iwrb.getImage(iAct == ACT21?"/tabs/habitants.gif":"/tabs/habitants1.gif");

      Link Link2 = new Link(habitants);

      Link2.addParameter(getParameter(ADM_HABITANTS));

      Link2.addParameter(strAction,ACT21);



      Image allocation = iwrb.getImage(iAct == ACT22?"/tabs/allocate.gif":"/tabs/allocate1.gif");

      Link Link3 = new Link(allocation);

      Link3.addParameter(getParameter(ADM_ALLOCATION));

      Link3.addParameter(strAction,ACT22);



      Image apartments = iwrb.getImage(iAct == ACT23?"/tabs/apartments.gif":"/tabs/apartments1.gif");

      Link Link4 = new Link(apartments);

      Link4.addParameter(getParameter(ADM_APARTMENTS));

      Link4.addParameter(strAction,ACT23);



      Image announce = iwrb.getImage(iAct == ACT24?"/tabs/announcements.gif":"/tabs/announcements1.gif");

      Link Link5 = new Link(announce );

      Link5.addParameter(getParameter(ADM_ANNOUNCE));

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

      LinkTable.setHorizontalAlignment(Table.HORIZONTAL_ALIGN_RIGHT);



      Link Link1 = new Link(iwrb.getImage(iAct == ACT20?"/tabs/finance.gif":"/tabs/finance1.gif"));

      Link Link2 = new Link(iwrb.getImage(iAct == ACT21?"/tabs/habitants.gif":"/tabs/habitants.gif"));

      Link Link3 = new Link(iwrb.getImage(iAct == ACT22?"/tabs/allocate.gif":"/tabs/allocate.gif"));

      Link Link4 = new Link(iwrb.getImage(iAct == ACT23?"/tabs/apartments.gif":"/tabs/apartments1.gif"));



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

      LinkTable.setHorizontalAlignment(Table.HORIZONTAL_ALIGN_RIGHT);



      Image profile = iwrb.getImage(iAct == ACT20?"/tabs/my_profile.gif":"/tabs/my_profile1.gif");

      Link Link1 =  new Link(profile);

      Link1.addParameter(getParameter(TEN_PROFILE));

      Link1.addParameter(strAction,ACT20);



      Image finance = iwrb.getImage(iAct == ACT21?"/tabs/finance.gif":"/tabs/finance1.gif");

      Link Link2 = new Link(finance);

      Link2.addParameter(getParameter(TEN_FINANCE));

      Link2.addParameter(strAction,ACT21);



      Image habitants = iwrb.getImage(iAct == ACT22?"/tabs/habitants.gif":"/tabs/habitants1.gif");

      Link Link3 = new Link(habitants);

      Link3.addParameter(getParameter(TEN_HABITANTS));

      Link3.addParameter(strAction,ACT22);



      Image announce = iwrb.getImage(iAct == ACT23?"/tabs/announcements.gif":"/tabs/announcements1.gif");

      Link Link4 = new Link(announce );

      Link4.addParameter(getParameter(TEN_ANNOUNCE));

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

      LinkTable.setHorizontalAlignment(Table.HORIZONTAL_ALIGN_RIGHT);



      Link Link1 = new Link(iwrb.getImage(iAct == ACT20?"/tabs/waitinglist.gif":"/tabs/waitinglist1.gif"));

      Link1.addParameter(strAction,ACT20);



      Link Link2 = new Link(iwrb.getImage(iAct == ACT21?"/tabs/apply.gif":"/tabs/apply1.gif"));

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



  private boolean getUserAccessGroups(IWContext iwc)throws SQLException{

    List group = com.idega.core.accesscontrol.business.LoginBusinessBean.getPermissionGroups(iwc);

    PermissionHash = new Hashtable();

    //System.err.println("getUserAccessGroups in Tabber");

    if(group != null){

      Iterator iter = group.iterator();

      while (iter.hasNext()) {

        Group item = (Group)iter.next();

        //System.err.println(item.getName());

        PermissionHash.put((Integer)item.getPrimaryKey(),item.getName() );

        return true;

      }

    }

    return false;

  }



  private boolean getPermissionHash(IWContext iwc)throws SQLException{

    if(iwc.getParameter("man_perm_hash") != null){

      PermissionHash = (Hashtable) iwc.getSession().getAttribute("man_perm_hash");

      return true;

    }

    else{

      boolean returner = getUserAccessGroups(iwc);

      iwc.getSession().setAttribute("man_perm_hash",PermissionHash);

      return returner;

    }

    //return false;

  }



  public static Parameter getParameter(int contentView){

    return CampusFactory.getParameter((contentView));

  }



  public void main(IWContext iwc)  {

    iwrb = getResourceBundle(iwc);

    iwb = getBundle(iwc);

    isAdmin = iwc.hasEditPermission(this);

    /** @todo fixa Admin*/

    control(iwc);

  }

}// class PriceCatalogueMaker





