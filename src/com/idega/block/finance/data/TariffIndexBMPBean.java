package com.idega.block.finance.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.category.data.CategoryEntityBMPBean;

/**
 * Title: idegaclasses Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class TariffIndexBMPBean extends CategoryEntityBMPBean implements
		TariffIndex {

	private static final String ENTITY_NAME = "FIN_TARIFF_INDEX";

	private static final String COLUMN_FROM_DATE = "FROM_DATE";

	private static final String COLUMN_INDEX_TYPE = "INDEX_TYPE";

	private static final String COLUMN_INFO = "INFO";

	private static final String COLUMN_NAME = "NAME";

	private static final String COLUMN_OLD_VALUE = "OLD_VALUE";

	private static final String COLUMN_NEW_VALUE = "NEW_VALUE";

	public static final String RENT_TYPE_A = "A";
	
	public static final String RENT_TYPE_B = "B";
	
	public static final String RENT_TYPE_C = "C";
	
	public static final String RENT_TYPE_D = "D";
	
	public static final String RENT_TYPE_E = "E";

	public static final String indexType = "ABCDEFGHIJK";

	public TariffIndexBMPBean() {
		super();
	}

	public TariffIndexBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());

		addAttribute(getColumnNameNewValue(), "New Value", Double.class);
		addAttribute(getColumnNameOldValue(), "Old alue", Double.class);
		addAttribute(getColumnNameDate(), "LastUpdated", Timestamp.class);
		addAttribute(getColumnNameName(), "Name", String.class);
		addAttribute(getColumnNameInfo(), "Info", String.class);
		addAttribute(getColumnNameType(), "type", String.class);
	}

	public static String getTariffIndexEntityName() {
		return ENTITY_NAME;
	}

	public static String getColumnNameNewValue() {
		return COLUMN_NEW_VALUE;
	}

	public static String getColumnNameOldValue() {
		return COLUMN_OLD_VALUE;
	}

	public static String getColumnNameName() {
		return COLUMN_NAME;
	}

	public static String getColumnNameInfo() {
		return COLUMN_INFO;
	}

	public static String getColumnNameType() {
		return COLUMN_INDEX_TYPE;
	}

	public static String getColumnNameDate() {
		return COLUMN_FROM_DATE;
	}

	public String getEntityName() {
		return getTariffIndexEntityName();
	}

	public double getIndex() {
		return getDoubleColumnValue(getColumnNameNewValue());
	}

	public void setIndex(double index) {
		setColumn(getColumnNameNewValue(), index);
	}

	public void setIndex(Double index) {
		setColumn(getColumnNameNewValue(), index);
	}

	public double getNewValue() {
		return getDoubleColumnValue(getColumnNameNewValue());
	}

	public void setNewValue(double index) {
		setColumn(getColumnNameNewValue(), index);
	}

	public void setNewValue(Double index) {
		setColumn(getColumnNameNewValue(), index);
	}

	public double getOldValue() {
		return getDoubleColumnValue(getColumnNameOldValue());
	}

	public void setOldValue(double index) {
		setColumn(getColumnNameOldValue(), index);
	}

	public void setOldValue(Double index) {
		setColumn(getColumnNameOldValue(), index);
	}

	public String getName() {
		return getStringColumnValue(getColumnNameName());
	}

	public void setName(String name) {
		setColumn(getColumnNameName(), name);
	}

	public String getInfo() {
		return getStringColumnValue(getColumnNameInfo());
	}

	public void setInfo(String info) {
		setColumn(getColumnNameInfo(), info);
	}

	public String getType() {
		return getStringColumnValue(getColumnNameType());
	}

	public void setType(String type) {
		setColumn(getColumnNameType(), type);
	}

	public java.sql.Timestamp getDate() {
		return (java.sql.Timestamp) getColumnValue(getColumnNameDate());
	}

	public void setDate(java.sql.Timestamp use_date) {
		setColumn(getColumnNameDate(), use_date);
	}

	public Object ejbFindLastByType(String type) throws FinderException {
		return super.idoFindOnePKByQuery(super.idoQueryGetSelect()
				.appendWhereEqualsQuoted(getColumnNameType(), type)
				.appendOrderByDescending(getIDColumnName()));
	}

	public Collection ejbFindLastTypeGrouped() throws FinderException {
		Collection coll = new java.util.ArrayList(indexType.length());
		for (int i = 0; i < indexType.length(); i++) {
			try {
				coll
						.add(ejbFindLastByType(String.valueOf(indexType
								.charAt(i))));
			} catch (FinderException e) {
				// e.printStackTrace();
			}
		}
		if (!coll.isEmpty()) {
			return coll;
		}
		throw new FinderException();
	}

}
