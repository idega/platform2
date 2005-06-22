package is.idega.idegaweb.travel.service.hotel.presentation;

import is.idega.idegaweb.travel.presentation.Voucher;
import is.idega.idegaweb.travel.interfaces.Booking;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;
import com.idega.idegaweb.IWResourceBundle;
import java.rmi.RemoteException;
import java.util.List;

/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class HotelVoucher extends Voucher {

  public HotelVoucher(Booking booking) throws Exception{
    super(booking);
  }

  protected void addBookingDates(Table table, List bookings, IWContext iwc) throws RemoteException {
		if (bookings.size() > 0) {
			IWResourceBundle iwrb = super.getResourceBundle(iwc);
		  IWTimestamp fromStamp = new IWTimestamp(((Booking)bookings.get(0)).getBookingDate());
		  IWTimestamp toStamp = new IWTimestamp(((Booking)bookings.get(bookings.size()-1)).getBookingDate());
		  toStamp.addDays(1);
		  table.add(getText(iwrb.getLocalizedString("travel.arrival_date","Arrival date")+" : "+fromStamp.getLocaleDate(iwc)), 1, 3);
		  table.add(getText(Text.BREAK), 1, 3);
		  table.add(getText(iwrb.getLocalizedString("travel.departure_date","Departure date")+" : "+toStamp.getLocaleDate(iwc)), 1, 3);
		}
	}

  protected void setupVoucher(IWContext iwc) throws RemoteException {
    IWResourceBundle iwrb = super.getResourceBundle(iwc);
    super.addToClientInfo(iwrb.getLocalizedString("travel.address_lg","Address")+" : "+_booking.getAddress()+", "+_booking.getPostalCode()+" "+_booking.getCity()+", "+_booking.getCountry());
    super.addToClientInfo(iwrb.getLocalizedString("travel.telephone_lg","Telephone number")+" : "+_booking.getTelephoneNumber());
    super.addToClientInfo(iwrb.getLocalizedString("travel.email_lg","E-mail")+" : "+_booking.getEmail());
  }
}
