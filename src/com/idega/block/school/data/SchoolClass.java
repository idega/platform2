package com.idega.block.school.data;

import javax.ejb.*;

public interface SchoolClass extends com.idega.data.IDOEntity
{
 public void setSchoolSeasonId(int p0) throws java.rmi.RemoteException;
 public int getTeacherId() throws java.rmi.RemoteException;
 public void setSchoolYear(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getSchoolClassName(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getSchoolYear() throws java.rmi.RemoteException;
 public int getSchoolSeasonId() throws java.rmi.RemoteException;
 public void setSchoolClassName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setTeacherId(int p0) throws java.rmi.RemoteException;
}
