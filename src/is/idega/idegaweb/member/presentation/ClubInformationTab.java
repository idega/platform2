/*
 * Created on Mar 11, 2003
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.business.plugins.ClubInformationPluginBusiness;
import is.idega.idegaweb.member.isi.ISIMemberConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.Calendar;
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

	private TextInput numberField;

	private TextInput ssnField;

	private DateInput foundedField;

	// private DropdownMenu _typeField;
	private CheckBox memberUMFIField;

	private DropdownMenu makeField;

	// private DropdownMenu _connectionToSpecialField;

	private Text regionalUnionField;

	private DropdownMenu statusField;

	private CheckBox inOperationField;

	private CheckBox usingMemberSystemField;

	private CheckBox usingNetbokhaldField;

	private Text numberText;

	private Text ssnText;

	private Text foundedText;

	// private Text _typeText;
	private Text memberUMFIText;

	private Text makeText;

	// private Text _connectionToSpecialText;

	private Text regionalUnionText;

	private Text statusText;

	private Text inOperationText;

	private Text usingMemberSystemText;

	private Text usingNetbokhaldText;

	private String numberFieldName;

	private String ssnFieldName;

	private String foundedFieldName;

	private String typeFieldName;

	private String memberUMFIFieldName;

	private String makeFieldName;

	// private String _connectionToSpecialFieldName;

	private String regionalUnionFieldName;

	private String statusFieldName;

	private String inOperationFieldName;

	private String usingMemberSystemFieldName;

	private String usingNetbokhaldFieldName;

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
		this.numberFieldName = "cit_number";
		this.ssnFieldName = "cit_ssn";
		this.foundedFieldName = "cit_founded";
		this.typeFieldName = "cit_type";
		this.memberUMFIFieldName = "cit_memberOfUMFI";
		this.makeFieldName = "cit_make";
		// _connectionToSpecialFieldName = "cit_special";
		this.regionalUnionFieldName = "cit_regional";
		this.statusFieldName = "cit_status";
		this.inOperationFieldName = "cit_operation";
		this.usingMemberSystemFieldName = "cit_usingSystem";
		this.usingNetbokhaldFieldName = "cit_usingNetbokhald";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		this.fieldValues = new Hashtable();
		this.fieldValues.put(this.numberFieldName, "");
		this.fieldValues.put(this.ssnFieldName, "");
		this.fieldValues.put(this.foundedFieldName, new IWTimestamp().getDate()
				.toString());
		this.fieldValues.put(this.typeFieldName, "");
		this.fieldValues.put(this.memberUMFIFieldName, new Boolean(false));
		this.fieldValues.put(this.makeFieldName, "");
		// fieldValues.put(_connectionToSpecialFieldName, "");
		this.fieldValues.put(this.regionalUnionFieldName, "");
		this.fieldValues.put(this.statusFieldName, "");
		this.fieldValues.put(this.inOperationFieldName, new Boolean(false));
		this.fieldValues.put(this.usingMemberSystemFieldName,
				new Boolean(false));
		this.fieldValues.put(this.usingNetbokhaldFieldName, new Boolean(false));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		lineUpFields();
		String number = (String) this.fieldValues.get(this.numberFieldName);
		this.numberField.setContent(number);

		this.ssnField.setContent((String) this.fieldValues
				.get(this.ssnFieldName));
		this.foundedField.setContent((String) this.fieldValues
				.get(this.foundedFieldName));
		this.memberUMFIField.setChecked(((Boolean) this.fieldValues
				.get(this.memberUMFIFieldName)).booleanValue());
		String make = (String) this.fieldValues.get(this.makeFieldName);
		this.makeField.removeElements();
		this.makeField.addMenuElement("-1", this.iwrb.getLocalizedString(
				"clubinformationtab.choose_make", "Choose type..."));
		this.makeField.addMenuElement(
				IWMemberConstants.META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB,
				this.iwrb.getLocalizedString(
						"clubinformationtab.multi_division_club",
						"Multi divisional"));
		this.makeField.addMenuElement(
				IWMemberConstants.META_DATA_CLUB_STATUS_NO_MEMBERS_CLUB,
				this.iwrb
						.getLocalizedString(
								"clubinformationtab.club_with_no_players",
								"No players"));
		this.makeField.setSelectedElement(make);
		this.regionalUnionField.setText((String) this.fieldValues
				.get(this.regionalUnionFieldName));
		this.statusField.setSelectedElement((String) this.fieldValues
				.get(this.statusFieldName));
		this.inOperationField.setChecked(((Boolean) this.fieldValues
				.get(this.inOperationFieldName)).booleanValue());
		this.usingMemberSystemField.setChecked(((Boolean) this.fieldValues
				.get(this.usingMemberSystemFieldName)).booleanValue());
		this.usingNetbokhaldField.setChecked(((Boolean) this.fieldValues
				.get(this.usingNetbokhaldFieldName)).booleanValue());
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
		this.numberField = new TextInput(this.numberFieldName);
		this.ssnField = new TextInput(this.ssnFieldName);
		this.foundedField = new DateInput(this.foundedFieldName);
		this.foundedField.setYearRange(1900, Calendar.getInstance().get(
				Calendar.YEAR));
		this.memberUMFIField = new CheckBox(this.memberUMFIFieldName);
		this.memberUMFIField.setWidth("10");
		this.memberUMFIField.setHeight("10");
		this.makeField = new DropdownMenu(this.makeFieldName);
		this.regionalUnionField = new Text();
		this.statusField = new DropdownMenu(this.statusFieldName);
		this.inOperationField = new CheckBox(this.inOperationFieldName);
		this.inOperationField.setWidth("10");
		this.inOperationField.setHeight("10");
		this.usingMemberSystemField = new CheckBox(
				this.usingMemberSystemFieldName);
		this.usingMemberSystemField.setWidth("10");
		this.usingMemberSystemField.setHeight("10");
		this.usingNetbokhaldField = new CheckBox(this.usingNetbokhaldFieldName);
		this.usingNetbokhaldField.setWidth("10");
		this.usingNetbokhaldField.setHeight("10");
		this.statusField.addMenuElement(
				IWMemberConstants.META_DATA_CLUB_STATE_ACTIVE, this.iwrb
						.getLocalizedString("clubinformationtab.state_active",
								"Active"));
		this.statusField.addMenuElement(
				IWMemberConstants.META_DATA_CLUB_STATE_INACTIVE,
				this.iwrb.getLocalizedString(
						"clubinformationtab.state_inactive", "Inactive"));
		this.statusField.addMenuElement(
				IWMemberConstants.META_DATA_CLUB_STATE_COMPETITION_BAN,
				this.iwrb.getLocalizedString(
						"clubinformationtab.state_banned_from_comp",
						"Competition ban"));
		this.statusField
				.setSelectedElement(IWMemberConstants.META_DATA_CLUB_STATE_ACTIVE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.numberText = new Text(iwrb.getLocalizedString(
				this.numberFieldName, "Number"));
		this.numberText.setBold();
		this.ssnText = new Text(iwrb.getLocalizedString(this.ssnFieldName,
				"SSN"));
		this.ssnText.setBold();
		this.foundedText = new Text(iwrb.getLocalizedString(
				this.foundedFieldName, "Founded"));
		this.foundedText.setBold();
		this.memberUMFIText = new Text(iwrb.getLocalizedString(
				this.memberUMFIFieldName, "UMFI membership"));
		this.memberUMFIText.setBold();
		this.makeText = new Text(iwrb.getLocalizedString(this.makeFieldName,
				"Make"));
		this.makeText.setBold();
		this.regionalUnionText = new Text(iwrb.getLocalizedString(
				this.regionalUnionFieldName, "Regional union"));
		this.regionalUnionText.setBold();
		this.statusText = new Text(iwrb.getLocalizedString(
				this.statusFieldName, "Status"));
		this.statusText.setBold();
		this.inOperationText = new Text(iwrb.getLocalizedString(
				this.inOperationFieldName, "In operation"));
		this.inOperationText.setBold();
		this.usingMemberSystemText = new Text(iwrb.getLocalizedString(
				this.usingMemberSystemFieldName, "In member system"));
		this.usingMemberSystemText.setBold();
		this.usingNetbokhaldText = new Text(iwrb.getLocalizedString(
				this.usingNetbokhaldFieldName, "Using Netbokhald"));
		this.usingNetbokhaldText.setBold();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		String type = "";
		try {
			if (getGroupId() > 0) {
				Group group = (((GroupHome) com.idega.data.IDOLookup
						.getHome(Group.class)).findByPrimaryKey(new Integer(
						getGroupId())));
				type = group.getGroupType();
			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		empty();
		Table t = new Table(2, 7);
		t.setWidth(Table.HUNDRED_PERCENT);
		t.setCellpadding(5);
		t.setCellspacing(0);
		t.add(this.numberText, 1, 1);
		t.add(Text.getBreak(), 1, 1);
		t.add(this.numberField, 1, 1);
		t.add(this.ssnText, 2, 1);
		t.add(Text.getBreak(), 2, 1);
		t.add(this.ssnField, 2, 1);
		t.add(this.foundedText, 1, 2);
		t.add(Text.getBreak(), 1, 2);
		t.add(this.foundedField, 1, 2);

		if (IWMemberConstants.GROUP_TYPE_CLUB.equals(type)) {
			t.add(this.makeText, 2, 2);
			t.add(Text.getBreak(), 2, 2);
			t.add(this.makeField, 2, 2);
			// t.add(_connectionToSpecialText, 1, 3);
			// t.add(Text.getBreak(), 1, 3);
			// t.add(_connectionToSpecialField, 1, 3);
			t.add(this.regionalUnionText, 2, 3);
			t.add(Text.getBreak(), 2, 3);
			t.add(this.regionalUnionField, 2, 3);
		}

		t.add(this.statusText, 1, 4);
		t.add(Text.getBreak(), 1, 4);
		t.add(this.statusField, 1, 4);
		// t.mergeCells(1, 5, 2, 5);
		t.add(this.memberUMFIField, 1, 5);
		t.add(this.memberUMFIText, 1, 5);
		t.add(this.usingNetbokhaldField, 2, 5);
		t.add(this.usingNetbokhaldText, 2, 5);
		t.mergeCells(1, 6, 2, 6);
		t.add(this.inOperationField, 1, 6);
		t.add(this.inOperationText, 1, 6);
		t.mergeCells(1, 7, 2, 7);
		t.add(this.usingMemberSystemField, 1, 7);
		t.add(this.usingMemberSystemText, 1, 7);
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
			String number = iwc.getParameter(this.numberFieldName);
			String ssn = iwc.getParameter(this.ssnFieldName);
			String founded = iwc.getParameter(this.foundedFieldName);
			String type = iwc.getParameter(this.typeFieldName);
			String member = iwc.getParameter(this.memberUMFIFieldName);
			String make = iwc.getParameter(this.makeFieldName);
			// String connection =
			// iwc.getParameter(_connectionToSpecialFieldName);
			String status = iwc.getParameter(this.statusFieldName);
			String inOperation = iwc.getParameter(this.inOperationFieldName);
			String using = iwc.getParameter(this.usingMemberSystemFieldName);
			String netbokhald = iwc.getParameter(this.usingNetbokhaldFieldName);
			if (number != null) {
				this.fieldValues.put(this.numberFieldName, number);
			} else {
				this.fieldValues.put(this.numberFieldName, "");
			}
			if (ssn != null) {
				this.fieldValues.put(this.ssnFieldName, ssn);
			} else {
				this.fieldValues.put(this.ssnFieldName, "");
			}
			if (founded != null) {
				this.fieldValues.put(this.foundedFieldName, founded);
			} else {
				this.fieldValues.put(this.foundedFieldName, "");
			}
			if (type != null) {
				this.fieldValues.put(this.typeFieldName, type);
			} else {
				this.fieldValues.put(this.typeFieldName, "");
			}
			this.fieldValues.put(this.memberUMFIFieldName, new Boolean(
					member != null));
			if (make != null) {
				this.fieldValues.put(this.makeFieldName, make);
			} else {
				this.fieldValues.put(this.makeFieldName, "");
			}
			if (status != null) {
				this.fieldValues.put(this.statusFieldName, status);
			} else {
				this.fieldValues.put(this.statusFieldName, "");
			}
			this.fieldValues.put(this.inOperationFieldName, new Boolean(
					inOperation != null));
			this.fieldValues.put(this.usingMemberSystemFieldName, new Boolean(
					using != null));
			this.fieldValues.put(this.usingNetbokhaldFieldName, new Boolean(
					netbokhald != null));
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
			group = (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class))
					.findByPrimaryKey(new Integer(getGroupId())));
			String groupType = group.getGroupType();

			String number = (String) this.fieldValues.get(this.numberFieldName);
			String ssn = (String) this.fieldValues.get(this.ssnFieldName);
			String founded = (String) this.fieldValues
					.get(this.foundedFieldName);

			Boolean memberUMFI = (Boolean) this.fieldValues
					.get(this.memberUMFIFieldName);
			String status = (String) this.fieldValues.get(this.statusFieldName);
			Boolean inOperation = (Boolean) this.fieldValues
					.get(this.inOperationFieldName);
			Boolean usingSystem = (Boolean) this.fieldValues
					.get(this.usingMemberSystemFieldName);
			Boolean usingNetbokhald = (Boolean) this.fieldValues
					.get(this.usingNetbokhaldFieldName);
			group.setMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER, number);
			group.setMetaData(IWMemberConstants.META_DATA_CLUB_SSN, ssn);
			group
					.setMetaData(IWMemberConstants.META_DATA_CLUB_FOUNDED,
							founded);

			if (memberUMFI != null) {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_IN_UMFI,
						memberUMFI.toString());
			}

			if (IWMemberConstants.GROUP_TYPE_CLUB.equals(groupType)) {
				String make = (String) this.fieldValues.get(this.makeFieldName);
				String type = (String) this.fieldValues.get(this.typeFieldName);
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_TYPE, type);
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_MAKE, make);
			}

			group.setMetaData(IWMemberConstants.META_DATA_CLUB_STATUS, status);
			if (inOperation != null) {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_OPERATION,
						inOperation.toString());
			} else {
				group.setMetaData(IWMemberConstants.META_DATA_CLUB_OPERATION,
						Boolean.FALSE.toString());
			}
			if (usingSystem != null) {
				group.setMetaData(
						IWMemberConstants.META_DATA_CLUB_USING_SYSTEM,
						usingSystem.toString());
			} else {
				group.setMetaData(
						IWMemberConstants.META_DATA_CLUB_USING_SYSTEM,
						Boolean.FALSE.toString());
			}
			if (usingNetbokhald != null) {
				group.setMetaData(
						ISIMemberConstants.META_DATA_CLUB_USING_NETBOKHALD,
						usingNetbokhald.toString());
			} else {
				group.setMetaData(
						ISIMemberConstants.META_DATA_CLUB_USING_NETBOKHALD,
						Boolean.FALSE.toString());
			}

			// and store everything
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
			group = (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class))
					.findByPrimaryKey(new Integer(getGroupId())));
			List parents = group.getParentGroups();
			Iterator it = parents.iterator();
			String regional = null;
			if (it != null) {
				while (it.hasNext()) {
					Group parent = (Group) it.next();
					if (parent.getGroupType().equals(
							IWMemberConstants.GROUP_TYPE_REGIONAL_UNION)) {
						regional = parent.getName();
					}
				}
			}
			String number = group
					.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER);
			String ssn = group
					.getMetaData(IWMemberConstants.META_DATA_CLUB_SSN);
			String founded = group
					.getMetaData(IWMemberConstants.META_DATA_CLUB_FOUNDED);
			String type = group
					.getMetaData(IWMemberConstants.META_DATA_CLUB_TYPE);
			String member = group
					.getMetaData(IWMemberConstants.META_DATA_CLUB_IN_UMFI);
			String make = group
					.getMetaData(IWMemberConstants.META_DATA_CLUB_MAKE);
			String status = group
					.getMetaData(IWMemberConstants.META_DATA_CLUB_STATUS);
			String inOperation = group
					.getMetaData(IWMemberConstants.META_DATA_CLUB_OPERATION);
			String using = group
					.getMetaData(IWMemberConstants.META_DATA_CLUB_USING_SYSTEM);
			String netbokhald = group
					.getMetaData(ISIMemberConstants.META_DATA_CLUB_USING_NETBOKHALD);
			if (number != null) {
				this.fieldValues.put(this.numberFieldName, number);
			}
			if (ssn != null) {
				this.fieldValues.put(this.ssnFieldName, ssn);
			}
			if (founded != null) {
				this.fieldValues.put(this.foundedFieldName, founded);
			}
			if (type != null) {
				this.fieldValues.put(this.typeFieldName, type);
			}
			this.fieldValues.put(this.memberUMFIFieldName, new Boolean(member));
			if (make != null) {
				this.fieldValues.put(this.makeFieldName, make);
			}
			if (regional != null) {
				this.fieldValues.put(this.regionalUnionFieldName, regional);
			}
			if (status != null) {
				this.fieldValues.put(this.statusFieldName, status);
			}
			this.fieldValues.put(this.inOperationFieldName, new Boolean(
					inOperation));
			this.fieldValues.put(this.usingMemberSystemFieldName, new Boolean(
					using));
			this.fieldValues.put(this.usingNetbokhaldFieldName, new Boolean(
					netbokhald));
			updateFieldsDisplayStatus();
		} catch (RemoteException e) {
			e.printStackTrace(System.err);
		} catch (FinderException e) {
			e.printStackTrace(System.err);
		}
	}

	public ClubInformationPluginBusiness getClubInformationPluginBusiness(
			IWApplicationContext iwc) {
		ClubInformationPluginBusiness business = null;
		try {
			business = (ClubInformationPluginBusiness) com.idega.business.IBOLookup
					.getServiceInstance(iwc,
							ClubInformationPluginBusiness.class);
		} catch (java.rmi.RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		return business;
	}

	public MemberUserBusiness getMemberUserBusiness(IWApplicationContext iwc) {
		MemberUserBusiness business = null;
		try {
			business = (MemberUserBusiness) com.idega.business.IBOLookup
					.getServiceInstance(iwc, MemberUserBusiness.class);
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