package com.idega.block.datareport.presentation;

import com.idega.block.dataquery.presentation.QueryBuilder;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.presentation.StyledIWAdminWindow;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 12, 2003
 */
public class ReportOverviewWindow extends StyledIWAdminWindow {
  
  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";
  
  public ReportOverviewWindow() {
    setResizable(true);
    setWidth(900);
    setHeight(500);
    setScrollbar(true);
  }

  public void main(IWContext iwc) throws Exception {  
    // get resource bundle 
    IWResourceBundle iwrb = getResourceBundle(iwc);
    addTitle(iwrb.getLocalizedString("ro_report", "ReportGenerator"), IWConstants.BUILDER_FONT_STYLE_TITLE);
    // decide to show the query builder or the overview
    if (iwc.isParameterSet(QueryBuilder.PARAM_CANCEL)) {
    	// do not show wizard even if the parameter show wizard is set
    	QueryBuilder.cleanSession(iwc);
    	ReportOverview overview = new ReportOverview();
    	add(overview,iwc);
    }
    else if (iwc.isParameterSet(QueryBuilder.PARAM_SAVE)) {
    	QueryBuilder queryBuilder = new QueryBuilder();
    	queryBuilder.main(iwc);
    	// get the jid of the just created new file
    	int queryId = queryBuilder.getQueryId();
    	QueryBuilder.cleanSession(iwc);
    	ReportOverview overview = new ReportOverview();
    	overview.setShowOnlyOneQueryWithId(queryId);
    	add(overview,iwc);
    }
    else if (iwc.isParameterSet(QueryBuilder.SHOW_WIZARD))	{
    	QueryBuilder queryBuilder = new QueryBuilder();
    	add(queryBuilder,iwc);
    }
    else {
	   	ReportOverview overview = new ReportOverview();
    	add(overview,iwc);
    }
  }
    
  public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  } 
}
