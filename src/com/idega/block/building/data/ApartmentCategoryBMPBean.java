/*
 * $Id: ApartmentCategoryBMPBean.java,v 1.5 2003/05/03 01:36:11 tryggvil Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.building.data;
/**
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class ApartmentCategoryBMPBean
	extends com.idega.block.text.data.TextEntityBMPBean 
	implements com.idega.block.building.data.ApartmentCategory {
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getNameColumnName(), "Name", true, true, java.lang.String.class);
		addAttribute(getInfoColumnName(), "Info", true, true, java.lang.String.class);
		addAttribute(getImageIdColumnName(), "Icon", true, true, java.lang.Integer.class);
		super.setMaxLength(getInfoColumnName(), 4000);
	}
	public String getEntityName() {
		return getNameTableName();
	}
	public static String getNameTableName() {
		return "bu_aprt_cat";
	}
	public static String getNameColumnName() {
		return "name";
	}
	public static String getInfoColumnName() {
		return "info";
	}
	public static String getImageIdColumnName() {
		return "ic_image_id";
	}
	public String getName() {
		return getStringColumnValue(getNameColumnName());
	}
	public void setName(String name) {
		setColumn(getNameColumnName(), name);
	}
	public String getInfo() {
		return getStringColumnValue(getInfoColumnName());
	}
	public void setInfo(String info) {
		setColumn(getInfoColumnName(), info);
	}
	public int getImageId() {
		return getIntColumnValue(getImageIdColumnName());
	}
	public void setImageId(int image_id) {
		setColumn(getImageIdColumnName(), image_id);
	}
	public void setImageId(Integer image_id) {
		setColumn(getImageIdColumnName(), image_id);
	}
}