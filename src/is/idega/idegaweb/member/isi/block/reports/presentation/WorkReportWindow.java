package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import java.rmi.RemoteException;
import java.util.List;

import com.idega.block.datareport.presentation.ReportGenerator;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.LinkContainer;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.presentation.UserWorkReportWindow;
import com.idega.util.IWTimestamp;
/**
 * This window is used to work with a clubs work reports.
 * Copyright : Idega Software 2003
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */

public class WorkReportWindow extends StyledIWAdminWindow implements UserWorkReportWindow {
	private static final String STATS_LOCALIZABLE_KEY_NAME = "STATS_LOCALIZABLE_KEY_NAME";
	public static final String STATS_LAYOUT_PREFIX = "STATS_LAYOUT_PREFIX";
	private static final String STATS_LAYOUT_PARAM = "STATS_LAYOUT_PARAM";
	private static final String STATS_INVOCATION_PARAM = "STATS_INVOCATION_PARAM";
	private static final String STATS_INVOCATION_PREFIX = "stats_invocation_xml_file_id_";
	private static final String STATS_LAYOUT_NAME_FROM_BUNDLE = "STATS_LAYOUT_NAME_FROM_BUNDLE";
	private static final String STATS_INVOCATION_NAME_FROM_BUNDLE = "STATS_INVOCATION_NAME_FROM_BUNDLE";
	
	private MemberUserBusiness memBiz;
	private GroupBusiness groupBiz;
	private UserBusiness userBiz;
	private String userType = null;
	private WorkReportBusiness workBiz = null;
	private String styledLink = "styledLinkGeneral";
	private String borderRightTable = "borderRight";
	private String borderTable = "borderAll";
	private boolean selectorIsSet = false;

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	public static final String PARAMETER_CLUB_ID = "iwme_wr_club_id";

	protected static final String ACTION = "iwme_wr_act";

	protected static final String ACTION_WORK_SELECT_REPORT = "iwme_wr_act_b1";
	protected static final String ACTION_EDIT_MEMBER_LIST = "iwme_wr_act_b2";
	protected static final String ACTION_EDIT_ACCOUNT = "iwme_wr_act_b3";
	protected static final String ACTION_EDIT_BOARD = "iwme_wr_act_b4";
	protected static final String ACTION_EDIT_DIVISION_BOARD = "iwme_wr_act_b4_1";
	protected static final String ACTION_SEND_REPORT = "iwme_wr_act_b5"; //b6 is useless
	protected static final String ACTION_IMPORT_MEMBERS = "iwme_wr_act_b7";
	protected static final String ACTION_IMPORT_ACCOUNT = "iwme_wr_act_b8";
	protected static final String ACTION_IMPORT_BOARD = "iwme_wr_act_b9";
	protected static final String ACTION_REPORT_OVERVIEW = "iwme_wr_act_b10";
	protected static final String ACTION_REPORT_OVERVIEW_CLOSE_VIEW = "iwme_wr_act_b10.2";
	protected static final String ACTION_CLOSE_REPORT = "iwme_wr_act_b11";
	protected static final String ACTION_STATISTICS = "iwme_wr_act_b12";
	protected static final String ACTION_CREATE_REPORTS = "iwme_wr_act_b13";
	protected static final String ACTION_GET_REPORTS = "iwme_wr_act_b13_2";

	protected static final String COLOR_DARKEST = "#9F9F9F";
	protected static final String COLOR_MIDDLE = "#DFDFDF";
	protected static final String COLOR_LIGHTEST = "#EFEFEF";
	
	private String helpTextKey = "";

	private IWResourceBundle iwrb;
	private IWBundle iwb;
	
	public WorkReportWindow() {
		setHeight(600);
		setWidth(800);
		setResizable(true);
		setScrollbar(true);

	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		iwrb = getResourceBundle(iwc);
		iwb = getBundle(iwc);
		
		workBiz = getWorkReportBusiness(iwc);

		//sets the type of user making or viewing the reports. union staff, regional union staff, league staff, federation staff or club staff
		//and then gets the primary key of the correct group
		Integer groupId = setUserTypeAndReturnGroupId(iwc);

		int year = new IWTimestamp(IWTimestamp.getTimestampRightNow()).getYear();
		String paramWorkReportYear = (String) iwc.getParameter(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_YEAR);
		
		if(paramWorkReportYear==null) {
			paramWorkReportYear = (String) iwc.getSessionAttribute(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_YEAR);
		}
		
		if(paramWorkReportYear!=null && !paramWorkReportYear.equals("")) {
			year  = Integer.parseInt(paramWorkReportYear);
		}
		
		setTitle(iwrb.getLocalizedString("workreportwindow.title", "Work Reports"));
		
		
		String action = iwc.getParameter(ACTION);

		Table table = new Table(2, 1);
		table.setWidthAndHeightToHundredPercent();
		table.setColumnColor(1, COLOR_LIGHTEST);
		table.setColumnWidth(1, "240");
		table.setRowHeight(1, Table.HUNDRED_PERCENT);
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);
		table.setCellpaddingAndCellspacing(0);
		table.mergeCells(1,1,1,2);
		table.setStyleClass(1,1,borderRightTable);
		table.setStyleClass(borderTable);

		//add left menu of links
		Table menuTable = getMenuTable(iwc);
		
		table.add(menuTable, 1, 1);

