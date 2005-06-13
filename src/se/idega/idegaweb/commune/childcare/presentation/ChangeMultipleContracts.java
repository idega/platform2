/*
 * $Id: ChangeMultipleContracts.java,v 1.1 2005/06/13 18:24:02 laddi Exp $
 * Created on Jun 13, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import com.idega.block.school.data.School;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;


/**
 * Last modified: $Date: 2005/06/13 18:24:02 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class ChangeMultipleContracts extends ChildCareBlock {
	
	private static final String PARAMETER_CHANGE = "change_all";
	private static final String PARAMETER_FROM_CARE_TIME = "from_care_time";
	private static final String PARAMETER_TO_CARE_TIME = "to_care_time";
	private static final String PARAMETER_DATE_OF_CHANGE = "date_of_change";
	private static final String PARAMETER_FROM_DATE_OF_BIRTH = "from_date_of_birth";
	private static final String PARAMETER_TO_DATE_OF_BIRTH = "to_date_of_birth";
	
	public void init(IWContext iwc) throws Exception {
		if (iwc.isParameterSet(PARAMETER_CHANGE)) {
			changeContracts(iwc);
		}
		else {
			showForm();
		}
	}
	
	private void showForm() {
		Form form = new Form();
		form.addParameter(PARAMETER_CHANGE, Boolean.TRUE.toString());
		form.setID("changeContractsForm");
		
		InterfaceObject fromCareTime = null;
		if (isUsePredefinedCareTimeValues()) {
			fromCareTime = getCareTimeMenu(PARAMETER_FROM_CARE_TIME);
		}
		else {
			fromCareTime = (TextInput) getStyledInterface(new TextInput(PARAMETER_FROM_CARE_TIME));
			((TextInput) fromCareTime).setAsNotEmpty(localize("child_care.child_care_time_required", "You must fill in the child care time."));
			((TextInput) fromCareTime).setAsIntegers(localize("child_care.only_integers_allowed", "Not a valid child care time."));
		}
		Label fromCareLabel = new Label(localize("child_care.from_care_time", "From care time"), fromCareTime);
		form.add(fromCareLabel);
		form.add(fromCareTime);
		form.add(new Break());

		InterfaceObject toCareTime = null;
		if (isUsePredefinedCareTimeValues()) {
			toCareTime = getCareTimeMenu(PARAMETER_TO_CARE_TIME);
		}
		else {
			toCareTime = (TextInput) getStyledInterface(new TextInput(PARAMETER_TO_CARE_TIME));
			((TextInput) toCareTime).setAsNotEmpty(localize("child_care.child_care_time_required", "You must fill in the child care time."));
			((TextInput) toCareTime).setAsIntegers(localize("child_care.only_integers_allowed", "Not a valid child care time."));
		}
		Label toCareLabel = new Label(localize("child_care.to_care_time", "To care time"), toCareTime);
		form.add(toCareLabel);
		form.add(toCareTime);
		form.add(new Break());

		IWTimestamp stamp = new IWTimestamp();
		DateInput dateOfChange = (DateInput) getStyledInterface(new DateInput(PARAMETER_DATE_OF_CHANGE));
		dateOfChange.setDate(stamp.getDate());
		Label dateOfChangeLabel = new Label(localize("child_care.date_of_change", "Date of change"), dateOfChange);
		form.add(dateOfChangeLabel);
		form.add(dateOfChange);
		form.add(new Break());
		
		IWTimestamp fromDate = new IWTimestamp();
		fromDate.addYears(-4);
		fromDate.setDay(1);
		fromDate.setMonth(1);
		DateInput fromDateOfBirth = (DateInput) getStyledInterface(new DateInput(PARAMETER_FROM_DATE_OF_BIRTH));
		fromDateOfBirth.setDate(fromDate.getDate());
		Label fromDateOfBirthLabel = new Label(localize("child_care.from_date_of_birth", "From date of birth"), fromDateOfBirth);
		form.add(fromDateOfBirthLabel);
		form.add(fromDateOfBirth);
		form.add(new Break());

		IWTimestamp toDate = new IWTimestamp();
		toDate.addYears(-4);
		toDate.setDay(31);
		toDate.setMonth(12);
		DateInput toDateOfBirth = (DateInput) getStyledInterface(new DateInput(PARAMETER_TO_DATE_OF_BIRTH));
		toDateOfBirth.setDate(toDate.getDate());
		Label toDateOfBirthLabel = new Label(localize("child_care.to_date_of_birth", "To date of birth"), toDateOfBirth);
		form.add(toDateOfBirthLabel);
		form.add(toDateOfBirth);
		form.add(new Break());

		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("submit", "Submit")));
		form.add(new Break());
		form.add(submit);

		add(form);
	}
	
	private void changeContracts(IWContext iwc) {
		String fromCareTime = iwc.getParameter(PARAMETER_FROM_CARE_TIME);
		String toCareTime = iwc.getParameter(PARAMETER_TO_CARE_TIME);
		IWTimestamp dateOfChange = new IWTimestamp(iwc.getParameter(PARAMETER_DATE_OF_CHANGE));
		IWTimestamp fromDateOfBirth = new IWTimestamp(iwc.getParameter(PARAMETER_FROM_DATE_OF_BIRTH));
		IWTimestamp toDateOfBirth = new IWTimestamp(iwc.getParameter(PARAMETER_TO_DATE_OF_BIRTH));
		
		try {
			Collection contracts = getBusiness().changeAllContractsInRange(fromCareTime, toCareTime, dateOfChange.getDate(), fromDateOfBirth.getDate(), toDateOfBirth.getDate(), iwc.getCurrentUser(), iwc.getCurrentLocale());
			
			Table table = new Table();
			table.setCellpadding(getCellpadding());
			table.setCellspacing(getCellspacing());
			table.setColumns(3);
			table.setRowColor(1, getHeaderColor());
			int row = 1;
			int column = 1;
				
			table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.provider","Provider"), column++, row++);

			Iterator iter = contracts.iterator();
			while (iter.hasNext()) {
				column = 1;
				ChildCareContract contract = (ChildCareContract) iter.next();
				User user = contract.getChild();
				School school = contract.getApplication().getProvider();
				Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
				
				table.add(getSmallText(name.getName(iwc.getCurrentLocale(), true)), column++, row);
				table.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				table.add(getSmallText(school.getSchoolName()), column, row++);
			}
			
			add(getHeader(localize("child_care.changed_contracts_for_children", "Contracts changed for the following children:")));
			add(new Break(2));
			add(table);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
}