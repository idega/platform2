package com.idega.block.trade.stockroom.business;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.block.text.data.*;
import com.idega.core.data.*;
import java.util.Locale;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.block.text.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.data.*;
import java.sql.SQLException;
import java.util.*;
import com.idega.util.*;

import com.idega.block.trade.stockroom.data.*;
/**
 * @todo losa við service;
 */
import is.idega.idegaweb.travel.data.Service;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductBusiness {
  public static final String PRODUCT_ID = "pr_bus_prod_id";

  public static String uniqueDepartureAddressType = "TB_TRIP_DEPARTURE_ADDRESS";
  public static String uniqueArrivalAddressType = "TB_TRIP_ARRIVAL_ADDRESS";
  public static String uniqueHotelPickupAddressType = "TB_HOTEL_PICKUP_ADDRESS";

  public static String PARAMETER_LOCALE_DROP = "product_locale_drop";
  public static int defaultLocaleId = 1;

  private static String productsApplication = "productsApplication_";
  public static HashMap products = new HashMap();

  public ProductBusiness() {
  }


  public static int updateProduct(int productId, int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    return createProduct(productId,supplierId, fileId, productName, number, productDescription, isValid, addressIds, discountTypeId, -1);
  }

  public static int updateProduct(int productId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int localeId) throws Exception{
    return createProduct(productId,-1, fileId, productName, number, productDescription, isValid, null, -1, localeId);
  }

  public static int createProduct(Integer fileId, String productName, String number, String productDescription, boolean isValid, int localeId) throws Exception{
    return createProduct(-1,-1, fileId, productName, number, productDescription, isValid, null, -1, localeId);
  }

  public static int createProduct(Integer fileId, String productName, String number, String productDescription, boolean isValid) throws Exception{
    return createProduct(-1,-1, fileId, productName, number, productDescription, isValid, null, -1, -1);
  }

  public static int createProduct(int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    return createProduct(-1,supplierId, fileId, productName, number, productDescription, isValid, addressIds, discountTypeId, -1);
  }

  static int createProduct(int productId, int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    return createProduct(productId, supplierId, fileId, productName, number, productDescription, isValid, addressIds, discountTypeId, -1);
  }

  static int createProduct(int productId, int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId, int localeId) throws Exception{
    Product product= null;
    if (productId == -1) {
      product = new Product();
    }else {
      product = ProductBusiness.getProduct(productId);// Product(productId);
    }

    if (supplierId != -1)
    product.setSupplierId(supplierId);
    if(fileId != null){
      product.setFileId(fileId);
    }
    product.setIsValid(isValid);
    if (discountTypeId != -1) {
      product.setDiscountTypeId(discountTypeId);
    }
    if (number == null) number = "";
    product.setNumber(number);


    if (productId == -1) {
      product.insert();
    }else {
      ProductBusiness.updateProduct(product);
      //product.update();
    }

    if (localeId == -1) {
      ProductBusiness.setProductName(product, productName);
      ProductBusiness.setProductDescription(product, productDescription);
    }else {
      ProductBusiness.setProductName(product, localeId, productName);
      ProductBusiness.setProductDescription(product, localeId, productDescription);
    }

    if(addressIds != null){
      for (int i = 0; i < addressIds.length; i++) {
	try {
	  product.addTo(TravelAddress.class, addressIds[i]);
	}catch (SQLException sql) {
	}
      }
    }

    ProductBusiness.removeProductApplication(IWContext.getInstance(), supplierId);
    return product.getID();
  }

  public static Product getProduct(int productId) throws SQLException{
    Object obj = products.get(Integer.toString(productId));
    if (obj == null) {
      Product prod = new Product(productId);
      products.put(Integer.toString(productId), prod);
      //System.err.println("ProductBusiness : creating product : "+productId);
      return prod;
    }else {
      //System.err.println("ProductBusiness : found product : "+productId);
      return (Product) obj;
    }
  }

  public static void deleteProduct(Product product) throws SQLException {
    products.remove(Integer.toString(product.getID()));
    product.delete();
  }

  public static Product updateProduct(Product product) throws SQLException {
    products.remove(Integer.toString(product.getID()));
    product.update();
    return getProduct(product.getID());
  }

  /**
   * @deprecated
   */
  public static String getProductName(Product product) {
    return getProductName(product, getSelectedLocaleId(IWContext.getInstance()));
  }

  public static String getProductName(Product product, int localeId) {
    LocalizedText text = TextFinder.getLocalizedText(product, localeId);
    if (text == null) text = TextFinder.getLocalizedText(product, defaultLocaleId);
    String name = "";
    if (text != null) {
      name = text.getHeadline();
    }
    return name;
  }

  public static String getProductNameWithNumber(Product product) {
    return getProductNameWithNumber(product, true);
  }

  public static String getProductNameWithNumber(Product product, int localeID) {
    return getProductNameWithNumber(product, true, localeID);
  }

  public static String getProductNameWithNumber(Product product, boolean numberInFront) {
    return getProductNameWithNumber(product, numberInFront, -1);
  }

  public static String getProductNameWithNumber(Product product, boolean numberInFront, int localeID) {
    String returnString = "";

    String number = product.getNumber();
    String name = "";
    if (localeID == -1) {
      name =  getProductName(product);
    }else {
      name = getProductName(product, localeID);
    }

    if (numberInFront) {
      if (!number.equals("")) {
        returnString = number + " " + name;
      }else {
        returnString = name;
      }
    }else {
      if (!number.equals("")) {
        returnString = name + " " + number;
      }else {
        returnString = name;
      }
    }

    return returnString;
  }

  public static String getProductTeaser(Product product) {
    return getProductTeaser(product, IWContext.getInstance());
  }

  public static String getProductTeaser(Product product, IWContext iwc) {
    return getProductTeaser(product, getSelectedLocaleId(iwc));
  }

  public static String getProductTeaser(Product product, int localeId) {
    LocalizedText text = TextFinder.getLocalizedText(product, localeId);
    if (text == null) text = TextFinder.getLocalizedText(product, defaultLocaleId);
    String teaser = "";
    if (text != null) {
      teaser = text.getTitle();
      if (teaser == null) teaser = "";
    }
    return teaser;
  }

  public static String getProductDescription(Product product, IWContext iwc) {
    return getProductDescription(product, getSelectedLocaleId(iwc));
  }

  public static String getProductDescription(Product product) {
    return getProductDescription(product, IWContext.getInstance());
  }

  public static String getProductDescription(Product product, int localeId) {
    LocalizedText text = TextFinder.getLocalizedText(product, localeId);
    if (text == null) text = TextFinder.getLocalizedText(product, defaultLocaleId);
    String description = "";
    if (text != null) {
      description = text.getBody();
    }
    return description;
  }

  public static void setProductTeaser(Product product, String teaser) {
    IWContext iwc = IWContext.getInstance();
      setProductTeaser(product, getSelectedLocaleId(iwc), teaser);
  }

  public static void setProductTeaser(Product product, int localeId, String teaser) {
    LocalizedText locText = TextFinder.getLocalizedText(product,localeId);
    boolean newLocText = false;
    if ( locText == null ) {
      locText = new LocalizedText();
      newLocText = true;
    }

    locText.setTitle(teaser);

    if ( newLocText ) {
      locText.setLocaleId(localeId);
      try {
	locText.insert();
	locText.addTo(product);
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

  public static void setProductName(Product product, String name) {
    IWContext iwc = IWContext.getInstance();
    setProductName(product, getSelectedLocaleId(iwc), name);
  }

  public static void setProductDescription(Product product, String description) {
    IWContext iwc = IWContext.getInstance();
    setProductDescription(product, getSelectedLocaleId(iwc), description);
  }

  public static void setProductName(Product product, int localeId, String name) {
    LocalizedText locText = TextFinder.getLocalizedText(product,localeId);
    boolean newLocText = false;
    if ( locText == null ) {
      locText = new LocalizedText();
      newLocText = true;
    }

    locText.setHeadline(name);

    if ( newLocText ) {
      locText.setLocaleId(localeId);
      try {
	locText.insert();
	locText.addTo(product);
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

  public static void setProductDescription(Product product, int localeId, String description) {
    LocalizedText locText = TextFinder.getLocalizedText(product,localeId);
    boolean newLocText = false;
    if ( locText == null ) {
      locText = new LocalizedText();
      newLocText = true;
    }

    locText.setBody(description);

    if ( newLocText ) {
      locText.setLocaleId(localeId);
      try {
	locText.insert();
	locText.addTo(product);
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

  public static int getSelectedLocaleId(IWContext iwc) {
    String sLocaleId = iwc.getParameter(PARAMETER_LOCALE_DROP);
    Locale currentLocale = iwc.getCurrentLocale(),chosenLocale;

    int iLocaleId = -1;
    if(sLocaleId!= null){
      iLocaleId = Integer.parseInt(sLocaleId);
      chosenLocale = TextFinder.getLocale(iLocaleId);
    }
    else{
      chosenLocale = currentLocale;
      iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);
    }

    return iLocaleId;
  }

  public static DropdownMenu getLocaleDropDown(IWContext iwc) {
    int iLocaleId = getSelectedLocaleId(iwc);
    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(PARAMETER_LOCALE_DROP);
      localeDrop.setToSubmit();
      localeDrop.setSelectedElement(Integer.toString(iLocaleId));
    return localeDrop;
  }


  public static void removeProductApplication(IWContext iwc, int supplierId) {
    iwc.removeApplicationAttribute(productsApplication+supplierId);
  }

  public static List getProducts(IWContext iwc, int supplierId) {
    List temp = (List) iwc.getApplicationAttribute(productsApplication+supplierId);
    if (temp == null) {
      temp = getProducts(supplierId);
      iwc.setApplicationAttribute(productsApplication+supplierId, temp);
      return temp;
    }else {
      return temp;
    }
  }

  public static List getProducts(int supplierId) {
    //return getProducts(supplierId, null);
      List products = new Vector();;

      try {
	String pTable = Product.getProductEntityName();

	StringBuffer sqlQuery = new StringBuffer();
	  sqlQuery.append("SELECT * FROM ").append(pTable);
	  sqlQuery.append(" WHERE ");
	  sqlQuery.append(pTable).append(".").append(Product.getColumnNameIsValid()).append(" = 'Y'");
	  if (supplierId != -1)
	  sqlQuery.append(" AND ").append(pTable).append(".").append(Product.getColumnNameSupplierId()).append(" = ").append(supplierId);
	  sqlQuery.append(" order by ").append(Product.getColumnNameNumber());

	products = EntityFinder.getInstance().findAll(Product.class,sqlQuery.toString());
      }catch(IDOFinderException sql) {
	sql.printStackTrace(System.err);
      }

      return products;

  }


  public static List getProducts() {
    return getProducts(-1, null);
  }

  public static List getProducts(List productCategories) {
    List returner = new Vector();
    List temp;
    Product product;
    for (int i = 0; i < productCategories.size(); i++) {
      temp = getProducts((ICCategory) productCategories.get(i));
      for (int j = 0; j < temp.size(); j++) {
        product = (Product) temp.get(j);
        if (!returner.contains(product)) {
          returner.add(product);
        }
      }
    }

    return returner;
  }

  public static List getProducts(ICCategory category) {
    return getProducts(-1, category.getID(), null,null);
  }

  public static List getProducts(ProductCategory productCategory) {
    return getProducts((ICCategory) productCategory);
  }

  public static List getProducts(idegaTimestamp stamp) {
    return getProducts(-1, stamp);
  }

  public static List getProducts(idegaTimestamp fromStamp, idegaTimestamp toStamp) {
    return getProducts(-1, fromStamp, toStamp);
  }

  public static List getProducts(int supplierId, idegaTimestamp stamp) {
    if (stamp != null)
      return getProducts(supplierId, stamp, new idegaTimestamp(stamp));
    else
      return getProducts(supplierId, null, null);
  }

  public static List getProducts(int supplierId, idegaTimestamp from, idegaTimestamp to) {
    return getProducts(supplierId, -1, from, to);
  }

  public static List getProducts(int supplierId, int productCategoryId ,idegaTimestamp from, idegaTimestamp to) {
    Object obj = IWContext.getInstance().getApplicationAttribute(productsApplication+supplierId+productCategoryId+from+to);
    List products = null;
    if (obj != null) {
      products = (List) obj;
    }

    if (products == null)
      try {
	  /**
	   * @todo Oracle support...
	   */

	  Timeframe timeframe = (Timeframe) Timeframe.getStaticInstance(Timeframe.class);
	  Product product = (Product) Product.getStaticInstance(Product.class);
	  ProductCategory pCat = (ProductCategory) ProductCategory.getStaticInstance(ProductCategory.class);
	  Product prod = null;
	  //Service tService = (Service) Service.getStaticInstance(Service.class);

	  String middleTable = EntityControl.getManyToManyRelationShipTableName(Timeframe.class,Product.class);
	  String Ttable = Timeframe.getTimeframeTableName();
	  String Ptable = Product.getProductEntityName();
	  String catMiddle = EntityControl.getManyToManyRelationShipTableName(ProductCategory.class,Product.class);

	  StringBuffer timeframeSQL = new StringBuffer();
	    timeframeSQL.append("SELECT distinct "+Ptable+".* FROM "+Ptable);
	    if (from != null && to != null) {
	      timeframeSQL.append(", "+Ttable+", "+middleTable);
	    }
	    if (productCategoryId != -1) {
	      timeframeSQL.append(", "+catMiddle);
	    }
	    timeframeSQL.append(" WHERE ");
	    timeframeSQL.append(Ptable+"."+Product.getColumnNameIsValid()+" = 'Y'");
	    if (from != null && to != null) {
	      timeframeSQL.append(" AND ");
	      timeframeSQL.append(Ttable+"."+timeframe.getIDColumnName()+" = "+middleTable+"."+timeframe.getIDColumnName());
	      timeframeSQL.append(" AND ");
	      timeframeSQL.append(Ptable+"."+product.getIDColumnName()+" = "+middleTable+"."+product.getIDColumnName());
	    }

	    if (productCategoryId != -1) {
	      timeframeSQL.append(" AND ");
	      timeframeSQL.append(Ptable+"."+product.getIDColumnName()+" = "+catMiddle+"."+product.getIDColumnName());
	      timeframeSQL.append(" AND ");
	      timeframeSQL.append(catMiddle+"."+pCat.getIDColumnName() +" = "+productCategoryId);
	    }

	  // Hondla ef supplierId != -1
	  List tempProducts = new Vector();
	  if (supplierId != -1) tempProducts = getProducts(supplierId);
	  if (tempProducts.size() > 0) {
	    timeframeSQL.append(" AND ");
	    timeframeSQL.append(middleTable+"."+product.getIDColumnName()+" in (");
	    for (int i = 0; i < tempProducts.size(); i++) {
	      prod = (Product) tempProducts.get(i);
	      if (i == 0) {
		timeframeSQL.append(prod.getID());
	      }else {
		timeframeSQL.append(","+prod.getID());
	      }
	    }
	    timeframeSQL.append(")");
	  }

	  if (from != null && to != null) {
	    timeframeSQL.append(" AND ");
	    timeframeSQL.append("(");
	    timeframeSQL.append(" ("+Timeframe.getTimeframeFromColumnName()+" <= '"+from.toSQLDateString()+"' AND "+Timeframe.getTimeframeToColumnName()+" >= '"+from.toSQLDateString()+"')");
	    timeframeSQL.append(" OR ");
	    timeframeSQL.append(" ("+Timeframe.getTimeframeFromColumnName()+" <= '"+to.toSQLDateString()+"' AND "+Timeframe.getTimeframeToColumnName()+" >= '"+to.toSQLDateString()+"')");
	    timeframeSQL.append(" OR ");
	    timeframeSQL.append(" ("+Timeframe.getTimeframeFromColumnName()+" >= '"+from.toSQLDateString()+"' AND "+Timeframe.getTimeframeToColumnName()+" <= '"+to.toSQLDateString()+"')");
	    timeframeSQL.append(")");
	  }

	  if (from != null && to != null) {
	    timeframeSQL.append(" ORDER BY "+Timeframe.getTimeframeFromColumnName());
	  }

	  //System.err.println(timeframeSQL.toString());
	  products = EntityFinder.getInstance().findAll(Product.class,timeframeSQL.toString());


      }catch(IDOFinderException  ido) {
	ido.printStackTrace(System.err);
      }

    return products;
  }


  public static Timeframe getTimeframe(Product product, idegaTimestamp stamp) {
    Timeframe returner = null;
    try {
      Timeframe[] frames = product.getTimeframes();
      for (int i = 0; i < frames.length; i++) {
	returner = frames[i];
	if (stamp.isInTimeframe( new idegaTimestamp(returner.getFrom()) , new idegaTimestamp(returner.getTo()), stamp, returner.getIfYearly() )) {
	  return returner;
	}
      }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return returner;
  }

  public static idegaTimestamp getDepartureTime(Product product) throws SQLException {
    return getDepartureTime(product.getID());
 }

  public static idegaTimestamp getDepartureTime(int productId) throws SQLException {
    Service service = new Service(productId);
    idegaTimestamp tempStamp = new idegaTimestamp(service.getDepartureTime());
    return tempStamp;
  }

  public static Address[] getDepartureAddressesOld(Product product) throws SQLException {
    return (Address[]) (product.findRelated( (Address) Address.getStaticInstance(Address.class), Address.getColumnNameAddressTypeId(), Integer.toString(AddressType.getId(uniqueDepartureAddressType))));
  }

  /**
   * @deprecated
   */
  public static TravelAddress[] getDepartureAddresses(Product product) throws SQLException {
    try {
      List list = getDepartureAddresses(product, true);
      return (TravelAddress[]) list.toArray(new TravelAddress[]{});
    }catch (IDOFinderException ido) {
      throw new SQLException(ido.getMessage());
    }
  }

  public static List getDepartureAddresses(Product product, boolean ordered) throws IDOFinderException  {
    List list = EntityFinder.getInstance().findRelated(product, TravelAddress.class, TravelAddress.getColumnNameAddressTypeId(), Integer.toString(TravelAddress.ADDRESS_TYPE_DEPARTURE) );
    if (ordered) {
      Collections.sort(list, new TravelAddressComparator(TravelAddressComparator.TIME));
    }
    return list;
  }

  public static TravelAddress getDepartureAddress(Product product) throws SQLException{
      TravelAddress[] tempAddresses = getDepartureAddresses(product);
      if (tempAddresses.length > 0) {
	return new TravelAddress(tempAddresses[0].getID());
      }else {
	return null;
      }
  }

  public static Address[] getArrivalAddresses(Product product) throws SQLException {
    return (Address[]) (product.findRelated( (Address) Address.getStaticInstance(Address.class), Address.getColumnNameAddressTypeId(), Integer.toString(AddressType.getId(uniqueArrivalAddressType))));
  }

  public static Address getArrivalAddress(Product product) throws SQLException{
    Address[] tempAddresses = getArrivalAddresses(product);
    if (tempAddresses.length > 0) {
      return new Address(tempAddresses[0].getID());
    }else {
      return null;
    }
  }


  public static DropdownMenu getDropdownMenuWithProducts(IWContext iwc, int supplierId) {
    List list = getProducts(iwc, supplierId);
    DropdownMenu menu = new DropdownMenu(((Product)Product.getStaticInstance(Product.class)).getEntityName());
    Product product;
    if (list != null && list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {
	product = (Product) list.get(i);
	menu.addMenuElement(product.getID(), getProductNameWithNumber(product));
      }
    }
    return menu;
  }

  /**
   * @deprecated
   */
  public static List getProductCategories() throws IDOFinderException{
    return EntityFinder.getInstance().findAllOrdered(ProductCategory.class, ProductCategory.getColumnName());
  }

  public static List getProductCategories(Product product) throws IDOFinderException{
    return EntityFinder.getInstance().findRelated(product, ProductCategory.class);
  }

}