package com.idega.block.trade.stockroom.data;

import java.util.List;
import com.idega.block.category.data.ICCategory;
import com.idega.data.IDORemoveRelationshipException;

/**
 * @author gimmi
 */
public interface ProductCategory extends ICCategory{
/**
 * @see com.idega.block.trade.stockroom.data.ProductCategoryBMPBean#setCategoryType
 */
public void setCategoryType(String catType);
/**
 * @see com.idega.block.trade.stockroom.data.ProductCategoryBMPBean#getCategoryType
 */
public String getCategoryType();
/**
 * @see com.idega.block.trade.stockroom.data.ProductCategoryBMPBean#removeProducts
 */
public void removeProducts(List products) throws IDORemoveRelationshipException;

}
