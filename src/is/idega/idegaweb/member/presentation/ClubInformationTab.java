/*
 * Created on Mar 11, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.plugins.ClubInformationPluginBusiness;

import java.rmi.RemoteException;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
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
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ClubInformationTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "cit_tab_name";
	private static final String DEFAULT_TAB_NAME = "Club Information";

	private TextInput _numberField;
	private TextInput _ssnField;
	private TextInput _abbreviationField;
	private TextInput _shortNameField;
	private TextInput _nameField;
	private DateInput _foundedField;
	private DropdownMenu _typeField;
	private CheckBox _memberUMFIField;
	private DropdownMenu _makeField;
	private TextInput _connectionToSpecialField;
	private Text _regionalUnionField;
	private DropdownMenu _statusField;
	private CheckBox _premierLeagueField;
	private CheckBox _inOperationField;
	private CheckBox _usingMemberSystemField;

	private Text _numberText;
	private Text _ssnText;
	private Text _abrvText;
	private Text _shortNameText;
	private Text _nameText;
	private Text _foundedText;
	private Text _typeText;
	private Text _memberUMFIText;
	private Text _makeText;
	private Text _connectionToSpecialText;
	private Text _regionalUnionText;
	private Text _statusText;
	private Text _premierLeagueText;
	private Text _inOperationText;
	private Text _usingMemberSystemText;

	private String _numberFieldName;
	private String _ssnFieldName;
	private String _abrvFieldName;
	private String _shortNameFieldName;
	private String _nameFieldName;
	private String _foundedFieldName;
	private String _typeFieldName;
	private String _memberUMFIFieldName;
	private String _makeFieldName;
	private String _connectionToSpecialFieldName;
	private String _regionalUnionFieldName;
	private String _statusFieldName;
	private String _premierLeagueFieldName;
	private String _inOperationFieldName;
	private String _usingMemberSystemFieldName;

	public ClubInformationTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	public ClubInformationTab(Group group) {
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
		_numberFieldName = "cit_number";
		_ssnFieldName = "cit_ssn";
		_abrvFieldName = "cit_abrv";
		_shortNameFieldName = "cit_short";
		_nameFieldName = "cit_name";
		_foundedFieldName = "cit_founded";
		_typeFieldName = "cit_type";
		_memberUMFIFieldName = "cit_memberOfUMFI";
		_makeFieldName = "cit_make";
		_connectionToSpecialFieldName = "cit_special";
		_regionalUnionFieldName = "cit_regional";
		_statusFieldName = "cit_status";
		_premierLeagueFieldName = "cit_premier";
		_inOperationFieldName = "cit_operation";
		_usingMemberSystemFieldName = "cit_usingSystem";
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
		fieldValues.put(_numberFieldName, "");
		fieldValues.put(_ssnFieldName, "");
		fieldValues.put(_abrvFieldName, "");
		fieldValues.put(_shortNameFieldName, "");
		fieldValues.put(_nameFieldName, "");
		fieldValues.put(_foundedFieldName, new IWTimestamp().getDate().toString());
		fieldValues.put(_typeFieldName, "");
		fieldValues.put(_memberUMFIFieldName, new Boolean(false));
		fieldValues.put(_makeFieldName, "");
		fieldValues.put(_connectionToSpecialFieldName, "");
		fieldValues.put(_regionalUnionFieldName, "");
		fieldValues.put(_statusFieldName, "");
		fieldValues.put(_premierLeagueFieldName, new Boolean(false));
		fieldValues.put(_inOperationFieldName, new Boolean(false));
		fieldValues.put(_usingMemberSystemFieldName, new Boolean(false));
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		lineUpFields();
		_numberField.setContent((String) fieldValues.get(_numberFieldName));
		_ssnField.setContent((String) fieldValues.get(_ssnFieldName));
		_abbreviationField.setContent((String) fieldValues.get(_abrvFieldName));
		_shortNameField.setContent((String) fieldValues.get(_shortNameFieldName));
		_nameField.setContent((String) fieldValues.get(_nameFieldName));
		_foundedField.setContent((String) fieldValues.get(_foundedFieldName));
		_typeField.setSelectedElement((String) fieldValues.get(_typeFieldName));
		_memberUMFIField.setChecked(((Boolean) fieldValues.get(_memberUMFIFieldName)).booleanValue());
		String make = (String) fieldValues.get(_makeFieldName);
		_makeField.setSelectedElement(make);
		
		_makeField.setToEnableWhenSelected(_connectionToSpecialFieldName,"2");
		_makeField.setToDisableWhenSelected(_connectionToSpecialFieldName,"0");
		_makeField.setToDisableWhenSelected(_connectionToSpecialFieldName,"1");
		_makeField.setToDisableWhenSelected(_connectionToSpecialFieldName,"3");
		_makeField.setToDisableWhenSelected(_connectionToSpecialFieldName,"4");
		_makeField.setToDisableWhenSelected(_connectionToSpecialFieldName,"5");
		_connectionToSpecialField.setContent((String) fieldValues.get(_connectionToSpecialFieldName));
		_regionalUnionField.setText((String) fieldValues.get(_regionalUnionFieldName));
		_statusField.setSelectedElement((String) fieldValues.get(_statusFieldName));
		_premierLeagueField.setChecked(((Boolean) fieldValues.get(_premierLeagueFieldName)).booleanValue());
		_inOperationField.setChecked(((Boolean) fieldValues.get(_inOperationFieldName)).booleanValue());
		_usingMemberSystemField.setChecked(((Boolean) fieldValues.get(_usingMemberSystemFieldName)).booleanValue());
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
		_numberField = new TextInput(_numberFieldName);
		_ssnField = new TextInput(_ssnFieldName);
		_abbreviationField = new TextInput(_abrvFieldName);
		_shortNameField = new TextInput(_shortNameFieldName);
		_nameField = new TextInput(_nameFieldName);
		_foundedField = new DateInput(_foundedFieldName);
		_foundedField.setYearRange(1900, GregorianCalendar.getInstance().get(GregorianCalendar.YEAR));
		_typeField = new DropdownMenu(_typeFieldName);
		_memberUMFIField = new CheckBox(_memberUMFIFieldName);
		_makeField = new DropdownMenu(_makeFieldName);
		_connectionToSpecialField = new TextInput(_connectionToSpecialFieldName);
		_regionalUnionField = new Text();
		_statusField = new DropdownMenu(_statusFieldName);
		_premierLeagueField = new CheckBox(_premierLeagueFieldName);
		_inOperationField = new CheckBox(_inOperationFieldName);
		_usingMemberSystemField = new CheckBox(_usingMemberSystemFieldName);
		
		/**
		 * @todo Setja í töflu og sækja þaðan.
		 */
		_typeField.addMenuElement("1","Innlent félag");
		_typeField.addMenuElement("2","Sérsamband");
		_typeField.addMenuElement("3","Héraðssamband/Íþróttabandalag");
		_typeField.addMenuElement("4","Erlent félag");
		
		_makeField.addMenuElement("0","");
		_makeField.addMenuElement("1","Fjölgreinafélag");
		_makeField.addMenuElement("2","Sérgreinafélag");
		_makeField.addMenuElement("3","Félag án iðkenda");
		_makeField.addMenuElement("4","Ungmennafélag");
		_makeField.addMenuElement("5","Óvirkt");
		
		_statusField.addMenuElement("0","");
		_statusField.addMenuElement("1","Virkt");
		_statusField.addMenuElement("2","Óvirkt");
		_statusField.addMenuElement("3","Keppnisbann");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		_numberText = new Text(iwrb.getLocalizedString(_numberFieldName, "Number") + ":");
		_ssnText = new Text(iwrb.getLocalizedString(_ssnFieldName, "SSN") + ":");
		_abrvText = new Text(iwrb.getLocalizedString(_abrvFieldName, "Abbreviation") + ":");
		_shortNameText = new Text(iwrb.getLocalizedString(_shortNameFieldName, "Short name") + ":");
		_nameText = new Text(iwrb.getLocalizedString(_nameFieldName, "Long name") + ":");
		_foundedText = new Text(iwrb.getLocalizedString(_foundedFieldName, "Founded") + ":");
		_typeText = new Text(iwrb.getLocalizedString(_typeFieldName, "Type") + ":");
		_memberUMFIText = new Text(iwrb.getLocalizedString(_memberUMFIFieldName, "UMFI membership") + ":");
		_makeText = new Text(iwrb.getLocalizedString(_makeFieldName, "Make") + ":");
		_connectionToSpecialText = new Text(iwrb.getLocalizedString(_connectionToSpecialFieldName, "Connection to special") + ":");
		_regionalUnionText = new Text(iwrb.getLocalizedString(_regionalUnionFieldName, "Regional union") + ":");
		_statusText = new Text(iwrb.getLocalizedString(_statusFieldName, "Status") + ":");
		_premierLeagueText = new Text(iwrb.getLocalizedString(_premierLeagueFieldName, "Premier league") + ":");
		_inOperationText = new Text(iwrb.getLocalizedString(_inOperationFieldName, "In operation") + ":");
		_usingMemberSystemText = new Text(iwrb.getLocalizedString(_usingMemberSystemFieldName, "In member system") + ":");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		empty();
		
		Table t = new Table(2, 16);
		t.add(_numberText, 1, 1);
		t.add(_numberField, 2, 1);
		t.add(_ssnText, 1, 2);
		t.add(_ssnField, 2, 2);
		t.add(_abrvText, 1, 3);
		t.add(_abbreviationField, 2, 3);
		t.add(_shortNameText, 1, 4);
		t.add(_shortNameField, 2, 4);
		t.add(_nameText, 1, 5);
		t.add(_nameField, 2, 5);
		t.add(_foundedText, 1, 6);
		t.add(_foundedField, 2, 6);
		t.add(_typeText, 1, 7);
		t.add(_typeField, 2, 7);
		t.add(_memberUMFIText, 1, 8);
		t.add(_memberUMFIField, 2, 8);
		t.add(_makeText, 1, 9);
		t.add(_makeField, 2, 9);
		t.add(_connectionToSpecialText, 1, 10);
		t.add(_connectionToSpecialField, 2, 10);
		t.add(_regionalUnionText, 1, 12);
		t.add(_regionalUnionField, 2, 12);
		t.add(_statusText, 1, 13);
		t.add(_statusField, 2, 13);
		t.add(_premierLeagueText, 1, 14);
		t.add(_premierLeagueField, 2, 14);
		t.add(_inOperationText, 1, 15);
		t.add(_inOperationField, 2, 15);
		t.add(_usingMemberSystemText, 1, 16);
		t.add(_usingMemberSystemField, 2, 16);

		add(t);
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String number = iwc.getParameter(_numberFieldName);
			String ssn = iwc.getParameter(_ssnFieldName);
			String abrv = iwc.getParameter(_abrvFieldName);
			String shortName = iwc.getParameter(_shortNameFieldName);
			String name = iwc.getParameter(_nameFieldName);
			String founded = iwc.getParameter(_foundedFieldName);
			String type = iwc.getParameter(_typeFieldName);
			String member = iwc.getParameter(_memberUMFIFieldName);
			String make = iwc.getParameter(_makeFieldName);
			String connection = iwc.getParameter(_connectionToSpecialFieldName);
