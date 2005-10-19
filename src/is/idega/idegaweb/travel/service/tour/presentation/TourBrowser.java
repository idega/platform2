/*
 * $Id: TourBrowser.java,v 1.12 2005/10/19 11:08:21 gimmi Exp $
 * Created on 28.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.tour.presentation;

import is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm;
import is.idega.idegaweb.travel.presentation.SupplierBrowser;
import is.idega.idegaweb.travel.presentation.SupplierBrowserPlugin;
import is.idega.idegaweb.travel.presentation.TravelBlock;
import is.idega.idegaweb.travel.service.tour.data.Tour;
import is.idega.idegaweb.travel.service.tour.data.TourCategory;
import is.idega.idegaweb.travel.service.tour.data.TourCategoryHome;
import is.idega.idegaweb.travel.service.tour.data.TourHome;
import is.idega.idegaweb.travel.service.tour.data.TourType;
import is.idega.idegaweb.travel.service.tour.data.TourTypeHome;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.PostalCodeHome;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORuntimeException;
import com.idega.data.query.Column;
import com.idega.data.query.JoinCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.Table;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;
import com.idega.util.Timer;


public class TourBrowser extends TravelBlock implements SupplierBrowserPlugin {

	private static final String PARAMETER_TOUR_CATEGORY_ID = "tb_tc";
	private static final String PARAMETER_TOUR_TYPE_ID = "tb_tt";
	
	private static final String PARAMETER_FROM_DATE = SupplierBrowser.PARAMETER_FROM;
	private static final String PARAMETER_TO_DATE = AbstractSearchForm.PARAMETER_TO_DATE;
	
	static final String PARAMETER_FORCED_TOUR_TYPE_ID = "tb_ftt";
	static final String PARAMETER_FORCED_TOUR_CATEGORY_ID = "tb_ftc";
	
	public boolean isProductSearchCompleted(IWContext iwc) {
		return iwc.isParameterSet(PARAMETER_FROM_DATE);
	}

	public String[] getParameters() {
		return new String[]{PARAMETER_TOUR_CATEGORY_ID, PARAMETER_TOUR_TYPE_ID, 
				PARAMETER_FROM_DATE, PARAMETER_TO_DATE, PARAMETER_FORCED_TOUR_TYPE_ID};
	}

	public Collection[] getSupplierSearchInputs(IWContext iwc, IWResourceBundle iwrb) {
		Collection txts = new Vector();
		Collection ios = new Vector();
		
		DropdownMenu tourTypes = new DropdownMenu(PARAMETER_TOUR_TYPE_ID );
		DropdownMenu tourCats = new DropdownMenu(PARAMETER_TOUR_CATEGORY_ID );
		try {
			String fttid = iwc.getParameter(PARAMETER_FORCED_TOUR_TYPE_ID);
			if (fttid == null) { // This only happens in the tourType hasnt been selected (forced)
				Iterator catIter = null;
				String ftc = iwc.getParameter(PARAMETER_FORCED_TOUR_CATEGORY_ID);
				if (ftc != null) {
					Vector v = new Vector();
					v.add(getTourCategoryHome().findByPrimaryKey(new Integer(ftc)));
					catIter = v.iterator();
				} else {
					catIter = getTourCategoryHome().findAll().iterator();
				}
				
				TourCategory cat;
				while (catIter.hasNext()) {
					cat = (TourCategory) catIter.next();
					Collection types = getTourTypeHome().findByCategory(cat.getName());
					tourCats.addMenuElement(cat.getPrimaryKey().toString(), iwrb.getLocalizedString(cat.getLocalizationKey(), cat.getLocalizationKey()));
					Iterator iter = types.iterator();
					TourType tt;
					while (iter.hasNext()) {
						tt = (TourType) iter.next();
						tourTypes.addMenuElement(tt.getPrimaryKey().toString(), iwrb.getLocalizedString(tt.getLocalizationKey(), tt.getLocalizationKey()));
					}
				}
				tourTypes.addMenuElementFirst("-1", iwrb.getLocalizedString("travel.any_type", "Any Type"));
				String tT = iwc.getParameter(PARAMETER_TOUR_TYPE_ID);
				if (tT != null) {
					tourTypes.setSelectedElement(tT);
				}

				ios.add(tourTypes);
				txts.add(iwrb.getLocalizedString("travel.tour_type", "Tour Type"));
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
		return new Collection[]{txts, ios};
	}

	public Collection[] getProductSearchInputs(IWContext iwc, IWResourceBundle iwrb) {
		Collection texts = new Vector();
		Collection ios = new Vector();
		
		texts.add(iwrb.getLocalizedString("date", "Date"));
		texts.add(iwrb.getLocalizedString("location", "Location"));
		
		IWTimestamp now = IWTimestamp.RightNow();
		
		DatePicker from = new DatePicker(PARAMETER_FROM_DATE);
		from.setDate(now.getDate());
		String pFrom = iwc.getParameter(PARAMETER_FROM_DATE);
		if (pFrom != null) {
			IWTimestamp tmp = new IWTimestamp(pFrom);
			from.setDate(tmp.getDate());
		}
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
		
		
		ios.add(from);
		ios.add(location);
		return new Collection[]{texts, ios};
	}

	public Collection getSupplierSearchCriterias(IWContext iwc) throws IDOCompositePrimaryKeyException, IDORelationshipException {
		Collection coll = new Vector();
		
		Table supplier = new Table(Supplier.class);
		Table product = new Table(Product.class);
		Table tour = new Table(Tour.class);
		
		Column prodCol = new Column(product, product.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		Column tourCol = new Column(tour, tour.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		JoinCriteria jc = new JoinCriteria(prodCol, tourCol);
		
		coll.add(jc);
		coll.add(new JoinCriteria(product, supplier));
		
		String tourType = iwc.getParameter(PARAMETER_TOUR_TYPE_ID);
		String fTourType = iwc.getParameter(PARAMETER_FORCED_TOUR_TYPE_ID);
		if (fTourType != null) {
			tourType = fTourType;
		}
		if (tourType != null && !tourType.equals("-1")) {
			Table tTourType = new Table(TourType.class);
			Column col = new Column(tTourType, tTourType.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
			coll.add(new JoinCriteria(tour, tTourType));
			coll.add(new MatchCriteria(col, MatchCriteria.EQUALS, new Integer(tourType)));
		}

		return coll;	
	}

	public Collection getProducts(Supplier supplier, Group supplierManager, IWContext iwc, String[][] postalCodes, boolean onlineOnly, boolean useSearchPriceCategoryKey) throws IDOLookupException, FinderException {
		String tourTypeId = iwc.getParameter(PARAMETER_TOUR_TYPE_ID);
		String from = iwc.getParameter(PARAMETER_FROM_DATE);
		String ftt = iwc.getParameter(PARAMETER_FORCED_TOUR_TYPE_ID);
		if (ftt != null) {
			tourTypeId = ftt;
		}
		
		IWTimestamp fromStamp = null;
		IWTimestamp toStamp = null;
		if (from != null) {
			fromStamp = new IWTimestamp(from);
			toStamp = new IWTimestamp(from);
			toStamp.addDays(1);
			
		}
		Object[] tourTypeIds = null;
		if (tourTypeId != null && !tourTypeId.equals("-1")) {
			tourTypeIds = new Object[]{tourTypeId};
		}
		
		PostalCodeHome pcHome = (PostalCodeHome) IDOLookup.getHome(PostalCode.class, getTourTypeHome().getDatasource());
		Collection pcoll = pcHome.findByPostalCodeFromTo(postalCodes[0], postalCodes[1]);
		
		Object[] sIds = null;
		if (supplier != null) {
			sIds = new Object[]{supplier.getPrimaryKey()};
		} else {
			SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
			Collection coller = sHome.findAll(supplierManager);
			if (coller != null && !coller.isEmpty()) {
				sIds = new Object[coller.size()];
				Iterator its = coller.iterator();
				Supplier s;
				int i = 0;
				while (its.hasNext()) {
					s = (Supplier) its.next();
					sIds[i++] = s.getPrimaryKey();
				}
			}
//			sIds = coller.toArray(new Object[]{});
		}
		
		Collection coll =  getProducts(fromStamp, toStamp, tourTypeIds, pcoll, sIds, null);
		Timer timer = new Timer();
		timer.start();
		if (coll != null && !coll.isEmpty()) {
			Collection pColl = new Vector();
			ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
			Iterator iter = coll.iterator();
			Tour tour;
			Product product;
			boolean checkValidity = isProductSearchCompleted(iwc);
			while (iter.hasNext()) {
				tour = (Tour) iter.next();
				product = pHome.findByPrimaryKey(tour.getPrimaryKey());
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
			timer.stop();
			System.out.println("[TourBrowser] time to check "+coll.size()+" products : "+timer.getTimeString());
			return pColl;
		}
		return coll;
	}
	
	private Collection getProducts(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] tourTypeId, Collection postalCodes, Object[] supplierId, String supplierName) throws FinderException, IDOLookupException {
		TourHome hHome = (TourHome) IDOLookup.getHome(Tour.class);
		return hHome.find(fromStamp, toStamp, tourTypeId, postalCodes, supplierId, supplierName);
	}

	private TourTypeHome getTourTypeHome() {
		try {
			return (TourTypeHome) IDOLookup.getHome(TourType.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}
	
	private TourCategoryHome getTourCategoryHome() {
		try {
			return (TourCategoryHome) IDOLookup.getHome(TourCategory.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	public boolean displaySupplierResults() {
		return false;
	}

	public Collection[] getExtraBookingFormElements(Product product, IWResourceBundle iwrb) {
		return null;
	}

	public Collection filterSuppliers(Collection suppliers, Group supplierManager, IWContext iwc, String[][] postalCodes, boolean onlineOnly, boolean useSearchPriceCategoryKey) {
		return suppliers;
	}

	public int addProductInfo(Product product, com.idega.presentation.Table table, int row, IWResourceBundle iwrb) {
		return row;
	}

	public String getLocalizationPrefix() {
		return "tour_browser_";
	}

}
