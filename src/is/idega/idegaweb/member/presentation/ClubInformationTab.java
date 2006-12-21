/*
 * Created on Mar 11, 2003
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.business.plugins.ClubInformationPluginBusiness;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.IDOLookupException;
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

//	private DropdownMenu _connectionToSpecialField;

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

//	private Text _connectionToSpecialText;

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

//	private String _connectionToSpecialFieldName;

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
		this._numberFieldName = "cit_number";
		this._ssnFieldName = "cit_ssn";
		this._foundedFieldName = "cit_founded";
		this._typeFieldName = "cit_type";
		this._memberUMFIFieldName = "cit_memberOfUMFI";
		this._makeFieldName = "cit_make";
//		_connectionToSpecialFieldName = "cit_special";
		this._regionalUnionFieldName = "cit_regional";
		this._statusFieldName = "cit_status";
		this._inOperationFieldName = "cit_operation";
		this._usingMemberSystemFieldName = "cit_usingSystem";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		this.fieldValues = new Hashtable();
		this.fieldValues.put(this._numberFieldName, "");
		this.fieldValues.put(this._ssnFieldName, "");
		this.fieldValues.put(this._foundedFieldName, new IWTimestamp().getDate().toString());
		this.fieldValues.put(this._typeFieldName, "");
		this.fieldValues.put(this._memberUMFIFieldName, new Boolean(false));
		this.fieldValues.put(this._makeFieldName, "");
//		fieldValues.put(_connectionToSpecialFieldName, "");
		this.fieldValues.put(this._regionalUnionFieldName, "");
		this.fieldValues.put(this._statusFieldName, "");
		this.fieldValues.put(this._inOperationFieldName, new Boolean(false));
		this.fieldValues.put(this._usingMemberSystemFieldName, new Boolean(false));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		lineUpFields();
		String number = (String) this.fieldValues.get(this._numberFieldName);
		this._numberField.setContent(number);
		
		this._ssnField.setContent((String) this.fieldValues.get(this._ssnFieldName));
		this._foundedField.setContent((String) this.fieldValues.get(this._foundedFieldName));
		//		_typeField.setSelectedElement((String)
		// fieldValues.get(_typeFieldName));
		this._memberUMFIField.setChecked(((Boolean) this.fieldValues.get(this._memberUMFIFieldName)).booleanValue());
		String make = (String) this.fieldValues.get(this._makeFieldName);
//		String connection = (String) fieldValues.get(_connectionToSpecialFieldName);
//		_connectionToSpecialField.setSelectedElement(connection);
		this._makeField.removeElements();
/*		if (connection != null && !connection.equals("")
				&& make.equals(IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB)) {
			_connectionToSpecialField.setDisabled(false);
			_connectionToSpecialField.setOnChange("alert('"
					+ iwrb.getLocalizedString("clubinformationtab.cannot_change_msg", "You can not change this field!")
					+ "')");
			_connectionToSpecialField.setToSubmit();
			_makeField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB,
					iwrb.getLocalizedString("clubinformationtab.single_division_club", "Single division"));
		}
		else {*/
/*			if (make.equals(IWMemberConstants.META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB)
					|| make.equals(IWMemberConstants.META_DATA_CLUB_STATUS_NO_MEMBERS_CLUB)) {
				_connectionToSpecialField.setDisabled(true);
			}
			else {
				_connectionToSpecialField.setDisabled(false);
			}*/
			this._makeField.addMenuElement("-1", this.iwrb.getLocalizedString("clubinformationtab.choose_make", "Choose type..."));
			//		_makeField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB,
			// iwrb.getLocalizedString(
			//				"clubinformationtab.empty", "Empty"));
			this._makeField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB,
					this.iwrb.getLocalizedString("clubinformationtab.multi_division_club", "Multi divisional"));
/*			_makeField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB,
					iwrb.getLocalizedString("clubinformationtab.single_division_club", "Single division"));*/
			this._makeField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATUS_NO_MEMBERS_CLUB, this.iwrb.getLocalizedString(
					"clubinformationtab.club_with_no_players", "No players"));
