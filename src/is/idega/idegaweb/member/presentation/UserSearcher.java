/*
 * Created on Aug 1, 2003
 *
 */
package is.idega.idegaweb.member.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.text.TextSoap;

/**
 * UserSearcher small adjustable search block, used to search for users in database.
 * 
 * @author aron 
 * @version 1.0
 */

public class UserSearcher extends Block {

	private static final String SEARCH_PERSONAL_ID = "usrch_search_pid";
	private static final String SEARCH_LAST_NAME = "usrch_search_lname";
	private static final String SEARCH_MIDDLE_NAME = "usrch_search_mname";
	private static final String SEARCH_FIRST_NAME = "usrch_search_fname";
	private static final String SEARCH_COMMITTED = "mbe_act_search";
	
	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_HEADER = "Header";
	
	private String textFontStyle = "font-weight:plain;";
	private String headerFontStyle = "font-weight:bold;";
	
	/** Parameter for user id */
	public static final String PRM_USER_ID = "usrch_user_id";
	/** The userID is the handled users ID. */
	private Integer userID = null;
	/** The user currently handled */
	private User user = null;
	/** A Collection of users complying to search */
	private Collection usersFound = null;
	/** flag telling if we have more than one user */
	private boolean hasManyUsers = false;
	/** Determines if we should allow search by users first name*/
	private boolean showFirstNameInSearch = true;
	/** Determines if we should allow search by users middle name*/
	private boolean showMiddleNameInSearch = true;
	/** Determines if we should allow search by users last name*/
	private boolean showLastNameInSearch = true;
	/** Determines if we should allow search by users personal ID*/
	private boolean showPersonalIDInSearch = true;
	/** Maximum search result rows */
	private int maxFoundUserRows = 20;
	/** Maximum search result columns */
	private int maxFoundUserCols = 3;
	/** The dynamic bundle identifier*/
	private String bundleIdentifer = null;
	/** The  static bundle identifier used in this package */
	private static String BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	/** The Bundle */
	private IWBundle iwb;
	/** The resource bundle */
	private IWResourceBundle iwrb;
	/** flag for process method */
	private boolean processed = false;
	/** list of maintainparameters */
	private List maintainedParameters = new Vector();
	
	/** personalID input length */
	private int personalIDLength = 10;
	/** firstname input length */
	private int firstNameLength = 10;
	/** middlename input length */
	private int middleNameLength = 10;
	/**lastname input length */
	private int lastNameLength = 10;
	
	/** stacked view flag : if stacked heading appears above inputs*/
	private boolean stacked = true;
	/** First letter in names case insensitive*/
	private boolean firstLetterCaseInsensitive = true;
	
	

	public void main(IWContext iwc) throws Exception {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		String message = null;
		try {
			process(iwc);
		}
		catch (RemoteException e) {
			e.printStackTrace();
			message = iwrb.getLocalizedString("usrch.service_available","Search service not available"); 
		}
		catch (FinderException e) {
			e.printStackTrace();
			message = iwrb.getLocalizedString("usrch.no_user_found","No user found"); 
		}
		Table T = new Table();
		T.add(presentateCurrentUserSearch(iwc), 1, 1);
		if (hasManyUsers) {
			T.add(presentateFoundUsers(iwc),1,2);
		}
		if(message!=null){
			Text tMessage = new Text(message);
			T.add(tMessage,1,3);
		}
			
		add(T);

	}
	
	/**
	 * Main processing method, searches if search has ben committed, or looks up the user chosen
	 * is called by main(),
	 * @param iwc
	 */
	public void process(IWContext iwc) throws FinderException, RemoteException{
		if(processed)
			return;
		if(iwc.isParameterSet(SEARCH_COMMITTED)){
			processSearch( iwc);
		}
		else if (iwc.isParameterSet(PRM_USER_ID))
			userID = Integer.valueOf(iwc.getParameter(PRM_USER_ID));
		if (userID != null) {
			try {
				UserHome home = (UserHome) IDOLookup.getHome(User.class);
				user = home.findByPrimaryKey(userID);
			}
			catch (IDOLookupException e) {
				throw new FinderException(e.getMessage());
			}
			
		}
		
		processed = true;
	}

