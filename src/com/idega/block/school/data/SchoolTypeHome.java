package com.idega.block.school.data;


public interface SchoolTypeHome extends com.idega.data.IDOHome
{
 public SchoolType create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolType findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllSchoolTypes()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllByCategory(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;

}