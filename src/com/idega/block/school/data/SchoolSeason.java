package com.idega.block.school.data;


public interface SchoolSeason extends com.idega.data.IDOEntity
{
 public java.lang.String getName() throws java.rmi.RemoteException;
 public java.sql.Date getSchoolSeasonDueDate() throws java.rmi.RemoteException;
 public java.sql.Date getSchoolSeasonEnd() throws java.rmi.RemoteException;
 public java.lang.String getSchoolSeasonName() throws java.rmi.RemoteException;
 public java.sql.Date getSchoolSeasonStart() throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public void setSchoolSeasonDueDate(java.util.Date p0) throws java.rmi.RemoteException;
 public void setSchoolSeasonEnd(java.util.Date p0) throws java.rmi.RemoteException;
 public void setSchoolSeasonName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setSchoolSeasonStart(java.util.Date p0) throws java.rmi.RemoteException;
}
