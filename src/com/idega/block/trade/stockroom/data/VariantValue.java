package com.idega.block.trade.stockroom.data;

import javax.ejb.*;

public interface VariantValue extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getName();
 public java.lang.String getValue();
 public com.idega.block.trade.stockroom.data.Variant getVariant()throws java.sql.SQLException;
 public int getVariantId();
 public void setValue(java.lang.String p0);
 public void setVariantId(int p0);
}
