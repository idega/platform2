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

public class ProductPrice extends GenericEntity{

  public static final int PRICETYPE_PRICE = 0;
  public static final int PRICETYPE_DISCOUNT = 1;

  public ProductPrice(){
          super();
  }
  public ProductPrice(int id)throws SQLException{
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

    this.addManyToManyRelationShip(Timeframe.class,getProductPriceTableName()+"_TIMEFRAME");
    this.addManyToManyRelationShip(Timeframe.class,getProductPriceTableName()+"_ADDRESS");
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
/*    float returner = 0;
    try {
      if (this.getPriceType() == PRICETYPE_PRICE) {
        returner = getFloatColumnValue(getColumnNamePrice());
      }else if (this.getPriceType() == PRICETYPE_DISCOUNT) {
        PriceCategory pCat = this.getPriceCategory();
        int parentId = pCat.getParentId();
        ProductPrice[] parent = (ProductPrice[]) (new ProductPrice()).findAllByColumn(getColumnNamePriceCategoryId(), parentId);
        if (parent.length > 0) {
          returner = parent[0].getPrice() * ((100 - getFloatColumnValue(getColumnNamePrice())) / 100);
        }else {
          System.err.println("Cannot find Parent");
        }
      }
    }catch (SQLException sql) {
        sql.printStackTrace(System.err);
    }
    return returner;
*/
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
  public static ProductPrice[] getProductPrices(int productId, int timeframeId, boolean netBookingOnly) {
      ProductPrice[] prices = {};
      try {
        ProductPrice price = (ProductPrice) ProductPrice.getStaticInstance(ProductPrice.class);
        PriceCategory category = (PriceCategory) PriceCategory.getStaticInstance(PriceCategory.class);
        Timeframe timeframe = (Timeframe) Timeframe.getStaticInstance(Timeframe.class);
        Product product = (Product) Product.getStaticInstance(Product.class);

        String mTable = EntityControl.getManyToManyRelationShipTableName(ProductPrice.class, Timeframe.class);
        String pTable = price.getProductPriceTableName();
        String cTable = category.getEntityName();

        StringBuffer SQLQuery = new StringBuffer();
          SQLQuery.append("SELECT "+pTable+".* FROM "+pTable+", "+cTable);
          if (timeframeId != -1) {
            SQLQuery.append(" , "+mTable);
          }
          SQLQuery.append(" WHERE ");
          if (timeframeId != -1) {
            SQLQuery.append(mTable+"."+timeframe.getIDColumnName()+" = "+timeframeId);
            SQLQuery.append(" AND ");
            SQLQuery.append(mTable+"."+price.getIDColumnName()+" = "+pTable+"."+price.getIDColumnName());
            SQLQuery.append(" AND ");
          }
          SQLQuery.append(pTable+"."+ProductPrice.getColumnNamePriceCategoryId() + " = "+cTable+"."+category.getIDColumnName());
          SQLQuery.append(" AND ");
          SQLQuery.append(pTable+"."+ProductPrice.getColumnNameProductId() +" = " + productId);
          SQLQuery.append(" AND ");
          SQLQuery.append(pTable+"."+ProductPrice.getColumnNameIsValid() +"='Y'");
          if (netBookingOnly) {
            SQLQuery.append(" AND ");
            SQLQuery.append(cTable+"."+PriceCategory.getColumnNameNetbookingCategory()+" = 'Y'");
          }
          SQLQuery.append(" ORDER BY "+pTable+"."+price.getColumnNamePriceType()+","+cTable+"."+category.getColumnNameName());

        prices = (ProductPrice[]) (ProductPrice.getStaticInstance(ProductPrice.class)).findAll(SQLQuery.toString());
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
      return prices;
  }


  public static String getProductPriceTableName(){return "SR_PRODUCT_PRICE";}
  public static String getColumnNameProductId(){return "SR_PRODUCT_ID";}
  public static String getColumnNamePriceCategoryId() {return "SR_PRICE_CATEGORY_ID";}
  public static String getColumnNamePrice() {return "PRICE";}
  public static String getColumnNamePriceDate() {return "PRICE_DATE"; }
  public static String getColumnNameCurrencyId() {return "TR_CURRENCY_ID"; }
  public static String getColumnNamePriceType() {return "PRICE_TYPE"; }
  public static String getColumnNameIsValid() {return "IS_VALID";}






}
