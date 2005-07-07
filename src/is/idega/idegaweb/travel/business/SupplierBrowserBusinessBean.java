/*
 * $Id: SupplierBrowserBusinessBean.java,v 1.1 2005/07/07 02:59:05 gimmi Exp $
 * Created on Jul 6, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.business;

import is.idega.idegaweb.travel.data.CashierQueue;
import is.idega.idegaweb.travel.data.CashierQueueHome;
import is.idega.idegaweb.travel.data.GeneralBooking;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import com.idega.block.basket.business.BasketBusiness;
import com.idega.block.basket.data.BasketEntry;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORuntimeException;
import com.idega.user.data.Group;
import com.idega.user.data.User;


public class SupplierBrowserBusinessBean extends IBOServiceBean  implements SupplierBrowserBusiness{
	
	public void sendToCashier(Group supplierManager, String clientName, User cashier, User performer, BasketBusiness basketBusiness) throws CreateException, RemoteException, IDOAddRelationshipException {
		CashierQueue queue = getCashierQueueHome().create();
		if (cashier != null) {
			queue.setCashier(cashier);
		}
		if (performer != null) {
			queue.setOwner(performer);
		}
		if (supplierManager != null) { 
			queue.setSupplierManager(supplierManager);
		}
		if (clientName != null) {
			queue.setClientName(clientName);
		}
		queue.store();
		
		Collection entries = basketBusiness.getBasket().values();
		Iterator iter = entries.iterator();
		BasketEntry entry;
		GeneralBooking booking;
		while (iter.hasNext()) {
			entry = (BasketEntry) iter.next();
			booking = (GeneralBooking) entry.getItem();
			booking.setIsValid(true);
			booking.store();
			queue.addBooking(booking);
		}
		basketBusiness.emptyBasket();
	}
	
	private CashierQueueHome getCashierQueueHome() {
		try {
			return (CashierQueueHome) IDOLookup.getHome(CashierQueue.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

}
