/*
 * Created on Nov 8, 2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package se.agura.applications.vacation.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import se.agura.applications.vacation.business.VacationBusiness;
import se.agura.applications.vacation.business.VacationConstants;
import se.agura.applications.vacation.data.VacationRequest;
import se.agura.applications.vacation.data.VacationTime;
import se.agura.applications.vacation.data.VacationType;
import com.idega.block.process.data.CaseLog;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author Anna
 */
public abstract class VacationBlock extends Block {

	protected static final String IW_BUNDLE_IDENTIFIER = "se.agura.applications.vacation";

	protected static final String PARAMETER_ACTION = "vac_action";
	protected static final String PARAMETER_PRIMARY_KEY_VAC = VacationConstants.PARAMETER_PRIMARY_KEY;
	protected static final String PARAMETER_PRIMARY_KEY_VAC_TIME = "vac_time_pk";
	protected static final String PARAMETER_PRIMARY_KEY_VAC_TYPE = "vac_type_pk";
	protected static final String PARAMETER_VACATION_TYPE = "vac_vacation_type";
	protected static final String PARAMETER_VACATION_FROM_DATE = "vac_vacation_from_date";
	protected static final String PARAMETER_VACATION_TO_DATE = "vac_vacation_to_date";
	protected static final String PARAMETER_VACATION_HOURS = "vac_vacation_hours";
	protected static final String PARAMETER_VACATION_WORKING_HOURS = "vac_vacation_working_hours";
	protected static final String PARAMETER_VACATION_EXTRA_TEXT = "vac_vacation_extra_text";
	protected static final String PARAMETER_COMMENT = "vac_comment";
	protected static final String PARAMETER_WITH_SALARY_COMPENSATION = "vac_salary_compensation";
	protected static final String PARAMETER_FORWARD_GROUP = "vac_forward_group";
	protected static final String PARAMETER_HANDLER = "vac_handler";
	
	protected static final String ACTION_NEXT = "next";
	protected static final String ACTION_CANCEL = "cancel";
	protected static final String ACTION_SEND = "send";
	protected static final String ACTION_DENIED = "denied";
	protected static final String ACTION_APPROVED = "approved";
	protected static final String ACTION_BACK = "back";
	protected static final String ACTION_PAGE_THREE = "page_three";
	protected static final String ACTION_PAGE_FOUR = "page_four";
	protected static final String ACTION_SAVE = "save";
	protected static final String ACTION_FORWARD = "forward";
	protected static final String ACTION_CLOSED = "closed";

	private IWBundle iwb;
	private IWResourceBundle iwrb;

	private ICPage iPage;
	
	private String iTextStyleClass;
	private String iHeaderStyleClass;
	private String iLinkStyleClass;
	private String iInputStyleClass;
	private String iButtonStyleClass;
	private String iRadioStyleClass;
	private String iLogColor = "#00FFFF";

	protected int iCellpadding = 3;
	protected int iHeaderColumnWidth = 260;
	private int iLogColorColumnWidth = 12;
	protected String iWidth = Table.HUNDRED_PERCENT;

	public void main(IWContext iwc) throws Exception {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		present(iwc);
	}

