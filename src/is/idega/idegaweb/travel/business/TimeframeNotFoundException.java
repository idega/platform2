package is.idega.idegaweb.travel.business;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TimeframeNotFoundException extends Exception {

  public TimeframeNotFoundException() {
      super("Timeframe not found");
  }
}