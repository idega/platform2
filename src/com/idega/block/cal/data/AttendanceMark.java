package com.idega.block.cal.data;


public interface AttendanceMark extends com.idega.data.IDOEntity{
	public String getMark();
	public String getMarkDescription();
	public void setMark(String mark);
	public void setMarkDescription(String markDescription);
}
