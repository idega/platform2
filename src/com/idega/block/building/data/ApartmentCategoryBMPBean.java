/*
 * $Id: ApartmentCategoryBMPBean.java,v 1.7 2004/06/05 06:41:28 aron Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

/**
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class ApartmentCategoryBMPBean
	extends com.idega.block.text.data.TextEntityBMPBean 
	implements com.idega.block.building.data.ApartmentCategory {
	
	protected static final String IC_IMAGE_ID = "ic_image_id";
	protected static final String INFO = "info";
	protected static final String NAME = "name";
	protected static final String BU_APRT_CAT = "bu_aprt_cat";
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(NAME, "Name", true, true, java.lang.String.class);
		addAttribute(INFO, "Info", true, true, java.lang.String.class);
		addAttribute(IC_IMAGE_ID, "Icon", true, true, java.lang.Integer.class);
		super.setMaxLength(INFO, 4000);
	}
	public String getEntityName() {
		return BU_APRT_CAT;
	}

	public String getName() {
		return getStringColumnValue(NAME);
	}
	public void setName(String name) {
		setColumn(NAME, name);
	}
	public String getInfo() {
		return getStringColumnValue(INFO);
	}
	public void setInfo(String info) {
		setColumn(INFO, info);
	}
	public int getImageId() {
		return getIntColumnValue(IC_IMAGE_ID);
	}
	public void setImageId(int image_id) {
		setColumn(IC_IMAGE_ID, image_id);
	}
	public void setImageId(Integer image_id) {
		setColumn(IC_IMAGE_ID, image_id);
	}
	public Collection ejbFindAll()throws FinderException{
		return super.idoFindAllIDsBySQL();
	}
	public Collection getApartmentTypes(){
		try {
			return super.idoGetRelatedEntities(ApartmentType.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getApartmentTypes() : " + e.getMessage());
		}
	}
}