package is.idega.idegaweb.member.isi.block.accounting.netbokhald.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierSubWindowTemplate;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierWindow;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectDropdownDouble;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.Group;

public class ConnectNetbokhald extends CashierSubWindowTemplate {

	protected static final String ACTION_SUBMIT = "cn_submit";

	protected static final String ACTION_DELETE = "cn_delete";

	protected static final String LABEL_DIVISION = "isi_acc_cn_division";

	protected static final String LABEL_GROUP = "isi_acc_cn_group";

	protected static final String LABEL_EXTERNAL_ID = "isi_acc_cn_external_id";

	protected static final String LABEL_DELETE = "isi_acc_cccc_delete";

	protected static final String ELEMENT_ALL_DIVISIONS = "isi_acc_cccc_all_divisions";

	protected static final String ELEMENT_ALL_GROUPS = "isi_acc_cccc_all_groups";

	private static final String ERROR_NO_DIVISION_SELECTED = "isi_acc_cccc_no_division_selected";

	private static final String ERROR_NO_GROUP_SELECTED = "isi_acc_cccc_no_group_selected";

	private static final String ERROR_NO_NUMBER_ENTERED = "isi_acc_cccc_no_number_entered";

	/**
	 * 
	 */
	public ConnectNetbokhald() {
		super();
	}

	private boolean saveConnection(IWContext iwc) {
		this.errorList = new ArrayList();

		String div = iwc.getParameter(LABEL_DIVISION);
		String grp = iwc.getParameter(LABEL_GROUP);

		if (div == null || "".equals(div)) {
			this.errorList.add(ERROR_NO_DIVISION_SELECTED);
		}

		if (grp == null || "".equals(grp)) {
			this.errorList.add(ERROR_NO_GROUP_SELECTED);
		}


		if (!this.errorList.isEmpty()) {
			return false;
		}

		boolean insert = false;

//		try {
//			insert = getAccountingBusiness(iwc).insertCreditCardContract(
//					getClub(), div, grp, number, type, ssn, company);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}

		return insert;
	}

	private void deleteConnection(IWContext iwc) {
		String delete[] = iwc.getParameterValues(LABEL_DELETE);

//		try {
//			getAccountingBusiness(iwc).deleteContract(delete);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
	}

	public void main(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);

		Form f = new Form();

		if (iwc.isParameterSet(ACTION_SUBMIT)) {
			if (!saveConnection(iwc)) {
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
			deleteConnection(iwc);
		}

		Table t = new Table();
		Table inputTable = new Table();
		t.setCellpadding(5);
		inputTable.setCellpadding(5);

		int row = 1;
		Text labelDivision = new Text(iwrb.getLocalizedString(
				LABEL_DIVISION, "Division"));
		labelDivision.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		Text labelGroup = new Text(iwrb.getLocalizedString(
				LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		Text labelExternalID = new Text(iwrb.getLocalizedString(
				LABEL_EXTERNAL_ID, "Contract number"));
		labelExternalID.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		Text labelDivisionGroup = new Text(iwrb.getLocalizedString(
				LABEL_DIVISION, "Division")
				+ "/" + iwrb.getLocalizedString(LABEL_GROUP, "Group"));
		labelDivisionGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);


		inputTable.add(labelDivisionGroup, 1, row);
		inputTable.add(labelExternalID, 2, row);

		Collection col = null;
		try {
			if (getClub() != null) {
				col = getAccountingBusiness(iwc)
						.findAllCreditCardContractByClub(getClub());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		SelectDropdownDouble divInput = new SelectDropdownDouble(
				LABEL_DIVISION, LABEL_GROUP);
		divInput.addEmptyElement(iwrb.getLocalizedString(ELEMENT_ALL_DIVISIONS,
				"All divisions"), iwrb.getLocalizedString(ELEMENT_ALL_GROUPS,
				"All groups"));
		ArrayList divisions = new ArrayList();
		getClubDivisions(divisions, getClub());
		if (!divisions.isEmpty()) {
			Iterator it = divisions.iterator();
			while (it.hasNext()) {
				Group division = (Group) it.next();
				ArrayList groups = new ArrayList();
				getGroupsUnderDivision(groups, division);
				Map map = new LinkedHashMap();
				if (groups != null && !groups.isEmpty()) {
					map.put("-1", iwrb.getLocalizedString(ELEMENT_ALL_GROUPS,
							"All groups"));

					Iterator it2 = groups.iterator();
					while (it2.hasNext()) {
						Group group = (Group) it2.next();
						map.put(group.getPrimaryKey().toString(), group
								.getName());
					}
				}
				divInput.addMenuElement(division.getPrimaryKey().toString(),
						division.getName(), map);
			}
		}
		TextInput externalIDInput = new TextInput(LABEL_EXTERNAL_ID);
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(
				ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

		inputTable.add(divInput, 1, row);
		inputTable.add(externalIDInput, 2, row);
		inputTable.add(submit, 3, row);

		row = 1;
		CheckBox checkAll = new CheckBox("checkall");
		checkAll.setToCheckOnClick(LABEL_DELETE, "this.checked");
		t.add(checkAll, 1, row);
		t.add(labelDivision, 2, row);
		t.add(labelGroup, 3, row);
		t.add(labelExternalID, 4, row++);

		if (col != null && !col.isEmpty()) {
			Iterator it = col.iterator();
			while (it.hasNext()) {
				CreditCardContract cont = (CreditCardContract) it.next();
				CheckBox deleteCheck = new CheckBox(LABEL_DELETE, cont
						.getPrimaryKey().toString());
				t.add(deleteCheck, 1, row);
				Group div = cont.getDivision();
				if (div != null) {
					t.add(div.getName(), 2, row);
				} else {
					t.add(iwrb.getLocalizedString(ELEMENT_ALL_DIVISIONS,
							"All divisions"), 2, row);
				}
				Group group = cont.getGroup();
				if (group != null) {
					t.add(group.getName(), 3, row);
				} else {
					t.add(iwrb.getLocalizedString(ELEMENT_ALL_GROUPS,
							"All groups"), 3, row);
				}
				t.add(cont.getContractNumber(), 4, row);
				t.add(cont.getCardType().getName(), 5, row);
				t.add(cont.getPersonalId(), 6, row);
				t.add(cont.getCompanyNumber(), 7, row++);
			}

			SubmitButton delete = new SubmitButton(iwrb.getLocalizedString(
					ACTION_DELETE, "Delete"), ACTION_DELETE, "delete");
			delete.setToEnableWhenChecked(LABEL_DELETE);
			t.add(delete, 5, row);
			t.setAlignment(5, row, "RIGHT");
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
