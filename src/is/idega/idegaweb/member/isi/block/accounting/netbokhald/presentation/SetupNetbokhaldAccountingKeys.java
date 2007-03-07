package is.idega.idegaweb.member.isi.block.accounting.netbokhald.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentType;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusiness;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeys;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysBMPBean;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysHome;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetup;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierSubWindowTemplate;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierWindow;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectDropdownDouble;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

public class SetupNetbokhaldAccountingKeys extends CashierSubWindowTemplate {

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi.block.accounting.netbokhald";

	public static final String PARAM_SETUP = "snak_setup_id";

	protected static final String ACTION_SUBMIT = "snak_submit";

	protected static final String ACTION_DELETE = "snak_delete";

	protected static final String LABEL_TYPE = "isi_acc_snak_type";

	protected static final String LABEL_KEY = "isi_acc_snak_key";

	protected static final String LABEL_DEBIT = "isi_acc_snak_debit";

	protected static final String LABEL_CREDIT = "isi_acc_snak_credit";

	protected static final String LABEL_DELETE = "isi_acc_snak_delete";

	protected static final String ELEMENT_ALL_KEYS = "isi_acc_snak_all_keys";

	private static final String ERROR_NO_TYPE_SELECTED = "isi_acc_snak_no_type_selected";

	private static final String ERROR_NO_DEBIT_ENTERED = "isi_acc_snak_no_debit_entered";

	private static final String ERROR_NO_CREDIT_ENTERED = "isi_acc_snak_no_credit_entered";

	private NetbokhaldSetup setup = null;

