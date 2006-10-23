package com.idega.block.building.data;


import java.util.Collection;

public interface Building extends BuildingEntity {
	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getComplexId
	 */
	public int getComplexId();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getComplex
	 */
	public Complex getComplex();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setComplexId
	 */
	public void setComplexId(int complex_id);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getImageId
	 */
	public int getImageId();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setImageId
	 */
	public void setImageId(int image_id);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setImageId
	 */
	public void setImageId(Integer image_id);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getStreet
	 */
	public String getStreet();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setStreet
	 */
	public void setStreet(String street);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getStreetNumber
	 */
	public String getStreetNumber();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setStreetNumber
	 */
	public void setStreetNumber(String street_number);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getSerie
	 */
	public String getSerie();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setSerie
	 */
	public void setSerie(String serie);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getDivision
	 */
	public String getDivision();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setDivision
	 */
	public void setDivision(String division);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getPostalCode
	 */
	public String getPostalCode();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setPostalCode
	 */
	public void setPostalCode(String postalCode);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getPostalAddress
	 */
	public String getPostalAddress();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setPostalAddress
	 */
	public void setPostalAddress(String postalAddress);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getLocked
	 */
	public boolean getLocked();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setLocked
	 */
	public void setLocked(boolean locked);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getFloors
	 */
	public Collection getFloors();
}