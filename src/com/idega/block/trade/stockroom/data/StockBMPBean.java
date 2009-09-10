package com.idega.block.trade.stockroom.data;



/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class StockBMPBean extends com.idega.data.GenericEntity implements com.idega.block.trade.stockroom.data.Stock {

  public StockBMPBean() {
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameVariantString(), "strengur smíðaður úr variontum", true, true, String.class);
    addAttribute(getColumnNameInStock(), "á lager", true, true, Integer.class);
    addAttribute(getColumnNameMinimumInStock(), "lágmark á lager", true, true, Integer.class);
    addAttribute(getColumnNameNotifyLevel(), "notify when in_stock is lower", true, true, Integer.class);
    addAttribute(getColumnNameStopSaleLevel(), "stop sale when in_stock is lower", true, true, Integer.class);
  }
  public String getEntityName() {
    return getTableNameStock();
  }

  public static String getTableNameStock() {return "SR_STOCK";}
  public static String getColumnNameVariantString() {return "VARIANT_STRING";}
  public static String getColumnNameInStock() {return "IN_STOCK";}
  public static String getColumnNameMinimumInStock() {return "MIN_IN_STOCK";}
  public static String getColumnNameNotifyLevel() {return "NOTIFY_LEVEL";}
  public static String getColumnNameStopSaleLevel() {return "STOP_SALE_LEVEL";}


  public void setVariantString(String string) {
    setColumn(getColumnNameVariantString(), string);
  }

  public void setInStock(int inStock) {
    setColumn(getColumnNameInStock(), inStock);
  }

  public void setMinimumInStock(int minInStock) {
    setColumn(getColumnNameMinimumInStock(), minInStock);
  }

  public void setNotifyLevel(int notifyLevel) {
    setColumn(getColumnNameNotifyLevel(), notifyLevel);
  }

  public void setStopSaleLevel(int stopSaleLevel) {
    setColumn(getColumnNameStopSaleLevel(), stopSaleLevel);
  }


  public String getVariantString() {
    return getStringColumnValue(getColumnNameVariantString());
  }

  public int getInStock() {
    return getIntColumnValue(getColumnNameInStock());
  }

  public int getMinimumInStock() {
    return getIntColumnValue(getColumnNameMinimumInStock());
  }

  public int getNotifyLevel() {
    return getIntColumnValue(getColumnNameNotifyLevel());
  }

  public int getStopSaleLevel() {
    return getIntColumnValue(getColumnNameStopSaleLevel());
  }

}
