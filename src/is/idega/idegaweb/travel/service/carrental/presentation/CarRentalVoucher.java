package is.idega.idegaweb.travel.service.carrental.presentation;

import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.presentation.Voucher;
import is.idega.idegaweb.travel.service.carrental.data.CarRentalBooking;
import is.idega.idegaweb.travel.service.carrental.data.CarRentalBookingHome;

import java.rmi.RemoteException;
import java.util.List;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class CarRentalVoucher extends Voucher{
	
	public CarRentalVoucher(Booking booking) throws Exception{
		super(booking);
	}
	
	protected void setupVoucher(IWContext iwc) throws RemoteException {
		IWResourceBundle iwrb = super.getResourceBundle();
		if (_booking.getAddress() != null) {
			super.addToClientInfo(iwrb.getLocalizedString("travel.address_lg","Address")+" : "+_booking.getAddress()+", "+_booking.getPostalCode()+" "+_booking.getCity()+", "+_booking.getCountry());
		}
		super.addToClientInfo(iwrb.getLocalizedString("travel.telephone_lg","Telephone number")+" : "+_booking.getTelephoneNumber());
		super.addToClientInfo(iwrb.getLocalizedString("travel.email_lg","E-mail")+" : "+_booking.getEmail());
		
		try {
			CarRentalBooking crBooking = ((CarRentalBookingHome) IDOLookup.getHome(CarRentalBooking.class)).findByPrimaryKey(_booking.getPrimaryKey());
			IWTimestamp pickupTime = new IWTimestamp(crBooking.getPickupTime());			
			IWTimestamp dropoffTime = new IWTimestamp(crBooking.getDropoffTime());			
			super.addToSectionFour(iwrb.getLocalizedString("travel.pickup","Pickup")+" : "+crBooking.getPickupPlace().getAddress().getStreetAddress()+" "+TextSoap.addZero(pickupTime.getHour())+":"+TextSoap.addZero(pickupTime.getMinute()));
			super.addToSectionFour(iwrb.getLocalizedString("travel.dropoff","Dropoff")+" : "+crBooking.getDropoffPlace().getAddress().getStreetAddress()+" "+TextSoap.addZero(dropoffTime.getHour())+":"+TextSoap.addZero(dropoffTime.getMinute()));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	protected void addPickupPlaces(Table table, List bookings, IWContext iwc) throws RemoteException {
	}

	protected void addBookingDates(Table table, List bookings, IWContext iwc) throws RemoteException {
		if (bookings.size() > 0) {
			IWResourceBundle iwrb = super.getResourceBundle(iwc);
			IWTimestamp fromStamp = new IWTimestamp(((Booking)bookings.get(0)).getBookingDate());
			IWTimestamp toStamp = new IWTimestamp(((Booking)bookings.get(bookings.size()-1)).getBookingDate());
			toStamp.addDays(1);
			table.add(getText(iwrb.getLocalizedString("travel.pickup_date","Pickup date")+" : "+fromStamp.getLocaleDate(iwc)), 1, 3);
			table.add(getText(Text.BREAK), 1, 3);
			table.add(getText(iwrb.getLocalizedString("travel.dropoff_date","Dropoff date")+" : "+toStamp.getLocaleDate(iwc)), 1, 3);
		}
	}
	
	
}