	private void processSearch(IWContext iwc) throws IDOLookupException, FinderException, RemoteException {
		UserHome home = (UserHome) IDOLookup.getHome(User.class);
		String first = iwc.getParameter(SEARCH_FIRST_NAME);
		String middle = iwc.getParameter(SEARCH_MIDDLE_NAME);
		String last = iwc.getParameter(SEARCH_LAST_NAME);
		String pid = iwc.getParameter(SEARCH_PERSONAL_ID);
		if(firstLetterCaseInsensitive){
			if(first!=null)
				first  = TextSoap.capitalize(first);
			if(middle!=null)
				middle = TextSoap.capitalize(middle);
			if(last!=null)
				last = TextSoap.capitalize(last);
		}
		usersFound = home.findUsersByConditions(first, middle, last, pid, null, null, -1, -1, -1, -1, null, null, true);
		System.out.println("users found " + usersFound.size());
		if (user == null && usersFound != null ) {
			// if some users found
			if(!usersFound.isEmpty()){
				hasManyUsers = usersFound.size() > 1;
				if(!hasManyUsers)
					user = (User)usersFound.iterator().next();
			}
			// if no user found
			else{
				throw new FinderException("No user was found");
			}
		}
	}

	/**
		 * Presentates the users personal info
		 * @param iwc the current context
		 */
	private Table presentateCurrentUserSearch(IWContext iwc) {
		Table searchTable = new Table();
		int row = 1;
		int col = 1;
		if (showPersonalIDInSearch) {
			Text tPersonalID = new Text(iwrb.getLocalizedString(SEARCH_PERSONAL_ID, "Personal ID"));
			setStyle(tPersonalID,STYLENAME_HEADER);
			searchTable.add(tPersonalID, col, row);
			TextInput input = new TextInput(SEARCH_PERSONAL_ID);
			input.setLength(personalIDLength);
			if (user != null && user.getPersonalID() != null) {
				input.setContent(user.getPersonalID());
			}
			if(stacked)
				searchTable.add(input, col++, row+1);
			else
				searchTable.add(input, ++col, row);
		}
		if (showLastNameInSearch) {
			Text tLastName = new Text(iwrb.getLocalizedString(SEARCH_LAST_NAME, "Last name"));
			setStyle(tLastName,STYLENAME_HEADER);
			searchTable.add(tLastName, col, row);
			TextInput input = new TextInput(SEARCH_LAST_NAME);
			input.setLength(lastNameLength);
			if (user != null && user.getLastName() != null) {
				input.setContent(user.getLastName());
			}
			if(stacked)
				searchTable.add(input, col++, row+1);
			else
				searchTable.add(input, ++col, row);
		}
		if (showMiddleNameInSearch) {
			Text tMiddleName = new Text(iwrb.getLocalizedString(SEARCH_MIDDLE_NAME, "Middle name"));
			setStyle(tMiddleName,STYLENAME_HEADER);
			searchTable.add(tMiddleName, col, row);
			TextInput input = new TextInput(SEARCH_MIDDLE_NAME);
			input.setLength(middleNameLength);
			if (user != null && user.getMiddleName() != null) {
				input.setContent(user.getMiddleName());
			}
			if(stacked)
				searchTable.add(input, col++, row+1);
			else
				searchTable.add(input, ++col, row);
		}
		if (showFirstNameInSearch) {
			Text tFirstName = new Text(iwrb.getLocalizedString(SEARCH_FIRST_NAME, "First name"));
			setStyle(tFirstName,STYLENAME_HEADER);
			searchTable.add(tFirstName, col, row);
			TextInput input = new TextInput(SEARCH_FIRST_NAME);
			input.setLength(firstNameLength);
			if (user != null) {
				input.setContent(user.getFirstName());
			}
			if(stacked)
				searchTable.add(input, col++, row+1);
			else
				searchTable.add(input, ++col, row);
		}
		SubmitButton search =
			new SubmitButton(iwrb.getLocalizedImageButton(SEARCH_COMMITTED, "Search"), SEARCH_COMMITTED, "true");
		if(stacked)
			searchTable.add(search, col, row+1);
		else
			searchTable.add(search,1,row+1);

		return searchTable;
	}

	/**
		 * Presentates the users found by search
		 * @param iwc the context
		*/
	private Table presentateFoundUsers(IWContext iwc) {
		Iterator iter = usersFound.iterator();
		Table T = new Table();
		T.setCellspacing(4);
		Link userLink;
		int row = 1;
		int col = 1;
		int colAdd = 1;
		while (iter.hasNext()) {
			User u = (User) iter.next();
			T.add(u.getPersonalID(), colAdd, row);
			userLink = new Link(u.getName());
			userLink.addParameter(PRM_USER_ID, u.getPrimaryKey().toString());
			addParameters(userLink);
			T.add(userLink, colAdd + 1, row);
			row++;
			if (row == maxFoundUserRows) {
				col++;
				colAdd += 2;
				row = 1;
			}
			if (col == maxFoundUserCols) {
				break;
			}
		}
		return T;
	}
	
