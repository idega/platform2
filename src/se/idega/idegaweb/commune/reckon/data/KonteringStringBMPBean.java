/*
 * $Id: KonteringStringBMPBean.java,v 1.1 2003/07/09 14:04:10 joakim Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.reckon.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

/**
 * Holds information about when ceratin file format strings for sending accounting data as a string are valid.
 * @author Joakim
 * @see KonteringField
 */
public class KonteringStringBMPBean extends GenericEntity implements KonteringString
{
	private static final String ENTITY_NAME = "cp_kontering_string";

//	private static final String COLUMN_KONTERING_ID = "kontering_id";
	private static final String COLUMN_VALID_FROM = "valid_from";
	private static final String COLUMN_VALID_TO = "valid_to";

	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_VALID_FROM, "", true, true, Date.class);
		addAttribute(COLUMN_VALID_TO, "", true, true, Date.class);
		setNullable(COLUMN_VALID_FROM, false);
		setNullable(COLUMN_VALID_TO, false);
	}

	public void setValidFrom(Date date) {
		setColumn(COLUMN_VALID_FROM, date);
	}

	public void setValidTo(Date date) {
		setColumn(COLUMN_VALID_TO, date);
	}

	public Date getValidFrom() {
		return (Date) getColumnValue(COLUMN_VALID_FROM);
	}

	public Date getValidTo() {
		return (Date) getColumnValue(COLUMN_VALID_TO);
	}
	
	public Collection ejbFindKonterignStrings() throws FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityName());
		return idoFindPKsBySQL(sql.toString());
	}
}
