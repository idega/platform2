package com.idega.block.school.business;

import javax.ejb.*;

public interface SchoolClassMemberBusiness extends com.idega.business.IBOService,com.idega.block.process.business.CaseBusiness
{
 public java.util.Collection findClassMember(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClassMember findClassMemberInClass(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findStudentsInClass(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClassMemberHome getSchoolClassMemberHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeSchoolClassMember(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeSchoolClassMemberFromClass(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void storeSchoolClassMember(int p0,int p1,java.sql.Timestamp p2,int p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
