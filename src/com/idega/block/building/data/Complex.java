package com.idega.block.building.data;


import java.util.Collection;

import com.idega.core.builder.data.ICPage;

public interface Complex extends BuildingEntity {
	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#getImageId
	 */
	public int getImageId();

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#getFlashPageID
	 */
	public int getFlashPageID();

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#getFlashPage
	 */
	public ICPage getFlashPage();

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#getLocked
	 */
	public boolean getLocked();

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#setImageId
	 */
	public void setImageId(int image_id);

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#setImageId
	 */
	public void setImageId(Integer image_id);

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#setFlashPageID
	 */
	public void setFlashPageID(int pageID);

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#setFlashPage
	 */
	public void setFlashPage(ICPage page);

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#setLocked
	 */
	public void setLocked(boolean locked);

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#getBuildings
	 */
	public Collection getBuildings();
}