package com.idega.block.school.data;

import javax.ejb.*;

public interface SchoolSeason extends com.idega.data.IDOEntity
{
 public java.sql.Date getSchoolSeasonEnd() throws java.rmi.RemoteException;
 public java.sql.Date getSchoolSeasonStart() throws java.rmi.RemoteException;
 public void setSchoolSeasonStart(java.sql.Date p0) throws java.rmi.RemoteException;
 public void setSchoolSeasonEnd(java.sql.Date p0) throws java.rmi.RemoteException;
}
