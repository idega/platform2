package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
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
import se.idega.idegaweb.commune.care.business.PlacementHelper;
import se.idega.idegaweb.commune.care.data.CareTimeBMPBean;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.business.NoPlacementFoundException;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;

import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractHome;
import com.idega.block.contract.data.ContractTag;
import com.idega.block.contract.data.ContractTagHome;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.presentation.SchoolClassDropdownDouble;
import com.idega.builder.business.BuilderLogic;
import com.idega.core.builder.data.ICPage;
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
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.TimePeriod;
import com.idega.util.URLUtil;
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

	public static final String PARAMETER_VACANCIES = "cc_vacancies";

	public static final String PARAMETER_SHOW_VACANCIES = "cc_show_vacancies";

	public static final String PARAMETER_PROVIDER_COMMENTS = "cc_provider_comments";

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

	public static final String PARAMETER_SHOW_PARENTAL = "cc_show_parental";

	public static final String PARAMETER_SHOW_EMPLOYMENT_DROP = "cc_show_employment_drop";

	public static final String PARAMETER_SHOW_PRE_SCHOOL = "cc_show_preschool";

	// private static final String PROPERTY_RESTRICT_DATES =
	// "child_care_restrict_alter_date";

	public static final String FIELD_CURRENT_DATE = "currentdate";

	// private final static String USER_MESSAGE_SUBJECT =
	// "child_care.application_received_subject";
	// private final static String USER_MESSAGE_BODY =
	// "child_care.application_received_body";

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

	private boolean _showVacancies = false;

	private boolean _showEmploymentDrop = true;

	private boolean _showPreSchool = true;

	// private IWTimestamp earliestDate;

	private CloseButton close;

	private Form form;

	// private boolean restrictDates = false;
	boolean onlyAllowFutureCareDate = true; // Changed according to #nacc149

	private boolean _addCareTimeScript = false;

	private User _child = null;
	
	private SubmitButton _submitButton = null;
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parse(iwc);
		switch (_action) {
			case ACTION_CLOSE:
				close();
				break;
			case ACTION_GRANT_PRIORITY:
				grantPriority(iwc);
				break;
			case ACTION_OFFER:
				makeOffer(iwc);
				break;
			case ACTION_CHANGE_DATE:
				changeDate(iwc);
				break;
			case ACTION_PARENTS_AGREE:
				parentsAgree(iwc);
				break;
			case ACTION_CREATE_CONTRACT:
				createContract(iwc);
				break;
			case ACTION_CREATE_GROUP:
				createGroup(iwc);
				break;
			case ACTION_PLACE_IN_GROUP:
				placeInGroup(iwc);
				break;
			case ACTION_MOVE_TO_GROUP:
				moveToGroup(iwc);
				break;
			case ACTION_CANCEL_CONTRACT:
				cancelContract(iwc);
				break;
			case ACTION_UPDATE_PROGNOSIS:
				updatePrognosis(iwc);
				break;
			case ACTION_ALTER_CARE_TIME:
				alterCareTime(iwc);
				break;
			case ACTION_DELETE_GROUP:
				deleteGroup(iwc);
				break;
			case ACTION_RETRACT_OFFER:
				retractOffer(iwc);
				break;
			case ACTION_REMOVE_FUTURE_CONTRACTS:
				removeFutureContracts(iwc);
				break;
			case ACTION_CREATE_CONTRACT_FOR_BANKID:
				createContractForBankID(iwc);
				break;
			case ACTION_ALTER_VALID_FROM_DATE:
				alterValidFromDate(iwc);
				break;
			case ACTION_END_CONTRACT:
				sendEndContractRequest(iwc);
				break;
			case ACTION_NEW_CARE_TIME:
				sendNewCareTimeRequest(iwc);
				break;
			case ACTION_SIGN_CONTRACT:
				processSignContract(iwc);
				break;
			case ACTION_REJECT_APPLICATION:
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
		form.maintainParameter(PARAMETER_SHOW_VACANCIES);
		form.maintainParameter(PARAMETER_SHOW_PARENTAL);
		form.maintainParameter(PARAMETER_SHOW_EMPLOYMENT_DROP);
		form.maintainParameter(PARAMETER_SHOW_PRE_SCHOOL);
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

		// close.setPageToOpen(getParentPageID());
		// close.addParameterToPage(PARAMETER_ACTION, ACTION_CLOSE);

		String userName = null;
		String personalId = null;
		String personalIdUserName = "";
		ChildCareApplication application = getBusiness().getApplication(_applicationID);
		User child;
		if (application != null && userName == null) {
			child = application.getChild();

			if (child != null) {
				personalId = child.getPersonalID();
				userName = getBusiness().getUserBusiness().getNameLastFirst(child, true);
			}
		}
		else if (_userID != -1) {
			child = getBusiness().getUserBusiness().getUser(_userID);

			if (child != null) {
				personalId = child.getPersonalID();
				userName = getBusiness().getUserBusiness().getNameLastFirst(child, true);
			}
		}

		if (userName != null) {
			personalIdUserName = "  -  " + userName + "   " + personalId;
		}

		_addCareTimeScript = false;
		
		switch (_method) {
			case METHOD_GRANT_PRIORITY:
				headerTable.add(getHeader(localize("child_care.grant_priority", "Grant priority") + personalIdUserName), 1, 1);
				contentTable.add(getPriorityForm(iwc), 1, 1);
				break;
			case METHOD_OFFER:
				headerTable.add(getHeader(localize("child_care.offer_placing", "Offer placing") + personalIdUserName), 1, 1);
				contentTable.add(getOfferForm(iwc), 1, 1);
				break;
			case METHOD_CHANGE_DATE:
				headerTable.add(getHeader(localize("child_care.change_date", "Change date") + personalIdUserName), 1, 1);
				contentTable.add(getChangeDateForm(iwc, false), 1, 1);
				break;
			case METHOD_PLACE_IN_GROUP:
				headerTable.add(getHeader(localize("child_care.place_in_group", "Place in group") + personalIdUserName), 1, 1);
				contentTable.add(getPlaceInGroupForm(), 1, 1);
				break;
			case METHOD_MOVE_TO_GROUP:
				headerTable.add(getHeader(localize("child_care.move_to_group", "Move to group") + personalIdUserName), 1, 1);
				contentTable.add(getMoveGroupForm(iwc), 1, 1);
				break;
			case METHOD_CREATE_GROUP:
				if (getSession().getGroupID() != -1)
					headerTable.add(getHeader(localize("child_care.change_group", "Change group") + personalIdUserName), 1, 1);
				else
					headerTable.add(getHeader(localize("child_care.create_group", "Create group")), 1, 1);
				contentTable.add(getCreateGroupForm(), 1, 1);
				break;
			case METHOD_UPDATE_PROGNOSIS:
				headerTable.add(getHeader(localize("child_care.set_prognosis", "Set prognosis")), 1, 1);
				contentTable.add(getUpdatePrognosisForm(), 1, 1);
				break;
			case METHOD_ALTER_CARE_TIME:
				// headerTable.add(getHeader(localize("child_care.alter_care_time",
				// "Alter care time")));
				headerTable.add(getHeader(localize("child_care.alter_contract_or_schooltype_for_child", "Alter the contract/schooltype for this child.") + personalIdUserName), 1, 1);
				contentTable.add(getAlterCareTimeForm(iwc), 1, 1);
				break;
			case METHOD_CANCEL_CONTRACT:
				headerTable.add(getHeader(localize("child_care.cancel_contract", "Cancel contract") + personalIdUserName), 1, 1);
				contentTable.add(getCancelContractForm(iwc), 1, 1);
				break;
			case METHOD_CHANGE_OFFER:
				headerTable.add(getHeader(localize("child_care.change_offer_placing", "Change offer placing") + personalIdUserName), 1, 1);
				contentTable.add(getChangeOfferForm(iwc), 1, 1);
				break;
			case METHOD_RETRACT_OFFER:
				headerTable.add(getHeader(localize("child_care.retract_offer", "Retract offer") + personalIdUserName), 1, 1);
				contentTable.add(getRetractOfferForm(iwc), 1, 1);
				break;
			case METHOD_ALTER_VALID_FROM_DATE:
				headerTable.add(getHeader(localize("child_care.alter_valid_from_date", "Change placement date") + personalIdUserName), 1, 1);
				contentTable.add(getChangeDateForm(iwc, true), 1, 1);
				break;
			case METHOD_VIEW_PROVIDER_QUEUE:
				headerTable.add(getHeader(localize("child_care.view_provider_queue", "Provider queue")), 1, 1);
				contentTable.add(getProviderQueueForm(iwc), 1, 1);
				break;
			case METHOD_END_CONTRACT:
				headerTable.add(getHeader(localize("child_care.end_contract", "End contract") + personalIdUserName), 1, 1);
				contentTable.add(getEndContractForm(), 1, 1);
				break;
			case METHOD_NEW_CARE_TIME:
				headerTable.add(getHeader(localize("child_care.new_care_time", "New care time") + personalIdUserName), 1, 1);
				contentTable.add(getNewCareTimeForm(), 1, 1);
				break;
			case METHOD_SIGN_CONTRACT:

				Object[] contractFormResult = getContractSignerForm(iwc);
				contentTable.add(contractFormResult[1]);
				if (((Boolean) contractFormResult[0]).booleanValue()) {
					headerTable.add(getHeader(localize("child_care.sign_contract", "Sign contract") + personalIdUserName), 1, 1);
				}
				else {
					headerTable.add(getHeader(localize("child_care.fill_in_fields", "Fill in contract fields") + personalIdUserName), 1, 1);
				}
				break;
			case METHOD_REJECT_APPLICATION:
				headerTable.add(getHeader(localize("child_care.reject_application", "Reject application") + personalIdUserName), 1, 1);
				contentTable.add(getRejectApplicationForm(iwc), 1, 1);
				break;

		}
		
		add(form);
		
		if (_addCareTimeScript) {
			_submitButton.setOnSubmitFunction("checkCareTime", getSubmitCheckCareTimeScript(_child));
		}
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
		textArea.setAsNotEmpty(localize("child_care.priority_message_required", "You must fill in the message."));

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
		textArea.setAsNotEmpty(localize("child_care.offer_message_required", "You must fill in the message."));

		table.add(getSmallHeader(localize("child_care.offer_message_info", "The following message will be sent to the child's parents.")), 1, row++);
		table.add(textArea, 1, row++);

		IWTimestamp stamp = new IWTimestamp();
		stamp.addDays(14);
		IWTimestamp tomorrow = new IWTimestamp();
		tomorrow.addDays(1);
		DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_OFFER_VALID_UNTIL));
		dateInput.setEarliestPossibleDate(tomorrow.getDate(), localize("child_care.cant_choose_earlier_date", "You can't choose a date back in time."));
		dateInput.setDate(stamp.getDate());

		table.add(getSmallHeader(localize("child_care.offer_valid_until", "Offer valid until") + ":"), 1, row++);
		table.add(dateInput, 1, row++);

		HiddenInput action = new HiddenInput(PARAMETER_ACTION);
		action.setValue(String.valueOf(ACTION_OFFER));
		table.add(action, 1, 1);
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
		textArea.setAsNotEmpty(localize("child_care.offer_message_required", "You must fill in the message."));

		table.add(getSmallHeader(localize("child_care.offer_message_info", "The following message will be sent to the child's parents.")), 1, row++);
		table.add(textArea, 1, row++);

		IWTimestamp stamp = new IWTimestamp();
		stamp.addDays(14);
		DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_OFFER_VALID_UNTIL));
		dateInput.setDate(stamp.getDate());

		table.add(getSmallHeader(localize("child_care.offer_valid_until", "Offer valid until") + ":"), 1, row++);
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
		textArea.setAsNotEmpty(localize("child_care.offer_message_required", "You must fill in the message."));

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
		textArea.setAsNotEmpty(localize("child_care.rejected_message_required", "You must fill in the message."));

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

		// /ChildCareApplication application =
		// getBusiness().getApplication(_applicationID);
		PlacementHelper helper = getPlacementHelper();
		IWTimestamp stamp = new IWTimestamp();

		TimePeriod deadlinePeriod = helper.getValidPeriod();

		ChildCareContract archive = getBusiness().getLatestContract(_userID);
		IWTimestamp oldTerminationDate = null;
		if (archive != null && archive.getTerminatedDate() != null) {
			oldTerminationDate = new IWTimestamp(archive.getTerminatedDate());
		}

		DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_CHANGE_DATE));
		if (deadlinePeriod != null && deadlinePeriod.getFirstTimestamp() != null) {
			DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
			// deadline has passed

			if (helper.hasDeadlinePassed()) {
				dateInput.setEarliestPossibleDate(deadlinePeriod.getFirstTimestamp().getDate(), localize("childcare.deadline_passed", "Deadline has passed earliest date possible is ") + format.format(deadlinePeriod.getFirstTimestamp().getDate()));
				dateInput.setDate(deadlinePeriod.getFirstTimestamp().getDate());
			}
			// still within deadline
			else {
				dateInput.setEarliestPossibleDate(deadlinePeriod.getFirstTimestamp().getDate(), localize("childcare.deadline_still_within", "You can not choose a date back in time."));
				dateInput.setDate(deadlinePeriod.getFirstTimestamp().getDate());
			}

			if (oldTerminationDate != null) {
				if (oldTerminationDate.isLaterThan(deadlinePeriod.getFirstTimestamp())) {
					oldTerminationDate.addDays(1);
					dateInput.setEarliestPossibleDate(oldTerminationDate.getDate(), localize("child_care.contract_dates_overlap", "You can not choose a date which overlaps another contract."));
					dateInput.setDate(oldTerminationDate.getDate());
				}
			}
		}
		else {
			dateInput.setDate(stamp.getDate());
			dateInput.setEarliestPossibleDate(stamp.getDate(), localize("school.dates_back_in_time_not_allowed", "You can not choose a date back in time."));

			if (oldTerminationDate != null) {
				if (oldTerminationDate.isLaterThan(stamp)) {
					oldTerminationDate.addDays(1);
					dateInput.setEarliestPossibleDate(oldTerminationDate.getDate(), localize("child_care.contract_dates_overlap", "You can not choose a date which overlaps another contract."));
					dateInput.setDate(oldTerminationDate.getDate());
				}
			}
		}
		dateInput.setAsNotEmpty(localize("child_care.must_select_date", "You must select a date."));

		String dateHeader = null;
		if (isAlteration) {
			dateHeader = localize("child_care.new_date", "Select the new placement date");
		}
		else {
			dateHeader = localize("child_care.change_date", "Change date");
		}
		table.add(getSmallHeader(dateHeader), 1, row++);
		if (helper.hasDeadlinePassed())
			table.add(getText(localize("school.deadline_msg_for_passedby_date", "Chosen period has been invoiced. Earliest possible date is the first day of next month.")), 1, row++);
		table.add(dateInput, 1, row++);

		if (isAlteration) {
			table.add(getSmallHeader(localize("child_care.enter_child_care_time", "Enter child care time:")), 1, row++);
			table.add(getSmallText(localize("child_care.child_care_time", "Time") + ":"), 1, row);

			if (isUsePredefinedCareTimeValues()) {
				DropdownMenu menu = getCareTimeMenu(PARAMETER_CHILDCARE_TIME);
				if (helper.getCurrentCareTimeHours() != null)
					menu.setSelectedElement(helper.getCurrentCareTimeHours());
				table.add(menu, 1, row++);
			}
			else {
				TextInput textInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_CHILDCARE_TIME));
				textInput.setLength(2);
				if (helper.getCurrentCareTimeHours() != null)
					textInput.setContent(helper.getCurrentCareTimeHours().toString());
				textInput.setAsNotEmpty(localize("child_care.child_care_time_required", "You must fill in the child care time."));
				textInput.setAsIntegers(localize("child_care.only_integers_allowed", "Not a valid child care time."));
				table.add(textInput, 1, row++);
			}
			
			Collection types = null;
			SchoolBusiness schBuiz = getBusiness().getSchoolBusiness();
			SchoolCategory typeChildcare = schBuiz.getCategoryChildcare();

			try {
				types = helper.getApplication().getProvider().findRelatedSchoolTypes(typeChildcare);

			}
			catch (IDORelationshipException e) {
				e.printStackTrace();
			}
			catch (EJBException e) {
				e.printStackTrace();
			}

			SchoolClassDropdownDouble schoolClasses = new SchoolClassDropdownDouble(PARAMETER_SCHOOL_TYPES, PARAMETER_SCHOOL_CLASS);
			schoolClasses.setLayoutVertical(true);
			schoolClasses.setPrimaryLabel(getSmallText(localize("child_care.school_type", "School type")));
			schoolClasses.setSecondaryLabel(getSmallText(localize("child_care.school_class", "School class")));
			schoolClasses.setVerticalSpaceBetween(15);
			schoolClasses.setSpaceBetween(15);
			schoolClasses.setNoDataListEntry(localize("child_care.no_school_classes", "No school classes"));
			schoolClasses = (SchoolClassDropdownDouble) getStyledInterface(schoolClasses);

			if (!types.isEmpty()) {
				Map typeGroupMap = getBusiness().getSchoolTypeClassMap(types, helper.getApplication().getProviderId(), getSession().getSeasonID(), null, null, localize("child_care.no_school_classes", "No school classes"));
				if (typeGroupMap != null) {
					Iterator iter = typeGroupMap.keySet().iterator();
					while (iter.hasNext()) {
						SchoolType schoolType = (SchoolType) iter.next();
						schoolClasses.addMenuElement(schoolType.getPrimaryKey().toString(), schoolType.getSchoolTypeName(), (Map) typeGroupMap.get(schoolType));
					}
				}
			}
			if (helper.getCurrentClassID() != null)
				schoolClasses.setSelectedValues(helper.getCurrentSchoolTypeID().toString(), helper.getCurrentClassID().toString());
			table.add(schoolClasses, 1, row++);
		}

		// Pre-school
		if (!getBusiness().isAfterSchoolApplication(helper.getApplication()) && _showPreSchool) {
			table.add(getSmallHeader(localize("child_care.pre_school", "Specify pre-school:")), 1, row++);
			TextInput preSchool = (TextInput) getStyledInterface(new TextInput(PARAMETER_PRE_SCHOOL));
			preSchool.setLength(40);
			if (helper.getApplication().getPreSchool() != null)
				preSchool.setContent(helper.getApplication().getPreSchool());
			table.add(preSchool, 1, row++);
		}

		SubmitButton changeDate = null;
		if (isAlteration)
			changeDate = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.alter_placing", "Change placing"), PARAMETER_ACTION, String.valueOf(ACTION_ALTER_VALID_FROM_DATE)));
		else
			changeDate = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.change_date", "Change date"), PARAMETER_ACTION, String.valueOf(ACTION_CHANGE_DATE)));
		_submitButton = changeDate;
		_child = helper.getApplication().getChild();
		_addCareTimeScript = isUsePredefinedCareTimeValues();
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

		if (hasBankId) {
			table.add(getSmallText(localize("child_care.child_care_time", "Time") + ":"), 1, row);
			table.add(getSmallText(getCareTime(application.getCareTime())), 1, row++);
			table.add(new HiddenInput(PARAMETER_CHILDCARE_TIME, application.getCareTime()), 1, 1);

		}
		else {
			table.add(getSmallHeader(localize("child_care.enter_child_care_time", "Enter child care time:")), 1, row++);
			table.add(getSmallText(localize("child_care.child_care_time", "Time") + ":"), 1, row);
			if (isUsePredefinedCareTimeValues()) {
				DropdownMenu menu = getCareTimeMenu(PARAMETER_CHILDCARE_TIME);
				table.add(menu, 1, row++);
			}
			else {
				TextInput textInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_CHILDCARE_TIME));
				textInput.setLength(2);
				textInput.setMaxlength(2);
				textInput.setAsNotEmpty(localize("child_care.child_care_time_required", "You must fill in the child care time."));
				textInput.setAsIntegers(localize("child_care.only_integers_allowed", "Not a valid child care time."));
				table.add(textInput, 1, row++);
			}
		}

		/* *******restricting the classes being chosen */
		Collection types = null;
		SchoolBusiness schBuiz = getBusiness().getSchoolBusiness();
		SchoolCategory typeChildcare = schBuiz.getCategoryChildcare();

		try {
			types = application.getProvider().findRelatedSchoolTypes(typeChildcare);

		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}

		SchoolClassDropdownDouble schoolClasses = new SchoolClassDropdownDouble(getSession().getParameterSchoolTypeID(), getSession().getParameterGroupID());
		// schoolClasses.setLayoutVertical(true);
		// schoolClasses.setPrimaryLabel(getSmallText(localize("child_care.schooltype",
		// "Type")+":"));
		// schoolClasses.setSecondaryLabel(getSmallText(localize("child_care.group",
		// "Group")+":"));
		// schoolClasses.setVerticalSpaceBetween(15);
		// schoolClasses.setSpaceBetween(15);
		// schoolClasses.setNoDataListEntry(localize("child_care.no_school_classes","No
		// school classes"));
		schoolClasses = (SchoolClassDropdownDouble) getStyledInterface(schoolClasses);
		// int classID = archive.getSchoolClassMember().getSchoolClassId();

		if (getChildcareID() != -1) {

			if (!types.isEmpty()) {
				Map typeGroupMap = getBusiness().getSchoolTypeClassMap(types, application.getProviderId(), getSession().getSeasonID(), null, null, localize("child_care.no_school_classes", "No school classes"));
				if (typeGroupMap != null) {
					Iterator iter = typeGroupMap.keySet().iterator();
					while (iter.hasNext()) {
						SchoolType schoolType = (SchoolType) iter.next();
						schoolClasses.addMenuElement(schoolType.getPrimaryKey().toString(), schoolType.getSchoolTypeName(), (Map) typeGroupMap.get(schoolType));
					}
				}
			}
		}

		Table dropdownTable = new Table();
		int dropRow = 1;

		schoolClasses.addEmptyElement(localize("child_care.choose_schooltype", "Choose here"), localize("child_care.choose_schoolgroup", "Choose here"));

		// DropdownMenu schoolTypes = getSchoolTypes(-1, -1);
		// DropdownMenu schoolTypes = schoolClasses.getPrimaryDropdown();
		// schoolClasses.getPrimaryDropdown().addMenuElementFirst("-1",localize("child_care.choose_schooltype","Choose
		// here"));
		schoolClasses.getPrimaryDropdown().setAsNotEmpty(localize("child_care.must_select_a_type", "You must select a type."), "-1");

		dropdownTable.add(getSmallText(localize("child_care.schooltype", "Type") + ":"), 1, dropRow);
		dropdownTable.add(schoolClasses.getPrimaryDropdown(), 2, dropRow++);
		// dropdownTable.add(schoolClasses);

		// DropdownMenu groups = getGroups(-1, -1)
		// DropdownMenu groups = schoolClasses.getSecondaryDropdown();
		// schoolClasses.getSecondaryDropdown().addMenuElementFirst("-1",localize("child_care.choose_schoolgroup","Choose
		// here"));
		schoolClasses.getSecondaryDropdown().setAsNotEmpty(localize("child_care.must_select_a_group", "You must select a group.  If one does not exist, you will have to create one first."), "-1");

		dropdownTable.add(getSmallText(localize("child_care.group", "Group") + ":"), 1, dropRow);
		dropdownTable.add(schoolClasses.getSecondaryDropdown(), 2, dropRow++);

		// dropdownTable.add(getSmallText(localize("child_care.school_type_and_school_class",
		// "School type and class")+":"), 1, dropRow);
		// table.add(mSchoolType, 2, row++);
		dropdownTable.add(schoolClasses, 2, dropRow);

		if (_showEmploymentDrop) {
			DropdownMenu employmentTypes = getEmploymentTypes(PARAMETER_EMPLOYMENT_TYPE, -1);
			employmentTypes.setAsNotEmpty(localize("child_care.must_select_employment_type", "You must select employment type."), "-1");

			dropdownTable.add(getSmallText(localize("child_care.employment_type", "Employment type") + ":"), 1, dropRow);
			dropdownTable.add(employmentTypes, 2, dropRow);
		}

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

		// ChildCareApplication application =
		// getBusiness().getApplication(_applicationID);
		// ChildCareContract archive =
		// getBusiness().getValidContract(_applicationID);

		// getBusiness().getContractFile(application.getContractFileId());
		PlacementHelper helper = getPlacementHelper();

		TimePeriod deadlinePeriod = null;
		deadlinePeriod = helper.getValidPeriod();

		table.add(new HiddenInput("ccc_old_archive_id", helper.getContract().getPrimaryKey().toString()), 1, 1);

		Date rejectDate = helper.getApplication().getRejectionDate();
		IWTimestamp rejectionDate = null;
		if (rejectDate != null)
			rejectionDate = new IWTimestamp(rejectDate);

		table.add(getSmallHeader(localize("child_care.enter_child_care_time", "Enter child care time:")), 1, row++);
		table.add(getSmallText(localize("child_care.child_care_time", "Time") + ":"), 1, row);

		if (isUsePredefinedCareTimeValues()) {
			DropdownMenu menu = getCareTimeMenu(PARAMETER_CHILDCARE_TIME);
			if (helper.getCurrentCareTimeHours() != null)
				menu.setSelectedElement(helper.getCurrentCareTimeHours());
			table.add(menu, 1, row++);
		}
		else {
			TextInput careTimeInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_CHILDCARE_TIME));
			careTimeInput.setLength(helper.getMaximumCareTimeHours().toString().length());
			careTimeInput.setMaxlength(helper.getMaximumCareTimeHours().toString().length());
			careTimeInput.setAsNotEmpty(localize("child_care.child_care_time_required", "You must fill in the child care time."));
			careTimeInput.setAsIntegers(localize("child_care.only_integers_allowed", "Not a valid child care time."));
			if (helper.getCurrentCareTimeHours() != null)
				careTimeInput.setContent(helper.getCurrentCareTimeHours());
			table.add(careTimeInput, 1, row++);
		}

		IWTimestamp stamp = new IWTimestamp();
		DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_CHANGE_DATE));
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
		if (deadlinePeriod != null && deadlinePeriod.getFirstTimestamp() != null) {

			// deadline has passed

			if (helper.hasDeadlinePassed()) {
				dateInput.setEarliestPossibleDate(deadlinePeriod.getFirstTimestamp().getDate(), localize("childcare.deadline_passed", "Deadline has passed earliest date possible is ") + format.format(deadlinePeriod.getFirstTimestamp().getDate()));
				if (rejectionDate != null)
					dateInput.setLatestPossibleDate(rejectionDate.getDate(), localize("child_care.contract_date_expired", "You can not choose a date after the contract has been terminated. The termination date is ") + format.format(rejectionDate.getDate()));
				dateInput.setDate(deadlinePeriod.getFirstTimestamp().getDate());
			}
			// still within deadline
			else {
				dateInput.setEarliestPossibleDate(deadlinePeriod.getFirstTimestamp().getDate(), localize("childcare.deadline_still_within", "You can not choose a date back in time."));
				if (rejectionDate != null)
					dateInput.setLatestPossibleDate(rejectionDate.getDate(), localize("child_care.contract_date_expired", "You can not choose a date after the contract has been terminated. The termination date is ") + format.format(rejectionDate.getDate()));
				dateInput.setDate(deadlinePeriod.getFirstTimestamp().getDate());
			}

		}
		else {
			dateInput.setDate(stamp.getDate());
			dateInput.setEarliestPossibleDate(stamp.getDate(), localize("school.dates_back_in_time_not_allowed", "You can not choose a date back in time."));
			if (rejectionDate != null)
				dateInput.setLatestPossibleDate(rejectionDate.getDate(), localize("child_care.contract_date_expired", "You can not choose a date after the contract has been terminated. The termination date is ") + format.format(rejectionDate.getDate()));
		}

		dateInput.setAsNotEmpty(localize("child_care.must_select_date", "You must select a date."));
		table.add(getSmallHeader(localize("child_care.new_date", "Select the new placement date")), 1, row++);
		table.add(dateInput, 1, row++);

		Collection types = null;
		SchoolBusiness schBuiz = getBusiness().getSchoolBusiness();
		SchoolCategory typeChildcare = schBuiz.getCategoryChildcare();

		try {
			types = helper.getApplication().getProvider().findRelatedSchoolTypes(typeChildcare);

		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}

		SchoolClassDropdownDouble schoolClasses = new SchoolClassDropdownDouble(PARAMETER_SCHOOL_TYPES, PARAMETER_SCHOOL_CLASS);
		schoolClasses.setLayoutVertical(true);
		schoolClasses.setPrimaryLabel(getSmallText(localize("child_care.school_type", "School type")));
		schoolClasses.setSecondaryLabel(getSmallText(localize("child_care.school_class", "School class")));
		schoolClasses.setVerticalSpaceBetween(15);
		schoolClasses.setSpaceBetween(15);
		schoolClasses.setNoDataListEntry(localize("child_care.no_school_classes", "No school classes"));
		schoolClasses = (SchoolClassDropdownDouble) getStyledInterface(schoolClasses);

		if (!types.isEmpty()) {
			Map typeGroupMap = getBusiness().getSchoolTypeClassMap(types, helper.getApplication().getProviderId(), getSession().getSeasonID(), null, null, localize("child_care.no_school_classes", "No school classes"));
			if (typeGroupMap != null) {
				Iterator iter = typeGroupMap.keySet().iterator();
				while (iter.hasNext()) {
					SchoolType schoolType = (SchoolType) iter.next();
					schoolClasses.addMenuElement(schoolType.getPrimaryKey().toString(), schoolType.getSchoolTypeName(), (Map) typeGroupMap.get(schoolType));
				}
			}
		}
		if (helper.getCurrentClassID() != null)
			schoolClasses.setSelectedValues(helper.getCurrentSchoolTypeID().toString(), helper.getCurrentClassID().toString());
		table.add(schoolClasses, 1, row++);

		if (_showEmploymentDrop) {
			DropdownMenu employmentTypes = getEmploymentTypes(PARAMETER_EMPLOYMENT_TYPE, -1);
			// /if(archive.getEmploymentTypeId()>0)
			// / employmentTypes.setSelectedElement(archive.getEmploymentTypeId());
			if (helper.getCurrentEmploymentID() != null)
				employmentTypes.setSelectedElement(helper.getCurrentEmploymentID().toString());
			employmentTypes.setAsNotEmpty(localize("child_care.must_select_employment_type", "You must select employment type."), "-1");
			employmentTypes = (DropdownMenu) getStyledInterface(employmentTypes);

			table.add(getSmallText(localize("child_care.employment_type", "Employment type") + ":"), 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(employmentTypes, 1, row++);
		}

		if (helper.hasDeadlinePassed())
			table.add(getText(localize("school.deadline_msg_for_passedby_date", "Chosen period has been invoiced. Earliest possible date is the first day of next month.")), 1, row++);

		SubmitButton placeInGroup = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.alter_care_time", "Alter care time"), PARAMETER_ACTION, String.valueOf(ACTION_ALTER_CARE_TIME)));
		_submitButton = placeInGroup;
		_child = helper.getApplication().getChild();
		_addCareTimeScript = isUsePredefinedCareTimeValues();
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
		IWTimestamp placementDate = null;
		IWTimestamp today = new IWTimestamp();
		// today.addDays(1);

		ChildCareApplication application = getBusiness().getApplicationForChildAndProvider(_userID, getSession().getChildCareID());
		if (application != null) {

			if (application.getFromDate() != null) {
				placementDate = new IWTimestamp(application.getFromDate());
			}

			boolean canCancel = getBusiness().canCancelContract(((Integer) application.getPrimaryKey()).intValue());

			if (canCancel) {
				if (application.getApplicationStatus() == getBusiness().getStatusReady() || application.getApplicationStatus() == getBusiness().getStatusParentTerminated()) {
					if (application.getApplicationStatus() == getBusiness().getStatusReady()) {
						RadioButton parentalLeave = this.getRadioButton(PARAMETER_CANCEL_REASON, String.valueOf(true));
						parentalLeave.keepStatusOnAction(true);
						RadioButton other = getRadioButton(PARAMETER_CANCEL_REASON, String.valueOf(false));
						other.keepStatusOnAction(true);
						other.setSelected(true);
	
						table.add(getSmallHeader(localize("child_care.enter_cancel_information", "Enter cancel information:")), 1, row++);
						table.add(parentalLeave, 1, row);
						table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.cancel_parental_leave", "Cancel because of parental leave")), 1, row);
						table.add(new Break(), 1, row);
						table.add(other, 1, row);
						table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.cancel_other", "Other reason")), 1, row++);
					}
					
					IWTimestamp stampNow = new IWTimestamp();
					stampNow.addDays(-1);
	
					PlacementHelper helper = getPlacementHelper();
					TimePeriod deadlinePeriod = null;
					deadlinePeriod = helper.getValidPeriod();
	
					DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_CANCEL_DATE));
					if (deadlinePeriod != null && deadlinePeriod.getFirstTimestamp() != null) {
						DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
	
						// deadline has passed
						if (helper.hasDeadlinePassed()) {
							dateInput.setEarliestPossibleDate(deadlinePeriod.getFirstTimestamp().getDate(), localize("childcare.deadline_passed", "Deadline has passed earliest date possible is ") + format.format(deadlinePeriod.getFirstTimestamp().getDate()));
							dateInput.setDate(deadlinePeriod.getFirstTimestamp().getDate());
						}
						// still within deadline
						else {
							if (placementDate != null && placementDate.isLaterThan(deadlinePeriod.getFirstTimestamp())) {
								today.addDays(2);
								dateInput.setEarliestPossibleDate(today.getDate(), localize("childcare.deadline_still_within_no_start_contract", "You can not choose a date back in time. If you want to have the contract removed, please contact Kundvalsgruppen"));
								dateInput.setDate(deadlinePeriod.getFirstTimestamp().getDate());
							}
							else {
								dateInput.setEarliestPossibleDate(deadlinePeriod.getFirstTimestamp().getDate(), localize("childcare.deadline_still_within", "You can not choose a date back in time."));
								dateInput.setDate(deadlinePeriod.getFirstTimestamp().getDate());
							}
						}
					}
					else {
						dateInput.setEarliestPossibleDate(stampNow.getDate(), localize("school.dates_back_in_time_not_allowed", "You can not choose a date back in time."));
					}
					dateInput.setAsNotEmpty(localize("child_care.must_select_date", "You must select a date."));
					dateInput.keepStatusOnAction(true);
					
					if (application.getRequestedCancelDate() != null) {
						dateInput.setDate(application.getRequestedCancelDate());
					}			
					
					table.add(getSmallHeader(localize("child_care.cancel_date", "Cancel date") + ":"), 1, row++);
					table.add(dateInput, 1, row++);
					if (helper.hasDeadlinePassed())
						table.add(getText(localize("school.deadline_msg_for_passedby_date", "Chosen period has been invoiced. Earliest possible date is the first day of next month.")), 1, row++);

					SubmitButton cancelContract = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.cancel_contract", "Cancel contract"), PARAMETER_ACTION, String.valueOf(ACTION_CANCEL_CONTRACT)));
					if (application.getApplicationStatus() == getBusiness().getStatusParentTerminated()) {
						form.addParameter(PARAMETER_METHOD, METHOD_CANCEL_CONTRACT);
					}
					form.setToDisableOnSubmit(cancelContract, true);
					table.add(cancelContract, 1, row);
					table.add(Text.getNonBrakingSpace(), 1, row);
				}
				else if (application.getApplicationStatus() == getBusiness().getStatusWaiting()) {
					IWTimestamp stampNow = new IWTimestamp();
					DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_CANCEL_DATE));
					dateInput.setDate(stampNow.getDate());
					
					table.add(getSmallHeader(localize("child_care.cancel_confirmation_received", "Cancel confirmation received") + ":"), 1, row++);
					table.add(dateInput, 1, row++);
					
					SubmitButton cancelContract = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.set_received", "Set"), PARAMETER_ACTION, String.valueOf(ACTION_CANCEL_CONTRACT)));
					form.setToDisableOnSubmit(cancelContract, true);
					table.add(cancelContract, 1, row);
					table.add(Text.getNonBrakingSpace(), 1, row);
					
					GenericButton showForm = getButton(new GenericButton("cancel_form", localize("child_care.show_cancel_form", "Show cancel form")));
					showForm.setFileToOpen(application.getCancelFormFileID());
					table.add(showForm, 1, row);
					table.add(Text.getNonBrakingSpace(), 1, row);
				}
				IWTimestamp stampNow = new IWTimestamp();
				stampNow.addDays(-1);
				
				PlacementHelper helper = getPlacementHelper();
				TimePeriod deadlinePeriod = null;
				deadlinePeriod = helper.getValidPeriod();

				DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_CANCEL_DATE));
				if (deadlinePeriod != null && deadlinePeriod.getFirstTimestamp() != null) {
					DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());

					// deadline has passed
					if (helper.hasDeadlinePassed()) {
						dateInput.setEarliestPossibleDate(deadlinePeriod.getFirstTimestamp().getDate(), localize("childcare.deadline_passed", "Deadline has passed earliest date possible is ") + format.format(deadlinePeriod.getFirstTimestamp().getDate()));
						dateInput.setDate(deadlinePeriod.getFirstTimestamp().getDate());
					}
					// still within deadline
					else {
						if (placementDate != null && placementDate.isLaterThan(deadlinePeriod.getFirstTimestamp())) {
							today.addDays(2);
							dateInput.setEarliestPossibleDate(today.getDate(), localize("childcare.deadline_still_within_no_start_contract", "You can not choose a date back in time. If you want to have the contract removed, please contact Kundvalsgruppen"));
							dateInput.setDate(deadlinePeriod.getFirstTimestamp().getDate());
						}
						else {
							dateInput.setEarliestPossibleDate(deadlinePeriod.getFirstTimestamp().getDate(), localize("childcare.deadline_still_within", "You can not choose a date back in time."));
							dateInput.setDate(deadlinePeriod.getFirstTimestamp().getDate());
						}
					}
				}
				else {
					dateInput.setEarliestPossibleDate(stampNow.getDate(), localize("school.dates_back_in_time_not_allowed", "You can not choose a date back in time."));
				}
				dateInput.setAsNotEmpty(localize("child_care.must_select_date", "You must select a date."));
				dateInput.keepStatusOnAction(true);
				
				if (application.getRequestedCancelDate() != null) {
					dateInput.setDate(application.getRequestedCancelDate());
				}

