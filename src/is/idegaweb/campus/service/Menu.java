/*
 * $Id: Menu.java,v 1.5 2001/07/13 16:19:48 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;

import com.idega.data.genericentity.Group;
import com.idega.data.genericentity.Member;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.login.business.*;
import java.sql.SQLException;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class Menu extends JModuleObject{

  private String iObjectName = "Menu";
  private String LightColor,MiddleColor,DarkColor;
  private int iAct;
  private String sAct;
  private String strAction = TabAction.sAction;
  private Member eMember;
  private boolean isAdmin;
  private Image Title;
  private final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4;
  private final int ACT5 = 5, ACT6 = 6, ACT7 = 7, ACT8 = 8;
  private final int NOACT = 0;

  public Menu(){
    MiddleColor = "#9FA9B3";
    LightColor = "#D7DADF";
    DarkColor = "#27324B";
  }

  private void control(ModuleInfo modinfo){

    try{
      eMember = AccessControl.getMember(modinfo);

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
      doAct();
    }
    catch(Exception S){	S.printStackTrace();	}
  }

  private void doAct(){
    int MenuCount = 7;
    Table LinkTable = new Table();
    LinkTable.setBorder(0);
    LinkTable.setCellpadding(0);
    LinkTable.setCellspacing(0);
    LinkTable.setWidth("100%");
    int iWidth = 130;
    int iHeight = 18;

    int row = 1;

    Image menu1 = new Image(iAct != ACT1?"/pics/menu/info.gif":"/pics/menu/info1.gif","Upplýsingar",iWidth,iHeight);
    Link link1 = new Link(menu1);
    link1.setURL(getUrl(ACT1));
    link1.addParameter(strAction,ACT1);
    LinkTable.add(link1,1,row);
    row++;

    Image menu2 = new Image(iAct != ACT2?"/pics/menu/office.gif":"/pics/menu/office1.gif","Skrifstofa",iWidth,iHeight);
    Link link2 = new Link(menu2);
    link2.setURL(getUrl(ACT2));
        link2.addParameter(TextControl.strAction,"1");
        link2.addParameter(strAction,ACT2);
    LinkTable.add(link2,1,row);
    row++;

    if ( iAct == ACT2 ) {
      Image menu2_1 = new Image("/pics/menu/staff.gif","Starfsfólk",iWidth,iHeight);
      Link link2_1 = new Link(menu2_1);
        link2_1.setURL(getUrl(ACT8));
        link2_1.addParameter(TextControl.strAction,"2");
      LinkTable.add(link2_1,1,row);
      row++;
    }

    Image menu3 = new Image(iAct != ACT3?"/pics/menu/apply.gif":"/pics/menu/apply1.gif","Umsókn",iWidth,iHeight);
    Link link3 = new Link(menu3);
    link3.setURL(getUrl(ACT3));
    link3.addParameter(strAction,ACT3);
    LinkTable.add(link3,1,row);
    row++;

    if ( iAct == ACT3 ) {
      Image menu3_1 = new Image("/pics/menu/rules.gif","Úthlutunarreglur",iWidth,iHeight);
      Link link3_1 = new Link(menu3_1);
        link3_1.setURL(getUrl(ACT8));
        link3_1.addParameter(TextControl.strAction,"3");
      LinkTable.add(link3_1,1,row);
      row++;
    }

    Image menu4 = new Image(iAct != ACT4?"/pics/menu/apartment.gif":"/pics/menu/apartment1.gif","Íbúðir");
    Link link4 = new Link(menu4);
    link4.setURL(getUrl(ACT4));
    link4.addParameter(strAction,ACT4);
    LinkTable.add(link4,1,row);
    row++;

    if ( iAct == ACT4 ) {
      Image menu4_1 = new Image("/pics/menu/complex.gif","Garðarnir",iWidth,iHeight);
      Link link4_1 = new Link(menu4_1);
        link4_1.setURL(getUrl(ACT4));
      LinkTable.add(link4_1,1,row);
      row++;

      Image menu4_2 = new Image("/pics/menu/instructions.gif","Leiðbeiningar",iWidth,iHeight);
      Link link4_2 = new Link(menu4_2);
        link4_2.setURL(getUrl(ACT8));
        link4_2.addParameter(TextControl.strAction,"4");
      LinkTable.add(link4_2,1,row);
      row++;

      Image menu4_3 = new Image("/pics/menu/search.gif","Leit",iWidth,iHeight);
      Link link4_3 = new Link(menu4_3);
        link4_3.setURL("/main/search.jsp");
      LinkTable.add(link4_3,1,row);
      row++;
    }

    Image menu5 = new Image(iAct != ACT5?"/pics/menu/links.gif":"/pics/menu/links1.gif","Tenglar",iWidth,iHeight);
    Link link5 = new Link(menu5);
    link5.setURL(getUrl(ACT5));
    link5.addParameter(strAction,ACT5);
    link5.addParameter(TextControl.strAction,"14");
    LinkTable.add(link5,1,row);
    row++;

    Image menu6 = new Image(iAct != ACT6?"/pics/menu/english.gif":"/pics/menu/english1.gif","English",iWidth,iHeight);
    Link link6 = new Link(menu6);
    link6.setURL(getUrl(ACT6));
    link6.addParameter(strAction,ACT6);
    LinkTable.add(link6,1,row);
    row++;

    Image menu7 = new Image("/pics/menu/home.gif","Heim",iWidth,iHeight);
    Link link7 = new Link(menu7);
    link7.setURL(getUrl(ACT7));
    link7.addParameter(strAction,ACT7);
    LinkTable.add(link7,1,row);
    row++;

    LinkTable.add(new Image("/pics/menu/redtab.gif","",iWidth,iHeight),1,row);
    LinkTable.add(new Image("/pics/menu/idegaweb.gif",""),1,row+1);

    Title = getT();

    add( LinkTable);
  }
  private Image getTitle(){
    return Title;
  }
  public int getAct(){
    return iAct;
  }

  private String getUrl(int act){
    String url = "";
      switch (act) {
      case ACT1:  url = "/main/info.jsp";            break;
      case ACT2:  url = "/main/text.jsp";            break;
      case ACT3:  url = "/main/apply.jsp";           break;
      case ACT4:  url = "/main/apartments.jsp";      break;
      case ACT5:  url = "/main/text.jsp";            break;
      case ACT6:  url = "/main/english.jsp";         break;
      case ACT7:  url = "/index2.jsp";               break;
      case ACT8:  url = "/main/text.jsp";            break;
      default: url =  "/index2.jsp";                 break;
    }
    return url;
  }

  private Image getT(){
    String TitleUrl;
    String lang = "IS";
    switch (iAct) {
      case ACT1:  TitleUrl = "/pics/titles/"+lang+"/info2.gif";         break;
      case ACT2:  TitleUrl = "/pics/titles/"+lang+"/regulations2.gif";  break;
      case ACT3:  TitleUrl = "/pics/titles/"+lang+"/application2.gif";  break;
      case ACT4:  TitleUrl = "/pics/titles/"+lang+"/apartment2.gif";    break;
      case ACT5:  TitleUrl = "/pics/titles/"+lang+"/links2.gif";        break;
      case ACT6:  TitleUrl = "/pics/titles/"+lang+"/english2.gif";      break;
      case ACT7:  TitleUrl = "/pics/titles/"+lang+"/home2.gif";         break;
      default: TitleUrl = "/pics/titles/"+lang+"/maintitle.gif";        break;
    }
    return new Image(TitleUrl);
  }

  public String getObjectName(){
      return iObjectName;
  }

  public void main(ModuleInfo modinfo)  {
    try{
    isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}

    control(modinfo);
  }
}// class Menu