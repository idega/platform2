package com.idega.block.building.data;
/**

 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class FloorBMPBean extends com.idega.block.text.data.TextEntityBMPBean  implements com.idega.block.building.data.Floor {
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getNameColumnName(), "Name", true, true, java.lang.String.class);
		addAttribute(getInfoColumnName(), "Info", true, true, java.lang.String.class, 4000);
		addAttribute(
			getBuildingIdColumnName(),
			"Building",
			true,
			true,
			java.lang.Integer.class,
			"many-to-one",
			com.idega.block.building.data.Building.class);
		addAttribute(getImageIdColumnName(), "Plan", true, true, java.lang.Integer.class);
	}
	public String getEntityName() {
		return getNameTableName();
	}
	public static String getNameTableName() {
		return "bu_floor";
	}
	public static String getNameColumnName() {
		return "name";
	}
	public static String getInfoColumnName() {
		return "info";
	}
	public static String getBuildingIdColumnName() {
		return "bu_building_id";
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
	public int getBuildingId() {
		return getIntColumnValue(getBuildingIdColumnName());
	}
	public void setBuildingId(int building_id) {
		setColumn(getBuildingIdColumnName(), building_id);
	}
	public void setBuildingId(Integer building_id) {
		setColumn(getBuildingIdColumnName(), building_id);
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
