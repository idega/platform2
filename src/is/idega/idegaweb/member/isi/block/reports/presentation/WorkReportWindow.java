package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import java.rmi.RemoteException;
import java.util.List;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.LinkContainer;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
/**
 * This window is used to work with a clubs work reports.
 * Copyright : Idega Software 2003
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */

public class WorkReportWindow extends IWAdminWindow {
	private MemberUserBusiness memBiz;
	private GroupBusiness groupBiz;
	private UserBusiness userBiz;
	private String userType = null;

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
	protected static final String ACTION_CLOSE_REPORT = "iwme_wr_act_b11";
	protected static final String ACTION_STATISTICS = "iwme_wr_act_b12";
	protected static final String ACTION_CREATE_REPORTS = "iwme_wr_act_b13";
	protected static final String ACTION_GET_REPORTS = "iwme_wr_act_b13_2";

	protected static final String COLOR_DARKEST = "#9F9F9F";
	protected static final String COLOR_MIDDLE = "#DFDFDF";
	protected static final String COLOR_LIGHTEST = "#EFEFEF";

	private IWResourceBundle iwrb;

	public WorkReportWindow() {
		setHeight(600);
		setWidth(800);
		setResizable(true);
		setScrollbar(true);

	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		iwrb = getResourceBundle(iwc);
		memBiz = getMemberUserBusiness(iwc);

		//sets the type of user making or viewing the reports. union staff, regional union staff, league staff, federation staff or club staff
		//and then gets the primary key of the correct group
		Integer groupId = setUserTypeAndReturnGroupId(iwc);

		setTitle(iwrb.getLocalizedString("workreportwindow.title", "Work Reports"));
		String action = iwc.getParameter(ACTION);

		Table table = new Table(2, 1);
		table.setWidthAndHeightToHundredPercent();
		table.setColumnColor(1, COLOR_LIGHTEST);
		table.setColumnWidth(1, "200");
		table.setRowHeight(1, Table.HUNDRED_PERCENT);
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);

		//add left menu of links
		table.add(getMenuTable(iwc), 1, 1);

		add(table);

		//add the main content
		if (action != null) {
			WorkReportSelector selector = null;

			//depending on the user type set the currect stuff
			if (action.equals(ACTION_WORK_SELECT_REPORT)) {
				selector = new WorkReportSelector();
				this.addTitle(iwrb.getLocalizedString(ACTION_WORK_SELECT_REPORT, "Select report"));
			}
			if (action.equals(ACTION_EDIT_MEMBER_LIST)) {
				selector = new WorkReportMemberEditor();
				this.addTitle(iwrb.getLocalizedString(ACTION_EDIT_MEMBER_LIST, "Edit members"));
			}
			else if (action.equals(ACTION_EDIT_ACCOUNT)) {
				selector = new WorkReportAccountEditor();
				this.addTitle(iwrb.getLocalizedString(ACTION_EDIT_ACCOUNT, "Edit account info"));
			}
			else if (action.equals(ACTION_EDIT_BOARD)) {
				selector = new WorkReportBoardMemberEditor();
				this.addTitle(iwrb.getLocalizedString(ACTION_EDIT_BOARD, "Edit board info"));
			}
			else if (action.equals(ACTION_EDIT_DIVISION_BOARD)) {
				selector = new WorkReportDivisionBoardEditor();
				this.addTitle(iwrb.getLocalizedString(ACTION_EDIT_DIVISION_BOARD, "Edit division info"));
			}
			else if (action.equals(ACTION_SEND_REPORT)) {
				selector = new WorkReportSender();
				this.addTitle(iwrb.getLocalizedString(ACTION_SEND_REPORT, "Send report"));
			}
			else if (action.equals(ACTION_IMPORT_MEMBERS)) {
				selector = new WorkReportMemberImporter();
				this.addTitle(iwrb.getLocalizedString(ACTION_IMPORT_MEMBERS, "Import members"));
			}
			else if (action.equals(ACTION_IMPORT_ACCOUNT)) {
				selector = new WorkReportAccountImporter();
				this.addTitle(iwrb.getLocalizedString(ACTION_IMPORT_ACCOUNT, "Import account info"));
			}
			else if (action.equals(ACTION_IMPORT_BOARD)) {
				selector = new WorkReportBoardImporter();
				this.addTitle(iwrb.getLocalizedString(ACTION_IMPORT_BOARD, "Import board info"));
			}
			else if (action.equals(ACTION_REPORT_OVERVIEW)) {
				selector = new WorkReportSelector();
				this.addTitle(iwrb.getLocalizedString(ACTION_REPORT_OVERVIEW, "Review work report"));
			}
			else if (action.equals(ACTION_CLOSE_REPORT)) {
				selector = new WorkReportCloser();
				this.addTitle(iwrb.getLocalizedString(ACTION_CLOSE_REPORT, "Close report"));
			}
			else if (action.equals(ACTION_STATISTICS)) {
				selector = new WorkReportSelector();
				this.addTitle(iwrb.getLocalizedString(ACTION_STATISTICS, "View statistics"));
			}
			else if (action.equals(ACTION_CREATE_REPORTS)) {
				table.add(new WorkReportExporter(),2,1);	//not a selector
				addTitle(iwrb.getLocalizedString(ACTION_CREATE_REPORTS,"Generate reports"));
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
		}
	}

