package se.idega.idegaweb.commune.childcare.check.presentation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import com.idega.block.school.data.SchoolType;
import is.idega.idegaweb.member.business.MemberFamilyLogic;
import com.idega.core.data.PostalCode;
import com.idega.core.data.Address;
import com.idega.user.data.User;
import java.util.*;

import se.idega.idegaweb.commune.presentation.*;
import se.idega.idegaweb.commune.childcare.check.data.*;
import se.idega.idegaweb.commune.childcare.check.business.*;

import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.builder.data.IBPage;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class CheckRequestAdmin extends CommuneBlock {

	private final static int ACTION_VIEW_CHECK_LIST = 1;
	private final static int ACTION_VIEW_CHECK = 2;
	private final static int ACTION_GRANT_CHECK = 3;
	private final static int ACTION_RETRIAL_CHECK = 4;
	private final static int ACTION_SAVE_CHECK = 5;

	private final static String PARAM_VIEW_CHECK_LIST = "chk_v_c_l";
	private final static String PARAM_VIEW_CHECK = "chk_view_check";
	private final static String PARAM_GRANT_CHECK = "chk_grant_check";
	private final static String PARAM_RETRIAL_CHECK = "chk_retrial_check";
	private final static String PARAM_SAVE_CHECK = "chk_save_check";
	private final static String PARAM_CHECK_ID = "chk_check_id";
	private final static String PARAM_RULE = "chk_rule";
	private final static String PARAM_NOTES = "chk_notes";

	public CheckRequestAdmin() {}

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
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	private int parseAction(IWContext iwc) {
		int action = ACTION_VIEW_CHECK_LIST;

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
		ColumnList checkList = new ColumnList(5);
		checkList.setWidth("600");
		checkList.setHeader(localize("check.check_number", "Check number"), 1);
		checkList.setHeader(localize("check.date", "Date"), 2);
		checkList.setHeader(localize("check.social_security_number", "Social security number"), 3);
		checkList.setHeader(localize("check.manager", "Manager"), 4);
		checkList.setHeader(localize("check.status", "Status"), 5);

		Collection checks = getCheckBusiness(iwc).findUnhandledChecks();
		Iterator iter = checks.iterator();
		while (iter.hasNext()) {
			Check check = (Check) iter.next();
			User child = getCheckBusiness(iwc).getUserById(iwc, check.getChildId());
			User manager = getCheckBusiness(iwc).getUserById(iwc, check.getManagerId());

			String childSSN = "-";
			if (child != null)
				childSSN = child.getPersonalID();
			String managerName = "-";
			if (manager != null)
				managerName = manager.getName();

			Link l = getLink(check.getPrimaryKey().toString());
			l.addParameter(PARAM_VIEW_CHECK, "true");
			l.addParameter(PARAM_CHECK_ID, check.getPrimaryKey().toString());
			checkList.add(l);
			checkList.add(check.getCreated().toString().substring(0, 10));
			checkList.add(childSSN);
			checkList.add(managerName);
			checkList.add(check.getStatus());
		}
		add(checkList);
	}

	private void viewCheck(IWContext iwc, Check check, boolean isError) throws Exception {
		add(getCheckInfoTable(iwc, check));
		add(new Break(2));

		if (isError) {
			add(getErrorText(localize("check.must_check_all_rules", "All rules must be checked.")));
			add(new Break(2));
		}

		add(getCheckForm(iwc, check, isError));
	}

	private Table getCheckInfoTable(IWContext iwc, Check check) throws Exception {
		Table frame = new Table();
		frame.setCellpadding(10);
		frame.setCellspacing(0);
		frame.setColor("#ffffcc");

		Table checkInfoTable = new Table(2, 8);
		checkInfoTable.setCellpadding(6);
		checkInfoTable.setCellspacing(0);
		int row = 1;

		checkInfoTable.add(getLocalizedSmallHeader("check.case_number", "Case number"), 1, row);
		checkInfoTable.add(getSmallHeader(":"), 1, row);

		String number = check.getPrimaryKey().toString();
		/*long checkNumber = ((Long) new Long(check.getPrimaryKey().toString())).longValue();
		checkNumber = 666;

		DecimalFormat format = new DecimalFormat("0000000");
		String number = format.format(checkNumber);*/
		checkInfoTable.add(getSmallText(number), 2, row++);

		checkInfoTable.add(getLocalizedSmallHeader("check.request_regarding", "Request regarding"), 1, row);
		checkInfoTable.add(getSmallHeader(":"), 1, row);
		SchoolType type = getCheckBusiness(iwc).getSchoolType(iwc, check.getChildCareType());
		if (type != null)
			checkInfoTable.add(getSmallText(type.getSchoolTypeName()), 2, row);
		row++;

		checkInfoTable.add(getLocalizedSmallHeader("check.child", "Child"), 1, row);
		checkInfoTable.add(getSmallHeader(":"), 1, row++);
		checkInfoTable.add(getLocalizedSmallHeader("check.custodians", "Custodians"), 1, row);
		checkInfoTable.add(getSmallHeader(":"), 1, row);
		checkInfoTable.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);

		User child = getCheckBusiness(iwc).getUserById(iwc, check.getChildId());
		if (child != null) {
			--row;
			checkInfoTable.add(getSmallText(child.getPersonalID() + ", " + child.getName()), 2, row);
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
				checkInfoTable.add(getSmallText(", " + address.getStreetName() + " " + address.getStreetNumber()), 2, row);
				if (zip != null) {
					checkInfoTable.add(getSmallText(" " + zip.getPostalCode()), 2, row);
				}
			}

			Collection custodians = getMemberFamilyLogic(iwc).getCustodiansFor(child);
			Iterator iter2 = custodians.iterator();
			while (iter2.hasNext()) {
				User parent = (User) iter2.next();
				checkInfoTable.add(getSmallText(parent.getNameLastFirst(false)), 2, ++row);
				//", 08-633 54 37, Studerande, Gift"),2,4);
			}
		}

		row++;
		checkInfoTable.add(getLocalizedSmallHeader("check.language_mother_child", "Language mother-child"), 1, row);
		checkInfoTable.add(getSmallHeader(":"), 1, row);
		checkInfoTable.add(getSmallText(check.getMotherToungueMotherChild()), 2, row++);
		checkInfoTable.add(getLocalizedSmallHeader("check.language_father_child", "Language father-child"), 1, row);
		checkInfoTable.add(getSmallHeader(":"), 1, row);
		checkInfoTable.add(getSmallText(check.getMotherToungueFatherChild()), 2, row++);
		checkInfoTable.add(getLocalizedSmallHeader("check.language_parents", "Language parents"), 1, row);
		checkInfoTable.add(getSmallHeader(":"), 1, row);
		checkInfoTable.add(getSmallText(check.getMotherToungueParents()), 2, row);

		frame.add(checkInfoTable);

		return frame;
	}

	private Form getCheckForm(IWContext iwc, Check check, boolean isError) throws Exception {
		Form f = new Form();
		f.addParameter(PARAM_CHECK_ID, check.getPrimaryKey().toString());

		Table frame = new Table(2, 1);
		frame.setCellpadding(14);
		frame.setCellspacing(0);
		frame.setColor(getBackgroundColor());
		frame.setVerticalAlignment(2, 1, "top");
		frame.add(getLocalizedSmallText("check.requirements", "Requirements"), 1, 1);
		frame.add(new Break(2));

		Table ruleTable = new Table(2, 5);
		ruleTable.setCellpadding(4);
		ruleTable.setCellspacing(0);
		
		ruleTable.add(getCheckBox("1", check.getRule1()), 1, 1);
		ruleTable.add(getRuleText(localize("check.nationally_registered", "Nationally registered"), check.getRule1(), isError), 2, 1);

		ruleTable.add(getCheckBox("2", check.getRule2()), 1, 2);
		ruleTable.add(getRuleText(localize("check.child_one_year", "Child one year of age"), check.getRule2(), isError), 2, 2);

		ruleTable.add(getCheckBox("3", check.getRule3()), 1, 3);
		ruleTable.add(getRuleText(localize("check.work_situation_approved", "Work situation approved"), check.getRule3(), isError), 2, 3);

		ruleTable.add(getCheckBox("4", check.getRule4()), 1, 4);
		ruleTable.add(getRuleText(localize("check.dept_control", "Skuldkontroll"), check.getRule4(), isError), 2, 4);

		ruleTable.add(getCheckBox("5", check.getRule5()), 1, 5);
		ruleTable.add(getRuleText(localize("check.need_for_special_support", "Need for special support"), check.getRule5(), isError), 2, 5);

		frame.add(ruleTable, 1, 1);
		frame.add(new Break(2), 1, 1);
		frame.add(getSubmitButtonTable(), 1, 1);
		
		frame.add(getLocalizedSmallText("check.notes", "Notes"), 2, 1);
		frame.add(new Break(2), 2, 1);
		TextArea notes = new TextArea(PARAM_NOTES);
		notes.setValue(check.getNotes());
		notes.setHeight(8);
		notes.setWidth(50);
		frame.add(notes, 2, 1);
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
		CheckBox rule = new CheckBox(PARAM_RULE, ruleNumber);
		rule.setChecked(checked);
		return rule;
	}

	private Text getRuleText(String ruleText, boolean ruleChecked, boolean isError) {
		if (ruleChecked || !isError) {
			return getText(ruleText);
		} else {
			return getErrorText(ruleText);
		}
	}

	private Table getSubmitButtonTable() {
		Table submitTable = new Table(5, 1);
		submitTable.setWidth(2, "3");
		submitTable.setWidth(4, "3");
		submitTable.setCellpaddingAndCellspacing(0);

		Image image = getResourceBundle().getLocalizedImageButton("check.grant_check", "Grant check");
		SubmitButton grantButton = new SubmitButton(image, PARAM_GRANT_CHECK);
		submitTable.add(grantButton, 1, 1);

		image = getResourceBundle().getLocalizedImageButton("check.retrial", "Retrial");
		SubmitButton retrialButton = new SubmitButton(image, PARAM_RETRIAL_CHECK);
		submitTable.add(retrialButton, 3, 1);

		image = getResourceBundle().getLocalizedImageButton("check.save", "Save");
		SubmitButton saveButton = new SubmitButton(image, PARAM_SAVE_CHECK);
		submitTable.add(saveButton, 5, 1);

		return submitTable;
	}

	private Check verifyCheckRules(IWContext iwc) throws Exception {
		int checkId = Integer.parseInt(iwc.getParameter(PARAM_CHECK_ID));
		String[] selectedRules = iwc.getParameterValues(PARAM_RULE);
		String notes = iwc.getParameter(PARAM_NOTES);
		//    int managerId = iwc.getUser().getID();
		int managerId = iwc.getUserId();
		return getCheckBusiness(iwc).saveCheckRules(checkId, selectedRules, notes, managerId);
	}

	private void grantCheck(IWContext iwc) throws Exception {
		Check check = verifyCheckRules(iwc);
		if (!getCheckBusiness(iwc).allRulesVerified(check)) {
			getCheckBusiness(iwc).commit(check);
			//      this.errorMessage = localize("check.must_check_all_rules","All rules must be checked.");
			//      this.isError = true;
			viewCheck(iwc, check, true);
			return;
		}
		getCheckBusiness(iwc).approveCheck(check);

		//Create message for archive
		//Create post message to citizen

		String subject = "...";
		String body = "...";
		int userID = getCheckBusiness(iwc).getUserID(check);
		getCheckBusiness(iwc).sendMessageToCitizen(iwc,check,userID,subject,body);
		getCheckBusiness(iwc).sendMessageToArchive(iwc,check,userID,subject,body);
		getCheckBusiness(iwc).sendMessageToPrinter(iwc,check,userID,subject,body);

		add(getText("Check granted:"));
		viewCheckList(iwc);
	}

	private void retrialCheck(IWContext iwc) throws Exception {
		Check check = verifyCheckRules(iwc);
		getCheckBusiness(iwc).retrialCheck(check);

		//Create message to user
		String subject = "...";
		String body = "...";
		int userID = getCheckBusiness(iwc).getUserID(check);
		getCheckBusiness(iwc).sendMessageToCitizen(iwc,check,userID,subject,body);

		viewCheckList(iwc);
	}

	/**
	 * A method that saves the current check.
	 * @param IWContext iwc
	 * @throws Exception
	 */
	private void saveCheck(IWContext iwc) throws Exception {
		Check check = verifyCheckRules(iwc);
		getCheckBusiness(iwc).saveCheck(check);
		viewCheckList(iwc);
	}

	private CheckBusiness getCheckBusiness(IWContext iwc) throws Exception {
		return (CheckBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CheckBusiness.class);
	}

	private MemberFamilyLogic getMemberFamilyLogic(IWContext iwc) throws Exception {
		return (MemberFamilyLogic) com.idega.business.IBOLookup.getServiceInstance(iwc, MemberFamilyLogic.class);
	}
}