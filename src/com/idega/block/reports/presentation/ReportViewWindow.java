package com.idega.block.reports.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
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

  public void main(IWContext iwc) throws Exception{

    iwrb = getResourceBundle(iwc);
    //ReportContentViewer RCV = new ReportContentViewer();

    add(new ReportContentViewer());
    String title = iwrb.getLocalizedString("report_viewer","Report Viewer");
    setTitle(title);
    addTitle(title);
    addHeaderObject(getLinkTable());
  }

  private PresentationObject getLinkTable(){
    Link L = new Link(iwrb.getLocalizedString("close","Close"));
    L.setFontStyle("text-decoration: none");
    L.setFontColor("#FFFFFF");
    L.setBold();
    L.setEventListener(ReportContentViewer.class);
    L.setOnClick("window.close()");
    return L;
  }

  private void doCloseNoAction(IWContext iwc){
    ReportContentViewer.removeSessionParameters(iwc);
    close();
  }
}