//			String regional = iwc.getParameter(_regionalUnionFieldName);
			String status = iwc.getParameter(_statusFieldName);
			String premier = iwc.getParameter(_premierLeagueFieldName);
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
			if (abrv != null)
				fieldValues.put(_abrvFieldName, abrv);
			else
				fieldValues.put(_abrvFieldName, "");
			if (shortName != null)
				fieldValues.put(_shortNameFieldName, shortName);
			else
				fieldValues.put(_shortNameFieldName, "");
			if (name != null)
				fieldValues.put(_nameFieldName, name);
			else
				fieldValues.put(_nameFieldName, "");
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
//			if (regional != null)
//				fieldValues.put(_regionalUnionFieldName, regional);
//			else
//				fieldValues.put(_regionalUnionFieldName, "");
			if (status != null)
				fieldValues.put(_statusFieldName, status);
			else
				fieldValues.put(_statusFieldName, "");
			fieldValues.put(_premierLeagueFieldName, new Boolean(premier != null));
			fieldValues.put(_inOperationFieldName, new Boolean(inOperation != null));
			fieldValues.put(_usingMemberSystemFieldName, new Boolean(using != null));

			updateFieldsDisplayStatus();
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
		System.out.println("Entering store");
		Group group;
		try {
			group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));
			// get corressponding service bean
			ClubInformationPluginBusiness ageGenderPluginBusiness = getClubInformationPluginBusiness(iwc);

			String number = (String) fieldValues.get(_numberFieldName);
			String ssn = (String) fieldValues.get(_ssnFieldName);
			String abrv = (String) fieldValues.get(_abrvFieldName);
			String shortName = (String) fieldValues.get(_shortNameFieldName);
			String name = (String) fieldValues.get(_nameFieldName);
			String founded = (String) fieldValues.get(_foundedFieldName);
			String type = (String) fieldValues.get(_typeFieldName);
			Boolean memberUMFI = (Boolean) fieldValues.get(_memberUMFIFieldName);
			String make = (String) fieldValues.get(_makeFieldName);
			String connection = (String) fieldValues.get(_connectionToSpecialFieldName);
