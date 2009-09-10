package com.idega.block.finance.business;

import java.rmi.RemoteException;

import com.idega.util.IWTimestamp;


public interface AccountBusiness extends com.idega.business.IBOService
{
 public com.idega.block.finance.data.Account getAccount(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.AccountHome getAccountHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.business.FinanceService getFinanceService()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getAccountEntries(java.lang.Integer p0) throws java.rmi.RemoteException;
 public java.util.Collection getAccountEntries(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2) throws java.rmi.RemoteException;
 public java.util.Collection getAccountEntries(int iAccountId,IWTimestamp from,IWTimestamp to,String status,String roundStatus) throws RemoteException;
 public java.util.Collection getAccountKeys() throws java.rmi.RemoteException;
 public java.util.Collection getUserAccounts(int p0) throws java.rmi.RemoteException;
 public java.util.Collection getAccounts() throws java.rmi.RemoteException;
 public java.util.Collection getUserAccounts(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.util.Collection getKeySortedAccountEntries(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,String roundStatus) throws java.rmi.RemoteException;
 public java.util.Collection getPhoneEntries(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2) throws java.rmi.RemoteException;
 public java.util.Collection getPhoneEntries(int p0,com.idega.util.IWTimestamp p1,java.lang.String p2) throws java.rmi.RemoteException;
 public java.util.Collection getTariffKeys() throws java.rmi.RemoteException;
 public com.idega.block.finance.data.Account createNewAccount(int p0,java.lang.String p1,java.lang.String p2,int p3,java.lang.String p4,int p5)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.finance.data.Account createNewAccount(int p0,java.lang.String p1,java.lang.String p2,int p3,int p4)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.finance.data.Account createNewFinanceAccount(int p0,java.lang.String p1,java.lang.String p2,int p3,int p4)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.finance.data.Account createNewPhoneAccount(int p0,java.lang.String p1,java.lang.String p2,int p3,int p4)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public java.util.Map getAccountKeyMap() throws java.rmi.RemoteException;
 public java.util.Map getTariffKeyMap() throws java.rmi.RemoteException;
}
