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
public class BankBMPBean extends GenericEntity implements Bank {

	protected final static String ENTITY_NAME = "fin_bank";
	
	protected final static String COLUMN_BANK_NAME = "bank_name";
	
	protected final static String COLUMN_BANK_SSN = "bank_ssn"; 
	
	protected final static String COLUMN_PLUGIN_NAME = "plugin";
	
	protected final static String COLUMN_SHORT_NAME = "short_name";
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_BANK_NAME, "bank name", String.class);
		addAttribute(COLUMN_BANK_SSN, "bank ssn", String.class);
		addAttribute(COLUMN_PLUGIN_NAME, "plugin", String.class);
		addAttribute(COLUMN_SHORT_NAME, "short name", String.class);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getBankName() {
		return getStringColumnValue(COLUMN_BANK_NAME);
	}

	public String getBankSSN() {
		return getStringColumnValue(COLUMN_BANK_SSN);
	}
	
	public String getPluginName() {
		return getStringColumnValue(COLUMN_PLUGIN_NAME);
	}
	
	public String getShortName() {
		return getStringColumnValue(COLUMN_SHORT_NAME);
	}

	public void setBankName(String bankName) {
		setColumn(COLUMN_BANK_NAME, bankName);
	}

	public void setBankSSN(String bankSSN) {
		setColumn(COLUMN_BANK_SSN, bankSSN);
	}
	
	public void setPluginName(String pluginName) {
		setColumn(COLUMN_PLUGIN_NAME, pluginName);
	}
	
	public void setShortName(String shortName) {
		setColumn(COLUMN_SHORT_NAME, shortName);
	}

	public Collection ejbFindAll() throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect());
	}
	
	public Object ejbFindByShotName(String name) throws FinderException {
		IDOQuery query = this.idoQueryGetSelect();
		query.appendWhereEquals(COLUMN_SHORT_NAME, name);

		return idoFindOnePKByQuery(query);
	}
}