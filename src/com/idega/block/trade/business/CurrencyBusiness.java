package com.idega.block.trade.business;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import java.sql.SQLException;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.Collections;
import com.idega.data.EntityBulkUpdater;
import com.idega.io.FileGrabber;
import com.idega.util.idegaTimestamp;
import com.idega.util.FileUtil;
import com.idega.util.text.TextSoap;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWBundle;
import com.idega.block.trade.data.*;

public class CurrencyBusiness {

public static final String filterSource = "currencyfilter.xml";
public static final String startTag1 = new String("<findandcenterbegin>");
public static final String startTag2 = new String("</findandcenterbegin>");
public static final String endTag1 = new String("<findandcenterend>");
public static final String endTag2 = new String("</findandcenterend>");
public static final String currencyBegins1 = new String("<currencybegins>");
public static final String currencyBegins2 = new String("</currencybegins>");
public static final String currencyEnds1 = new String("<currencyends>");
public static final String currencyEnds2 = new String("</currencyends>");
public static final String theSiteURLB = new String("<siteurl>");
public static final String theSiteURLE = new String("</siteurl>");
public static final String currencyB = new String("<currency>");
public static final String currencyE = new String("</currency>");

public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
public static final String IW_CURRENCY_MAP = "iw_currency_map";
public static final String IW_DEFAULT_CURRENCY = "iw_currency";

public static String fileString;
public static HashMap currencyMap;
public static String defaultCurrency = CurrencyHolder.ICELANDIC_KRONA;

  public static void getCurrencyMap(IWBundle bundle) {
    idegaTimestamp stamp = new idegaTimestamp();
    FileGrabber grabber = new FileGrabber();
    if ( fileString == null ) {
      try {
	fileString = FileUtil.getStringFromFile(bundle.getResourcesRealPath()+FileUtil.getFileSeparator()+filterSource);
      }
      catch (Exception e) {
	e.printStackTrace(System.err);
      }
    }

    if ( fileString != null ) {
      try {
	String site = TextSoap.FindAllBetween(fileString,theSiteURLB,theSiteURLE).firstElement().toString();
	String beginTag = TextSoap.FindAllBetween(fileString,startTag1,startTag2).firstElement().toString();
	String endTag = TextSoap.FindAllBetween(fileString,endTag1,endTag2).firstElement().toString();
	String currB = TextSoap.FindAllBetween(fileString,currencyBegins1,currencyBegins2).firstElement().toString();
	String currE = TextSoap.FindAllBetween(fileString,currencyEnds1,currencyEnds2).firstElement().toString();
	String currency = TextSoap.FindAllBetween(fileString,currencyB,currencyE).firstElement().toString();
	if ( currency != null ) {
	  bundle.getApplication().setAttribute(IW_CURRENCY_MAP,currency);
	  defaultCurrency = currency;
	}

	CurrencyHolder holder = null;
	String pageString = grabber.getTheURL(site);

	if ( pageString != null ) {
	  if ( currencyMap == null )
	    currencyMap = new HashMap();
	  Vector pageVector = TextSoap.FindAllBetween(pageString,beginTag,endTag);
	  Iterator iter = pageVector.iterator();
	  int a = 1;

	  while (iter.hasNext()) {
	    a = 1;
	    holder = new CurrencyHolder();
	    Vector currencyVector = TextSoap.FindAllBetween((String)iter.next(),currB,currE);
	    Iterator iter2 = currencyVector.iterator();
	    while (iter2.hasNext()) {
	      String vectorString = TextSoap.findAndReplace((String)iter2.next(),',','.');
	      if ( a == 1 )
		holder.setCurrencyName(vectorString);
	      else if ( a == 2 )
		holder.setBuyValue(Float.parseFloat(vectorString));
	      else if ( a == 3 )
		holder.setSellValue(Float.parseFloat(vectorString));
	      else if ( a == 4 )
		holder.setMiddleValue(Float.parseFloat(vectorString));
	      a++;
	    }
	    holder.setTimestamp(stamp);
	    currencyMap.put(holder.getCurrencyName(),holder);
	  }
	  saveCurrencyValuesToDatabase();
	}
	else {
	  System.err.println("Error: No currency information found at "+TextSoap.FindAllBetween(fileString,theSiteURLB,theSiteURLE).firstElement().toString());
	  getValuesFromDatabase();
	}
      }
      catch (Exception e) {
	e.printStackTrace(System.err);
      }
    }
    else {
      System.err.print("Error: currencyfilter.xml not found at "+bundle.getResourcesRealPath()+FileUtil.getFileSeparator()+filterSource);
      getValuesFromDatabase();
    }

    if ( getCurrencyHolder(defaultCurrency) == null && currencyMap != null ) {
      CurrencyHolder holder = new CurrencyHolder();
	holder.setCurrencyName(defaultCurrency);
	holder.setBuyValue(1);
	holder.setSellValue(1);
	holder.setMiddleValue(1);
      currencyMap.put(holder.getCurrencyName(),holder);
    }
  }

