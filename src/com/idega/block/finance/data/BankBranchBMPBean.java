/*
 * Created on Dec 8, 2004
 *
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * @author birna
 * 
 */
public class BankBranchBMPBean extends GenericEntity implements BankBranch {

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameBankBranchNumber(), "bank branch number",
				true, true, String.class);
		addAttribute(getColumnNameBankBranchName(), "bank branch name", true,
				true, String.class);
		addAttribute(getColumnNameBankId(), "bank id", true, true,
				Integer.class, "many-to-one", Bank.class);
	}

	public static String getEntityTableName() {
		return "fin_bank_branch";
	}

	public static String getColumnNameBankBranchNumber() {
		return "bank_branch_number";
	}

	public static String getColumnNameBankBranchName() {
		return "bank_branch_name";
	}

	public static String getColumnNameBankId() {
		return "bank_id";
	}

	public String getEntityName() {
		return getEntityTableName();
	}

	public String getBankBranchName() {
		return getStringColumnValue(getColumnNameBankBranchName());
	}

	public String getBankBranchNumber() {
		return getStringColumnValue(getColumnNameBankBranchNumber());
	}

	public int getBankId() {
		return getIntColumnValue(getColumnNameBankId());
	}

	public Bank getBank() {
		return (Bank) getColumnValue(getColumnNameBankId());
	}

	public void setBankBranchName(String bankBranchName) {
		setColumn(getColumnNameBankBranchName(), bankBranchName);
	}

	public void setBankBranchNumber(String bankBranchNumber) {
		setColumn(getColumnNameBankBranchNumber(), bankBranchNumber);
	}

	public void setBankId(int id) {
		setColumn(getColumnNameBankId(), id);
	}

	public Collection ejbFindByBank(Bank bank) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(this.getColumnNameBankId(), bank);
		return super.idoFindPKsByQuery(query);
	}
}