package com.idega.block.datareport.presentation;

import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 12, 2003
 */
public class ReportOverviewWindow extends IWAdminWindow {
  
  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";
  
  public ReportOverviewWindow() {
    setResizable(true);
    setWidth(800);
    setHeight(600);
    setScrollbar(true);
  }

  public void main(IWContext iwc) {  
    // get resource bundle 
    IWResourceBundle iwrb = getResourceBundle(iwc);
    addTitle(iwrb.getLocalizedString("user_report_layout_chooser", "ReportMaster"), IWConstants.BUILDER_FONT_STYLE_TITLE);
    ReportOverview overview = new ReportOverview();
    add(overview);
    //ReportLayoutChooser layoutChooser = new ReportLayoutChooser();
    //add(layoutChooser);
  }
    
  public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  } 
}
