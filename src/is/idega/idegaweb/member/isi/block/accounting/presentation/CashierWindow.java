/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.isi.ISIMemberConstants;
import is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusiness;
import is.idega.idegaweb.member.isi.block.accounting.export.presentation.EntriesNotInBatch;
import is.idega.idegaweb.member.isi.block.accounting.export.presentation.GetFiles;
import is.idega.idegaweb.member.isi.block.accounting.export.presentation.RunLog;
import is.idega.idegaweb.member.isi.block.accounting.export.presentation.SendFiles;
import is.idega.idegaweb.member.isi.block.accounting.export.presentation.Setup;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.presentation.ConnectNetbokhald;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.presentation.SetupNetbokhaldAccountingKeys;

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

	public static final String ROLE_KEY_CASHIER_ADMIN = "CashierAdmin";

	public static final String ROLE_KEY_CASHIER = "Gjaldkeri";

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi.block.accounting";

	public static final String PARAMETER_GROUP_ID = GroupPropertyWindow.PARAMETERSTRING_GROUP_ID;

	public static final String PARAMETER_CLUB_ID = "cashier_club_id";

	public static final String PARAMETER_DIVISION_ID = "cashier_div_id";

	public static final String PARAMETER_USER_ID = "cashier_user_id";

	public static final String PARAMETER_USING_NETBOKHALD = "cashier_using_netbokhald";

	public final static String STYLE_2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";

	public static final String ACTION = "cw_act";

	protected static final String COLOR_DARKEST = "#9F9F9F";

	protected static final String COLOR_MIDDLE = "#DFDFDF";

	protected static final String COLOR_LIGHTEST = "#EFEFEF";

	private static final String ACTION_TARIFF = "isi_acc_cw_act_tariff";

	private static final String ACTION_TARIFF_TYPE = "isi_acc_cw_act_tariff_type";

	private static final String ACTION_MANUAL_ASSESSMENT = "isi_acc_cw_act_ass_man";

	private static final String ACTION_AUTOMATIC_ASSESSMENT = "isi_acc_cw_act_ass_auto";

	private static final String ACTION_CREDITCARD_COMPANY_CONTRACT = "isi_acc_cw_act_cc_contract";

	private static final String ACTION_BANK_CONTRACT = "isi_acc_cw_act_b_contract";

	public static final String ACTION_CASHIER_LEDGER = "isi_acc_cw_act_cash_ledger";

	private static final String ACTION_CANCEL = "isi_acc_cw_act_cc";

	private static final String ACTION_PAYMENT_HISTORY = "cw_act_pay_hist";

	private static final String ACTION_SELECT_PAYMENTS = "isi_acc_cw_act_sel_pay";

	public static final String ACTION_CHECKOUT = "isi_acc_cw_act_pay";

	public static final String ACTION_VISA_FILES = "isi_acc_cw_visa_files";

	private static final String ACTION_REPORTS = "isi_acc_cw_reports";

	private static final String ACTION_NETBOKHALD = "isi_acc_cw_netbokhald";

	public static final String ACTION_NETBOKHALD_KEYS = "isi_acc_cw_netbokhald_keys";

	private static final String STATS_LOCALIZABLE_KEY_NAME = "STATS_LOCALIZABLE_KEY_NAME";

	private static final String STATS_LAYOUT_PARAM = "STATS_LAYOUT_PARAM";

	private static final String STATS_INVOCATION_PARAM = "STATS_INVOCATION_PARAM";

	private static final String STATS_LAYOUT_NAME_FROM_BUNDLE = "STATS_LAYOUT_NAME_FROM_BUNDLE";

	private static final String STATS_INVOCATION_NAME_FROM_BUNDLE = "STATS_INVOCATION_NAME_FROM_BUNDLE";

	private static final String ADMIN_SEND_CREDITCARD_FILES = "isi_acc_cw_send_creditcard_files";

	private static final String ADMIN_GET_CREDITCARD_FILES = "isi_acc_cw_get_creditcard_files";

	private static final String ADMIN_CREDITCARD_SETUP = "isi_acc_cw_creditcard_setup";

	private static final String ADMIN_UNBATCHED_FILES = "isi_acc_cw_unbatched";

	private static final String ADMIN_RUN_LOG = "isi_acc_cw_runlog";

	private static final String HELP_TEXT_KEY = "cashier_window";

	private String PARAMETER_PID = "cw_pid";

	private String PARAMETER_STATUS = "cw_sta";

	private String PARAMETER_SAVE = "cw_sv";

	private String PARAMETER_Group = "cw_group";

	private Group eGroup;

	private Group eClub;

	private Group eDiv;

	private User eUser;

	private IWResourceBundle iwrb;

	private IWBundle iwb;

	private MemberUserBusiness membBiz;

	private String styledLink = "styledLinkGeneral";

	private String helpTextKey = "";

	private String rightBorderTable = "borderRight";

	private String borderTable = "borderAll";

	private boolean isUsingNetbokhald = false;

	private boolean hasCheckedForNetbokhaldUsage = false;

	/**
	 * The default constructor. Creates a window with the size 600x1024. Sets it
	 * as resisable and adds a scrollbar.
	 */
	public CashierWindow() {
		setHeight(600);
		setWidth(1024);
		setResizable(true);
		setScrollbar(true);
	}

	/*
	 * A initialization method. Gets all the parameters that are maintained and
	 * sets the
	 */
	private void init(IWContext iwc) {
		// Checks the group parameter and sets the eGroup variable accordingly.
		String sGroupId = iwc.getParameter(PARAMETER_GROUP_ID);
		if (sGroupId != null) {
			try {
				this.eGroup = getGroupBusiness(iwc).getGroupByGroupID(
						new Integer(sGroupId).intValue());
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (FinderException e1) {
				e1.printStackTrace();
			}
		}

		// Checks the club parameter and sets the eClub variable accordingly.
		String sClubId = iwc.getParameter(PARAMETER_CLUB_ID);
		if (sClubId != null) {
			try {
				this.eClub = getGroupBusiness(iwc).getGroupByGroupID(
						new Integer(sClubId).intValue());
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (FinderException e1) {
				e1.printStackTrace();
			}
		}

		// Checks the division parameter and sets the eDivision variable
		// accordingly.
		String sDivId = iwc.getParameter(PARAMETER_DIVISION_ID);
		if (sDivId != null) {
			try {
				this.eDiv = getGroupBusiness(iwc).getGroupByGroupID(
						new Integer(sDivId).intValue());
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (FinderException e1) {
				e1.printStackTrace();
			}
		}

		// Checks the user parameter and sets the eUser variable accordingly.
		String sUserId = iwc.getParameter(PARAMETER_USER_ID);
		if (sUserId != null && !"".equals(sUserId)) {
			try {
				this.eUser = getUserBusiness(iwc).getUser(new Integer(sUserId));
				iwc.setSessionAttribute(PARAMETER_USER_ID, this.eUser);

			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			this.eUser = (User) iwc.getSessionAttribute(PARAMETER_USER_ID);
		}

		this.iwrb = getResourceBundle(iwc);
		this.iwb = getBundle(iwc);
	}

	private void checkForExternalAccountingSystems() {
		// Checks if the club is using the netbokhald system
		isUsingNetbokhald = isUsingNetbokhald(eClub);
	}

	/*
	 * A method that creates a com.idega.presentation.Table object and sets up
	 * the left hand side menu of the CashierWindow in it. Returns the Table
	 * object.
	 */
	private Table getMenuTable(IWContext iwc) {
		boolean isCashierAdministrator = isCashierAdministrator(iwc);

		Table menu;

		if (!isCashierAdministrator) {
			if (isUsingNetbokhald) {
				menu = new Table(2, 23);
			} else {
				menu = new Table(2, 21);
			}

		} else {
			menu = new Table(2, 6);
		}
		menu.setWidth(Table.HUNDRED_PERCENT);
		menu.setCellpadding(3);
		menu.setCellspacing(0);

		if (!isCashierAdministrator) {
			// Label for the operation that are applied to whole groups.
			Text clubOperations = formatText(
					this.iwrb.getLocalizedString(
							"isi_acc_cashierwindow.club_operations",
							"Club operations"), true);

			// Label for the operations that are applied to individuals.
			Text memberOperations = formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.member_operations",
					"Member operations"), true);

			// Label for the reports.
			Text reports = formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.reports", "Reports"), true);

			Text ledger = formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.ledger", "Ledger"), true);

			LinkContainer editTariffType = new LinkContainer();
			editTariffType.setStyleClass(this.styledLink);
			editTariffType.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.edit_tariff_type",
					"Edit club tariff type (A.12)")));
			addParametersToMenuItems(editTariffType, ACTION_TARIFF_TYPE);

			LinkContainer viewTariff = new LinkContainer();
			viewTariff.setStyleClass(this.styledLink);
			viewTariff.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.view_tariff",
					"View club tariff list")));
			addParametersToMenuItems(viewTariff, ACTION_TARIFF);

			LinkContainer autoAss = new LinkContainer();
			autoAss.setStyleClass(this.styledLink);
			autoAss.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.auto_assessment",
					"Automatic assessment (A.15)")));
			addParametersToMenuItems(autoAss, ACTION_AUTOMATIC_ASSESSMENT);

			LinkContainer ccContract = new LinkContainer();
			ccContract.setStyleClass(this.styledLink);
			ccContract.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.cc_contract",
					"Edit creditcard company contract (A.24)")));
			addParametersToMenuItems(ccContract,
					ACTION_CREDITCARD_COMPANY_CONTRACT);

			LinkContainer bContract = new LinkContainer();
			bContract.setStyleClass(this.styledLink);
			bContract.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.b_contract", "Edit bank contract")));
			addParametersToMenuItems(bContract, ACTION_BANK_CONTRACT);

			LinkContainer manAss = new LinkContainer();
			manAss.setStyleClass(this.styledLink);
			manAss.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.manual_assessment",
					"Manual assessment (A.14)")));
			addParametersToMenuItems(manAss, ACTION_MANUAL_ASSESSMENT);

			LinkContainer paymentHistory = new LinkContainer();
			paymentHistory.setStyleClass(this.styledLink);
			paymentHistory.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.paymentHistory",
					"Payment history (3.11)")));
			addParametersToMenuItems(paymentHistory, ACTION_PAYMENT_HISTORY);

			LinkContainer selectPayments = new LinkContainer();
			selectPayments.setStyleClass(this.styledLink);
			selectPayments.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.select_payments",
					"Select payments for user")));
			addParametersToMenuItems(selectPayments, ACTION_SELECT_PAYMENTS);

			LinkContainer checkOut = new LinkContainer();
			checkOut.setStyleClass(this.styledLink);
			checkOut.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.checkout", "Checkout")));
			addParametersToMenuItems(checkOut, ACTION_CHECKOUT);

			LinkContainer visaFiles = new LinkContainer();
			visaFiles.setStyleClass(this.styledLink);
			visaFiles.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.visa_files", "VISA files")));
			addParametersToMenuItems(visaFiles, ACTION_VISA_FILES);

			/*
			 * LinkContainer nonPayedEntries = new LinkContainer();
			 * nonPayedEntries.setStyleClass(styledLink);
			 * nonPayedEntries.add(formatText(iwrb.getLocalizedString("isi_acc_cashierwindow.nonPayed",
			 * "Non-payed"))); // addParametersToMenuItems(checkOut,
			 * ACTION_CHECKOUT);
			 * 
			 * LinkContainer errorEntries = new LinkContainer();
			 * errorEntries.setStyleClass(styledLink);
			 * errorEntries.add(formatText(iwrb.getLocalizedString("isi_acc_cashierwindow.errorEntries",
			 * "Error entries"))); // addParametersToMenuItems(checkOut,
			 * ACTION_CHECKOUT);
			 */

			// reports
			LinkContainer paymentStatus = new LinkContainer();
			paymentStatus.setStyleClass(this.styledLink);
			paymentStatus.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.paymentStatus",
					"Payment Status (A.29.1)"), false));
			addParametersToMenuItems(paymentStatus, ACTION_REPORTS);
			paymentStatus.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,
					"Invocation-A29.1.xml");
			paymentStatus.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,
					"Layout-A29.1.xml");
			paymentStatus.addParameter(STATS_LOCALIZABLE_KEY_NAME,
					"isi_acc_cashierwindow.paymentStatus");

			LinkContainer paymentOverview = new LinkContainer();
			paymentOverview.setStyleClass(this.styledLink);
			paymentOverview.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.paymentOverview",
					"Payment overview (A.29.2)")));
			addParametersToMenuItems(paymentOverview, ACTION_REPORTS);
			paymentOverview.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,
					"Invocation-A29.2.xml");
			paymentOverview.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,
					"Layout-A29.2.xml");
			paymentOverview.addParameter(STATS_LOCALIZABLE_KEY_NAME,
					"isi_acc_cashierwindow.paymentOverview");

			LinkContainer debtOverview = new LinkContainer();
			debtOverview.setStyleClass(this.styledLink);
			debtOverview.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.debtOverview",
					"Debt overview (A.29.3)")));
			addParametersToMenuItems(debtOverview, ACTION_REPORTS);
			debtOverview.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,
					"Invocation-A29.3.xml");
			debtOverview.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,
					"Layout-A29.3.xml");
			debtOverview.addParameter(STATS_LOCALIZABLE_KEY_NAME,
					"isi_acc_cashierwindow.debtOverview");

			LinkContainer entryOverview = new LinkContainer();
			entryOverview.setStyleClass(this.styledLink);
			entryOverview.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.entryOverview",
					"Entry overview (A.29.4)")));
			addParametersToMenuItems(entryOverview, ACTION_REPORTS);
			entryOverview.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,
					"Invocation-A29.4.xml");
			entryOverview.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,
					"Layout-A29.4.xml");
			entryOverview.addParameter(STATS_LOCALIZABLE_KEY_NAME,
					"isi_acc_cashierwindow.entryOverview");

			LinkContainer latePaymentList = new LinkContainer();
			latePaymentList.setStyleClass(this.styledLink);
			latePaymentList.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.latePaymentList",
					"Late payment list (A.29.5)")));
			addParametersToMenuItems(latePaymentList, ACTION_REPORTS);
			latePaymentList.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,
					"Invocation-A29.5.xml");
			latePaymentList.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,
					"Layout-A29.5.xml");
			latePaymentList.addParameter(STATS_LOCALIZABLE_KEY_NAME,
					"isi_acc_cashierwindow.latePaymentList");

			LinkContainer paymentList = new LinkContainer();
			paymentList.setStyleClass(this.styledLink);
			paymentList.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.paymentList",
					"Payment list (A.29.6)")));
			addParametersToMenuItems(paymentList, ACTION_REPORTS);
			paymentList.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,
					"Invocation-A29.6.xml");
			paymentList.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,
					"Layout-A29.6.xml");
			paymentList.addParameter(STATS_LOCALIZABLE_KEY_NAME,
					"isi_acc_cashierwindow.paymentList");

			LinkContainer ledgerList = new LinkContainer();
			ledgerList.setStyleClass(this.styledLink);
			ledgerList.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.ledgerList", "Ledger list")));
			// ledgerList.addParameter(ACTION,ACTION_CASHIER_LEDGER);
			addParametersToMenuItems(ledgerList, ACTION_CASHIER_LEDGER);

			Text externalAccountingOperations = formatText(
					this.iwrb
							.getLocalizedString(
									"isi_acc_cashierwindow.external_accounting_operations",
									"External accounting systems"), true);

			LinkContainer netbokhald = new LinkContainer();
			netbokhald.setStyleClass(this.styledLink);
			netbokhald.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.netbokahld", "Netbokhald")));
			addParametersToMenuItems(netbokhald, ACTION_NETBOKHALD);

			// add to window
			menu.add(clubOperations, 1, 1);
			menu.add(getHelpWithGrayImage("cashierwindow.clubOperations_help",
					true), 2, 1);
			menu.setRowColor(1, COLOR_MIDDLE);
			menu.add(editTariffType, 1, 2);
			// menu.add(editTariff, 1, 3);
			menu.add(autoAss, 1, 3);
			menu.add(ccContract, 1, 4);
			menu.add(bContract, 1, 5);
			menu.add(viewTariff, 1, 6);

			menu.add(memberOperations, 1, 7);
			menu.add(getHelpWithGrayImage(
					"cashierwindow.memberOperations_help", true), 2, 7);
			menu.setRowColor(7, COLOR_MIDDLE);
			menu.add(manAss, 1, 8);
			menu.add(paymentHistory, 1, 9);
			menu.add(selectPayments, 1, 10);
			menu.add(checkOut, 1, 11);
			menu.add(visaFiles, 1, 12);

			menu.add(reports, 1, 13);
			menu.add(getHelpWithGrayImage("cashierwindow.reports_help", true),
					2, 13);
			menu.setRowColor(13, COLOR_MIDDLE);
			menu.add(paymentStatus, 1, 14);
			menu.add(debtOverview, 1, 15);
			menu.add(paymentOverview, 1, 16);
			menu.add(entryOverview, 1, 17);
			menu.add(paymentList, 1, 18);

			menu.add(ledger, 1, 20);
			menu.add(getHelpWithGrayImage("cashierwindow.ledger_help", true),
					2, 20);
			menu.setRowColor(20, COLOR_MIDDLE);
			menu.add(ledgerList, 1, 21);

			if (isUsingNetbokhald) {
				menu.add(externalAccountingOperations, 1, 22);
				menu.add(getHelpWithGrayImage(
						"cashierwindow.externalaccounting_help", true), 2, 22);
				menu.setRowColor(22, COLOR_MIDDLE);
				menu.add(netbokhald, 1, 23);
			}
		} else {
			Text admin = formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.admin", "Admin"), true);

			LinkContainer adminCreditCardSendFiles = new LinkContainer();
			adminCreditCardSendFiles.setStyleClass(this.styledLink);
			adminCreditCardSendFiles.add(formatText(this.iwrb
					.getLocalizedString(
							"isi_acc_cashierwindow.send_creditcard_files",
							"Send creditcard files")));
			addParametersToMenuItems(adminCreditCardSendFiles,
					ADMIN_SEND_CREDITCARD_FILES);

			LinkContainer adminCreditCardSetup = new LinkContainer();
			adminCreditCardSetup.setStyleClass(this.styledLink);
			adminCreditCardSetup.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.creditcard_setup",
					"Creditcard setup")));
			addParametersToMenuItems(adminCreditCardSetup,
					ADMIN_CREDITCARD_SETUP);

			LinkContainer adminUnbatched = new LinkContainer();
			adminUnbatched.setStyleClass(this.styledLink);
			adminUnbatched.add(formatText(this.iwrb
					.getLocalizedString("isi_acc_cashierwindow.unbatched",
							"Entries not in a batch")));
			addParametersToMenuItems(adminUnbatched, ADMIN_UNBATCHED_FILES);

			LinkContainer adminRunLog = new LinkContainer();
			adminRunLog.setStyleClass(this.styledLink);
			adminRunLog.add(formatText(this.iwrb.getLocalizedString(
					"isi_acc_cashierwindow.runlog", "Run log")));
			addParametersToMenuItems(adminRunLog, ADMIN_RUN_LOG);

			menu.add(admin, 1, 1);
			menu.add(getHelpWithGrayImage("cashierwindow.admin_help", true), 2,
					1);
			menu.setRowColor(1, COLOR_MIDDLE);
			menu.add(adminCreditCardSendFiles, 1, 2);
			// menu.add(adminCreditCardGetFiles, 1, 3);
			menu.add(adminCreditCardSetup, 1, 3);
			menu.add(adminUnbatched, 1, 4);
			menu.add(adminRunLog, 1, 5);
		}

		return menu;
	}

	private void addParametersToMenuItems(LinkContainer menuItem, String action) {
		menuItem.addParameter(ACTION, action);
		menuItem.addParameter(PARAMETER_GROUP_ID, ((Integer) this.eGroup
				.getPrimaryKey()).toString());
		if (this.eUser != null) {
			menuItem.addParameter(PARAMETER_USER_ID, ((Integer) this.eUser
					.getPrimaryKey()).toString());
		}
		if (this.eDiv != null) {
			menuItem.addParameter(PARAMETER_DIVISION_ID, ((Integer) this.eDiv
					.getPrimaryKey()).toString());
		}
		if (this.eClub != null) {
			menuItem.addParameter(PARAMETER_CLUB_ID, ((Integer) this.eClub
					.getPrimaryKey()).toString());
		}
	}

	protected boolean getHasPermissionToViewWindow(IWContext iwc) {
		if (isCashierAdministrator(iwc)) {
			return true;
		}

		if (this.eClub == null) {
			if (this.eGroup == null) {
				Text errorText = new Text(this.iwrb.getLocalizedString(
						"isi_acc_no_group_selected",
						"You must select a group in a club"));
				errorText
						.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

				add(errorText);
				return false;
			} else {
				try {
					this.eClub = getAccountingBusiness(iwc).findClubForGroup(
							this.eGroup);
				} catch (RemoteException e) {
					e.printStackTrace();
					this.eClub = null;
				}

				if (this.eClub == null) {
					Text errorText = new Text(this.iwrb.getLocalizedString(
							"isi_acc_no_group_selected",
							"You must select a group in a club"));
					errorText
							.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

					add(errorText);
					return false;
				}
			}
		}

		if (iwc.isSuperAdmin()) {
			return true;
		}

		if (!iwc.isLoggedOn()) {
			Text errorText = new Text(this.iwrb.getLocalizedString(
					"isi_acc_no_user",
					"There is no user logged in. Please log in and try again."));
			errorText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

			add(errorText);
			return false;
		}

		// System.out.println("Going to get all the cashierGroups for the
		// user");
		User currentUser = iwc.getCurrentUser();
		Collection cashierGroupsInClubForUser = getGroupsForUser(iwc,
				currentUser);
		// System.out.println("Done getting groups.");
		if (cashierGroupsInClubForUser == null
				|| cashierGroupsInClubForUser.isEmpty()) {
			Text errorText = new Text(
					this.iwrb
							.getLocalizedString(
									"isi_acc_user_not_cashier",
									"You are not logged in as a cashier for this club. Please log in as a cashier and try again."));
			errorText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

			add(errorText);
			return false;
		}

		// @TODO what happens when someone is a cashier in two divisions, but
		// not for the whole club -> will only see the first division
		boolean first = true;
		Iterator it = cashierGroupsInClubForUser.iterator();
		while (it.hasNext()) {
			Group cashierGroup = (Group) it.next();
			try {
				Group tmpDiv = getAccountingBusiness(iwc).findDivisionForGroup(
						cashierGroup);
				if (first) {
					this.eDiv = tmpDiv;
					first = false;
				} else {
					if (this.eDiv != null) {
						this.eDiv = tmpDiv;
					}
				}

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	private Collection getGroupsForUser(IWContext iwc, User user) {
		ArrayList userCashierGroups = new ArrayList();
		ArrayList userGroupsInClub = new ArrayList();
		Collection allCashierGroups = iwc.getAccessController()
				.getAllGroupsForRoleKey(ROLE_KEY_CASHIER, iwc);

		try {
			Collection allUserGroups = getUserBusiness(iwc)
					.getUserGroupsDirectlyRelated(user);
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
					Group parent = getAccountingBusiness(iwc).findClubForGroup(
							group);
					if (parent != null && this.eClub.equals(parent)) {
						userGroupsInClub.add(group);
					}
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return userGroupsInClub;
	}

	private boolean isCashierAdministrator(IWContext iwc) {
		User user = iwc.getCurrentUser();
		Collection allCashierAdminGroups = iwc.getAccessController()
				.getAllGroupsForRoleKey(ROLE_KEY_CASHIER_ADMIN, iwc);

		try {
			Collection allUserGroups = getUserBusiness(iwc)
					.getUserGroupsDirectlyRelated(user);
			if (allUserGroups != null && !allUserGroups.isEmpty()) {
				Iterator it = allUserGroups.iterator();
				while (it.hasNext()) {
					Group group = (Group) it.next();
					if (allCashierAdminGroups.contains(group)) {
						return true;
					}
				}
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean isUsingNetbokhald(Group club) {
		String using = null;

		if (club != null) {
			using = club
					.getMetaData(ISIMemberConstants.META_DATA_CLUB_USING_NETBOKHALD);
		}

		return new Boolean(using).booleanValue();
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		init(iwc);

		boolean hasPermission = getHasPermissionToViewWindow(iwc);
		if (!hasPermission) {
			return;
		}

		checkForExternalAccountingSystems();

		StringBuffer title = new StringBuffer(this.iwrb.getLocalizedString(
				"isi_acc_cashier_window", "Cashier Window"));
		if (this.eClub != null) {
			title.append(" - ");
			title.append(this.eClub.getName());
			if (this.eDiv != null) {
				title.append(" / ");
				title.append(this.eDiv.getName());
			}
		}
		setTitle(title.toString());
		addTitle(title.toString(), TITLE_STYLECLASS);

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
		table.setStyleClass(1, 1, this.rightBorderTable);
		table.setStyleClass(this.borderTable);

		// add left menu of links
		Table menuTable = getMenuTable(iwc);

		table.add(menuTable, 1, 1);

		if (action != null) {
			CashierSubWindowTemplate subWindow = null;
			StringBuffer actionTitle = new StringBuffer();

			if (action.equals(ACTION_TARIFF)) {
				actionTitle.append(this.iwrb.getLocalizedString(ACTION_TARIFF,
						"View tariff list"));
				subWindow = new EditTariffList();
				this.helpTextKey = ACTION_TARIFF + "_help";
			} else if (action.equals(ACTION_TARIFF_TYPE)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ACTION_TARIFF_TYPE, "Edit tariff type"));
				subWindow = new EditTariffType();
				this.helpTextKey = ACTION_TARIFF_TYPE + "_help";
			} else if (action.equals(ACTION_MANUAL_ASSESSMENT)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ACTION_MANUAL_ASSESSMENT, "Manual assessment"));
				subWindow = new ManualAssessment();
				this.helpTextKey = ACTION_MANUAL_ASSESSMENT + "_help";
			} else if (action.equals(ACTION_AUTOMATIC_ASSESSMENT)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ACTION_AUTOMATIC_ASSESSMENT, "Automatic assessment"));
				subWindow = new AutomaticAssessment();
				this.helpTextKey = ACTION_AUTOMATIC_ASSESSMENT + "_help";
			} else if (action.equals(ACTION_CREDITCARD_COMPANY_CONTRACT)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ACTION_CREDITCARD_COMPANY_CONTRACT,
						"Club/division credit cardcontract"));
				subWindow = new ClubCreditCardContract();
				this.helpTextKey = ACTION_CREDITCARD_COMPANY_CONTRACT + "_help";
			} else if (action.equals(ACTION_BANK_CONTRACT)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ACTION_BANK_CONTRACT, "Club/division bank contract"));
				subWindow = new ClubBankContract();
				this.helpTextKey = ACTION_BANK_CONTRACT + "_help";
			} else if (action.equals(ACTION_PAYMENT_HISTORY)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ACTION_PAYMENT_HISTORY, "View user payment history"));
				subWindow = new UserPaymentHistory();
				this.helpTextKey = ACTION_PAYMENT_HISTORY + "_help";
			} else if (action.equals(ACTION_SELECT_PAYMENTS)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ACTION_SELECT_PAYMENTS, "Select payments"));
				subWindow = new SelectPayments();
				this.helpTextKey = ACTION_SELECT_PAYMENTS + "_help";
			} else if (action.equals(ACTION_CHECKOUT)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ACTION_CHECKOUT, "Checkout"));
				subWindow = new Checkout();
				this.helpTextKey = ACTION_CHECKOUT + "_help";
			} else if (action.equals(ACTION_VISA_FILES)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ACTION_VISA_FILES, "VISA files"));
				subWindow = new VisaFiles();
				this.helpTextKey = ACTION_VISA_FILES + "_help";
			} else if (action.equals(ACTION_CASHIER_LEDGER)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ACTION_CASHIER_LEDGER, "View cashier ledger"));
				subWindow = new CashierLedgerWindow();
				this.helpTextKey = ACTION_CASHIER_LEDGER + "_help";
			} else if (action.equals(ADMIN_SEND_CREDITCARD_FILES)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ADMIN_SEND_CREDITCARD_FILES, "Send creditcard files"));
				subWindow = new SendFiles();
				this.helpTextKey = ADMIN_SEND_CREDITCARD_FILES + "_help";
			} else if (action.equals(ADMIN_GET_CREDITCARD_FILES)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ADMIN_GET_CREDITCARD_FILES, "Get creditcard files"));
				subWindow = new GetFiles();
				this.helpTextKey = ADMIN_GET_CREDITCARD_FILES + "_help";
			} else if (action.equals(ADMIN_CREDITCARD_SETUP)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ADMIN_CREDITCARD_SETUP, "Creditcard setup"));
				subWindow = new Setup();
				this.helpTextKey = ADMIN_CREDITCARD_SETUP + "_help";
			} else if (action.equals(ADMIN_UNBATCHED_FILES)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ADMIN_UNBATCHED_FILES, "Entries not in a batch"));
				subWindow = new EntriesNotInBatch();
				this.helpTextKey = ADMIN_UNBATCHED_FILES + "_help";
			} else if (action.equals(ADMIN_RUN_LOG)) {
				actionTitle.append(this.iwrb.getLocalizedString(ADMIN_RUN_LOG,
						"Run log"));
				subWindow = new RunLog();
				this.helpTextKey = ADMIN_RUN_LOG + "_help";
			} else if (action.equals(ACTION_REPORTS)) {
				actionTitle.append(this.iwrb.getLocalizedString(ACTION_REPORTS,
						"Reports"));
				ReportGenerator repGen = new ReportGenerator();
				repGen.setParameterToMaintain(ACTION);
				repGen.setParameterToMaintain(STATS_INVOCATION_PARAM);
				repGen.setParameterToMaintain(STATS_LAYOUT_PARAM);
				repGen.setParameterToMaintain(STATS_LAYOUT_NAME_FROM_BUNDLE);
				repGen
						.setParameterToMaintain(STATS_INVOCATION_NAME_FROM_BUNDLE);
				repGen.setParameterToMaintain(STATS_LOCALIZABLE_KEY_NAME);
				repGen.setParameterToMaintain(PARAMETER_GROUP_ID);
				repGen.setParameterToMaintain(PARAMETER_CLUB_ID);
				String invocationKey = iwc.getParameter(STATS_INVOCATION_PARAM);
				String invocationFileName = iwc
						.getParameter(STATS_INVOCATION_NAME_FROM_BUNDLE);
				String layoutKey = iwc.getParameter(STATS_LAYOUT_PARAM);
				String layoutFileName = iwc
						.getParameter(STATS_LAYOUT_NAME_FROM_BUNDLE);
				String localizedNameKey = iwc
						.getParameter(STATS_LOCALIZABLE_KEY_NAME);
				if ((invocationKey != null && this.iwb.getProperty(
						invocationKey, "-1") != null)
						|| invocationFileName != null) {

					if (invocationFileName != null) {
						repGen.setMethodInvocationBundleAndFileName(this.iwb,
								invocationFileName);
					} else {
						Integer invocationICFileID = new Integer(this.iwb
								.getProperty(invocationKey));

						if (invocationICFileID.intValue() > 0) {
							repGen
									.setMethodInvocationICFileID(invocationICFileID);
						}
					}
					if (layoutFileName != null) {
						repGen.setLayoutBundleAndFileName(this.iwb,
								layoutFileName);
					} else if (layoutKey != null
							&& this.iwb.getProperty(layoutKey, "-1") != null) {
						Integer layoutICFileID = new Integer(this.iwb
								.getProperty(layoutKey));
						if (layoutICFileID.intValue() > 0) {
							repGen.setLayoutICFileID(layoutICFileID);
						}
					}
					if (localizedNameKey != null) {
						String reportName = this.iwrb
								.getLocalizedString(localizedNameKey);
						repGen.setReportName(reportName);
						table.add(formatHeadline(reportName), 2, 1); // not a
						// selector
						table.addBreak(2, 1);
					}
				}
				table.add(repGen, 2, 1); // not a selector
			} else if (action.equals(ACTION_NETBOKHALD)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ACTION_NETBOKHALD, "Netbokhald"));
				subWindow = new ConnectNetbokhald();
				this.helpTextKey = ACTION_NETBOKHALD + "_help";
			}
			else if (action.equals(ACTION_NETBOKHALD_KEYS)) {
				actionTitle.append(this.iwrb.getLocalizedString(
						ACTION_NETBOKHALD_KEYS, "Netbokhald keys"));
				subWindow = new SetupNetbokhaldAccountingKeys();
				this.helpTextKey = ACTION_NETBOKHALD_KEYS + "_help";
			}

			if (this.eClub != null) {
				actionTitle.append(" - ");
				actionTitle.append(this.eClub.getName());
			}

			addTitle(actionTitle.toString(), TITLE_STYLECLASS);

			if (subWindow != null) {
				Table helpTable = new Table(1, 1);
				helpTable.setWidth(Table.HUNDRED_PERCENT);
				helpTable.setHeight(15);
				helpTable.setAlignment(1, 1, "right");
				helpTable.add(getHelpWithGrayImage(this.helpTextKey, false), 1,
						1);
				table.add(helpTable, 2, 1);

				subWindow.setClub(this.eClub);
				if (this.eUser != null) {
					subWindow.setUser(this.eUser);
				}
				if (this.eDiv != null) {
					subWindow.setDivision(this.eDiv);
				}

				table.add(subWindow, 2, 1);
			}
		}
		add(table, iwc);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	protected MemberUserBusiness getMemberUserBusiness(IWApplicationContext iwc) {
		if (this.membBiz == null) {
			try {
				this.membBiz = (MemberUserBusiness) com.idega.business.IBOLookup
						.getServiceInstance(iwc, MemberUserBusiness.class);
			} catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return this.membBiz;
	}

	// service method
	private GroupBusiness getGroupBusiness(IWContext iwc) {
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(iwc,
					GroupBusiness.class);
		} catch (RemoteException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	// service method
	private UserBusiness getUserBusiness(IWContext iwc) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwc,
					UserBusiness.class);
		} catch (RemoteException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	private AccountingBusiness getAccountingBusiness(IWContext iwc) {
		try {
			return (AccountingBusiness) IBOLookup.getServiceInstance(iwc,
					AccountingBusiness.class);
		} catch (RemoteException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}