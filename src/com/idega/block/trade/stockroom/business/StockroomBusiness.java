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
        ProductPrice pPrice = ((com.idega.block.trade.stockroom.data.ProductPriceHome)com.idega.data.IDOLookup.getHomeLegacy(ProductPrice.class)).findByPrimaryKeyLegacy(productPriceIdToReplace);
          pPrice.invalidate();
          pPrice.update();
    }

    setPrice(productId, priceCategoryId, currencyId, time, price, priceType, timeframeId, addressId);
  }

  public void setPrice(int productId, int priceCategoryId, int currencyId, Timestamp time, float price, int priceType, int timeframeId, int addressId) throws SQLException {
       ProductPrice prPrice = ((com.idega.block.trade.stockroom.data.ProductPriceHome)com.idega.data.IDOLookup.getHomeLegacy(ProductPrice.class)).createLegacy();
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
        prPrice.addTo(TravelAddress.class, addressId);
       }
  }

  public static float getPrice(int productPriceId, int productId, int priceCategoryId, int currencyId, Timestamp time) throws SQLException  {
    return getPrice(productPriceId, productId, priceCategoryId, currencyId,time, -1, -1);
  }

  public static ProductPrice getPrice(Product product) {
    ProductPrice pPrice = (ProductPrice) com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getStaticInstance(ProductPrice.class);
    StringBuffer buffer = new StringBuffer();
      buffer.append("SELECT * FROM "+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPriceTableName());
      buffer.append(" WHERE ");
      buffer.append(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameProductId() +" = "+product.getID());
      buffer.append(" AND ");
      buffer.append(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceCategoryId() +" is null");
      buffer.append(" ORDER BY "+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate()+" DESC");

    try {
//      EntityFinder.debug = true;
      List prices = EntityFinder.getInstance().findAll(ProductPrice.class, buffer.toString());
//      List prices = EntityFinder.findAll(((com.idega.block.trade.stockroom.data.ProductPriceHome)com.idega.data.IDOLookup.getHomeLegacy(ProductPrice.class)).createLegacy(), buffer.toString());
//      EntityFinder.debug = false;
      if (prices != null)
      if (prices.size() > 0) {
        return ((ProductPrice)prices.get(0));
      }
    }catch (IDOFinderException ido) {
      ido.printStackTrace(System.err);
    }

    return null;
  }

  public static float getPrice(int productPriceId, int productId, int priceCategoryId, int currencyId, Timestamp time, int timeframeId, int addressId) throws SQLException  {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    /*skila verði ef PRICETYPE_PRICE annars verði með tilliti til afsláttar*/

    try {
        PriceCategory cat = ((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).findByPrimaryKeyLegacy(priceCategoryId);
        ProductPrice ppr = ((ProductPrice)com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getStaticInstance(ProductPrice.class));
        TravelAddress taddr = ((TravelAddress) com.idega.block.trade.stockroom.data.TravelAddressBMPBean.getStaticInstance(TravelAddress.class));
        Timeframe tfr = ((Timeframe) com.idega.block.trade.stockroom.data.TimeframeBMPBean.getStaticInstance(Timeframe.class));
        String addrTable = EntityControl.getManyToManyRelationShipTableName(TravelAddress.class, ProductPrice.class);
        String tfrTable = EntityControl.getManyToManyRelationShipTableName(Timeframe.class, ProductPrice.class);

        if(cat.getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE)){
          StringBuffer buffer = new StringBuffer();
            buffer.append("select p.* from "+ppr.getEntityName()+" p");
            if (timeframeId != -1) {
              buffer.append(",  "+tfrTable+" tm");
            }
            if (addressId != -1) {
              buffer.append(", "+addrTable+" am");
            }
            buffer.append(" where ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameProductId()+" = "+productId);

            if (timeframeId != -1) {
              buffer.append(" and ");
              buffer.append("tm."+tfr.getIDColumnName()+" = "+timeframeId);
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = tm."+ppr.getIDColumnName());
            }
            if (addressId != -1) {
              buffer.append(" and ");
              buffer.append("am."+taddr.getIDColumnName()+" = "+addressId);
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = am."+ppr.getIDColumnName());
            }

            if (productPriceId != -1) {
              buffer.append(" and ");
              buffer.append(ppr.getIDColumnName()+" = "+productPriceId);
            }
            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceCategoryId()+" = "+priceCategoryId);
            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameCurrencyId()+" = "+currencyId);
            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate()+" <= '"+time.toString()+"'");
            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceType()+" = "+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_PRICE);
            //buffer.append(" and ");
            //buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameIsValid()+" = 'Y'");
            buffer.append(" order by p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate()+ " desc");
          List result = EntityFinder.findAll(ppr,buffer.toString());

          if(result != null && result.size() > 0){
            return ((ProductPrice)result.get(0)).getPrice();
          }else{
            System.err.println(buffer.toString());
            throw new ProductPriceException();
          }
        }else if(cat.getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT)){
          StringBuffer buffer = new StringBuffer();
            buffer.append("select p.* from "+ppr.getEntityName()+" p");
            if (timeframeId != -1) {
              buffer.append(",  "+tfrTable+" tm");
            }
            if (addressId != -1) {
              buffer.append(", "+addrTable+" am");
            }
            buffer.append(" where ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameProductId()+" = "+productId);

            if (timeframeId != -1) {
              buffer.append(" and ");
              buffer.append("tm."+tfr.getIDColumnName()+" = "+timeframeId);
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = tm."+ppr.getIDColumnName());
            }
            if (addressId != -1) {
              buffer.append(" and ");
              buffer.append("am."+taddr.getIDColumnName()+" = "+addressId);
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = am."+ppr.getIDColumnName());
            }

            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceCategoryId()+" = "+priceCategoryId);
            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate()+" < '"+time.toString()+"'");
            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceType()+" = "+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_DISCOUNT);
            //buffer.append(" and ");
            //buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameIsValid()+" = 'Y'");
            buffer.append(" order by p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate()+ " desc");
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
   * returns 0.0 if pricecategory is not of type com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT
   */
  public float getDiscount(int productId, int priceCategoryId, Timestamp time) throws SQLException{
    PriceCategory cat = ((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).findByPrimaryKeyLegacy(priceCategoryId);
    if(cat.getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT)){
      ProductPrice ppr = ((ProductPrice)com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getStaticInstance(ProductPrice.class));
      StringBuffer buffer = new StringBuffer();
        buffer.append("select * from "+ppr.getEntityName());
        buffer.append(" where ");
        buffer.append(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameProductId()+" = "+productId);
        buffer.append(" and ");
        buffer.append(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceCategoryId()+" = "+priceCategoryId);
        buffer.append(" and ");
        buffer.append(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate()+" < '"+time.toString()+"'");
        buffer.append(" and ");
        buffer.append(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameIsValid()+" = 'Y'");
        buffer.append(" order by "+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate());
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
    PriceCategory cat = ((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).createLegacy();
    cat.setType(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE);

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
    PriceCategory cat = ((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).createLegacy();
    cat.setParentId(parentId);
    cat.setType(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT);

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
    com.idega.core.data.GenericGroup gGroup = ((com.idega.core.data.GenericGroupHome)com.idega.data.IDOLookup.getHomeLegacy(GenericGroup.class)).createLegacy();
    List gr = gGroup.getAllGroupsContainingUser(user);
    if(gr != null){
      Iterator iter = gr.iterator();
      while (iter.hasNext()) {
        GenericGroup item = (GenericGroup)iter.next();
        if(item.getGroupType().equals(((SupplierStaffGroup)com.idega.block.trade.stockroom.data.SupplierStaffGroupBMPBean.getStaticInstance(SupplierStaffGroup.class)).getGroupTypeValue())){
          IDOLegacyEntity[] supp = ((Supplier)com.idega.block.trade.stockroom.data.SupplierBMPBean.getStaticInstance(Supplier.class)).findAllByColumn(com.idega.block.trade.stockroom.data.SupplierBMPBean.getColumnNameGroupID(),item.getID());
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
    com.idega.core.data.GenericGroup gGroup = ((com.idega.core.data.GenericGroupHome)com.idega.data.IDOLookup.getHomeLegacy(GenericGroup.class)).createLegacy();
    List gr = gGroup.getAllGroupsContainingUser(user);
    if(gr != null){
      Iterator iter = gr.iterator();
      while (iter.hasNext()) {
        GenericGroup item = (GenericGroup)iter.next();
        if(item.getGroupType().equals(((ResellerStaffGroup)com.idega.block.trade.stockroom.data.ResellerStaffGroupBMPBean.getStaticInstance(ResellerStaffGroup.class)).getGroupTypeValue())){
          IDOLegacyEntity[] reseller = ((Reseller)com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class)).findAllByColumn(com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameGroupID(),item.getID());
          if(reseller != null && reseller.length > 0){
            return reseller[0].getID();
          }
        }
      }
    }
    throw new RuntimeException("Does not belong to any reseller");
  }

  public static int updateProduct(int productId, int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    return ProductBusiness.createProduct(productId,supplierId, fileId, productName, number, productDescription, isValid, addressIds, discountTypeId);
  }

  public static int createProduct(int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    return ProductBusiness.createProduct(-1,supplierId, fileId, productName, number, productDescription, isValid, addressIds, discountTypeId);
  }





}
