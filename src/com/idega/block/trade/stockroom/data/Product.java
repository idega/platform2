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
 *  Title: IW Trade Description: Copyright: Copyright (c) 2001 Company: idega.is
 *
 *@author     2000 - idega team - <br>
 *      <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a> <br>
 *      <a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 *@created    6. mars 2002
 *@version    1.0
 */

public class Product extends GenericEntity {

  /**
   *  Description of the Field
   */
  public final static int DISCOUNT_TYPE_ID_AMOUNT = 0;
  /**
   *  Description of the Field
   */
  public final static int DISCOUNT_TYPE_ID_PERCENT = 1;

  /**
   *  Constructor for the Product object
   */
  public Product() {
    super();
  }

  /**
   *@param  id                Description of the Parameter
   *@exception  SQLException  Description of the Exception
   *@deprecated
   */
  public Product( int id ) throws SQLException {
    super( id );
  }


  /**
   *  Description of the Method
   */
  public void initializeAttributes() {
    this.addAttribute( getIDColumnName() );
    this.addAttribute( getColumnNameSupplierId(), "Birgi", true, true, Integer.class, "many_to_one", Supplier.class );
    this.addAttribute( getColumnNameFileId(), "Fylgiskjal(mynd)", true, true, Integer.class, "many_to_one", ICFile.class );
    this.addAttribute( getColumnNameIsValid(), "í notkun", true, true, Boolean.class );
    this.addAttribute( getDiscountTypeIdColumnName(), "discount type", true, true, Integer.class );
    this.addAttribute( getColumnNameNumber(), "númer", true, true, String.class );
    this.addAttribute( getColumnNameCreationDate(), "creation date", true, true, Timestamp.class );
    this.addAttribute( getColumnNameModificationDate(), "edit date", true, true, Timestamp.class );

    this.addManyToManyRelationShip( ProductCategory.class, "SR_PRODUCT_PRODUCT_CATEGORY" );
    this.setNullable( getColumnNameFileId(), true );
    this.addManyToManyRelationShip( com.idega.block.text.data.LocalizedText.class, "SR_PRODUCT_LOCALIZED_TEXT" );
    this.addManyToManyRelationShip( Address.class, "SR_PRODUCT_IC_ADDRESS" );
    this.addManyToManyRelationShip( TxText.class );

    this.addManyToManyRelationShip( ICFile.class );
  }

  /**
   *  Description of the Method
   *
   *@exception  SQLException  Description of the Exception
   */
  public void delete() throws SQLException {
    /*
     *  List prices = EntityFinder.findAllByColumn(ProductPrice.getStaticInstance(ProductPrice.class),ProductPrice.getColumnNameProductId(), this.getID() );
     *  if (prices != null) {
     *  ProductPrice price;
     *  for (int i = 0; i < prices.size(); i++) {
     *  price = (ProductPrice) prices.get(i);
     *  price.delete();
     *  }
     *  }
     *  super.delete();
     */
    this.setIsValid( false );
    this.update();
  }

  /**
   *  Sets the defaultValues attribute of the Product object
   */
  public void setDefaultValues() {
    this.setIsValid( true );
    this.setDiscountTypeId( DISCOUNT_TYPE_ID_PERCENT );
    this.setCreationDate( idegaTimestamp.getTimestampRightNow() );
    this.setModificationDate( idegaTimestamp.getTimestampRightNow() );
  }

  /**
   *  Gets the entityName attribute of the Product object
   *
   *@return    The entityName value
   */
  public String getEntityName() {
    return getProductEntityName();
  }

  /**
   *  Gets the productEntityName attribute of the Product class
   *
   *@return    The productEntityName value
   */
  public static String getProductEntityName() {
    return "SR_PRODUCT";
  }

  /**
   *  Gets the columnNameSupplierId attribute of the Product class
   *
   *@return    The columnNameSupplierId value
   */
  public static String getColumnNameSupplierId() {
    return "SR_SUPPLIER_ID";
  }

  /**
   *  Gets the columnNameFileId attribute of the Product class
   *
   *@return    The columnNameFileId value
   */
  public static String getColumnNameFileId() {
    return "IC_FILE_ID";
  }

  /**
   *  Gets the columnNameProductName attribute of the Product class
   *
   *@return    The columnNameProductName value
   */
  public static String getColumnNameProductName() {
    return "PRODUCT_NAME";
  }

