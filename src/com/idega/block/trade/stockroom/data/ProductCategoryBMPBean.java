package com.idega.block.trade.stockroom.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.data.IDORemoveRelationshipException;
/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author       <a href="mailto:gimmi@idega.is">Grimur Jónsson</a>
 * @version 1.0
 */

public class ProductCategoryBMPBean extends com.idega.block.category.data.ICCategoryBMPBean implements com.idega.block.trade.stockroom.data.ProductCategory {

  public static final String CATEGORY_TYPE_PRODUCT = "sr_prod_cat_product";

  public ProductCategoryBMPBean(){
    super();
  }

  public ProductCategoryBMPBean(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    super.initializeAttributes();
  }

  public void setDefaultValues() {
    super.setDefaultValues();
    this.setType(CATEGORY_TYPE_PRODUCT);
  }

  public void setCategoryType(String catType) {
    setType(catType);
//    this.setColumn(com.idega.core.data.ICCategoryBMPBean.getColumnType(), catType);
  }

  public String getCategoryType() {
    return getStringColumnValue(com.idega.block.category.data.ICCategoryBMPBean.getColumnType());
  }

  public void removeProducts(List products) throws IDORemoveRelationshipException{
    if (products != null) {
      Iterator iter = products.iterator();
      Product prod;
      while (iter.hasNext()) {
        prod = (Product) iter.next();
        this.idoRemoveFrom(prod);
      }
    }
//    this.idoRemoveFrom();
  }

  public ProductCategory ejbHomeGetProductCategory(String type) throws FinderException, RemoteException {
    Collection coll = this.idoFindAllIDsByColumnBySQL(com.idega.block.category.data.ICCategoryBMPBean.getColumnType(), type);
    ProductCategoryHome pcHome = (ProductCategoryHome) IDOLookup.getHomeLegacy(ProductCategory.class);
    if (coll.size() == 1) {

      Iterator iter = coll.iterator();
      try {
        return pcHome.findByPrimaryKeyLegacy( ( (Integer) iter.next() ).intValue());
      }catch (SQLException sql) {
        throw new FinderException(sql.getMessage());
      }

    }else if (coll.size() == 0) {

      ProductCategory pCat = pcHome.createLegacy();
        pCat.setCategoryType(type);
        pCat.setName(type);
        pCat.store();
      return pCat;

    }else {//(coll.size() > 1) {

      throw new FinderException("Found more than one ProductCategory, should only be one.");

    }
  }


} // Class ProductCategory
