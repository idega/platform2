package is.idega.idegaweb.travel.service.fishing.presentation;

import is.idega.idegaweb.travel.presentation.Voucher;
import is.idega.idegaweb.travel.interfaces.Booking;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWResourceBundle;
import java.rmi.RemoteException;

/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class FishingVoucher extends Voucher{

  public FishingVoucher(Booking booking) throws Exception{
    super(booking);
  }

  protected void setupVoucher(IWContext iwc) throws RemoteException {
    IWResourceBundle iwrb = super.getResourceBundle();
    if (_booking.getAddress() != null) {
    	super.addToClientInfo(iwrb.getLocalizedString("travel.address_lg","Address")+" : "+_booking.getAddress()+", "+_booking.getPostalCode()+" "+_booking.getCity()+", "+_booking.getCountry());
    }
    super.addToClientInfo(iwrb.getLocalizedString("travel.telephone_lg","Telephone number")+" : "+_booking.getTelephoneNumber());
    super.addToClientInfo(iwrb.getLocalizedString("travel.email_lg","E-mail")+" : "+_booking.getEmail());
  }
}