//				table.add(getSmallHeader(localize("child_care.cancel_date", "Cancel date") + ":"), 1, row++);
//				table.add(dateInput, 1, row++);
//				if (helper.hasDeadlinePassed())
//					table.add(getText(localize("school.deadline_msg_for_passedby_date", "Chosen period has been invoiced. Earliest possible date is the first day of next month.")), 1, row++);

//				SubmitButton cancelContract = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.cancel_contract", "Cancel contract"), PARAMETER_ACTION, String.valueOf(ACTION_CANCEL_CONTRACT)));
//				if (application.getApplicationStatus() == getBusiness().getStatusParentTerminated()) {
//					form.addParameter(PARAMETER_METHOD, METHOD_CANCEL_CONTRACT);
//				}
//				form.setToDisableOnSubmit(cancelContract, true);
//				table.add(cancelContract, 1, row);
//				table.add(Text.getNonBrakingSpace(), 1, row);
			}
			else if (application.getApplicationStatus() == getBusiness().getStatusWaiting()) {
				IWTimestamp stampNow = new IWTimestamp();
				DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_CANCEL_DATE));
				dateInput.setDate(stampNow.getDate());
				
				table.add(getSmallHeader(localize("child_care.cancel_confirmation_received", "Cancel confirmation received") + ":"), 1, row++);
				table.add(dateInput, 1, row++);
				
				SubmitButton cancelContract = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.cancel_contract", "Cancel contract"), PARAMETER_ACTION, String.valueOf(ACTION_CANCEL_CONTRACT)));
				form.setToDisableOnSubmit(cancelContract, true);
				table.add(cancelContract, 1, row);
				table.add(Text.getNonBrakingSpace(), 1, row);
				
				GenericButton showForm = getButton(new GenericButton("cancel_form", localize("child_care.show_cancel_form", "Show cancel form")));
				showForm.setFileToOpen(application.getCancelFormFileID());
				table.add(showForm, 1, row);
				table.add(Text.getNonBrakingSpace(), 1, row);
			}
			else {
				table.add(getSmallErrorText(localize("child_care.must_remove_future_contracts", "Future contracts must be removed before cancel is possible.")), 1, row++);
			}
		}
		else {
			table.add(getSmallErrorText(localize("child_care.no_application_found", "No childcare application found.")), 1, row++);
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
		textInput.setAsNotEmpty(localize("child_care.group_name_required", "You must fill in a name for the group."));

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

		// school types

		School school = getSession().getProvider();
		Collection availableTypes = new ArrayList();
		try {
			availableTypes = school.getSchoolTypes();
		}
		catch (IDORelationshipException ex) {
			ex.printStackTrace();
		}

		DropdownMenu types = getDropdownMenuLocalized(PARAMETER_SCHOOL_TYPES, availableTypes, "getLocalizationKey", "");
		if (currentType != null) {
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
		groups.addMenuElementFirst("-1", "");
		groups.setAsNotEmpty(localize("child_care.must_select_a_group", "You must select a group.  If one does not exist, you will have to create one first."), "-1");

		table.add(getSmallHeader(localize("child_care.select_group", "Select group to move child to")), 1, row++);
		table.add(getSmallText(localize("child_care.group", "Group") + ":"), 1, row);
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
		table.add(getSmallHeader(localize("child_care.prognosis_information", "Enter the prognosis information for your childcare.")), 1, row++);

		table.mergeCells(1, row, 2, row);
		table.add(getSmallHeader(localize("child_care.enter_prognosis", "Enter prognosis:")), 1, row++);

		TextInput threeMonths = (TextInput) getStyledInterface(new TextInput(PARAMETER_THREE_MONTHS_PROGNOSIS));
		threeMonths.setLength(3);
		threeMonths.setAsNotEmpty(localize("child_care.three_months_prognosis_required", "You must fill in the three months prognosis."));
		threeMonths.setAsIntegers(localize("child_care.only_integers_allowed", "Not a valid prognosis."));
		if (prognosis != null)
			threeMonths.setContent(String.valueOf(prognosis.getThreeMonthsPrognosis()));

		TextInput threeMonthsPriority = (TextInput) getStyledInterface(new TextInput(PARAMETER_THREE_MONTHS_PRIORITY));
		threeMonthsPriority.setLength(3);
		threeMonthsPriority.setAsNotEmpty(localize("child_care.three_months_priority_required", "You must fill in the three months priority."));
		threeMonthsPriority.setAsIntegers(localize("child_care.only_integers_allowed", "Not a valid prognosis."));
		if (prognosis != null && prognosis.getThreeMonthsPriority() != -1)
			threeMonthsPriority.setContent(String.valueOf(prognosis.getThreeMonthsPriority()));

		table.add(getSmallText(localize("child_care.three_months_prognosis", "Three months prognosis") + ":"), 1, row);
		table.add(threeMonths, 2, row);
		table.add(getSmallText(localize("child_care.thereof_priority", "there of priority") + ":"), 3, row);
		table.add(threeMonthsPriority, 4, row++);

		TextInput oneYear = (TextInput) getStyledInterface(new TextInput(PARAMETER_ONE_YEAR_PROGNOSIS));
		oneYear.setLength(3);
		oneYear.setAsNotEmpty(localize("child_care.one_year_prognosis_required", "You must fill in the one year prognosis."));
		oneYear.setAsIntegers(localize("child_care.only_integers_allowed", "Not a valid prognosis."));
		if (prognosis != null)
			oneYear.setContent(String.valueOf(prognosis.getOneYearPrognosis()));

		TextInput oneYearPriority = (TextInput) getStyledInterface(new TextInput(PARAMETER_ONE_YEAR_PRIORITY));
		oneYearPriority.setLength(3);
		oneYearPriority.setAsNotEmpty(localize("child_care.one_year_priority_required", "You must fill in the one year priority."));
		oneYearPriority.setAsIntegers(localize("child_care.only_integers_allowed", "Not a valid prognosis."));
		if (prognosis != null && prognosis.getOneYearPriority() != -1)
			oneYearPriority.setContent(String.valueOf(prognosis.getOneYearPriority()));

		table.add(getSmallText(localize("child_care.one_year_prognosis", "Twelve months prognosis") + ":"), 1, row);
		table.add(oneYear, 2, row);
		table.add(getSmallText(localize("child_care.thereof_priority", "there of priority") + ":"), 3, row);
		table.add(oneYearPriority, 4, row++);

		// //////////////// added provider capacity 040402 Malin
		table.mergeCells(1, row, 4, row);
		table.add(getSmallHeader(localize("child_care.capacity_information", "Enter the provider capacity.")), 1, row++);

		table.mergeCells(2, row, 4, row);
		TextInput providerCapacity = (TextInput) getStyledInterface(new TextInput(PARAMETER_PROVIDER_CAPACITY));
		providerCapacity.setLength(3);
		providerCapacity.setAsNotEmpty(localize("child_care.capacity_required", "You must fill in the provider capacity."));
		providerCapacity.setAsIntegers(localize("child_care.capacity_only_integers_allowed", "Not a valid number."));
		if (prognosis != null && prognosis.getProviderCapacity() != -1)
			providerCapacity.setContent(String.valueOf(prognosis.getProviderCapacity()));

		table.add(getSmallText(localize("child_care.provider_capacity", "Provider capacity") + ":"), 1, row);
		table.add(providerCapacity, 2, row);
		table.add("", 3, row++);
		if (_showVacancies) {
			TextInput tiVacancies = (TextInput) getStyledInterface(new TextInput(PARAMETER_VACANCIES));
			tiVacancies.setLength(3);
			// tiVacancies.setAsNotEmpty(localize("child_care.vacancies_required","You
			// must fill in vacancies."));
			tiVacancies.setAsIntegers(localize("child_care.vacancies_only_integers_allowed", "Not a valid number."));
			if (prognosis != null && prognosis.getVacancies() != -1) {
				tiVacancies.setContent(String.valueOf(prognosis.getVacancies()));
			}
			table.add(getSmallText(localize("child_care.vacancies", "Number of vacancies") + ":"), 1, row);
			table.add(tiVacancies, 2, row);
			table.add("", 3, row++);
		}

		TextArea taProviderComments = (TextArea) getStyledInterface(new TextArea(PARAMETER_PROVIDER_COMMENTS));
		taProviderComments.setColumns(50);
		taProviderComments.setRows(6);
		taProviderComments.setMaximumCharacters(200);
		if (prognosis != null && prognosis.getProviderComments() != null)
			taProviderComments.setContent(String.valueOf(prognosis.getProviderComments()));

		table.add(getSmallHeader(localize("child_care.provider_comments", "Comments") + ":"), 1, row++);
		table.mergeCells(1, row, 4, row);
		table.add(taProviderComments, 1, row);
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
	 * 
	 * @param iwc
	 * @return
	 * @throws Exception
	 */
	private Table getProviderQueueForm(IWContext iwc) throws RemoteException {

		String providerId = iwc.getParameter(CCConstants.PROVIDER_ID);
		String appId = iwc.getParameter(CCConstants.APPID);
		School school = getBusiness().getSchoolBusiness().getSchool(providerId);

		ChildCarePrognosis prognosis = getBusiness().getPrognosis(Integer.parseInt(providerId));

		String prognosisText = prognosis == null ? localize("ccpqw_no_prognosis", "No prognosis available") : localize("ccpqw_three_months", "Three months:") + " " + prognosis.getThreeMonthsPrognosis() + Text.BREAK + localize("ccpqw_one_year", "One year:") + " " + prognosis.getOneYearPrognosis() + Text.BREAK + localize("ccpqw_updated_date", "Updated date:") + " " + prognosis.getUpdatedDate();

		Table appTbl = new Table();

		// add(new Text("ProviderId: " + providerId));
		if (providerId != null) {
			Collection applications = getBusiness().getOpenAndGrantedApplicationsByProvider(new Integer(providerId).intValue());

			Iterator i = applications.iterator();

			appTbl.add(getSmallHeader(localize("ccpqw_order", "Queue number")), 1, 1);
			appTbl.add(getSmallHeader(localize("ccpqw_queue_date", "Queue date")), 2, 1);
			appTbl.add(getSmallHeader(localize("ccpqw_from_date", "Placement date")), 3, 1);
			appTbl.setRowColor(1, getHeaderColor());

			int row = 2;

			while (i.hasNext()) {
				ChildCareApplication app = (ChildCareApplication) i.next();

				Text queueOrder = getSmallText("" + getBusiness().getNumberInQueue(app)), queueDate = getSmallText(app.getQueueDate().toString()), fromDate = getSmallText(app.getFromDate().toString());
				// currentAppId = style.getSmallText(""+app.getNodeID()); //debug only

				appTbl.add(queueOrder, 1, row);
				appTbl.add(queueDate, 2, row);
				appTbl.add(fromDate, 3, row);
				// appTbl.add(currentAppId, 4, row); //debug only

				if (app.getNodeID() == new Integer(appId).intValue()) {
					makeBlueAndBold(queueOrder);
					makeBlueAndBold(queueDate);
					makeBlueAndBold(fromDate);
				}

				if (row % 2 == 0) {
					appTbl.setRowColor(row, getZebraColor1());
				}
				else {
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

		layoutTbl.mergeCells(1, row, 2, row);

		layoutTbl.add(getSmallText(localize("ccpqw_prognosis_info", "On this page the childcare providers are presented with their total queue...")), 1, row++);

		layoutTbl.add(getSmallHeader(localize("ccpqw_provider", "Provider") + ":"), 1, row);
		layoutTbl.add(getSmallText(school.getName()), 2, row++);

		layoutTbl.setRowHeight(2, "20px");

		layoutTbl.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		layoutTbl.setVerticalAlignment(2, row, Table.VERTICAL_ALIGN_TOP);
		layoutTbl.add(getSmallHeader(localize("ccpqw_prognosisr", "Prognosis") + ":"), 1, row);
		layoutTbl.add(getSmallText(prognosisText), 2, row++);

		layoutTbl.setRowHeight(row++, "20px");

		layoutTbl.add(appTbl, 1, row);
		layoutTbl.mergeCells(1, row, 2, row);
		row++;

		layoutTbl.add(close, 1, row);
		layoutTbl.setHeight(row, Table.HUNDRED_PERCENT);
		layoutTbl.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		// CloseButton closeBtn = (CloseButton) getStyledInterface(new
		// CloseButton(localize("ccpqw_close", "Close")));
		// layoutTbl.add(closeBtn, 1, 6);
		// layoutTbl.setAlignment(1, 6, "left");

		return layoutTbl;
	}

	private Text makeBlueAndBold(Text t) {
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
		layoutTbl.mergeCells(1, row, 2, row);
		layoutTbl.add(getSmallHeader(localize("ccnctw_info_end_contr", "Info about ending contract.")), 1, row++);
		layoutTbl.mergeCells(1, row, 2, row);
		layoutTbl.add(getSmallText(localize("ccnctw_cancel_info", "This is just a request to cancel the contract")), 1, row++);
		layoutTbl.add(getSmallHeader(localize("ccnctw_from_date", "From date") + ":"), 1, row);
		DateInput fromDate = (DateInput) getStyledInterface(new DateInput(PARAMETER_CHANGE_DATE));
		fromDate.setAsNotEmpty(localize("ccecw_date_format_alert", "Please choose a valid from date."));
		IWTimestamp calendar = new IWTimestamp();
		calendar.addDays(-1);
		fromDate.setEarliestPossibleDate(calendar.getDate(), localize("ccecw_date_alert", "Date must be not earlier than two months from today."));
		layoutTbl.add(fromDate, 2, row++);

		RadioButton parentalLeave = this.getRadioButton(PARAMETER_CANCEL_REASON, String.valueOf(true));
		parentalLeave.keepStatusOnAction(true);
		RadioButton other = getRadioButton(PARAMETER_CANCEL_REASON, String.valueOf(false));
		other.keepStatusOnAction(true);
		other.setSelected(true);

		layoutTbl.mergeCells(1, row, 2, row);
		layoutTbl.add(getSmallHeader(localize("child_care.enter_cancel_information", "Enter cancel information:")), 1, row++);
		layoutTbl.mergeCells(1, row, 2, row);
		layoutTbl.add(parentalLeave, 1, row);
		layoutTbl.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.cancel_parental_leave", "Cancel because of parental leave")), 1, row);
		layoutTbl.add(new Break(), 1, row);
		layoutTbl.add(other, 1, row);
		layoutTbl.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.cancel_other", "Other reason")), 1, row++);

		TextArea textArea = (TextArea) getStyledInterface(new TextArea(PARAMETER_REJECT_MESSAGE));
		textArea.setWidth(Table.HUNDRED_PERCENT);
		textArea.setRows(7);

		layoutTbl.mergeCells(1, row, 2, row);
		layoutTbl.add(getSmallHeader(localize("child_care.cancel_message_info", "Message")), 1, row);
		layoutTbl.add(new Break(), 1, row);
		layoutTbl.add(textArea, 1, row++);

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
		layoutTbl.mergeCells(1, row, 2, row);
		layoutTbl.add(getSmallText(localize("ccnctw_info_more", "This is just a request about changing the caretime")), 1, row++);

		layoutTbl.add(getSmallHeader(localize("ccnctw_care_time", "Care time") + ":"), 1, row);
		if (isUsePredefinedCareTimeValues()) {
			DropdownMenu menu = getCareTimeMenu(PARAMETER_CHILDCARE_TIME);
			menu.setSelectedElement(application.getCareTime());
			layoutTbl.add(menu, 2, row++);
		}
		else {
			TextInput careTime = (TextInput) getStyledInterface(new TextInput(PARAMETER_CHILDCARE_TIME));
			careTime.setValue(application.getCareTime());
			careTime.setAsIntegers(localize("ccnctw_alert_care_time_format", "Care time must be an integer"));
			careTime.setLength(4);
			layoutTbl.add(careTime, 2, row++);
		}

		layoutTbl.add(getSmallHeader(localize("ccnctw_from_date", "From date") + ":"), 1, row);

		DateInput fromDate = (DateInput) getStyledInterface(new DateInput(PARAMETER_CHANGE_DATE));
		fromDate.setAsNotEmpty(localize("ccnctw_unvalid_date_format_alert", "Please choose a valid from date."));
		if (onlyAllowFutureCareDate) {
			fromDate.setEarliestPossibleDate(new Date(), localize("ccnctw_unvalid_date_alert", "The date most be in the future."));
		}
		layoutTbl.add(fromDate, 2, row++);

		row++;

		SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton(localize("cc_ok", "Submit"), PARAMETER_ACTION, String.valueOf(ACTION_NEW_CARE_TIME)));
		_submitButton = submit;
		_child = application.getChild();
		_addCareTimeScript = isUsePredefinedCareTimeValues();
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
	 * @return Object array of size 2, first (Boolean) is true iff signwindow is
	 *         shown, second is the Table containing the form
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

			return new Object[] { new Boolean(false), table };

		}
		else {

			// Storing all fields set in this request
			Map fieldValues = new HashMap();
			Enumeration parameters = iwc.getRequest().getParameterNames();
			while (parameters.hasMoreElements()) {
				String name = (String) parameters.nextElement();
				if (name.startsWith(PARAMETER_TEXT_FIELD)) {
					String value = iwc.getParameter(name);
					if (value != null && value.length() != 0) {
						fieldValues.put(name.substring(PARAMETER_TEXT_FIELD.length()), iwc.getParameter(name));
						ChildCareApplication application = getBusiness().getChildCareContractArchiveHome().findApplicationByContract(((Integer) contract.getPrimaryKey()).intValue()).getApplication();
						if (name.equals(PARAMETER_TEXT_FIELD + "care-time")) {
							application.setCareTime(value);
							application.store();
						}
					}
				}
			}

			contract.setUnsetFields(fieldValues);
			ContractTagHome contractHome = (ContractTagHome) IDOLookup.getHome(ContractTag.class);
			Collection tags = contractHome.findAllByCategory(contract.getCategoryId().intValue());

			// create form for still unset fields
			table = getContractFieldsForm(contract.getUnsetFields(), tags);
			if (table != null && table.getRows() > 1) {
				return new Object[] { new Boolean(false), table };
			}
			else {
				// TODO: (Roar) Not working...
				((Window) getParentObject()).setWidth(700);
				((Window) getParentObject()).setHeight(400);
				return new Object[] { new Boolean(true), initSignContract(iwc) };
			}
		}
	}

	private Table getContractFieldsForm(Set fields, Collection tags) {
		Table table = null;
		if (fields.size() != 0) {
			int row = 1;
			Iterator i = fields.iterator();

			// loops through contract fields and add them to the form
			while (i.hasNext()) {
				String field = (String) i.next();
				if (!field.equalsIgnoreCase(FIELD_CURRENT_DATE)) { // /the currentdate
																														// field is given
																														// value when
																														// signing
					if (table == null) {
						table = new Table();
						table.setCellpadding(5);
						table.setWidth(Table.HUNDRED_PERCENT);
						table.setHeight(Table.HUNDRED_PERCENT);
						table.add(getHeader(localize("ccconsign_formHeading", "Please, fill out the contract fields")), 1, row++);
						table.mergeCells(1, 1, 2, 1);
					}

					Iterator itags = tags.iterator();
					TextInput input = new TextInput(PARAMETER_TEXT_FIELD + field);

					// search for tag with same name and look up type information
					while (itags.hasNext()) {
						ContractTag tag = (ContractTag) itags.next();
						if (tag.getName().equals(field)) {
							if (tag.getType() != null && tag.getType().equals(java.lang.Integer.class)) {
								input.setAsIntegers(localize("ccconsign_integer", "Use numbers only for " + field + "."));
								input.setAsNotEmpty(localize("ccconsign_integer_not_empty", "A number needs to be entered"));
								input.setMaxlength(2);
							}
						}
					}
					String fieldPrompt = field.substring(0, 1).toUpperCase() + field.substring(1).toLowerCase();
					table.add(getSmallHeader(localize("ccconsign_ " + fieldPrompt, fieldPrompt) + ":"), 1, row);
					table.add(getStyledInterface(input), 2, row);
					row++;
				}
			}

			if (table != null) {
				HiddenInput action = new HiddenInput(PARAMETER_METHOD, String.valueOf(METHOD_SIGN_CONTRACT));
				table.add(action, 1, 1);
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

	private Table initSignContract(IWContext iwc) {
		Contract contract = getContract(iwc);

		// Setting current date
		Map fields = new HashMap();
		final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
		fields.put(FIELD_CURRENT_DATE, dateFormat.format(new Date()));
		contract.setUnsetFields(fields);
		contract.store();

		iwc.setSessionAttribute(NBSSigningBlock.NBS_SIGNED_ENTITY, new NBSSignedEntity() {

			private Contract _contract = null;

			public Object init(Contract contract) {
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
		}.init(contract));

		Table table = new Table();

		Text heading = getLocalizedHeader("ccconsign_read_before_sign", "Read through the contrat and sign by using your BankId password. Then click OK.");
		table.add(heading, 1, 1);
		// table.setHeight(1, 1, Table.HUNDRED_PERCENT);
		table.setVerticalAlignment(1, 1, "MIDDLE");
		NBSSigningBlock nbsSigningBlock = new NBSSigningBlock();
		nbsSigningBlock.setParameter(PARAMETER_ACTION, "" + ACTION_SIGN_CONTRACT);
		nbsSigningBlock.setParameter(PARAMETER_METHOD, "" + METHOD_SIGN_CONTRACT);
		nbsSigningBlock.setParameter(PARAMETER_CONTRACT_ID, contract.getPrimaryKey().toString());
		table.add(nbsSigningBlock, 1, 2);
		CloseButton closeBtn = new CloseButton(localize("ccconsign_CANCEL", "avbryt"));
		closeBtn.setAsImageButton(true);
		table.add(closeBtn, 1, 3);
		table.setHeight(3, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(3, Table.VERTICAL_ALIGN_BOTTOM);
		return table;
	}

	private void processSignContract(IWContext iwc) throws Exception {
		NBSSigningBlock nbsSigningBlock = new NBSSigningBlock();
		nbsSigningBlock.processSignContract(iwc);

		ChildCareApplication application = getBusiness().getChildCareContractArchiveHome().findApplicationByContract(((Integer) getContract(iwc).getPrimaryKey()).intValue()).getApplication();

		User owner = application.getOwner();
		com.idega.core.user.data.User child = UserBusiness.getUser(application.getChildId());

		getBusiness().sendMessageToProvider(application, localize("ccecw_signcon_subject", "Contract signed"), owner.getName() + " " + localize("ccecw_signcon_body", " has signed the contract for ") + " " + child.getName() + " " + child.getPersonalID() + ".");

	}

	/**
	 * 
	 * @param iwc
	 * @return the contract specified by the ChildCareContractSigner_CONTRACT_ID
	 *         parameter, null if errors or no contract
	 */
	private Contract getContract(IWContext iwc) {
		int contractId;
		Contract contract = null;
		try {
			contractId = Integer.parseInt(iwc.getParameter(PARAMETER_CONTRACT_ID));
			contract = ((ContractHome) IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(contractId));
		}
		catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		catch (FinderException ex) {
			ex.printStackTrace();
		}
		catch (IDOLookupException ex) {
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

		if (iwc.isParameterSet(PARAMETER_SHOW_VACANCIES))
			_showVacancies = Boolean.valueOf(iwc.getParameter(PARAMETER_SHOW_VACANCIES)).booleanValue();

		if (iwc.isParameterSet(PARAMETER_SHOW_EMPLOYMENT_DROP))
			_showEmploymentDrop = Boolean.valueOf(iwc.getParameter(PARAMETER_SHOW_EMPLOYMENT_DROP)).booleanValue();
		if (iwc.isParameterSet(PARAMETER_SHOW_PRE_SCHOOL))
			_showPreSchool = Boolean.valueOf(iwc.getParameter(PARAMETER_SHOW_PRE_SCHOOL)).booleanValue();
	}

	private void alterCareTime(IWContext iwc) throws RemoteException {
		IWTimestamp validFrom = new IWTimestamp(iwc.getParameter(PARAMETER_CHANGE_DATE));
		String childCareTime = iwc.getParameter(PARAMETER_CHILDCARE_TIME);
		int employmentType = -1;
		if (iwc.isParameterSet(PARAMETER_EMPLOYMENT_TYPE))
			employmentType = Integer.parseInt(iwc.getParameter(PARAMETER_EMPLOYMENT_TYPE));

		int schoolTypeId = -1;
		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPES))
			schoolTypeId = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_TYPES));
		int schoolClassId = -1;
		if (iwc.isParameterSet(PARAMETER_SCHOOL_CLASS))
			schoolClassId = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_CLASS));
		int oldArchiveId = -1;
		if (iwc.isParameterSet("ccc_old_archive_id"))
			oldArchiveId = Integer.parseInt(iwc.getParameter("ccc_old_archive_id"));
		/*
		 * ChildCareApplication application =
		 * getBusiness().getApplication(_applicationID); ChildCareContract archive =
		 * getBusiness().getContractFile(application.getContractFileId());
		 * SchoolClassMember classMember = null; /* included in business
		 * assignContractToApplication if(archive!=null){ classMember =
		 * archive.getSchoolClassMember(); if(schoolTypeId !=
		 * classMember.getSchoolTypeId() || schoolClassId!=
		 * classMember.getSchoolClassId()){ // end old placement with the chosen
		 * date -1 and create new placement classMember =
		 * getBusiness().createNewPlacement(_applicationID,schoolTypeId,schoolClassId,validFrom,iwc.getCurrentUser()); } }
		 */

		// Add a control that says IF schooltypeid !=oldschooltypeid &&
		// schoolclassid == oldschoolclassid message: "You are trying to change the
		// school type but keep the child in the same group"
		// +
		// Add control that they in the future only can place in groups with correct
		// school type
		if (!getBusiness().isTryingToChangeSchoolTypeButNotSchoolClass(oldArchiveId, schoolTypeId, schoolClassId)) {
			if (getBusiness().isSchoolClassBelongingToSchooltype(schoolClassId, schoolTypeId)) {
				getBusiness().assignContractToApplication(_applicationID, oldArchiveId, childCareTime, validFrom, employmentType, iwc.getCurrentUser(), iwc.getCurrentLocale(), false, true, schoolTypeId, schoolClassId);
				close();
			}
			else {
				// add a message : "Chosen school group does not belong to chosen school
				// type"
				getParentPage().setAlertOnLoad(localize("child_care.warning.group_not_belonging_to_type", "Chosen school group does not belong to chosen school type"));
			}
		}
		else {
			// add a message : "You are trying to change the school type but keep the
			// child in the same group"
			getParentPage().setAlertOnLoad(localize("child_care.warning.change_school_type_but_keep_same_group", "You are trying to change the school type but keep the child in the same group"));
		}
	}

	private void alterValidFromDate(IWContext iwc) throws RemoteException, NoPlacementFoundException {
		IWTimestamp validFrom = new IWTimestamp(iwc.getParameter(PARAMETER_CHANGE_DATE));
		String careTime = iwc.getParameter(PARAMETER_CHILDCARE_TIME);

		int schoolTypeId = -1;
		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPES))
			schoolTypeId = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_TYPES));
		int schoolClassId = -1;
		if (iwc.isParameterSet(PARAMETER_SCHOOL_CLASS))
			schoolClassId = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_CLASS));

		getBusiness().alterValidFromDate(_applicationID, validFrom.getDate(), -1, schoolTypeId, schoolClassId, iwc.getCurrentLocale(), iwc.getCurrentUser());
		getBusiness().placeApplication(_applicationID, null, null, careTime, -1, -1, -1, iwc.getCurrentUser(), iwc.getCurrentLocale());

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}

	private void makeOffer(IWContext iwc) throws RemoteException {
		String messageHeader = localize("child_care.application_accepted_subject", "Child care application accepted.");
		String messageBody = iwc.getParameter(PARAMETER_OFFER_MESSAGE);
		User child = getBusiness().getUserBusiness().getUser(_userID);

		int pageID = -1;
		String theUrl = null;
		ICPage page = null;
		URLUtil url = new URLUtil(iwc.getServerURL());
		String linkName = null;
		String link = null;

		// get page to childcareOverview to add to the link in the childcare offer
		// message
		if (getBusiness().isAfterSchoolApplication(_applicationID)) {
			page = ChildCareAdminApplication.ascOverviewPage;
			linkName = localize("after_school_care.overview", "after school care overview");
		}
		else {
			page = ChildCareAdminApplication.ccOverviewPage;
			linkName = localize("child_care.overview", "childcare overview");
		}

		if (page != null)
			pageID = ((Integer) page.getPrimaryKey()).intValue();

		if (pageID != -1)
			url.setPage(pageID);

		if (pageID != -1) {
			url.addParameter("comm_child_id", child.getPrimaryKey().toString());
			theUrl = "javascript:openChildcareParentWindow(''" + url.toString() + "'');window.close();";
		}
		else {
			theUrl = "#";
		}
		// link which is set in makeOffer(iwc), $link$ is replaced by this
		link = "<a href=" + theUrl + " class=commune_SmallLink>" + linkName + "</a>";

		if (messageBody.indexOf("$datum$") != -1) {
			messageBody = TextSoap.findAndReplace(messageBody, "$datum$", "{4}");
		}
		if (messageBody.indexOf("$link$") != -1) {
			messageBody = TextSoap.findAndReplace(messageBody, "$link$", link);
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
		getBusiness().assignContractToApplication(_applicationID, -1, null, null, -1, iwc.getCurrentUser(), iwc.getCurrentLocale(), true);

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		close();
	}

	private void createContractForBankID(IWContext iwc) throws RemoteException {
		getBusiness().assignContractToApplication(_applicationID, -1, null, null, -1, iwc.getCurrentUser(), iwc.getCurrentLocale(), true);

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
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
		String childCareTime = iwc.getParameter(PARAMETER_CHILDCARE_TIME);
		int groupID = Integer.parseInt(iwc.getParameter(getSession().getParameterGroupID()));
		int typeID = Integer.parseInt(iwc.getParameter(getSession().getParameterSchoolTypeID()));
		int employmentType = -1;
		if (iwc.isParameterSet(PARAMETER_EMPLOYMENT_TYPE))
			employmentType = Integer.parseInt(iwc.getParameter(PARAMETER_EMPLOYMENT_TYPE));

		String subject = localize("child_care.placing_subject", "Your child placed in child care.");
		String body = localize("child_care.placing_body", "{0} has been placed in a group at {1}.");
		getBusiness().placeApplication(getSession().getApplicationID(), subject, body, childCareTime, groupID, typeID, employmentType, iwc.getCurrentUser(), iwc.getCurrentLocale());

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		close();
	}

	private void cancelContract(IWContext iwc) throws RemoteException {
		ChildCareApplication application = getBusiness().getApplicationForChildAndProvider(_userID, getSession().getChildCareID());
		if (application != null) {
			if (application.getApplicationStatus() == getBusiness().getStatusReady()) {
				boolean reason = true;
				if (iwc.isParameterSet(PARAMETER_CANCEL_REASON))
					reason = Boolean.valueOf(iwc.getParameter(PARAMETER_CANCEL_REASON)).booleanValue();
				IWTimestamp date = new IWTimestamp(iwc.getParameter(PARAMETER_CANCEL_DATE));
	
				String reasonMessage = "";
				if (reason)
					reasonMessage = localize("child_care.parental_leave", "Parental leave");
				else
					reasonMessage = localize("child_care.cancellation_other_reason", "Other reason");
	
				Object[] arguments = { "{0}", "{1}", reasonMessage, date.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT) };
	
				String subject = localize("child_care.cancel_contract_subject", "Your child care contract has been terminated.");
				String body = localize("child_care.cancel_contract_body", "Your contract for {0} at {1} has been terminated because of {2}. The termination will be active on {3}.");
	
				getBusiness().cancelContract(application, reason, date, reasonMessage, subject, MessageFormat.format(body, arguments), iwc.getCurrentUser());
				getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
				getParentPage().close();
			}
			else if (application.getApplicationStatus() == getBusiness().getStatusParentTerminated()) {
				IWTimestamp date = new IWTimestamp(iwc.getParameter(PARAMETER_CANCEL_DATE));
				getBusiness().createCancelForm(application, date.getDate(), iwc.getCurrentLocale());
			}
			else if (application.getApplicationStatus() == getBusiness().getStatusWaiting()) {
				IWTimestamp date = new IWTimestamp(iwc.getParameter(PARAMETER_CANCEL_DATE));
				application.setCancelConfirmationReceived(date.getDate());
				
				String reasonMessage = "";
				if (application.getParentalLeave())
					reasonMessage = localize("child_care.parental_leave", "Parental leave");
				else
					reasonMessage = localize("child_care.cancellation_other_reason", "Other reason");
	
				Object[] arguments = { "{0}", "{1}", reasonMessage, new IWTimestamp(application.getRequestedCancelDate()).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT) };
	
				String subject = localize("child_care.cancel_contract_subject", "Your child care contract has been terminated.");
				String body = localize("child_care.cancel_contract_body", "Your contract for {0} at {1} has been terminated because of {2}. The termination will be active on {3}.");
	
				getBusiness().cancelContract(application, application.getParentalLeave(), new IWTimestamp(application.getRequestedCancelDate()), reasonMessage, subject, MessageFormat.format(body, arguments), iwc.getCurrentUser());
				getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
				getParentPage().close();
			}
		}
	}

	private void moveToGroup(IWContext iwc) throws RemoteException {
		int groupID = Integer.parseInt(iwc.getParameter(getSession().getParameterGroupID()));
		getBusiness().moveToGroup(_placementID, groupID, iwc.getCurrentUser());

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}

	private void removeFutureContracts(IWContext iwc) throws RemoteException {
		getBusiness().removeFutureContracts(_applicationID);

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}

	private void updatePrognosis(IWContext iwc) throws RemoteException {
		int vacancies = -1;
		int threeMonths = Integer.parseInt(iwc.getParameter(PARAMETER_THREE_MONTHS_PROGNOSIS));
		int oneYear = Integer.parseInt(iwc.getParameter(PARAMETER_ONE_YEAR_PROGNOSIS));
		int threeMonthsPriority = Integer.parseInt(iwc.getParameter(PARAMETER_THREE_MONTHS_PRIORITY));
		int oneYearPriority = Integer.parseInt(iwc.getParameter(PARAMETER_ONE_YEAR_PRIORITY));
		int providerCapacity = Integer.parseInt(iwc.getParameter(PARAMETER_PROVIDER_CAPACITY));
		if (iwc.isParameterSet(PARAMETER_VACANCIES))
			vacancies = Integer.parseInt(iwc.getParameter(PARAMETER_VACANCIES));

		String providerComments = iwc.getParameter(PARAMETER_PROVIDER_COMMENTS);
		getBusiness().updatePrognosis(getSession().getChildCareID(), threeMonths, oneYear, threeMonthsPriority, oneYearPriority, providerCapacity, vacancies, providerComments);

		getSession().setHasPrognosis(true);
		getSession().setHasOutdatedPrognosis(false);

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}

	private void sendEndContractRequest(IWContext iwc) throws RemoteException {
		IWTimestamp stamp = new IWTimestamp(iwc.getParameter(PARAMETER_CHANGE_DATE));
		boolean parentalLeave = new Boolean(iwc.getParameter(PARAMETER_CANCEL_REASON)).booleanValue();

		ChildCareApplication application = getBusiness().getApplication(_applicationID);
		application.setApplicationStatus(getBusiness().getStatusParentTerminated());
		application.setCancelMessage(iwc.getParameter(PARAMETER_REJECT_MESSAGE));
		application.setRequestedCancelDate(stamp.getDate());
		application.setParentalLeave(parentalLeave);
		application.store();

		User owner = application.getOwner();
		com.idega.core.user.data.User child = UserBusiness.getUser(application.getChildId());
		String p = child.getPersonalID();
		String pnr = p.substring(2, 8) + "-" + p.substring(8, 12);
		getBusiness().sendMessageToParents(application, localize("ccecw_encon_par1", "Beg�ran om upps�gning av kontrakt gjord"), localize("ccecw_encon_par2", "Du har skickat en beg�ran om upps�gning av kontrakt f�r") + " " + child.getName() + " " + pnr + " " + localize("ccecw_encon_par3", "fr.o.m.") + " " + stamp.getDateString("yyyy-MM-dd") + ".");

		getBusiness().sendMessageToProvider(application, localize("ccecw_encon_prov1", "Upps�gning av kontrakt"), owner.getName() + " " + localize("ccecw_encon_prov2", "har beg�rt upps�gning av kontrakt f�r") + " " + child.getName() + " " + pnr + ". " + localize("ccecw_encon_prov3", "Kontraktet ska upph�ra fr.o.m.") + " " + stamp.getDateString("yyyy-MM-dd") + ".", application.getOwner());

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}

	private void sendNewCareTimeRequest(IWContext iwc) throws RemoteException {
		ChildCareApplication application = getBusiness().getApplication(_applicationID);
		User owner = application.getOwner();
		com.idega.core.user.data.User child = UserBusiness.getUser(application.getChildId());

		getBusiness().sendMessageToParents(application, localize("ccnctw_new_caretime_msg_parents_subject", "Beg�ran om �ndrad omsorgstid gjord"), localize("ccnctw_new_caretime_msg_parents_message", "Du har skickat en beg�ran om �ndrad omsorgstid f�r ") + child.getName() + " " + child.getPersonalID());

		getBusiness().sendMessageToProvider(application, localize("ccnctw_new_caretime_msg_provider_subject", "Beg�ran om �ndrad omsorgstid"), owner.getName() + " " + localize("ccnctw_new_caretime_msg_provider_message1", "har beg�rt �ndrad omsorgstid till") + " " + iwc.getParameter(PARAMETER_CHILDCARE_TIME) + " " + localize("ccnctw_new_caretime_msg_provider_message2", "tim/vecka f�r") + " " + child.getName() + " " + child.getPersonalID() + ". " + localize("ccnctw_new_caretime_msg_provider_message3", "Den nya omsorgstiden skall g�lla fr.o.m.") + " " + iwc.getParameter(PARAMETER_CHANGE_DATE) + ".", application.getOwner());

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

		Object[] arguments = { child.getName(), user.getName(), email, workphone, new IWTimestamp(application.getFromDate()).getLocaleDate(iwc.getCurrentLocale()), application.getProvider().getName() };
		return arguments;
	}

	private PlacementHelper getPlacementHelper() throws RemoteException {
		return getBusiness().getPlacementHelper(new Integer(_applicationID));
	}
	
	protected DropdownMenu getCareTimeMenu(String parameter) {
		DropdownMenu menu = super.getCareTimeMenu(parameter);
		menu.addMenuElementFirst("", localize("child_care.select_care_time", "Select care time"));
		return menu;
	}
	
	private String getSubmitCheckCareTimeScript(User child) {
		String childDate = child.getDateOfBirth().toString();
		String childYear = childDate.substring(0, 4);
		String emptyCareTimeMessage = localize("child_care.care_time_empty", "Care time must be selected.");
		String errorMessage = localize("child_care.care_time_not_valid_for_date", "Care time not valid for the selected date.");
		StringBuffer buffer = new StringBuffer();
		buffer.append("\nfunction checkCareTime(){\n\t");
		buffer.append("\n\t var message = '';");
		buffer.append("\n\t var childYear = " + childYear + ";");
		buffer.append("\n\t var dropDay = ").append("findObj('").append(PARAMETER_CHANGE_DATE + "_day").append("');");
		buffer.append("\n\t var dropMonth = ").append("findObj('").append(PARAMETER_CHANGE_DATE + "_month").append("');");
		buffer.append("\n\t var dropYear = ").append("findObj('").append(PARAMETER_CHANGE_DATE + "_year").append("');");
		buffer.append("\n\t var dateDay = ").append("parseInt(dropDay.options[dropDay.selectedIndex].value, 10);");
		buffer.append("\n\t var dateMonth = ").append("parseInt(dropMonth.options[dropMonth.selectedIndex].value, 10);");
		buffer.append("\n\t var dateYear = ").append("parseInt(dropYear.options[dropYear.selectedIndex].value, 10);");

		buffer.append("\n\n\t if (dateYear < 2000) {\n\t\t return true;\n\t }");

		buffer.append("\n\t var dropCareTime = ").append("findObj('").append(PARAMETER_CHILDCARE_TIME).append("');");
		buffer.append("\n\t var careTimeCode = ").append("dropCareTime.options[dropCareTime.selectedIndex].value;");

		buffer.append("\n\n\t if (careTimeCode == '') {");
		buffer.append("\n\t\t alert('" + emptyCareTimeMessage + "');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");

		buffer.append("\n\n\t if (careTimeCode == '" + CareTimeBMPBean.CODE_FSKHEL + "') {");
		buffer.append("\n\t\t var childYears = dateYear - childYear;");
		buffer.append("\n\t\t if (dateMonth > 8) {");
		buffer.append("\n\t\t\t childYears++;");
		buffer.append("\n\t\t } else if (dateMonth == 8 && dateDay >= 14) {");
		buffer.append("\n\t\t\t childYears++;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t\t if (childYears >= 4) {");
		buffer.append("\n\t\t\t message = '" + errorMessage + "';");
		buffer.append("\n\t\t }");
		buffer.append("\n\t }");

		buffer.append("\n\n\t else if (careTimeCode == '" + CareTimeBMPBean.CODE_FSKHEL4_5 + "') {");
		buffer.append("\n\t\t var childYears = dateYear - childYear;");
		buffer.append("\n\t\t if (dateMonth > 8) {");
		buffer.append("\n\t\t\t childYears++;");
		buffer.append("\n\t\t } else if (dateMonth == 8 && dateDay >= 15) {");
		buffer.append("\n\t\t\t childYears++;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t\t if (childYears < 4) {");
		buffer.append("\n\t\t\t message = '" + errorMessage + "';");
		buffer.append("\n\t\t }");
		buffer.append("\n\t }");

		buffer.append("\n\n\t else if (careTimeCode == '" + CareTimeBMPBean.CODE_FSKDEL4_5 + "') {");
		buffer.append("\n\t\t var childYears = dateYear - childYear;");
		buffer.append("\n\t\t if (dateMonth > 8) {");
		buffer.append("\n\t\t\t childYears++;");
		buffer.append("\n\t\t } else if (dateMonth == 8 && dateDay >= 15) {");
		buffer.append("\n\t\t\t childYears++;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t\t if (childYears < 4) {");
		buffer.append("\n\t\t\t message = '" + errorMessage + "';");
		buffer.append("\n\t\t }");
		buffer.append("\n\t }");

		buffer.append("\n\t if (message != '') {");
		buffer.append("\n\t\t alert(message);");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		
		buffer.append("\n\t return true;");

		buffer.append("\n }");

		return buffer.toString();
	}
}
