package com.idega.block.trade.stockroom.data;



public interface Product extends com.idega.data.IDOEntity, com.idega.data.MetaDataCapable//, BasketItem
{
 public void addArrivalAddress(com.idega.core.location.data.Address p0) throws java.rmi.RemoteException;
 public boolean addCategory(com.idega.block.trade.stockroom.data.ProductCategory p0) throws java.rmi.RemoteException;
 public void addICFile(com.idega.core.file.data.ICFile p0)throws com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 public void addText(com.idega.block.text.data.TxText p0)throws com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 public void addTimeframe(com.idega.block.trade.stockroom.data.Timeframe p0)throws com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 public void addTravelAddress(com.idega.block.trade.stockroom.data.TravelAddress p0) throws java.rmi.RemoteException;
 public void addTravelAddresses(int[] p0)throws com.idega.data.IDOAddRelationshipException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getArrivalAddresses()throws com.idega.data.IDOFinderException, java.rmi.RemoteException;
 public java.sql.Timestamp getCreationDate() throws java.rmi.RemoteException;
 public java.util.List getDepartureAddresses(boolean p0)throws com.idega.data.IDOFinderException, java.rmi.RemoteException;
 public int getDiscountTypeId() throws java.rmi.RemoteException;
 public java.sql.Timestamp getEditDate() throws java.rmi.RemoteException;
 public com.idega.core.file.data.ICFile getFile() throws java.rmi.RemoteException;
 public int getFileId() throws java.rmi.RemoteException;
 public java.util.Collection getICFile()throws com.idega.data.IDORelationshipException, java.rmi.RemoteException;
 public int getID() throws java.rmi.RemoteException;
 public boolean getIsValid() throws java.rmi.RemoteException;
 public java.lang.String getNumber() throws java.rmi.RemoteException;
 public java.util.Collection getProductCategories()throws com.idega.data.IDORelationshipException, java.rmi.RemoteException;
 public java.lang.String getProductDescription(int p0) throws java.rmi.RemoteException;
 public java.lang.String getProductName(int p0) throws java.rmi.RemoteException;
 public java.lang.String getProductTeaser(int p0) throws java.rmi.RemoteException;
 public Supplier getSupplier();
 public int getSupplierId() throws java.rmi.RemoteException;
 public com.idega.block.text.data.TxText getText()throws java.sql.SQLException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Timeframe getTimeframe()throws java.sql.SQLException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Timeframe[] getTimeframes()throws java.sql.SQLException, java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public void invalidate()throws com.idega.data.IDOException, java.rmi.RemoteException;
 public void removeAllFrom(java.lang.Class p0)throws com.idega.data.IDORemoveRelationshipException, java.rmi.RemoteException;
 public void removeCategory(com.idega.block.trade.stockroom.data.ProductCategory p0)throws com.idega.data.IDORemoveRelationshipException, java.rmi.RemoteException;
 public void removeICFile(com.idega.core.file.data.ICFile p0)throws com.idega.data.IDORemoveRelationshipException, java.rmi.RemoteException;
 public void removeTimeframe(com.idega.block.trade.stockroom.data.Timeframe p0)throws com.idega.data.IDORemoveRelationshipException, java.rmi.RemoteException;
 public void removeTravelAddress(com.idega.block.trade.stockroom.data.TravelAddress p0)throws com.idega.data.IDORemoveRelationshipException, java.rmi.RemoteException;
 public void setCreationDate(com.idega.util.IWTimestamp p0) throws java.rmi.RemoteException;
 public void setCreationDate(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void setDiscountTypeId(int p0) throws java.rmi.RemoteException;
 public void setFileId(java.lang.Integer p0) throws java.rmi.RemoteException;
 public void setFileId(int p0) throws java.rmi.RemoteException;
 public void setIsValid(boolean p0) throws java.rmi.RemoteException;
 public void setNumber(java.lang.String p0) throws java.rmi.RemoteException;
 public void setProductCategories(int[] p0)throws com.idega.data.IDORemoveRelationshipException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void setProductDescription(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public void setProductName(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public void setProductTeaser(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public void setSupplierId(int p0) throws java.rmi.RemoteException;
 public void setSupplierId(java.lang.Integer p0) throws java.rmi.RemoteException;
 public void setRefundable(boolean refundable);
 public boolean getRefundable();
}
