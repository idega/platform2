package com.idega.block.cal.data;


public interface CalendarEntryGroup extends com.idega.data.IDOEntity {
	
	public int getEntryGroupID();
	public String getName();
	public void setName(String p0);
	public void addEntry(CalendarEntry p0);
	public void removeEntryRelation();
	public void removeOneEntryRelation(CalendarEntry entry);
}
