package com.idega.block.school.business;

import javax.ejb.*;

public interface SchoolClassBusiness extends com.idega.business.IBOService
{
 public com.idega.block.school.data.SchoolClass findSchoolClass(java.lang.Object p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findSchoolClassesBySchool(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findSchoolClassesBySchoolAndSeason(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findSchoolClassesBySchoolAndSeasonAndTeacher(int p0,int p1,int p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findSchoolClassesBySchoolAndSeasonAndYear(int p0,int p1,int p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findSchoolClassesBySchoolAndTeacher(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findSchoolClassesBySchoolAndYear(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findSchoolClassesByTeacher(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getNumberOfStudentsInClass(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClassHome getSchoolClassHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void invalidateSchoolClass(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeSchoolClass(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void storeSchoolClass(int p0,java.lang.String p1,int p2,int p3,int p4,int p5)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
