 /*
 * Created on Mar 11, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.business.plugins.ClubInformationPluginBusiness;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.presentation.GroupChooser;
import com.idega.user.presentation.UserGroupTab;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ClubDivisionTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	
	private static final String TAB_NAME = "cdiv_tab_name";
	private static final String DEFAULT_TAB_NAME = "Club Division";
	
	private TextInput _numberField;
	private TextInput _ssnField;
	private DateInput _foundedField;
	private DropdownMenu _connectionToSpecialField;
	private GroupChooser _boardGroupField;	

	private Text _numberText;
	private Text _ssnText;
	private Text _foundedText;
	private Text _connectionToSpecialText;	
	private Text _boardGroupText;

	private String _numberFieldName;
	private String _ssnFieldName;
	private String _foundedFieldName;
	private String _connectionToSpecialFieldName;
	private String _boardGroupFieldName;
	
	public ClubDivisionTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	public ClubDivisionTab(Group group) {
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
		_numberFieldName = "cdiv_number";
		_ssnFieldName = "cdiv_ssn";
		_foundedFieldName = "cdiv_founded";
		_connectionToSpecialFieldName = "cdiv_special";
		_boardGroupFieldName = "cdiv_board";
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
		fieldValues.put(_numberFieldName, "");
		fieldValues.put(_ssnFieldName, "");
		fieldValues.put(_foundedFieldName, new IWTimestamp().getDate().toString());
		fieldValues.put(_connectionToSpecialFieldName, "");
		fieldValues.put(_boardGroupFieldName,"");		
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		_numberField.setContent((String) fieldValues.get(_numberFieldName));
		_ssnField.setContent((String) fieldValues.get(_ssnFieldName));
		_foundedField.setContent((String) fieldValues.get(_foundedFieldName));
		String connection = (String) fieldValues.get(_connectionToSpecialFieldName);
		_connectionToSpecialField.setSelectedElement(connection);
		if (connection != null && !connection.equals(""))
			_connectionToSpecialField.setDisabled(true);
		try {
			GroupHome home = (GroupHome) com.idega.data.IDOLookup.getHome(Group.class);
			String groupId = (String) fieldValues.get(_boardGroupFieldName);

			if (groupId != null && !groupId.equals("")) {
				try {
					int index = groupId.indexOf("_");
					groupId = groupId.substring(index+1);	
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				Group group = (Group) (home.findByPrimaryKey(new Integer(groupId)));
				_boardGroupField.setSelectedGroup(groupId,group.getName());
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
		_ssnField = new TextInput(_ssnFieldName);
//		_ssnField.setAsIcelandicSSNumber("Vartöluprófun stemmir ekki");
		_foundedField = new DateInput(_foundedFieldName);
		_connectionToSpecialField = new DropdownMenu(_connectionToSpecialFieldName);	
		
		Collection special = null;
		try {
			special = ((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findGroupsByType("iwme_league");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		if (special != null) {
			Iterator it = special.iterator();
			while (it.hasNext()) {
				Group spec = (Group)it.next();
				_connectionToSpecialField.addMenuElement(((Integer)spec.getPrimaryKey()).intValue(),spec.getName());
			}
		}		
		
		_boardGroupField = new GroupChooser(_boardGroupFieldName);
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		_numberText = new Text(iwrb.getLocalizedString(_numberFieldName, "Number") + ":");
		_ssnText = new Text(iwrb.getLocalizedString(_ssnFieldName, "SSN") + ":");
		_foundedText = new Text(iwrb.getLocalizedString(_foundedFieldName, "Founded") + ":");
		_connectionToSpecialText = new Text(iwrb.getLocalizedString(_connectionToSpecialFieldName, "Connection to special") + ":");
		_boardGroupText = new Text(iwrb.getLocalizedString(_boardGroupFieldName, "Board") + ":");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		Table t = new Table(2, 5);
		t.add(_numberText, 1, 1);
		t.add(_numberField, 2, 1);
		t.add(_ssnText, 1, 2);
		t.add(_ssnField, 2, 2);
		t.add(_foundedText, 1, 3);
		t.add(_foundedField, 2, 3);
		t.add(_connectionToSpecialText, 1, 4);
		t.add(_connectionToSpecialField, 2, 4);
		t.add(_boardGroupText, 1, 5);
		t.add(_boardGroupField, 2, 5);

		add(t);
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String number = iwc.getParameter(_numberFieldName);
			String ssn = iwc.getParameter(_ssnFieldName);
			String founded = iwc.getParameter(_foundedFieldName);
			String connection = iwc.getParameter(_connectionToSpecialFieldName);
			String boardGroup = iwc.getParameter(_boardGroupFieldName);

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
			if (connection != null)
				fieldValues.put(_connectionToSpecialFieldName, connection);
			else
				fieldValues.put(_connectionToSpecialFieldName, "");
			if (boardGroup != null)
				fieldValues.put(_boardGroupFieldName, boardGroup);
			else
				fieldValues.put(_boardGroupFieldName, "");
				
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

			String number = (String) fieldValues.get(_numberFieldName);
			String ssn = (String) fieldValues.get(_ssnFieldName);
			String founded = (String) fieldValues.get(_foundedFieldName);
			String connection = (String) fieldValues.get(_connectionToSpecialFieldName);
			String board = (String) fieldValues.get(_boardGroupFieldName);
			
			group.setMetaData("CLUBDIV_NUMBER", number);
			group.setMetaData("CLUBDIV_SSN", ssn);
			group.setMetaData("CLUBDIV_FOUNDED", founded);
//			group.setMetaData("CLUBDIV_CONN", connection);
				String oldConnection = group.getMetaData("CLUBDIV_CONN");
				if (oldConnection == null && connection != null) {
					String clubName = null; 
					Group club = getMemberUserBusiness(iwc).getClubForGroup(group,iwc);
					if (club != null)
						clubName = club.getName();
					getClubInformationPluginBusiness(iwc).createSpecialConnectionDivision(connection, getGroupId(), clubName, iwc);
					group.setMetaData("CLUBDIV_CONN", connection);
				}

			

			group.setMetaData("CLUBDIV_BOARD", board);
			
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

			String number = group.getMetaData("CLUBDIV_NUMBER");
			String ssn = group.getMetaData("CLUBDIV_SSN");
			String founded = group.getMetaData("CLUBDIV_FOUNDED");
			String connection = group.getMetaData("CLUBDIV_CONN");
			String board = group.getMetaData("CLUBDIV_BOARD");

			if (number != null)
				fieldValues.put(_numberFieldName, number);
			if (ssn != null)
				fieldValues.put(_ssnFieldName, ssn);
			if (founded != null)
				fieldValues.put(_foundedFieldName, founded);
			if (connection != null)
				fieldValues.put(_connectionToSpecialFieldName, connection);
			if (board != null)
				fieldValues.put(_boardGroupFieldName, board);
				
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
	
	public MemberUserBusiness getMemberUserBusiness(IWApplicationContext iwc) {
		MemberUserBusiness business = null;
		try {
			business = (MemberUserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, MemberUserBusiness.class);
		}
		catch (java.rmi.RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		return business;
	}
	
}