package is.idega.idegaweb.travel.service.presentation;

import java.util.*;
import com.idega.block.trade.stockroom.data.*;
import java.sql.SQLException;
import is.idega.idegaweb.travel.data.Contract;
import com.idega.util.IWTimestamp;
import java.rmi.*;

import javax.ejb.*;

import com.idega.presentation.*;
import is.idega.idegaweb.travel.presentation.*;

/**
 * <p>Title: idegaWeb TravelBooking</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: idega</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class AbstractBookingOverview extends TravelManager implements BookingOverview{

  protected Supplier _supplier;
  protected Reseller _reseller;
  protected Contract _contract;

  private String PARAMETER_FROM_STAMP = "active_from";
  private String PARAMETER_TO_STAMP = "active_to";
  protected String PARAMETER_VIEW_ALL = "-109";
  protected String PARAMETER_CLOSER_LOOK_DATE = "viewServiceDate";

//  private String closerLookDateParameter = "viewServiceDate";



  public AbstractBookingOverview() {
  }
  public AbstractBookingOverview(IWContext iwc) throws RemoteException{
    this.main(iwc);
  }

  public void main(IWContext iwc) throws RemoteException{
    super.initializer(iwc);
    this.initialize(iwc);
  }



  private void initialize(IWContext iwc) throws RemoteException {
    _supplier = super.getTravelSessionManager(iwc).getSupplier();
    _reseller = super.getTravelSessionManager(iwc).getReseller();


  }

  protected Contract getContract(Product product) throws RemoteException{
    if ((_reseller != null) && (product != null)){
      try {
          Contract[] contracts = (Contract[]) (is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class)).findAllByColumn(is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId(), Integer.toString(_reseller.getID()), is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId(), Integer.toString(product.getID()) );
          if (contracts.length > 0) {
            return contracts[0];
          }

      }catch (SQLException sql) {
          sql.printStackTrace(System.err);
      }

    }
    return null;
  }

  public Table getBookingOverviewTable(IWContext iwc, Collection products) throws CreateException, RemoteException, FinderException {
    Table table = new Table();
    table.add(getHeaderText("Unimplemented"));
    return table;
  }

  public Table getDetailedInfo(IWContext iwc, Product product, IWTimestamp stamp) throws FinderException, CreateException, RemoteException {
    Table table = new Table();
    table.add(getHeaderText("Unimplemented"));
    return table;
  }

  // BUSINESS

  public IWTimestamp getTimestampFrom(IWContext iwc) {
    IWTimestamp stamp = null;
    String from_time = iwc.getParameter(PARAMETER_FROM_STAMP);
    if (from_time!= null) {
      stamp = new IWTimestamp(from_time);
    } else {
      stamp = IWTimestamp.RightNow();
    }
    return stamp;
  }

  // BUSINESS
  public IWTimestamp getTimestampTo(IWContext iwc) {
    IWTimestamp stamp = null;
    String from_time = iwc.getParameter(PARAMETER_TO_STAMP);
    if (from_time!= null) {
      stamp = new IWTimestamp(from_time);
    } else {
      stamp = IWTimestamp.RightNow();
      stamp.addDays(15);
    }
    return stamp;
  }


}