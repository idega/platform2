package is.idega.idegaweb.campus.presentation;





import is.idega.idegaweb.campus.templates.CampusPage;

import com.idega.block.building.presentation.BuildingViewer;
import com.idega.block.finance.presentation.AccountViewer;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;



/**

 * Title:        idegaclasses

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega

 * @author <a href="aron@idega.is">Aron Birkir</a>

 * @version 1.0

 */



public class CampusLinkFactory {



  public final static int

  ADM_FINANCE = 1,

  ADM_HABITANTS =2,

  ADM_ALLOCATION=3,

  ADM_APARTMENTS=4,

  ADM_ANNOUNCE=5,

  TEN_PROFILE = 10,

  TEN_FINANCE =11,

  TEN_ANNOUNCE = 12 ,

  TEN_HABITANTS = 13,

  MENU_OFFICE = 20,

  MENU_APARTMENTS=21,

  MENU_APPLICATION=22,

  MENU_INFO=23,

  MENU_LINKS=24,

  MENU_INSTRUCT=25,

  MENU_RULES=26;



  public static Class DEFAULT_TEMPLATE_CLASS = CampusPage.class;



  public CampusLinkFactory() {

  }



  public static Link getLink(int LinkId,PresentationObject PO){

   return getLink( PO,LinkId ,DEFAULT_TEMPLATE_CLASS );

  }



  public static Link getLink(PresentationObject PO,int LinkId){

   return getLink( PO,LinkId ,DEFAULT_TEMPLATE_CLASS );

  }



  public static Link getLink(PresentationObject PO,int LinkId,Class TemplateClass){



    Class C = null;

    if((C = getInstanciateClass( LinkId)) !=null){

      if(TemplateClass == null){

        return new Link(PO,C,DEFAULT_TEMPLATE_CLASS );

      }

      else{

        return new Link(PO,C,TemplateClass );

      }

    }

    return new Link();

  }



  private static Class getInstanciateClass(int ID){

    Class C = null;

    switch (ID) {

     //case ADM_FINANCE :    C=CampusFinance.class;          break;

      case ADM_HABITANTS :  C=CampusHabitants.class;      break;

      case ADM_ALLOCATION:  C=CampusAllocation.class;         break;

      case ADM_APARTMENTS:  C=CampusBuilding.class;           break;

      case ADM_ANNOUNCE:    C=CampusAnnounceMents.class;      break;

      case TEN_PROFILE :    C=CampusAnnounceMents.class;      break;

      case TEN_FINANCE:     C=AccountViewer.class;            break;

      case TEN_HABITANTS:   C=CampusHabitants.class;          break;

      case TEN_ANNOUNCE:    C=CampusAnnounceMents.class;      break;

      case MENU_APARTMENTS :C=BuildingViewer.class;      break;

      case MENU_APPLICATION:C=CampusApply.class;           break;

      case MENU_INFO:       C=TextControl.class;         break;

      case MENU_INSTRUCT:   C=TextControl.class;     break;

      case MENU_LINKS:      C=TextControl.class;     break;

      case MENU_OFFICE :    C=TextControl.class;     break;

      case MENU_RULES :     C=TextControl.class;     break;



    }

    return C;

  }



}

