/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.business.MemberUserBusiness;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.LinkContainer;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.presentation.GroupPropertyWindow;
import com.idega.user.presentation.StyledIWAdminWindow;

/**
 * @author palli
 */
public class CashierWindow extends StyledIWAdminWindow {
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi.block.accounting";
	public static final String PARAMETER_GROUP_ID = GroupPropertyWindow.PARAMETERSTRING_GROUP_ID;
	public static final String PARAMETER_USER_ID = "cashier_user_id";
	public final static String STYLE_2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";
	
//	public static final String SELECTED_GROUP_PROVIDER_PRESENTATION_STATE_ID_KEY = "selected_group_mm_id_key";

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

	private String PARAMETER_PID = "cw_pid";
	private String PARAMETER_STATUS = "cw_sta";
	private String PARAMETER_SAVE = "cw_sv";

	private static final String HELP_TEXT_KEY = "cashier_window";

	private Group _group;
	private User _user;
	private IWResourceBundle _iwrb;
	private IWBundle _iwb;
	private MemberUserBusiness _membBiz;

	private String styledLink = "styledLinkGeneral";

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
		System.out.println("CashierWindow : sGroupId = " + sGroupId);
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
		
		String sUserId = iwc.getParameter(PARAMETER_USER_ID);
		System.out.println("CashierWindow : sUserId = " + sUserId);
		if (sUserId != null) {
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
		Table menu = new Table(1, 21);
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
		editTariffType.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		if (_user != null) {
			editTariffType.addParameter(PARAMETER_USER_ID,((Integer)_user.getPrimaryKey()).toString());
		}
		
		LinkContainer editTariff = new LinkContainer();
		editTariff.setStyleClass(styledLink);
		editTariff.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.edit_tariff", "Edit club tariff list (A.12)")));
		editTariff.addParameter(ACTION, ACTION_TARIFF);
		editTariff.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		if (_user != null) {
			editTariff.addParameter(PARAMETER_USER_ID,((Integer)_user.getPrimaryKey()).toString());
		}
		
		LinkContainer autoAss = new LinkContainer();
		autoAss.setStyleClass(styledLink);
		autoAss.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.auto_assessment", "Automatic assessment (A.15)")));
		autoAss.addParameter(ACTION, ACTION_AUTOMATIC_ASSESSMENT);
		autoAss.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		if (_user != null) {
			autoAss.addParameter(PARAMETER_USER_ID,((Integer)_user.getPrimaryKey()).toString());
		}
		
		LinkContainer ccContract = new LinkContainer();
		ccContract.setStyleClass(styledLink);
		ccContract.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.cc_contract", "Edit creditcard company contract (A.24)")));
		ccContract.addParameter(ACTION, ACTION_CREDITCARD_COMPANY_CONTRACT);
		ccContract.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		if (_user != null) {
			ccContract.addParameter(PARAMETER_USER_ID,((Integer)_user.getPrimaryKey()).toString());
		}
		
		LinkContainer selectUser = new LinkContainer();
		selectUser.setStyleClass(styledLink);
		selectUser.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.select_user", "Select user to work with")));
		selectUser.addParameter(ACTION, ACTION_SELECT_USER);
		selectUser.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		if (_user != null) {
			selectUser.addParameter(PARAMETER_USER_ID,((Integer)_user.getPrimaryKey()).toString());
		}
		
		LinkContainer manAss = new LinkContainer();
		manAss.setStyleClass(styledLink);
		manAss.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.manual_assessment", "Manual assessment (A.14)")));
		manAss.addParameter(ACTION, ACTION_MANUAL_ASSESSMENT);
		manAss.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		if (_user != null) {
			manAss.addParameter(PARAMETER_USER_ID,((Integer)_user.getPrimaryKey()).toString());
		}
		
		LinkContainer insertContract = new LinkContainer();
		insertContract.setStyleClass(styledLink);
		insertContract.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.insert_contract", "Insert/edit member contract (new/A.10)")));
		insertContract.addParameter(ACTION, ACTION_CONTRACT);
		insertContract.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		if (_user != null) {
			insertContract.addParameter(PARAMETER_USER_ID,((Integer)_user.getPrimaryKey()).toString());
		}
		
		LinkContainer registerPayment = new LinkContainer();
		registerPayment.setStyleClass(styledLink);
		registerPayment.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.payment", "Register payment (A.11)")));
		registerPayment.addParameter(ACTION, ACTION_PAYMENT);
		registerPayment.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		if (_user != null) {
			registerPayment.addParameter(PARAMETER_USER_ID,((Integer)_user.getPrimaryKey()).toString());
		}
		
		LinkContainer paymentHistory = new LinkContainer();
		paymentHistory.setStyleClass(styledLink);
		paymentHistory.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.paymentHistory", "Payment history (3.11)")));
		paymentHistory.addParameter(ACTION, ACTION_PAYMENT_HISTORY);
		paymentHistory.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		if (_user != null) {
			paymentHistory.addParameter(PARAMETER_USER_ID,((Integer)_user.getPrimaryKey()).toString());
		}
		
		LinkContainer memberCreditCard = new LinkContainer();
		memberCreditCard.setStyleClass(styledLink);
		memberCreditCard.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.memberCreditCard", "Member credit card info (A.10)")));
		memberCreditCard.addParameter(ACTION, ACTION_MEMBER_CREDITCARD);
		memberCreditCard.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		if (_user != null) {
			memberCreditCard.addParameter(PARAMETER_USER_ID,((Integer)_user.getPrimaryKey()).toString());
		}
		
		//reports
		LinkContainer paymentStatus = new LinkContainer();
		paymentStatus.setStyleClass(styledLink);
		paymentStatus.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.paymentStatus", "Payment status (A.29)")));
		paymentStatus.addParameter(ACTION, REPORT_PAYMENT_STATUS);
		paymentStatus.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		
		LinkContainer paymentOverview = new LinkContainer();
		paymentOverview.setStyleClass(styledLink);
		paymentOverview.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.paymentOverview", "Payment overview (A.29)")));
		paymentOverview.addParameter(ACTION, REPORT_PAYMENT_OVERVIEW);
		paymentOverview.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());

