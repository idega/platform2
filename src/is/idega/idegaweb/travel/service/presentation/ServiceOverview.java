package is.idega.idegaweb.travel.service.presentation;
import javax.ejb.FinderException;
import java.rmi.*;
import java.sql.*;


import com.idega.block.trade.stockroom.data.*;
import com.idega.data.*;
import com.idega.presentation.*;
import is.idega.idegaweb.travel.business.*;

/**
 * <p>Title: idegaWeb TravelBooking</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: idega</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public interface ServiceOverview {
  public Table getServiceInfoTable(IWContext iwc, Product product) throws IDOFinderException, SQLException, ServiceNotFoundException, TimeframeNotFoundException, RemoteException, FinderException;
  public Table getPublicServiceInfoTable(IWContext iwc, Product product) throws FinderException, RemoteException;
}