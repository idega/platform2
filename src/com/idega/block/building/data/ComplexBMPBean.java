package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class ComplexBMPBean extends com.idega.block.text.data.TextEntityBMPBean implements com.idega.block.building.data.Complex {
	
	protected static final String IC_IMAGE_ID = "ic_image_id";
	protected static final String INFO = "info";
	public static final String NAME = "name";
	protected static final String BU_COMPLEX = "bu_complex";
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(NAME, "Name", true, true, java.lang.String.class);
		addAttribute(INFO, "Info", true, true, java.lang.String.class, 4000);
		addAttribute(IC_IMAGE_ID, "Map", true, true, java.lang.Integer.class);
	}
	public String getEntityName() {
		return BU_COMPLEX;
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
	
	public Collection ejbFindAll() throws FinderException{
		return idoFindAllIDsBySQL();
	}
	
	public Collection getBuildings(){
		try {
			return super.idoGetRelatedEntities(Building.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getBuildings() : " + e.getMessage());
		}
	}
	
}
