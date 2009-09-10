package com.idega.block.school.data;


public interface SchoolView extends com.idega.data.IDOEntity
{
 public java.util.List findAllBySchoolType(int p0)throws com.idega.data.IDOFinderException, java.rmi.RemoteException;
 public int getSchoolTypeId() throws java.rmi.RemoteException;
 public java.lang.String getSchoolName() throws java.rmi.RemoteException;
 public java.lang.String getSchoolTypeName() throws java.rmi.RemoteException;
 public java.lang.String getSchoolCategory() throws java.rmi.RemoteException;
 public int getSchoolId() throws java.rmi.RemoteException;
 public java.lang.String getSchoolAreaName() throws java.rmi.RemoteException;
 public int getSchoolAreaId() throws java.rmi.RemoteException;
}
