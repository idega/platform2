package com.idega.block.school.data;

import javax.ejb.*;

public interface SchoolType extends com.idega.data.IDOEntity
{
 public java.lang.String getSchoolTypeInfo() throws java.rmi.RemoteException;
 public void setSchoolTypeInfo(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getSchoolTypeName() throws java.rmi.RemoteException;
 public void setSchoolTypeName(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
}
