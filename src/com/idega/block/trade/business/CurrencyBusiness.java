package com.idega.block.trade.business;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.Collections;
import com.idega.data.EntityBulkUpdater;
import com.idega.io.FileGrabber;
import com.idega.util.IWTimestamp;
import com.idega.util.FileUtil;
import com.idega.util.text.TextSoap;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;
import com.idega.xml.XMLParser;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWBundle;
import com.idega.block.trade.data.*;

public class CurrencyBusiness {

	public static final String filterSource = "currencyfilter.xml";
	public static final String buyValue = new String("<buyvalue>");
	public static final String buyEndValue = new String("</buyvalue>");
	public static final String sellValue = new String("<sellvalue>");
	public static final String sellEndValue = new String("</sellvalue>");
	public static final String middleValue = new String("<middlevalue>");
	public static final String middleEndValue = new String("</middlevalue>");
	public static final String siteURL = new String("<siteurl>");
	public static final String siteEndURL = new String("</siteurl>");
	public static final String currency = new String("<currency>");
	public static final String currencyEnd = new String("</currency>");
	public static final String currencyDefault = new String("<default>");
	public static final String currencyDefaultEnd = new String("</default>");

	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
	public static final String IW_CURRENCY_MAP = "iw_currency_map";
	public static final String IW_DEFAULT_CURRENCY = "iw_currency";

	public static File file;
	public static String fileString;
	public static HashMap currencyMap;
	public static String defaultCurrency = CurrencyHolder.ICELANDIC_KRONA;

	public static void getCurrencyMap(IWBundle bundle) {
		IWTimestamp stamp = new IWTimestamp();
		file = null;
		
		try {
			String path = bundle.getResourcesRealPath() + FileUtil.getFileSeparator() + filterSource;
			fileString = FileUtil.getStringFromFile(path);
		}
		catch (IOException e) {
			fileString = null;
		}
		
		String url = null;
		String currency_name = null;
		String buy_value = null;
		String sell_value = null;
		String middle_value = null;
		
		if ( fileString != null ) {
			try {
				url = (String) TextSoap.FindAllBetween(fileString, siteURL, siteEndURL).firstElement();
				currency_name = (String) TextSoap.FindAllBetween(fileString,currency,currencyEnd).firstElement();	
				buy_value = (String) TextSoap.FindAllBetween(fileString,buyValue,buyEndValue).firstElement();	
				sell_value = (String) TextSoap.FindAllBetween(fileString,sellValue,sellEndValue).firstElement();	
				middle_value = (String) TextSoap.FindAllBetween(fileString,middleValue,middleEndValue).firstElement();	
				defaultCurrency = (String) TextSoap.FindAllBetween(fileString,currencyDefault,currencyDefaultEnd).firstElement();	
			}
			catch (Exception e) {
			}
		}

		XMLParser parser = new XMLParser();
		XMLElement rootElement = null;
		try {
			rootElement = parser.parse(url).getRootElement();
		}
		catch (XMLException e) {
			rootElement = null;
		}

		CurrencyHolder holder = null;

		if (rootElement != null) {
			if (currencyMap == null)
				currencyMap = new HashMap();

			List currencies = rootElement.getChildren();
			Iterator iter = currencies.iterator();
			int a = 1;

			while (iter.hasNext()) {
				XMLElement childElement = (XMLElement) iter.next();

				holder = new CurrencyHolder();
				Iterator iter2 = childElement.getChildren().iterator();
				while (iter2.hasNext()) {
					XMLElement currencyValues = (XMLElement) iter2.next();

					if (currencyValues.getName().equalsIgnoreCase(currency_name))
						holder.setCurrencyName(currencyValues.getText());
					else if (currencyValues.getName().equalsIgnoreCase(buy_value))
						holder.setBuyValue(Float.parseFloat(currencyValues.getText()));
					else if (currencyValues.getName().equalsIgnoreCase(sell_value))
						holder.setSellValue(Float.parseFloat(currencyValues.getText()));
					else if (currencyValues.getName().equalsIgnoreCase(middle_value))
						holder.setMiddleValue(Float.parseFloat(currencyValues.getText()));
					a++;
				}
				holder.setTimestamp(stamp);
				currencyMap.put(holder.getCurrencyName(), holder);
			}
			addDefaultCurrency();
			saveCurrencyValuesToDatabase();
		}
		else {
			getValuesFromDatabase();
		}

		if (getCurrencyHolder(defaultCurrency) == null && currencyMap != null) {
			CurrencyHolder defaultHolder = new CurrencyHolder();
			defaultHolder.setCurrencyName(defaultCurrency);
			defaultHolder.setBuyValue(1);
			defaultHolder.setSellValue(1);
			defaultHolder.setMiddleValue(1);
			currencyMap.put(holder.getCurrencyName(), defaultHolder);
		}

		System.out.println("Default currency: " + defaultCurrency);
	}

