/*
 * Created on 11.4.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import se.idega.idegaweb.commune.care.business.CareConstants;
import se.idega.idegaweb.commune.care.check.data.GrantedCheck;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import com.idega.block.navigation.presentation.UserHomeLink;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * @author laddi
 */
public class ChildCareChildApplication extends ChildCareBlock {

	private User child;
	private GrantedCheck check;
	
	private final static int ACTION_VIEW_FORM = 1;
	private final static int ACTION_SUBMIT = 2;
	
	private int _action = -1;
	
	private final static String PARAMETER_ACTION = "cca_action";
	private final static String PARAM_FORM_SUBMIT = "cca_submit";
	private final static String PARAM_DATE = "cca_date";
	private final static String PARAM_QUEUE_DATE = "cca_queue_date";
	private final static String PARAM_AREA = "cca_area";
	private final static String PARAM_PROVIDER = "cca_provider";
	private final static String PARAM_MESSAGE = "cca_message";

	private final static String PROVIDERS = "cca_providers";
	private final static String NAME = "cca_name";
	private final static String PID = "cca_pid";
	private final static String ADDRESS = "cca_address";
	private final static String CARE_FROM = "cca_care_from";
	private final static String APPLICATION_INSERTED = "cca_application_ok";
	private final static String APPLICATION_FAILURE = "cca_application_failed";

	private final static String EMAIL_PROVIDER_SUBJECT = "child_care.application_received_subject";
	private final static String EMAIL_PROVIDER_MESSAGE = "child_care.application_received_body";
	
	private Collection areas;
	private Map providerMap;
	private School currentProvider;
	
	private boolean _noCheckError = false;
	private boolean isAdmin = false;
	private boolean hasActivePlacement = false;

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parseAction(iwc);
		
		if (child != null) {
			try {
				currentProvider = getBusiness().getCurrentProviderByPlacement(((Integer) child.getPrimaryKey()).intValue());
				hasActivePlacement = getBusiness().hasActiveApplication(((Integer) child.getPrimaryKey()).intValue(), CareConstants.CASE_CODE_KEY);
			}
			catch (RemoteException re) {
				currentProvider = null;
				hasActivePlacement = false;
			}
		}
		
