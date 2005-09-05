/*
 * $Id: SupplierBrowserBusinessBean.java,v 1.6 2005/09/05 10:37:08 gimmi Exp $
 * Created on Jul 6, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.business;

import is.idega.idegaweb.travel.IWBundleStarter;
import is.idega.idegaweb.travel.data.CashierQueue;
import is.idega.idegaweb.travel.data.CashierQueueHome;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.SupplierBrowserSearchForm;
import is.idega.idegaweb.travel.presentation.TravelBlock;
import is.idega.idegaweb.travel.service.business.ServiceHandler;
import is.idega.idegaweb.travel.service.hotel.presentation.HotelSearch;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import is.idega.idegaweb.travel.service.tour.data.TourType;
import is.idega.idegaweb.travel.service.tour.data.TourTypeHome;
import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.CreateException;
import com.idega.block.basket.business.BasketBusiness;
import com.idega.block.basket.data.BasketEntry;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORuntimeException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;
import com.idega.xml.XMLParser;


public class SupplierBrowserBusinessBean extends IBOServiceBean  implements SupplierBrowserBusiness{
	
	public static final String ELEMENT_SEPARATOR = "Separator";
	public static final String ELEMENT_DEFAULT_VALUES = "DefaultValues";
	public static final String ELEMENT_SEARCH_INPUT = "SearchInput";
	public static final String ELEMENT_SEARCH_FORM = "SearchForm";
	public static final String ELEMENT_HEADING = "Heading";
	public static final String SEARCH_INPUT_NAME = "name";
	public static final String SEARCH_INPUT_VALUE = "value";
	public static final String ATTRIBUTE_NAME = "name";
	public static final String ATTRIBUTE_VALUE = "value";
	public static final String ATTRIBUTE_TYPE = "type";
	public static final String ATTRIBUTE_STYLE_CLASS = "styleClass";
	public static final String ATTRIBUTE_LOCALIZATION_KEY = "localizationKey";
	public static final String ATTRIBUTE_LAYER_IDS = "divIDs";
	public static final String ATTRIBUTE_SELECTED = "selected";
	public static final String ATTRIBUTE_PAGE_ID = "pageID";
	public static final String TYPE_DROPDOWN = "dropdown";
	public static final String TYPE_DROPDOWN_MENU_ITEM = "menuItem";
	public static final String TYPE_DATE = "date";
	public static final String ATTRIBUTE_DATE_MODIFIER = "dateModifier";
	public static final String TYPE_TEXT = "text";
	public static final String TYPE_LOCATION = "location";
	public static final String TYPE_ACCOMMODATION = "typeofAccommodation";
	public static final String TYPE_ROOM = "typeofRoom";
	public static final String TYPE_TOUR = "typeofTour";
	public static final String ATTRIBUTE_TOUR_TYPE = "tourType";
	public static final String OBJECT_TYPE_TEXT = "text";
	public static final String OBJECT_TYPE_INTERFACE_OBJECT = "io";

	public static final String LAYER_ID_SEPARATOR = "separatordiv";
	public static final String LAYER_ID_INPUT = "inputdiv";
	public static final String LAYER_ID_MAIN = "maindiv";
	
	private String defaultTextStyleClass = null;
	private String defaultIOStyleClass = null;
	private String maindiv = "sbs_main_div";
	private String inputdiv = "sbs_input_div";
	private String separatordiv = "sbs_separator_div";
	
	private IWResourceBundle iwrb;
	private String datasource;
	
	private HashMap formMap = new HashMap();
	private HashMap xmlMap = new HashMap();
	private HashMap parameterNameMap = new HashMap();
	
	public void sendToCashier(Group supplierManager, String clientName, User cashier, User performer, BasketBusiness basketBusiness) throws CreateException, RemoteException, IDOAddRelationshipException {
		CashierQueue queue = getCashierQueueHome().create();
		if (cashier != null) {
			queue.setCashier(cashier);
		}
		if (performer != null) {
			queue.setOwner(performer);
		}
		if (supplierManager != null) { 
			queue.setSupplierManager(supplierManager);
		}
		if (clientName != null) {
			queue.setClientName(clientName);
		}
		queue.store();
		
		Collection entries = basketBusiness.getBasket().values();
		Iterator iter = entries.iterator();
		BasketEntry entry;
		GeneralBooking booking;
		while (iter.hasNext()) {
			entry = (BasketEntry) iter.next();
			booking = (GeneralBooking) entry.getItem();
			booking.setIsValid(true);
			booking.store();
			queue.addBooking(booking);
		}
		basketBusiness.emptyBasket();
	}
	
	private CashierQueueHome getCashierQueueHome() {
		try {
			return (CashierQueueHome) IDOLookup.getHome(CashierQueue.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	/**
	 * Collection on SupplierBrowserSearchForm
	 * @param xml
	 * @return
	 * @throws RemoteException 
	 * @throws XMLException 
	 */
	public synchronized Collection parseXML(Group supplierManager, File xml) throws RemoteException, XMLException {
		
		XMLParser parser = new XMLParser();
		XMLDocument doc = parser.parse(xml);
		XMLElement el = doc.getRootElement();

		String engineName = el.getAttributeValue(ATTRIBUTE_NAME);
		String key = engineName+"_"+supplierManager.getPrimaryKey().toString()+IWTimestamp.RightNow().toSQLDateString();
		
//		xmlMap = new HashMap();
		Collection returner = (Collection) xmlMap.get(key);
		if (returner == null) {
			returner = new Vector();
			Iterator its = el.getChildren(SupplierBrowserBusinessBean.ELEMENT_SEARCH_FORM).iterator();
			while (its.hasNext()) {
				el = (XMLElement) its.next();
				String searchForm = el.getAttributeValue(ATTRIBUTE_LOCALIZATION_KEY);
				String pageID = el.getAttributeValue(ATTRIBUTE_PAGE_ID);
				
				Collection paragraphs = new Vector();
				List children = el.getChildren();
				Iterator cIterator = children.iterator();
				while (cIterator.hasNext()) {
					listElement(supplierManager, (XMLElement) cIterator.next(), paragraphs);
				}
		
				SupplierBrowserSearchForm sf = new SupplierBrowserSearchForm(searchForm, pageID);
				sf.setPararaphs(paragraphs);

				Collection prmName = sf.getParameters();
				
				parameterNameMap.put(engineName+searchForm, prmName);
				formMap.put(engineName+searchForm, sf);
				returner.add(sf);
			}
			xmlMap.put(key, returner);
		}
		
		return returner;
	}
	
	public Collection getParameters(String engineName, String searchFormName) {
		return (Collection) parameterNameMap.get(engineName+searchFormName);
	}
	
	private void listElement(Group supplierManager, XMLElement e, Collection paragraphs) throws RemoteException {

		if (e.getName().equalsIgnoreCase(ELEMENT_DEFAULT_VALUES)) {
			List list = e.getChildren();
			Iterator defIter = list.iterator();
			while (defIter.hasNext()) {
				XMLElement el = (XMLElement) defIter.next();
				String elName = el.getName();
				if (elName.equalsIgnoreCase(ATTRIBUTE_STYLE_CLASS)) {
					defaultTextStyleClass = el.getAttributeValue(OBJECT_TYPE_TEXT);
					defaultIOStyleClass = el.getAttributeValue(OBJECT_TYPE_INTERFACE_OBJECT);
				} else if (elName.equalsIgnoreCase(ATTRIBUTE_LAYER_IDS)){
					if (el.getAttributeValue(LAYER_ID_INPUT) != null) {
						inputdiv = el.getAttributeValue(LAYER_ID_INPUT);
					}
					if (el.getAttributeValue(LAYER_ID_MAIN) != null) {
						maindiv = el.getAttributeValue(LAYER_ID_MAIN); 
					}
					if (el.getAttributeValue(LAYER_ID_SEPARATOR) != null) {
						separatordiv = el.getAttributeValue(LAYER_ID_SEPARATOR);
					}
				} 
			}
		}
		else 
		if (e.getName().equalsIgnoreCase(ELEMENT_SEARCH_INPUT)) {
			Paragraph p = new Paragraph();
			String styleClass = e.getAttributeValue(ATTRIBUTE_STYLE_CLASS);
			if (styleClass != null) {
				p.setStyleClass(styleClass);
			}
			handleSearchInput(supplierManager, e, p);
			paragraphs.add(p);
		}
		else if (e.getName().equalsIgnoreCase(ELEMENT_SEPARATOR)) {
			Paragraph p = new Paragraph();
			Label label = new Label();
			label.setValue(ELEMENT_SEPARATOR);
			p.add(label);
			p.add((InterfaceObject)null);
			paragraphs.add(p);
		}
		
		List children = e.getChildren();
		Iterator iter = children.iterator();
		while (iter.hasNext()) {
			listElement(supplierManager, (XMLElement) iter.next(), paragraphs);
		}
	}
	
	private void handleSearchInput(Group supplierManager, XMLElement e, Paragraph p) throws RemoteException {
		List children = e.getChildren();
		Iterator iter = children.iterator();
		if (!e.getChildren(SEARCH_INPUT_NAME).isEmpty() && !e.getChildren(SEARCH_INPUT_VALUE).isEmpty()) {
			while (iter.hasNext()) {
				XMLElement el = (XMLElement) iter.next();
				if (el.getName().equalsIgnoreCase(SEARCH_INPUT_NAME)) {
					String loc = el.getAttributeValue(ATTRIBUTE_LOCALIZATION_KEY);
					Label text = new Label();
					if (loc != null) {
						text.setLabel(getResourceBundle().getLocalizedString(loc, loc));
					}
					String styleClass = el.getAttributeValue(ATTRIBUTE_STYLE_CLASS);
					if (styleClass != null) {
						text.setStyleClass(styleClass);
					} else {
						text.setStyleClass(defaultTextStyleClass);
					}
					p.add(text);
				} else if (el.getName().equalsIgnoreCase(SEARCH_INPUT_VALUE)) {
					String type = el.getAttributeValue(ATTRIBUTE_TYPE);
					String styleClass = el.getAttributeValue(ATTRIBUTE_STYLE_CLASS);
					InterfaceObject input = null;
					if (type.equalsIgnoreCase(TYPE_TEXT)) {
						input = new TextInput(el.getAttributeValue(ATTRIBUTE_NAME));
					}
					else if (type.equalsIgnoreCase(TYPE_DROPDOWN)) {
						input = new DropdownMenu(el.getAttributeValue(ATTRIBUTE_NAME));
						List menuItems = el.getChildren(TYPE_DROPDOWN_MENU_ITEM);
						Iterator mIter = menuItems.iterator();
						while (mIter.hasNext()) {
							XMLElement menuItem  = (XMLElement) mIter.next();
							((DropdownMenu) input).addMenuElement(menuItem.getAttributeValue(ATTRIBUTE_VALUE), menuItem.getAttributeValue(ATTRIBUTE_NAME));
							String selected = menuItem.getAttributeValue(ATTRIBUTE_SELECTED);
							if (selected != null && selected.equalsIgnoreCase("true")) {
								((DropdownMenu) input).setSelectedElement(menuItem.getAttributeValue(ATTRIBUTE_VALUE));
							}
						}
					} 
					else if (type.equalsIgnoreCase(TYPE_DATE)) {
						input = new DateInput(el.getAttributeValue(ATTRIBUTE_NAME), true);
						String modder = el.getAttributeValue(ATTRIBUTE_DATE_MODIFIER);
						IWTimestamp stamp = IWTimestamp.RightNow();
						if (modder != null) {
							stamp.addDays(Integer.parseInt(modder));
						}
						((DateInput)input).setDate(stamp.getDate());
					} 
					else if (type.equalsIgnoreCase(TYPE_LOCATION)) {
						try {
							BookingForm bf = getServiceHandler().getBookingForm(IWContext.getInstance(), null, false);
							input = bf.getPostalCodeDropdown(getResourceBundle(), getDatasource());
							input.setName(el.getAttributeValue(ATTRIBUTE_NAME));
						}
						catch (Exception e1) {
							e1.printStackTrace();
						}
					}

					// Accommodation Specific
					else if (type.equalsIgnoreCase(TYPE_ACCOMMODATION)) {
						HotelSearch hs = new HotelSearch();
						input = hs.getHotelTypeDropdown(supplierManager, getResourceBundle(), el.getAttributeValue(ATTRIBUTE_NAME));
						String selected = el.getAttributeValue(ATTRIBUTE_SELECTED);
						if (selected != null && selected.equalsIgnoreCase("true")) {
							((DropdownMenu) input).setSelectedElement(el.getAttributeValue(ATTRIBUTE_VALUE));
						}
					}
					else if (type.equalsIgnoreCase(TYPE_ROOM)) {
						HotelSearch hs = new HotelSearch();
						input = hs.getRoomTypeDropdown(supplierManager, el.getAttributeValue(ATTRIBUTE_NAME));
						String selected = el.getAttributeValue(ATTRIBUTE_SELECTED);
						if (selected != null && selected.equalsIgnoreCase("true")) {
							((DropdownMenu) input).setSelectedElement(el.getAttributeValue(ATTRIBUTE_VALUE));
						}
					}
					// Tour Specific
					else if (type.equalsIgnoreCase(TYPE_TOUR)) {
						TourTypeHome tth = (TourTypeHome) IDOLookup.getHome(TourType.class, getDatasource());
						SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class, getDatasource());
						try {
							Collection tourTypes = tth.findByCategoryUsedBySuppliers(el.getAttributeValue(ATTRIBUTE_TOUR_TYPE), sHome.findAll(supplierManager));
							input = new DropdownMenu(tourTypes, el.getAttributeValue(ATTRIBUTE_NAME));
							String selected = el.getAttributeValue(ATTRIBUTE_SELECTED);
							if (selected != null && selected.equalsIgnoreCase("true")) {
								((DropdownMenu) input).setSelectedElement(el.getAttributeValue(ATTRIBUTE_VALUE));
							}
						}
						catch (Exception e1) {
							e1.printStackTrace();
						}

					}
					

					// If type is unsupported
					else {
						input = new TextInput(el.getAttributeValue(ATTRIBUTE_NAME));
					}
					if (styleClass != null) {
						input.setStyleClass(styleClass);
					} else {
						input.setStyleClass(defaultIOStyleClass);
					}
					input.keepStatusOnAction();
					p.add(input);
				}
			}
		}
	}
	
	private IWResourceBundle getResourceBundle() {
		if (iwrb == null) {
			IWBundle bundle = getBundle();
			iwrb = bundle.getResourceBundle(IWContext.getInstance());
		}
		return iwrb;
	}
	
	public String getBundleIdentifier() {
		return TravelBlock.IW_BUNDLE_IDENTIFIER;
	}
	
	protected String getDatasource() throws RemoteException {
		if (datasource == null) {
			String tmp = getBundle().getProperty(IWBundleStarter.DATASOURCE);
			if (tmp != null) {
				datasource = tmp;
			} else {
				datasource = "default";
			}
		}
		
		return datasource;
	}
	
	public ServiceHandler getServiceHandler() {
		try {
			return (ServiceHandler) IBOLookup.getServiceInstance(getIWApplicationContext(), ServiceHandler.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}
