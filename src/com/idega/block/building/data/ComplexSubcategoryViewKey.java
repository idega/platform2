package com.idega.block.building.data;

import com.idega.data.PrimaryKey;

public class ComplexSubcategoryViewKey extends PrimaryKey {
	private static final String COLUMN_COMPLEX_ID = ComplexSubcategoryViewBMPBean.COLUMN_COMPLEX;
	private static final String COLUMN_SUBCATEGORY = ComplexSubcategoryViewBMPBean.COLUMN_SUBCATEGORY;

	public ComplexSubcategoryViewKey(Object complexID, Object subcategoryID) {
		this();
		setComplexID(complexID);
		setSubcategoryID(subcategoryID);
	}

	public ComplexSubcategoryViewKey() {
		super();
	}

	public void setSubcategoryID(Object subcategoryID) {
		setPrimaryKeyValue(this.COLUMN_SUBCATEGORY, subcategoryID);
	}

	public Object getsubcategoryID() {
		return getPrimaryKeyValue(this.COLUMN_SUBCATEGORY);
	}

	public void setComplexID(Object complexID) {
		setPrimaryKeyValue(this.COLUMN_COMPLEX_ID, complexID);
	}

	public Object getComplexID() {
		return getPrimaryKeyValue(this.COLUMN_COMPLEX_ID);
	}
}