package com.idega.block.school.business;

import javax.ejb.*;

public interface SchoolClassMemberBusiness extends com.idega.business.IBOService,com.idega.block.process.business.CaseBusiness
{
 public com.idega.block.school.data.SchoolClassMember findByStudentAndSeason(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findClassMember(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClassMember findClassMemberInClass(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findStudentsBySchoolAndSeason(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findStudentsBySchoolAndSeasonAndYear(int p0,int p1,int p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findStudentsInClass(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClassMemberHome getSchoolClassMemberHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeSchoolClassMember(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeSchoolClassMemberFromClass(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void storeSchoolClassMember(int p0,int p1,java.sql.Timestamp p2,int p3,java.lang.String p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void storeSchoolClassMember(int p0,int p1,java.sql.Timestamp p2,int p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
