package com.idega.block.building.data;


import java.sql.Date;

public interface Apartment extends BuildingEntity {
	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#getFloorId
	 */
	public int getFloorId();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#getFloor
	 */
	public Floor getFloor();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setFloorId
	 */
	public void setFloorId(int floor_id);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setFloorId
	 */
	public void setFloorId(Integer floor_id);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#getApartmentTypeId
	 */
	public int getApartmentTypeId();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#getApartmentType
	 */
	public ApartmentType getApartmentType();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setApartmentTypeId
	 */
	public void setApartmentTypeId(int apartment_type_id);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setApartmentTypeId
	 */
	public void setApartmentTypeId(Integer apartment_type_id);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#getImageId
	 */
	public int getImageId();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setImageId
	 */
	public void setImageId(int room_type_id);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setImageId
	 */
	public void setImageId(Integer room_type_id);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#getRentable
	 */
	public boolean getRentable();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#getUnavailableUntil
	 */
	public Date getUnavailableUntil();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setRentable
	 */
	public void setRentable(boolean rentable);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setUnavailableUntil
	 */
	public void setUnavailableUntil(Date date);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#getSerie
	 */
	public String getSerie();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setSerie
	 */
	public void setSerie(String serie);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#getStatus
	 */
	public String getStatus();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setStatus
	 */
	public void setStatus(String status);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setStatusFrozen
	 */
	public void setStatusFrozen();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setStatusAvailable
	 */
	public void setStatusAvailable();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setStatusRented
	 */
	public void setStatusRented();

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#setSerialNumber
	 */
	public void setSerialNumber(String number);

	/**
	 * @see com.idega.block.building.data.ApartmentBMPBean#getSerialNumber
	 */
	public String getSerialNumber();
}