package is.idega.idegaweb.travel.block.search.presentation;

import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingBMPBean;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.presentation.AdministratorReport;
import is.idega.idegaweb.travel.presentation.OnlineBookingReport;
import is.idega.idegaweb.travel.presentation.Report;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class SearchEngineBookingReport extends OnlineBookingReport implements Report, AdministratorReport{

	private ServiceSearchEngine engine = null;
	
	public SearchEngineBookingReport(IWContext iwc) throws Exception {
		super(iwc);
	}

	protected Booking[] getBookings(IWContext iwc, IWTimestamp fromStamp,	IWTimestamp toStamp, List products) throws RemoteException, FinderException {

		//return getBooker(iwc).getBookings(products, new int[] {Booking.BOOKING_TYPE_ID_ONLINE_BOOKING}, fromStamp, toStamp, null, null, searchByDateOfBooking);

		String dateColumn = GeneralBookingBMPBean.getBookingDateColumnName();
		if (searchByDateOfBooking) {
			dateColumn = GeneralBookingBMPBean.getDateOfBookingColumnName();
		}
		
		int[] productIds = new int[]{};
		if (products != null && !products.isEmpty()) {
			int productSize = products.size();
			productIds = new int[productSize];
			for (int i = 0; i < productSize; i++) {
				productIds[i] = ((Product) products.get(i)).getID();
			}
		}
		
		Collection coll = getGeneralBookingHome().findBookings(productIds, fromStamp, toStamp, new int[] {Booking.BOOKING_TYPE_ID_ONLINE_BOOKING}, null, null, null, dateColumn, engine.getCode());
		if (coll != null) {
			return (Booking[]) coll.toArray(new Booking[]{});
		} else {
			return new Booking[]{};
		}
		/*
		if (searchByDateOfBooking) {
		  return this.collectionToBookingsArray(getGeneralBookingHome().findBookingsByDateOfBooking(ids, fromStamp, toStamp,bookingTypeIds, columnName, columnValue, null));
		}else {
		  return this.collectionToBookingsArray(getGeneralBookingHome().findBookings(ids, fromStamp, toStamp,bookingTypeIds, columnName, columnValue, null));
		}*/
//this.getDateOfBookingColumnName()
		
		//  private Collection ejbFindBookings(int[] serviceIds, IWTimestamp fromStamp, 
		//IWTimestamp toStamp,int[] bookingTypeIds, String columnName, String columnValue, 
		//TravelAddress address, String dateColumn, String code) 
		//throws FinderException, RemoteException{

		//return super.getBookings(iwc, fromStamp, toStamp, products);
	}

	public PresentationObject getAdministratorReport(List suppliers, IWContext iwc, IWTimestamp stamp) throws RemoteException, FinderException {
		return getAdminReport(iwc, suppliers, stamp, null);
	}
	
	public PresentationObject getAdministratorReport(List suppliers, IWContext iwc, IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException, FinderException {
	  isAdminReport = true;
	  String view_supplier = iwc.getParameter(PARAMETER_VIEW_SUPPLIER);
	  if (view_supplier == null) {
	  	return getAdminReport(iwc, suppliers, fromStamp, toStamp);
	  } else {
	  	SupplierHome sHome = (SupplierHome) IDOLookup.getHomeLegacy(Supplier.class);
	  	_supplier = sHome.findByPrimaryKey(new Integer(view_supplier));
	  	List products = getProductBusiness(iwc).getProducts(iwc, Integer.parseInt(view_supplier));

	  	return getReport(iwc, products, fromStamp, toStamp);	
	  }

	}
/*
	private Collection getSuppliers() {
		Collection coll = new Vector();
		  try {
			coll = engine.getSuppliers();
		  } catch (IDORelationshipException e) {
			e.printStackTrace();
		  }
		return coll;
	}*/

	public void setSearchEngine(ServiceSearchEngine engine) {
		this.engine = engine;
	}
	
	public GeneralBookingHome getGeneralBookingHome() throws RemoteException {
		return (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
	}
}
