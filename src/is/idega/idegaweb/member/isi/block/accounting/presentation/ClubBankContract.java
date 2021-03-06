/*
 * $Id: ClubBankContract.java,v 1.3.2.3 2006/12/21 12:11:13 idegaweb Exp $ Created on
 * Feb 17, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.FinderException;

import com.idega.block.finance.business.BankInfoBusiness;
import com.idega.block.finance.data.Bank;
import com.idega.block.finance.data.BankBranch;
import com.idega.block.finance.data.BankInfo;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SelectDropdownDouble;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;

/**
 * 
 * Last modified: $Date: 2006/12/21 12:11:13 $ by $Author: idegaweb $
 * 
 * @author <a href="mailto:birna@idega.com">birna</a>
 * @version $Revision: 1.3.2.3 $
 */
public class ClubBankContract extends CashierSubWindowTemplate {

	private static final String ACTION_SUBMIT = "cbc_submit";

	private static final String ACTION_DELETE = "cbc_delete";

	private static final String LABEL_DIVISION = "isi_acc_cbc_division";

	private static final String LABEL_GROUP = "isi_acc_cbc_group";

	private static final String LABEL_BANK_BRANCH_NR = "isi_acc_cbc_bb_nr";

	private static final String LABEL_LEDGER = "isi_acc_cbc_ledger";

	private static final String LABEL_ACCOUNT_ID = "isi_acc_cbc_acc_id";

	private static final String LABEL_CLAIMANTS_SSN = "isi_acc_cbc_claimants_ssn";

	private static final String LABEL_CLAIMANTS_NAME = "isi_acc_cbc_claimants_name";

	private static final String LABEL_USERNAME = "isi_acc_cbc_username";

	private static final String LABEL_PASSWORD = "isi_acc_cbc_psw";

	private static final String LABEL_DELETE = "isi_acc_cccc_delete";

	protected static final String ELEMENT_ALL_DIVISIONS = "isi_acc_cccc_all_divisions";

	protected static final String ELEMENT_ALL_GROUPS = "isi_acc_cccc_all_groupsf";

	private static final String ERROR_NO_DIVISION_SELECTED = "isi_acc_cbc_no_division_selected";

	private static final String ERROR_NO_GROUP_SELECTED = "isi_acc_cbc_no_group_selected";

	private static final String ERROR_NO_BANK_BRANCH_ENTERED = "isi_acc_cbc_no_bb_nr";

	// private static final String ERROR_NO_LEDGER_ENTERED =
	// "isi_acc_cbc_no_ledger";

	private static final String ERROR_NO_ACCOUNT_ID_ENTERED = "isi_acc_cbc_no_account_id";

	private static final String ERROR_NO_CLAIMANTS_SSN_ENTERED = "isi_acc_cbc_no_claimants_ssn";

	private static final String ERROR_NO_CLAIMANTS_NAME_ENTERED = "isi_acc_cbc_no_claimants_name";

	private static final String ERROR_NO_USERNAME_ENTERED = "isi_acc_cbc_no_username";

	private static final String ERROR_NO_PSW_ENTERED = "isi_acc_cbc_no_psw";

	private static final String ERROR_BB_NR_NOT_VALID = "isi_acc_cbc_bb_nr_not_valid";

	// private static final String ERROR_LEDGER_NOT_VALID =
	// "isi_acc_cbc_ledger_not_valid";

	private static final String ERROR_ACCOUNT_ID_NOT_VALID = "isi_acc_cbc_account_id_not_valid";

	private static final String ERROR_CLAIMANTS_SSN_NOT_VALID = "isi_acc_cbc_claimants_ssn_not_valid";

	private static final String ERROR_CLAIMANTS_NAME_NOT_VALID = "isi_acc_cbc_claimants_name_not_valid";

	private static final String ERROR_USERNAME_NOT_VALID = "isi_acc_cbc_username_not_valid";

	private static final String ERROR_PSW_NOT_VALID = "isi_acc_cbc_psw_not_valid";

	public ClubBankContract() {
		super();
	}

	public void main(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);

		Form f = new Form();

