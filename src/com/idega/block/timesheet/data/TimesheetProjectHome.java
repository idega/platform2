package com.idega.block.timesheet.data;


public interface TimesheetProjectHome extends com.idega.data.IDOHome
{
 public TimesheetProject create() throws javax.ejb.CreateException;
 public TimesheetProject createLegacy();
 public TimesheetProject findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TimesheetProject findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TimesheetProject findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}