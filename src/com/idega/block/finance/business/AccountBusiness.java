package com.idega.block.finance.business;


public interface AccountBusiness extends com.idega.business.IBOService
{
 public com.idega.block.finance.data.Account getAccount(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.AccountHome getAccountHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.business.FinanceService getFinanceService()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection listOfAccountEntries(java.lang.Integer p0) throws java.rmi.RemoteException;
 public java.util.Collection listOfAccountEntries(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2) throws java.rmi.RemoteException;
 public java.util.Collection listOfAccountKeys() throws java.rmi.RemoteException;
 public java.util.Collection listOfAccounts(int p0) throws java.rmi.RemoteException;
 public java.util.Collection listOfAccounts() throws java.rmi.RemoteException;
 public java.util.Collection listOfAccounts(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.util.Collection listOfKeySortedEntries(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2) throws java.rmi.RemoteException;
 public java.util.Collection listOfPhoneEntries(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2) throws java.rmi.RemoteException;
 public java.util.Collection listOfPhoneEntries(int p0,com.idega.util.IWTimestamp p1,java.lang.String p2) throws java.rmi.RemoteException;
 public java.util.Collection listOfTariffKeys() throws java.rmi.RemoteException;
 public com.idega.block.finance.data.Account makeNewAccount(int p0,java.lang.String p1,java.lang.String p2,int p3,java.lang.String p4,int p5)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.finance.data.Account makeNewAccount(int p0,java.lang.String p1,java.lang.String p2,int p3,int p4)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.finance.data.Account makeNewFinanceAccount(int p0,java.lang.String p1,java.lang.String p2,int p3,int p4)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.finance.data.Account makeNewPhoneAccount(int p0,java.lang.String p1,java.lang.String p2,int p3,int p4)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public java.util.Map mapOfAccountKeys() throws java.rmi.RemoteException;
 public java.util.Map mapOfTariffKeys() throws java.rmi.RemoteException;
}
