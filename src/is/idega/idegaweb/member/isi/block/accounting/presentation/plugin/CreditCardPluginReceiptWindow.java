/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.presentation.plugin;

import is.idega.idegaweb.member.isi.block.accounting.business.FinanceExtraBasketInfo;
import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound;
import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundHome;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.block.basket.business.BasketBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.contact.data.Phone;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.PrintButton;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CreditCardPluginReceiptWindow extends StyledIWAdminWindow {

    public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi.block.accounting";

    protected static final String LABEL_CLUB = "isi_acc_dcrw_club";

    protected static final String LABEL_DATE = "isi_acc_dcrw_date";

    protected static final String LABEL_CASHIER = "isi_acc_dcrw_cashier";

    protected static final String LABEL_PHONE = "isi_acc_dcrw_phone";

    protected static final String LABEL_DIVISION = "isi_acc_dcrw_div";

    protected static final String LABEL_GROUP = "isi_acc_dcrw_group";

    protected static final String LABEL_INFO = "isi_acc_dcrw_info";

    protected static final String LABEL_USER = "isi_acc_dcrw_user";

    protected static final String LABEL_AMOUNT = "isi_acc_dcrw_amount";

    protected static final String LABEL_AMOUNT_PAID = "isi_acc_dcrw_amount_paid";

    protected static final String LABEL_SUM = "isi_acc_dcrw_sum";

    protected static final String LABEL_TITLE = "isi_acc_dcrw_window";

    private String backTableStyle = "back";

    private String borderTableStyle = "borderAll";

    public CreditCardPluginReceiptWindow() {
        setHeight(600);
        setWidth(400);
        setResizable(true);
        setScrollbar(true);
    }

    public void main(IWContext iwc) throws Exception {
        super.main(iwc);
        IWResourceBundle iwrb = getResourceBundle(iwc);

        setTitle("Receipt window");
        addTitle(iwrb.getLocalizedString(LABEL_TITLE, "Receipt window"),
                IWConstants.BUILDER_FONT_STYLE_TITLE);

        showList(iwc);
    }

    private void showList(IWContext iwc) {
        IWResourceBundle iwrb = getResourceBundle(iwc);
        Table backTable = new Table(3, 3);
        backTable.setStyleClass(backTableStyle);
        backTable.setWidth(Table.HUNDRED_PERCENT);
        backTable.setHeight(1, 1, "6");
        backTable.setWidth(1, 2, "6");
        backTable.setWidth(3, 2, "6");
        Table heading = new Table();
        heading.setColor("#ffffff");
        heading.setStyleClass(borderTableStyle);
        heading.setWidth(Table.HUNDRED_PERCENT);
        Table t = new Table();
        heading.setCellpadding(5);
        t.setWidth(Table.HUNDRED_PERCENT);
        t.setCellpadding(5);
        t.setColor("#ffffff");
        t.setStyleClass(borderTableStyle);

        int row = 1;
        Text labelClub = new Text(iwrb.getLocalizedString(LABEL_CLUB, "Club"));
        labelClub.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelDate = new Text(iwrb.getLocalizedString(LABEL_DATE, "Date"));
        labelDate.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelCashier = new Text(iwrb.getLocalizedString(LABEL_CASHIER,
                "Cashier"));
        labelCashier.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelPhone = new Text(iwrb
                .getLocalizedString(LABEL_PHONE, "Phone"));
        labelPhone.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

        Text labelDiv = new Text(iwrb.getLocalizedString(LABEL_DIVISION,
                "Division"));
        labelDiv.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelGroup = new Text(iwrb
                .getLocalizedString(LABEL_GROUP, "Group"));
        labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelUser = new Text(iwrb.getLocalizedString(LABEL_USER, "User"));
        labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelInfo = new Text(iwrb.getLocalizedString(LABEL_INFO, "Info"));
        labelInfo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT,
                "Amount"));
        labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelAmountPaid = new Text(iwrb.getLocalizedString(
                LABEL_AMOUNT_PAID, "Amount paid"));
        labelAmountPaid.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelSum = new Text(iwrb.getLocalizedString(LABEL_SUM, "Sum"));
        labelSum.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

        t.add(labelDiv, 1, row);
        t.add(labelGroup, 2, row);
        t.add(labelUser, 3, row);
        t.add(labelInfo, 4, row);
        t.setAlignment(5, row, "RIGHT");
        t.add(labelAmount, 5, row);
        t.setAlignment(6, row, "RIGHT");
        t.add(labelAmountPaid, 6, row);
        row++;

        NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
        nf.setMaximumFractionDigits(0);

        try {
            int headingRow = 1;
            heading.add(labelClub, 1, headingRow);
            heading.add(labelCashier, 2, headingRow);
            heading.add(labelDate, 3, headingRow);
            heading.add(labelPhone, 4, headingRow++);
            //            heading.add("Klœbbur", 1, headingRow);
            heading.add(iwc.getCurrentUser().getName(), 2, headingRow);
            IWTimestamp ex = new IWTimestamp();
            heading.add(ex.getDateString("dd.MM.yyyy"), 3, headingRow);
            //            heading.add("S’mi", 4, headingRow);

            List paid = null;
            try {
                paid = getBasketBusiness(iwc).getExtraInfo();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            Group club = null;
            CreditCardExtraInfo ccinfo = null;

            if (paid != null && !paid.isEmpty()) {
                Iterator it = paid.iterator();
                double sum = 0;
                while (it.hasNext()) {
                    Object tmp = it.next();
                    if (tmp instanceof CreditCardExtraInfo) {
                        ccinfo = (CreditCardExtraInfo) tmp;
                    } else {
                        FinanceExtraBasketInfo info = (FinanceExtraBasketInfo) tmp;
                        if (club == null) {
                            club = info.getClub();
                        }
                        if (info.getDivision() != null) {
                            t.add(info.getDivision().getName(), 1, row);
                        }
                        if (info.getGroup() != null) {
                            t.add(info.getGroup().getName(), 2, row);
                        }
                        t.add(info.getUser().getName(), 3, row);
                        if (info.getInfo() != null) {
                            t.add(info.getInfo(), 4, row);
                        }
                        t
                                .add(nf.format(info.getAmount().doubleValue()),
                                        5, row);
                        t.setAlignment(5, row, "RIGHT");
                        t.add(nf.format(info.getAmountPaid()), 6, row);
                        t.setAlignment(6, row, "RIGHT");
                        sum += info.getAmountPaid();
                        row++;
                    }
                }
                t.mergeCells(1, row, 6, row);
                t.add("<hr>", 1, row++);
                t.setAlignment(5, row, "RIGHT");
                t.add(labelSum, 5, row);
                t.add(nf.format(sum), 6, row);
                t.setAlignment(6, row, "RIGHT");
            }

            if (club != null) {
                heading.add(club.getName(), 1, headingRow);
                Collection phones = club.getPhones();
                Iterator it = phones.iterator();
                String phone = null;
                while (it.hasNext() && phone == null) {
                    Phone p = (Phone) it.next();
                    phone = p.getNumber();
                }
                if (phone != null) {
                    heading.add(phone, 4, headingRow);
                }
            }
            
            if (ccinfo != null) {
                row += 3;
            
                IWTimestamp start = new IWTimestamp(ccinfo.contract.getFirstPayment());
                for (int i = 0; i < ccinfo.contract.getNumberOfPayments(); i++) {
                    IWTimestamp next = new IWTimestamp(start);
                    next.addMonths(i);
                    t.add(next.getDateString("dd.MM.yyyy"), 1, row);
                    t.add(Integer.toString(ccinfo.amount[i]), 2, row++);
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        backTable.add(heading, 2, 2);
        backTable.add(t, 2, 3);
        add(backTable);
        add(new PrintButton("Prenta"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
     */
    public String getBundleIdentifier() {
        return IW_BUNDLE_IDENTIFIER;
    }

    private FinanceEntryHome getFinanceEntryHome() {
        try {
            return (FinanceEntryHome) IDOLookup.getHome(FinanceEntry.class);
        } catch (IDOLookupException e) {
            e.printStackTrace();
        }

        return null;
    }

    private AssessmentRoundHome getAssessmentRoundHome() {
        try {
            return (AssessmentRoundHome) IDOLookup
                    .getHome(AssessmentRound.class);
        } catch (IDOLookupException e) {
            e.printStackTrace();
        }

        return null;
    }

    //session business
    private BasketBusiness getBasketBusiness(IWContext iwc) {
        try {
            return (BasketBusiness) IBOLookup.getSessionInstance(iwc,
                    BasketBusiness.class);
        } catch (IBOLookupException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}