package com.idega.block.school.data;


public interface SchoolHome extends com.idega.data.IDOHome
{
 public School create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public School findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllBySchoolArea(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllBySchoolType(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllBySchoolAreaAndType(int p0,int p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllSchools()throws javax.ejb.FinderException, java.rmi.RemoteException;

}