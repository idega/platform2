package com.idega.block.trade.stockroom.business;

import java.sql.Timestamp;
import com.idega.block.trade.stockroom.data.*;
import java.sql.SQLException;
import com.idega.core.data.*;
import com.idega.core.user.data.User;
import com.idega.data.GenericEntity;
import com.idega.block.login.business.LoginBusiness;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.core.accesscontrol.business.NotLoggedOnException;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class StockroomBusiness implements SupplyManager {

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
  public void setPrice(int product_id, Timestamp time) {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method setPrice() not yet implemented.");
  }
  public float getPrice(int product_id, Timestamp time) {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method getPrice() not yet implemented.");
  }
  public void createPriceCategory() {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method createPriceCategory() not yet implemented.");
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