//			String regional = (String) fieldValues.get(_regionalUnionFieldName);
			String status = (String) fieldValues.get(_statusFieldName);
			Boolean premier = (Boolean) fieldValues.get(_premierLeagueFieldName);
			Boolean inOperation = (Boolean) fieldValues.get(_inOperationFieldName);
			Boolean usingSystem = (Boolean) fieldValues.get(_usingMemberSystemFieldName);

			group.setMetaData("CLUBINFO_NUMBER", number);
			group.setMetaData("CLUBINFO_SSN", ssn);
			group.setMetaData("CLUBINFO_ABRV", abrv);
			group.setMetaData("CLUBINFO_SHORT", shortName);
			group.setMetaData("CLUBINFO_NAME", name);
			group.setMetaData("CLUBINFO_FOUNDED", founded);
			group.setMetaData("CLUBINFO_TYPE", type);
			if (memberUMFI != null)
				group.setMetaData("CLUBINFO_MEMBER", memberUMFI.toString());
			group.setMetaData("CLUBINFO_MAKE", make);
			if (make.equals("2"))
				group.setMetaData("CLUBINFO_CONN", connection);
			else
				group.setMetaData("CLUBINFO_CONN", "");
//			group.setMetaData("CLUBINFO_REGIONAL", regional);
			group.setMetaData("CLUBINFO_STATUS", status);
			if (premier != null)
				group.setMetaData("CLUBINFO_PREMIER", premier.toString());
			else
				group.setMetaData("CLUBINFO_PREMIER", Boolean.FALSE.toString());
			if (inOperation != null)
				group.setMetaData("CLUBINFO_OPERATION", inOperation.toString());
			else
				group.setMetaData("CLUBINFO_OPERATION", Boolean.FALSE.toString());
			if (usingSystem != null)
				group.setMetaData("CLUBINFO_SYSTEM", usingSystem.toString());
			else
				group.setMetaData("CLUBINFO_SYSTEM", Boolean.FALSE.toString());

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
		
			List parents = group.getParentGroups();
			Iterator it = parents.iterator();
			
			String regional = null;
			
			if (it != null) {
				while (it.hasNext()) {
					Group parent = (Group)it.next();
					if (parent.getGroupType().equals("iw_me_regional_union"))
						regional = parent.getName();
				}
			}
		
