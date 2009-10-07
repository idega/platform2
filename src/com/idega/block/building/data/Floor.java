package com.idega.block.building.data;


import java.util.Collection;

public interface Floor extends BuildingEntity {
	/**
	 * @see com.idega.block.building.data.FloorBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.building.data.FloorBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.building.data.FloorBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.building.data.FloorBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see com.idega.block.building.data.FloorBMPBean#getBuildingId
	 */
	public int getBuildingId();

	/**
	 * @see com.idega.block.building.data.FloorBMPBean#getBuilding
	 */
	public Building getBuilding();

	/**
	 * @see com.idega.block.building.data.FloorBMPBean#setBuildingId
	 */
	public void setBuildingId(int building_id);

	/**
	 * @see com.idega.block.building.data.FloorBMPBean#setBuildingId
	 */
	public void setBuildingId(Integer building_id);

	/**
	 * @see com.idega.block.building.data.FloorBMPBean#setBuilding
	 */
	public void setBuilding(Building building);

	/**
	 * @see com.idega.block.building.data.FloorBMPBean#getImageId
	 */
	public int getImageId();

	/**
	 * @see com.idega.block.building.data.FloorBMPBean#setImageId
	 */
	public void setImageId(int image_id);

	/**
	 * @see com.idega.block.building.data.FloorBMPBean#setImageId
	 */
	public void setImageId(Integer image_id);

	/**
	 * @see com.idega.block.building.data.FloorBMPBean#getApartments
	 */
	public Collection getApartments();
}