package com.idega.block.school.business;

import javax.ejb.*;

public interface SchoolBusiness extends com.idega.business.IBOService
{
 public java.util.Collection findAllSchoolsByAreaAndType(int p0,int p1) throws java.rmi.RemoteException;
 public java.util.Map getSchoolRelatedSchoolTypes(com.idega.block.school.data.School p0) throws java.rmi.RemoteException;
 public java.util.Map getSchoolRelatedSchoolYears(com.idega.block.school.data.School p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllSchools() throws java.rmi.RemoteException;
 public void removeSchool(int p0) throws java.rmi.RemoteException;
 public java.util.Map getMapOfSchools() throws java.rmi.RemoteException;
 public void storeSchool(int p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.lang.String p9,int p10,int[] p11,int[] p12)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolHome getSchoolHome() throws java.rmi.RemoteException;
 public com.idega.block.school.data.School getSchool(java.lang.Object p0) throws java.rmi.RemoteException;
}
