package com.idega.block.trade.stockroom.data;

import com.idega.core.data.ICFile;
import com.idega.core.data.ICCategory;
import java.sql.SQLException;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author       <a href="mailto:gimmi@idega.is">Grimur Jónsson</a>
 * @version 1.0
 */

public class ProductCategory extends ICCategory {

  public static final String CATEGORY_TYPE_TOUR = "sr_prod_cat_tour";
  public static final String CATEGORY_TYPE_HOTEL = "sr_prod_cat_hotel";
  public static final String CATEGORY_TYPE_FISHING = "sr_prod_cat_fishing";
  public static final String CATEGORY_TYPE_PRODUCT = "sr_prod_cat_product";

  public ProductCategory(){
    super();
  }

  public ProductCategory(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    super.initializeAttributes();
    //this.addManyToManyRelationShip(ICFile.class);
  }

  public void setDefaultValues() {
    super.setDefaultValues();
    this.setType(CATEGORY_TYPE_PRODUCT);
  }
/*
  public ProductCategory() {
  }

  public ProductCategory(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(this.getIDColumnName());
    this.addAttribute(getCategoryNameColumnName(),"Nafn",true,true,String.class,255);
    this.addAttribute(getDescriptionColumnName(),"Lýsing",true,true,String.class,1000);
    this.addAttribute(getIsValidColumnName(),"í notkun",true,true,Boolean.class);
    this.addAttribute(getExtraInfoColumnName(),"Aukaupplýsingar",true,true,String.class,1000);
    this.addTreeRelationShip();
  }

  public String getEntityName(){
    return "SR_PRODUCT_CATEGORY";
  }

  public static String getCategoryNameColumnName(){return "category_name";}
  public static String getDescriptionColumnName(){return "description";}
  public static String getExtraInfoColumnName(){return "extra_info";}
  public static String getCategoryTypeColumnName(){return "category_type";}
  public static String getIsValidColumnName(){return "is_valid";}

  public String getName(){
    return getStringColumnValue(getCategoryNameColumnName());
  }
  public String getDescription(){
    return getStringColumnValue(getDescriptionColumnName());
  }
  public String getExtraInfo(){
    return getStringColumnValue(getExtraInfoColumnName());
  }
  public boolean getIsValid(){
    return getBooleanColumnValue(getIsValidColumnName());
  }
  public void setName(String name){
    setColumn(getCategoryNameColumnName(),name);
  }
  public void setDescription(String description){
    setColumn(getDescriptionColumnName(),description);
  }
  public void setExtraInfo(String info){
    setColumn(getExtraInfoColumnName(),info);
  }
  public void setIsValid(boolean valid){
    setColumn(getIsValidColumnName(),valid);
  }
*/

} // Class ProductCategory