/*
 * $Id: AdminAdultEducationStudentPlacings.java,v 1.1.2.5 2006/09/18 11:32:21 palli Exp $
 * Created on Oct 19, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.idega.idegaweb.commune.adulteducation.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberGrade;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SelectDropdownDouble;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.UserSession;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public class AdminAdultEducationStudentPlacings extends AdultEducationBlock {

	private final static int ACTION_SHOW_LIST = 0;

	private final static int ACTION_EDIT = 1;

	private final static int ACTION_DELETE = 2;

	private final static int ACTION_SAVE = 3;

	private final static String PARAM_DELETE_ID = "adultadmin_delete";

	private final static String PARAM_EDIT_ID = "adultadmin_edit";

	private final static String PARAM_SAVE_ID = "adultadmin_save";

	private final static String PARAM_DATE_FROM = "adultadmin_date_from";

	private final static String PARAM_DATE_TO = "adultadmin_date_to";

	private final static String PARAM_COURSE_CODE = "adultadmin_course_code";

	private final static String PARAM_GROUP = "adultadmin_group";

	private final static String PARAM_REMOVE_DATE_TO = "adultadmin_remove_date_to";

	private final static String ERROR_INVALID_START_DATE = "admin_adult_invalid_start_date";

	private final static String ERROR_INVALID_END_DATE = "admin_adult_invalid_end_date";

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.idega.idegaweb.commune.school.presentation.SchoolCommuneBlock#init(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			User user = getUserSession(iwc).getUser();
			if (user != null) {
				int method = parseAction(iwc);

				switch (method) {
				case ACTION_SHOW_LIST:
					showList(iwc, user);
					break;
				case ACTION_EDIT:
					edit(iwc);
					break;
				case ACTION_DELETE:
					delete(iwc);
					showList(iwc, user);
					break;
				case ACTION_SAVE:
					save(iwc);
					showList(iwc, user);
					break;
				default:
					showList(iwc, user);
					break;
				}
			}
		} catch (RemoteException e) {

		}
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAM_DELETE_ID)) {
			return ACTION_DELETE;
		} else if (iwc.isParameterSet(PARAM_SAVE_ID)) {
			return ACTION_SAVE;
		} else if (iwc.isParameterSet(PARAM_EDIT_ID)) {
			return ACTION_EDIT;
		}

		return ACTION_SHOW_LIST;
	}

	protected void save(IWContext iwc) {
		String saveClassMemberId = iwc.getParameter(PARAM_SAVE_ID);
		String groupId = iwc.getParameter(PARAM_GROUP);
		String dateFromString = iwc.getParameter(PARAM_DATE_FROM);
		String dateToString = iwc.getParameter(PARAM_DATE_TO);
		boolean removeDateTo = iwc.isParameterSet(PARAM_REMOVE_DATE_TO);
		try {
			SchoolClassMember member = getBusiness().getSchoolBusiness()
					.getSchoolClassMemberHome().findByPrimaryKey(
							new Integer(saveClassMemberId));
			member.setSchoolClassId(new Integer(groupId).intValue());
			IWTimestamp dateFrom = new IWTimestamp(dateFromString);
			member.setRegisterDate(dateFrom.getTimestamp());
			if (removeDateTo) {
				member.setRemovedDate(null);
			} else {
				if (dateToString == null || "".equals(dateToString)) {
					member.setRemovedDate(null);
				} else {
					IWTimestamp dateTo = new IWTimestamp(dateToString);
					member.setRemovedDate(dateTo.getTimestamp());
				}
			}

			member.store();

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	protected void delete(IWContext iwc) {
		String deleteClassMemberId = iwc.getParameter(PARAM_DELETE_ID);

		try {
			getBusiness().getSchoolBusiness().deleteSchoolClassMemberEntry(
					new Integer(deleteClassMemberId).intValue());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void edit(IWContext iwc) {
		String placementId = iwc.getParameter(PARAM_EDIT_ID);

		Form f = new Form();
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		f.add(table);

		int row = 1;

		table.setRowHeight(row++, "16");
		if (useStyleNames()) {
			table.setCellpaddingLeft(1, row, 12);
			table.setCellpaddingRight(1, row, 12);
		}
		table.add(getSmallHeader(localize("adultadmin.edit_header",
				"Edit placement within adult education")), 1, row++);
		table.setRowHeight(row++, "3");
		try {
			table.add(getEditTable(placementId, iwc), 1, row++);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		table.setRowHeight(row++, "16");
		if (useStyleNames()) {
			table.setCellpaddingLeft(1, row, 12);
			table.setCellpaddingRight(1, row, 12);
		}
		add(f);
	}

	protected Table getEditTable(String placementId, IWContext iwc)
			throws NumberFormatException, RemoteException, FinderException {
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(3);

		int row = 1;

		SchoolClassMember member;
		SchoolClass group;
		SchoolSeason season;
		IWTimestamp validFrom;
		IWTimestamp terminated = null;
		SchoolClassMemberGrade grade = null;

		member = getBusiness().getSchoolBusiness().getSchoolClassMemberHome()
				.findByPrimaryKey(new Integer(placementId));
		group = member.getSchoolClass();
		season = group.getSchoolSeason();
		validFrom = new IWTimestamp(member.getRegisterDate());
		if (member.getRemovedDate() != null) {
			terminated = new IWTimestamp(member.getRemovedDate());
		} else {
			terminated = null;
		}
		grade = getBusiness().getStudentGrade(member);

		// Provider
		table.add(getLocalizedSmallHeader("adultadmin.provider", "Provider"),
				1, row);
		table.add(getSmallText(group.getSchool().getName()), 2, row++);

		// Get the study path
		SchoolStudyPath path = null;
		if (group.getCode() != null && !"".equals(group.getCode())) {
			try {
				path = getBusiness().getCourse(season.getPrimaryKey(),
						group.getCode()).getStudyPath();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}

		// Course code
		table.add(getLocalizedSmallHeader("adultadmin.code_and_group",
				"Course code / group"), 1, row);

		SelectDropdownDouble codeInput = (SelectDropdownDouble) getStyledInterface(new SelectDropdownDouble(
				PARAM_COURSE_CODE, PARAM_GROUP));
		Collection classes = getBusiness().getSchoolBusiness()
				.getSchoolClassHome().findBySchoolAndSeason(
						group.getSchoolId(), group.getSchoolSeasonId());
		ArrayList inList = new ArrayList();
		if (!classes.isEmpty()) {
			Iterator it = classes.iterator();
			while (it.hasNext()) {
				SchoolClass sClass = (SchoolClass) it.next();
				if (sClass.getCode() != null && !"".equals(sClass.getCode())) {
					if (!inList.contains(sClass.getCode())) {
						try {
							SchoolStudyPath sClassStudyPath = getBusiness()
									.getCourse(season.getPrimaryKey(),
											sClass.getCode()).getStudyPath();

							if (path != null && sClassStudyPath != null
									&& path.equals(sClassStudyPath)) {
								inList.add(sClass.getCode());
								Map map = new LinkedHashMap();
								map.put(sClass.getPrimaryKey().toString(),
										sClass.getName());
								codeInput.addMenuElement(sClass.getPrimaryKey()
										.toString(), sClass.getCode(), map);
							}
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (FinderException e) {
						}
					}
				}
			}
		}
		table.add(codeInput, 2, row++);

		// Study path
		table.add(
				getLocalizedSmallHeader("adultadmin.study_path", "Study path"),
				1, row);

		if (path != null && path.getDescription() != null) {
			StringBuffer pathText = new StringBuffer(path.getDescription());
			pathText.append(", ");
			pathText.append(path.getPoints());
			table.add(getSmallText(pathText.toString()), 2, row++);
		} else {
			table.add(getSmallText("-"), 2, row++);
		}

		// Start date
		table.add(
				getLocalizedSmallHeader("adultadmin.start_date", "Start date"),
				1, row);
		DateInput dateFrom = (DateInput) getStyledInterface(new DateInput(
				PARAM_DATE_FROM));
		dateFrom.setStyleClass(getSmallTextFontStyle());
		getBusiness().getCourse(season.getPrimaryKey(), group.getCode())
				.getStartDate();

		dateFrom
				.setEarliestPossibleDate(
						getBusiness().getCourse(season.getPrimaryKey(),
								group.getCode()).getStartDate(),
						getLocalizedString(
								ERROR_INVALID_START_DATE,
								"It is not possible to set a start date earlier then the course start date",
								iwc));
		dateFrom.setDate(validFrom.getDate());
		dateFrom.setYearRange(validFrom.getYear() - 2, validFrom.getYear() + 5);
		table.add(dateFrom, 2, row++);

		// End date
		table.add(getLocalizedSmallHeader("adultadmin.end_date", "End date"),
				1, row);
		/*
		 * if (terminated != null) {
		 * table.add(getSmallText(terminated.getLocaleDate(iwc
		 * .getCurrentLocale(), IWTimestamp.SHORT)), 2, row++); } else {
		 * table.add(getSmallText("-"), 2, row++); }
		 */
		DateInput dateTo = (DateInput) getStyledInterface(new DateInput(
				PARAM_DATE_TO));
		dateTo.setStyleClass(getSmallTextFontStyle());
		dateTo
				.setEarliestPossibleDate(
						validFrom.getDate(),
						getLocalizedString(
								ERROR_INVALID_END_DATE,
								"It is not possible to set an end date earlier then the chosen start date",
								iwc));
		dateTo.setYearRange(validFrom.getYear(), validFrom.getYear() + 5);
		if (terminated != null) {
			dateTo.setDate(terminated.getDate());
			dateTo.setYearRange(terminated.getYear() -2, terminated.getYear() + 5);
		} else {
			IWTimestamp now = new IWTimestamp();
			dateTo.setYearRange(now.getYear() - 2, now.getYear() + 5);
		}
		table.add(dateTo, 2, row);

		if (grade != null && grade.getGrade() != null
				&& grade.getGrade().getGrade() != null) {
			CheckBox removeDateTo = new CheckBox(PARAM_REMOVE_DATE_TO,
					"remove_date_to");
			table.add(removeDateTo, 3, row);
			table.add(getLocalizedSmallHeader("adultadmin.remove_date_TO",
					"Remove date to"), 3, row++);
		} else {
			row++;
		}

		// Grade
		table.add(getLocalizedSmallHeader("adultadmin.grade", "Grade"), 1, row);
		if (grade != null && grade.getGrade() != null
				&& grade.getGrade().getGrade() != null) {
			table.add(getSmallText(grade.getGrade().getGrade()), 2, row++);
		} else {
			table.add(getSmallText("-"), 2, row++);
		}

		SubmitButton save = new SubmitButton(
				localize("adultadmin.save", "Save"), PARAM_SAVE_ID, member
						.getPrimaryKey().toString());
		save.setDescription(localize("adultadmin.save_tooltip",
				"Click here to save placement"));
		table.add(save, 3, row);

		return table;
	}

	protected void showList(IWContext iwc, User user) {
		Form f = new Form();
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());

		f.add(table);

		int row = 1;

		table.setRowHeight(row++, "16");
		if (useStyleNames()) {
			table.setCellpaddingLeft(1, row, 12);
			table.setCellpaddingRight(1, row, 12);
		}
		GenericButton button = getButton(new GenericButton(localize(
				"edit_personal_info", "Edit personal info")));
		button.setWindowToOpen(PersonalInfoWindow.class);
		try {
			getSession().setStudent(user.getPrimaryKey().toString());
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (EJBException e1) {
			e1.printStackTrace();
		}
		table.add(button, 1, row);
		table.setAlignment(1, row++, Table.HORIZONTAL_ALIGN_RIGHT);

		table.add(getSmallHeader(localize("adultadmin.header",
				"Placements within adult education")), 1, row++);
		table.setRowHeight(row++, "3");
		try {
			table.add(getPlacingsTable(iwc, user), 1, row++);
		} catch (RemoteException e) {

		}
		table.setRowHeight(row++, "16");
		if (useStyleNames()) {
			table.setCellpaddingLeft(1, row, 12);
			table.setCellpaddingRight(1, row, 12);
		}

		table.add(new HiddenInput(PARAM_EDIT_ID, ""));
		table.add(new HiddenInput(PARAM_DELETE_ID, ""));

		add(f);
	}

	protected Table getPlacingsTable(IWContext iwc, User user)
			throws RemoteException {
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(8);
		if (useStyleNames()) {
			table.setRowStyleClass(1, getHeaderRowClass());
		} else {
			table.setRowColor(1, getHeaderColor());
		}

		int column = 1;
		int row = 1;

		if (useStyleNames()) {
			table.setCellpaddingLeft(1, row, 12);
			table.setCellpaddingRight(table.getColumns(), row, 12);
		}
		table.add(getLocalizedSmallHeader("adultadmin.provider", "Provider"),
				column++, row);
		table.add(getLocalizedSmallHeader("adultadmin.code", "Course code"),
				column++, row);
		table.add(
				getLocalizedSmallHeader("adultadmin.study_path", "Study path"),
				column++, row);
		table.add(
				getLocalizedSmallHeader("adultadmin.start_date", "Start date"),
				column++, row);
		table.add(getLocalizedSmallHeader("adultadmin.end_date", "End date"),
				column++, row);
		table.add(getLocalizedSmallHeader("adultadmin.grade", "Grade"),
				column++, row++);

		SchoolClassMember member;
		SchoolClass group;
		SchoolSeason season;
		IWTimestamp validFrom;
		IWTimestamp terminated = null;
		SchoolClassMemberGrade grade = null;

		Collection placings = getBusiness().getSchoolBusiness()
				.findClassMemberInAdultEducation(
						((Integer) user.getPrimaryKey()).intValue());
		Iterator iter = placings.iterator();
		while (iter.hasNext()) {
			column = 1;
			member = (SchoolClassMember) iter.next();
			group = member.getSchoolClass();
			season = group.getSchoolSeason();
			validFrom = new IWTimestamp(member.getRegisterDate());
			if (member.getRemovedDate() != null) {
				terminated = new IWTimestamp(member.getRemovedDate());
			} else {
				terminated = null;
			}
			grade = getBusiness().getStudentGrade(member);

			if (useStyleNames()) {
				if (row % 2 == 0) {
					table.setRowStyleClass(row, getDarkRowClass());
				} else {
					table.setRowStyleClass(row, getLightRowClass());
				}
				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(table.getColumns(), row, 12);
			} else {
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());
			}

			// Provider
			table.add(getSmallText(group.getSchool().getName()), column++, row);
			// Course code
			table.add(getSmallText(group.getCode()), column++, row);
			// Study path
			SchoolStudyPath path = null;
			if (group.getCode() != null && !"".equals(group.getCode())) {
				try {
					path = getBusiness().getCourse(season.getPrimaryKey(),
							group.getCode()).getStudyPath();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				}
			}

			if (path != null && path.getDescription() != null) {
				StringBuffer pathText = new StringBuffer(path.getDescription());
				pathText.append(", ");
				pathText.append(path.getPoints());
				table.add(getSmallText(pathText.toString()), column++, row);
			} else {
				table.add(getSmallText("-"), column++, row);
			}
			// Start date
			table.add(getSmallText(validFrom.getLocaleDate(iwc
					.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			// End date
			if (terminated != null) {
				table
						.add(getSmallText(terminated.getLocaleDate(iwc
								.getCurrentLocale(), IWTimestamp.SHORT)),
								column++, row);
			} else {
				table.add(getSmallText("-"), column++, row);
			}
			// Grade
			if (grade != null && grade.getGrade() != null
					&& grade.getGrade().getGrade() != null) {
				table.add(getSmallText(grade.getGrade().getGrade()), column++,
						row);
			} else {
				table.add(getSmallText("-"), column++, row);
			}

			SubmitButton edit = new SubmitButton(getEditIcon(localize(
					"adultadmin.edit", "Edit")));
			edit.setDescription(localize("adultadmin.edit_tooltip",
					"Click here to edit placement"));
			edit.setValueOnClick(PARAM_EDIT_ID, member.getPrimaryKey()
					.toString());
			table.add(edit, column++, row);

			if (grade != null && grade.getGrade() != null
					&& grade.getGrade().getGrade() != null) {
				row++;
			} else {
				SubmitButton delete = new SubmitButton(getDeleteIcon(localize(
						"adultadmin.delete", "Delete")), PARAM_DELETE_ID,
						member.getPrimaryKey().toString());
				delete.setDescription(localize("adultadmin.delete_tooltip",
						"Click here to delete placement"));
				// delete.setValueOnClick(PARAM_DELETE_ID,
				// member.getPrimaryKey()
				// .toString());
				delete.setSubmitConfirm(localize(
						"adultadmin.delete_confirmation",
						"Are you sure you want to delete this placement?"));
				table.add(delete, column++, row++);
			}

		}
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);

		return table;
	}

	protected UserSession getUserSession(IWUserContext iwuc) {
		try {
			return (UserSession) IBOLookup.getSessionInstance(iwuc,
					UserSession.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}