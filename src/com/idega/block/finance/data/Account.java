package com.idega.block.finance.data;


public interface Account extends com.idega.data.IDOEntity,FinanceAccount
{
 public void addAmount(float p0) throws java.rmi.RemoteException;
 public int countByTypeAndCategory(java.lang.String p0,int p1)throws com.idega.data.IDOException, java.rmi.RemoteException;
 public void addAmount(java.lang.Float p0) throws java.rmi.RemoteException;
 public void setBalance(java.lang.Float p0) throws java.rmi.RemoteException;
 public void addKredit(java.lang.Float p0) throws java.rmi.RemoteException;
 public void setExtraInfo(java.lang.String p0) throws java.rmi.RemoteException;
 public void addDebet(float p0) throws java.rmi.RemoteException;
 public void setBalance(float p0) throws java.rmi.RemoteException;
 public java.lang.String getAccountType() throws java.rmi.RemoteException;
 public int getCashierId() throws java.rmi.RemoteException;
 public java.sql.Timestamp getCreationDate() throws java.rmi.RemoteException;
 public void setAccountTypeId(int p0) throws java.rmi.RemoteException;
 public java.lang.String getType() throws java.rmi.RemoteException;
 public int getAccountId() throws java.rmi.RemoteException;
 public void setType(java.lang.String p0) throws java.rmi.RemoteException;
 public int getCategoryId() throws java.rmi.RemoteException;
 public void setValid(boolean p0) throws java.rmi.RemoteException;
 public void setCashierId(java.lang.Integer p0) throws java.rmi.RemoteException;
 public void setLastUpdated(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public boolean getValid() throws java.rmi.RemoteException;
 public java.lang.String getAccountName() throws java.rmi.RemoteException;
 public void addKredit(float p0) throws java.rmi.RemoteException;
 public void addDebet(java.lang.Float p0) throws java.rmi.RemoteException;
 public void setCreationDate(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
 public void setTypePhone() throws java.rmi.RemoteException;
 public void setName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setTypeFinancial() throws java.rmi.RemoteException;
 public java.lang.String getExtraInfo() throws java.rmi.RemoteException;
 public void setUserId(java.lang.Integer p0) throws java.rmi.RemoteException;
 public java.sql.Timestamp getLastUpdated() throws java.rmi.RemoteException;
 public int getUserId() throws java.rmi.RemoteException;
 public float getBalance() throws java.rmi.RemoteException;
 public void setCashierId(int p0) throws java.rmi.RemoteException;
 public void setCategoryId(int p0) throws java.rmi.RemoteException;
 public int getAccountTypeId() throws java.rmi.RemoteException;
 public void setUserId(int p0) throws java.rmi.RemoteException;
}
