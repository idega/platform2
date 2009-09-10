package com.idega.block.building.data;


import com.idega.data.IDOEntity;

public interface ComplexSubcategoryView extends IDOEntity {
	/**
	 * @see com.idega.block.building.data.ComplexSubcategoryViewBMPBean#getPrimaryKeyClass
	 */
	public Class getPrimaryKeyClass();

	/**
	 * @see com.idega.block.building.data.ComplexSubcategoryViewBMPBean#getComplexName
	 */
	public String getComplexName();

	/**
	 * @see com.idega.block.building.data.ComplexSubcategoryViewBMPBean#getSubcategoryName
	 */
	public String getSubcategoryName();

	/**
	 * @see com.idega.block.building.data.ComplexSubcategoryViewBMPBean#getSubcategoryID
	 */
	public Integer getSubcategoryID();

	/**
	 * @see com.idega.block.building.data.ComplexSubcategoryViewBMPBean#getSubcategory
	 */
	public ApartmentSubcategory getSubcategory();

	/**
	 * @see com.idega.block.building.data.ComplexSubcategoryViewBMPBean#getComplexID
	 */
	public Integer getComplexID();

	/**
	 * @see com.idega.block.building.data.ComplexSubcategoryViewBMPBean#getComplex
	 */
	public Complex getComplex();

	/**
	 * @see com.idega.block.building.data.ComplexSubcategoryViewBMPBean#getCreationSQL
	 */
	public String getCreationSQL();
}