		if (iwc.isParameterSet(ACTION_SUBMIT)) {
			if (!saveContract(iwc)) {
				Table error = new Table();
				Text labelError = new Text(iwrb.getLocalizedString(
						ERROR_COULD_NOT_SAVE, "Could not save:"));
				labelError
						.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

				int r = 1;
				error.add(labelError, 1, r++);
				if (this.errorList != null && !this.errorList.isEmpty()) {
					Iterator it = this.errorList.iterator();
					while (it.hasNext()) {
						String loc = (String) it.next();
						Text errorText = new Text(iwrb.getLocalizedString(loc,
								loc));
						errorText
								.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

						error.add(errorText, 1, r++);
					}
				}
				f.add(error);
			}
		} else if (iwc.isParameterSet(ACTION_DELETE)) {
			if (!deleteContract(iwc)) {
				Table error = new Table();
				error.add("Could not delete contract!!");
				f.add(error);
			}
		}
		Table t = new Table();
		Table inputTable = new Table();
		t.setCellpadding(5);
		inputTable.setCellpadding(5);

		int row = 1;
		Text labelDivisionGroup = new Text(iwrb.getLocalizedString(
				LABEL_DIVISION, "Division")
				+ "/" + iwrb.getLocalizedString(LABEL_GROUP, "Group"));
		labelDivisionGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDivision = new Text(iwrb.getLocalizedString(LABEL_DIVISION,
				"Division"));
		labelDivision.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelGroup = new Text(iwrb
				.getLocalizedString(LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelBankBranchNumber = new Text(iwrb.getLocalizedString(
				LABEL_BANK_BRANCH_NR, "Bank branch number"));
		labelBankBranchNumber
				.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelLedger = new Text(iwrb.getLocalizedString(LABEL_LEDGER,
				"Ledger"));
		labelLedger.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAccountId = new Text(iwrb.getLocalizedString(
				LABEL_ACCOUNT_ID, "Account id"));
		labelAccountId.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelClaimantsSSN = new Text(iwrb.getLocalizedString(
				LABEL_CLAIMANTS_SSN, "Claimants SSN"));
		labelClaimantsSSN.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelClaimantsName = new Text(iwrb.getLocalizedString(
				LABEL_CLAIMANTS_NAME, "Claimants name"));
		labelClaimantsName.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelUsername = new Text(iwrb.getLocalizedString(LABEL_USERNAME,
				"Username"));
		labelUsername.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelPsw = new Text(iwrb.getLocalizedString(LABEL_PASSWORD,
				"Password"));
		labelPsw.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

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

		SelectDropdownDouble bankInput = new SelectDropdownDouble("BANKS",
				LABEL_BANK_BRANCH_NR);
		try {
			Collection banks = getBankInfoBusiness(iwc).getBankHome().findAll();
			if (banks != null && !banks.isEmpty()) {
				Iterator it = banks.iterator();
				while (it.hasNext()) {
					Bank bank = (Bank) it.next();
					Collection branches = null;
					try {
						branches = getBankInfoBusiness(iwc).getBankBranchHome()
								.findByBank(bank);
					} catch (FinderException e2) {
						branches = null;
					}
					if (branches != null && !branches.isEmpty()) {
						Iterator it2 = branches.iterator();
						Map map = new LinkedHashMap();
						while (it2.hasNext()) {
							BankBranch branch = (BankBranch) it2.next();
							map.put(branch.getPrimaryKey().toString(), branch
									.getBankBranchNumber());
						}
						bankInput.addMenuElement(bank.getPrimaryKey()
								.toString(), bank.getBankName(), map);
					}
				}
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (FinderException e1) {
			e1.printStackTrace();
		}

		TextInput accountIdInput = new TextInput(LABEL_ACCOUNT_ID);
		accountIdInput.setMaxlength(3);
		accountIdInput.setLength(6);

		TextInput ssnInput = new TextInput(LABEL_CLAIMANTS_SSN);
		ssnInput.setMaxlength(10);
		ssnInput.setLength(10);

		TextInput nameInput = new TextInput(LABEL_CLAIMANTS_NAME);
		nameInput.setMaxlength(8);
		nameInput.setLength(10);

		TextInput usernameInput = new TextInput(LABEL_USERNAME);

		PasswordInput pswInput = new PasswordInput(LABEL_PASSWORD);
		//pswInput.setAsPasswordInput(true);

		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(
				ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

		inputTable.add(labelDivisionGroup, 1, row);
		inputTable.add(labelBankBranchNumber, 2, row);
		inputTable.add(labelLedger, 3, row);
		inputTable.add(labelAccountId, 4, row);
		inputTable.add(labelClaimantsSSN, 5, row);
		inputTable.add(labelClaimantsName, 6, row);
		inputTable.add(labelUsername, 7, row);
		inputTable.add(labelPsw, 8, row++);

		inputTable.add(divInput, 1, row);
		inputTable.add(bankInput, 2, row);
		inputTable.add("66", 3, row);
		inputTable.add(accountIdInput, 4, row);
		inputTable.add(ssnInput, 5, row);
		inputTable.add(nameInput, 6, row);
		inputTable.add(usernameInput, 7, row);
		inputTable.add(pswInput, 8, row++);
		inputTable.add(submit, 8, row++);

		Collection contracts = null;

		if (getClub() != null) {
			try {
				contracts = getBankInfoBusiness(iwc).findAllContractsByClub(
						getClub());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		row = 1;
		CheckBox checkAll = new CheckBox("checkall");
		checkAll.setToCheckOnClick(LABEL_DELETE, "this.checked");

		t.add(checkAll, 1, row);
		t.add(labelDivision, 2, row);
		t.add(labelGroup, 3, row);
		t.add(labelBankBranchNumber, 4, row);
		t.add(labelLedger, 5, row);
		t.add(labelAccountId, 6, row);
		t.add(labelClaimantsSSN, 7, row);
		t.add(labelClaimantsName, 8, row);
		t.add(labelUsername, 9, row++);

		if (contracts != null && !contracts.isEmpty()) {
			Iterator it = contracts.iterator();
			while (it.hasNext()) {
				BankInfo bi = (BankInfo) it.next();
				CheckBox deleteCheck = new CheckBox(LABEL_DELETE, bi
						.getPrimaryKey().toString());
				t.add(deleteCheck, 1, row);

				if (bi.getDivisionId() != -1) {
					Group div = getGroupById(bi.getDivisionId(), iwc);
					if (div != null) {
						t.add(div.getName(), 2, row);
					} else {
						t.add(iwrb.getLocalizedString(ELEMENT_ALL_DIVISIONS,
								"All divisions"), 2, row);
					}
				} else {
					t.add(iwrb.getLocalizedString(ELEMENT_ALL_DIVISIONS,
							"All divisions"), 2, row);
				}

				if (bi.getGroupId() != -1) {
					Group group = getGroupById(bi.getGroupId(), iwc);
					if (group != null) {
						t.add(group.getName(), 3, row);
					} else {
						t.add(iwrb.getLocalizedString(ELEMENT_ALL_GROUPS,
								"All groups"), 3, row);
					}
				} else {
					t.add(iwrb.getLocalizedString(ELEMENT_ALL_GROUPS,
							"All groups"), 3, row);
				}

				if (bi.getClaimantsBankBranch() != null) {
					t.add(bi.getClaimantsBankBranch().getBankBranchNumber(), 4,
							row);
				}
				t.add(Integer.toString(bi.getAccountBook()), 5, row);
				t.add(bi.getAccountId(), 6, row);
				t.add(bi.getClaimantsSSN(), 7, row);
				t.add(bi.getClaimantsName(), 8, row);
				t.add(bi.getUsername(), 9, row);
				row++;
			}

			SubmitButton delete = new SubmitButton(iwrb.getLocalizedString(
					ACTION_DELETE, "Delete"), ACTION_DELETE, "delete");
			delete.setToEnableWhenChecked(LABEL_DELETE);
			t.add(delete, 10, ++row);
			t.setAlignment(10, row, "RIGHT");
		}

		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);

		f.add(inputTable);
		f.add(t);
		add(f);

	}

	private boolean saveContract(IWContext iwc) {
		this.errorList = new ArrayList();

		String div = iwc.getParameter(LABEL_DIVISION);
		String group = iwc.getParameter(LABEL_GROUP);
		String branch = iwc.getParameter(LABEL_BANK_BRANCH_NR);
		String accountId = iwc.getParameter(LABEL_ACCOUNT_ID);
		String ssn = iwc.getParameter(LABEL_CLAIMANTS_SSN);
		String name = iwc.getParameter(LABEL_CLAIMANTS_NAME);
		String username = iwc.getParameter(LABEL_USERNAME);
		String password = iwc.getParameter(LABEL_PASSWORD);

		System.out.println("[ClubBankContract] password = " + password);
		
		if (div == null || "".equals(div)) {
			this.errorList.add(ERROR_NO_DIVISION_SELECTED);
		}
		if (group == null || "".equals(group)) {
			this.errorList.add(ERROR_NO_GROUP_SELECTED);
		}
		if (branch == null || "".equals(branch)) {
			this.errorList.add(ERROR_NO_BANK_BRANCH_ENTERED);
		}
		if (accountId == null || "".equals(accountId)) {
			this.errorList.add(ERROR_NO_ACCOUNT_ID_ENTERED);
		}
		if (ssn == null || "".equals(ssn)) {
			this.errorList.add(ERROR_NO_CLAIMANTS_SSN_ENTERED);
		}
		if (name == null || "".equals(name)) {
			this.errorList.add(ERROR_NO_CLAIMANTS_NAME_ENTERED);
		}
		if (username == null || "".equals(username)) {
			this.errorList.add(ERROR_NO_USERNAME_ENTERED);
		}
		if (password == null || "".equals(password)) {
			this.errorList.add(ERROR_NO_PSW_ENTERED);
		}

		if (accountId != null && accountId.length() != 3) {
			this.errorList.add(ERROR_ACCOUNT_ID_NOT_VALID);
		}
		if (ssn != null && ssn.length() != 10 && !validateAsNumber(ssn)) {
			this.errorList.add(ERROR_CLAIMANTS_SSN_NOT_VALID);
		}
		if (name != null
				&& !validateAsNonEmptySpaceAndNonIllegalCharacters(name)) {
			this.errorList.add(ERROR_CLAIMANTS_NAME_NOT_VALID);
		}
		if (username != null
				&& !validateAsNonEmptySpaceAndNonIllegalCharacters(username)) {
			this.errorList.add(ERROR_USERNAME_NOT_VALID);
		}
		if (password != null
				&& !validateAsNonEmptySpaceAndNonIllegalCharacters(password)) {
			this.errorList.add(ERROR_PSW_NOT_VALID);
		}

		if (branch != null) {
			this.errorList.add(ERROR_BB_NR_NOT_VALID);
		}

		Integer branchId = null;

		try {
			branchId = new Integer(branch);
		} catch (NumberFormatException e) {

		}

		boolean insert = false;

		try {
			insert = getBankInfoBusiness(iwc).insertBankInfoContract(getClub(),
					div, group, branchId.intValue(), accountId, ssn, name,
					username, password);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return insert;
	}

	private boolean deleteContract(IWContext iwc) {
		String delete[] = iwc.getParameterValues(LABEL_DELETE);
		try {
			return getBankInfoBusiness(iwc).deleteContract(delete);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean validateAsNumber(String s) {
		Pattern pattern = Pattern.compile("\\D"); // not a digit
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			return false;
		}
		return true;
	}

	private boolean validateAsNonEmptySpaceAndNonIllegalCharacters(String s) {
		Pattern pattern = Pattern.compile("\\p{Punct}"); // the characters:
		// !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			return false;
		}
		return true;
	}

	protected BankInfoBusiness getBankInfoBusiness(IWApplicationContext iwc) {
		try {
			return (BankInfoBusiness) IBOLookup.getServiceInstance(iwc,
					BankInfoBusiness.class);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Group getGroupById(int groupId, IWApplicationContext iwc) {
		GroupBusiness biz = null;
		try {
			biz = (GroupBusiness) IBOLookup.getServiceInstance(iwc,
					GroupBusiness.class);
			return biz.getGroupByGroupID(groupId);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException f) {
			f.printStackTrace();
		}

		return null;
	}
}