	//searches the current users top nodes to figure out who he is. 
	//TODO Eiki CHANGE TO ROLES and optimize?
	private Integer setUserTypeAndReturnGroupId(IWContext iwc) {
		User user = iwc.getCurrentUser();

		try {
			List union = getMemberUserBusiness(iwc).getUnionListForUserFromTopNodes(user, iwc); //should only be one
			if (!union.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_UNION;
				return null;
			}

			List federation = getMemberUserBusiness(iwc).getFederationListForUserFromTopNodes(user, iwc); //should only be one
			if (!federation.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_FEDERATION;
				return null;
			}

			List club = getMemberUserBusiness(iwc).getClubListForUserFromTopNodes(user, iwc); //should only be one
			if (!club.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_CLUB;
				return ((Integer) ((Group)club.iterator().next()).getPrimaryKey());
			}

			List regional = getMemberUserBusiness(iwc).getRegionalUnionListForUserFromTopNodes(user, iwc); //should only be one
			if (!regional.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION;
				return ((Integer) ((Group)regional.iterator().next()).getPrimaryKey());
			}

			List leagues = getMemberUserBusiness(iwc).getLeaguesListForUserFromTopNodes(user, iwc); //should only be one
			if (!leagues.isEmpty()) {
				userType = WorkReportConstants.WR_USER_TYPE_LEAGUE;
				return ((Integer) ((Group)leagues.iterator().next()).getPrimaryKey());
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

		Table menu = new Table(1, 14);
		menu.setWidth(Table.HUNDRED_PERCENT);
		menu.setHeight(Table.HUNDRED_PERCENT);
		menu.setCellspacing(0);

		Text operations = formatHeadline(iwrb.getLocalizedString("workreportwindow.operations", "Operations"));

		//B.1
		LinkContainer selectReport = new LinkContainer();
		selectReport.add(formatText(iwrb.getLocalizedString("workreportwindow.select_report", "Select report"), true));
		selectReport.addParameter(ACTION, ACTION_WORK_SELECT_REPORT);
		selectReport.addParameter(WorkReportConstants.WR_SESSION_CLEAR, "TRUE");

		//B.2
		Text workOnReport = formatText(iwrb.getLocalizedString("workreportwindow.edit_report", "Edit report"), true);
		Lists editList = new Lists();

		if (!WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION.equals(type) && !WorkReportConstants.WR_USER_TYPE_UNION.equals(type) && !WorkReportConstants.WR_USER_TYPE_LEAGUE.equals(type)) {
			LinkContainer editMemberList = new LinkContainer();
			editMemberList.add(formatText(iwrb.getLocalizedString("workreportwindow.edit_members", "Edit member list")));
			editMemberList.addParameter(ACTION, ACTION_EDIT_MEMBER_LIST);
			editList.add(editMemberList);
		}

		//B.3
		LinkContainer editAccountList = new LinkContainer();
		editAccountList.add(formatText(iwrb.getLocalizedString("workreportwindow.edit_account", "Edit account info")));
		editAccountList.addParameter(ACTION, ACTION_EDIT_ACCOUNT);
		editList.add(editAccountList);
		//B.4
		LinkContainer editBoardList = new LinkContainer();
		editBoardList.add(formatText(iwrb.getLocalizedString("workreportwindow.edit_board", "Edit board info")));
		editBoardList.addParameter(ACTION, ACTION_EDIT_BOARD);
		editList.add(editBoardList);
		//B.4_1
		LinkContainer editDivisionBoardList = new LinkContainer();
		editDivisionBoardList.add(formatText(iwrb.getLocalizedString("workreportwindow.division_edit_board", "Edit division board info")));
		editDivisionBoardList.addParameter(ACTION, ACTION_EDIT_DIVISION_BOARD);
		editList.add(editDivisionBoardList);

		//
		//B.5		
		LinkContainer sendReport = new LinkContainer();
		sendReport.add(formatText(iwrb.getLocalizedString("workreportwindow.send_report", "Send report"), true));
		sendReport.addParameter(ACTION, ACTION_SEND_REPORT);

		//
		Lists uploadList = new Lists();
		Text uploadReport = formatText(iwrb.getLocalizedString("workreportwindow.excel_imports", "Excel imports"), true);

		//B.7
		LinkContainer importMemberList = new LinkContainer();
		importMemberList.add(formatText(iwrb.getLocalizedString("workreportwindow.import_members", "Import member list")));
		importMemberList.addParameter(ACTION, ACTION_IMPORT_MEMBERS);
		uploadList.add(importMemberList);
		//B.8
		LinkContainer importAccount = new LinkContainer();
		importAccount.add(formatText(iwrb.getLocalizedString("workreportwindow.import_account", "Import account info")));
		importAccount.addParameter(ACTION, ACTION_IMPORT_ACCOUNT);
		uploadList.add(importAccount);
		//B.9
		LinkContainer importBoard = new LinkContainer();
		importBoard.add(formatText(iwrb.getLocalizedString("workreportwindow.import_board", "Import board info")));
		importBoard.addParameter(ACTION, ACTION_IMPORT_BOARD);
		uploadList.add(importBoard);

		//B.10
		LinkContainer reportsOverview = new LinkContainer();
		reportsOverview.add(formatText(iwrb.getLocalizedString("workreportwindow.report_overview", "Reports overview"), true));
		reportsOverview.addParameter(ACTION, ACTION_REPORT_OVERVIEW);

		//B.11
		LinkContainer closeReport = new LinkContainer();
		closeReport.add(formatText(iwrb.getLocalizedString("workreportwindow.close_report", "Close report"), true));
		reportsOverview.addParameter(ACTION, ACTION_CLOSE_REPORT);

		//B.12
		Text statistics = formatHeadline(iwrb.getLocalizedString("workreportwindow.statistics", "Statistics"));

		Lists statsList = new Lists();
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.leagues", "Leagues")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.regional_unions", "Regional unions")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.clubs", "Clubs")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.accounts", "Accounts")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.reports_list", "Reports list")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.misc", "Misc")));

		//B.13
		LinkContainer createReports = new LinkContainer();
		createReports.add(formatText(iwrb.getLocalizedString("workreportwindow.create_reports", "Get Excel reports"), true));
		createReports.addParameter(ACTION, ACTION_CREATE_REPORTS);

		if (type != null) {
			//add to window
			menu.add(operations, 1, 1);
			menu.setRowColor(1, COLOR_MIDDLE);
			menu.add(selectReport, 1, 2);
			menu.add(uploadReport, 1, 4);
			menu.setRowColor(4, COLOR_MIDDLE);
			menu.add(uploadList, 1, 5);
			menu.add(workOnReport, 1, 6);
			menu.setRowColor(6, COLOR_MIDDLE);
			menu.add(editList, 1, 7);

			//insert ugly hax
			if (WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION.equals(type) || WorkReportConstants.WR_USER_TYPE_FEDERATION.equals(type)) {
				menu.add(reportsOverview, 1, 8);
			}

			menu.add(sendReport, 1, 9);

			if (WorkReportConstants.WR_USER_TYPE_REGIONAL_UNION.equals(type) || WorkReportConstants.WR_USER_TYPE_FEDERATION.equals(type)) {
				menu.add(closeReport, 1, 10);
			}

			if (!WorkReportConstants.WR_USER_TYPE_CLUB.equals(type)) {
				menu.add(statistics, 1, 11);
				menu.setRowColor(11, COLOR_MIDDLE);
				menu.add(statsList, 1, 12);
			}
			
//			if (WorkReportConstants.WR_USER_TYPE_FEDERATION.equals(type)) {
				menu.add(createReports, 1, 13);
//			}
		}
		
		return menu;
	}

	public MemberUserBusiness getMemberUserBusiness(IWApplicationContext iwc) {
		if (memBiz == null) {
			try {
				memBiz = (MemberUserBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc, MemberUserBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return memBiz;
	}

	public String getBundleIdentifier() {
		return this.IW_BUNDLE_IDENTIFIER;
	}
}