package is.idega.idegaweb.travel.service.business;

import java.rmi.RemoteException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.business.IBOService;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public interface BookingBusiness extends IBOService {

	/**
	 * @see is.idega.idegaweb.travel.service.business.BookingBusinessBean#isProductValid
	 */
	public boolean isProductValid(Product product, IWTimestamp from, IWTimestamp to) throws Exception,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.service.business.BookingBusinessBean#getIsProductValid
	 */
	public boolean getIsProductValid(IWContext iwc, Product product, IWTimestamp from, IWTimestamp to)
			throws Exception, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.service.business.BookingBusinessBean#getServiceHandler
	 */
	public ServiceHandler getServiceHandler() throws RemoteException, java.rmi.RemoteException;
}
