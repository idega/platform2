/*
 * $Id: HotelBrowser.java,v 1.11 2005/10/05 22:45:10 gimmi Exp $
 * Created on 19.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.hotel.presentation;

import is.idega.idegaweb.travel.presentation.SupplierBrowser;
import is.idega.idegaweb.travel.presentation.SupplierBrowserPlugin;
import is.idega.idegaweb.travel.presentation.TravelBlock;
import is.idega.idegaweb.travel.service.hotel.data.Hotel;
import is.idega.idegaweb.travel.service.hotel.data.HotelHome;
import is.idega.idegaweb.travel.service.hotel.data.HotelType;
import is.idega.idegaweb.travel.service.hotel.data.HotelTypeHome;
import is.idega.idegaweb.travel.service.hotel.data.RoomType;
import is.idega.idegaweb.travel.service.hotel.data.RoomTypeHome;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.JoinCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.Table;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;


public class HotelBrowser extends TravelBlock implements SupplierBrowserPlugin {

	private static final String PARAMETER_ACCOMMODATION_TYPE = "hb_pat";
	private static final String PARAMETER_ROOM_TYPE = "hb_rt";
	private static final String PARAMETER_FROM_DATE = SupplierBrowser.PARAMETER_FROM;
	private static final String PARAMETER_TO_DATE = SupplierBrowser.PARAMETER_TO;
	private static final String PARAMETER_MAX_RATING = "hb_mar";
	private static final String PARAMETER_MIN_RATING = "hb_mir";
	
	public boolean isProductSearchCompleted(IWContext iwc) {
		return iwc.isParameterSet(PARAMETER_FROM_DATE) && iwc.isParameterSet(PARAMETER_TO_DATE);
	}

	public Collection[] getProductSearchInputs(IWContext iwc, IWResourceBundle iwrb) {
//		Collection texts = new Vector();
//		Collection ios = new Vector();
//		
//		return new Collection[]{texts, ios};
		return getSupplierSearchInputs(iwc, iwrb);
	}	

	public Collection[] getSupplierSearchInputs(IWContext iwc, IWResourceBundle iwrb) {
		
		Collection texts = new Vector();
		Collection ios = new Vector();
		
		Collection hotelTypes = new Vector();
		try {
			HotelTypeHome trh = (HotelTypeHome) IDOLookup.getHome(HotelType.class);
			hotelTypes = trh.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DropdownMenu spHotelTypes = new DropdownMenu(PARAMETER_ACCOMMODATION_TYPE );
		SelectorUtility su = new SelectorUtility();
		spHotelTypes = (DropdownMenu) su.getSelectorFromIDOEntities(spHotelTypes, hotelTypes, "getLocalizationKey", iwrb);
		spHotelTypes.addMenuElementFirst("-1", iwrb.getLocalizedString("travel.any_types", "Any type"));

		Collection roomTypes = new Vector();
		try {
			RoomTypeHome trh = (RoomTypeHome) IDOLookup.getHome(RoomType.class);
			roomTypes = trh.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}

		DropdownMenu roomTypeDrop = new DropdownMenu(roomTypes, PARAMETER_ROOM_TYPE);
		roomTypeDrop.addMenuElementFirst("-1", iwrb.getLocalizedString("travel.any_types", "Any type"));
		
		texts.add(iwrb.getLocalizedString("arrival_date", "Arrival date"));
		texts.add(iwrb.getLocalizedString("departure_date", "Departure date"));
		texts.add(iwrb.getLocalizedString("accomodation_type", "Accommodation Type"));
		texts.add(iwrb.getLocalizedString("room_type", "Room Type"));
		
		String pHotel = iwc.getParameter(PARAMETER_ACCOMMODATION_TYPE);
		if (pHotel != null) {
			spHotelTypes.setSelectedElement(pHotel);
		}
		String pRoom = iwc.getParameter(PARAMETER_ROOM_TYPE);
		if (pRoom != null) {
			roomTypeDrop.setSelectedElement(pRoom);
		}
		
		IWTimestamp now = IWTimestamp.RightNow();
		
		DatePicker from = new DatePicker(PARAMETER_FROM_DATE);
		from.setDate(now.getDate());
		DatePicker to = new DatePicker(PARAMETER_TO_DATE);
		now.addDays(1);
		to.setDate(now.getDate());		String pFrom = iwc.getParameter(PARAMETER_FROM_DATE);
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

		ios.add(spHotelTypes);
		ios.add(roomTypeDrop);
		

		texts.add(iwrb.getLocalizedString("minimum_rating", "Minimum rating"));
		texts.add(iwrb.getLocalizedString("maximum_rating", "Maximum rating"));
		
		DropdownMenu min = new DropdownMenu(PARAMETER_MIN_RATING);
		min.addMenuElement("-1", "");
		min.addMenuElement("1", "1");
		min.addMenuElement("2", "2");
		min.addMenuElement("3", "3");
		min.addMenuElement("4", "4");
		min.addMenuElement("5", "5");
		DropdownMenu max = new DropdownMenu(PARAMETER_MAX_RATING);
		max.addMenuElement("-1", "");
		max.addMenuElement("1", "1");
		max.addMenuElement("2", "2");
		max.addMenuElement("3", "3");
		max.addMenuElement("4", "4");
		max.addMenuElement("5", "5");
		

		String pMin = iwc.getParameter(PARAMETER_MIN_RATING);
		if (pMin != null) {
			min.setSelectedElement(pMin);
		}
		String pMax = iwc.getParameter(PARAMETER_MAX_RATING);
		if (pMax != null) {
			max.setSelectedElement(pMax);
		}
		ios.add(min);
		ios.add(max);
		
		
		
		return new Collection[]{texts, ios};
	}
	
	public Collection getSupplierSearchCriterias(IWContext iwc) throws IDOCompositePrimaryKeyException, IDORelationshipException {
		Collection coll = new Vector();
		
		Table supplier = new Table(Supplier.class);
		Table product = new Table(Product.class);
		Table hotel = new Table(Hotel.class);
		
		Column prodCol = new Column(product, product.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		Column hotelCol = new Column(hotel, hotel.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		JoinCriteria jc = new JoinCriteria(prodCol, hotelCol);
		
		coll.add(jc);
		coll.add(new JoinCriteria(product, supplier));
		
		String accType = iwc.getParameter(PARAMETER_ACCOMMODATION_TYPE);
		if (accType != null && !accType.equals("-1")) {
			Table hotelType = new Table(HotelType.class);
			Column col = new Column(hotelType, hotelType.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
			coll.add(new JoinCriteria(hotel, hotelType));
			coll.add(new MatchCriteria(col, MatchCriteria.EQUALS, new Integer(accType)));
		}
		
		String rooType = iwc.getParameter(PARAMETER_ROOM_TYPE);
		if (rooType != null && !rooType.equals("-1")) {
			Table roomType = new Table(RoomType.class);
			Column col = new Column(roomType, roomType.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
			coll.add(new JoinCriteria(hotel, roomType));
			coll.add(new MatchCriteria(col, MatchCriteria.EQUALS, new Integer(rooType)));
		}
		
		return coll;
	}

	public String[] getParameters() {
		return new String[]{PARAMETER_ACCOMMODATION_TYPE, PARAMETER_ROOM_TYPE, 
				PARAMETER_FROM_DATE, PARAMETER_TO_DATE, 
				PARAMETER_MAX_RATING, PARAMETER_MIN_RATING};
	}

	public Collection getProducts(Supplier supplier, Group supplierManager, IWContext iwc, String[][] postalCodes, boolean onlineOnly, boolean useSearchPriceCategoryKey) throws IDOLookupException, FinderException {
		String from = (String) iwc.getParameter(PARAMETER_FROM_DATE);
		String to = (String) iwc.getParameter(PARAMETER_TO_DATE);
		String roomType = (String) iwc.getParameter(PARAMETER_ROOM_TYPE);
		String hotelType = (String) iwc.getParameter(PARAMETER_ACCOMMODATION_TYPE);
		String min = (String) iwc.getParameter(PARAMETER_MIN_RATING);
		String max = (String) iwc.getParameter(PARAMETER_MAX_RATING);
		
		IWTimestamp fromStamp = null;
		if (from != null) {
			fromStamp = new IWTimestamp(from);
		}
		IWTimestamp toStamp = null;
		if (to != null) {
			toStamp = new IWTimestamp(to);
		}
		
		String[] roomTypes = null;
		if (roomType != null && !roomType.equals("-1")) {
			roomTypes = new String[]{roomType};
		}
		String[] hotelTypes = null;
		if (hotelType != null && !hotelType.equals("-1")) {
			hotelTypes = new String[]{hotelType};
		}
		
		float iMin = -1;
		if (min != null) {
			iMin = Float.parseFloat(min);
		}
		float iMax = -1;
		if (max != null) {
			iMax = Float.parseFloat(max);
		}
		
//		Timer t = new Timer();
		Collection coll = getProducts(fromStamp, toStamp, roomTypes, hotelTypes, null, new Object[]{supplier.getPrimaryKey()},iMin, iMax, null);
		if (coll != null && !coll.isEmpty()) {
			Collection pColl = new Vector();
//			ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
			Iterator iter = coll.iterator();
			Hotel hotel;
			Product product;
			boolean checkValidity = isProductSearchCompleted(iwc);
			while (iter.hasNext()) {
				hotel = (Hotel) iter.next();
//				product = pHome.findByPrimaryKey(hotel.getPrimaryKey());
				try {
					product = getProductBusiness(iwc).getProduct(((Integer) hotel.getPrimaryKey()).intValue());
					if (checkValidity) {
//						t.start();
						if (getBookingBusiness(iwc).getIsProductValid(iwc, product, fromStamp, toStamp, onlineOnly, useSearchPriceCategoryKey)) {
							pColl.add(product);
						}
//						t.stop();
//						System.out.println("  checking validity : "+t.getTimeString());
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
	
	public Collection getProducts(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] roomTypeId, Object[] hotelTypeId, Collection postalCodes, Object[] supplierId, float minRating, float maxRating, String supplierName) throws FinderException, IDOLookupException {
		HotelHome hHome = (HotelHome) IDOLookup.getHome(Hotel.class);
		
		return hHome.find(fromStamp, toStamp, roomTypeId, hotelTypeId, postalCodes, supplierId, minRating, maxRating, supplierName);
	}

	public boolean displaySupplierResults() {
		return true;
	}

	public Collection[] getExtraBookingFormElements(Product product, IWResourceBundle iwrb) {
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
//				Timer t = new Timer();
//				t.start();
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
//				t.stop();
//				System.out.println("Time to filter products..."+t.getTimeString());
			}
			return returner;
		}
		return suppliers;
	}
	public int addProductInfo(Product product, com.idega.presentation.Table table, int row, IWResourceBundle iwrb) {
		return row;
	}

}