  public static CurrencyHolder getCurrencyHolder(String currencyName) {
    if ( currencyMap != null ) {
      CurrencyHolder holder = (CurrencyHolder) currencyMap.get(currencyName);
      if ( holder != null )
	return holder;
      return null;
    }
    return null;
  }

  public static float convertCurrency(String fromCurrency,String toCurrency,float amount) {
    if ( fromCurrency != defaultCurrency ) {
      CurrencyHolder fromHolder = getCurrencyHolder(fromCurrency);
      if ( fromHolder != null ) {
	amount = amount * fromHolder.getBuyValue();
      }
    }
    if ( toCurrency != defaultCurrency ) {
      CurrencyHolder toHolder = getCurrencyHolder(toCurrency);
      if ( toHolder != null ) {
	amount = amount / toHolder.getSellValue();
      }
    }
    return amount;
  }

  public static void saveCurrencyValuesToDatabase() {
    if ( currencyMap != null ) {
      EntityBulkUpdater bulk = new EntityBulkUpdater();
      idegaTimestamp stamp = new idegaTimestamp();

      HashMap currencies = saveCurrenciesToDatabase();

      CurrencyHolder holder = null;
      Currency currency = null;
      CurrencyValues values = null;
      boolean update;

      Iterator iter = currencyMap.keySet().iterator();
      while (iter.hasNext()) {
	update = true;
	holder = (CurrencyHolder) currencyMap.get((String)iter.next());
	currency = (Currency) currencies.get(holder.getCurrencyName());
	values = CurrencyFinder.getCurrencyValue(currency.getID());
	if ( values == null ) {
	  update = false;
	  values = new CurrencyValues();
	  values.setID(currency.getID());
	}
	values.setBuyValue(holder.getBuyValue());
	values.setSellValue(holder.getSellValue());
	values.setMiddleValue(holder.getMiddleValue());
	values.setCurrencyDate(stamp.getTimestamp());

	if ( update )
	  bulk.add(values,EntityBulkUpdater.update);
	else
	   bulk.add(values,EntityBulkUpdater.insert);
      }

      try {
	bulk.execute();
      }
      catch (Exception e) {
	e.printStackTrace(System.err);
      }
    }
  }

  public static HashMap saveCurrenciesToDatabase() {
    EntityBulkUpdater bulk = new EntityBulkUpdater();
    Currency currency = null;
    boolean execute = false;

    Iterator iter = currencyMap.keySet().iterator();
    HashMap map = getValuesFromDB();
    while (iter.hasNext()) {
      CurrencyHolder holder = (CurrencyHolder) currencyMap.get((String)iter.next());
      if ( holder != null && !map.containsKey(holder.getCurrencyName()) ) {
	currency = new Currency();
	currency.setCurrencyAbbreviation(holder.getCurrencyName());
	currency.setCurrencyName(holder.getCurrencyName());
	bulk.add(currency,bulk.insert);
	execute = true;
      }
    }

    if ( execute ) {
      try {
	bulk.execute();
      }
      catch (Exception e) {
	e.printStackTrace(System.err);
      }
    }

    return CurrencyFinder.getCurrenciesMap();
  }

  public static void getValuesFromDatabase() {
    HashMap map = getValuesFromDB();
    currencyMap = map;
  }

  public static HashMap getValuesFromDB() {
    HashMap map = new HashMap();

    CurrencyHolder holder = null;
    Currency currency = null;
    CurrencyValues value = null;

    HashMap currencies = CurrencyFinder.getCurrenciesMap();
    HashMap values = CurrencyFinder.getCurrencyValuesMap();
    if ( currencies != null && values != null ) {
      Iterator iter = currencies.keySet().iterator();
      while (iter.hasNext()) {
	currency = (Currency) currencies.get(iter.next());
	value = (CurrencyValues) values.get(new Integer(currency.getID()));
	if ( currency != null && value != null ) {
	  holder = new CurrencyHolder();
	  holder.setBuyValue(value.getBuyValue());
	  holder.setCurrencyName(currency.getCurrencyAbbreviation());
	  holder.setMiddleValue(value.getMiddleValue());
	  holder.setSellValue(value.getSellValue());
	  map.put(holder.getCurrencyName(),holder);
	}
      }
    }

    return map;
  }

  public static List getCurrencyList() {
    Vector vector = new Vector();
    if ( currencyMap != null ) {
      Iterator iter = currencyMap.keySet().iterator();
      while (iter.hasNext()) {
	vector.add((CurrencyHolder)currencyMap.get((String)iter.next()));
      }
      Collections.sort(vector,new CurrencyComparator());

      return vector;
    }
    return null;
  }

}