package com.idega.block.trade.stockroom.data;

import javax.ejb.*;

public interface ProductPrice extends com.idega.data.IDOLegacyEntity
{
 public void delete();
 public int getCurrencyId();
 public int getDiscount();
 public boolean getIsValid();
 public float getPrice();
 public com.idega.block.trade.stockroom.data.PriceCategory getPriceCategory();
 public int getPriceCategoryID();
 public java.lang.Integer getPriceCategoryIDInteger();
 public java.sql.Timestamp getPriceDate();
 public int getPriceType();
 public int getProductId();
 public void invalidate();
 public void setCurrencyId(int p0);
 public void setCurrencyId(java.lang.Integer p0);
 public void setDefaultValues();
 public void setIsValid(boolean p0);
 public void setPrice(float p0);
 public void setPriceCategoryID(int p0);
 public void setPriceDate(java.sql.Timestamp p0);
 public void setPriceType(int p0);
 public void setProductId(int p0);
 public void validate();
}
