/*
 * $Id: VacationApprover.java,v 1.2 2004/12/06 21:30:34 laddi Exp $ Created on
 * 18.11.2004
 * 
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.agura.applications.vacation.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import se.agura.applications.vacation.data.VacationRequest;
import se.agura.applications.vacation.data.VacationType;

import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.data.Group;

/**
 * Last modified: 18.11.2004 10:21:40 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna </a>
 * @version $Revision: 1.2 $
 */
public class VacationApprover extends VacationBlock {

	private static final String ROLE_SALARY_ADMINISTRATION = "can_administer_salaries";
	
	private String iWidth = Table.HUNDRED_PERCENT;

	private int iCellpadding = 3;

	String action = null;

	VacationRequest vacation = null;

	VacationType vacationType = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.agura.applications.vacation.presentation.VacationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			parse(iwc);

			if (action.equals(ACTION_SEND)) {
				getSendToHandleForm();
			}
			else if (action.equals(ACTION_FORWARD)) {
				forward(iwc);
				showMessage(getResourceBundle().getLocalizedString("meeting_approver.application_forwarded", "Application forwarded."));
			}
			else if (action.equals(ACTION_DENIED)) {
				reject(iwc);
				showMessage(getResourceBundle().getLocalizedString("meeting_approver.application_rejected", "Application denied."));
			}
			else if (action.equals(ACTION_APPROVED)) {
				approve(iwc);
				showMessage(getResourceBundle().getLocalizedString("meeting_approver.application_approved", "Application approved."));
			}
			else {
				add(supervisorView(iwc));
			}
		}
		catch (RemoteException re) {
			log(re);
		}
	}

	private void parse(IWContext iwc) {
		action = iwc.getParameter(PARAMETER_ACTION);
		if (action == null) {
			action = "";
		}

		vacation = getVacation(iwc);
		if (vacation != null) {
			vacationType = vacation.getVacationType();
		}
		else {
			throw new IBORuntimeException("No vacation request found...");
		}
	}

	private void showMessage(String message) {
		add(getHeader(message));
		add(new Break(2));

		Link link = getLink(getResourceBundle().getLocalizedString("meeting.home_page", "Back to My Page"));
		if (getPage() != null) {
			link.setPage(getPage());
		}
		add(link);
	}

	private VacationRequest getVacation(IWContext iwc) {
		VacationRequest vacation = null;
		try {
			if (iwc.isParameterSet(PARAMETER_PRIMARY_KEY_VAC)) {
				vacation = getBusiness(iwc).getVacationRequest(iwc.getParameter(PARAMETER_PRIMARY_KEY_VAC));
			}
			else {
				vacation = getBusiness(iwc).getVacationRequest("1");
			}
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (NullPointerException npe) {
			log(npe);
		}
		return vacation;
	}

	private void reject(IWContext iwc) {
		String comment = iwc.getParameter(PARAMETER_COMMENT);
		try {
			getBusiness(iwc).rejectApplication(vacation, iwc.getCurrentUser(), comment);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private void approve(IWContext iwc) {
		String comment = iwc.getParameter(PARAMETER_COMMENT);
		try {
			getBusiness(iwc).approveApplication(vacation, iwc.getCurrentUser(), comment);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private void forward(IWContext iwc) {
		String comment = iwc.getParameter(PARAMETER_COMMENT);
		Group group = null;
		try {
			getBusiness(iwc).forwardApplication(vacation, iwc.getCurrentUser(), group, comment);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private Form getSendToHandleForm() {
		Form form = new Form();
		//DropdownMenu sentToHandle = new DropdownMenu();
		form.add(getResourceBundle().getLocalizedString("vacation.request.handle", "Sent to be handled by"));
		// LADDI! EKKI EYÜA GR®NUM TEXTA - LESA FYRST!
		// for(int i = o; i < handleGroup.length; i++) {
		// sentToHandle.addMenuElement(i, String.valueOf(i));
		// }
		form.addParameter(PARAMETER_ACTION, ACTION_CANCEL);
		form.addParameter(PARAMETER_ACTION, ACTION_SEND);
		form.add(getCancelButton());
		form.add(getSendButton());// skicka
		return form;
	}

	private Form supervisorView(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PRIMARY_KEY_VAC);
		
		Table logs = getVacationActionOverview(iwc, vacation);
		if (logs != null) {
			form.add(logs);
			form.add(new Break());
		}
		
		form.add(showVacationRequest(iwc, vacation));
		form.add(new Break());
		form.add(handleRequest(iwc));
		form.add(new Break(2));
		form.add(getDeniedButton());
		form.add(getApprovedButton());
		if (vacationType.getAllowsForwarding()) {
			form.add(getForwardButton());
		}
		return form;
	}

	private SubmitButton getDeniedButton() {
		SubmitButton deniedButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("vacation_approver.reject_application", "Reject"), PARAMETER_ACTION, ACTION_DENIED));
		return deniedButton;
	}

	private SubmitButton getApprovedButton() {
		SubmitButton approvedButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("vacation_approver.approve_application", "Approve"), PARAMETER_ACTION, ACTION_APPROVED));
		return approvedButton;
	}

	private SubmitButton getSendButton() {
		SubmitButton sendButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("vacation_approver.send_application", "Send"), PARAMETER_ACTION, ACTION_SEND));
		return sendButton;
	}

	private SubmitButton getForwardButton() {
		SubmitButton forwardButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("vacation_approver.forward_application", "Forward"), PARAMETER_ACTION, ACTION_FORWARD));
		return forwardButton;
	}

	private GenericButton getCancelButton() {
		GenericButton cancelButton = getButton(new GenericButton(getResourceBundle().getLocalizedString("vacation_approver.cancel", "Cancel")));
		if (getPage() != null) {
			cancelButton.setPageToOpen(getPage());
		}
		return cancelButton;
	}

	private Table handleRequest(IWContext iwc) {
		Table table = new Table();
		table.setWidth(iWidth);
		table.setCellpadding(iCellpadding);
		table.setCellspacing(0);
		int row = 1;
		
		boolean hasSalaryRole = iwc.getAccessController().hasRole(ROLE_SALARY_ADMINISTRATION, iwc);
		if (hasSalaryRole) {
			log("Has salary role...");
		}
		
		TextArea area = (TextArea) getInput(new TextArea(PARAMETER_COMMENT));
		area.setWidth(Table.HUNDRED_PERCENT);
		area.setRows(4);

		table.add(getResourceBundle().getLocalizedString("vacation.request.message_to_worker", "Message to worker"), 1, row);
		table.add(area, 2, row);

		table.setWidth(1, iHeaderColumnWidth);
		table.setCellpaddingLeft(1, 0);
		
		return table;
	}
}