package com.idega.block.timesheet.data;

import com.idega.data.IDOHome;

public interface TimesheetProjectHome extends IDOHome
{
 public TimesheetProject create() throws javax.ejb.CreateException;
 public TimesheetProject findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllOrderByNumber()throws javax.ejb.FinderException;
 public java.util.Collection findEntryRelatedByUserWithinPeriod(java.lang.Integer p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException;
 public java.util.Collection findUserRelated(com.idega.user.data.User p0)throws javax.ejb.FinderException;

}