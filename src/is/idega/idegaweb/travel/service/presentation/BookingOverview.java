package is.idega.idegaweb.travel.service.presentation;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.util.IWTimestamp;

/**
 * <p>Title: idegaWeb TravelBooking</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: idega</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public interface BookingOverview {
  public Table getBookingOverviewTable(IWContext iwc, Collection products) throws CreateException, RemoteException, FinderException;
  public Table getDetailedInfo(IWContext iwc, Product product, IWTimestamp stamp) throws FinderException, CreateException, RemoteException;
}