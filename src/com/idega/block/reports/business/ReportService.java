package com.idega.block.reports.business;



import com.idega.block.reports.data.Report;
import com.idega.presentation.IWContext;



/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega multimedia

 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>

 * @version 1.0

 */



public class ReportService {



  public static String getPrmName(){

    return "reports.session.report";

  }



  public static boolean isSessionReport(IWContext iwc){

    if(iwc.getSessionAttribute(getPrmName())!=null) {
		return true;
	}
	else {
		return false;
	}

  }



  public static void setSessionReport(IWContext iwc,Report R){

    iwc.setSessionAttribute(getPrmName(),R );

  }

  public static Report getSessionReport(IWContext iwc){

    if(iwc.getSessionAttribute(getPrmName()) != null) {
		return (Report) iwc.getSessionAttribute(getPrmName());
	}
	else {
		return null;
	}

  }

  public static void removeSessionReport(IWContext iwc){

    if(iwc.getSessionAttribute(getPrmName()) != null) {
		iwc.removeSessionAttribute(getPrmName());
	}

  }

  public static String categoryPrm(){

    return "reports.category";

  }



  public static boolean isSessionCategory(IWContext iwc){

    if(iwc.getSessionAttribute(categoryPrm())!=null) {
		return true;
	}
	else {
		return false;
	}

  }



  public static int getSessionCategory(IWContext iwc){

    int r = 0;

    if(iwc.getSessionAttribute( categoryPrm())!= null) {
		r = ((Integer)iwc.getSessionAttribute( categoryPrm())).intValue();
	}

    return r;

  }

  public static void setSessionCategory(IWContext iwc,int category){

    iwc.setSessionAttribute( categoryPrm(),new Integer(category) );

  }

  public static void removeSessionCategory(IWContext iwc){

    if(iwc.getSessionAttribute( categoryPrm()) != null) {
		iwc.removeSessionAttribute( categoryPrm());
	}

  }

}