//			_makeField.setToEnableWhenSelected(_connectionToSpecialFieldName,
//					IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB);
//		}
		this._makeField.setSelectedElement(make);
		this._regionalUnionField.setText((String) this.fieldValues.get(this._regionalUnionFieldName));
		this._statusField.setSelectedElement((String) this.fieldValues.get(this._statusFieldName));
		this._inOperationField.setChecked(((Boolean) this.fieldValues.get(this._inOperationFieldName)).booleanValue());
		this._usingMemberSystemField.setChecked(((Boolean) this.fieldValues.get(this._usingMemberSystemFieldName)).booleanValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
		IWContext iwc = IWContext.getInstance();
		this.iwrb = getResourceBundle(iwc);
		setName(this.iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
		this._numberField = new TextInput(this._numberFieldName);
		this._ssnField = new TextInput(this._ssnFieldName);
		this._foundedField = new DateInput(this._foundedFieldName);
		this._foundedField.setYearRange(1900, Calendar.getInstance().get(Calendar.YEAR));
		//_typeField = new DropdownMenu(_typeFieldName);
		this._memberUMFIField = new CheckBox(this._memberUMFIFieldName);
		this._memberUMFIField.setWidth("10");
		this._memberUMFIField.setHeight("10");
		this._makeField = new DropdownMenu(this._makeFieldName);
//		_connectionToSpecialField = new DropdownMenu(_connectionToSpecialFieldName);
		this._regionalUnionField = new Text();
		this._statusField = new DropdownMenu(this._statusFieldName);
		this._inOperationField = new CheckBox(this._inOperationFieldName);
		this._inOperationField.setWidth("10");
		this._inOperationField.setHeight("10");
		this._usingMemberSystemField = new CheckBox(this._usingMemberSystemFieldName);
		this._usingMemberSystemField.setWidth("10");
		this._usingMemberSystemField.setHeight("10");
		this._statusField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATE_ACTIVE, this.iwrb.getLocalizedString(
				"clubinformationtab.state_active", "Active"));
		this._statusField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATE_INACTIVE, this.iwrb.getLocalizedString(
				"clubinformationtab.state_inactive", "Inactive"));
		this._statusField.addMenuElement(IWMemberConstants.META_DATA_CLUB_STATE_COMPETITION_BAN, this.iwrb.getLocalizedString(
				"clubinformationtab.state_banned_from_comp", "Competition ban"));
		this._statusField.setSelectedElement(IWMemberConstants.META_DATA_CLUB_STATE_ACTIVE);
/*		List special = null;
		try {
			special = (List) ((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findGroupsByType(IWMemberConstants.GROUP_TYPE_LEAGUE);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		if (special != null) {
			final Collator collator = Collator.getInstance(iwc.getLocale());
			Collections.sort(special, new Comparator() {

				public int compare(Object arg0, Object arg1) {
					return collator.compare(((Group) arg0).getName(), ((Group) arg1).getName());
				}
			});
			_connectionToSpecialField.addMenuElement("-1", iwrb.getLocalizedString("clubinformationtab.choose_reg_un",
					"Choose a regional union..."));
			Iterator it = special.iterator();
			while (it.hasNext()) {
				Group spec = (Group) it.next();
				_connectionToSpecialField.addMenuElement(((Integer) spec.getPrimaryKey()).intValue(), spec.getName());
			}
		}*/
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this._numberText = new Text(iwrb.getLocalizedString(this._numberFieldName, "Number"));
		this._numberText.setBold();
		this._ssnText = new Text(iwrb.getLocalizedString(this._ssnFieldName, "SSN"));
		this._ssnText.setBold();
		this._foundedText = new Text(iwrb.getLocalizedString(this._foundedFieldName, "Founded"));
		this._foundedText.setBold();
		this._memberUMFIText = new Text(iwrb.getLocalizedString(this._memberUMFIFieldName, "UMFI membership"));
		this._memberUMFIText.setBold();
		this._makeText = new Text(iwrb.getLocalizedString(this._makeFieldName, "Make"));
		this._makeText.setBold();
