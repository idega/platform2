package com.idega.block.trade.stockroom.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.core.data.*;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class PriceCategory extends GenericEntity{

  public PriceCategory(){
    super();
  }
  public PriceCategory(int id)throws SQLException{
    super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(), "Name", true, true, String.class, 255);
    addAttribute(getDescriptionColumnName(), "Lýsing", true, true, java.sql.Date.class);
    addAttribute(getExtraInfoColumnName(), "Aðrar upplysingar", true, true, java.sql.Date.class);
    addAttribute(getPriceIdColumnName(),"Verð (optional)",true,true,Integer.class,"many_to_one",ProductPrice.class);
    addAttribute(getDiscountColumnName(), "Afsláttur (optional)", true, true, Double.class);
    this.addTreeRelationShip();
  }


  public void setDefaultValue() {
    setName("");
  }

  public String getEntityName(){
    return getPriceCategoryTableName();
  }
  public String getName(){
    return getNameColumnName();
  }

  public void setName(String name){
    setColumn(getNameColumnName(),name);
  }

  public String getDescription() {
    return getStringColumnValue(getDescriptionColumnName());
  }

  public void setDescription(String description) {
    setColumn(getDescriptionColumnName(),description);
  }

  public String getExtraInfo() {
    return getStringColumnValue(getExtraInfoColumnName());
  }

  public void setExtraInfo(String extraInfo) {
    setColumn(getExtraInfoColumnName(), extraInfo);
  }

  public float getDiscount() {
    return getFloatColumnValue(getDiscountColumnName());
  }

  public void setDiscount(float discount) {
    setColumn(getDiscountColumnName(),discount);
  }

  public static String getPriceCategoryTableName(){return "SR_PRICE_CATEGORY";}
  public static String getNameColumnName() {return "CATEGORY_NAME";}
  public static String getDescriptionColumnName() {return "DESCRIPTION";}
  public static String getExtraInfoColumnName() {return "EXTRA_INFO";}
  public static String getPriceIdColumnName(){return "SR_PRODUCT_PRICE";}
  public static String getDiscountColumnName() {return "DISCOUNT";}






}
