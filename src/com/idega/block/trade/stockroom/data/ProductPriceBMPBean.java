package com.idega.block.trade.stockroom.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.core.data.*;
import com.idega.block.trade.data.Currency;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class ProductPriceBMPBean extends com.idega.data.GenericEntity implements com.idega.block.trade.stockroom.data.ProductPrice {

  public static final int PRICETYPE_PRICE = 0;
  public static final int PRICETYPE_DISCOUNT = 1;

  public ProductPriceBMPBean(){
          super();
  }
  public ProductPriceBMPBean(int id)throws SQLException{
          super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameProductId(), "Vara" ,true, true, Integer.class, "many_to_one", Product.class);
    addAttribute(getColumnNamePriceCategoryId(), "Verðflokkur" ,true, true, Integer.class, "many_to_one", PriceCategory.class);
    addAttribute(getColumnNameCurrencyId(),"Gjaldmiðill",true,true,Integer.class,"many_to_one", Currency.class);
    addAttribute(getColumnNamePrice(), "Verð", true, true, Float.class);
    addAttribute(getColumnNamePriceDate(), "Dagsetning verðs", true, true, Timestamp.class);
    addAttribute(getColumnNamePriceType(),"Gerð",true,true,Integer.class);
    addAttribute(getColumnNameIsValid(), "virkt", true, true, Boolean.class);
    /** added 22.04.2002 by gimmi */
    addAttribute(getColumnNameMaxUsage(), "hámarks fjoldi", true, true, Integer.class);

    this.addManyToManyRelationShip(Timeframe.class,getProductPriceTableName()+"_TIMEFRAME");
    this.addManyToManyRelationShip(Address.class,getProductPriceTableName()+"_ADDRESS");
    this.addManyToManyRelationShip(TravelAddress.class);
  }


  public void delete() {
    this.invalidate();
  }

  public void invalidate() {
    this.setIsValid(false);
  }

  public void validate() {
    this.setIsValid(true);
  }

  public String getEntityName(){
    return getProductPriceTableName();
  }

  public void setDefaultValues() {
    this.setIsValid(true);
    this.setMaxUsage(-1);
  }

  public int getProductId() {
    return getIntColumnValue(getColumnNameProductId());
  }

  public void setProductId(int id){
    setColumn(getColumnNameProductId(),id);
  }


  public PriceCategory getPriceCategory() {
    return (PriceCategory) getColumnValue(getColumnNamePriceCategoryId());
  }

  public int getPriceCategoryID() {
    return getIntColumnValue(getColumnNamePriceCategoryId());
  }

  public Integer getPriceCategoryIDInteger() {
    return getIntegerColumnValue(getColumnNamePriceCategoryId());
  }

  public void setPriceCategoryID(int id) {
    setColumn(getColumnNamePriceCategoryId(), id);
  }

  public int getCurrencyId(){
    return getIntColumnValue(getColumnNameCurrencyId());
  }

  public void setCurrencyId(int id){
    setColumn(getColumnNameCurrencyId(), id);
  }

  public void setCurrencyId(Integer id){
    setColumn(getColumnNameCurrencyId(), id);
  }

  public float getPrice() {
    return getFloatColumnValue(getColumnNamePrice());
  }

  public int getDiscount() {
    int returner = 0;
    if (this.getPriceType() == PRICETYPE_DISCOUNT) {
      returner = (int) getFloatColumnValue(getColumnNamePrice());
    }
    return returner;
  }

  public void setPrice(float price) {
    setColumn(getColumnNamePrice(), price);
  }

  public Timestamp getPriceDate() {
    return (Timestamp) getColumnValue(getColumnNamePriceDate());
  }

  public void setPriceDate(Timestamp timestamp) {
    setColumn(getColumnNamePriceDate(), timestamp);
  }

  public int getPriceType(){
    return getIntColumnValue(getColumnNamePriceType());
  }

  public void setPriceType(int type){
    setColumn(getColumnNamePriceType(), type);
  }

  public void setIsValid(boolean isValid) {
    setColumn(getColumnNameIsValid(), isValid);
  }

  public boolean getIsValid() {
    return getBooleanColumnValue(getColumnNameIsValid());
  }

  public static void clearPrices(int productId) throws SQLException {
    ProductPrice[] prices = getProductPrices(productId, -1, false);
    for (int i = 0; i < prices.length; i++) {
      prices[i].invalidate();
      prices[i].update();
    }
  }

  public static ProductPrice[] getProductPrices(int productId, boolean netBookingOnly) {
    return getProductPrices(productId, -1, netBookingOnly);
  }
  /**
   * @deprecated
   */
  public static ProductPrice[] getProductPrices(int productId, int timeframeId, boolean netBookingOnly) {
    return getProductPrices(productId, timeframeId, -1, netBookingOnly);
  }
  public static ProductPrice[] getProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly) {
      ProductPrice[] prices = {};
      try {
        ProductPrice price = (ProductPrice) com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getStaticInstance(ProductPrice.class);
        PriceCategory category = (PriceCategory) com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getStaticInstance(PriceCategory.class);
        Timeframe timeframe = (Timeframe) com.idega.block.trade.stockroom.data.TimeframeBMPBean.getStaticInstance(Timeframe.class);
        TravelAddress tAddress = (TravelAddress) com.idega.block.trade.stockroom.data.TravelAddressBMPBean.getStaticInstance(TravelAddress.class);
        Product product = (Product) com.idega.block.trade.stockroom.data.ProductBMPBean.getStaticInstance(Product.class);

        String ptmTable = EntityControl.getManyToManyRelationShipTableName(ProductPrice.class, Timeframe.class);
        String pamTable = EntityControl.getManyToManyRelationShipTableName(ProductPrice.class, TravelAddress.class);
        String pTable = getProductPriceTableName();
        String cTable = category.getEntityName();

        StringBuffer SQLQuery = new StringBuffer();
          SQLQuery.append("SELECT "+pTable+".* FROM "+pTable+", "+cTable);
          if (timeframeId != -1) {
            SQLQuery.append(" , "+ptmTable);
          }
          if (addressId != -1) {
            SQLQuery.append(" , "+pamTable);
          }
          SQLQuery.append(" WHERE ");
          if (timeframeId != -1) {
            SQLQuery.append(ptmTable+"."+timeframe.getIDColumnName()+" = "+timeframeId);
            SQLQuery.append(" AND ");
            SQLQuery.append(ptmTable+"."+price.getIDColumnName()+" = "+pTable+"."+price.getIDColumnName());
            SQLQuery.append(" AND ");
          }
          if (addressId != -1) {
            SQLQuery.append(pamTable+"."+tAddress.getIDColumnName()+" = "+addressId);
            SQLQuery.append(" AND ");
            SQLQuery.append(pamTable+"."+price.getIDColumnName()+" = "+pTable+"."+price.getIDColumnName());
            SQLQuery.append(" AND ");
          }
          SQLQuery.append(pTable+"."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceCategoryId() + " = "+cTable+"."+category.getIDColumnName());
          SQLQuery.append(" AND ");
          SQLQuery.append(pTable+"."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameProductId() +" = " + productId);
          SQLQuery.append(" AND ");
          SQLQuery.append(pTable+"."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameIsValid() +"='Y'");
          if (netBookingOnly) {
            SQLQuery.append(" AND ");
            SQLQuery.append(cTable+"."+com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getColumnNameNetbookingCategory()+" = 'Y'");
          }
          SQLQuery.append(" ORDER BY "+pTable+"."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceType()+","+cTable+"."+com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getColumnNameName());

//        System.err.println(SQLQuery.toString());
        prices = (ProductPrice[]) (com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getStaticInstance(ProductPrice.class)).findAll(SQLQuery.toString());
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
      return prices;
  }

  /** added 22.04.2002 by gimmi */
  public int getMaxUsage() {
    return getIntColumnValue(getColumnNameMaxUsage());
  }

  public void setMaxUsage(int maxUsage) {
    setColumn(getColumnNameMaxUsage(), maxUsage);
  }



  public static String getProductPriceTableName(){return "SR_PRODUCT_PRICE";}
  public static String getColumnNameProductId(){return "SR_PRODUCT_ID";}
  public static String getColumnNamePriceCategoryId() {return "SR_PRICE_CATEGORY_ID";}
  public static String getColumnNamePrice() {return "PRICE";}
  public static String getColumnNamePriceDate() {return "PRICE_DATE"; }
  public static String getColumnNameCurrencyId() {return "TR_CURRENCY_ID"; }
  public static String getColumnNamePriceType() {return "PRICE_TYPE"; }
  public static String getColumnNameIsValid() {return "IS_VALID";}
  /** added 22.04.2002 by gimmi */
  public static String getColumnNameMaxUsage() {return "MAX_USAGE";}




}
