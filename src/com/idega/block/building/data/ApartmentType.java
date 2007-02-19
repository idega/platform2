package com.idega.block.building.data;


import java.util.Collection;

public interface ApartmentType extends BuildingEntity {
	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getApartmentCategoryId
	 */
	public int getApartmentCategoryId();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getApartmentCategory
	 */
	public ApartmentCategory getApartmentCategory();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getAbbreviation
	 */
	public String getAbbreviation();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getExtraInfo
	 */
	public String getExtraInfo();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getImageId
	 */
	public int getImageId();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getFloorPlanId
	 */
	public int getFloorPlanId();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getRoomCount
	 */
	public int getRoomCount();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getArea
	 */
	public double getArea();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getRent
	 */
	public int getRent();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getKitchen
	 */
	public boolean getKitchen();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getBathRoom
	 */
	public boolean getBathRoom();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getStorage
	 */
	public boolean getStorage();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getBalcony
	 */
	public boolean getBalcony();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getStudy
	 */
	public boolean getStudy();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getLoft
	 */
	public boolean getLoft();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getFurniture
	 */
	public boolean getFurniture();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getApartments
	 */
	public Collection getApartments();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#getLocked
	 */
	public boolean getLocked();

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setApartmentCategoryId
	 */
	public void setApartmentCategoryId(int apartment_category_id);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setApartmentCategoryId
	 */
	public void setApartmentCategoryId(Integer apartment_category_id);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setAbbreviation
	 */
	public void setAbbreviation(String abbreviation);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setExtraInfo
	 */
	public void setExtraInfo(String info);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setImageId
	 */
	public void setImageId(int image_id);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setImageId
	 */
	public void setImageId(Integer image_id);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setFloorPlanId
	 */
	public void setFloorPlanId(int floorplan_id);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setFloorPlanId
	 */
	public void setFloorPlanId(Integer floorplan_id);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setRoomCount
	 */
	public void setRoomCount(int room_count);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setRoomCount
	 */
	public void setRoomCount(Integer room_count);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setArea
	 */
	public void setArea(double area);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setArea
	 */
	public void setArea(Double area);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setRent
	 */
	public void setRent(int rent);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setRent
	 */
	public void setRent(Integer rent);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setKitchen
	 */
	public void setKitchen(boolean kitchen);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setBathRoom
	 */
	public void setBathRoom(boolean bathroom);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setStorage
	 */
	public void setStorage(boolean storage);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setBalcony
	 */
	public void setBalcony(boolean balcony);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setStudy
	 */
	public void setStudy(boolean study);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setLoft
	 */
	public void setLoft(boolean loft);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setFurniture
	 */
	public void setFurniture(boolean furniture);

	/**
	 * @see com.idega.block.building.data.ApartmentTypeBMPBean#setLocked
	 */
	public void setLocked(boolean locked);
}