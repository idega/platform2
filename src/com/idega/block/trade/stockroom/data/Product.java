package com.idega.block.trade.stockroom.data;

import javax.ejb.*;
import java.util.Collection;
import com.idega.data.IDORelationshipException;

public interface Product extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public java.sql.Timestamp getCreationDate();
 public int getDiscountTypeId();
 public java.sql.Timestamp getEditDate();
 public int getFileId();
 public boolean getIsValid();
 public java.lang.String getName();
 public java.lang.String getNumber();
 public java.lang.String getProdcutDescription();
 public java.lang.String getProductName();
 public int getSupplierId();
 public com.idega.block.text.data.TxText getText()throws java.sql.SQLException;
 public com.idega.block.trade.stockroom.data.Timeframe getTimeframe()throws java.sql.SQLException;
 public com.idega.block.trade.stockroom.data.Timeframe[] getTimeframes()throws java.sql.SQLException;
 public void setCreationDate(java.sql.Timestamp p0);
 public void setCreationDate(com.idega.util.idegaTimestamp p0);
 public void setDefaultValues();
 public void setDiscountTypeId(int p0);
 public void setFileId(java.lang.Integer p0);
 public void setFileId(int p0);
 public void setIsValid(boolean p0);
 public void setNumber(java.lang.String p0);
 public void setSupplierId(java.lang.Integer p0);
 public void setSupplierId(int p0);
 public Collection getProductCategories() throws IDORelationshipException;
}
