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
  public static String uniqueDepartureAddressType = "TB_TRIP_DEPARTURE_ADDRESS";
  public static String uniqueArrivalAddressType = "TB_TRIP_ARRIVAL_ADDRESS";
  public static String uniqueHotelPickupAddressType = "TB_HOTEL_PICKUP_ADDRESS";

  public static String PARAMETER_LOCALE_DROP = "product_locale_drop";
  public static int defaultLocaleId = 1;

  private static String productsApplication = "productsApplication_";
  public static HashMap products = new HashMap();

  public ProductBusiness() {
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

  public static Product updateProduct(Product product) throws SQLException {
    products.remove(Integer.toString(product.getID()));
    product.update();
    return getProduct(product.getID());
  }

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

  public static String getProductNameWithNumber(Product product, boolean numberInFrom) {
    if (numberInFrom) {
      return product.getNumber() + Text.NON_BREAKING_SPACE + getProductName(product);
    }else {
      return getProductName(product) + Text.NON_BREAKING_SPACE + product.getNumber();
    }
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

  /**
   * @deprecated
   */
  public static List getProducts(int supplierId) {
      List products = null;

      try {
        String pTable = Product.getProductEntityName();

        StringBuffer sqlQuery = new StringBuffer();
          sqlQuery.append("SELECT * FROM ").append(pTable);
          sqlQuery.append(" WHERE ");
          sqlQuery.append(pTable).append(".").append(Product.getColumnNameIsValid()).append(" = 'Y'");
          if (supplierId != -1)
          sqlQuery.append(" AND ").append(pTable).append(".").append(Product.getColumnNameSupplierId()).append(" = ").append(supplierId);
          sqlQuery.append(" order by ").append(Product.getColumnNameNumber());

        products =EntityFinder.findAll(Product.getStaticInstance(Product.class),sqlQuery.toString());
      }catch(SQLException sql) {
        sql.printStackTrace(System.err);
      }

      return products;
  }


  public static List getProducts(int supplierId, idegaTimestamp stamp) {
    return getProducts(supplierId, stamp, new idegaTimestamp(stamp));
  }

  public static List getProducts(int supplierId, idegaTimestamp from, idegaTimestamp to) {
    List products = null;
      try {
          /**
           * @todo Oracle support...
           */
          List tempProducts = getProducts(supplierId);
          if (tempProducts.size() > 0) {

              Timeframe timeframe = (Timeframe) Timeframe.getStaticInstance(Timeframe.class);
              Product product = (Product) Product.getStaticInstance(Product.class);
              Product prod = null;
              //Service tService = (Service) Service.getStaticInstance(Service.class);

              String middleTable = EntityControl.getManyToManyRelationShipTableName(Timeframe.class,Product.class);
              String Ttable = Timeframe.getTimeframeTableName();
              String Ptable = Product.getProductEntityName();


              StringBuffer timeframeSQL = new StringBuffer();
                timeframeSQL.append("SELECT "+Ptable+".* FROM "+Ptable+", "+Ttable+", "+middleTable);
                timeframeSQL.append(" WHERE ");
                timeframeSQL.append(Ttable+"."+timeframe.getIDColumnName()+" = "+middleTable+"."+timeframe.getIDColumnName());
                timeframeSQL.append(" AND ");
                timeframeSQL.append(Ptable+"."+product.getIDColumnName()+" = "+middleTable+"."+product.getIDColumnName());
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
                timeframeSQL.append(" AND ");
                timeframeSQL.append("(");
                timeframeSQL.append(" ("+Timeframe.getTimeframeFromColumnName()+" <= '"+from.toSQLDateString()+"' AND "+Timeframe.getTimeframeToColumnName()+" >= '"+from.toSQLDateString()+"')");
                timeframeSQL.append(" OR ");
                timeframeSQL.append(" ("+Timeframe.getTimeframeFromColumnName()+" <= '"+to.toSQLDateString()+"' AND "+Timeframe.getTimeframeToColumnName()+" >= '"+to.toSQLDateString()+"')");
                timeframeSQL.append(" OR ");
                timeframeSQL.append(" ("+Timeframe.getTimeframeFromColumnName()+" >= '"+from.toSQLDateString()+"' AND "+Timeframe.getTimeframeToColumnName()+" <= '"+to.toSQLDateString()+"')");
                timeframeSQL.append(")");
                timeframeSQL.append(" AND ");
                timeframeSQL.append(Ptable+"."+Product.getColumnNameIsValid()+" = 'Y'");
                timeframeSQL.append(" ORDER BY "+Timeframe.getTimeframeFromColumnName());

              products = EntityFinder.findAll(Product.getStaticInstance(Product.class),timeframeSQL.toString());
          }


      }catch(SQLException sql) {
        sql.printStackTrace(System.err);
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

  public static TravelAddress[] getDepartureAddresses(Product product) throws SQLException {
    TravelAddress[] tempAddresses = (TravelAddress[]) (product.findRelated( (TravelAddress) TravelAddress.getStaticInstance(TravelAddress.class), TravelAddress.getColumnNameAddressTypeId(), Integer.toString(TravelAddress.ADDRESS_TYPE_DEPARTURE)));
    return tempAddresses;
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

}