//idega 2001 - Gimmi

package com.idega.projects.nat.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.core.data.*;

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
    addAttribute(getDiscountColumnName(), "Afsláttur", true, true, Double.class);

    this.addManyToManyRelationShip(Service.class,"TB_SERVICE_PRICE_CATEGORY");
    this.addManyToManyRelationShip(com.idega.core.user.data.User.class,"TB_PRICE_CATEGORY_IC_USER");
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

  public static String getPriceCategoryTableName(){return "TB_PRICE_CATEGORY";}
  public static String getNameColumnName() {return "CATEGORY_NAME";}
  public static String getDescriptionColumnName() {return "DESCRIPTION";}
  public static String getExtraInfoColumnName() {return "EXTRA_INFO";}
  public static String getDiscountColumnName() {return "DISCOUNT";}






}
