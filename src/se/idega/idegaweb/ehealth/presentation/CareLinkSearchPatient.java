/**
 * Created on 2004-nov-04
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;

import java.util.Collection;
import java.util.Iterator;

import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
//import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.RadioGroup;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
//import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.presentation.UserSearcher;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;
import com.idega.util.text.TextSoap;

/**
 * @author laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class CareLinkSearchPatient extends EHealthBlock {

	public static final String PREFIX = "carelink_";
	public static final String PARAMETER_SEARCH = "cul_search";
	public static final String PARAMETER_METHOD = "cul_method";
	public static final String PARAMETER_PERSONAL_ID = "cul_personal_id";
	public static final String PARAMETER_AGREE = PREFIX + "agree";
	public static final int METHOD_SEARCH = 1;
	public static final int METHOD_LOGIN = 2;
	
	//private static final String DEFAULT_HOME_PAGE = "default_citizen_home_page";
	
	private static final String AGREE_TEXT_1_KEY = PREFIX + "cl_no";
	private static final String AGREE_TEXT_2_KEY = PREFIX + "cl_no_because";
	private static final String AGREE_TEXT_3_KEY = PREFIX + "cl_yes_for_now";
	private static final String AGREE_TEXT_4_KEY = PREFIX + "cl_yes_until";
	private static final String NEXT_KEY =  PREFIX + "cl_next";
		
	private static final String AGREE_TEXT_1_DEFAULT = "No";
	private static final String AGREE_TEXT_2_DEFAULT = "No because..";
	private static final String AGREE_TEXT_3_DEFAULT = "Yes, ";
	private static final String AGREE_TEXT_4_DEFAULT = "Yes, until..";
	private static final String NEXT_DEFAULT =  "Next";
	
	private String searchString;
	private String personalID = null;
	private String userID = null;
	private int method = METHOD_SEARCH;
	private String searchIdentifier = "commlogin";
	private boolean useSearcher = true;
	/**
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		
		parseAction(iwc);
		
		switch (method) {
			case METHOD_SEARCH:
				if(useSearcher)
					drawForm2();
				else
					drawForm(iwc);
				break;
			case METHOD_LOGIN:
				//logIn(iwc);
				drawForm2();
				getAgreeForm();
				
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
		
		String searcherUserPRM = UserSearcher.getUniqueUserParameterName(searchIdentifier);
		if (iwc.isParameterSet(searcherUserPRM))
			userID = iwc.getParameter(searcherUserPRM);
	}
	
	private void drawForm2(){
		Table table = new Table(1,3);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setHeight(2, 18);
		UserSearcher searcher = new UserSearcher();
		searcher.setUniqueIdentifier(searchIdentifier);
		searcher.setBundleIdentifer(this.getBundleIdentifier());
		searcher.maintainParameter(new Parameter(PARAMETER_METHOD,String.valueOf(METHOD_LOGIN)));
		searcher.setSkipResultsForOneFound(false);
		searcher.setLegalNonDigitPIDLetters("TFtf");
		table.add(searcher,1,1);
		add(table);
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
			Collection users = getUserBusiness(iwc).getUserHome().findUsersBySearchCondition(searchString, true);
			if (!users.isEmpty()) {
				User user;
				//RadioButton radio;
				int row = 1;
				
				table.add(getSmallHeader(localize("commune.found_users","Found users")+":"), 1, row++);
				table.setHeight(row++, 6);
				
				Iterator iter = users.iterator();
				while (iter.hasNext()) {
					user = (User) iter.next();
				   //radio = getRadioButton(PARAMETER_PERSONAL_ID, user.getPersonalID());
					if (row == 3)
					//	radio.setSelected();
					
					//table.add(radio, 1, row);
					table.add(Text.getNonBrakingSpace(), 1, row);
					Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
					table.add(getSmallText(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), 1, row);
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

	
	private void getAgreeForm() {
		Form form = new Form();
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		form.add(table);
		
		final RadioGroup radioGroup = new RadioGroup (PARAMETER_AGREE);
		radioGroup.addRadioButton (localize (AGREE_TEXT_1_KEY, AGREE_TEXT_1_DEFAULT));
		radioGroup.addRadioButton (localize (AGREE_TEXT_2_KEY, AGREE_TEXT_2_DEFAULT));
		radioGroup.addRadioButton (localize (AGREE_TEXT_3_KEY, AGREE_TEXT_3_DEFAULT));
		radioGroup.addRadioButton (localize (AGREE_TEXT_4_KEY, AGREE_TEXT_4_DEFAULT));
		
		GenericButton send = getButton(new GenericButton("next", localize(NEXT_KEY, NEXT_DEFAULT)));
		
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_BOTTOM);
		table.add(new Break(2), 3, 1);
		
		table.add(radioGroup, 1, 1);
		table.setHeight(2, 1, "20");
		table.add(send, 3, 1);
		
		add(form);
	}
	
	
	public void setToUseSearcher(boolean flag){
		useSearcher = flag;
	}
	
}
