/*
 * $Id: PaymentAuthorizationBusinessBean.java,v 1.6 2004/10/13 15:29:57 thomas Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.accounting.childcare.data.ChildCareContractHome;
import se.idega.idegaweb.commune.accounting.invoice.data.ConstantStatus;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.presentation.ManuallyPaymentEntriesList;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;


/**
 * This business handles the logic for Payment authorisation
 * 
 * <p>
 * $Id: PaymentAuthorizationBusinessBean.java,v 1.6 2004/10/13 15:29:57 thomas Exp $
 *
 * @author Kelly
 */
public class PaymentAuthorizationBusinessBean extends IBOServiceBean implements PaymentAuthorizationBusiness {

	private final static String KP = "payment_authorization."; // key prefix 

	public final static String KEY_AUTH_MESSAGE_SUBJECT = KP + "auth_subject";

	/**
	 * Authorizes the payments. 
	 * Sets authorization date in PaymentRecord
	 * and sets status i PaymentRecord from U to P
	 * @return 
	 */
	public void authorizePayments(IWContext iwc, User user) {
	
		try {
			int providerID = -1;
			try{
				School provider = getCareBusiness().getProviderForUser(user);
				providerID = Integer.parseInt(provider.getPrimaryKey().toString());
			} catch(FinderException ex){
				//If no provider for current user, and current user is administrator, read from the parameter set in the PaymentRecordMaintenance class
				
				if (isCentralAdministrator(iwc)){
					providerID = Integer.parseInt(iwc.getParameter(ManuallyPaymentEntriesList.PAR_SELECTED_PROVIDER));
				} 				
			}				
			Iterator payments; 
			payments = getPaymentHeaderHome().
					findByStatusAndSchoolId(ConstantStatus.BASE, providerID).iterator();
			while (payments.hasNext()) {
				Date today = new Date(System.currentTimeMillis());
				PaymentHeader ph = (PaymentHeader) payments.next();
				Iterator records = getPaymentRecordHome().findByPaymentHeader(ph).iterator();
				while(records.hasNext()){
					PaymentRecord pr = (PaymentRecord) records.next();
					pr.setStatus(ConstantStatus.PRELIMINARY);
					pr.store();
				}

				ph.setStatus(ConstantStatus.PRELIMINARY);
				ph.setSignaturelID(user);
				ph.setDateAttested(today);
				ph.store();
			}
							
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hasAuthorizablePayments(IWContext iwc, User user) {
		boolean ret = false;
		try {
			int providerID = -1;		
			try{
				School provider = getCareBusiness().getProviderForUser(user);
				providerID = Integer.parseInt(provider.getPrimaryKey().toString());				
			} catch(FinderException ex){
				//If no provider for current user, and current user is administrator, read from the parameter set in the PaymentRecordMaintenance class
				
				if (isCentralAdministrator(iwc)){
					providerID = Integer.parseInt(iwc.getParameter(ManuallyPaymentEntriesList.PAR_SELECTED_PROVIDER));
				} 				
			}
			
			if (providerID != -1){
				Collection payments = getPaymentHeaderHome().findByStatusAndSchoolId(ConstantStatus.BASE, providerID);
				if (! payments.isEmpty()) {
					ret = true;
				}
			}
							
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private boolean isCentralAdministrator(final IWContext context) {
		try {
			// first see if we have cached certificate
			final String sessionKey = getClass() + ".isCentralAdministrator";
			final User verifiedCentralAdmin = (User) context
					.getSessionAttribute(sessionKey);
			final User user = context.getCurrentUser();

			if (null != verifiedCentralAdmin
					&& user.equals(verifiedCentralAdmin)) {
			// certificate were cached
			return true; }

			// since no cert were cached, check current users group instaed
			final int groupId = getCommuneUserBusiness().getRootAdministratorGroupID();
			final GroupHome home = (GroupHome) IDOLookup.getHome(Group.class);
			final Group communeGroup = home.findByPrimaryKey(new Integer(
					groupId));
			final Collection usersGroups = getUserBusiness().getUserGroups(
					((Integer) user.getPrimaryKey()).intValue());
			if (usersGroups != null
					&& communeGroup != null
					&& (usersGroups.contains(communeGroup) || user
							.getPrimaryKey().equals(new Integer(1)))) {
				// user is allaowed, cache certificate and return true
				context.setSessionAttribute(sessionKey, user);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
		

	public String getProviderNameForUser(User user) {
		String name = ""; 
		try {
			School provider = getCareBusiness().getProviderForUser(user);
			name = provider.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}	
	
	/**
	 * Returns PaymentHeaderHome home 
	 */	
	protected SchoolUserBusiness getSchoolUserBusiness(IWContext iwc) throws RemoteException {
		return (SchoolUserBusiness) IBOLookup.getServiceInstance(iwc, SchoolUserBusiness.class);
	}
		
		 
	/**
	 * Returns PaymentHeaderHome home 
	 */	
	protected PaymentHeaderHome getPaymentHeaderHome() throws RemoteException {
			return (PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class);
	}
	/**
	 * Returns ChildCareContracts home 
	 */	
	protected ChildCareContractHome getChildCareContractHome() throws RemoteException {
			return (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
	}

	/**
	 * Returns Payment Records home 
	 */	
	protected PaymentRecordHome getPaymentRecordHome() throws RemoteException {
			return (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
	}

	/**
	 * Returns school business. 
	 */	
	protected SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) this.getServiceInstance(SchoolBusiness.class);
	}

	/**
	 * Returns user business. 
	 */	
	protected UserBusiness getUserBusiness() throws RemoteException {
		return (UserBusiness) this.getServiceInstance(UserBusiness.class);
	}

	/**
	 * Returns message business. 
	 */	
	protected MessageBusiness getMessageBusiness() throws RemoteException {
		return (MessageBusiness) this.getServiceInstance(MessageBusiness.class);
	}
	
	/**	
	 * Returns school commune business 
	 */	
	protected CareBusiness getCareBusiness() throws RemoteException {
			return (CareBusiness) getServiceInstance(CareBusiness.class);
	}
	
	/**	
	 * Returns school commune business 
	 */	
	protected CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
			return (CommuneUserBusiness) getServiceInstance(CommuneUserBusiness.class);
	}
}
