/*
 * $Id: VacationApprover.java,v 1.11 2005/01/19 12:34:37 laddi Exp $ Created on
 * 18.11.2004
 * 
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.agura.applications.vacation.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import se.agura.applications.vacation.data.VacationRequest;
import se.agura.applications.vacation.data.VacationType;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * Last modified: 18.11.2004 10:21:40 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna </a>
 * @version $Revision: 1.11 $
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

			if (action.equals(ACTION_FORWARD_VIEW)) {
				add(getSendToHandleForm(iwc));
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
			else if (action.equals(ACTION_CLOSED)) {
				close(iwc);
				showMessage(getResourceBundle().getLocalizedString("meeting_approver.application_closed", "Application closed."));
			}
			else {
				User owner = vacation.getOwner();
				if (owner.equals(iwc.getCurrentUser())) {
					add(ownerView(iwc));
				}
				else {
					add(supervisorView(iwc));
				}
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
	
	private void close(IWContext iwc) {
		try {
			getBusiness(iwc).closeApplication(vacation, iwc.getCurrentUser());
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private void approve(IWContext iwc) {
		String comment = iwc.getParameter(PARAMETER_COMMENT);
		boolean salaryCompensation = iwc.isParameterSet(PARAMETER_WITH_SALARY_COMPENSATION) ? new Boolean(iwc.getParameter(PARAMETER_WITH_SALARY_COMPENSATION)).booleanValue() : false;
		try {
			getBusiness(iwc).approveApplication(vacation, iwc.getCurrentUser(), comment, salaryCompensation);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private void forward(IWContext iwc) {
		try {
			String comment = iwc.getParameter(PARAMETER_COMMENT);
			boolean salaryCompensation = iwc.isParameterSet(PARAMETER_WITH_SALARY_COMPENSATION) ? new Boolean(iwc.getParameter(PARAMETER_WITH_SALARY_COMPENSATION)).booleanValue() : false;
			Group group = getUserBusiness(iwc).getGroupBusiness().getGroupByGroupID(Integer.parseInt(iwc.getParameter(PARAMETER_FORWARD_GROUP)));
			User handler = getUserBusiness(iwc).getUser(new Integer(iwc.getParameter(PARAMETER_HANDLER)));
			getBusiness(iwc).forwardApplication(vacation, iwc.getCurrentUser(), group, handler, comment, salaryCompensation);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (FinderException fe) {
			log(fe);
			add("FinderException occured...");
		}
	}

	private Form getSendToHandleForm(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.add(forwardView(iwc));
		form.add(new Break());
		form.add(getCancelButton());
		form.add(getForwardButton());
		return form;
	}
	
	private Table forwardView(IWContext iwc) throws RemoteException {
		Group parentGroup = (Group) getBusiness(iwc).getParentGroup(iwc.getCurrentUser()).getParentNode();
		Table table = new Table();
		table.setWidth(iWidth);
		table.setCellpadding(iCellpadding);
		table.setCellspacing(0);
		int row = 1;
		
		if (parentGroup != null) {
			Collection users = getUserBusiness(iwc).getUsersInGroup(parentGroup);
					
			table.add(new HiddenInput(PARAMETER_FORWARD_GROUP, parentGroup.getPrimaryKey().toString()), 1, row);
			table.add(getHeader(getResourceBundle().getLocalizedString("vacation.request.handle", "Send to be handled by")), 1, row);
	
			SelectorUtility util = new SelectorUtility();
			DropdownMenu menu = (DropdownMenu) getInput(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_HANDLER), users, "getName"));
			table.add(menu, 2, row);
		}
		else {
			table.add(getHeader(getResourceBundle().getLocalizedString("vacation.request.handle", "Send to be handled by")), 1, row);
			table.add(new Break(2), 1, row);
			table.add(new BackButton(getResourceBundle().getLocalizedString("vacation.request.back", "Back")), 1, row);
		}
		
		table.setWidth(1, iHeaderColumnWidth);
		table.setCellpaddingLeft(1, 0);
		
		return table;
	}
	
	private Form ownerView(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PRIMARY_KEY_VAC);
		
		Table logs = getVacationActionOverview(iwc, vacation);
		if (logs != null) {
			form.add(logs);
			form.add(new Break());
		}
		
		form.add(showVacationRequest(iwc, vacation));
		form.add(new Break());
		form.add(getCloseButton());
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
		//birtir uppl‡singar um sendanda umsóknar -ac
		VacationRequest application = null;
		try {
			application = getBusiness(iwc).getVacationRequest(iwc.getParameter(PARAMETER_PRIMARY_KEY_VAC));
		}
		catch (FinderException fe) {
			log(fe);
		}
		
		User user = application.getOwner();
		
		form.add(getPersonInfo(iwc, user)); 
		form.add(new Break());
		//hinga›
		form.add(showVacationRequest(iwc, vacation));
		form.add(new Break());
		form.add(handleRequest(iwc));
		form.add(new Break(2));
		form.add(getDeniedButton());
		form.add(getApprovedButton());
		if (vacationType.getAllowsForwarding()) {
			form.add(getForwardViewButton());
		}
		return form;
	}

	private SubmitButton getCloseButton() {
		SubmitButton closeButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("vacation_approver.close_application", "Close"), PARAMETER_ACTION, ACTION_CLOSED));
		closeButton.setToolTip(getResourceBundle().getLocalizedString("vacation.close.tooltip","Puts the application to a closed status"));
		closeButton.setSubmitConfirm(getResourceBundle().getLocalizedString("vacation.close.popup","Are you sure you want to finally close the application?"));

		return closeButton;
	}

	private SubmitButton getDeniedButton() {
		SubmitButton deniedButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("vacation_approver.reject_application", "Reject"), PARAMETER_ACTION, ACTION_DENIED));
		deniedButton.setToolTip(getResourceBundle().getLocalizedString("vacation.deny.tooltip","Denies the application"));
		deniedButton.setSubmitConfirm(getResourceBundle().getLocalizedString("vacation.deny.popup","Are you sure you want to deny this application?"));
		return deniedButton;
	}

	private SubmitButton getApprovedButton() {
		SubmitButton approvedButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("vacation_approver.approve_application", "Approve"), PARAMETER_ACTION, ACTION_APPROVED));
		approvedButton.setToolTip(getResourceBundle().getLocalizedString("vacation.approve.tooltip","Approves the application"));
		approvedButton.setSubmitConfirm(getResourceBundle().getLocalizedString("vacation.approve.popup","Are you sure you want to approve this application?"));
		return approvedButton;
	}

	private SubmitButton getForwardButton() {
		SubmitButton forwardButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("vacation_approver.forward_application", "Forward"), PARAMETER_ACTION, ACTION_FORWARD));
		forwardButton.setToolTip(getResourceBundle().getLocalizedString("vacation.forward.tooltip","Forwards the application"));
		forwardButton.setSubmitConfirm(getResourceBundle().getLocalizedString("vacation.forward.popup","Are you sure you want to forward this application?"));
		return forwardButton;
	}

	private SubmitButton getForwardViewButton() {
		SubmitButton forwardButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("vacation_approver.forward_application", "Forward"), PARAMETER_ACTION, ACTION_FORWARD_VIEW));
		forwardButton.setToolTip(getResourceBundle().getLocalizedString("vacation.forward.tooltip","Forwards the application"));
		return forwardButton;
	}

	private Table handleRequest(IWContext iwc) {
		Table table = new Table();
		table.setWidth(iWidth);
		table.setCellpadding(iCellpadding);
		table.setCellspacing(0);
		int row = 1;
		
		boolean hasSalaryRole = iwc.getAccessController().hasRole(ROLE_SALARY_ADMINISTRATION, iwc);
		if (hasSalaryRole) {
			RadioButton withCompensation = (RadioButton) getRadioButton(new RadioButton(PARAMETER_WITH_SALARY_COMPENSATION, Boolean.TRUE.toString()));
			RadioButton withoutCompensation = (RadioButton) getRadioButton(new RadioButton(PARAMETER_WITH_SALARY_COMPENSATION, Boolean.FALSE.toString()));
			if (vacation.getSalaryCompensation()) {
				withCompensation.setSelected(true);
			}
			else {
				withoutCompensation.setSelected(false);
			}
			
			table.add(getHeader(getResourceBundle().getLocalizedString("vacation.request.salary_compensation", "Salary compensation")), 1, row);
			table.add(withCompensation, 2, row);
			table.add(Text.getNonBrakingSpace(), 2, row);
			table.add(getText(getResourceBundle().getLocalizedString("vacation.request.with", "With")), 2, row);

			table.add(Text.getNonBrakingSpace(), 2, row);
			table.add(Text.getNonBrakingSpace(), 2, row);

			table.add(withoutCompensation, 2, row);
			table.add(Text.getNonBrakingSpace(), 2, row);
			table.add(getText(getResourceBundle().getLocalizedString("vacation.request.without", "Without")), 2, row++);
			table.setHeight(row++, 12);
		}

		TextArea area = (TextArea) getInput(new TextArea(PARAMETER_COMMENT));
		area.setWidth(Table.HUNDRED_PERCENT);
		area.setRows(4);

		table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		table.add(getHeader(getResourceBundle().getLocalizedString("vacation.request.message_to_worker", "Message to worker")), 1, row);
		table.add(area, 2, row++);
		
		table.setWidth(1, iHeaderColumnWidth);
		table.setCellpaddingLeft(1, 0);
		
		return table;
	}
}