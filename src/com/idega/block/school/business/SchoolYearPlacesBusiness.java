package com.idega.block.school.business;

import javax.ejb.*;

public interface SchoolYearPlacesBusiness extends com.idega.business.IBOService
{
 public com.idega.block.school.data.SchoolYearPlaces getSchoolYearPlaces(java.lang.Object p0) throws java.rmi.RemoteException;
 public java.util.Map getMapOfSchoolYearPlaces(int p0) throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolYearPlacesHome getSchoolYearPlacesHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeSchool(int p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolYearPlaces(int p0) throws java.rmi.RemoteException;
 public void storeSchoolYearPlaces(int p0,int p1,int p2,int p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
