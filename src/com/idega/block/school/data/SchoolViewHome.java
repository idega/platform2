package com.idega.block.school.data;


public interface SchoolViewHome extends com.idega.data.IDOHome
{
 public SchoolView create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolView findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}