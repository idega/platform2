package is.idega.idegaweb.travel.service.presentation;

import is.idega.idegaweb.travel.data.Contract;
import is.idega.idegaweb.travel.presentation.TravelManager;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
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

public class AbstractBookingOverview extends TravelManager implements BookingOverview{

  protected Supplier _supplier;
  protected Reseller _reseller;
  private Contract _contract;

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

	protected Contract getContract(IWContext iwc, Product product) throws RemoteException{
		if (_contract == null) {
			_contract = getContractBusiness(iwc).getContract(_reseller, product);
		}	
		return _contract;
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