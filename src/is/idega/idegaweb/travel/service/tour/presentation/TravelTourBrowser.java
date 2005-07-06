/*
 * $Id: TravelTourBrowser.java,v 1.2 2005/07/06 02:22:33 gimmi Exp $
 * Created on Jul 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.tour.presentation;

import is.idega.idegaweb.travel.presentation.TravelSupplierBrowser;
import is.idega.idegaweb.travel.service.tour.data.TourCategory;
import is.idega.idegaweb.travel.service.tour.data.TourCategoryHome;
import is.idega.idegaweb.travel.service.tour.data.TourType;
import is.idega.idegaweb.travel.service.tour.data.TourTypeHome;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;


public class TravelTourBrowser extends TravelSupplierBrowser {
	
	protected Class getPlugin() {
		return TourBrowser.class;
	}

	protected Table getHeaderTable(IWContext iwc) {
		Table table = getTable();
		
		try {
			TourCategoryHome tcHome = (TourCategoryHome) IDOLookup.getHome(TourCategory.class);
			TourTypeHome ttHome = (TourTypeHome) IDOLookup.getHome(TourType.class);
			SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
			Collection allSupps = sHome.findAll(getSupplierManager());

			Collection cats = tcHome.findAll();
			Iterator citer = cats.iterator();
			TourCategory cat;
			TourType type;
			Link link;
			int row = 1;
			table.mergeCells(1, row, 2, row);
			table.setRowColor(row, backgroundColor);
			table.add(getHeaderText(getResourceBundle().getLocalizedString("travel.tour_types", "Tour types")), 1, row++);
			while (citer.hasNext()) {
				cat = (TourCategory) citer.next();
				Collection types = ttHome.findByCategoryUsedBySuppliers(cat.getPrimaryKey().toString(), allSupps);
				Iterator titer = types.iterator();
				boolean first = true;
				while (titer.hasNext()) {
					type = (TourType) titer.next();
					if (first) {
						table.add(getText(getResourceBundle().getLocalizedString(cat.getLocalizationKey(), cat.getName())), 1, row);
						table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
						first = false;
					} else {
						table.add(getText(", "), 2, row);
					}
					link = new Link(getResourceBundle().getLocalizedString(type.getLocalizationKey(), type.getName()));
					link.addParameter(TourBrowser.PARAMETER_FORCED_TOUR_TYPE_ID, type.getPrimaryKey().toString());
					table.add(link, 2, row);
					table.setRowColor(row, GRAY);
					table.setWidth(1, row, 120);
				}
				if (!first) {
					++row; 
				}
			}
//			table.add("gimmi");
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (IDOCompositePrimaryKeyException e) {
			e.printStackTrace();
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		
		
		return table;
	}

	protected boolean showBrowser(IWContext iwc) {
		return iwc.isParameterSet(TourBrowser.PARAMETER_FORCED_TOUR_TYPE_ID);
	}

}
