package com.idega.block.trade.stockroom.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.LocalizedTextBMPBean;
import com.idega.block.text.data.TxText;
import com.idega.block.trade.stockroom.business.TimeframeComparator;
import com.idega.core.file.data.ICFile;
import com.idega.core.location.data.Address;
import com.idega.data.EntityControl;
import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.MetaData;
import com.idega.data.MetaDataCapable;
import com.idega.util.IWTimestamp;

/**
 *  Title: IW Trade Description: Copyright: Copyright (c) 2001 Company: idega.is
 *
 *@author     2000 - idega team - <br>
 *      <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a> <br>
 *      <a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 *@created    6. mars 2002
 *@version    1.0
 */

public class ProductBMPBean extends GenericEntity implements Product, IDOLegacyEntity, MetaDataCapable {

  public final static int DISCOUNT_TYPE_ID_AMOUNT = 0;
  public final static int DISCOUNT_TYPE_ID_PERCENT = 1;

  private final int FILTER_NOT_CONNECTED_TO_CATEGORY = 0;
  /**
   *  Constructor for the Product object
   */
  public ProductBMPBean() {
    super();
  }

  /**
   *@param  id                Description of the Parameter
   *@exception  SQLException  Description of the Exception
   *@deprecated
   */

  public ProductBMPBean( int id ) throws SQLException {
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
    addMetaDataRelationship();
    
    addIndex("IDX_PROD_1", new String[]{getColumnNameSupplierId(), getColumnNameIsValid()});
    addIndex("IDX_PROD_2", new String[]{getIDColumnName(), getColumnNameSupplierId(), getColumnNameIsValid()});
  }


  public void invalidate() throws IDOException{
    this.setIsValid( false );
    this.store();
  }

  /**
   * deprecated
   */
  public int getID() {
    return ((Integer) this.getPrimaryKey()).intValue();
  }

  /**
   *  Sets the defaultValues attribute of the Product object
   */
  public void setDefaultValues() {
    this.setIsValid( true );
    this.setDiscountTypeId( DISCOUNT_TYPE_ID_PERCENT );
    this.setCreationDate( IWTimestamp.getTimestampRightNow() );
    this.setModificationDate( IWTimestamp.getTimestampRightNow() );
  }

  /**
   *  Gets the entityName attribute of the Product object
   *
   *@return    The entityName value
   */
  public String getEntityName() {
    return getProductEntityName();
  }

