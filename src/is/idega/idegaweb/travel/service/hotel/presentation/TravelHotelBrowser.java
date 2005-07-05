/*
 * $Id: TravelHotelBrowser.java,v 1.1 2005/07/05 22:53:05 gimmi Exp $
 * Created on Jun 30, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.hotel.presentation;

import is.idega.idegaweb.travel.presentation.TravelSupplierBrowser;


public class TravelHotelBrowser extends TravelSupplierBrowser {

	protected Class getPlugin() {
		return HotelBrowser.class;
	}


}
