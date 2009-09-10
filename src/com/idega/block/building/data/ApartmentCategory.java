package com.idega.block.building.data;


import java.util.Collection;

public interface ApartmentCategory extends BuildingEntity {
	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#getImageId
	 */
	public int getImageId();

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#getShowSpouse
	 */
	public boolean getShowSpouse();

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#getSpouseMandatory
	 */
	public boolean getSpouseMandatory();

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#getShowChildren
	 */
	public boolean getShowChildren();

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#getChildrenMandatory
	 */
	public boolean getChildrenMandatory();

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#getMaxNumberOfChoices
	 */
	public int getMaxNumberOfChoices();

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#setImageId
	 */
	public void setImageId(int image_id);

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#setImageId
	 */
	public void setImageId(Integer image_id);

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#setShowSpouse
	 */
	public void setShowSpouse(boolean showSpouse);

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#setSpouseMandatory
	 */
	public void setSpouseMandatory(boolean spouseMandatory);

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#setShowChildren
	 */
	public void setShowChildren(boolean showChildren);

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#setChildrenMandatory
	 */
	public void setChildrenMandatory(boolean childrenMandatory);

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#setMaxNumberOfChoices
	 */
	public void setMaxNumberOfChoices(int maxNumberOfChoices);

	/**
	 * @see com.idega.block.building.data.ApartmentCategoryBMPBean#getApartmentTypes
	 */
	public Collection getApartmentTypes();
}