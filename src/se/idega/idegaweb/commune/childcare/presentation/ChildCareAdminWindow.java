package se.idega.idegaweb.commune.childcare.presentation;

import is.idega.idegaweb.member.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.school.data.SchoolChoice;

import com.idega.block.school.data.School;
import com.idega.builder.business.BuilderLogic;
import com.idega.core.data.Address;
import com.idega.core.data.Email;
import com.idega.core.data.Phone;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.URLUtil;

/**
 * @author laddi
 */
public class ChildCareAdminWindow extends ChildCareBlock {

	public static final String PARAMETER_ACTION = "cc_admin_action";
	public static final String PARAMETER_METHOD = "cc_admin_method";
	public static final String PARAMETER_USER_ID = "cc_user_id";
	public static final String PARAMETER_APPLICATION_ID = "cc_application_id";
	public static final String PARAMETER_PAGE_ID = "cc_page_id";
	public static final String PARAMETER_REJECT_MESSAGE = "cc_reject_message";
	public static final String PARAMETER_OFFER_MESSAGE = "cc_offer_message";

	private final static String USER_MESSAGE_SUBJECT = "child_care.application_received_subject";
	private final static String USER_MESSAGE_BODY = "child_care.application_received_body";

	public static final int METHOD_OVERVIEW = 1;
	public static final int METHOD_REJECT = 2;
	public static final int METHOD_OFFER = 3;

	public static final int ACTION_CLOSE = 0;
	public static final int ACTION_REJECT = 1;
	public static final int ACTION_OFFER = 2;

	private int _method = -1;
	private int _action = -1;

	private int _userID = -1;
	private int _applicationID = -1;
	private int _pageID;

	private boolean _protocol = true;
	private boolean _move = true;
	private boolean _showOnlyOverview = false;
	private boolean _showNoChoices = false;

	private SubmitButton close;
	private Form form;

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parse(iwc);
		switch (_action) {
			case ACTION_CLOSE :
				close(iwc);
				break;
			case ACTION_REJECT :
				reject(iwc);
				break;
			case ACTION_OFFER :
				makeOffer(iwc);
				break;
		}

