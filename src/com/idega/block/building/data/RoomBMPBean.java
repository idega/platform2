package com.idega.block.building.data;
/**

 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class RoomBMPBean extends com.idega.block.text.data.TextEntityBMPBean implements com.idega.block.building.data.Room {
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute("name", "Name", true, true, "java.lang.String");
		addAttribute("info", "Info", true, true, "java.lang.String");
		addAttribute(
			"floor_id",
			"Floor",
			true,
			true,
			"java.lang.Integer",
			"many-to-one",
			"com.idega.block.building.data.Floor");
		addAttribute(
			"Apartment_type_id",
			"Apartmenttype",
			true,
			true,
			"java.lang.Integer",
			"many-to-one",
			"com.idega.block.building.data.ApartmentType");
		addAttribute("rentable", "Rentable", true, true, "java.lang.Boolean");
		addAttribute("ic_image_id", "Photo", true, true, "java.lang.Integer");
		super.setMaxLength("info", 4000);
	}
	public String getEntityName() {
		return "room";
	}
	public String getName() {
		return getStringColumnValue("name");
	}
	public void setName(String name) {
		setColumn("name", name);
	}
	public String getInfo() {
		return getStringColumnValue("info");
	}
	public void setInfo(String info) {
		setColumn("info", info);
	}
	public int getFloorId() {
		return getIntColumnValue("floor_id");
	}
	public void setFloorId(int floor_id) {
		setColumn("floor_id", floor_id);
	}
	public void setFloorId(Integer floor_id) {
		setColumn("floor_id", floor_id);
	}
	public int getRoomSubTypeId() {
		return getIntColumnValue("sub_type_id");
	}
	public void setRoomSubTypeId(int sub_type_id) {
		setColumn("sub_type_id", sub_type_id);
	}
	public void setRoomSubTypeId(Integer sub_type_id) {
		setColumn("sub_type_id", sub_type_id);
	}
	public int getImageId() {
		return getIntColumnValue("image_id");
	}
	public void setImageId(int room_type_id) {
		setColumn("image_id", room_type_id);
	}
	public void setImageId(Integer room_type_id) {
		setColumn("image_id", room_type_id);
	}
	public boolean getRentable() {
		return getBooleanColumnValue("rentable");
	}
	public void setRentable(boolean rentable) {
		setColumn("rentable", rentable);
	}
}
