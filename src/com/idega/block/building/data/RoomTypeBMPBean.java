package com.idega.block.building.data;
/**

 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class RoomTypeBMPBean extends com.idega.block.text.data.TextEntityBMPBean  implements com.idega.block.building.data.RoomType {
	
	public String getEntityName() {
		return "room_type";
	}
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute("name", "Heiti", true, true, "java.lang.String");
		addAttribute("info", "Uppl?singar", true, true, "java.lang.String");
		super.setMaxLength("info", 5000);
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
}
