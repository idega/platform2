package com.idega.block.trade.stockroom.data;

import java.sql.*;
import java.util.List;
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

public class PriceCategoryBMPBean extends com.idega.data.GenericEntity implements com.idega.block.trade.stockroom.data.PriceCategory {

  public static final String PRICETYPE_PRICE = "sr_pricetype_price";
  public static final String PRICETYPE_DISCOUNT = "sr_pricetype_discount";

  public PriceCategoryBMPBean(){
    super();
  }

  public PriceCategoryBMPBean(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameName(), "Name", true, true, String.class, 255);
    addAttribute(getColumnNameDescription(), "Lýsing", true, true, String.class, 255);
    addAttribute(getColumnNameType(),"Type",true,true,String.class,255);
    addAttribute(getColumnNameExtraInfo(), "Aðrar upplysingar", true, true, String.class, 255);
    addAttribute(getColumnNameNetbookingCategory(), "Verðflokkur fyrir netbókun", true, true, Boolean.class, 255);
    addAttribute(getColumnNameSupplierId(),"supplier_id (owner)", true, true, Integer.class, "many_to_one", Supplier.class);
    addAttribute(getColumnNameParentId(),"parent_id", true, true, Integer.class, "many_to_one", PriceCategory.class);
    addAttribute(getColumnNameIsValid(), "is valid", true, true, Boolean.class);
    addAttribute(getColumnNameCountAsPerson(), "count as person", true, true, Boolean.class);

    this.addManyToManyRelationShip(Address.class);
    this.addTreeRelationShip();
  }

  public void delete() {
    try {
      setColumn(getColumnNameIsValid(), false);
      this.update();
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
  }

  public void setDefaultValues() {
    setColumn(getColumnNameIsValid(), true);
    setColumn(getColumnNameCountAsPerson(), "true");
  }

  public String getEntityName(){
    return getPriceCategoryTableName();
  }
  public String getName(){
    return getStringColumnValue(getColumnNameName());
  }

  public void setName(String name){
    setColumn(getColumnNameName(),name);
  }

  public String getDescription() {
    return getStringColumnValue(getColumnNameDescription());
  }

  public void setDescription(String description) {
    setColumn(getColumnNameDescription(),description);
  }

  public String getExtraInfo(){
    return getStringColumnValue(getColumnNameExtraInfo());
  }

  public void setExtraInfo(String extraInfo){
    setColumn(getColumnNameExtraInfo(),extraInfo);
  }


  public String getType(){
    return getStringColumnValue(getColumnNameType());
  }

  public void setType(String type){
    setColumn(getColumnNameType(),type);
  }

  public void isNetbookingCategory(boolean value){
    setColumn(getColumnNameNetbookingCategory(), value);
  }

  public boolean isNetbookingCategory(){
    return getBooleanColumnValue(getColumnNameNetbookingCategory());
  }

  public void setSupplierId(int id){
    setColumn(getColumnNameSupplierId(), id);
  }

  public int getSupplierId(){
    return getIntColumnValue(getColumnNameSupplierId());
  }

  public void setParentId(int id){
    setColumn(getColumnNameParentId(), id);
  }

  public int getParentId(){
    return getIntColumnValue(getColumnNameParentId());
  }

  public void setCountAsPerson(boolean countAsPerson) {
    setColumn(getColumnNameCountAsPerson(), countAsPerson);
  }

  public boolean getCountAsPerson() {
    return getBooleanColumnValue(getColumnNameCountAsPerson(), true);
  }

  public static String getColumnNameName() {return "CATEGORY_NAME";}
  public static String getColumnNameDescription() {return "DESCRIPTION";}
  public static String getColumnNameType(){return "CATEGORY_TYPE";}
  public static String getColumnNameExtraInfo() {return "EXTRA_INFO";}
  public static String getColumnNameSupplierId() {return "SUPPLIER_ID";}
  public static String getColumnNameParentId() {return "PARENT_ID";}
  public static String getColumnNameNetbookingCategory() {return "NETBOOKING_CATEGORY";}
  public static String getColumnNameIsValid() {return "IS_VALID";}
  public static String getColumnNameCountAsPerson() {return "COUNT_AS_PERSON";}
  public static String getPriceCategoryTableName() {return "SR_PRICE_CATEGORY";}

}

