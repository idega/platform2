/*
 * $Id: SearchEventListener.java,v 1.2 2005/07/07 03:01:37 gimmi Exp $
 * Created on 29.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.block.search.business;

import is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm;
import java.rmi.RemoteException;
import java.util.Collection;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;


public class SearchEventListener implements IWPageEventListener {
	
	public boolean actionPerformed(IWContext iwc) throws IWException {
		
		ServiceSearchBusiness ssBus;
		try {
			ssBus = (ServiceSearchBusiness) IBOLookup.getServiceInstance(iwc, ServiceSearchBusiness.class);
		}
		catch (IBOLookupException e3) {
			throw new IWException("Cannot get service instance for class "+ServiceSearchBusiness.class.getName());
		}
		
		String[] newBasketIDs = iwc.getParameterValues(ServiceSearchBusinessBean.PARAMETER_BOOKING_IDS_FOR_BASKET);
		try {
			if (newBasketIDs != null) {
				ssBus.setNewBookingsInBasket(iwc, newBasketIDs);
			}
		}
		catch (RemoteException e2) {
			e2.printStackTrace();
		}
		
		String action = iwc.getParameter(AbstractSearchForm.ACTION);
		if (action == null) {
			action = "";
		}
		if (action != null && action.equals(AbstractSearchForm.ACTION_ADD_TO_BASKET)) {
			boolean success = false;
			try {
				success = ssBus.addToBasket(iwc);
				ssBus.getSearchSession(iwc).setAddToBasketSuccess(success);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			return success;
		} else if (action.equals(AbstractSearchForm.ACTION_CONFIRM)){
			try {
				Collection bookings = ssBus.doBasketBooking(iwc);
				ssBus.getSearchSession(iwc).setBookingsSavedFromBasket(bookings);
				return true;
			}
			catch (Exception e) {
				try {
					ssBus.getSearchSession(iwc).setException(e);
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				}
				return false;
			}
		} else if (action.equals(ServiceSearchBusinessBean.PARAMETER_BOOKING_ID_REMOVAL)) {
			try {
				ssBus.removeFromBasket(iwc);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
		
	}
}
