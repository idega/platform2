package com.idega.block.cal.data;

import java.util.Collection;


public interface CalendarEntryGroupHome extends com.idega.data.IDOHome
{
 public CalendarEntryGroup create() throws javax.ejb.CreateException;
 public CalendarEntryGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Collection findEntryGroupsByLedgerID(int ledgerID) throws javax.ejb.FinderException;

}