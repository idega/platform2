package com.idega.projects.campus.service;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.login.business.AccessControl;
import com.idega.data.genericentity.Member;
import com.idega.data.genericentity.Group;
import com.idega.jmodule.text.presentation.TextReader;
import com.idega.projects.campus.service.*;
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

public class TextControl extends JModuleObject {

  private String LightColor,MiddleColor,DarkColor;
  private String action;
  public static final String strAction = "text_action";
  private Member eMember;
  private ModuleObject Tabs;
  private CampusObject CampObj;
  private Hashtable PermissionHash;
  private boolean isAdmin;
  private String sAct;
  private int iAct;
  public final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4;
  public final int ACT5 = 5, ACT6 = 6, ACT7 = 7, ACT8 = 8, ACT9 = 9;
  private final int NOACT = 0;


  public TextControl(){
    MiddleColor = "#9FA9B3";
    LightColor = "#D7DADF";
    DarkColor = "#27324B";
  }

  public ModuleObject getTabs(){
    return Tabs;
  }

  private void control(ModuleInfo modinfo){

    try{


      if(modinfo.getParameter(strAction) == null){
        iAct = NOACT;
      }
      else {
        sAct = modinfo.getParameter(strAction);
        iAct = Integer.parseInt(sAct);
      }
      switch (iAct) {
        case ACT1 :   doText1();         break;
        case ACT2 :   doText2();          break;
        case ACT3 :   doText3();          break;
        case ACT4 :   doText4();          break;
        case ACT5 :   doText5();          break;
        case ACT6 :   doText6();          break;
        case ACT7 :   doText7();          break;
        case ACT8 :   doText8();          break;
        case ACT9 :   doText9();          break;


        default:   doMenu();            break;
      }

    }
    catch(Exception S){	S.printStackTrace();	}

  }

  public void doMenu(){

    Table T = new Table();
      T.setWidth(400);
      T.setWidth(1,"50%");
      T.setWidth(2,"50%");
      T.setBorder(0);
      //T.setAlignment("CENTER");

    Link L = new Link("Gamli Garður","/apartments.jsp");
    L.addParameter(strAction,ACT1);
    L.addParameter(TabAction.sAction,TabAction.ACT5);
    Link L2 = new Link("Skerjagarður","/apartments.jsp");
    L2.addParameter(strAction,ACT2);
    L2.addParameter(TabAction.sAction,TabAction.ACT5);
    Link L3 = new Link("Ásgarðar","/apartments.jsp");
    L3.addParameter(strAction,ACT3);
    L3.addParameter(TabAction.sAction,TabAction.ACT5);
    Link L4 = new Link("Hjónagarðar","/apartments.jsp");
    L4.addParameter(strAction,ACT4);
    L4.addParameter(TabAction.sAction,TabAction.ACT5);
    Link L5 = new Link("Vetrargarðar","/apartments.jsp");
    L5.addParameter(strAction,ACT5);
    L5.addParameter(TabAction.sAction,TabAction.ACT5);
    Link L6 = new Link("Ásgarðar","/apartments.jsp");
    L6.addParameter(strAction,ACT6);
    L6.addParameter(TabAction.sAction,TabAction.ACT5);

    Text LText1 = new Text("v/Hringbraut");
      LText1.setFontSize(1);
    Text LText2 = new Text("Suðurgata 121");
      LText2.setFontSize(1);
    Text LText3 = new Text("Eggertsgata 16 - 34");
      LText3.setFontSize(1);
    Text LText4 = new Text("Eggertsgata 2 - 4");
      LText4.setFontSize(1);
    Text LText5 = new Text("Eggertsgata 6 - 10");
      LText5.setFontSize(1);
    Text LText6 = new Text("Eggertsgata 12 - 14");
      LText6.setFontSize(1);
    Text LText7 = new Text("Garðar með einstaklingsherbergjum / íbúðum og paríbúðum.");
      LText7.setFontSize(1);
    Text LText8 = new Text("Garðar með Fjölskylduíbúðum");
      LText8.setFontSize(1);


    T.add(LText8,2,1);
    T.add(LText4,2,4);
    T.add(LText5,2,6);
    T.add(LText6,2,8);
    T.add(LText7,1,1);
    T.add(LText3,1,8);
    T.add(LText1,1,4);
    T.add(LText2,1,6);

    T.add(L4,2,3);
    T.add(L5,2,5);
    T.add(L6,2,7);
    T.add(L,1,3);
    T.add(L2,1,5);
    T.add(L3,1,7);


    add(T);
  }
  public void doText1(){
      add(new TextReader(29));
  }
  public void doText2(){
       add(new TextReader(30));
  }
  public void doText3(){
      add(new TextReader(31));
  }
  public void doText4(){
    add(new TextReader(32));}


  public void doText5(){
    add(new TextReader(33));}


  public void doText6(){
    add(new TextReader(34));}

    public void doText7(){}

    public void doText8(){}

    public void doText9(){}



  public void main(ModuleInfo modinfo)  {
    try{
    isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo fixa Admin*/
    control(modinfo);
  }
}// class PriceCatalogueMaker


