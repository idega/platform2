package com.idega.block.school.business;

import javax.ejb.*;

public interface SchoolBusiness extends com.idega.business.IBOService
{
 public com.idega.block.school.data.School createSchool(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,int p5)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.school.data.School createSchool(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,int p5,int[] p6)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.school.data.School createSchool(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,int p9,int[] p10)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.school.data.School createSchool(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,int p9,int[] p10,int[] p11)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClass createSchoolClass(java.lang.String p0,com.idega.block.school.data.School p1,com.idega.block.school.data.SchoolYear p2,com.idega.block.school.data.SchoolSeason p3)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClassMember createSchoolClassMember(com.idega.block.school.data.SchoolClass p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findAllSchools() throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolsByAreaAndType(int p0,int p1) throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolsByType(int p0) throws java.rmi.RemoteException;
 public java.util.Map getMapOfSchools() throws java.rmi.RemoteException;
 public com.idega.user.data.Group getNewSchoolGroup(java.lang.String p0,java.lang.String p1)throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootSchoolGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.School getSchool(java.lang.Object p0) throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolAreaHome getSchoolAreaHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClassHome getSchoolClassHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClassMemberHome getSchoolClassMemberHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolHome getSchoolHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getSchoolRelatedSchoolTypes(com.idega.block.school.data.School p0) throws java.rmi.RemoteException;
 public java.util.Map getSchoolRelatedSchoolYears(com.idega.block.school.data.School p0) throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolTypeHome getSchoolTypeHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolYearHome getSchoolYearHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeSchool(int p0) throws java.rmi.RemoteException;
 public com.idega.block.school.data.School storeSchool(int p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.lang.String p9,int p10,int[] p11,int[] p12)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