		LinkContainer debtOverview = new LinkContainer();
		debtOverview.setStyleClass(styledLink);
		debtOverview.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.debtOverview", "Debt overview (A.29)")));
		debtOverview.addParameter(ACTION, REPORT_DEBT_OVERVIEW);
		debtOverview.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		
		LinkContainer entryOverview = new LinkContainer();
		entryOverview.setStyleClass(styledLink);
		entryOverview.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.entryOverview", "Entry overview (A.29)")));
		entryOverview.addParameter(ACTION, REPORT_ENTRY_OVERVIEW);
		entryOverview.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		
		LinkContainer latePaymentList = new LinkContainer();
		latePaymentList.setStyleClass(styledLink);
		latePaymentList.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.latePaymentList", "Late payment list (A.29)")));
		latePaymentList.addParameter(ACTION, REPORT_LATE_PAYMENT_LIST);
		latePaymentList.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		
		LinkContainer paymentList = new LinkContainer();
		paymentList.setStyleClass(styledLink);
		paymentList.add(formatText(_iwrb.getLocalizedString("isi_acc_cashierwindow.paymentList", "Payment list (A.29)")));
		paymentList.addParameter(ACTION, REPORT_LATE_PAYMENT_LIST);
		paymentList.addParameter(PARAMETER_GROUP_ID,((Integer)_group.getPrimaryKey()).toString());
		
		//add to window
		menu.add(clubOperations, 1, 1);
		menu.setRowColor(1, COLOR_MIDDLE);
		menu.add(editTariffType, 1, 2);
		menu.add(editTariff, 1, 3);
		menu.add(autoAss, 1, 4);
		menu.add(ccContract, 1, 5);
		
		menu.add(memberOperations, 1, 7);
		menu.setRowColor(7, COLOR_MIDDLE);
		menu.add(selectUser, 1, 8);
		menu.add(manAss, 1, 9);
		menu.add(insertContract, 1, 10);
		menu.add(registerPayment, 1, 11);
		menu.add(paymentHistory, 1, 12);
		menu.add(memberCreditCard, 1, 13);
		
