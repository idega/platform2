/*
 * $Id: CitizenAccountAdmin.java,v 1.2 2002/07/22 15:30:29 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.account.citizen.data.AdminListOfApplications;
import se.idega.idegaweb.commune.account.citizen.data.CitizenAccount;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.business.IBOLookup;
import com.idega.core.data.Address;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.CheckBox;
import com.idega.user.data.User;

import com.idega.user.Converter;

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

	private final static String PARAM_FORM_APPROVE = "caa_adm_approve";
	private final static String PARAM_FORM_REJECT = "caa_adm_reject";
	private final static String PARAM_FORM_DETAILS = "caa_adm_details";
	private final static String PARAM_FORM_CANCEL = "caa_adm_cancel";
	private final static String PARAM_FORM_LIST = "caa_adm_list";

	public CitizenAccountAdmin() {

	}

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
		DataTable data = new DataTable();
		data.setUseTitles(false);
		data.setUseBottom(false);
		data.setUseTop(false);
		data.setWidth("100%");

		int row = 1;
		int col = 1;
		data.add(getHeader(localize(PARAM_NAME, "Name")), col++, row);
		data.add(getHeader(localize(PARAM_PID, "PID")), col++, row);
		data.add(getHeader(localize(PARAM_ADDRESS, "Address")), col, row++);

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
			data.add(getSmallText(list.getName()), col++, row);
			data.add(getSmallText(list.getPID()), col++, row);
			data.add(getSmallText(list.getAddress()), col++, row);

			SubmitButton details = new SubmitButton(localize(PARAM_FORM_DETAILS, "Administrate"), PARAM_FORM_DETAILS, list.getId());
			details.setAsImageButton(true);
			data.add(details, col, row++);
		}

		form.add(data);
		add(form);
	}

	private void viewDetails(IWContext iwc) {
		String id = iwc.getParameter(PARAM_FORM_DETAILS);

		Form form = new Form();
		DataTable data = new DataTable();
		data.setUseTitles(false);
		data.setUseBottom(false);
		data.setUseTop(false);
		data.getContentTable().setWidth(1, "30%");
		data.getContentTable().setWidth(2, "70%");

		int row = 1;
		int col = 1;
		data.add(getHeader(localize(PARAM_NAME, "Name")), col, row++);
		data.add(getHeader(localize(PARAM_PID, "PID")), col, row++);
		data.add(getHeader(localize(PARAM_ADDRESS, "Address")), col, row++);
		data.add(getHeader(localize(PARAM_EMAIL, "E-mail")), col, row++);
		data.add(getHeader(localize(PARAM_PHONE_HOME, "Phone home")), col, row++);
		data.add(getHeader(localize(PARAM_PHONE_WORK, "Phone work/mobile")), col, row++);
		data.add(getHeader(localize(PARAM_NOT_CITIZEN, "Not citizen")), col, row++);
		data.add(getHeader(localize(PARAM_MESSAGE, "Message")), col++, row);

		try {
			CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			CitizenAccount acc = (CitizenAccount) business.getAccount(new Integer(id).intValue());
			User user = acc.getOwner();

			row = 1;
			if (user != null)
				data.add(getText(user.getName()), col, row++);
			else
				row++;
			data.add(getText(acc.getPID()), col, row++);
			if (user != null) {
				Collection addresses = user.getAddresses();
				Iterator it2 = addresses.iterator();
				Address address = null;
				if (it2.hasNext())
					address = (Address) it2.next();
				if (address != null) {
					StringBuffer fullAddress = new StringBuffer(address.getStreetName());
					fullAddress.append(" ");
					fullAddress.append(address.getStreetNumber());
					data.add(getText(fullAddress.toString()), col, row++);
				}
			}
			else
				row++;
			data.add(new Link(acc.getEmail(), "mailto:" + acc.getEmail()), col, row++);
			data.add(getText(acc.getPhoneHome()), col, row++);
			data.add(getText(acc.getPhoneWork()), col, row++);
			data.add(new CheckBox(PARAM_NOT_CITIZEN), col, row++);
			TextArea area = new TextArea(PARAM_MESSAGE);
			area.setHeight(7);
			area.setWidth(40);
			data.add(area, col, row);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		SubmitButton approve = new SubmitButton(localize(PARAM_FORM_APPROVE, "Approve"), PARAM_FORM_APPROVE, id);
		approve.setAsImageButton(true);
		SubmitButton reject = new SubmitButton(localize(PARAM_FORM_REJECT, "Reject"), PARAM_FORM_REJECT, id);
		reject.setAsImageButton(true);
		SubmitButton cancel = new SubmitButton(localize(PARAM_FORM_CANCEL, "Cancel"), PARAM_FORM_CANCEL, id);
		cancel.setAsImageButton(true);

		data.addButton(approve);
		data.addButton(reject);
		data.addButton(cancel);

		form.add(data);
		add(form);
	}

	private void approve(IWContext iwc) {
		Form form = new Form();
		String id = iwc.getParameter(PARAM_FORM_APPROVE);

		try {
			CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			business.acceptApplication(new Integer(id).intValue(),Converter.convertToNewUser(iwc.getUser()));

			form.add(getText("Approved appl. for : " + id));
		}
		catch (Exception e) {
			e.printStackTrace();
			form.add(getText("Failed to approve appl. for : " + id));
		}

		SubmitButton list = new SubmitButton(localize(PARAM_FORM_LIST, "List"), PARAM_FORM_LIST, "");
		list.setAsImageButton(true);
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
				business.rejectApplication(new Integer(id).intValue(),Converter.convertToNewUser(iwc.getUser()),"Not citizen of Nacka");
			}
			else if (iwc.isParameterSet(PARAM_MESSAGE)) {
				business.rejectApplication(new Integer(id).intValue(),Converter.convertToNewUser(iwc.getUser()),iwc.getParameter(PARAM_MESSAGE));			
			}

			form.add(getText("Rejected appl. for : " + id));
		}
		catch (Exception e) {
			e.printStackTrace();
			form.add(getText("Failed to reject appl. for : " + id));
		}
		
		SubmitButton list = new SubmitButton(localize(PARAM_FORM_LIST, "List"), PARAM_FORM_LIST, "");
		list.setAsImageButton(true);
		form.add(Text.BREAK);
		form.add(list);
		add(form);
	}
}