package com.idega.block.school.data;

import javax.ejb.*;

public interface SchoolYear extends com.idega.data.IDOEntity
{
 public int getSchoolYearAge() throws java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
 public void setSchoolYearAge(int p0) throws java.rmi.RemoteException;
 public void setSchoolYearName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setSchoolYearInfo(java.lang.String p0) throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public java.lang.String getSchoolYearName() throws java.rmi.RemoteException;
 public java.lang.String getSchoolYearInfo() throws java.rmi.RemoteException;
}