	public static CurrencyHolder getCurrencyHolder(String currencyName) {
		if (currencyMap != null) {
			CurrencyHolder holder = (CurrencyHolder) currencyMap.get(currencyName);
			if (holder != null)
				return holder;
			return null;
		}
		return null;
	}

	public static float convertCurrency(String fromCurrency, String toCurrency, float amount) {
		if (fromCurrency != defaultCurrency) {
			CurrencyHolder fromHolder = getCurrencyHolder(fromCurrency);
			if (fromHolder != null) {
				amount = amount * fromHolder.getBuyValue();
			}
		}
		if (toCurrency != defaultCurrency) {
			CurrencyHolder toHolder = getCurrencyHolder(toCurrency);
			if (toHolder != null) {
				amount = amount / toHolder.getSellValue();
			}
		}
		return amount;
	}

	public static void addDefaultCurrency() {
		CurrencyHolder holder = new CurrencyHolder();
		holder.setCurrencyName(defaultCurrency);
		holder.setBuyValue(1);
		holder.setSellValue(1);
		holder.setMiddleValue(1);
		currencyMap.put(holder.getCurrencyName(), holder);
	}

	public static void saveCurrencyValuesToDatabase() {
		if (currencyMap != null) {
			EntityBulkUpdater bulk = new EntityBulkUpdater();
			IWTimestamp stamp = new IWTimestamp();

			HashMap currencies = saveCurrenciesToDatabase();

			CurrencyHolder holder = null;
			Currency currency = null;
			CurrencyValues values = null;
			boolean update;

			Iterator iter = currencyMap.keySet().iterator();
			while (iter.hasNext()) {
				update = true;
				holder = (CurrencyHolder) currencyMap.get((String) iter.next());
				currency = (Currency) currencies.get(holder.getCurrencyName());
				values = CurrencyFinder.getCurrencyValue(currency.getID());
				if (values == null) {
					update = false;
					values = ((com.idega.block.trade.data.CurrencyValuesHome) com.idega.data.IDOLookup.getHomeLegacy(CurrencyValues.class)).createLegacy();
					values.setID(currency.getID());
				}
				values.setBuyValue(holder.getBuyValue());
				values.setSellValue(holder.getSellValue());
				values.setMiddleValue(holder.getMiddleValue());
				values.setCurrencyDate(stamp.getTimestamp());
				holder.setCurrencyID(currency.getID());
				currencyMap.put(holder.getCurrencyName(), holder);

				if (update)
					bulk.add(values, EntityBulkUpdater.update);
				else
					bulk.add(values, EntityBulkUpdater.insert);
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
			CurrencyHolder holder = (CurrencyHolder) currencyMap.get((String) iter.next());
			if (holder != null && !map.containsKey(holder.getCurrencyName())) {
				currency = ((com.idega.block.trade.data.CurrencyHome) com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).createLegacy();
				currency.setCurrencyAbbreviation(holder.getCurrencyName());
				currency.setCurrencyName(holder.getCurrencyName());
				bulk.add(currency, bulk.insert);
				execute = true;
			}
		}

		if (execute) {
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
		if (currencies != null && values != null) {
			Iterator iter = currencies.keySet().iterator();
			while (iter.hasNext()) {
				currency = (Currency) currencies.get(iter.next());
				value = (CurrencyValues) values.get(new Integer(currency.getID()));
				if (currency != null && value != null) {
					holder = new CurrencyHolder();
					holder.setBuyValue(value.getBuyValue());
					holder.setCurrencyName(currency.getCurrencyAbbreviation());
					holder.setMiddleValue(value.getMiddleValue());
					holder.setSellValue(value.getSellValue());
					map.put(holder.getCurrencyName(), holder);
				}
			}
		}

		return map;
	}

	public static List getCurrencyList() {
		Vector vector = new Vector();
		if (currencyMap != null) {
			Iterator iter = currencyMap.keySet().iterator();
			while (iter.hasNext()) {
				vector.add((CurrencyHolder) currencyMap.get((String) iter.next()));
			}
			Collections.sort(vector, new CurrencyComparator());

			return vector;
		}
		return null;
	}

}
