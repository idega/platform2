/*
 * $Id: CitizenAccountAdmin.java,v 1.7 2002/11/14 10:21:14 laddi Exp $
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
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;
import java.rmi.RemoteException;
import java.util.*;
import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.account.citizen.data.*;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CitizenAccountAdmin extends CommuneBlock {
	private final static int ACTION_VIEW_LIST = 0;
	private final static int ACTION_VIEW_DETAILS = 1;
	private final static int ACTION_APPROVE = 2;
	private final static int ACTION_REJECT = 3;

	private final static String PARAM_PID = "caa_pid";
	private final static String PARAM_EMAIL = "caa_email";
	private final static String PARAM_PHONE_HOME = "caa_phone_home";
	private final static String PARAM_PHONE_WORK = "caa_phone_work";
	private final static String PARAM_NAME = "caa_adm_name";
	private final static String PARAM_ADDRESS = "caa_adm_address";
	private final static String PARAM_MESSAGE = "caa_adm_message";
	private final static String PARAM_NOT_CITIZEN = "caa_adm_not_citizen";

	private final static String PARAM_APPLICANT_NAME = "caa_applicant_name";
	private final static String PARAM_CUSTODIAN1_PID = "caa_custodian1_pid";
	private final static String PARAM_CUSTODIAN1_CIVIL_STATUS = "caa_custodian1_civil_status";
	private final static String PARAM_CUSTODIAN2_PID = "caa_custodian2_pid";
	private final static String PARAM_CUSTODIAN2_CIVIL_STATUS = "caa_custodian2_civil_status";
	private final static String PARAM_STREET = "caa_street";
	private final static String PARAM_ZIP_CODE = "caa_zip_code";
	private final static String PARAM_CITY = "caa_city";

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
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setWidth(getWidth());

		int row = 1;
		int col = 1;
		table.setRowColor(row, getHeaderColor());
		table.add(getSmallHeader(localize(PARAM_NAME, "Namn")), col++, row);
		table.add(getSmallHeader(localize(PARAM_PID, "Personnummer")), col++, row);
		table.add(getSmallHeader(localize(PARAM_ADDRESS, "Adress")), col, row++);

		List applications = null;
		try {
			CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			applications = business.getListOfUnapprovedApplications();
		}
		catch (RemoteException e) {
		}

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

		form.add(table);
		add(form);
	}

	private void viewDetails(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(0);
		table.setWidth(1, "30%");
		table.setWidth(2, "70%");

		int row = 1;
		int col = 1;
		table.add(getSmallHeader(localize(PARAM_NAME, "Namn")), col, row++);
		table.add(getSmallHeader(localize(PARAM_PID, "Personnummer")), col, row++);
		table.add(getSmallHeader(localize(PARAM_EMAIL, "E-post")), col, row++);
		table.add(getSmallHeader(localize(PARAM_PHONE_HOME, "Telefon (hem)")), col, row++);
		table.add(getSmallHeader(localize(PARAM_PHONE_WORK, "Telefon (arbete/mobil)")), col, row++);
		table.add(getSmallHeader(localize(PARAM_ADDRESS, "Adress")), col, row++);
		table.add(getSmallHeader("Vårdnadshavare 1: " + localize(PARAM_CUSTODIAN1_PID, "Personnummer")), col, row++);
		table.add(getSmallHeader("Vårdnadshavare 1: " + localize(PARAM_CUSTODIAN1_CIVIL_STATUS, "Civilstånd")), col, row++);
		table.add(getSmallHeader("Vårdnadshavare 2: " + localize(PARAM_CUSTODIAN2_PID, "Personnummer")), col, row++);
		table.add(getSmallHeader("Vårdnadshavare 2: " + localize(PARAM_CUSTODIAN2_CIVIL_STATUS, "Civilstånd")), col, row++);

		table.add(getSmallHeader(localize(PARAM_NOT_CITIZEN, "Not citizen")), col, row++);
		table.add(getSmallHeader(localize(PARAM_MESSAGE, "Message")), col++, row);

		try {
			final CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			final String idAsString = iwc.getParameter(PARAM_FORM_DETAILS);
			final int id = new Integer(idAsString).intValue();
			final CitizenAccount applicant = (CitizenAccount) business.getAccount(id);
			row = 1;
			table.add(getSmallText(applicant.getApplicantName()), col, row++);
			final String pid = PersonalIDFormatter.format(applicant.getPID(), iwc.getApplication().getSettings().getApplicationLocale());
			table.add(getText(pid), col, row++);
			final String email = applicant.getEmail();
			table.add(new Link(email, "mailto:" + email), col, row++);
			table.add(getSmallText(applicant.getPhoneHome()), col, row++);
			table.add(getSmallText(applicant.getPhoneWork()), col, row++);
			final String address = applicant.getStreet() + "; " + applicant.getZipCode() + " " + applicant.getCity();
			table.add(getSmallText(address), col, row++);
			table.add(getSmallText(applicant.getCustodian1Pid()), col, row++);
			table.add(getSmallText(applicant.getCustodian1CivilStatus()), col, row++);
			table.add(getSmallText(applicant.getCustodian2Pid()), col, row++);
			table.add(getSmallText(applicant.getCustodian2CivilStatus()), col, row++);
			table.add(new CheckBox(PARAM_NOT_CITIZEN), col, row++);
			TextArea area = new TextArea(PARAM_MESSAGE);
			area.setHeight(7);
			area.setWidth(40);
			table.add(area, col, row++);

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
			if (iwc.isParameterSet(PARAM_NOT_CITIZEN)) {
				business.rejectApplication(new Integer(id).intValue(), Converter.convertToNewUser(iwc.getUser()), "Not citizen of Nacka");
			}
			else if (iwc.isParameterSet(PARAM_MESSAGE)) {
				business.rejectApplication(new Integer(id).intValue(), Converter.convertToNewUser(iwc.getUser()), iwc.getParameter(PARAM_MESSAGE));
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
