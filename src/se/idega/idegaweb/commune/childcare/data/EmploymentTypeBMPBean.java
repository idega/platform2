/*
 * $Id: EmploymentTypeBMPBean.java,v 1.3 2004/10/14 10:23:41 thomas Exp $
 *
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software hf.
 * Use is subject to license terms.
 * 
 */
package se.idega.idegaweb.commune.childcare.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;

/**
 * A class for the lookup table holding the data for the different types of employment
 * people can choose from when filling out check applications etc.....
 * 
 * @author palli
 */
public class EmploymentTypeBMPBean extends GenericEntity implements EmploymentType {
	private final static String ENTITY_NAME = "comm_empl_type";

	protected final static String COLUMN_EMPLOYMENT_TYPE = "empl_type";
	protected final static String COLUMN_LOCALIZATION_KEY = "empl_type_loc_key";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_EMPLOYMENT_TYPE, "Code for the type", true, true, String.class);
		addAttribute(COLUMN_LOCALIZATION_KEY, "A key to the localized name of the type", true, true, String.class);
	}

	/**
	 * A method to insert initial data into the table. Used to have some test data to start with.
	 */
	public void insertStartData() throws Exception {
		EmploymentTypeHome home = (EmploymentTypeHome) IDOLookup.getHome(EmploymentType.class);
		final String[] data = { "work_study", "seeking", "parent" };
		for (int i = 0; i < data.length; i++) {
			EmploymentType type = home.create();
			type.setEmploymentType(data[i]);
			type.setLocalizationKey("cc_" + data[i]);
			type.store();
		}
	}

	public void setEmploymentType(String type) {
		setColumn(COLUMN_EMPLOYMENT_TYPE, type);
	}

	public String getEmploymentType() {
		return getStringColumnValue(COLUMN_EMPLOYMENT_TYPE);
	}

	public void setLocalizationKey(String key) {
		setColumn(COLUMN_LOCALIZATION_KEY, key);
	}

	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
	}

	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}
}
