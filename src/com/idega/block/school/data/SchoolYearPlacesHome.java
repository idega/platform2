package com.idega.block.school.data;


public interface SchoolYearPlacesHome extends com.idega.data.IDOHome
{
 public SchoolYearPlaces create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolYearPlaces findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}