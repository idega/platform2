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
public class CashierWindow extends StyledIWAdminWindow  {

    public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi.block.accounting";

    public static final String PARAMETER_GROUP_ID = GroupPropertyWindow.PARAMETERSTRING_GROUP_ID;

    public static final String PARAMETER_CLUB_ID = "cashier_club_id";

    public static final String PARAMETER_DIVISION_ID = "cashier_div_id";

    public static final String PARAMETER_USER_ID = "cashier_user_id";

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

    private static final String ACTION_CASHIER_LEDGER = "isi_acc_cw_act_cash_ledger";

    private static final String ACTION_CANCEL = "isi_acc_cw_act_cc";

    private static final String ACTION_PAYMENT_HISTORY = "cw_act_pay_hist";

    private static final String ACTION_SELECT_PAYMENTS = "isi_acc_cw_act_sel_pay";

    public static final String ACTION_CHECKOUT = "isi_acc_cw_act_pay";

    private static final String ACTION_REPORTS = "isi_acc_cw_reports";

    private static final String STATS_LOCALIZABLE_KEY_NAME = "STATS_LOCALIZABLE_KEY_NAME";

    private static final String STATS_LAYOUT_PARAM = "STATS_LAYOUT_PARAM";

    private static final String STATS_INVOCATION_PARAM = "STATS_INVOCATION_PARAM";

    private static final String STATS_LAYOUT_NAME_FROM_BUNDLE = "STATS_LAYOUT_NAME_FROM_BUNDLE";

    private static final String STATS_INVOCATION_NAME_FROM_BUNDLE = "STATS_INVOCATION_NAME_FROM_BUNDLE";

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
        //Checks the group parameter and sets the eGroup variable accordingly.
        String sGroupId = iwc.getParameter(PARAMETER_GROUP_ID);
        if (sGroupId != null) {
            try {
                eGroup = getGroupBusiness(iwc).getGroupByGroupID(
                        new Integer(sGroupId).intValue());
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            } catch (FinderException e1) {
                e1.printStackTrace();
            }
        }

        //Checks the club parameter and sets the eClub variable accordingly.
        String sClubId = iwc.getParameter(PARAMETER_CLUB_ID);
        if (sClubId != null) {
            try {
                eClub = getGroupBusiness(iwc).getGroupByGroupID(
                        new Integer(sClubId).intValue());
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            } catch (FinderException e1) {
                e1.printStackTrace();
            }
        }

        //Checks the division parameter and sets the eDivision variable
        // accordingly.
        String sDivId = iwc.getParameter(PARAMETER_DIVISION_ID);
        if (sDivId != null) {
            try {
                eDiv = getGroupBusiness(iwc).getGroupByGroupID(
                        new Integer(sDivId).intValue());
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            } catch (FinderException e1) {
                e1.printStackTrace();
            }
        }

        //Checks the user parameter and sets the eUser variable accordingly.
        String sUserId = iwc.getParameter(PARAMETER_USER_ID);
        if (sUserId != null && !"".equals(sUserId)) {
            try {
                eUser = getUserBusiness(iwc).getUser(new Integer(sUserId));
                iwc.setSessionAttribute(PARAMETER_USER_ID, eUser);

            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            eUser = (User) iwc.getSessionAttribute(PARAMETER_USER_ID);
        }

        iwrb = getResourceBundle(iwc);
        iwb = getBundle(iwc);
    }

    /*
     * A method that creates a com.idega.presentation.Table object and sets up
     * the left hand side menu of the CashierWindow in it. Returns the Table
     * object.
     */
    private Table getMenuTable(IWContext iwc) {
        Table menu = new Table(2, 24);
        menu.setWidth(Table.HUNDRED_PERCENT);
        menu.setCellpadding(3);
        menu.setCellspacing(0);

        //Label for the operation that are applied to whole groups.
        Text clubOperations = formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.club_operations", "Club operations"),
                true);

        //Label for the operations that are applied to individuals.
        Text memberOperations = formatText(
                iwrb.getLocalizedString(
                        "isi_acc_cashierwindow.member_operations",
                        "Member operations"), true);

