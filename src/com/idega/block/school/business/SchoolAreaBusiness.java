package com.idega.block.school.business;

import javax.ejb.*;

public interface SchoolAreaBusiness extends com.idega.business.IBOService
{
 public java.util.Collection findAllSchoolAreasByType(int p0) throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolArea getSchoolArea(java.lang.Object p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolAreas() throws java.rmi.RemoteException;
 public void storeSchoolArea(int p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findAllSchoolAreasByTypes(java.util.Collection p0) throws java.rmi.RemoteException;
 public void removeSchoolArea(int p0) throws java.rmi.RemoteException;
}