/*		_connectionToSpecialText = new Text(iwrb.getLocalizedString(_connectionToSpecialFieldName,
				"Connection to special"));
		_connectionToSpecialText.setBold();*/
		this._regionalUnionText = new Text(iwrb.getLocalizedString(this._regionalUnionFieldName, "Regional union"));
		this._regionalUnionText.setBold();
		this._statusText = new Text(iwrb.getLocalizedString(this._statusFieldName, "Status"));
		this._statusText.setBold();
		this._inOperationText = new Text(iwrb.getLocalizedString(this._inOperationFieldName, "In operation"));
		this._inOperationText.setBold();
		this._usingMemberSystemText = new Text(iwrb.getLocalizedString(this._usingMemberSystemFieldName, "In member system"));
		this._usingMemberSystemText.setBold();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		String type = "";
		try {
			if(getGroupId()>0){
				Group group = (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));
				type = group.getGroupType();
			}
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		empty();
		Table t = new Table(2, 7);
		t.setWidth(Table.HUNDRED_PERCENT);
		t.setCellpadding(5);
		t.setCellspacing(0);
		t.add(this._numberText, 1, 1);
		t.add(Text.getBreak(), 1, 1);
		t.add(this._numberField, 1, 1);
		t.add(this._ssnText, 2, 1);
		t.add(Text.getBreak(), 2, 1);
		t.add(this._ssnField, 2, 1);
		t.add(this._foundedText, 1, 2);
		t.add(Text.getBreak(), 1, 2);
		t.add(this._foundedField, 1, 2);
		
		if(IWMemberConstants.GROUP_TYPE_CLUB.equals(type)){
			t.add(this._makeText, 2, 2);
			t.add(Text.getBreak(), 2, 2);
			t.add(this._makeField, 2, 2);
//			t.add(_connectionToSpecialText, 1, 3);
//			t.add(Text.getBreak(), 1, 3);
//			t.add(_connectionToSpecialField, 1, 3);
			t.add(this._regionalUnionText, 2, 3);
			t.add(Text.getBreak(), 2, 3);
			t.add(this._regionalUnionField, 2, 3);
		}
		
		t.add(this._statusText, 1, 4);
		t.add(Text.getBreak(), 1, 4);
		t.add(this._statusField, 1, 4);
		t.mergeCells(1, 5, 2, 5);
		t.add(this._memberUMFIField, 1, 5);
		t.add(this._memberUMFIText, 1, 5);
		t.mergeCells(1, 6, 2, 6);
		t.add(this._inOperationField, 1, 6);
		t.add(this._inOperationText, 1, 6);
		t.mergeCells(1, 7, 2, 7);
		t.add(this._usingMemberSystemField, 1, 7);
		t.add(this._usingMemberSystemText, 1, 7);
		add(t);
	}

	public void main(IWContext iwc) {
		getPanel().addHelpButton(getHelpButton());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String number = iwc.getParameter(this._numberFieldName);
			String ssn = iwc.getParameter(this._ssnFieldName);
			String founded = iwc.getParameter(this._foundedFieldName);
			String type = iwc.getParameter(this._typeFieldName);
			String member = iwc.getParameter(this._memberUMFIFieldName);
			String make = iwc.getParameter(this._makeFieldName);
//			String connection = iwc.getParameter(_connectionToSpecialFieldName);
			String status = iwc.getParameter(this._statusFieldName);
			String inOperation = iwc.getParameter(this._inOperationFieldName);
			String using = iwc.getParameter(this._usingMemberSystemFieldName);
			if (number != null) {
				this.fieldValues.put(this._numberFieldName, number);
			}
			else {
				this.fieldValues.put(this._numberFieldName, "");
			}
			if (ssn != null) {
				this.fieldValues.put(this._ssnFieldName, ssn);
			}
			else {
				this.fieldValues.put(this._ssnFieldName, "");
			}
			if (founded != null) {
				this.fieldValues.put(this._foundedFieldName, founded);
			}
			else {
				this.fieldValues.put(this._foundedFieldName, "");
			}
			if (type != null) {
				this.fieldValues.put(this._typeFieldName, type);
			}
			else {
				this.fieldValues.put(this._typeFieldName, "");
			}
			this.fieldValues.put(this._memberUMFIFieldName, new Boolean(member != null));
			if (make != null) {
				this.fieldValues.put(this._makeFieldName, make);
			}
			else {
				this.fieldValues.put(this._makeFieldName, "");
			}
/*			if (connection != null) {
				fieldValues.put(_connectionToSpecialFieldName, connection);
			}
			else {
				fieldValues.put(_connectionToSpecialFieldName, "");
			}*/
			if (status != null) {
				this.fieldValues.put(this._statusFieldName, status);
			}
			else {
				this.fieldValues.put(this._statusFieldName, "");
			}
			this.fieldValues.put(this._inOperationFieldName, new Boolean(inOperation != null));
			this.fieldValues.put(this._usingMemberSystemFieldName, new Boolean(using != null));
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
			group = (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(
					getGroupId())));
			String groupType = group.getGroupType();
			
			String number = (String) this.fieldValues.get(this._numberFieldName);
			String ssn = (String) this.fieldValues.get(this._ssnFieldName);
			String founded = (String) this.fieldValues.get(this._foundedFieldName);
			
			Boolean memberUMFI = (Boolean) this.fieldValues.get(this._memberUMFIFieldName);
			String status = (String) this.fieldValues.get(this._statusFieldName);
			Boolean inOperation = (Boolean) this.fieldValues.get(this._inOperationFieldName);
			Boolean usingSystem = (Boolean) this.fieldValues.get(this._usingMemberSystemFieldName);
			group.setMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER, number);
			group.setMetaData(IWMemberConstants.META_DATA_CLUB_SSN, ssn);
			group.setMetaData(IWMemberConstants.META_DATA_CLUB_FOUNDED, founded);
			
			if (memberUMFI != null) {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_IN_UMFI, memberUMFI.toString());
			}
			
			if(IWMemberConstants.GROUP_TYPE_CLUB.equals(groupType)){
				String make = (String) this.fieldValues.get(this._makeFieldName);
//				String connection = (String) fieldValues.get(_connectionToSpecialFieldName);
				String type = (String) this.fieldValues.get(this._typeFieldName);
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_TYPE, type);
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_MAKE, make);
/*				if (make.equals(IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB)) {
					String oldConnection = group.getMetaData(IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION);
					if ((oldConnection == null || oldConnection.trim().equals("")) && connection != null) {
						group.setMetaData(IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION, connection);
						group.store();
						getClubInformationPluginBusiness(iwc).createSpecialConnection(connection, getGroupId(),
								group.getName(), iwc);
					}
				}
				else {
					group.setMetaData(IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION, "");
				}*/
				
			}
			
			group.setMetaData(IWMemberConstants.META_DATA_CLUB_STATUS, status);
			if (inOperation != null) {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_OPERATION, inOperation.toString());
			}
			else {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_OPERATION, Boolean.FALSE.toString());
			}
			if (usingSystem != null) {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_USING_SYSTEM, usingSystem.toString());
			}
			else {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_USING_SYSTEM, Boolean.FALSE.toString());
			}
			//and store everything
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#initFieldContents()
	 */
	public void initFieldContents() {
		Group group;
		try {
			group = (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(
					getGroupId())));
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
//			String connection = group.getMetaData(IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION);
			String status = group.getMetaData(IWMemberConstants.META_DATA_CLUB_STATUS);
			String inOperation = group.getMetaData(IWMemberConstants.META_DATA_CLUB_OPERATION);
			String using = group.getMetaData(IWMemberConstants.META_DATA_CLUB_USING_SYSTEM);
			if (number != null) {
				this.fieldValues.put(this._numberFieldName, number);
			}
			if (ssn != null) {
				this.fieldValues.put(this._ssnFieldName, ssn);
			}
			if (founded != null) {
				this.fieldValues.put(this._foundedFieldName, founded);
			}
			if (type != null) {
				this.fieldValues.put(this._typeFieldName, type);
			}
			this.fieldValues.put(this._memberUMFIFieldName, new Boolean(member));
			if (make != null) {
				this.fieldValues.put(this._makeFieldName, make);
			}
/*			if (connection != null) {
				fieldValues.put(_connectionToSpecialFieldName, connection);
			}*/
			if (regional != null) {
				this.fieldValues.put(this._regionalUnionFieldName, regional);
			}
			if (status != null) {
				this.fieldValues.put(this._statusFieldName, status);
			}
			this.fieldValues.put(this._inOperationFieldName, new Boolean(inOperation));
			this.fieldValues.put(this._usingMemberSystemFieldName, new Boolean(using));
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
		try {
			business = (ClubInformationPluginBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc,
					ClubInformationPluginBusiness.class);
		}
		catch (java.rmi.RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		return business;
	}

	public MemberUserBusiness getMemberUserBusiness(IWApplicationContext iwc) {
		MemberUserBusiness business = null;
		try {
			business = (MemberUserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc,
					MemberUserBusiness.class);
		}
		catch (java.rmi.RemoteException rme) {
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