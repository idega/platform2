package com.idega.block.trade.stockroom.data;

import java.util.List;

import com.idega.data.IDORemoveRelationshipException;

public interface ProductCategory extends com.idega.block.category.data.ICCategory
{
 public void setDefaultValues();
 public void setCategoryType(String type);
 public void removeProducts(List products) throws IDORemoveRelationshipException;

}
