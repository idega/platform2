/*
 * $Id: CarRentalBrowser.java,v 1.7 2005/10/19 11:08:21 gimmi Exp $
 * Created on 18.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.carrental.presentation;

import is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm;
import is.idega.idegaweb.travel.presentation.SupplierBrowser;
import is.idega.idegaweb.travel.presentation.SupplierBrowserPlugin;
import is.idega.idegaweb.travel.presentation.TravelBlock;
import is.idega.idegaweb.travel.service.carrental.data.CarRental;
import is.idega.idegaweb.travel.service.carrental.data.CarRentalHome;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.JoinCriteria;
import com.idega.data.query.Table;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.TimeInput;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;


public class CarRentalBrowser extends TravelBlock implements SupplierBrowserPlugin {
	
	private static final String PARAMETER_TO_DATE = AbstractSearchForm.PARAMETER_TO_DATE;

	public boolean displaySupplierResults() {
		return true;
	}

	public boolean isProductSearchCompleted(IWContext iwc) {
		return iwc.isParameterSet(SupplierBrowser.PARAMETER_FROM);
	}

	public String[] getParameters() {
		return new String[]{SupplierBrowser.PARAMETER_FROM, PARAMETER_TO_DATE};
	}

	public Collection[] getSupplierSearchInputs(IWContext iwc, IWResourceBundle iwrb) {
		Collection texts = new Vector();
		Collection ios = new Vector();

		texts.add(iwrb.getLocalizedString("location", "Location"));
		
		DropdownMenu location = new DropdownMenu(SupplierBrowser.PARAMETER_POSTAL_CODES);
		location.addMenuElement("100-999", iwrb.getLocalizedString("travel.search.iceland", "Iceland"));
		location.addMenuElement("100-199", iwrb.getLocalizedString("travel.search.reykjavik", "Reykjavik"));
		location.addMenuElement("100-299", iwrb.getLocalizedString("travel.search.reykjavik_area", "Reykjavik Area"));
		location.addMenuElement("300-399", iwrb.getLocalizedString("travel.search.west_iceland", "West Iceland"));
		location.addMenuElement("400-499", iwrb.getLocalizedString("travel.search.west_fjords", "West Fjords"));
		location.addMenuElement("500-699", iwrb.getLocalizedString("travel.search.north_iceland", "North Iceland"));
		location.addMenuElement("700-799", iwrb.getLocalizedString("travel.search.east_iceland", "East Iceland"));
		location.addMenuElement("800-899", iwrb.getLocalizedString("travel.search.south_iceland", "South Iceland"));
		location.addMenuElement("900-998", iwrb.getLocalizedString("travel.search.westman_islands", "Westman Islands"));
		String sLoc = iwc.getParameter(SupplierBrowser.PARAMETER_POSTAL_CODES);
		if (sLoc != null) {
			location.setSelectedElement(sLoc);
		}
		
		ios.add(location);
		return new Collection[]{texts, ios};
	}

	public Collection[] getProductSearchInputs(IWContext iwc, IWResourceBundle iwrb) {
		Collection texts = new Vector();
		Collection ios = new Vector();
		
		texts.add(iwrb.getLocalizedString("arrival_date", "Arrival date"));
		texts.add(iwrb.getLocalizedString("departure_date", "Departure date"));
		IWTimestamp now = IWTimestamp.RightNow();
		
		DatePicker from = new DatePicker(SupplierBrowser.PARAMETER_FROM);
		from.setDate(now.getDate());
		DatePicker to = new DatePicker(PARAMETER_TO_DATE);
		now.addDays(1);
		to.setDate(now.getDate());
		
		String pFrom = iwc.getParameter(SupplierBrowser.PARAMETER_FROM);
		if (pFrom != null) {
			IWTimestamp tmp = new IWTimestamp(pFrom);
			from.setDate(tmp.getDate());
		}
		String pTo = iwc.getParameter(PARAMETER_TO_DATE);
		if (pTo != null) {
			IWTimestamp tmp = new IWTimestamp(pTo);
			to.setDate(tmp.getDate());
		}

		ios.add(from);
		ios.add(to);
		
		return new Collection[]{texts, ios};
	}

	public Collection getProducts(IWTimestamp fromStamp, IWTimestamp toStamp, Collection postalCodes, Object[] supplierId, String supplierName) throws FinderException, IDOLookupException {
		CarRentalHome cHome = (CarRentalHome) IDOLookup.getHome(CarRental.class);
		return cHome.find(fromStamp, toStamp, postalCodes, supplierId, supplierName);
//		return hHome.find(fromStamp, toStamp, roomTypeId, hotelTypeId, postalCodes, supplierId, minRating, maxRating, supplierName);
	}

	public Collection getSupplierSearchCriterias(IWContext iwc) throws IDOCompositePrimaryKeyException,
			IDORelationshipException {
		Collection coll = new Vector();
		
		Table supplier = new Table(Supplier.class);
		Table product = new Table(Product.class);
		Table car = new Table(CarRental.class);
		
		Column prodCol = new Column(product, product.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		Column carCol = new Column(car, car.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		JoinCriteria jc = new JoinCriteria(prodCol, carCol);
		
		coll.add(jc);
		coll.add(new JoinCriteria(product, supplier));

		return coll;
	}

	public Collection getProducts(Supplier supplier, Group supplierManager, IWContext iwc, String[][] postalCodes, boolean onlineOnly, boolean useSearchPriceCategoryKey) throws IDOLookupException, FinderException {
		String from = (String) iwc.getParameter(SupplierBrowser.PARAMETER_FROM);
		String to = (String) iwc.getParameter(PARAMETER_TO_DATE);
		String noDays = (String) iwc.getParameter(SupplierBrowser.PARAMETER_NUMBER_OF_DAYS);
	
		IWTimestamp fromStamp = null;
		if (from != null) {
			fromStamp = new IWTimestamp(from);
		}
		IWTimestamp toStamp = null;
		if (to != null) {
			toStamp = new IWTimestamp(to);
		} else if (noDays != null && from != null) {
			toStamp = new IWTimestamp(from);
			toStamp.addDays(Integer.parseInt(noDays));
		}

		
		Collection coll = getProducts(fromStamp, toStamp, null, new Object[]{supplier.getPrimaryKey()}, null);
		if (coll != null && !coll.isEmpty()) {
			Collection pColl = new Vector();
			ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
			Iterator iter = coll.iterator();
			CarRental car;
			Product product;
			boolean checkValidity = isProductSearchCompleted(iwc);
			while (iter.hasNext()) {
				car = (CarRental) iter.next();
				product =pHome.findByPrimaryKey(car.getPrimaryKey());
				try {
					if (checkValidity) {
						if (getBookingBusiness(iwc).getIsProductValid(iwc, product, fromStamp, toStamp, onlineOnly, useSearchPriceCategoryKey)) {
							pColl.add(product);
						}
					} else {
						pColl.add(product);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			return pColl;
		}
		
		return coll;
	
	}

	public Collection[] getExtraBookingFormElements(Product product, IWResourceBundle iwrb) {
		try {
			CarRentalHome cHome = (CarRentalHome) IDOLookup.getHome(CarRental.class);
			CarRental c = cHome.findByPrimaryKey(product.getPrimaryKey());
			
			Collection pickups = c.getPickupPlaces();
			Collection dropdoffs = c.getDropoffPlaces();
	
			DropdownMenu ddPickup = new DropdownMenu(pickups, CarRentalBookingForm.parameterPickupId);
			TimeInput pickTime = new TimeInput(CarRentalBookingForm.PARAMETER_PICKUP_TIME);
			DropdownMenu ddDropoff = new DropdownMenu(dropdoffs, CarRentalBookingForm.PARAMETER_DROPOFF_PLACE);
			TimeInput dropTime = new TimeInput(CarRentalBookingForm.PARAMETER_DROPOFF_TIME);
			
			pickTime.setHour(8);
			pickTime.setMinute(0);

			dropTime.setHour(8);
			dropTime.setMinute(0);
			
			Collection strings = new Vector();
			Collection ios = new Vector();
			
			strings.add(iwrb.getLocalizedString("travel.pickup_place", "Pickup place"));
			strings.add(iwrb.getLocalizedString("travel.pickup_time", "Pickup time"));
			strings.add(iwrb.getLocalizedString("travel.dropoff_place", "Dropoff place"));
			strings.add(iwrb.getLocalizedString("travel.dropoff_time", "Dropoff time"));

			ios.add(ddPickup);
			ios.add(pickTime);
			ios.add(ddDropoff);
			ios.add(dropTime);
			
			return new Collection[]{strings, ios};
		} catch (Exception e) {
			
		}
		return null;
	}

	public Collection filterSuppliers(Collection suppliers, Group supplierManager, IWContext iwc, String[][] postalCodes, boolean onlineOnly, boolean useSearchPriceCategoryKey) {
		if (suppliers != null && isProductSearchCompleted(iwc)) {
			Iterator sIter = suppliers.iterator();
			Supplier supp;
			Collection products;
			Collection returner = new Vector();
			while (sIter.hasNext()) {
				supp = (Supplier) sIter.next();
				try {
					products = getProducts(supp, supplierManager, iwc, postalCodes, onlineOnly, useSearchPriceCategoryKey);
					if (products != null && !products.isEmpty()) {
						returner.add(supp);
					}
				}
				catch (IDOLookupException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
					e.printStackTrace();
				}
			}
			return returner;
		}
		return suppliers;
	}
	
	public int addProductInfo(Product product, com.idega.presentation.Table table, int row, IWResourceBundle iwrb) {
		return row;
	}

	public String getLocalizationPrefix() {
		return "carrental_browser_";
	}
}
