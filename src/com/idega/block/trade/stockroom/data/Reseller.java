package com.idega.block.trade.stockroom.data;

import javax.ejb.*;

public interface Reseller extends com.idega.data.IDOLegacyEntity, com.idega.data.TreeableEntity
{
 public void delete()throws java.sql.SQLException;
 public com.idega.core.data.Address getAddress()throws java.sql.SQLException;
 public java.util.List getAddresses()throws java.sql.SQLException;
 public java.lang.String getDescription();
 public com.idega.core.data.Email getEmail()throws java.sql.SQLException;
 public java.util.List getEmails()throws java.sql.SQLException;
 public java.util.List getFaxPhone()throws java.sql.SQLException;
 public int getGroupId();
 public java.util.List getHomePhone()throws java.sql.SQLException;
 public boolean getIsValid();
 public java.lang.String getName();
 public com.idega.block.trade.stockroom.data.Reseller getParent();
 public java.util.List getPhones()throws java.sql.SQLException;
 public java.util.List getPhones(int p0)throws java.sql.SQLException;
 public java.lang.String getReferenceNumber();
 public com.idega.block.trade.stockroom.data.Settings getSettings()throws javax.ejb.CreateException,java.rmi.RemoteException,javax.ejb.FinderException;
 public com.idega.block.tpos.data.TPosMerchant getTPosMerchant()throws javax.ejb.FinderException,java.rmi.RemoteException;
 public int getTPosMerchantId();
 public void setDefaultValues();
 public void setDescription(java.lang.String p0);
 public void setGroupId(int p0);
 public void setIsValid(boolean p0);
 public void setName(java.lang.String p0);
 public void setReferenceNumber(java.lang.String p0);
 public void setTPosMerchantId(int p0);
}