	/**
	 * 
	 */
	public SetupNetbokhaldAccountingKeys() {
		super();
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	private boolean saveAccountingKey(IWContext iwc) {
		this.errorList = new ArrayList();

		String type = iwc.getParameter(LABEL_TYPE);
		String key = iwc.getParameter(LABEL_KEY);
		String debit = iwc.getParameter(LABEL_DEBIT);
		String credit = iwc.getParameter(LABEL_CREDIT);

		if (type == null || "".equals(type)) {
			this.errorList.add(ERROR_NO_TYPE_SELECTED);
		}

		if (debit == null || "".equals(debit)) {
			this.errorList.add(ERROR_NO_DEBIT_ENTERED);
		}

		if (credit == null || "".equals(credit)) {
			this.errorList.add(ERROR_NO_CREDIT_ENTERED);
		}

		if (!this.errorList.isEmpty()) {
			return false;
		}

		boolean insert = false;

		int keyValue = -1;

		try {
			keyValue = Integer.parseInt(key);
		} catch (NumberFormatException e) {
			keyValue = -1;
		}

		try {
			insert = getNetbokhaldBusiness(iwc).insertNetbokhaldAccountingKey(
					getSetup(iwc), type, keyValue, debit, credit);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return insert;
	}

	private NetbokhaldSetup getSetup(IWContext iwc) {
		if (this.setup != null) {
			return this.setup;
		}

		try {
			setup = getNetbokhaldBusiness(iwc).getNetbokhaldSetup(
					iwc.getParameter(PARAM_SETUP));
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return setup;
	}

	private void deleteAccountingKeys(IWContext iwc) {
		String delete[] = iwc.getParameterValues(LABEL_DELETE);

		try {
			getNetbokhaldBusiness(iwc).deleteAccountingKeys(delete);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void main(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);

		Form f = new Form();

		if (iwc.isParameterSet(ACTION_SUBMIT)) {
			if (!saveAccountingKey(iwc)) {
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
			deleteAccountingKeys(iwc);
		}

		Table t = new Table();
		Table inputTable = new Table();
		t.setCellpadding(5);
		inputTable.setCellpadding(5);

		int row = 1;
		Text labelType = new Text(iwrb.getLocalizedString(LABEL_TYPE, "Type"));
		labelType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelKey = new Text(iwrb.getLocalizedString(LABEL_KEY, "Key"));
		labelKey.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDebit = new Text(iwrb
				.getLocalizedString(LABEL_DEBIT, "Debit"));
		labelDebit.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelCredit = new Text(iwrb.getLocalizedString(LABEL_CREDIT,
				"Credit"));
		labelCredit.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelTypeKey = new Text(iwrb.getLocalizedString(LABEL_TYPE+LABEL_KEY, "Type/key"));
		labelTypeKey.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		inputTable.add(labelTypeKey, 1, row);
		inputTable.add(labelDebit, 2, row);
		inputTable.add(labelCredit, 3, row++);

		Collection col = null;
		try {
			col = getNetbokhaldAccountingKeysHome().findAllBySetupID(
					getSetup(iwc));
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
		}

		SelectDropdownDouble typeInput = new SelectDropdownDouble(LABEL_TYPE,
				LABEL_KEY);
		ArrayList types = new ArrayList();
		getTypes(types, iwc);
		if (!types.isEmpty()) {
			Iterator it = types.iterator();
			while (it.hasNext()) {
				String type = (String) it.next();
				ArrayList keys = new ArrayList();
				getKeysForType(keys, type, iwc);
				Map map = new LinkedHashMap();
				if (keys != null && !keys.isEmpty()) {
					map.put("-1", iwrb.getLocalizedString(ELEMENT_ALL_KEYS,
							"All keys"));

					Iterator it2 = keys.iterator();
					while (it2.hasNext()) {
						updateMapForKey(map, it2.next(), type, iwrb);
					}
				}
				typeInput.addMenuElement(type, iwrb.getLocalizedString(
						LABEL_TYPE + "_" + type, "Type " + type), map);
			}
		}

		TextInput debitInput = new TextInput(LABEL_DEBIT);
		TextInput creditInput = new TextInput(LABEL_CREDIT);

		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(
				ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

		inputTable.add(typeInput, 1, row);
		inputTable.add(debitInput, 2, row);
		inputTable.add(creditInput, 3, row);
		inputTable.add(submit, 4, row);

		row = 1;
		CheckBox checkAll = new CheckBox("checkall");
		checkAll.setToCheckOnClick(LABEL_DELETE, "this.checked");
		t.add(checkAll, 1, row);
		t.add(labelType, 2, row);
		t.add(labelKey, 3, row);
		t.add(labelDebit, 4, row);
		t.add(labelCredit, 5, row++);

		if (col != null && !col.isEmpty()) {
			Iterator it = col.iterator();
			while (it.hasNext()) {
				NetbokhaldAccountingKeys key = (NetbokhaldAccountingKeys) it
						.next();
				CheckBox deleteCheck = new CheckBox(LABEL_DELETE, key
						.getPrimaryKey().toString());
				t.add(deleteCheck, 1, row);
				String type = key.getType();
				t.add(iwrb.getLocalizedString(LABEL_TYPE + "_" + type, "Type "
						+ type), 2, row);
				int keyID = key.getKey();
				if (keyID > 0) {
					t.add(getKeyName(type, keyID, iwrb), 3, row);
				} else {
					t.add(
							iwrb.getLocalizedString(ELEMENT_ALL_KEYS,
									"All keys"), 3, row);
				}
				t.add(key.getDebitKey(), 4, row);
				t.add(key.getCreditKey(), 5, row++);
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
		f.maintainParameter(PARAM_SETUP);

		f.add(inputTable);
		f.add(t);
		add(f);
	}

	private void getTypes(ArrayList types, IWContext iwc) {
		types.add(NetbokhaldAccountingKeysBMPBean.TYPE_ASSESSMENT);
		types.add(NetbokhaldAccountingKeysBMPBean.TYPE_PAYMENT);
	}

	private void getKeysForType(ArrayList keys, String type, IWContext iwc) {
		if (type.equals(NetbokhaldAccountingKeysBMPBean.TYPE_ASSESSMENT)) {
			Collection col = null;
			try {
				if (getClub() != null) {
					col = getAccountingBusiness(iwc).findAllTariffTypeByClub(
							getClub());
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			if (col != null) {
				keys.addAll(col);
			}
		} else if (type.equals(NetbokhaldAccountingKeysBMPBean.TYPE_PAYMENT)) {
			Collection types = null;
			try {
				types = getAccountingBusiness(iwc).findAllPaymentTypes();
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			if (types != null) {
				keys.addAll(types);
			}
		}
	}

	private void updateMapForKey(Map keyMap, Object key, String type,
			IWResourceBundle iwrb) {
		if (type.equals(NetbokhaldAccountingKeysBMPBean.TYPE_ASSESSMENT)) {
			ClubTariffType tariffKey = (ClubTariffType) key;
			keyMap.put(tariffKey.getPrimaryKey().toString(), tariffKey
					.getName());
		} else if (type.equals(NetbokhaldAccountingKeysBMPBean.TYPE_PAYMENT)) {
			PaymentType paymentKey = (PaymentType) key;
			keyMap.put(paymentKey.getPrimaryKey().toString(), iwrb
					.getLocalizedString(paymentKey.getLocalizationKey()));
		}
	}

	private String getKeyName(String type, int key, IWResourceBundle iwrb) {
		String keyName = "";
		if (type.equals(NetbokhaldAccountingKeysBMPBean.TYPE_ASSESSMENT)) {
			try {
				ClubTariffType clubType = getClubTariffTypeHome()
						.findByPrimaryKey(new Integer(key));
				keyName = clubType.getName();
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		} else if (type.equals(NetbokhaldAccountingKeysBMPBean.TYPE_PAYMENT)) {
			try {
				PaymentType paymentType = getPaymentTypeHome()
						.findByPrimaryKey(new Integer(key));
				keyName = iwrb.getLocalizedString(paymentType
						.getLocalizationKey());
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}

		return keyName;
	}

	private NetbokhaldAccountingKeysHome getNetbokhaldAccountingKeysHome()
			throws IDOLookupException {
		return (NetbokhaldAccountingKeysHome) IDOLookup
				.getHome(NetbokhaldAccountingKeys.class);
	}

	private ClubTariffTypeHome getClubTariffTypeHome()
			throws IDOLookupException {
		return (ClubTariffTypeHome) IDOLookup.getHome(ClubTariffType.class);
	}

	private PaymentTypeHome getPaymentTypeHome() throws IDOLookupException {
		return (PaymentTypeHome) IDOLookup.getHome(PaymentType.class);
	}

	protected NetbokhaldBusiness getNetbokhaldBusiness(IWApplicationContext iwc) {
		try {
			return (NetbokhaldBusiness) IBOLookup.getServiceInstance(iwc,
					NetbokhaldBusiness.class);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;
	}
}