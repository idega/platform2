/*
 * $Id: CitizenAccountAdmin.java,v 1.11 2002/11/20 11:50:59 staffan Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.presentation;

import com.idega.business.IBOLookup;
import com.idega.core.data.Address;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.Converter;
import com.idega.user.data.*;
import com.idega.util.PersonalIDFormatter;
import java.rmi.RemoteException;
import java.util.*;
import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.account.citizen.data.*;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * CitizenAccountAdmin is an IdegaWeb block that displays the admintstation user
 * interface for accpting and rejecting citizen account applications. It is
 * based on session ejb classes in
 * {@link se.idega.idegaweb.commune.account.citizen.business} and entity ejb
 * classes in {@link se.idega.idegaweb.commune.account.citizen.business.data}.
 * <p>
 * Last modified: $Date: 2002/11/20 11:50:59 $ by $Author: staffan $
 *
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.11 $
 */
public class CitizenAccountAdmin extends CommuneBlock {
	private final static int ACTION_VIEW_LIST = 0;
	private final static int ACTION_VIEW_DETAILS = 1;
	private final static int ACTION_APPROVE = 2;
	private final static int ACTION_REJECT = 3;

	private final static String ADDRESS_DEFAULT = "Address";
	private final static String ADDRESS_KEY = "caa_adm_address";
	private final static String MESSAGE_DEFAULT = "Meddelande";
	private final static String MESSAGE_KEY = "caa_adm_message";
	private final static String NAME_DEFAULT = "Namn";
	private final static String NAME_KEY = "caa_adm_name";

	private final static String PARAM_FORM_APPROVE = "caa_adm_approve";
	private final static String PARAM_FORM_REJECT = "caa_adm_reject";
	private final static String PARAM_FORM_DETAILS = "caa_adm_details";
	private final static String PARAM_FORM_CANCEL = "caa_adm_cancel";
	private final static String PARAM_FORM_LIST = "caa_adm_list";