		menu.add(reports, 1, 15);
		menu.setRowColor(15, COLOR_MIDDLE);
		menu.add(paymentStatus, 1, 16);
		menu.add(paymentOverview, 1, 17);
		menu.add(debtOverview, 1, 18);
		menu.add(entryOverview, 1, 19);
		menu.add(latePaymentList, 1, 20);
		menu.add(paymentList, 1, 21);
		
		return menu;
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		init(iwc);
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setTitle("Cashier window");
		addTitle(_iwrb.getLocalizedString("isi_acc_cashier_window", "Cashier Window"), IWConstants.BUILDER_FONT_STYLE_TITLE);

		String action = iwc.getParameter(ACTION);

		Table table = new Table(2, 1);
		table.setWidthAndHeightToHundredPercent();
		table.setColumnColor(1, COLOR_LIGHTEST);
		table.setColumnWidth(1, "200");
		table.setRowHeight(1, Table.HUNDRED_PERCENT);
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);
		table.setCellpaddingAndCellspacing(0);

		//add left menu of links
		Table menuTable = getMenuTable(iwc);

		table.add(menuTable, 1, 1);

		if (action != null) {
			CashierSubWindowTemplate subWindow = null;

			if (action.equals(ACTION_TARIFF)) {
				addTitle(iwrb.getLocalizedString(ACTION_TARIFF, "Edit tariff list"), IWConstants.BUILDER_FONT_STYLE_TITLE);
				subWindow = new EditTariffList();
			}
			else if (action.equals(ACTION_TARIFF_TYPE)) {
				addTitle(iwrb.getLocalizedString(ACTION_TARIFF_TYPE, "Edit tariff type"), IWConstants.BUILDER_FONT_STYLE_TITLE);
				subWindow = new EditTariffType();
			}
			else if (action.equals(ACTION_MANUAL_ASSESSMENT)) {
				addTitle(iwrb.getLocalizedString(ACTION_MANUAL_ASSESSMENT, "Manual assessment"), IWConstants.BUILDER_FONT_STYLE_TITLE);
				subWindow = new ManualAssessment();
			}
			else if (action.equals(ACTION_AUTOMATIC_ASSESSMENT)) {
				addTitle(iwrb.getLocalizedString(ACTION_AUTOMATIC_ASSESSMENT, "Automatic assessment"), IWConstants.BUILDER_FONT_STYLE_TITLE);
				subWindow = new AutomaticAssessment();
			}
			else if (action.equals(ACTION_CREDITCARD_COMPANY_CONTRACT)) {
				addTitle(iwrb.getLocalizedString(ACTION_CREDITCARD_COMPANY_CONTRACT, "Club/division credit cardcontract"), IWConstants.BUILDER_FONT_STYLE_TITLE);
				subWindow = new ClubCreditCardContract();
			}
			else if (action.equals(ACTION_SELECT_USER)) {
				addTitle(iwrb.getLocalizedString(ACTION_SELECT_USER, "Select user"), IWConstants.BUILDER_FONT_STYLE_TITLE);
				subWindow = new SelectUser();
			}
			else if (action.equals(ACTION_CONTRACT)) {
				addTitle(iwrb.getLocalizedString(ACTION_CONTRACT, "Create user contract"), IWConstants.BUILDER_FONT_STYLE_TITLE);
				subWindow = new UserContract();
			}
			else if (action.equals(ACTION_PAYMENT)) {
				addTitle(iwrb.getLocalizedString(ACTION_PAYMENT, "Enter user payment"), IWConstants.BUILDER_FONT_STYLE_TITLE);
				subWindow = new UserPayment();
			}
			else if (action.equals(ACTION_PAYMENT_HISTORY)) {
				addTitle(iwrb.getLocalizedString(ACTION_PAYMENT_HISTORY, "View user payment history"), IWConstants.BUILDER_FONT_STYLE_TITLE);
				subWindow = new UserPaymentHistory();
			}
			else if (action.equals(ACTION_MEMBER_CREDITCARD)) {
				addTitle(iwrb.getLocalizedString(ACTION_MEMBER_CREDITCARD, "Edit users creditcard info"), IWConstants.BUILDER_FONT_STYLE_TITLE);
				subWindow = new EditTariffList();
			}

			if (subWindow != null) {
				subWindow.setClub(_group);
				table.add(subWindow, 2, 1);
			}
		}

		add(table, iwc);
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
}