package com.idega.block.trade.stockroom.business;

import java.sql.Timestamp;
import com.idega.block.trade.stockroom.data.*;
import java.sql.SQLException;
import com.idega.core.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.data.EntityFinder;
import com.idega.core.user.data.User;
import com.idega.data.*;
import com.idega.block.login.business.LoginBusiness;
import com.idega.presentation.IWContext;
import com.idega.core.accesscontrol.business.NotLoggedOnException;
import java.util.List;
import java.util.Iterator;

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

  public void setPrice(int productPriceIdToReplace, int productId, int priceCategoryId, int currencyId, Timestamp time, float price, int priceType, int timeframeId, int addressId) throws SQLException {
    if (productPriceIdToReplace != -1) {
        ProductPrice pPrice = new ProductPrice(productPriceIdToReplace);
          pPrice.invalidate();
          pPrice.update();
    }

    setPrice(productId, priceCategoryId, currencyId, time, price, priceType, timeframeId, addressId);
  }

  public void setPrice(int productId, int priceCategoryId, int currencyId, Timestamp time, float price, int priceType, int timeframeId, int addressId) throws SQLException {
       ProductPrice prPrice = new ProductPrice();
         prPrice.setProductId(productId);
         prPrice.setCurrencyId(currencyId);
         prPrice.setPriceCategoryID(priceCategoryId);
         prPrice.setPriceDate(time);
         prPrice.setPrice(price);
         prPrice.setPriceType(priceType);
       prPrice.insert();
       if (timeframeId != -1) {
        prPrice.addTo(Timeframe.class, timeframeId);
       }
       if (addressId != -1) {
        prPrice.addTo(Address.class, addressId);
       }
  }

  public static float getPrice(int productPriceId, int productId, int priceCategoryId, int currencyId, Timestamp time) throws SQLException  {
    return getPrice(productPriceId, productId, priceCategoryId, currencyId,time, -1, -1);
  }

  public static float getPrice(int productPriceId, int productId, int priceCategoryId, int currencyId, Timestamp time, int timeframeId, int addressId) throws SQLException  {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    /*skila verði ef PRICETYPE_PRICE annars verði með tilliti til afsláttar*/

    try {
        PriceCategory cat = new PriceCategory(priceCategoryId);
        ProductPrice ppr = ((ProductPrice)ProductPrice.getStaticInstance(ProductPrice.class));
        Address addr = ((Address) Address.getStaticInstance(Address.class));
        Timeframe tfr = ((Timeframe) Timeframe.getStaticInstance(Timeframe.class));
        String addrTable = EntityControl.getManyToManyRelationShipTableName(Address.class, ProductPrice.class);
        String tfrTable = EntityControl.getManyToManyRelationShipTableName(Timeframe.class, ProductPrice.class);

        if(cat.getType().equals(PriceCategory.PRICETYPE_PRICE)){
          StringBuffer buffer = new StringBuffer();
            buffer.append("select p.* from "+ppr.getEntityName()+" p");
            if (timeframeId != -1) {
              buffer.append(",  "+tfrTable+" tm");
            }
            if (addressId != -1) {
              buffer.append(", "+addrTable+" am");
            }
            buffer.append(" where ");
            buffer.append("p."+ProductPrice.getColumnNameProductId()+" = "+productId);

            if (timeframeId != -1) {
              buffer.append(" and ");
              buffer.append("tm."+tfr.getIDColumnName()+" = "+timeframeId);
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = tm."+ppr.getIDColumnName());
            }
            if (addressId != -1) {
              buffer.append(" and ");
              buffer.append("am."+addr.getIDColumnName()+" = "+addressId);
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = am."+ppr.getIDColumnName());
            }

            if (productPriceId != -1) {
              buffer.append(" and ");
              buffer.append(ppr.getIDColumnName()+" = "+productPriceId);
            }
            buffer.append(" and ");
            buffer.append("p."+ProductPrice.getColumnNamePriceCategoryId()+" = "+priceCategoryId);
            buffer.append(" and ");
            buffer.append("p."+ProductPrice.getColumnNameCurrencyId()+" = "+currencyId);
            buffer.append(" and ");
            buffer.append("p."+ProductPrice.getColumnNamePriceDate()+" <= '"+time.toString()+"'");
            buffer.append(" and ");
            buffer.append("p."+ProductPrice.getColumnNamePriceType()+" = "+ProductPrice.PRICETYPE_PRICE);
            //buffer.append(" and ");
            //buffer.append("p."+ProductPrice.getColumnNameIsValid()+" = 'Y'");
            buffer.append(" order by p."+ProductPrice.getColumnNamePriceDate());
          List result = EntityFinder.findAll(ppr,buffer.toString());

          if(result != null && result.size() > 0){
            return ((ProductPrice)result.get(0)).getPrice();
          }else{
            System.err.println(buffer.toString());
            throw new ProductPriceException();
          }
        }else if(cat.getType().equals(PriceCategory.PRICETYPE_DISCOUNT)){
          StringBuffer buffer = new StringBuffer();
            buffer.append("select p.* from "+ppr.getEntityName()+" p");
            if (timeframeId != -1) {
              buffer.append(",  "+tfrTable+" tm");
            }
            if (addressId != -1) {
              buffer.append(", "+addrTable+" am");
            }
            buffer.append(" where ");
            buffer.append("p."+ProductPrice.getColumnNameProductId()+" = "+productId);

            if (timeframeId != -1) {
              buffer.append(" and ");
              buffer.append("tm."+tfr.getIDColumnName()+" = "+timeframeId);
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = tm."+ppr.getIDColumnName());
            }
            if (addressId != -1) {
              buffer.append(" and ");
              buffer.append("am."+addr.getIDColumnName()+" = "+addressId);
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = am."+ppr.getIDColumnName());
            }

            buffer.append(" and ");
            buffer.append("p."+ProductPrice.getColumnNamePriceCategoryId()+" = "+priceCategoryId);
            buffer.append(" and ");
            buffer.append("p."+ProductPrice.getColumnNamePriceDate()+" < '"+time.toString()+"'");
            buffer.append(" and ");
            buffer.append("p."+ProductPrice.getColumnNamePriceType()+" = "+ProductPrice.PRICETYPE_DISCOUNT);
            buffer.append(" and ");
            buffer.append("p."+ProductPrice.getColumnNameIsValid()+" = 'Y'");
            buffer.append(" order by p."+ProductPrice.getColumnNamePriceDate());
          List result = EntityFinder.findAll(ppr,buffer.toString());
          float disc = 0;
          if(result != null && result.size() > 0){
            disc = ((ProductPrice)result.get(0)).getPrice();
          }

          float pr = StockroomBusiness.getPrice(-1, productId,cat.getParentId(),currencyId,time, timeframeId, addressId);
          //System.err.println("Parent price : "+pr);
          return pr*((100-disc) /100);
        }else{
          throw new ProductPriceException();
        }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return 0;
    }

  }


  /**
   * returns 0.0 if pricecategory is not of type PriceCategory.PRICETYPE_DISCOUNT
   */
  public float getDiscount(int productId, int priceCategoryId, Timestamp time) throws SQLException{
    PriceCategory cat = new PriceCategory(priceCategoryId);
    if(cat.getType().equals(PriceCategory.PRICETYPE_DISCOUNT)){
      ProductPrice ppr = ((ProductPrice)ProductPrice.getStaticInstance(ProductPrice.class));
      StringBuffer buffer = new StringBuffer();
        buffer.append("select * from "+ppr.getEntityName());
        buffer.append(" where ");
        buffer.append(ProductPrice.getColumnNameProductId()+" = "+productId);
        buffer.append(" and ");
        buffer.append(ProductPrice.getColumnNamePriceCategoryId()+" = "+priceCategoryId);
        buffer.append(" and ");
        buffer.append(ProductPrice.getColumnNamePriceDate()+" < '"+time.toString()+"'");
        buffer.append(" and ");
        buffer.append(ProductPrice.getColumnNameIsValid()+" = 'Y'");
        buffer.append(" order by "+ProductPrice.getColumnNamePriceDate());
      List result = EntityFinder.findAll(ppr,buffer.toString());
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
    List gr = gGroup.getAllGroupsContainingUser(user);
    if(gr != null){
      Iterator iter = gr.iterator();
      while (iter.hasNext()) {
        GenericGroup item = (GenericGroup)iter.next();
        if(item.getGroupType().equals(((SupplierStaffGroup)SupplierStaffGroup.getStaticInstance(SupplierStaffGroup.class)).getGroupTypeValue())){
          GenericEntity[] supp = ((Supplier)Supplier.getStaticInstance(Supplier.class)).findAllByColumn(Supplier.getColumnNameGroupID(),item.getID());
          if(supp != null && supp.length > 0){
            return supp[0].getID();
          }
        }
      }
    }
    throw new RuntimeException("Does not belong to any supplier");
  }

  public static int getUserSupplierId(IWContext iwc) throws RuntimeException, SQLException {
    String supplierLoginAttributeString = "sr_supplier_id";

    Object obj = LoginBusiness.getLoginAttribute(supplierLoginAttributeString,iwc);

    if(obj != null){
      return ((Integer)obj).intValue();
    }else{
      User us = LoginBusiness.getUser(iwc);
      if(us != null){
        int suppId = getUserSupplierId(us);
        LoginBusiness.setLoginAttribute(supplierLoginAttributeString,new Integer(suppId), iwc);
        return suppId;
      } else{
        throw new NotLoggedOnException();
      }
    }
  }

  public static int getUserResellerId(IWContext iwc) throws RuntimeException, SQLException {
    String resellerLoginAttributeString = "sr_reseller_id";

    Object obj = LoginBusiness.getLoginAttribute(resellerLoginAttributeString,iwc);

    if(obj != null){
      return ((Integer)obj).intValue();
    }else{
      User us = LoginBusiness.getUser(iwc);
      if(us != null){
        int resellerId = getUserResellerId(us);
        LoginBusiness.setLoginAttribute(resellerLoginAttributeString,new Integer(resellerId), iwc);
        return resellerId;
      } else{
        throw new NotLoggedOnException();
      }
    }
  }


  public static int getUserResellerId(User user) throws RuntimeException, SQLException{
    com.idega.core.data.GenericGroup gGroup = new GenericGroup();
    List gr = gGroup.getAllGroupsContainingUser(user);
    if(gr != null){
      Iterator iter = gr.iterator();
      while (iter.hasNext()) {
        GenericGroup item = (GenericGroup)iter.next();
        if(item.getGroupType().equals(((ResellerStaffGroup)ResellerStaffGroup.getStaticInstance(ResellerStaffGroup.class)).getGroupTypeValue())){
          GenericEntity[] reseller = ((Reseller)Reseller.getStaticInstance(Reseller.class)).findAllByColumn(Reseller.getColumnNameGroupID(),item.getID());
          if(reseller != null && reseller.length > 0){
            return reseller[0].getID();
          }
        }
      }
    }
    throw new RuntimeException("Does not belong to any reseller");
  }

  public static int updateProduct(int productId, int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    return createProduct(productId,supplierId, fileId, productName, number, productDescription, isValid, addressIds, discountTypeId);
  }

  public static int createProduct(int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    return createProduct(-1,supplierId, fileId, productName, number, productDescription, isValid, addressIds, discountTypeId);
  }

  private static int createProduct(int productId, int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    Product product= null;
    if (productId == -1) {
      product = new Product();
    }else {
      product = new Product(productId);
    }

    product.setSupplierId(supplierId);
    if(fileId != null){
      product.setFileId(fileId);
    }
    product.setIsValid(isValid);
    if (discountTypeId != -1) {
      product.setDiscountTypeId(discountTypeId);
    }
    if (number == null) number = "";
    product.setNumber(number);


    if (productId == -1) {
      product.insert();
    }else {
      product.update();
    }

    ProductBusiness.setProductName(product, productName);
    ProductBusiness.setProductDescription(product, productDescription);

    if(addressIds != null){
      for (int i = 0; i < addressIds.length; i++) {
        try {
          product.addTo(Address.class, addressIds[i]);
        }catch (SQLException sql) {
        }
      }
    }

    ProductBusiness.removeProductApplication(IWContext.getInstance(), supplierId);
    return product.getID();
  }




}