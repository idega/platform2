package com.idega.projects.golf.business;
import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import java.lang.String;
import com.idega.projects.golf.presentation.*;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class GolferPageViewController {

  public GolferPageViewController() {
  }

  public static JModuleObject getView(ModuleInfo modinfo){
    if (modinfo.isParameterSet(LinkParameters.sTopMenuParameterName)) {
      String parameterValue = modinfo.getParameter(LinkParameters.sTopMenuParameterName);

      if (parameterValue.equalsIgnoreCase(LinkParameters.sHomeParameterValue)) {
        return( new HomeView());
      }

      else if (parameterValue.equalsIgnoreCase(LinkParameters.abroadResultsParameterValue)) {
        return( new AbroadResultsView());
      }

      else if (parameterValue.equalsIgnoreCase(LinkParameters.homeResultsParameterValue)) {
        return( new HomeResultsView());
      }

      else if (parameterValue.equalsIgnoreCase(LinkParameters.sInfoParameterValue)) {
        return( new GolfbagView());
      }

      else if (parameterValue.equalsIgnoreCase(LinkParameters.sInterviewsParameterValue)) {
        return( new SupportView());
      }

      else if (parameterValue.equalsIgnoreCase(LinkParameters.sPicturesParameterValue)) {
        return( new JModuleObject());
      }

      else if (parameterValue.equalsIgnoreCase(LinkParameters.sRecordParameterValue)) {
        return( new HomeResultsView());
      }

      else if (parameterValue.equalsIgnoreCase(LinkParameters.sStatisticsParameterValue)) {
        return( new StatisticsView());
      }

      else if (parameterValue.equalsIgnoreCase(LinkParameters.sSubmitParameterValue)) {
        return( new JModuleObject());
      }
      else {
        return( new HomeView());
      }
    }
    System.err.println("ARRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRGGGGGGGGGGGGGGGGGGGGGGG");
    return( new HomeView());
  }
}