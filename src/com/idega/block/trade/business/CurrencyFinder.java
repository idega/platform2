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

import com.idega.block.trade.data.Currency;
import com.idega.block.trade.data.CurrencyValues;

public class CurrencyFinder {

  public static Currency getCurrency(int currencyID) {
    try {
      return ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(currencyID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static CurrencyValues getCurrencyValue(int currencyID) {
    try {
      return ((com.idega.block.trade.data.CurrencyValuesHome)com.idega.data.IDOLookup.getHomeLegacy(CurrencyValues.class)).findByPrimaryKeyLegacy(currencyID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static Currency[] getCurrencies() {
    try {
      return (Currency[]) com.idega.block.trade.data.CurrencyBMPBean.getStaticInstance(Currency.class).findAll();
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static CurrencyValues[] getCurrencyValues() {
    try {
      return (CurrencyValues[]) com.idega.block.trade.data.CurrencyValuesBMPBean.getStaticInstance(CurrencyValues.class).findAll();
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
