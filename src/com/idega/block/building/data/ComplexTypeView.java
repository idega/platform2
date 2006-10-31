package com.idega.block.building.data;


import com.idega.data.IDOEntity;

public interface ComplexTypeView extends IDOEntity {
	/**
	 * @see com.idega.block.building.data.ComplexTypeViewBMPBean#getPrimaryKeyClass
	 */
	public Class getPrimaryKeyClass();

	/**
	 * @see com.idega.block.building.data.ComplexTypeViewBMPBean#getComplexName
	 */
	public String getComplexName();

	/**
	 * @see com.idega.block.building.data.ComplexTypeViewBMPBean#getApartmentTypeName
	 */
	public String getApartmentTypeName();

	/**
	 * @see com.idega.block.building.data.ComplexTypeViewBMPBean#getApartmentTypeID
	 */
	public Integer getApartmentTypeID();

	/**
	 * @see com.idega.block.building.data.ComplexTypeViewBMPBean#getApartmentType
	 */
	public ApartmentType getApartmentType();

	/**
	 * @see com.idega.block.building.data.ComplexTypeViewBMPBean#getComplexID
	 */
	public Integer getComplexID();

	/**
	 * @see com.idega.block.building.data.ComplexTypeViewBMPBean#getComplex
	 */
	public Complex getComplex();

	/**
	 * @see com.idega.block.building.data.ComplexTypeViewBMPBean#getCreationSQL
	 */
	public String getCreationSQL();
}