  public static String getIdColumnName() { return ProductBMPBean.getProductEntityName()+"_ID";}
  public static String getProductEntityName() {    return "SR_PRODUCT";  }
  public static String getColumnNameSupplierId() {    return "SR_SUPPLIER_ID";  }
  public static String getColumnNameFileId() {    return "IC_FILE_ID";  }
  public static String getColumnNameProductName() {    return "PRODUCT_NAME";  }
  public static String getColumnNameProductDescription() {    return "PRODUCT_DESCRIPTION";  }
  public static String getColumnNameIsValid() {    return "IS_VALID";  }
  public static String getDiscountTypeIdColumnName() {    return "DISCOUNT_TYPE_ID";  }
  public static String getColumnNameNumber() {    return "PRODUCT_NUMBER";  }
  public static String getColumnNameCreationDate() {    return "CREATION_DATE";  }
  public static String getColumnNameModificationDate() {    return "MODIFICATION_DATE";  }


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
  public void setCreationDate( IWTimestamp timestamp ) {
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
  private void setModificationDate( IWTimestamp timestamp ) {
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

  public Supplier getSupplier() {
  		return (Supplier) getColumnValue( getColumnNameSupplierId() );
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
  *  Gets the fileId attribute of the Product object
  *
  *@return    The fileId value
  */
	public ICFile getFile() {
	  return (ICFile) this.getColumnValue( getColumnNameFileId() );
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
      try {
        Collection coll = this.idoGetRelatedEntities(Timeframe.class);
        //TimeframeHome tHome = (TimeframeHome) IDOLookup.getHome(Timeframe.class);
        List tFrames = new Vector();
        Iterator iter = coll.iterator();
        while (iter.hasNext()) {
          //Timeframe item = tHome.findByPrimaryKey(iter.next());
          tFrames.add(iter.next());
        }
        TimeframeComparator comparator = new TimeframeComparator( TimeframeComparator.FROMDATE );
        Collections.sort( tFrames, comparator );
        return ( Timeframe[] ) tFrames.toArray( new Timeframe[]{} );
      }catch (Exception e) {
        e.printStackTrace(System.err);
        throw new SQLException(e.getMessage());
      }
    } else {
      return ( Timeframe[] ) this.findRelated( com.idega.block.trade.stockroom.data.TimeframeBMPBean.getStaticInstance( Timeframe.class ) );
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
		try {
			Collection coll = this.idoGetRelatedEntities( TxText.class);
			if (coll != null && (coll.size() > 0) ) {
				Iterator iter = coll.iterator();
				Object obj;
				// Returns the last object in Collection
				while (iter.hasNext()) {
					obj = iter.next();
					if (!iter.hasNext()) {
						return (TxText) obj;	
					}	
				}
			}else {
				return null;
			}	
		} catch (IDORelationshipException e) {
			throw new SQLException(e.getMessage());
		}
		return null;
//    TxText[] texti = ( TxText[] ) this.findRelated( ( TxText ) com.idega.block.text.data.TxTextBMPBean.getStaticInstance( TxText.class ) );
//    if ( texti.length > 0 ) {
//      return texti[texti.length - 1];
//    } else {
//      return null;
//    }
  }

  /**
   *  Description of the Method
   *
   *@exception  SQLException  Description of the Exception
   */
  public void update() throws SQLException {
    setModificationDate( IWTimestamp.getTimestampRightNow() );
    super.update();
  }


  public Collection getProductCategories() throws IDORelationshipException {
    Collection coll = this.idoGetRelatedEntities(ProductCategory.class);
    return coll;
  }

  public void setProductCategories(int[] categoryIds) throws RemoteException, FinderException, IDORemoveRelationshipException{
    this.idoRemoveFrom(ProductCategory.class);
    ProductCategory pCat;
    ProductCategoryHome pCatHome = (ProductCategoryHome) IDOLookup.getHome(ProductCategory.class);
    for (int i = 0; i < categoryIds.length; i++) {
      pCat = pCatHome.findByPrimaryKey(categoryIds[i]);
      //pCat = ((com.idega.block.trade.stockroom.data.ProductCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ProductCategory.class)).findByPrimaryKeyLegacy(categoryIds[i]);
      addCategory(pCat);
    }
  }

  public boolean addCategory(ProductCategory productCategory) {
    try {
      productCategory.addTo(this);
      return true;
    }catch (Exception e) {
      return false;
    }
  }

  public void removeCategory(ProductCategory productCategory) throws IDORemoveRelationshipException{
    this.idoRemoveFrom(productCategory);
  }

  public void removeAllFrom(Class entityInterface) throws IDORemoveRelationshipException{
    this.idoRemoveFrom(entityInterface);
  }

  public void addTravelAddresses(int[] addressIds) throws RemoteException, FinderException, IDOAddRelationshipException{
    TravelAddress address;
    TravelAddressHome home = (TravelAddressHome) IDOLookup.getHome(TravelAddress.class);
    if(addressIds != null){
      for (int i = 0; i < addressIds.length; i++) {
        address = home.findByPrimaryKey(addressIds[i]);
        addTravelAddress(address);
//	  product.addTo(TravelAddress.class, addressIds[i]);
      }
    }
  }

  public void addTravelAddress(TravelAddress address)  {
    try {
      this.idoAddTo(address);
    }catch (IDOAddRelationshipException e) {
      debug("product already connencted to address");
    }
  }

  public void removeTravelAddress(TravelAddress address) throws IDORemoveRelationshipException {
    this.idoRemoveFrom(address);
  }

  public String getProductName(int localeId) {
    LocalizedText text = TextFinder.getLocalizedText(this, localeId);
    if (text == null) text = TextFinder.getLocalizedText(this, 1);
    String name = "";
    if (text != null) {
      name = text.getHeadline();
    }
    return name;
  }

  public void setProductName(int localeId, String name) {
    LocalizedText locText = TextFinder.getLocalizedText(this,localeId);
    boolean newLocText = false;
    if ( locText == null ) {
      locText = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
      newLocText = true;
    }

    locText.setHeadline(name);

    if ( newLocText ) {
      locText.setLocaleId(localeId);
      try {
	locText.insert();
	locText.addTo(this);
      }
      catch (SQLException e) {
	e.printStackTrace(System.err);
      }
    }
    else {
      try {
	locText.update();
      }
      catch (SQLException e) {
	e.printStackTrace(System.err);
      }
    }
  }


  public String getProductDescription(int localeId) {
    LocalizedText text = TextFinder.getLocalizedText(this, localeId);
    if (text == null) text = TextFinder.getLocalizedText(this, 1);
    String description = "";
    if (text != null) {
      description = text.getBody();
    }
    return description;
  }

  public void setProductDescription(int localeId, String description) {
    LocalizedText locText = TextFinder.getLocalizedText(this,localeId);
    boolean newLocText = false;
    if ( locText == null ) {
      locText = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
      newLocText = true;
    }

    locText.setBody(description);

    if ( newLocText ) {
      locText.setLocaleId(localeId);
      try {
	locText.insert();
	locText.addTo(this);
      }
      catch (SQLException e) {
	e.printStackTrace(System.err);
      }
    }
    else {
      try {
	locText.update();
      }
      catch (SQLException e) {
	e.printStackTrace(System.err);
      }
    }
  }


  public String getProductTeaser(int localeId) {
    LocalizedText text = TextFinder.getLocalizedText(this, localeId);
    if (text == null) text = TextFinder.getLocalizedText(this, 1);
    String teaser = "";
    if (text != null) {
      teaser = text.getTitle();
      if (teaser == null) teaser = "";
    }
    return teaser;
  }

  public void setProductTeaser(int localeId, String teaser) {
    LocalizedText locText = TextFinder.getLocalizedText(this,localeId);
    boolean newLocText = false;
    if ( locText == null ) {
      locText = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
      newLocText = true;
    }

    locText.setTitle(teaser);

    if ( newLocText ) {
      locText.setLocaleId(localeId);
      try {
	locText.insert();
	locText.addTo(this);
      }
      catch (SQLException e) {
	e.printStackTrace(System.err);
      }
    }
    else {
      try {
	locText.update();
      }
      catch (SQLException e) {
	e.printStackTrace(System.err);
      }
    }
  }

  public Collection ejbHomeGetProductsOrderedByProductCategory(int supplierId) throws FinderException {
    return ejbHomeGetProductsOrderedByProductCategory(supplierId, null, null);
  }

  public Collection ejbHomeGetProductsOrderedByProductCategory(int supplierId, IWTimestamp stamp) throws FinderException {
    return ejbHomeGetProductsOrderedByProductCategory(supplierId, stamp, null);
  }

  public Collection ejbHomeGetProductsOrderedByProductCategory(int supplierId, IWTimestamp from, IWTimestamp to) throws FinderException {
    return ejbHomeGetProducts(supplierId, -1, from, to, ProductCategoryBMPBean.getEntityTableName()+"."+ProductCategoryBMPBean.getColumnName(), -1, -1, false);
  }

  public Collection ejbHomeGetProducts(int supplierId) throws FinderException {
    String pTable = com.idega.block.trade.stockroom.data.ProductBMPBean.getProductEntityName();

    StringBuffer sqlQuery = new StringBuffer();
      sqlQuery.append("SELECT * FROM ").append(pTable);
      sqlQuery.append(" WHERE ");
      sqlQuery.append(pTable).append(".").append(com.idega.block.trade.stockroom.data.ProductBMPBean.getColumnNameIsValid()).append(" = 'Y'");
      if (supplierId != -1)
      sqlQuery.append(" AND ").append(pTable).append(".").append(com.idega.block.trade.stockroom.data.ProductBMPBean.getColumnNameSupplierId()).append(" = ").append(supplierId);
      sqlQuery.append(" order by ").append(com.idega.block.trade.stockroom.data.ProductBMPBean.getColumnNameNumber());

    return this.idoFindPKsBySQL(sqlQuery.toString());
  }


  public Collection ejbHomeGetProducts(int supplierId, int productCategoryId ,IWTimestamp from, IWTimestamp to) throws FinderException{
    return ejbHomeGetProducts(supplierId, productCategoryId, from, to, null);
  }

  public Collection ejbHomeGetProducts(int supplierId, int productCategoryId ,IWTimestamp from, IWTimestamp to, String orderBy) throws FinderException{
    return ejbHomeGetProducts(supplierId, productCategoryId, from, to, orderBy, -1, -1);
  }

  public int ejbHomeGetProductFilterNotConnectedToAnyProductCategory() {
    return FILTER_NOT_CONNECTED_TO_CATEGORY;
  }

  public Collection ejbHomeGetProducts(int supplierId, int productCategoryId ,IWTimestamp from, IWTimestamp to, String orderBy, int localeId, int filter) throws FinderException{
		return ejbHomeGetProducts(supplierId, productCategoryId, from, to, orderBy, localeId, filter, true);
  }
  
  public Collection ejbHomeGetProducts(int supplierId, int productCategoryId ,IWTimestamp from, IWTimestamp to, String orderBy, int localeId, int filter, boolean useTimeframes) throws FinderException{
    Collection coll;

    Timeframe timeframe = (Timeframe) com.idega.block.trade.stockroom.data.TimeframeBMPBean.getStaticInstance(Timeframe.class);
    ProductCategory pCat = (ProductCategory) com.idega.block.trade.stockroom.data.ProductCategoryBMPBean.getStaticInstance(ProductCategory.class);
    LocalizedText locText = (LocalizedText) LocalizedTextBMPBean.getStaticInstance(LocalizedText.class);
    //Service tService = (Service) is.idega.idegaweb.travel.data.ServiceBMPBean.getStaticInstance(Service.class);

    String middleTable = EntityControl.getManyToManyRelationShipTableName(Timeframe.class,Product.class);
    String Ttable = com.idega.block.trade.stockroom.data.TimeframeBMPBean.getTimeframeTableName();
    String Ptable = com.idega.block.trade.stockroom.data.ProductBMPBean.getProductEntityName();
    String catMiddle = EntityControl.getManyToManyRelationShipTableName(ProductCategory.class,Product.class);
    String catTable = pCat.getEntityName();

    String locMiddleTable = EntityControl.getManyToManyRelationShipTableName(Product.class, LocalizedText.class);
    String locTxtTable = LocalizedTextBMPBean.getEntityTableName();


    StringBuffer timeframeSQL = new StringBuffer();
      timeframeSQL.append("SELECT distinct "+Ptable+".* FROM "+Ptable);
      if (from != null && to != null && useTimeframes) {
        timeframeSQL.append(", "+Ttable+", "+middleTable);
      }
      if (localeId != -1) {
        timeframeSQL.append(", "+locMiddleTable+", "+locTxtTable);
      }

      timeframeSQL.append(", "+catMiddle+", "+catTable);
      timeframeSQL.append(" WHERE ");
      timeframeSQL.append(Ptable+"."+com.idega.block.trade.stockroom.data.ProductBMPBean.getColumnNameIsValid()+" = 'Y'");
      if (from != null && to != null && useTimeframes) {
        timeframeSQL.append(" AND ");
        timeframeSQL.append(Ttable+"."+timeframe.getIDColumnName()+" = "+middleTable+"."+timeframe.getIDColumnName());
        timeframeSQL.append(" AND ");
        timeframeSQL.append(Ptable+"."+this.getIDColumnName()+" = "+middleTable+"."+this.getIDColumnName());
      }
      timeframeSQL.append(" AND ");
      timeframeSQL.append(Ptable+"."+this.getIDColumnName()+" = "+catMiddle+"."+this.getIDColumnName());

      timeframeSQL.append(" AND ");
      timeframeSQL.append(catMiddle+"."+pCat.getIDColumnName() +" = "+catTable+"."+pCat.getIDColumnName());
      if (productCategoryId != -1) {
        timeframeSQL.append(" AND ");
        timeframeSQL.append(catMiddle+"."+pCat.getIDColumnName() +" = "+productCategoryId);
      }

    // Hondla ef supplierId != -1
    Collection tempProducts = null;;
    if (supplierId != -1) tempProducts = ejbHomeGetProducts(supplierId);
    if (tempProducts != null)
    if (tempProducts.size() > 0) {
      timeframeSQL.append(" AND ");
      timeframeSQL.append(Ptable+"."+this.getIDColumnName()+" in (");
      Iterator iter = tempProducts.iterator();
      Object item;
      int counter = 0;
      while (iter.hasNext()) {
        item = iter.next();
        ++counter;
        if (counter == 1) {
          timeframeSQL.append(item.toString() );
        }else {
          timeframeSQL.append(","+item.toString() );
        }
      }
      timeframeSQL.append(")");
    }

    if (from != null && to != null && useTimeframes) {
      timeframeSQL.append(" AND ");
      timeframeSQL.append("(");
      timeframeSQL.append(" ("+com.idega.block.trade.stockroom.data.TimeframeBMPBean.getTimeframeFromColumnName()+" <= '"+from.toSQLDateString()+"' AND "+com.idega.block.trade.stockroom.data.TimeframeBMPBean.getTimeframeToColumnName()+" >= '"+from.toSQLDateString()+"')");
      timeframeSQL.append(" OR ");
      timeframeSQL.append(" ("+com.idega.block.trade.stockroom.data.TimeframeBMPBean.getTimeframeFromColumnName()+" <= '"+to.toSQLDateString()+"' AND "+com.idega.block.trade.stockroom.data.TimeframeBMPBean.getTimeframeToColumnName()+" >= '"+to.toSQLDateString()+"')");
      timeframeSQL.append(" OR ");
      timeframeSQL.append(" ("+com.idega.block.trade.stockroom.data.TimeframeBMPBean.getTimeframeFromColumnName()+" >= '"+from.toSQLDateString()+"' AND "+com.idega.block.trade.stockroom.data.TimeframeBMPBean.getTimeframeToColumnName()+" <= '"+to.toSQLDateString()+"')");
      timeframeSQL.append(")");
    }

    if (localeId != -1) {
      timeframeSQL.append(" AND ")
          .append(locMiddleTable+"."+locText.getIDColumnName()+ " = "+locTxtTable+"."+locText.getIDColumnName())
          .append(" AND ")
          .append(locMiddleTable+"."+this.getIdColumnName()+" = "+Ptable+"."+this.getIdColumnName())
          .append(" AND ")
          .append(locTxtTable+"."+LocalizedTextBMPBean.getColumnNameLocaleId()+" = "+localeId);
    }

    /**
     * @todo bæta við filter supporti
    switch (filter) {
      case FILTER_NOT_CONNECTED_TO_CATEGORY :
        break;
      default:
        break;
    }
     */


    if (orderBy != null) {
      timeframeSQL.append(" ORDER BY "+orderBy);
    }else if (from != null && to != null) {
      timeframeSQL.append(" ORDER BY "+com.idega.block.trade.stockroom.data.TimeframeBMPBean.getTimeframeFromColumnName());
    }

//    System.out.println(timeframeSQL.toString());
    coll = this.idoFindPKsBySQL(timeframeSQL.toString());
//    products = EntityFinder.getInstance().findAll(Product.class,timeframeSQL.toString());
    return coll;
  }

  public List getDepartureAddresses(boolean ordered) throws IDOFinderException  {
    List list = EntityFinder.getInstance().findRelated(this, TravelAddress.class, com.idega.block.trade.stockroom.data.TravelAddressBMPBean.getColumnNameAddressTypeId(), Integer.toString(com.idega.block.trade.stockroom.data.TravelAddressBMPBean.ADDRESS_TYPE_DEPARTURE) );
    if (list == null) {
      list = new Vector();
    }
    return list;
  }

  public void addArrivalAddress(Address address)  {
    try {
      this.idoAddTo(address);
/*
      StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ").append(EntityControl.getManyToManyRelationShipTableName(Product.class, Address.class))
           .append(" (SR_PRODUCT_ID, IC_ADDRESS_ID, ").append(TravelAddressBMPBean.getColumnNameAddressTypeId())
           .append(") VALUES (")
           .append(this.getPrimaryKey().toString()).append(", ")
           .append(address.getID()).append(", ")
           .append(Integer.toString(com.idega.block.trade.stockroom.data.TravelAddressBMPBean.ADDRESS_TYPE_ARRIVAL))
           .append(")");
      this.idoExecuteTableUpdate(sql.toString());*/
    }catch (Exception e) {
//      e.printStackTrace();
      debug("product already connected to address");
    }
  }

  public List getArrivalAddresses() throws IDOFinderException  {
    List list = EntityFinder.getInstance().findRelated(this, Address.class);//, com.idega.block.trade.stockroom.data.TravelAddressBMPBean.getColumnNameAddressTypeId(), Integer.toString(com.idega.block.trade.stockroom.data.TravelAddressBMPBean.ADDRESS_TYPE_ARRIVAL) );
    if (list == null) {
      list = new Vector();
    }
    return list;
  }

  public Collection getICFile() throws IDORelationshipException {
    return this.idoGetRelatedEntities(ICFile.class);
  }

  public void removeICFile(ICFile file) throws IDORemoveRelationshipException{
    this.idoRemoveFrom(file);
  }

  public void addICFile(ICFile file) throws IDOAddRelationshipException{
    this.idoAddTo(file);
  }

  public void addTimeframe(Timeframe frame) throws IDOAddRelationshipException{
    this.idoAddTo(frame);
  }

  public void removeTimeframe(Timeframe frame) throws IDORemoveRelationshipException{
    this.idoRemoveFrom(frame);
  }

  public void addText(TxText text) throws IDOAddRelationshipException{
    this.idoAddTo(text);
  }

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#delete()
	 */
	public void delete() throws SQLException {
		this.removeFrom(MetaData.class);
		super.delete();
	}
}