        //Label for the reports.
        Text reports = formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.reports", "Reports"), true);

        Text ledger = formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.ledger", "Ledger"), true);

        LinkContainer editTariffType = new LinkContainer();
        editTariffType.setStyleClass(styledLink);
        editTariffType.add(formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.edit_tariff_type",
                "Edit club tariff type (A.12)")));
        addParametersToMenuItems(editTariffType, ACTION_TARIFF_TYPE);

        LinkContainer editTariff = new LinkContainer();
        editTariff.setStyleClass(styledLink);
        editTariff.add(formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.edit_tariff",
                "Edit club tariff list (A.12)")));
        addParametersToMenuItems(editTariff, ACTION_TARIFF);

        LinkContainer autoAss = new LinkContainer();
        autoAss.setStyleClass(styledLink);
        autoAss.add(formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.auto_assessment",
                "Automatic assessment (A.15)")));
        addParametersToMenuItems(autoAss, ACTION_AUTOMATIC_ASSESSMENT);

        LinkContainer ccContract = new LinkContainer();
        ccContract.setStyleClass(styledLink);
        ccContract.add(formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.cc_contract",
                "Edit creditcard company contract (A.24)")));
        addParametersToMenuItems(ccContract, ACTION_CREDITCARD_COMPANY_CONTRACT);

        LinkContainer manAss = new LinkContainer();
        manAss.setStyleClass(styledLink);
        manAss.add(formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.manual_assessment",
                "Manual assessment (A.14)")));
        addParametersToMenuItems(manAss, ACTION_MANUAL_ASSESSMENT);

        LinkContainer paymentHistory = new LinkContainer();
        paymentHistory.setStyleClass(styledLink);
        paymentHistory.add(formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.paymentHistory",
                "Payment history (3.11)")));
        addParametersToMenuItems(paymentHistory, ACTION_PAYMENT_HISTORY);

        LinkContainer selectPayments = new LinkContainer();
        selectPayments.setStyleClass(styledLink);
        selectPayments.add(formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.select_payments",
                "Select payments for user")));
        addParametersToMenuItems(selectPayments, ACTION_SELECT_PAYMENTS);

        LinkContainer checkOut = new LinkContainer();
        checkOut.setStyleClass(styledLink);
        checkOut.add(formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.checkout", "Checkout")));
        addParametersToMenuItems(checkOut, ACTION_CHECKOUT);

        //reports
        LinkContainer paymentStatus = new LinkContainer();
        paymentStatus.setStyleClass(styledLink);
        paymentStatus.add(formatText(
                iwrb.getLocalizedString("isi_acc_cashierwindow.paymentStatus",
                        "Payment Status (A.29.1)"), false));
        addParametersToMenuItems(paymentStatus, ACTION_REPORTS);
        paymentStatus.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,
                "Invocation-A29.1.xml");
        paymentStatus.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,
                "Layout-A29.1.xml");
        paymentStatus.addParameter(STATS_LOCALIZABLE_KEY_NAME,
                "isi_acc_cashierwindow.paymentStatus");

        LinkContainer paymentOverview = new LinkContainer();
        paymentOverview.setStyleClass(styledLink);
        paymentOverview.add(formatText(iwrb.getLocalizedString(
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
        debtOverview.setStyleClass(styledLink);
        debtOverview.add(formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.debtOverview", "Debt overview (A.29.3)")));
        addParametersToMenuItems(debtOverview, ACTION_REPORTS);
        debtOverview.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,
                "Invocation-A29.3.xml");
        debtOverview.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,
                "Layout-A29.3.xml");
        debtOverview.addParameter(STATS_LOCALIZABLE_KEY_NAME,
                "isi_acc_cashierwindow.debtOverview");

        LinkContainer entryOverview = new LinkContainer();
        entryOverview.setStyleClass(styledLink);
        entryOverview
                .add(formatText(iwrb.getLocalizedString(
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
        latePaymentList.setStyleClass(styledLink);
        latePaymentList.add(formatText(iwrb.getLocalizedString(
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
        paymentList.setStyleClass(styledLink);
        paymentList.add(formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.paymentList", "Payment list (A.29.6)")));
        addParametersToMenuItems(paymentList, ACTION_REPORTS);
        paymentList.addParameter(STATS_INVOCATION_NAME_FROM_BUNDLE,
                "Invocation-A29.6.xml");
        paymentList.addParameter(STATS_LAYOUT_NAME_FROM_BUNDLE,
                "Layout-A29.6.xml");
        paymentList.addParameter(STATS_LOCALIZABLE_KEY_NAME,
                "isi_acc_cashierwindow.paymentList");

        LinkContainer ledgerList = new LinkContainer();
        ledgerList.setStyleClass(styledLink);
        ledgerList.add(formatText(iwrb.getLocalizedString(
                "isi_acc_cashierwindow.ledgerList", "Ledger list")));
        //       ledgerList.addParameter(ACTION,ACTION_CASHIER_LEDGER);
        addParametersToMenuItems(ledgerList, ACTION_CASHIER_LEDGER);

        //add to window
        menu.add(clubOperations, 1, 1);
        menu
                .add(getHelpWithGrayImage("cashierwindow.clubOperations_help",
                        true), 2, 1);
        menu.setRowColor(1, COLOR_MIDDLE);
        menu.add(editTariffType, 1, 2);
        menu.add(editTariff, 1, 3);
        menu.add(autoAss, 1, 4);
        menu.add(ccContract, 1, 5);

        menu.add(memberOperations, 1, 7);
        menu.add(getHelpWithGrayImage("cashierwindow.memberOperations_help",
                true), 2, 7);
        menu.setRowColor(7, COLOR_MIDDLE);
        menu.add(manAss, 1, 8);
        menu.add(paymentHistory, 1, 9);
        menu.add(selectPayments, 1, 10);
        menu.add(checkOut, 1, 11);

        menu.add(reports, 1, 13);
        menu.add(getHelpWithGrayImage("cashierwindow.reports_help", true), 2,
                13);
        menu.setRowColor(13, COLOR_MIDDLE);
        menu.add(paymentStatus, 1, 14);
        menu.add(paymentOverview, 1, 15);
        menu.add(debtOverview, 1, 16);
        menu.add(entryOverview, 1, 17);
        menu.add(latePaymentList, 1, 18);
        menu.add(paymentList, 1, 19);

        menu.add(ledger, 1, 21);
        menu
                .add(getHelpWithGrayImage("cashierwindow.ledger_help", true),
                        2, 21);
        menu.setRowColor(21, COLOR_MIDDLE);
        menu.add(ledgerList, 1, 22);

        return menu;
    }

    private void addParametersToMenuItems(LinkContainer menuItem, String action) {
        menuItem.addParameter(ACTION, action);
        menuItem.addParameter(PARAMETER_GROUP_ID, ((Integer) eGroup
                .getPrimaryKey()).toString());
        if (eUser != null) {
            menuItem.addParameter(PARAMETER_USER_ID, ((Integer) eUser
                    .getPrimaryKey()).toString());
        }
        if (eDiv != null) {
            menuItem.addParameter(PARAMETER_DIVISION_ID, ((Integer) eDiv
                    .getPrimaryKey()).toString());
        }
        if (eClub != null) {
            menuItem.addParameter(PARAMETER_CLUB_ID, ((Integer) eClub
                    .getPrimaryKey()).toString());
        }
    }

    protected boolean getHasPermissionToViewWindow(IWContext iwc) {
        if (eClub == null) {
            if (eGroup == null) {
                Text errorText = new Text(iwrb.getLocalizedString(
                        "isi_acc_no_group_selected",
                        "You must select a group in a club"));
                errorText
                        .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

                add(errorText);
                return false;
            } else {
                try {
                    eClub = getAccountingBusiness(iwc).findClubForGroup(eGroup);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    eClub = null;
                }

                if (eClub == null) {
                    Text errorText = new Text(iwrb.getLocalizedString(
                            "isi_acc_no_group_selected",
                            "You must select a group in a club"));
                    errorText
                            .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

                    add(errorText);
                    return false;
                }
            }
        }

        if (iwc.isSuperAdmin()) { return true; }

        if (!iwc.isLoggedOn()) {
            Text errorText = new Text(iwrb.getLocalizedString(
                    "isi_acc_no_user",
                    "There is no user logged in. Please log in and try again."));
            errorText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

            add(errorText);
            return false;
        }

        //System.out.println("Going to get all the cashierGroups for the
        // user");
        User currentUser = iwc.getCurrentUser();
        Collection cashierGroupsInClubForUser = getGroupsForUser(iwc,
                currentUser);
        //System.out.println("Done getting groups.");
        if (cashierGroupsInClubForUser == null
                || cashierGroupsInClubForUser.isEmpty()) {
            Text errorText = new Text(
                    iwrb
                            .getLocalizedString(
                                    "isi_acc_user_not_cashier",
                                    "You are not logged in as a cashier for this club. Please log in as a cashier and try again."));
            errorText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

            add(errorText);
            return false;
        }

        //@TODO what happens when someone is a cashier in two divisions, but
        // not for the whole club -> will only see the first division
        boolean first = true;
        Iterator it = cashierGroupsInClubForUser.iterator();
        while (it.hasNext()) {
            Group cashierGroup = (Group) it.next();
            try {
                Group tmpDiv = getAccountingBusiness(iwc).findDivisionForGroup(
                        cashierGroup);
                if (first) {
                    eDiv = tmpDiv;
                    first = false;
                } else {
                    if (eDiv != null) {
                        eDiv = tmpDiv;
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
                .getAllGroupsForRoleKey("Gjaldkeri", iwc);

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
                    if (parent != null && eClub.equals(parent)) {
                        userGroupsInClub.add(group);
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return userGroupsInClub;
    }

    public void main(IWContext iwc) throws Exception {
        super.main(iwc);
        init(iwc);

        boolean hasPermission = getHasPermissionToViewWindow(iwc);
        if (!hasPermission) { return; }

        setTitle("Cashier window");
        StringBuffer title = new StringBuffer(iwrb.getLocalizedString(
                "isi_acc_cashier_window", "Cashier Window"));
        if (eClub != null) {
            title.append(" - ");
            title.append(eClub.getName());
            if (eDiv != null) {
                title.append(" / ");
                title.append(eDiv.getName());
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


        if (action != null) {
            CashierSubWindowTemplate subWindow = null;
            StringBuffer actionTitle = new StringBuffer();
            if (action.equals(ACTION_TARIFF)) {
                actionTitle.append(iwrb.getLocalizedString(ACTION_TARIFF,
                        "Edit tariff list"));
                subWindow = new EditTariffList();
                helpTextKey = ACTION_TARIFF + "_help";
            } else if (action.equals(ACTION_TARIFF_TYPE)) {
                actionTitle.append(iwrb.getLocalizedString(ACTION_TARIFF_TYPE,
                        "Edit tariff type"));
                subWindow = new EditTariffType();
                helpTextKey = ACTION_TARIFF_TYPE + "_help";
            } else if (action.equals(ACTION_MANUAL_ASSESSMENT)) {
                actionTitle.append(iwrb.getLocalizedString(
                        ACTION_MANUAL_ASSESSMENT, "Manual assessment"));
                subWindow = new ManualAssessment();
                helpTextKey = ACTION_MANUAL_ASSESSMENT + "_help";
            } else if (action.equals(ACTION_AUTOMATIC_ASSESSMENT)) {
                actionTitle.append(iwrb.getLocalizedString(
                        ACTION_AUTOMATIC_ASSESSMENT, "Automatic assessment"));
                subWindow = new AutomaticAssessment();
                helpTextKey = ACTION_AUTOMATIC_ASSESSMENT + "_help";
            } else if (action.equals(ACTION_CREDITCARD_COMPANY_CONTRACT)) {
                actionTitle.append(iwrb.getLocalizedString(
                        ACTION_CREDITCARD_COMPANY_CONTRACT,
                        "Club/division credit cardcontract"));
                subWindow = new ClubCreditCardContract();
                helpTextKey = ACTION_CREDITCARD_COMPANY_CONTRACT + "_help";
            } else if (action.equals(ACTION_PAYMENT_HISTORY)) {
                actionTitle.append(iwrb.getLocalizedString(
                        ACTION_PAYMENT_HISTORY, "View user payment history"));
                subWindow = new UserPaymentHistory();
                helpTextKey = ACTION_PAYMENT_HISTORY + "_help";
            } else if (action.equals(ACTION_SELECT_PAYMENTS)) {
                actionTitle.append(iwrb.getLocalizedString(
                        ACTION_SELECT_PAYMENTS, "Select payments"));
                subWindow = new SelectPayments();
                helpTextKey = ACTION_SELECT_PAYMENTS + "_help";
            } else if (action.equals(ACTION_CHECKOUT)) {
                actionTitle.append(iwrb.getLocalizedString(ACTION_CHECKOUT,
                        "Checkout"));
                subWindow = new Checkout();
                helpTextKey = ACTION_CHECKOUT + "_help";
            } else if (action.equals(ACTION_CASHIER_LEDGER)) {
                actionTitle.append(iwrb.getLocalizedString(
                        ACTION_CASHIER_LEDGER, "View cashier ledger"));
                subWindow = new CashierLedgerWindow();
                helpTextKey = ACTION_CASHIER_LEDGER + "_help";
            } else if (action.equals(ACTION_REPORTS)) {
            	actionTitle.append(iwrb.getLocalizedString(
            			ACTION_REPORTS, "Reports"));
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
                if ((invocationKey != null && iwb.getProperty(invocationKey,
                        "-1") != null)
                        || invocationFileName != null) {

                    if (invocationFileName != null) {
                        repGen.setMethodInvocationBundleAndFileName(iwb,
                                invocationFileName);
                    } else {
                        Integer invocationICFileID = new Integer(iwb
                                .getProperty(invocationKey));

                        if (invocationICFileID.intValue() > 0) {
                            repGen
                                    .setMethodInvocationICFileID(invocationICFileID);
                        }
                    }
                    if (layoutFileName != null) {
                        repGen.setLayoutBundleAndFileName(iwb, layoutFileName);
                    } else if (layoutKey != null
                            && iwb.getProperty(layoutKey, "-1") != null) {
                        Integer layoutICFileID = new Integer(iwb
                                .getProperty(layoutKey));
                        if (layoutICFileID.intValue() > 0)
                                repGen.setLayoutICFileID(layoutICFileID);
                    }
                    if (localizedNameKey != null) {
                        String reportName = iwrb
                                .getLocalizedString(localizedNameKey);
                        repGen.setReportName(reportName);
                        table.add(formatHeadline(reportName), 2, 1); //not a
                        // selector
                        table.addBreak(2, 1);
                    }
                }
                table.add(repGen, 2, 1); //not a selector
            }

            if (eClub != null) {
                actionTitle.append(" - ");
                actionTitle.append(eClub.getName());
            }
            
            addTitle(actionTitle.toString(),
                    IWConstants.BUILDER_FONT_STYLE_TITLE);

            if (subWindow != null) {
                Table helpTable = new Table(1, 1);
                helpTable.setWidth(Table.HUNDRED_PERCENT);
                helpTable.setHeight(15);
                helpTable.setAlignment(1, 1, "right");
                helpTable.add(getHelpWithGrayImage(helpTextKey, false), 1, 1);
                table.add(helpTable, 2, 1);

                subWindow.setClub(eClub);
                if (eUser != null) subWindow.setUser(eUser);
                if (eDiv != null) subWindow.setDivision(eDiv);

                table.add(subWindow, 2, 1);
            }
        }
        add(table, iwc);
    }

    public String getBundleIdentifier() {
        return IW_BUNDLE_IDENTIFIER;
    }

    protected MemberUserBusiness getMemberUserBusiness(IWApplicationContext iwc) {
        if (membBiz == null) {
            try {
                membBiz = (MemberUserBusiness) com.idega.business.IBOLookup
                        .getServiceInstance(iwc, MemberUserBusiness.class);
            } catch (java.rmi.RemoteException rme) {
                throw new RuntimeException(rme.getMessage());
            }
        }
        return membBiz;
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