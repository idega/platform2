package com.idega.block.timesheet.presentation;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.idega.block.timesheet.business.TimesheetSession;
import com.idega.block.timesheet.data.Resource;
import com.idega.block.timesheet.data.ResourceHome;
import com.idega.block.timesheet.data.TimesheetEntry;
import com.idega.block.timesheet.data.TimesheetEntryHome;
import com.idega.block.timesheet.data.TimesheetProject;
import com.idega.block.timesheet.data.TimesheetProjectHome;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.FieldSet;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Legend;
import com.idega.presentation.ui.PrintButton;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;
public class Timesheet extends Block {
	private static final String ACT_SAVE_PROJECT = "save_project";
	private static final String ACT_CREATE_PROJECT = "actcreate_project";
	private static final String PRM_LINECOUNT = "idega_timesheet_entry_number_of_lines";
	private static final String PRM_DAYCOUNT = "idega_timesheet_entry_number_of_days";
	private static final String PRM_YEAR = "idega_timesheet_entry_ar";
	private static final String PRM_MONTH = "idega_timesheet_entry_manudur";
	private static final String PRM_DAY = "idega_timesheet_entry_dagur";
	private static final String ACT_PAST_ENTRY_CHECK = "checkPreviousEntries";
	private static final String ACT_PROJECTS_MOVE = "move_project";
	private static final String ACT_USER_PROJECTS = "my_projects";
	private static final String ACT_BOOKED = "booked";
	private static final String ACT_UNBOOKED = "unbooked";
	private static final String ACT_PROJECT_HOURS_ALL = "hour_pr_project_all";
	private static final String ACT_PROJECT_HOURS = "hour_pr_project";
	private static final String ACT_EMLOYEE_HOURS = "hour_pr_employee";
	private static final String ACT_PROJECT_ENTRIES_ALL = "hreyfingVerkAll";
	private static final String ACT_PROJECT_ENTRIES = "hreyfingVerk";
	private static final String ACT_EMPLOYEE_ENTRIES = "hreyfingStarfsmann";
	private static final String ACT_SUBREPORT = "undirskyrsla";
	private static final String ACT_REMOVE = "henda";
	private static final String PRM_CORRECTION = "i_timesheet_correction";
	private static final String PRM_PROJECT_ID = "i_timesheet_project_id";
	private static final String PRM_PRINTABLE = "i_timesheet_printable";
	private static final String PRM_ACTION = "idega_timesheet_entry_edit";
	private static final String ENTRY_ID = "idega_timesheet_entry_timesheet_entry_id";
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.timesheet";
	private IWResourceBundle iwrb;
	private IWBundle iwb;
	private boolean bookAllAtOnce = true;
	private boolean displayReportButton = true;
	private boolean allowCorrection = false;
	private boolean isPrintable = false;
	String timesheet_project_id = null;
	private boolean isAdmin;
	//private String URI;
	private int daysShown;
	private int extraLines;
	public int month, year, day;
	private int daysInMonth;
	private Date fromDate, toDate;
	private Integer projectID = null;
	private com.idega.util.IWCalendar FunctColl = new com.idega.util.IWCalendar();
	private int days;
	private String monthName;
	private Text myDags;
	private String nameOfMonth;
	private User user;
	private Integer userID;
	private String temp_user_id;
	private String table_width;
	private String table_height;
	private int cellspacing;
	private int cellpadding;
	private String color_1;
	private String color_2;
	private String header_color;
	private String header_text_color;
	private int border;
	private boolean correct;
	private String edit;
	//        private String URI = "";
	private String hour_report_image_url;
	private String delete_image_url;
	private String report_image_url;
	private String next_image_url;
	private String previous_image_url;
	private String today_image_url;
	private String correction_image_url;
	private String back_image_url;
	private String save_image_url;
	private String employee_report_image_url;
	private String book_image_url;
	private String booked_image_url;
	private String register_image_url;
	private String language = "IS";
	private IWTimestamp stamp;
	private int fjLinuIToflu = 40;
	//////////////////////////////////// STRENGIRNIR
	private String report_string = " ";
	private String day_string = " ";
	private String resource_string = " ";
	private String project_string = " ";
	private String quantity_string = " ";
	private String description_string = " ";
	private String delete_string = " ";
	private String total_hours_string = " ";
	private String previous_week_string = " ";
	private String next_week_string = " ";
	private String correct_string = " ";
	private String back_string = " ";
	private String today_string = " ";
	private String save_string = " ";
	private String total_string = " ";
	private String previous_month_string = " ";
	private String next_month_string = " ";
	private String employee_report_string = " ";
	private String project_id_string = " ";
	private String hours_string = " ";
	private String project_name_string = " ";
	private String must_login = " ";
	private int userDefinedProjectId = -1;
	private TimesheetSession timesheetSession = null;
	public synchronized Object clone() {
		Timesheet obj = null;
		try {
			obj = (Timesheet) super.clone();
			//      obj.FunctColl = this.FunctColl.clone();
			if (this.myDags != null) {
				obj.myDags = (Text) this.myDags.clone();
			}
			if (this.header_color != null) {
				obj.header_color = this.header_color;
			}
			if (this.header_text_color != null) {
				obj.header_text_color = this.header_text_color;
			//      if (this.stamp != null)
			//        obj.stamp = (IWTimestamp)stamp.clone();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}
	///////////////////////////////////
	public Timesheet() {
		super();
		setDefaultValues();
		setDate();
	}
	public Timesheet(boolean isAdmin) {
		super();
		setDefaultValues();
		this.isAdmin = isAdmin;
		setDate();
	}
	public Timesheet(int year, int month, int day) {
		super();
		setDefaultValues();
		this.year = year;
		this.month = month;
		this.day = day;
	}
	public Timesheet(int year, int month, int day, boolean isAdmin) {
		super();
		setDefaultValues();
		this.year = year;
		this.month = month;
		this.day = day;
		this.isAdmin = isAdmin;
	}
	private void setDefaultValues() {
		this.daysShown = 6;
		this.extraLines = 1;
		this.table_width = "20";
		this.cellspacing = 0;
		this.cellpadding = 3;
		this.color_1 = "#DDDDDD";
		this.color_2 = "#444444";
		this.header_color = "#000000";
		this.header_text_color = "#FFFFFF";
		this.border = 0;
		this.language = "IS";
		this.correct = false;
	}
	private void setImageUrls(IWContext iwc) {
		this.hour_report_image_url = this.iwrb.getImageURI("");
		this.delete_image_url = this.iwrb.getImageURI("delete.gif");
		this.report_image_url = this.iwrb.getImageURI("reports.gif");
		this.next_image_url = this.iwrb.getImageURI("next.gif");
		this.previous_image_url = this.iwrb.getImageURI("prev.gif");
		this.today_image_url = this.iwrb.getImageURI("today.gif");
		this.correction_image_url = this.iwrb.getImageURI("correction.gif");
		this.back_image_url = this.iwrb.getImageURI("back.gif");
		this.save_image_url = this.iwrb.getImageURI("save.gif");
		this.employee_report_image_url = this.iwrb.getImageURI("employee.gif");
		this.book_image_url = this.iwrb.getImageURI("");
		this.booked_image_url = this.iwrb.getImageURI("");
		this.register_image_url = this.iwrb.getImageURI("");
	}
	private void setStrings(IWContext iwc) {
		this.report_string = this.iwrb.getLocalizedString("reports", "Reports");
		this.day_string = this.iwrb.getLocalizedString("date", "Date");
		this.resource_string = this.iwrb.getLocalizedString("resource", "Resource");
		this.project_string = this.iwrb.getLocalizedString("project", "Project");
		this.quantity_string = this.iwrb.getLocalizedString("quantity", "Qty.");
		this.description_string = this.iwrb.getLocalizedString("description", "Description");
		this.delete_string = this.iwrb.getLocalizedString("delete", "Delete");
		this.total_hours_string = this.iwrb.getLocalizedString("total_hours", "Total hours");
		this.previous_week_string = this.iwrb.getLocalizedString("prev_week", "Previous week");
		this.next_week_string = this.iwrb.getLocalizedString("next_week", "Next week");
		this.correct_string = this.iwrb.getLocalizedString("correction", "Correction");
		this.back_string = this.iwrb.getLocalizedString("back", "Back");
		this.today_string = this.iwrb.getLocalizedString("today", "Today");
		this.save_string = this.iwrb.getLocalizedString("save", "Save");
		this.total_string = this.iwrb.getLocalizedString("total", "Total");
		this.previous_month_string = this.iwrb.getLocalizedString("prev_month", "Previous month");
		this.next_month_string = this.iwrb.getLocalizedString("next_month", "Next month");
		this.employee_report_string = this.iwrb.getLocalizedString("employee_report", "Employee report");
		this.project_id_string = this.iwrb.getLocalizedString("project_id", "Project #");
		this.hours_string = this.iwrb.getLocalizedString("hours", "Hours");
		this.project_name_string = this.iwrb.getLocalizedString("nafn", "Name");
		this.must_login = this.iwrb.getLocalizedString("warning.not_logged_on", "You have to log on first");
	}
	/*
	 * public void setLanguage(String language) { this.language = language }
	 */
	public void setBookAllAtOnce(boolean bookAtOnce) {
		this.bookAllAtOnce = bookAtOnce;
	}
	public void setDisplayReportButton(boolean display) {
		this.displayReportButton = display;
	}
	public void setUserDefinedProjectId(int projectId) {
		this.userDefinedProjectId = projectId;
	}
	public void setAllowCorrection(boolean allowCorrection) {
		this.allowCorrection = allowCorrection;
	}
	public void setEmployeeReportImageUrl(String url) {
		this.employee_report_image_url = url;
	}
	public void setBookImageUrl(String url) {
		this.book_image_url = url;
	}
	public void setBookedImageUrl(String url) {
		this.booked_image_url = url;
	}
	public void setRegisterImageUrl(String url) {
		this.register_image_url = url;
	}
	public void setDeleteImageUrl(String url) {
		this.delete_image_url = url;
	}
	public void setSaveImageUrl(String url) {
		this.save_image_url = url;
	}
	public void setBackImageUrl(String url) {
		this.back_image_url = url;
	}
	public void setNextImageUrl(String url) {
		this.next_image_url = url;
	}
	public void setCorrectionImageUrl(String url) {
		this.correction_image_url = url;
	}
	public void setPreviousImageUrl(String url) {
		this.previous_image_url = url;
	}
	public void setTodayImageUrl(String url) {
		this.today_image_url = url;
	}
	public void setReportImageUrl(String url) {
		this.report_image_url = url;
	}
	public void setHourReportImageUrl(String url) {
		this.hour_report_image_url = url;
	}
	public void setDaysShown(int number_of_days_shown) {
		this.daysShown = number_of_days_shown;
	}
	public void setExtraLines(int number_if_extra_lines) {
		this.extraLines = number_if_extra_lines;
	}
	public void setWidth(String width) {
		this.table_width = width;
	}
	public void setHeight(int height) {
		this.table_height = Integer.toString(height);
	}
	public void setCellpadding(int cellpadding) {
		this.cellpadding = cellpadding;
	}
	public void setCellspacing(int cellspacing) {
		this.cellspacing = cellspacing;
	}
	public void setColor(String color) {
		this.color_1 = color;
		this.color_2 = color;
	}
	public void setZebraColors(String color1, String color2) {
		this.color_1 = color1;
		this.color_2 = color2;
	}
	public void setHeaderColor(String header_color) {
		this.header_color = header_color;
	}
	public void setHeaderTextColor(String header_text_color) {
		this.header_text_color = header_text_color;
	}
	public void setBorder(int border) {
		this.border = border;
	}
	private void initialize(IWContext iwc) throws Exception {
		this.timesheetSession = (TimesheetSession) IBOLookup.getSessionInstance(iwc, TimesheetSession.class);
		this.isAdmin = iwc.hasEditPermission(this);
		this.iwb = getBundle(iwc);
		this.iwrb = getResourceBundle(iwc);
		this.save_image_url = this.iwrb.getImage("save.gif").getURL();
		initDays(iwc);
		calculate(iwc);
		setStrings(iwc);
		setImageUrls(iwc);
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	public void main(IWContext iwc) throws SQLException, IOException, Exception {
		initialize(iwc);
		if (this.isAdmin) {
			Link prodMan = new Link(this.iwrb.getLocalizedString("project_manager", "Project manager"));
			//prodMan.setWindowToOpen(com.idega.block.projectmanager.presentation.ProjectAdminWindow.class);
			add(prodMan);
			add(Text.getBreak());
		}
		String sIsPrintable = iwc.getParameter(PRM_PRINTABLE);
		this.isPrintable = (sIsPrintable != null && sIsPrintable.equalsIgnoreCase("true"));
		if (iwc.isParameterSet(PRM_PROJECT_ID)) {
			this.projectID = Integer.valueOf(iwc.getParameter(PRM_PROJECT_ID));
		}
		/*
		 * if (sIsPrintable != null &&) { if
		 * (sIsPrintable.equalsIgnoreCase("true")) { this.isPrintable = true; }
		 */
		this.timesheet_project_id = iwc.getParameter(PRM_PROJECT_ID);
		if (this.edit == null) {
			this.edit = iwc.getParameter(PRM_ACTION);
		}
		if (this.edit == null) {
			this.edit = "";
		}
		if (iwc.isLoggedOn()) {
			this.user = iwc.getCurrentUser();
			this.userID = (Integer) this.user.getPrimaryKey();
			String temp_member_id = iwc.getParameter("i_timesheet_member_id");
			if (temp_member_id != null) {
				try {
					if (this.isAdmin) {
						this.user =
							((UserHome) IDOLookup.getHome(User.class)).findByPrimaryKey(Integer.valueOf(this.temp_user_id));
						this.userID = (Integer) this.user.getPrimaryKey();
					}
				}
				catch (Exception e) {
				}
			}
			if (this.edit.equals(PRM_CORRECTION)) {
				this.correct = true;
			}
			if (this.edit.equals(ACT_REMOVE)) {
				processEntryRemoval(iwc);
				presentateToday(iwc);
			}
			else if (this.edit.equals(this.save_string)) {
				processEntrySave(iwc);
				presentateToday(iwc);
			}
			else if (this.edit.equals(ACT_SUBREPORT)) {
				presentateSubreport(iwc);
				if (!this.isPrintable) {
					//add(getPrintableLink("undirskyrsla"));
				}
				else {
					add(getPrintButton());
				}
			}
			else if (this.edit.equals(ACT_EMPLOYEE_ENTRIES)) {
				presentateEmployeeEntries(iwc);
				if (!this.isPrintable) {
					//add(getPrintableLink("hreyfingStarfsmann"));
				}
				else {
					add(getPrintButton());
				}
			}
			else if (this.edit.equals(ACT_PROJECT_ENTRIES)) {
				presentateProjectEntries(iwc);
				if (!this.isPrintable) {
					//add(getPrintableLink("hreyfingVerk"));
				}
				else {
					add(getPrintButton());
				}
			}
			else if (this.edit.equals(ACT_PROJECT_ENTRIES_ALL)) {
				presentateAllProjectEntries(iwc);
				if (!this.isPrintable) {
					// add(getPrintableLink("hreyfingVerkAll"));
				}
				else {
					add(getPrintButton());
				}
			}
			else if (this.edit.equals(ACT_EMLOYEE_HOURS)) {
				presentateEmployeeHours(iwc);
				if (!this.isPrintable) {
					//add(getPrintableLink("hour_pr_employee"));
				}
				else {
					add(getPrintButton());
				}
			}
			else if (this.edit.equals(ACT_PROJECT_HOURS)) {
				presentateProjectHours(iwc);
				if (!this.isPrintable) {
					// add(getPrintableLink("hour_pr_project"));
				}
				else {
					add(getPrintButton());
				}
			}
			else if (this.edit.equals(ACT_PROJECT_HOURS_ALL)) {
				presentateAllProjectHours(iwc);
				if (!this.isPrintable) {
					//add(getPrintableLink("hour_pr_project_all"));
				}
				else {
					add(getPrintButton());
				}
			}
			else if (this.edit.equals(ACT_UNBOOKED)) {
				presentateUnbookedEntries(iwc, false);
				if (!this.isPrintable) {
					// add(getPrintableLink("unbooked"));
				}
				else {
					add(getPrintButton());
				}
			}
			else if (this.edit.equals(ACT_BOOKED)) {
				presentateBookedEntries(iwc);
				if (!this.isPrintable) {
					// add(getPrintableLink("booked"));
				}
				else {
					add(getPrintButton());
				}
			}
			else if (this.edit.equals("save_booked")) {
				processBooking(iwc);
				presentateBookedEntries(iwc);
			}
			else if (this.edit.equals("save_registered")) {
				processRegistration(iwc);
				presentateUserProjects(iwc);
			}
			else if (this.edit.equals(ACT_USER_PROJECTS)) {
				presentateUserProjects(iwc);
			}
			else if (this.edit.equals(ACT_PROJECTS_MOVE)) {
				processProjectMove(iwc);
				presentateUserProjects(iwc);
			}
			else if (this.edit.equals(ACT_PAST_ENTRY_CHECK)) {
				presentateUnbookedEntries(iwc, true);
				if (!this.isPrintable) {
					//add(getPrintableLink("checkPreviousEntries"));
				}
				else {
					add(getPrintButton());
				}
			}
			else if (this.edit.equals(ACT_CREATE_PROJECT)) {
				presentateProjectForm(iwc);
			}
			else if (this.edit.equals(ACT_SAVE_PROJECT)) {
				processProjectSave(iwc);
				presentateUserProjects(iwc);
			}
			else {
				presentateToday(iwc);
			}
			add(getMyProjectsLink());
		}
		else {
			add(this.must_login);
		}
	}
	private void setDate() {
		this.month = this.FunctColl.getMonth();
		this.year = this.FunctColl.getYear();
		this.day = this.FunctColl.getDay();
	}
	private void initDays(IWContext iwc) {
		try {
			String temp_dagur = iwc.getParameter(PRM_DAY);
			String temp_manudur = iwc.getParameter(PRM_MONTH);
			String temp_ar = iwc.getParameter(PRM_YEAR);
			if (temp_manudur != null) {
				this.month = Integer.parseInt(temp_manudur);
			}
			if (temp_dagur != null) {
				this.day = Integer.parseInt(temp_dagur);
			}
			if (temp_ar != null) {
				this.year = Integer.parseInt(temp_ar);
			}
		}
		catch (NumberFormatException n) {
		}
		if (this.day > this.FunctColl.getLengthOfMonth(this.month, this.year)) {
			this.day = this.day - this.FunctColl.getLengthOfMonth(this.month, this.year);
			++this.month;
			if (this.month == 13) {
				this.month = 1;
				++this.year;
			}
		}
		if ((this.day) < 1) {
			--this.month;
			if (this.month == 0) {
				this.month = 12;
				--this.year;
			}
			this.day = this.FunctColl.getLengthOfMonth(this.month, this.year) + this.day;
		}
		if (this.month == 13) {
			this.month = 1;
			++this.year;
		}
		else if (this.month == 0) {
			this.month = 12;
			--this.year;
		}
		this.daysInMonth = this.FunctColl.getLengthOfMonth(this.month, this.year);
		this.fromDate = new IWTimestamp(1, this.month, this.year).getDate();
		this.toDate = new IWTimestamp(this.daysInMonth, this.month, this.year).getDate();
	}
	private void calculate(IWContext iwc) {
		String temp_daysShown = iwc.getParameter(PRM_DAYCOUNT);
		if (temp_daysShown != null) {
			try {
				this.daysShown = Integer.parseInt(temp_daysShown);
			}
			catch (NumberFormatException n) {
			}
		}
		String temp_extraLines = iwc.getParameter(PRM_LINECOUNT);
		if (temp_extraLines != null) {
			try {
				this.extraLines = Integer.parseInt(temp_extraLines);
			}
			catch (NumberFormatException n) {
			}
		}
		this.days = this.FunctColl.getLengthOfMonth(this.month, this.year);
		this.monthName = this.FunctColl.getMonthName(this.month);
		//		String plus_lina = request.getParameter("nylina");
		this.days = this.FunctColl.getLengthOfMonth(this.month, this.year);
		this.nameOfMonth = (this.FunctColl.getMonthName(this.month));
		this.myDags = new Text();
		this.myDags.setFontColor(this.header_text_color);
		this.myDags.setFontSize(3);
		this.myDags.setBold();
		if ((this.day - this.daysShown) < 1) {
			int mon = this.month - 1;
			if (mon == 0) {
				mon = 12;
			}
			this.myDags.addToText(this.FunctColl.getMonthName(mon, iwc.getCurrentLocale(), IWCalendar.LONG) + "/");
		}
		this.myDags.addToText(this.FunctColl.getMonthName(this.month, iwc.getCurrentLocale(), IWCalendar.LONG) + " " + this.year);
	}
	private void presentateToday(IWContext iwc) throws Exception {
		boolean fridagur;
		String eining = this.iwrb.getLocalizedString("hour", "hr");
		boolean skrifaDags = true;
		int current_row = 1;
		int vikuDagurNr;
		double heildartimar = 0;
		double timaridag = 0;
		String dags;
		/////////////////
		DropdownMenu resources = new DropdownMenu();
		resources.setName("resource");
		resources.addMenuElement(-1, this.user.getName());
		Collection res = this.timesheetSession.getResources();
		if (res != null && !res.isEmpty()) {
			String the_name;
			for (Iterator iter = res.iterator(); iter.hasNext();) {
				Resource resource = (Resource) iter.next();
				the_name = resource.getName();
				if (the_name.length() > 30) {
					resources.addMenuElement(resource.getPrimaryKey().toString(), the_name.substring(0, 30) + "...");
				}
				else {
					resources.addMenuElement(resource.getPrimaryKey().toString(), the_name);
				}
			}
		}
		DropdownMenu projectSelect = new DropdownMenu();
		if (this.userDefinedProjectId == -1) {
			projectSelect.setName("projects");
			Collection projects = getTimesheetProjectHome().findUserRelated(this.user);
			if (projects != null && !projects.isEmpty()) {
				String the_name;
				for (Iterator iter = projects.iterator(); iter.hasNext();) {
					TimesheetProject project = (TimesheetProject) iter.next();
					the_name = project.getName();
					if (the_name.length() > 30) {
						projectSelect.addMenuElement(
							project.getPrimaryKey().toString() + "",
							the_name.substring(0, 30) + "...");
					}
					else {
						projectSelect.addMenuElement(project.getPrimaryKey().toString() + "", the_name);
					}
				}
			}
		}
		DropdownMenu myDropdownTimar = new DropdownMenu("timar");
		myDropdownTimar.addMenuElement("null", "&nbsp;");
		if (this.correct) {
			for (int i = 0; i <= 24; i++) {
				if (i != 0) {
					myDropdownTimar.addMenuElement("-" + i + ".0", "-" + i + ".0");
				}
				if (i != 24) {
					myDropdownTimar.addMenuElement("-" + i + ".5", "-" + i + ".5");
				}
			}
		}
		else {
			for (int i = 0; i <= 24; i++) {
				if (i != 0) {
					myDropdownTimar.addMenuElement(i + ".0", i + ".0");
				}
				if (i != 24) {
					myDropdownTimar.addMenuElement(i + ".5", i + ".5");
				}
			}
		}
		/////////////////////////
		User user = ((UserHome) IDOLookup.getHome(User.class)).findByPrimaryKey(this.userID);
		Form myForm = new Form();
		Table headerTable = this.getHeaderTable();
		headerTable.add(this.myDags, 2, 1);
		myForm.add(headerTable);
		//                add(myDags);
		Table myTable = new Table();
		myForm.add(myTable);
		myTable.setWidth(this.table_width);
		if (this.table_height != null) {
			myTable.setHeight(this.table_height);
		}
		myTable.setCellspacing(this.cellspacing);
		myTable.setCellpadding(this.cellpadding);
		myTable.setColor(this.color_1);
		myTable.setBorder(this.border);
		Text dagur_text = new Text(this.day_string + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		dagur_text.setFontColor(this.header_text_color);
		Text fordi_text = new Text(this.resource_string);
		fordi_text.setFontColor(this.header_text_color);
		Text verk_text = new Text(this.project_string);
		verk_text.setFontColor(this.header_text_color);
		Text eining_text = new Text(this.quantity_string);
		eining_text.setFontColor(this.header_text_color);
		Text lysing_text = new Text(this.description_string);
		lysing_text.setFontColor(this.header_text_color);
		myTable.add(dagur_text, 1, 1);
		myTable.add(fordi_text, 2, 1);
		myTable.add(verk_text, 3, 1);
		myTable.add(eining_text, 4, 1);
		myTable.add(lysing_text, 5, 1);
		myTable.setColumnAlignment(6, "center");
		TimesheetProject project;
		Resource resource;
		for (int u = 0; u <= this.daysShown; u++) {
			skrifaDags = true;
			fridagur = this.FunctColl.isHoliday(this.year, this.month, (this.day - u));
			if ((this.day - u) < 1) {
				--this.month;
				if (this.month == 0) {
					this.month = 12;
					--this.year;
				}
				this.day = this.FunctColl.getLengthOfMonth(this.month, this.year) + this.day;
			}
			vikuDagurNr = this.FunctColl.getDayOfWeek(this.year, this.month, (this.day) - u);
			dags = (this.year + "-" + this.month + "-" + (this.day - u));
			IWTimestamp date = new IWTimestamp(this.day - u, this.month, this.year);
			if ((this.month < 10) && (this.day - u < 10)) {
				dags = (this.year + "-0" + this.month + "-0" + (this.day - u));
			}
			else if ((this.month >= 10) && (this.day - u < 10)) {
				dags = (this.year + "-" + this.month + "-0" + (this.day - u));
			}
			else if ((this.month < 10) && (this.day - u >= 10)) {
				dags = (this.year + "-0" + this.month + "-" + (this.day - u));
			}
			java.util.Collection entries =
				((TimesheetEntryHome) IDOLookup.getHome(TimesheetEntry.class)).findByDateAndUser(
					date.getDate(),
					(this.userID));
			if (entries != null && !entries.isEmpty()) {
				for (Iterator iter = entries.iterator(); iter.hasNext();) {
					TimesheetEntry timesheetEntry = (TimesheetEntry) iter.next();
					project = timesheetEntry.getProject();
					int resource_id = 0;
					boolean resource_id_null = false;
					if (project != null) {
						++current_row;
						if (skrifaDags) {
							Text myText1 = new Text();
							myText1.setFontSize(1);
							if (fridagur) {
								myText1.setFontColor("red");
							}
							else {
							}
							myText1.addToText(
								this.FunctColl.getDayName(vikuDagurNr, iwc.getCurrentLocale(), IWCalendar.LONG).substring(
									0,
									3)
									+ " "
									+ (this.day - u)
									+ ".");
							myTable.add(myText1, 1, current_row);
						}
						skrifaDags = false;
						try {
							resource_id = timesheetEntry.getResourceId();
						}
						catch (Exception e) {
							resource_id_null = true;
						}
						if (resource_id_null) {
							myTable.add(user.getName(), 2, current_row);
						}
						if (project.getName().length() > 27) {
							myTable.addText(project.getName().substring(0, 26) + "...", 3, current_row);
						}
						else {
							myTable.addText(project.getName(), 3, current_row);
						}
						if (!resource_id_null) {
							if ((resource_id != -1) && (resource_id != 0)) {
								resource = getResourceHome().findByPrimaryKey(new Integer(resource_id));
								myTable.add(resource.getName(), 2, current_row);
								if (resource.getUnitName() != null) {
									eining = resource.getUnitName();
								}
							}
							else {
								myTable.add(user.getName(), 2, current_row);
							}
						}
						myTable.addText(timesheetEntry.getQuantity() + " " + eining, 4, current_row);
						if (eining.equalsIgnoreCase("klst")) {
							heildartimar += timesheetEntry.getQuantity();
							timaridag += timesheetEntry.getQuantity();
						}
						if (timesheetEntry.getDescription().length() > 30) {
							myTable.addText(timesheetEntry.getDescription().substring(0, 30) + "...", 5, current_row);
						}
						else {
							myTable.addText(timesheetEntry.getDescription(), 5, current_row);
						}
						if (!(timesheetEntry.isBooked())) {
							if (this.delete_image_url != null) {
								Image myTunna = new Image(this.delete_image_url, "Henda f�rslu");
								Link hlekkur = new Link(myTunna);
								setLink(hlekkur, ACT_REMOVE, 0, 0, 0);
								hlekkur.addParameter("idega_timesheet_entry_resource_id", resource_id);
								hlekkur.addParameter(ENTRY_ID, timesheetEntry.getPrimaryKey().toString());
								myTable.add(hlekkur, 6, current_row);
							}
							else {
								Link hlekkur = new Link(this.delete_string);
								setLink(hlekkur, ACT_REMOVE, 0, 0, 0);
								hlekkur.addParameter("idega_timesheet_entry_resource_id", resource_id);
								hlekkur.addParameter(ENTRY_ID, timesheetEntry.getPrimaryKey().toString());
								myTable.add(hlekkur, 6, current_row);
							}
						}
						else {
						}
					}
				}
			}
			/*
			 * if (u == fjLinuIToflu) { 2; myTable.resize(6,fjLinuIToflu); } }
			 */
			for (int y = 1; y <= this.extraLines; y++) {
				++current_row;
				if (skrifaDags) {
					Text myTextAuka1 = new Text();
					myTextAuka1.setFontSize(1);
					if (fridagur) {
						myTextAuka1.setFontColor("red");
					}
					else {
					}
					myTextAuka1.addToText(
						this.FunctColl.getDayName(vikuDagurNr, iwc.getCurrentLocale(), IWCalendar.LONG).substring(0, 3)
							+ " "
							+ (this.day - u)
							+ ".");
					myTable.add(myTextAuka1, 1, current_row);
				}
				skrifaDags = false;
				myTable.add(resources, 2, current_row);
				if (this.userDefinedProjectId == -1) {
					myTable.add(projectSelect, 3, current_row);
				}
				else {
					TimesheetProject userProject =
						getTimesheetProjectHome().findByPrimaryKey(new Integer(this.userDefinedProjectId));
					myTable.add(userProject.getName(), 3, current_row);
					myTable.add(new HiddenInput("projects", "" + this.userDefinedProjectId), 3, current_row);
				}
				myTable.add(myDropdownTimar, 4, current_row);
				TextInput myTextInputComment = new TextInput("description");
				myTextInputComment.setSize(30);
				myTable.add(myTextInputComment, 5, current_row);
				myTable.add(new HiddenInput("timesheet_entry_id", "null"), 6, current_row);
				myTable.add(new HiddenInput("timesheet_date", dags), 6, current_row);
				myTable.add(new HiddenInput("resource_id", "0"), 6, current_row);
			}
			++current_row;
			for (int j = 1; j <= 6; j++) {
				myTable.setColor(j, current_row, this.color_2);
			}
			myTable.addText(Double.toString(timaridag), 4, current_row);
			timaridag = 0;
		} //u endar;
		++current_row;
		myTable.addText(this.total_hours_string, 3, current_row);
		myTable.addText(Double.toString(heildartimar), 4, current_row);
		myTable.add(new HiddenInput(PRM_YEAR, Integer.toString(this.year)), 6, current_row);
		myTable.add(new HiddenInput(PRM_DAY, Integer.toString(this.day)), 6, current_row);
		myTable.add(new HiddenInput(PRM_MONTH, Integer.toString(this.month)), 6, current_row);
		myTable.add(new HiddenInput(PRM_DAYCOUNT, "" + this.daysShown), 6, current_row);
		myTable.add(new HiddenInput(PRM_LINECOUNT, "" + this.extraLines), 6, current_row);
		++current_row;
		if (this.previous_image_url != null) {
			Image prev = new Image(this.previous_image_url, this.previous_week_string);
			Link back = new Link(prev);
			setLink(back, "", 0, 0, -this.daysShown - 1);
			myTable.add(back, 2, current_row);
		}
		else {
			Text previous_week_text = new Text(this.previous_week_string);
			if (this.header_text_color != null) {
				previous_week_text.setFontColor(this.header_text_color);
			}
			Link back = new Link(previous_week_text);
			setLink(back, "", 0, 0, -this.daysShown - 1);
			myTable.add(back, 2, current_row);
		}
		myTable.add("&nbsp;&nbsp;", 2, current_row);
		if (this.today_image_url != null) {
			Image todayImg = new Image(this.today_image_url, this.today_string);
			myTable.add(new Link(todayImg), 2, current_row);
		}
		else {
			Text today_text = new Text(this.today_string);
			if (this.header_text_color != null) {
				today_text.setFontColor(this.header_text_color);
			}
			myTable.add(new Link(today_text), 2, current_row);
		}
		myTable.add("&nbsp;&nbsp;", 2, current_row);
		if (this.next_image_url != null) {
			Image nextImage = new Image(this.next_image_url, this.next_week_string);
			Link forward = new Link(nextImage);
			setLink(forward, "", 0, 0, this.daysShown + 1);
			myTable.add(forward, 2, current_row);
		}
		else {
			Text next_week_text = new Text(this.next_week_string);
			if (this.header_text_color != null) {
				next_week_text.setFontColor(this.header_text_color);
			}
			Link forward = new Link(next_week_text);
			setLink(forward, "", 0, 0, this.daysShown + 1);
			myTable.add(forward, 2, current_row);
		}
		if (this.displayReportButton) {
			if (this.report_image_url == null) {
				Text report_text = new Text(this.report_string);
				if (this.header_text_color != null) {
					report_text.setFontColor(this.header_text_color);
				}
				Link reports = new Link(report_text);
				setLink(reports, ACT_SUBREPORT, 0, 0, 0);
				myTable.add(reports, 5, current_row);
			}
			else {
				Link reports = new Link(new Image(this.report_image_url, this.report_string));
				setLink(reports, ACT_SUBREPORT, 0, 0, 0);
				myTable.add(reports, 5, current_row);
			}
		}
		if (this.allowCorrection) {
			if (this.correction_image_url == null) {
				Link correct = new Link(this.iwrb.getLocalizedString("correct", "Correct"));
				this.setLink(correct, PRM_CORRECTION, 0, 0, 0);
				myTable.add(correct, 3, current_row);
			}
			else {
				Link correct = new Link(new Image(this.correction_image_url));
				this.setLink(correct, PRM_CORRECTION, 0, 0, 0);
				myTable.add(correct, 3, current_row);
			}
		}
		if (this.save_image_url != null) {
			Image saveImg = new Image(this.save_image_url, this.save_string);
			myTable.add(new SubmitButton(saveImg, PRM_ACTION, this.save_string), 5, current_row);
			myTable.add(new HiddenInput(PRM_ACTION, this.save_string), 6, current_row);
		}
		else {
			myTable.add(new SubmitButton(PRM_ACTION, this.save_string), 5, current_row);
			myTable.add(new HiddenInput(PRM_ACTION, this.save_string), 6, current_row);
		}
		myTable.setAlignment(5, current_row, "right");
		if (this.header_color != null) {
			myTable.setRowColor(1, this.header_color);
			myTable.setRowColor(current_row, this.color_2);
		}
		add(myForm);
	}
	private void presentateSubreport(IWContext iwc) throws Exception {
		Text myText;
		Text myName;
		int resource_id;
		double timarverk = 0;
		double samtals = 0;
		double[] timardag = new double[this.daysInMonth];
		Collection userProjects = getTimesheetProjectHome().findEntryRelatedByUserWithinPeriod(this.userID, this.fromDate, this.toDate);
		int rows = 0;
		Form myForm = new Form();
		add(myForm);
		Table headerTable = new Table(3, 1);
		headerTable.setCellpadding(0);
		headerTable.setCellspacing(0);
		headerTable.setColor(this.header_color);
		headerTable.setWidth(1, "17");
		headerTable.setWidth(3, "17");
		headerTable.setWidth(this.table_width);
		headerTable.setHeight("36");
		headerTable.setVerticalAlignment(1, 1, "top");
		headerTable.setVerticalAlignment(2, 1, "middle");
		headerTable.setVerticalAlignment(3, 1, "top");
		headerTable.setAlignment(1, 1, "left");
		headerTable.setAlignment(2, 1, "center");
		headerTable.setAlignment(3, 1, "right");
		//headerTable.setAlignment("center");
		headerTable.add(this.iwb.getImage("shared/leftcorner.gif"), 1, 1);
		headerTable.add(this.iwb.getImage("shared/rightcorner.gif", ""), 3, 1);
		//                Text header = new Text(FunctColl.getNameOfMonth(manudur, iwc) + " "
		// +ar);
		//                  header.setFontSize(5);
		//                add(header);
		Text nafnPaMoned =
			new Text(
				"Unnir t�mar � " + this.FunctColl.getMonthName(this.month, iwc.getCurrentLocale(), IWCalendar.LONG) + " " + this.year);
		nafnPaMoned.setFontSize(3);
		nafnPaMoned.setBold();
		nafnPaMoned.setFontColor(this.header_text_color);
		Text memberName = new Text(this.user.getName());
		memberName.setFontSize(3);
		memberName.setBold();
		memberName.setFontColor(this.header_text_color);
		headerTable.add(memberName, 2, 1);
		headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 2, 1);
		headerTable.add(nafnPaMoned, 2, 1);
		headerTable.setNoWrap();
		myForm.add(headerTable);
		Table myTable = new Table(this.daysInMonth + 4, rows + 3);
		myForm.add(myTable);
		myTable.setBorder(this.border);
		//                myTable.setBorder(1);
		myTable.setWidth(this.table_width);
		myTable.setColor(this.color_1);
		//                myTable.setHorizontalZebraColored(color_1,color_2);
		myTable.setCellpadding(2);
		myTable.setCellspacing(0);
		myTable.setNoWrap();
		Text project_id_text = new Text(this.project_string);
		project_id_text.setFontColor(this.header_text_color);
		Text project_name_text =
			new Text(this.project_name_string + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		project_name_text.setFontColor(this.header_text_color);
		//		myTable.add(project_id_text,1,1);
		myTable.add(project_name_text, 2, 1);
		myTable.setColumnAttribute(this.daysInMonth + 3, "align", "center");
		for (int j = 1; j <= this.daysInMonth; j++) {
			myText = new Text(Integer.toString(j));
			//                        myText.setFontSize(1);
			myText.setFontColor(this.header_text_color);
			myTable.setWidth(2 + j, "17");
			if (this.FunctColl.isHoliday(this.year, this.month, j)) {
				myText.setFontColor("red");
			}
			myTable.add(myText, 2 + j, 1);
			timardag[j - 1] = 0;
		}
		Text samtals_text1 = new Text(this.total_string);
		samtals_text1.setFontColor(this.header_text_color);
		myTable.add(samtals_text1, this.daysInMonth + 3, 1);
		int currentrow = 1;
		Collection insideEntries = null;
		TimesheetEntryHome entryHome = getTimesheetEntryHome();
		IWTimestamp todayStamp;
		TimesheetProject project;
		double tala;
		double how_many_today;
		for (Iterator iter = userProjects.iterator(); iter.hasNext();) {
			project = (TimesheetProject) iter.next();
			timarverk = 0;
			String project_name = project.getName();
			if (project_name != null) {
				if (project_name.length() > 15) {
					project_name = project_name.substring(0, 15) + "...";
				}
			}
			myName = new Text(project_name);
			myName.setFontSize(1);
			Link myLinkName = new Link(myName);
			setLink(myLinkName, ACT_PROJECT_ENTRIES, 0, 0, 0);
			myLinkName.addParameter(PRM_PROJECT_ID, project.getPrimaryKey().toString());
			currentrow++;
			myTable.add(myLinkName, 2, currentrow);
			for (int j = 1; j <= this.daysInMonth; j++) {
				todayStamp = new IWTimestamp(j, this.month, this.year);
				myTable.setColumnAttribute(j + 2, "align", "center");
				insideEntries = entryHome.findByDateAndUser(todayStamp.getDate(), this.userID);
				myTable.setVerticalAlignment(2 + j, currentrow, "top");
				//if (inside_entry != null) {
				if (insideEntries != null && !insideEntries.isEmpty()) {
					how_many_today = 0;
					myText = new Text("0");
					myText.setFontSize(1);
					for (Iterator iterator = insideEntries.iterator(); iterator.hasNext();) {
						TimesheetEntry entry = (TimesheetEntry) iterator.next();
						resource_id = entry.getResourceId();
						if ((resource_id == 0) || (resource_id == -1)) {
							tala = entry.getQuantity();
							how_many_today += tala;
						}
					}
					if (how_many_today != 0) {
						myText.setText(Double.toString(how_many_today));
					}
					else {
						myText.setFontColor("gray");
					}
					myTable.add(myText, 2 + j, currentrow);
					timarverk += how_many_today;
					timardag[j - 1] += how_many_today;
				}
				else {
					myTable.addText("&nbsp; ", 2 + j, currentrow);
				}
			}
			myText = new Text(Double.toString(timarverk));
			myText.setBold();
			myText.setFontSize("1");
			myTable.setVerticalAlignment(this.daysInMonth + 3, currentrow, "top");
			myTable.add(myText, this.daysInMonth + 3, currentrow);
			samtals += timarverk;
		}
		Text samtals_text = new Text(this.total_string);
		samtals_text.setFontSize(1);
		myTable.add(samtals_text, 2, rows + 2);
		myTable.setAttribute(2, rows + 2, "align", "right");
		for (int k = 1; k <= this.daysInMonth; k++) {
			if (timardag[k - 1] != 0) {
				myText = new Text(Double.toString(timardag[k - 1]));
				myText.setFontSize("1");
				myText.setFontColor("black");
				myText.setBold();
				myTable.add(myText, 2 + k, rows + 2);
			}
			else {
				myText = new Text("0");
				myText.setFontSize("1");
				myText.setFontColor("gray");
				myTable.add(myText, 2 + k, rows + 2);
			}
		}
		myText = new Text(Double.toString(samtals));
		myText.setFontSize(1);
		myText.setBold();
		myTable.setAlignment(this.daysInMonth + 3, rows + 2, "center");
		myTable.add(myText, this.daysInMonth + 3, rows + 2);
		myTable.mergeCells(1, rows + 3, 25, rows + 3);
		myTable.mergeCells(26, rows + 3, this.daysInMonth + 3, rows + 3);
		myTable.add("&nbsp;&nbsp;&nbsp;", 1, rows + 3);
		if (!this.isPrintable) {
			Link bakka = this.getPreviousMonthLink(ACT_SUBREPORT);
			Link afram = this.getNextMonthLink(ACT_SUBREPORT);
			myTable.add(bakka, 1, rows + 3);
			myTable.add(afram, 1, rows + 3);
			if (this.employee_report_image_url != null) {
				Image staffRep = new Image(this.employee_report_image_url, this.employee_report_string);
				Link starfsmanns = new Link(staffRep);
				setLink(starfsmanns, ACT_EMPLOYEE_ENTRIES, 0, 0, 0);
				myTable.add(starfsmanns, 26, rows + 3);
			}
			else {
				Link starfsmanns = new Link(this.employee_report_string);
				setLink(starfsmanns, ACT_EMPLOYEE_ENTRIES, 0, 0, 0);
				myTable.add(starfsmanns, 26, rows + 3);
			}
		}
		myForm.add(new HiddenInput("edit", "valinskyrsla"));
		myForm.add(new HiddenInput("manudur", this.month + ""));
		myForm.add(new HiddenInput("ar", this.year + ""));
		myTable.setHorizontalZebraColored(this.color_1, this.color_2);
		myTable.setRowColor(1, this.header_color);
		myTable.setRowColor(rows + 3, this.header_color);
	}
	private void presentateProjectEntries(IWContext iwc) throws Exception {
		String project_id = iwc.getParameter(PRM_PROJECT_ID);
		if (project_id != null) {
			Collection entries =
				getTimesheetEntryHome().findByUserAndProjectWithinPeriod(this.userID, this.projectID, this.fromDate, this.toDate);
			presentateProjectEntries(iwc, entries, this.projectID, false);
		}
	}
	private void presentateAllProjectEntries(IWContext iwc) throws Exception {
		String project_id = iwc.getParameter(PRM_PROJECT_ID);
		if (this.isAdmin) {
			if (project_id != null) {
				Integer projectID = Integer.valueOf(project_id);
				Collection entries = getTimesheetEntryHome().findByProjectWithinPeriod(projectID, this.fromDate, this.toDate);
				presentateProjectEntries(iwc, entries, projectID, true);
			}
		}
		else {
			presentateProjectEntries(iwc);
		}
	}
	private void presentateProjectEntries(IWContext iwc, Collection tsEntries, Integer projectID, boolean viewAll)
		throws Exception {
		String edit_string = ACT_PROJECT_ENTRIES;
		String hour_report_string = ACT_PROJECT_HOURS;
		User current_user = this.user;
		if (viewAll) {
			edit_string = ACT_PROJECT_ENTRIES_ALL;
			hour_report_string = ACT_PROJECT_HOURS_ALL;
		}
		TimesheetProject project = getTimesheetProjectHome().findByPrimaryKey(projectID);
		double vinnaSamtals = 0;
		double aksturSamtals = 0;
		//int rows = entry.length;
		int rows = tsEntries.size();
		int row = 1;
		Table headerTable = this.getHeaderTable();
		Text nafnPaMoned =
			new Text(
				this.iwrb.getLocalizedString("work_report", "Workreport")
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ this.FunctColl.getMonthName(this.month, iwc.getCurrentLocale(), IWCalendar.LONG)
					+ " "
					+ this.year);
		nafnPaMoned.setFontSize(3);
		nafnPaMoned.setBold();
		nafnPaMoned.setFontColor(this.header_text_color);
		Text projectName = new Text(project.getName());
		projectName.setFontSize(3);
		projectName.setBold();
		projectName.setFontColor(this.header_text_color);
		headerTable.add(projectName, 2, 1);
		headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 2, 1);
		headerTable.add(nafnPaMoned, 2, 1);
		add(headerTable);
		Table myTable = new Table();
		myTable.setWidth(this.table_width);
		add(myTable);
		myTable.setCellspacing(0);
		myTable.setCellpadding(0);
		myTable.setBorder(this.border);
		Text dags = new Text(this.iwrb.getLocalizedString("date", "Date"));
		Text the_member = new Text(this.iwrb.getLocalizedString("employee", "Employee"));
		Text the_resource = new Text(this.iwrb.getLocalizedString("supply", "Supply")); //"For�i");
		Text horas = new Text(this.iwrb.getLocalizedString("hours", "Hours"));
		Text miles = new Text(this.iwrb.getLocalizedString("driving", "Driving"));
		Text other = new Text(this.iwrb.getLocalizedString("equipment_usage", "Equipment usage"));
		dags.setFontColor(this.header_text_color);
		the_member.setFontColor(this.header_text_color);
		the_resource.setFontColor(this.header_text_color);
		horas.setFontColor(this.header_text_color);
		miles.setFontColor(this.header_text_color);
		other.setFontColor(this.header_text_color);
		myTable.add(dags, 2, row);
		myTable.add(the_member, 3, row);
		myTable.add(the_resource, 4, row);
		myTable.add(horas, 5, row);
		myTable.add(miles, 6, row);
		myTable.mergeCells(7, 1, 8, row);
		myTable.add(other, 7, row);
		String resource_name = null;
		String resource_unit_name = null;
		Text themDags;
		Text themMember;
		Text themResource;
		Text themHours;
		Text themMiles;
		Text themOther;
		int fontSize = 1;
		ResourceHome resHome = getResourceHome();
		Resource resource;
		for (Iterator iter = tsEntries.iterator(); iter.hasNext();) {
			TimesheetEntry entry = (TimesheetEntry) iter.next();
			Integer resourceID = new Integer(entry.getResourceId());
			resource_name = null;
			resource_unit_name = null;
			if (resourceID.intValue() > 0) {
				resource = resHome.findByPrimaryKey(resourceID);
				resource_name = resource.getName();
				resource_unit_name = resource.getUnitName();
			}
			++row;
			themDags = new Text(entry.getDate().toString().substring(0, 10));
			if (viewAll) {
				current_user = entry.getUser();
			}
			themMember = new Text(current_user.getName());
			themDags.setFontSize(fontSize);
			themMember.setFontSize(fontSize);
			myTable.add(themDags, 2, row);
			myTable.add(themMember, 3, row);
			if (resource_name != null) {
				themResource = new Text(resource_name);
				themResource.setFontSize(fontSize);
				myTable.add(themResource, 4, row);
				if (resource_unit_name != null) {
					if (resource_unit_name.equalsIgnoreCase("klst")) {
						themHours = new Text("" + entry.getQuantity());
						themHours.setFontSize(fontSize);
						myTable.add(themHours, 5, row);
						vinnaSamtals += entry.getQuantity();
					}
					else if (resource_unit_name.equalsIgnoreCase("km")) {
						themMiles = new Text("" + entry.getQuantity());
						themMiles.setFontSize(fontSize);
						myTable.add(themMiles, 6, row);
						aksturSamtals += entry.getQuantity();
					}
					else {
						themOther = new Text("" + entry.getQuantity());
						themOther.setFontSize(fontSize);
						myTable.add(themOther, 7, row);
					}
				}
			}
			else {
				myTable.add("-", 4, row);
				themHours = new Text("" + entry.getQuantity());
				themHours.setFontSize(fontSize);
				myTable.add(themHours, 5, row);
				vinnaSamtals += entry.getQuantity();
			}
		}
		row++;
		Text totalWork = new Text("" + vinnaSamtals);
		totalWork.setFontSize(fontSize);
		Text totalMiles = new Text("" + aksturSamtals);
		totalMiles.setFontSize(fontSize);
		myTable.add(totalWork, 5, row);
		myTable.add(totalMiles, 6, row);
		row++;
		myTable.mergeCells(2, row, 5, row);
		myTable.setHeight(row, "34");
		myTable.setVerticalAlignment(2, row, "middle");
		Link bakka = this.getPreviousMonthLink(edit_string);
		if (bakka != null) {
			bakka.addParameter(PRM_PROJECT_ID, projectID.toString());
		}
		Link afram = this.getNextMonthLink(edit_string);
		if (afram != null) {
			afram.addParameter(PRM_PROJECT_ID, projectID.toString());
		}
		myTable.add(bakka, 2, row);
		myTable.add(afram, 2, row);
		if (this.hour_report_image_url != null) {
			Image repImg = new Image(this.hour_report_image_url);
			Link hour_report = new Link(repImg);
			setLink(hour_report, hour_report_string, 0, 0, 0);
			hour_report.addParameter(PRM_PROJECT_ID, projectID.toString());
			myTable.add(hour_report, 7, rows + 3);
		}
		else {
			Link hour_report = new Link(this.iwrb.getLocalizedString("time_report", "Time report"));
			setLink(hour_report, hour_report_string, 0, 0, 0);
			hour_report.addParameter(PRM_PROJECT_ID, projectID.toString());
			myTable.add(hour_report, 7, rows + 3);
		}
		myTable.setHorizontalZebraColored(this.color_1, this.color_2);
		myTable.setRowColor(1, this.header_color);
		myTable.setRowColor(row, this.header_color);
	}
	private void presentateEmployeeEntries(IWContext iwc) throws Exception {
		double vinnaSamtals = 0;
		double aksturSamtals = 0;
		Collection entries = getTimesheetEntryHome().findByUserWithinPeriod(this.userID, this.fromDate, this.toDate);
		TimesheetProjectHome projectHome = getTimesheetProjectHome();
		ResourceHome resHome = getResourceHome();
		TimesheetProject project;
		int rows = 0;
		if (entries != null) {
			rows = entries.size();
		}
		Table headerTable = this.getHeaderTable();
		Text nafnPaMoned =
			new Text(this.FunctColl.getMonthName(this.month, iwc.getCurrentLocale(), IWCalendar.LONG) + " " + this.year);
		nafnPaMoned.setFontSize(3);
		nafnPaMoned.setBold();
		nafnPaMoned.setFontColor(this.header_text_color);
		Text memberName =
			new Text(
				this.iwrb.getLocalizedString("employee_report", "Employee report")
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ this.user.getName());
		memberName.setFontSize(3);
		memberName.setBold();
		memberName.setFontColor(this.header_text_color);
		headerTable.add(memberName, 2, 1);
		headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 2, 1);
		headerTable.add(nafnPaMoned, 2, 1);
		add(headerTable);
		int row = 1;
		Table myTable = new Table();
		myTable.setWidth(this.table_width);
		add(myTable);
		myTable.setCellspacing(0);
		myTable.setCellpadding(0);
		myTable.setBorder(this.border);
		myTable.setNoWrap();
		Text dags = new Text(this.iwrb.getLocalizedString("date", "Date"));
		Text the_member = new Text(this.iwrb.getLocalizedString("project", "Project"));
		Text the_resource = new Text("For�i");
		Text horas = new Text(this.iwrb.getLocalizedString("hours", "Hours"));
		Text miles = new Text(this.iwrb.getLocalizedString("driving", "Driving"));
		Text other = new Text(this.iwrb.getLocalizedString("equipment_usage", "Equipment usage"));
		dags.setFontColor(this.header_text_color);
		the_member.setFontColor(this.header_text_color);
		the_resource.setFontColor(this.header_text_color);
		horas.setFontColor(this.header_text_color);
		miles.setFontColor(this.header_text_color);
		other.setFontColor(this.header_text_color);
		myTable.add(dags, 2, row);
		myTable.add(the_member, 3, row);
		myTable.add(the_resource, 4, row);
		myTable.add(horas, 5, row);
		myTable.add(miles, 6, row);
		myTable.mergeCells(7, 1, 8, row);
		myTable.add(other, 7, row);
		String resource_name = null;
		String resource_unit_name = null;
		Text themDags;
		Text themProject;
		Text themResource;
		Text themHours;
		Text themMiles;
		Text themOther;
		int fontSize = 1;
		for (Iterator iter = entries.iterator(); iter.hasNext();) {
			TimesheetEntry entry = (TimesheetEntry) iter.next();
			project = projectHome.findByPrimaryKey(new Integer(entry.getProjectId()));
			resource_name = null;
			resource_unit_name = null;
			if (entry.getResourceId() > 0) {
				Resource resource = resHome.findByPrimaryKey(new Integer(entry.getResourceId()));
				resource_name = resource.getName();
				resource_unit_name = resource.getUnitName();
			}
			++row;
			themDags = new Text(entry.getDate().toString().substring(0, 10));
			themProject = new Text(project.getName());
			themDags.setFontSize(fontSize);
			themProject.setFontSize(fontSize);
			myTable.add(themDags, 2, row);
			myTable.add(themProject, 3, row);
			if (resource_name != null) {
				themResource = new Text(resource_name);
				themResource.setFontSize(fontSize);
				myTable.add(themResource, 4, row);
				if (resource_unit_name != null) {
					if (resource_unit_name.equalsIgnoreCase("klst")) {
						themHours = new Text("" + entry.getQuantity());
						themHours.setFontSize(fontSize);
						myTable.add(themHours, 5, row);
						vinnaSamtals += entry.getQuantity();
					}
					else if (resource_unit_name.equalsIgnoreCase("km")) {
						themMiles = new Text("" + entry.getQuantity());
						themMiles.setFontSize(fontSize);
						myTable.add(themMiles, 6, row);
						aksturSamtals += entry.getQuantity();
					}
					else {
						themOther = new Text("" + entry.getQuantity());
						themOther.setFontSize(fontSize);
						myTable.add(themOther, 7, row);
					}
				}
			}
			else {
				myTable.add("-", 4, row);
				themHours = new Text("" + entry.getQuantity());
				themHours.setFontSize(fontSize);
				myTable.add(themHours, 5, row);
				vinnaSamtals += entry.getQuantity();
			}
		}
		row++;
		Text totalWork = new Text("" + vinnaSamtals);
		totalWork.setFontSize(fontSize);
		Text totalMiles = new Text("" + aksturSamtals);
		totalMiles.setFontSize(fontSize);
		myTable.add(totalWork, 5, row);
		myTable.add(totalMiles, 6, row);
		row++;
		if (!this.isPrintable) {
			if (this.hour_report_image_url != null) {
				Image repImg = new Image(this.hour_report_image_url);
				Link hour_report = new Link(repImg);
				setLink(hour_report, ACT_EMLOYEE_HOURS, 0, 0, 0);
				myTable.add(hour_report, 7, rows + 3);
			}
			else {
				Link hour_report = new Link("T�mask�rsla");
				setLink(hour_report, ACT_EMLOYEE_HOURS, 0, 0, 0);
				myTable.add(hour_report, 7, rows + 3);
			}
		}
		Link bakka = this.getPreviousMonthLink(ACT_EMPLOYEE_ENTRIES);
		Link afram = this.getNextMonthLink(ACT_EMPLOYEE_ENTRIES);
		myTable.add(bakka, 2, rows + 3);
		myTable.add(afram, 2, rows + 3);
		myTable.setHorizontalZebraColored(this.color_1, this.color_2);
		myTable.setRowColor(1, this.header_color);
		myTable.setRowColor(rows + 3, this.header_color);
	}
	public void processEntryRemoval(IWContext iwc) throws Exception {
		String entry_id = iwc.getParameter(ENTRY_ID);
		TimesheetEntry entry = getTimesheetEntryHome().findByPrimaryKey(Integer.valueOf(entry_id));
		entry.remove();
	}
	private void processProjectSave(IWContext iwc) throws Exception {
		String projectName = iwc.getParameter("pr_name");
		String projectNumber = iwc.getParameter("pr_number");
		TimesheetProject project = getTimesheetProjectHome().create();
		project.setName(projectName);
		project.setProjectNumber(projectNumber);
		project.store();
	}
	private void processEntrySave(IWContext iwc) throws Exception {
		//		String member_id = iwc.getParameter("member_id");
		String project_id[] = iwc.getParameterValues("projects");
		String resource_id[] = iwc.getParameterValues("resource");
		String timar[] = iwc.getParameterValues("timar");
		String entry_id[] = iwc.getParameterValues("timesheet_entry_id");
		String date[] = iwc.getParameterValues("timesheet_date");
		String description[] = iwc.getParameterValues("description");
		if (entry_id != null) {
			System.out.println("");
			IWTimestamp stamp;
			String timarString = "0";
			for (int telja = 0; telja < date.length; telja++) {
				if (project_id != null) {
					if (entry_id[telja].equals("null")) {
						if (!timar[telja].equals("null")) {
							TimesheetEntry entry = getTimesheetEntryHome().create();
							stamp = new IWTimestamp(date[telja]);
							entry.setDate(stamp.getTimestamp());
							entry.setUserId(this.userID.intValue());
							if (resource_id != null) {
								if (!resource_id[telja].equals("-1")) {
									if (!resource_id[telja].equals("0")) {
										entry.setResourceId(Integer.parseInt(resource_id[telja]));
									}
								}
							}
							entry.setProjectId(Integer.parseInt(project_id[telja]));
							timarString = timar[telja];
							entry.setQuantity(Double.parseDouble(timarString));
							entry.setDescription(description[telja]);
							if ((Double.parseDouble(timarString) < 0) && (description[telja].equalsIgnoreCase(""))) {
								entry.setDescription(
									this.iwrb.getLocalizedString("corrected_by", "Corrected by ") + this.user.getName());
							}
							entry.setBooked(false);
							entry.setRegistered(false);
							//entry.insert();
							entry.store();
						}
					}
					/*
					 * else { TimesheetEntry entry =
					 * ((com.idega.block.timesheet.data.TimesheetEntryHome)com.idega.data.IDOLookup.getHomeLegacy(TimesheetEntry.class)).findByPrimaryKeyLegacy(Integer.parseInt(entry_id[telja]));
					 * 
					 * stamp = new IWTimestamp(date[telja]);
					 * 
					 * entry.setDate(stamp.getTimestamp());
					 * entry.setMemberId(member_id); if
					 * (!resource_id[telja].equals("0")) {
					 * entry.setResourceId(Integer.parseInt(resource_id[telja])); }
					 * entry.setProjectId(Integer.parseInt(project_id[telja]));
					 * 
					 * timarString = timar[telja];
					 * 
					 * entry.setHowMany(Double.parseDouble(timarString));
					 * 
					 * entry.setDescription(description[telja]);
					 * entry.setBooked(false); entry.setRegistered(false);
					 * entry.update(); }
					 */
				}
			}
		}
	}
	private void presentateEmployeeHours(IWContext iwc) throws Exception {
		int dagariman = this.FunctColl.getLengthOfMonth(this.month, this.year);
		IWCalendar cal = new IWCalendar();
		/*
		 * Days[] days = (Days[])(new Days()).findAllOrdered("days_id");
		 */
		double[] hoursPerDay = new double[8];
		/*
		 * for (int i = 0; i
		 * < days.length; i++) { hoursPerDay[days[i].getID()] =
		 * days[i].getWorkHours(); }
		 */
		double totalHours = 0;
		double totalOvertime = 0;
		double totalDay = 0;
		double totalOverhour = 0;
		String tableWidth = "0";
		try {
			tableWidth = Integer.toString(2 * (Integer.parseInt(this.table_width) / 3));
		}
		catch (NumberFormatException n) {
			tableWidth = "70%";
		}
		Table headerTable = this.getHeaderTable();
		headerTable.setWidth(tableWidth);
		Text nafnPaMoned =
			new Text(
				this.iwrb.getLocalizedString("time_report", "Timereport")
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ this.FunctColl.getMonthName(this.month, iwc.getCurrentLocale(), IWCalendar.LONG)
					+ " "
					+ this.year);
		nafnPaMoned.setFontSize(3);
		nafnPaMoned.setBold();
		nafnPaMoned.setFontColor(this.header_text_color);
		Text memberName = new Text(this.user.getName());
		memberName.setFontSize(3);
		memberName.setBold();
		memberName.setFontColor(this.header_text_color);
		headerTable.add(memberName, 2, 1);
		headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 2, 1);
		headerTable.add(nafnPaMoned, 2, 1);
		add(headerTable);
		Table table = new Table();
		table.setWidth(tableWidth);
		table.setCellpadding(0);
		table.setCellspacing(0);
		add(table);
		int row = 1;
		Text dayTxt = new Text(this.iwrb.getLocalizedString("date", "Date"));
		Text hoursTxt = new Text(this.iwrb.getLocalizedString("hours", "Hours"));
		Text dayhourTxt = new Text(this.iwrb.getLocalizedString("daytime", "Daytime"));
		Text overhourTxt = new Text(this.iwrb.getLocalizedString("overtime", "Overtime"));
		Text differenceTxt = new Text(this.iwrb.getLocalizedString("difference", "Difference"));
		dayTxt.setFontColor(this.header_text_color);
		hoursTxt.setFontColor(this.header_text_color);
		dayhourTxt.setFontColor(this.header_text_color);
		overhourTxt.setFontColor(this.header_text_color);
		differenceTxt.setFontColor(this.header_text_color);
		table.add(dayTxt, 2, row);
		table.add(hoursTxt, 3, row);
		table.add(dayhourTxt, 4, row);
		table.add(overhourTxt, 5, row);
		table.add(differenceTxt, 6, row);
		Text dayTxt_reusable;
		Text hoursTxt_reusable;
		Text dayhourTxt_reusable;
		Text overhourTxt_reusable;
		Text overtimeTxt_reusable;
		Collection entries;
		int day_of_week;
		double total_hour;
		double day_hour;
		double over_hour;
		double over_time;
		for (int i = 1; i < dagariman + 1; i++) {
			++row;
			entries = getTimesheetEntryHome().findByDateAndUser(new IWTimestamp(i, this.month, this.year).getDate(), this.userID);
			day_of_week = cal.getDayOfWeek(this.year, this.month, i);
			dayTxt_reusable = new Text(this.year + "-" + TextSoap.addZero(this.month) + "-" + TextSoap.addZero(i));
			dayTxt_reusable.setFontSize(1);
			table.add(dayTxt_reusable, 2, row);
			total_hour = 0;
			day_hour = 0;
			over_hour = 0;
			over_time = 0;
			for (Iterator iter = entries.iterator(); iter.hasNext();) {
				TimesheetEntry entry = (TimesheetEntry) iter.next();
				//total_hour += entry[j].getQuantity();
				total_hour += entry.getQuantity();
			}
			if (cal.isHoliday(this.year, this.month, i)) {
				dayTxt_reusable.setFontColor("red");
				over_hour = total_hour;
				over_time = total_hour;
			}
			else {
				if (total_hour > hoursPerDay[day_of_week]) {
					over_hour = total_hour - hoursPerDay[day_of_week];
					day_hour = hoursPerDay[day_of_week];
					over_time = total_hour - hoursPerDay[day_of_week];
				}
				else if (total_hour < hoursPerDay[day_of_week]) {
					over_hour = total_hour - hoursPerDay[day_of_week];
					day_hour = total_hour;
					over_time = 0;
				}
				else {
					day_hour = total_hour;
					over_hour = 0;
					over_time = 0;
				}
			}
			hoursTxt_reusable = new Text("" + total_hour);
			dayhourTxt_reusable = new Text("" + day_hour);
			overhourTxt_reusable = new Text("" + over_hour);
			overtimeTxt_reusable = new Text("" + over_time);
			if (total_hour == 0) {
				hoursTxt_reusable.setFontColor("gray");
			}
			if (day_hour == 0) {
				dayhourTxt_reusable.setFontColor("gray");
			}
			if (over_hour == 0) {
				overhourTxt_reusable.setFontColor("gray");
			}
			if (over_time == 0) {
				overtimeTxt_reusable.setFontColor("gray");
			}
			if (i == dagariman) {
				hoursTxt_reusable.setUnderline();
				dayhourTxt_reusable.setUnderline();
				overhourTxt_reusable.setUnderline();
				overtimeTxt_reusable.setUnderline();
			}
			hoursTxt_reusable.setFontSize(1);
			dayhourTxt_reusable.setFontSize(1);
			overhourTxt_reusable.setFontSize(1);
			overtimeTxt_reusable.setFontSize(1);
			table.add(hoursTxt_reusable, 3, row);
			table.add(dayhourTxt_reusable, 4, row);
			table.add(overtimeTxt_reusable, 5, row);
			table.add(overhourTxt_reusable, 6, row);
			totalHours += total_hour;
			totalOverhour += over_hour;
			totalOvertime += over_time;
			totalDay += day_hour;
		}
		++row;
		Text finalHours = new Text("" + totalHours);
		finalHours.setFontSize(1);
		finalHours.setBold();
		Text finalDay = new Text("" + totalDay);
		finalDay.setFontSize(1);
		finalDay.setBold();
		Text finalOvertime = new Text("" + totalOvertime);
		finalOvertime.setFontSize(1);
		finalOvertime.setBold();
		Text finalOverhour = new Text("" + totalOverhour);
		finalOverhour.setFontSize(1);
		finalOverhour.setBold();
		table.add(finalHours, 3, row);
		table.add(finalDay, 4, row);
		table.add(finalOvertime, 5, row);
		table.add(finalOverhour, 6, row);
		++row;
		Link bakka = this.getPreviousMonthLink(ACT_EMLOYEE_HOURS);
		Link afram = this.getNextMonthLink(ACT_EMLOYEE_HOURS);
		table.add(bakka, 2, row);
		table.add(afram, 2, row);
		table.setHorizontalZebraColored(this.color_1, this.color_2);
		table.setRowColor(1, this.header_color);
		table.setRowColor(row, this.header_color);
		/*
		 * from timesheet_entry where member_id="+this.member_id+" AND
		 * timesheet_entry_date >= '"+dags1+"' AND timesheet_entry_date
		 * <= '"+dags2+"' order by timesheet_entry_date,project_id"); //
		 * com.idega.data.genericentity.Member member =
		 * ((com.idega.data.genericentity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.data.genericentity.Member.class)).createLegacy();
		 * com.idega.jmodule.timesheet.data.TimesheetProject project; Days[]
		 * days = (Days[])(new Days()).findAllOrdered("days_id"); double
		 * totalHours = 0; double totalOvertime = 0; double totalDay = 0;
		 * 
		 * 
		 * double[] hoursPerDay = new double[8]; for (int i = 0; i
		 * < days.length; i++) { hoursPerDay[days[i].getID()] = days[i].getWorkHours(); }
		 * 
		 * Table headerTable = this.getHeaderTable(); Text nafnPaMoned = new
		 * Text("T�mask�rsla&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
		 * FunctColl.getNameOfMonth(manudur, iwc) + " " +ar);
		 * nafnPaMoned.setFontSize(3); nafnPaMoned.setBold();
		 * nafnPaMoned.setFontColor(this.header_text_color);
		 * 
		 * Text memberName = new Text(
		 * (((com.idega.data.genericentity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.data.genericentity.Member.class)).findByPrimaryKeyLegacy(this.member_id)).getName());
		 * memberName.setFontSize(3); memberName.setBold();
		 * memberName.setFontColor(this.header_text_color);
		 * 
		 * headerTable.add(memberName,2,1);
		 * headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",2,1);
		 * headerTable.add(nafnPaMoned,2,1); add(headerTable);
		 * 
		 * Timestamp stamp; IWTimestamp i_stamp; IWCalendar cal = new
		 * IWCalendar(); int day_of_week; double total_hour; double day_hour;
		 * double over_hour;
		 * 
		 * int row = 1; Table table = new Table();
		 * table.setWidth(this.table_width); table.setCellpadding(0);
		 * table.setCellspacing(0); add(table);
		 * 
		 * Text dayTxt = new Text("dags"); Text projectTxt = new Text("verk");
		 * Text hoursTxt = new Text("t�mar"); Text dayhourTxt = new
		 * Text("dagvinna"); Text overhourTxt = new Text("yfirvinna");
		 * dayTxt.setFontColor("#FFFFFF"); projectTxt.setFontColor("#FFFFFF");
		 * hoursTxt.setFontColor("#FFFFFF");
		 * dayhourTxt.setFontColor("#FFFFFF");
		 * overhourTxt.setFontColor("#FFFFFF");
		 * 
		 * table.add(dayTxt,2,row); table.add(projectTxt,3,row);
		 * table.add(hoursTxt,4,row); table.add(dayhourTxt,5,row);
		 * table.add(overhourTxt,6,row);
		 * 
		 * 
		 * Text dayTxt_reusable; Text projectTxt_reusable; Text
		 * hoursTxt_reusable; Text dayhourTxt_reusable; Text
		 * overhourTxt_reusable;
		 * 
		 * for (int i = 0; i
		 * < entry.length; i++) { if (entry[i].getResourceId() == 0 ) { ++row;
		 * stamp = entry[i].getDate(); i_stamp = new IWTimestamp(stamp);
		 * project =
		 * ((com.idega.block.timesheet.data.TimesheetProjectHome)com.idega.data.IDOLookup.getHomeLegacy(TimesheetProject.class)).findByPrimaryKeyLegacy(entry[i].getProjectId());
		 * day_of_week =
		 * cal.getDayOfWeek(i_stamp.getYear(),i_stamp.getMonth(),i_stamp.getDate());
		 * total_hour = 0; day_hour = 0; over_hour = 0;
		 * 
		 * total_hour = entry[i].getQuantity();
		 * 
		 * if (total_hour > hoursPerDay[day_of_week] ) { over_hour = total_hour -
		 * hoursPerDay[day_of_week]; day_hour = hoursPerDay[day_of_week]; }
		 * else { day_hour = total_hour; }
		 * 
		 * totalHours += total_hour; totalOvertime += over_hour; totalDay +=
		 * day_hour;
		 * 
		 * dayTxt_reusable = new
		 * Text(entry[i].getDate().toString().substring(0,10));
		 * projectTxt_reusable = new Text(project.getName()); hoursTxt_reusable =
		 * new Text(""+total_hour); dayhourTxt_reusable = new
		 * Text(""+day_hour); overhourTxt_reusable = new Text(""+over_hour);
		 * dayTxt_reusable.setFontSize(1); projectTxt_reusable.setFontSize(1);
		 * hoursTxt_reusable.setFontSize(1);
		 * dayhourTxt_reusable.setFontSize(1);
		 * overhourTxt_reusable.setFontSize(1);
		 * 
		 * table.add(dayTxt_reusable,2,row);
		 * table.add(projectTxt_reusable,3,row);
		 * table.add(hoursTxt_reusable,4,row);
		 * table.add(dayhourTxt_reusable,5,row);
		 * table.add(overhourTxt_reusable,6,row); } }
		 * 
		 * ++row; Text finalHours = new Text(""+totalHours);
		 * finalHours.setFontSize(1); finalHours.setBold(); Text finalDay = new
		 * Text(""+totalDay); finalDay.setFontSize(1); finalDay.setBold(); Text
		 * finalOver = new Text(""+totalOvertime); finalOver.setFontSize(1);
		 * finalOver.setBold();
		 * 
		 * table.add(finalHours,4,row); table.add(finalDay,5,row);
		 * table.add(finalOver,6,row);
		 * 
		 * ++row;
		 * 
		 * Link bakka = this.getPreviousMonthLink("hour_pr_employee"); Link
		 * afram = this.getNextMonthLink("hour_pr_employee");
		 * 
		 * table.add(bakka,2,row); table.add(afram,2,row);
		 * 
		 * 
		 * table.setHorizontalZebraColored(color_1,color_2);
		 * table.setRowColor(1,this.header_color);
		 * table.setRowColor(row,this.header_color);
		 * 
		 * 
		 * 
		 *  
		 */
	}
	private void presentateProjectHours(IWContext iwc) throws Exception {
		Collection entries =
			getTimesheetEntryHome().findByUserAndProjectWithinPeriod(this.userID, this.projectID, this.fromDate, this.toDate);
		presentateHours(iwc, entries, this.projectID, false);
	}
	private void presentateAllProjectHours(IWContext iwc) throws Exception {
		if (this.isAdmin) {
			Collection entries = getTimesheetEntryHome().findByProjectWithinPeriod(this.projectID, this.fromDate, this.toDate);
			presentateHours(iwc, entries, this.projectID, true);
		}
		else {
			presentateProjectHours(iwc);
		}
	}
	private void presentateHours(IWContext iwc, Collection entries, Integer projectID, boolean viewAll)
		throws Exception {
		String edit_string = ACT_PROJECT_HOURS;
		User current_user = this.user;
		if (viewAll) {
			edit_string = ACT_PROJECT_HOURS_ALL;
		}
		//Days[] days = (Days[])(new Days()).findAllOrdered("days_id");
		double totalHours = 0;
		double totalOvertime = 0;
		double totalDay = 0;
		double[] hoursPerDay = new double[8];
		/*
		 * for (int i = 0; i
		 * < days.length; i++) { hoursPerDay[days[i].getID()] =
		 * days[i].getWorkHours(); }
		 */
		Table headerTable = this.getHeaderTable();
		Text nafnPaMoned =
			new Text(
				"T�mask�rsla&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ this.FunctColl.getMonthName(this.month, iwc.getCurrentLocale(), IWCalendar.LONG)
					+ " "
					+ this.year);
		nafnPaMoned.setFontSize(3);
		nafnPaMoned.setBold();
		nafnPaMoned.setFontColor(this.header_text_color);
		TimesheetProject project = getTimesheetProjectHome().findByPrimaryKey(projectID);
		Text memberName = new Text(project.getName());
		memberName.setFontSize(3);
		memberName.setBold();
		memberName.setFontColor(this.header_text_color);
		headerTable.add(memberName, 2, 1);
		headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 2, 1);
		headerTable.add(nafnPaMoned, 2, 1);
		add(headerTable);
		Timestamp stamp;
		IWTimestamp i_stamp;
		IWCalendar cal = new IWCalendar();
		int day_of_week;
		double total_hour;
		double day_hour;
		double over_hour;
		int row = 1;
		Table table = new Table();
		table.setWidth(this.table_width);
		table.setCellpadding(0);
		table.setCellspacing(0);
		add(table);
		Text dayTxt = new Text(this.iwrb.getLocalizedString("date", "Date"));
		Text projectTxt = new Text(this.iwrb.getLocalizedString("employee", "Employee"));
		Text hoursTxt = new Text(this.iwrb.getLocalizedString("hours", "Hours"));
		Text dayhourTxt = new Text(this.iwrb.getLocalizedString("daytime", "Daytime"));
		Text overhourTxt = new Text(this.iwrb.getLocalizedString("overtime", "Overtime"));
		dayTxt.setFontColor("#FFFFFF");
		projectTxt.setFontColor("#FFFFFF");
		hoursTxt.setFontColor("#FFFFFF");
		dayhourTxt.setFontColor("#FFFFFF");
		overhourTxt.setFontColor("#FFFFFF");
		table.add(dayTxt, 2, row);
		table.add(projectTxt, 3, row);
		table.add(hoursTxt, 4, row);
		table.add(dayhourTxt, 5, row);
		table.add(overhourTxt, 6, row);
		Text dayTxt_reusable;
		Text projectTxt_reusable;
		Text hoursTxt_reusable;
		Text dayhourTxt_reusable;
		Text overhourTxt_reusable;
		String member_name = current_user.getName();
		for (Iterator iter = entries.iterator(); iter.hasNext();) {
			TimesheetEntry entry = (TimesheetEntry) iter.next();
			if (entry.getResourceId() == 0) {
				++row;
				stamp = entry.getDate();
				i_stamp = new IWTimestamp(stamp);
				day_of_week = cal.getDayOfWeek(i_stamp.getYear(), i_stamp.getMonth(), i_stamp.getDay());
				if (viewAll) {
					current_user = entry.getUser();
					member_name = current_user.getName();
				}
				total_hour = 0;
				day_hour = 0;
				over_hour = 0;
				total_hour = entry.getQuantity();
				if (total_hour > hoursPerDay[day_of_week]) {
					over_hour = total_hour - hoursPerDay[day_of_week];
					day_hour = hoursPerDay[day_of_week];
				}
				else {
					day_hour = total_hour;
				}
				totalHours += total_hour;
				totalOvertime += over_hour;
				totalDay += day_hour;
				dayTxt_reusable = new Text(entry.getDate().toString().substring(0, 10));
				projectTxt_reusable = new Text(member_name);
				hoursTxt_reusable = new Text("" + total_hour);
				dayhourTxt_reusable = new Text("" + day_hour);
				overhourTxt_reusable = new Text("" + over_hour);
				dayTxt_reusable.setFontSize(1);
				projectTxt_reusable.setFontSize(1);
				hoursTxt_reusable.setFontSize(1);
				dayhourTxt_reusable.setFontSize(1);
				overhourTxt_reusable.setFontSize(1);
				table.add(dayTxt_reusable, 2, row);
				table.add(projectTxt_reusable, 3, row);
				table.add(hoursTxt_reusable, 4, row);
				table.add(dayhourTxt_reusable, 5, row);
				table.add(overhourTxt_reusable, 6, row);
			}
		}
		++row;
		Text finalHours = new Text("" + totalHours);
		finalHours.setFontSize(1);
		finalHours.setBold();
		Text finalDay = new Text("" + totalDay);
		finalDay.setFontSize(1);
		finalDay.setBold();
		Text finalOver = new Text("" + totalOvertime);
		finalOver.setFontSize(1);
		finalOver.setBold();
		table.add(finalHours, 4, row);
		table.add(finalDay, 5, row);
		table.add(finalOver, 6, row);
		++row;
		Link bakka = this.getPreviousMonthLink(edit_string);
		if (bakka != null) {
			bakka.addParameter(PRM_PROJECT_ID, projectID.toString());
		}
		Link afram = this.getNextMonthLink(edit_string);
		if (afram != null) {
			afram.addParameter(PRM_PROJECT_ID, projectID.toString());
		}
		table.add(bakka, 2, row);
		table.add(afram, 2, row);
		table.setHorizontalZebraColored(this.color_1, this.color_2);
		table.setRowColor(1, this.header_color);
		table.setRowColor(row, this.header_color);
	}
	private void presentateUnbookedEntries(IWContext iwc, boolean viewPrevious) throws Exception {
		boolean arePreviousEntries = false;
		double vinnaSamtals = 0;
		double aksturSamtals = 0;
		//TimesheetEntry[] entry = null;
		Collection entries = null;
		if (viewPrevious) {
			entries =
				getTimesheetEntryHome().findUnbookedByUserBeforeDate(this.userID, new IWTimestamp(1, this.month, this.year).getDate());
		}
		else {
			entries = getTimesheetEntryHome().findUnbookedByUserWithinPeriod(this.userID, this.fromDate, this.toDate);
			arePreviousEntries =
				getTimesheetEntryHome().countByUserBeforeDate(this.userID, new IWTimestamp(1, this.month, this.year).getDate()) > 0;
		}
		TimesheetProject project;
		Form form = new Form();
		Table headerTable = this.getHeaderTable();
		Text unBooked = new Text(this.iwrb.getLocalizedString("unbooked_entries", "Unbooked entries"));
		unBooked.setFontSize(3);
		unBooked.setBold();
		unBooked.setFontColor(this.header_text_color);
		Text memName = new Text(this.user.getName());
		memName.setFontSize(3);
		memName.setBold();
		memName.setFontColor(this.header_text_color);
		Text monthName =
			new Text(this.FunctColl.getMonthName(this.month, iwc.getCurrentLocale(), IWCalendar.LONG) + " " + this.year);
		if (viewPrevious) {
			monthName.setText("fyrir " + monthName.getText());
			add(monthName.getText());
		}
		monthName.setFontSize(3);
		monthName.setBold();
		monthName.setFontColor(this.header_text_color);
		headerTable.add(unBooked, 2, 1);
		headerTable.add(
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
			2,
			1);
		headerTable.add(memName, 2, 1);
		headerTable.add(
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
			2,
			1);
		headerTable.add(monthName, 2, 1);
		form.add(headerTable);
		add(form);
		int row = 1;
		Table myTable = new Table();
		myTable.setWidth(this.table_width);
		form.add(myTable);
		myTable.setCellspacing(0);
		myTable.setCellpadding(0);
		myTable.setBorder(this.border);
		Text dags = new Text(this.iwrb.getLocalizedString("date", "Date"));
		Text the_number = new Text(this.iwrb.getLocalizedString("project_number", "Project nr."));
		Text the_member = new Text(this.iwrb.getLocalizedString("project", "Project"));
		Text the_resource = new Text("For�i");
		Text horas = new Text(this.iwrb.getLocalizedString("hours", "Hours"));
		Text miles = new Text(this.iwrb.getLocalizedString("driving", "Driving"));
		Text other = new Text(this.iwrb.getLocalizedString("equipment_usage", "Equipment usage"));
		Text booked = new Text(this.iwrb.getLocalizedString("book", "Book"));
		dags.setFontColor(this.header_text_color);
		the_number.setFontColor(this.header_text_color);
		the_member.setFontColor(this.header_text_color);
		the_resource.setFontColor(this.header_text_color);
		horas.setFontColor(this.header_text_color);
		miles.setFontColor(this.header_text_color);
		other.setFontColor(this.header_text_color);
		myTable.add(dags, 2, row);
		myTable.add(the_number, 3, row);
		myTable.add(the_member, 4, row);
		myTable.add(the_resource, 5, row);
		myTable.add(horas, 6, row);
		myTable.add(miles, 7, row);
		myTable.add(other, 8, row);
		if (!this.bookAllAtOnce) {
			booked.setFontColor(this.header_text_color);
			myTable.add(booked, 9, row);
		}
		String resource_name = null;
		String resource_unit_name = null;
		String project_name = null;
		String project_number;
		Text themDags;
		Text themProject;
		Text themProjectNumber;
		Text themResource;
		Text themHours;
		Text themMiles;
		Text themOther;
		int fontSize = 1;
		//for (int i = 0; i < entry.length; i++) {
		TimesheetProjectHome projectHome = getTimesheetProjectHome();
		ResourceHome resHome = getResourceHome();
		for (Iterator iter = entries.iterator(); iter.hasNext();) {
			TimesheetEntry entry = (TimesheetEntry) iter.next();
			project = projectHome.findByPrimaryKey(new Integer(entry.getProjectId()));
			resource_name = null;
			resource_unit_name = null;
			project_name = null;
			project_number = null;
			//if (entry[i].getResource() != null) {
			if (entry.getResourceId() > 0) {
				Resource resource = resHome.findByPrimaryKey(new Integer(entry.getResourceId()));
				resource_name = resource.getName();
				resource_unit_name = resource.getUnitName();
			}
			++row;
			themDags = new Text(entry.getDate().toString().substring(0, 10));
			project_number = project.getProjectNumber();
			if (project_number == null) {
				project_number = "";
			}
			project_name = project.getName();
			if (project_name.length() > 30) {
				project_name = project_name.substring(0, 30) + "...";
			}
			themProjectNumber = new Text(project_number);
			themProjectNumber.setFontSize(fontSize);
			themProject = new Text(project_name);
			themDags.setFontSize(fontSize);
			themProject.setFontSize(fontSize);
			myTable.add(themDags, 2, row);
			myTable.add(themProjectNumber, 3, row);
			myTable.add(themProject, 4, row);
			if (resource_name != null) {
				themResource = new Text(resource_name);
				themResource.setFontSize(fontSize);
				myTable.add(themResource, 5, row);
				if (resource_unit_name != null) {
					if (resource_unit_name.equalsIgnoreCase("klst")) {
						themHours = new Text("" + entry.getQuantity());
						themHours.setFontSize(fontSize);
						myTable.add(themHours, 6, row);
						vinnaSamtals += entry.getQuantity();
					}
					else if (resource_unit_name.equalsIgnoreCase("km")) {
						themMiles = new Text("" + entry.getQuantity());
						themMiles.setFontSize(fontSize);
						myTable.add(themMiles, 7, row);
						aksturSamtals += entry.getQuantity();
					}
					else {
						themOther = new Text("" + entry.getQuantity());
						themOther.setFontSize(fontSize);
						myTable.add(themOther, 8, row);
					}
				}
			}
			else {
				myTable.add("-", 5, row);
				themHours = new Text("" + entry.getQuantity());
				themHours.setFontSize(fontSize);
				myTable.add(themHours, 6, row);
				vinnaSamtals += entry.getQuantity();
			}
			myTable.add(new HiddenInput("idega_timesheet_entry_id", entry.getPrimaryKey().toString() + ""), 9, row);
			if (!this.bookAllAtOnce) {
				myTable.add(new CheckBox("idega_timesheet_Book" + entry.getPrimaryKey().toString()), 9, row);
			}
		}
		row++;
		myTable.add(new HiddenInput(PRM_ACTION, "save_booked"));
		Text totalWork = new Text("" + vinnaSamtals);
		totalWork.setFontSize(fontSize);
		Text totalMiles = new Text("" + aksturSamtals);
		totalMiles.setFontSize(fontSize);
		myTable.add(totalWork, 6, row);
		myTable.add(totalMiles, 7, row);
		++row;
		if (this.booked_image_url != null) {
			Link theBooked = new Link(new Image(this.booked_image_url));
			setLink(theBooked, ACT_BOOKED, 0, 0, 0);
			myTable.add(theBooked, 8, row);
		}
		else {
			Link theBooked = new Link(this.iwrb.getLocalizedString("booked_hours", "Booked hours"));
			setLink(theBooked, ACT_BOOKED, 0, 0, 0);
			myTable.add(theBooked, 8, row);
		}
		if (this.book_image_url != null) {
			myTable.add(new SubmitButton(new Image(this.book_image_url)), 9, row);
		}
		else {
			myTable.add(new SubmitButton("action", "B�ka"), 9, row);
		}
		Link prev = this.getPreviousMonthLink(ACT_UNBOOKED);
		Link next = this.getNextMonthLink(ACT_UNBOOKED);
		myTable.add(prev, 2, row);
		myTable.add(next, 2, row);
		myTable.setWidth(2, "100");
		myTable.setWidth(3, "90");
		myTable.setWidth(8, "140");
		myTable.setWidth(9, "80");
		myTable.setHorizontalZebraColored(this.color_1, this.color_2);
		myTable.setRowColor(1, this.header_color);
		myTable.setRowColor(row, this.header_color);
		if (arePreviousEntries) {
			add("�� �tt �b�ka�ar f�rslur � fyrri m�nu�um - ");
			Link checkPrevious = new Link("listi");
			this.setLink(checkPrevious, ACT_PAST_ENTRY_CHECK, 0, 0, 0);
			add(checkPrevious);
		}
	}
	private void presentateBookedEntries(IWContext iwc) throws Exception {
		double vinnaSamtals = 0;
		double aksturSamtals = 0;
		Collection entries = getTimesheetEntryHome().findBookedByUserWithinPeriod(this.userID, this.fromDate, this.toDate);
		TimesheetProject project;
		Form form = new Form();
		Table headerTable = this.getHeaderTable();
		Text unBooked = new Text(this.iwrb.getLocalizedString("booked_entries", "Booked entries"));
		unBooked.setFontSize(3);
		unBooked.setBold();
		unBooked.setFontColor(this.header_text_color);
		Text memName = new Text(this.user.getName());
		memName.setFontSize(3);
		memName.setBold();
		memName.setFontColor(this.header_text_color);
		Text monthName =
			new Text(this.FunctColl.getMonthName(this.month, iwc.getCurrentLocale(), IWCalendar.LONG) + " " + this.year);
		monthName.setFontSize(3);
		monthName.setBold();
		monthName.setFontColor(this.header_text_color);
		headerTable.add(unBooked, 2, 1);
		headerTable.add(
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
			2,
			1);
		headerTable.add(memName, 2, 1);
		headerTable.add(
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
			2,
			1);
		headerTable.add(monthName, 2, 1);
		form.add(headerTable);
		add(form);
		int row = 1;
		Table myTable = new Table();
		myTable.setWidth(this.table_width);
		form.add(myTable);
		myTable.setCellspacing(0);
		myTable.setCellpadding(0);
		myTable.setBorder(this.border);
		Text dags = new Text(this.iwrb.getLocalizedString("date", "Date"));
		Text the_number = new Text(this.iwrb.getLocalizedString("project_number", "Project nr."));
		Text the_member = new Text(this.iwrb.getLocalizedString("project", "Project"));
		Text the_resource = new Text("For�i");
		Text horas = new Text(this.iwrb.getLocalizedString("hours", "Hours"));
		Text miles = new Text(this.iwrb.getLocalizedString("driving", "Driving"));
		Text other = new Text(this.iwrb.getLocalizedString("equipment_usage", "Equipment usage"));
		Text booked = new Text(this.iwrb.getLocalizedString("book", "Book"));
		dags.setFontColor(this.header_text_color);
		the_number.setFontColor(this.header_text_color);
		the_member.setFontColor(this.header_text_color);
		the_resource.setFontColor(this.header_text_color);
		horas.setFontColor(this.header_text_color);
		miles.setFontColor(this.header_text_color);
		other.setFontColor(this.header_text_color);
		booked.setFontColor(this.header_text_color);
		myTable.add(dags, 2, row);
		myTable.add(the_number, 3, row);
		myTable.add(the_member, 4, row);
		myTable.add(the_resource, 5, row);
		myTable.add(horas, 6, row);
		myTable.add(miles, 7, row);
		myTable.add(other, 8, row);
		String resource_name = null;
		String resource_unit_name = null;
		String project_name = null;
		String project_number = null;
		Text themDags;
		Text themProject;
		Text themProjectNumber;
		Text themResource;
		Text themHours;
		Text themMiles;
		Text themOther;
		int fontSize = 1;
		TimesheetProjectHome projectHome = getTimesheetProjectHome();
		ResourceHome resHome = getResourceHome();
		for (Iterator iter = entries.iterator(); iter.hasNext();) {
			TimesheetEntry entry = (TimesheetEntry) iter.next();
			project = projectHome.findByPrimaryKey(new Integer(entry.getProjectId()));
			resource_name = null;
			resource_unit_name = null;
			project_name = null;
			project_number = null;
			if (entry.getResourceId() > 0) {
				Resource resource = resHome.findByPrimaryKey(new Integer(entry.getResourceId()));
				resource_name = resource.getName();
				resource_unit_name = resource.getUnitName();
			}
			++row;
			themDags = new Text(entry.getDate().toString().substring(0, 10));
			project_number = project.getProjectNumber();
			if (project_number == null) {
				project_number = "";
			}
			project_name = project.getName();
			if (project_name.length() > 30) {
				project_name = project_name.substring(0, 30) + "...";
			}
			themProjectNumber = new Text(project_number);
			themProjectNumber.setFontSize(fontSize);
			themProject = new Text(project_name);
			themDags.setFontSize(fontSize);
			themProject.setFontSize(fontSize);
			myTable.add(themDags, 2, row);
			myTable.add(themProjectNumber, 3, row);
			myTable.add(themProject, 4, row);
			if (resource_name != null) {
				themResource = new Text(resource_name);
				themResource.setFontSize(fontSize);
				myTable.add(themResource, 5, row);
				if (resource_unit_name != null) {
					if (resource_unit_name.equalsIgnoreCase("klst")) {
						themHours = new Text("" + entry.getQuantity());
						themHours.setFontSize(fontSize);
						myTable.add(themHours, 6, row);
						vinnaSamtals += entry.getQuantity();
					}
					else if (resource_unit_name.equalsIgnoreCase("km")) {
						themMiles = new Text("" + entry.getQuantity());
						themMiles.setFontSize(fontSize);
						myTable.add(themMiles, 7, row);
						aksturSamtals += entry.getQuantity();
					}
					else {
						themOther = new Text("" + entry.getQuantity());
						themOther.setFontSize(fontSize);
						myTable.add(themOther, 8, row);
					}
				}
			}
			else {
				myTable.add("-", 5, row);
				themHours = new Text("" + entry.getQuantity());
				themHours.setFontSize(fontSize);
				myTable.add(themHours, 6, row);
				vinnaSamtals += entry.getQuantity();
			}
		}
		row++;
		/*
		 * if (this.register_image_url != null) { myTable.add(new
		 * SubmitButton(new Image(register_image_url)),8,row); } else {
		 * myTable.add(new SubmitButton("action","Skr�setja"),8,row); }
		 */
		Text totalWork = new Text("" + vinnaSamtals);
		totalWork.setFontSize(fontSize);
		Text totalMiles = new Text("" + aksturSamtals);
		totalMiles.setFontSize(fontSize);
		myTable.add(totalWork, 6, row);
		myTable.add(totalMiles, 7, row);
		row++;
		Link prev = this.getPreviousMonthLink(ACT_BOOKED);
		Link next = this.getNextMonthLink(ACT_BOOKED);
		myTable.add(prev, 2, row);
		myTable.add(next, 2, row);
		myTable.setHorizontalZebraColored(this.color_1, this.color_2);
		myTable.setRowColor(1, this.header_color);
		myTable.setRowColor(row, this.header_color);
		myTable.setWidth(2, "100");
		myTable.setWidth(3, "90");
		myTable.setWidth(8, "140");
	}
	private void processBooking(IWContext iwc) throws Exception {
		String[] entry_id = (String[]) iwc.getParameterValues("idega_timesheet_entry_id");
		TimesheetEntry entry;
		String active = "";
		if (entry_id != null) {
			TimesheetEntryHome entryHome = getTimesheetEntryHome();
			for (int i = 0; i < entry_id.length; i++) {
				if (this.bookAllAtOnce) {
					entry = entryHome.findByPrimaryKey(Integer.valueOf(entry_id[i]));
					entry.setBooked(true);
					entry.store();
				}
				else {
					active = "";
					active = iwc.getParameter("idega_timesheet_Book" + entry_id[i]);
					if (active != null) {
						if (!active.equals("")) {
							entry = entryHome.findByPrimaryKey(Integer.valueOf(entry_id[i]));
							entry.setBooked(true);
							entry.store();
						}
					}
				}
			}
		}
	}
	private void processRegistration(IWContext iwc) throws Exception {
		String[] entry_id = (String[]) iwc.getParameterValues("idega_timesheet_entry_id");
		TimesheetEntry entry;
		String active = "";
		if (entry_id != null) {
			TimesheetEntryHome entryHome = getTimesheetEntryHome();
			for (int i = 0; i < entry_id.length; i++) {
				active = "";
				active = iwc.getParameter("idega_timesheet_Register" + entry_id[i]);
				if (active != null) {
					if (!active.equals("")) {
						entry = entryHome.findByPrimaryKey(Integer.valueOf(entry_id[i]));
						entry.setRegistered(true);
						entry.store();
					}
				}
			}
		}
	}
	private void processProjectMove(IWContext iwc) throws Exception {
		String where_to = iwc.getParameter("direction");
		String[] project_id = iwc.getParameterValues(PRM_PROJECT_ID);
		if ((where_to != null) && (project_id != null)) {
			try {
				TimesheetProject project;
				TimesheetProjectHome projectHome = getTimesheetProjectHome();
				if (where_to.equals("<=")) {
					for (int i = 0; i < project_id.length; i++) {
						project = projectHome.findByPrimaryKey(Integer.valueOf(project_id[i]));
						project.addUser(this.user);
					}
				}
				else if (where_to.equals("=>")) {
					for (int i = 0; i < project_id.length; i++) {
						project = projectHome.findByPrimaryKey(Integer.valueOf(project_id[i]));
						project.removeUser(this.user);
					}
				}
			}
			catch (Exception e) {
			}
		}
	}
	private void presentateUserProjects(IWContext iwc) throws Exception {
		TimesheetProjectHome projectHome = getTimesheetProjectHome();
		Collection projectsAll = projectHome.findAllOrderByNumber();
		Collection projectsUsers = projectHome.findUserRelated(this.user);
		Vector projects_left = new Vector();
		if (!projectsAll.isEmpty()) {
			for (Iterator iter = projectsAll.iterator(); iter.hasNext();) {
				TimesheetProject project = (TimesheetProject) iter.next();
				projects_left.addElement(project);
			}
		}
		SelectionBox projects_used = new SelectionBox(PRM_PROJECT_ID);
		projects_used.setMarkupAttribute("size", "20");
		if (!projectsUsers.isEmpty()) {
			String project_number;
			String project_name;
			for (Iterator iter = projectsUsers.iterator(); iter.hasNext();) {
				TimesheetProject project = (TimesheetProject) iter.next();
				project_number = project.getProjectNumber();
				project_name = project.getName();
				for (int j = 0; j < projects_left.size(); j++) {
					if (((TimesheetProject) projects_left.elementAt(j)).equals(project)) {
						projects_left.removeElementAt(j);
					}
				}
				if (project_name.length() > 32) {
					project_name = project_name.substring(0, 32) + "...";
				}
				if (project_number == null) {
					projects_used.addMenuElement(project.getPrimaryKey().toString(), project_name);
				}
				else {
					projects_used.addMenuElement(
						project.getPrimaryKey().toString(),
						project_number + " - " + project_name);
				}
			}
		}
		SelectionBox projects_available = new SelectionBox(PRM_PROJECT_ID);
		projects_available.setMarkupAttribute("size", "20");
		if (projects_left.size() > 0) {
			TimesheetProject project;
			String project_number;
			String project_name;
			for (int i = 0; i < projects_left.size(); i++) {
				project = (TimesheetProject) projects_left.elementAt(i);
				project_number = project.getProjectNumber();
				project_name = project.getName();
				if (project_name.length() > 32) {
					project_name = project_name.substring(0, 32) + "...";
				}
				if (project_number == null) {
					projects_available.addMenuElement(project.getPrimaryKey().toString(), project_name);
				}
				else {
					projects_available.addMenuElement(
						project.getPrimaryKey().toString(),
						project_number + "  -  " + project_name);
				}
			}
		}
		projects_used.addMenuElement(
			0,
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		projects_available.addMenuElement(
			0,
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		Form form = new Form();
		Table headerTable = new Table(3, 1);
		headerTable.setCellpadding(0);
		headerTable.setCellspacing(0);
		headerTable.setColor(this.header_color);
		headerTable.setWidth(1, "17");
		headerTable.setWidth(3, "17");
		headerTable.setWidth(this.table_width);
		headerTable.setHeight("36");
		headerTable.setVerticalAlignment(1, 1, "top");
		headerTable.setVerticalAlignment(2, 1, "middle");
		headerTable.setVerticalAlignment(3, 1, "top");
		headerTable.setAlignment(1, 1, "left");
		headerTable.setAlignment(2, 1, "center");
		headerTable.setAlignment(3, 1, "right");
		headerTable.add(this.iwb.getImage("shared/leftcorner.gif", ""), 1, 1);
		headerTable.add(this.iwb.getImage("shared/rightcorner.gif", ""), 3, 1);
		Text nafnPaMoned = new Text(this.iwrb.getLocalizedString(ACT_USER_PROJECTS, "My projects"));
		nafnPaMoned.setFontSize(3);
		nafnPaMoned.setBold();
		nafnPaMoned.setFontColor(this.header_text_color);
		headerTable.add(nafnPaMoned, 2, 1);
		form.add(headerTable);
		Table table = new Table(3, 4);
		form.add(table);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setWidth(this.table_width);
		table.setAlignment(1, 1, "right");
		table.setAlignment(1, 2, "right");
		int row = 1;
		Text myProjects = new Text(this.iwrb.getLocalizedString(ACT_USER_PROJECTS, "My projects"));
		myProjects.setFontColor(this.header_text_color);
		Text otherProjects = new Text(this.iwrb.getLocalizedString("other_projects", "Other projects"));
		otherProjects.setFontColor(this.header_text_color);
		table.add(myProjects, 1, row);
		table.add(otherProjects, 3, row);
		row++;
		table.add(projects_used, 1, row);
		table.add(projects_available, 3, row);
		table.add(new SubmitButton("direction", "<="), 2, row);
		table.addBreak(2, row);
		table.add(new SubmitButton("direction", "=>"), 2, row);
		table.add(new HiddenInput(PRM_ACTION, ACT_PROJECTS_MOVE));
		++row;
		Link createProjectLink = new Link(this.iwrb.getLocalizedString("create_project", "Create project"));
		createProjectLink.addParameter(PRM_ACTION, ACT_CREATE_PROJECT);
		table.add(createProjectLink, 3, row++);
		table.setColor(this.color_1);
		table.setRowColor(1, this.header_color);
		table.setRowColor(row, this.header_color);
		add(form);
	}
	private void presentateProjectForm(IWContext iwc) {
		Table table = new Table();
		TextInput nameInput = new TextInput("pr_name");
		TextInput numberInput = new TextInput("pr_number");
		table.add(getHeaderText(this.iwrb.getLocalizedString("name", "Name")), 1, 2);
		table.add(getHeaderText(this.iwrb.getLocalizedString("number", "Number")), 1, 3);
		table.add(nameInput, 2, 2);
		table.add(numberInput, 2, 3);
		SubmitButton save = new SubmitButton(this.iwrb.getLocalizedImageButton("save", "Save"));
		table.add(new HiddenInput(PRM_ACTION, ACT_SAVE_PROJECT));
		table.add(save, 2, 5);
		Form form = new Form();
		FieldSet set = new FieldSet();
		set.add(new Legend("New project"));
		set.add(table);
		form.add(set);
		add(form);
	}
	private Table getHeaderTable() {
		Table headerTable = new Table(3, 1);
		headerTable.setCellpadding(0);
		headerTable.setCellspacing(0);
		headerTable.setColor(this.header_color);
		headerTable.setWidth(1, "17");
		headerTable.setWidth(3, "17");
		headerTable.setWidth(this.table_width);
		headerTable.setHeight("36");
		headerTable.setVerticalAlignment(1, 1, "top");
		headerTable.setVerticalAlignment(2, 1, "middle");
		headerTable.setVerticalAlignment(3, 1, "top");
		headerTable.setAlignment(1, 1, "left");
		headerTable.setAlignment(2, 1, "center");
		headerTable.setAlignment(3, 1, "right");
		Image leftcorner = this.iwb.getImage("shared/leftcorner.gif");
		headerTable.add(leftcorner, 1, 1);
		Image rightcorner = this.iwb.getImage("shared/rightcorner.gif");
		headerTable.add(rightcorner, 3, 1);
		headerTable.setNoWrap();
		return headerTable;
	}
	private Link getPreviousMonthLink(String edit) {
		Link link = null;
		if (!this.isPrintable) {
			if (this.previous_image_url != null) {
				link = new Link(new Image(this.previous_image_url, this.previous_month_string));
			}
			else {
				Text textinn = new Text(this.previous_month_string);
				if (this.header_text_color != null) {
					textinn.setFontColor(this.header_text_color);
				}
				link = new Link(textinn);
			}
			this.day = 1;
			setLink(link, edit, 0, -1, 0);
		}
		return link;
	}
	private Link getNextMonthLink(String edit) {
		Link link = null;
		if (!this.isPrintable) {
			if (this.next_image_url != null) {
				link = new Link(new Image(this.next_image_url, this.next_month_string));
			}
			else {
				Text textinn = new Text(this.next_month_string);
				if (this.header_text_color != null) {
					textinn.setFontColor(this.header_text_color);
				}
				link = new Link(textinn);
			}
			this.day = 1;
			setLink(link, edit, 0, 1, 0);
		}
		return link;
	}
	private void setLink(Link link, String edit, int year_adjustment, int month_adjustment, int day_adjustment) {
		link.addParameter(PRM_ACTION, edit);
		link.addParameter(PRM_MONTH, (this.month + month_adjustment));
		link.addParameter(PRM_YEAR, this.year + year_adjustment);
		link.addParameter(PRM_DAY, this.day + day_adjustment);
		link.addParameter(PRM_DAYCOUNT, this.daysShown);
		link.addParameter(PRM_LINECOUNT, this.extraLines);
		link.addParameter("i_timesheet_member_id", this.userID.intValue());
	}
	private Link getPrintableLink(String edit) {
		Window gluggi = new Window(this.iwrb.getLocalizedString("printable", "Printable"));
		gluggi.setResizable(true);
		Link link = new Link(gluggi);
		setLink(link, edit, 0, 0, 0);
		link.addParameter(PRM_PRINTABLE, "true");
		if (this.timesheet_project_id != null) {
			link.addParameter(PRM_PROJECT_ID, this.timesheet_project_id);
		}
		return link;
	}
	public Link getMyProjectsLink() {
		Link link = new Link(this.iwrb.getLocalizedString(ACT_USER_PROJECTS, "My projects"));
		link.addParameter(PRM_ACTION, ACT_USER_PROJECTS);
		return link;
	}
	public PrintButton getPrintButton() {
		return new PrintButton(this.iwrb.getLocalizedImageButton("print", "Print"));
	}
	private Text getHeaderText(String caption) {
		Text text = new Text(caption);
		text.setFontColor(this.header_text_color);
		return text;
	}
	private TimesheetEntryHome getTimesheetEntryHome() throws RemoteException {
		return ((TimesheetEntryHome) IDOLookup.getHome(TimesheetEntry.class));
	}
	private TimesheetProjectHome getTimesheetProjectHome() throws RemoteException {
		return (TimesheetProjectHome) IDOLookup.getHome(TimesheetProject.class);
	}
	private ResourceHome getResourceHome() throws RemoteException {
		return (ResourceHome) IDOLookup.getHome(Resource.class);
	}
}