package com.idega.projects.golf.presentation;

import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Image;
import java.lang.String;
import java.sql.SQLException;
import com.idega.jmodule.object.textObject.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.projects.golf.entity.GolferPageData;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class FakeSideMenu extends GolferJModuleObject implements LinkParameters{

  public FakeSideMenu() {
  }

  private void addFakeResultsSidemenu(){
    Image iRecordLogo = iwrb.getImage("/golferpage/ferill.gif");
    //this.addLeftLogo(iRecordLogo);
    //this.setStyleSheetURL("/style/GolferStatisticsView.css");
    Table dummyTable = new Table(1,1);
    dummyTable.setCellspacing(5);
    Table fakeSideMenuHomeTable = new Table(2,1);
    Image bullet = iwb.getImage("shared/bullet.gif");
    fakeSideMenuHomeTable.add(bullet,1,1);
    Link fakeSideMenuLinkHome = new Link("  ÁRANGUR HEIMA");
    fakeSideMenuLinkHome.setCSSClass("style1");
    fakeSideMenuLinkHome.setFontFace(Text.FONT_FACE_VERDANA);
    fakeSideMenuLinkHome.setFontSize(1);
    fakeSideMenuLinkHome.setBold();
    fakeSideMenuLinkHome.setStyle("linkur");
    fakeSideMenuLinkHome.addParameter(sTopMenuParameterName, homeResultsParameterValue);
    fakeSideMenuHomeTable.add(fakeSideMenuLinkHome,2,1);
    dummyTable.add(fakeSideMenuHomeTable,1,1);
    Link fakeSideMenuLinkAbroad = new Link("  ÁRANGUR ERLENDIS");
    fakeSideMenuLinkAbroad.setCSSClass("style1");
    fakeSideMenuLinkAbroad.setFontFace(Text.FONT_FACE_VERDANA);
    fakeSideMenuLinkAbroad.setStyle("linkur");
    fakeSideMenuLinkAbroad.setFontSize(1);
    fakeSideMenuLinkAbroad.setBold();
    fakeSideMenuLinkAbroad.addParameter(sTopMenuParameterName, abroadResultsParameterValue);
    Table fakeSideMenuLinkAbroadTable = new Table();
    fakeSideMenuLinkAbroadTable.add(bullet,1,1);
    fakeSideMenuLinkAbroadTable.add(fakeSideMenuLinkAbroad,2,1);
//    dummyTable.addBreak(1,1);
    dummyTable.add(fakeSideMenuLinkAbroadTable,1,1);
    add(dummyTable);
  }

  public void main(ModuleInfo modinfo) throws SQLException{
    super.main(modinfo);
    if (modinfo.isParameterSet(LinkParameters.sTopMenuParameterName)) {
      String parameterValue = modinfo.getParameter(LinkParameters.sTopMenuParameterName);
      if ((parameterValue.equalsIgnoreCase(LinkParameters.sRecordParameterValue)) ||
        (parameterValue.equalsIgnoreCase(LinkParameters.homeResultsParameterValue)) ||
        (parameterValue.equalsIgnoreCase(LinkParameters.abroadResultsParameterValue))){
        addFakeResultsSidemenu();
      }
    }
  }
}