//			group.getParentGroups()

			String number = group.getMetaData("CLUBINFO_NUMBER");
			String ssn = group.getMetaData("CLUBINFO_SSN");
			String abrv = group.getMetaData("CLUBINFO_ABRV");
			String shortName = group.getMetaData("CLUBINFO_SHORT");
			String name = group.getMetaData("CLUBINFO_NAME");
			String founded = group.getMetaData("CLUBINFO_FOUNDED");
			String type = group.getMetaData("CLUBINFO_TYPE");
			String member = group.getMetaData("CLUBINFO_MEMBER");
			String make = group.getMetaData("CLUBINFO_MAKE");
			String connection = group.getMetaData("CLUBINFO_CONN");
//			String regional = group.getMetaData("CLUBINFO_REGIONAL");
			String status = group.getMetaData("CLUBINFO_STATUS");
			String premier = group.getMetaData("CLUBINFO_PREMIER");
			String inOperation = group.getMetaData("CLUBINFO_OPERATION");
			String using = group.getMetaData("CLUBINFO_SYSTEM");

			if (number != null)
				fieldValues.put(_numberFieldName, number);
			if (ssn != null)
				fieldValues.put(_ssnFieldName, ssn);
			if (abrv != null)
				fieldValues.put(_abrvFieldName, abrv);
			if (shortName != null)
				fieldValues.put(_shortNameFieldName, shortName);
			if (name != null)
				fieldValues.put(_nameFieldName, name);
			if (founded != null)
				fieldValues.put(_foundedFieldName, founded);
			if (type != null)
				fieldValues.put(_typeFieldName, type);
			fieldValues.put(_memberUMFIFieldName, new Boolean(member != null));
			if (make != null)
				fieldValues.put(_makeFieldName, make);
			if (connection != null)
				fieldValues.put(_connectionToSpecialFieldName, connection);
		if (regional != null)
				fieldValues.put(_regionalUnionFieldName, regional);
			if (status != null)
				fieldValues.put(_statusFieldName, status);
			fieldValues.put(_premierLeagueFieldName, new Boolean(premier != null));
			fieldValues.put(_inOperationFieldName, new Boolean(inOperation != null));
			fieldValues.put(_usingMemberSystemFieldName, new Boolean(using != null));

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
}