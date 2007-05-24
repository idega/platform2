package com.idega.block.building.data;



public interface ApartmentSubcategory extends BuildingEntity {
	/**
	 * @see com.idega.block.building.data.ApartmentSubcategoryBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.building.data.ApartmentSubcategoryBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.building.data.ApartmentSubcategoryBMPBean#getImage
	 */
	public int getImage();

	/**
	 * @see com.idega.block.building.data.ApartmentSubcategoryBMPBean#getApartmentCategory
	 */
	public ApartmentCategory getApartmentCategory();

	/**
	 * @see com.idega.block.building.data.ApartmentSubcategoryBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see com.idega.block.building.data.ApartmentSubcategoryBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.building.data.ApartmentSubcategoryBMPBean#setImage
	 */
	public void setImage(int imageID);

	/**
	 * @see com.idega.block.building.data.ApartmentSubcategoryBMPBean#setImage
	 */
	public void setImage(Integer imageID);

	/**
	 * @see com.idega.block.building.data.ApartmentSubcategoryBMPBean#setApartmentCategory
	 */
	public void setApartmentCategory(ApartmentCategory aprtSubcat);

	/**
	 * @see com.idega.block.building.data.ApartmentSubcategoryBMPBean#setApartmentCategoryID
	 */
	public void setApartmentCategoryID(Integer aprtSubcat);
}