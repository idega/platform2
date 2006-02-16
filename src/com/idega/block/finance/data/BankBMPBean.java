/*
 * Created on Dec 8, 2004
 *
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

/**
 * @author birna
 * 
 */
public class BankBMPBean extends GenericEntity implements Bank {

	protected final static String COLUMN_PLUGIN_NAME = "plugin"; 
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameBankName(), "bank name", true, true,
				String.class);
		addAttribute(getColumnNameBankSSN(), "bank ssn", true, true,
				String.class);
		addAttribute(COLUMN_PLUGIN_NAME, "plugin", String.class);
	}

	public static String getEntityTableName() {
		return "fin_bank";
	}

	public static String getColumnNameBankName() {
		return "bank_name";
	}

	public static String getColumnNameBankSSN() {
		return "bank_ssn";
	}

	public String getEntityName() {
		return getEntityTableName();
	}

	public String getBankName() {
		return getStringColumnValue(getColumnNameBankName());
	}

	public String getBankSSN() {
		return getStringColumnValue(getColumnNameBankSSN());
	}
	
	public String getPluginName() {
		return getStringColumnValue(COLUMN_PLUGIN_NAME);
	}

	public void setBankName(String bankName) {
		setColumn(getColumnNameBankName(), bankName);
	}

	public void setBankSSN(String bankSSN) {
		setColumn(getColumnNameBankSSN(), bankSSN);
	}
	
	public void setPluginName(String pluginName) {
		setColumn(COLUMN_PLUGIN_NAME, pluginName);
	}

	public Collection ejbFindAll() throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect());
	}
}