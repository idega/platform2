package com.idega.block.finance.data;
import java.sql.SQLException;
/**
 * Title: idegaclasses Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class FinanceCategoryBMPBean extends com.idega.data.GenericEntity
		implements
			com.idega.block.finance.data.FinanceCategory {
	public FinanceCategoryBMPBean() {
		super();
	}
	public FinanceCategoryBMPBean(int id) throws SQLException {
		super(id);
	}
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getNameColumnName(), "Name", true, true, String.class);
		addAttribute(getDescriptionColumnName(), "Description", true, true, String.class);
		addAttribute(getDateColumnName(), "Date", true, true, java.sql.Date.class);
		addAttribute(getValidColumnName(), "Valid", true, true, Boolean.class);
		addManyToManyRelationShip(com.idega.core.component.data.ICObjectInstance.class);
	}
	public void insertStartData() throws Exception {
		FinanceCategory cat = ((FinanceCategoryHome) com.idega.data.IDOLookup.getHome(FinanceCategory.class)).create();
		cat.setName("Default finance");
		cat.setValid(true);
		cat.setDescription("Default Category for idegaWeb");
		cat.store();
	}
	public static String getCategoryTableName() {
		return "FIN_CAT";
	}
	public static String getNameColumnName() {
		return "NAME";
	}
	public static String getDescriptionColumnName() {
		return "DESCRIPTION";
	}
	public static String getValidColumnName() {
		return "VALID";
	}
	public static String getDateColumnName() {
		return "CAT_DATE";
	}
	public String getEntityName() {
		return getCategoryTableName();
	}
	public String getName() {
		return getCategoryName();
	}
	public String getCategoryName() {
		return getStringColumnValue(getNameColumnName());
	}
	public void setName(String name) {
		setCategoryName(name);
	}
	public void setCategoryName(String category_name) {
		setColumn(getNameColumnName(), category_name);
	}
	public String getDescription() {
		return getStringColumnValue(getDescriptionColumnName());
	}
	public void setDescription(String description) {
		setColumn(getDescriptionColumnName(), description);
	}
	public boolean getValid() {
		return getBooleanColumnValue(getValidColumnName());
	}
	public void setValid(boolean valid) {
		setColumn(getValidColumnName(), valid);
	}
	public java.sql.Date getDate() {
		return (java.sql.Date) getColumnValue(getDateColumnName());
	}
	public void setDate(java.sql.Date CATEGORY_DATE) {
		setColumn(getDateColumnName(), CATEGORY_DATE);
	}
}
