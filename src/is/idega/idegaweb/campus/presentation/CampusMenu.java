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
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.Window;
import com.idega.util.LocaleUtil;



/**

 * Title:   idegaclasses

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author  <a href="mailto:aron@idega.is">aron@idega.is

 * @version 1.0

 */



public class CampusMenu extends Block implements Campus{



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



  public CampusMenu(){

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



    Image office = (iAct != ACT2?iwrb.getImage("/menu/office.gif","/menu/office_o.gif","Office",iWidth,iHeight):iwrb.getImage("/menu/office1.gif",iWidth,iHeight));

    Link lOffice = new Link(office);

    lOffice.addParameter(TextControl.strAction,"1");

    lOffice.addParameter(getParameter(MENU_OFFICE));

    lOffice.addParameter(strAction,ACT2);

    LinkTable.add(lOffice,1,row);

    row++;



    Image apartment = (iAct != ACT4?iwrb.getImage("/menu/apartment.gif","/menu/apartment_o.gif","Appartment",iWidth,iHeight):iwrb.getImage("/menu/apartment1.gif",iWidth,iHeight));

    Link lApartment = new Link(apartment);

    lApartment.addParameter(strAction,ACT4);

    lApartment.addParameter(getParameter(MENU_APARTMENTS));

    LinkTable.add(lApartment,1,row);

    row++;



    Image apply = (iAct != ACT3?iwrb.getImage("/menu/apply.gif","/menu/apply_o.gif","Apply",iWidth,iHeight):iwrb.getImage("/menu/apply1.gif",iWidth,iHeight));

    Link lApply = new Link(apply);

    lApply.addParameter(strAction,ACT3);

    lApply.addParameter(getParameter(MENU_APPLICATION));

    LinkTable.add(lApply,1,row);

    row++;



    if ( iAct == ACT3 ) {

      Image instructs = iwrb.getImage("/menu/instructions.gif",iWidth,iHeight);

      Link lInstr = new Link(instructs);

      lInstr.addParameter(getParameter(MENU_INSTRUCT));

      lInstr.addParameter(TextControl.strAction,"4");

      LinkTable.add(lInstr,1,row);

      row++;



      Image rules = iwrb.getImage("/menu/rules.gif",iWidth,iHeight);

      Link lRules = new Link(rules);

      lRules.addParameter(getParameter(MENU_RULES));

      lRules.addParameter(TextControl.strAction,"3");

      LinkTable.add(lRules,1,row);

      row++;

    }



    Image info = (iAct != ACT1?iwrb.getImage("/menu/info.gif","/menu/info_o.gif","Info",iWidth,iHeight):iwrb.getImage("/menu/info1.gif",iWidth,iHeight));

    Link lInfo = new Link(info);

		lInfo.addParameter(getParameter(MENU_INFO));

    lInfo.addParameter(strAction,ACT1);

    LinkTable.add(lInfo,1,row);

    row++;



    Image links = (iAct != ACT5?iwrb.getImage("/menu/links.gif","/menu/links_o.gif","Links",iWidth,iHeight):iwrb.getImage("/menu/links1.gif",iWidth,iHeight));

    Link lLinks = new Link(links);

		lLinks.addParameter(getParameter(MENU_LINKS));

    lLinks.addParameter(strAction,ACT5);

    lLinks.addParameter(TextControl.strAction,"14");

    LinkTable.add(lLinks,1,row);

    row++;



    Image home = iwrb.getImage("/menu/home.gif","/menu/home_o.gif","Home",iWidth,iHeight);

    Link lHome = new Link(home);

    lHome.addParameter(strAction,NOACT);

		lHome.addParameter(getParameter(HOME));

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



    row++;

    /** @todo move away */

/*    Link Requests = new Link(iwrb.getLocalizedString("requests","Beiðnir"));

    Requests.addParameter(strAction,NOACT);

    Requests.addParameter(getParameter(REQUESTS));

    LinkTable.add(Requests,1,row);

    LinkTable.setAlignment(1,row,"center");*/

    Window w = new Window("gardar_popup",550,550,"http://einar.vortex.is/hjalp/index.html");

    w.setResizable(true);

    w.setScrollbar(true);

    w.setLocation(false);

    w.setStatus(false);

    w.setMenubar(false);

    Link RHI = new Link(iwrb.getLocalizedString("rhi","Uppsetning nettengingar"));

    RHI.setWindow(w);

    RHI.setURL("http://einar.vortex.is/hjalp/index.html");

    Edit.setStyle(RHI);

    RHI.setFontSize(Edit.textFontSize);

    LinkTable.add(RHI,1,row);

    LinkTable.setAlignment(1,row,"left");

    row++;

    Link telephone = new Link(iwrb.getLocalizedString("telephone","Símaskrá"));

    telephone.addParameter(getParameter(TEN_HABITANTS));

    telephone.addParameter(strAction,22);

    Edit.setStyle(telephone);

    telephone.setFontSize(Edit.textFontSize);

    LinkTable.add(telephone,1,row);

    LinkTable.setAlignment(1,row,"left");



    add(LinkTable);

  }



  public static Parameter getParameter(int contentView){

    return CampusFactory.getParameter((contentView));

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

