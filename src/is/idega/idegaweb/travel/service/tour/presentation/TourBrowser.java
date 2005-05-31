/*
 * $Id: TourBrowser.java,v 1.1 2005/05/31 19:35:57 gimmi Exp $
 * Created on 28.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.tour.presentation;

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
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;


public class TourBrowser extends TravelBlock implements SupplierBrowserPlugin {

	private static final String PARAMETER_TOUR_CATEGORY_ID = "tb_tc";
	private static final String PARAMETER_TOUR_TYPE_ID = "tb_tt";
	
	
	
	public boolean isProductSearchCompleted(IWContext iwc) {
		// TODO Auto-generated method stub
		return false;
	}

	public String[] getParameters() {
		return new String[]{PARAMETER_TOUR_CATEGORY_ID, PARAMETER_TOUR_TYPE_ID};
	}

	public Collection[] getSupplierSearchInputs(IWContext iwc, IWResourceBundle iwrb) {
		Collection txts = new Vector();
		Collection ios = new Vector();
		
//		txts.add(iwrb.getLocalizedString("travel.tour_category", "Tour Category"));
		txts.add(iwrb.getLocalizedString("travel.tour_type", "Tour Type"));
		
		DropdownMenu tourTypes = new DropdownMenu(PARAMETER_TOUR_TYPE_ID );
		DropdownMenu tourCats = new DropdownMenu(PARAMETER_TOUR_CATEGORY_ID );
		try {
			Iterator catIter = getTourCategoryHome().findAll().iterator();
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
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
//		ios.add(tourCats);
		ios.add(tourTypes);
		
		return new Collection[]{txts, ios};
	}

	public Collection[] getProductSearchInputs(IWContext iwc, IWResourceBundle iwrb) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getSuppplierSearchCriterias(IWContext iwc) throws IDOCompositePrimaryKeyException, IDORelationshipException {
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
		if (tourType != null && !tourType.equals("-1")) {
			Table tTourType = new Table(TourType.class);
			Column col = new Column(tTourType, tTourType.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
			coll.add(new JoinCriteria(tour, tTourType));
			coll.add(new MatchCriteria(col, MatchCriteria.EQUALS, new Integer(tourType)));
		}

		//		String rooType = iwc.getParameter(PARAMETER_ROOM_TYPE);
//		if (rooType != null && !rooType.equals("-1")) {
//			Table roomType = new Table(RoomType.class);
//			Column col = new Column(roomType, roomType.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
//			coll.add(new JoinCriteria(hotel, roomType));
//			coll.add(new MatchCriteria(col, MatchCriteria.EQUALS, new Integer(rooType)));
//		}
		
		return coll;	
	}

	public Collection getProducts(Supplier supplier, IWContext iwc) throws IDOLookupException, FinderException {
		String tourTypeId = iwc.getParameter(PARAMETER_TOUR_TYPE_ID);
		Object[] tourTypeIds = null;
		if (tourTypeId != null) {
			tourTypeIds = new Object[]{tourTypeId};
		}
		
		Collection coll =  getProducts(null, null, tourTypeIds, null, new Object[]{supplier.getPrimaryKey()}, null);
		if (coll != null && !coll.isEmpty()) {
			Collection pColl = new Vector();
			ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
			Iterator iter = coll.iterator();
			Tour tour;
			Product product;
			boolean checkValidity = isProductSearchCompleted(iwc);
			while (iter.hasNext()) {
				tour = (Tour) iter.next();
				product =pHome.findByPrimaryKey(tour.getPrimaryKey());
				try {
					if (checkValidity) {
						IWTimestamp fromStamp = null;
						IWTimestamp toStamp = null;
						if (getBookingBusiness(iwc).getIsProductValid(iwc, product, fromStamp, toStamp)) {
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
}
