package com.idega.block.trade.stockroom.data;

import com.idega.data.*;
import com.idega.core.data.*;
import java.sql.SQLException;
import java.util.List;


/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class Product extends GenericEntity {

  public Product() {
    super();
  }

  public Product(int id) throws SQLException {
    super(id);
  }


  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(getColumnNameSupplierId(),"Birgi",true,true,Integer.class,"many_to_one",Supplier.class);
    this.addAttribute(getColumnNameFileId(),"Fylgiskjal(mynd)",true,true,Integer.class,"many_to_one",ICFile.class);
    this.addAttribute(getColumnNameProductName(),"Nafn vöru",true,true,String.class,255);
    this.addAttribute(getColumnNameProductDescription(),"Lýsing vöru",true,true,String.class,510);
    this.addAttribute(getColumnNameIsValid(),"í notkun",true,true,Boolean.class);
//    this.addManyToManyRelationShip(PriceCategory.class,"SR_PRODUCT_PRICE_CATEGORY");
    this.addManyToManyRelationShip(ProductCategory.class,"SR_PRODUCT_PRODUCT_CATEGORY");
    this.setNullable(getColumnNameFileId(), true);
  }

  public void delete() throws SQLException {
      List prices = EntityFinder.findAllByColumn(ProductPrice.getStaticInstance(ProductPrice.class),ProductPrice.getColumnNameProductId(), this.getID() );
      if (prices != null) {
          ProductPrice price;
          for (int i = 0; i < prices.size(); i++) {
              price = (ProductPrice) prices.get(i);
              price.delete();
          }
      }

      super.delete();
  }

  public String getEntityName() {
    return getProductEntityName();
  }

  public static String getProductEntityName(){return "SR_PRODUCT";}
  public static String getColumnNameSupplierId(){return "SR_SUPPLIER_ID";}
  public static String getColumnNameFileId(){return "IC_FILE_ID";}
  public static String getColumnNameProductName(){return "PRODUCT_NAME";}
  public static String getColumnNameProductDescription(){return "PRODUCT_DESCRIPTION";}
  public static String getColumnNameIsValid(){return "IS_VALID";}


  /* Setters */

  public void setSupplierId(int id){
    this.setColumn(getColumnNameSupplierId(),id);
  }

  public void setSupplierId(Integer id){
    this.setColumn(getColumnNameSupplierId(),id);
  }

  public void setFileId(int id){
    this.setColumn(getColumnNameFileId(),id);
  }

  public void setFileId(Integer id){
    this.setColumn(getColumnNameFileId(),id);
  }

  public void setProductName(String name){
    this.setColumn(getColumnNameProductName(),name);
  }

  public void setProdcutDescription(String description){
    this.setColumn(getColumnNameProductDescription(),description);
  }

  public void setIsValid(boolean valid){
    this.setColumn(getColumnNameIsValid(),valid);
  }


  /* Getters */


  public int getSupplierId(){
    return this.getIntColumnValue(getColumnNameSupplierId());
  }

  public int getFileId(){
    return this.getIntColumnValue(getColumnNameFileId());
  }

  public String getProductName(){
    return this.getStringColumnValue(getColumnNameProductName());
  }

  public String getProdcutDescription(){
    return this.getStringColumnValue(getColumnNameProductDescription());
  }

  public boolean getIsValid(){
    return this.getBooleanColumnValue(getColumnNameIsValid());
  }
  public String getName() {
    return this.getProductName();
  }

}


