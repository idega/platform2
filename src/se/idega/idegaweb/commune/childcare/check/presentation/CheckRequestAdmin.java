package se.idega.idegaweb.commune.childcare.check.presentation;

import is.idega.idegaweb.member.business.MemberFamilyLogic;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;

import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.check.data.Check;
import se.idega.idegaweb.commune.presentation.CitizenChildren;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.school.data.SchoolType;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.PersonalIDFormatter;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class CheckRequestAdmin extends CommuneBlock {

	protected final static int ACTION_VIEW_CHECK_LIST = 1;
	protected final static int ACTION_VIEW_CHECK = 2;
	protected final static int ACTION_GRANT_CHECK = 3;
	protected final static int ACTION_RETRIAL_CHECK = 4;
	protected final static int ACTION_SAVE_CHECK = 5;

	protected final static String PARAM_TYPE = "chk_type";
	protected final static String PARAM_VIEW_CHECK_LIST = "chk_v_c_l";
	protected final static String PARAM_VIEW_CHECK = "chk_view_check";
	protected final static String PARAM_GRANT_CHECK = "chk_grant_check";
	protected final static String PARAM_RETRIAL_CHECK = "chk_retrial_check";
	protected final static String PARAM_SAVE_CHECK = "chk_save_check";
	protected final static String PARAM_CHECK_ID = "chk_check_id";
	protected final static String PARAM_RULE = "chk_rule";
	protected final static String PARAM_NOTES = "chk_notes";
	protected final static String PARAM_USER_NOTES = "chk_user_notes";

	public CheckRequestAdmin() {
	}

	public void main(IWContext iwc) {
		this.setResourceBundle(getResourceBundle(iwc));

		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_VIEW_CHECK_LIST :
					viewCheckList(iwc);
					break;
				case ACTION_VIEW_CHECK :
					int checkId = Integer.parseInt(iwc.getParameter(PARAM_CHECK_ID));
					Check check = getCheckBusiness(iwc).getCheck(checkId);
					viewCheck(iwc, check, false);
					break;
				case ACTION_GRANT_CHECK :
					grantCheck(iwc);
					break;
				case ACTION_RETRIAL_CHECK :
					retrialCheck(iwc);
					break;
				case ACTION_SAVE_CHECK :
					saveCheck(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	protected int parseAction(IWContext iwc) {
		int action = getDefaultView();

		if (iwc.isParameterSet(PARAM_VIEW_CHECK)) {
			action = ACTION_VIEW_CHECK;
		}

		if (iwc.isParameterSet(PARAM_GRANT_CHECK)) {
			action = ACTION_GRANT_CHECK;
		}

		if (iwc.isParameterSet(PARAM_RETRIAL_CHECK)) {
			action = ACTION_RETRIAL_CHECK;
		}

		if (iwc.isParameterSet(PARAM_SAVE_CHECK)) {
			action = ACTION_SAVE_CHECK;
		}

		return action;
	}

	private void viewCheckList(IWContext iwc) throws Exception {
		Table table = new Table();
		table.setColumns(5);
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		int row = 1;
		
		table.setRowColor(row, getHeaderColor());
		table.add(getSmallHeader(localize("check.check_number", "Check number")), 1, row);
		table.add(getSmallHeader(localize("check.date", "Date")), 2, row);
		table.add(getSmallHeader(localize("check.social_security_number", "Social security number")), 3, row);
		table.add(getSmallHeader(localize("check.manager", "Manager")), 4, row);
		table.add(getSmallHeader(localize("check.status", "Status")), 5, row++);

		Check check;
		User child;
		User manager;
		IWCalendar calendar;

		Collection checks = getCheckBusiness(iwc).findUnhandledChecks();
		Iterator iter = checks.iterator();
		while (iter.hasNext()) {
			check = (Check) iter.next();
			child = getCheckBusiness(iwc).getUserById(check.getChildId());
			manager = getCheckBusiness(iwc).getUserById(check.getManagerId());
			calendar = new IWCalendar(iwc.getCurrentLocale(),check.getCreated());

			String childSSN = "-";
			if (child != null) {
				childSSN = PersonalIDFormatter.format(child.getPersonalID(), iwc.getApplication().getSettings().getApplicationLocale());
			}

			String managerName = "-";
			if (manager != null)
				managerName = manager.getName();
				
			String caseStatus = getCheckBusiness(iwc).getLocalizedCaseStatusDescription(check.getCaseStatus(), iwc.getCurrentLocale());

			if (row % 2 == 0)
				table.setRowColor(row, getZebraColor1());
			else
				table.setRowColor(row, getZebraColor2());
			
			Link link = getSmallLink(check.getPrimaryKey().toString());
			link.addParameter(PARAM_VIEW_CHECK, "true");
			link.addParameter(PARAM_CHECK_ID, check.getPrimaryKey().toString());
			table.add(link, 1, row);
			table.add(getSmallText(calendar.getLocaleDate(IWCalendar.SHORT)), 2, row);
			table.add(getSmallText(childSSN), 3, row);
			table.add(getSmallText(managerName), 4, row);
			table.add(getSmallText(caseStatus), 5, row++);
		}
		add(table);
	}

	protected void viewCheck(IWContext iwc, Check check, boolean isError) throws Exception {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		int row = 1;
		
		table.add(getCheckInfoTable(iwc, check), 1, row++);
		table.setHeight(row++, 16);

		if (isError) {
			table.add(getSmallErrorText(localize("check.must_check_all_rules", "All rules must be checked.")), 1, row);
			table.setHeight(row++, 6);
		}

		table.add(getCheckForm(iwc, check, isError), 1, row);
		add(table);
	}

	private Table getCheckInfoTable(IWContext iwc, Check check) throws Exception {
		Table checkInfoTable = new Table();
		checkInfoTable.setCellpadding(getCellpadding());
		checkInfoTable.setCellspacing(0);
		checkInfoTable.setWidth(1, "150");
		int row = 1;

		if (check != null) {
			checkInfoTable.add(getLocalizedSmallHeader("check.case_number", "Case number"), 1, row);
			checkInfoTable.add(getSmallHeader(":"), 1, row);
			String number = check.getPrimaryKey().toString();
			checkInfoTable.add(getSmallText(number), 2, row++);

			SchoolType type = getCheckBusiness(iwc).getSchoolType(check.getChildCareType());
			if (type != null) {
				checkInfoTable.add(getLocalizedSmallHeader("check.request_regarding", "Request regarding"), 1, row);
				checkInfoTable.add(getSmallHeader(":"), 1, row);
				checkInfoTable.add(getSmallText(type.getSchoolTypeName()), 2, row++);
			}
		}

		User child = null;
		if (check != null) {
			child = getCheckBusiness(iwc).getUserById(check.getChildId());
		}
		else {
			child = getChild(iwc);
		}

		if (child != null) {
			checkInfoTable.add(getLocalizedSmallHeader("check.child", "Child"), 1, row);
			checkInfoTable.add(getSmallHeader(":"), 1, row);

			String childSSN = PersonalIDFormatter.format(child.getPersonalID(), iwc.getApplication().getSettings().getApplicationLocale());

			checkInfoTable.add(getSmallText(childSSN + ", " + child.getName()), 2, row++);
			Collection addresses = child.getAddresses();
			Address address = null;
			PostalCode zip = null;
			Iterator iter = addresses.iterator();
			while (iter.hasNext()) {
				address = (Address) iter.next();
				zip = address.getPostalCode();
				break;
			}
			if (address != null) {
				checkInfoTable.add(getLocalizedSmallHeader("check.address", "Address"), 1, row);
				checkInfoTable.add(getSmallHeader(":"), 1, row);
				checkInfoTable.add(getSmallText(address.getStreetAddress()), 2, row);
				if (zip != null) {
					checkInfoTable.add(getSmallText(", " + zip.getPostalAddress()), 2, row);
				}
			}

			Collection custodians = getMemberFamilyLogic(iwc).getCustodiansFor(child);
			if (custodians != null && custodians.size() > 0) {
				checkInfoTable.add(getLocalizedSmallHeader("check.custodians", "Custodians"), 1, row + 1);
				checkInfoTable.add(getSmallHeader(":"), 1, row + 1);
				checkInfoTable.setVerticalAlignment(1, row + 1, Table.VERTICAL_ALIGN_TOP);

				Iterator iter2 = custodians.iterator();
				int count = 1;
				while (iter2.hasNext()) {
					User parent = (User) iter2.next();
					checkInfoTable.add(getSmallText(parent.getNameLastFirst(true)), 2, ++row);
					checkInfoTable.add(getSmallText(" - " + PersonalIDFormatter.format(parent.getPersonalID(), iwc.getCurrentLocale())), 2, row);
					if (check != null && getWorkSituation(check.getWorkSituation1()) != null) {
						if (count == 1 && getWorkSituation(check.getWorkSituation1()) != null)
							checkInfoTable.add(getSmallText(", " + getWorkSituation(check.getWorkSituation1())), 2, row);
						if (count == 2 && getWorkSituation(check.getWorkSituation2()) != null)
							checkInfoTable.add(getSmallText(", " + getWorkSituation(check.getWorkSituation2())), 2, row);
					}
				}
			}
		}

		row++;

		/*if (check != null && check.getMotherToungueMotherChild() != null) {
			checkInfoTable.add(getLocalizedSmallHeader("check.language_mother_child", "Language mother-child"), 1, row);
			checkInfoTable.add(getSmallHeader(":"), 1, row);
			checkInfoTable.add(getSmallText(check.getMotherToungueMotherChild()), 2, row++);
		}
		if (check != null && check.getMotherToungueFatherChild() != null) {
			checkInfoTable.add(getLocalizedSmallHeader("check.language_father_child", "Language father-child"), 1, row);
			checkInfoTable.add(getSmallHeader(":"), 1, row);
			checkInfoTable.add(getSmallText(check.getMotherToungueFatherChild()), 2, row++);
		}
		if (check != null && check.getMotherToungueParents() != null) {
			checkInfoTable.add(getLocalizedSmallHeader("check.language_parents", "Language parents"), 1, row);
			checkInfoTable.add(getSmallHeader(":"), 1, row);
			checkInfoTable.add(getSmallText(check.getMotherToungueParents()), 2, row);
		}*/

		return checkInfoTable;
	}

	private Form getCheckForm(IWContext iwc, Check check, boolean isError) throws Exception {
		Form f = new Form();
		if (check != null) {
			f.addParameter(PARAM_CHECK_ID, check.getPrimaryKey().toString());
		}

		User user = getChild(iwc);
		if (user != null) {
			f.add(new HiddenInput(CitizenChildren.getChildIDParameterName(), user.getPrimaryKey().toString()));
		}

		Table frame = new Table(1, 5);
		frame.setCellpadding(getCellpadding());
		frame.setCellspacing(0);
		frame.setWidth(Table.HUNDRED_PERCENT);
		
		frame.add(getLocalizedSmallHeader("check.requirements", "Requirements"), 1, 1);
		frame.add(new Break());

		Table ruleTable = new Table(2, 3);
		ruleTable.setCellpadding(getCellpadding());
		ruleTable.setCellspacing(0);

		boolean rule3 = false;
		boolean rule4 = false;
		boolean rule5 = false;

		if (check != null) {
			rule3 = check.getRule3();
			rule4 = check.getRule4();
			rule5 = check.getRule5();
		}

		ruleTable.add(getCheckBox("3", rule3), 1, 1);
		ruleTable.add(getRuleText(localize("check.work_situation_approved", "Work situation approved"), rule3, isError), 2, 1);

		ruleTable.add(getCheckBox("4", rule4), 1, 2);
		ruleTable.add(getRuleText(localize("check.dept_control", "Skuldkontroll"), rule4, isError), 2, 2);

		ruleTable.add(getCheckBox("5", rule5), 1, 3);
		ruleTable.add(getRuleText(localize("check.need_for_special_support", "Need for special support"), rule5, isError), 2, 3);

		frame.add(ruleTable, 1, 1);

		frame.add(getLocalizedSmallHeader("check.notes", "Notes"), 1, 2);
		frame.add(new Break(), 1, 2);
		TextArea notes = (TextArea) getStyledInterface(new TextArea(PARAM_NOTES));
		if (check != null && check.getNotes() != null)
			notes.setValue(check.getNotes());
		notes.setRows(4);
		notes.setColumns(65);
		frame.add(notes, 1, 2);


		frame.add(getLocalizedSmallHeader("check.user_notes", "User notes"), 1, 3);
		frame.add(new Break(), 1, 3);
		TextArea userNotes = (TextArea) getStyledInterface(new TextArea(PARAM_USER_NOTES));
		if (check != null && check.getUserNotes() != null)
			userNotes.setValue(check.getUserNotes());
		userNotes.setRows(4);
		userNotes.setColumns(65);
		frame.add(userNotes, 1, 3);

		frame.setHeight(4, 6);
		frame.add(getSubmitButtonTable(), 1, 5);

		f.add(frame);

		return f;
	}

	/**
	 * A method to get a checkbox for a certain rule.
	 * <br>
	 * @param String ruleNumber, the number of the rule
	 * @param boolean checked, true if rule is checked
	 * @return CheckBox for the given rule
	 */
	private CheckBox getCheckBox(String ruleNumber, boolean checked) {
		CheckBox rule = getCheckBox(PARAM_RULE, ruleNumber);
		rule.setChecked(checked);
		return rule;
	}

	private Text getRuleText(String ruleText, boolean ruleChecked, boolean isError) {
		if (ruleChecked || !isError) {
			return getSmallText(ruleText);
		}
		else {
			return getSmallErrorText(ruleText);
		}
	}

	private Table getSubmitButtonTable() {
		Table submitTable = new Table(5, 1);
		submitTable.setWidth(2, "3");
		submitTable.setWidth(4, "3");
		submitTable.setCellpaddingAndCellspacing(0);

		SubmitButton grantButton = (SubmitButton) getButton(new SubmitButton(localize("check.grant_check", "Grant check"), PARAM_GRANT_CHECK, "true"));
		submitTable.add(grantButton, 1, 1);

		SubmitButton retrialButton = (SubmitButton) getButton(new SubmitButton(localize("check.retrial", "Retrial"), PARAM_RETRIAL_CHECK, "true"));
		submitTable.add(retrialButton, 3, 1);

		SubmitButton saveButton = (SubmitButton) getButton(new SubmitButton(localize("check.save", "Save"), PARAM_SAVE_CHECK, "true"));
		submitTable.add(saveButton, 5, 1);

		return submitTable;
	}

	private Check verifyCheckRules(IWContext iwc) throws Exception {
		int checkId = Integer.parseInt(iwc.getParameter(PARAM_CHECK_ID));
		String[] selectedRules = iwc.getParameterValues(PARAM_RULE);
		String notes = iwc.getParameter(PARAM_NOTES);
		String userNotes = iwc.getParameter(PARAM_USER_NOTES);
		//    int managerId = iwc.getUser().getID();
		return getCheckBusiness(iwc).saveCheckRules(checkId, selectedRules, notes, userNotes, iwc.getCurrentUser());
	}

	private void grantCheck(IWContext iwc) throws Exception {
		Check check = verifyCheckRules(iwc);
		if (!getCheckBusiness(iwc).allRulesVerified(check)) {
			//      this.errorMessage = localize("check.must_check_all_rules","All rules must be checked.");
			//      this.isError = true;
			viewCheck(iwc, check, true);
			return;
		}
		
		StringBuffer subject = new StringBuffer(getResourceBundle(iwc).getLocalizedString("check.granted_message_headline", "Check granted"));
		User child = getChild(iwc,check.getChildId());
		if (child != null) {
			subject.append(" ");
			subject.append(getResourceBundle(iwc).getLocalizedString("check.for","for"));
			subject.append(" ");
			subject.append(child.getName());
		}
		String body = getResourceBundle(iwc).getLocalizedString("check.granted_message_body", "Your check has been granted");
		
		try {
			getCheckBusiness(iwc).approveCheck(check, subject.toString(), body, iwc.getCurrentUser());
			add(getText(getResourceBundle(iwc).getLocalizedString("check.check_granted", "Check granted") + ": " + ((Integer) check.getPrimaryKey()).toString()));
			add(new Break(2));
			viewCheckList(iwc);
		}
		catch (CreateException e) {
			e.printStackTrace();
			add(getText(getResourceBundle(iwc).getLocalizedString("check.check_already_granted", "Child already has granted check") + ": " + ((Integer) check.getPrimaryKey()).toString()));
			add(new Break(2));
			viewCheckList(iwc);
		}

	}

	private void retrialCheck(IWContext iwc) throws Exception {
		Check check = verifyCheckRules(iwc);
		StringBuffer subject = new StringBuffer(getResourceBundle(iwc).getLocalizedString("check.retrial_message_headline", "Check denied"));
		User child = getChild(iwc,check.getChildId());
		if (child != null) {
			subject.append(" ");
			subject.append(getResourceBundle(iwc).getLocalizedString("check.for","for"));
			subject.append(" ");
			subject.append(child.getName());
		}
		String body = getResourceBundle(iwc).getLocalizedString("check.retrial_message_body", "Your check has been denied");
		getCheckBusiness(iwc).retrialCheck(check, subject.toString(), body, iwc.getCurrentUser());

		viewCheckList(iwc);
	}

	/**
	 * A method that saves the current check.
	 * @param IWContext iwc
	 * @throws Exception
	 */
	private void saveCheck(IWContext iwc) throws Exception {
		verifyCheckRules(iwc);
		viewCheckList(iwc);
	}

	protected User getChild(IWContext iwc, int child_id) {
		try {
			return getCheckBusiness(iwc).getUserById(child_id);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	protected User getChild(IWContext iwc) {
		if (iwc.isParameterSet(CitizenChildren.getChildIDParameterName())) {
			try {
				return getCheckBusiness(iwc).getUserById(Integer.parseInt(iwc.getParameter(CitizenChildren.getChildIDParameterName())));
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
				return null;
			}
		}
		if (iwc.isParameterSet(CitizenChildren.getChildSSNParameterName())) {
			try {
				return getCheckBusiness(iwc).getUserByPersonalId(iwc.getParameter(CitizenChildren.getChildSSNParameterName()));
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
				return null;
			}
		}
		return null;
	}

	private String getWorkSituation(int workSituation) {
		String returnString = "";
		switch (workSituation) {
			case 1 :
				returnString = localize("check.working", "Working");
				break;
			case 2 :
				returnString = localize("check.studying", "Studying");
				break;
			case 3 :
				returnString = localize("check.seeking_work", "Seeking work");
				break;
			case 4 :
				returnString = localize("check.parental_leave", "Parental leave");
				break;
			default :
				returnString = null;
		}
		return returnString;
	}
	
	private int getDefaultView() {
		return ACTION_VIEW_CHECK_LIST;
	}

	protected CheckBusiness getCheckBusiness(IWContext iwc) throws Exception {
		return (CheckBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CheckBusiness.class);
	}

	protected MemberFamilyLogic getMemberFamilyLogic(IWContext iwc) throws Exception {
		return (MemberFamilyLogic) com.idega.business.IBOLookup.getServiceInstance(iwc, MemberFamilyLogic.class);
	}
}