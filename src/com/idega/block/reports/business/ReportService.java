package com.idega.block.reports.business;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.reports.data.Report;

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

  public static void setSessionReport(ModuleInfo modinfo,Report R){
    modinfo.setSessionAttribute(getPrmName(),R );
  }
  public static Report getSessionReport(ModuleInfo modinfo){
    if(modinfo.getSessionAttribute(getPrmName()) != null)
      return (Report) modinfo.getSessionAttribute(getPrmName());
    else
      return null;
  }
  public static void removeSessionReport(ModuleInfo modinfo){
    if(modinfo.getSessionAttribute(getPrmName()) != null)
      modinfo.removeSessionAttribute(getPrmName());
  }

  public static int getSessionCategory(ModuleInfo modinfo){
    int r = 0;
    if(modinfo.getSessionAttribute("reports.category")!= null)
      r = ((Integer)modinfo.getSessionAttribute("reports.category")).intValue();
    return r;
  }
  public static void setSessionCategory(ModuleInfo modinfo,int category){
    modinfo.setSessionAttribute(getPrmName(),new Integer(category) );
  }
  public static void removeSessionCategory(ModuleInfo modinfo){
    if(modinfo.getSessionAttribute("reports.category") != null)
      modinfo.removeSessionAttribute("reports.category");
  }
}
