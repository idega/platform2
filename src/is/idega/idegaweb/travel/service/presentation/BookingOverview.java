package is.idega.idegaweb.travel.service.presentation;
import com.idega.util.IWTimestamp;
import com.idega.block.trade.stockroom.data.Product;
import java.util.Collection;
import java.util.List;
import java.rmi.*;

import javax.ejb.*;

import com.idega.presentation.*;

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