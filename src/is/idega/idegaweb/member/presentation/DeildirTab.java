/*
 * Created on Mar 11, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusiness;
import is.idega.idegaweb.member.business.plugins.ClubInformationPluginBusiness;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.FinderException;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
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
public class DeildirTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private TextInput _numberField;
	private TextInput _ssnField;
	private DateInput _foundedField;

	private Text _numberText;
	private Text _ssnText;
	private Text _foundedText;

	private String _numberFieldName;
	private String _ssnFieldName;
	private String _foundedFieldName;

	public DeildirTab() {
		setName("Deildir");
	}

	public DeildirTab(Group group) {
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
		_foundedFieldName = "founded";
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
		fieldValues.put(_numberFieldName, "");
		fieldValues.put(_ssnFieldName, "");
		fieldValues.put(_foundedFieldName, new IWTimestamp().getDate().toString());
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		_numberField.setContent((String) fieldValues.get(_numberFieldName));
		_ssnField.setContent((String) fieldValues.get(_ssnFieldName));
		_foundedField.setContent((String) fieldValues.get(_foundedFieldName));
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
		_numberField = new TextInput(_numberFieldName);
		_ssnField = new TextInput(_ssnFieldName);
//		_ssnField.setAsIcelandicSSNumber("Vartöluprófun stemmir ekki");
		_foundedField = new DateInput(_foundedFieldName);
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
			IWContext iwc = //getEventIWContext();
	IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		_numberText = new Text(iwrb.getLocalizedString(_numberFieldName, "Number") + ":");
		_ssnText = new Text(iwrb.getLocalizedString(_ssnFieldName, "SSN") + ":");
		_foundedText = new Text(iwrb.getLocalizedString(_foundedFieldName, "Founded") + ":");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		Table t = new Table(2, 3);
		t.add(_numberText, 1, 1);
		t.add(_numberField, 2, 1);
		t.add(_ssnText, 1, 2);
		t.add(_ssnField, 2, 2);
		t.add(_foundedText, 1, 3);
		t.add(_foundedField, 2, 3);

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
			String ssn = (String) fieldValues.get(_ssnFieldName);
			String founded = (String) fieldValues.get(_foundedFieldName);

			group.setMetaData("CLUBDIV_NUMBER", number);
			group.setMetaData("CLUBDIV_SSN", ssn);
			group.setMetaData("CLUBDIV_FOUNDED", founded);

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
			String founded = group.getMetaData("CLUBINFO_FOUNDED");

			if (number != null)
				fieldValues.put(_numberFieldName, number);
			if (ssn != null)
				fieldValues.put(_ssnFieldName, ssn);
			if (founded != null)
				fieldValues.put(_foundedFieldName, founded);
			
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