		add(table,iwc);

		//add the main content
		if (action != null) {			
			WorkReportSelector selector = null;
			

			//depending on the user type set the currect stuff
			if (action.equals(ACTION_WORK_SELECT_REPORT)) {
				selector = new WorkReportSelector();
				selectorIsSet = true;
				helpTextKey = ACTION_WORK_SELECT_REPORT + "_help";
				this.addTitle(iwrb.getLocalizedString(ACTION_WORK_SELECT_REPORT, "Select report"));
				this.addTitle(iwrb.getLocalizedString(ACTION_WORK_SELECT_REPORT, "Select report"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			if (action.equals(ACTION_EDIT_MEMBER_LIST)) {
				selector = new WorkReportMemberEditor();
				selectorIsSet = true;
				helpTextKey = ACTION_EDIT_MEMBER_LIST + "_help";
				this.addTitle(iwrb.getLocalizedString(ACTION_EDIT_MEMBER_LIST, "Edit members"));
				this.addTitle(iwrb.getLocalizedString(ACTION_EDIT_MEMBER_LIST, "Edit members"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			else if (action.equals(ACTION_EDIT_ACCOUNT)) {
				selector = new WorkReportAccountEditor();
				selectorIsSet = true;
				helpTextKey = ACTION_EDIT_ACCOUNT + "_help";
				this.addTitle(iwrb.getLocalizedString(ACTION_EDIT_ACCOUNT, "Edit account info"));
				this.addTitle(iwrb.getLocalizedString(ACTION_EDIT_ACCOUNT, "Edit account info"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			else if (action.equals(ACTION_EDIT_BOARD)) {
				selector = new WorkReportBoardMemberEditor();
				selectorIsSet = true;
				helpTextKey = ACTION_EDIT_BOARD + "_help";
				this.addTitle(iwrb.getLocalizedString(ACTION_EDIT_BOARD, "Edit board info"));
				this.addTitle(iwrb.getLocalizedString(ACTION_EDIT_BOARD, "Edit board info"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			else if (action.equals(ACTION_EDIT_DIVISION_BOARD)) {
				selector = new WorkReportDivisionBoardEditor();
				selectorIsSet = true;
				helpTextKey = ACTION_EDIT_DIVISION_BOARD + "_help";
				this.addTitle(iwrb.getLocalizedString(ACTION_EDIT_DIVISION_BOARD, "Edit division info"));
				this.addTitle(iwrb.getLocalizedString(ACTION_EDIT_DIVISION_BOARD, "Edit division info"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			else if (action.equals(ACTION_SEND_REPORT)) {
				selector = new WorkReportSender();
				selectorIsSet = true;
				helpTextKey = ACTION_SEND_REPORT + "_help";
				this.addTitle(iwrb.getLocalizedString(ACTION_SEND_REPORT, "Send report"));
				this.addTitle(iwrb.getLocalizedString(ACTION_SEND_REPORT, "Send report"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			else if (action.equals(ACTION_IMPORT_MEMBERS)) {
				selector = new WorkReportMemberImporterConfirm();
				selectorIsSet = true;
				helpTextKey = ACTION_IMPORT_MEMBERS + "_help";
				this.addTitle(iwrb.getLocalizedString(ACTION_IMPORT_MEMBERS, "Import members"));
				this.addTitle(iwrb.getLocalizedString(ACTION_IMPORT_MEMBERS, "Import members"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			else if (action.equals(ACTION_IMPORT_ACCOUNT)) {
				selector = new WorkReportAccountImporter();
				selectorIsSet = true;
				helpTextKey = ACTION_IMPORT_ACCOUNT + "_help";
				this.addTitle(iwrb.getLocalizedString(ACTION_IMPORT_ACCOUNT, "Import account info"));
				this.addTitle(iwrb.getLocalizedString(ACTION_IMPORT_ACCOUNT, "Import account info"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			else if (action.equals(ACTION_IMPORT_BOARD)) {
				selector = new WorkReportBoardImporter();
				selectorIsSet = true;
				helpTextKey = ACTION_IMPORT_BOARD + "_help";
				this.addTitle(iwrb.getLocalizedString(ACTION_IMPORT_BOARD, "Import board info"));
				this.addTitle(iwrb.getLocalizedString(ACTION_IMPORT_BOARD, "Import board info"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			else if (action.equals(ACTION_REPORT_OVERVIEW)) {
				Form yearForm = getYearSelectionForm(iwc,year);
				yearForm.maintainParameter(ACTION);
				table.add(yearForm,2,1);
				WorkReportOverView overView = new WorkReportOverView();
				overView.setYear(year);
				table.add(overView,2,1);	//not a selector
				WorkReportOverViewStats stats = new WorkReportOverViewStats();
				stats.setYear(year);
				menuTable.add(stats,1,15);
				helpTextKey = ACTION_REPORT_OVERVIEW + "_help";
				selectorIsSet = true;
				this.addTitle(iwrb.getLocalizedString(ACTION_REPORT_OVERVIEW, "Review work report"));
				this.addTitle(iwrb.getLocalizedString(ACTION_REPORT_OVERVIEW, "Review work report"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			else if( action.equals(ACTION_REPORT_OVERVIEW_CLOSE_VIEW)) {
				Form yearForm = getYearSelectionForm(iwc,year);
				yearForm.maintainParameter(ACTION);
				yearForm.maintainParameter(WorkReportOverViewCloseView.CLOSE_VIEW_WORK_REPORT_ID);
				table.add(yearForm,2,1);
				WorkReportOverViewCloseView closeView = new WorkReportOverViewCloseView();
				closeView.setYear(year);
				table.add(closeView,2,1);	//not a selector
				WorkReportOverViewStats stats = new WorkReportOverViewStats();
				stats.setYear(year);
				menuTable.add(stats,1,15);
				helpTextKey = ACTION_REPORT_OVERVIEW_CLOSE_VIEW + "_help";
				selectorIsSet = true;
				this.addTitle(iwrb.getLocalizedString(ACTION_REPORT_OVERVIEW, "Review work report"));
				this.addTitle(iwrb.getLocalizedString(ACTION_REPORT_OVERVIEW, "Review work report"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			else if (action.equals(ACTION_CLOSE_REPORT)) {
				table.add(new WorkReportCloser(),2,1);	//not a selector
				helpTextKey = ACTION_CLOSE_REPORT + "_help";
				selectorIsSet = true;
				this.addTitle(iwrb.getLocalizedString(ACTION_CLOSE_REPORT, "Close work report"));
				this.addTitle(iwrb.getLocalizedString(ACTION_CLOSE_REPORT, "Close work report"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			else if (action.equals(ACTION_STATISTICS)) {
				ReportGenerator repGen = new ReportGenerator();
				repGen.setParameterToMaintain(ACTION);
				repGen.setParameterToMaintain(STATS_INVOCATION_PARAM);
				repGen.setParameterToMaintain(STATS_LAYOUT_PARAM);
				repGen.setParameterToMaintain(STATS_LAYOUT_NAME_FROM_BUNDLE);
				repGen.setParameterToMaintain(STATS_INVOCATION_NAME_FROM_BUNDLE);
				repGen.setParameterToMaintain(STATS_LOCALIZABLE_KEY_NAME);
				
				String invocationKey = iwc.getParameter(STATS_INVOCATION_PARAM);
				String invocationFileName = iwc.getParameter(STATS_INVOCATION_NAME_FROM_BUNDLE);
				String layoutKey = iwc.getParameter(STATS_LAYOUT_PARAM);
				String layoutFileName = iwc.getParameter(STATS_LAYOUT_NAME_FROM_BUNDLE);
				String localizedNameKey = iwc.getParameter(STATS_LOCALIZABLE_KEY_NAME);
				
				if( (invocationKey!=null && iwb.getProperty(invocationKey,"-1")!=null) || invocationFileName!=null ){
					
					if(invocationFileName!=null){
						repGen.setMethodInvocationBundleAndFileName(iwb,invocationFileName);
					}
					else{
						Integer invocationICFileID = new Integer(iwb.getProperty(invocationKey));
					
						if(invocationICFileID.intValue()>0){
							repGen.setMethodInvocationICFileID(invocationICFileID);
						}
					}
					
					if(layoutFileName!=null){
						repGen.setLayoutBundleAndFileName(iwb,layoutFileName);
					}
					else if(layoutKey!=null && iwb.getProperty(layoutKey,"-1")!=null ){
						Integer layoutICFileID = new Integer(iwb.getProperty(layoutKey));
						if(layoutICFileID.intValue()>0)
						repGen.setLayoutICFileID(layoutICFileID);
					}
					
					if(localizedNameKey!=null){
						String reportName = iwrb.getLocalizedString(localizedNameKey);
						repGen.setReportName(reportName);
						
						table.add(formatHeadline(reportName),2,1);	//not a selector
						table.addBreak(2,1);
					}
				}
				
				table.add(repGen,2,1);	//not a selector
				selectorIsSet = true;
				this.addTitle(iwrb.getLocalizedString(ACTION_STATISTICS, "View statistics"));
				this.addTitle(iwrb.getLocalizedString(ACTION_STATISTICS, "View statistics"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}
			else if (action.equals(ACTION_CREATE_REPORTS)) {
				table.add(new WorkReportExporter(),2,1);	//not a selector
				selectorIsSet = true;
				addTitle(iwrb.getLocalizedString(ACTION_CREATE_REPORTS,"Generate reports"));
				addTitle(iwrb.getLocalizedString(ACTION_CREATE_REPORTS,"Generate reports"), IWConstants.BUILDER_FONT_STYLE_TITLE);
			}

			if (selector != null) {
				if (groupId != null) {
					if (WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION.equals(getUserType())) {
						selector.setRegionalUnionId(groupId.intValue());
					}
					selector.setClubId(groupId.intValue());
				}
				selector.setUserType(getUserType());
				table.add(selector, 2, 1);
			}
			if(!action.equals(ACTION_STATISTICS)) {
				Table helpTable =new Table(1,1);
				helpTable.setWidth(Table.HUNDRED_PERCENT);
				helpTable.setHeight(15);
				helpTable.setAlignment(1,1,"right");
				helpTable.add(getHelpWithGrayImage(helpTextKey,false),1,1);
				table.add(helpTable,2,1);
			}
			
				
		}
		if(!selectorIsSet) {
			addTitle(iwrb.getLocalizedString("workreportwindow.title", "Work Reports"), IWConstants.BUILDER_FONT_STYLE_TITLE);		
		}
		
	}

	private Form getYearSelectionForm(IWContext iwc, int year) throws RemoteException {
		Form form = new Form();
		Table table = new Table(2,1);
		table.setColor(this.COLOR_MIDDLE);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setAlignment(1,1,Table.HORIZONTAL_ALIGN_LEFT);
		table.setAlignment(2,1,Table.HORIZONTAL_ALIGN_RIGHT);
		
		Text yearText = new Text(Integer.toString(year),true,false,false);
		table.add(yearText,1,1);
		
		DropdownMenu yearSelector = getWorkReportBusiness(iwc).getYearDropdownMenu(year);
		yearSelector.setToSubmit();
		table.add(yearSelector,2,1);
		
		form.add(table);
		
	
		return form;
	}

	//searches the current users top nodes to figure out who he is. 
	private Integer setUserTypeAndReturnGroupId(IWContext iwc) {
		User user = iwc.getCurrentUser();

		try {
			
			List federation = getWorkReportBusiness(iwc).getFederationListForUserFromTopNodes(user, iwc); //should only be one
			if (!federation.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_FEDERATION;
				return null;
			}
			
			List union = getWorkReportBusiness(iwc).getUnionListForUserFromTopNodes(user, iwc); //should only be one
			if (!union.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_UNION;
				return null;
			}

			List regional = getWorkReportBusiness(iwc).getRegionalUnionListForUserFromTopNodes(user, iwc); //should only be one
			if (!regional.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION;
				return ((Integer) ((Group)regional.iterator().next()).getPrimaryKey());
			}

			List leagues = getWorkReportBusiness(iwc).getLeaguesListForUserFromTopNodes(user, iwc); //should only be one
			if (!leagues.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_LEAGUE;
				return ((Integer) ((Group)leagues.iterator().next()).getPrimaryKey());
			}
			
			List club = getWorkReportBusiness(iwc).getClubListForUserFromTopNodes(user, iwc); //should only be one
			if (!club.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_CLUB;
				return ((Integer) ((Group)club.iterator().next()).getPrimaryKey());
			}

		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;

	}

	private String getUserType() {
		return userType;
	}

	private Table getMenuTable(IWContext iwc) {
		String type = getUserType();

		Table menu = new Table(2, 15);
		menu.setWidth(Table.HUNDRED_PERCENT);
		menu.setCellpadding(3);
		menu.setCellspacing(0);

		Text operations = formatHeadline(iwrb.getLocalizedString("workreportwindow.operations", "Operations"));

		//B.1
		LinkContainer selectReport = new LinkContainer();
		//added for a styled link font:
		selectReport.setStyleClass(styledLink);
		selectReport.add(formatText(iwrb.getLocalizedString("workreportwindow.select_report", "Select report"), true));
		selectReport.addParameter(ACTION, ACTION_WORK_SELECT_REPORT);
		selectReport.addParameter(WorkReportConstants.WR_SESSION_CLEAR, "TRUE");

		//B.2
		Text workOnReport = formatText(iwrb.getLocalizedString("workreportwindow.edit_report", "Edit report"), true);
		Lists editList = new Lists();

		if ((!WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION.equals(type) && !WorkReportConstants.WR_USER_TYPE_UNION.equals(type) && !WorkReportConstants.WR_USER_TYPE_LEAGUE.equals(type))  || iwc.isSuperAdmin()) {
			LinkContainer editMemberList = new LinkContainer();
			editMemberList.setStyleClass(styledLink);
			editMemberList.add(formatText(iwrb.getLocalizedString("workreportwindow.edit_members", "Edit member list")));
			editMemberList.addParameter(ACTION, ACTION_EDIT_MEMBER_LIST);
			editList.add(editMemberList);
		}

		//B.3
		LinkContainer editAccountList = new LinkContainer();
		editAccountList.setStyleClass(styledLink);
		editAccountList.add(formatText(iwrb.getLocalizedString("workreportwindow.edit_account", "Edit account info")));
		editAccountList.addParameter(ACTION, ACTION_EDIT_ACCOUNT);
		editList.add(editAccountList);
		//B.4
		LinkContainer editBoardList = new LinkContainer();
		editBoardList.setStyleClass(styledLink);
		editBoardList.add(formatText(iwrb.getLocalizedString("workreportwindow.edit_board", "Edit board info")));
		editBoardList.addParameter(ACTION, ACTION_EDIT_BOARD);
		editList.add(editBoardList);
		//B.4_1
		LinkContainer editDivisionBoardList = new LinkContainer();
		editDivisionBoardList.setStyleClass(styledLink);
		editDivisionBoardList.add(formatText(iwrb.getLocalizedString("workreportwindow.division_edit_board", "Edit division board info")));
		editDivisionBoardList.addParameter(ACTION, ACTION_EDIT_DIVISION_BOARD);
		editList.add(editDivisionBoardList);

		//
		//B.5		
		LinkContainer sendReport = new LinkContainer();
		sendReport.setStyleClass(styledLink);
		sendReport.add(formatText(iwrb.getLocalizedString("workreportwindow.send_report", "Send report"), true));
		sendReport.addParameter(ACTION, ACTION_SEND_REPORT);

		//
		Lists uploadList = new Lists();
		Text uploadReport = formatText(iwrb.getLocalizedString("workreportwindow.excel_imports", "Excel imports"), true);

		//B.7
		//if (WorkReportConstants.WR_USER_TYPE_FEDERATION.equals(type) || iwc.isSuperAdmin()) {	
		//if (WorkReportConstants.WR_USER_TYPE_FEDERATION.equals(type) || iwc.isSuperAdmin()) {
		
			LinkContainer importMemberList = new LinkContainer();
			importMemberList.setStyleClass(styledLink);
			importMemberList.add(formatText(iwrb.getLocalizedString("workreportwindow.import_members", "Import member list")));
			importMemberList.addParameter(ACTION, ACTION_IMPORT_MEMBERS);
			uploadList.add(importMemberList);
		//}
		
		//B.8
		LinkContainer importAccount = new LinkContainer();
		importAccount.setStyleClass(styledLink);
		importAccount.add(formatText(iwrb.getLocalizedString("workreportwindow.import_account", "Import account info")));
		importAccount.addParameter(ACTION, ACTION_IMPORT_ACCOUNT);
		uploadList.add(importAccount);
		//B.9
		LinkContainer importBoard = new LinkContainer();
		importBoard.setStyleClass(styledLink);
		importBoard.add(formatText(iwrb.getLocalizedString("workreportwindow.import_board", "Import board info")));
		importBoard.addParameter(ACTION, ACTION_IMPORT_BOARD);
		uploadList.add(importBoard);

		//B.10
		LinkContainer reportsOverview = new LinkContainer();
		reportsOverview.setStyleClass(styledLink);
		reportsOverview.add(formatText(iwrb.getLocalizedString("workreportwindow.report_overview", "Reports overview"), true));
		reportsOverview.addParameter(ACTION, ACTION_REPORT_OVERVIEW);

		//B.11
		LinkContainer closeReport = new LinkContainer();
		closeReport.setStyleClass(styledLink);
		closeReport.add(formatText(iwrb.getLocalizedString("workreportwindow.close_report", "Close report"), true));
		closeReport.addParameter(ACTION, ACTION_CLOSE_REPORT);

		//B.12
		Text statistics = formatHeadline(iwrb.getLocalizedString("workreportwindow.statistics", "Statistics"));

		Table stats = new Table(2,14);
		stats.setColumnWidth(1,"20");
		stats.mergeCells(1,1,2,1);
		stats.mergeCells(1,3,2,3);
		stats.mergeCells(1,5,2,5);
		stats.mergeCells(1,7,2,7);
		stats.mergeCells(1,9,2,9);
		stats.mergeCells(1,11,2,11);
		stats.mergeCells(1,13,2,13);
		
		stats.add(formatText(iwrb.getLocalizedString("workreportwindow.leagues", "Leagues")),1,1);
		
		
		LinkContainer b12_1_1 = new LinkContainer();
		b12_1_1.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_1_1_reportname", "Players per club -/+ 16"), false));
		b12_1_1.addParameter(ACTION, ACTION_STATISTICS);
		b12_1_1.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE, "Invocation-B12.1.1.xml");
		b12_1_1.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.1.1.xml");
		b12_1_1.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_1_1_reportname");
		b12_1_1.setStyleClass(styledLink);
		
		stats.add(b12_1_1,2,2);
		stats.addBreak(2,2);
		
		if(!WorkReportConstants.WR_USER_TYPE_CLUB.equals(getUserType()) && !WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION.equals(getUserType())){
			LinkContainer b12_1_2 = new LinkContainer();
			b12_1_2.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_1_2_reportname", "Players per reg.uni. -/+ 16"), false));
			b12_1_2.addParameter(ACTION, ACTION_STATISTICS);
			b12_1_2.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE, "Invocation-B12.1.2.xml");
			b12_1_2.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.1.2.xml");
			b12_1_2.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_1_2_reportname");
			b12_1_2.setStyleClass(styledLink);
			
			stats.add(b12_1_2,2,2);
			stats.addBreak(2,2);
			
			LinkContainer b12_1_3 = new LinkContainer();
			b12_1_3.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_1_3_reportname", "Players per league -/+ 16"), false));
			b12_1_3.addParameter(ACTION, ACTION_STATISTICS);
			b12_1_3.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE, "Invocation-B12.1.3.xml");
			b12_1_3.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.1.3.xml");
			b12_1_3.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_1_3_reportname");
			b12_1_3.setStyleClass(styledLink);
			
			stats.add(b12_1_3,2,2);
			stats.addBreak(2,2);
			
			LinkContainer b12_1_4 = new LinkContainer();
			b12_1_4.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_1_4_reportname", "Compare player stats with previous year"), false));
			b12_1_4.addParameter(ACTION, ACTION_STATISTICS);
			b12_1_4.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE, "Invocation-B12.1.4.xml");
			b12_1_4.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.1.4.xml");
			b12_1_4.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_1_4_reportname");
			b12_1_4.setStyleClass(styledLink);
			
			stats.add(b12_1_4,2,2);
			stats.addBreak(2,2);
			
			LinkContainer b12_1_5 = new LinkContainer();
			b12_1_5.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_1_5_reportname", "Cost per player"), false));
			b12_1_5.addParameter(ACTION, ACTION_STATISTICS);
			b12_1_5.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.1.5.xml");
			b12_1_5.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.1.5.xml");
			b12_1_5.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_1_5_reportname");
			b12_1_5.setStyleClass(styledLink);
			
			stats.add(b12_1_5,2,2);
			stats.addBreak(2,2);
		}
			
		if(!WorkReportConstants.WR_USER_TYPE_CLUB.equals(getUserType()) && !WorkReportConstants.WR_USER_TYPE_LEAGUE.equals(getUserType())){
			stats.add(formatText(iwrb.getLocalizedString("workreportwindow.regional_unions", "Regional unions")),1,3);
		
		
			LinkContainer b12_2_1 = new LinkContainer();
			b12_2_1.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_2_1_reportname", "Members per reg.uni."), false));
			b12_2_1.addParameter(ACTION, ACTION_STATISTICS);
			b12_2_1.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE, "Invocation-B12.2.1.xml");
			b12_2_1.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.2.1.xml");
			b12_2_1.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_2_1_reportname");
			b12_2_1.setStyleClass(styledLink);
			
			stats.add(b12_2_1,2,4);
			stats.addBreak(2,4);
			
			LinkContainer b12_2_2 = new LinkContainer();
			b12_2_2.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_2_2_reportname", "Members per reg.uni. -/+ 16"), false));
			b12_2_2.addParameter(ACTION, ACTION_STATISTICS);
			b12_2_2.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE, "Invocation-B12.2.2.xml");
			b12_2_2.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.2.2.xml");
			b12_2_2.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_2_2_reportname");
			b12_2_2.setStyleClass(styledLink);
			
			stats.add(b12_2_2,2,4);
			stats.addBreak(2,4);
	
			LinkContainer b12_2_3 = new LinkContainer();
			b12_2_3.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_2_3_reportname", "Players per club -/+ 16"), false));
			b12_2_3.addParameter(ACTION, ACTION_STATISTICS);
			b12_2_3.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE, "Invocation-B12.2.3.xml");
			b12_2_3.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.2.3.xml");
			b12_2_3.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_2_3_reportname");
			b12_2_3.setStyleClass(styledLink);
			
			stats.add(b12_2_3,2,4);
			stats.addBreak(2,4);
			
	
			LinkContainer b12_2_4 = new LinkContainer();
			b12_2_4.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_2_4_reportname", "Members per club -/+ 16"), false));
			b12_2_4.addParameter(ACTION, ACTION_STATISTICS);
			b12_2_4.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE, "Invocation-B12.2.4.xml");
			b12_2_4.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.2.4.xml");
			b12_2_4.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_2_4_reportname");
			b12_2_4.setStyleClass(styledLink);
			
			stats.add(b12_2_4,2,4);
			stats.addBreak(2,4);
			
			LinkContainer b12_2_5 = new LinkContainer();
			b12_2_5.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_2_5_reportname", "Players per reg.uni. -/+ 16"), false));
			b12_2_5.addParameter(ACTION, ACTION_STATISTICS);
			b12_2_5.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE, "Invocation-B12.2.5.xml");
			b12_2_5.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.2.5.xml");
			b12_2_5.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_2_5_reportname");
			b12_2_5.setStyleClass(styledLink);
			
			stats.add(b12_2_5,2,4);
			stats.addBreak(2,4);
			
			LinkContainer b12_2_6 = new LinkContainer();
			b12_2_6.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_2_6_reportname", "Players per league -/+ 16"), false));
			b12_2_6.addParameter(ACTION, ACTION_STATISTICS);
			b12_2_6.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE, "Invocation-B12.2.6.xml");
			b12_2_6.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.2.6.xml");
			b12_2_6.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_2_6_reportname");
			b12_2_6.setStyleClass(styledLink);
			
			stats.add(b12_2_6,2,4);
			stats.addBreak(2,4);
		
		
			LinkContainer b12_2_7 = new LinkContainer();
			b12_2_7.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_2_7_reportname", "Members per reg.uni. and type"), false));
			b12_2_7.addParameter(ACTION, ACTION_STATISTICS);
			b12_2_7.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.2.7.xml");
			b12_2_7.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.2.7.xml");
			b12_2_7.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_2_7_reportname");
			b12_2_7.setStyleClass(styledLink);
			
			stats.add(b12_2_7,2,4);
			stats.addBreak(2,4);
		
			LinkContainer b12_2_8 = new LinkContainer();
			b12_2_8.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_2_8_reportname", "Players per reg.uni. and type"), false));
			b12_2_8.addParameter(ACTION, ACTION_STATISTICS);
			b12_2_8.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.2.8.xml");
			b12_2_8.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.2.8.xml");
			b12_2_8.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_2_8_reportname");
			b12_2_8.setStyleClass(styledLink);
	
			stats.add(b12_2_8,2,4);
			stats.addBreak(2,4);
			
		}

		stats.add(formatText(iwrb.getLocalizedString("workreportwindow.clubs", "Clubs")),1,5);
		
		if(!WorkReportConstants.WR_USER_TYPE_LEAGUE.equals(getUserType())){
			
			LinkContainer b12_3_1 = new LinkContainer();
			b12_3_1.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_3_1_reportname", "Members -/+ 16 by type"), false));
			b12_3_1.addParameter(ACTION, ACTION_STATISTICS);
			b12_3_1.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.3.1.xml");
			b12_3_1.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.3.1.xml");
			b12_3_1.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_3_1_reportname");
			b12_3_1.setStyleClass(styledLink);
	
			stats.add(b12_3_1,2,6);
			stats.addBreak(2,6);
		}
		
		LinkContainer b12_3_2 = new LinkContainer();
		b12_3_2.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_3_2_reportname", "Players -/+ 16 by type"), false));
		b12_3_2.addParameter(ACTION, ACTION_STATISTICS);
		b12_3_2.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.3.2.xml");
		b12_3_2.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.3.2.xml");
		b12_3_2.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_3_2_reportname");
		b12_3_2.setStyleClass(styledLink);

		stats.add(b12_3_2,2,6);
		
		
		stats.add(formatText(iwrb.getLocalizedString("workreportwindow.accounts", "Accounts")),1,7);
		
		LinkContainer b12_4_2 = new LinkContainer();
		b12_4_2.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_4_2_reportname", "Summary"), false));
		b12_4_2.addParameter(ACTION, ACTION_STATISTICS);
		b12_4_2.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.4.2.xml");
		b12_4_2.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.4.2.xml");
		b12_4_2.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_4_2_reportname");
		b12_4_2.setStyleClass(styledLink);

		stats.add(b12_4_2,2,8);
		//stats.addBreak(2,8);
		if(!WorkReportConstants.WR_USER_TYPE_LEAGUE.equals(getUserType())){	
			stats.add(formatText(iwrb.getLocalizedString("workreportwindow.reports_list", "Reports list")),1,9);
		
			if(!WorkReportConstants.WR_USER_TYPE_CLUB.equals(getUserType())){
				LinkContainer b12_5_1 = new LinkContainer();
				b12_5_1.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_5_1_reportname", "League and reg.uni."), false));
				b12_5_1.addParameter(ACTION, ACTION_STATISTICS);
				b12_5_1.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.5.1.xml");
				b12_5_1.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.5.1.xml");
				b12_5_1.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_5_1_reportname");
				b12_5_1.setStyleClass(styledLink);
		
				stats.add(b12_5_1,2,10);
				stats.addBreak(2,10);
			}
			
		
			LinkContainer b12_5_2 = new LinkContainer();
			b12_5_2.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_5_2_reportname", "Clubs split"), false));
			b12_5_2.addParameter(ACTION, ACTION_STATISTICS);
			b12_5_2.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.5.2.xml");
			b12_5_2.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.5.2.xml");
			b12_5_2.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_5_2_reportname");
			b12_5_2.setStyleClass(styledLink);
	
			stats.add(b12_5_2,2,10);
			stats.addBreak(2,10);
			
			
			if(!WorkReportConstants.WR_USER_TYPE_CLUB.equals(getUserType())){
				LinkContainer b12_5_3 = new LinkContainer();
				b12_5_3.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_5_3_reportname", "Clubs comparison"), false));
				b12_5_3.addParameter(ACTION, ACTION_STATISTICS);
				b12_5_3.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.5.3.xml");
				b12_5_3.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.5.3.xml");
				b12_5_3.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_5_3_reportname");
				b12_5_3.setStyleClass(styledLink);
		
				stats.add(b12_5_3,2,10);
				stats.addBreak(2,10);
				
				LinkContainer b12_5_4 = new LinkContainer();
				b12_5_4.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_5_4_reportname", "Regional unions"), false));
				b12_5_4.addParameter(ACTION, ACTION_STATISTICS);
				b12_5_4.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.5.4.xml");
		  	b12_5_4.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.5.4.xml");
				b12_5_4.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_5_4_reportname");
				b12_5_4.setStyleClass(styledLink);
		
				stats.add(b12_5_4,2,10);
				stats.addBreak(2,10);
				
				LinkContainer b12_5_5 = new LinkContainer();
				b12_5_5.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_5_5_reportname", "Regional union, comparison"), false));
				b12_5_5.addParameter(ACTION, ACTION_STATISTICS);
				b12_5_5.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.5.5.xml");
				b12_5_5.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.5.5.xml");
				b12_5_5.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_5_5_reportname");
				b12_5_5.setStyleClass(styledLink);
		
				stats.add(b12_5_5,2,10);
				
			
				stats.addBreak(2,10);
			}
		
		}
		
		if(!WorkReportConstants.WR_USER_TYPE_CLUB.equals(getUserType())){
			stats.add(formatText(iwrb.getLocalizedString("workreportwindow.misc_statistics", "Misc Statistics")),1,11);
		
			if(!WorkReportConstants.WR_USER_TYPE_LEAGUE.equals(getUserType())){
				LinkContainer b12_6_1 = new LinkContainer();
				b12_6_1.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_6_1_reportname", "Regional union, age statistics"), false));
				b12_6_1.addParameter(ACTION, ACTION_STATISTICS);
				b12_6_1.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.6.1.xml");
				b12_6_1.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.6.1.xml");
				b12_6_1.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_6_1_reportname");
				b12_6_1.setStyleClass(styledLink);
			
	
				stats.add(b12_6_1,2,12);	
				stats.addBreak(2,12);	
			}
			
			if(!WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION.equals(getUserType())){
				LinkContainer b12_6_2 = new LinkContainer();
				b12_6_2.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_6_2_reportname", "League, age statistics"), false));
				b12_6_2.addParameter(ACTION, ACTION_STATISTICS);
				b12_6_2.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.6.2.xml");
				b12_6_2.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.6.2.xml");
				b12_6_2.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_6_2_reportname");
				b12_6_2.setStyleClass(styledLink);
		
				stats.add(b12_6_2,2,12);	
				stats.addBreak(2,12);
			}
			
			if(!WorkReportConstants.WR_USER_TYPE_LEAGUE.equals(getUserType())){
				LinkContainer b12_6_3 = new LinkContainer();
				b12_6_3.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_6_3_reportname", "Regional union"), false));
				b12_6_3.addParameter(ACTION, ACTION_STATISTICS);
				b12_6_3.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.6.3.xml");
				b12_6_3.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.6.3.xml");
				b12_6_3.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_6_3_reportname");
				b12_6_3.setStyleClass(styledLink);
		
				stats.add(b12_6_3,2,12);	
				stats.addBreak(2,12);
			}
			
			if(!WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION.equals(getUserType())){
				LinkContainer b12_6_4 = new LinkContainer();
				b12_6_4.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_6_4_reportname", "League"), false));
				b12_6_4.addParameter(ACTION, ACTION_STATISTICS);
				b12_6_4.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.6.4.xml");
				b12_6_4.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.6.4.xml");
				b12_6_4.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_6_4_reportname");
				b12_6_4.setStyleClass(styledLink);
				
				stats.add(b12_6_4,2,12);	
				stats.addBreak(2,12);
			}
		}
		
		
		
		stats.add(formatText(iwrb.getLocalizedString("workreportwindow.misc_league", "Misc League")),1,13);
		
		LinkContainer b12_7_1 = new LinkContainer();
		b12_7_1.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_7_1_reportname", "Players/Members"), false));
		b12_7_1.addParameter(ACTION, ACTION_STATISTICS);
		b12_7_1.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.7.1.xml");
		b12_7_1.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.7.1.xml");
		b12_7_1.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_7_1_reportname");
		b12_7_1.setStyleClass(styledLink);
		
		stats.add(b12_7_1,2,14);	
		stats.addBreak(2,14);
		
		LinkContainer b12_7_2 = new LinkContainer();
		b12_7_2.add(formatText(iwrb.getLocalizedString("workreportwindow.b12_7_2_reportname", "Stickers for Members"), false));
		b12_7_2.addParameter(ACTION, ACTION_STATISTICS);
		b12_7_2.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,"Invocation-B12.7.2.xml");
		b12_7_2.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-B12.7.2.xml");
		b12_7_2.addParameter(STATS_LOCALIZABLE_KEY_NAME, "workreportwindow.b12_7_2_reportname");
		b12_7_2.setStyleClass(styledLink);
		
		stats.add(b12_7_2,2,14);	
		stats.addBreak(2,14);

		//B.13
		LinkContainer createReports = new LinkContainer();
		createReports.setStyleClass(styledLink);
		createReports.add(formatText(iwrb.getLocalizedString("workreportwindow.create_reports", "Get Excel reports"), true));
		createReports.addParameter(ACTION, ACTION_CREATE_REPORTS);

		if (type != null) {
			//add to window
			menu.add(operations, 1, 1);
			menu.setRowColor(1, COLOR_MIDDLE);
			menu.add(getHelpWithGrayImage("workreportwindow.operations_help",true),2,1);
			menu.add(selectReport, 1, 2);

			if(WorkReportConstants.WR_USER_TYPE_UNION.equals(type) || WorkReportConstants.WR_USER_TYPE_FEDERATION.equals(type)  || iwc.isSuperAdmin()) {
				menu.add(uploadReport, 1, 4);
				menu.setRowColor(4, COLOR_MIDDLE);
				menu.add(getHelpWithGrayImage("workreportwindow.uploadReport_help",true),2,4);
				menu.add(uploadList, 1, 5);
			}
			
			menu.add(workOnReport, 1, 6);
			menu.setRowColor(6, COLOR_MIDDLE);
			menu.add(getHelpWithGrayImage("workreportwindow.workOnReport_help",true),2,6);
			menu.add(editList, 1, 7);
			
			if (WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION.equals(type) || WorkReportConstants.WR_USER_TYPE_FEDERATION.equals(type) || iwc.isSuperAdmin()) {
				menu.add(reportsOverview, 1, 8);
			}

			menu.add(sendReport, 1, 9);

			if (WorkReportConstants.WR_USER_TYPE_UNION.equals(type) || WorkReportConstants.WR_USER_TYPE_FEDERATION.equals(type) || iwc.isSuperAdmin()) {
				menu.add(closeReport, 1, 10);
			}
			
			if (WorkReportConstants.WR_USER_TYPE_UNION.equals(type) || WorkReportConstants.WR_USER_TYPE_FEDERATION.equals(type)  || iwc.isSuperAdmin()) {
				menu.add(createReports, 1, 11);
			}

			if (type!=null  || iwc.isSuperAdmin()) {
				menu.add(statistics, 1, 12);
				menu.setRowColor(12, COLOR_MIDDLE);
				menu.add(getHelpWithGrayImage("workreportwindow.statistics_help",true),2,12);
				menu.add(stats, 1, 13);
			}
			

			

			
			
			
		}
		
		return menu;
	}


	protected WorkReportBusiness getWorkReportBusiness(IWApplicationContext iwc) {
		if (workBiz == null) {
			try {
				workBiz = (WorkReportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, WorkReportBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workBiz;
	}


	public String getBundleIdentifier() {
		return this.IW_BUNDLE_IDENTIFIER;
	}
}