package is.idega.idegaweb.travel.service.tour.presentation;

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

public class TourVoucher extends Voucher{
  public TourVoucher(Booking booking) throws Exception{
    super(booking);
  }

  public void main(IWContext iwc) throws Exception{
    setupVoucher(iwc);
    super.main(iwc);
  }
  protected void setupVoucher(IWContext iwc) throws RemoteException {
    IWResourceBundle iwrb = super.getResourceBundle(iwc);
    super.addToClientInfo(iwrb.getLocalizedString("travel.email_lg","E-MAIL")+" : "+_booking.getEmail());
  }
}
