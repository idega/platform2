/*
 * $Id: RequestAdminViewDetails.java,v 1.10.4.7 2007/09/04 00:42:46 eiki Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.presentation;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.request.business.RequestFinder;
import is.idega.idegaweb.campus.block.request.data.Request;
import is.idega.idegaweb.campus.block.request.data.RequestHome;
import is.idega.idegaweb.campus.business.CampusSettings;
import is.idega.idegaweb.campus.presentation.CampusWindow;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;
import javax.mail.MessagingException;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.data.ApartmentView;
import com.idega.block.building.data.ApartmentViewHome;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;
import com.idega.util.SendMail;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RequestAdminViewDetails extends CampusWindow {

	protected final static String REQUESTADMIN_SEND = "requestadmin_send";

	protected final static String REQUEST_TYPE = "request_type";

	public final static String REQUEST_STREET = "request_street";

	public final static String REQUEST_APRT = "request_aprt";

	public final static String REQUEST_NAME = "request_name";

	public final static String REQUEST_TEL = "request_tel";

	public final static String REQUEST_EMAIL = "request_email";

	protected final static String REQUESTADMIN_TABLE_TITLE = "requestadmin_table_title";

	protected final static String REQUEST_DATE_OF_CRASH = "request_date_of_crash";

	protected final static String REQUEST_COMMENT = "request_comment";

	protected final static String REQUEST_TIME = "request_time";

	protected final static String REQUEST_DAYTIME = "request_daytime";

	protected final static String REQUEST_SPECIAL_TIME = "request_special_time";
	
	protected final static String REQUEST_SPECIAL_TIME_REQUESTED = "request_special_time_requested";

	protected final static String REQUEST_STATUS = "request_status";

	protected final static String REQUEST_NO_COMMENT = "request_no_comment";

	protected final static String REQUEST_NO_DATE_OF_CRASH = "request_no_date_of_crash";

	protected final static String REQUEST_NO_SPECIAL_TIME = "request_no_special_time";

	protected static final String REQUEST_WAS_REPORTED = "request_was_reported_by_phone";
	
	protected final static String REQUEST_REPAIR = "R";

	protected final static String REQUEST_COMPUTER = "C";

	protected static final String REQUEST_EMAIL_TO = "campus_request_email";

	protected static final String REQUEST_EMAIL_CC = "campus_request_email_cc";

	protected static final String REQUEST_EMAIL_SUBJECT = "request_email_subject";

	private boolean isAdmin;

	private boolean isLoggedOn;

	public RequestAdminViewDetails() {
		setWidth(650);
		setHeight(450);
		setResizable(true);
	}

	protected void control(IWContext iwc) {
		if (isAdmin || isLoggedOn) {

			if (iwc.isParameterSet(REQUESTADMIN_SEND)) {
				boolean check = doSendRequest(iwc);
				if (check) {
					setParentToReload();
					close();
				}
			}

			addMainForm(iwc);
		} else
			add(getNoAccessObject(iwc));

	}

	protected boolean doSendRequest(IWContext iwc) {
		String status = iwc.getParameter(REQUEST_STATUS);
		String id = iwc.getParameter("request_id");

		if (id != null) {
			Request request = null;
			try {
				request = ((RequestHome) IDOLookup
						.getHome(Request.class)).findByPrimaryKey(new Integer(
						id));
				request.setStatus(status);
				request.store();
			} catch (RemoteException e) {
				e.printStackTrace();
				return false;
			} catch (FinderException e) {
				e.printStackTrace();

				return false;
			}
			
			String emailTo = iwc.getParameter(REQUEST_EMAIL_TO);
			String emailCC = iwc.getParameter(REQUEST_EMAIL_CC);
			
			if("".equals(emailTo) && !"".equals(emailCC)){
				emailTo = emailCC;
			}
			
			if("".equals(emailCC)){
				emailCC = null;
			}
			
//			Only send if email sending allowed
			if(!"".equals(emailTo)){
				CampusSettings settings = null;
				try {
					settings = getCampusService(iwc).getCampusSettings();
				
					String dateFailure = request.getDateFailure().toString();
					String comment = request.getDescription();
					String special = request.getSpecialTime();
					
					String aprt = "";
					String street = "";
					String name = "";
					String email = "";
					String telephone = "";
					
					//boolean reported = request.getReportedViaTelephone();
					
					String requestType = request.getRequestType();
					if(REQUEST_COMPUTER.equals(requestType)){
						requestType = localize(REQUEST_COMPUTER, "Computer repairs");
					}
					else{
						requestType = localize(REQUEST_REPAIR, "General repairs");
					}
					
					Contract contract = null;
					ApartmentView apartmentView = null;
					Applicant applicant = null;
					try {
						Collection contracts = getContractService(iwc)
						.getContractHome().findByUserAndRented(
								new Integer(request.getUserId()), Boolean.TRUE);
						contract = (Contract) contracts.iterator().next();
					} catch (Exception e) {
						contract = null;
					}
					if (contract != null) {
						try {
							apartmentView = ((ApartmentViewHome) IDOLookup
									.getHome(ApartmentView.class))
									.findByPrimaryKey(contract.getApartmentId());
						} catch (IDOLookupException e1) {
							e1.printStackTrace();
						} catch (FinderException e1) {
							e1.printStackTrace();
						}

						applicant = contract.getApplicant();
					}

					CampusApplication campusApplication = null;
					try {
						campusApplication = CampusApplicationFinder.getApplicantInfo(applicant).getCampusApplication();
					} catch (Exception e) {
						campusApplication = null;
					}

					if (apartmentView != null) {
						 aprt = apartmentView.getApartmentName();
						 street = apartmentView.getBuildingName();
					}
					
					if (applicant != null) {
						name = applicant.getFullName();
						telephone = applicant.getResidencePhone();
					}
					
					if (campusApplication != null){
						email = campusApplication.getEmail();
					}
					

					if (settings != null && settings.getSendEventMail()) {
						StringBuffer repairInfo = new StringBuffer();
						repairInfo.append(localize(REQUEST_TYPE, "Request type")).append(" : ").append(requestType).append("\n");
						repairInfo.append(localize(REQUEST_STREET, "Building")).append(" : ").append(street).append("\n");
						repairInfo.append(localize(REQUEST_APRT, "Apartment")).append(" : ").append(aprt).append("\n");
						repairInfo.append(localize(REQUEST_NAME, "Tenant")).append(" : ").append(name).append("\n");
						repairInfo.append(localize(REQUEST_TEL, "Phone number")).append(" : ").append(telephone).append("\n");
						repairInfo.append(localize(REQUEST_EMAIL, "Email")).append(" : ").append(email).append("\n");
						
						repairInfo.append(localize(REQUEST_DATE_OF_CRASH, "Failure date")).append(" : ").append(dateFailure).append("\n");
						repairInfo.append(localize(REQUEST_COMMENT, "Comments")).append(" : ").append(comment).append("\n");
						if(special!=null){
							repairInfo.append(localize(REQUEST_SPECIAL_TIME_REQUESTED, "Requested repair time")).append(" : ").append(request.getRequestType()).append("\n");
						}
						
						try {
							SendMail.send(settings.getAdminEmail(), emailTo, emailCC,
									"palli@idega.is", settings.getSmtpServer(),
									localize(REQUEST_EMAIL_SUBJECT,"Repairs request"), repairInfo.toString());
						} catch (MessagingException e) {
							e.printStackTrace();
						}
					}
				
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}
		return true;
	}

	protected void addMainForm(IWContext iwc) {
		Form form = new Form();
		add(form);

		Request request = null;
		String street = null;
		String aprt = null;
		String name = null;
		String telephone = null;
		String email = null;
		String type = null;
		String id = iwc.getParameter("request_id");
		if (id != null) {
			try {
				request = ((RequestHome) IDOLookup.getHome(Request.class))
						.findByPrimaryKey(new Integer(id));
				type = request.getRequestType();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}

		if (request != null) {
			Contract contract = null;
			ApartmentView apartmentView = null;

			Applicant applicant = null;
			try {
				Collection contracts = getContractService(iwc)
						.getContractHome().findByUserAndRented(
								new Integer(request.getUserId()), Boolean.TRUE);
				contract = (Contract) contracts.iterator().next();
			} catch (Exception e) {
				contract = null;
			}
			if (contract != null) {
				try {
					apartmentView = ((ApartmentViewHome) IDOLookup
							.getHome(ApartmentView.class))
							.findByPrimaryKey(contract.getApartmentId());
				} catch (IDOLookupException e1) {
					e1.printStackTrace();
				} catch (FinderException e1) {
					e1.printStackTrace();
				}

				applicant = contract.getApplicant();
			}

			CampusApplication campusApplication = null;
			try {
				campusApplication = CampusApplicationFinder.getApplicantInfo(
						applicant).getCampusApplication();
			} catch (Exception e) {
				campusApplication = null;
			}

			if (apartmentView != null) {
				aprt = apartmentView.getApartmentName();
				street = apartmentView.getBuildingName();
			}
			if (applicant != null) {
				name = applicant.getFullName();
				telephone = applicant.getResidencePhone();
			}
			if (campusApplication != null)
				email = campusApplication.getEmail();
		}

		DataTable data = new DataTable();
		data.setWidth("100%");
		data.addTitle(localize(REQUESTADMIN_TABLE_TITLE, "View request"));
		data.addButton(new SubmitButton(REQUESTADMIN_SEND, "Update request"));
		form.add(data);

		int row = 1;

		data.add(getHeader(localize(REQUEST_STREET, "Building")), 1, row);
		data.add(getText(street), 2, row);

		row++;
		data.add(getHeader(localize(REQUEST_APRT, "Apartment")), 1, row);
		data.add(getText(aprt), 2, row);

		row++;

		data.add(getHeader(localize(REQUEST_NAME, "Name")), 1, row);
		data.add(getText(name), 2, row);

		row++;

		data.add(getHeader(localize(REQUEST_TEL, "Phone")), 1, row);
		data.add(getText(telephone), 2, row);

		row++;

		data.add(getHeader(localize(REQUEST_EMAIL, "Email")), 1, row);
		data.add(getText(email), 2, row);
		
		row++;

		addRepair(data, row, request,iwc);

		form.add(new HiddenInput("request_id", id));
	}

	protected void addRepair(DataTable data, int row, Request request,IWContext iwc) {
		data.add(getHeader(localize(REQUEST_DATE_OF_CRASH, "Date of failure")),1, row);

		Timestamp dateFailure = null;
		String comment = null;
		String special = null;
		String requestStatus = null;
		boolean reported = false;
		try {
			dateFailure = request.getDateFailure();
			comment = request.getDescription();
			special = request.getSpecialTime();
			requestStatus = request.getStatus();
			reported = request.getReportedViaTelephone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		data.add(getHeader(dateFailure.toString()), 2, row);
		row++;

		data.add(getHeader(localize(REQUEST_COMMENT, "Comments")), 1, row);
		data.add(getHeader(comment), 2, row);

		row++;
		RadioButton b1 = new RadioButton(REQUEST_TIME, REQUEST_DAYTIME);
		b1.setDisabled(true);
		if (special == null || special.equals(""))
			b1.setSelected();
		data.add(b1, 1, row);

		data.add(getHeader(localize(REQUEST_DAYTIME,"Repairs may be done during business hours even though I'm not at home.")),2, row);

		row++;
		RadioButton b2 = new RadioButton(REQUEST_TIME, REQUEST_DAYTIME);
		b2.setDisabled(true);
		if (special != null && !special.equals(""))
			b2.setSelected();
		data.add(b2, 1, row);

		data.add(getHeader(localize(REQUEST_SPECIAL_TIME,"I would like to have the repairs done at this specific date/time: ")),2, row);

		if (special != null){
			data.add(getHeader(special), 2, row);	
		}
		row++;

		if(reported){
			data.add(getHeader("X"),1, row);
			data.add(getHeader(localize(REQUEST_WAS_REPORTED,"This request was already reported by telephone.")),2, row);
			row++;
		}
		
		data.add(getHeader(localize(REQUEST_EMAIL_TO, "Email to")), 1, row);
		TextInput mailto = new TextInput(REQUEST_EMAIL_TO);
		mailto.setValue(iwc.getApplicationSettings().getProperty(REQUEST_EMAIL_TO,""));
		data.add(mailto, 2, row);
		row++;
		
		data.add(getHeader(localize(REQUEST_EMAIL_CC, "Email CC to")), 1, row);
		TextInput mailtoCC = new TextInput(REQUEST_EMAIL_CC);
		mailtoCC.setValue(iwc.getApplicationSettings().getProperty(REQUEST_EMAIL_CC,""));
		data.add(mailtoCC, 2, row);
		row++;
		
		DropdownMenu status = new DropdownMenu(REQUEST_STATUS);

		status.addMenuElement(RequestFinder.REQUEST_STATUS_SENT, localize("REQUEST_STATUS_S", "S"));
		if(Boolean.parseBoolean(iwc.getApplicationSettings().getProperty("CAMPUS_USE_RECEIVED_STATUS","true"))){
			status.addMenuElement(RequestFinder.REQUEST_STATUS_RECEIVED, localize("REQUEST_STATUS_R", "R"));
		}
		status.addMenuElement(RequestFinder.REQUEST_STATUS_IN_PROGRESS,localize("REQUEST_STATUS_P", "P"));
		status.addMenuElement(RequestFinder.REQUEST_STATUS_DONE, localize("REQUEST_STATUS_D", "D"));
		status.addMenuElement(RequestFinder.REQUEST_STATUS_DENIED, localize("REQUEST_STATUS_X", "X"));

		Edit.setStyle(status);
		if (requestStatus != null){
			status.setSelectedElement(requestStatus);
		}
		
		data.add(getHeader(localize(REQUEST_STATUS, "Status")), 1, row);
		data.add(status, 2, row);
		row++;
	}

	public void main(IWContext iwc) throws Exception {
		isAdmin = iwc.hasEditPermission(this);
		isLoggedOn = LoginBusinessBean.isLoggedOn(iwc);
		control(iwc);
	}
}