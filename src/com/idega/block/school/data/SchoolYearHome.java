package com.idega.block.school.data;


public interface SchoolYearHome extends com.idega.data.IDOHome
{
 public SchoolYear create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolYear findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllSchoolYears()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllByAge(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;

}