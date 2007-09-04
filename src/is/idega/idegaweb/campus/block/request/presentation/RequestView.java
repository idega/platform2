/*
 * $Id: RequestView.java,v 1.12.4.4 2007/09/04 11:14:03 eiki Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.presentation;

import is.idega.idegaweb.campus.block.request.business.RequestBusiness;
import is.idega.idegaweb.campus.presentation.CampusWindow;

import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.user.data.User;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;
import com.idega.util.IWTimestamp;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RequestView extends CampusWindow {

	protected final static String REQUEST_SEND = "request_send";

	protected final static String REQUEST_TYPE = "request_type";

	public final static String REQUEST_STREET = "request_street";

	public final static String REQUEST_APRT = "request_aprt";

	public final static String REQUEST_NAME = "request_name";

	public final static String REQUEST_TEL = "request_tel";

	public final static String REQUEST_EMAIL = "request_email";

	protected final static String REQUEST_TABLE_TITLE = "request_table_title";

	protected final static String REQUEST_DATE_OF_CRASH = "request_date_of_crash";

	protected final static String REQUEST_COMMENT = "request_comment";

	protected final static String REQUEST_TIME = "request_time";

	protected final static String REQUEST_DAYTIME = "request_daytime";

	protected final static String REQUEST_SPECIAL_TIME = "request_special_time";

	protected final static String REQUEST_NO_COMMENT = "request_no_comment";

	protected final static String REQUEST_NO_DATE_OF_CRASH = "request_no_date_of_crash";

	protected final static String REQUEST_NO_SPECIAL_TIME = "request_no_special_time";

	protected final static String REQUEST_REPAIR = "request_repair";

	protected final static String REQUEST_COMPUTER = "request_computer";

	protected static final String REQUEST_REPORTED = "request_reported_by_phone";

	private boolean isAdmin;

	private boolean isLoggedOn;

	private User eUser = null;

	/**
	 * 
	 */
	public RequestView() {
		setWidth(650);
		setHeight(450);
		setResizable(true);
	}

	/**
	 * 
	 */
	public String getBundleIdentifier() {
		return (IW_BUNDLE_IDENTIFIER);
	}

	/**
	 * 
	 */
	protected void control(IWContext iwc) {

		if (isAdmin || isLoggedOn) {

			if (iwc.isParameterSet(REQUEST_SEND)) {
				boolean check = doSendRequest(iwc);
				if (check) {
					setParentToReload();
					close();
				}
			}

			addMainForm(iwc);
		} else
			add(getText(localize("access_denied", "Access denied")));
	}

	/**
	 * 
	 */
	protected boolean doSendRequest(IWContext iwc) {
		String comment = iwc.getParameter(REQUEST_COMMENT);
		String dateOfFailureString = iwc.getParameter(REQUEST_DATE_OF_CRASH);
		String type = iwc.getParameter(REQUEST_TYPE);
		if (type.equals(REQUEST_COMPUTER))
			type = RequestBusiness.REQUEST_COMPUTER;
		else
			type = RequestBusiness.REQUEST_REPAIR;
		String special = iwc.getParameter(REQUEST_SPECIAL_TIME);
		boolean reported = iwc.isParameterSet(REQUEST_REPORTED);

		System.out.println("DateOfFailureString = " + dateOfFailureString);

		IWTimestamp t = new IWTimestamp();
		t = IWTimestamp.RightNow();

		boolean insert = RequestBusiness.insertRequest(((Integer) eUser
				.getPrimaryKey()).intValue(), comment, t.getTimestamp(), type,
				special,reported);

		return (insert);
	}

	/**
	 * 
	 */
	protected void addMainForm(IWContext iwc) {
		Form form = new Form();
		add(form);

		DropdownMenu mnu = new DropdownMenu(REQUEST_TYPE);
		mnu.addMenuElement(REQUEST_COMPUTER, "Computer repairs");
		mnu.addMenuElement(REQUEST_REPAIR, "General repairs");
		mnu.setToSubmit();
		Edit.setStyle(mnu);
		// form.add(mnu);

		String type = iwc.getParameter(REQUEST_TYPE);
		if (type == null)
			type = REQUEST_REPAIR;
		mnu.setSelectedElement(type);

		String street = iwc.getParameter(REQUEST_STREET);
		String aprt = iwc.getParameter(REQUEST_APRT);
		String name = iwc.getParameter(REQUEST_NAME);
		String telephone = iwc.getParameter(REQUEST_TEL);
		String email = iwc.getParameter(REQUEST_EMAIL);

		DataTable data = new DataTable();
		data.setWidth("100%");
		data.addTitle(localize(REQUEST_TABLE_TITLE, localize(REQUEST_SEND,"Send request")));
		data.addButton(new SubmitButton(REQUEST_SEND, localize(REQUEST_SEND,"Send request")));
		form.add(data);

		int row = 1;
		data.add(getHeader(localize(REQUEST_TYPE, "Request type")), 1, row);
		data.add(mnu, 2, row);
		row++;
		data.add(getHeader(localize(REQUEST_STREET, "Building")), 1, row);
		data.add(getText(street), 2, row);
		row++;
		data.add(getHeader(localize(REQUEST_APRT, "Apartment")), 1, row);
		data.add(getText(aprt), 2, row);
		row++;
		data.add(getHeader(localize(REQUEST_NAME, "Tenant")), 1, row);
		data.add(getText(name), 2, row);
		row++;
		data.add(getHeader(localize(REQUEST_TEL, "Phone number")), 1, row);
		data.add(getText(telephone), 2, row);
		row++;
		data.add(getHeader(localize(REQUEST_EMAIL, "Email")), 1, row);
		data.add(getText(email), 2, row);
		row++;

		if (type.equals(REQUEST_REPAIR)){
			addRepair(data, row,true);
		}
		else if (type.equals(REQUEST_COMPUTER)){
			addRepair(data, row,false);
		}

		form.add(new HiddenInput(REQUEST_STREET, street));
		form.add(new HiddenInput(REQUEST_APRT, aprt));
		form.add(new HiddenInput(REQUEST_NAME, name));
		form.add(new HiddenInput(REQUEST_TEL, telephone));
		form.add(new HiddenInput(REQUEST_EMAIL, email));
	}

	/**
	 * 
	 */
	protected void addRepair(DataTable data, int row, boolean showRadioButtons) {
		data.add(getHeader(localize(REQUEST_DATE_OF_CRASH, "Failure date")), 1,row);
		DateInput dateOfCrash = new DateInput(REQUEST_DATE_OF_CRASH);
		dateOfCrash.setToCurrentDate();
		Edit.setStyle(dateOfCrash);
		data.add(dateOfCrash, 2, row);
		row++;
		data.add(getHeader(localize(REQUEST_COMMENT, "Comments")), 1, row);
		TextArea comment = new TextArea(REQUEST_COMMENT, "", 60, 5);
		Edit.setStyle(comment);
		data.add(comment, 2, row);
		row++;
		if(showRadioButtons){
			RadioButton radioButton = new RadioButton(REQUEST_TIME, REQUEST_DAYTIME);
			radioButton.setSelected();
			data.add(radioButton, 1, row);
			data.add(getHeader(localize(REQUEST_DAYTIME,"Repairs may be done during business hours even though I'm not at home.")), 2, row);
			row++;
			data.add(new RadioButton(REQUEST_TIME, REQUEST_SPECIAL_TIME), 1, row);
		}
		data.add(getHeader(localize(REQUEST_SPECIAL_TIME,"I would like to have the repairs done at this specific date/time: ")),2, row);
		data.add(new TextInput(REQUEST_SPECIAL_TIME), 2, row);
		row++;
		
		CheckBox reportedByPhone = new CheckBox(REQUEST_REPORTED);
		data.add(reportedByPhone, 1, row);
		data.add(getHeader(localize(REQUEST_REPORTED,"Problem already reported by telephone.")),2, row);
		row++;
	}


	/**
	 * 
	 */
	public void main(IWContext iwc) throws Exception {
		eUser = iwc.getCurrentUser();
		isAdmin = iwc.hasEditPermission(this);
		isLoggedOn = LoginBusinessBean.isLoggedOn(iwc);
		control(iwc);
	}
}
