package com.idega.block.cal.data;


public interface CalendarEntryGroup extends com.idega.data.IDOEntity {
	
	public int getEntryGroupID();
	public String getName();
	public int getLedgerID();
	public void setName(String name);
	public void setLedgerID(int ledgerID);
	public void addEntry(CalendarEntry entry);
	public void removeEntryRelation();
	public void removeOneEntryRelation(CalendarEntry entry);
}
