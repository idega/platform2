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
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.TextArea;
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
public class FlokkarTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private TextInput _unionField;
	private TextInput _flokkurField;
	private TextInput _nameField;
	private TextInput _competitionField;
	private TextArea _coachesField;

	private Text _unionText;
	private Text _flokkurText;
	private Text _nameText;
	private Text _competitionText;
	private Text _coachesText;

	private String _unionFieldName;
	private String _flokkurFieldName;
	private String _nameFieldName;
	private String _competitionFieldName;
	private String _coachesFieldName;

	public FlokkarTab() {
		setName("Deildir");
	}

	public FlokkarTab(Group group) {
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
		_unionFieldName = "union";
		_flokkurFieldName = "flokkur";
		_nameFieldName = "name";
		_competitionFieldName = "comp";
		_coachesFieldName = "coach";		
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
		fieldValues.put(_unionFieldName, "");
		fieldValues.put(_flokkurFieldName, "");
		fieldValues.put(_nameFieldName, "");
		fieldValues.put(_competitionFieldName, "");
		fieldValues.put(_coachesFieldName, "");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		_unionField.setContent((String) fieldValues.get(_unionFieldName));
		_flokkurField.setContent((String) fieldValues.get(_flokkurFieldName));
		_nameField.setContent((String) fieldValues.get(_nameFieldName));
		_competitionField.setContent((String) fieldValues.get(_competitionFieldName));		
		_coachesField.setContent((String) fieldValues.get(_coachesFieldName));		
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
		_unionField = new TextInput(_unionFieldName);
		_nameField = new TextInput(_nameFieldName);
		_flokkurField = new TextInput(_flokkurFieldName);
		_competitionField = new TextInput(_competitionFieldName);
		_coachesField = new TextArea(_coachesFieldName);
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
			IWContext iwc = //getEventIWContext();
	IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		_unionText = new Text(iwrb.getLocalizedString(_unionFieldName, "Sérsamband") + ":");
		_flokkurText = new Text(iwrb.getLocalizedString(_flokkurFieldName, "Flokkur") + ":");
		_nameText = new Text(iwrb.getLocalizedString(_nameFieldName, "Heiti flokks") + ":");
		_competitionText = new Text(iwrb.getLocalizedString(_competitionFieldName, "Keppendamerking") + ":");
		_coachesText = new Text(iwrb.getLocalizedString(_coachesFieldName, "Þjálfarar") + ":");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		Table t = new Table(2, 5);
		t.add(_unionText, 1, 1);
		t.add(_unionField, 2, 1);
		t.add(_flokkurText, 1, 2);
		t.add(_flokkurField, 2, 2);
		t.add(_nameText, 1, 3);
		t.add(_nameField, 2, 3);
		t.add(_competitionText, 1, 4);
		t.add(_competitionField, 2, 4);
		t.add(_coachesText, 1, 5);
		t.add(_coachesField, 2, 5);

		add(t);
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String union = iwc.getParameter(_unionFieldName);
			String flokkur = iwc.getParameter(_flokkurFieldName);
			String name = iwc.getParameter(_nameFieldName);
			String comp = iwc.getParameter(_competitionFieldName);
			String coach = iwc.getParameter(_coachesFieldName);
			

			if (union != null)
				fieldValues.put(_unionFieldName, union);
			else
				fieldValues.put(_unionFieldName, "");
			if (flokkur != null)
				fieldValues.put(_flokkurFieldName, flokkur);
			else
				fieldValues.put(_flokkurFieldName, "");
			if (name != null)
				fieldValues.put(_nameFieldName, name);
			else
				fieldValues.put(_nameFieldName, "");
			if (comp != null)
				fieldValues.put(_competitionFieldName, comp);
			else
				fieldValues.put(_competitionFieldName, "");
			if (coach != null)
				fieldValues.put(_coachesFieldName, coach);
			else
				fieldValues.put(_coachesFieldName, "");
				

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

			String union = (String) fieldValues.get(_unionFieldName);
			String flokkur = (String) fieldValues.get(_flokkurFieldName);
			String name = (String) fieldValues.get(_nameFieldName);
			String comp = (String) fieldValues.get(_competitionFieldName);
			String coach = (String) fieldValues.get(_coachesFieldName);

			group.setMetaData("CLUBFLOKKUR_UNION", union);
			group.setMetaData("CLUBFLOKKUR_FLOKKUR", flokkur);
			group.setMetaData("CLUBFLOKKUR_NAME", name);
			group.setMetaData("CLUBFLOKKUR_COMP", comp);
			group.setMetaData("CLUBFLOKKUR_COACH", coach);

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

			String union = group.getMetaData("CLUBFLOKKUR_UNION");
			String flokkur = group.getMetaData("CLUBFLOKKUR_FLOKKUR");
			String name = group.getMetaData("CLUBFLOKKUR_NAME");
			String comp = group.getMetaData("CLUBFLOKKUR_COMP");
			String coach = group.getMetaData("CLUBFLOKKUR_COACH");

			if (union != null)
				fieldValues.put(_unionFieldName, union);
			if (flokkur != null)
				fieldValues.put(_flokkurFieldName, flokkur);
			if (name != null)
				fieldValues.put(_nameFieldName, name);
			if (comp != null)
				fieldValues.put(_competitionFieldName, comp);
			if (coach != null)
				fieldValues.put(_coachesFieldName, coach);
			
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