/**
 * Created on 1.2.2003
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.school.music.business.MusicSchoolGroupWriter;
import se.idega.idegaweb.commune.school.music.event.MusicSchoolEventListener;
import se.idega.util.SchoolClassMemberComparatorForSweden;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolYear;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.io.MediaWritable;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Window;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.text.Name;

/**
 * @author laddi
 */
public class MusicSchoolStudents extends MusicSchoolBlock {

	private final String PARAMETER_ACTION = "sch_action";
	private final String PARAMETER_SORT = "sch_student_sort";

	private final int ACTION_MANAGE = 1;

	private int action = 0;
	private int sortStudentsBy = SchoolClassMemberComparatorForSweden.NAME_SORT;

	/**
	 * @see se.idega.idegaweb.commune.school.presentation.SchoolCommuneBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws RemoteException {
		if (iwc.isLoggedOn()) {
			parseAction(iwc);

			switch (action) {
				case ACTION_MANAGE :
					drawForm(iwc);
					break;
			}
		}
		else {
			add(super.getSmallHeader(localize("not_logged_on", "Not logged on")));
		}
	}

	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION))
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		else
			action = ACTION_MANAGE;

		if (iwc.isParameterSet(PARAMETER_SORT))
			sortStudentsBy = Integer.parseInt(iwc.getParameter(PARAMETER_SORT));
		else
			sortStudentsBy = SchoolClassMemberComparatorForSweden.NAME_SORT;
	}

	private void drawForm(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setEventListener(MusicSchoolEventListener.class);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(action)));

		Table table = new Table(1, 7);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		table.setHeight(2, 12);
		table.setHeight(3, 12);
		table.setHeight(6, 12);

		form.add(table);

		Table headerTable = new Table(1, 3);
		headerTable.setWidth(Table.HUNDRED_PERCENT);
		headerTable.setCellpaddingAndCellspacing(0);
		headerTable.setHeight(1, 2, 6);
		table.add(headerTable, 1, 1);

		headerTable.setCellpaddingLeft(1, 1, 12);
		headerTable.add(getNavigationTable(this), 1, 1);
		headerTable.setCellpaddingLeft(1, 3, 12);
		headerTable.add(getSortTable(), 1, 3);
		headerTable.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_BOTTOM);

		if (getSession().getSeason() != null) {
			table.setCellpaddingRight(1, 3, 6);
			table.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(getXLSLink(), 1, 3);
			table.add(getStudentTable(iwc), 1, 5);
		}
		
		table.setCellpaddingRight(1, 7, 12);
		table.setAlignment(1, 7, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(getHelpButton("help_music_school_student_list"), 1, 7);

		add(form);
	}

	private Link getXLSLink() {
		Window window = new Window(localize("Group", "School group"), getIWApplicationContext().getIWMainApplication().getMediaServletURI());
		window.setResizable(true);
		window.setMenubar(true);
		window.setHeight(400);
		window.setWidth(500);
		
		Image image = getBundle().getImage("shared/xls.gif");
		image.setToolTip(localize("excel_list", "Get list in Excel format"));

		Link link = new Link(image);
		link.setWindow(window);
		link.addParameter(MediaWritable.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(MusicSchoolGroupWriter.class));
		return link;
	}

	private Table getStudentTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setColumns(7);
		int iRow = 1;
		int iColumn = 1;

		table.add(getSmallHeader(localize("nr", "Nr.")), iColumn++, iRow);
		table.add(getSmallHeader(localize("name", "Name")), iColumn++, iRow);
		table.add(getSmallHeader(localize("date_of_birth", "Date of birth")), iColumn++, iRow);
		table.add(getSmallHeader(localize("age", "Age")), iColumn++, iRow);
		table.add(getSmallHeader(localize("postal_code", "Postal code")), iColumn++, iRow);
		table.add(getSmallHeader(localize("instruments.plural_or_singular", "Instrument/s")), iColumn++, iRow);
		table.add(getSmallHeader(localize("department", "Department")), iColumn++, iRow);
		table.setCellpaddingLeft(1, iRow, 12);
		table.setRowStyleClass(iRow++, getHeaderRow2Class());

		User student;
		Address address;
		PostalCode postal;
		SchoolClassMember studentMember;
		SchoolYear department;
		Collection instruments;
		Link userLink;
		int numberOfStudents = 0;
		boolean notStarted = false;
		boolean hasTerminationDate = false;
		boolean showNotStarted = false;
		boolean showHasTermination = false;
		int count = 1;

		IWTimestamp stamp = new IWTimestamp();
		IWTimestamp startDate;

		List students = null;
		try {
			students = new ArrayList(getSchoolBusiness().getSchoolClassMemberHome().findBySchoolAndSeasonAndYearAndStudyPath(getSession().getProvider(), getSession().getSeason(), getSession().getDepartment(), getSession().getInstrument()));
		}
		catch (FinderException fe) {
			students = new ArrayList();
		}

		if (!students.isEmpty()) {
			numberOfStudents = students.size();
			Map studentMap = getCareBusiness().getStudentList(students);
			Collections.sort(students, SchoolClassMemberComparatorForSweden.getComparatorSortBy(sortStudentsBy, iwc.getCurrentLocale(), getUserBusiness(), studentMap));
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				iColumn = 1;
				studentMember = (SchoolClassMember) iter.next();
				student = (User) studentMap.get(new Integer(studentMember.getClassMemberId()));
				address = getUserBusiness().getUsersMainAddress(student);
				if (address != null) {
					postal = address.getPostalCode();
				}
				else {
					postal = null;
				}
				Age age = new Age(student.getDateOfBirth());
				IWTimestamp dateOfBirth = new IWTimestamp(student.getDateOfBirth());
				try {
					instruments = studentMember.getStudyPaths();
				}
				catch (IDORelationshipException ire) {
					log(ire);
					instruments = null;
				}
				department = studentMember.getSchoolYear();

				notStarted = false;
				hasTerminationDate = false;

				if (studentMember.getRegisterDate() != null) {
					startDate = new IWTimestamp(studentMember.getRegisterDate());
					if (startDate.isLaterThan(stamp)) {
						notStarted = true;
					}
				}
				if (studentMember.getRemovedDate() != null) {
					hasTerminationDate = true;
				}

				Name studentName = new Name(student.getFirstName(), student.getMiddleName(), student.getLastName());
				String name = studentName.getName(iwc.getApplicationSettings().getDefaultLocale(), true);

				userLink = getSmallLink(name);
				userLink.setEventListener(MusicSchoolEventListener.class);
				userLink.addParameter(getSession().getParameterNameChildID(), student.getPrimaryKey().toString());
				userLink.addParameter(getSession().getParameterNameStudentID(), studentMember.getPrimaryKey().toString());
				if (getResponsePage() != null) {
					userLink.setPage(getResponsePage());
				}

				if (iRow % 2 == 0)
					table.setRowStyleClass(iRow, getDarkRowClass());
				else
					table.setRowStyleClass(iRow, getLightRowClass());

				if (notStarted) {
					showNotStarted = true;
					table.add(getSmallErrorText("+"), 1, iRow);
				}
				if (hasTerminationDate) {
					showHasTermination = true;
					table.add(getSmallErrorText("&Delta;"), 1, iRow);
				}
				if (notStarted || hasTerminationDate) {
					table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, iRow);
				}

				table.setCellpaddingLeft(1, iRow, 12);
				table.add(getSmallText(String.valueOf(count)), iColumn++, iRow);
				if (getResponsePage() != null) {
					table.add(userLink, iColumn++, iRow);
				}
				else {
					table.add(getSmallText(name), iColumn++, iRow);
				}
				table.add(getSmallText(dateOfBirth.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), iColumn++, iRow);
				table.add(getSmallText(String.valueOf(age.getYears())), iColumn++, iRow);

				if (postal != null) {
					table.add(getSmallText(postal.getPostalCode()), iColumn++, iRow);
				}
				else {
					table.add(getSmallText("-"), iColumn++, iRow);
				}
				
				Iterator iterator = instruments.iterator();
				Text instrumentText = null;
				while (iterator.hasNext()) {
					SchoolStudyPath instrument = (SchoolStudyPath) iterator.next();
					if (instrumentText == null) {
						instrumentText = getSmallText(localize(instrument.getLocalizedKey(), instrument.getDescription()));
					}
					else {
						instrumentText.addToText(localize(instrument.getLocalizedKey(), instrument.getDescription()));
					}
					
					if (iterator.hasNext()) {
						instrumentText.addToText(", ");
					}
				}
				table.add(instrumentText, iColumn++, iRow);
				if (department != null) {
					table.add(getSmallText(localize(department.getLocalizedKey(), department.getSchoolYearName())), iColumn, iRow);
				}
				
				iRow++;
				count++;
			}

			if (showNotStarted || showHasTermination) {
				table.setHeight(iRow++, 2);
				if (showNotStarted) {
					table.mergeCells(1, iRow, table.getColumns(), iRow);
					table.setCellpaddingLeft(1, iRow, 12);
					table.add(getSmallErrorText("+ "), 1, iRow);
					table.add(getSmallText(localize("school.placement_has_not_started", "Placment has not started yet")), 1, iRow++);
				}
				if (showHasTermination) {
					table.mergeCells(1, iRow, table.getColumns(), iRow);
					table.setCellpaddingLeft(1, iRow, 12);
					table.add(getSmallErrorText("&Delta; "), 1, iRow);
					table.add(getSmallText(localize("school.placement_has_termination_date", "Placment has termination date")), 1, iRow++);
				}
			}
		}

		if (numberOfStudents > 0) {
			table.setHeight(iRow++, 6);
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.setCellpaddingLeft(1, iRow, 12);
			table.add(getSmallHeader(localize("school.number_of_students", "Number of students") + ": " + String.valueOf(numberOfStudents)), 1, iRow++);
		}

		return table;
	}

	protected Table getSortTable() {
		Table table = new Table(2, 3);
		table.setCellpadding(0);
		table.setCellspacing(0);

		table.add(getSmallHeader(localize("school.sort_by", "Sort by") + ":" + Text.NON_BREAKING_SPACE), 1, 3);

		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_SORT));
		menu.addMenuElement(SchoolClassMemberComparatorForSweden.NAME_SORT, localize("school.sort_by", "- Sort by -"));
		menu.addMenuElement(SchoolClassMemberComparatorForSweden.NAME_SORT, localize("school.sort_name", "Name"));
		menu.addMenuElement(SchoolClassMemberComparatorForSweden.AGE_SORT, localize("school.sort_age", "Age"));
		menu.addMenuElement(SchoolClassMemberComparatorForSweden.GENDER_SORT, localize("school.sort_gender", "Gender"));
		menu.setSelectedElement(sortStudentsBy);
		table.add(menu, 2, 3);

		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_RIGHT);

		return table;
	}
}