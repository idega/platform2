package com.idega.block.trade.stockroom.business;

import java.sql.Timestamp;
import com.idega.block.trade.stockroom.data.*;
import java.sql.SQLException;
import com.idega.core.data.*;
import com.idega.data.EntityFinder;
import com.idega.core.user.data.User;
import com.idega.data.GenericEntity;
import com.idega.block.login.business.LoginBusiness;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.core.accesscontrol.business.NotLoggedOnException;
import java.util.List;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class StockroomBusiness /* implements SupplyManager */ {

  public StockroomBusiness() {
  }
  public void addSupplies(int product_id, float amount) {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method addSupplies() not yet implemented.");
  }
  public void depleteSupplies(int product_id, float amount) {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method depleteSupplies() not yet implemented.");
  }
  public void setSupplyStatus(int product_id, float status) {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method addSupplies() not yet implemented.");
  }
  public float getSupplyStatus(int product_id)  throws SQLException {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method getSupplyStatus() not yet implemented.");
  }
  public float getSupplyStatus(int product_id, Timestamp time) {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method getSupplyStatus() not yet implemented.");
  }



  public void setPrice(int productId, int priceCategoryId, int currencyId, Timestamp time, float price) throws SQLException {
   ProductPrice prPrice = new ProductPrice();

   prPrice.setProductId(productId);
   prPrice.setCurrencyId(currencyId);
   prPrice.setPriceCategoryID(priceCategoryId);
   prPrice.setPriceDate(time);
   prPrice.setPrice(price);

   prPrice.insert();
  }

  public float getPrice(int productId, int priceCategoryId, int currencyId, Timestamp time) throws SQLException  {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    /*skila verði ef PRICETYPE_PRICE annars verði með tilliti til afsláttar*/
    PriceCategory cat = new PriceCategory(priceCategoryId);


    if(cat.getType().equals(PriceCategory.PRICETYPE_PRICE)){
      ProductPrice ppr = ((ProductPrice)ProductPrice.getStaticInstance(ProductPrice.class));
      List result = EntityFinder.findAll(ppr,"select * from "+ppr.getEntityName()+" where "+ProductPrice.getColumnNameProductId()+" = "+productId+" and "+ProductPrice.getColumnNamePriceCategoryId()+" = "+priceCategoryId+" and "+ProductPrice.getColumnNameCurrencyId()+" = "+currencyId+" and "+ProductPrice.getColumnNamePriceDate()+" < '"+time.toString()+"' order by "+ProductPrice.getColumnNamePriceDate());
      if(result != null && result.size() > 0){
        return ((ProductPrice)result.get(0)).getPrice();
      }else{
        throw new ProductPriceException();
      }
    }else if(cat.getType().equals(PriceCategory.PRICETYPE_DISCOUNT)){
      ProductPrice ppr = ((ProductPrice)ProductPrice.getStaticInstance(ProductPrice.class));
      List result = EntityFinder.findAll(ppr,"select * from "+ppr.getEntityName()+" where "+ProductPrice.getColumnNameProductId()+" = "+productId+" and "+ProductPrice.getColumnNamePriceCategoryId()+" = "+priceCategoryId+" and "+ProductPrice.getColumnNamePriceDate()+" < '"+time.toString()+"' order by "+ProductPrice.getColumnNamePriceDate());
      float disc = 0;
      if(result != null && result.size() > 0){
        disc = ((ProductPrice)result.get(0)).getPrice();
      }
      float pr = this.getPrice(productId,cat.getParentId(),currencyId,time);
      return pr - pr*disc;
    }else{
      throw new ProductPriceException();
    }
  }

  /**
   * returns 0.0 if pricecategory is not of type PriceCategory.PRICETYPE_DISCOUNT
   */
  public float getDiscount(int productId, int priceCategoryId, Timestamp time) throws SQLException{
    PriceCategory cat = new PriceCategory(priceCategoryId);
    if(cat.getType().equals(PriceCategory.PRICETYPE_DISCOUNT)){
      ProductPrice ppr = ((ProductPrice)ProductPrice.getStaticInstance(ProductPrice.class));
      List result = EntityFinder.findAll(ppr,"select * from "+ppr.getEntityName()+" where "+ProductPrice.getColumnNameProductId()+" = "+productId+" and "+ProductPrice.getColumnNamePriceCategoryId()+" = "+priceCategoryId+" and "+ProductPrice.getColumnNamePriceDate()+" < '"+time.toString()+"' order by "+ProductPrice.getColumnNamePriceDate());
      if(result != null && result.size() > 0){
        return ((ProductPrice)result.get(0)).getPrice();
      }else{
        return 0;
      }
    }else{
      throw new ProductPriceException();
    }
  }


  public int createPriceCategory(int supplierId, String name, String description, String extraInfo )throws SQLException {
    PriceCategory cat = new PriceCategory();
    cat.setType(PriceCategory.PRICETYPE_PRICE);

    cat.setName(name);

    if(description != null){
      cat.setDescription(description);
    }

    if(extraInfo != null){
      cat.setExtraInfo(extraInfo);
    }

    cat.insert();

    return cat.getID();
  }


  public void createPriceDiscountCategory(int parentId, int supplierId, String name, String description, String extraInfo) throws SQLException{
    PriceCategory cat = new PriceCategory();
    cat.setParentId(parentId);
    cat.setType(PriceCategory.PRICETYPE_DISCOUNT);

    cat.setName(name);

    if(description != null){
      cat.setDescription(description);
    }

    if(extraInfo != null){
      cat.setExtraInfo(extraInfo);
    }

    cat.insert();

  }


  public static int getUserSupplierId(User user) throws RuntimeException, SQLException{
    com.idega.core.data.GenericGroup gGroup = new GenericGroup();
    GenericGroup[] gr = gGroup.getAllGroupsContainingUser(user);
    if(gr != null){
      for (int i = 0; i < gr.length; i++) {
        if(gr[i].getGroupType().equals(((SupplierStaffGroup)SupplierStaffGroup.getStaticInstance(SupplierStaffGroup.class)).getGroupTypeValue())){
          GenericEntity[] supp = ((Supplier)Supplier.getStaticInstance(Supplier.class)).findAllByColumn(Supplier.getColumnNameGroupID(),gr[i].getID());
          if(supp != null && supp.length > 0){
            return supp[0].getID();
          }
        }
      }
    }
    throw new RuntimeException("Does not belong to any supplier");
  }

  public static int getUserSupplierId(ModuleInfo modinfo) throws RuntimeException, SQLException {
    String supplierLoginAttributeString = "sr_supplier_id";

    Object obj = LoginBusiness.getLoginAttribute(supplierLoginAttributeString,modinfo);

    if(obj != null){
      return ((Integer)obj).intValue();
    }else{
      User us = LoginBusiness.getUser(modinfo);
      if(us != null){
        int suppId = getUserSupplierId(us);
        LoginBusiness.setLoginAttribute(supplierLoginAttributeString,new Integer(suppId), modinfo);
        return suppId;
      } else{
        throw new NotLoggedOnException();
      }
    }
  }


  public int createProduct(int supplierId, Integer fileId, String productName, String ProductDescription, boolean isValid) throws Exception{
    Product product = new Product();

    product.setSupplierId(supplierId);
    if(fileId != null){
      product.setFileId(fileId);
    }
    product.setProductName(productName);
    product.setProdcutDescription(ProductDescription);
    product.setIsValid(isValid);

    product.insert();

    return product.getID();

  }




}