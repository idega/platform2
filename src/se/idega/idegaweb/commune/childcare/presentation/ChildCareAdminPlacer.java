/*
 * Created on 14.8.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import se.idega.idegaweb.commune.block.importer.business.AlreadyCreatedException;
import se.idega.idegaweb.commune.childcare.check.data.GrantedCheck;

import com.idega.block.navigation.presentation.UserHomeLink;
import com.idega.block.school.data.SchoolArea;
import com.idega.core.data.Address;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareAdminPlacer extends ChildCareBlock {

	private User child;
	private GrantedCheck check;
	
	private final static int ACTION_VIEW_FORM = 1;
	private final static int ACTION_SUBMIT = 2;
	
	private int _action = -1;
	
	private final static String PARAMETER_ACTION = "cca_action";
	private final static String PARAM_FORM_SUBMIT = "cca_submit";
	private final static String PARAM_FROM_DATE = "cca_from_date";
	private final static String PARAM_TO_DATE = "cca_to_date";
	private final static String PARAM_CARE_TIME = "cca_care_time";
	private final static String PARAM_AREA = "cca_area";
	private final static String PARAM_PROVIDER = "cca_provider";

	private boolean _noChildError = false;

	/* (non-Javadoc)
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

	private void viewForm(IWContext iwc) {
		boolean hasPlacing = false;
		if (child != null) {
			try {
				hasPlacing = getBusiness().hasActiveApplication(getSession().getChildID());
			}
			catch (RemoteException e) {
				hasPlacing = false;
			}
		}
		
		if (!_noChildError && !hasPlacing) {
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
	
			SubmitButton submit = (SubmitButton)getButton(new SubmitButton(localize("child_care.place_child", "Place child"), PARAMETER_ACTION, String.valueOf(ACTION_SUBMIT)));
			try {
				User parent = getBusiness().getUserBusiness().getCustodianForChild(child);
				if (parent == null)
					submit.setDisabled(true);
			}
			catch (RemoteException re) {
				submit.setDisabled(true);
			}

			table.add(submit, 1, row);
			
			if (submit.getDisabled()) {
				row++;
				table.setHeight(row++, 6);
				table.add(getSmallErrorText(localize("child_care.no_parent_found", "No parent found")), 1, row);
			}
	
			add(form);
		}
		else {
			if (hasPlacing)
				add(getErrorText(localize("child_care.child_has_placing", "Child has active placement.")));
			else
				add(getErrorText(localize("child_care.no_child_selected", "No child selected.")));
			add(new Break(2));
			add(new UserHomeLink());
		}
	}
	
	private Table getChildInfoTable(IWContext iwc) {
		Table table = new Table(3,3);
		table.setColumns(3);
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setWidth(1, 100);
		table.setWidth(2, 8);
		
		table.add(getSmallHeader(localize("child_care.name", "Name")+":"), 1, 1);
		table.add(getSmallHeader(localize("child_care.personal_id", "Personal ID")+":"), 1, 2);
		table.add(getSmallHeader(localize("child_care.address", "Address")+":"), 1, 3);

		table.add(getSmallText(child.getNameLastFirst(true)), 3, 1);
		String personalID = PersonalIDFormatter.format(child.getPersonalID(), iwc.getApplication().getSettings().getApplicationLocale());
		table.add(getSmallText(personalID), 3, 2);
		
		try {
			Address address = getBusiness().getUserBusiness().getUsersMainAddress(child);
			if (address != null)
				table.add(getSmallText(address.getStreetAddress() + ", " + address.getPostalAddress()), 3, 3);
		}
		catch (RemoteException e) {
		}

		return table;
	}

	private Table getInputTable(IWContext iwc) {
		Table inputTable = new Table();
		inputTable.setCellspacing(0);
		inputTable.setCellpadding(2);
		inputTable.setColumns(3);

		int row = 1;
		IWTimestamp stamp = new IWTimestamp();

		ProviderDropdownDouble dropdown = (ProviderDropdownDouble) getStyledInterface(getDropdown(iwc.getCurrentLocale(), PARAM_AREA, PARAM_PROVIDER));
		dropdown.getSecondaryDropdown().setAsNotEmpty(localize("child_care.must_select_provider", "You have to select a provider."));
		inputTable.add(getSmallHeader(localize("child_care.provider", "Provider") + ":"), 1, row);
		inputTable.add(dropdown, 3, row++);

		DateInput date = (DateInput)getStyledInterface(new DateInput(PARAM_FROM_DATE));
		date.setToCurrentDate();
		date.setAsNotEmpty(localize("child_care.must_select_from_date", "You have to select a from date."));
		date.setYearRange(stamp.getYear() - 5, stamp.getYear() + 5);
		inputTable.add(getSmallHeader(localize("child_care.from_date", "From") + ":"), 1, row);
		inputTable.add(date, 3, row++);
		
		DateInput toDate = (DateInput)getStyledInterface(new DateInput(PARAM_TO_DATE));
		toDate.setYearRange(stamp.getYear() - 5, stamp.getYear() + 5);
		inputTable.add(getSmallHeader(localize("child_care.to_date", "To") + ":"), 1, row);
		inputTable.add(toDate, 3, row++);
		
		inputTable.setHeight(row++, 6);
		
		TextInput careTime = (TextInput) getStyledInterface(new TextInput(PARAM_CARE_TIME));
		careTime.setAsIntegers(localize("child_care.only_allows_integers", "Only integers allowed"));
		careTime.setAsNotEmpty(localize("child_care.must_select_care_time", "You have to select a care time."));
		inputTable.add(getSmallHeader(localize("child_care.care_time", "Care time") + ":"), 1, row);
		inputTable.add(careTime, 3, row++);
		
		inputTable.setWidth(1, 100);
		inputTable.setWidth(2, 8);
		
		return inputTable;
	}

	private void submitForm(IWContext iwc) {
		boolean done = false;
		try {
			int providerID = Integer.parseInt(iwc.getParameter(PARAM_PROVIDER));
			int careTime = Integer.parseInt(iwc.getParameter(PARAM_CARE_TIME));
			IWTimestamp fromDate = new IWTimestamp(iwc.getParameter(PARAM_FROM_DATE));
			IWTimestamp toDate = null;
			if (iwc.isParameterSet(PARAM_TO_DATE))
				toDate = new IWTimestamp(iwc.getParameter(PARAM_TO_DATE));
			
			User parent = getBusiness().getUserBusiness().getCustodianForChild(child);
			getBusiness().importChildToProvider(getSession().getChildID(), providerID, -1, careTime, fromDate, toDate, iwc.getCurrentLocale(), parent, iwc.getCurrentUser());
			done = true;
		}
		catch (RemoteException e) {
			e.printStackTrace();
			done = false;
		}
		catch (AlreadyCreatedException e) {
			done = false;
		}

		if (done) {
			if (getResponsePage() != null)
				iwc.forwardToIBPage(getParentPage(), getResponsePage());
			else
				add(getHeader(localize("child_care.child_placed", "Child placed")));
		}
		else
			add(getErrorText(localize("child_care.placing_failed", "Failed to place child")));
	}

	private ProviderDropdownDouble getDropdown(Locale locale, String primaryName, String secondaryName) {
		ProviderDropdownDouble dropdown = new ProviderDropdownDouble(primaryName, secondaryName);
		String emptyString = localize("child_care.select_provider","Select provider...");
		dropdown.addEmptyElement(localize("child_care.select_area","Select area..."), emptyString);
		
		try {
			Collection areas = getBusiness().getSchoolBusiness().findAllSchoolAreas();
			Map providerMap = getBusiness().getProviderAreaMap(areas, locale, emptyString);
				
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

	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION))
			_action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		else
			_action = ACTION_VIEW_FORM;

		try {
			child = getBusiness().getUserBusiness().getUser(getSession().getChildID());
		}
		catch (RemoteException re) {
			_noChildError = true;
		}
	}
}