/*
 * Created on 11.4.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.check.data.GrantedCheck;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

import com.idega.block.school.data.SchoolArea;
import com.idega.business.IBOLookup;
import com.idega.core.data.Address;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;

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
	
	private boolean _noCheckError = false;

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parseAction(iwc);
		
		switch (_action) {
			case ACTION_VIEW_FORM :
				viewForm(iwc);
				break;
			case ACTION_SUBMIT :
				submitForm(iwc);
				break;
		}
	}
	
	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION))
			_action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		else
			_action = ACTION_VIEW_FORM;

		try {
			check = getCheckBusiness(iwc).getGrantedCheck(getSession().getCheckID());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		if (check != null) {
			child = check.getChild();
		}
		else {
			_noCheckError = true;
		}

	}

	private void viewForm(IWContext iwc) {
		Form form = new Form();
		form.setOnSubmit("return checkApplication()");
		
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(0);
		table.setCellspacing(0);
		form.add(table);
		
		int row = 1;
		if (!_noCheckError) {
			table.add(getChildInfoTable(iwc), 1, row++);
			table.setHeight(row++, 12);
			table.add(getInputTable(iwc), 1, row++);
			table.setHeight(row++, 12);
	
			SubmitButton submit = (SubmitButton)getButton(new SubmitButton(localize(PARAM_FORM_SUBMIT, "Submit application"), PARAMETER_ACTION, String.valueOf(ACTION_SUBMIT)));
			table.add(submit, 1, row);
	
			Page page = this.getParentPage();
			if (page != null) {
				Script script = page.getAssociatedScript();
				script.addFunction("checkApplication", getSubmitCheckScript());
			}
		}
		else {
			add(getErrorText(localize("child_care.no_check_selected", "No check or child selected.")));
		}

		add(form);
	}
	
	private void submitForm(IWContext iwc) {
		boolean done = false;
		try {
			int[] providers = new int[5];
			String[] dates = new String[5];
			
			for (int i = 0; i < 5; i++) {
				providers[i] = iwc.isParameterSet(PARAM_PROVIDER + "_" + (i + 1)) ? Integer.parseInt(iwc.getParameter(PARAM_PROVIDER + "_" + (i + 1))) : -1;
				dates[i] = iwc.isParameterSet(PARAM_DATE + "_" + (i + 1)) ? iwc.getParameter(PARAM_DATE + "_" + (i + 1)) : null;
			}
			String message = iwc.getParameter(PARAM_MESSAGE);

			String subject = localize(EMAIL_PROVIDER_SUBJECT, "Child care application received");
			String body = localize(EMAIL_PROVIDER_MESSAGE, "You have received a new childcare application");

			done = getBusiness().insertApplications(iwc.getCurrentUser(), providers, dates, message, getSession().getCheckID(), getSession().getChildID(), subject, body, false);
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

		ChildCareApplication application = null;
		int areaID = -1;
		for (int i = 1; i < 6; i++) {
			try {
				application = getBusiness().getApplication(getSession().getChildID(), i);
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
			inputTable.add(dropdown, 3, row++);

			DateInput date = (DateInput)getStyledInterface(new DateInput(PARAM_DATE + "_" + i));
			if (application != null)
				date.setDate(application.getFromDate());
			else
				date.setToCurrentDate();
			inputTable.add(fromText, 1, row);
			inputTable.add(date, 3, row++);

			inputTable.setHeight(row++, 6);
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

		table.add(getSmallText(child.getNameLastFirst(true)), 3, 1);
		String personalID = PersonalIDFormatter.format(child.getPersonalID(), iwc.getApplication().getSettings().getApplicationLocale());
		table.add(getSmallText(personalID), 3, 2);
		
		try {
			Address address = getUserBusiness(iwc).getUsersMainAddress(child);
			if (address != null)
				table.add(getSmallText(address.getStreetAddress() + ", " + address.getPostalAddress()), 3, 3);
		}
		catch (RemoteException e) {
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
		buffer.append("\n\t if (dropFive.selectedIndex > 0) {\n\t\t five = dropFive.options[dropFive.selectedIndex].value;\n\t\t length++;\n\t }");

		buffer.append("\n\t if(one > 0){");
		buffer.append("\n\t if(one == two || one == three || one == four || one == five){");
		String message = localize("child_care.must_not_be_the_same", "Please do not choose the same provider more than once.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t if(two > 0 && (two == three || two == four || two == five)){");
		message = localize("child_care.must_not_be_the_same", "Please do not choose the same provider more than once.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t if(three > 0 && (three == four || three == five)){");
		message = localize("child_care.must_not_be_the_same", "Please do not choose the same provider more than once.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t if(four > 0 && (four == five)){");
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
		message = localize("child_care.less_than_five_chosen", "You have chosen less than five choices.  An offer can not be guaranteed within three months.");
		buffer.append("\n\t if(length < 5)\n\t\t return confirm('").append(message).append("');");
		buffer.append("\n\t return true;");
		buffer.append("\n}\n");
		return buffer.toString();
	}
	
	private ProviderDropdownDouble getDropdown(Locale locale, String primaryName, String secondaryName) {
		ProviderDropdownDouble dropdown = new ProviderDropdownDouble(primaryName, secondaryName);
		dropdown.addEmptyElement(localize("child_care.select_area","Select area..."), localize("child_care.select_provider","Select provider..."));
		
		try {
			if (areas == null)
				areas = getBusiness().getSchoolBusiness().findAllSchoolAreas();
			if (providerMap == null)
				providerMap = getBusiness().getProviderAreaMap(areas, locale);
				
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

	private CommuneUserBusiness getUserBusiness(IWApplicationContext iwac) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwac, CommuneUserBusiness.class);
	}

	private CheckBusiness getCheckBusiness(IWApplicationContext iwac) throws RemoteException {
		return (CheckBusiness) IBOLookup.getServiceInstance(iwac, CheckBusiness.class);
	}
}