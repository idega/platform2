package com.idega.block.building.data;

import com.idega.block.text.data.TextEntityBMPBean;

public class ApartmentSubcategoryBMPBean extends TextEntityBMPBean implements
		ApartmentSubcategory {

	protected static final String ENTITY_NAME = "bu_aprt_sub_cat";
	
	protected static final String COLUMN_NAME = "name";

	protected static final String COLUMN_IMAGE = "image";

	protected static final String COLUMN_INFO = "info";
	
	protected static final String COLUMN_APARTMENT_CATEGORY = "aprt_cat";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", String.class);
		addAttribute(COLUMN_INFO, "Info", String.class, 4000);
		addAttribute(COLUMN_IMAGE, "Icon", Integer.class);
		addManyToOneRelationship(COLUMN_APARTMENT_CATEGORY, ApartmentCategory.class);
	}

	//getters
	public String getInfo() {
		return getStringColumnValue(COLUMN_INFO);
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public int getIcon() {
		return getIntColumnValue(COLUMN_IMAGE);
	}
	
	public ApartmentCategory getApartmentCategory() {
		return (ApartmentCategory) getColumnValue(COLUMN_APARTMENT_CATEGORY);
	}
	
	//setters
	public void setInfo(String info) {
		setColumn(COLUMN_INFO, info);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}
	
	public void setImage(int imageID) {
		setColumn(COLUMN_IMAGE, imageID);
	}
	
	public void setImage(Integer imageID) {
		setColumn(COLUMN_IMAGE, imageID);
	}
	
	public void setApartmentCategory(ApartmentCategory aprtCat) {
		setColumn(COLUMN_APARTMENT_CATEGORY, aprtCat);
	}
	
	//sql
	
}