package is.idega.idegaweb.travel.presentation;
import is.idega.idegaweb.travel.interfaces.Booking;
import com.idega.presentation.IWContext;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class OnlineBookingReport extends BookingReport implements Report, AdministratorReport{

  public OnlineBookingReport(IWContext iwc) throws Exception{
	  super(iwc);
  }

  public String getReportName() {
    return _iwrb.getLocalizedString("travel.report_name_online_booking_report","Online-booking report");
  }
  public String getReportDescription() {
    return _iwrb.getLocalizedString("travel_report_description_online_booking_report","Displays booking made online");
  }
  
  public int[] getBookingTypeIds() {
	  return new int[]{Booking.BOOKING_TYPE_ID_ONLINE_BOOKING};
  }

}