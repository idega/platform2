package com.idega.projects.campus.service;

import com.idega.data.genericentity.Group;
import com.idega.data.genericentity.Member;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.login.business.*;
import com.idega.block.finance.presentation.Finance;
import com.idega.projects.campus.admin.BuildingMaker;
import java.sql.SQLException;
import java.io.IOException;
import com.idega.projects.campus.tariffs.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class Adminer extends CampusObject implements Tabs{

  private String iObjectName = "Admin";
  private String LightColor,MiddleColor,DarkColor;
  private int iAct;
  private String sAct;
  public static final String strAction = "admin_action";
  private Member eMember;
  private boolean isAdmin;
  public final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4,ACT5 = 5;

  public Adminer(){
    MiddleColor = "#9FA9B3";
    LightColor = "#D7DADF";
    DarkColor = "#27324B";
  }

  private void control(ModuleInfo modinfo){

    try{
      eMember = AccessControl.getMember(modinfo);

      if(modinfo.getParameter(strAction) == null){
        doAct1(modinfo);
      }
      if(modinfo.getParameter(strAction) != null){
        sAct = modinfo.getParameter(strAction);
        iAct = Integer.parseInt(sAct);

        switch (iAct) {
          case ACT1 : doAct1(modinfo);        break;
          case ACT2 : doAct2(modinfo);        break;
          case ACT3 : doAct3(modinfo);        break;
          case ACT4 : doAct4(modinfo);        break;
          case ACT5 : doAct5(modinfo);        break;
          default : doAct1(modinfo);
        }
      }
    }
    catch(SQLException S){	S.printStackTrace();	}
    catch(Exception s){s.printStackTrace();}
}

    private void doAct1(ModuleInfo modinfo) throws SQLException {
      add(new CampusFinance("Fjármál"));
    }
    private void doAct2(ModuleInfo modinfo) throws SQLException{
    }
    private void doAct3(ModuleInfo modinfo) throws SQLException{
    }
    private void doAct4(ModuleInfo modinfo) throws SQLException{
    }
    private void doAct5(ModuleInfo modinfo) throws SQLException{
    }
    public ModuleObject getTabs(){
      return makeLinkTable();
    }
    private Table makeLinkTable(){
      Table LinkTable = new Table(1,1);
      LinkTable.setBorder(0);
      LinkTable.setCellpadding(0);
      LinkTable.setCellspacing(0);
      LinkTable.setWidth("100%");

      Link Link1 = new Link(new Image(iAct == ACT1?"/pics/tabs/IS/yfirlit.gif":"/pics/tabs/IS/yfirlit1.gif"));
      Link1.addParameter(strAction,ACT1);
      Link Link2 = new Link(new Image(iAct == ACT2?"/pics/tabs/IS/yfirlit.gif":"/pics/tabs/IS/yfirlit1.gif"));
      Link2.addParameter(strAction,ACT2);
      Link Link3 = new Link(new Image(iAct == ACT3?"/pics/tabs/IS/yfirlit.gif":"/pics/tabs/IS/yfirlit1.gif"));
      Link3.addParameter(strAction,ACT3);
      Link Link4 = new Link(new Image(iAct == ACT4?"/pics/tabs/IS/yfirlit.gif":"/pics/tabs/IS/yfirlit1.gif"));
      Link4.addParameter(strAction,ACT4);
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,1,1);
      LinkTable.add(Link3,1,1);
      LinkTable.add(Link4,1,1);

      return LinkTable;
  }

  public void main(ModuleInfo modinfo)  {
    try{
    isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo: fixa Admin*/
    control(modinfo);
  }
}// class PriceCatalogueMaker


