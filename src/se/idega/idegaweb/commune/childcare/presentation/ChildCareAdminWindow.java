package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ejb.FinderException;

import se.idega.block.pki.data.NBSSignedEntity;
import se.idega.block.pki.presentation.NBSSigningBlock;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchive;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;

import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractHome;
import com.idega.block.contract.data.ContractTag;
import com.idega.block.contract.data.ContractTagHome;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.builder.business.BuilderLogic;
import com.idega.core.data.Email;
import com.idega.core.data.Phone;
import com.idega.core.user.business.UserBusiness;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

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
	public static final String PARAMETER_OFFER_VALID_UNTIL = "cc_offer_valid_until";
	public static final String PARAMETER_CANCEL_REASON = "cc_cancel_reason";
	public static final String PARAMETER_CANCEL_MESSAGE = "cc_cancel_message";
	public static final String PARAMETER_CANCEL_DATE = "cc_cancel_date";
	public static final String PARAMETER_EARLIEST_DATE = "cc_earliest_date";
	public static final String PARAMETER_CONTRACT_ID = "cc_contract_id";	
	public static final String PARAMETER_TEXT_FIELD = "cc_xml_signing_text_field";
	
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

	private int _method = -1;
	private int _action = -1;

	private int _userID = -1;
	private int _applicationID = -1;
	private int _pageID;
	
	private IWTimestamp earliestDate;

	private CloseButton close;
	private Form form;
	private boolean restrictDates;


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
				contentTable.add(getChangeDateForm(false));
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
				contentTable.add(getAlterCareTimeForm());
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
				contentTable.add(getChangeDateForm(true));
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
				headerTable.add(getHeader(localize("child_care.sign_contract", "Sign contract")));			
				contentTable.add(getContractSignerForm(iwc));	
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

		String message = MessageFormat.format(localize("child_care.offer_message", "We can offer {0} a placing in our childcare from {4}.\n\nRegards,\n{1}\n{2}\n{3}"), getArguments(iwc));
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

		SubmitButton offer = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.make_offer", "Make offer"), PARAMETER_ACTION, String.valueOf(ACTION_OFFER)));
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
		table.add(retract, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getChangeDateForm(boolean isAlteration) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		ChildCareApplication application = getBusiness().getApplication(_applicationID);

		DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_CHANGE_DATE));
		IWTimestamp stampNow = new IWTimestamp();
		if (restrictDates) {
			if (earliestDate != null) {
				dateInput.setEarliestPossibleDate(earliestDate.getDate(), localize("child_care.contract_dates_overlap", "You can not choose a date which overlaps another contract."));
				dateInput.setDate(earliestDate.getDate());
			}
			else {
				ChildCareContractArchive archive = getBusiness().getLatestContract(_userID);
				if (archive != null && archive.getTerminatedDate() != null) {
					IWTimestamp stamp = new IWTimestamp(archive.getTerminatedDate());
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
		

		table.add(getSmallHeader(localize("child_care.new_date", "Select the new placement date")), 1, row++);
		table.add(dateInput, 1, row++);

		SubmitButton changeDate = null;
		if (isAlteration)
			changeDate = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.alter_placement_date", "Change placement date"), PARAMETER_ACTION, String.valueOf(ACTION_ALTER_VALID_FROM_DATE)));
		else
			changeDate = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.change_date", "Change date"), PARAMETER_ACTION, String.valueOf(ACTION_CHANGE_DATE)));
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

		TextInput textInput = (TextInput) getStyledInterface(new TextInput(this.PARAMETER_CHILDCARE_TIME));
		textInput.setLength(2);
		textInput.setAsNotEmpty(localize("child_care.child_care_time_required","You must fill in the child care time."));
		textInput.setAsIntegers(localize("child_care.only_integers_allowed","Not a valid child care time."));

		table.add(getSmallHeader(localize("child_care.enter_child_care_time", "Enter child care time:")), 1, row++);
		table.add(getSmallText(localize("child_care.child_care_time", "Time")+":"), 1, row);
		table.add(textInput, 1, row++);
		
		DropdownMenu groups = getGroups(-1, -1);
		groups.addMenuElementFirst("-1","");
		groups.setAsNotEmpty(localize("child_care.must_select_a_group","You must select a group.  If one does not exist, you will have to create one first."), "-1");
		
		table.add(getSmallText(localize("child_care.group", "Group")+":"), 1, row);
		table.add(groups, 1, row++);

		SubmitButton placeInGroup = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.place_in_group", "Place in group"), PARAMETER_ACTION, String.valueOf(ACTION_PLACE_IN_GROUP)));
		table.add(placeInGroup, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getAlterCareTimeForm() throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		ChildCareApplication application = getBusiness().getApplication(_applicationID);
		ChildCareContractArchive archive = getBusiness().getContractFile(application.getContractFileId());

		TextInput textInput = (TextInput) getStyledInterface(new TextInput(this.PARAMETER_CHILDCARE_TIME));
		textInput.setLength(2);
		textInput.setAsNotEmpty(localize("child_care.child_care_time_required","You must fill in the child care time."));
		textInput.setAsIntegers(localize("child_care.only_integers_allowed","Not a valid child care time."));

		table.add(getSmallHeader(localize("child_care.enter_child_care_time", "Enter child care time:")), 1, row++);
		table.add(getSmallText(localize("child_care.child_care_time", "Time")+":"), 1, row);
		table.add(textInput, 1, row++);
		
		DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_CHANGE_DATE));
		
		if (!restrictDates) {
			IWTimestamp stamp = new IWTimestamp();
			if (archive != null) {
				IWTimestamp validFrom = new IWTimestamp(archive.getValidFromDate());
				validFrom.addDays(1);
				dateInput.setDate(validFrom.getDate());
				if (validFrom.isEarlierThan(stamp))
					dateInput.setEarliestPossibleDate(stamp.getDate(), localize("child_care.not_a_valid_date", "You can not choose a date back in time."));
				else
					dateInput.setEarliestPossibleDate(validFrom.getDate(), localize("child_care.contract_dates_overlap", "You can not choose a date which overlaps another contract."));
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
		dateInput.setAsNotEmpty(localize("child_care.must_select_date","You must select a date."));

		table.add(getSmallHeader(localize("child_care.new_date", "Select the new placement date")), 1, row++);
		table.add(dateInput, 1, row++);

		SubmitButton placeInGroup = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.alter_care_time", "Alter care time"), PARAMETER_ACTION, String.valueOf(ACTION_ALTER_CARE_TIME)));
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
		boolean canCancel = !getBusiness().hasFutureContracts(((Integer)application.getPrimaryKey()).intValue());
		
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

		TextInput textInput = (TextInput) getStyledInterface(new TextInput(this.PARAMETER_GROUP_NAME));
		textInput.setLength(24);
		textInput.setAsNotEmpty(localize("child_care.group_name_required","You must fill in a name for the group."));
		if (getSession().getGroupID() != -1) {
			SchoolClass group = getBusiness().getSchoolBusiness().findSchoolClass(new Integer(getSession().getGroupID()));
			if (group.getSchoolClassName() != null)
				textInput.setContent(group.getSchoolClassName());
		}

		table.add(getSmallHeader(localize("child_care.enter_group_name", "Enter group name:")), 1, row++);
		table.add(getSmallText(localize("child_care.group_name", "Name")), 1, row);
		table.add(textInput, 1, row++);

		String localized = "";
		if (getSession().getGroupID() != -1)
			localized = localize("child_care.change_group", "Change group");
		else
			localized = localize("child_care.create_group", "Create group");

		SubmitButton createGroup = (SubmitButton) getStyledInterface(new SubmitButton(localized, PARAMETER_ACTION, String.valueOf(ACTION_CREATE_GROUP)));
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

		SubmitButton updatePrognosis = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.set_prognosis", "Set prognosis"), PARAMETER_ACTION, String.valueOf(ACTION_UPDATE_PROGNOSIS)));
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
						
		//todo: (Roar) localize
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
		
		layoutTbl.add(getSmallHeader(localize("ccnctw_info", "Info about care time.")), 1, row++);
		
		layoutTbl.add(getSmallHeader(localize("ccnctw_from_date", "From date") + ":"), 1, row);	
		DateInput fromDate = (DateInput) getStyledInterface(new DateInput(PARAMETER_CHANGE_DATE));
		fromDate.setAsNotEmpty(localize("ccecw_date_format_alert", "Please choose a valid from date."));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 2);
		fromDate.setEarliestPossibleDate(cal.getTime(), localize("ccecw_date_alert", "Date must be not earlier than two months from today."));
		layoutTbl.add(fromDate, 2, row++);
		
		SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton(localize("cc_ok", "Submit"), PARAMETER_ACTION, String.valueOf(ACTION_END_CONTRACT)));
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
		fromDate.setEarliestPossibleDate(
			new Date(),
			localize("ccnctw_unvalid_date_alert", "The date most be in the future."));

		layoutTbl.add(fromDate, 2, row++);

		row++;

		SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton(localize("cc_ok", "Submit"), PARAMETER_ACTION, String.valueOf(ACTION_NEW_CARE_TIME)));
		layoutTbl.add(submit, 1, row);
		layoutTbl.add(Text.getNonBrakingSpace(), 1, row);
		layoutTbl.add(close, 1, row);
		layoutTbl.setHeight(row, Table.HUNDRED_PERCENT);
		layoutTbl.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return layoutTbl;
	}	
	
	private Table getContractSignerForm(IWContext iwc) throws Exception {
		Table table = null;		
						
		Contract contract = getContract(iwc);
		if (contract.isSigned()) {
			table = new Table();
			table.setCellpadding(5);
			table.setWidth(Table.HUNDRED_PERCENT);
			table.setHeight(Table.HUNDRED_PERCENT);					
			table.add(getHeader("The contract is signed."), 1, 1);
			table.add(close, 1, 2);
			table.setHeight(2, Table.HUNDRED_PERCENT);
			table.setRowVerticalAlignment(2, Table.VERTICAL_ALIGN_BOTTOM);
			
			((Window) getParentObject()).setParentToReload();
			
			return table;
			
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
					}
				}
			}
			
			contract.setUnsetFields(fieldValues);
			ContractTagHome contractHome = (ContractTagHome) IDOLookup.getHome(ContractTag.class);
			Collection tags = contractHome.findAllByCategory(contract.getCategoryId().intValue());
			
			
		//create form for still unset fields
			table = getContractFieldsForm(contract.getUnsetFields(), tags);
			if (table != null && table.getRows() > 1){
				return table;			
			} else {
				//todo: (Roar) Fungerer ikke...
				((Window) getParentObject()).setWidth(700);
				((Window) getParentObject()).setHeight(400);
				return initSignContract(iwc);
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
				
					table.add(getSmallHeader(field.substring(0, 1).toUpperCase() + field.substring(1).toLowerCase() + ":"), 1, row);
					table.add(getStyledInterface(input), 2, row);
					row ++;
				}
			}
		
			if (table != null){
				SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton(localize("cc_ok", "Submit"), PARAMETER_METHOD, String.valueOf(METHOD_SIGN_CONTRACT)));
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
		final DateFormat dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, iwc.getCurrentLocale());
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
		NBSSigningBlock nbsSigningBlock = new NBSSigningBlock();
		nbsSigningBlock.setParameter(PARAMETER_ACTION, ""+ACTION_SIGN_CONTRACT);
		nbsSigningBlock.setParameter(PARAMETER_METHOD, ""+METHOD_SIGN_CONTRACT);		
		nbsSigningBlock.setParameter(PARAMETER_CONTRACT_ID, contract.getPrimaryKey().toString());
		table.add(nbsSigningBlock, 1, 1);
		table.add(close, 1, 2);
		table.setHeight(2, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(2, Table.VERTICAL_ALIGN_BOTTOM);		
		return table; 
	}
		
	
	private void processSignContract(IWContext iwc) throws Exception{
			NBSSigningBlock nbsSigningBlock = new NBSSigningBlock();
			nbsSigningBlock.processSignContract(iwc);	
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

		if (iwc.isParameterSet(PARAMETER_PAGE_ID))
			_pageID = Integer.parseInt(iwc.getParameter(PARAMETER_PAGE_ID));
			
		if (iwc.isParameterSet(PARAMETER_EARLIEST_DATE))
			earliestDate = new IWTimestamp(iwc.getParameter(PARAMETER_EARLIEST_DATE));

		String restrict = getBundle().getProperty(PROPERTY_RESTRICT_DATES, "true");
		restrictDates = true;
		if (restrict != null) {
			restrictDates = Boolean.valueOf(restrict).booleanValue();
		}
	}
	
	private void alterCareTime(IWContext iwc) throws RemoteException {
		IWTimestamp validFrom = new IWTimestamp(iwc.getParameter(PARAMETER_CHANGE_DATE));
		int childCareTime = Integer.parseInt(iwc.getParameter(PARAMETER_CHILDCARE_TIME));
		getBusiness().assignContractToApplication(_applicationID, childCareTime, validFrom, iwc.getCurrentUser(), iwc.getCurrentLocale(), false);

		close();
	}

	private void alterValidFromDate(IWContext iwc) throws RemoteException {
		IWTimestamp validFrom = new IWTimestamp(iwc.getParameter(PARAMETER_CHANGE_DATE));
		getBusiness().alterValidFromDate(_applicationID, validFrom.getDate(), iwc.getCurrentLocale(), iwc.getCurrentUser());

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}

	private void makeOffer(IWContext iwc) throws RemoteException {
		String messageHeader = localize("child_care.application_accepted_subject", "Child care application accepted.");
		String messageBody = iwc.getParameter(PARAMETER_OFFER_MESSAGE);
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
	
	private void changeDate(IWContext iwc) throws RemoteException {
		String placingDate = iwc.getParameter(PARAMETER_CHANGE_DATE);
		IWTimestamp stamp = new IWTimestamp(placingDate);
		getBusiness().changePlacingDate(_applicationID, stamp.getDate());

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
		getBusiness().assignContractToApplication(_applicationID, -1, null, iwc.getCurrentUser(), iwc.getCurrentLocale(), true);

		close();
	}
	
	private void createContractForBankID(IWContext iwc) throws RemoteException {
		getBusiness().assignContractToApplication(_applicationID, -1, null, iwc.getCurrentUser(), iwc.getCurrentLocale(), true);

		close();
	}
	
	private void createGroup(IWContext iwc) throws RemoteException {
		String groupName = iwc.getParameter(PARAMETER_GROUP_NAME);
		getBusiness().getSchoolBusiness().storeSchoolClass(getSession().getGroupID(), groupName, getSession().getChildCareID(), -1, -1, -1);

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
		
		String subject = localize("child_care.placing_subject","Your child placed in child care.");
		String body = localize("child_care.placing_body","{0} has been placed in a group at {1}.");
		getBusiness().placeApplication(getSession().getApplicationID(), subject, body, childCareTime, groupID, iwc.getCurrentUser(), iwc.getCurrentLocale());

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
		getBusiness().moveToGroup(_userID, getSession().getChildCareID(), groupID);

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
		getBusiness().updatePrognosis(getSession().getChildCareID(), threeMonths, oneYear, threeMonthsPriority, oneYearPriority);
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

		Object[] arguments = { child.getNameLastFirst(true), user.getName(), email, workphone, new IWTimestamp(application.getFromDate()).getLocaleDate(iwc.getCurrentLocale()) };
		return arguments;
	}
}
