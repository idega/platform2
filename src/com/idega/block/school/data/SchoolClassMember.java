package com.idega.block.school.data;

import javax.ejb.*;

public interface SchoolClassMember extends com.idega.data.IDOEntity
{
 public int getRegistratorId() throws java.rmi.RemoteException;
 public void setRegistratorId(int p0) throws java.rmi.RemoteException;
 public void setRegisterDate(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public int getClassMemberId() throws java.rmi.RemoteException;
 public int getSchoolClassId() throws java.rmi.RemoteException;
 public void setSchoolClassId(int p0) throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public java.sql.Timestamp getRegisterDate() throws java.rmi.RemoteException;
 public void setClassMemberId(int p0) throws java.rmi.RemoteException;
}
