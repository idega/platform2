/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusiness;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.datareport.presentation.ReportGenerator;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.LinkContainer;
import com.idega.presentation.text.Text;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.presentation.GroupPropertyWindow;

/**
 * @author palli
 */
public class CashierWindow extends StyledIWAdminWindow {
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi.block.accounting";
	public static final String PARAMETER_GROUP_ID = GroupPropertyWindow.PARAMETERSTRING_GROUP_ID;
	public static final String PARAMETER_CLUB_ID = "cashier_club_id";
	public static final String PARAMETER_DIVISION_ID = "cashier_div_id";
	public static final String PARAMETER_USER_ID = "cashier_user_id";
	public final static String STYLE_2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";

	//	public static final String
	// SELECTED_GROUP_PROVIDER_PRESENTATION_STATE_ID_KEY =
	// "selected_group_mm_id_key";

	public static final String ACTION = "cw_act";
	private static final String ACTION_CONTRACT = "isi_acc_cw_act_contract";
	private static final String ACTION_PAYMENT = "isi_acc_cw_act_payment";
	private static final String ACTION_TARIFF = "isi_acc_cw_act_tariff";
	private static final String ACTION_TARIFF_TYPE = "isi_acc_cw_act_tariff_type";
	private static final String ACTION_MANUAL_ASSESSMENT = "isi_acc_cw_act_ass_man";
	private static final String ACTION_AUTOMATIC_ASSESSMENT = "isi_acc_cw_act_ass_auto";
	private static final String ACTION_CREDITCARD_COMPANY_CONTRACT = "isi_acc_cw_act_cc_contract";
	private static final String ACTION_CANCEL = "isi_acc_cw_act_cc";
	private static final String ACTION_SELECT_USER = "isi_acc_cw_act_sel_usr";
	private static final String ACTION_PAYMENT_HISTORY = "cw_act_pay_hist";
	private static final String ACTION_MEMBER_CREDITCARD = "isi_acc_cw_act_memb_cc";

	private static final String REPORT_PAYMENT_STATUS = "isi_acc_cw_rep_payment_status";
	private static final String REPORT_PAYMENT_OVERVIEW = "isi_acc_cw_rep_payment_overview";
	private static final String REPORT_DEBT_OVERVIEW = "isi_acc_cw_rep_debt_overview";
	private static final String REPORT_ENTRY_OVERVIEW = "isi_acc_cw_rep_entry_overview";
	private static final String REPORT_LATE_PAYMENT_LIST = "isi_acc_cw_rep_late_payment_list";
	private static final String REPORT_PAYMENT_LIST = "isi_acc_cw_rep_payment_list";
	
	private static final String STATS_LOCALIZABLE_KEY_NAME = "STATS_LOCALIZABLE_KEY_NAME";
	//public static final String STATS_LAYOUT_PREFIX = "STATS_LAYOUT_PREFIX";
	private static final String STATS_LAYOUT_PARAM = "STATS_LAYOUT_PARAM";
	private static final String STATS_INVOCATION_PARAM = "STATS_INVOCATION_PARAM";
	//private static final String STATS_INVOCATION_PREFIX = "stats_invocation_xml_file_id_";
	private static final String STATS_LAYOUT_NAME_FROM_BUNDLE = "STATS_LAYOUT_NAME_FROM_BUNDLE";
	private static final String STATS_INVOCATION_NAME_FROM_BUNDLE = "STATS_INVOCATION_NAME_FROM_BUNDLE";
	

	private String PARAMETER_PID = "cw_pid";
	private String PARAMETER_STATUS = "cw_sta";
	private String PARAMETER_SAVE = "cw_sv";
	private String PARAMETER_Group = "cw_group";
	
	private IWResourceBundle iwrb;
	private IWBundle iwb;

	private static final String HELP_TEXT_KEY = "cashier_window";

	private Group _group;
	private Group _club;
	private Group _div;
	private User _user;
	private IWResourceBundle _iwrb;
	private IWBundle _iwb;
	private MemberUserBusiness _membBiz;

	private String styledLink = "styledLinkGeneral";
	private String helpTextKey = "";
	private String rightBorderTable = "borderRight";
	private String borderTable = "borderAll";

