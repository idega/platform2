package is.idega.idegaweb.travel.service.presentation;
import is.idega.idegaweb.travel.business.ServiceNotFoundException;
import java.sql.SQLException;
import com.idega.data.IDOFinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import is.idega.idegaweb.travel.business.TimeframeNotFoundException;
import java.rmi.RemoteException;
import com.idega.util.IWCalendar;
import is.idega.idegaweb.travel.data.ServiceDayBMPBean;

/**
 * <p>Title: idegaWeb TravelBooking</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: idega</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public interface ServiceOverview {

  public Table getServiceInfoTable(IWContext iwc, Product product) throws IDOFinderException, SQLException, ServiceNotFoundException, TimeframeNotFoundException, RemoteException;

}