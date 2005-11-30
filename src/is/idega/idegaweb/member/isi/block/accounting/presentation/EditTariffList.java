/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariff;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType;

import java.rmi.RemoteException;
import java.text.NumberFormat;
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
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class EditTariffList extends CashierSubWindowTemplate {
//	protected static final String ACTION_SUBMIT = "etl_submit";

	protected static final String ACTION_DELETE = "etl_delete";

	protected static final String LABEL_DIVISION = "isi_acc_etl_div";

	protected static final String LABEL_GROUP = "isi_acc_etl_group";

	protected static final String LABEL_TARIFF_TYPE = "isi_acc_etl_tariff_type";

	protected static final String LABEL_TEXT = "isi_acc_etl_text";

	protected static final String LABEL_AMOUNT = "isi_acc_etl_amount";

	protected static final String LABEL_FROM = "isi_acc_etl_from";

	protected static final String LABEL_TO = "isi_acc_etl_to";

	protected static final String LABEL_CHILDREN = "isi_acc_etl_appl_children";

	protected static final String LABEL_DELETE = "isi_acc_etl_delete";

	// protected static final String PROPERTY_SKIP = "isi_acc_skip_assessment";

/*	private static final String ERROR_NO_GROUP_SELECTED = "isi_acc_etl_no_group_selected";

	private static final String ERROR_NO_TYPE_SELECTED = "isi_acc_etl_no_type_selected";

	private static final String ERROR_NO_TEXT_ENTERED = "isi_acc_etl_no_text_entered";

	private static final String ERROR_NO_AMOUNT_ENTERED = "isi_acc_etl_no_amount_entered";

	private static final String ERROR_NO_FROM_DATE_SELECTED = "isi_acc_etl_no_from_date_selected";

	private static final String ERROR_NO_TO_DATE_SELECTED = "isi_acc_etl_no_to_date_selected";*/

	/**
	 * 
	 */
	public EditTariffList() {
		super();
	}

	private void deleteTariffEntry(IWContext iwc) {
		String delete[] = iwc.getParameterValues(LABEL_DELETE);

		try {
			getAccountingBusiness(iwc).deleteTariff(delete);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void main(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);

		Form f = new Form();
		if (iwc.isParameterSet(ACTION_DELETE)) {
			deleteTariffEntry(iwc);
		}

		Table t = new Table();

		int row = 1;
		Text labelDiv = new Text(iwrb.getLocalizedString(LABEL_DIVISION,
				"Division"));
		labelDiv.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelGroup = new Text(iwrb
				.getLocalizedString(LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelType = new Text(iwrb.getLocalizedString(LABEL_TARIFF_TYPE,
				"Tariff type"));
		labelType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelText = new Text(iwrb.getLocalizedString(LABEL_TEXT, "Text"));
		labelText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT,
				"Amount"));
		labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelFrom = new Text(iwrb.getLocalizedString(LABEL_FROM, "From"));
		labelFrom.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelTo = new Text(iwrb.getLocalizedString(LABEL_TO, "To"));
		labelTo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelChildren = new Text(iwrb.getLocalizedString(LABEL_CHILDREN,
				"Apply to children"));
		labelChildren.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		Collection col = null;
		try {
			if (getClub() != null) {
				col = getAccountingBusiness(iwc)
						.findAllTariffByClubAndDivision(getClub(),
								getDivision());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		row = 1;
		CheckBox checkAll = new CheckBox("checkall");
		checkAll.setToCheckOnClick(LABEL_DELETE, "this.checked");
		t.add(checkAll, 1, row);
		t.add(labelDiv, 2, row);
		t.add(labelGroup, 3, row);
		t.add(labelType, 4, row);
		t.add(labelText, 5, row);
		t.add(labelAmount, 6, row);
		t.setAlignment(6, row, "RIGHT");
		t.add(labelFrom, 7, row);
		t.add(labelTo, 8, row++);

		NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
		nf.setMaximumFractionDigits(0);

		if (col != null && !col.isEmpty()) {
			Iterator it = col.iterator();
			while (it.hasNext()) {
				ClubTariff tariff = (ClubTariff) it.next();
				CheckBox deleteCheck = new CheckBox(LABEL_DELETE, tariff
						.getPrimaryKey().toString());
				t.add(deleteCheck, 1, row);

				if (tariff.getDivision() != null)
					t.add(tariff.getDivision().getName(), 2, row);
				Group group = tariff.getGroup();
				if (group != null)
					t.add(group.getName(), 3, row);

				ClubTariffType type = tariff.getTariffType();
				t.add(type.getName(), 4, row);
				t.add(tariff.getText(), 5, row);

				t.add(nf.format(tariff.getAmount()), 6, row);
				t.setAlignment(6, row, "RIGHT");
				if (tariff.getPeriodFrom() != null) {
					IWTimestamp p = new IWTimestamp(tariff.getPeriodFrom());
					t.add(p.getDateString("dd.MM.yyyy"), 7, row);
				}
				if (tariff.getPeriodTo() != null) {
					IWTimestamp p = new IWTimestamp(tariff.getPeriodTo());
					t.add(p.getDateString("dd.MM.yyyy"), 8, row++);
				}
			}

			SubmitButton delete = new SubmitButton(iwrb.getLocalizedString(
					ACTION_DELETE, "Delete"), ACTION_DELETE, "delete");
			delete.setToEnableWhenChecked(LABEL_DELETE);
			t.add(delete, 8, row);
			t.setAlignment(8, row, "RIGHT");
		}

		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);

		f.add(t);
		add(f);
	}
}