package com.idega.block.finance.business;


public interface AssessmentBusiness extends com.idega.business.IBOService
{
 public void groupEntries(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws java.lang.Exception, java.rmi.RemoteException;
 public void assessTariffsToAccount(java.util.Collection p0,int p1,java.util.Date p2,int p3,int p4,int p5) throws java.rmi.RemoteException;
 public void assessTariffsToAccount(float p0,java.lang.String p1,java.lang.String p2,int p3,int p4,java.util.Date p5,int p6,int p7,boolean p8) throws java.rmi.RemoteException;
 public void assessTariffsToAccount(java.lang.String[] p0,int p1,java.util.Date p2,int p3,int p4,int p5)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void groupEntriesWithSQL(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws java.lang.Exception, java.rmi.RemoteException;
 public com.idega.block.finance.data.AccountEntry storeAccountEntry(int p0,int p1,int p2,int p3,float p4,float p5,float p6,java.util.Date p7,java.lang.String p8,java.lang.String p9,java.lang.String p10)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean rollBackAssessment(int p0) throws java.rmi.RemoteException;
 public int getGroupEntryCount(com.idega.block.finance.data.EntryGroup p0) throws java.rmi.RemoteException;
}
