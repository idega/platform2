/*
 * $Id: PostingStringBMPBean.java,v 1.3 2003/08/28 15:25:42 joakim Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.posting.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOQuery;

/**
 * Holds information about when ceratin file format strings for sending accounting data as a string are valid.
 * @author Joakim
 * @see PostingField
 */
public class PostingStringBMPBean extends GenericEntity implements PostingString, IDOLegacyEntity
{
	private static final String ENTITY_NAME = "cacc_posting_string";

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
	
	public Integer ejbFindPostingStringByDate(Date date) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere(COLUMN_VALID_FROM);
		sql.appendLessThanOrEqualsSign().append("'"+date+"'");
		sql.appendAnd().append(COLUMN_VALID_TO);
		sql.appendGreaterThanOrEqualsSign().append("'"+date+"'");
		return (Integer) idoFindOnePKByQuery(sql);
	}
/*	
	public int ejbFindPostingStringByDate(Date date) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere(COLUMN_VALID_FROM);
		sql.appendLessThanOrEqualsSign().append("'"+date+"'");
		sql.appendAnd().append(COLUMN_VALID_TO);
		sql.appendGreaterThanOrEqualsSign().append("'"+date+"'");
		return ((Integer) idoFindOnePKByQuery(sql)).intValue();
	}
*/	
	public Collection ejbFindKonterignStrings() throws FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityName());
		return idoFindPKsBySQL(sql.toString());
	}
}
