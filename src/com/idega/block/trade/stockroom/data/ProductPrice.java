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

  public ProductPrice(){
          super();
  }
  public ProductPrice(int id)throws SQLException{
          super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getProductIdColumnName(), "Vara" ,true, true, Integer.class, "many_to_one", Product.class);
    addAttribute(getPriceCategoryIdColumnName(), "Verðflokkur" ,true, true, Integer.class, "many_to_one", ProductCategory.class);
    addAttribute(getColumnNameCurrencyId(),"Gjaldmiðill",true,true,Integer.class,"many_to_one", Currency.class);
    addAttribute(getPriceColumnName(), "Verð", true, true, Double.class);
    addAttribute(getPriceDateColumnName(), "Dagsetning verðs", true, true, java.sql.Date.class);
    addAttribute(getColumnNamePriceType(),"Gerð",true,true,String.class,255);
  }

  public String getEntityName(){
    return getProductPriceTableName();
  }

  public PriceCategory getPriceCategory() {
    return (PriceCategory) getColumnValue(getPriceCategoryIdColumnName());
  }

  public int getPriceCategoryID() {
    return getIntColumnValue(getPriceCategoryIdColumnName());
  }

  public void setPriceCategoryID(int id) {
    setColumn(getPriceCategoryIdColumnName(), id);
  }

  public int getPrice() {
    return getIntColumnValue(getPriceColumnName());
  }

  public void setPrice(int price) {
    setColumn(getPriceColumnName(), price);
  }

  public Timestamp getPriceDate() {
    return (Timestamp) getColumnValue(getPriceDateColumnName());
  }

  public void setPriceDate(Timestamp timestamp) {
    setColumn(getPriceDateColumnName(), timestamp);
  }

  public static String getProductPriceTableName(){return "SR_PRODUCT_PRICE";}
  public static String getProductIdColumnName(){return "SR_PRODUCT_ID";}
  public static String getPriceCategoryIdColumnName() {return "SR_PRICE_CATEGORY_ID";}
  public static String getPriceColumnName() {return "PRICE";}
  public static String getPriceDateColumnName() {return "PRICE_DATE"; }
  public static String getColumnNameCurrencyId() {return "TR_CURRENCY_ID"; }
  public static String getColumnNamePriceType() {return "PRICE_TYPE"; }






}
