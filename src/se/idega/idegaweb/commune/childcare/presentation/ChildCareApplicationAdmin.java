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
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.Converter;
import com.idega.user.data.User;

import se.idega.idegaweb.commune.childcare.presentation.ViewChildCareContract;
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
	private final static String SHOW_CONTRACT = "ccaa_show_contract";
	
	private final static String ASSIGN_SUBJECT = "ccaa_assign_msg_subject";
	private final static String ASSIGN_BODY = "ccaa_assign_msg_body";

	private final static String PARAM_PROGNOSIS = "ccaa_prognosis";
	private final static String PARAM_PRESENTATION = "ccaa_presentation";
	private final static String PARAM_FORM_CONTRACT = "ccaa_contract";
	private final static String PARAM_FORM_CONTRACT_CHECK = "ccaa_contract_check";
	private final static String PARAM_FORM_ASSIGN = "ccaa_assign";
	private final static String PARAM_FORM_ASSIGN_CHECK = "ccaa_assign_check";
	
	private final static String PARAM_WANT_FROM_OK = "ccaa_want_from_ok";
	private final static String PARAM_CARE_TIME = "ccaa_care_time";
	private final static String PARAM_YES = "ccaa_yes";
	private final static String PARAM_NO = "ccaa_no";
	private final static String PARAM_ID = "ccaa_id";
	
	private final static String ERROR_NOT_LOGGED_IN = "ccaa_not_logged_in";
	private final static String ERROR_NO_APPLICATIONS = "ccaa_no_applications";
	private final static String ERROR_UNABLE_TO_CHANGE = "ccaa_unable_to_change_status";
	private final static String ERROR_MUST_BE_INTEGER	 = "ccaa_must_be_integer";
	private final static String ERROR_UNABLE_TO_ASSIGN_CONTRACT = "ccaa_unable_to_assign_contract";
	private final static String ERROR_UNABLE_TO_ASSIGN_PLACE = "ccaa_unable_to_assign_place";
	
	private final static String EMAIL_PROVIDER_SUBJECT = "cca_provider_email_subject";
	private final static String EMAIL_PROVIDER_MESSAGE = "cca_provider_email_message";
	private final static String EMAIL_USER_SUBJECT = "child_care.application_received_subject";
	private final static String EMAIL_USER_MESSAGE = "child_care.application_received_body";
	private final static String EMAIL_USER_REJECT_SUBJECT = "child_care.application_rejected_subject";
	private final static String EMAIL_USER_REJECT_MESSAGE = "child_care.application_rejected_body";

	private final static String HIDDEN_PARAM_APPL_COUNT = "ccaa_appl_count";
	
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

	private void control(IWContext iwc, int action) throws RemoteException {
		if ((action == ACTION_YES) || (action == ACTION_NO)) {
			boolean done = changeApplication(iwc,action);
			if (!done)
				add(getErrorText(localize(ERROR_UNABLE_TO_CHANGE,"Unable to change application status")));
		}
		else if (action == ACTION_ASSIGN_CONTRACT) {
			boolean done = assignContract(iwc);
			if (!done)
				add(getErrorText(localize(ERROR_UNABLE_TO_ASSIGN_CONTRACT,"Unable to assign a contract to the application")));
		}	
		else if (action == ACTION_ASSIGN_PLACE) {
			boolean done = assignPlace(iwc);
			if (!done)
				add(getErrorText(localize(ERROR_UNABLE_TO_ASSIGN_PLACE,"Unable to assign a place to the application")));
		}	

		viewList(iwc);	
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
			Table outer = new Table(1,6);
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
				String name = null;
				User child = application.getChild();
				data.add(child.getName(),1,row);
				data.add(child.getPersonalID(),2,row);					
				data.add(application.getQueueDate().toString(),3,row);
				data.add(Integer.toString(application.getCheckId()),4,row);
				data.add(application.getFromDate().toString(),6,row);
				name = child.getName();

				String id = null;;
				//try {
					id = ((Integer) application.getPrimaryKey()).toString();
				//}
				//catch (RemoteException e) {
				//}

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
					
					CheckBox check = new CheckBox(PARAM_WANT_FROM_OK,id);
					data.add(check,7,row);
				}
				else if (prel) {
					TextInput careTime = new TextInput(PARAM_CARE_TIME);
					careTime.setAsIntegers(localize(ERROR_MUST_BE_INTEGER,"You must enter an integer here"));
					careTime.setStyleAttribute(getSmallTextFontStyle());
					careTime.setLength(3);
					data.add(careTime,5,row);
										
					CheckBox contract = new CheckBox(PARAM_FORM_CONTRACT_CHECK,id);
					data.add(contract,9,row);
				}
				else if (kout) {
					Link contract = new Link(localize(SHOW_CONTRACT,"Show"));
					contract.setWindowToOpen(ViewChildCareContract.class);
					contract.addParameter(ViewChildCareContract.VIEW_CONTRACT_FILE,application.getContractFileId());
	
					data.add(contract,9,row);

					CheckBox assign = new CheckBox(PARAM_FORM_ASSIGN_CHECK,id);
					data.add(assign,10,row);
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
			
			outer.add(getHeader(localize(PARAM_PROGNOSIS,"Prognosis")),1,3);
			outer.add(getHeader(localize(PARAM_PRESENTATION,"Presentation")),1,5);
			
			TextArea prognosis = new TextArea(PARAM_PROGNOSIS,80,5);
			TextArea presentation = new TextArea(PARAM_PRESENTATION,80,5);
			prognosis.setMaximumCharacters(1000);
			presentation.setMaximumCharacters(1000);
			prognosis.setStyleAttribute(getSmallTextFontStyle());
			presentation.setStyleAttribute(getSmallTextFontStyle());

			outer.add(prognosis,1,4);
			outer.add(presentation,1,6);
			
			form.add(outer);
		}
		else {
			form.add(getErrorText(localize(ERROR_NO_APPLICATIONS,"No applications")));	
		}
		
		add(form);		
	}
	
	private boolean assignContract(IWContext iwc) {
    String[] ids = iwc.getParameterValues(PARAM_FORM_CONTRACT_CHECK);
	
		if (ids != null) {
			try {
				return getChildCareBusiness(iwc).assignContractToApplication(ids,iwc.getCurrentUser());
			}
			catch (RemoteException e) {
			}
		}
		
		return false;	
	}
	
	private boolean assignPlace(IWContext iwc) {
    String[] ids = iwc.getParameterValues(PARAM_FORM_ASSIGN_CHECK);
	
		if (ids != null) {
			String subject = localize(ASSIGN_SUBJECT,"A child has been assigned a spot");
			String body = localize(ASSIGN_BODY,"Something about the child being allocated a spot...");
			try {
				return getChildCareBusiness(iwc).assignApplication(ids,iwc.getCurrentUser(),subject,body);
			}
			catch (RemoteException e) {
			}
		}
		
		return false;	
	}
	
	private boolean changeApplication(IWContext iwc, int action) throws RemoteException {
		if (action == ACTION_NO) {
			String id = iwc.getParameter(PARAM_ID);
			if (id != null) {
				String rejectSubject = localize(EMAIL_USER_REJECT_SUBJECT,"Child care application");
				String rejectMessage = localize(EMAIL_USER_REJECT_MESSAGE,"You have received a new childcare application");
				String receiveSubject = localize(EMAIL_USER_SUBJECT,"Child care application");
				String receiveMessage = localize(EMAIL_USER_MESSAGE,"You have received a new childcare application");

				return getChildCareBusiness(iwc).rejectApplication(Integer.parseInt(id),rejectSubject,rejectMessage,receiveSubject,receiveMessage,iwc.getCurrentUser());
			}
		}
		else if (action == ACTION_YES) {
			String id = iwc.getParameter(PARAM_ID);
			if (id != null) {
				try {
					String subject = localize(EMAIL_USER_SUBJECT,"Child care application");
					StringBuffer message = new StringBuffer(localize(EMAIL_USER_MESSAGE,"Your child care application has been accepted."));
					
					String prognosis = iwc.getParameter(PARAM_PROGNOSIS);
					String presentation = iwc.getParameter(PARAM_PRESENTATION);
					
					if (prognosis != null) {
						message.append("\n");
						message.append(prognosis);	
					}

					if (presentation != null) {
						message.append("\n");
						message.append(presentation);	
					}

					return getChildCareBusiness(iwc).acceptApplication(Integer.parseInt(id),subject,message.toString(),iwc.getCurrentUser());
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
		else if (iwc.isParameterSet(PARAM_FORM_CONTRACT)) {
			return ACTION_ASSIGN_CONTRACT;	
		}
		else if (iwc.isParameterSet(PARAM_FORM_ASSIGN)) {
			return ACTION_ASSIGN_PLACE;	
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