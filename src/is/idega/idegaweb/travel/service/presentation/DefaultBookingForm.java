package is.idega.idegaweb.travel.service.presentation;

import java.rmi.RemoteException;
import java.util.List;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.data.IDOException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.HiddenInput;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public class DefaultBookingForm extends BookingForm {

  public DefaultBookingForm(IWContext iwc, Product product) throws Exception{
  		super(iwc ,product);
  }

	protected void setupSpecialFieldsForBookingForm(Table table, int row, List errorFields) {
	}

	public String getParameterTypeCountName() {
		return parameterCountToCheck;
	}

	public void saveServiceBooking(IWContext iwc, int bookingId, IWTimestamp stamp) throws RemoteException,	IDOException {
	}
	protected int addPublicFromDateInput(IWContext iwc, Table table, int fRow) {
		table.add(getSmallText(iwrb.getLocalizedString("travel.date", "Date")), 1, fRow);
		table.add(new HiddenInput(parameterFromDate, _stamp.toSQLDateString()), 2, fRow);
		table.add(getOrangeText(_stamp.getLocaleDate(iwc)), 2, fRow++);
		return fRow;
	}
	
	protected int addPublicToDateInput(IWContext iwc, Table table, int fRow) {
		return fRow;
	}

	public String getUnitName() {
		return iwrb.getLocalizedString("travel.unit", "Unit");
	}
	
	public String getUnitNamePlural() {
		return iwrb.getLocalizedString("travel.units", "Units");
	}

	public boolean useNumberOfDays() {
		return true;
	}

	protected int addPublicExtraBookingInput(IWContext iwc, Table table, int fRow) {
		return fRow;
	}

	public String getNumberOfDaysString() {
		return iwrb.getLocalizedString("travel.number_of_days", "Number of days");
	}

	public String getPerDayString() {
		return iwrb.getLocalizedString("travel.search.per_day","per day");
	}

}
