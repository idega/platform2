package com.idega.block.building.data;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class ApartmentTypeBMPBean
	extends com.idega.block.text.data.TextEntityBMPBean 
	implements ApartmentType {
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(
			getApartmentCategoryIdColumnName(),
			"Category",
			true,
			true,
			java.lang.Integer.class,
			"many-to-one",
			ApartmentCategory.class);
		addAttribute(getNameColumnName(), "Name", true, true, java.lang.String.class);
		addAttribute(getInfoColumnName(), "Info", true, true, java.lang.String.class, 4000);
		addAttribute(getExtraInfoColumnName(), "Extra_Info", true, true, java.lang.String.class, 4000);
		addAttribute(getImageIdColumnName(), "Photo", true, true, java.lang.Integer.class);
		addAttribute(getFloorPlanIdColumnName(), "Plan", true, true, java.lang.Integer.class);
		addAttribute(getRoomCountColumnName(), "Room Count", true, true, java.lang.Integer.class);
		addAttribute(getAreaColumnName(), "Area", true, true, java.lang.Float.class);
		addAttribute(getKitchenColumnName(), "Kitchen", true, true, java.lang.Boolean.class);
		addAttribute(getBathroomColumnName(), "Bath", true, true, java.lang.Boolean.class);
		addAttribute(getStorageColumnName(), "Storage", true, true, java.lang.Boolean.class);
		addAttribute(getBalconyColumnName(), "Balcony", true, true, java.lang.Boolean.class);
		addAttribute(getStudyColumnName(), "Study", true, true, java.lang.Boolean.class);
		addAttribute(getLoftColumnName(), "Loft", true, true, java.lang.Boolean.class);
		addAttribute(getRentColumnName(), "Rent", true, true, java.lang.Integer.class);
		addAttribute(getFurnitureColumnName(), "Furniture", true, true, java.lang.Boolean.class);
		super.setMaxLength(getInfoColumnName(), 4000);
	}
	public String getEntityName() {
		return getNameTableName();
	}
	public static String getNameTableName() {
		return "bu_aprt_type";
	}
	public static String getApartmentCategoryIdColumnName() {
		return "BU_APRT_CAT_ID";
	}
	public static String getNameColumnName() {
		return "name";
	}
	public static String getInfoColumnName() {
		return "info";
	}
	public static String getExtraInfoColumnName() {
		return "extra_info";
	}
	public static String getImageIdColumnName() {
		return "ic_image_id";
	}
	public static String getFloorPlanIdColumnName() {
		return "plan_id";
	}
	public static String getRoomCountColumnName() {
		return "room_count";
	}
	public static String getAreaColumnName() {
		return "area";
	}
	public static String getKitchenColumnName() {
		return "kitchen";
	}
	public static String getBathroomColumnName() {
		return "bathroom";
	}
	public static String getStorageColumnName() {
		return "storage";
	}
	public static String getBalconyColumnName() {
		return "balcony";
	}
	public static String getStudyColumnName() {
		return "study";
	}
	public static String getLoftColumnName() {
		return "loft";
	}
	public static String getRentColumnName() {
		return "rent";
	}
	public static String getFurnitureColumnName() {
		return "furniture";
	}
	public String getName() {
		return getStringColumnValue(getNameColumnName());
	}
	public void setName(String name) {
		setColumn(getNameColumnName(), name);
	}
	public int getApartmentCategoryId() {
		return getIntColumnValue(getApartmentCategoryIdColumnName());
	}
	public void setApartmentCategoryId(int apartment_category_id) {
		setColumn(getApartmentCategoryIdColumnName(), apartment_category_id);
	}
	public void setApartmentCategoryId(Integer apartment_category_id) {
		setColumn(getApartmentCategoryIdColumnName(), apartment_category_id);
	}
	public String getInfo() {
		return getStringColumnValue(getInfoColumnName());
	}
	public void setInfo(String info) {
		setColumn(getInfoColumnName(), info);
	}
	public String getExtraInfo() {
		return getStringColumnValue(getExtraInfoColumnName());
	}
	public void setExtraInfo(String info) {
		setColumn(getExtraInfoColumnName(), info);
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
	public int getFloorPlanId() {
		return getIntColumnValue(getFloorPlanIdColumnName());
	}
	public void setFloorPlanId(int floorplan_id) {
		setColumn(getFloorPlanIdColumnName(), floorplan_id);
	}
	public void setFloorPlanId(Integer floorplan_id) {
		setColumn(getFloorPlanIdColumnName(), floorplan_id);
	}
	public int getRoomCount() {
		return getIntColumnValue(getRoomCountColumnName());
	}
	public void setRoomCount(int room_count) {
		setColumn(getRoomCountColumnName(), room_count);
	}
	public void setRoomCount(Integer room_count) {
		setColumn(getRoomCountColumnName(), room_count);
	}
	public float getArea() {
		return getFloatColumnValue(getAreaColumnName());
	}
	public void setArea(float area) {
		setColumn(getAreaColumnName(), area);
	}
	public void setArea(Float area) {
		setColumn(getAreaColumnName(), area);
	}
	public int getRent() {
		return getIntColumnValue(getRentColumnName());
	}
	public void setRent(int rent) {
		setColumn(getRentColumnName(), rent);
	}
	public void setRent(Integer rent) {
		setColumn(getRentColumnName(), rent);
	}
	public void setKitchen(boolean kitchen) {
		setColumn(getKitchenColumnName(), kitchen);
	}
	public boolean getKitchen() {
		return getBooleanColumnValue(getKitchenColumnName());
	}
	public void setBathRoom(boolean bathroom) {
		setColumn(getBathroomColumnName(), bathroom);
	}
	public boolean getBathRoom() {
		return getBooleanColumnValue(getBathroomColumnName());
	}
	public void setStorage(boolean storage) {
		setColumn(getStorageColumnName(), storage);
	}
	public boolean getStorage() {
		return getBooleanColumnValue(getStorageColumnName());
	}
	public void setBalcony(boolean balcony) {
		setColumn(getBalconyColumnName(), balcony);
	}
	public boolean getBalcony() {
		return getBooleanColumnValue(getBalconyColumnName());
	}
	public void setStudy(boolean study) {
		setColumn(getStudyColumnName(), study);
	}
	public boolean getStudy() {
		return getBooleanColumnValue(getStudyColumnName());
	}
	public void setLoft(boolean loft) {
		setColumn(getLoftColumnName(), loft);
	}
	public boolean getLoft() {
		return getBooleanColumnValue(getLoftColumnName());
	}
	public boolean getFurniture() {
		return getBooleanColumnValue(getFurnitureColumnName());
	}
	public void setFurniture(boolean furniture) {
		setColumn(getFurnitureColumnName(), furniture);
	}
}
