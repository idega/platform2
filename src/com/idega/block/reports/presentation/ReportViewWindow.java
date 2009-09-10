package com.idega.block.reports.presentation;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ReportViewWindow extends IWAdminWindow implements Reports{

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.reports";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public static final  String prmCategoryId = PRM_CATEGORYID;
  public final static String prmReportId = PRM_REPORTID;

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

    this.iwrb = getResourceBundle(iwc);
    //ReportContentViewer RCV = new ReportContentViewer();

    ReportViewer rv = new ReportViewer();

    rv.setShowLinks(false);
    add(rv);
    String title = this.iwrb.getLocalizedString("report_viewer","Report Viewer");
    setTitle(title);
    addTitle(title);
    addHeaderObject(rv.getLinks());
  }

  private PresentationObject getLinkTable(){
    Link L = new Link(this.iwrb.getLocalizedString("close","Close"));
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
