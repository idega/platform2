/*
 * Created on Mar 11, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.plugins.ClubInformationPluginBusiness;

import java.util.Hashtable;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
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
	
	private TextInput _numberField;
	private TextInput _ssnField;
	private TextInput _abbreviationField;
	private TextInput _shortNameField;
	private TextInput _nameField;
	private DateInput _foundedField;
	private TextInput _typeField;
	private CheckBox _memberUMFIField;
	private TextInput _makeField;
	private TextInput _connectionToSpecialField;
	private CheckBox _belongsToUMFIField;
	private TextInput _regionalUnionField;
	private TextInput _statusField;
	private CheckBox _premierLeagueField;
	private CheckBox _inOperationField;
//	private Image _clubLogoField;
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
	private Text _belongsToUMFIText;
	private Text _regionalUnionText;
	private Text _statusText;
	private Text _premierLeagueText;
	private Text _inOperationText;
//	private Text _clubLogoText;
	private Text _usingMemberSystemText;

	private String _numberFieldName;
	private String _ssnFieldName;
	private String _abrvFieldName; //:)
	private String _shortNameFieldName;
	private String _nameFieldName;
	private String _foundedFieldName;
	private String _typeFieldName;
	private String _memberUMFIFieldName;
	private String _makeFieldName;
	private String _connectionToSpecialFieldName;
	private String _belongsToUMFIFieldName;
	private String _regionalUnionFieldName;
	private String _statusFieldName;
	private String _premierLeagueFieldName;
	private String _inOperationFieldName;
//	private String _clubLogoFieldName;
	private String _usingMemberSystemFieldName;

	public ClubInformationTab() {
		setName("ClubInformation");
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
		_numberFieldName = "number";
		_ssnFieldName = "ssn";
		_abrvFieldName = "abrv";
		_shortNameFieldName = "short";
		_nameFieldName = "name";
		_foundedFieldName = "founded";
		_typeFieldName = "type";
		_memberUMFIFieldName = "memberOfUMFI";
		_makeFieldName = "make";
		_connectionToSpecialFieldName = "special";
		_belongsToUMFIFieldName = "belongs";
		_regionalUnionFieldName = "regional";
		_statusFieldName = "status";
		_premierLeagueFieldName = "premier";
		_inOperationFieldName = "operation";
//		_clubLogoFieldName = "logo";
		_usingMemberSystemFieldName = "usingSystem";
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
		fieldValues.put(_belongsToUMFIFieldName, new Boolean(false));
		fieldValues.put(_regionalUnionFieldName, "");
		fieldValues.put(_statusFieldName, "");
		fieldValues.put(_premierLeagueFieldName, new Boolean(false));
		fieldValues.put(_inOperationFieldName, new Boolean(false));
//		fieldValues.put(_clubLogoFieldName, new Integer(-1));
		fieldValues.put(_usingMemberSystemFieldName, new Boolean(false));
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		_numberField.setContent((String) fieldValues.get(_numberFieldName));
		_ssnField.setContent((String) fieldValues.get(_ssnFieldName));
		_abbreviationField.setContent((String) fieldValues.get(_abrvFieldName));
		_shortNameField.setContent((String) fieldValues.get(_shortNameFieldName));
		_nameField.setContent((String) fieldValues.get(_nameFieldName));
		_foundedField.setContent((String) fieldValues.get(_foundedFieldName));
		_typeField.setContent((String) fieldValues.get(_typeFieldName));
		_memberUMFIField.setChecked(((Boolean) fieldValues.get(_memberUMFIFieldName)).booleanValue());
		_makeField.setContent((String) fieldValues.get(_makeFieldName));
		_connectionToSpecialField.setContent((String) fieldValues.get(_connectionToSpecialFieldName));
		_belongsToUMFIField.setChecked(((Boolean) fieldValues.get(_belongsToUMFIFieldName)).booleanValue());
		_regionalUnionField.setContent((String) fieldValues.get(_regionalUnionFieldName));
		_statusField.setContent((String) fieldValues.get(_statusFieldName));
		_premierLeagueField.setChecked(((Boolean) fieldValues.get(_premierLeagueFieldName)).booleanValue());
		_inOperationField.setChecked(((Boolean) fieldValues.get(_inOperationFieldName)).booleanValue());
//		_clubLogoField.setImageID(-1);
		_usingMemberSystemField.setChecked(((Boolean) fieldValues.get(_usingMemberSystemFieldName)).booleanValue());
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
		_numberField = new TextInput(_numberFieldName);
		_ssnField = new TextInput(_ssnFieldName);
		_ssnField.setAsIcelandicSSNumber("Vartöluprófun stemmir ekki");
		_abbreviationField = new TextInput(_abrvFieldName);
		_shortNameField = new TextInput(_shortNameFieldName);
		_nameField = new TextInput(_nameFieldName);
		_foundedField = new DateInput(_foundedFieldName);
		_typeField = new TextInput(_typeFieldName);
		_memberUMFIField = new CheckBox(_memberUMFIFieldName);
		_makeField = new TextInput(_makeFieldName);
		_connectionToSpecialField = new TextInput(_connectionToSpecialFieldName);
		_belongsToUMFIField = new CheckBox(_belongsToUMFIFieldName);
		_regionalUnionField = new TextInput(_regionalUnionFieldName);
		_statusField = new TextInput(_statusFieldName);
		_premierLeagueField = new CheckBox(_premierLeagueFieldName);
		_inOperationField = new CheckBox(_inOperationFieldName);
//		_clubLogoField = new Image(_clubLogoFieldName);
		_usingMemberSystemField = new CheckBox(_usingMemberSystemFieldName);
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = //getEventIWContext();
		IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		_numberText = new Text(iwrb.getLocalizedString(_numberFieldName,"Number") + ":");
		_ssnText = new Text(iwrb.getLocalizedString(_ssnFieldName,"SSN") + ":");
		_abrvText = new Text(iwrb.getLocalizedString(_abrvFieldName,"Abbreviation") + ":");
		_shortNameText = new Text(iwrb.getLocalizedString(_shortNameFieldName,"Short name") + ":");
		_nameText = new Text(iwrb.getLocalizedString(_nameFieldName,"Long name") + ":");
		_foundedText = new Text(iwrb.getLocalizedString(_foundedFieldName,"Founded") + ":");
		_typeText = new Text(iwrb.getLocalizedString(_typeFieldName,"Type") + ":");
		_memberUMFIText = new Text(iwrb.getLocalizedString(_belongsToUMFIFieldName,"UMFI membership") + ":");
		_makeText = new Text(iwrb.getLocalizedString(_makeFieldName,"Make") + ":");
		_connectionToSpecialText = new Text(iwrb.getLocalizedString(_connectionToSpecialFieldName,"Connection to special") + ":");
		_belongsToUMFIText = new Text(iwrb.getLocalizedString(_belongsToUMFIFieldName,"Belongs to") + ":");
		_regionalUnionText = new Text(iwrb.getLocalizedString(_regionalUnionFieldName,"Regional union") + ":");
		_statusText = new Text(iwrb.getLocalizedString(_statusFieldName,"Status") + ":");
		_premierLeagueText = new Text(iwrb.getLocalizedString(_premierLeagueFieldName,"Premier league") + ":");
		_inOperationText = new Text(iwrb.getLocalizedString(_inOperationFieldName,"In operation") + ":");
//		_clubLogoText = new Text("Merki:");
		_usingMemberSystemText = new Text(iwrb.getLocalizedString(_usingMemberSystemFieldName,"In member system") + ":");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		Table t = new Table(2,16);
		t.add(_numberText,1,1);
		t.add(_numberField,2,1);
		t.add(_ssnText,1,2);
		t.add(_ssnField,2,2);
		t.add(_abrvText,1,3);
		t.add(_abbreviationField,2,3);
		t.add(_shortNameText,1,4);
		t.add(_shortNameField,2,4);
		t.add(_nameText,1,5);
		t.add(_nameField,2,5);
		t.add(_foundedText,1,6);
		t.add(_foundedField,2,6);
		t.add(_typeText,1,7);
		t.add(_typeField,2,7);
		t.add(_memberUMFIText,1,8);
		t.add(_memberUMFIField,2,8);
		t.add(_makeText,1,9);
		t.add(_makeField,2,9);
		t.add(_connectionToSpecialText,1,10);
		t.add(_connectionToSpecialField,2,10);
		t.add(_belongsToUMFIText,1,11);
		t.add(_belongsToUMFIField,2,11);
		t.add(_regionalUnionText,1,12);
		t.add(_regionalUnionField,2,12);
		t.add(_statusText,1,13);
		t.add(_statusField,2,13);
		t.add(_premierLeagueText,1,14);
		t.add(_premierLeagueField,2,14);
		t.add(_inOperationText,1,15);
		t.add(_inOperationField,2,15);
//		t.add(_clubLogoText,1,16);
//		t.add(_clubLogoField,2,16);
		t.add(_usingMemberSystemText,1,16);
		t.add(_usingMemberSystemField,2,16);
		
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
			String belongs = iwc.getParameter(_belongsToUMFIFieldName);
			String regional = iwc.getParameter(_regionalUnionFieldName);
			String status = iwc.getParameter(_statusFieldName);
			String premier = iwc.getParameter(_premierLeagueFieldName);
			String inOperation = iwc.getParameter(_inOperationFieldName);
			String using = iwc.getParameter(_usingMemberSystemFieldName);

			fieldValues.put(_numberFieldName, number);
			fieldValues.put(_ssnFieldName, ssn);
			fieldValues.put(_abrvFieldName, abrv);
			fieldValues.put(_shortNameFieldName, shortName);
			fieldValues.put(_nameFieldName, name);
			fieldValues.put(_foundedFieldName, founded);
			fieldValues.put(_typeFieldName, type);
			fieldValues.put(_memberUMFIFieldName, new Boolean(member != null));
			fieldValues.put(_makeFieldName, make);
			fieldValues.put(_connectionToSpecialFieldName, connection);
			fieldValues.put(_belongsToUMFIFieldName, new Boolean(belongs != null));
			fieldValues.put(_regionalUnionFieldName, regional);
			fieldValues.put(_statusFieldName, status);
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
		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initFieldContents()
	 */
	public void initFieldContents() {
	}
	
	public ClubInformationPluginBusiness getClubInformationPluginBusiness(IWApplicationContext iwc){
		ClubInformationPluginBusiness business = null;
		if(business == null){
			try{
				business = (ClubInformationPluginBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,ClubInformationPluginBusiness.class);
			}
			catch(java.rmi.RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return business;
	}	
}