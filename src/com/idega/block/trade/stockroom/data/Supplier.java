package com.idega.block.trade.stockroom.data;


public interface Supplier extends com.idega.data.IDOLegacyEntity
{
 public void addCreditCardInformation(com.idega.block.trade.data.CreditCardInformation p0)throws com.idega.data.IDOAddRelationshipException,javax.ejb.EJBException;
 public void addCreditCardInformationPK(java.lang.Object p0)throws com.idega.data.IDOAddRelationshipException;
 public com.idega.core.location.data.Address getAddress()throws java.sql.SQLException;
 public java.util.List getAddresses()throws java.sql.SQLException;
 public java.util.Collection getCreditCardInformation()throws com.idega.data.IDORelationshipException;
 public java.lang.String getDescription();
 public com.idega.core.contact.data.Email getEmail()throws java.sql.SQLException;
 public java.util.List getEmails()throws java.sql.SQLException;
 public java.util.List getFaxPhone()throws java.sql.SQLException;
 public int getGroupId();
 public java.util.List getHomePhone()throws java.sql.SQLException;
 public boolean getIsValid();
 public java.util.List getMobilePhone()throws java.sql.SQLException;
 public java.lang.String getName();
 public java.util.List getPhones()throws java.sql.SQLException;
 public java.util.List getPhones(int p0)throws java.sql.SQLException;
 public java.util.Collection getProductCategories()throws com.idega.data.IDORelationshipException;
 public com.idega.block.trade.stockroom.data.Settings getSettings()throws javax.ejb.FinderException,java.rmi.RemoteException,javax.ejb.CreateException;
 public java.util.List getWorkPhone()throws java.sql.SQLException;
 public void setCreditCardInformation(java.util.Collection p0)throws com.idega.data.IDORemoveRelationshipException,com.idega.data.IDOAddRelationshipException,javax.ejb.EJBException;
 public void setDescription(java.lang.String p0);
 public void setGroupId(int p0);
 public void setIsValid(boolean p0);
 public void setName(java.lang.String p0);
 public int getTPosMerchantId();
}
