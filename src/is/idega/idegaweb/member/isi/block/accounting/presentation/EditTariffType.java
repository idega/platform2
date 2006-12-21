/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author palli
 */
public class EditTariffType extends CashierSubWindowTemplate {

    protected static final String ACTION_SUBMIT = "ett_submit";

    protected static final String ACTION_DELETE = "ett_delete";

    protected static final String LABEL_NAME = "isi_acc_ett_name";

    protected static final String LABEL_LIST_NAMES = "isi_acc_ett_list_names";

    protected static final String LABEL_DELETE = "isi_acc_ett_delete";

    private final static String ERROR_NO_NAME_ENTERED = "isi_acc_ett_no_name_entered";

    /**
     *  
     */
    public EditTariffType() {
        super();
    }

    private boolean saveTariffType(IWContext iwc) {
		this.errorList = new ArrayList();

        String name = iwc.getParameter(LABEL_NAME);

        if (name == null || "".equals(name)) {
            this.errorList.add(ERROR_NO_NAME_ENTERED);
        }

        if (!this.errorList.isEmpty()) { return false; }

        boolean insert = false;
        try {
            insert = getAccountingBusiness(iwc).insertTariffType(null, name,
                    null, getClub());

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return insert;
    }

    private void deleteTariffType(IWContext iwc) {
        String delete[] = iwc.getParameterValues(LABEL_DELETE);

        try {
            getAccountingBusiness(iwc).deleteTariffType(delete);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void main(IWContext iwc) {
        Form f = new Form();
        IWResourceBundle iwrb = getResourceBundle(iwc);

        if (iwc.isParameterSet(ACTION_SUBMIT)) {
            if (!saveTariffType(iwc)) {
                Table error = new Table();
                Text labelError = new Text(iwrb.getLocalizedString(
                        ERROR_COULD_NOT_SAVE, "Could not save")
                        + ":");
                labelError
                        .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

                int r = 1;
                error.add(labelError, 1, r++);
                if (this.errorList != null && !this.errorList.isEmpty()) {
                    Iterator it = this.errorList.iterator();
                    while (it.hasNext()) {
                        String loc = (String) it.next();
                        Text errorText = new Text(iwrb.getLocalizedString(loc,
                                ""));
                        errorText
                                .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

                        error.add(errorText, 1, r++);
                    }
                }

                f.add(error);
            }
        } else if (iwc.isParameterSet(ACTION_DELETE)) {
            deleteTariffType(iwc);
        }

        Table t = new Table();
        Table inputTable = new Table();
        t.setCellpadding(5);
        inputTable.setCellpadding(5);

        int row = 1;
        Text labelName = new Text(iwrb.getLocalizedString(LABEL_NAME, "Name"));
        labelName.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelListName = new Text(iwrb.getLocalizedString(LABEL_LIST_NAMES,
                "Names"));
        labelListName.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

        inputTable.add(labelName, 1, row++);

        TextInput nameInput = new TextInput(LABEL_NAME);
        SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(
                ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

        inputTable.add(nameInput, 1, row);
        inputTable.add(submit, 2, row);

        row = 1;
        CheckBox checkAll = new CheckBox("checkall");
        checkAll.setToCheckOnClick(LABEL_DELETE, "this.checked");
        t.add(checkAll, 1, row);
        t.add(labelListName, 2, row++);

        Collection col = null;
        try {
            if (getClub() != null) {
                col = getAccountingBusiness(iwc).findAllTariffTypeByClub(
                        getClub());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (col != null && !col.isEmpty()) {
            Iterator it = col.iterator();
            while (it.hasNext()) {
                ClubTariffType type = (ClubTariffType) it.next();
                CheckBox deleteCheck = new CheckBox(LABEL_DELETE, type
                        .getPrimaryKey().toString());
                t.add(deleteCheck, 1, row);
                t.add(type.getName(), 2, row);
                row++;
            }

            SubmitButton delete = new SubmitButton(iwrb.getLocalizedString(
                    ACTION_DELETE, "Delete"), ACTION_DELETE, "delete");
            delete.setToEnableWhenChecked(LABEL_DELETE);

            t.add(delete, 2, row);
            t.setAlignment(2, row, "RIGHT");
        }

        f.maintainParameter(CashierWindow.ACTION);
        f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
        f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
        f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);

        f.add(inputTable);
        f.add(t);
        add(f);
    }
}