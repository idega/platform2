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
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;

import com.idega.block.trade.data.Currency;
import com.idega.block.trade.data.CurrencyHome;
import com.idega.block.trade.data.CurrencyValues;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.util.FileUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;
import com.idega.xml.XMLParser;

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
	
	public static String currencyUrl = null;
	public static IWTimestamp lastUpdate = null;
	
	public static void getCurrencyMap(IWBundle bundle) throws RemoteException {
		IWTimestamp stamp = new IWTimestamp();
		file = null;

		try {
			String path = bundle.getResourcesRealPath() + FileUtil.getFileSeparator() + filterSource;
			fileString = FileUtil.getStringFromFile(path);
		}
		catch (IOException e) {
			e.printStackTrace(System.err);
			fileString = null;
		}

		String url = null;
		String currency_name = null;
		String buy_value = null;
		String sell_value = null;
		String middle_value = null;

		if (fileString != null) {
			try {
				/** @todo setja Abbreviation inn */
				url = (String) TextSoap.FindAllBetween(fileString, siteURL, siteEndURL).firstElement();
				currency_name = (String) TextSoap.FindAllBetween(fileString, currency, currencyEnd).firstElement();
				buy_value = (String) TextSoap.FindAllBetween(fileString, buyValue, buyEndValue).firstElement();
				sell_value = (String) TextSoap.FindAllBetween(fileString, sellValue, sellEndValue).firstElement();
				middle_value = (String) TextSoap.FindAllBetween(fileString, middleValue, middleEndValue).firstElement();
				defaultCurrency = (String) TextSoap.FindAllBetween(fileString, currencyDefault, currencyDefaultEnd).firstElement();
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}

		XMLParser parser = new XMLParser();
		XMLElement rootElement = null;
		try {
			rootElement = parser.parse(url).getRootElement();
			currencyUrl = url;
			lastUpdate = IWTimestamp.RightNow();
		}
		catch (XMLException e) {
				e.printStackTrace(System.err);
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

					if (currencyValues.getName().equalsIgnoreCase(currency_name)) {
						holder.setCurrencyName(currencyValues.getText());
						holder.setCurrencyAbbreviation(currencyValues.getText());
					}
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
			defaultHolder.setCurrencyAbbreviation(defaultCurrency);
			defaultHolder.setBuyValue(1);
			defaultHolder.setSellValue(1);
			defaultHolder.setMiddleValue(1);
			currencyMap.put(holder.getCurrencyName(), defaultHolder);
		}

		System.out.println("Default currency: " + defaultCurrency);
	}

	public static CurrencyHolder getCurrencyHolder(String currencyName) {
		if (getCurrencyMap() != null) {
			CurrencyHolder holder = (CurrencyHolder) currencyMap.get(currencyName);
			if (holder != null) {
				return holder;
			}
			return null;
		}
		return null;
	}

	private static HashMap getCurrencyMap() {
		if (currencyMap == null) {
			try {
				System.out.println("[CurrencyBusiness] currencyMap == null, trying to get a new one...");
				getCurrencyMap(IWContext.getInstance().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER));
				System.out.println("[CurrencyBusiness] getCurrencyMap(bundle) done...");
			} catch (RemoteException e) {
				System.out.println("[CurrencyBusiness] getCurrencyMap(bundle) FAILED...");
				e.printStackTrace(System.err);
			}
		}
		return currencyMap;
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

	public static void saveCurrencyValuesToDatabase() throws RemoteException {
		if (getCurrencyMap() != null) {
			//EntityBulkUpdater bulk = new EntityBulkUpdater();
			IWTimestamp stamp = new IWTimestamp();

			HashMap currencies = saveCurrenciesToDatabase();

			CurrencyHolder holder = null;
			Currency currency = null;
			CurrencyValues values = null;
//			boolean update;

			Iterator iter = getCurrencyMap().keySet().iterator();
			while (iter.hasNext()) {
//				update = true;
				holder = (CurrencyHolder) getCurrencyMap().get((String) iter.next());
				currency = (Currency) currencies.get(holder.getCurrencyName());
				if (currency != null) {
					values = CurrencyFinder.getCurrencyValue(currency.getID());
					if (values == null) {
	//					update = false;
						values = ((com.idega.block.trade.data.CurrencyValuesHome) com.idega.data.IDOLookup.getHomeLegacy(CurrencyValues.class)).createLegacy();
						values.setID(currency.getID());
					}
					values.setBuyValue(holder.getBuyValue());
					values.setSellValue(holder.getSellValue());
					values.setMiddleValue(holder.getMiddleValue());
					values.setCurrencyDate(stamp.getTimestamp());
					holder.setCurrencyID(currency.getID());
					values.store();
					getCurrencyMap().put(holder.getCurrencyName(), holder);
				} else {
					System.out.println("Cannot find currency : " + holder.getCurrencyName());
				}
//				if (update)
//					bulk.add(values, EntityBulkUpdater.update);
//				else
//					bulk.add(values, EntityBulkUpdater.insert);
			}

			try {
//				bulk.execute();
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}

	public static HashMap saveCurrenciesToDatabase() throws RemoteException {
//		EntityBulkUpdater bulk = new EntityBulkUpdater();
		Currency currency = null;
//		boolean execute = false;

		String currAbbr;
		CurrencyHome home = (CurrencyHome) IDOLookup.getHome(Currency.class);
		boolean update;

		Iterator iter = getCurrencyMap().keySet().iterator();
		HashMap map = getValuesFromDB();
		while (iter.hasNext()) {
			currAbbr = (String) iter.next();
			CurrencyHolder holder = (CurrencyHolder) getCurrencyMap().get(currAbbr);
			if (holder != null && !map.containsKey(holder.getCurrencyName())) {
				try {
					try {
						if (holder.getCurrencyAbbreviation() != null) {
							currency = home.getCurrencyByAbbreviation(holder.getCurrencyAbbreviation());
						}
						if (currency != null) {
							update = true;
						}else {
							update = false;
							currency = home.create();
						}

						if (currency.getID() < 1 ) {
							update = false;
							currency = home.create();	
						}

					} catch (Exception e) {
						currency = home.create();
						update = false;
					}
					
					currency.setCurrencyAbbreviation(holder.getCurrencyName());
					currency.setCurrencyName(holder.getCurrencyName());
					if (update) {
						System.out.println("[CurrencyBusiness] Updating existing currency : " + currency.getCurrencyName() + " (id: " + currency.getID() + ")");
						
//						bulk.add(currency, bulk.update);
						currency.store();
					}
					else {
						System.out.println("[CurrencyBusiness] Creating new currency, name : " + currency.getCurrencyName() + ", abbr : "+currency.getCurrencyAbbreviation());
//						bulk.add(currency, bulk.insert);
						currency.store();
					}

//					execute = true;
				}
				catch (CreateException ce) {
					ce.printStackTrace(System.err);
				}
			}
		}
		
/*
		if (execute) {
			try {
				bulk.execute();
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
*/
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
					holder.setCurrencyAbbreviation(currency.getCurrencyAbbreviation());
					holder.setCurrencyID(currency.getID());
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
		
		if (getCurrencyMap() != null) {
			Iterator iter = getCurrencyMap().keySet().iterator();
			while (iter.hasNext()) {
				vector.add((CurrencyHolder) getCurrencyMap().get((String) iter.next()));
			}
			Collections.sort(vector, new CurrencyComparator());

			return vector;
		} 
		return null;
	}
	
	public static String getCurrencyUrl() {
		return currencyUrl;
	}

	public static IWTimestamp getLastUpdate() {
		return lastUpdate;
	}
}
