/*
 * $Id: CampusFactory.java,v 1.17 2004/05/24 14:21:40 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.block.application.presentation.ReferenceNumberInfo;

import com.idega.block.application.presentation.ReferenceNumber;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.Parameter;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class CampusFactory extends Block implements Campus {
  public final static String prmContentView = "cam_fact_view";
  private final static String prmContentViewContent = "sess_cam_fact_view";
  public final static int TABBER = 100;
  public final static int CONTENT = 200;
  public final static int MENU = 300;
  private int page_id = -1;

  private int iContentType = CONTENT;

  /**
   *
   */
  public CampusFactory() {
  }

  /**
   *
   */
  public void setContentView(int viewType) {
    iContentType = viewType;
  }

  /**
   *
   */
  public void main(IWContext iwc) {
    if (iContentType == TABBER){
      add(new CampusTabber());
    }
    else if(iContentType == MENU){
      add(new CampusMenu());
    }
    else if(iContentType == CONTENT){
      String view = iwc.getParameter(prmContentView);
      String refnum = iwc.getParameter(ReferenceNumber.CAM_REF_NUMBER);
      if (view != null) {
        iwc.setSessionAttribute(prmContentViewContent,view);
      }
      else if(iwc.getSessionAttribute(prmContentViewContent) != null) {
        view = (String)iwc.getSessionAttribute(prmContentViewContent);
      }

      if (refnum != null) {
        iwc.setSessionAttribute(ReferenceNumberInfo.SESSION_REFERENCE_NUMBER,refnum);
      }
      else {
        iwc.setSessionAttribute(ReferenceNumberInfo.SESSION_REFERENCE_NUMBER,"");
      }

      int iView = 0;
      if (view !=null){
        iView = Integer.parseInt(view);
        if (iView == 13) {

        }
        else if(iView < 20 && !iwc.isLoggedOn()){
          iwc.removeSessionAttribute(prmContentViewContent);
          iView = HOME;

        }
       // System.err.println("view "+iView);
      }
      add(getPresentationObject(iView));
    }
  }

  /**
   *
   */
  private PresentationObject getPresentationObject(int ContentView) {
    PresentationObject obj = new CampusHome();
    switch (ContentView) {
      case HOME :
        obj = new CampusHome();
        break;
      case ADM_FINANCE :
        //Finance fin = new Finance(1);
        //FinanceIndex fin = new FinanceIndex(1);

        //fin.addFinanceObject(new PhoneFiles());
        //obj = fin;
        break;
      case ADM_HABITANTS :
        //obj = new CampusHabitants();
//        obj = new TenantsHabitants();
        obj = new Habitants();
        break;
      case ADM_ALLOCATION :
        obj = new CampusAllocation();
        break;
      case ADM_APARTMENTS :
        obj = new CampusApartments();
        break;
      case ADM_ANNOUNCE :
        obj = new CampusAnnounceMents();
        break;
      case TEN_PROFILE :
        obj = new TenantsProfile();
        break;
      case TEN_FINANCE :
        obj = new TenantsFinance();
        break;
      case TEN_HABITANTS :
        obj = new TenantsHabitants();
        break;
      case TEN_ANNOUNCE :
        obj = new CampusAnnounceMents();
        break;
      case MENU_APARTMENTS :
        obj = new CampusBuildings();
        break;
      case MENU_APPLICATION :
//        obj = new CampusRefInfo();
        obj = new CampusApply();
        break;
      case MENU_INFO :
        obj = new TextControl();
        break;
      case MENU_INSTRUCT :
        obj = new TextControl();
        break;
      case MENU_LINKS :
        obj = new TextControl();
        break;
      case MENU_OFFICE :
        obj = new TextControl();
        break;
      case MENU_RULES :
        obj = new TextControl();
        break;
      case REF_INFO :
        obj = new CampusRefInfo();
        break;
      case REQUESTS :
        obj = new CampusRequests();
        break;
    }
    return(obj);
  }

  /**
   *
   */
  public static Parameter getParameter(int contentView) {
    return(new Parameter(prmContentView,String.valueOf(contentView)));
  }

}
