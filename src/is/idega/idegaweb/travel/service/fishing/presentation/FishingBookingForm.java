package is.idega.idegaweb.travel.service.fishing.presentation;

import is.idega.idegaweb.travel.service.presentation.BookingForm;
import java.rmi.RemoteException;
import java.util.List;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.data.IDOException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.HiddenInput;
import com.idega.util.IWTimestamp;

/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class FishingBookingForm extends BookingForm {

  public FishingBookingForm(IWContext iwc, Product product) throws Exception{
    super(iwc, product);
  }
  
	public void saveServiceBooking(	IWContext iwc, int bookingId,		IWTimestamp stamp) throws RemoteException, IDOException {
	}

	protected void setupSpecialFieldsForBookingForm(Table table, int row, List errorFields) {
	}

	public String getParameterTypeCountName() {
		return parameterCountToCheck;
	}

	protected int addPublicFromDateInput(IWContext iwc, Table table, int fRow) {
		table.add(getSmallText(iwrb.getLocalizedString("travel.date_of_arrival", "Date of arrival")), 1, fRow);
		table.add(new HiddenInput(parameterFromDate, _stamp.toSQLDateString()), 2, fRow);
		table.add(getOrangeText(_stamp.getLocaleDate(iwc)), 2, fRow++);
		return fRow;
	}
	
	protected int addPublicToDateInput(IWContext iwc, Table table, int fRow) {
		table.add(getSmallText(iwrb.getLocalizedString("travel.date_of_departure", "Date of departure")), 1, fRow);
		DateInput inp = new DateInput(parameterToDate);
		IWTimestamp toS = new IWTimestamp(_stamp);
		toS.addDays(1);
		inp.setDate(toS.getDate());
		table.add(getStyleObject(inp, getStyleName(BookingForm.STYLENAME_INTERFACE)), 2, fRow++);
		return fRow;
	}

	public String getUnitName() {
		return iwrb.getLocalizedString("travel.fishing_rod", "Fishing rod");
	}
	
	public String getUnitNamePlural() {
		return iwrb.getLocalizedString("travel.fishing_rods", "Fishing rods");
	}
	
	public boolean useNumberOfDays() {
		return true;
	}

	protected int addPublicExtraBookingInput(IWContext iwc, Table table, int fRow) {
		return fRow;
	}
}
