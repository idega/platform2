package com.idega.block.trade.stockroom.data;

import javax.ejb.*;

public interface Variant extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getName();
 public com.idega.block.trade.stockroom.data.Product getProduct()throws java.sql.SQLException;
 public int getProductId();
 public void setName(java.lang.String p0);
 public void setProduct(com.idega.block.trade.stockroom.data.Product p0);
 public void setProductId(int p0);
}
