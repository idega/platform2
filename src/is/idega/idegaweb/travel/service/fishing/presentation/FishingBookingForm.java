package is.idega.idegaweb.travel.service.fishing.presentation;

import java.rmi.RemoteException;

import com.idega.block.trade.stockroom.data.*;
import com.idega.data.IDOException;
import com.idega.presentation.*;
import com.idega.util.IWTimestamp;

import is.idega.idegaweb.travel.service.presentation.*;

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
  
  
	public void saveServiceBooking(
		IWContext iwc,
		int bookingId,
		IWTimestamp stamp)
		throws RemoteException, IDOException {

	}

}
