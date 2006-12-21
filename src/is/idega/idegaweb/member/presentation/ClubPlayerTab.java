/*
 * Created on Mar 11, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.presentation;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.FinderException;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.presentation.GroupChooser;
import com.idega.user.presentation.UserGroupTab;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ClubPlayerTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "cplay_tab_name";
	private static final String DEFAULT_TAB_NAME = "Club Player";
	
	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String HELP_TEXT_KEY = "club_player_tab";
	

	private Text _unionField;
	private TextInput _cplayField;
	private TextInput _nameField;
	private CheckBox _competitionField;
	private GroupChooser _coachesField;

	private Text _unionText;
	private Text _cplayText;
	private Text _nameText;
	private Text _competitionText;
	private Text _coachesText;

	private String _unionFieldName;
	private String _cplayFieldName;
	private String _nameFieldName;
	private String _competitionFieldName;
	private String _coachesFieldName;

	public ClubPlayerTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	public ClubPlayerTab(Group group) {
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
		this._unionFieldName = "cplay_union";
		this._cplayFieldName = "cplay_division";
		this._nameFieldName = "cplay_name";
		this._competitionFieldName = "cplay_comp";
		this._coachesFieldName = "cplay_coach";		
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		this.fieldValues = new Hashtable();
		this.fieldValues.put(this._unionFieldName, "");
		this.fieldValues.put(this._cplayFieldName, "");
		this.fieldValues.put(this._nameFieldName, "");
		this.fieldValues.put(this._competitionFieldName, new Boolean(false));
		this.fieldValues.put(this._coachesFieldName, "");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		this._unionField.setText((String) this.fieldValues.get(this._unionFieldName));
		this._cplayField.setContent((String) this.fieldValues.get(this._cplayFieldName));
		this._nameField.setContent((String) this.fieldValues.get(this._nameFieldName));
		this._competitionField.setChecked(((Boolean) this.fieldValues.get(this._competitionFieldName)).booleanValue());
		try {
			GroupHome home = (GroupHome) com.idega.data.IDOLookup.getHome(Group.class);
			String groupId = (String) this.fieldValues.get(this._coachesFieldName);

			if (groupId != null && !groupId.equals("")) {
				try {
					int index = groupId.indexOf("_");
					groupId = groupId.substring(index+1);	
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				Group group = (home.findByPrimaryKey(new Integer(groupId)));
				this._coachesField.setSelectedGroup(groupId,group.getName());
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}				
//		_coachesField.setContent((String) fieldValues.get(_coachesFieldName));		
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
		this._unionField = new Text();
		this._nameField = new TextInput(this._nameFieldName);
		this._cplayField = new TextInput(this._cplayFieldName);
		this._competitionField = new CheckBox(this._competitionFieldName);
		this._competitionField.setWidth("10");
		this._competitionField.setHeight("10");
		this._coachesField = new GroupChooser(this._coachesFieldName);
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
			IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		this._unionText = new Text(iwrb.getLocalizedString(this._unionFieldName, "S�rsamband"));
		this._unionText.setBold();
		
		this._cplayText = new Text(iwrb.getLocalizedString(this._cplayFieldName, "Flokkur"));
		this._cplayText.setBold();
		
		this._nameText = new Text(iwrb.getLocalizedString(this._nameFieldName, "Heiti flokks"));
		this._nameText.setBold();
		
		this._competitionText = new Text(iwrb.getLocalizedString(this._competitionFieldName, "Keppendamerking"));
		this._competitionText.setBold();
		
		this._coachesText = new Text(iwrb.getLocalizedString(this._coachesFieldName, "�j�lfarar"));
		this._coachesText.setBold();
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		Table t = new Table(2, 6);
		t.setCellpadding(5);
		t.setCellspacing(0);
		t.setWidth(Table.HUNDRED_PERCENT);
		
		t.add(this._unionText, 1, 1);
		t.add(Text.getBreak(), 1, 1);
		t.add(this._unionField, 1, 1);
		
		t.add(this._cplayText, 2, 1);
		t.add(Text.getBreak(), 2, 1);
		t.add(this._cplayField, 2, 1);
		
		t.add(this._nameText, 1, 2);
		t.add(Text.getBreak(), 1, 2);
		t.add(this._nameField, 1, 2);
		
		t.add(this._coachesText, 2, 2);
		t.add(Text.getBreak(), 2, 2);
		t.add(this._coachesField, 2, 2);

		t.mergeCells(1, 3, 2, 3);
		t.add(this._competitionText, 1, 3);
		t.add(this._competitionField, 1, 3);
		
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
			String flokkur = iwc.getParameter(this._cplayFieldName);
			String name = iwc.getParameter(this._nameFieldName);
			String comp = iwc.getParameter(this._competitionFieldName);
			String coach = iwc.getParameter(this._coachesFieldName);
			
			if (flokkur != null) {
				this.fieldValues.put(this._cplayFieldName, flokkur);
			}
			else {
				this.fieldValues.put(this._cplayFieldName, "");
			}
			if (name != null) {
				this.fieldValues.put(this._nameFieldName, name);
			}
			else {
				this.fieldValues.put(this._nameFieldName, "");
			}
			this.fieldValues.put(this._competitionFieldName, new Boolean(comp != null));
			if (coach != null) {
				this.fieldValues.put(this._coachesFieldName, coach);
			}
			else {
				this.fieldValues.put(this._coachesFieldName, "");
			}
				
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
			group = (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));

			String flokkur = (String) this.fieldValues.get(this._cplayFieldName);
			String name = (String) this.fieldValues.get(this._nameFieldName);
			Boolean comp = (Boolean) this.fieldValues.get(this._competitionFieldName);
			String coach = (String) this.fieldValues.get(this._coachesFieldName);

			group.setMetaData("CLUBPLAYER_FLOKKUR", flokkur);
			group.setMetaData("CLUBPLAYER_NAME", name);
			if (comp != null) {
				group.setMetaData("CLUBPLAYER_COMP", comp.toString());
			}
			else {
				group.setMetaData("CLUBPLAYER_COMP", Boolean.FALSE.toString());
			}
			group.setMetaData("CLUBPLAYER_COACH", coach);

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
			group = (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));

			String flokkur = group.getMetaData("CLUBPLAYER_FLOKKUR");
			String name = group.getMetaData("CLUBPLAYER_NAME");
			String comp = group.getMetaData("CLUBPLAYER_COMP");
			String coach = group.getMetaData("CLUBPLAYER_COACH");

			if (flokkur != null) {
				this.fieldValues.put(this._cplayFieldName, flokkur);
			}
			if (name != null) {
				this.fieldValues.put(this._nameFieldName, name);
			}
			this.fieldValues.put(this._competitionFieldName, new Boolean(comp!=null));
			if (coach != null) {
				this.fieldValues.put(this._coachesFieldName, coach);
			}
			
			updateFieldsDisplayStatus();
		}
		catch (RemoteException e) {
			e.printStackTrace(System.err);
		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
		}
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