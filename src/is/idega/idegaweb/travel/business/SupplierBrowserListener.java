/*
 * $Id: SupplierBrowserListener.java,v 1.1 2005/07/07 02:59:05 gimmi Exp $
 * Created on Jul 6, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.business;

import java.rmi.RemoteException;
import is.idega.idegaweb.travel.block.search.business.ServiceSearchSession;
import is.idega.idegaweb.travel.presentation.SupplierBrowserBookingForm;
import com.idega.block.basket.business.BasketBusiness;
import com.idega.block.trade.stockroom.business.SupplierManagerBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;


public class SupplierBrowserListener implements IWPageEventListener {
	
	public static String ACTION = "sbl_a";
	public static String ACTION_SEND_TO_CASHIER = "sbl_as2c";

	public boolean actionPerformed(IWContext iwc) throws IWException {
		String action = iwc.getParameter(ACTION);
		if (action == null) {
			action = "";
		}
		
		if (action.equals(ACTION_SEND_TO_CASHIER)) {
			try {
				UserHome uHome = (UserHome) IDOLookup.getHome(User.class);
				String cashierID = iwc.getParameter(SupplierBrowserBookingForm.PARAMETER_CASHIER_ID);
				String clientName = iwc.getParameter(SupplierBrowserBookingForm.PARAMETER_CLIENT_NAME);
				User cashier = null;
				if (cashierID != null && !cashierID.equals("-1")) {
					cashier = uHome.findByPrimaryKey(new Integer(cashierID));
				}
				User performer = iwc.getCurrentUser();
				Group supplierManager = getSupplierManagerBusiness(iwc).getSupplierManager(performer);

				if (getBasketBusiness(iwc).getBasket().isEmpty()) {
					Exception e = new Exception("basket_is_empty");
					throw e;
				}
				
				getSupplierBrowserBusiness(iwc).sendToCashier(supplierManager, clientName, cashier, performer, getBasketBusiness(iwc));
			} catch (Exception e) {
				try {
					getServiceSearchSession(iwc).setException(e);
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		
		return true;
	}

	protected BasketBusiness getBasketBusiness(IWContext iwc) {
		try {
			return (BasketBusiness) IBOLookup.getSessionInstance(iwc, BasketBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	private ServiceSearchSession getServiceSearchSession(IWContext iwc) {
		try {
			return (ServiceSearchSession) IBOLookup.getServiceInstance(iwc, ServiceSearchSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	private SupplierBrowserBusiness getSupplierBrowserBusiness(IWContext iwc) {
		try {
			return (SupplierBrowserBusiness) IBOLookup.getServiceInstance(iwc, SupplierBrowserBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	private SupplierManagerBusiness getSupplierManagerBusiness(IWContext iwc) {
		try {
			return (SupplierManagerBusiness) IBOLookup.getServiceInstance(iwc, SupplierManagerBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
 }
