/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.presentation;

import com.idega.block.process.business.CaseBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.Converter;
import com.idega.user.data.User;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ChildCareApplicationAdmin extends CommuneBlock {
	private final static int ACTION_VIEW = 0;
	private final static int ACTION_YES = 1;
	private final static int ACTION_NO = 2;
	private final static int ACTION_ASSIGN_CONTRACT = 3;
	private final static int ACTION_PRINT_CONTRACT = 4;
	private final static int ACTION_ASSIGN_PLACE = 5;
	
	private final static String CHILD = "ccaa_child";
	private final static String SSN = "ccaa_ssn";
	private final static String QUEUE_DATE = "ccaa_queue_date";
	private final static String WANT_FROM = "ccaa_want_from";
	private final static String CHECK_NUMBER = "ccaa_check_number";
	private final static String ACCEPT = "ccaa_accept";

	private final static String PARAM_FORM_CONTRACT = "ccaa_contract";
	private final static String PARAM_FORM_ASSIGN = "ccaa_assign";
	
	private final static String PARAM_WANT_FROM_OK = "ccaa_want_from_ok";
	private final static String PARAM_CARE_TIME = "ccaa_care_time";
	private final static String PARAM_YES = "ccaa_yes";
	private final static String PARAM_NO = "ccaa_no";
	private final static String PARAM_ID = "ccaa_id";
	
	private final static String ERROR_NOT_LOGGED_IN = "ccaa_not_logged_in";
	private final static String ERROR_NO_APPLICATIONS = "ccaa_no_applications";
	private final static String ERROR_UNABLE_TO_CHANGE = "ccaa_unable_to_change_status";
	private final static String ERROR_MUST_BE_INTEGER	 = "ccaa_must_be_integer";
	
	protected User _user = null;
//	protected IWBundle _iwb;
//	protected IWResourceBundle _iwrb;

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
//		_iwb = getBundle(iwc);
//		_iwrb = getResourceBundle(iwc);

		if (iwc.getUser() != null)
			_user = Converter.convertToNewUser(iwc.getUser());
		else
			_user = null;

		if (_user != null) {
			setResourceBundle(getResourceBundle(iwc));
			
			try {
				int action = parseAction(iwc);				
					control(iwc,action);
			}
			catch (Exception e) {
				super.add(new ExceptionWrapper(e, this));
			}
		}
		else {
			add(getErrorText(localize(ERROR_NOT_LOGGED_IN, "No user logged in")));
		}		
	}

	private void control(IWContext iwc, int action) {
		if (action == ACTION_VIEW) {
			viewList(iwc);	
		}
		else if ((action == ACTION_YES) || (action == ACTION_NO)) {
			boolean done = changeApplication(iwc,action);
			if (!done)
				add(getErrorText(localize(ERROR_UNABLE_TO_CHANGE,"Unable to change application status")));
			viewList(iwc);	
		}		
	}

	private void viewList(IWContext iwc) {
		Form form = new Form();
		
		Collection appl = null;
		
		try {
			appl = getChildCareBusiness(iwc).getApplicationsByProvider(_user);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		if (appl != null) {
			int row = 1;
			Table outer = new Table(1,2);
			Table data = new Table(10,appl.size()+1);
			data.setCellspacing(2);
			data.setCellpadding(4);
			data.setHorizontalZebraColored("WHITE",getBackgroundColor());

			data.add(getHeader(localize(CHILD,"Child")),1,row);
			data.add(getHeader(localize(SSN,"SSN")),2,row);
			data.add(getHeader(localize(QUEUE_DATE,"Queue date")),3,row);
			data.add(getHeader(localize(CHECK_NUMBER,"Cheque number")),4,row);
			data.add(getHeader(localize(PARAM_CARE_TIME,"Care time")),5,row);
			data.add(getHeader(localize(WANT_FROM,"Want from")),6,row);
			data.add(getHeader(localize(PARAM_WANT_FROM_OK,"OK")),7,row);
			data.add(getHeader(localize(ACCEPT,"Accept")),8,row);
			data.add(getHeader(localize(PARAM_FORM_CONTRACT,"Contract")),9,row);
			data.add(getHeader(localize(PARAM_FORM_ASSIGN,"Assign")),10,row++);

			Iterator it = appl.iterator();
			CaseBusiness caseBiz = getCaseBusiness(iwc);
			ChildCareApplication application = null;
			boolean ubeh = false, prel = false, kout = false;
			while (it.hasNext()) {
				application = (ChildCareApplication)it.next();
				try {
					User child = application.getChild();
					data.add(child.getName(),1,row);
					data.add(child.getPersonalID(),2,row);					
					data.add(application.getQueueDate().toString(),3,row);
					data.add(Integer.toString(application.getCheckId()),4,row);
					data.add(application.getFromDate().toString(),6,row);
					
				}
				catch (RemoteException e) {
					data.add("Unable to get child",1,row);
				}

				String id = null;;
				try {
					id = ((Integer) application.getPrimaryKey()).toString();
				}
				catch (RemoteException e) {
				}

				try {
					String status = application.getStatus();
										
					if (status.equals(caseBiz.getCaseStatusOpen().toString())) {
						ubeh = true;
						prel = false;
						kout = false;
					}
					else if (status.equals(caseBiz.getCaseStatusPreliminary().toString())) {
						ubeh = false;
						prel = true;
						kout = false;
					}
					else if (status.equals(caseBiz.getCaseStatusContract().toString())) {
						ubeh = false;
						prel = false;
						kout = true;
					}
				}
				catch (RemoteException e) {
					e.printStackTrace();
					ubeh = false;
					prel = false;
					kout = false;
				}

				if (ubeh) {				
					SubmitButton no = new SubmitButton(localize(PARAM_NO, "No"), PARAM_ID, id);
					no.setName(PARAM_NO);
					no.setAsImageButton(true);

					SubmitButton yes = new SubmitButton(localize(PARAM_YES, "Yes"), PARAM_ID, id);
					yes.setName(PARAM_YES);
					yes.setAsImageButton(true);
				
					data.add(yes,8,row);
					data.add(Text.getNonBrakingSpace(),8,row);
					data.add(no,8,row);

					TextInput careTime = new TextInput(PARAM_CARE_TIME);
					careTime.setAsIntegers(localize(ERROR_MUST_BE_INTEGER,"You must enter an integer here"));
					careTime.setStyleAttribute(getSmallTextFontStyle());
					careTime.setLength(3);
					data.add(careTime,5,row);
					
					CheckBox check = new CheckBox();
					check.setName(PARAM_WANT_FROM_OK);
					data.add(check,7,row);
				}
				else if (prel) {
					TextInput careTime = new TextInput(PARAM_CARE_TIME);
					careTime.setAsIntegers(localize(ERROR_MUST_BE_INTEGER,"You must enter an integer here"));
					careTime.setStyleAttribute(getSmallTextFontStyle());
					careTime.setLength(3);
					data.add(careTime,5,row);
					
					CheckBox check = new CheckBox();
					check.setName(PARAM_WANT_FROM_OK);
					data.add(check,7,row);
					
					CheckBox contract = new CheckBox();
					contract.setName(PARAM_FORM_CONTRACT);
					data.add(contract,9,row);					
				}
				
				row++;
			}
			
			outer.add(data,1,1);
			outer.setAlignment(1,2,"RIGHT");
			
			SubmitButton contract = new SubmitButton(PARAM_FORM_CONTRACT, localize(PARAM_FORM_CONTRACT, "Create contract"));
			SubmitButton assign = new SubmitButton(PARAM_FORM_ASSIGN, localize(PARAM_FORM_ASSIGN, "Assign"));
			contract.setAsImageButton(true);
			assign.setAsImageButton(true);
			outer.add(contract,1,2);
			outer.add(Text.getNonBrakingSpace(),1,2);
			outer.add(assign,1,2);
			
			form.add(outer);
		}
		else {
			form.add(getErrorText(localize(ERROR_NO_APPLICATIONS,"No applications")));	
		}
		
		add(form);		
	}
	
	private boolean changeApplication(IWContext iwc, int action) {
		if (action == ACTION_NO) {
			String id = iwc.getParameter(PARAM_ID);
			if (id != null) {
				try {
					return getChildCareBusiness(iwc).rejectApplication(Integer.parseInt(id));
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		else if (action == ACTION_YES) {
			String id = iwc.getParameter(PARAM_ID);
			if (id != null) {
				try {
					return getChildCareBusiness(iwc).acceptApplication(Integer.parseInt(id));
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}				
			}			
		}
		
		return false;
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAM_NO)) {			
			return ACTION_NO;
		}
		else if (iwc.isParameterSet(PARAM_YES)) {
			return ACTION_YES;
		}

		return ACTION_VIEW;	
	}
	
	private ChildCareBusiness getChildCareBusiness(IWContext iwc) {
		try {
			return (ChildCareBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);
		}
		catch (RemoteException e) {
			return null;
		}
	}	
	
	private CaseBusiness getCaseBusiness(IWContext iwc) {
		try {
			return (CaseBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CaseBusiness.class);
		}
		catch (RemoteException e) {
			return null;
		}
	}		
}