package com.idega.block.timesheet.data;


public interface TimesheetEntryHome extends com.idega.data.IDOHome
{
 public TimesheetEntry create() throws javax.ejb.CreateException;
 public TimesheetEntry createLegacy();
 public TimesheetEntry findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TimesheetEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TimesheetEntry findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}