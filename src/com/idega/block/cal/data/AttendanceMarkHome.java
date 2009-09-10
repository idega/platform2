package com.idega.block.cal.data;

import java.util.Collection;


public interface AttendanceMarkHome extends com.idega.data.IDOHome
{
 public AttendanceMark create() throws javax.ejb.CreateException;
 public AttendanceMark findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Collection findMarks() throws javax.ejb.FinderException;

}