	protected Table getPersonInfo(IWContext iwc, User user) {
		Table personInfo = new Table(2, 3);
		personInfo.setBorder(0);
		personInfo.setCellspacing(0);
		personInfo.setCellpadding(iCellpadding);
		personInfo.setWidth(1, iHeaderColumnWidth);
		personInfo.setCellpaddingLeft(1, 1, 0);
		personInfo.setCellpaddingLeft(1, 2, 0);
		personInfo.setCellpaddingLeft(1, 3, 0);
		
		int row = 1;
		
		String name = user.getName();
		String personalID = PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale());
		String parish = "";
		Group group = user.getPrimaryGroup();
		if (group != null) {
			parish = group.getName();
		}
		personInfo.add(getHeader(getResourceBundle().getLocalizedString("vacation.user_name", "Name")), 1, row);
		personInfo.add(getText(name), 2, row++);
		personInfo.add(getHeader(getResourceBundle().getLocalizedString("vacation.user_personal_id", "PersonalID")), 1, row);
		personInfo.add(getText(personalID), 2, row++);
		personInfo.add(getHeader(getResourceBundle().getLocalizedString("vacation.Parish", "Parish")), 1, row);
		personInfo.add(getText(parish), 2, row++);
		return personInfo;
	}

	protected Table showVacationRequest(IWContext iwc, VacationRequest vacation) {
		Table table = new Table();
		//table.setWidth(iWidth);
		table.setCellpadding(iCellpadding);
		table.setCellspacing(0);
		table.setColumns(9);
		int row = 1;
		
		VacationType vacationType = vacation.getVacationType();

		IWTimestamp fromDate = new IWTimestamp(vacation.getFromDate());
		IWTimestamp toDate = new IWTimestamp(vacation.getToDate());
		IWTimestamp date = new IWTimestamp(vacation.getCreatedDate());
		int selectedHours = vacation.getOrdinaryWorkingHours();
		Collection times = null;
		try {
			times = getBusiness(iwc).getVacationTimes(vacation);
		}
		catch (RemoteException re) {
			log(re);
		}

		table.mergeCells(2, row, table.getColumns(), row);
		table.add(getHeader(getResourceBundle().getLocalizedString("vacation.time.required_vacation", "Required vacation")), 1, row);
		table.add(getText(getResourceBundle().getLocalizedString("vacation.time.from_date", "From date") + ":" + Text.NON_BREAKING_SPACE), 2, row);
		table.add(getText(fromDate.getLocaleDate(iwc.getCurrentLocale())), 2, row++);

		table.mergeCells(2, row, table.getColumns(), row);
		table.add(getText(getResourceBundle().getLocalizedString("vacation.time.to_date", "To date") + ":" + Text.NON_BREAKING_SPACE), 2, row);
		table.add(getText(toDate.getLocaleDate(iwc.getCurrentLocale())), 2, row++);
		table.setHeight(row++, 12);

		table.add(getHeader(getResourceBundle().getLocalizedString("vacation.time.ordinary_hours", "Ordinary workinghours per day")), 1, row);
		table.add(getText(String.valueOf(selectedHours) + Text.NON_BREAKING_SPACE + getResourceBundle().getLocalizedString("vacation.hours", "hours")), 2, row++);
		table.setHeight(row++, 12);

		if (times.size() > 0) {
			int startRow = row;
			table.add(getHeader(getResourceBundle().getLocalizedString("vacation.time.period", "Working days and hours under the period")), 1, row);
			table.add(getText(getResourceBundle().getLocalizedString("vacation.time.week", "Week")), 2, row);
			table.add(getText(getResourceBundle().getLocalizedString("vacation.time.monday", "Mo")), 3, row);
			table.add(getText(getResourceBundle().getLocalizedString("vacation.time.tuesday", "Tu")), 4, row);
			table.add(getText(getResourceBundle().getLocalizedString("vacation.time.wednesday", "We")), 5, row);
			table.add(getText(getResourceBundle().getLocalizedString("vacation.time.thursday", "th")), 6, row);
			table.add(getText(getResourceBundle().getLocalizedString("vacation.time.friday", "Fr")), 7, row);
			table.add(getText(getResourceBundle().getLocalizedString("vacation.time.saturday", "Sa")), 8, row);
			table.add(getText(getResourceBundle().getLocalizedString("vacation.time.sunday", "Su")), 9, row++);
			Iterator iter = times.iterator();
			while (iter.hasNext()) {
				VacationTime time = (VacationTime) iter.next();
				table.add(getText(String.valueOf(time.getWeekNumber())), 2, row);
				if (time.getMonday() > 0) {
					table.add(getText(String.valueOf(time.getMonday())), 3, row);
				}
				if (time.getTuesday() > 0) {
					table.add(getText(String.valueOf(time.getTuesday())), 4, row);
				}
				if (time.getWednesday() > 0) {
					table.add(getText(String.valueOf(time.getWednesday())), 5, row);
				}
				if (time.getThursday() > 0) {
					table.add(getText(String.valueOf(time.getThursday())), 6, row);
				}
				if (time.getFriday() > 0) {
					table.add(getText(String.valueOf(time.getFriday())), 7, row);
				}
				if (time.getSaturday() > 0) {
					table.add(getText(String.valueOf(time.getSaturday())), 8, row);
				}
				if (time.getSunday() > 0) {
					table.add(getText(String.valueOf(time.getSunday())), 9, row);
				}
				row++;
			}
			table.setVerticalAlignment(1, startRow, Table.VERTICAL_ALIGN_TOP);
			table.mergeCells(1, startRow, 1, row -1);
			table.setHeight(row++, 12);
		}
		
		table.mergeCells(2, row, table.getColumns(), row);
		table.add(getHeader(getResourceBundle().getLocalizedString("vacation.type", "Type")), 1, row);
		table.add(getText(vacationType.getTypeName()), 2, row++);
		table.setHeight(row++, 12);

		table.mergeCells(2, row, table.getColumns(), row);
		table.add(getHeader(getResourceBundle().getLocalizedString("vacation.request_date", "Request date")), 1, row);
		table.add(getText(date.getLocaleDate(iwc.getCurrentLocale())), 2, row++);
		table.setHeight(row++, 12);

		table.setWidth(1, iHeaderColumnWidth);
		table.setCellpaddingLeft(1, 0);

		return table;
	}
	
	protected Table getVacationActionOverview(IWContext iwc, VacationRequest vacation) throws RemoteException {
		Collection logs = getBusiness(iwc).getLogs(vacation);
		if (logs != null) {
			Table table = new Table();
			table.setWidth(iWidth);
			table.setCellpadding(0);
			table.setCellspacing(0);
			int row = 1;
			
			Iterator iter = logs.iterator();
			while (iter.hasNext()) {
				CaseLog log = (CaseLog) iter.next();
				User performer = log.getPerformer();
				String comment = log.getComment();
				IWTimestamp timestamp = new IWTimestamp(log.getTimeStamp());
				String status = log.getCaseStatusAfter().getStatus();
				
				Table logTable = new Table(3, 3);
				logTable.setCellpadding(iCellpadding);
				logTable.setCellspacing(0);
				logTable.mergeCells(1, 1, 1, 3);
				logTable.setColor(1, 1, iLogColor);
				logTable.setWidth(2, iHeaderColumnWidth);
				logTable.setWidth(1, iLogColorColumnWidth);
				
				String action = "";
				if (status.equals(getBusiness(iwc).getCaseStatusDenied().getStatus())) {
					action = getResourceBundle().getLocalizedString("vacation.rejected_by", "Rejected by");
				}
				else if (status.equals(getBusiness(iwc).getCaseStatusGranted().getStatus())) {
					action = getResourceBundle().getLocalizedString("vacation.granted_by", "Granted by");
				}
				else if (status.equals(getBusiness(iwc).getCaseStatusMoved().getStatus())) {
					action = getResourceBundle().getLocalizedString("vacation.supported_by", "Supported by");
				}
				
				logTable.add(getHeader(action), 2, 1);
				logTable.add(getText(performer.getName()), 3, 1);

				logTable.add(getHeader(getResourceBundle().getLocalizedString("vacation.message", "Message")), 2, 2);
				logTable.add(getText(comment), 3, 2);

				logTable.add(getHeader(getResourceBundle().getLocalizedString("vacation.date", "Date")), 2, 3);
				logTable.add(getText(timestamp.getLocaleDate(iwc.getCurrentLocale())), 3, 3);
				
				table.add(logTable, 1, row);
				table.setCellBorder(1, row++, 1, "#dfdfdf", "solid");
				if (iter.hasNext()) {
					table.setHeight(row++, 6);
				}
			}
			
			return table;
		}
		return null;
	}

	protected VacationBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (VacationBusiness) IBOLookup.getServiceInstance(iwac, VacationBusiness.class);
		}
		catch (IBOLookupException ible) {
			throw new IBORuntimeException(ible);
		}
	}

	protected UserBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
		}
		catch (IBOLookupException ible) {
			throw new IBORuntimeException(ible);
		}
	}

	public abstract void present(IWContext iwc);

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	/**
	 * @return Returns the iwb.
	 */
	protected IWBundle getBundle() {
		return iwb;
	}

	/**
	 * @return Returns the iwrb.
	 */
	protected IWResourceBundle getResourceBundle() {
		return iwrb;
	}
	
	protected Text getText(String string) {
		Text text = new Text(string);
		if (iTextStyleClass != null) {
			text.setStyleClass(iTextStyleClass);
		}
		return text;
	}
	
	protected Text getHeader(String string) {
		Text text = new Text(string);
		if (iHeaderStyleClass != null) {
			text.setStyleClass(iHeaderStyleClass);
		}
		return text;
	}
	
	protected Link getLink(String string) {
		Link link = new Link(string);
		if (iLinkStyleClass != null) {
			link.setStyleClass(iLinkStyleClass);
		}
		return link;
	}
	
	protected InterfaceObject getInput(InterfaceObject input) {
		if (iInputStyleClass != null) {
			input.setStyleClass(iInputStyleClass);
		}
		return input;
	}
	
	protected InterfaceObject getRadioButton(InterfaceObject radioButton) {
		if (iRadioStyleClass != null) {
			radioButton.setStyleClass(iRadioStyleClass);
		}
		return radioButton;
	}
	
	protected GenericButton getButton(GenericButton button) {
		if (iButtonStyleClass != null) {
			button.setStyleClass(iButtonStyleClass);
		}
		return button;
	}
	
	
	public SubmitButton getSendButton() {
		SubmitButton sendButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("vacation_approver.send_application", "Send"), PARAMETER_ACTION, ACTION_SEND));
		sendButton.setToolTip(getResourceBundle().getLocalizedString("vacation.send.tooltip","Sends your application in"));
		sendButton.setSubmitConfirm(getResourceBundle().getLocalizedString("vacation.send.popup","Are you sure you want to send the application now?"));
		return sendButton;
	}
	
	public GenericButton getCancelButton() {
		GenericButton cancelButton = getButton(new GenericButton(getResourceBundle().getLocalizedString("vacation_approver.cancel", "Cancel")));
		if (getPage() != null) {
			cancelButton.setPageToOpen(getPage());
		}
		cancelButton.setToolTip(getResourceBundle().getLocalizedString("vacation.cancel.tooltip","Cancels and returns to last page"));
		return cancelButton;
	}

	/**
	 * @param buttonStyleClass The buttonStyleClass to set.
	 */
	public void setButtonStyleClass(String buttonStyleClass) {
		iButtonStyleClass = buttonStyleClass;
	}
	/**
	 * @param headerStyleClass The headerStyleClass to set.
	 */
	public void setHeaderStyleClass(String headerStyleClass) {
		iHeaderStyleClass = headerStyleClass;
	}
	/**
	 * @param inputStyleClass The inputStyleClass to set.
	 */
	public void setInputStyleClass(String inputStyleClass) {
		iInputStyleClass = inputStyleClass;
	}
	/**
	 * @param linkStyleClass The linkStyleClass to set.
	 */
	public void setLinkStyleClass(String linkStyleClass) {
		iLinkStyleClass = linkStyleClass;
	}
	/**
	 * @param radioStyleClass The radioStyleClass to set.
	 */
	public void setRadioStyleClass(String radioStyleClass) {
		iRadioStyleClass = radioStyleClass;
	}
	/**
	 * @param textStyleClass The textStyleClass to set.
	 */
	public void setTextStyleClass(String textStyleClass) {
		iTextStyleClass = textStyleClass;
	}
	/**
	 * @return Returns the iPage.
	 */
	protected ICPage getPage() {
		return iPage;
	}
	/**
	 * 
	 * @param page
	 *          The page to set.
	 */
	public void setPage(ICPage page) {
		iPage = page;
	}
	
	/**
	 * @param cellpadding The cellpadding to set.
	 */
	public void setCellpadding(int cellpadding) {
		iCellpadding = cellpadding;
	}
	
	/**
	 * @param headerColumnWidth The headerColumnWidth to set.
	 */
	public void setHeaderColumnWidth(int headerColumnWidth) {
		iHeaderColumnWidth = headerColumnWidth;
	}
	
	/**
	 * @param logColor The logColor to set.
	 */
	public void setLogColor(String logColor) {
		iLogColor = logColor;
	}
	
	/**
	 * @param logColorColumnWidth The logColorColumnWidth to set.
	 */
	public void setLogColorColumnWidth(int logColorColumnWidth) {
		iLogColorColumnWidth = logColorColumnWidth;
	}
}