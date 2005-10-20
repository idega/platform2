/*
 * $Id: AdultEducationStudentPlacings.java,v 1.2 2005/10/20 01:07:32 palli Exp $
 * Created on Oct 19, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.idega.idegaweb.commune.adulteducation.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.school.business.SchoolCommuneSession;

import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberGrade;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

public class AdultEducationStudentPlacings extends AdultEducationBlock {

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.idega.idegaweb.commune.school.presentation.SchoolCommuneBlock#init(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());

		int row = 1;
		GenericButton back = (GenericButton) getStyledInterface(new GenericButton("back", localize("back", "Back")));
		if (getResponsePage() != null)
			back.setPageToOpen(getResponsePage());

		int studentId = -1;
		try {
			studentId = getSchoolCommuneSession(iwc).getStudentID();
		}
		catch (RemoteException e) {
			studentId = -1;
		}

		if (studentId != -1) {
			if (useStyleNames()) {
				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(1, row, 12);
			}
			try {
				table.add(getInformationTable(iwc), 1, row++);
			}
			catch (RemoteException e) {

			}
			table.setRowHeight(row++, "16");
			if (useStyleNames()) {
				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(1, row, 12);
			}
			table.add(getSmallHeader(localize("school.placements", "Placements")), 1, row++);
			table.setRowHeight(row++, "3");
			try {
				table.add(getPlacingsTable(iwc), 1, row++);
			}
			catch (RemoteException e) {

			}
			table.setRowHeight(row++, "16");
			if (useStyleNames()) {
				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(1, row, 12);
			}
			table.add(back, 1, row++);
		}
		else {
			if (useStyleNames()) {
				table.setCellpaddingLeft(1, 1, 12);
				table.setCellpaddingLeft(1, 3, 12);
			}
			table.add(getLocalizedHeader("school.no_student_found", "No student found."), 1, 1);
			table.add(back, 1, 3);
		}

		add(table);
	}

	protected Table getPlacingsTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(7);
		if (useStyleNames()) {
			table.setRowStyleClass(1, getHeaderRowClass());
		}
		else {
			table.setRowColor(1, getHeaderColor());
		}
		int column = 1;
		int row = 1;

		if (useStyleNames()) {
			table.setCellpaddingLeft(1, row, 12);
			table.setCellpaddingRight(table.getColumns(), row, 12);
		}
		table.add(getLocalizedSmallHeader("period", "Period"), column++, row);
		table.add(getLocalizedSmallHeader("code", "Course code"), column++, row);
		table.add(getLocalizedSmallHeader("study_path", "Study path"), column++, row);
		table.add(getLocalizedSmallHeader("start_date", "Start date"), column++, row);
		table.add(getLocalizedSmallHeader("end_date", "End date"), column++, row);
		table.add(getLocalizedSmallHeader("grade", "Grade"), column++, row++);

		SchoolClassMember member;
		SchoolClass group;
		SchoolSeason season;
		IWTimestamp validFrom;
		IWTimestamp terminated = null;
		SchoolClassMemberGrade grade = null;
		
		Collection placings = getBusiness().getSchoolBusiness().findClassMemberInAdultEducation(
				getSchoolCommuneSession(iwc).getStudentID(), getSchoolCommuneSession(iwc).getSchoolID());
		Iterator iter = placings.iterator();
		while (iter.hasNext()) {
			column = 1;
			member = (SchoolClassMember) iter.next();
			group = member.getSchoolClass();
			season = group.getSchoolSeason();
			validFrom = new IWTimestamp(member.getRegisterDate());
			if (member.getRemovedDate() != null) {
				terminated = new IWTimestamp(member.getRemovedDate());
			}
			grade = getBusiness().getStudentGrade(member);

			if (useStyleNames()) {
				if (row % 2 == 0) {
					table.setRowStyleClass(row, getDarkRowClass());
				}
				else {
					table.setRowStyleClass(row, getLightRowClass());
				}
				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(table.getColumns(), row, 12);
			}
			else {
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());
			}

			//Period
			table.add(getSmallText(season.getSchoolSeasonName()), column++, row);
			//Course code
			table.add(getSmallText(group.getCode()), column++, row);
			//Study path
			if (member.getStudyPathId() > 0) {
				SchoolStudyPath path = getBusiness().getSchoolBusiness().getSchoolStudyPath(new Integer(member.getStudyPathId()));
				if (path != null && path.getDescription() != null) {
					table.add(getSmallText(path.getDescription()), column++, row);					
				}
				else {
					table.add(getSmallText("-"), column++, row);
				}
			} 
			else {
				table.add(getSmallText("-"), column++, row);
			}
			//Start date
			table.add(getSmallText(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			//End date
			if (terminated != null) {
				table.add(getSmallText(terminated.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			}
			else {
				table.add(getSmallText("-"), column++, row++);
			}
			//Grade
			if (grade != null && grade.getGrade() != null && grade.getGrade().getGrade() != null) {
				table.add(getSmallText(grade.getGrade().getGrade()), column++, row++);
			}
			else {
				table.add(getSmallText("-"), column++, row++);
			}
		}
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);

		return table;
	}

	protected Table getInformationTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setColumns(3);
		table.setWidth(1, "100");
		table.setWidth(2, "6");
		int row = 1;

		User user = getBusiness().getUserBusiness().getUser(getSchoolCommuneSession(iwc).getStudentID());
		if (user != null) {
			Address address = getBusiness().getUserBusiness().getUsersMainAddress(user);

			table.add(getLocalizedSmallHeader("school.student", "Student"), 1, row);
			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
			table.add(getSmallText(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), 3, row);
			table.add(getSmallText(" - "), 3, row);
			table.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), 3, row++);

			if (address != null) {
				table.add(getLocalizedSmallHeader("school.address", "Address"), 1, row);
				table.add(getSmallText(address.getStreetAddress()), 3, row);
				if (address.getPostalAddress() != null)
					table.add(getSmallText(", " + address.getPostalAddress()), 3, row);
				row++;
			}

			table.setHeight(row++, 12);

			Phone phone = getBusiness().getUserBusiness().getHomePhone(user);
			Email email = getBusiness().getUserBusiness().getEmail(user);

			if (phone != null && phone.getNumber() != null) {
				table.add(getSmallText(localize("school.phone", "Phone") + ": "), 3, row);
				table.add(getSmallText(phone.getNumber()), 3, row++);
			}
			if (email != null && email.getEmailAddress() != null) {
				Link link = getSmallLink(email.getEmailAddress());
				link.setURL("mailto:" + email.getEmailAddress(), false, false);
				table.add(link, 3, row++);
			}

			table.setHeight(row++, 12);
		}

		return table;
	}

/*	private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) {
		try {
			return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
			return null;
		}
	}*/

	private SchoolCommuneSession getSchoolCommuneSession(IWContext iwc) {
		try {
			return (SchoolCommuneSession) IBOLookup.getSessionInstance(iwc, SchoolCommuneSession.class);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
			return null;
		}
	}

}
