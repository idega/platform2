package com.idega.block.school.data;

import javax.ejb.*;

public interface SchoolClass extends com.idega.data.IDOEntity
{
 public java.lang.String getName() throws java.rmi.RemoteException;
 public void setSchoolSeasonId(int p0) throws java.rmi.RemoteException;
 public int getSchoolYearId() throws java.rmi.RemoteException;
 public int getTeacherId() throws java.rmi.RemoteException;
 public void setSchoolId(int p0) throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public int getSchoolId() throws java.rmi.RemoteException;
 public void setSchoolYearId(int p0) throws java.rmi.RemoteException;
 public java.lang.String getSchoolClassName(java.lang.String p0) throws java.rmi.RemoteException;
 public int getSchoolSeasonId() throws java.rmi.RemoteException;
 public void setSchoolClassName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setTeacherId(int p0) throws java.rmi.RemoteException;
}
