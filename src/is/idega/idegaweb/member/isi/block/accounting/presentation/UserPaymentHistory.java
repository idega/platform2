/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.basket.business.BasketBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.UserBusiness;
import com.idega.user.presentation.UserChooserBrowser;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class UserPaymentHistory extends CashierSubWindowTemplate {

    protected static final String ACTION_SELECT_USER = "sp_select_user";

    private final static String LABEL_SELECTED_USER = "isi_acc_uph_selected_user";

    private final static String LABEL_DATE = "isi_acc_uph_date";

    private final static String LABEL_DIVISION = "isi_acc_uph_division";

//    private final static String LABEL_GROUP = "isi_acc_uph_group";

    private final static String LABEL_INFO = "isi_acc_uph_info";

    private final static String LABEL_PAYMENT_TYPE = "isi_acc_uph_payment_type";

    private final static String LABEL_AMOUNT = "isi_acc_uph_amount";

    private final static String LABEL_CASHIER = "isi_acc_uph_cashier";

    /**
     *  
     */
    public UserPaymentHistory() {
        super();
    }

    public void main(IWContext iwc) {
        Form f = new Form();
        IWResourceBundle iwrb = getResourceBundle(iwc);

        Table inputTable = new Table();
        Table paymentTable = new Table();
        inputTable.setCellpadding(5);
        paymentTable.setCellpadding(5);

        Text labelDate = new Text(iwrb.getLocalizedString(LABEL_DATE, "Date"));
        labelDate.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelDiv = new Text(iwrb.getLocalizedString(LABEL_DIVISION,
                "Division"));
        labelDiv.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
//        Text labelGrp = new Text(iwrb.getLocalizedString(LABEL_GROUP, "Group"));
//        labelGrp.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelInfo = new Text(iwrb.getLocalizedString(LABEL_INFO, "Info"));
        labelInfo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelType = new Text(iwrb.getLocalizedString(LABEL_PAYMENT_TYPE,
                "Payment type"));
        labelType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT,
                "Amount"));
        labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelCashier = new Text(iwrb.getLocalizedString(LABEL_CASHIER,
                "Cashier"));
        labelCashier.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

        Text labelUser = new Text(iwrb.getLocalizedString(LABEL_SELECTED_USER,
                "Selected user:"));
        labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

        SubmitButton selectUser = new SubmitButton(iwrb.getLocalizedString(
                ACTION_SELECT_USER, "Select user"), ACTION_SELECT_USER,
                "select_user");

        int row = 1;
        inputTable.add(labelUser, 1, row);
        if (getUser() != null) {
            inputTable.add(getUser().getName(), 2, row);
        }

        row++;
        inputTable.add(new UserChooserBrowser(CashierWindow.PARAMETER_USER_ID),
                1, row);
        inputTable.add(selectUser, 2, row);

        inputTable.setAlignment(2, row, "RIGHT");

        if (getUser() != null) {
            Collection entries = null;
            try {
                if (getClub() != null && getUser() != null) {
                    entries = getAccountingBusiness(iwc)
                            .findAllPaymentEntriesByUserGroupAndDivision(
                                    getClub(), getDivision(), getUser());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            row = 1;
            paymentTable.add(labelDate, 1, row);
            paymentTable.add(labelDiv, 2, row);
//            paymentTable.add(labelGrp, 3, row);
            paymentTable.add(labelInfo, 3, row);
            paymentTable.add(labelType, 4, row);
            paymentTable.add(labelAmount, 5, row);
			paymentTable.setAlignment(5, row, "RIGHT");            
            paymentTable.add(labelCashier, 6, row++);

            NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
            nf.setMaximumFractionDigits(0);

            if (entries != null && !entries.isEmpty()) {
                Iterator it = entries.iterator();
                while (it.hasNext()) {
                    FinanceEntry entry = (FinanceEntry) it.next();
                    if (entry.getDateOfEntry() != null) {
                        IWTimestamp doe = new IWTimestamp(entry
                                .getDateOfEntry());
                        paymentTable.add(doe.getDateString("dd.MM.yyyy"), 1,
                                row);
                    }
                    if (entry.getDivision() != null) {
                        paymentTable.add(entry.getDivision().getName(), 2, row);
                    }
                    if (entry.getInfo() != null) {
                        paymentTable.add(entry.getInfo(), 3, row);
                    }
                    if (entry.getPaymentType() != null) {
                        paymentTable
                                .add(iwrb.getLocalizedString(entry
                                        .getPaymentType().getLocalizationKey(),
                                        entry.getPaymentType()
                                                .getLocalizationKey()), 4, row);
                    }

                    paymentTable.add(nf.format(entry.getAmount()), 5, row);
                    paymentTable.setAlignment(5, row, "RIGHT");            
                    if (entry.getInsertedByUser() != null) {
                        paymentTable.add(entry.getInsertedByUser().getName(),
                                6, row);
                    }

                    row++;
                }
            }
        }

        f.add(inputTable);
        f.add(paymentTable);
        f.maintainParameter(CashierWindow.ACTION);
        f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
        f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
        f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);

        add(f);
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