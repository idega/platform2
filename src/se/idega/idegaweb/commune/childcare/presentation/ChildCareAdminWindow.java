package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.text.MessageFormat;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

import com.idega.block.school.data.SchoolClass;
import com.idega.builder.business.BuilderLogic;
import com.idega.core.data.Email;
import com.idega.core.data.Phone;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
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
	public static final String PARAMETER_PRIORITY_MESSAGE = "cc_priority_message";
	public static final String PARAMETER_CHANGE_DATE = "cc_change_date";
	public static final String PARAMETER_CHILDCARE_TIME = "cc_childcare_time";
	public static final String PARAMETER_GROUP_NAME = "cc_group_name";
	public static final String PARAMETER_OLD_GROUP = "cc_old_group";

	private final static String USER_MESSAGE_SUBJECT = "child_care.application_received_subject";
	private final static String USER_MESSAGE_BODY = "child_care.application_received_body";

	public static final int METHOD_GRANT_PRIORITY = 2;
	public static final int METHOD_OFFER = 3;
	public static final int METHOD_CHANGE_DATE = 4;
	public static final int METHOD_CREATE_CONTRACT = 5;
	public static final int METHOD_CREATE_GROUP = 6;
	public static final int METHOD_PLACE_IN_GROUP = 7;
	public static final int METHOD_MOVE_TO_GROUP = 8;

	public static final int ACTION_CLOSE = 0;
	public static final int ACTION_GRANT_PRIORITY = 1;
	public static final int ACTION_OFFER = 2;
	public static final int ACTION_CHANGE_DATE = 3;
	public static final int ACTION_PARENTS_AGREE = 4;
	public static final int ACTION_CREATE_CONTRACT = 5;
	public static final int ACTION_CREATE_GROUP = 6;
	public static final int ACTION_PLACE_IN_GROUP = 7;
	public static final int ACTION_MOVE_TO_GROUP = 8;

	private int _method = -1;
	private int _action = -1;

	private int _userID = -1;
	private int _applicationID = -1;
	private int _pageID;

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
				contentTable.add(getChangeDateForm(iwc));
				break;
			case METHOD_PLACE_IN_GROUP :
				headerTable.add(getHeader(localize("child_care.place_in_group", "Place in group")));
				contentTable.add(getPlaceInGroupForm(iwc));
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
				contentTable.add(getCreateGroupForm(iwc));
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
		textArea.setHeight(7);
		textArea.setAsNotEmpty(localize("child_care.priority_message_required","You must fill in the message."));

		table.add(getSmallHeader(localize("child_care.priority_message_info", "The following message will be sent to BUN.")), 1, row++);
		table.add(textArea, 1, row++);

		SubmitButton grantPriority = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.grant_priority", "Grant priority"), PARAMETER_ACTION, String.valueOf(ACTION_GRANT_PRIORITY)));
		table.add(grantPriority, 1, row);
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

		String message = MessageFormat.format(localize("child_care.offer_message", "We can offer {0} a placing in our childcare from {4}.\n\nRegards,\n{1}\n{2}\n{3}"), getArguments(iwc));
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

	private Table getChangeDateForm(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		ChildCareApplication application = getBusiness().getApplication(_applicationID);

		DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_CHANGE_DATE));
		if (application != null)
			dateInput.setDate(application.getFromDate());

		table.add(getSmallHeader(localize("child_care.new_date", "Select the new placement date")), 1, row++);
		table.add(dateInput, 1, row++);

		SubmitButton reject = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.change_date", "Change date"), PARAMETER_ACTION, String.valueOf(ACTION_CHANGE_DATE)));
		table.add(reject, 1, row);
		table.add(Text.NON_BREAKING_SPACE, 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getLetterForm(IWContext iwc) {
		return null;
	}

	private Table getPlaceInGroupForm(IWContext iwc) throws RemoteException {
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
		table.add(Text.NON_BREAKING_SPACE, 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}

	private Table getCreateGroupForm(IWContext iwc) throws RemoteException {
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
		table.add(Text.NON_BREAKING_SPACE, 1, row);
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
		table.add(Text.NON_BREAKING_SPACE, 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
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
	
	private void makeOffer(IWContext iwc) throws RemoteException {
		String messageHeader = localize("child_care.application_accepted_subject", "Child care application accepted.");
		String messageBody = iwc.getParameter(PARAMETER_OFFER_MESSAGE);
		getBusiness().acceptApplication(_applicationID, messageHeader, messageBody, iwc.getCurrentUser());

		close(iwc);
	}
	
	private void changeDate(IWContext iwc) throws RemoteException {
		String placingDate = iwc.getParameter(PARAMETER_CHANGE_DATE);
		IWTimestamp stamp = new IWTimestamp(placingDate);
		getBusiness().changePlacingDate(_applicationID, stamp.getDate());

		close(iwc);
	}
	
	private void grantPriority(IWContext iwc) throws RemoteException {
		String messageHeader = localize("child_care.priority_subject", "Child care application priority.");
		String messageBody = iwc.getParameter(PARAMETER_PRIORITY_MESSAGE);
		getBusiness().setAsPriorityApplication(_applicationID, messageHeader, messageBody);

		close(iwc);
	}
	
	private void parentsAgree(IWContext iwc) throws RemoteException {
		String subject = localize("child_care.parents_agree_subject", "Parents accept placing offer.");
		String message = localize("child_care.parents_agree_body", "The parents of {0} accept your offer for a placing in {1} from {2}.");
		
		getBusiness().parentsAgree(_applicationID, iwc.getCurrentUser(), subject, message);

		close(iwc);
	}
	
	private void createContract(IWContext iwc) throws RemoteException {
		getBusiness().assignContractToApplication(_applicationID, -1, iwc.getCurrentUser(), iwc.getCurrentLocale());

		close(iwc);
	}
	
	private void createGroup(IWContext iwc) throws RemoteException {
		String groupName = iwc.getParameter(PARAMETER_GROUP_NAME);
		getBusiness().getSchoolBusiness().storeSchoolClass(getSession().getGroupID(), groupName, getSession().getChildCareID(), -1, -1, -1);

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}
	
	private void placeInGroup(IWContext iwc) throws RemoteException {
		int childCareTime = Integer.parseInt(iwc.getParameter(PARAMETER_CHILDCARE_TIME));
		int groupID = Integer.parseInt(iwc.getParameter(getSession().getParameterGroupID()));
		getBusiness().placeApplication(getSession().getApplicationID(), "", "", childCareTime, groupID, iwc.getCurrentUser());

		close(iwc);
	}
	
	private void moveToGroup(IWContext iwc) throws RemoteException {
		int groupID = Integer.parseInt(iwc.getParameter(getSession().getParameterGroupID()));
		getBusiness().moveToGroup(_userID, getSession().getChildCareID(), groupID);

		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}
	
	private void close(IWContext iwc) {
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
