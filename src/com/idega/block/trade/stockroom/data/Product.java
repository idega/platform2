package com.idega.block.trade.stockroom.data;

import java.util.Collections;
import com.idega.data.*;
import com.idega.core.data.*;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import java.sql.SQLException;
import java.util.List;
import java.sql.Timestamp;
import com.idega.util.idegaTimestamp;
import com.idega.block.text.business.*;
import com.idega.block.text.data.TxText;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.business.TimeframeComparator;
import com.idega.data.EntityFinder;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class Product extends GenericEntity {

  public static final int DISCOUNT_TYPE_ID_AMOUNT = 0;
  public static final int DISCOUNT_TYPE_ID_PERCENT = 1;

  public Product() {
    super();
  }

  /**
   * @deprecated
   */
  public Product(int id) throws SQLException {
    super(id);
  }


  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(getColumnNameSupplierId(),"Birgi",true,true,Integer.class,"many_to_one",Supplier.class);
    this.addAttribute(getColumnNameFileId(),"Fylgiskjal(mynd)",true,true,Integer.class,"many_to_one",ICFile.class);
    this.addAttribute(getColumnNameIsValid(),"í notkun",true,true,Boolean.class);
    this.addAttribute(getDiscountTypeIdColumnName(), "discount type", true, true, Integer.class);
    this.addAttribute(getColumnNameNumber(), "númer", true, true, String.class);
    this.addAttribute(getColumnNameCreationDate(), "creation date", true, true, Timestamp.class);
    this.addAttribute(getColumnNameModificationDate(), "edit date", true, true, Timestamp.class);

    this.addManyToManyRelationShip(ProductCategory.class,"SR_PRODUCT_PRODUCT_CATEGORY");
    this.setNullable(getColumnNameFileId(), true);
    this.addManyToManyRelationShip(com.idega.block.text.data.LocalizedText.class, "SR_PRODUCT_LOCALIZED_TEXT");
    this.addManyToManyRelationShip(Timeframe.class ,"SR_PRODUCT_TIMEFRAME");
    this.addManyToManyRelationShip(TravelAddress.class, "SR_PRODUCT_SR_ADDRESS");
    this.addManyToManyRelationShip(Address.class, "SR_PRODUCT_IC_ADDRESS");
    this.addManyToManyRelationShip(TxText.class);
  }

  public void delete() throws SQLException {
/*      List prices = EntityFinder.findAllByColumn(ProductPrice.getStaticInstance(ProductPrice.class),ProductPrice.getColumnNameProductId(), this.getID() );
      if (prices != null) {
          ProductPrice price;
          for (int i = 0; i < prices.size(); i++) {
              price = (ProductPrice) prices.get(i);
              price.delete();
          }
      }

      super.delete();
      */
      this.setIsValid(false);
      this.update();
  }

  public void setDefaultValues() {
    this.setIsValid(true);
    this.setDiscountTypeId(DISCOUNT_TYPE_ID_PERCENT);
    this.setCreationDate(idegaTimestamp.getTimestampRightNow());
    this.setModificationDate(idegaTimestamp.getTimestampRightNow());
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
  public static String getDiscountTypeIdColumnName() {return "DISCOUNT_TYPE_ID";}
  public static String getColumnNameNumber() {return "PRODUCT_NUMBER";}
  public static String getColumnNameCreationDate() {return "CREATION_DATE";}
  public static String getColumnNameModificationDate() {return "MODIFICATION_DATE";}


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

/*  public void setProductName(String name){
    this.setColumn(getColumnNameProductName(),name);
  }

  public void setProdcutDescription(String description){
    this.setColumn(getColumnNameProductDescription(),description);
  }
*/
  public void setIsValid(boolean valid){
    this.setColumn(getColumnNameIsValid(),valid);
  }

  public void setDiscountTypeId(int discountTypeId) {
    setColumn(getDiscountTypeIdColumnName(), discountTypeId);
  }

  public void setNumber(String number) {
    setColumn(getColumnNameNumber(), number);
  }

  public void setCreationDate(idegaTimestamp timestamp) {
    setCreationDate(timestamp.getTimestamp());
  }

  public void setCreationDate(Timestamp timestamp) {
    setColumn(getColumnNameCreationDate(), timestamp);
  }

  private void setModificationDate(idegaTimestamp timestamp) {
    setModificationDate(timestamp.getTimestamp());
  }

  private void setModificationDate(Timestamp timestamp) {
    setColumn(getColumnNameModificationDate(), timestamp);
  }


  /* Getters */
  public int getSupplierId(){
    return this.getIntColumnValue(getColumnNameSupplierId());
  }

  public int getFileId(){
    return this.getIntColumnValue(getColumnNameFileId());
  }

  /**
   * @deprecated
   */
  public String getProductName(){
    return ProductBusiness.getProductName(this);
//    return this.getStringColumnValue(getColumnNameProductName());
  }

  /**
   * @deprecated
   */
  public String getProdcutDescription(){
    return ProductBusiness.getProductDescription(this);
//    return this.getStringColumnValue(getColumnNameProductDescription());
  }

  public boolean getIsValid(){
    return this.getBooleanColumnValue(getColumnNameIsValid());
  }

  /**
   * @deprecated
   */
  public String getName() {
    return this.getProductName();
//    return "Ekki nota";
  }

  public int getDiscountTypeId() {
    return getIntColumnValue(getDiscountTypeIdColumnName());
  }

  public String getNumber() {
    return getStringColumnValue(getColumnNameNumber());
  }

  public Timeframe[] getTimeframes() throws SQLException  {
    return getTimeframesOrdered(TimeframeComparator.FROMDATE);
  }

  private Timeframe[] getTimeframesOrdered(int orderBy) throws SQLException  {
    if (orderBy != -1) {
      List tFrames = EntityFinder.findRelated(this, Timeframe.getStaticInstance(Timeframe.class));
      TimeframeComparator comparator = new TimeframeComparator(TimeframeComparator.FROMDATE);
      Collections.sort(tFrames,comparator);
      return (Timeframe[]) tFrames.toArray(new Timeframe[]{});
    }else {
      return (Timeframe[]) this.findRelated(Timeframe.getStaticInstance(Timeframe.class));
    }
  }

  public Timeframe getTimeframe() throws SQLException{
    Timeframe[] temp = getTimeframes();
    if (temp.length > 0) {
      return temp[0];
    }
    else {
      return null;
    }
  }

  public Timestamp getCreationDate() {
    return (Timestamp) getColumnValue(getColumnNameCreationDate());
  }

  public Timestamp getEditDate() {
    return (Timestamp) getColumnValue(getColumnNameModificationDate());
  }

  public TxText getText() throws SQLException{
    TxText[] texti = (TxText[]) this.findRelated((TxText) TxText.getStaticInstance(TxText.class));
    if (texti.length > 0) {
      return texti[texti.length -1];
    }else {
      return null;
    }
  }

  public void update() throws SQLException{
    setModificationDate(idegaTimestamp.getTimestampRightNow());
    super.update();
  }

}


