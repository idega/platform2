/*
 * $Id: VacationApprover.java,v 1.1 2004/11/25 14:22:35 anna Exp $ Created on 18.11.2004
 * 
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.agura.applications.vacation.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import se.agura.applications.vacation.data.VacationRequest;
import se.agura.applications.vacation.data.VacationTime;
import se.agura.applications.vacation.data.VacationType;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * Last modified: 18.11.2004 10:21:40 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna </a>
 * @version $Revision: 1.1 $
 */
public class VacationApprover extends VacationBlock {

	private String iWidth = Table.HUNDRED_PERCENT;

	private int iCellpadding = 3;

	String action = null;

	boolean allows_forwarding = true;

	VacationType vacationType = null;

	private VacationRequest getVacation(IWContext iwc) {
		VacationRequest vacation = null;
		try {
			vacation = getBusiness(iwc).getVacationRequest(null);
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (FinderException fe) {
			log(fe);
		}
		return vacation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.agura.applications.vacation.presentation.VacationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		action = iwc.getParameter(PARAMETER_ACTION);
		// ﬂarf ekki hvert erind fyrir sig a› vera linkur???? <--Laddi
		// hvernig læt t.d. ég supervisor sjá ﬂær bei›nir sem hafa veri› stíla›ar á
		// hann
		// ﬂarf ekki a› vera til fall í Business bauninni --> public Collection
		// getVacationRequests(Object primaryKey)
		supervisorView(iwc);
		headOfDepartmentView(iwc);
		headOfOfficeView(iwc);
		if (action.equals(ACTION_SEND)) {
			// getSendToHandleForm(iwc);
		}
	}

	// here is the "popup" with the dropdown including the names of those who
	// receive the request to handle it!
	private Form getSendToHandleForm(IWContext iwc) {
		Form form = new Form();
		DropdownMenu sentToHandle = new DropdownMenu();
		form.add(getResourceBundle().getLocalizedString("vacation.request.handle", "Sent to be handled by"));
		// LADDI! EKKI EY‹A GRÆNUM TEXTA - LESA FYRST!
		// for(int i = o; i < handleGroup.length; i++) {
		// sentToHandle.addMenuElement(i, String.valueOf(i));
		// }
		form.addParameter(PARAMETER_ACTION, ACTION_CANCEL);
		form.addParameter(PARAMETER_ACTION, ACTION_SEND);
		form.add(getCancelButton());
		form.add(getSendButton());// skicka
		return form;
	}

	private Form supervisorView(IWContext iwc) {
		Form form = new Form();
		form.add(handleRequestSupervisor(iwc));
		form.addParameter(PARAMETER_ACTION, ACTION_DENIED);
		form.addParameter(PARAMETER_ACTION, ACTION_APPROVED);
		form.add(getDeniedButton());
		form.add(getApprovedButton());
		vacationType = getVacation(iwc).getVacationType();
		if (vacationType.getTypeName() == "Fackligt uppdrag") {
			form.addParameter(PARAMETER_ACTION, ACTION_FORWARD);// skicka vidare
			form.add(getForwardButton());
		}
		return form;
	}

	private Form headOfDepartmentView(IWContext iwc) {
		Form form = new Form();
		form.add(handleRequestHeadOfDep(iwc));
		form.addParameter(PARAMETER_ACTION, ACTION_DENIED);
		form.addParameter(PARAMETER_ACTION, ACTION_APPROVED);
		form.add(getDeniedButton());
		form.add(getApprovedButton());
		vacationType = getVacation(iwc).getVacationType();
		if (vacationType.getTypeName() == "Fackligt uppdrag") {
			form.addParameter(PARAMETER_ACTION, ACTION_FORWARD);// skicka vidare
			form.add(getForwardButton());
		}
		return form;
	}

	private Form headOfOfficeView(IWContext iwc) {
		Form form = new Form();
		form.add(handleRequestHeadOfOffice(iwc));
		form.addParameter(PARAMETER_ACTION, ACTION_DENIED);
		form.addParameter(PARAMETER_ACTION, ACTION_APPROVED);
		form.add(getDeniedButton());
		form.add(getApprovedButton());
		vacationType = getVacation(iwc).getVacationType();
		if (vacationType.getTypeName() == "Fackligt uppdrag") {
			form.addParameter(PARAMETER_ACTION, ACTION_FORWARD);// skicka vidare
			form.add(getForwardButton());
		}
		return form;
	}

	private SubmitButton getDeniedButton() {
		SubmitButton deniedButton = new SubmitButton();
		return deniedButton;
	}

	private SubmitButton getApprovedButton() {
		SubmitButton approvedButton = new SubmitButton();
		return approvedButton;
	}

	private SubmitButton getSendButton() {
		SubmitButton sendButton = new SubmitButton();
		return sendButton;
	}

	private SubmitButton getForwardButton() {
		SubmitButton forwardButton = new SubmitButton();
		return forwardButton;
	}

	// HJÁLP - HVERNIG TAKKI Á ﬁETTA A‹ VERA????
	private GenericButton getCancelButton() {
		GenericButton cancelButton = new GenericButton();
		return cancelButton;
	}

	private Table showVacationRequest(IWContext iwc) {
		Table table = new Table();
		table.setWidth(iWidth);
		table.setCellpadding(iCellpadding);
		table.setCellspacing(0);
		int row = 1;
		IWTimestamp fromDate = new IWTimestamp(getVacation(iwc).getFromDate());
		IWTimestamp toDate = new IWTimestamp(getVacation(iwc).getToDate());
		IWTimestamp date = new IWTimestamp(getVacation(iwc).getCreatedDate());
		int selectedHours = getVacation(iwc).getOrdinaryWorkingHours();
		vacationType = getVacation(iwc).getVacationType();
		Collection times = null;
		try {
			times = getBusiness(iwc).getVacationTimes(getVacation(iwc));
		}
		catch (RemoteException re) {
			log(re);
		}
		table.add(getResourceBundle().getLocalizedString("vacation.time.required_vacation", "Required vacation"), 1, row);
		table.add(fromDate.getLocaleDate(iwc.getCurrentLocale()), 2, row++);
		table.add(toDate.getLocaleDate(iwc.getCurrentLocale()), 2, row++);
		table.add(getResourceBundle().getLocalizedString("vacation.time.ordinary_hours", "Ordinary workinghours per day"),
				1, row);
		table.add(String.valueOf(selectedHours), 2, row);// hvernig á ég a› bæta or›inu
		// "timmar" inn!
		table.add(
				getResourceBundle().getLocalizedString("vacation.time.period", "Working days and hours under the period*"), 1,
				row);
		table.add(getResourceBundle().getLocalizedString("vacation.time.week", "Week"), 2, row);
		table.add(getResourceBundle().getLocalizedString("vacation.time.monday", "Mo"), 3, row);
		table.add(getResourceBundle().getLocalizedString("vacation.time.tuesday", "Tu"), 4, row);
		table.add(getResourceBundle().getLocalizedString("vacation.time.wednesday", "We"), 5, row);
		table.add(getResourceBundle().getLocalizedString("vacation.time.thursday", "th"), 6, row);
		table.add(getResourceBundle().getLocalizedString("vacation.time.friday", "Fr"), 7, row);
		table.add(getResourceBundle().getLocalizedString("vacation.time.saturday", "Sa"), 8, row);
		table.add(getResourceBundle().getLocalizedString("vacation.time.sunday", "Su"), 9, row++);
		Iterator iter = times.iterator();
		while (iter.hasNext()) {
			VacationTime time = (VacationTime) iter.next();
			table.add(String.valueOf(time.getWeekNumber()), 2, row);
			if (time.getMonday() > 0) {
				table.add(String.valueOf(time.getMonday()), 3, row);
			}
			if (time.getTuesday() > 0) {
				table.add(String.valueOf(time.getTuesday()), 4, row);
			}
			if (time.getWednesday() > 0) {
				table.add(String.valueOf(time.getWednesday()), 5, row);
			}
			if (time.getThursday() > 0) {
				table.add(String.valueOf(time.getThursday()), 6, row);
			}
			if (time.getFriday() > 0) {
				table.add(String.valueOf(time.getFriday()), 7, row);
			}
			if (time.getSaturday() > 0) {
				table.add(String.valueOf(time.getSaturday()), 8, row);
			}
			if (time.getSunday() > 0) {
				table.add(String.valueOf(time.getSunday()), 9, row);
			}
			row++;
		}
		table.add(getResourceBundle().getLocalizedString("vacation.type", "Type"), 1, row);
		table.add(vacationType.getTypeName(), 2, row++);
		table.add(getResourceBundle().getLocalizedString("vacation.time.request_date", "Request date"), 1, row);
		table.add(date.getLocaleDate(iwc.getCurrentLocale()), 2, row);
		return table;
	}

	private Table handleRequestSupervisor(IWContext iwc) /* 4 */{
		Table table = new Table();
		TextInput input = new TextInput();
		int row = 1;
		table.add(getResourceBundle().getLocalizedString("vacation.request.handle", "Please handle the vacation request"),
				1, row);
		showVacationRequest(iwc);
		table.add(getResourceBundle().getLocalizedString("vacation.request.message_to_worker", "Message to worker"), 1, row);
		table.add(input);
		return table;
	}

	// er ekki hægt a› breyta í showVacationRequest, hvort sú tafla s‡ni
	// Löneförmäner
	private Table handleRequestHeadOfDep(IWContext iwc) /* 5 */{
		Table table = new Table();
		TextInput input = new TextInput();
		int row = 1;
		table.add(getResourceBundle().getLocalizedString("vacation.request.handle", "Please handle the vacation request"),
				1, row);
		approvedBy(iwc);
		showVacationRequest(iwc);// hér inn vantar Löneförmäner, sjá Fig 8
		table.add(getResourceBundle().getLocalizedString("vacation.request.message_to_worker", "Message to worker"), 1, row);
		table.add(input);
		return table;
	}

	// er ekki hægt a› breyta í showVacationRequest, hvort sú tafla s‡ni
	// Löneförmäner
	private Table handleRequestHeadOfOffice(IWContext iwc) /* 7 */{
		Table table = new Table();
		int row = 1;
		table.add(getResourceBundle().getLocalizedString("vacation.request.handle", "Please handle the vacation request"),
				1, row);
		forwardedBy(iwc);
		approvedBy(iwc);
		showVacationRequest(iwc); // hér inn vantar Löneförmäner, sjá Fig 10
		return table;
	}

	private Table forwardedBy(IWContext iwc) {
		Table table = new Table();
		int row = 1;
		table.setBorder(2);
		table.add(getResourceBundle().getLocalizedString("vacation.request.handle_forwarded_by", "Forwarded by"), 1, row);
		// table.add(name, 2, row);
		table.add(getResourceBundle().getLocalizedString("vacation.request.handle_message", "Message"), 1, row);
		// table.add(message, 2, row);
		return table;
	}

	private Table approvedBy(IWContext iwc) {
		Table table = new Table();
		int row = 1;
		table.setBorder(2);
		table.add(getResourceBundle().getLocalizedString("vacation.request.handle_approved_by", "Approved by"), 1, row);
		// table.add(name, 2, row); hvernig nálgast ég nafn ﬂess sem sendi
		// emailinn?????
		table.add(getResourceBundle().getLocalizedString("vacation.request.handle_message", "Message"), 1, row);
		// table.add(message, 2, row); eru ﬂetta skilabo› frá fyrri sí›u úr
		// "Meddelande til metarbetaren"?
		table.add(getResourceBundle().getLocalizedString("vacation.request.handle_date", "Date"), 1, row);
		// table.add(getDate(), 2, row); the date when the supervisor made his
		// decision, hvernig gerir ma›ur ﬂa›?
		return table;
	}

	private Table disapprovedBy(IWContext iwc) {
		Table table = new Table();
		int row = 1;
		table.setBorder(2);
		table.add(getResourceBundle().getLocalizedString("vacation.request.handle_disapproved_by", "Disapproved by"), 1,
				row);
		// table.add(name, 2, row); hvernig nálgast ég nafn ﬂess sem sendi
		// emailinn?????
		table.add(getResourceBundle().getLocalizedString("vacation.request.handle_message", "Message"), 1, row);
		// table.add(message, 2, row); eru ﬂetta skilabo› frá fyrri sí›u úr
		// "Meddelande til metarbetaren"?
		table.add(getResourceBundle().getLocalizedString("vacation.request.handle_date", "Date"), 1, row);
		// table.add(getDate(), 2, row); the date when the supervisor made his
		// decision, hvernig gerir ma›ur ﬂa›?
		return table;
	}
	// we need one for "Avstyrkt af"!
}