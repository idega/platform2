package com.idega.projects.campus.service;

import com.idega.data.genericentity.Group;
import com.idega.data.genericentity.Member;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.login.business.*;
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

public class Title extends JModuleObject{

  private String iObjectName = "Title";
  private String LightColor,MiddleColor,DarkColor;
  private int iAct;
  private String sAct;
  private static final String strAction = TabAction.sAction;
  private boolean isAdmin;
  private Image Title;
  private final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4;
  private final int ACT5 = 5, ACT6 = 6, ACT7 = 7, ACT8 = 8;
  private final int NOACT = 0;

  public Title(){
    MiddleColor = "#9FA9B3";
    LightColor = "#D7DADF";
    DarkColor = "#27324B";
  }

  private void control(ModuleInfo modinfo){

    try{

      if(modinfo.getParameter(strAction) == null){
        iAct = NOACT;
      }
      if(modinfo.getParameter(strAction) != null){
        sAct = modinfo.getParameter(strAction);
        iAct = Integer.parseInt(sAct);
      }
      doAct();
    }
    catch(Exception S){	S.printStackTrace();	}
  }


  private void doAct(){
    String TitleUrl;
    String lang = "IS";
    switch (iAct) {
      case ACT1:  TitleUrl = "/pics/titles/"+lang+"/info2.gif";             break;
      case ACT2:  TitleUrl = "/pics/titles/"+lang+"/regulations2.gif";      break;
      case ACT3:  TitleUrl = "/pics/titles/"+lang+"/application2.gif";      break;
      case ACT4:  TitleUrl = "/pics/titles/"+lang+"/apartment2.gif";        break;
      case ACT5:  TitleUrl = "/pics/titles/"+lang+"/links2.gif";            break;
      case ACT6:  TitleUrl = "/pics/titles/"+lang+"/english2.gif";          break;
      case ACT7:  TitleUrl = "/pics/titles/"+lang+"/maintitle.gif";             break;
      //case ACT8:  TitleUrl = "/pics/titles/"+lang+"/maintitle.gif";       break;
      default: TitleUrl = "/pics/titles/"+lang+"/maintitle.gif";            break;
    }
    add(new Image(TitleUrl));
  }

  public String getObjectName(){
      return iObjectName;
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


