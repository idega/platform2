package is.idega.idegaweb.travel.service.tour.business;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TourNotFoundException extends Exception {

  public TourNotFoundException() {
      super("Tour not found");
  }
}