package com.idega.block.school.data;


public interface SchoolClassHome extends com.idega.data.IDOHome
{
 public SchoolClass create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolClass findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}