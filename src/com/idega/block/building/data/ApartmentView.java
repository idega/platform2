package com.idega.block.building.data;


import com.idega.data.IDOEntity;

public interface ApartmentView extends IDOEntity {
	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getCreationSQL
	 */
	public String getCreationSQL();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getComplexID
	 */
	public Integer getComplexID();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getComplex
	 */
	public Complex getComplex();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getBuildingID
	 */
	public Integer getBuildingID();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getBuilding
	 */
	public Building getBuilding();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getFloorID
	 */
	public Integer getFloorID();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getFloor
	 */
	public Floor getFloor();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getApartmentID
	 */
	public Integer getApartmentID();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getApartment
	 */
	public Apartment getApartment();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getCategoryID
	 */
	public Integer getCategoryID();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getCategory
	 */
	public ApartmentCategory getCategory();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getTypeID
	 */
	public Integer getTypeID();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getType
	 */
	public ApartmentType getType();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getComplexName
	 */
	public String getComplexName();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getBuildingName
	 */
	public String getBuildingName();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getFloorName
	 */
	public String getFloorName();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getApartmentName
	 */
	public String getApartmentName();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getCategoryName
	 */
	public String getCategoryName();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getTypeName
	 */
	public String getTypeName();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getSubcategory
	 */
	public ApartmentSubcategory getSubcategory();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getSubcategoryID
	 */
	public int getSubcategoryID();

	/**
	 * @see com.idega.block.building.data.ApartmentViewBMPBean#getApartmentString
	 */
	public String getApartmentString(String delimiter);
}