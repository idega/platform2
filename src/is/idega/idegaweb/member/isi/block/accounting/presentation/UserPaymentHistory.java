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

import javax.ejb.EJBException;

import com.idega.block.basket.business.BasketBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.UserBusiness;
import com.idega.user.presentation.UserChooserBrowser;

/**
 * @author palli
 */
public class UserPaymentHistory extends CashierSubWindowTemplate {

    protected static final String ACTION_SELECT_USER = "sp_select_user";

    private final static String LABEL_SELECTED_USER = "isi_acc_uph_selected_user";

    private final static String LABEL_CLUB = "isi_acc_uph_club";

    private final static String LABEL_DIVISION = "isi_acc_uph_division";

    private final static String LABEL_INFO = "isi_acc_uph_info";

    private final static String LABEL_AMOUNT = "isi_acc_uph_amount";

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

        Text labelClub = new Text(iwrb.getLocalizedString(LABEL_CLUB, "Club"));
        labelClub.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelDiv = new Text(iwrb.getLocalizedString(LABEL_DIVISION,
                "Division"));
        labelDiv.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelUser = new Text(iwrb.getLocalizedString(LABEL_SELECTED_USER,
                "Selected user:"));
        labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelInfo = new Text(iwrb.getLocalizedString(LABEL_INFO, "Info"));
        labelInfo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT,
                "Amount"));
        labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

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
                            .findAllOpenAssessmentEntriesByUserGroupAndDivision(
                                    getClub(), getDivision(), getUser());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            row = 1;
            paymentTable.add(labelClub, 1, row);
            paymentTable.add(labelDiv, 2, row);
            paymentTable.add(labelInfo, 3, row);
            paymentTable.add(labelAmount, 4, row++);

            NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
            nf.setMaximumFractionDigits(0);

            if (entries != null && !entries.isEmpty()) {
                Iterator it = entries.iterator();
                while (it.hasNext()) {
                    FinanceEntry entry = (FinanceEntry) it.next();
                    if (entry.getClub() != null) {
                        paymentTable.add(entry.getClub().getName(), 1, row);
                    }
                    if (entry.getDivision() != null) {
                        paymentTable.add(entry.getDivision().getName(), 2, row);
                    }
                    paymentTable.add(entry.getInfo(), 3, row);
                    paymentTable.add(nf.format(entry.getAmount()), 4, row);

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