package com.idega.block.finance.data;


public interface TariffHome extends com.idega.data.IDOHome
{
 public Tariff create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Tariff findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllByPrimaryKeyArray(java.lang.String[] p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllByColumn(java.lang.String p0,int p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllByColumnOrdered(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllByColumnOrdered(java.lang.String p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllByColumn(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException, java.rmi.RemoteException;

}