package com.idega.block.trade.stockroom.business;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.text.data.*;
import java.util.Locale;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.block.text.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.data.*;
import java.sql.SQLException;
import java.util.List;
import com.idega.util.*;

import is.idega.idegaweb.travel.data.Timeframe;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductBusiness {

  public static String PARAMETER_LOCALE_DROP = "product_locale_drop";
  public static int defaultLocaleId = 1;

  public ProductBusiness() {
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

  public static List getProducts(int supplierId) {
      List products = null;

      try {
        String pTable = Product.getProductEntityName();

        StringBuffer sqlQuery = new StringBuffer();
          sqlQuery.append("SELECT * FROM "+pTable);
          sqlQuery.append(" WHERE ");
          sqlQuery.append(pTable+"."+Product.getColumnNameIsValid()+" = 'Y'");
          if (supplierId != -1)
          sqlQuery.append(" AND "+pTable+"."+Product.getColumnNameSupplierId()+" = "+supplierId);

        products =EntityFinder.findAll(Product.getStaticInstance(Product.class),sqlQuery.toString());
      }catch(SQLException sql) {
        sql.printStackTrace(System.err);
      }

      return products;
  }


  public static List getProducts(int supplierId, idegaTimestamp stamp) {
    List products = null;

      try {
          /**
           * @todo Oracle support...
           */
          List tempProducts = getProducts(supplierId);

          if (tempProducts.size() > 0) {
            idegaCalendar calendar = new idegaCalendar();

            int dayOfWeek = calendar.getDayOfWeek(stamp.getYear(),stamp.getMonth(),stamp.getDay());
            Timeframe timeframe = (Timeframe) Timeframe.getStaticInstance(Timeframe.class);
            Supplier supplier = (Supplier) Supplier.getStaticInstance(Supplier.class);
            Product producter = (Product) Product.getStaticInstance(Product.class);

            String middleTable = EntityControl.getManyToManyRelationShipTableName(Timeframe.class,Product.class);
            String Ttable = Timeframe.getTimeframeTableName();
            String Ptable = Product.getProductEntityName();
            String Stable = Supplier.getSupplierTableName();

            StringBuffer timeframeSQL = new StringBuffer();
              timeframeSQL.append("SELECT "+Ptable+".* FROM  "+Ttable+", "+Ptable+","+middleTable);
              timeframeSQL.append(" WHERE ");
              timeframeSQL.append(Ttable+"."+timeframe.getIDColumnName()+" = "+middleTable+"."+timeframe.getIDColumnName());
              timeframeSQL.append(" AND ");
              timeframeSQL.append(Ptable+"."+producter.getIDColumnName()+" = "+middleTable+"."+producter.getIDColumnName());
              timeframeSQL.append(" AND ");
              timeframeSQL.append(Ptable+"."+supplier.getIDColumnName()+" = "+supplierId);
              timeframeSQL.append(" AND ");
              timeframeSQL.append(" ( ");
              timeframeSQL.append(Ttable+"."+timeframe.getYearlyColumnName()+" = 'N'");
              timeframeSQL.append(" AND ");
              timeframeSQL.append(Timeframe.getTimeframeFromColumnName()+" <= '"+stamp.toSQLDateString()+"'");
              timeframeSQL.append(" AND ");
              timeframeSQL.append(Timeframe.getTimeframeToColumnName()+" >= '"+stamp.toSQLDateString()+"'");
              timeframeSQL.append(" ) ");
              timeframeSQL.append(" OR ");
              timeframeSQL.append(" ( ");
              timeframeSQL.append(Ttable+"."+timeframe.getYearlyColumnName()+" = 'Y'");
              timeframeSQL.append(" AND ");
              timeframeSQL.append(Timeframe.getTimeframeFromColumnName()+" containing '-"+stamp.getMonth()+"-"+stamp.getDay()+"'");
              timeframeSQL.append(" AND ");
              timeframeSQL.append(Timeframe.getTimeframeToColumnName()+" containing '-"+stamp.getMonth()+"-"+stamp.getDay()+"'");
              timeframeSQL.append(" ) ");
              timeframeSQL.append(" AND ");
              timeframeSQL.append(Ptable+"."+Product.getColumnNameIsValid()+" = 'Y'");
              timeframeSQL.append(" ORDER BY "+Ttable+"."+Timeframe.getTimeframeFromColumnName());
//              timeframeSQL.append(" ORDER BY "+Ttable+"."+Timeframe.getTimeframeFromColumnName()+","+Ptable+"."+Product.getColumnNameProductName());

//            System.err.println(timeframeSQL.toString());
            products = EntityFinder.findAll(Product.getStaticInstance(Product.class),timeframeSQL.toString());

          }

      }catch(SQLException sql) {
        sql.printStackTrace(System.err);
      }

    return products;
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

}