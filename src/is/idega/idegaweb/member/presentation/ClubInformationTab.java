/*
 * Created on Mar 11, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.presentation;
import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.business.plugins.ClubInformationPluginBusiness;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.presentation.UserGroupTab;
import com.idega.util.IWTimestamp;
/**
 * @author palli
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
public class ClubInformationTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	private static final String TAB_NAME = "cit_tab_name";
	private static final String DEFAULT_TAB_NAME = "Club Information";
	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String HELP_TEXT_KEY = "club_information_tab";
	private TextInput _numberField;
	private TextInput _ssnField;
	private DateInput _foundedField;
	//	private DropdownMenu _typeField;
	private CheckBox _memberUMFIField;
	private DropdownMenu _makeField;
	private DropdownMenu _connectionToSpecialField;
	private Text _regionalUnionField;
	private DropdownMenu _statusField;
	private CheckBox _inOperationField;
	private CheckBox _usingMemberSystemField;
	private Text _numberText;
	private Text _ssnText;
	private Text _foundedText;
	//	private Text _typeText;
	private Text _memberUMFIText;
	private Text _makeText;
	private Text _connectionToSpecialText;
	private Text _regionalUnionText;
	private Text _statusText;
	private Text _inOperationText;
	private Text _usingMemberSystemText;
	private String _numberFieldName;
	private String _ssnFieldName;
	private String _foundedFieldName;
	private String _typeFieldName;
	private String _memberUMFIFieldName;
	private String _makeFieldName;
	private String _connectionToSpecialFieldName;
	private String _regionalUnionFieldName;
	private String _statusFieldName;
	private String _inOperationFieldName;
	private String _usingMemberSystemFieldName;
	private IWResourceBundle iwrb;
	public ClubInformationTab() {
		super();
	}
	public ClubInformationTab(Group group) {
		this();
		setGroupId(((Integer) group.getPrimaryKey()).intValue());
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldNames()
	 */
	public void initializeFieldNames() {
		_numberFieldName = "cit_number";
		_ssnFieldName = "cit_ssn";
		_foundedFieldName = "cit_founded";
		_typeFieldName = "cit_type";
		_memberUMFIFieldName = "cit_memberOfUMFI";
		_makeFieldName = "cit_make";
		_connectionToSpecialFieldName = "cit_special";
		_regionalUnionFieldName = "cit_regional";
		_statusFieldName = "cit_status";
		_inOperationFieldName = "cit_operation";
		_usingMemberSystemFieldName = "cit_usingSystem";
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
		fieldValues.put(_numberFieldName, "");
		fieldValues.put(_ssnFieldName, "");
		fieldValues.put(_foundedFieldName, new IWTimestamp().getDate().toString());
		fieldValues.put(_typeFieldName, "");
		fieldValues.put(_memberUMFIFieldName, new Boolean(false));
		fieldValues.put(_makeFieldName, "");
		fieldValues.put(_connectionToSpecialFieldName, "");
		fieldValues.put(_regionalUnionFieldName, "");
		fieldValues.put(_statusFieldName, "");
		fieldValues.put(_inOperationFieldName, new Boolean(false));
		fieldValues.put(_usingMemberSystemFieldName, new Boolean(false));
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		lineUpFields();
		_numberField.setContent((String) fieldValues.get(_numberFieldName));
		_ssnField.setContent((String) fieldValues.get(_ssnFieldName));
		_foundedField.setContent((String) fieldValues.get(_foundedFieldName));
		//		_typeField.setSelectedElement((String)
		// fieldValues.get(_typeFieldName));
		_memberUMFIField.setChecked(((Boolean) fieldValues.get(_memberUMFIFieldName)).booleanValue());
		String make = (String) fieldValues.get(_makeFieldName);
		_connectionToSpecialField.setDisabled(false);
		String connection = (String) fieldValues.get(_connectionToSpecialFieldName);
		_connectionToSpecialField.setSelectedElement(connection);
		_makeField.removeElements();
		if (connection != null && !connection.equals("") && make == IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB) {
			_connectionToSpecialField.setDisabled(true);
			_makeField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB, iwrb.getLocalizedString(
					"clubinformationtab.single_division_club", "Single division"));
		} else {
//			_makeField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB, iwrb.getLocalizedString(
//					"clubinformationtab.empty", "Empty"));
			_makeField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB, iwrb.getLocalizedString(
					"clubinformationtab.multi_division_club", "Multi divisional"));
			_makeField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB, iwrb.getLocalizedString(
					"clubinformationtab.single_division_club", "Single division"));
			_makeField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATUS_NO_MEMBERS_CLUB, iwrb.getLocalizedString(
					"clubinformationtab.club_with_no_players", "No players"));
			_makeField.setToEnableWhenSelected(_connectionToSpecialFieldName, IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB);
		}
		_makeField.setSelectedElement(make);
		_regionalUnionField.setText((String) fieldValues.get(_regionalUnionFieldName));
		_statusField.setSelectedElement((String) fieldValues.get(_statusFieldName));
		_inOperationField.setChecked(((Boolean) fieldValues.get(_inOperationFieldName)).booleanValue());
		_usingMemberSystemField.setChecked(((Boolean) fieldValues.get(_usingMemberSystemFieldName)).booleanValue());
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
		IWContext iwc = IWContext.getInstance();
		iwrb = getResourceBundle(iwc);
		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
		_numberField = new TextInput(_numberFieldName);
		_ssnField = new TextInput(_ssnFieldName);
		_foundedField = new DateInput(_foundedFieldName);
		_foundedField.setYearRange(1900, GregorianCalendar.getInstance().get(GregorianCalendar.YEAR));
		//_typeField = new DropdownMenu(_typeFieldName);
		_memberUMFIField = new CheckBox(_memberUMFIFieldName);
		_makeField = new DropdownMenu(_makeFieldName);
		_connectionToSpecialField = new DropdownMenu(_connectionToSpecialFieldName);
		_regionalUnionField = new Text();
		_statusField = new DropdownMenu(_statusFieldName);
		_inOperationField = new CheckBox(_inOperationFieldName);
		_usingMemberSystemField = new CheckBox(_usingMemberSystemFieldName);
		_statusField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATE_ACTIVE, iwrb.getLocalizedString("clubinformationtab.state_active",
				"Active"));
		_statusField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATE_INACTIVE, iwrb.getLocalizedString("clubinformationtab.state_inactive",
				"Inactive"));
		_statusField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATE_COMPETITION_BAN, iwrb.getLocalizedString(
				"clubinformationtab.state_banned_from_comp", "Competition ban"));
		_statusField.setSelectedElement(IWMemberConstants.META_DATA_CLUB_STATE_ACTIVE);
		Collection special = null;
		try {
			special = ((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findGroupsByType(IWMemberConstants.GROUP_TYPE_LEAGUE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (special != null) {
			Iterator it = special.iterator();
			while (it.hasNext()) {
				Group spec = (Group) it.next();
				_connectionToSpecialField.addMenuElement(((Integer) spec.getPrimaryKey()).intValue(), spec.getName());
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		_numberText = new Text(iwrb.getLocalizedString(_numberFieldName, "Number") + ":");
		_ssnText = new Text(iwrb.getLocalizedString(_ssnFieldName, "SSN") + ":");
		_foundedText = new Text(iwrb.getLocalizedString(_foundedFieldName, "Founded") + ":");
		_memberUMFIText = new Text(iwrb.getLocalizedString(_memberUMFIFieldName, "UMFI membership") + ":");
		_makeText = new Text(iwrb.getLocalizedString(_makeFieldName, "Make") + ":");
		_connectionToSpecialText = new Text(iwrb.getLocalizedString(_connectionToSpecialFieldName, "Connection to special") + ":");
		_regionalUnionText = new Text(iwrb.getLocalizedString(_regionalUnionFieldName, "Regional union") + ":");
		_statusText = new Text(iwrb.getLocalizedString(_statusFieldName, "Status") + ":");
		_inOperationText = new Text(iwrb.getLocalizedString(_inOperationFieldName, "In operation") + ":");
		_usingMemberSystemText = new Text(iwrb.getLocalizedString(_usingMemberSystemFieldName, "In member system") + ":");
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		empty();
		Table t = new Table(2, 11);
		t.add(_numberText, 1, 1);
		t.add(_numberField, 2, 1);
		t.add(_ssnText, 1, 2);
		t.add(_ssnField, 2, 2);
		t.add(_foundedText, 1, 3);
		t.add(_foundedField, 2, 3);
		t.add(_memberUMFIText, 1, 4);
		t.add(_memberUMFIField, 2, 4);
		t.add(_makeText, 1, 5);
		t.add(_makeField, 2, 5);
		t.add(_connectionToSpecialText, 1, 6);
		t.add(_connectionToSpecialField, 2, 6);
		t.add(_regionalUnionText, 1, 7);
		t.add(_regionalUnionField, 2, 7);
		t.add(_statusText, 1, 8);
		t.add(_statusField, 2, 8);
		t.add(_inOperationText, 1, 9);
		t.add(_inOperationField, 2, 9);
		t.add(_usingMemberSystemText, 1, 10);
		t.add(_usingMemberSystemField, 2, 10);
		Help help = getHelpButton();
		t.add(help, 1, 11);
		add(t);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String number = iwc.getParameter(_numberFieldName);
			String ssn = iwc.getParameter(_ssnFieldName);
			String founded = iwc.getParameter(_foundedFieldName);
			String type = iwc.getParameter(_typeFieldName);
			String member = iwc.getParameter(_memberUMFIFieldName);
			String make = iwc.getParameter(_makeFieldName);
			String connection = iwc.getParameter(_connectionToSpecialFieldName);
			String status = iwc.getParameter(_statusFieldName);
			String inOperation = iwc.getParameter(_inOperationFieldName);
			String using = iwc.getParameter(_usingMemberSystemFieldName);
			if (number != null)
				fieldValues.put(_numberFieldName, number);
			else
				fieldValues.put(_numberFieldName, "");
			if (ssn != null)
				fieldValues.put(_ssnFieldName, ssn);
			else
				fieldValues.put(_ssnFieldName, "");
			if (founded != null)
				fieldValues.put(_foundedFieldName, founded);
			else
				fieldValues.put(_foundedFieldName, "");
			if (type != null)
				fieldValues.put(_typeFieldName, type);
			else
				fieldValues.put(_typeFieldName, "");
			fieldValues.put(_memberUMFIFieldName, new Boolean(member != null));
			if (make != null)
				fieldValues.put(_makeFieldName, make);
			else
				fieldValues.put(_makeFieldName, "");
			if (connection != null)
				fieldValues.put(_connectionToSpecialFieldName, connection);
			else
				fieldValues.put(_connectionToSpecialFieldName, "");
			if (status != null)
				fieldValues.put(_statusFieldName, status);
			else
				fieldValues.put(_statusFieldName, "");
			fieldValues.put(_inOperationFieldName, new Boolean(inOperation != null));
			fieldValues.put(_usingMemberSystemFieldName, new Boolean(using != null));
			updateFieldsDisplayStatus();
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
		Group group;
		try {
			group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));
			String number = (String) fieldValues.get(_numberFieldName);
			String ssn = (String) fieldValues.get(_ssnFieldName);
			String founded = (String) fieldValues.get(_foundedFieldName);
			String type = (String) fieldValues.get(_typeFieldName);
			Boolean memberUMFI = (Boolean) fieldValues.get(_memberUMFIFieldName);
			String make = (String) fieldValues.get(_makeFieldName);
			String connection = (String) fieldValues.get(_connectionToSpecialFieldName);
			String status = (String) fieldValues.get(_statusFieldName);
			Boolean inOperation = (Boolean) fieldValues.get(_inOperationFieldName);
			Boolean usingSystem = (Boolean) fieldValues.get(_usingMemberSystemFieldName);
			
			
			group.setMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER, number);
			group.setMetaData(IWMemberConstants.META_DATA_CLUB_SSN, ssn);
			group.setMetaData(IWMemberConstants.META_DATA_CLUB_FOUNDED, founded);
			group.setMetaData(IWMemberConstants.META_DATA_CLUB_TYPE, type);
			if (memberUMFI != null) {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_IN_UMFI, memberUMFI.toString());
			}
			String oldMake = group.getMetaData(IWMemberConstants.META_DATA_CLUB_MAKE);
			group.setMetaData(IWMemberConstants.META_DATA_CLUB_MAKE, make);
			if (make.equals(IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB)) {
				String oldConnection = group.getMetaData(IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION);
				if (oldConnection == null && connection != null && (oldMake == null || oldMake != IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB)) {
					group.setMetaData(IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION, connection);
					group.store();
					getClubInformationPluginBusiness(iwc).createSpecialConnection(connection, getGroupId(), group.getName(), iwc);
				}
			} else {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION, "");
			}
			group.setMetaData(IWMemberConstants.META_DATA_CLUB_STATUS, status);
			if (inOperation != null) {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_OPERATION, inOperation.toString());
			} else {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_OPERATION, Boolean.FALSE.toString());
			}
			if (usingSystem != null) {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_USING_SYSTEM, usingSystem.toString());
			} else {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_USING_SYSTEM, Boolean.FALSE.toString());
			}
			
			//and store everything
			group.store();
		} catch (RemoteException e) {
			e.printStackTrace(System.err);
			return false;
		} catch (FinderException e) {
			e.printStackTrace(System.err);
			return false;
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#initFieldContents()
	 */
	public void initFieldContents() {
		Group group;
		try {
			group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));
			List parents = group.getParentGroups();
			Iterator it = parents.iterator();
			String regional = null;
			if (it != null) {
				while (it.hasNext()) {
					Group parent = (Group) it.next();
					if (parent.getGroupType().equals(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION)) {
						regional = parent.getName();
					}
				}
			}
			String number = group.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER);
			String ssn = group.getMetaData(IWMemberConstants.META_DATA_CLUB_SSN);
			String founded = group.getMetaData(IWMemberConstants.META_DATA_CLUB_FOUNDED);
			String type = group.getMetaData(IWMemberConstants.META_DATA_CLUB_TYPE);
			String member = group.getMetaData(IWMemberConstants.META_DATA_CLUB_IN_UMFI);
			String make = group.getMetaData(IWMemberConstants.META_DATA_CLUB_MAKE);
			String connection = group.getMetaData(IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION);
			String status = group.getMetaData(IWMemberConstants.META_DATA_CLUB_STATUS);
			String inOperation = group.getMetaData(IWMemberConstants.META_DATA_CLUB_OPERATION);
			String using = group.getMetaData(IWMemberConstants.META_DATA_CLUB_USING_SYSTEM);
			if (number != null) {
				fieldValues.put(_numberFieldName, number);
			}
			if (ssn != null) {
				fieldValues.put(_ssnFieldName, ssn);
			}
			if (founded != null) {
				fieldValues.put(_foundedFieldName, founded);
			}
			if (type != null) {
				fieldValues.put(_typeFieldName, type);
			}
			fieldValues.put(_memberUMFIFieldName, new Boolean(member));
			if (make != null) {
				fieldValues.put(_makeFieldName, make);
			}
			if (connection != null) {
				fieldValues.put(_connectionToSpecialFieldName, connection);
			}
			if (regional != null) {
				fieldValues.put(_regionalUnionFieldName, regional);
			}
			if (status != null) {
				fieldValues.put(_statusFieldName, status);
			}
			fieldValues.put(_inOperationFieldName, new Boolean(inOperation));
			fieldValues.put(_usingMemberSystemFieldName, new Boolean(using));
			updateFieldsDisplayStatus();
		} catch (RemoteException e) {
			e.printStackTrace(System.err);
		} catch (FinderException e) {
			e.printStackTrace(System.err);
		}
	}
	public ClubInformationPluginBusiness getClubInformationPluginBusiness(IWApplicationContext iwc) {
		ClubInformationPluginBusiness business = null;
		try {
			business = (ClubInformationPluginBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ClubInformationPluginBusiness.class);
		} catch (java.rmi.RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		return business;
	}
	public MemberUserBusiness getMemberUserBusiness(IWApplicationContext iwc) {
		MemberUserBusiness business = null;
		try {
			business = (MemberUserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, MemberUserBusiness.class);
		} catch (java.rmi.RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		return business;
	}
	public Help getHelpButton() {
		IWContext iwc = IWContext.getInstance();
		IWBundle iwb = getBundle(iwc);
		Help help = new Help();
		Image helpImage = iwb.getImage("help.gif");
		help.setHelpTextBundle(MEMBER_HELP_BUNDLE_IDENTIFIER);
		help.setHelpTextKey(HELP_TEXT_KEY);
		help.setImage(helpImage);
		return help;
	}
}