/*
 * Created on Nov 11, 2004
 */
package se.agura.applications.vacation.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.agura.applications.vacation.business.ExtraInformation;
import se.agura.applications.vacation.data.VacationType;

import com.idega.block.media.presentation.FileChooser;
import com.idega.core.builder.data.ICPage;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.util.IWTimestamp;

/**
 * @author Anna
 */
public class VacationApplication extends VacationBlock {

	
	private ICPage iPage;
	

	public void present(IWContext iwc) {
		try {
			String action = iwc.getParameter(PARAMETER_ACTION);
			if (action == null) {
				add(showPageOne(iwc));
			}
			else if (action.equals(ACTION_NEXT)) {
				add(showPageTwo(iwc));
			}
			else if (action.equals(ACTION_PAGE_THREE)) {
				add(showPageThree(iwc));
			}
			else if (action.equals(ACTION_SEND)) {
				save(iwc);
				add(showPageFour());
			}
		}
		catch (RemoteException re) {
			log(re);
		}
	}

	private void save(IWContext iwc) throws RemoteException {
		VacationType vacationType = null;
		try {
			vacationType = getBusiness(iwc).getVacationType(iwc.getParameter(PARAMETER_VACATION_TYPE));
		}
		catch (FinderException fe) {
			log(fe);
		}
		IWTimestamp fromDate = new IWTimestamp(iwc.getParameter(PARAMETER_VACATION_FROM_DATE));
		IWTimestamp toDate = new IWTimestamp(iwc.getParameter(PARAMETER_VACATION_TO_DATE));
		int selectedHours = Integer.parseInt(iwc.getParameter(PARAMETER_VACATION_WORKING_HOURS));
		String[] workingHours = iwc.getParameterValues(PARAMETER_VACATION_HOURS);
		String comment = iwc.getParameter(PARAMETER_VACATION_EXTRA_TEXT);
		Collection extra = new ArrayList();
		Map extraInfo = getBusiness(iwc).getExtraVacationTypeInformation(vacationType);
		if (extraInfo != null && extraInfo.size() > 0) {
			Iterator iter = extraInfo.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				String value = iwc.getParameter(key);
				String type = getBusiness(iwc).getExtraInformationType(vacationType, key);
				ExtraInformation info = new ExtraInformation();
				info.setKey(key);
				info.setValue(value);
				info.setType(type);
				extra.add(info);
			}
		}
		try {
			getBusiness(iwc).storeApplication(iwc.getCurrentUser(), fromDate.getDate(), toDate.getDate(), selectedHours,
					vacationType, workingHours, extra, comment);
		}
		catch (CreateException ce) {
			log(ce);
		}
	}

	// view of the first page in the process
	private Form showPageOne(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.add(getPersonInfo(iwc, iwc.getCurrentUser()));
		form.add(new Break());
		form.add(getTimeTable(iwc));
		form.add(new Break());
		form.addParameter(PARAMETER_ACTION, ACTION_NEXT);
		form.add(getNextButton());
		return form;
	}

	// view of the second step of the vacation request
	private Form showPageTwo(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.add(showWorkingDaysTable(iwc));
		form.add(new Break());
		form.add(getReasonTable(iwc));
		form.add(new Break());
		form.addParameter(PARAMETER_ACTION, ACTION_PAGE_THREE);
		form.add(getCancelButton());
		form.add(getNextButton());
		form.maintainParameter(PARAMETER_VACATION_FROM_DATE);
		form.maintainParameter(PARAMETER_VACATION_TO_DATE);
		form.maintainParameter(PARAMETER_VACATION_WORKING_HOURS);
		form.maintainParameter(PARAMETER_VACATION_TYPE);
		return form;
	}

	// shows the overview over the application request made by the user.
	private Form showPageThree(IWContext iwc) {
		Form form = new Form();
		form.add(getPersonInfo(iwc, iwc.getCurrentUser()));
		form.add(new Break());
		form.add(showVacationRequest(iwc));
		form.add(new Break());
		form.addParameter(PARAMETER_ACTION, ACTION_SEND);
		form.add(getCancelButton());
		form.add(getNextButton());
		form.maintainParameter(PARAMETER_VACATION_FROM_DATE);
		form.maintainParameter(PARAMETER_VACATION_TO_DATE);
		form.maintainParameter(PARAMETER_VACATION_WORKING_HOURS);
		form.maintainParameter(PARAMETER_VACATION_TYPE);
		form.maintainParameter(PARAMETER_VACATION_HOURS);
		form.maintainParameter(PARAMETER_VACATION_EXTRA_TEXT);
		try {
			VacationType vacationType = getBusiness(iwc).getVacationType(iwc.getParameter(PARAMETER_VACATION_TYPE));
			Map extraInfo = getBusiness(iwc).getExtraVacationTypeInformation(vacationType);
			if (extraInfo != null && extraInfo.size() > 0) {
				Iterator iter = extraInfo.keySet().iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					form.maintainParameter(key);
				}
			}
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (RemoteException re) {
			log(re);
		}
		return form;
	}

	private Table showPageFour() {
		Table table = new Table();
		table.add(getText(getResourceBundle().getLocalizedString("vacation.application_sent", "Your application has been sent.")));
		
		return table;
	}

	private Table getTimeTable(IWContext iwc) throws RemoteException {
		DateInput fromDateInput = (DateInput) getInput(new DateInput(PARAMETER_VACATION_FROM_DATE));
		fromDateInput.setAsNotEmpty(getResourceBundle().getLocalizedString("vacation.from_date_not_empty", "This field may not be empty"));
		
		DateInput toDateInput = (DateInput) getInput(new DateInput(PARAMETER_VACATION_TO_DATE));
		toDateInput.setAsNotEmpty(getResourceBundle().getLocalizedString("vacation.to_date_not_empty", "This field may not be empty"));
		
		TextInput hours = (TextInput) getInput(new TextInput(PARAMETER_VACATION_WORKING_HOURS));
		hours.setAsNotEmpty(getResourceBundle().getLocalizedString("vacation.hours_not_empty", "This field may not be empty"));
		
		Table timeTable = new Table(2, 6);
		timeTable.setCellpadding(iCellpadding);
		timeTable.setCellspacing(0);
		timeTable.setBorder(0);
		timeTable.setWidth(1, iHeaderColumnWidth);
		timeTable.setCellpaddingLeft(1, 1, 0);
		timeTable.setCellpaddingLeft(1, 2, 0);
		timeTable.setCellpaddingLeft(1, 4, 0);
		timeTable.setCellpaddingLeft(1, 6, 0);
		
		int row = 1;
		timeTable.setCellpaddingLeft(1, row, 0);
		timeTable.add(getHeader(getResourceBundle().getLocalizedString("vacation.time.required_vacation", "Required vacation")), 1,
				row);
		timeTable.add(getText(getResourceBundle().getLocalizedString("vacation.time.from_date", "From date") + ":" + Text.NON_BREAKING_SPACE), 2, row);
		timeTable.add(fromDateInput, 2, row++);
		
		timeTable.add(getText(getResourceBundle().getLocalizedString("vacation.time.to.date", "To date") + ":" + Text.NON_BREAKING_SPACE), 2, row);
		timeTable.add(toDateInput, 2, row++);
		
		timeTable.setHeight(row++, 12);
		
		timeTable.add(getHeader(getResourceBundle().getLocalizedString("vacation.time.ordinary_hours",
				"Ordinary workinghours per day")), 1, row);
		timeTable.add(hours, 2, row);
		timeTable.add(getText(Text.NON_BREAKING_SPACE + getResourceBundle().getLocalizedString("vacation.time.hours", "hours")), 2, row++);

		timeTable.setHeight(row++, 12);

		timeTable.add(getHeader(getResourceBundle().getLocalizedString("vacation.vacation_type",
		"Vacation type")), 1, row);
		timeTable.add(getReasonDropdown(iwc), 2, row);
		
		return timeTable;
	}

	private GenericButton getCancelButton() {
		GenericButton cancel = getButton(new GenericButton("cancel", getResourceBundle().getLocalizedString("vacation.cancel", "Cancel")));
		if (iPage != null) {
			cancel.setPageToOpen(iPage);
		}
		return cancel;
	}

	private SubmitButton getNextButton() {
		SubmitButton nextButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("vacation.next", "Next step")));
		return nextButton;
	}

	private DropdownMenu getReasonDropdown(IWContext iwc) throws RemoteException {
		SelectorUtility util = new SelectorUtility();
		DropdownMenu reason = (DropdownMenu) getInput(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_VACATION_TYPE),
				getBusiness(iwc).getVacationTypes(), "getLocalizedKey", getResourceBundle()));
		return reason;
	}

	private Table getReasonTable(IWContext iwc) throws RemoteException {
		Table reasonTable = new Table();
		TextArea text = new TextArea(PARAMETER_VACATION_EXTRA_TEXT);
		text.setWidth(Table.HUNDRED_PERCENT);
		text.setRows(4);
		reasonTable.setWidth(iWidth);
		reasonTable.setCellpadding(iCellpadding);
		reasonTable.setCellspacing(0);
		reasonTable.setBorder(0);
		int row = 1;
		
		VacationType vacationType = null;
		try {
			vacationType = getBusiness(iwc).getVacationType(iwc.getParameter(PARAMETER_VACATION_TYPE));
		}
		catch (FinderException fe) {
			log(fe);
		}
		reasonTable.add(getHeader(getResourceBundle().getLocalizedString("vacation.type", "Type")), 1, row);
		reasonTable.add(getText(getResourceBundle().getLocalizedString(vacationType.getLocalizedKey())), 2, row++);
		Map extraInformation = getBusiness(iwc).getExtraVacationTypeInformation(vacationType);
		if (extraInformation != null && extraInformation.size() > 0) {
			Iterator iter = extraInformation.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				String value = (String) extraInformation.get(key);
				String type = getBusiness(iwc).getExtraInformationType(vacationType, key);
				reasonTable.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
				reasonTable.add(getHeader(getResourceBundle().getLocalizedString("vacation_type_metadata." + key, key)), 1, row);
				if (type.equals("com.idega.presentation.ui.TextArea")) {
					TextArea input = (TextArea) getInput(new TextArea(key));
					input.setWidth(Table.HUNDRED_PERCENT);
					input.setRows(4);
					reasonTable.add(input, 2, row);
				}
				else if (type.equals("com.idega.presentation.ui.TextInput")) {
					TextInput input = (TextInput) getInput(new TextInput(key));
					reasonTable.add(input, 2, row);
				}
				else if (type.equals("com.idega.presentation.ui.RadioButton")) {
					StringTokenizer tokens = new StringTokenizer(value, ",");
					while (tokens.hasMoreTokens()) {
						String booleanValue = tokens.nextToken();
						RadioButton button = (RadioButton) getRadioButton(new RadioButton(key, booleanValue));
						reasonTable.add(button, 2, row);
						reasonTable.add(getText(getResourceBundle().getLocalizedString("vacation_type_metadata_boolean." + booleanValue,
								booleanValue)), 2, row);
						if (tokens.hasMoreTokens()) {
							reasonTable.add(new Break(), 2, row);
						}
					}
				}
				else if (type.equals("com.idega.block.media.presentation.FileChooser")) {
					FileChooser chooser = new FileChooser(key);
					reasonTable.add(chooser, 2, row);
				}
				row++;
			}
		}
		reasonTable.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		reasonTable.add(getHeader(getResourceBundle().getLocalizedString("vacation.type_text", "Comment")), 1, row);
		reasonTable.add(text, 2, row++);
		reasonTable.setWidth(1, iHeaderColumnWidth);
		
		for (int a = 1; a <= reasonTable.getRows(); a++) {
			reasonTable.setCellpaddingLeft(1, a, 0);
		}
		
		return reasonTable;
	}

	private Table showWorkingDaysTable(IWContext iwc) {
		int selectedHours = Integer.parseInt(iwc.getParameter(PARAMETER_VACATION_WORKING_HOURS));
		IWTimestamp from = new IWTimestamp(iwc.getParameter(PARAMETER_VACATION_FROM_DATE));
		IWTimestamp to = new IWTimestamp(iwc.getParameter(PARAMETER_VACATION_TO_DATE));

		Table workingDaysTable = new Table();
		workingDaysTable.setBorder(0);
		workingDaysTable.setCellspacing(0);
		workingDaysTable.setCellpadding(iCellpadding);
		int row = 1;
		
		workingDaysTable.add(getHeader(getResourceBundle().getLocalizedString("vacation.time.period",
				"Working days and hours under the period")), 1, row);
		workingDaysTable.add(getText(getResourceBundle().getLocalizedString("vacation.time.week", "Week")), 2, row);
		workingDaysTable.add(getText(getResourceBundle().getLocalizedString("vacation.time.monday", "Mo")), 3, row);
		workingDaysTable.add(getText(getResourceBundle().getLocalizedString("vacation.time.tuesday", "Tu")), 4, row);
		workingDaysTable.add(getText(getResourceBundle().getLocalizedString("vacation.time.wednesday", "We")), 5, row);
		workingDaysTable.add(getText(getResourceBundle().getLocalizedString("vacation.time.thursday", "th")), 6, row);
		workingDaysTable.add(getText(getResourceBundle().getLocalizedString("vacation.time.friday", "Fr")), 7, row);
		workingDaysTable.add(getText(getResourceBundle().getLocalizedString("vacation.time.saturday", "Sa")), 8, row);
		workingDaysTable.add(getText(getResourceBundle().getLocalizedString("vacation.time.sunday", "Su")), 9, row);
		
		int week = -1;
		to.addDays(1);
		while (from.isEarlierThan(to)) {
			if (week != from.getWeekOfYear() && !(from.getDayOfWeek() == Calendar.SUNDAY)) {
				row++;
				week = from.getWeekOfYear();
				workingDaysTable.add(getText(String.valueOf(from.getWeekOfYear())), 2, row);
			}
			if (week == -1 && (from.getDayOfWeek() == Calendar.SUNDAY)) {
				row++;
				workingDaysTable.add(getText(String.valueOf(from.getWeekOfYear())), 2, row);
			}
			
			int hours = selectedHours;
			int dayOfWeek = from.getDayOfWeek();
			if (dayOfWeek == Calendar.SUNDAY) {
				dayOfWeek = 9;
				hours = 0;
			}
			else {
				if (dayOfWeek == Calendar.SATURDAY) {
					hours = 0;
				}
				dayOfWeek++;
			}
			
			workingDaysTable.add(getWorkingHoursMenu(hours, 8), dayOfWeek, row);
			from.addDays(1);
		}
		
		workingDaysTable.mergeCells(1, 1, 1, workingDaysTable.getRows());
		workingDaysTable.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		workingDaysTable.setWidth(1, iHeaderColumnWidth);
		workingDaysTable.setCellpaddingLeft(1, 1, 0);
		
		return workingDaysTable;
	}

	private DropdownMenu getWorkingHoursMenu(int selectedHours, int maxHours) {
		DropdownMenu menu = (DropdownMenu) getInput(new DropdownMenu(PARAMETER_VACATION_HOURS));
		for (int a = 0; a <= maxHours; a++) {
			menu.addMenuElement(a, String.valueOf(a));
		}
		if (selectedHours > 0) {
			menu.setSelectedElement(selectedHours);
		}
		return menu;
	}

	private Table showVacationRequest(IWContext iwc) {
		Table table = new Table();
		table.setColumns(9);
		table.setCellpadding(iCellpadding);
		table.setCellspacing(0);
		table.setBorder(0);
		
		int row = 1;
		IWTimestamp fromDate = new IWTimestamp(iwc.getParameter(PARAMETER_VACATION_FROM_DATE));
		IWTimestamp toDate = new IWTimestamp(iwc.getParameter(PARAMETER_VACATION_TO_DATE));
		IWTimestamp today = new IWTimestamp();
		String selectedHours = iwc.getParameter(PARAMETER_VACATION_WORKING_HOURS);
		String[] workingHours = iwc.getParameterValues(PARAMETER_VACATION_HOURS);
		String vacationType = iwc.getParameter(PARAMETER_VACATION_TYPE);
		VacationType type = null;
		try {
			type = getBusiness(iwc).getVacationType(vacationType);
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (RemoteException re) {
			log(re);
		}

		table.setCellpaddingLeft(1, row, 0);
		table.add(getHeader(getResourceBundle().getLocalizedString("vacation.time.required_vacation", "Required vacation")), 1, row);
		table.mergeCells(2, row, table.getColumns(), row);
		table.add(getText(getResourceBundle().getLocalizedString("vacation.time.from_date", "From date") + ":" + Text.NON_BREAKING_SPACE), 2, row);
		table.add(getText(fromDate.getLocaleDate(iwc.getCurrentLocale())), 2, row++);
		
		table.mergeCells(2, row, table.getColumns(), row);
		table.add(getText(getResourceBundle().getLocalizedString("vacation.time.to_date", "To date") + ":" + Text.NON_BREAKING_SPACE), 2, row);
		table.add(getText(toDate.getLocaleDate(iwc.getCurrentLocale())), 2, row++);
		table.setHeight(row++, 12);

		table.setCellpaddingLeft(1, row, 0);
		table.add(getHeader(getResourceBundle().getLocalizedString("vacation.time.ordinary_hours", "Ordinary workinghours per day")),
				1, row);
		table.add(getText(selectedHours), 2, row++);
		table.setHeight(row++, 12);

		table.setCellpaddingLeft(1, row, 0);
		table.add(getHeader(
				getResourceBundle().getLocalizedString("vacation.time.period", "Working days and hours under the period*")), 1,
				row);
		
		table.add(getText(getResourceBundle().getLocalizedString("vacation.time.week", "Week")), 2, row);
		table.add(getText(getResourceBundle().getLocalizedString("vacation.time.monday", "Mo")), 3, row);
		table.add(getText(getResourceBundle().getLocalizedString("vacation.time.tuesday", "Tu")), 4, row);
		table.add(getText(getResourceBundle().getLocalizedString("vacation.time.wednesday", "We")), 5, row);
		table.add(getText(getResourceBundle().getLocalizedString("vacation.time.thursday", "th")), 6, row);
		table.add(getText(getResourceBundle().getLocalizedString("vacation.time.friday", "Fr")), 7, row);
		table.add(getText(getResourceBundle().getLocalizedString("vacation.time.saturday", "Sa")), 8, row);
		table.add(getText(getResourceBundle().getLocalizedString("vacation.time.sunday", "Su")), 9, row);
		int startRow = row;

		int week = -1;
		int a = 0;
		toDate.addDays(1);
		while (fromDate.isEarlierThan(toDate)) {
			if (week != fromDate.getWeekOfYear() && !(fromDate.getDayOfWeek() == Calendar.SUNDAY)) {
				row++;
				week = fromDate.getWeekOfYear();
				table.add(String.valueOf(fromDate.getWeekOfYear()), 2, row);
			}
			if (week == -1 && (fromDate.getDayOfWeek() == Calendar.SUNDAY)) {
				row++;
				table.add(String.valueOf(fromDate.getWeekOfYear()), 2, row);
			}
			
			int dayOfWeek = fromDate.getDayOfWeek();
			if (dayOfWeek == Calendar.SUNDAY) {
				dayOfWeek = 8;
			}
			dayOfWeek++;
			
			table.add(getText(workingHours[a]), dayOfWeek, row);
			fromDate.addDays(1);
			a++;
		}
		table.setVerticalAlignment(1, startRow, Table.VERTICAL_ALIGN_TOP);
		table.mergeCells(1, startRow, 1, row++);
		table.setHeight(row++, 12);

		table.setCellpaddingLeft(1, row, 0);
		table.add(getHeader(getResourceBundle().getLocalizedString("vacation.type", "Type")), 1, row);
		table.mergeCells(2, row, table.getColumns(), row);
		if (type != null) {
			table.add(getText(getResourceBundle().getLocalizedString(type.getLocalizedKey())), 2, row++);
		}

		table.setCellpaddingLeft(1, row, 0);
		table.add(getHeader(getResourceBundle().getLocalizedString("vacation.time.request_date", "Request date")), 1, row);
		table.mergeCells(2, row, table.getColumns(), row);
		table.add(getText(today.getLocaleDate(iwc.getCurrentLocale())), 2, row++);
		
		table.setWidth(1, iHeaderColumnWidth);
		
		return table;
	}
}