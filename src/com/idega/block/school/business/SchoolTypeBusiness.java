package com.idega.block.school.business;

import javax.ejb.*;

public interface SchoolTypeBusiness extends com.idega.business.IBOService
{
 public com.idega.block.school.data.SchoolType getSchoolType(java.lang.Object p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolTypesInCategory(java.lang.String p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolTypesForChildCare() throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolTypes() throws java.rmi.RemoteException;
 public void storeSchoolType(int p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeSchoolType(int p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolTypesForSchool() throws java.rmi.RemoteException;
}
