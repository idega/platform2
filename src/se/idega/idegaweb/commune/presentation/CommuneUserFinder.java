/*
 * Created on 30.3.2003
 */
package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.TextSoap;

/**
 * @author laddi
 */
public abstract class CommuneUserFinder extends CommuneBlock {

	private static final String SEARCH_PERSONAL_ID = "usrch_search_pid";
	private static final String SEARCH_LAST_NAME = "usrch_search_lname";
	private static final String SEARCH_MIDDLE_NAME = "usrch_search_mname";
	private static final String SEARCH_FIRST_NAME = "usrch_search_fname";
	
	private static final String PARAMETER_FIRST_NAME = "cul_pfn";
	private static final String PARAMETER_MIDDLE_NAME = "cul_pmn";
	private static final String PARAMETER_LAST_NAME = "cul_pln";
	private static final String PARAMETER_PERSONAL_ID = "cul_pid";
	private static final String PARAMETER_SEARCH = "cul_search";

	private boolean multipleInputs = false;
	private Collection users = null;
	
	/**
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		parseAction(iwc);
		drawForm(iwc);		
	}
	
	private void parseAction(IWContext iwc) throws RemoteException {
		if (iwc.isParameterSet(PARAMETER_SEARCH)) {
			String searchString = iwc.getParameter(PARAMETER_SEARCH);
			users = getUserBusiness(iwc).findSchoolChildrenBySearchCondition(searchString);
		} else if (iwc.isParameterSet(PARAMETER_FIRST_NAME) || iwc.isParameterSet(PARAMETER_MIDDLE_NAME) || iwc.isParameterSet(PARAMETER_LAST_NAME) || iwc.isParameterSet(PARAMETER_PERSONAL_ID)) {
			String first = iwc.getParameter(PARAMETER_FIRST_NAME);
			String middle = iwc.getParameter(PARAMETER_MIDDLE_NAME);
			String last = iwc.getParameter(PARAMETER_LAST_NAME);
			if (first != null)
				first = TextSoap.capitalize(first);
			if (middle != null)
				middle = TextSoap.capitalize(middle);
			if (last != null)
				last = TextSoap.capitalize(last);
			String pid = iwc.getParameter(PARAMETER_PERSONAL_ID);
			pid = pid.replaceAll("-", "");
			UserHome home = (UserHome) IDOLookup.getHome(User.class);
			try {
				users = home.findUsersByConditions(first, middle, last, pid, null, null, -1, -1, -1, -1, null, null, true, false);
			} catch (FinderException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void drawForm(IWContext iwc) {
		Table table = new Table(1,3);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setHeight(2, 18);
		
		table.add(getSearchForm(), 1, 1);
		if (users != null) {
			table.add(getUserForm(iwc), 1, 3);
		}
		
		add(table);
	}
	
	private Form getSearchForm() {
		Form form = new Form();
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setHeight(3, 6);
		form.add(table);
		int column = 1;
		

		if (this.multipleInputs) {
			table.add(getSmallHeader(localize(SEARCH_PERSONAL_ID, "Personal ID")), column, 1);
			TextInput pidInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_PERSONAL_ID));
			pidInput.setLength(15);
			pidInput.keepStatusOnAction(true);
			table.add(pidInput, column++, 2);
			
			++column;
			table.add(getSmallHeader(localize(SEARCH_LAST_NAME, "Last name")), column, 1);
			TextInput lastNameInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_LAST_NAME));
			lastNameInput.setLength(15);
			lastNameInput.keepStatusOnAction(true);
			table.add(lastNameInput, column++, 2);
			
			++column;
			table.add(getSmallHeader(localize(SEARCH_MIDDLE_NAME, "Middle name")), column, 1);
			TextInput middleNameInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_MIDDLE_NAME));
			middleNameInput.setLength(15);
			middleNameInput.keepStatusOnAction(true);
			table.add(middleNameInput, column++, 2);

			++column;
			table.add(getSmallHeader(localize(SEARCH_FIRST_NAME, "First name")), column, 1);
			TextInput firstNameInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_FIRST_NAME));
			firstNameInput.setLength(15);
			firstNameInput.keepStatusOnAction(true);
			table.add(firstNameInput, column++, 2);
		} else {
			table.add(getSmallHeader(localize("commune.enter_search_string","Enter search string")+":"), 1, 1);
			TextInput searchInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_SEARCH));
			searchInput.setLength(40);
			searchInput.keepStatusOnAction(true);
			table.add(searchInput, 1, 2);
		}
		
		if (column > 1) {
			table.mergeCells(1, 3, column, 3);
			table.mergeCells(1, 4, column, 4);
			table.mergeCells(1, 5, column, 5);
		}
		table.setRowHeight(3, "12");
		table.add(getSmallHeader(localize("commune.instructions", "Instructions")+" :"), 1, 4);
		table.add(getSmallText(localize("commune.search_instructions", "When searching for a name make sure you use Capital letters when needed. Ex. <i>john</i> might not work while <i>John</i> would. <br>When searching for a personal id, you must use the whole year. Ex. <i>97</i> will not work, but <i>1997</i> will.")), 1, 5);
		
		SubmitButton searchButton = (SubmitButton) this.getButton(new SubmitButton(getSearchSubmitDisplay()));
		table.setRowHeight(6, "12");
		table.add(searchButton, 1, 7);
				
		return form;
	}
	
	private Form getUserForm(IWContext iwc) {
		Form form = new Form();
		if (getEventListener() != null)
			form.setEventListener(getEventListener());
		if (getResponsePage() != null)
			form.setPageToSubmitTo(getResponsePage());
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		form.add(table);
		
		try {
			if (!users.isEmpty()) {
				User user;
				RadioButton radio;
				int row = 1;
				
				table.add(getSmallHeader(getFoundUsersString()+":"), 1, row++);
				table.setHeight(row++, 6);
				boolean showSubmit = false;
				
				Iterator iter = users.iterator();
				while (iter.hasNext()) {
					user = (User) iter.next();
					if (addUser(iwc, user)) {
						showSubmit = true;
						radio = getRadioButton(getParameterName(iwc), user.getPrimaryKey().toString());
						if (row == 3)
							radio.setSelected();
					
						table.add(radio, 1, row);
						table.add(Text.getNonBrakingSpace(), 1, row);
						table.add(getSmallText(user.getNameLastFirst(true)), 1, row);
						if (user.getPersonalID() != null) {
							table.add(getSmallText(" ("), 1, row);
							table.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())+")"), 1, row++);
						}
						else
							row++;
					}
				}
				
				if (showSubmit) {
					table.setHeight(row++, 6);
					SubmitButton submit = (SubmitButton) getButton(new SubmitButton(getSubmitDisplay()));
					table.add(submit, 1, row);
				}
				else 
					table.add(getSmallErrorText(getNoUserFoundString()));
			}
			else 
				table.add(getSmallErrorText(getNoUserFoundString()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return form;
	}
	
	public abstract boolean addUser(IWContext iwc, User user);
	
	public abstract String getParameterName(IWContext iwc);
	
	public abstract Class getEventListener();
	
	public abstract String getSubmitDisplay();
	
	public abstract String getSearchSubmitDisplay();
	
	public abstract String getNoUserFoundString();
	
	public abstract String getFoundUsersString();

	private CommuneUserBusiness getUserBusiness(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
	
	public void setUsesMultipleInputs(boolean multipleInputs) {
		this.multipleInputs = multipleInputs;
	}
}