  /**
   *  Gets the columnNameProductDescription attribute of the Product class
   *
   *@return    The columnNameProductDescription value
   */
  public static String getColumnNameProductDescription() {
    return "PRODUCT_DESCRIPTION";
  }

  /**
   *  Gets the columnNameIsValid attribute of the Product class
   *
   *@return    The columnNameIsValid value
   */
  public static String getColumnNameIsValid() {
    return "IS_VALID";
  }

  /**
   *  Gets the discountTypeIdColumnName attribute of the Product class
   *
   *@return    The discountTypeIdColumnName value
   */
  public static String getDiscountTypeIdColumnName() {
    return "DISCOUNT_TYPE_ID";
  }

  /**
   *  Gets the columnNameNumber attribute of the Product class
   *
   *@return    The columnNameNumber value
   */
  public static String getColumnNameNumber() {
    return "PRODUCT_NUMBER";
  }

  /**
   *  Gets the columnNameCreationDate attribute of the Product class
   *
   *@return    The columnNameCreationDate value
   */
  public static String getColumnNameCreationDate() {
    return "CREATION_DATE";
  }

  /**
   *  Gets the columnNameModificationDate attribute of the Product class
   *
   *@return    The columnNameModificationDate value
   */
  public static String getColumnNameModificationDate() {
    return "MODIFICATION_DATE";
  }


  /*
   *  Setters
   */
  /**
   *  Sets the supplierId attribute of the Product object
   *
   *@param  id  The new supplierId value
   */
  public void setSupplierId( int id ) {
    this.setColumn( getColumnNameSupplierId(), id );
  }

  /**
   *  Sets the supplierId attribute of the Product object
   *
   *@param  id  The new supplierId value
   */
  public void setSupplierId( Integer id ) {
    this.setColumn( getColumnNameSupplierId(), id );
  }

  /**
   *  Sets the fileId attribute of the Product object
   *
   *@param  id  The new fileId value
   */
  public void setFileId( int id ) {
    this.setColumn( getColumnNameFileId(), id );
  }

  /**
   *  Sets the fileId attribute of the Product object
   *
   *@param  id  The new fileId value
   */
  public void setFileId( Integer id ) {
    if (id == null) {
      System.err.println("Removing fileID : " +id);
      this.removeFromColumn(getColumnNameFileId());
    }else {
      this.setColumn( getColumnNameFileId(), id );
    }

  }


  /*
   *  public void setProductName(String name){
   *  this.setColumn(getColumnNameProductName(),name);
   *  }
   *  public void setProdcutDescription(String description){
   *  this.setColumn(getColumnNameProductDescription(),description);
   *  }
   */
  /**
   *  Sets the isValid attribute of the Product object
   *
   *@param  valid  The new isValid value
   */
  public void setIsValid( boolean valid ) {
    this.setColumn( getColumnNameIsValid(), valid );
  }

  /**
   *  Sets the discountTypeId attribute of the Product object
   *
   *@param  discountTypeId  The new discountTypeId value
   */
  public void setDiscountTypeId( int discountTypeId ) {
    setColumn( getDiscountTypeIdColumnName(), discountTypeId );
  }

  /**
   *  Sets the number attribute of the Product object
   *
   *@param  number  The new number value
   */
  public void setNumber( String number ) {
    setColumn( getColumnNameNumber(), number );
  }

  /**
   *  Sets the creationDate attribute of the Product object
   *
   *@param  timestamp  The new creationDate value
   */
  public void setCreationDate( idegaTimestamp timestamp ) {
    setCreationDate( timestamp.getTimestamp() );
  }

  /**
   *  Sets the creationDate attribute of the Product object
   *
   *@param  timestamp  The new creationDate value
   */
  public void setCreationDate( Timestamp timestamp ) {
    setColumn( getColumnNameCreationDate(), timestamp );
  }

  /**
   *  Sets the modificationDate attribute of the Product object
   *
   *@param  timestamp  The new modificationDate value
   */
  private void setModificationDate( idegaTimestamp timestamp ) {
    setModificationDate( timestamp.getTimestamp() );
  }

  /**
   *  Sets the modificationDate attribute of the Product object
   *
   *@param  timestamp  The new modificationDate value
   */
  private void setModificationDate( Timestamp timestamp ) {
    setColumn( getColumnNameModificationDate(), timestamp );
  }


