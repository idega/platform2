package com.idega.block.reports.presentation;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.AbstractMenuBlock;
import com.idega.presentation.IWContext;
import com.idega.presentation.MenuBlock;
import com.idega.presentation.ui.Parameter;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class ReportViewer extends AbstractMenuBlock implements MenuBlock,Reports{

  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  public static String prmClass = "rep_clss";

  public void main(IWContext iwc){
    this.iwrb = getResourceBundle(iwc);
    this.iwb = getBundle(iwc);
    if(iwc.isParameterSet(PRM_CATEGORYID)) {
		addParameterToMaintain(new Parameter(PRM_CATEGORYID,iwc.getParameter(PRM_CATEGORYID)));
	}
    if(iwc.isParameterSet(PRM_REPORTID)) {
		addParameterToMaintain(new Parameter(PRM_REPORTID,iwc.getParameter(PRM_REPORTID)));
	}
  }

  public void addStandardObjects(){
    addBlockObject(new ReportContentViewer());
    addBlockObject(new ReportSQLEditor());
    addBlockObject(new ReportPDFSetupEditor());
    addBlockObject(new ReportPDFEditor());
    addBlockObject(new ReportPrinter());
  }

  public Class getDefaultBlockClass(){
    return ReportContentViewer.class;
  }

  public String getMenuClassParameterName(){
    return prmClass;
  }

  public static Parameter getMenuStartClassParameter(Class menuStartClass){
    return new Parameter(prmClass,IWMainApplication.getEncryptedClassName(menuStartClass));
  }

  public Parameter getSQLEditorLink(){
    return getMenuLinkParameter(ReportSQLEditor.class);
  }

  public String getBundleIdentifier(){
    return REPORTS_BUNDLE_IDENTIFIER;
  }
}
