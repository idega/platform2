package com.idega.block.datareport.presentation;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jan 21, 2004
 */
public class QueryResultViewerWindow extends com.idega.idegaweb.presentation.StyledIWAdminWindow {

 public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";
  
  public QueryResultViewerWindow() {
    setResizable(true);
    setWidth(1024);
    setHeight(768);
    setScrollbar(true);
  }

  public void main(IWContext iwc) throws Exception {  
    // get resource bundle 
    IWResourceBundle iwrb = getResourceBundle(iwc);
    setTitle(iwrb.getLocalizedString("ro_report_viewer", "ReportGeneratorViewer"));
    addTitle(iwrb.getLocalizedString("ro_report_viewer", "ReportGeneratorViewer"), TITLE_STYLECLASS);
    QueryResultViewer result = new QueryResultViewer();
    add(result);
 }
    
  public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  } 
}
