
/*
 * $Id: PaymentAuthorization.java,v 1.3 2004/05/12 16:00:58 roar Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.invoice.business.PaymentAuthorizationBusiness;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.user.data.User;

/**
 * PaymentAuthorization is an idegaWeb block that handles Authorization of
 * payment to providers
 * <p>
 * $Id: PaymentAuthorization.java,v 1.3 2004/05/12 16:00:58 roar Exp $
 *
 * @author <a href="http://www.lindman.se">Kelly</a>
 * @version $Revision: 1.3 $
 */
public class PaymentAuthorization extends AccountingBlock {

	
	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_SAVE = 1;
	
	private final static String KEY_PREFIX = "payment_authorization."; 
	
	private final static String KEY_TITLE = KEY_PREFIX + "title";
	private final static String KEY_OPERATION = KEY_PREFIX + "operation";
	private final static String KEY_PROVIDER = KEY_PREFIX + "provider";
	private final static String KEY_SIGNATURE = KEY_PREFIX + "signature";
	private final static String KEY_AUTH_DATE = KEY_PREFIX + "auth_date";
	private final static String KEY_AUTHORIZED = KEY_PREFIX + "authorized";
	private final static String KEY_SAVE = KEY_PREFIX + "save";
	private final static String KEY_ABORT = KEY_PREFIX + "abort";
	private final static String KEY_BACK = KEY_PREFIX + "back";
	private final static String KEY_NOT_LOGGED_IN = KEY_PREFIX + "not_logged_in";
	private final static String KEY_NOT_AUTHORIZED = KEY_PREFIX + "not_authorized";
	private final static String KEY_NO_AUTHORIZING = KEY_PREFIX + "no_authorizing";
	
	private final static String PARAM_SAVE = "param_save";
	private final static String PARAM_ABORT = "param_abort";
	private final static String PARAM_BACK = "param_back";

	private User _user;
	private String _providerName = "";
	private String _userName = "";
	private Date _authDate;
	
	/**
	 * Handles all of the blocks presentation.
	 * @param iwc user/session context 
	 */
	public void init(final IWContext iwc) {
		try {
			if(loadUserVariables(iwc)) {
				int action = parseAction(iwc);
				switch (action) {
					case ACTION_DEFAULT :
						viewDefaultForm(iwc);
						break;
					case ACTION_SAVE :
						updatePayments(iwc);
						viewUpdatedForm();
						break;
				}
			} else {
				getErrorForm();
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		int action = ACTION_DEFAULT;
		if (iwc.isParameterSet(PARAM_SAVE)) {
			action = ACTION_SAVE;
		}
		return action;
	}

	/*
	 * Adds the default form to the block.
	 */	
	private void viewDefaultForm(IWContext iwc) {
		boolean hasPayments = false;
		try{
			hasPayments = getPaymentAuthorizationBusiness(iwc).hasAuthorizablePayments(iwc, _user);		
		}catch(RemoteException ex){
			ex.printStackTrace();
		}
		ApplicationForm app = new ApplicationForm(this);
		app.maintainParameter(ManuallyPaymentEntriesList.PAR_SELECTED_PROVIDER);
		
		app.setLocalizedTitle(KEY_TITLE, "Payment authorization");
		if (hasPayments){
			app.setMainPanel(getAuthorizationPanel());
			app.setButtonPanel(getButtonPanel(true, true));	//Save, Abort		
		} else {
			app.setMainPanel(getLocalizedText(KEY_NO_AUTHORIZING, "No payments to be authorized"));
			app.setButtonPanel(getButtonPanel(false, false));	//No Save, Back	
		}

		add(app);
	}

	/*
	 * Adds the default form to the block.
	 */	
	private void viewUpdatedForm() {
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE, "Payment authorization");
		app.setMainPanel(getLocalizedText(KEY_AUTHORIZED, "Payments authorized"));
		app.setButtonPanel(getButtonPanel(false, false));	//No save, Back	
		add(app);
	}

	/*
	 * Adds an error from to the block.
	 */	
	private void getErrorForm() {
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE, "Payment authorization");
		if(_userName.length() == 0) {
			app.setMainPanel(getLocalizedText(KEY_NOT_LOGGED_IN, "Not logged in"));
		} else {
			app.setMainPanel(getLocalizedText(KEY_NOT_AUTHORIZED, "Not authorized"));
		}
		app.setButtonPanel(getButtonPanel(false, false));	//No save, Back			
		add(app);
	}

	
	/*
	 * Returns the Authorization panel
	 */
	private Table getAuthorizationPanel() {
		Table table = new Table();
		table.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_LEFT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		table.add(getLocalizedLabel(KEY_OPERATION, "Operation"), 1, 1);
		table.add(getLocalizedLabel(KEY_PROVIDER, "Provider"), 1, 2);
		table.add(getLocalizedLabel(KEY_SIGNATURE, "Signature"), 1, 3);
		table.add(getLocalizedLabel(KEY_AUTH_DATE, "Authorization date"), 1, 4);
			
		table.add(getSmallText(""), 2, 1);
		table.add(getSmallText(""+_providerName), 2, 2);
		table.add(getSmallText(""+_userName), 2, 3);
		table.add(getSmallText(""+_authDate.toString()), 2, 4);
		


		return table;
	}

	/*
	 * Loads the current user variables. 
	 * Returns true if logged in
	 */
	private boolean loadUserVariables(IWContext iwc) {
		
		try {
			_user = getUserBusiness(iwc).getUser(iwc.getCurrentUserId());
			if (_user != null) {
				_providerName = getPaymentAuthorizationBusiness(iwc).getProviderNameForUser(_user);
				
				//If no provider for current user, read from the parameter set in the PaymentRecordMaintenance class
				if (_providerName.length() == 0){
					String providerID = iwc.getParameter(ManuallyPaymentEntriesList.PAR_SELECTED_PROVIDER);
					School school = null;
					try{
						SchoolHome sh = (SchoolHome) IDOLookup.getHome(School.class);
						school = sh.findByPrimaryKey(providerID);
						_providerName = school.getName();
					}catch(IDOLookupException ex){
						ex.printStackTrace(); 
					}catch(FinderException ex){
						ex.printStackTrace(); 
					}
				}
				_userName = _user.getName();
			}
			_authDate = new Date(System.currentTimeMillis());
		} catch (Exception e) {
		}
		return (_userName.length() != 0 ); 
//		return (_userName.length() != 0 && _providerName.length() != 0) ? true : false; 
	}
		
	/*
	 * Returns the button panel for this block
	 */
	private ButtonPanel getButtonPanel(boolean includeSave, boolean useAbort) {
		ButtonPanel bp = new ButtonPanel(this);
		
		if (includeSave){
			bp.addLocalizedButton(PARAM_SAVE, KEY_SAVE, "Save");
		}
		if (useAbort){
			bp.addLocalizedButton(PARAM_ABORT, KEY_ABORT, "Abort", getResponsePage());
		} else {
			bp.addLocalizedButton(PARAM_BACK, KEY_BACK, "Back", getResponsePage());
		}
		return bp;
	}

	private void updatePayments(IWContext iwc) {
		try {
			getPaymentAuthorizationBusiness(iwc).authorizePayments(iwc, _user);
		} catch (Exception e) {	
		}
	}
	
	/*
	 * Returns a PaymentAuthorization business object
	 */
	private PaymentAuthorizationBusiness getPaymentAuthorizationBusiness(IWContext iwc) throws RemoteException {
		return (PaymentAuthorizationBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, PaymentAuthorizationBusiness.class);
	}	

	


}
