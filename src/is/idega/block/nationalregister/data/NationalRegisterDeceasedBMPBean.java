/*
 * $Id: NationalRegisterDeceasedBMPBean.java,v 1.1.2.1 2006/11/21 23:35:25 idegaweb Exp $
 * Created on Nov 20, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.nationalregister.data;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;


public class NationalRegisterDeceasedBMPBean extends GenericEntity implements NationalRegisterDeceased {

	protected static final String ENTITY_NAME = "reg_national_deceased_is";
	
	protected static final String SYMBOL = "symbol";

	protected static final String SSN = "ssn";

	protected static final String DATE_OF_DEATH = "date_of_death";

	protected static final String NAME = "name";

	protected static final String STREET = "street";

	protected static final String COMMUNE = "commune";

	protected static final String GENDER = "gender";

	protected static final String MARITAL_STATUS = "marital_status";

	protected static final String SPOUSE_SSN = "spouse_ssn"; 

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		
		addAttribute(getIDColumnName());
		addAttribute(SYMBOL, "Einkenni", true, true, java.lang.String.class, 2);
		addAttribute(SSN, "Kennitala", true, true, java.lang.String.class, 10);
		addAttribute(DATE_OF_DEATH, "Danardagur", true, true, String.class, 8);
		addAttribute(NAME, "Nafn", true, true, java.lang.String.class, 31);
		addAttribute(STREET, "Heimili", true, true, java.lang.String.class, 21);
		addAttribute(COMMUNE, "Sveitarfelag", true, true, java.lang.String.class, 21);
		addAttribute(GENDER, "Kyn", true, true, java.lang.String.class, 1);
		addAttribute(MARITAL_STATUS, "Hjuskaparstada", true, true, java.lang.String.class, 1);
		addAttribute(SPOUSE_SSN, "Kennitala maka", true, true, java.lang.String.class, 10);
		
		addIndex(SSN);
		setUnique(SSN, true);
	}
	
	public void setSymbol(String symbol) {
		setColumn(SYMBOL, symbol);
	}

	public String getSymbol() {
		return getStringColumnValue(SYMBOL);
	}

	public void setSSN(String ssn) {
		setColumn(SSN, ssn);
	}

	public String getSSN() {
		return getStringColumnValue(SSN);
	}

	public void setDateOfDeath(String date) {
		setColumn(DATE_OF_DEATH, date);
	}
	
	public String getDateOfDeath() {
		return getStringColumnValue(DATE_OF_DEATH);
	}

	public void setName(String name) {
		setColumn(NAME, name);
	}

	public String getName() {
		return getStringColumnValue(NAME);
	}

	public void setCommune(String commune) {
		setColumn(COMMUNE, commune);
	}

	public String getCommune() {
		return getStringColumnValue(COMMUNE);
	}

	public void setStreet(String street) {
		setColumn(STREET, street);
	}

	public String getStreet() {
		return getStringColumnValue(STREET);
	}

	public void setGender(String gender) {
		setColumn(GENDER, gender);
	}

	public String getGender() {
		return getStringColumnValue(GENDER);
	}

	public void setMaritalStatus(String status) {
		setColumn(MARITAL_STATUS, status);
	}

	public String getMaritalStatus() {
		return getStringColumnValue(MARITAL_STATUS);
	}

	public void setSpouseSSN(String ssn) {
		setColumn(SPOUSE_SSN, ssn);
	}

	public String getSpouseSSN() {
		return getStringColumnValue(SPOUSE_SSN);
	}
	
	public Collection ejbFindAll() throws FinderException, RemoteException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindAllBySSN(String ssn) throws FinderException, RemoteException {
		StringBuffer query = new StringBuffer("select "+ getIDColumnName() +" from ");
		query.append(this.getEntityName());
		query.append(" where ");
		query.append(SSN);
		query.append(" = '");
		query.append(ssn);
		query.append("'");
		
		return idoFindPKsBySQL(query.toString());
	}
}
