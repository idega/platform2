package com.idega.block.reports.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.textObject.Link;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ReportViewWindow extends IWAdminWindow {

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.reports";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public final static String prmReportId = ReportContentViewer.prmReportId;

  public ReportViewWindow() {
    setWidth(800);
    setHeight(600);
    setResizable( true);
    setMenubar( true);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(ModuleInfo modinfo) throws Exception{

    iwrb = getResourceBundle(modinfo);
    ReportContentViewer RCV = new ReportContentViewer();
    add(RCV);
    String title = iwrb.getLocalizedString("report_viewer","Report Viewer");
    setTitle(title);
    addTitle(title);
    addHeaderObject(getLinkTable());
  }

  private ModuleObject getLinkTable(){
    Link L = new Link(iwrb.getLocalizedString("close","Close"));
    L.setFontStyle("text-decoration: none");
    L.setFontColor("#FFFFFF");
    L.setBold();
    L.setEventListener(ReportContentViewer.class);
    L.setOnClick("window.close()");
    return L;
  }

  private void doCloseNoAction(ModuleInfo modinfo){
    ReportContentViewer.removeSessionParameters(modinfo);
    close();
  }
}