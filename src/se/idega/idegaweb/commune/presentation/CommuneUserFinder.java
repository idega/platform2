/*
 * Created on 30.3.2003
 */
package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.TextSoap;

/**
 * @author laddi
 */
public abstract class CommuneUserFinder extends CommuneBlock {

	private String PARAMETER_SEARCH = "cul_search";
	private String searchString;

	/**
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		parseAction(iwc);
		drawForm(iwc);		
	}
	
	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_SEARCH))
			searchString = iwc.getParameter(PARAMETER_SEARCH);
		if (searchString != null && searchString.length() > 0) {
			try {
				String temp = searchString;
				temp = TextSoap.findAndCut(temp, "-");
				Long.parseLong(temp);
				if (temp.length() == 10 ) {
					int firstTwo = Integer.parseInt(temp.substring(0, 2));
					if (firstTwo < 04) {
						temp = "20"+temp;
					}	else {
						temp = "19"+temp;
					}
				}
				searchString = temp;
			}
			catch (NumberFormatException nfe) {}
		}
			
	}
	
	private void drawForm(IWContext iwc) {
		Table table = new Table(1,3);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setHeight(2, 18);
		
		table.add(getSearchForm(), 1, 1);
		if (searchString != null)
			table.add(getUserForm(iwc), 1, 3);
		
		add(table);
	}
	
	private Form getSearchForm() {
		Form form = new Form();
		
		Table table = new Table(1,4);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setHeight(3, 6);
		form.add(table);
		
		table.add(getSmallHeader(localize("commune.enter_search_string","Enter search string")+":"), 1, 1);
		
		TextInput searchInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_SEARCH));
		searchInput.setLength(40);
		searchInput.keepStatusOnAction(true);
		table.add(searchInput, 1, 2);
		
		SubmitButton searchButton = (SubmitButton) this.getButton(new SubmitButton(localize("search","Search")));
		table.add(searchButton, 1, 4);
				
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
			Collection users = getUserBusiness(iwc).getUserHome().findUsersBySearchCondition(searchString);
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
						if (row == 2)
							radio.setSelected();
					
						table.add(radio, 1, row);
						table.add(Text.getNonBrakingSpace(), 1, row);
						table.add(getSmallText(user.getNameLastFirst(true)), 1, row);
						table.add(getSmallText(" ("), 1, row);
						table.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())+")"), 1, row++);
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
		}
		catch (Exception e) {
		}
		
		return form;
	}
	
	public abstract boolean addUser(IWContext iwc, User user);
	
	public abstract String getParameterName(IWContext iwc);
	
	public abstract Class getEventListener();
	
	public abstract String getSubmitDisplay();
	
	public abstract String getNoUserFoundString();
	
	public abstract String getFoundUsersString();

	private CommuneUserBusiness getUserBusiness(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
}