	protected static final String COLOR_DARKEST = "#9F9F9F";
	protected static final String COLOR_MIDDLE = "#DFDFDF";
	protected static final String COLOR_LIGHTEST = "#EFEFEF";

	public CashierWindow() {
		setHeight(600);
		setWidth(1024);
		setResizable(true);
		setScrollbar(true);
	}

	private void init(IWContext iwc) {
		String sGroupId = iwc.getParameter(PARAMETER_GROUP_ID);
		if (sGroupId != null) {
			try {
				_group = getGroupBusiness(iwc).getGroupByGroupID(new Integer(sGroupId).intValue());
			}
			catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
		}

		String sClubId = iwc.getParameter(PARAMETER_CLUB_ID);
		if (sClubId != null) {
			try {
				_club = getGroupBusiness(iwc).getGroupByGroupID(new Integer(sClubId).intValue());
			}
			catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
		}
		
		String sDivId = iwc.getParameter(PARAMETER_DIVISION_ID);
		if (sDivId != null) {
			try {
				_div = getGroupBusiness(iwc).getGroupByGroupID(new Integer(sDivId).intValue());
			}
			catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
		}
		
		String sUserId = iwc.getParameter(PARAMETER_USER_ID);
		if (sUserId != null && !"".equals(sUserId)) {
			try {
				_user = getUserBusiness(iwc).getUser(new Integer(sUserId));
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		_iwrb = getResourceBundle(iwc);
		_iwb = getBundle(iwc);
	}

	private Table getMenuTable(IWContext iwc) {
		Table menu = new Table(2, 21);
		menu.setWidth(Table.HUNDRED_PERCENT);
		menu.setCellpadding(3);
		menu.setCellspacing(0);

		Text clubOperations = formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.club_operations", "Club operations"), true);

		Text memberOperations = formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.member_operations", "Member operations"), true);

		Text reports = formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.reports", "Reports"), true);

		LinkContainer editTariffType = new LinkContainer();
		editTariffType.setStyleClass(styledLink);
		editTariffType.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.edit_tariff_type", "Edit club tariff type (A.12)")));
		editTariffType.addParameter(ACTION, ACTION_TARIFF_TYPE);
		editTariffType.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());
		if (_user != null) {
			editTariffType.addParameter(PARAMETER_USER_ID, ((Integer) _user.getPrimaryKey()).toString());
		}
		if (_div != null) {
			editTariffType.addParameter(PARAMETER_DIVISION_ID, ((Integer) _div.getPrimaryKey()).toString());
		}
		if (_club != null) {
			editTariffType.addParameter(PARAMETER_CLUB_ID, ((Integer) _club.getPrimaryKey()).toString());
		}
		
		LinkContainer editTariff = new LinkContainer();
		editTariff.setStyleClass(styledLink);
		editTariff.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.edit_tariff", "Edit club tariff list (A.12)")));
		editTariff.addParameter(ACTION, ACTION_TARIFF);
		editTariff.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());
		if (_user != null) {
			editTariff.addParameter(PARAMETER_USER_ID, ((Integer) _user.getPrimaryKey()).toString());
		}
		if (_div != null) {
			editTariff.addParameter(PARAMETER_DIVISION_ID, ((Integer) _div.getPrimaryKey()).toString());
		}
		if (_club != null) {
			editTariff.addParameter(PARAMETER_CLUB_ID, ((Integer) _club.getPrimaryKey()).toString());
		}
		
		LinkContainer autoAss = new LinkContainer();
		autoAss.setStyleClass(styledLink);
		autoAss.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.auto_assessment", "Automatic assessment (A.15)")));
		autoAss.addParameter(ACTION, ACTION_AUTOMATIC_ASSESSMENT);
		autoAss.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());
		if (_user != null) {
			autoAss.addParameter(PARAMETER_USER_ID, ((Integer) _user.getPrimaryKey()).toString());
		}
		if (_div != null) {
			autoAss.addParameter(PARAMETER_DIVISION_ID, ((Integer) _div.getPrimaryKey()).toString());
		}
		if (_club != null) {
			autoAss.addParameter(PARAMETER_CLUB_ID, ((Integer) _club.getPrimaryKey()).toString());
		}
		
