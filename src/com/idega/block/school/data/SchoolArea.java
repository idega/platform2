package com.idega.block.school.data;

import javax.ejb.*;

public interface SchoolArea extends com.idega.data.IDOEntity
{
 public java.lang.String getName() throws java.rmi.RemoteException;
 public java.lang.String getSchoolAreaCity() throws java.rmi.RemoteException;
 public void setSchoolAreaName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setSchoolAreaInfo(java.lang.String p0) throws java.rmi.RemoteException;
 public void setSchoolAreaCity(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getSchoolAreaName() throws java.rmi.RemoteException;
 public java.lang.String getSchoolAreaInfo() throws java.rmi.RemoteException;
}
