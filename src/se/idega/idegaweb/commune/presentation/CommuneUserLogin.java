/**
 * Created on 30.1.2003
 *
 * This class does something very clever.
 */
package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.block.login.business.LoginBusiness;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.TextSoap;

/**
 * @author laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class CommuneUserLogin extends CommuneBlock {

	public static final String PARAMETER_SEARCH = "cul_search";
	public static final String PARAMETER_METHOD = "cul_method";
	public static final String PARAMETER_PERSONAL_ID = "cul_personal_id";
	public static final int METHOD_SEARCH = 1;
	public static final int METHOD_LOGIN = 2;
	
	private String searchString;
	private String personalID;
	private int method = METHOD_SEARCH;
	/**
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		parseAction(iwc);
		
		switch (method) {
			case METHOD_SEARCH:
				drawForm(iwc);
				break;
			case METHOD_LOGIN:
				logIn(iwc);
				break;
		}
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
			
		if (iwc.isParameterSet(PARAMETER_METHOD))
			method = Integer.parseInt(iwc.getParameter(PARAMETER_METHOD));
		else
			method = METHOD_SEARCH;
			
		if (iwc.isParameterSet(PARAMETER_PERSONAL_ID))
			personalID = iwc.getParameter(PARAMETER_PERSONAL_ID);
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
				
				table.add(getSmallHeader(localize("commune.found_users","Found users")+":"), 1, row++);
				table.setHeight(row++, 6);
				
				Iterator iter = users.iterator();
				while (iter.hasNext()) {
					user = (User) iter.next();
					radio = getRadioButton(PARAMETER_PERSONAL_ID, user.getPersonalID());
					if (row == 3)
						radio.setSelected();
					
					table.add(radio, 1, row);
					table.add(Text.getNonBrakingSpace(), 1, row);
					table.add(getSmallText(user.getNameLastFirst(true)), 1, row);
					table.add(getSmallText(" ("), 1, row);
					table.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())+")"), 1, row++);
				}
				
				table.setHeight(row++, 6);
				SubmitButton login = (SubmitButton) getButton(new SubmitButton(localize("commune.login_user","Login user")));
				table.add(new HiddenInput(PARAMETER_METHOD,String.valueOf(METHOD_LOGIN)), 1, row);
				table.add(login, 1, row);
			}
		}
		catch (Exception e) {
		}
		
		return form;
	}

	private void logIn(IWContext iwc) {
		LoginBusiness business = new LoginBusiness();
		try {
			boolean canLogIn = business.logInByPersonalID(iwc, personalID);
			
			if (canLogIn) {
				User user = getUserBusiness(iwc).getUser(personalID);
				Group group = user.getPrimaryGroup();
				if ( user.getHomePageID() != -1 )
					iwc.forwardToIBPage(getParentPage(), user.getHomePage());
				if (group != null && group.getHomePageID() != -1 )
					iwc.forwardToIBPage(getParentPage(), group.getHomePage());
			}
			else {
				add(getBackTable());
			}
		}
		catch (Exception e) {
			add(getBackTable());
		}
	}
	
	private Form getBackTable() {
		Form form = new Form();
		form.add(new HiddenInput(PARAMETER_METHOD,String.valueOf(METHOD_SEARCH)));
		form.add(localize("commune.login_error","Error logging in as user")+": "+personalID);
		form.add(new Break(2));
		form.add(new SubmitButton(localize("back","Back")));
		
		return form;
	}
	
	private CommuneUserBusiness getUserBusiness(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
}