		LinkContainer ccContract = new LinkContainer();
		ccContract.setStyleClass(styledLink);
		ccContract.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.cc_contract", "Edit creditcard company contract (A.24)")));
		ccContract.addParameter(ACTION, ACTION_CREDITCARD_COMPANY_CONTRACT);
		ccContract.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());
		if (_user != null) {
			ccContract.addParameter(PARAMETER_USER_ID, ((Integer) _user.getPrimaryKey()).toString());
		}
		if (_div != null) {
			ccContract.addParameter(PARAMETER_DIVISION_ID, ((Integer) _div.getPrimaryKey()).toString());
		}
		if (_club != null) {
			ccContract.addParameter(PARAMETER_CLUB_ID, ((Integer) _club.getPrimaryKey()).toString());
		}
		
		
		LinkContainer selectUser = new LinkContainer();
		selectUser.setStyleClass(styledLink);
		selectUser.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.select_user", "Select user to work with")));
		selectUser.addParameter(ACTION, ACTION_SELECT_USER);
		selectUser.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());
		if (_user != null) {
			selectUser.addParameter(PARAMETER_USER_ID, ((Integer) _user.getPrimaryKey()).toString());
		}
		if (_div != null) {
			selectUser.addParameter(PARAMETER_DIVISION_ID, ((Integer) _div.getPrimaryKey()).toString());
		}
		if (_club != null) {
			selectUser.addParameter(PARAMETER_CLUB_ID, ((Integer) _club.getPrimaryKey()).toString());
		}
		
		LinkContainer manAss = new LinkContainer();
		manAss.setStyleClass(styledLink);
		manAss.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.manual_assessment", "Manual assessment (A.14)")));
		manAss.addParameter(ACTION, ACTION_MANUAL_ASSESSMENT);
		manAss.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());
		if (_user != null) {
			manAss.addParameter(PARAMETER_USER_ID, ((Integer) _user.getPrimaryKey()).toString());
		}
		if (_div != null) {
			manAss.addParameter(PARAMETER_DIVISION_ID, ((Integer) _div.getPrimaryKey()).toString());
		}
		if (_club != null) {
			manAss.addParameter(PARAMETER_CLUB_ID, ((Integer) _club.getPrimaryKey()).toString());
		}
		
		LinkContainer insertContract = new LinkContainer();
		insertContract.setStyleClass(styledLink);
		insertContract.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.insert_contract", "Insert/edit member contract (new/A.10)")));
		insertContract.addParameter(ACTION, ACTION_CONTRACT);
		insertContract.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());
		if (_user != null) {
			insertContract.addParameter(PARAMETER_USER_ID, ((Integer) _user.getPrimaryKey()).toString());
		}
		if (_div != null) {
			insertContract.addParameter(PARAMETER_DIVISION_ID, ((Integer) _div.getPrimaryKey()).toString());
		}
		if (_club != null) {
			insertContract.addParameter(PARAMETER_CLUB_ID, ((Integer) _club.getPrimaryKey()).toString());
		}
		
		LinkContainer registerPayment = new LinkContainer();
		registerPayment.setStyleClass(styledLink);
		registerPayment.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.payment", "Register payment (A.11)")));
		registerPayment.addParameter(ACTION, ACTION_PAYMENT);
		registerPayment.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());
		if (_user != null) {
			registerPayment.addParameter(PARAMETER_USER_ID, ((Integer) _user.getPrimaryKey()).toString());
		}
		if (_div != null) {
			registerPayment.addParameter(PARAMETER_DIVISION_ID, ((Integer) _div.getPrimaryKey()).toString());
		}
		if (_club != null) {
			registerPayment.addParameter(PARAMETER_CLUB_ID, ((Integer) _club.getPrimaryKey()).toString());
		}
		
		LinkContainer paymentHistory = new LinkContainer();
		paymentHistory.setStyleClass(styledLink);
		paymentHistory.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.paymentHistory", "Payment history (3.11)")));
		paymentHistory.addParameter(ACTION, ACTION_PAYMENT_HISTORY);
		paymentHistory.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());
		if (_user != null) {
			paymentHistory.addParameter(PARAMETER_USER_ID, ((Integer) _user.getPrimaryKey()).toString());
		}
		if (_div != null) {
			paymentHistory.addParameter(PARAMETER_DIVISION_ID, ((Integer) _div.getPrimaryKey()).toString());
		}
		if (_club != null) {
			paymentHistory.addParameter(PARAMETER_CLUB_ID, ((Integer) _club.getPrimaryKey()).toString());
		}
		
		LinkContainer memberCreditCard = new LinkContainer();
		memberCreditCard.setStyleClass(styledLink);
		memberCreditCard.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.memberCreditCard", "Member credit card info (A.10)")));
		memberCreditCard.addParameter(ACTION, ACTION_MEMBER_CREDITCARD);
		memberCreditCard.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());
		if (_user != null) {
			memberCreditCard.addParameter(PARAMETER_USER_ID, ((Integer) _user.getPrimaryKey()).toString());
		}
		if (_div != null) {
			memberCreditCard.addParameter(PARAMETER_DIVISION_ID, ((Integer) _div.getPrimaryKey()).toString());
		}
		if (_club != null) {
			memberCreditCard.addParameter(PARAMETER_CLUB_ID, ((Integer) _club.getPrimaryKey()).toString());
		}
		
		//reports
		Text statistics = formatHeadline(iwrb.getLocalizedString("isi_acc_cashierwindow.statistics", "Statistics"));

		Table stats = new Table(2,14);
		stats.setColumnWidth(1,"20");
		stats.mergeCells(1,1,2,1);
		stats.mergeCells(1,3,2,3);
		stats.mergeCells(1,5,2,5);
		stats.mergeCells(1,7,2,7);
		stats.mergeCells(1,9,2,9);
		stats.mergeCells(1,11,2,11);
		stats.mergeCells(1,13,2,13);
		
		stats.add(formatText(iwrb.getLocalizedString("isi_acc_cashierwindow.leagues", "Leagues")),1,1);
		
		LinkContainer paymentStatus = new LinkContainer();
		paymentStatus.add(formatText(iwrb.getLocalizedString("isi_acc_cashierwindow.paymentStatus", "Payment Status"), false));
		paymentStatus.addParameter(ACTION, REPORT_PAYMENT_STATUS);
		paymentStatus.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());
		paymentStatus.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE, "Invocation-A29.1.xml");
		paymentStatus.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,"Layout-A29.1.xml");
		paymentStatus.addParameter(STATS_LOCALIZABLE_KEY_NAME, "isi_acc_cashierwindow.payment_status_reportname");
		if (_user != null) {
			paymentStatus.addParameter(PARAMETER_USER_ID, ((Integer) _user.getPrimaryKey()).toString());
		}
		if (_club != null) {
			paymentStatus.addParameter(PARAMETER_CLUB_ID, ((Integer) _club.getPrimaryKey()).toString());
		}
			
		paymentStatus.setStyleClass(styledLink);
		
		stats.add(paymentStatus,2,2);
		stats.addBreak(2,2);

		LinkContainer paymentOverview = new LinkContainer();
		paymentOverview.setStyleClass(styledLink);
		paymentOverview.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.paymentOverview", "Payment overview (A.29)")));
		paymentOverview.addParameter(ACTION, REPORT_PAYMENT_OVERVIEW);
		paymentOverview.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());

		LinkContainer debtOverview = new LinkContainer();
		debtOverview.setStyleClass(styledLink);
		debtOverview.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.debtOverview", "Debt overview (A.29)")));
		debtOverview.addParameter(ACTION, REPORT_DEBT_OVERVIEW);
		debtOverview.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());

		LinkContainer entryOverview = new LinkContainer();
		entryOverview.setStyleClass(styledLink);
		entryOverview.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.entryOverview", "Entry overview (A.29)")));
		entryOverview.addParameter(ACTION, REPORT_ENTRY_OVERVIEW);
		entryOverview.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());

		LinkContainer latePaymentList = new LinkContainer();
		latePaymentList.setStyleClass(styledLink);
		latePaymentList.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.latePaymentList", "Late payment list (A.29)")));
		latePaymentList.addParameter(ACTION, REPORT_LATE_PAYMENT_LIST);
		latePaymentList.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());

		LinkContainer paymentList = new LinkContainer();
		paymentList.setStyleClass(styledLink);
		paymentList.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.paymentList", "Payment list (A.29)")));
		paymentList.addParameter(ACTION, REPORT_LATE_PAYMENT_LIST);
		paymentList.addParameter(PARAMETER_GROUP_ID, ((Integer) _group.getPrimaryKey()).toString());

		//add to window
		menu.add(clubOperations, 1, 1);
		menu.add(getHelpWithGrayImage("cashierwindow.clubOperations_help", true), 2, 1);
		menu.setRowColor(1, COLOR_MIDDLE);
		menu.add(editTariffType, 1, 2);
		menu.add(editTariff, 1, 3);
		menu.add(autoAss, 1, 4);
		menu.add(ccContract, 1, 5);

		menu.add(memberOperations, 1, 7);
		menu.add(getHelpWithGrayImage("cashierwindow.memberOperations_help", true), 2, 7);
		menu.setRowColor(7, COLOR_MIDDLE);
		menu.add(selectUser, 1, 8);
		menu.add(manAss, 1, 9);
		menu.add(registerPayment, 1, 10);
		menu.add(paymentHistory, 1, 11);
		menu.add(memberCreditCard, 1, 12);
		menu.add(insertContract, 1, 13);
		
		menu.add(reports, 1, 15);
		menu.add(getHelpWithGrayImage("cashierwindow.reports_help", true), 2, 15);
		menu.setRowColor(15, COLOR_MIDDLE);
		menu.add(paymentStatus, 1, 16);
		menu.add(paymentOverview, 1, 17);
		menu.add(debtOverview, 1, 18);
		menu.add(entryOverview, 1, 19);
		menu.add(latePaymentList, 1, 20);
		menu.add(paymentList, 1, 21);

		return menu;
	}

	protected boolean getHasPermissionToViewWindow(IWContext iwc) {
		if (_club == null) {
			if (_group == null) {
				Text errorText = new Text(_iwrb.getLocalizedString("isi_acc_no_group_selected", "You must select a group in a club"));
				errorText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);
				
				add(errorText);
				return false;
			}
			else {
				try {
					_club = getAccountingBusiness(iwc).findClubForGroup(_group);
				}
				catch (RemoteException e) {
					e.printStackTrace();
					_club = null;
				}
			
				if (_club == null) {
					Text errorText = new Text(_iwrb.getLocalizedString("isi_acc_no_group_selected", "You must select a group in a club"));
					errorText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);
					
					add(errorText);
					return false;
				}
			}
		}
		
		if (iwc.isSuperAdmin())
			return true;
		
		User currentUser = iwc.getCurrentUser();
		if (currentUser == null) {
			Text errorText = new Text(_iwrb.getLocalizedString("isi_acc_no_user", "There is no user logged in. Please log in and try again."));
			errorText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);
			
			add(errorText);
			return false;
		}
		
		//System.out.println("Going to get all the cashierGroups for the user");
		
		Collection cashierGroupsInClubForUser = getGroupsForUser(iwc, currentUser);
		//System.out.println("Done getting groups.");
		if (cashierGroupsInClubForUser == null || cashierGroupsInClubForUser.isEmpty()) {
			Text errorText = new Text(_iwrb.getLocalizedString("isi_acc_user_not_cashier", "You are not logged in as a cashier for this club. Please log in as a cashier and try again."));
			errorText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);
			
			add(errorText);
			return false;
		}
		
		//@TODO fix ef einhver er gjaldkeri í tveimur deildum en ekki félagi....
		boolean first = true;
		Iterator it = cashierGroupsInClubForUser.iterator();
		while (it.hasNext()) {
			Group cashierGroup = (Group) it.next();
			try {
				Group tmpDiv = getAccountingBusiness(iwc).findDivisionForGroup(cashierGroup);
				if (first) {
					_div = tmpDiv;
					first = false;
				}
				else {
					if (_div != null) {
						_div = tmpDiv;
					}
				}
				
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}			
		}
		
		return true;
 	}
	
	private Collection getGroupsForUser(IWContext iwc, User user) {
		ArrayList userCashierGroups = new ArrayList();
		ArrayList userGroupsInClub = new ArrayList();
		Collection allCashierGroups = iwc.getAccessController().getAllGroupsForRoleKey("Gjaldkeri", iwc);

		try {
			Collection allUserGroups = getUserBusiness(iwc).getUserGroupsDirectlyRelated(user);
			if (allUserGroups != null && !allUserGroups.isEmpty()) {
				Iterator it = allUserGroups.iterator();
				while (it.hasNext()) {
					Group group = (Group) it.next();
					if (allCashierGroups.contains(group)) {
						userCashierGroups.add(group);
					}					
				}
			}
			
			if (userCashierGroups != null && !userCashierGroups.isEmpty()) {
				Iterator it = userCashierGroups.iterator();
				while (it.hasNext()) {
					Group group = (Group) it.next();
					Group parent = getAccountingBusiness(iwc).findClubForGroup(group);
					if (parent != null && _club.equals(parent)) {
						userGroupsInClub.add(group);
					}
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}		
		
		return userGroupsInClub;
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		iwrb = getResourceBundle(iwc);
		iwb = getBundle(iwc);
		init(iwc);
		
		boolean hasPermission = getHasPermissionToViewWindow(iwc);
		if (!hasPermission) {
			return;
		}

		setTitle("Cashier window");
		StringBuffer title = new StringBuffer(_iwrb.getLocalizedString("isi_acc_cashier_window", "Cashier Window"));
		if (_club != null) {
			title.append(" - ");
			title.append(_club.getName());
			if (_div != null) {
				title.append(" / ");
				title.append(_div.getName());
			}
		}
		addTitle(title.toString(), IWConstants.BUILDER_FONT_STYLE_TITLE);

		String action = iwc.getParameter(ACTION);

		Table table = new Table(2, 1);
		table.setWidthAndHeightToHundredPercent();
		table.setColumnColor(1, COLOR_LIGHTEST);
		table.setColumnWidth(1, "200");
		table.setRowHeight(1, Table.HUNDRED_PERCENT);
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);
		table.setCellpaddingAndCellspacing(0);
		table.mergeCells(1, 1, 1, 2);
		table.setStyleClass(1, 1, rightBorderTable);
		table.setStyleClass(borderTable);

		//add left menu of links
		Table menuTable = getMenuTable(iwc);

		table.add(menuTable, 1, 1);
		
		add(table, iwc);

		if (action != null) {
			CashierSubWindowTemplate subWindow = null;
			StringBuffer actionTitle = new StringBuffer();
			if (action.equals(ACTION_TARIFF)) {
				actionTitle.append(_iwrb.getLocalizedString(ACTION_TARIFF, "Edit tariff list"));
				subWindow = new EditTariffList();
				helpTextKey = ACTION_TARIFF + "_help";
			}
			else if (action.equals(ACTION_TARIFF_TYPE)) {
				actionTitle.append(_iwrb.getLocalizedString(ACTION_TARIFF_TYPE, "Edit tariff type"));
				subWindow = new EditTariffType();
				helpTextKey = ACTION_TARIFF_TYPE + "_help";
			}
			else if (action.equals(ACTION_MANUAL_ASSESSMENT)) {
				actionTitle.append(_iwrb.getLocalizedString(ACTION_MANUAL_ASSESSMENT, "Manual assessment"));
				subWindow = new ManualAssessment();
				helpTextKey = ACTION_MANUAL_ASSESSMENT + "_help";
			}
			else if (action.equals(ACTION_AUTOMATIC_ASSESSMENT)) {
				actionTitle.append(_iwrb.getLocalizedString(ACTION_AUTOMATIC_ASSESSMENT, "Automatic assessment"));
				subWindow = new AutomaticAssessment();
				helpTextKey = ACTION_AUTOMATIC_ASSESSMENT + "_help";
			}
			else if (action.equals(ACTION_CREDITCARD_COMPANY_CONTRACT)) {
				actionTitle.append(_iwrb.getLocalizedString(ACTION_CREDITCARD_COMPANY_CONTRACT, "Club/division credit cardcontract"));
				subWindow = new ClubCreditCardContract();
				helpTextKey = ACTION_CREDITCARD_COMPANY_CONTRACT + "_help";
			}
			else if (action.equals(ACTION_SELECT_USER)) {
				actionTitle.append(_iwrb.getLocalizedString(ACTION_SELECT_USER, "Select user"));
				subWindow = new SelectUser();
				helpTextKey = ACTION_SELECT_USER + "_help";
			}
			else if (action.equals(ACTION_CONTRACT)) {
				actionTitle.append(_iwrb.getLocalizedString(ACTION_CONTRACT, "Create user contract"));
				subWindow = new UserContract();
				helpTextKey = ACTION_CONTRACT + "_help";
			}
			else if (action.equals(ACTION_PAYMENT)) {
				actionTitle.append(_iwrb.getLocalizedString(ACTION_PAYMENT, "Enter user payment"));
				subWindow = new UserPayment();
				helpTextKey = ACTION_PAYMENT + "_help";
			}
			else if (action.equals(ACTION_PAYMENT_HISTORY)) {
				actionTitle.append(_iwrb.getLocalizedString(ACTION_PAYMENT_HISTORY, "View user payment history"));
				subWindow = new UserPaymentHistory();
				helpTextKey = ACTION_PAYMENT_HISTORY + "_help";
			}
			else if (action.equals(ACTION_MEMBER_CREDITCARD)) {
				actionTitle.append(_iwrb.getLocalizedString(ACTION_MEMBER_CREDITCARD, "Edit users creditcard info"));
				subWindow = new EditTariffList();
				helpTextKey = ACTION_MEMBER_CREDITCARD + "_help";
				subWindow = new UserCreditcard();
			} 
			else if (action.equals(REPORT_PAYMENT_STATUS)) {
				ReportGenerator repGen = new ReportGenerator();
				repGen.setParameterToMaintain(ACTION);
				repGen.setParameterToMaintain(STATS_INVOCATION_PARAM);
				repGen.setParameterToMaintain(STATS_LAYOUT_PARAM);
				repGen.setParameterToMaintain(STATS_LAYOUT_NAME_FROM_BUNDLE);
				repGen.setParameterToMaintain(STATS_INVOCATION_NAME_FROM_BUNDLE);
				repGen.setParameterToMaintain(STATS_LOCALIZABLE_KEY_NAME);
				repGen.setParameterToMaintain(PARAMETER_GROUP_ID);
				repGen.setParameterToMaintain(PARAMETER_CLUB_ID);
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
				//selectorIsSet = true;
				//this.addTitle(iwrb.getLocalizedString(REPORT_PAYMENT_STATUS, "Statistics"));
				//this.addTitle(iwrb.getLocalizedString(REPORT_PAYMENT_STATUS, "Statistics"), IWConstants.BUILDER_FONT_STYLE_TITLE);
				this.addTitle("Statistics");
				this.addTitle("Statistics", IWConstants.BUILDER_FONT_STYLE_TITLE);
			}

			if (_club != null) {
				actionTitle.append(" - ");
				actionTitle.append(_club.getName());
			}
			addTitle(actionTitle.toString(), IWConstants.BUILDER_FONT_STYLE_TITLE);
			
			if (subWindow != null) {
				Table helpTable = new Table(1, 1);
				helpTable.setWidth(Table.HUNDRED_PERCENT);
				helpTable.setHeight(15);
				helpTable.setAlignment(1, 1, "right");
				helpTable.add(getHelpWithGrayImage(helpTextKey, false), 1, 1);
				table.add(helpTable, 2, 1);
				
				subWindow.setClub(_club);
				if (_user != null)
					subWindow.setUser(_user);
				if (_div != null)
					subWindow.setDivision(_div);
				
				table.add(subWindow, 2, 1);
			}
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	protected MemberUserBusiness getMemberUserBusiness(IWApplicationContext iwc) {
		if (_membBiz == null) {
			try {
				_membBiz = (MemberUserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, MemberUserBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return _membBiz;
	}

	// service method
	private GroupBusiness getGroupBusiness(IWContext iwc) {
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
		}
		catch (RemoteException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	// service method
	private UserBusiness getUserBusiness(IWContext iwc) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		}
		catch (RemoteException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	private AccountingBusiness getAccountingBusiness(IWContext iwc) {
		try {
			return (AccountingBusiness) IBOLookup.getServiceInstance(iwc, AccountingBusiness.class);
		}
		catch (RemoteException e) {
			throw new RuntimeException(e.getMessage());
		}
	}	
}