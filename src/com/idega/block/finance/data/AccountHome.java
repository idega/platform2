package com.idega.block.finance.data;


public interface AccountHome extends com.idega.data.IDOHome
{
 public Account create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Account findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllByUserId(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findBySearch(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,int p5)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllByUserIdAndType(int p0,java.lang.String p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByAssessmentRound(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;

}