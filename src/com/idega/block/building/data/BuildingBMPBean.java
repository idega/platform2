package com.idega.block.building.data;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class BuildingBMPBean extends com.idega.data.TextEntityBMPBean  implements com.idega.block.building.data.Building {
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getNameColumnName(), "Name", true, true, java.lang.String.class);
		addAttribute(getInfoColumnName(), "Info", true, true, java.lang.String.class);
		addAttribute(getImageIdColumnName(), "Photo", true, true, java.lang.Integer.class);
		addAttribute(
			getComplexIdColumnName(),
			"Complex",
			true,
			true,
			java.lang.Integer.class,
			"many-to-one",
			Complex.class);
		addAttribute(getStreetColumnName(), "Street", true, true, java.lang.String.class);
		addAttribute(getStreetNumberColumnName(), "Streetnumber", true, true, java.lang.Integer.class);
		addAttribute(getSerieColumnName(), "Serie", true, true, java.lang.String.class, 2);
		super.setMaxLength("info", 4000);
	}
	public String getEntityName() {
		return getNameTableName();
	}
	public static String getNameTableName() {
		return "bu_building";
	}
	public static String getNameColumnName() {
		return "name";
	}
	public static String getInfoColumnName() {
		return "info";
	}
	public static String getComplexIdColumnName() {
		return "bu_complex_id";
	}
	public static String getImageIdColumnName() {
		return "ic_image_id";
	}
	public static String getStreetColumnName() {
		return "street";
	}
	public static String getStreetNumberColumnName() {
		return "street_number";
	}
	public static String getSerieColumnName() {
		return "serie";
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
	public int getComplexId() {
		return getIntColumnValue(getComplexIdColumnName());
	}
	public void setComplexId(int complex_id) {
		setColumn(getComplexIdColumnName(), complex_id);
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
	public String getStreet() {
		return getStringColumnValue(getStreetColumnName());
	}
	public void setStreet(String street) {
		setColumn(getStreetColumnName(), street);
	}
	public String getStreetNumber() {
		return getStringColumnValue(getStreetNumberColumnName());
	}
	public void setStreetNumber(String street_number) {
		setColumn(getStreetNumberColumnName(), street_number);
	}
	public String getSerie() {
		return getStringColumnValue(getSerieColumnName());
	}
	public void setSerie(String serie) {
		setColumn(getSerieColumnName(), serie);
	}
}
