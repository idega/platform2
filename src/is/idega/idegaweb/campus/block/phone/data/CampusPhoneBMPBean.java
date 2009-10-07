package is.idega.idegaweb.campus.block.phone.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.building.data.Apartment;
import com.idega.data.GenericEntity;

/**
 * 
 * Title:
 * 
 * Description:
 * 
 * Copyright: Copyright (c) 2000-2001 idega.is All Rights Reserved
 * 
 * Company: idega
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir </a>
 * 
 * @version 1.1
 * 
 */

public class CampusPhoneBMPBean extends GenericEntity implements CampusPhone {
	protected static String ENTITY_NAME = "cam_phone";
	
	protected static String COLUMN_PHONE_NUMBER = "phone_number";
	protected static String COLUMN_APARTMENT = "bu_apartment_id";
	protected static String COLUMN_DATE_INSTALLED = "date_installed";
	protected static String COLUMN_DATE_RESIGNED = "date_resigned";
	
	public CampusPhoneBMPBean() {

	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_PHONE_NUMBER, "Phone number", String.class);
		addOneToOneRelationship(COLUMN_APARTMENT, Apartment.class);
		addAttribute(COLUMN_DATE_INSTALLED, "Installed", Date.class);
		addAttribute(COLUMN_DATE_RESIGNED, "Resigned", Date.class);
	}

	public void setPhoneNumber(String number) {
		setColumn(COLUMN_PHONE_NUMBER, number);
	}

	public String getPhoneNumber() {
		return getStringColumnValue(COLUMN_PHONE_NUMBER);
	}

	public int getApartmentId() {
		return getIntColumnValue(COLUMN_APARTMENT);
	}

	public Apartment getApartment() {
		return (Apartment) getColumnValue(COLUMN_APARTMENT);
	}
	
	public void setApartmentId(int id) {
		setColumn(COLUMN_APARTMENT, id);
	}

	public void setApartment(Apartment apartment) {
		setColumn(COLUMN_APARTMENT, apartment);
	}
	
	public void setDateInstalled(java.sql.Date date) {
		setColumn(COLUMN_DATE_INSTALLED, date);
	}

	public Date getDateInstalled() {
		return getDateColumnValue(COLUMN_DATE_INSTALLED);
	}

	public void setDateResigned(Date date) {
		setColumn(COLUMN_DATE_RESIGNED, date);
	}

	public Date getDateResigned() {
		return getDateColumnValue(COLUMN_DATE_RESIGNED);
	}

	public Collection ejbFindAll() throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetSelect());
	}

	public Collection ejbFindByPhoneNumber(String number)
			throws FinderException {
		return idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(
				COLUMN_PHONE_NUMBER, number));
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}
}