	public void main(IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));

		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_VIEW_LIST :
					viewList(iwc);
					break;
				case ACTION_VIEW_DETAILS :
					viewDetails(iwc);
					break;
				case ACTION_APPROVE :
					approve(iwc);
					break;
				case ACTION_REJECT :
					reject(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAM_FORM_APPROVE)) {
			String value = iwc.getParameter(PARAM_FORM_APPROVE);
			if (value != null && !value.equals(""))
				return ACTION_APPROVE;
		}

		if (iwc.isParameterSet(PARAM_FORM_REJECT)) {
			String value = iwc.getParameter(PARAM_FORM_REJECT);
			if (value != null && !value.equals(""))
				return ACTION_REJECT;
		}

		if (iwc.isParameterSet(PARAM_FORM_DETAILS)) {
			String value = iwc.getParameter(PARAM_FORM_DETAILS);
			if (value != null && !value.equals(""))
				return ACTION_VIEW_DETAILS;
		}

		return ACTION_VIEW_LIST;
	}

	private void viewList(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		table.setColumns(4);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setWidth(getWidth());
		table.setWidth(4, "12");

		int row = 1;
		int col = 1;
		table.setRowColor(row, getHeaderColor());
		table.add(getSmallHeader(localize(NAME_KEY, NAME_DEFAULT)), col++, row);
		table.add(getSmallHeader(localize(CitizenAccountApplication.SSN_KEY, CitizenAccountApplication.SSN_DEFAULT)), col++, row);
		table.add(getSmallHeader(localize(ADDRESS_KEY, ADDRESS_DEFAULT)), col, row++);

		List applications = null;
		try {
			CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			applications = business.getListOfUnapprovedApplications();
		}
		catch (RemoteException e) {
		}

		if (applications != null && !applications.isEmpty()) {
			Iterator it = applications.iterator();
			while (it.hasNext()) {
				col = 1;
				AdminListOfApplications list = (AdminListOfApplications) it.next();
				table.add(getSmallText(list.getName()), col++, row);
				String personalID = PersonalIDFormatter.format(list.getPID(), iwc.getApplication().getSettings().getApplicationLocale());
				table.add(getSmallText(personalID), col++, row);
				table.add(getSmallText(list.getAddress()), col++, row);
	
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());
	
				SubmitButton details = new SubmitButton(getVariousIcon(""), PARAM_FORM_DETAILS, list.getId());
				details.setDescription(localize(PARAM_FORM_DETAILS, "Administrate"));
				table.add(details, col, row++);
			}
		}

		form.add(table);
		add(form);
	}

	private void viewDetails(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(0);
		table.setColumns(3);
		table.setWidth(1, "30%");
		int row = 1;

		try {
			final CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			final String idAsString = iwc.getParameter(PARAM_FORM_DETAILS);
			final int id = new Integer(idAsString).intValue();
			final CitizenAccount applicant = (CitizenAccount) business.getAccount(id);

			table.add(getSmallHeader(localize(NAME_KEY, NAME_DEFAULT)), 1, row);
			table.add(getSmallText(applicant.getApplicantName()), 3, row++);
			
			table.add(getSmallHeader(localize(CitizenAccountApplication.SSN_KEY, CitizenAccountApplication.SSN_DEFAULT)), 1, row);
			final String pid = PersonalIDFormatter.format(applicant.getSsn(), iwc.getApplication().getSettings().getApplicationLocale());
			table.add(getSmallText(pid), 3, row++);
			
			table.add(getSmallHeader(localize(CitizenAccountApplication.EMAIL_KEY, CitizenAccountApplication.EMAIL_DEFAULT)), 1, row);
			final Link email = getSmallLink(applicant.getEmail());
			email.setURL("mailto:" + applicant.getEmail());
			table.add(email, 3, row++);
			
			table.add(getSmallHeader(localize(CitizenAccountApplication.PHONE_HOME_KEY, CitizenAccountApplication.PHONE_HOME_DEFAULT)), 1, row);
			table.add(getSmallText(applicant.getPhoneHome()), 3, row++);
			
			table.add(getSmallHeader(localize(CitizenAccountApplication.PHONE_WORK_KEY, CitizenAccountApplication.PHONE_WORK_DEFAULT)), 1, row);
			table.add(getSmallText(applicant.getPhoneWork()), 3, row++);
			
			final String address = applicant.getStreet() + "; " + applicant.getZipCode() + " " + applicant.getCity();
			if (applicant.getStreet() != null && applicant.getZipCode() != null && applicant.getCity() != null) {
				table.add(getSmallHeader(localize(ADDRESS_KEY, ADDRESS_DEFAULT)), 1, row);
				table.add(getSmallText(address), 3, row++);
			}

			if (applicant.getCivilStatus() != null) {
				table.add(getSmallHeader(localize(CitizenAccountApplication.CIVIL_STATUS_KEY, CitizenAccountApplication.CIVIL_STATUS_DEFAULT)), 1, row);
				table.add(getSmallText(applicant.getCivilStatus()), 3, row++);
			}

			table.add(getSmallHeader(localize(CitizenAccountApplication.COHABITANT_KEY, CitizenAccountApplication.COHABITANT_DEFAULT)), 1, row);
			final String hasCohabitant = applicant.hasCohabitant() ? localize(CitizenAccountApplication.YES_KEY, CitizenAccountApplication.YES_DEFAULT) : localize(CitizenAccountApplication.NO_KEY, CitizenAccountApplication.NO_DEFAULT);
			table.add(getSmallText(hasCohabitant), 3, row++);
			
			table.add(getSmallHeader(localize(CitizenAccountApplication.CHILDREN_COUNT_KEY, CitizenAccountApplication.CHILDREN_COUNT_DEFAULT)), 1, row);
			table.add(getSmallText(String.valueOf(applicant.getChildrenCount())), 3, row++);
			
			if (applicant.getApplicationReason() != null) {
				table.add(getSmallHeader(localize(CitizenAccountApplication.APPLICATION_REASON_KEY, CitizenAccountApplication.APPLICATION_REASON_DEFAULT)), 1, row);
				final String applicationReason = localize(applicant.getApplicationReason(), "?");
				table.add(getSmallText(applicationReason), 3, row++);
			}
			
			table.setHeight(row++, 6);
			table.mergeCells(1, row, 3, row);
			table.setWidth(row, Table.HUNDRED_PERCENT);
			table.add(getSmallHeader(localize(MESSAGE_KEY, MESSAGE_DEFAULT)), 1, row);
			table.add(new Break(), 1, row);
			TextArea area = new TextArea(MESSAGE_KEY);
			area.setHeight(7);
			area.setWidth(40);
			table.add(area, 1, row++);

			SubmitButton approve = (SubmitButton) getButton(new SubmitButton(localize(PARAM_FORM_APPROVE, "Godkänn"), PARAM_FORM_APPROVE, idAsString));
			SubmitButton reject = (SubmitButton) getButton(new SubmitButton(localize(PARAM_FORM_REJECT, "Avslå"), PARAM_FORM_REJECT, idAsString));
			SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize(PARAM_FORM_CANCEL, "Avbryt"), PARAM_FORM_CANCEL, idAsString));

			table.setHeight(row++, 12);
			table.mergeCells(1, row, table.getColumns(), row);

			table.add(approve, 1, row);
			table.add(Text.NON_BREAKING_SPACE, 1, row);
			table.add(reject, 1, row);
			table.add(Text.NON_BREAKING_SPACE, 1, row);
			table.add(cancel, 1, row);
		}
		catch (final Exception e) {
			e.printStackTrace();
		}

		form.add(table);
		add(form);
	}

	private void approve(IWContext iwc) {
		final Form form = new Form();
		final String id = iwc.getParameter(PARAM_FORM_APPROVE);

		try {
			final CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			business.acceptApplication(new Integer(id).intValue(), iwc.getCurrentUser());

			form.add(getText(localize("caa_acc_application", "Godkänd ansökan: ") + id));
		}
		catch (Exception e) {
			e.printStackTrace();
			form.add(getText(localize("caa_acc_application_failed", "Ett fel inträffade vid godkännade av ansökan: ") + id));
		}

		final SubmitButton list = (SubmitButton) getButton(new SubmitButton(localize(PARAM_FORM_LIST, "Lista"), PARAM_FORM_LIST, ""));
		form.add(Text.BREAK);
		form.add(Text.BREAK);
		form.add(list);
		add(form);
	}

	private void reject(IWContext iwc) {
		Form form = new Form();
		String id = iwc.getParameter(PARAM_FORM_REJECT);

		try {
			CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			if (iwc.isParameterSet(MESSAGE_KEY)) {
				business.rejectApplication(new Integer(id).intValue(), Converter.convertToNewUser(iwc.getUser()), iwc.getParameter(MESSAGE_KEY));
			}

			form.add(getText(localize("caa_rej_application", "Rejected application number : ") + id));
		}
		catch (Exception e) {
			e.printStackTrace();
			form.add(getText(localize("caa_rej_application_failed", "There was an error rejecting application number : ") + id));
		}

		SubmitButton list = (SubmitButton) getButton(new SubmitButton(localize(PARAM_FORM_LIST, "List"), PARAM_FORM_LIST, ""));
		form.add(Text.BREAK);
		form.add(Text.BREAK);
		form.add(list);
		add(form);
	}
}
