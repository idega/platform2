 /*
 * Created on Mar 11, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.plugins.ClubInformationPluginBusiness;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.FinderException;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.user.presentation.UserChooserBrowser;
import com.idega.user.presentation.UserGroupTab;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ClubDivisionHandlerTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	
	private static final String TAB_NAME = "cdivh_tab_name";
	private static final String DEFAULT_TAB_NAME = "Club Division Handler";
	
	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String HELP_TEXT_KEY = "club_division_handler_tab";
	
	
	private TextInput _numberField;
	private TextInput _nameField;
	private TextInput _divField;
	private UserChooserBrowser _contactField;

	private Text _numberText;
	private Text _nameText;
	private Text _divText;
	private Text _contactText;

	private String _numberFieldName;
	private String _nameFieldName;
	private String _divFieldName;
	private String _contactFieldName;

	public ClubDivisionHandlerTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	public ClubDivisionHandlerTab(Group group) {
		this();
		setGroupId(((Integer) group.getPrimaryKey()).intValue());
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldNames()
	 */
	public void initializeFieldNames() {
		_numberFieldName = "cdivh_number";
		_nameFieldName = "cdivh_name";
		_divFieldName = "cdivh_div";
		_contactFieldName = "cdivh_contact";
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
		fieldValues.put(_numberFieldName, "");
		fieldValues.put(_nameFieldName, "");
		fieldValues.put(_divFieldName, "");
		fieldValues.put(_contactFieldName, "");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		_numberField.setContent((String) fieldValues.get(_numberFieldName));
		_nameField.setContent((String) fieldValues.get(_nameFieldName));
		_divField.setContent((String) fieldValues.get(_divFieldName));
		
		try {
			UserHome home = (UserHome) com.idega.data.IDOLookup.getHome(User.class);
			String userId = (String) fieldValues.get(_contactFieldName);

			if (userId != null && !userId.equals("")) {
				User user = (User) (home.findByPrimaryKey(new Integer(userId)));
				_contactField.setSelectedUser(userId,user.getName());
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
		_numberField = new TextInput(_numberFieldName);
		_nameField = new TextInput(_nameFieldName);
//		_ssnField.setAsIcelandicSSNumber("Vart�lupr�fun stemmir ekki");
		_divField = new TextInput(_divFieldName);
		_contactField = new UserChooserBrowser(_contactFieldName);
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
			IWContext iwc = //getEventIWContext();
	IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		_numberText = new Text(iwrb.getLocalizedString(_numberFieldName, "Short name"));
		_numberText.setBold();
		
		_nameText = new Text(iwrb.getLocalizedString(_nameFieldName, "Description"));
		_nameText.setBold();
		
		_divText = new Text(iwrb.getLocalizedString(_divFieldName, "Division"));
		_divText.setBold();
		
		_contactText = new Text(iwrb.getLocalizedString(_contactFieldName, "Contact"));
		_contactText.setBold();
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		Table t = new Table(2, 2);
		t.setWidth(Table.HUNDRED_PERCENT);
		t.setCellpadding(5);
		t.setCellspacing(0);
		
		t.add(_numberText, 1, 1);
		t.add(Text.getBreak(), 1, 1);
		t.add(_numberField, 1, 1);
		
		t.add(_nameText, 2, 1);
		t.add(Text.getBreak(), 2, 1);
		t.add(_nameField, 2, 1);
		
		t.add(_divText, 1, 2);
		t.add(Text.getBreak(), 1, 2);
		t.add(_divField, 1, 2);
		
		t.add(_contactText, 2, 2);
		t.add(Text.getBreak(), 2, 2);
		t.add(_contactField, 2, 2);

		add(t);
	}

	public void main(IWContext iwc) {
		getPanel().addHelpButton(getHelpButton());		
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String number = iwc.getParameter(_numberFieldName);
			String name = iwc.getParameter(_nameFieldName);
			String div = iwc.getParameter(_divFieldName);
			String contact = iwc.getParameter(_contactFieldName);

			if (number != null)
				fieldValues.put(_numberFieldName, number);
			else
				fieldValues.put(_numberFieldName, "");
			if (name != null)
				fieldValues.put(_nameFieldName, name);
			else
				fieldValues.put(_nameFieldName, "");
			if (div != null)
				fieldValues.put(_divFieldName, div);
			else
				fieldValues.put(_divFieldName, "");
			if (contact != null)
				fieldValues.put(_contactFieldName, contact);
			else
				fieldValues.put(_contactFieldName, "");

			updateFieldsDisplayStatus();
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
		Group group;
		try {
			group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));
			// get corressponding service bean
			ClubInformationPluginBusiness ageGenderPluginBusiness = getClubInformationPluginBusiness(iwc);

			String number = (String) fieldValues.get(_numberFieldName);
			String name = (String) fieldValues.get(_nameFieldName);
			String div = (String) fieldValues.get(_divFieldName);
			String contact = (String) fieldValues.get(_contactFieldName);

			group.setMetaData("CLUBDIVH_NUMBER", number);
			group.setMetaData("CLUBDIVH_NAME", name);
			group.setMetaData("CLUBDIVH_DIV", div);
			group.setMetaData("CLUBDIVH_CONTACT", contact);

			group.store();
		}
		catch (RemoteException e) {
			e.printStackTrace(System.err);
			return false;
		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initFieldContents()
	 */
	public void initFieldContents() {
		Group group;
		try {
			group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));

			String number = group.getMetaData("CLUBDIVH_NUMBER");
			String name = group.getMetaData("CLUBDIVH_NAME");
			String div = group.getMetaData("CLUBDIVH_DIV");
			String contact = group.getMetaData("CLUBDIVH_CONTACT");

			if (number != null)
				fieldValues.put(_numberFieldName, number);
			if (name != null)
				fieldValues.put(_nameFieldName, name);
			if (div != null)
				fieldValues.put(_divFieldName, div);
			if (contact != null)
				fieldValues.put(_contactFieldName, contact);
			
			updateFieldsDisplayStatus();
		}
		catch (RemoteException e) {
			e.printStackTrace(System.err);
		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
		}
	}

	public ClubInformationPluginBusiness getClubInformationPluginBusiness(IWApplicationContext iwc) {
		ClubInformationPluginBusiness business = null;
		if (business == null) {
			try {
				business = (ClubInformationPluginBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ClubInformationPluginBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return business;
	}
	public Help getHelpButton() {
		IWContext iwc = IWContext.getInstance();
		IWBundle iwb = getBundle(iwc);
		Help help = new Help();
		Image helpImage = iwb.getImage("help.gif");
		help.setHelpTextBundle( MEMBER_HELP_BUNDLE_IDENTIFIER);
		help.setHelpTextKey(HELP_TEXT_KEY);
		help.setImage(helpImage);
		return help;
		
	}
}