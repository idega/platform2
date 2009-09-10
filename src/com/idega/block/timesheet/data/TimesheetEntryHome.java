package com.idega.block.timesheet.data;


public interface TimesheetEntryHome extends com.idega.data.IDOHome
{
 public TimesheetEntry create() throws javax.ejb.CreateException;
 public TimesheetEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findBookedByUserWithinPeriod(java.lang.Integer p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException;
 public java.util.Collection findByDateAndUser(java.sql.Date p0,java.lang.Integer p1)throws javax.ejb.FinderException;
 public java.util.Collection findByProjectWithinPeriod(java.lang.Integer p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException;
 public java.util.Collection findByUserAndProjectWithinPeriod(java.lang.Integer p0,java.lang.Integer p1,java.sql.Date p2,java.sql.Date p3)throws javax.ejb.FinderException;
 public java.util.Collection findByUserWithinPeriod(java.lang.Integer p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException;
 public java.util.Collection findUnbookedByUserBeforeDate(java.lang.Integer p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public java.util.Collection findUnbookedByUserWithinPeriod(java.lang.Integer p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException;
 public int countByUserBeforeDate(java.lang.Integer p0,java.sql.Date p1)throws com.idega.data.IDOException;

}