package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.block.creditcard.business.CreditCardBusinessBean;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;


/**
 * @author gimmi
 */
public class RefundReport extends TravelManager implements AdministratorReport {

	private IWResourceBundle iwrb = null;
	
	private int CC_CLIENT_TYPE = CreditCardBusinessBean.CLIENT_TYPE_TPOS;
	
	private static final String PARAMETER_CC_CLIENT_TYPE = "cc_c_t";
	
	public RefundReport(IWContext iwc) throws Exception {
		iwrb = this.getTravelSessionManager(iwc).getIWResourceBundle();
		
		String ccType = iwc.getParameter(PARAMETER_CC_CLIENT_TYPE);
		if (ccType != null) {
			try {
				CC_CLIENT_TYPE = Integer.parseInt(ccType);
			} catch (NumberFormatException ignore) {}
		}
		
	}
	
	public boolean useTwoDates() {
		return true;
	}

	public PresentationObject getAdministratorReport(List suppliers, IWContext iwc, IWTimestamp stamp)	throws RemoteException, FinderException {
		IWTimestamp toStamp = new IWTimestamp(stamp);
		toStamp.addDays(7);
		return getAdministratorReport(suppliers, iwc, stamp, toStamp);
	}

	public PresentationObject getAdministratorReport(List suppliers, IWContext iwc, IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException, FinderException {
		
		Collection refunds = getCreditCardBusiness(iwc).getAllRefunds(fromStamp, toStamp, CC_CLIENT_TYPE); 
		
    Table table = super.getTable();
    	table.setWidth("90%");
    	table.setAlignment(Table.HORIZONTAL_ALIGN_CENTER);
    int row = 1;
    
    DropdownMenu types = new DropdownMenu(PARAMETER_CC_CLIENT_TYPE);
    types.addMenuElement(CreditCardBusinessBean.CLIENT_TYPE_TPOS, "TPOS");
    types.addMenuElement(CreditCardBusinessBean.CLIENT_TYPE_KORTATHJONUSTAN, "KORTATHJONUSTAN");
    types.setSelectedElement(CC_CLIENT_TYPE);
    types.setToSubmit();
    table.add(types, 5, row);
    table.setAlignment(5, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(row, super.backgroundColor);
    
    ++row;
    table.add(getHeaderText(iwrb.getLocalizedString("travel.date","Date")), 1, row);
    table.add(getHeaderText(iwrb.getLocalizedString("travel.supplier","Supplier")), 2, row);
    table.add(getHeaderText(iwrb.getLocalizedString("travel.price","Price")), 3, row);
    table.add(getHeaderText(iwrb.getLocalizedString("travel.card_type","Card type")), 4, row);
    table.add(getHeaderText(iwrb.getLocalizedString("travel.oroginal_payment","Original payment")), 5, row);
    table.setRowColor(row, super.backgroundColor);
    
    CreditCardAuthorizationEntry entry;
    CreditCardAuthorizationEntry parent;
    GeneralBooking booking;
    GeneralBookingHome bookingHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
    Supplier supplier;
    IWTimestamp entryStamp;
    IWTimestamp parentStamp;
    double totalAmount = 0;
    double amount = 0;
    double parentAmount = 0;
    Iterator iter = refunds.iterator();
    while (iter.hasNext()) {
    		entry = (CreditCardAuthorizationEntry) iter.next();
    		parent = entry.getParent();
    		++row;
    		entryStamp = new IWTimestamp(entry.getDate());
    		amount = entry.getAmount();
    		amount /= 100;
    		totalAmount += amount;
    		//parent.getAuthorizationCode()
	    table.add(getText(entryStamp.getLocaleDate(iwc)), 1, row);
	    table.add(getText(TextSoap.decimalFormat(amount, 2)+" "+entry.getCurrency()), 3, row);
      table.setRowColor(row, super.GRAY);
	  		if (parent != null) {
	    		parentStamp = new IWTimestamp(parent.getDate());
	    		parentAmount = parent.getAmount();
	    		parentAmount /= 100;
	    		booking = bookingHome.findByAuthorizationNumber(parent.getAuthorizationCode(), new IWTimestamp(parent.getDate()));
	  	    table.add(getText(booking.getService().getProduct().getSupplier().getName()), 2, row);
	  	    table.add(getText(parent.getBrandName()), 4, row);
	  	    table.add(getText(TextSoap.decimalFormat(parentAmount, 2)+" "+entry.getCurrency()+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+parentStamp.getLocaleDate(iwc)), 5, row);
	  		}

    }

    ++row;
    table.add(getText(TextSoap.decimalFormat(totalAmount, 2)+" ISK"), 3, row);
    table.setRowColor(row, super.GRAY);
    
    table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
    table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_RIGHT);
    
    return table;
	}
	
	public String getReportName() {
		return iwrb.getLocalizedString("travel.report.refund_report","RefundReport");
	}

	public String getReportDescription() {
		return iwrb.getLocalizedString("travel.report.refund_report_description","Show the refunds made over a period of time");
	}
}
