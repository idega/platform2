package is.idega.idegaweb.travel.service.hotel.presentation;

import is.idega.idegaweb.travel.service.presentation.BookingForm;
import com.idega.presentation.IWContext;
import com.idega.block.trade.stockroom.data.*;

/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class HotelBookingForm extends BookingForm {

  public HotelBookingForm(IWContext iwc, Product product) throws Exception{
    super(iwc, product);
  }
}
