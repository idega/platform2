/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusiness;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariff;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.FloatInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.Group;
import com.idega.user.presentation.GroupChooser;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class EditTariffList extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "etl_submit";

	protected static final String LABEL_GROUP = "etl_group";
	protected static final String LABEL_TARIFF_TYPE = "etl_tariff_type";
	protected static final String LABEL_TEXT = "etl_text";
	protected static final String LABEL_AMOUNT = "etl_amount";
	protected static final String LABEL_FROM = "etl_from";
	protected static final String LABEL_TO = "etl_to";

	/**
	 *  
	 */
	public EditTariffList() {
		super();
	}

	private void saveTariffEntry(IWContext iwc) {
		String group = iwc.getParameter(LABEL_GROUP);
		String type = iwc.getParameter(LABEL_TARIFF_TYPE);
		String text = iwc.getParameter(LABEL_TEXT);
		String amount = iwc.getParameter(LABEL_AMOUNT);
		String from = iwc.getParameter(LABEL_FROM);
		String to = iwc.getParameter(LABEL_TO);

		if (group != null) {
			group = group.substring(group.indexOf("_")+1);
		}

		try {
			IWTimestamp fromTimestamp = new IWTimestamp(from);
			IWTimestamp toTimestamp = new IWTimestamp(to);
			System.out.println("Inserting entry");
			getAccountingBusiness(iwc).insertTariff(getClub(),group,type,text,amount,fromTimestamp.getDate(), toTimestamp.getDate()); 
		}
		catch (RemoteException e) { 
			e.printStackTrace();
		}
	}

	public void main(IWContext iwc) {
		if (iwc.isParameterSet(ACTION_SUBMIT)) {
			saveTariffEntry(iwc);
		}

		IWResourceBundle iwrb = getResourceBundle(iwc);

		Form f = new Form();
		Table t = new Table();
		t.setCellpadding(5);

		int row = 1;
		Text labelGroup = new Text(iwrb.getLocalizedString(LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelType = new Text(iwrb.getLocalizedString(LABEL_TARIFF_TYPE, "Tariff type"));
		labelType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelText = new Text(iwrb.getLocalizedString(LABEL_TEXT, "Text"));
		labelText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT, "Amount"));
		labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelFrom = new Text(iwrb.getLocalizedString(LABEL_FROM, "From"));
		labelFrom.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelTo = new Text(iwrb.getLocalizedString(LABEL_TO, "To"));
		labelTo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		t.add(labelGroup, 2, row);
		t.add(labelType, 3, row);
		t.add(labelText, 4, row);
		t.add(labelAmount, 5, row);
		t.add(labelFrom, 6, row);
		t.add(labelTo, 7, row++);

		Collection col = null;
		Collection types = null;
		try {
			if (getClub() != null) {
				col = getAccountingBusiness(iwc).findAllTariffByClub(getClub());
				types = getAccountingBusiness(iwc).findAllTariffTypeByClub(getClub());
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		if (col != null && !col.isEmpty()) {
			Iterator it = col.iterator();
			while (it.hasNext()) {
				ClubTariff tariff = (ClubTariff) it.next();
				Group group = tariff.getGroup();
				if (group != null)
					t.add(group.getName(), 2, row);
				t.add(tariff.getTariffType().getName(), 3, row);
				t.add(tariff.getText(), 4, row);
				t.add(Float.toString(tariff.getAmount()), 5, row);
				t.add(tariff.getPeriodFrom().toString(), 6, row);
				t.add(tariff.getPeriodTo().toString(), 7, row++);
			}
		}

		row += 6;

		GroupChooser groupInput = new GroupChooser(LABEL_GROUP);
		DropdownMenu typeInput = new DropdownMenu(LABEL_TARIFF_TYPE);
		SelectorUtility util = new SelectorUtility();
		if (types != null && !types.isEmpty()) {
			typeInput = (DropdownMenu) util.getSelectorFromIDOEntities(typeInput, types, "getName");
		}
		TextInput textInput = new TextInput(LABEL_TEXT);
		FloatInput amountInput = new FloatInput(LABEL_AMOUNT);
		DateInput fromInput = new DateInput(LABEL_FROM);
		DateInput toInput = new DateInput(LABEL_TO);
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

		t.add(groupInput, 2, row);
		t.add(typeInput,3,row);
		t.add(textInput, 4, row);
		t.add(amountInput, 5, row);
		t.add(fromInput, 6, row);
		t.add(toInput, 7, row);
		t.add(submit, 8, row);

		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);

		f.add(t);
		add(f);
	}

	private AccountingBusiness getAccountingBusiness(IWApplicationContext iwc) {
		try {
			return (AccountingBusiness) IBOLookup.getServiceInstance(iwc, AccountingBusiness.class);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;
	}
}