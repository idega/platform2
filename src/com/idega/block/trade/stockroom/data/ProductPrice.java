package com.idega.block.trade.stockroom.data;


public interface ProductPrice extends com.idega.data.IDOLegacyEntity
{
 public int getCurrencyId();
 public void invalidate();
 public int getDiscount();
 public void setPriceCategoryID(int p0);
 public java.sql.Timestamp getPriceDate();
 public int getPriceType();
 public java.lang.Integer getPriceCategoryIDInteger();
 public void setProductId(int p0);
 public void validate();
 public void setCurrencyId(int p0);
 public boolean getIsValid();
 public void setPrice(float p0);
 public java.util.Collection getTravelAddresses()throws com.idega.data.IDORelationshipException;
 public float getPrice();
 public void setCurrencyId(java.lang.Integer p0);
 public int getMaxUsage();
 public void setPriceDate(java.sql.Timestamp p0);
 public java.util.Collection getTimeframes()throws com.idega.data.IDORelationshipException;
 public void setPriceType(int p0);
 public void initializeAttributes();
 public void setMaxUsage(int p0);
 public int getProductId();
 public int getPriceCategoryID();
 public com.idega.block.trade.stockroom.data.PriceCategory getPriceCategory();
 public void setIsValid(boolean p0);
}
