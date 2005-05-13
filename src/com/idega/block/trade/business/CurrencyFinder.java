package com.idega.block.trade.business;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.ejb.FinderException;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.data.CurrencyHome;
import com.idega.block.trade.data.CurrencyValues;
import com.idega.block.trade.data.CurrencyValuesHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;

public class CurrencyFinder {

  public static Currency getCurrency(int currencyID, String datasource) {
    try {
		CurrencyHome cHome = (CurrencyHome) IDOLookup.getHome(Currency.class);
		cHome.setDatasource(datasource);
		return cHome.findByPrimaryKey(currencyID);
    }
	catch (FinderException e) {
		e.printStackTrace();
	}
	catch (IDOLookupException e) {
		e.printStackTrace();
	}
    return null;
  }

  public static CurrencyValues getCurrencyValue(int currencyID, String datasource) {
    try {
		CurrencyValuesHome cHome = (CurrencyValuesHome) IDOLookup.getHome(CurrencyValues.class);
		cHome.setDatasource(datasource);
		return cHome.findByPrimaryKey(new Integer(currencyID));
    }
	catch (IDOLookupException e) {
		e.printStackTrace();
	}
	catch (FinderException e) {
		e.printStackTrace();
	}
    return null;
  }

  public static Collection getCurrencies(String datasource) {
    try {
		CurrencyHome cHome = (CurrencyHome) IDOLookup.getHome(Currency.class);
		cHome.setDatasource(datasource);
		return cHome.findAll();
    }
	catch (FinderException e) {
		e.printStackTrace();
	}
	catch (IDOLookupException e) {
		e.printStackTrace();
	}
	
	return null;
  }

  public static Collection getCurrencyValues(String datasource) {
    try {
		CurrencyValuesHome cHome = (CurrencyValuesHome) IDOLookup.getHome(CurrencyValues.class);
		cHome.setDatasource(datasource);
		return cHome.findAll();
    }
	catch (FinderException e) {
		e.printStackTrace();
	}
	catch (IDOLookupException e) {
		e.printStackTrace();
	}
	
	return null;
  }

  public static HashMap getCurrenciesMap(String datasource) {
    try {
      Collection currencies = getCurrencies(datasource);
      if ( currencies != null && !currencies.isEmpty() ) {
        HashMap map = new HashMap();
		Iterator iter = currencies.iterator();
		Currency currency;
		while (iter.hasNext()) {
			currency = (Currency) iter.next();
          map.put(currency.getCurrencyAbbreviation(),currency);
        }
        return map;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  public static HashMap getCurrencyValuesMap(String datasource) {
    try {
      Collection currencies = getCurrencyValues(datasource);
      if ( currencies != null && !currencies.isEmpty() ) {
        HashMap map = new HashMap();
		Iterator iter = currencies.iterator();
		CurrencyValues value;
		while (iter.hasNext()) {
			value = (CurrencyValues) iter.next();
          map.put(new Integer(value.getPrimaryKey().toString()),value);
        }
        return map;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }
}
