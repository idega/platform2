package com.idega.block.datareport.presentation;

import com.idega.block.dataquery.presentation.ReportQueryBuilder;
import com.idega.block.entity.presentation.converter.ButtonConverter;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;

/**
 * <p>
 * Title: idegaWeb
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: idega Software
 * </p>
 * 
 * @author <a href="thomas@idega.is">Thomas Hilbig </a>
 * @version 1.0 Created on Jun 12, 2003
 */
public class ReportOverviewWindow extends StyledIWAdminWindow {

	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";

	public ReportOverviewWindow() {
		setResizable(true);
		setWidth(1024);
		setHeight(768);
		setScrollbar(true);
	}

	public void main(IWContext iwc) throws Exception {
		// get resource bundle
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (!iwc.isLoggedOn()) {
			String userNotLoggedIn = iwrb.getLocalizedString("ro_user_not_logged_in", "Sorry, you are not logged in");
			Text userNotLoggedInText = new Text(userNotLoggedIn);
			userNotLoggedInText.setBold();
			add(userNotLoggedInText, iwc);
			return;
		}
		
		setTitle(iwrb.getLocalizedString("ro_report", "ReportGenerator"));
		addTitle(iwrb.getLocalizedString("ro_report", "ReportGenerator"), TITLE_STYLECLASS);
		if (iwc.isParameterSet(ReportQueryOverview.EDIT_QUERY_EXPERT_MODE_KEY) || iwc.isParameterSet(ReportQueryOverview.EDIT_QUERY_SIMPLE_MODE_KEY) || iwc.isParameterSet(ReportQueryOverview.EDIT_NEW_QUERY)) {
			ReportQueryBuilder.cleanSession(iwc);
		}

		// decide to show the query builder or the overview
		if (iwc.isParameterSet(ReportQueryBuilder.PARAM_CANCEL)) {
			// do not show wizard even if the parameter show wizard is set
			ReportQueryBuilder.cleanSession(iwc);
			ReportQueryOverview overview = new ReportQueryOverview();
			add(overview, iwc);
		}
		else if (iwc.isParameterSet(ReportQueryBuilder.PARAM_SAVE)) {
			ReportQueryBuilder queryBuilder = new ReportQueryBuilder();
			queryBuilder.main(iwc);
			// get the id of the just created new file
			int queryId = queryBuilder.getQueryId();
			ReportQueryBuilder.cleanSession(iwc);
			ReportQueryOverview overview = new ReportQueryOverview();
			overview.setShowOnlyOneQueryWithId(queryId);
			add(overview, iwc);
		}
		else if (iwc.isParameterSet(ReportQueryOverview.UPLOAD_LAYOUT) || iwc.isParameterSet(LayoutUploader.KEY_DELETE_LAYOUT_IS_SUBMITTED) || iwc.isParameterSet(LayoutUploader.KEY_LAYOUT_DOWNLOAD_IS_SUBMITTED)) {
			LayoutUploader layoutUploader = new LayoutUploader();
			add(layoutUploader, iwc);
		}
		else if (iwc.isParameterSet(LayoutUploader.KEY_LAYOUT_UPLOAD_IS_SUBMITTED)) {
			LayoutUploader layoutUploader = new LayoutUploader();
			layoutUploader.main(iwc);
			ReportQueryOverview overview = new ReportQueryOverview();
			add(overview, iwc);
		}
		else if (iwc.isParameterSet(ReportQueryOverview.UPLOAD_QUERY) || iwc.isParameterSet(QueryUploader.KEY_QUERY_DOWNLOAD_IS_SUBMITTED)) {
			QueryUploader queryUploader = new QueryUploader();
			add(queryUploader, iwc);
		}
		else if (iwc.isParameterSet(QueryUploader.KEY_QUERY_UPLOAD_IS_SUBMITTED)) {
			QueryUploader queryUploader = new QueryUploader();
			queryUploader.main(iwc);
			int queryId = queryUploader.getUserQueryId();
			ReportQueryOverview overview = new ReportQueryOverview();
			overview.setShowOnlyOneQueryWithId(queryId);
			add(overview, iwc);
		}
		else if (iwc.isParameterSet(ReportQueryBuilder.SHOW_WIZARD)) {
			ReportQueryBuilder queryBuilder = new ReportQueryBuilder();
			add(queryBuilder, iwc);
		}
		else if (iwc.isParameterSet(ReportQueryOverview.DELETE_ITEMS_KEY)) {
			ReportQueryOverview overview = new ReportQueryOverview();
			add(overview, iwc);
		}
		else if (iwc.isParameterSet(QueryResultViewer.EXECUTE_QUERY_KEY) || ButtonConverter.getResultByParsing(iwc).isValid()) {
			addTitle(iwrb.getLocalizedString("ro_report_viewer", "ReportGeneratorViewer"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			QueryResultViewer result = new QueryResultViewer();
			add(result, iwc);
		}
		else {
			ReportQueryOverview overview = new ReportQueryOverview();
			add(overview, iwc);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}