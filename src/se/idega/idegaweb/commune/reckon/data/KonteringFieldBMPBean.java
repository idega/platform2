/*
 * $Id: KonteringFieldBMPBean.java,v 1.1 2003/07/09 14:09:49 joakim Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.reckon.data;

import com.idega.data.GenericEntity;

/**
 * Holds information about fields in strings holding accounting information, sent to external accounting systems
 * From Kravspecifikation Check & Peng 13.3
 * @author Joakim
 * @see KonteringField
 */

public class KonteringFieldBMPBean extends GenericEntity implements KonteringField
{
	private static final String ENTITY_NAME = "cp_kontering_field";

//	private static final String COLUMN_KONTERING_ID = "kontering_field_id";
	private static final String COLUMN_CP_KONTERING_STRING_ID = "cp_kontering_string_id";
	private static final String COLUMN_ORDER = "order";
	private static final String COLUMN_FIELD_TITLE = "field_title";
	private static final String COLUMN_LENGTH = "length";
	private static final String COLUMN_JUSTIFICATION = "justification";
	private static final String COLUMN_MANDATORY = "mandatory";
	private static final String COLUMN_PAD_CHAR = "pad_char";

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
		addAttribute(COLUMN_CP_KONTERING_STRING_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_ORDER, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_FIELD_TITLE, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_LENGTH, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_JUSTIFICATION, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_MANDATORY,"",true,true,java.lang.Boolean.class);
		addAttribute(COLUMN_PAD_CHAR, "", true, true, java.lang.String.class, 1);
		
		
		addManyToOneRelationship(COLUMN_CP_KONTERING_STRING_ID,KonteringString.class);
		setNullable(COLUMN_CP_KONTERING_STRING_ID, false);
		setNullable(COLUMN_ORDER, false);
		setNullable(COLUMN_FIELD_TITLE, false);
		setNullable(COLUMN_LENGTH, false);
		setNullable(COLUMN_JUSTIFICATION, false);
		setNullable(COLUMN_MANDATORY, false);
		setNullable(COLUMN_PAD_CHAR, false);
	}

	public int getKonteringStringId() {
		return getIntColumnValue(COLUMN_CP_KONTERING_STRING_ID);	
	}
	
	public int getOrder() {
		return getIntColumnValue(COLUMN_ORDER);	
	}
	
	public String getFieldTitle() {
		return getStringColumnValue(COLUMN_FIELD_TITLE);	
	}

	public int getLength() {
		return getIntColumnValue(COLUMN_LENGTH);	
	}
	
	public int getJustification() {
		return getIntColumnValue(COLUMN_JUSTIFICATION);	
	}
	
	public boolean getIsMandatory() {
		return getBooleanColumnValue(COLUMN_MANDATORY, false);
	}
	
	public char getPadChar() {
		return getCharColumnValue(COLUMN_PAD_CHAR);
	}

	public void setKonteringStringId(int konteringStringId) {
		setColumn(COLUMN_CP_KONTERING_STRING_ID, konteringStringId);
	}

	public void setOrder(int order) {
		setColumn(COLUMN_ORDER, order);
	}

	public void setFieldTitle(String title) {
		setColumn(COLUMN_FIELD_TITLE, title);
	}

	public void setLength(int length) {
		setColumn(COLUMN_LENGTH, length);
	}

	public void setJustification(int justification) {
		setColumn(COLUMN_JUSTIFICATION, justification);
	}

	public void setIsMandatory(boolean mandatory) {
		setColumn(COLUMN_FIELD_TITLE, mandatory);
	}

	public void setPadChar(char padChar) {
		setColumn(COLUMN_PAD_CHAR, padChar);
	}
}
