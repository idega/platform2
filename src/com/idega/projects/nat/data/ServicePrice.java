//idega 2001 - Gimmi

package com.idega.projects.nat.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.core.data.*;

public class ServicePrice extends GenericEntity{

  public ServicePrice(){
          super();
  }
  public ServicePrice(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getPriceCategoryIDColumnName(), "Verðflokkur" ,true, true, Integer.class, "many-to-one", PriceCategory.class);
    addAttribute(getPriceColumnName(), "Verð", true, true, Integer.class);
    addAttribute(getPriceDateColumnName(), "Dagsetning verðs", true, true, java.sql.Date.class);
  }

  public String getEntityName(){
    return getServicePriceTableName();
  }

  public PriceCategory getPriceCategory() {
    return (PriceCategory) getColumnValue(getPriceCategoryIDColumnName());
  }

  public int getPriceCategoryID() {
    return getIntColumnValue(getPriceCategoryIDColumnName());
  }

  public void setPriceCategoryID(int id) {
    setColumn(getPriceCategoryIDColumnName(), id);
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

  public static String getServicePriceTableName(){return "TB_SERVICE_PRICE";}
  public static String getPriceCategoryIDColumnName() {return "TB_PRICE_CATEGORY_ID";}
  public static String getPriceColumnName() {return "PRICE";}
  public static String getPriceDateColumnName() {return "PRICE_DATE"; }






}
