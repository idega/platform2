package com.idega.block.finance.data;


public interface Tariff extends com.idega.data.IDOEntity
{
 public void setUseIndex(boolean p0) throws java.rmi.RemoteException;
 public void setUseFromDate(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void setIndexType(java.lang.String p0) throws java.rmi.RemoteException;
 public void setTariffGroupId(java.lang.Integer p0) throws java.rmi.RemoteException;
 public int getTariffGroupId() throws java.rmi.RemoteException;
 public void setUseToDate(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public java.lang.String getInfo() throws java.rmi.RemoteException;
 public java.sql.Timestamp getUseFromDate() throws java.rmi.RemoteException;
 public void setInfo(java.lang.String p0) throws java.rmi.RemoteException;
 public int getAccountKeyId() throws java.rmi.RemoteException;
 public java.lang.String getTariffAttribute() throws java.rmi.RemoteException;
 public void setPrice(java.lang.Float p0) throws java.rmi.RemoteException;
 public java.sql.Timestamp getIndexUpdated() throws java.rmi.RemoteException;
 public float getPrice() throws java.rmi.RemoteException;
 public void setTariffAttribute(java.lang.String p0) throws java.rmi.RemoteException;
 public void setAccountKeyId(int p0) throws java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
 public java.lang.String getIndexType() throws java.rmi.RemoteException;
 public boolean getUseIndex() throws java.rmi.RemoteException;
 public void setName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setInUse(boolean p0) throws java.rmi.RemoteException;
 public void setPrice(float p0) throws java.rmi.RemoteException;
 public void setTariffGroupId(int p0) throws java.rmi.RemoteException;
 public java.sql.Timestamp getUseToDate() throws java.rmi.RemoteException;
 public void setIndexUpdated(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public boolean getInUse() throws java.rmi.RemoteException;
 public void setAccountKeyId(java.lang.Integer p0) throws java.rmi.RemoteException;
}
