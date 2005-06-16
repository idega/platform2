package is.idega.idegaweb.travel.service.business;

import is.idega.idegaweb.travel.service.presentation.BookingForm;
import java.rmi.RemoteException;
import java.util.Collection;
import com.idega.block.trade.stockroom.data.PriceCategoryBMPBean;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.ProductPriceHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.util.Timer;


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
			productIsValid = getServiceHandler().getServiceBusiness(product).getIfDay((IWContext) getIWApplicationContext(), product, product.getTimeframes(), tmp, false, true);
			
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

	public boolean getIsProductValid(IWContext iwc, Product product, IWTimestamp from, IWTimestamp to) throws Exception {
		IWTimestamp tmp;
		Collection addresses;
		int addressId;
		int timeframeId;
		Timeframe timeframe;
		BookingForm bf;
		Collection prices;
		boolean productIsValid	 = true;
		Timer t = new Timer();
		t.start();
		//System.out.println("Checking product = "+product.getProductName(iwc.getCurrentLocaleId()));
		bf = getServiceHandler().getBookingForm(iwc, product, false);
//		addresses = getServiceHandler().getProductBusiness().getDepartureAddresses(product, from, true);
		t.stop();
		System.out.println("[BookingBusiness] check 1b : "+t.getTimeString());
		t.start();
		addressId = -1;
		timeframeId = -1;
		timeframe = getServiceHandler().getProductBusiness().getTimeframe(product, from, addressId);
//		t.stop();
//		System.out.println("[BookingBusiness] check 1c : "+t.getTimeString());
//		t.start();
		if (timeframe != null) {
			timeframeId = timeframe.getID();
		}
//		t.stop();
//		System.out.println("[BookingBusiness] check 1 : "+t.getTimeString());
//		t.start();
//		System.out.println("BookingBusinessBean checking product");
		ProductPriceHome ppHome = (ProductPriceHome) IDOLookup.getHome(ProductPrice.class);
		prices = ppHome.findProductPrices(product.getID(), timeframeId, addressId, new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_PUBLIC, PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC}, bf.getPriceCategorySearchKey());
		t.stop();
		System.out.println("[BookingBusiness] check 2 : "+t.getTimeString());
//		t.start();
		
		if (prices != null && !prices.isEmpty()) { 
//			System.out.println("BookingBusinessBean found prices : "+prices.length);
			/** Not inserting product without proper price categories */
			tmp = new IWTimestamp(from);
			productIsValid = true;
			while ( tmp.isEarlierThan(to) && productIsValid) {
				/** Checking if day is available */
//				t.stop();
//				System.out.println("[BookingBusiness] check 3 ("+tmp.toSQLDateString()+") : "+t.getTimeString());
//				t.start();
				productIsValid = getServiceHandler().getServiceBusiness(product).getIfDay(iwc, product, product.getTimeframes(), tmp, false, true);
//				System.out.println("BookingBusinessBean productIsValid 1 = "+productIsValid);
				
				if (productIsValid) {
					productIsValid = !bf.isFullyBooked(iwc, product, tmp);
				}
//				System.out.println("BookingBusinessBean productIsValid 2 = "+productIsValid);
				if (productIsValid) {
					productIsValid = !bf.isUnderBooked(iwc, product, tmp);
				}
//				System.out.println("BookingBusinessBean productIsValid 3 = "+productIsValid);
				//productIsValid = (bf.checkBooking(iwc, false, false, false) >= 0);
				//productIsValid = bus.getIfDay(iwc, product, tmp);
				tmp.addDays(1);
			}
			return productIsValid;
		} else {
//			System.out.println("BookingBusinessBean no prices found, type = "+bf.getPriceCategorySearchKey());
		}
		return false;
	}	
	public ServiceHandler getServiceHandler() throws RemoteException {
		return (ServiceHandler) IBOLookup.getServiceInstance(getIWApplicationContext(), ServiceHandler.class);
	}	
}
