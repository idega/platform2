package com.idega.block.school.data;


public interface SchoolAreaHome extends com.idega.data.IDOHome
{
 public SchoolArea create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolArea findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllBySchoolType(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllSchoolAreas()throws javax.ejb.FinderException, java.rmi.RemoteException;

}