		if (_method != -1)
			drawForm(iwc);
	}

	private void drawForm(IWContext iwc) throws RemoteException {
		form = new Form();
		form.maintainParameter(PARAMETER_USER_ID);
		form.maintainParameter(PARAMETER_APPLICATION_ID);
		form.maintainParameter(PARAMETER_PAGE_ID);
		form.setStyleAttribute("height:100%");

		Table table = new Table(3, 5);
		table.setRowColor(1, "#000000");
		table.setRowColor(3, "#000000");
		table.setRowColor(5, "#000000");
		table.setColumnColor(1, "#000000");
		table.setColumnColor(3, "#000000");
		table.setColor(2, 2, "#CCCCCC");
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setWidth(2, Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		table.setHeight(4, Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		form.add(table);

		Table headerTable = new Table(1, 1);
		headerTable.setCellpadding(6);
		table.add(headerTable, 2, 2);

		Table contentTable = new Table(1, 1);
		contentTable.setCellpadding(10);
		contentTable.setWidth(Table.HUNDRED_PERCENT);
		contentTable.setHeight(Table.HUNDRED_PERCENT);
		table.add(contentTable, 2, 4);

		close = (SubmitButton) getStyledInterface(new SubmitButton(localize("close_window", "Close"), PARAMETER_ACTION, String.valueOf(ACTION_CLOSE)));

		switch (_method) {
			case METHOD_OVERVIEW :
				headerTable.add(getHeader(localize("child_care.application_overview", "Application overview")));
				contentTable.add(getOverview(iwc));
				break;
			case METHOD_REJECT :
				headerTable.add(getHeader(localize("child_care.reject_application", "Reject application")));
				contentTable.add(getRejectForm(iwc));
				break;
			case METHOD_OFFER :
				headerTable.add(getHeader(localize("child_care.offer_placing", "Offer placing")));
				contentTable.add(getOfferForm(iwc));
				break;
		}
		
		add(form);
	}

	private Table getOverview(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;

		if (_userID != -1) {
			User user = getBusiness().getUserBusiness().getUser(_userID);
			Address address = getBusiness().getUserBusiness().getUsersMainAddress(_userID);

			table.add(getSmallHeader(localize("child_care.name", "Name")), 1, row);
			table.add(getSmallText(user.getNameLastFirst(true)), 2, row++);

			table.add(getSmallHeader(localize("child_care.personal_id", "Personal ID")), 1, row);
			table.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), 2, row++);

			table.add(getSmallHeader(localize("child_care.address", "Address")), 1, row);
			if (address != null)
				table.add(getSmallText(address.getStreetAddress() + ", " + address.getPostalAddress()), 2, row++);
			else
				row++;

			try {
				Collection parents = getBusiness().getUserBusiness().getMemberFamilyLogic().getCustodiansFor(user);
				table.add(getSmallHeader(localize("child_care.custodians", "Custodians")), 1, row);
				if (parents != null && !parents.isEmpty()) {
					Iterator iter = parents.iterator();
					while (iter.hasNext()) {
						User parent = (User) iter.next();
						table.add(getSmallText(parent.getNameLastFirst(true)), 2, row);
						try {
							Phone phone = getBusiness().getUserBusiness().getUsersHomePhone(parent);
							if (phone != null && phone.getNumber() != null) {
								table.add(new Break(), 2, row);
								table.add(getSmallText(localize("child_care.phone","Phone")+": "), 2, row);
								table.add(getSmallText(phone.getNumber()), 2, row);
							}
						}
						catch (NoPhoneFoundException npf) {
						}
						try {
							Email email = getBusiness().getUserBusiness().getUsersMainEmail(parent);
							if (email != null && email.getEmailAddress() != null) {
								Link emailLink = this.getSmallLink(email.getEmailAddress());
								emailLink.setURL("mailto:"+email.getEmailAddress());
								table.add(new Break(), 2, row);
								table.add(getSmallText(localize("child_care.email","E-mail")+": "), 2, row);
								table.add(emailLink, 2, row);
							}
						}
						catch (NoEmailFoundException nef) {
						}
						if (iter.hasNext())
							table.add(new Break(2), 2, row);
					}
				}
				row++;
			}
			catch (NoCustodianFound ncf) {
			}

			Collection applications = getBusiness().getApplicationsForChild(user);
			String message = null;
			IWCalendar queueDate = null;
			IWCalendar placementDate = null;
			if (applications != null && !applications.isEmpty()) {
				table.add(getSmallHeader(localize("child_care.child_care_applications", "Child care applications")), 1, row);

				School school;
				ChildCareApplication application;
				Iterator iter = applications.iterator();
				while (iter.hasNext()) {
					application = (ChildCareApplication) iter.next();

					school = application.getProvider();
					String string = String.valueOf(application.getChoiceNumber()) + ". " + school.getName() + " (" + getBusiness().getLocalizedCaseStatusDescription(application.getCaseStatus(), iwc.getCurrentLocale()) + ")";
					if (application.getProviderId() == getSession().getChildCareID()) {
						table.add(this.getSmallHeader(string), 2, row);
					}
					else {
						table.add(getSmallText(string), 2, row);
					}

					if (iter.hasNext())
						table.add(new Break(), 2, row);
					else {
						queueDate = new IWCalendar(iwc.getCurrentLocale(), application.getQueueDate());
						placementDate = new IWCalendar(iwc.getCurrentLocale(), application.getFromDate());
					}
				}
				row++;
			}

			if (queueDate != null) {
				table.add(getSmallHeader(localize("child_care.queue_date","Queue date")), 1, row);
				table.add(getSmallText(queueDate.getLocaleDate(IWCalendar.SHORT)), 2, row++);
			}
			if (placementDate != null) {
				table.add(getSmallHeader(localize("child_care.placement_date","From date")), 1, row);
				table.add(getSmallText(placementDate.getLocaleDate(IWCalendar.SHORT)), 2, row++);
			}

			table.setColumnVerticalAlignment(1, Table.VERTICAL_ALIGN_TOP);
			table.mergeCells(1, row, table.getColumns(), row);
			table.setHeight(row, Table.HUNDRED_PERCENT);
			table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

			SubmitButton offer = (SubmitButton) getStyledInterface(new SubmitButton(localize("school.make_offer", "Make offer"), PARAMETER_METHOD, String.valueOf(METHOD_OFFER)));
			SubmitButton reject = (SubmitButton) getStyledInterface(new SubmitButton(localize("school.reject", "Reject"), PARAMETER_METHOD, String.valueOf(METHOD_REJECT)));

			if (getSession().getChildCareID() != -1) {
				table.add(offer, 1, row);
				table.add(Text.NON_BREAKING_SPACE, 1, row);
				table.add(reject, 1, row);
				table.add(Text.NON_BREAKING_SPACE, 1, row);
			}

			table.add(close, 1, row);
		}

		return table;
	}
	
	private Table getRejectForm(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;

		User user = iwc.getCurrentUser();
		Email mail = getBusiness().getUserBusiness().getUserMail(user);

		String email = "";
		if (mail != null)
			email = mail.getEmailAddress();

		String workphone = "";
		try {
			Phone phone = getBusiness().getUserBusiness().getUsersWorkPhone(user);
			workphone = phone.getNumber();
		}
		catch (NoPhoneFoundException npfe) {
			workphone = "";
		}

		Object[] arguments = { user.getName(), mail, workphone };

		String message = MessageFormat.format(localize("child_care.reject_application_message", "We are sorry that we cannot offer you a place in our school at present, if you have any questions, please contact {0} via either phone ({1}) or e-mail ({2})."), arguments);
		TextArea textArea = (TextArea) getStyledInterface(new TextArea(PARAMETER_REJECT_MESSAGE, message));
		textArea.setWidth(Table.HUNDRED_PERCENT);
		textArea.setHeight(7);
		textArea.setAsNotEmpty(localize("child_care.reason_for_rejection_needed","You must fill in the message."));

		table.add(getSmallHeader(localize("child_care.reject_application_message_info", "The following message will be sent to the child's parents.")), 1, row++);
		table.add(textArea, 1, row++);

		SubmitButton reject = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.reject", "Reject"), PARAMETER_ACTION, String.valueOf(ACTION_REJECT)));
		table.add(reject, 1, row);
		table.add(Text.NON_BREAKING_SPACE, 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getOfferForm(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;

		User user = iwc.getCurrentUser();
		User child = getBusiness().getUserBusiness().getUser(_userID);
		Email mail = getBusiness().getUserBusiness().getUserMail(user);

		String email = "";
		if (mail != null && mail.getEmailAddress() != null)
			email = mail.getEmailAddress();

		String workphone = "";
		try {
			Phone phone = getBusiness().getUserBusiness().getUsersWorkPhone(user);
			workphone = phone.getNumber();
		}
		catch (NoPhoneFoundException npfe) {
			workphone = "";
		}

		Object[] arguments = { child.getFirstName(), user.getName(), mail, workphone };

		String message = MessageFormat.format(localize("child_care.offer_message", "We can offer {0} a placing in our childcare from (date).\n\nRegards,\n{1}\n{2}\n{3}"), arguments);
		TextArea textArea = (TextArea) getStyledInterface(new TextArea(PARAMETER_OFFER_MESSAGE, message));
		textArea.setWidth(Table.HUNDRED_PERCENT);
		textArea.setHeight(7);
		textArea.setAsNotEmpty(localize("child_care.offer_message_required","You must fill in the message."));

		table.add(getSmallHeader(localize("child_care.offer_message_info", "The following message will be sent to the child's parents.")), 1, row++);
		table.add(textArea, 1, row++);

		SubmitButton reject = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.make_offer", "Make offer"), PARAMETER_ACTION, String.valueOf(ACTION_OFFER)));
		table.add(reject, 1, row);
		table.add(Text.NON_BREAKING_SPACE, 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getChangeDateForm(IWContext iwc) {
		return null;
	}

	private Table getLetterForm(IWContext iwc) {
		return null;
	}

	private void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_METHOD))
			_method = Integer.parseInt(iwc.getParameter(PARAMETER_METHOD));

		if (iwc.isParameterSet(PARAMETER_ACTION))
			_action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));

		if (iwc.isParameterSet(PARAMETER_USER_ID))
			_userID = Integer.parseInt(iwc.getParameter(PARAMETER_USER_ID));

		if (iwc.isParameterSet(PARAMETER_APPLICATION_ID))
			_applicationID = Integer.parseInt(iwc.getParameter(PARAMETER_APPLICATION_ID));

		if (iwc.isParameterSet(PARAMETER_PAGE_ID))
			_pageID = Integer.parseInt(iwc.getParameter(PARAMETER_PAGE_ID));
	}
	
	private void reject(IWContext iwc) {
		String messageHeader = localize("child_care.application_rejected_subject", "Child care application rejected.");
		String messageBody = iwc.getParameter(PARAMETER_REJECT_MESSAGE);
		getBusiness().rejectApplication(_applicationID, messageHeader, messageBody, localize(USER_MESSAGE_SUBJECT,"Application received."), localize(USER_MESSAGE_BODY,"Your application has been received."), iwc.getCurrentUser());

		_method = METHOD_OVERVIEW;
	}
	
	private void makeOffer(IWContext iwc) {
		String messageHeader = localize("child_care.application_rejected_subject", "Child care application rejected.");
		String messageBody = iwc.getParameter(PARAMETER_OFFER_MESSAGE);
		getBusiness().assignApplication(_applicationID, iwc.getCurrentUser(), messageHeader, messageBody);

		_method = METHOD_OVERVIEW;
	}
	
	private void close(IWContext iwc) {
		URLUtil URL = new URLUtil(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().setParentToRedirect(URL.toString());
		getParentPage().close();
	}
}
