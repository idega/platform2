/*
 * Created on 24.6.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package com.idega.block.building.data;



import com.idega.data.PrimaryKey;

/**
 * @author aron
 *
 * ComplexTypeViewKey TODO Describe this type
 */
public class ComplexTypeViewKey extends PrimaryKey {

	private String COLUMN_COMPLEX_ID= ComplexTypeViewBMPBean.BU_COMPLEX_ID;
	private String COLUMN_APRT_TYPE_ID = ComplexTypeViewBMPBean.BU_APRT_TYPE_ID;
	
	/**
	 * @param scorecardID
	 * @param holeID
	 */
	public ComplexTypeViewKey(Object complexID, Object apartmentTypeID) {
		this();
		setComplexID(complexID);
		setApartmentTypeID(apartmentTypeID);
	}
	
	public ComplexTypeViewKey() {
		super();
	}
	
	public void setApartmentTypeID(Object apartmentTypeID) {
		setPrimaryKeyValue(COLUMN_APRT_TYPE_ID, apartmentTypeID);
	}

	public Object getApartmentTypeID() {
		return getPrimaryKeyValue(COLUMN_APRT_TYPE_ID);
	}

	public void setComplexID(Object complexID) {
		setPrimaryKeyValue(COLUMN_COMPLEX_ID, complexID);
	}

	public Object getComplexID() {
		return getPrimaryKeyValue(COLUMN_COMPLEX_ID);
	}
}
