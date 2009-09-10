package com.idega.block.building.data;
/**

 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class RoomSubTypeBMPBean
	extends com.idega.block.text.data.TextEntityBMPBean 
	implements com.idega.block.building.data.RoomSubType {
	
	public String getEntityName() {
		return "room_sub_type";
	}
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(
			"room_type_id",
			"Mynd",
			true,
			true,
			"java.lang.Integer",
			"many-to-one",
			"com.idega.block.building.data.RoomType");
		addAttribute("name", "Heiti", true, true, "java.lang.String");
		addAttribute("info", "Uppl?singar", true, true, "java.lang.String");
		addAttribute("image_id", "Mynd", true, true, "java.lang.Integer");
		addAttribute("floorplan_id", "Teikning", true, true, "java.lang.Integer");
		addAttribute("room_count", "Fj?ldi herbergja", true, true, "java.lang.Integer");
		addAttribute("area", "Flatarm?l", true, true, "java.lang.Float");
		addAttribute("kitchen", "Eldh?s", true, true, "java.lang.Boolean");
		addAttribute("bathroom", "Ba?herbergi", true, true, "java.lang.Boolean");
		addAttribute("storage", "Geymsla", true, true, "java.lang.Boolean");
		addAttribute("balcony", "Svalir", true, true, "java.lang.Boolean");
		addAttribute("study", "Lesa?sta?a", true, true, "java.lang.Boolean");
		addAttribute("loft", "H?aloft", true, true, "java.lang.Boolean");
		addAttribute("rent", "Leiga", true, true, "java.lang.Integer");
		super.setMaxLength("info", 5000);
	}
	public String getName() {
		return getStringColumnValue("name");
	}
	public void setName(String name) {
		setColumn("name", name);
	}
	public int getRoomTypeId() {
		return getIntColumnValue("room_type_id");
	}
	public void setRoomTypeId(int room_type_id) {
		setColumn("room_type_id", room_type_id);
	}
	public void setRoomTypeId(Integer room_type_id) {
		setColumn("room_type_id", room_type_id);
	}
	public String getInfo() {
		return getStringColumnValue("info");
	}
	public void setInfo(String info) {
		setColumn("info", info);
	}
	public int getImageId() {
		return getIntColumnValue("image_id");
	}
	public void setImageId(int image_id) {
		setColumn("image_id", image_id);
	}
	public void setImageId(Integer image_id) {
		setColumn("image_id", image_id);
	}
	public int getFloorPlanId() {
		return getIntColumnValue("floorplan_id");
	}
	public void setFloorPlanId(int floorplan_id) {
		setColumn("floorplan_id", floorplan_id);
	}
	public void setFloorPlanId(Integer floorplan_id) {
		setColumn("floorplan_id", floorplan_id);
	}
	public int getRoomCount() {
		return getIntColumnValue("room_count");
	}
	public void setRoomCount(int room_count) {
		setColumn("room_count", room_count);
	}
	public void setRoomCount(Integer room_count) {
		setColumn("room_count", room_count);
	}
	public float getArea() {
		return getFloatColumnValue("area");
	}
	public void setArea(float area) {
		setColumn("area", area);
	}
	public void setArea(Float area) {
		setColumn("area", area);
	}
	public int getRent() {
		return getIntColumnValue("rent");
	}
	public void setRent(int rent) {
		setColumn("rent", rent);
	}
	public void setRent(Integer rent) {
		setColumn("rent", rent);
	}
	public void setKitchen(boolean kitchen) {
		setColumn("kitchen", kitchen);
	}
	public boolean getKitchen() {
		return getBooleanColumnValue("kitchen");
	}
	public void setBathRoom(boolean bathroom) {
		setColumn("bathroom", bathroom);
	}
	public boolean getBathRoom() {
		return getBooleanColumnValue("kitchen");
	}
	public void setStorage(boolean storage) {
		setColumn("storage", storage);
	}
	public boolean getStorage() {
		return getBooleanColumnValue("storage");
	}
	public void setBalcony(boolean balcony) {
		setColumn("balcony", balcony);
	}
	public boolean getBalcony() {
		return getBooleanColumnValue("balcony");
	}
	public void setStudy(boolean study) {
		setColumn("study", study);
	}
	public boolean getStudy() {
		return getBooleanColumnValue("study");
	}
	public void setLoft(boolean loft) {
		setColumn("loft", loft);
	}
	public boolean getLoft() {
		return getBooleanColumnValue("loft");
	}
}
