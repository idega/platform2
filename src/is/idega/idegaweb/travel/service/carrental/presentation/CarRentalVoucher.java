package is.idega.idegaweb.travel.service.carrental.presentation;

import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.presentation.Voucher;
import is.idega.idegaweb.travel.service.carrental.data.CarRentalBooking;
import is.idega.idegaweb.travel.service.carrental.data.CarRentalBookingHome;

import java.rmi.RemoteException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
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
    super.addToClientInfo(iwrb.getLocalizedString("travel.address_lg","Address")+" : "+_booking.getAddress()+", "+_booking.getPostalCode()+" "+_booking.getCity()+", "+_booking.getCountry());
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
}
