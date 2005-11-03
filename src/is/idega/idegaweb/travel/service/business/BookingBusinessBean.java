package is.idega.idegaweb.travel.service.business;

import is.idega.idegaweb.travel.service.presentation.BookingForm;
import java.rmi.RemoteException;
import java.util.Collection;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.business.ProductPriceBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public class BookingBusinessBean extends IBOServiceBean  implements BookingBusiness{

	public boolean isProductValid(Product product, IWTimestamp from, IWTimestamp to) throws Exception{
		IWTimestamp tmp = new IWTimestamp(from);
		boolean productIsValid = true;
		BookingForm bf = getServiceHandler().getBookingForm((IWContext) getIWApplicationContext(), product);
		while ( tmp.isEarlierThan(to) && productIsValid) {
			/** Checking if day is available */
			productIsValid = getServiceHandler().getServiceBusiness(product).getIfDay((IWContext) getIWApplicationContext(), product, getProductBusiness().getTimeframes(product), tmp, false, true);
			
			if (productIsValid) {
				productIsValid = !bf.isFullyBooked((IWContext) getIWApplicationContext(), product, tmp);
			}
			if (productIsValid) {
				productIsValid = !bf.isUnderBooked((IWContext) getIWApplicationContext(), product, tmp);
			}
			//productIsValid = (bf.checkBooking(iwc, false, false, false) >= 0);
			//productIsValid = bus.getIfDay(iwc, product, tmp);
			tmp.addDays(1);
		}
		return productIsValid;
		//return getServiceHandler().getServiceBusiness(product).getIfDay(iwc, product, product.getTimeframes(), tmp, false, true);
	}

	public boolean getIsProductValid(IWContext iwc, Product product, IWTimestamp from, IWTimestamp to, boolean onlineOnly, boolean useSearchPriceCategoryKey) throws Exception {
		return getIsProductValid(iwc, product, from, to, 1, onlineOnly, useSearchPriceCategoryKey);
	}

	public boolean getIsProductValid(IWContext iwc, Product product, IWTimestamp from, IWTimestamp to, int numberOfUnits, boolean onlineOnly, boolean useSearchPriceCategoryKey) throws Exception {
		IWTimestamp tmp;
		Collection addresses;
		int addressId;
		int timeframeId;
		Timeframe timeframe;
		BookingForm bf;
		Collection prices;
		boolean productIsValid	 = true;
//		System.out.println("Checking product = "+product.getProductName(iwc.getCurrentLocaleId()));
		bf = getServiceHandler().getBookingForm(iwc, product, false);
		addresses = getServiceHandler().getProductBusiness().getDepartureAddresses(product, from, true);
		addressId = -1;
		timeframeId = -1;
		timeframe = getServiceHandler().getProductBusiness().getTimeframe(product, from, addressId);
		if (timeframe != null) {
			timeframeId = timeframe.getID();
		}

		String key = null;
		if (useSearchPriceCategoryKey) {
			key = bf.getPriceCategorySearchKey();
		}
		prices = getProductPriceBusiness().getProductPrices(product.getID(), timeframeId, addressId, onlineOnly, key, null);


		if (prices != null && !prices.isEmpty()) { 
//			System.out.println("BookingBusinessBean found prices : "+prices.length);
			/** Not inserting product without proper price categories */
			tmp = new IWTimestamp(from);
			productIsValid = true;
			while ( tmp.isEarlierThan(to) && productIsValid) {
				/** Checking if day is available */
				productIsValid = getServiceHandler().getServiceBusiness(product).getIfDay(iwc, product, getProductBusiness().getTimeframes(product), tmp, false, true);
				if (productIsValid) {
					productIsValid = !bf.isFullyBooked(iwc, product, tmp, numberOfUnits);
				}
				if (productIsValid) {
					productIsValid = !bf.isUnderBooked(iwc, product, tmp);
				}
				tmp.addDays(1);
			}
			return productIsValid;
		} else {
		}
		return false;
	}	
	
	public ProductPriceBusiness getProductPriceBusiness() {
		try {
			return (ProductPriceBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductPriceBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public ProductBusiness getProductBusiness() {
		try {
			return (ProductBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public ServiceHandler getServiceHandler() throws RemoteException {
		ServiceHandler sh = (ServiceHandler) IBOLookup.getServiceInstance(getIWApplicationContext(), ServiceHandler.class);
		return sh;
	}	
}
