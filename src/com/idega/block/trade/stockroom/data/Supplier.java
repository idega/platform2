package com.idega.block.trade.stockroom.data;


public interface Supplier extends com.idega.data.IDOLegacyEntity
{
 public java.util.List getFaxPhone()throws java.sql.SQLException;
 public java.util.List getEmails()throws java.sql.SQLException;
 public void setTPosMerchantId(int p0);
 public com.idega.core.data.Address getAddress()throws java.sql.SQLException;
 public java.util.List getPhones(int p0)throws java.sql.SQLException;
 public int getGroupId();
 public java.util.List getAddresses()throws java.sql.SQLException;
 public com.idega.core.data.Email getEmail()throws java.sql.SQLException;
 public java.util.List getHomePhone()throws java.sql.SQLException;
 public java.util.List getMobilePhone()throws java.sql.SQLException;
 public boolean getIsValid();
 public void setDescription(java.lang.String p0);
 public java.util.List getPhones()throws java.sql.SQLException;
 public com.idega.block.tpos.data.TPosMerchant getTPosMerchant()throws javax.ejb.FinderException,java.rmi.RemoteException;
 public void setDefaultValues();
 public int getTPosMerchantId();
 public java.lang.String getDescription();
 public java.lang.String getName();
 public com.idega.block.trade.stockroom.data.Settings getSettings()throws javax.ejb.CreateException,java.rmi.RemoteException,javax.ejb.FinderException;
 public void setName(java.lang.String p0);
 public void setGroupId(int p0);
 public java.util.Collection getProductCategories()throws com.idega.data.IDORelationshipException;
 public java.util.List getWorkPhone()throws java.sql.SQLException;
 public void setIsValid(boolean p0);
}
