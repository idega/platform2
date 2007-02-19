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

	public static final String A = "A", B = "B", C = "C", D = "D", E = "E";

	public static final String indexType = "ABCDEFGHIJK";

	public TariffIndexBMPBean() {
		super();
	}

	public TariffIndexBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());

		addAttribute(getColumnNameNewValue(), "New Value", true, true,
				Double.class);
		addAttribute(getColumnNameOldValue(), "Old alue", true, true,
				Double.class);
		addAttribute(getColumnNameDate(), "LastUpdated", true, true,
				Timestamp.class);
		addAttribute(getColumnNameName(), "Name", true, true, String.class);
		addAttribute(getColumnNameInfo(), "Info", true, true, String.class);
		addAttribute(getColumnNameType(), "type", true, true, String.class);
	}

	public static String getTariffIndexEntityName() {
		return "FIN_TARIFF_INDEX";
	}

	public static String getColumnNameNewValue() {
		return "NEW_VALUE";
	}

	public static String getColumnNameOldValue() {
		return "OLD_VALUE";
	}

	public static String getColumnNameName() {
		return "NAME";
	}

	public static String getColumnNameInfo() {
		return "INFO";
	}

	public static String getColumnNameType() {
		return "INDEX_TYPE";
	}

	public static String getColumnNameDate() {
		return "FROM_DATE";
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
