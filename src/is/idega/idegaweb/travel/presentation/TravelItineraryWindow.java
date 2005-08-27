/*
 * $Id: TravelItineraryWindow.java,v 1.1 2005/08/27 15:36:14 gimmi Exp $
 * Created on Aug 27, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.IWContext;


public class TravelItineraryWindow extends TravelWindow {

	public static final String PARAMETER_BOOKING_ID = "tiw_bid";
	
	public TravelItineraryWindow() {
		super(600, 250);
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		String[] bids = iwc.getParameterValues(PARAMETER_BOOKING_ID);
		TravelBasket tb = new TravelBasket();
		tb.setBookingPKs(bids);
		tb.setShowDeleteLink(false);
		tb.setShowCheckoutLink(false);
		tb.setHeaderStyleClass("sbrowser_header");
		tb.setTextStyleClass("sbrowser_text");
		add(tb);
	}

}
