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
import java.util.HashMap;
import com.idega.data.EntityFinder;
import com.idega.block.trade.data.*;

public class CurrencyFinder {

  public static Currency getCurrency(int currencyID) {
    try {
      return new Currency(currencyID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static CurrencyValues getCurrencyValue(int currencyID) {
    try {
      return new CurrencyValues(currencyID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static Currency[] getCurrencies() {
    try {
      return (Currency[]) Currency.getStaticInstance(Currency.class).findAll();
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static CurrencyValues[] getCurrencyValues() {
    try {
      return (CurrencyValues[]) CurrencyValues.getStaticInstance(CurrencyValues.class).findAll();
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static HashMap getCurrenciesMap() {
    try {
      Currency[] currencies = getCurrencies();
      if ( currencies != null && currencies.length > 0 ) {
        HashMap map = new HashMap();
        for (int i = 0; i < currencies.length; i++) {
          map.put(currencies[i].getCurrencyAbbreviation(),currencies[i]);
        }
        return map;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  public static HashMap getCurrencyValuesMap() {
    try {
      CurrencyValues[] currencies = getCurrencyValues();
      if ( currencies != null && currencies.length > 0 ) {
        HashMap map = new HashMap();
        for (int i = 0; i < currencies.length; i++) {
          map.put(new Integer(currencies[i].getID()),currencies[i]);
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