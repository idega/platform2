/*
 * $Id: Title.java,v 1.5 2001/08/30 01:31:32 laddi Exp $
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
import com.idega.jmodule.login.business.*;
import java.sql.SQLException;
import java.io.IOException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
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
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus";


  public Title(){
    MiddleColor = "#9FA9B3";
    LightColor = "#D7DADF";
    DarkColor = "#27324B";
  }

  private void control(ModuleInfo modinfo){

    try{

      if(modinfo.getParameter(strAction) == null){
        if ( modinfo.getServletContext().getAttribute(strAction) != null ) {
          sAct = (String) modinfo.getSessionAttribute(strAction);
          try {
            iAct = Integer.parseInt(sAct);
          }
          catch (NumberFormatException e){
            iAct = -1;
          }
        }
        else {
          iAct = NOACT;
        }
      }
      if(modinfo.getParameter(strAction) != null){
        sAct = modinfo.getParameter(strAction);
        try {
          iAct = Integer.parseInt(sAct);
        }
        catch (NumberFormatException e){
          iAct = -1;
        }
        if ( ((String) modinfo.getSessionAttribute(strAction)) != (sAct) ) {
          modinfo.setSessionAttribute(strAction,sAct);
        }
      }
      doAct();
    }
    catch(Exception S){	S.printStackTrace();	}
  }


  private void doAct(){
    String TitleUrl;
    String lang = "IS";
    Image image = null;
    switch (iAct) {
      case ACT1:  image = iwrb.getImage("/title/info.gif");             break;
      case ACT2:  image = iwrb.getImage("/title/office.gif");            break;
      case ACT3:  image = iwrb.getImage("/title/application.gif");      break;
      case ACT4:  image = iwrb.getImage("/title/apartment.gif");        break;
      case ACT5:  image = iwrb.getImage("/title/links.gif");            break;
      case ACT6:  image = iwrb.getImage("/title/english.gif");          break;
      //case ACT7:  image = iwrb.getImage("/title/maintitle.gif");         break;
      //case ACT8:  TitleUrl = iwrb.getImage("/title/maintitle.gif";       break;
      //default: image = iwrb.getImage("/title/maintitle.gif");            break;
    }
    if ( image != null ) {
      add(image);
    }
  }

  public String getObjectName(){
      return iObjectName;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(ModuleInfo modinfo)  {
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    try{
    isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo: fixa Admin*/
    control(modinfo);
  }
}// class PriceCatalogueMaker


