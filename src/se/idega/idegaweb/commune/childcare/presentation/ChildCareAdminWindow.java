package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.block.pki.business.NBSLoginBusinessBean;
import se.idega.block.pki.data.NBSSignedEntity;
import se.idega.block.pki.presentation.NBSSigningBlock;
import se.idega.idegaweb.commune.childcare.business.NoPlacementFoundException;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;

import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractHome;
import com.idega.block.contract.data.ContractTag;
import com.idega.block.contract.data.ContractTagHome;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.block.school.presentation.SchoolClassDropdownDouble;
import com.idega.builder.business.BuilderLogic;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.user.business.UserBusiness;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

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
	public static final String PARAMETER_PRIORITY_MESSAGE = "cc_priority_message";
	public static final String PARAMETER_CHANGE_DATE = "cc_change_date";
	public static final String PARAMETER_CHILDCARE_TIME = "cc_childcare_time";
	public static final String PARAMETER_GROUP_NAME = "cc_group_name";
	public static final String PARAMETER_OLD_GROUP = "cc_old_group";
	public static final String PARAMETER_THREE_MONTHS_PROGNOSIS = "cc_three_months";
	public static final String PARAMETER_ONE_YEAR_PROGNOSIS = "cc_one_year";
	public static final String PARAMETER_THREE_MONTHS_PRIORITY = "cc_three_months_priority";
	public static final String PARAMETER_ONE_YEAR_PRIORITY = "cc_one_year_priority";
	public static final String PARAMETER_PROVIDER_CAPACITY = "cc_provider_capacity";
	public static final String PARAMETER_OFFER_VALID_UNTIL = "cc_offer_valid_until";
	public static final String PARAMETER_CANCEL_REASON = "cc_cancel_reason";
	public static final String PARAMETER_CANCEL_MESSAGE = "cc_cancel_message";
	public static final String PARAMETER_CANCEL_DATE = "cc_cancel_date";
	public static final String PARAMETER_EARLIEST_DATE = "cc_earliest_date";
	public static final String PARAMETER_CONTRACT_ID = "cc_contract_id";	
	public static final String PARAMETER_TEXT_FIELD = "cc_xml_signing_text_field";
	public static final String PARAMETER_PRE_SCHOOL = "cc_pre_school";	
	public static final String PARAMETER_SCHOOL_TYPES = "cc_school_types";	
	public static final String PARAMETER_EMPLOYMENT_TYPE = "cc_employment_type";
	public static final String PARAMETER_PLACEMENT_ID = "cc_placement_id";
	public static final String PARAMETER_SCHOOL_CLASS = "cc_sch_class";
	
	private static final String PROPERTY_RESTRICT_DATES = "child_care_restrict_alter_date";
	
	public static final String FIELD_CURRENT_DATE = "currentdate";
	

	//private final static String USER_MESSAGE_SUBJECT = "child_care.application_received_subject";
	//private final static String USER_MESSAGE_BODY = "child_care.application_received_body";

	public static final int METHOD_GRANT_PRIORITY = 2;
	public static final int METHOD_OFFER = 3;
	public static final int METHOD_CHANGE_DATE = 4;
	public static final int METHOD_CREATE_CONTRACT = 5;
	public static final int METHOD_CREATE_GROUP = 6;
	public static final int METHOD_PLACE_IN_GROUP = 7;
	public static final int METHOD_MOVE_TO_GROUP = 8;
	public static final int METHOD_UPDATE_PROGNOSIS = 9;
	public static final int METHOD_ALTER_CARE_TIME = 10;
	public static final int METHOD_CANCEL_CONTRACT = 11;
	public static final int METHOD_CHANGE_OFFER = 12;
	public static final int METHOD_RETRACT_OFFER = 13;
	public static final int METHOD_ALTER_VALID_FROM_DATE = 14;
	public static final int METHOD_VIEW_PROVIDER_QUEUE = 15;
	public static final int METHOD_END_CONTRACT = 16;
	public static final int METHOD_NEW_CARE_TIME = 17;
	public static final int METHOD_SIGN_CONTRACT = 18;
	public static final int METHOD_REJECT_APPLICATION = 19;
	
	public static final int ACTION_CLOSE = 0;
	public static final int ACTION_GRANT_PRIORITY = 1;
	public static final int ACTION_OFFER = 2;
	public static final int ACTION_CHANGE_DATE = 3;
	public static final int ACTION_PARENTS_AGREE = 4;
	public static final int ACTION_CREATE_CONTRACT = 5;
	public static final int ACTION_CREATE_GROUP = 6;
	public static final int ACTION_PLACE_IN_GROUP = 7;
	public static final int ACTION_MOVE_TO_GROUP = 8;
	public static final int ACTION_CANCEL_CONTRACT = 9;
	public static final int ACTION_UPDATE_PROGNOSIS = 10;
	public static final int ACTION_ALTER_CARE_TIME = 11;
	public static final int ACTION_DELETE_GROUP = 12;
	public static final int ACTION_RETRACT_OFFER = 13;
	public static final int ACTION_REMOVE_FUTURE_CONTRACTS = 14;
	public static final int ACTION_CREATE_CONTRACT_FOR_BANKID = 15;
	public static final int ACTION_ALTER_VALID_FROM_DATE = 16;
	public static final int ACTION_END_CONTRACT = 17;	
	public static final int ACTION_NEW_CARE_TIME = 18;
	public static final int ACTION_SIGN_CONTRACT = 19;	
	public static final int ACTION_REJECT_APPLICATION = 20;	

	private int _method = -1;
	private int _action = -1;

	private int _userID = -1;
	private int _applicationID = -1;
	private int _placementID = -1;
	private int _pageID;
	
	private IWTimestamp earliestDate;

	private CloseButton close;
	private Form form;
	private boolean restrictDates = false;
	boolean onlyAllowFutureCareDate = true; //Changed according to #nacc149

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parse(iwc);
		switch (_action) {
			case ACTION_CLOSE :
				close();
				break;
			case ACTION_GRANT_PRIORITY :
				grantPriority(iwc);
				break;
			case ACTION_OFFER :
				makeOffer(iwc);
				break;
			case ACTION_CHANGE_DATE :
				changeDate(iwc);
				break;
			case ACTION_PARENTS_AGREE :
				parentsAgree(iwc);
				break;
			case ACTION_CREATE_CONTRACT :
				createContract(iwc);
				break;
			case ACTION_CREATE_GROUP :
				createGroup(iwc);
				break;
			case ACTION_PLACE_IN_GROUP :
				placeInGroup(iwc);
				break;
			case ACTION_MOVE_TO_GROUP :
				moveToGroup(iwc);
				break;
			case ACTION_CANCEL_CONTRACT :
				cancelContract(iwc);
				break;
			case ACTION_UPDATE_PROGNOSIS :
				updatePrognosis(iwc);
				break;
			case ACTION_ALTER_CARE_TIME :
				alterCareTime(iwc);
				break;
			case ACTION_DELETE_GROUP :
				deleteGroup(iwc);
				break;
			case ACTION_RETRACT_OFFER :
				retractOffer(iwc);
				break;
			case ACTION_REMOVE_FUTURE_CONTRACTS :
				removeFutureContracts(iwc);
				break;
			case ACTION_CREATE_CONTRACT_FOR_BANKID :
				createContractForBankID(iwc);
				break;
			case ACTION_ALTER_VALID_FROM_DATE :
				alterValidFromDate(iwc);
				break;
			case ACTION_END_CONTRACT :
				sendEndContractRequest(iwc);
				break;
			case ACTION_NEW_CARE_TIME :
				sendNewCareTimeRequest(iwc);
				break;	
			case ACTION_SIGN_CONTRACT :
				processSignContract(iwc);
				break;								
			case ACTION_REJECT_APPLICATION :
				rejectApplication(iwc);
				break;								
		}

		if (_method != -1)
			drawForm(iwc);
	}

	private void drawForm(IWContext iwc) throws RemoteException, Exception {
		form = new Form();
		form.maintainParameter(PARAMETER_USER_ID);
		form.maintainParameter(PARAMETER_APPLICATION_ID);
		form.maintainParameter(PARAMETER_PAGE_ID);
		form.maintainParameter(PARAMETER_CONTRACT_ID);
		form.maintainParameter(PARAMETER_PLACEMENT_ID);
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

		close = (CloseButton) getStyledInterface(new CloseButton(localize("close_window", "Close")));

		//close.setPageToOpen(getParentPageID());
		//close.addParameterToPage(PARAMETER_ACTION, ACTION_CLOSE);

		switch (_method) {
			case METHOD_GRANT_PRIORITY :
				headerTable.add(getHeader(localize("child_care.grant_priority", "Grant priority")));
				contentTable.add(getPriorityForm(iwc));
				break;
			case METHOD_OFFER :
				headerTable.add(getHeader(localize("child_care.offer_placing", "Offer placing")));
				contentTable.add(getOfferForm(iwc));
				break;
			case METHOD_CHANGE_DATE :
				headerTable.add(getHeader(localize("child_care.change_date", "Change date")));
				contentTable.add(getChangeDateForm(iwc, false));
				break;
			case METHOD_PLACE_IN_GROUP :
				headerTable.add(getHeader(localize("child_care.place_in_group", "Place in group")));
				contentTable.add(getPlaceInGroupForm());
				break;
			case METHOD_MOVE_TO_GROUP :
				headerTable.add(getHeader(localize("child_care.move_to_group", "Move to group")));
				contentTable.add(getMoveGroupForm(iwc));
				break;
			case METHOD_CREATE_GROUP :
				if (getSession().getGroupID() != -1)
					headerTable.add(getHeader(localize("child_care.change_group", "Change group")));
				else
					headerTable.add(getHeader(localize("child_care.create_group", "Create group")));
				contentTable.add(getCreateGroupForm());
				break;
			case METHOD_UPDATE_PROGNOSIS :
				headerTable.add(getHeader(localize("child_care.set_prognosis", "Set prognosis")));
				contentTable.add(getUpdatePrognosisForm());
				break;
			case METHOD_ALTER_CARE_TIME :
				headerTable.add(getHeader(localize("child_care.alter_care_time", "Alter care time")));
				contentTable.add(getAlterCareTimeForm(iwc));
				break;
			case METHOD_CANCEL_CONTRACT :
				headerTable.add(getHeader(localize("child_care.cancel_contract", "Cancel contract")));
				contentTable.add(getCancelContractForm(iwc));
				break;
			case METHOD_CHANGE_OFFER :
				headerTable.add(getHeader(localize("child_care.change_offer_placing", "Change offer placing")));
				contentTable.add(getChangeOfferForm(iwc));
				break;
			case METHOD_RETRACT_OFFER :
				headerTable.add(getHeader(localize("child_care.retract_offer", "Retract offer")));
				contentTable.add(getRetractOfferForm(iwc));
				break;
			case METHOD_ALTER_VALID_FROM_DATE :
				headerTable.add(getHeader(localize("child_care.alter_valid_from_date", "Change placement date")));
				contentTable.add(getChangeDateForm(iwc, true));
				break;
			case METHOD_VIEW_PROVIDER_QUEUE :
				headerTable.add(getHeader(localize("child_care.view_provider_queue", "Provider queue")));			
				contentTable.add(getProviderQueueForm(iwc));	
				break;	
			case METHOD_END_CONTRACT :
				headerTable.add(getHeader(localize("child_care.end_contract", "End contract")));			
				contentTable.add(getEndContractForm());	
				break;	
			case METHOD_NEW_CARE_TIME :
				headerTable.add(getHeader(localize("child_care.new_care_time", "New care time")));			
				contentTable.add(getNewCareTimeForm());	
				break;		
			case METHOD_SIGN_CONTRACT :
	
				Object[] contractFormResult = getContractSignerForm(iwc);	
				contentTable.add(contractFormResult[1]);	
				if (((Boolean) contractFormResult[0]).booleanValue()) {
					headerTable.add(getHeader(localize("child_care.sign_contract", "Sign contract")));
				} else {
					headerTable.add(getHeader(localize("child_care.fill_in_fields", "Fill in contract fields")));										
				}
				break;		
			case METHOD_REJECT_APPLICATION :
				headerTable.add(getHeader(localize("child_care.reject_application", "Reject application")));			
				contentTable.add(getRejectApplicationForm(iwc));	
				break;		
							
		}
		
		add(form);
	}

	private Table getPriorityForm(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;

		String message = MessageFormat.format(localize("child_care.priority_message", "Because of special circumstances, {0} has been granted priority in our queue for a childcare placing.\n\nRegards,\n{1}\n{2}\n{3}"), getArguments(iwc));
		TextArea textArea = (TextArea) getStyledInterface(new TextArea(PARAMETER_PRIORITY_MESSAGE, message));
		textArea.setWidth(Table.HUNDRED_PERCENT);
		textArea.setRows(7);
		textArea.setAsNotEmpty(localize("child_care.priority_message_required","You must fill in the message."));

		table.add(getSmallHeader(localize("child_care.priority_message_info", "The following message will be sent to BUN.")), 1, row++);
		table.add(textArea, 1, row++);

		SubmitButton grantPriority = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.grant_priority", "Grant priority"), PARAMETER_ACTION, String.valueOf(ACTION_GRANT_PRIORITY)));
		form.setToDisableOnSubmit(grantPriority, true);
		table.add(grantPriority, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
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

		String message = null;
		if (getBusiness().isAfterSchoolApplication(_applicationID)) {
			message = MessageFormat.format(localize("after_school_care.offer_message", "We can offer {0} a placing in {5} from {4}.\n\nRegards,\n{1}\n{2}\n{3}"), getArguments(iwc));
		}
		else {
			message = MessageFormat.format(localize("child_care.offer_message", "We can offer {0} a placing in {5} from {4}.\n\nRegards,\n{1}\n{2}\n{3}"), getArguments(iwc));
		}
		TextArea textArea = (TextArea) getStyledInterface(new TextArea(PARAMETER_OFFER_MESSAGE, message));
		textArea.setWidth(Table.HUNDRED_PERCENT);
		textArea.setRows(7);
		textArea.setAsNotEmpty(localize("child_care.offer_message_required","You must fill in the message."));

		table.add(getSmallHeader(localize("child_care.offer_message_info", "The following message will be sent to the child's parents.")), 1, row++);
		table.add(textArea, 1, row++);

		IWTimestamp stamp = new IWTimestamp();
		stamp.addDays(14);
		IWTimestamp tomorrow = new IWTimestamp();
		tomorrow.addDays(1);
		DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_OFFER_VALID_UNTIL));
		dateInput.setEarliestPossibleDate(tomorrow.getDate(), localize("child_care.cant_choose_earlier_date", "You can't choose a date back in time."));
		dateInput.setDate(stamp.getDate());

		table.add(getSmallHeader(localize("child_care.offer_valid_until", "Offer valid until")+":"), 1, row++);
		table.add(dateInput, 1, row++);

		
		HiddenInput action = new HiddenInput(PARAMETER_ACTION);
		action.setValue(String.valueOf(ACTION_OFFER));
		table.add(action);
		SubmitButton offer = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.make_offer", "Make offer")));
		form.setToDisableOnSubmit(offer, true);
		table.add(offer, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getChangeOfferForm(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;

		String message = MessageFormat.format(localize("child_care.change_offer_message", "We are extending our offer for a placing for {0} in our childcare since you haven't answered our previous offer.\n\nRegards,\n{1}\n{2}\n{3}"), getArguments(iwc));
		TextArea textArea = (TextArea) getStyledInterface(new TextArea(PARAMETER_OFFER_MESSAGE, message));
		textArea.setWidth(Table.HUNDRED_PERCENT);
		textArea.setRows(7);
		textArea.setAsNotEmpty(localize("child_care.offer_message_required","You must fill in the message."));

		table.add(getSmallHeader(localize("child_care.offer_message_info", "The following message will be sent to the child's parents.")), 1, row++);
		table.add(textArea, 1, row++);

		IWTimestamp stamp = new IWTimestamp();
		stamp.addDays(14);
		DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_OFFER_VALID_UNTIL));
		dateInput.setDate(stamp.getDate());

		table.add(getSmallHeader(localize("child_care.offer_valid_until", "Offer valid until")+":"), 1, row++);
		table.add(dateInput, 1, row++);

		SubmitButton changeOffer = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.change_offer", "Change offer"), PARAMETER_ACTION, String.valueOf(ACTION_OFFER)));
		form.setToDisableOnSubmit(changeOffer, true);
		table.add(changeOffer, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getRetractOfferForm(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;

		String message = MessageFormat.format(localize("child_care.retract_offer_message", "We have retracted our offer for {0} for a placing in our childcare because you haven't replied to our offer.\n\nRegards,\n{1}\n{2}\n{3}"), getArguments(iwc));
		TextArea textArea = (TextArea) getStyledInterface(new TextArea(PARAMETER_OFFER_MESSAGE, message));
		textArea.setWidth(Table.HUNDRED_PERCENT);
		textArea.setRows(7);
		textArea.setAsNotEmpty(localize("child_care.offer_message_required","You must fill in the message."));

		table.add(getSmallHeader(localize("child_care.offer_message_info", "The following message will be sent to the child's parents.")), 1, row++);
		table.add(textArea, 1, row++);

		SubmitButton retract = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.retract_offer", "Retract offer"), PARAMETER_ACTION, String.valueOf(ACTION_RETRACT_OFFER)));
		form.setToDisableOnSubmit(retract, true);
		table.add(retract, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getRejectApplicationForm(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;

		String message = MessageFormat.format(localize("child_care.reject_application_message", "We have rejected your application for {0} for a placing in {5}.\n\nRegards,\n{1}\n{2}\n{3}"), getArguments(iwc));
		TextArea textArea = (TextArea) getStyledInterface(new TextArea(PARAMETER_REJECT_MESSAGE, message));
		textArea.setWidth(Table.HUNDRED_PERCENT);
		textArea.setRows(7);
		textArea.setAsNotEmpty(localize("child_care.rejected_message_required","You must fill in the message."));

		table.add(getSmallHeader(localize("child_care.offer_message_info", "The following message will be sent to the child's parents.")), 1, row++);
		table.add(textArea, 1, row++);

		SubmitButton reject = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.reject_application", "Reject application"), PARAMETER_ACTION, String.valueOf(ACTION_REJECT_APPLICATION)));
		table.add(reject, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getChangeDateForm(IWContext iwc, boolean isAlteration) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		ChildCareApplication application = getBusiness().getApplication(_applicationID);

		DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_CHANGE_DATE));
		IWTimestamp stampNow = new IWTimestamp();
		boolean oldPlacementTerminated = false;
		IWTimestamp terminationDate = null;
		if (restrictDates) {
			if (earliestDate != null) {
				dateInput.setEarliestPossibleDate(earliestDate.getDate(), localize("child_care.contract_dates_overlap", "You can not choose a date which overlaps another contract."));
				dateInput.setDate(earliestDate.getDate());
				terminationDate = new IWTimestamp(earliestDate);
				oldPlacementTerminated = true;
			}
			else {
				ChildCareContract archive = getBusiness().getLatestContract(_userID);
				if (archive != null && archive.getTerminatedDate() != null) {
					IWTimestamp stamp = new IWTimestamp(archive.getTerminatedDate());
					terminationDate = new IWTimestamp(stamp);
					oldPlacementTerminated = true;
					stamp.addDays(1);
					if (stamp.isEarlierThan(stampNow)) {
						dateInput.setEarliestPossibleDate(stampNow.getDate(), localize("child_care.not_a_valid_date", "You can not choose a date back in time."));
						dateInput.setDate(stampNow.getDate());
					}
					else {
						dateInput.setEarliestPossibleDate(stamp.getDate(), localize("child_care.contract_dates_overlap", "You can not choose a date which overlaps another contract."));
						dateInput.setDate(stamp.getDate());
					}
				}
				else {
					dateInput.setEarliestPossibleDate(stampNow.getDate(), localize("child_care.not_a_valid_date", "You can not choose a date back in time."));
					if (application != null) {
						IWTimestamp fromDate = new IWTimestamp(application.getFromDate());
						if (fromDate.isLaterThan(stampNow))
							dateInput.setDate(fromDate.getDate());
						else
							dateInput.setDate(stampNow.getDate());
					}
					else
						dateInput.setDate(stampNow.getDate());
				}
			}
		}
		else {
			dateInput.setDate(stampNow.getDate());
		}
		
		String dateHeader = null;
		if (isAlteration) {
			dateHeader = localize("child_care.new_date", "Select the new placement date");
		} else {
			dateHeader = localize("child_care.change_date", "Change date");
		}
		table.add(getSmallHeader(dateHeader), 1, row++);
		table.add(dateInput, 1, row++);
		if (oldPlacementTerminated) {
			table.add(getSmallHeader(localize("child_care.old_placement_terminated", "Old placement terminated") + ":"), 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(getSmallErrorText(terminationDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)));
		}

		if (isAlteration) {
			TextInput textInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_CHILDCARE_TIME));
			textInput.setLength(2);
			if (application != null)
				textInput.setContent(String.valueOf(application.getCareTime()));
			textInput.setAsNotEmpty(localize("child_care.child_care_time_required","You must fill in the child care time."));
			textInput.setAsIntegers(localize("child_care.only_integers_allowed","Not a valid child care time."));
	
			table.add(getSmallHeader(localize("child_care.enter_child_care_time", "Enter child care time:")), 1, row++);
			table.add(getSmallText(localize("child_care.child_care_time", "Time")+":"), 1, row);
			table.add(textInput, 1, row++);
		}
		
		//Pre-school
		if (!getBusiness().isAfterSchoolApplication(application)) {
			table.add(getSmallHeader(localize("child_care.pre_school", "Specify pre-school:")), 1, row++);
			TextInput preSchool = (TextInput) getStyledInterface(new TextInput(PARAMETER_PRE_SCHOOL));
			preSchool.setLength(40);
			if (application.getPreSchool() != null)
				preSchool.setContent(application.getPreSchool());
			table.add(preSchool, 1, row++);
		}
		
		SubmitButton changeDate = null;
		if (isAlteration)
			changeDate = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.alter_placing", "Change placing"), PARAMETER_ACTION, String.valueOf(ACTION_ALTER_VALID_FROM_DATE)));
		else
			changeDate = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.change_date", "Change date"), PARAMETER_ACTION, String.valueOf(ACTION_CHANGE_DATE)));
		form.setToDisableOnSubmit(changeDate, true);
		table.add(changeDate, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getPlaceInGroupForm() throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		ChildCareApplication application = getBusiness().getApplication(_applicationID);
		boolean hasBankId = false;

		hasBankId = new NBSLoginBusinessBean().hasBankLogin(application.getOwner());
	
		
		if (hasBankId){
			table.add(getSmallText(localize("child_care.child_care_time", "Time")+":"), 1, row);
			table.add(getSmallText("" + application.getCareTime()), 1, row++);
			table.add(new HiddenInput(PARAMETER_CHILDCARE_TIME, "" + application.getCareTime()));
			
		} else {
			TextInput textInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_CHILDCARE_TIME));
			textInput.setLength(2);
			textInput.setAsNotEmpty(localize("child_care.child_care_time_required","You must fill in the child care time."));
			textInput.setAsIntegers(localize("child_care.only_integers_allowed","Not a valid child care time."));

			table.add(getSmallHeader(localize("child_care.enter_child_care_time", "Enter child care time:")), 1, row++);
			table.add(getSmallText(localize("child_care.child_care_time", "Time")+":"), 1, row);
			table.add(textInput, 1, row++);
		}

		Table dropdownTable = new Table(2, 3);
		int dropRow = 1;
		DropdownMenu groups = getGroups(-1, -1);
		groups.addMenuElementFirst("-1","");
		groups.setAsNotEmpty(localize("child_care.must_select_a_group","You must select a group.  If one does not exist, you will have to create one first."), "-1");
		
		dropdownTable.add(getSmallText(localize("child_care.group", "Group")+":"), 1, dropRow);
		dropdownTable.add(groups, 2, dropRow++);

		DropdownMenu schoolTypes = getSchoolTypes(-1, -1);
		schoolTypes.addMenuElementFirst("-1","");
		schoolTypes.setAsNotEmpty(localize("child_care.must_select_a_type","You must select a type."), "-1");
		
		dropdownTable.add(getSmallText(localize("child_care.schooltype", "Type")+":"), 1, dropRow);
		dropdownTable.add(schoolTypes, 2, dropRow++);
		
		DropdownMenu employmentTypes = getEmploymentTypes(PARAMETER_EMPLOYMENT_TYPE, -1);
		employmentTypes.setAsNotEmpty(localize("child_care.must_select_employment_type","You must select employment type."), "-1");
		
		dropdownTable.add(getSmallText(localize("child_care.employment_type", "Employment type")+":"), 1, dropRow);
		dropdownTable.add(employmentTypes, 2, dropRow);
		
		table.add(dropdownTable, 1, row++);

		SubmitButton placeInGroup = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.place_in_group", "Place in group"), PARAMETER_ACTION, String.valueOf(ACTION_PLACE_IN_GROUP)));
		form.setToDisableOnSubmit(placeInGroup, true);
		table.add(placeInGroup, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getAlterCareTimeForm(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		ChildCareApplication application = getBusiness().getApplication(_applicationID);
		ChildCareContract archive = getBusiness().getContractFile(application.getContractFileId());

		TextInput textInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_CHILDCARE_TIME));
		textInput.setLength(2);
		textInput.setAsNotEmpty(localize("child_care.child_care_time_required","You must fill in the child care time."));
		textInput.setAsIntegers(localize("child_care.only_integers_allowed","Not a valid child care time."));

		table.add(getSmallHeader(localize("child_care.enter_child_care_time", "Enter child care time:")), 1, row++);
		table.add(getSmallText(localize("child_care.child_care_time", "Time")+":"), 1, row);
		table.add(textInput, 1, row++);
		
		DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_CHANGE_DATE));
		
		if (restrictDates) {
			IWTimestamp stamp = new IWTimestamp();
			if (archive != null) {
				IWTimestamp validFrom = new IWTimestamp(archive.getValidFromDate());
				validFrom.addDays(1);
				dateInput.setDate(validFrom.getDate());
				if (validFrom.isEarlierThan(stamp))
				{
					if(onlyAllowFutureCareDate){
						dateInput.setEarliestPossibleDate(stamp.getDate(), localize("child_care.not_a_valid_date", "You can not choose a date back in time."));
					}
				}
				else{
					dateInput.setEarliestPossibleDate(validFrom.getDate(), localize("child_care.contract_dates_overlap", "You can not choose a date which overlaps another contract."));
				}
				if (archive.getTerminatedDate() != null) {
					IWTimestamp terminated = new IWTimestamp(archive.getTerminatedDate());
					dateInput.setLatestPossibleDate(terminated.getDate(), localize("child_care.contract_date_expired", "You can not choose a date after the contract has been terminated."));
				}
			}
			else {
				dateInput.setDate(new IWTimestamp().getDate());
				dateInput.setEarliestPossibleDate(stamp.getDate(), localize("child_care.not_a_valid_date", "You can not choose a date back in time."));
			}
		}
		else {
			if (archive.getTerminatedDate() != null) {
				IWTimestamp terminated = new IWTimestamp(archive.getTerminatedDate());
				dateInput.setLatestPossibleDate(terminated.getDate(), localize("child_care.contract_date_expired", "You can not choose a date after the contract has been terminated."));
			}
		}
		dateInput.setAsNotEmpty(localize("child_care.must_select_date","You must select a date."));

		table.add(getSmallHeader(localize("child_care.new_date", "Select the new placement date")), 1, row++);
		table.add(dateInput, 1, row++);
		
		/* New requirements: Add Schooltype and Group dropdowns */
		
		// Schooltype change :
		DropdownMenu schoolTypes = new DropdownMenu(PARAMETER_SCHOOL_TYPES);
		Collection types = null;
		try {
			types = application.getProvider().findRelatedSchoolTypes();
			Iterator iter = types .iterator();
			while (iter.hasNext()) {
				SchoolType element = (SchoolType) iter.next();
				schoolTypes.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolTypeName());
			}
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		}
		int presentSchoolTypeId = archive.getSchoolClassMember().getSchoolTypeId();
		if (presentSchoolTypeId != -1)
			schoolTypes.setSelectedElement(presentSchoolTypeId);
		
		schoolTypes = (DropdownMenu) getStyledInterface(schoolTypes);	
		
		table.add(getSmallText(localize("child_care.school_type", "School type")),1,row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(schoolTypes, 1, row++);
		
		// Group change, (school class)
		
		SchoolClassDropdownDouble schoolClassess = new SchoolClassDropdownDouble(PARAMETER_SCHOOL_TYPES,PARAMETER_SCHOOL_CLASS);
		schoolClassess = (SchoolClassDropdownDouble) getStyledInterface(schoolClassess);	
		//int classID = archive.getSchoolClassMember().getSchoolClassId();
		
		if (getChildcareID() != -1) {
			
			if (!types.isEmpty()) {
				SchoolCommuneBusiness sb = (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc,SchoolCommuneBusiness.class);
				Map typeGroupMap = sb.getSchoolTypeClassMap(types,application.getProviderId() , getSession().getSeasonID(), false);
				if (typeGroupMap != null) {
					Iterator iter = typeGroupMap.keySet().iterator();
					while (iter.hasNext()) {
						SchoolYear year = (SchoolYear) iter.next();
						schoolClassess.addMenuElement(year.getPrimaryKey().toString(), year.getSchoolYearName(), (Map) typeGroupMap.get(year));
					}
				}
			}
		}
		
		table.add(getSmallText(localize("child_care.school_class", "School class")),1,row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(schoolClassess, 1, row++);
		

		DropdownMenu employmentTypes = getEmploymentTypes(PARAMETER_EMPLOYMENT_TYPE, -1);
		employmentTypes.setAsNotEmpty(localize("child_care.must_select_employment_type","You must select employment type."), "-1");
		
		table.add(getSmallText(localize("child_care.employment_type", "Employment type")+":"), 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(employmentTypes, 1, row++);

		SubmitButton placeInGroup = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.alter_care_time", "Alter care time"), PARAMETER_ACTION, String.valueOf(ACTION_ALTER_CARE_TIME)));
		form.setToDisableOnSubmit(placeInGroup, true);
		table.add(placeInGroup, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getCancelContractForm(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		ChildCareApplication application = getBusiness().getApplicationForChildAndProvider(_userID, getSession().getChildCareID());
		boolean canCancel = getBusiness().canCancelContract(((Integer)application.getPrimaryKey()).intValue());
		
		if (canCancel) {
			RadioButton parentalLeave = this.getRadioButton(PARAMETER_CANCEL_REASON, String.valueOf(true));
			parentalLeave.keepStatusOnAction(true);
			RadioButton other = getRadioButton(PARAMETER_CANCEL_REASON, String.valueOf(false));
			other.keepStatusOnAction(true);
			if (!iwc.isParameterSet(PARAMETER_CANCEL_REASON))
				other.setSelected(true);
			
			table.add(getSmallHeader(localize("child_care.enter_cancel_information", "Enter cancel information:")), 1, row++);
			table.add(parentalLeave, 1, row);
			table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.cancel_parental_leave", "Cancel because of parental leave")), 1, row);
			table.add(new Break(), 1, row);
			table.add(other, 1, row);
			table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.cancel_other", "Other reason")), 1, row++);
			
			TextArea textArea = (TextArea) getStyledInterface(new TextArea(PARAMETER_CANCEL_MESSAGE));
			textArea.setWidth(Table.HUNDRED_PERCENT);
			textArea.setRows(4);
			textArea.setAsNotEmpty(localize("child_care.offer_message_required","You must fill in the message."));
			textArea.keepStatusOnAction(true);
	
			table.add(getSmallHeader(localize("child_care.cancel_reason_message", "Reason for cancel")+":"), 1, row++);
			table.add(textArea, 1, row++);
	
			IWTimestamp stampNow = new IWTimestamp();
			stampNow.addDays(1);
			IWTimestamp stamp = new IWTimestamp();
			stamp.addMonths(2);
			DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_CANCEL_DATE));
			dateInput.setDate(stamp.getDate());
			if (restrictDates)
				dateInput.setEarliestPossibleDate(stampNow.getDate(), localize("child_care.not_a_valid_date", "You can not choose a date back in time."));
			dateInput.setAsNotEmpty(localize("child_care.must_select_date","You must select a date."));
			dateInput.keepStatusOnAction(true);
	
			table.add(getSmallHeader(localize("child_care.cancel_date", "Cancel date")+":"), 1, row++);
			table.add(dateInput, 1, row++);
	
			SubmitButton placeInGroup = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.cancel_contract", "Cancel contract"), PARAMETER_ACTION, String.valueOf(ACTION_CANCEL_CONTRACT)));
			form.setToDisableOnSubmit(placeInGroup, true);
			table.add(placeInGroup, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
		}
		else {
			table.add(getSmallErrorText(localize("child_care.must_remove_future_contracts", "Future contracts must be removed before cancel is possible.")), 1, row++);
		}

		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getCreateGroupForm() throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;

		TextInput textInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_GROUP_NAME));
		textInput.setLength(24);
		textInput.setAsNotEmpty(localize("child_care.group_name_required","You must fill in a name for the group."));
		
		SchoolType currentType = null;
		if (getSession().getGroupID() != -1) {
			SchoolClass group = getBusiness().getSchoolBusiness().findSchoolClass(new Integer(getSession().getGroupID()));
			currentType = group.getSchoolType();
			if (group.getSchoolClassName() != null)
				textInput.setContent(group.getSchoolClassName());
		}
		
		table.add(getSmallHeader(localize("child_care.enter_group_name", "Enter group name:")), 1, row++);
		table.add(getSmallText(localize("child_care.group_name", "Name")), 1, row);
		table.add(textInput, 1, row++);

		//school types
	
		School school = getSession().getProvider();
		Collection availableTypes = new ArrayList();
		try{
			availableTypes = school.getSchoolTypes();			
		}catch(IDORelationshipException ex){
			ex.printStackTrace();	
		}
		
		DropdownMenu types = getDropdownMenuLocalized(PARAMETER_SCHOOL_TYPES, availableTypes, "getLocalizationKey", "");
		if (currentType != null){
			types.setSelectedElement(localize(currentType.getLocalizationKey(), ""));
		}
		table.add(getSmallHeader(localize("child_care.choose_school_type", "Choose school type:")), 1, row++);
		table.add(getSmallText(localize("child_care.school_type", "School type")), 1, row);
		table.add(types, 1, row++);

		String localized = "";
		if (getSession().getGroupID() != -1)
			localized = localize("child_care.change_group", "Change group");
		else
			localized = localize("child_care.create_group", "Create group");

		SubmitButton createGroup = (SubmitButton) getStyledInterface(new SubmitButton(localized, PARAMETER_ACTION, String.valueOf(ACTION_CREATE_GROUP)));
		form.setToDisableOnSubmit(createGroup, true);
		table.add(createGroup, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getMoveGroupForm(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;

		int oldGroup = Integer.parseInt(iwc.getParameter(PARAMETER_OLD_GROUP));
		DropdownMenu groups = getGroups(-1, oldGroup);
		groups.addMenuElementFirst("-1","");
		groups.setAsNotEmpty(localize("child_care.must_select_a_group","You must select a group.  If one does not exist, you will have to create one first."), "-1");
		
		table.add(getSmallHeader(localize("child_care.select_group", "Select group to move child to")), 1, row++);
		table.add(getSmallText(localize("child_care.group", "Group")+":"), 1, row);
		table.add(groups, 1, row++);

		SubmitButton placeInGroup = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.move_group", "Move to group"), PARAMETER_ACTION, String.valueOf(ACTION_MOVE_TO_GROUP)));
		form.setToDisableOnSubmit(placeInGroup, true);
		table.add(placeInGroup, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getUpdatePrognosisForm() throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		ChildCarePrognosis prognosis = getBusiness().getPrognosis(getSession().getChildCareID());

		table.mergeCells(1, row, 4, row);
		table.add(getSmallHeader(localize("child_care.prognosis_information","Enter the prognosis information for your childcare.")), 1, row++);
		
		table.mergeCells(1, row, 2, row);
		table.add(getSmallHeader(localize("child_care.enter_prognosis", "Enter prognosis:")), 1, row++);

		TextInput threeMonths = (TextInput) getStyledInterface(new TextInput(PARAMETER_THREE_MONTHS_PROGNOSIS));
		threeMonths.setLength(3);
		threeMonths.setAsNotEmpty(localize("child_care.three_months_prognosis_required","You must fill in the three months prognosis."));
		threeMonths.setAsIntegers(localize("child_care.only_integers_allowed","Not a valid prognosis."));
		if (prognosis != null)
			threeMonths.setContent(String.valueOf(prognosis.getThreeMonthsPrognosis()));

		TextInput threeMonthsPriority = (TextInput) getStyledInterface(new TextInput(PARAMETER_THREE_MONTHS_PRIORITY));
		threeMonthsPriority.setLength(3);
		threeMonthsPriority.setAsNotEmpty(localize("child_care.three_months_priority_required","You must fill in the three months priority."));
		threeMonthsPriority.setAsIntegers(localize("child_care.only_integers_allowed","Not a valid prognosis."));
		if (prognosis != null && prognosis.getThreeMonthsPriority() != -1)
			threeMonthsPriority.setContent(String.valueOf(prognosis.getThreeMonthsPriority()));

		table.add(getSmallText(localize("child_care.three_months_prognosis", "Three months prognosis")+":"), 1, row);
		table.add(threeMonths, 2, row);
		table.add(getSmallText(localize("child_care.thereof_priority", "there of priority")+":"), 3, row);
		table.add(threeMonthsPriority, 4, row++);
		
		TextInput oneYear = (TextInput) getStyledInterface(new TextInput(PARAMETER_ONE_YEAR_PROGNOSIS));
		oneYear.setLength(3);
		oneYear.setAsNotEmpty(localize("child_care.one_year_prognosis_required","You must fill in the one year prognosis."));
		oneYear.setAsIntegers(localize("child_care.only_integers_allowed","Not a valid prognosis."));
		if (prognosis != null)
			oneYear.setContent(String.valueOf(prognosis.getOneYearPrognosis()));

		TextInput oneYearPriority = (TextInput) getStyledInterface(new TextInput(PARAMETER_ONE_YEAR_PRIORITY));
		oneYearPriority.setLength(3);
		oneYearPriority.setAsNotEmpty(localize("child_care.one_year_priority_required","You must fill in the one year priority."));
		oneYearPriority.setAsIntegers(localize("child_care.only_integers_allowed","Not a valid prognosis."));
		if (prognosis != null && prognosis.getOneYearPriority() != -1)
		oneYearPriority.setContent(String.valueOf(prognosis.getOneYearPriority()));

		table.add(getSmallText(localize("child_care.one_year_prognosis", "Twelve months prognosis")+":"), 1, row);
		table.add(oneYear, 2, row);
		table.add(getSmallText(localize("child_care.thereof_priority", "there of priority")+":"), 3, row);
		table.add(oneYearPriority, 4, row++);
		
		
		////////////////// added provider capacity 040402 Malin
		table.mergeCells(1, row, 4, row);
		table.add(getSmallHeader(localize("child_care.capacity_information","Enter the provider capacity.")), 1, row++);
		
		table.mergeCells(2, row, 4, row);
		TextInput providerCapacity = (TextInput) getStyledInterface(new TextInput(PARAMETER_PROVIDER_CAPACITY));
		providerCapacity.setLength(3);
		providerCapacity.setAsNotEmpty(localize("child_care.capacity_required","You must fill in the provider capacity."));
		providerCapacity.setAsIntegers(localize("child_care.capacity_only_integers_allowed","Not a valid number."));
		if (prognosis != null && prognosis.getProviderCapacity() != -1)
			providerCapacity.setContent(String.valueOf(prognosis.getProviderCapacity()));

		table.add(getSmallText(localize("child_care.provider_capacity", "Provider capacity")+":"), 1, row);
		table.add(providerCapacity, 2, row);
		table.add("", 3, row++);
		
			
		SubmitButton updatePrognosis = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.set_prognosis", "Set prognosis"), PARAMETER_ACTION, String.valueOf(ACTION_UPDATE_PROGNOSIS)));
		form.setToDisableOnSubmit(updatePrognosis, true);
		table.add(updatePrognosis, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.mergeCells(1, row, 2, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}
	
	/**
	 * Shows the providerqueue without personal information. Used by citizen.
	 * @param iwc
	 * @return
	 * @throws Exception
	 */
	private Table getProviderQueueForm(IWContext iwc) throws RemoteException {
		
		String providerId = iwc.getParameter(CCConstants.PROVIDER_ID);
		String appId = iwc.getParameter(CCConstants.APPID);		
		School school = getBusiness().getSchoolBusiness().getSchool(providerId);
		
		
		ChildCarePrognosis prognosis = getBusiness().getPrognosis(Integer.parseInt(providerId));
						
		String prognosisText = prognosis == null ? localize("ccpqw_no_prognosis", "No prognosis available") :
			localize("ccpqw_three_months", "Three months:") +" " + prognosis.getThreeMonthsPrognosis()+ "  " +
			localize("ccpqw_one_year", "One year:") + " " + prognosis.getOneYearPrognosis() + "  " +
			localize("ccpqw_updated_date", "Updated date:") + " " + prognosis.getUpdatedDate();	
		
		Table appTbl = new Table();
		
//		add(new Text("ProviderId: " + providerId));
		if (providerId != null){
			Collection applications = getBusiness().getOpenAndGrantedApplicationsByProvider(new Integer(providerId).intValue());
			
			Iterator i = applications.iterator();
			
			appTbl.add(getSmallHeader(localize("ccpqw_order", "Queue number")), 1, 1);
			appTbl.add(getSmallHeader(localize("ccpqw_queue_date", "Queue date")), 2, 1);
			appTbl.add(getSmallHeader(localize("ccpqw_from_date", "Placement date")), 3, 1);
			appTbl.setRowColor(1, getHeaderColor());			
	
			int row = 2;
			
			while(i.hasNext()){
				ChildCareApplication app = (ChildCareApplication) i.next();
				
				Text queueOrder = getSmallText("" + getBusiness().getNumberInQueue(app)),
					queueDate = getSmallText(app.getQueueDate().toString()),
					fromDate = getSmallText(app.getFromDate().toString());
//					currentAppId = style.getSmallText(""+app.getNodeID());   //debug only
				
				appTbl.add(queueOrder, 1, row);
				appTbl.add(queueDate, 2, row);
				appTbl.add(fromDate, 3, row);
//				appTbl.add(currentAppId, 4, row);  //debug only
			
				if (app.getNodeID() == new Integer(appId).intValue()){
					makeBlueAndBold(queueOrder);
					makeBlueAndBold(queueDate);
					makeBlueAndBold(fromDate);
				}
				
				if (row % 2 == 0) {
					appTbl.setRowColor(row, getZebraColor1());
				} else {
					appTbl.setRowColor(row, getZebraColor2());
				}				
				row++;
			}
		}
				
		Table layoutTbl = new Table();	
		layoutTbl.setCellpadding(5);
		layoutTbl.setWidth(Table.HUNDRED_PERCENT);
		layoutTbl.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
					
		layoutTbl.add(getSmallHeader(localize("ccpqw_provider", "Provider") + ":"), 1, row);
		layoutTbl.add(getSmallText(school.getName()), 2, row++);	

		layoutTbl.setRowHeight(2, "20px");	
		
		layoutTbl.add(getSmallHeader(localize("ccpqw_prognosisr", "Prognosis") + ":"), 1, row);
		layoutTbl.add(getSmallText(prognosisText), 2, row++);		
			
		layoutTbl.setRowHeight(row++, "20px");
			
		layoutTbl.add(appTbl, 1, row);
		layoutTbl.mergeCells(1, row, 2, row);
		row++;
	
		layoutTbl.add(close, 1, row);
		layoutTbl.setHeight(row, Table.HUNDRED_PERCENT);
		layoutTbl.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
				
//		CloseButton closeBtn = (CloseButton) getStyledInterface(new CloseButton(localize("ccpqw_close", "Close")));
//		layoutTbl.add(closeBtn, 1, 6);
//		layoutTbl.setAlignment(1, 6, "left");

		return layoutTbl;
	}	
		
	private Text makeBlueAndBold(Text t){
		t.setBold(true);
		t.setStyleAttribute("color:blue");	
		return t;
	}
	
	private Table getEndContractForm() {
		Table layoutTbl = new Table();
		layoutTbl.setCellpadding(5);
		layoutTbl.setWidth(Table.HUNDRED_PERCENT);
		layoutTbl.setHeight(Table.HUNDRED_PERCENT);		

		int row = 1;
		
		
		layoutTbl.add(getSmallHeader(localize("ccnctw_from_date", "From date") + ":"), 1, row);	
		DateInput fromDate = (DateInput) getStyledInterface(new DateInput(PARAMETER_CHANGE_DATE));
		fromDate.setAsNotEmpty(localize("ccecw_date_format_alert", "Please choose a valid from date."));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 2);
		fromDate.setEarliestPossibleDate(cal.getTime(), localize("ccecw_date_alert", "Date must be not earlier than two months from today."));
		layoutTbl.add(fromDate, 2, row++);
		
		SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton(localize("cc_ok", "Submit"), PARAMETER_ACTION, String.valueOf(ACTION_END_CONTRACT)));
		form.setToDisableOnSubmit(submit, true);
		layoutTbl.add(submit, 1, row);
		layoutTbl.add(Text.getNonBrakingSpace(), 1, row);
		layoutTbl.add(close, 1, row);
		layoutTbl.setHeight(row, Table.HUNDRED_PERCENT);
		layoutTbl.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
		
		return layoutTbl;
	}
	
	private Table getNewCareTimeForm() throws RemoteException {
		Table layoutTbl = new Table();
		layoutTbl.setCellpadding(5);
		layoutTbl.setWidth(Table.HUNDRED_PERCENT);
		layoutTbl.setHeight(Table.HUNDRED_PERCENT);			

		ChildCareApplication application = getBusiness().getApplication(_applicationID);	

		int row = 1;
		layoutTbl.add(getSmallHeader(localize("ccnctw_info", "Info about care time.")), 1, row++);
				
		layoutTbl.add(
			getSmallHeader(localize("ccnctw_care_time", "Care time") + ":"),
			1,
			row);
		TextInput careTime = (TextInput) getStyledInterface(new TextInput(PARAMETER_CHILDCARE_TIME));
		careTime.setValue(application.getCareTime());
		careTime.setAsIntegers(localize("ccnctw_alert_care_time_format", "Care time must be an integer"));
		careTime.setLength(4);
		layoutTbl.add(careTime, 2, row++);

		layoutTbl.add(
		getSmallHeader(localize("ccnctw_from_date", "From date") + ":"),
			1,
			row);
			
		DateInput fromDate = (DateInput) getStyledInterface(new DateInput(PARAMETER_CHANGE_DATE));
		fromDate.setAsNotEmpty(localize("ccnctw_unvalid_date_format_alert", "Please choose a valid from date."));
		if(onlyAllowFutureCareDate){
			fromDate.setEarliestPossibleDate(
				new Date(),
				localize("ccnctw_unvalid_date_alert", "The date most be in the future."));			
		}
		layoutTbl.add(fromDate, 2, row++);

		row++;

		SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton(localize("cc_ok", "Submit"), PARAMETER_ACTION, String.valueOf(ACTION_NEW_CARE_TIME)));
		form.setToDisableOnSubmit(submit, true);
		layoutTbl.add(submit, 1, row);
		layoutTbl.add(Text.getNonBrakingSpace(), 1, row);
		layoutTbl.add(close, 1, row);
		layoutTbl.setHeight(row, Table.HUNDRED_PERCENT);
		layoutTbl.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return layoutTbl;
	}	
	
	/**
	 * 
	 * @param iwc
	 * @return Object array of size 2, first (Boolean) is true iff signwindow is shown, second is the Table containing the form
	 * @throws Exception
	 */
	private Object[] getContractSignerForm(IWContext iwc) throws Exception {
		Table table = null;		
						
		Contract contract = getContract(iwc);
		if (contract.isSigned()) {
			table = new Table();
			table.setCellpadding(5);
			table.setWidth(Table.HUNDRED_PERCENT);
			table.setHeight(Table.HUNDRED_PERCENT);					
			table.add(getHeader(localize("ccconsign_issigned", "The contract is signed.")), 1, 1);
			table.add(close, 1, 2);
			table.setHeight(2, Table.HUNDRED_PERCENT);
			table.setRowVerticalAlignment(2, Table.VERTICAL_ALIGN_BOTTOM);
			
			((Window) getParentObject()).setParentToReload();
			
			return new Object[]{new Boolean(false), table};
			
		} else {
		
			//Storing all fields set in this request
			Map fieldValues = new HashMap();
			Enumeration parameters = iwc.getRequest().getParameterNames();
			while(parameters.hasMoreElements()){
				String name = (String) parameters.nextElement();
				if (name.startsWith(PARAMETER_TEXT_FIELD)){
					String value = iwc.getParameter(name);
					if (value != null && value.length() != 0) {
						fieldValues.put(name.substring(PARAMETER_TEXT_FIELD.length()), iwc.getParameter(name));
						ChildCareApplication application = getBusiness().getChildCareContractArchiveHome().findApplicationByContract(((Integer)contract.getPrimaryKey()).intValue()).getApplication();
						if (name.equals(PARAMETER_TEXT_FIELD + "care-time")){
							application.setCareTime(Integer.parseInt(value));
							application.store();
						}
					}
				}
			}

			contract.setUnsetFields(fieldValues);
			ContractTagHome contractHome = (ContractTagHome) IDOLookup.getHome(ContractTag.class);
			Collection tags = contractHome.findAllByCategory(contract.getCategoryId().intValue());
			
			
		//create form for still unset fields
			table = getContractFieldsForm(contract.getUnsetFields(), tags);
			if (table != null && table.getRows() > 1){
				return new Object[]{new Boolean(false), table};			
			} else {
				//TODO: (Roar) Not working...
				((Window) getParentObject()).setWidth(700);
				((Window) getParentObject()).setHeight(400);
				return new Object[]{new Boolean(true), initSignContract(iwc)};
			}
		}
	} 	
	
	
	private Table getContractFieldsForm(Set fields, Collection tags) {
		Table table = null;			
		if (fields.size() != 0){
			int row = 1;
			Iterator i = fields.iterator();

			//loops through contract fields and add them to the form
			while (i.hasNext()) {
				String field = (String) i.next();
				if (! field.equalsIgnoreCase(FIELD_CURRENT_DATE)) { ///the currentdate field is given value when signing
					if (table == null){
						table = new Table();
						table.setCellpadding(5);
						table.setWidth(Table.HUNDRED_PERCENT);
						table.setHeight(Table.HUNDRED_PERCENT);							
						table.add(getHeader(localize("ccconsign_formHeading", "Please, fill out the contract fields")), 1, row++);
						table.mergeCells(1, 1, 2, 1);						
					}
				
					Iterator itags = tags.iterator();
					TextInput input = new TextInput(PARAMETER_TEXT_FIELD + field);
				
  					//search for tag with same name and look up type information
					while (itags.hasNext()) {
						ContractTag tag = (ContractTag) itags.next();
						if (tag.getName().equals(field)) {
							if (tag.getType() != null && tag.getType().equals(java.lang.Integer.class)){
								input.setAsIntegers(localize("ccconsign_integer", "Use numbers only for " + field + "."));
							}
						}
					}
					String fieldPrompt = field.substring(0, 1).toUpperCase() + field.substring(1).toLowerCase();
					table.add(getSmallHeader(localize("ccconsign_ " + fieldPrompt, fieldPrompt) + ":"), 1, row);
					table.add(getStyledInterface(input), 2, row);
					row ++;
				}
			}
		
			if (table != null){
				HiddenInput action = new HiddenInput(PARAMETER_METHOD, String.valueOf(METHOD_SIGN_CONTRACT));
				table.add(action);
				SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton(localize("cc_ok", "Submit")));
				table.add(submit, 1, row);
				table.add(Text.getNonBrakingSpace(), 1, row);
				table.add(close, 1, row);
				table.setHeight(row, Table.HUNDRED_PERCENT);
				table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
			}
		}

		return table;
	}
	
	private Table initSignContract(IWContext iwc){
		Contract contract = getContract(iwc);	
		
		//Setting current date
		Map fields = new HashMap();
		final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
		fields.put(FIELD_CURRENT_DATE, dateFormat.format(new Date()));
		contract.setUnsetFields(fields);
		contract.store();
			
		iwc.setSessionAttribute(NBSSigningBlock.NBS_SIGNED_ENTITY, 
			new NBSSignedEntity() {
				private Contract _contract = null;
					
				public Object init(Contract contract){
					_contract = contract;
					return this;
				}
					
				public void setXmlSignedData(String data) {
					_contract.setXmlSignedData(data);
				}
	
				public void setSignedBy(int userId) {
					_contract.setSignedBy(new Integer(userId));
				}
	
				public void setSignedDate(java.sql.Date time) {
					_contract.setSignedDate(time);
				}
	
				public void setSignedFlag(boolean flag) {
					_contract.setSignedFlag(new Boolean(flag));												
				}
				
				public void setText(String text) {
					_contract.setText(text);											
				}				
	
				public void store() {
					_contract.store();
				}
	
				public String getText() {
					return _contract.getText();
				}
			}
			.init(contract)
		);
		
		Table table = new Table();
		
		Text heading = getLocalizedHeader("ccconsign_read_before_sign", "Read through the contrat and sign by using your BankId password. Then click OK.");
		table.add(heading, 1, 1);
		//table.setHeight(1, 1, Table.HUNDRED_PERCENT);
		table.setVerticalAlignment(1, 1, "MIDDLE");
		NBSSigningBlock nbsSigningBlock = new NBSSigningBlock();
		nbsSigningBlock.setParameter(PARAMETER_ACTION, ""+ACTION_SIGN_CONTRACT);
		nbsSigningBlock.setParameter(PARAMETER_METHOD, ""+METHOD_SIGN_CONTRACT);		
		nbsSigningBlock.setParameter(PARAMETER_CONTRACT_ID, contract.getPrimaryKey().toString());
		table.add(nbsSigningBlock, 1, 2);
		CloseButton closeBtn = new CloseButton(localize("ccconsign_CANCEL", "avbryt"));
		closeBtn.setAsImageButton(true);
		table.add(closeBtn, 1, 3);
		table.setHeight(3, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(3, Table.VERTICAL_ALIGN_BOTTOM);		
		return table; 
	}
		
	
	private void processSignContract(IWContext iwc) throws Exception{
			NBSSigningBlock nbsSigningBlock = new NBSSigningBlock();
			nbsSigningBlock.processSignContract(iwc);	

			ChildCareApplication application = getBusiness().getChildCareContractArchiveHome().findApplicationByContract(((Integer)getContract(iwc).getPrimaryKey()).intValue()).getApplication();
			
			User owner = application.getOwner();
			com.idega.core.user.data.User child = UserBusiness.getUser(application.getChildId());
						
			getBusiness().sendMessageToProvider(
				application,
				localize("ccecw_signcon_subject", "Contract signed"),
				owner.getName() + " " + localize("ccecw_signcon_body", " has signed the contract for ") + " " +
				child.getName() + " " +  child.getPersonalID() + ".");	
			
	}
			
	/**
	 * 
	 * @param iwc
	 * @return the contract specified by the ChildCareContractSigner_CONTRACT_ID parameter, null if errors or no contract
	 */
	private Contract getContract(IWContext iwc) {
		int contractId;
		Contract contract = null;		
		try {
			contractId = Integer.parseInt(iwc.getParameter(PARAMETER_CONTRACT_ID));
			contract = ((ContractHome) IDOLookup.getHome(Contract.class))
					.findByPrimaryKey(new Integer(contractId));	
		}catch(NumberFormatException ex){
			ex.printStackTrace();			
		} catch(FinderException ex){
			ex.printStackTrace();
		} catch(IDOLookupException ex){
			ex.printStackTrace();
		}
		return contract;		
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

		if (iwc.isParameterSet(PARAMETER_PLACEMENT_ID))
			_placementID = Integer.parseInt(iwc.getParameter(PARAMETER_PLACEMENT_ID));

		if (iwc.isParameterSet(PARAMETER_PAGE_ID))
			_pageID = Integer.parseInt(iwc.getParameter(PARAMETER_PAGE_ID));
			
		if (iwc.isParameterSet(PARAMETER_EARLIEST_DATE))
			earliestDate = new IWTimestamp(iwc.getParameter(PARAMETER_EARLIEST_DATE));

		String restrict = getBundle().getProperty(PROPERTY_RESTRICT_DATES, "false");
		restrictDates = false;
		if (restrict != null) {
			restrictDates = Boolean.valueOf(restrict).booleanValue();
		}
	}
	
	private void alterCareTime(IWContext iwc) throws RemoteException {
		IWTimestamp validFrom = new IWTimestamp(iwc.getParameter(PARAMETER_CHANGE_DATE));
		int childCareTime = Integer.parseInt(iwc.getParameter(PARAMETER_CHILDCARE_TIME));
		int employmentType = Integer.parseInt(iwc.getParameter(PARAMETER_EMPLOYMENT_TYPE));
		//int schoolTypeId = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_TYPES));
		//int schoolClassId = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_CLASS));
		getBusiness().assignContractToApplication(_applicationID, childCareTime, validFrom, employmentType, iwc.getCurrentUser(), iwc.getCurrentLocale(), false);

		close();
	}

	private void alterValidFromDate(IWContext iwc) throws RemoteException , NoPlacementFoundException{
		IWTimestamp validFrom = new IWTimestamp(iwc.getParameter(PARAMETER_CHANGE_DATE));
		int careTime = Integer.parseInt(iwc.getParameter(PARAMETER_CHILDCARE_TIME));
		getBusiness().alterValidFromDate(_applicationID, validFrom.getDate(), -1, iwc.getCurrentLocale(), iwc.getCurrentUser());
		getBusiness().placeApplication(_applicationID, null, null, careTime, -1, -1, -1, iwc.getCurrentUser(), iwc.getCurrentLocale());
		
		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}

	private void makeOffer(IWContext iwc) throws RemoteException {
		String messageHeader = localize("child_care.application_accepted_subject", "Child care application accepted.");
		String messageBody = iwc.getParameter(PARAMETER_OFFER_MESSAGE);
		if (messageBody.indexOf("$datum$") != -1) {
			messageBody = TextSoap.findAndReplace(messageBody, "$datum$", "{4}");
		}
		IWTimestamp validUntil = new IWTimestamp(iwc.getParameter(PARAMETER_OFFER_VALID_UNTIL));
		getBusiness().acceptApplication(_applicationID, validUntil, messageHeader, messageBody, iwc.getCurrentUser());

		close();
	}
	
	private void retractOffer(IWContext iwc) throws RemoteException {
		String messageHeader = localize("child_care.offer_retracted_subject", "Offer for child care retracted.");
		String messageBody = iwc.getParameter(PARAMETER_OFFER_MESSAGE);
		getBusiness().retractOffer(_applicationID, messageHeader, messageBody, iwc.getCurrentUser());

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}
	
	private void rejectApplication(IWContext iwc) throws RemoteException {
		String messageHeader = localize("child_care.application_rejected_subject", "Application for after school placing rejected.");
		String messageBody = iwc.getParameter(PARAMETER_REJECT_MESSAGE);
		getBusiness().rejectApplication(_applicationID, messageHeader, messageBody, iwc.getCurrentUser());

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}
	
	private void changeDate(IWContext iwc) throws RemoteException {
		String placingDate = iwc.getParameter(PARAMETER_CHANGE_DATE);
		String preSchool = iwc.getParameter(PARAMETER_PRE_SCHOOL);		
		IWTimestamp stamp = new IWTimestamp(placingDate);
		getBusiness().changePlacingDate(_applicationID, stamp.getDate(), preSchool);

		close();
	}
	
	private void grantPriority(IWContext iwc) throws RemoteException {
		String messageHeader = localize("child_care.priority_subject", "Child care application priority.");
		String messageBody = iwc.getParameter(PARAMETER_PRIORITY_MESSAGE);
		getBusiness().setAsPriorityApplication(_applicationID, messageHeader, messageBody);

		close();
	}
	
	private void parentsAgree(IWContext iwc) throws RemoteException {
		String subject = localize("child_care.parents_agree_subject", "Parents accept placing offer.");
		String message = localize("child_care.parents_agree_body", "The parents of {0} accept your offer for a placing in {1} from {2}.");
		
		getBusiness().parentsAgree(_applicationID, iwc.getCurrentUser(), subject, message);

		close();
	}
	
	private void createContract(IWContext iwc) throws RemoteException {
		getBusiness().assignContractToApplication(_applicationID, -1, null, -1, iwc.getCurrentUser(), iwc.getCurrentLocale(), true);

		close();
	}
	
	private void createContractForBankID(IWContext iwc) throws RemoteException {
		getBusiness().assignContractToApplication(_applicationID, -1, null, -1, iwc.getCurrentUser(), iwc.getCurrentLocale(), true);

		close();
	}
	
	private void createGroup(IWContext iwc) throws RemoteException {
		String groupName = iwc.getParameter(PARAMETER_GROUP_NAME);
		int schoolTypeId = new Integer(iwc.getParameter(PARAMETER_SCHOOL_TYPES)).intValue();	
			
		getBusiness().getSchoolBusiness().storeSchoolClass(getSession().getGroupID(), groupName, getSession().getChildCareID(), schoolTypeId, -1, null, null);

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}
	
	private void deleteGroup(IWContext iwc) throws RemoteException {
		getBusiness().getSchoolBusiness().removeSchoolClass(getSession().getGroupID());
		getSession().setGroupID(-1);

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}
	
	private void placeInGroup(IWContext iwc) throws RemoteException {
		int childCareTime = Integer.parseInt(iwc.getParameter(PARAMETER_CHILDCARE_TIME));
		int groupID = Integer.parseInt(iwc.getParameter(getSession().getParameterGroupID()));
		int typeID = Integer.parseInt(iwc.getParameter(getSession().getParameterSchoolTypeID()));
		int employmentType = Integer.parseInt(iwc.getParameter(PARAMETER_EMPLOYMENT_TYPE));
		
		String subject = localize("child_care.placing_subject","Your child placed in child care.");
		String body = localize("child_care.placing_body","{0} has been placed in a group at {1}.");
		getBusiness().placeApplication(getSession().getApplicationID(), subject, body, childCareTime, groupID, typeID, employmentType, iwc.getCurrentUser(), iwc.getCurrentLocale());

		close();
	}
	
	private void cancelContract(IWContext iwc) throws RemoteException {
		ChildCareApplication application = getBusiness().getApplicationForChildAndProvider(_userID, getSession().getChildCareID());
		if (application != null) {
			boolean reason = Boolean.valueOf(iwc.getParameter(PARAMETER_CANCEL_REASON)).booleanValue();
			String message = iwc.getParameter(PARAMETER_CANCEL_MESSAGE);
			IWTimestamp date = new IWTimestamp(iwc.getParameter(PARAMETER_CANCEL_DATE));
			
			String reasonMessage = "";
			if (reason)
				reasonMessage = localize("child_care.parental_leave", "Parental leave");
			else
				reasonMessage = message;
			
			Object[] arguments = { "{0}", "{1}", reasonMessage, date.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT) };
			
			String subject = localize("child_care.cancel_contract_subject","Your child care contract has been terminated.");
			String body = localize("child_care.cancel_contract_body","Your contract for {0} at {1} has been terminated because of {2}. The termination will be active on {3}.");
			
			getBusiness().cancelContract(application, reason, date, message, subject, MessageFormat.format(body, arguments), iwc.getCurrentUser());
		}
		
		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}
	
	private void moveToGroup(IWContext iwc) throws RemoteException {
		int groupID = Integer.parseInt(iwc.getParameter(getSession().getParameterGroupID()));
		getBusiness().moveToGroup(_placementID, groupID);

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}
	
	private void removeFutureContracts(IWContext iwc) throws RemoteException {
		getBusiness().removeFutureContracts(_applicationID);

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}
	
	private void updatePrognosis(IWContext iwc) throws RemoteException {
		int threeMonths = Integer.parseInt(iwc.getParameter(PARAMETER_THREE_MONTHS_PROGNOSIS));
		int oneYear = Integer.parseInt(iwc.getParameter(PARAMETER_ONE_YEAR_PROGNOSIS));
		int threeMonthsPriority = Integer.parseInt(iwc.getParameter(PARAMETER_THREE_MONTHS_PRIORITY));
		int oneYearPriority = Integer.parseInt(iwc.getParameter(PARAMETER_ONE_YEAR_PRIORITY));
		int providerCapacity = Integer.parseInt(iwc.getParameter(PARAMETER_PROVIDER_CAPACITY));
		getBusiness().updatePrognosis(getSession().getChildCareID(), threeMonths, oneYear, threeMonthsPriority, oneYearPriority, providerCapacity);
		
		getSession().setHasPrognosis(true);
		getSession().setHasOutdatedPrognosis(false);

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}
	
	private void sendEndContractRequest(IWContext iwc) throws RemoteException{
		
		ChildCareApplication application = getBusiness().getApplication(_applicationID);
		User owner = application.getOwner();
		com.idega.core.user.data.User child = UserBusiness.getUser(application.getChildId());
		
		getBusiness().sendMessageToParents(
			application, 
			localize("ccecw_encon_par1", "Beg�ran om upps�gning av kontrakt gjord"), 
			localize("ccecw_encon_par2", "Du har skickat en beg�ran om upps�gning av kontrakt f�r") + " " +
			child.getName() + " " +  child.getPersonalID() + " " +
			localize("ccecw_encon_par3", "fr.o.m.")+ " " + iwc.getParameter(PARAMETER_CHANGE_DATE) + ".");
		
		
		getBusiness().sendMessageToProvider(
			application,
			localize("ccecw_encon_prov1", "Upps�gning av kontrakt"),
			owner.getName() + " " + localize("ccecw_encon_prov2", "har beg�rt upps�gning av kontrakt f�r") + " " +
			child.getName() + " " +  child.getPersonalID() + ". " + 
			localize("ccecw_encon_prov3", "Kontraktet ska upph�ra fr.o.m.") + " " + iwc.getParameter(PARAMETER_CHANGE_DATE) + ".",
			application.getOwner());	
			
		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();	
	}
		
	private void sendNewCareTimeRequest(IWContext iwc)	throws RemoteException {
		ChildCareApplication application = getBusiness().getApplication(_applicationID);
		User owner = application.getOwner();
		com.idega.core.user.data.User child = UserBusiness.getUser(application.getChildId());

		getBusiness().sendMessageToParents(
			application,
			localize(
				"ccnctw_new_caretime_msg_parents_subject",
				"Beg�ran om �ndrad omsorgstid gjord"),
			localize(
				"ccnctw_new_caretime_msg_parents_message",
				"Du har skickat en beg�ran om �ndrad omsorgstid f�r ")
				+ child.getName()
				+ " "
				+ child.getPersonalID());

		getBusiness().sendMessageToProvider(
			application,
			localize(
				"ccnctw_new_caretime_msg_provider_subject",
				"Beg�ran om �ndrad omsorgstid"),
			owner.getName()
				+ " "
				+ localize(
					"ccnctw_new_caretime_msg_provider_message1",
					"har beg�rt �ndrad omsorgstid till")
				+ " "
				+ iwc.getParameter(PARAMETER_CHILDCARE_TIME)
				+ " "
				+ localize(
					"ccnctw_new_caretime_msg_provider_message2",
					"tim/vecka f�r")
				+ " "
				+ child.getName()
				+ " "
				+ child.getPersonalID()
				+ ". "
				+ localize(
					"ccnctw_new_caretime_msg_provider_message3",
					"Den nya omsorgstiden skall g�lla fr.o.m.")
				+ " "
				+ iwc.getParameter(PARAMETER_CHANGE_DATE)
				+ ".",
			application.getOwner());
			
		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();				
	}
		
	private void close() {
		getParentPage().setParentToReload();
		getParentPage().close();
	}
	
	private Object[] getArguments(IWContext iwc) throws RemoteException {
		User user = iwc.getCurrentUser();
		User child = getBusiness().getUserBusiness().getUser(_userID);
		Email mail = getBusiness().getUserBusiness().getUserMail(user);
		ChildCareApplication application = getBusiness().getApplication(_applicationID);

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

		Object[] arguments = { child.getNameLastFirst(true), user.getName(), email, workphone, new IWTimestamp(application.getFromDate()).getLocaleDate(iwc.getCurrentLocale()), application.getProvider().getName() };
		return arguments;
	}
}