	private void addParameters(Link link){
		for (Iterator iter = maintainedParameters.iterator(); iter.hasNext();) {
			Parameter element = (Parameter) iter.next();
			link.addParameter(element);
		}
	}

	/**
	 * Flags the first name field in the user search
	 * @param b
	 */
	public void setShowFirstNameInSearch(boolean b) {
		showFirstNameInSearch = b;
	}

	/**
	 * Flags the last name in the user search
	 * @param b
	 */
	public void setShowLastNameInSearch(boolean b) {
		showLastNameInSearch = b;
	}

	/**
	 * Flags the middle name in the user search
	 * @param b
	 */
	public void setShowMiddleNameInSearch(boolean b) {
		showMiddleNameInSearch = b;
	}

	/**
	 * Flags the personal id in the user search
	 * @param b
	 */
	public void setShowPersonalIDInSearch(boolean b) {
		showPersonalIDInSearch = b;
	}

	/**
	 * @return
	 */
	public boolean isHasManyUsers() {
		return hasManyUsers;
	}

	/**
	 * @return
	 */
	public int getMaxFoundUserCols() {
		return maxFoundUserCols;
	}

	/**
	 * @return
	 */
	public int getMaxFoundUserRows() {
		return maxFoundUserRows;
	}

	/**
	 * @return
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return
	 */
	public Collection getUsersFound() {
		return usersFound;
	}

	/**
	 * @param i
	 */
	public void setMaxFoundUserCols(int i) {
		maxFoundUserCols = i;
	}

	/**
	 * @param i
	 */
	public void setMaxFoundUserRows(int i) {
		maxFoundUserRows = i;
	}

	/**
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @param collection
	 */
	public void setUsersFound(Collection collection) {
		usersFound = collection;
	}
	
	public void maintainParameter(Parameter parameter){
		maintainedParameters.add(parameter);
	}

	/* (non-Javadoc)
		 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
		 */
	public String getBundleIdentifier() {
		if (bundleIdentifer != null)
			return bundleIdentifer;
		return BUNDLE_IDENTIFIER;
	}

	/**
	 * Sets the dynamic bundle identifier
	 * @param string
	 */
	public void setBundleIdentifer(String string) {
		bundleIdentifer = string;
	}
	
	


	/* (non-Javadoc)
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public Map getStyleNames() {
		HashMap map = new HashMap();
		map.put(STYLENAME_HEADER,headerFontStyle);
		map.put(STYLENAME_TEXT,textFontStyle);
		return map;
	}

	/**
	 * @return
	 */
	public int getFirstNameLength() {
		return firstNameLength;
	}

	/**
	 * @return
	 */
	public String getHeaderFontStyle() {
		return headerFontStyle;
	}

	/**
	 * @return
	 */
	public int getLastNameLength() {
		return lastNameLength;
	}

	/**
	 * @return
	 */
	public int getMiddleNameLength() {
		return middleNameLength;
	}

	/**
	 * @return
	 */
	public int getPersonalIDLength() {
		return personalIDLength;
	}

	/**
	 * @return
	 */
	public boolean isShowFirstNameInSearch() {
		return showFirstNameInSearch;
	}

	/**
	 * @return
	 */
	public boolean isShowLastNameInSearch() {
		return showLastNameInSearch;
	}

	/**
	 * @return
	 */
	public boolean isShowMiddleNameInSearch() {
		return showMiddleNameInSearch;
	}

	/**
	 * @return
	 */
	public boolean isShowPersonalIDInSearch() {
		return showPersonalIDInSearch;
	}

	/**
	 * @return
	 */
	public boolean isStacked() {
		return stacked;
	}

	/**
	 * @return
	 */
	public String getTextFontStyle() {
		return textFontStyle;
	}

	/**
	 * @param i
	 */
	public void setFirstNameLength(int i) {
		firstNameLength = i;
	}

	/**
	 * @param string
	 */
	public void setHeaderFontStyle(String string) {
		headerFontStyle = string;
	}

	/**
	 * @param i
	 */
	public void setLastNameLength(int i) {
		lastNameLength = i;
	}

	/**
	 * @param i
	 */
	public void setMiddleNameLength(int i) {
		middleNameLength = i;
	}

	/**
	 * @param i
	 */
	public void setPersonalIDLength(int i) {
		personalIDLength = i;
	}

	/**
	 * @param b
	 */
	public void setStacked(boolean b) {
		stacked = b;
	}

	/**
	 * @param string
	 */
	public void setTextFontStyle(String string) {
		textFontStyle = string;
	}

}