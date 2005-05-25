/*
 * $Id: ChoiceGranter.java,v 1.3 2005/05/25 18:52:04 laddi Exp $
 * Created on May 24, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReason;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationPersonalInfo;
import se.idega.idegaweb.commune.message.presentation.StandardMessageArea;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBORuntimeException;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDORelationshipException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * Last modified: $Date: 2005/05/25 18:52:04 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class ChoiceGranter extends AdultEducationBlock implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_SEASON = "prm_season";
	private static final String PARAMETER_USER = "prm_user";
	
	private static final String PARAMETER_REQUIREMENT_1 = "prm_requirement_1";
	private static final String PARAMETER_REQUIREMENT_2 = "prm_requirement_2";
	private static final String PARAMETER_REQUIREMENT_3 = "prm_requirement_3";
	private static final String PARAMETER_REQUIREMENT_4 = "prm_requirement_4";
	private static final String PARAMETER_SPECIAL_REQUIREMENT = "prm_special_requirement";
	private static final String PARAMETER_PRIORITY = "prm_priority";
	private static final String PARAMETER_NOTES = "prm_notes";
	private static final String PARAMETER_REJECTION_MESSAGE = "prm_rejection_message";
	
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_SAVE = 3;
	private static final int ACTION_GRANT = 4;
	private static final int ACTION_DENY = 5;
	private static final int ACTION_REMOVE = 6;
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			switch (parseAction(iwc)) {
				case ACTION_VIEW:
					showChoices(iwc);
					break;
					
				case ACTION_EDIT:
					showEditor(iwc);
					break;
					
				case ACTION_SAVE:
					save(iwc);
					showChoices(iwc);
					break;
					
				case ACTION_GRANT:
					grant(iwc);
					showChoices(iwc);
					break;
					
				case ACTION_DENY:
					deny(iwc);
					showChoices(iwc);
					break;
					
				case ACTION_REMOVE:
					remove(iwc);
					showChoices(iwc);
					break;
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private void showChoices(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, "");
		form.addParameter(PARAMETER_STUDY_PATH, "");
		form.addParameter(PARAMETER_SCHOOL_SEASON, "");
		form.addParameter(PARAMETER_USER, "");
		form.setEventListener(ChoiceGranter.class);
		
		SelectorUtility util = new SelectorUtility();
		
		DropdownMenu seasons = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SEASON), getBusiness().getSeasons(), "getSeasonName"));
		if (getSession().getSchoolSeason() != null) {
			seasons.setSelectedElement(getSession().getSchoolSeason().getPrimaryKey().toString());
		}
		seasons.setToSubmit();

		form.add(getHeader(localize("season", "Season") + ":"));
		form.add(Text.getNonBrakingSpace());
		form.add(seasons);
		form.add(new Break(2));
		form.add(getChoices(iwc));
		
		add(form);
	}

	private Table getChoices(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(6);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 1;
		
		table.add(getLocalizedSmallHeader("case_number","Case number"), column++, row);
		table.add(getLocalizedSmallHeader("date","Date"), column++, row);
		table.add(getLocalizedSmallHeader("personal_id","Personal ID"), column++, row);
		table.add(getLocalizedSmallHeader("handler","Handler"), column++, row);
		table.add(getLocalizedSmallHeader("status","Status"), column++, row++);

		Collection choices = getBusiness().getChoices(getSession().getSchoolSeason());
		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			AdultEducationChoice choice = (AdultEducationChoice) iter.next();
			IWTimestamp date = new IWTimestamp(choice.getChoiceDate());
			User user = choice.getUser();
			Group handler = choice.getHandler();
			column = 1;
			
			Link link = getSmallLink(choice.getPrimaryKey().toString());
			link.addParameter(PARAMETER_CHOICE, choice.getPrimaryKey().toString());
			link.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_EDIT));
			link.setEventListener(ChoiceGranter.class);
			
			if (row % 2 == 0) {
				table.setRowColor(row, getZebraColor1());
			}
			else {
				table.setRowColor(row, getZebraColor2());
			}

			table.add(link, column++, row);
			table.add(getSmallText(date.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			table.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), column++, row);
			if (handler != null) {
				table.add(getSmallText(getUserBusiness(iwc).getNameOfGroupOrUser(handler)), column++, row);
			}
			else {
				table.add(getSmallText("-"), column++, row);
			}
			table.add(getLocalizedSmallText("case_status." + choice.getStatus(), choice.getStatus()), column++, row);
			
			if (choice.getCaseStatus().equals(getBusiness().getCaseStatusReview())) {
				AdultEducationCourse course = choice.getCourse();

				SubmitButton delete = new SubmitButton(getDeleteIcon(localize("delete_choice", "Delete choice")), PARAMETER_ACTION, String.valueOf(ACTION_REMOVE));
				delete.setDescription(localize("delete_choices", "Delete choices"));
				delete.setValueOnClick(PARAMETER_STUDY_PATH, course.getStudyPathPK().toString());
				delete.setValueOnClick(PARAMETER_SCHOOL_SEASON, course.getSchoolSeasonPK().toString());
				delete.setValueOnClick(PARAMETER_USER, user.getPrimaryKey().toString());
				delete.setSubmitConfirm(localize("confirm_choice_delete", "Are you sure you want to remove the choice?"));
				table.add(delete, column++, row);
			}
			
			row++;
		}
		
		return table;
	}
	
	private void showEditor(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, "-1");
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		Table infoTable = new Table();
		infoTable.setColumns(6);
		infoTable.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(1, row, 10);
		table.setCellBorder(1, row, 1, "#999999", "solid");
		table.add(infoTable, 1, row++);
		table.setHeight(row++, 12);
		
		AdultEducationChoice choice = getSession().getChoice();
		AdultEducationCourse course = choice.getCourse();
		SchoolStudyPath path = course.getStudyPath();
		SchoolType type = path.getSchoolType();
		SchoolSeason season = course.getSchoolSeason();
		
		User user = choice.getUser();
		Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
		Address address = getUserBusiness(iwc).getUsersMainAddress(user);
		PostalCode code = address != null? address.getPostalCode() : null;

		Collection reasons = null;
		AdultEducationPersonalInfo info = null;
		try {
			info = getBusiness().getAdultEducationPersonalHome().findByUserId(new Integer(choice.getUserPK().toString()));
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		try {
			reasons = choice.getReasons();
		}
		catch (IDORelationshipException ire) {
			ire.printStackTrace();
		}
		String otherReason = choice.getOtherReason();

		infoTable.add(getSmallHeader(localize("case_number", "Case number") + ":"), 1, 1);
		infoTable.add(getSmallText(choice.getPrimaryKey().toString()), 2, 1);
		
		infoTable.add(getSmallHeader(localize("school_type", "School type") + ":"), 1, 2);
		infoTable.mergeCells(2, 2, 6, 2);
		infoTable.add(getSmallText(localize(type.getLocalizationKey(), type.getSchoolTypeName())), 2, 2);
		
		infoTable.add(getSmallHeader(localize("student", "Student") + ":"), 1, 3);
		infoTable.mergeCells(2, 3, 6, 3);
		infoTable.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale()) + ", " + name.getLastName() + " " + name.getFirstName()), 2, 3);
		
		infoTable.add(getSmallHeader(localize("address", "Address") + ":"), 1, 4);
		infoTable.mergeCells(2, 4, 6, 4);
		infoTable.add(getSmallText((address != null ? address.getStreetAddress() : "-") + ", " + (code != null ? code.getPostalAddress() : "-")), 2, 4);
		
		infoTable.setHeight(5, 16);
		
		infoTable.add(getSmallHeader(localize("citizenship", "Citizenship") + ":"), 1, 6);
		infoTable.add(getSmallHeader(localize("nationality", "Nationality") + ":"), 3, 6);
		infoTable.add(getSmallHeader(localize("language", "Language") + ":"), 5, 6);
		
		infoTable.add(getSmallText(info.getCitizenThisCountry() ? localize("swedish", "Swedish") : localize("other", "Other")), 2, 6);
		infoTable.add(getSmallText(!info.getNativeThisCountry() ? info.getNativeCountry().getName() : localize("swedish", "Swedish")), 4, 6);
		infoTable.add(getSmallText(info.getIcLanguageID() != -1 ? info.getICLanguage().getName() : "-"), 6, 6);
		
		Table pathTable = new Table(2, 1);
		pathTable.setWidth(Table.HUNDRED_PERCENT);
		pathTable.setCellpadding(0);
		pathTable.setCellspacing(0);
		table.add(pathTable, 1, row++);
		table.setHeight(row++, 8);
		
		pathTable.add(getSmallHeader(localize("choice_for_path", "Choice for study path") + ": "), 1, 1);
		pathTable.add(getSmallText(path.getDescription()), 1, 1);
		pathTable.add(getSmallHeader(localize("school_season", "School season") + ": "), 2, 1);
		pathTable.add(getSmallText(season.getSchoolSeasonName()), 2, 1);
		
		table.setTopCellBorder(1, row++, 1, "#999999", "solid");
		table.setHeight(row++, 8);
		table.add(getHeader(localize("reasons_for_study", "Reasons for study")), 1, row++);
		table.setHeight(row++, 6);
		if (reasons != null) {
			Iterator iter = reasons.iterator();
			while (iter.hasNext()) {
				AdultEducationChoiceReason reason = (AdultEducationChoiceReason) iter.next();
				table.add(getSmallText(localize(reason.getLocalizedKey(), reason.getName())), 1, row++);
			}
		}
		if (otherReason != null) {
			table.add(getSmallText(otherReason), 1, row++);
		}
		table.setHeight(row++, 8);
		table.setTopCellBorder(1, row++, 1, "#999999", "solid");
		table.setHeight(row++, 8);

		table.add(getHeader(localize("study_information", "Study information")), 1, row++);
		table.setHeight(row++, 6);

		infoTable = new Table(2, 3);
		infoTable.setCellpadding(0);
		infoTable.setCellspacing(0);
		infoTable.setWidth(Table.HUNDRED_PERCENT);
		infoTable.setHeight(1, 60);
		infoTable.setHeight(2, 60);
		infoTable.setHeight(3, 60);
		table.add(infoTable, 1, row++);
		
		Text studies = getSmallText("");
		boolean addComma = false;
		if (info.getEduA()) {
			studies.addToText(getResourceBundle().getLocalizedString("persInfo.education_A"));
			addComma = true;
		}
		if (info.getEduB()) {
			if (addComma) {
				studies.addToText(", ");
			}
			studies.addToText(getResourceBundle().getLocalizedString("persInfo.education_B"));
			addComma = true;
		}
		if (info.getEduC()) {
			if (addComma) {
				studies.addToText(", ");
			}
			studies.addToText(getResourceBundle().getLocalizedString("persInfo.education_C"));
			addComma = true;
		}
		if (info.getEduD()) {
			if (addComma) {
				studies.addToText(", ");
			}
			studies.addToText(getResourceBundle().getLocalizedString("persInfo.education_D"));
			addComma = true;
		}
		if (info.getEduE()) {
			if (addComma) {
				studies.addToText(", ");
			}
			studies.addToText(getResourceBundle().getLocalizedString("persInfo.education_E"));
			addComma = true;
		}
		if (info.getEduF() != null) {
			if (addComma) {
				studies.addToText(", ");
			}
			studies.addToText(info.getEduF());
			addComma = true;
		}
		if (info.getEduGCountryID() != -1) {
			if (addComma) {
				studies.addToText(Text.BREAK);
			}
			Country country = info.getEduGCountry();
			studies.addToText(info.getEduG() + " - " + country.getName() + " - " + getResourceBundle().getLocalizedString("persInfo.education_G_Years") + info.getEduGYears());
		}
		infoTable.add(getSmallHeader(localize("studies", "Studies")), 1, 1);
		infoTable.add(new Break(), 1, 1);
		infoTable.add(studies, 1, 1);
		infoTable.setCellpaddingRight(1, 1, 12);
		
		Text previousStudies = getSmallText("");
		addComma = false;
		if (info.getEduHA()) {
			previousStudies.addToText(getResourceBundle().getLocalizedString("persInfo.education_HA"));
			addComma = true;
		}
		if (info.getEduHB()) {
			if (addComma) {
				previousStudies.addToText(", ");
			}
			previousStudies.addToText(getResourceBundle().getLocalizedString("persInfo.education_HB"));
			addComma = true;
		}
		if (info.getEduHC()) {
			if (addComma) {
				previousStudies.addToText(", ");
			}
			previousStudies.addToText(getResourceBundle().getLocalizedString("persInfo.education_HC"));
			addComma = true;
		}
		if (info.getEduHCommune() != null) {
			previousStudies.addToText(" - " + info.getEduHCommune());
		}
		infoTable.add(getSmallHeader(localize("previous_studies", "Previous studies")), 1, 2);
		infoTable.add(new Break(), 1, 2);
		infoTable.add(previousStudies, 1, 2);
		infoTable.setCellpaddingRight(1, 2, 12);
		
		String studying = "";
		if (info.getFulltime()) {
			studying = getResourceBundle().getLocalizedString("persInfo.fulltime");
		}
		else {
			studying = getResourceBundle().getLocalizedString("persInfo.parttime");
		}
		infoTable.add(getSmallHeader(localize("studying", "Studying")), 1, 3);
		infoTable.add(new Break(), 1, 3);
		infoTable.add(getSmallText(studying), 1, 3);
		infoTable.setCellpaddingRight(1, 3, 12);

		Text languages = getSmallText("");
		addComma = false;
		if (info.getLangSFI()) {
			languages.addToText(getResourceBundle().getLocalizedString("persInfo.langSFI"));
			addComma = true;
		}
		if (info.getLangSAS()) {
			if (addComma) {
				languages.addToText(", ");
			}
			languages.addToText(getResourceBundle().getLocalizedString("persInfo.langSAS"));
			addComma = true;
		}
		if (info.getLangOTHER()) {
			if (addComma) {
				languages.addToText(", ");
			}
			languages.addToText(getResourceBundle().getLocalizedString("persInfo.langOther"));
		}
		infoTable.add(getSmallHeader(localize("languages", "Languages")), 2, 1);
		infoTable.add(new Break(), 2, 1);
		infoTable.add(languages, 2, 1);
		
		String studySupport = "";
		if (info.getStudySupport()) {
			studySupport = getResourceBundle().getLocalizedString("persInfo.study_support");
		}
		infoTable.add(getSmallHeader(localize("study_support", "Study support")), 2, 2);
		infoTable.add(new Break(), 2, 2);
		infoTable.add(studySupport, 2, 2);

		Text workSituation = getSmallText("");
		addComma = false;
		if (info.getWorkUnEmploy()) {
			workSituation.addToText(getResourceBundle().getLocalizedString("persInfo.work_unemployed"));
			addComma = true;
		}
		if (info.getWorkEmploy()) {
			if (addComma) {
				workSituation.addToText(", ");
			}
			workSituation.addToText(getResourceBundle().getLocalizedString("persInfo.work_employed"));
			addComma = true;
		}
		if (info.getWorkKicked()) {
			if (addComma) {
				workSituation.addToText(", ");
			}
			workSituation.addToText(getResourceBundle().getLocalizedString("persInfo.work_kicked"));
			addComma = true;
		}
		if (info.getWorkOther() != null) {
			if (addComma) {
				workSituation.addToText(", ");
			}
			workSituation.addToText(info.getWorkOther());
		}
		infoTable.add(getSmallHeader(localize("work_situation", "Work situation")), 2, 3);
		infoTable.add(new Break(), 2, 3);
		infoTable.add(workSituation, 2, 3);
		
		GenericButton button = getButton(new GenericButton(localize("edit_personal_info", "Edit personal info")));
		button.setWindowToOpen(PersonalInfoWindow.class);
		button.addParameterToWindow(PersonalInfo.PARAMETER_UNIQUE_ID, user.getUniqueId());
		table.setHeight(row++, 8);
		table.add(button, 1, row);
		table.setAlignment(1, row++, Table.HORIZONTAL_ALIGN_RIGHT);

		table.setHeight(row++, 8);
		table.setTopCellBorder(1, row++, 1, "#999999", "solid");
		table.setHeight(row++, 8);

		table.add(getHeader(localize("basic_requirements", "Basic requirements")), 1, row++);
		table.setHeight(row++, 12);

		CheckBox requirement1 = getCheckBox(PARAMETER_REQUIREMENT_1, Boolean.TRUE.toString());
		requirement1.setChecked(choice.hasGrantedRule1());
		CheckBox requirement2 = getCheckBox(PARAMETER_REQUIREMENT_2, Boolean.TRUE.toString());
		requirement2.setChecked(choice.hasGrantedRule2());
		CheckBox requirement3 = getCheckBox(PARAMETER_REQUIREMENT_3, Boolean.TRUE.toString());
		requirement3.setChecked(choice.hasGrantedRule3());
		CheckBox requirement4 = getCheckBox(PARAMETER_REQUIREMENT_4, Boolean.TRUE.toString());
		requirement4.setChecked(choice.hasGrantedRule4());
		
		table.add(requirement1, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("requirement_1", "Requirement 1")), 1, row++);
		table.add(requirement2, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("requirement_2", "Requirement 2")), 1, row++);
		table.add(requirement3, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("requirement_3", "Requirement 3")), 1, row++);
		table.add(requirement4, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("requirement_4", "Requirement 4") + ": "), 1, row);
		
		TextArea specialRequirement = (TextArea) getStyledInterface(new TextArea(PARAMETER_SPECIAL_REQUIREMENT));
		specialRequirement.setWidth(Table.HUNDRED_PERCENT);
		specialRequirement.setRows(3);
		if (choice.getGrantedRuleNotes() != null) {
			specialRequirement.setContent(choice.getGrantedRuleNotes());
		}
		table.add(specialRequirement, 1, row);
		table.setVerticalAlignment(1, row++, Table.VERTICAL_ALIGN_TOP);
		
		DropdownMenu priority = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_PRIORITY));
		priority.addMenuElement("-1", "");
		for (int a = 0; a <= 5; a++) {
			priority.addMenuElement(a, String.valueOf(a));
		}
		if (choice.getPriority() != -1) {
			priority.setSelectedElement(choice.getPriority());
		}
		table.setHeight(row++, 18);
		table.add(getSmallHeader(localize("priority", "Priority") + ": "), 1, row);
		table.add(priority, 1, row++);
		
		TextArea notes = (TextArea) getStyledInterface(new TextArea(PARAMETER_NOTES));
		notes.setWidth(Table.HUNDRED_PERCENT);
		notes.setRows(5);
		if (choice.getNotes() != null) {
			notes.setContent(choice.getNotes());
		}
		table.setHeight(row++, 18);
		table.add(getSmallHeader(localize("notes", "Notes")), 1, row++);
		table.add(notes, 1, row++);
		
		StandardMessageArea standardMsgArea = new StandardMessageArea();
		standardMsgArea.setCategory("VUXSTDMSG");
		standardMsgArea.setTextAreaName(PARAMETER_REJECTION_MESSAGE);
		
		TextArea rejectionMessage = (TextArea) getStyledInterface(standardMsgArea.getTextArea(table));
		rejectionMessage.setRows(4);
		rejectionMessage.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(row++, 18);
		table.add(getSmallHeader(localize("rejection_message", "Rejection message")), 1, row++);
		table.add(rejectionMessage, 1, row++);
		
		Table messageList = standardMsgArea.getMessageList(table);
		table.setHeight(row++, 18);
		table.add(getSmallHeader(localize("standard_messages", "Standard messages")), 1, row++);
		table.add(messageList, 1, row++);

		table.setHeight(row++, 18);
		
		SubmitButton grant = (SubmitButton) getButton(new SubmitButton(localize("grant", "Grant")));
		grant.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_GRANT));
		
		SubmitButton deny = (SubmitButton) getButton(new SubmitButton(localize("deny", "Deny")));
		deny.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_DENY));
		
		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save_notes", "Save notes")));
		save.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		
		GenericButton msgButton = getButton(new GenericButton("create_standard_message",localize("new_standard_message", "New Standard message")));
		msgButton.setWindowToOpen(standardMsgArea.getManageWindowClass());
		msgButton.addParameters(standardMsgArea.getCreateParameters());
		
		table.add(grant, 1, row);
		grant.setOnSubmitFunction("checkApplication", getSubmitCheckScript());
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(deny, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(save, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(msgButton, 1, row);

		add(form);
	}
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}

	private void remove(IWContext iwc) throws RemoteException {
		getBusiness().removeChoices(iwc.getParameter(PARAMETER_STUDY_PATH), iwc.getParameter(PARAMETER_SCHOOL_SEASON), iwc.getCurrentUser());
	}
	
	private void grant(IWContext iwc) throws RemoteException {
		boolean requirement1 = iwc.isParameterSet(PARAMETER_REQUIREMENT_1);
		boolean requirement2 = iwc.isParameterSet(PARAMETER_REQUIREMENT_2);
		boolean requirement3 = iwc.isParameterSet(PARAMETER_REQUIREMENT_3);
		boolean requirement4 = iwc.isParameterSet(PARAMETER_REQUIREMENT_4);
		String requirementNotes = iwc.getParameter(PARAMETER_SPECIAL_REQUIREMENT);
		String notes = iwc.getParameter(PARAMETER_NOTES);
		int priority = Integer.parseInt(iwc.getParameter(PARAMETER_PRIORITY));
		
		getBusiness().grantChoice(getSession().getChoice(), requirement1, requirement2, requirement3, requirement4, requirementNotes, notes, priority, iwc.getCurrentUser());
	}

	private void deny(IWContext iwc) throws RemoteException {
		String rejectionMessage = iwc.getParameter(PARAMETER_REJECTION_MESSAGE);
		
		getBusiness().denyChoice(getSession().getChoice(), rejectionMessage, iwc.getCurrentUser());
	}

	private void save(IWContext iwc) throws RemoteException {
		boolean requirement1 = iwc.isParameterSet(PARAMETER_REQUIREMENT_1);
		boolean requirement2 = iwc.isParameterSet(PARAMETER_REQUIREMENT_2);
		boolean requirement3 = iwc.isParameterSet(PARAMETER_REQUIREMENT_3);
		boolean requirement4 = iwc.isParameterSet(PARAMETER_REQUIREMENT_4);
		String requirementNotes = iwc.getParameter(PARAMETER_SPECIAL_REQUIREMENT);
		String notes = iwc.getParameter(PARAMETER_NOTES);
		int priority = Integer.parseInt(iwc.getParameter(PARAMETER_PRIORITY));
		
		getBusiness().saveChoiceChanges(getSession().getChoice(), requirement1, requirement2, requirement3, requirement4, requirementNotes, notes, priority);
	}

	public boolean actionPerformed(IWContext iwc) throws IWException {
		boolean actionPerformed = false;
		if (iwc.isParameterSet(PARAMETER_SEASON)) {
			try {
				getSession(iwc).setSeason(iwc.getParameter(PARAMETER_SEASON));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		if (iwc.isParameterSet(PARAMETER_CHOICE)) {
			try {
				getSession(iwc).setChoice(iwc.getParameter(PARAMETER_CHOICE));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		return actionPerformed;
	}

	private String getSubmitCheckScript() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\nfunction checkApplication(){\n\t");
		buffer.append("\n\t var action = ").append("findObj('").append(PARAMETER_ACTION).append("').value;");
		buffer.append("\n\t if (action != '").append(ACTION_GRANT).append("') return true");
		buffer.append("\n\t var rule1 = ").append("findObj('").append(PARAMETER_REQUIREMENT_1).append("');");
		buffer.append("\n\t var rule2 = ").append("findObj('").append(PARAMETER_REQUIREMENT_2).append("');");
		buffer.append("\n\t var rule3 = ").append("findObj('").append(PARAMETER_REQUIREMENT_3).append("');");
		buffer.append("\n\t var rule4 = ").append("findObj('").append(PARAMETER_REQUIREMENT_4).append("');");
		buffer.append("\n\t var ruleNotes = ").append("findObj('").append(PARAMETER_SPECIAL_REQUIREMENT).append("').value;");
		buffer.append("\n\t var priority = ").append("findObj('").append(PARAMETER_PRIORITY).append("');");

		buffer.append("\n\t var one = eval(rule1.checked);");
		buffer.append("\n\t var two = eval(rule2.checked);");
		buffer.append("\n\t var three = eval(rule3.checked);");
		buffer.append("\n\t var four = eval(rule4.checked);");
		buffer.append("\n\t var hasNotes = ruleNotes.length > 0;");
		buffer.append("\n\t var hasPriority = priority.selectedIndex > 0;");

		buffer.append("\n\t if (!hasPriority) {");
		String message = localize("must_set_priority", "Priority must be set.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n\t if (one && two) {");
		message = localize("cant_select_requirement_combination", "You can not select this requirement combination.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n\t if (four && !hasNotes) {");
		message = localize("must_write_requirement_note", "You have to write a note if you choose requirement 4.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n\t if (!four && hasNotes) {");
		message = localize("must_select_requirement_4", "You have to select requirement 4 if you write a note.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n\t if (one) {");
		buffer.append("\n\t\t findObj('").append(PARAMETER_ACTION).append("').value = " + ACTION_GRANT + ";");
		buffer.append("\n\t\t return true;");
		buffer.append("\n\t }");
		buffer.append("\n\t else if (two) {");
		buffer.append("\n\t\t findObj('").append(PARAMETER_ACTION).append("').value = " + ACTION_GRANT + ";");
		buffer.append("\n\t\t return true;");
		buffer.append("\n\t }");
		buffer.append("\n\t else if (four && hasNotes) {");
		buffer.append("\n\t\t findObj('").append(PARAMETER_ACTION).append("').value = " + ACTION_GRANT + ";");
		buffer.append("\n\t\t return true;");
		buffer.append("\n\t }");
		buffer.append("\n\t else {");
		message = localize("must_meet_requirements", "Requirements must be met in order to grant choice.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n}\n");
		return buffer.toString();
	}
}