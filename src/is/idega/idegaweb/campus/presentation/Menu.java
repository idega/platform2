/*

 * $Id: Menu.java,v 1.5 2004/05/24 14:21:40 palli Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.campus.presentation;







import java.util.Enumeration;

import com.idega.core.localisation.business.LocaleSwitcher;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.util.LocaleUtil;



/**

 *

 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>

 * @version 1.0

 */

public class Menu extends Block{



  private String iObjectName = "Menu";

  private String LightColor,MiddleColor,DarkColor;

  private int iAct;

  private String sAct;

  private String strAction = TabAction.sAction;

  private boolean isAdmin;

  private final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4;

  private final int ACT5 = 5, ACT6 = 6, ACT7 = 7, ACT8 = 8;

  private final int NOACT = 0;

  protected IWResourceBundle iwrb;

  protected IWBundle iwb;

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";

	private int templateId = -1;



  public Menu(){

    MiddleColor = "#9FA9B3";

    LightColor = "#D7DADF";

    DarkColor = "#27324B";

  }



  private void control(IWContext iwc){

    iwrb = getResourceBundle(iwc);

    iwb = getBundle(iwc);

    try{



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

        if(iAct == 0)

          iwc.removeSessionAttribute(strAction);

        //if ( ((String) iwc.getSessionAttribute(strAction)) != (sAct) ) {

          iwc.setSessionAttribute(strAction,sAct);

        //}

      }

      doAct(iwc);





    }

    catch(Exception S){	S.printStackTrace();	}

  }



  public String getBundleIdentifier(){

    return IW_BUNDLE_IDENTIFIER;

  }



  private void doAct(IWContext iwc){

    int MenuCount = 7;

    Table LinkTable = new Table();

    LinkTable.setBorder(0);

    LinkTable.setCellpadding(0);

    LinkTable.setCellspacing(0);

    LinkTable.setWidth("100%");

    int iWidth = 130;

    int iHeight = 18;



    int row = 1;

    CampusLinkFactory CF = new CampusLinkFactory();



    Image office = (iAct != ACT2?iwrb.getImage("/menu/office.gif","/menu/office_o.gif","Office",iWidth,iHeight):iwrb.getImage("/menu/office1.gif",iWidth,iHeight));

    Link lOffice = CF.getLink(CF.MENU_OFFICE,office);

    lOffice.addParameter(TextControl.strAction,"1");

    lOffice.addParameter(strAction,ACT2);

    LinkTable.add(lOffice,1,row);

    row++;



    Image apartment = (iAct != ACT4?iwrb.getImage("/menu/apartment.gif","/menu/apartment_o.gif","Appartment",iWidth,iHeight):iwrb.getImage("/menu/apartment1.gif",iWidth,iHeight));

    Link lApartment = CF.getLink(CF.MENU_APARTMENTS,apartment);

    lApartment.addParameter(strAction,ACT4);

    LinkTable.add(lApartment,1,row);

    row++;



    Image apply = (iAct != ACT3?iwrb.getImage("/menu/apply.gif","/menu/apply_o.gif","Apply",iWidth,iHeight):iwrb.getImage("/menu/apply1.gif",iWidth,iHeight));

    Link lApply = CF.getLink(CF.MENU_APPLICATION,apply);

    lApply.addParameter(strAction,ACT3);

    LinkTable.add(lApply,1,row);

    row++;



    if ( iAct == ACT3 ) {

      Image instructs = iwrb.getImage("/menu/instructions.gif",iWidth,iHeight);

      Link lInstr = CF.getLink(CF.MENU_INSTRUCT,instructs);

      lInstr.addParameter(TextControl.strAction,"4");

      LinkTable.add(lInstr,1,row);

      row++;



      Image rules = iwrb.getImage("/menu/rules.gif",iWidth,iHeight);

      Link lRules = CF.getLink(CF.MENU_RULES,rules);

      lRules.addParameter(TextControl.strAction,"3");

      LinkTable.add(lRules,1,row);

      row++;

    }



    Image info = (iAct != ACT1?iwrb.getImage("/menu/info.gif","/menu/info_o.gif","Info",iWidth,iHeight):iwrb.getImage("/menu/info1.gif",iWidth,iHeight));

    Link lInfo = CF.getLink(CF.MENU_INFO,info);

    lInfo.addParameter(strAction,ACT1);

    LinkTable.add(lInfo,1,row);

    row++;



    Image links = (iAct != ACT5?iwrb.getImage("/menu/links.gif","/menu/links_o.gif","Links",iWidth,iHeight):iwrb.getImage("/menu/links1.gif",iWidth,iHeight));

    Link lLinks = CF.getLink(CF.MENU_LINKS,links);

    lLinks.addParameter(strAction,ACT5);

    lLinks.addParameter(TextControl.strAction,"14");

    LinkTable.add(lLinks,1,row);

    row++;



    Image home = iwrb.getImage("/menu/home.gif","/menu/home_o.gif","Home",iWidth,iHeight);

    Link lHome = new Link(home,"/index.jsp");

    lHome.addParameter(strAction,NOACT);

    LinkTable.add(lHome,1,row);

    row++;



    LinkTable.add(iwb.getImage("redtab.gif","",iWidth,iHeight),1,row);

    row++;



    Image language = iwrb.getImage("/menu/language.gif",iwrb.getLocalizedString("language","English"),95,37);

    Link link6 = new Link(language);

    if(iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale())){

      link6.addParameter(LocaleSwitcher.languageParameterString,LocaleSwitcher.englishParameterString);

    }

    else{

      link6.addParameter(LocaleSwitcher.languageParameterString,LocaleSwitcher.icelandicParameterString);

    }

    link6.setEventListener(com.idega.core.localisation.business.LocaleSwitcher.class.getName());

    //link6.maintainParameter("text_action",iwc);

    //link6.maintainParameter("campus_action",iwc);

    Enumeration e = iwc.getParameterNames();

    while(e.hasMoreElements()){

      String s = (String)e.nextElement();

      link6.addParameter(s,iwc.getParameter(s));

    }



    LinkTable.add(link6,1,row);

    LinkTable.setHeight(1,row,"51");

    LinkTable.setAlignment(1,row,"center");



    add( LinkTable);

  }



  public int getAct(){

    return iAct;

  }



	public void setTemplate(com.idega.core.builder.data.ICPage templatePage){

	  templateId = templatePage.getID();

	}



  public String getObjectName(){

      return iObjectName;

  }



  public void main(IWContext iwc)  {

    isAdmin = iwc.hasEditPermission(this);

    control(iwc);

  }

}// class Menu