		switch (_action) {
			case ACTION_VIEW_FORM :
				viewForm(iwc);
				break;
			case ACTION_SUBMIT :
				submitForm(iwc);
				break;
		}
	}
	
	protected boolean isAdmin(IWContext iwc) {
		if (iwc.hasEditPermission(this))
			return true;
			
		try {
			return getBusiness().getUserBusiness().isRootCommuneAdministrator(iwc.getCurrentUser());
		}
		catch (RemoteException re) {
			return false;
		}
	}
	
	private void parseAction(IWContext iwc) throws RemoteException {
		isAdmin = isAdmin(iwc);
		
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			_action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		else {
			_action = ACTION_VIEW_FORM;
		}

		child = getBusiness().getUserBusiness().getUser(getSession().getChildID());
		
		if (isCheckRequired()) {
			try {
				check = getCheckBusiness(iwc).getGrantedCheckByChild(child);
				if (check == null) {
					_noCheckError = true;
				}
			}
			catch (RemoteException re) {
				_noCheckError = true;
			}
		}
	}

	private void viewForm(IWContext iwc) {
		boolean hasOffers = false;
		boolean hasPendingApplications = false;
		if (child != null) {
			try {
				hasPendingApplications = getBusiness().hasPendingApplications(((Integer) child.getPrimaryKey()).intValue(), CareConstants.CASE_CODE_KEY);
				hasOffers = getBusiness().hasUnansweredOffers(((Integer) child.getPrimaryKey()).intValue(), null);
			}
			catch (RemoteException e) {
				hasOffers = false;
			}
		}
		
		if (!_noCheckError && !hasOffers && !hasPendingApplications) {
			Form form = new Form();
		
			Table table = new Table();
			table.setWidth(getWidth());
			table.setCellpadding(0);
			table.setCellspacing(0);
			form.add(table);
		
			int row = 1;
			table.add(getChildInfoTable(iwc), 1, row++);
			table.setHeight(row++, 12);
			table.add(getInputTable(iwc), 1, row++);
			table.setHeight(row++, 12);
	
			GenericButton showPrognosis = getButton(new GenericButton("show_prognosis", localize("child_care.view_prognosis", "View prognosis")));
			showPrognosis.setWindowToOpen(ChildCarePrognosisWindow.class);
			
			SubmitButton submit = (SubmitButton)getButton(new SubmitButton(localize(PARAM_FORM_SUBMIT, "Submit application"), PARAMETER_ACTION, String.valueOf(ACTION_SUBMIT)));
			if (isAdmin) {
				try {
					User parent = getBusiness().getUserBusiness().getCustodianForChild(child);
					if (parent == null)
						submit.setDisabled(true);
				}
				catch (RemoteException re) {
					submit.setDisabled(true);
				}
			}
			
			table.add(showPrognosis, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(submit, 1, row);
			submit.setOnSubmitFunction("checkApplication", getSubmitCheckScript());
			form.setToDisableOnSubmit(submit, true);
			
			if (submit.getDisabled()) {
				row++;
				table.setHeight(row++, 6);
				table.add(getSmallErrorText(localize("child_care.no_parent_found", "No parent found")), 1, row);
			}
	
			add(form);
		}
		else {
			if (hasOffers)
				add(getErrorText(localize("child_care.child_has_offers", "Child has offers not yet replied to. New choices can not be made until dealt with.")));
			else if (hasPendingApplications)
				add(getErrorText(localize("child_care.child_has_pending_applications", "Child has pending applications that have to be updated or removed. New choices can not be made until dealt with.")));
			else
				add(getErrorText(localize("child_care.no_check_selected", "No check or child selected.")));
			add(new Break(2));
			add(new UserHomeLink());
		}
	}
	
	private void submitForm(IWContext iwc) {
		boolean done = false;
		try {
			int numberOfApplications = hasActivePlacement ? 4 : 5;
			int[] providers = new int[numberOfApplications];
			String[] dates = new String[numberOfApplications];
			Date[] queueDates = new Date[numberOfApplications];
			
			for (int i = 0; i < numberOfApplications; i++) {
				providers[i] = iwc.isParameterSet(PARAM_PROVIDER + "_" + (i + 1)) ? Integer.parseInt(iwc.getParameter(PARAM_PROVIDER + "_" + (i + 1))) : -1;
				dates[i] = iwc.isParameterSet(PARAM_DATE + "_" + (i + 1)) ? iwc.getParameter(PARAM_DATE + "_" + (i + 1)) : null;
				if (isAdmin) {
					if (iwc.isParameterSet(PARAM_QUEUE_DATE + "_" + (i + 1))) {
						queueDates[i] = new IWTimestamp(iwc.getParameter(PARAM_QUEUE_DATE + "_" + (i + 1))).getDate();
					}
					else
						queueDates[i] = null;
				}
			}
				
			if (!isAdmin) {
				Collection applications = getBusiness().getApplicationsForChild(child);
				loop:
				for (int i = 0; i < providers.length; i++){
					Iterator apps = applications.iterator();
					while(apps.hasNext()){
						ChildCareApplication app = (ChildCareApplication) apps.next();
						if (app.getProviderId() == providers[i]){
							queueDates[i] = app.getQueueDate();
							continue loop;
						}
					}
				}
			}
						
			String message = iwc.getParameter(PARAM_MESSAGE);

			String subject = localize(EMAIL_PROVIDER_SUBJECT, "Child care application received");
			String body = localize(EMAIL_PROVIDER_MESSAGE, "You have received a new childcare application");
			User parent = null;
			boolean sendMessages = true;
			if (isAdmin) {
				parent = getBusiness().getUserBusiness().getCustodianForChild(child);
				sendMessages = false;
			}
			else {
				parent = iwc.getCurrentUser();
			}

			done = getBusiness().insertApplications(parent, providers, dates, message, check != null ? ((Integer) check.getPrimaryKey()).intValue() : -1, ((Integer) child.getPrimaryKey()).intValue(), subject, body, false, sendMessages, queueDates, null);
		}
		catch (RemoteException e) {
			e.printStackTrace();
			done = false;
		}

		if (done) {
			if (getResponsePage() != null)
				iwc.forwardToIBPage(getParentPage(), getResponsePage());
			else
				add(new Text(localize(APPLICATION_INSERTED, "Application submitted")));
		}
		else
			add(new Text(localize(APPLICATION_FAILURE, "Failed to submit application")));
	}
	
	private Table getInputTable(IWContext iwc) {
		Table inputTable = new Table();
		inputTable.setCellspacing(0);
		inputTable.setCellpadding(2);
		inputTable.setColumns(3);

		int row = 1;
		inputTable.mergeCells(1, 1, inputTable.getColumns(), row);
		inputTable.add(getHeader(localize(PROVIDERS, "Providers")), 1, row++);

		String provider = localize(PARAM_PROVIDER, "Provider");
		String from = localize(CARE_FROM, "From") + ":";
		String message = null;
		Text providerText = null;
		Text fromText = getSmallHeader(from);
		Text queueDateText = getSmallHeader(localize("child_care.queue_data", "Queue date") + ":");
		IWTimestamp stamp = new IWTimestamp();

		ChildCareApplication application = null;
		int areaID = -1;
		int numberOfApplications = hasActivePlacement ? 4 : 5;
		for (int i = 1; i < (numberOfApplications + 1); i++) {
			try {
				application = getBusiness().getNonActiveApplication(getSession().getChildID(), i);
				if (application != null) {
					areaID = application.getProvider().getSchoolAreaId();
					message = application.getMessage();
				}
			}
			catch (RemoteException re) {
				application = null;
			}
			
			ProviderDropdownDouble dropdown = (ProviderDropdownDouble) getStyledInterface(getDropdown(iwc.getCurrentLocale(), PARAM_AREA + "_" + i, PARAM_PROVIDER + "_" + i));
			if (application != null) {
				dropdown.setSelectedValues(String.valueOf(areaID), String.valueOf(application.getProviderId()));
			}
			providerText = getSmallHeader(provider + Text.NON_BREAKING_SPACE + i + ":");
			inputTable.add(providerText, 1, row);
			//inputTable.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
			inputTable.add(dropdown, 3, row++);

			DateInput date = (DateInput)getStyledInterface(new DateInput(PARAM_DATE + "_" + i));
			if (application != null){
				date.setDate(application.getFromDate());
				date.setEarliestPossibleDate(application.getFromDate(), localize("child_care.no_date_back_prev", "You cannot set a date before the previous start date"));
			}
			else {
				date.setEarliestPossibleDate(stamp.getDate(), localize("child_care.no_date_back_in_time", "You cannot set a date back in time"));
			}
		//	else Nacka doesn't want the dates to be set by default
		//		date.setToCurrentDate();
			if (isAdmin)
				date.setYearRange(stamp.getYear() - 5, stamp.getYear() + 5);
			inputTable.add(fromText, 1, row);
			inputTable.add(date, 3, row++);
			
			if (isAdmin) {
				DateInput queueDate = (DateInput)getStyledInterface(new DateInput(PARAM_QUEUE_DATE + "_" + i));
				if (application != null)
					queueDate.setDate(application.getQueueDate());
				else
					queueDate.setToCurrentDate();
				if (isAdmin)
					queueDate.setYearRange(stamp.getYear() - 5, stamp.getYear() + 5);
				inputTable.add(queueDateText, 1, row);
				inputTable.add(queueDate, 3, row++);
			}

			inputTable.setHeight(row++, 12);
		}
		
		TextArea messageArea = (TextArea) getStyledInterface(new TextArea(PARAM_MESSAGE));
		messageArea.setRows(4);
		messageArea.setWidth(Table.HUNDRED_PERCENT);
		if (message != null)
			messageArea.setContent(message);

		inputTable.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		inputTable.add(getSmallHeader(localize("child_care.message","Message")), 1, row);
		inputTable.add(messageArea, 3, row++);

		inputTable.setWidth(1, 100);
		inputTable.setWidth(2, 8);
		
		return inputTable;
	}

	private Table getChildInfoTable(IWContext iwc) {
		Table table = new Table(3,3);
		table.setColumns(3);
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setWidth(1, 100);
		table.setWidth(2, 8);
		
		table.add(getSmallHeader(localize(NAME, "Name")+":"), 1, 1);
		table.add(getSmallHeader(localize(PID, "Personal ID")+":"), 1, 2);
		table.add(getSmallHeader(localize(ADDRESS, "Address")+":"), 1, 3);

		Name name = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
		table.add(getSmallText(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), 3, 1);
		String personalID = PersonalIDFormatter.format(child.getPersonalID(), iwc.getIWMainApplication().getSettings().getApplicationLocale());
		table.add(getSmallText(personalID), 3, 2);
		
		try {
			Address address = getUserBusiness(iwc).getUsersMainAddress(child);
			if (address != null)
				table.add(getSmallText(address.getStreetAddress() + ", " + address.getPostalAddress()), 3, 3);
		}
		catch (RemoteException e) {
			// empty
		}

		return table;
	}

	public String getSubmitCheckScript() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\nfunction checkApplication(){\n\t");
		buffer.append("\n\t var dropOne = ").append("findObj('").append(PARAM_PROVIDER + "_1").append("');");
		buffer.append("\n\t var dropTwo = ").append("findObj('").append(PARAM_PROVIDER + "_2").append("');");
		buffer.append("\n\t var dropThree = ").append("findObj('").append(PARAM_PROVIDER + "_3").append("');");
		buffer.append("\n\t var dropFour = ").append("findObj('").append(PARAM_PROVIDER + "_4").append("');");
		buffer.append("\n\t var dropFive = ").append("findObj('").append(PARAM_PROVIDER + "_5").append("');");

		buffer.append("\n\t var one = 0;");
		buffer.append("\n\t var two = 0;");
		buffer.append("\n\t var three = 0;");
		buffer.append("\n\t var four = 0;");
		buffer.append("\n\t var five = 0;");
		buffer.append("\n\t var length = 0;");

			
		
		
		buffer.append("\n\n\t if (dropOne.selectedIndex > 0) {\n\t\t one = dropOne.options[dropOne.selectedIndex].value;\n\t\t length++;\n\t }");
		buffer.append("\n\t if (dropTwo.selectedIndex > 0) {\n\t\t two = dropTwo.options[dropTwo.selectedIndex].value;\n\t\t length++;\n\t }");
		buffer.append("\n\t if (dropThree.selectedIndex > 0) {\n\t\t three = dropThree.options[dropThree.selectedIndex].value;\n\t\t length++;\n\t }");
		buffer.append("\n\t if (dropFour.selectedIndex > 0) {\n\t\t four = dropFour.options[dropFour.selectedIndex].value;\n\t\t length++;\n\t }");
		
		buffer.append("\n\t var dateDayOne = ").append("findObj('").append(PARAM_DATE + "_1_day").append("');");
		buffer.append("\n\t var dateDayTwo = ").append("findObj('").append(PARAM_DATE + "_2_day").append("');");
		buffer.append("\n\t var dateDayThree = ").append("findObj('").append(PARAM_DATE + "_3_day").append("');");
		buffer.append("\n\t var dateDayFour = ").append("findObj('").append(PARAM_DATE + "_4_day").append("');");

		buffer.append("\n\t var dateMonthOne = ").append("findObj('").append(PARAM_DATE + "_1_month").append("');");
		buffer.append("\n\t var dateMonthTwo = ").append("findObj('").append(PARAM_DATE + "_2_month").append("');");
		buffer.append("\n\t var dateMonthThree = ").append("findObj('").append(PARAM_DATE + "_3_month").append("');");
		buffer.append("\n\t var dateMonthFour = ").append("findObj('").append(PARAM_DATE + "_4_month").append("');");
		
		buffer.append("\n\t var dateYearOne = ").append("findObj('").append(PARAM_DATE + "_1_year").append("');");
		buffer.append("\n\t var dateYearTwo = ").append("findObj('").append(PARAM_DATE + "_2_year").append("');");
		buffer.append("\n\t var dateYearThree = ").append("findObj('").append(PARAM_DATE + "_3_year").append("');");
		buffer.append("\n\t var dateYearFour = ").append("findObj('").append(PARAM_DATE + "_4_year").append("');");
		
		if (!hasActivePlacement) {
			buffer.append("\n\t if (dropFive.selectedIndex > 0) {\n\t\t five = dropFive.options[dropFive.selectedIndex].value;\n\t\t length++;\n\t ");
			buffer.append("\n\t var dateDayFive = ").append("findObj('").append(PARAM_DATE + "_5_day").append("');");
			buffer.append("\n\t var dateMonthFive = ").append("findObj('").append(PARAM_DATE + "_5_month").append("');");
			buffer.append("\n\t var dateYearFive = ").append("findObj('").append(PARAM_DATE + "_5_year").append("');");
			buffer.append("\n\t }");
		}
	
		buffer.append("\n\t if(one > 0 && dateDayOne.selectedIndex <= 0 || dateMonthOne.selectedIndex <= 0 || dateYearOne.selectedIndex <= 0){");
		String message = localize("must_set_date", "Please set the date.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t else if(two > 0 && dateDayTwo.selectedIndex <= 0 || dateMonthTwo.selectedIndex <= 0 || dateYearTwo.selectedIndex <= 0){");
		message = localize("must_set_date", "Please set the date.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t else if(three > 0 && dateDayThree.selectedIndex <= 0 || dateMonthThree.selectedIndex <= 0 || dateYearThree.selectedIndex <= 0){");
		message = localize("must_set_date", "Please set the date.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t else if(four > 0 && dateDayFour.selectedIndex <= 0 || dateMonthFour.selectedIndex <= 0 || dateYearFour.selectedIndex <= 0){");
		message = localize("must_set_date", "Please set the date.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		if (!hasActivePlacement){
		buffer.append("\n\t else if(five > 0 && dateDayFive.selectedIndex <= 0 || dateMonthFive.selectedIndex <= 0 || dateYearFive.selectedIndex <= 0){");
		message = localize("must_set_date", "Please set the date.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		}
		
		buffer.append("\n\t if(length > 0){");
		buffer.append("\n\t\t if(one > 0 && (one == two || one == three || one == four || one == five)){");
		message = localize("child_care.must_not_be_the_same", "Please do not choose the same provider more than once.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t\t if(two > 0 && (two == one || two == three || two == four || two == five)){");
		message = localize("child_care.must_not_be_the_same", "Please do not choose the same provider more than once.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t\t if(three > 0 && (three == one || three == two || three == four || three == five)){");
		message = localize("child_care.must_not_be_the_same", "Please do not choose the same provider more than once.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t\t if(four > 0 && (four == one || four == two || four == three || four == five)){");
		message = localize("child_care.must_not_be_the_same", "Please do not choose the same provider more than once.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t\t if(five > 0 && (five == one || five == two || five == three || five == four)){");
		message = localize("child_care.must_not_be_the_same", "Please do not choose the same provider more than once.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t }");
		buffer.append("\n\t else {");
		message = localize("child_care.must_fill_out_one", "Please fill out the first choice.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		if (!hasActivePlacement) {
			message = localize("child_care.less_than_five_chosen", "You have chosen less than five choices.  An offer can not be guaranteed within three months.");
			buffer.append("\n\t if(length < 5)\n\t\t return confirm('").append(message).append("');");
			buffer.append("\n\t return true;");
		}
		buffer.append("\n}\n");
		return buffer.toString();
	}
	
	private ProviderDropdownDouble getDropdown(Locale locale, String primaryName, String secondaryName) {
		ProviderDropdownDouble dropdown = new ProviderDropdownDouble(primaryName, secondaryName);
		String emptyString = localize("child_care.select_provider","Select provider...");
		dropdown.addEmptyElement(localize("child_care.select_area","Select area..."), emptyString);
		
		try {
			if (areas == null)
				areas = getBusiness().getSchoolBusiness().findAllSchoolAreas();
			if (providerMap == null)
				providerMap = getBusiness().getProviderAreaMap(areas, currentProvider, locale, emptyString, false);
				
			if (areas != null && providerMap != null) {
				Iterator iter = areas.iterator();
				while (iter.hasNext()) {
					SchoolArea area = (SchoolArea) iter.next();
					dropdown.addMenuElement(area.getPrimaryKey().toString(), area.getSchoolAreaName(), (Map) providerMap.get(area));
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return dropdown;
	}

	private CheckBusiness getCheckBusiness(IWApplicationContext iwac) throws RemoteException {
		return (CheckBusiness) IBOLookup.getServiceInstance(iwac, CheckBusiness.class);
	}
}