package com.idega.block.finance.business;


public interface AccountBusiness extends com.idega.business.IBOService
{
 public java.util.Map hashOfTariffKeys() throws java.rmi.RemoteException;
 public java.util.List listOfPhoneEntries(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2) throws java.rmi.RemoteException;
 public com.idega.block.finance.data.Account makeNewFinanceAccount(int p0,java.lang.String p1,java.lang.String p2,int p3,int p4)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.Account getAccount(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.Account makeNewAccount(int p0,java.lang.String p1,java.lang.String p2,int p3,int p4)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection listOfAccounts(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public com.idega.block.finance.data.Account makeNewPhoneAccount(int p0,java.lang.String p1,java.lang.String p2,int p3,int p4)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List listOfAccountEntries(int p0) throws java.rmi.RemoteException;
 public java.util.List listOfTariffKeys() throws java.rmi.RemoteException;
 public java.util.List listOfKeySortedEntries(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2) throws java.rmi.RemoteException;
 public java.util.List listOfPhoneEntries(int p0,com.idega.util.IWTimestamp p1,java.lang.String p2) throws java.rmi.RemoteException;
 public java.util.Collection listOfAccounts(int p0) throws java.rmi.RemoteException;
 public java.util.List listOfAccountEntries(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2) throws java.rmi.RemoteException;
 public com.idega.block.finance.data.Account makeNewAccount(int p0,java.lang.String p1,java.lang.String p2,int p3,java.lang.String p4,int p5)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map hashOfAccountKeys() throws java.rmi.RemoteException;
 public java.util.List listOfAccountKeys() throws java.rmi.RemoteException;
 public java.util.Collection listOfAccounts() throws java.rmi.RemoteException;
 public com.idega.block.finance.data.AccountHome getAccountHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
}
