package com.idega.block.school.business;

import javax.ejb.*;

import com.idega.block.school.data.SchoolYearHome;

public interface SchoolYearBusiness extends com.idega.business.IBOService
{
 public com.idega.block.school.data.SchoolYear getSchoolYear(java.lang.Object p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolYearsByAge(int p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolYears() throws java.rmi.RemoteException;
 public void storeSchoolYear(int p0,java.lang.String p1,java.lang.String p2,int p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeSchoolYear(int p0) throws java.rmi.RemoteException;
 public SchoolYearHome getSchoolYearHome() throws java.rmi.RemoteException;
}