  /*
   *  Getters
   */
  /**
   *  Gets the supplierId attribute of the Product object
   *
   *@return    The supplierId value
   */
  public int getSupplierId() {
    return this.getIntColumnValue( getColumnNameSupplierId() );
  }

  /**
   *  Gets the fileId attribute of the Product object
   *
   *@return    The fileId value
   */
  public int getFileId() {
    return this.getIntColumnValue( getColumnNameFileId() );
  }

  /**
   *@return        The productName value
   *@deprecated
   */
  public String getProductName() {
    return ProductBusiness.getProductName( this );
//    return this.getStringColumnValue(getColumnNameProductName());
  }

  /**
   *@return        The prodcutDescription value
   *@deprecated
   */
  public String getProdcutDescription() {
    return ProductBusiness.getProductDescription( this );
//    return this.getStringColumnValue(getColumnNameProductDescription());
  }

  /**
   *  Gets the isValid attribute of the Product object
   *
   *@return    The isValid value
   */
  public boolean getIsValid() {
    return this.getBooleanColumnValue( getColumnNameIsValid() );
  }

  /**
   *@return        The name value
   *@deprecated
   */
  public String getName() {
    return this.getProductName();
//    return "Ekki nota";
  }

  /**
   *  Gets the discountTypeId attribute of the Product object
   *
   *@return    The discountTypeId value
   */
  public int getDiscountTypeId() {
    return getIntColumnValue( getDiscountTypeIdColumnName() );
  }

  /**
   *  Gets the number attribute of the Product object
   *
   *@return    The number value
   */
  public String getNumber() {
    return getStringColumnValue( getColumnNameNumber() );
  }

  /**
   *  Gets the timeframes attribute of the Product object
   *
   *@return                   The timeframes value
   *@exception  SQLException  Description of the Exception
   */
  public Timeframe[] getTimeframes() throws SQLException {
    return getTimeframesOrdered( TimeframeComparator.FROMDATE );
  }

  /**
   *  Gets the timeframesOrdered attribute of the Product object
   *
   *@param  orderBy           Description of the Parameter
   *@return                   The timeframesOrdered value
   *@exception  SQLException  Description of the Exception
   */
  private Timeframe[] getTimeframesOrdered( int orderBy ) throws SQLException {
    if ( orderBy != -1 ) {
      List tFrames = EntityFinder.findRelated( this, Timeframe.getStaticInstance( Timeframe.class ) );
      TimeframeComparator comparator = new TimeframeComparator( TimeframeComparator.FROMDATE );
      Collections.sort( tFrames, comparator );
      return ( Timeframe[] ) tFrames.toArray( new Timeframe[]{} );
    } else {
      return ( Timeframe[] ) this.findRelated( Timeframe.getStaticInstance( Timeframe.class ) );
    }
  }

  /**
   *  Gets the timeframe attribute of the Product object
   *
   *@return                   The timeframe value
   *@exception  SQLException  Description of the Exception
   */
  public Timeframe getTimeframe() throws SQLException {
    Timeframe[] temp = getTimeframes();
    if ( temp.length > 0 ) {
      return temp[0];
    } else {
      return null;
    }
  }

  /**
   *  Gets the creationDate attribute of the Product object
   *
   *@return    The creationDate value
   */
  public Timestamp getCreationDate() {
    return ( Timestamp ) getColumnValue( getColumnNameCreationDate() );
  }

  /**
   *  Gets the editDate attribute of the Product object
   *
   *@return    The editDate value
   */
  public Timestamp getEditDate() {
    return ( Timestamp ) getColumnValue( getColumnNameModificationDate() );
  }

  /**
   *  Gets the text attribute of the Product object
   *
   *@return                   The text value
   *@exception  SQLException  Description of the Exception
   */
  public TxText getText() throws SQLException {
    TxText[] texti = ( TxText[] ) this.findRelated( ( TxText ) TxText.getStaticInstance( TxText.class ) );
    if ( texti.length > 0 ) {
      return texti[texti.length - 1];
    } else {
      return null;
    }
  }

  /**
   *  Description of the Method
   *
   *@exception  SQLException  Description of the Exception
   */
  public void update() throws SQLException {
    setModificationDate( idegaTimestamp.getTimestampRightNow() );
    super.update();
  }

}

