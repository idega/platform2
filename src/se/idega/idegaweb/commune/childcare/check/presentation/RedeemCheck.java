/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.check.presentation;

import is.idega.idegaweb.member.business.MemberFamilyLogic;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.check.data.GrantedCheck;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.school.data.School;
import com.idega.core.data.Address;
import com.idega.core.data.PostalCode;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RedeemCheck extends CommuneBlock {
	private final static int ACTION_VIEW_CHECKS = 0;
	private final static int ACTION_VIEW_DETAILS = 1;
	private final static int ACTION_REDEEM_CHECK = 2;

	private final static String PARAM_APPL_ID = "rc_application_id";
	private final static String PARAM_REDEEM_APPL_ID = "rc_redeem_id";

	private final static String NOT_LOGGED_IN = "rc_not_logged_in";
	private final static String NO_CHECK_TO_REDEEM = "rc_no_checks";
	
	private final static String CUSTODIANS = "rc_custodians";
	private final static String CHILD = "rc_child";
	private final static String CHILD_ADDRESS = "rc_child_address";
	private final static String CHECK_NUMBER = "rc_check_number";
	private final static String DATE_OF_REQUEST = "rc_date_request";
	private final static String CARE_CENTER_NAME = "rc_care_center_name";
	private final static String CARE_CENTER_ADDRESS = "rc_care_center_address";
	private final static String CONTACT = "rc_contact";

	private final static String CHECK_REDEEMED = "rc_check_redeemed";
	private final static String REDEEM_FAILED = "rc_failed";

	protected User _user = null;

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		_user = iwc.getCurrentUser();

		if (_user != null) {
			setResourceBundle(getResourceBundle(iwc));

			try {
				int action = parseAction(iwc);
				switch (action) {
					case ACTION_VIEW_CHECKS :
						viewChecks(iwc);
						break;
					case ACTION_VIEW_DETAILS :
						viewDetails(iwc);
						break;
					case ACTION_REDEEM_CHECK :
						redeemCheck(iwc);
						break;
				}
			}
			catch (Exception e) {
				super.add(new ExceptionWrapper(e, this));
			}
		}
		else {
			add(getErrorText(localize(NOT_LOGGED_IN, "No user logged in")));
		}
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAM_APPL_ID))
			return ACTION_VIEW_DETAILS;
		else if (iwc.isParameterSet(PARAM_REDEEM_APPL_ID))
			return ACTION_REDEEM_CHECK;
			
		return ACTION_VIEW_CHECKS;	
	}
	
	private void viewChecks(IWContext iwc) {
		Form form = new Form();
		Table inner = null;
		
		Collection applications = null;

		try {
			applications = getChildCareBusiness(iwc).findAllApplicationsWithChecksToRedeem();
		}
		catch (RemoteException e) {
		}
		catch (Exception e) {
		}

		if (applications != null && !applications.isEmpty()) {
			inner = new Table(2,applications.size());		
			form.add(inner);
			add(form);	
			Iterator it = applications.iterator();
			ChildCareApplication appl = null;
			GrantedCheck check = null;
			int row = 1;
			while (it.hasNext()) {
				appl = (ChildCareApplication)it.next();				

				check = appl.getCheck();
				inner.add(getSmallText(check.getPrimaryKey().toString()),1,row);
				
				SubmitButton details = new SubmitButton(localize(PARAM_APPL_ID, "Handle"), PARAM_APPL_ID, ((Integer) appl.getPrimaryKey()).toString());
				details.setAsImageButton(true);
				inner.add(details,2,row++);
			}
		}
		else {
			add(getErrorText(localize(NO_CHECK_TO_REDEEM, "There are no checks to redeem.")));
		}		
	}
	
	private void viewDetails(IWContext iwc) {
		String appId = iwc.getParameter(PARAM_APPL_ID);
		Form form = new Form();
		
		Table checkInfoTable = new Table();		
		checkInfoTable.setCellpadding(6);
		checkInfoTable.setCellspacing(0);
		int row = 1;
		
		add(form);
		
		ChildCareApplication appl = null;
		try {
			appl = getChildCareBusiness(iwc).getApplicationByPrimaryKey(appId);
		}
		catch(Exception e) {
			e.printStackTrace();	
		}
		
		if (appl != null) {
			User child = appl.getChild();
			
			if (child != null) {
				Collection custodians = null;
				try {
					custodians = getMemberFamilyLogic(iwc).getCustodiansFor(child);
					if (custodians != null && custodians.size() > 0) {
						checkInfoTable.add(getLocalizedSmallHeader(CUSTODIANS, "Custodians"), 1, row);
						checkInfoTable.add(getSmallHeader(":"), 1, row);
						checkInfoTable.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		
						Iterator iter2 = custodians.iterator();
						while (iter2.hasNext()) {
							User parent = (User) iter2.next();
							checkInfoTable.add(getSmallText(parent.getNameLastFirst(false)), 2, row++);
						}
					}
					
					checkInfoTable.add(getLocalizedSmallHeader(CHILD, "Child"), 1, row);
					checkInfoTable.add(getSmallHeader(":"), 1, row);
					checkInfoTable.add(getSmallText(child.getName()), 2, row++);
					
					checkInfoTable.add(getLocalizedSmallHeader(CHILD_ADDRESS, "Address"), 1, row);
					checkInfoTable.add(getSmallHeader(":"), 1, row);
					Collection addresses = child.getAddresses();
					Address address = null;
					PostalCode zip = null;
					Iterator iter = addresses.iterator();
					if (iter.hasNext()) {
						address = (Address) iter.next();
						zip = address.getPostalCode();
					}
					if (address != null) {
						checkInfoTable.add(getSmallText(address.getStreetAddress()), 2, row);
						if (zip != null) {
							checkInfoTable.add(getSmallText(" " + zip.getPostalAddress()), 2, row);
						}
					}
					row++;
					
					checkInfoTable.add(getLocalizedSmallHeader(CHECK_NUMBER, "Check number"), 1, row);
					checkInfoTable.add(getSmallHeader(":"), 1, row);
					checkInfoTable.add(getSmallText(Integer.toString(appl.getCheckId())), 2, row++);
					
					checkInfoTable.add(getLocalizedSmallHeader(DATE_OF_REQUEST, "Date of request"), 1, row);
					checkInfoTable.add(getSmallHeader(":"), 1, row);
					checkInfoTable.add(getSmallText(appl.getQueueDate().toString()), 2, row++);
					row++;
					
					School provider = appl.getProvider();
					if (provider != null) {
						checkInfoTable.add(getLocalizedSmallHeader(CARE_CENTER_NAME, "Care center name"), 1, row);
						checkInfoTable.add(getSmallHeader(":"), 1, row);
						checkInfoTable.add(getSmallText(provider.getName()), 2, row++);
					
						checkInfoTable.add(getLocalizedSmallHeader(CARE_CENTER_ADDRESS, "Care center address"), 1, row);
						checkInfoTable.add(getSmallHeader(":"), 1, row);
						checkInfoTable.add(getSmallText(appl.getProvider().getSchoolAddress()), 2, row++);
						
						checkInfoTable.add(getLocalizedSmallHeader(CONTACT, "Contact"), 1, row);
						checkInfoTable.add(getSmallHeader(":"), 1, row);
						Collection headmasters = getUserBusiness(iwc).getUsersInGroup(provider.getHeadmasterGroupId());

						if (headmasters != null) {
							Iterator it = headmasters.iterator();
							if (it.hasNext()) {	
								User providerUser = (User)it.next();
								checkInfoTable.add(getSmallText(providerUser.getName()), 2, row++);								
							}				
						}						
					}					
				}
				catch (Exception e) {
					e.printStackTrace();
				}				
			}
			
			checkInfoTable.setAlignment(2,row,"RIGHT");			
			SubmitButton details = new SubmitButton(localize(PARAM_REDEEM_APPL_ID, "Redeem check"), PARAM_REDEEM_APPL_ID, appId);
			details.setAsImageButton(true);
			checkInfoTable.add(details,2,row);
		}
		
		form.add(checkInfoTable);
	}

	private void redeemCheck(IWContext iwc) {
		String appId = iwc.getParameter(PARAM_REDEEM_APPL_ID);
		boolean done = false;
		try {
			done = this.getChildCareBusiness(iwc).redeemApplication(appId,iwc.getCurrentUser());	
		}
		catch(Exception e) {
			e.printStackTrace();
			
			done = false;	
		}
		
		if (done) {
			if (getResponsePage() != null)
				iwc.forwardToIBPage(getParentPage(), getResponsePage());
			else
				add(new Text(localize(CHECK_REDEEMED, "Check redeemed")));
		}
		else
			add(new Text(localize(REDEEM_FAILED, "Failed to redeem check")));
	}	
	
	private ChildCareBusiness getChildCareBusiness(IWContext iwc) {
		try {
			return (ChildCareBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);
		}
		catch (RemoteException e) {
			return null;
		}
	}

	private CheckBusiness getCheckBusiness(IWContext iwc) {
		try {
			return (CheckBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CheckBusiness.class);
		}
		catch (RemoteException e) {
			return null;
		}
	}

	protected MemberFamilyLogic getMemberFamilyLogic(IWContext iwc) {
		try {
			return (MemberFamilyLogic) com.idega.business.IBOLookup.getServiceInstance(iwc, MemberFamilyLogic.class);
		}
		catch (RemoteException e) {
			return null;
		}
	}
	
	protected UserBusiness getUserBusiness(IWContext iwc) {
		try {
			return (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		}
		catch (RemoteException e) {
			return null;
		}
	}	
}