package com.idega.block.finance.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega multimedia
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class TariffBMPBean extends com.idega.data.GenericEntity implements
		com.idega.block.finance.data.Tariff {

	public static final String COLUMN_USE_DISCOUNT = "use_discount";
	
	public TariffBMPBean() {
	}

	public TariffBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getColumnTariffGroup(), "Group", true, true,
				Integer.class, "many-to-one", TariffGroup.class);
		addAttribute(getColumnName(), "Name", true, true,
				java.lang.String.class);
		addAttribute(getColumnPrice(), "Price", true, true,
				java.lang.Float.class);
		addAttribute(getColumnInfo(), "Info", true, true,
				java.lang.String.class, 4000);
		addAttribute(getColumnAccountKeyId(), "Accountkey", true, true,
				java.lang.Integer.class, "one-to-many",
				com.idega.block.finance.data.AccountKey.class);
		addAttribute(getColumnFromdate(), "From date", true, true,
				java.sql.Timestamp.class);
		addAttribute(getColumnTodate(), "To date", true, true,
				java.sql.Timestamp.class);
		addAttribute(getColumnAttribute(), "Attribute", true, true,
				java.lang.String.class);
		addAttribute(getColumnUseIndex(), "Use index", true, true,
				java.lang.Boolean.class);
		addAttribute(getColumnInUse(), "In Use", true, true,
				java.lang.Boolean.class);
		addAttribute(getColumnIndexType(), "Index type", true, true,
				java.lang.String.class, 10);
		addAttribute(getColumnIndexUpdated(), "Index updated", true, true,
				java.sql.Timestamp.class);

		addAttribute(COLUMN_USE_DISCOUNT, "Use discount", Boolean.class);
	}

	public static String getTariffEntityName() {
		return "FIN_TARIFF";
	}

	public static String getColumnTariffGroup() {
		return "FIN_TARIFF_GROUP_ID";
	}

	public static String getColumnAccountKeyId() {
		return "FIN_ACC_KEY_ID";
	}

	public static String getColumnName() {
		return "NAME";
	}

	public static String getColumnInfo() {
		return "INFO";
	}

	public static String getColumnPrice() {
		return "PRICE";
	}

	public static String getColumnFromdate() {
		return "FROM_DATE";
	}

	public static String getColumnTodate() {
		return "TO_DATE";
	}

	public static String getColumnInUse() {
		return "IN_USE";
	}

	public static String getColumnUseIndex() {
		return "USE_INDEX";
	}

	public static String getColumnAttribute() {
		return "ATTRIBUTE";
	}

	public static String getColumnIndexType() {
		return "INDEX_TYPE";
	}

	public static String getColumnIndexUpdated() {
		return "INDEX_UPDATED";
	}

	public String getEntityName() {
		return getTariffEntityName();
	}

	public String getName() {
		return getStringColumnValue(getColumnName());
	}

	public void setName(String name) {
		setColumn(getColumnName(), name);
	}

	public String getTariffAttribute() {
		return getStringColumnValue(getColumnAttribute());
	}

	public void setTariffAttribute(String attribute) {
		setColumn(getColumnAttribute(), attribute);
	}

	public float getPrice() {
		return getFloatColumnValue(getColumnPrice());
	}

	public void setPrice(float price) {
		setColumn(getColumnPrice(), price);
	}

	public void setPrice(Float price) {
		setColumn(getColumnPrice(), price);
	}

	public String getInfo() {
		return getStringColumnValue(getColumnInfo());
	}

	public void setInfo(String info) {
		setColumn(getColumnInfo(), info);
	}

	public int getAccountKeyId() {
		return getIntColumnValue(getColumnAccountKeyId());
	}

	public void setAccountKeyId(Integer account_key_id) {
		setColumn(getColumnAccountKeyId(), account_key_id);
	}

	public void setAccountKeyId(int account_key_id) {
		setColumn(getColumnAccountKeyId(), account_key_id);
	}

	public int getTariffGroupId() {
		return getIntColumnValue(getColumnTariffGroup());
	}

	public void setTariffGroupId(Integer group_id) {
		setColumn(getColumnTariffGroup(), group_id);
	}

	public void setTariffGroupId(int group_id) {
		setColumn(getColumnTariffGroup(), group_id);
	}

	public Timestamp getUseFromDate() {
		return (Timestamp) getColumnValue(getColumnFromdate());
	}

	public void setUseFromDate(Timestamp use_date) {
		setColumn(getColumnFromdate(), use_date);
	}

	public Timestamp getUseToDate() {
		return (Timestamp) getColumnValue(getColumnTodate());
	}

	public void setUseToDate(Timestamp use_date) {
		setColumn(getColumnTodate(), use_date);
	}

	public Timestamp getIndexUpdated() {
		return (Timestamp) getColumnValue(getColumnIndexUpdated());
	}

	public void setIndexUpdated(Timestamp use_date) {
		setColumn(getColumnIndexUpdated(), use_date);
	}

	public void setUseIndex(boolean useindex) {
		setColumn(getColumnUseIndex(), useindex);
	}

	public boolean getUseIndex() {
		return getBooleanColumnValue(getColumnUseIndex());
	}

	public void setInUse(boolean useindex) {
		setColumn(getColumnInUse(), useindex);
	}

	public boolean getInUse() {
		return getBooleanColumnValue(getColumnInUse());
	}

	public String getIndexType() {
		return getStringColumnValue(getColumnIndexType());
	}

	public void setIndexType(String type) {
		setColumn(getColumnIndexType(), type);
	}

	public boolean getUseDiscount() {
		return getBooleanColumnValue(COLUMN_USE_DISCOUNT, false);
	}
	
	public void setUseDiscount(boolean useDiscount) {
		setColumn(COLUMN_USE_DISCOUNT, useDiscount);
	}
	
	public java.util.Collection ejbFindAllByPrimaryKeyArray(String[] array)
			throws javax.ejb.FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(getIDColumnName());
		sql.append(" in (");
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sql.append(",");
			}
			sql.append(array[i]);
		}
		sql.append(")");
		return super.idoFindPKsBySQL(sql.toString());

	}

	public java.util.Collection ejbFindAllByColumnOrdered(String column,
			String value, String order) throws javax.ejb.FinderException {
		return super.idoFindAllIDsByColumnOrderedBySQL(column, value, order);
	}

	public java.util.Collection ejbFindAllByColumnOrdered(String column,
			String value, String column2, String value2, String order)
			throws javax.ejb.FinderException {
		return super.idoFindAllIDsByColumnOrderedBySQL(column, value, order);
	}

	public java.util.Collection ejbFindAllByColumn(String column, String value)
			throws javax.ejb.FinderException {
		return super.idoFindAllIDsByColumnBySQL(column, value);
	}

	public java.util.Collection ejbFindAllByColumn(String column, int value)
			throws javax.ejb.FinderException {
		return super.idoFindPKsBySQL("select * from " + getEntityName()
				+ " where " + column + " = " + value);
	}

	public Collection ejbFindByTariffGroup(Integer groupId)
			throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect()
				.appendWhereEquals(getColumnTariffGroup(), groupId));
	}

	public Collection ejbFindByAttribute(String attribute)
			throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect()
				.appendWhereEqualsQuoted(getColumnAttribute(), attribute));
	}

}
