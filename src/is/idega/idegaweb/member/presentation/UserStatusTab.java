/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.presentation;

import java.sql.SQLException;
import java.util.Hashtable;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.SelectDropdown;
import com.idega.presentation.ui.SelectOption;
import com.idega.user.data.Group;
import com.idega.user.data.Status;
import com.idega.user.presentation.UserTab;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UserStatusTab extends UserTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "usr_stat_tab_name";
	private static final String DEFAULT_TAB_NAME = "Status";
	
	private CheckBox _inactiveField;
	private Text _groupField;
	private SelectDropdown _statusField;
	private CheckBox _parent1StatusField;
	private CheckBox _parent2StatusField;
	private CheckBox _parent3StatusField;
	
	private Text _inactiveText;
	private Text _groupText;
	private Text _statusText;
	private Text _parent1StatusText;
	private Text _parent2StatusText;
	private Text _parent3StatusText;
	
	private String _inactiveFieldName;
	private String _groupFieldName;
	private String _statusFieldName;
	private String _parent1StatusFieldName;
	private String _parent2StatusFieldName;
	private String _parent3StatusFieldName;
	
	public UserStatusTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}	
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldNames()
	 */
	public void initializeFieldNames() {
		_inactiveFieldName = "usr_stat_inactive";
		_groupFieldName = "usr_grp_status";
		_statusFieldName = "usr_stat_status";
		_parent1StatusFieldName = "usr_stat_parent1_status";
		_parent2StatusFieldName = "usr_stat_parent2_status";
		_parent3StatusFieldName = "usr_stat_parent3_status";
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
		fieldValues.put(_inactiveFieldName, Boolean.FALSE);
		fieldValues.put(_groupFieldName, "");
		fieldValues.put(_statusFieldName, "");
		fieldValues.put(_parent1StatusFieldName, Boolean.FALSE);	
		fieldValues.put(_parent2StatusFieldName, Boolean.FALSE);	
		fieldValues.put(_parent3StatusFieldName, Boolean.FALSE);	
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		_inactiveField.setChecked(((Boolean) fieldValues.get(_inactiveFieldName)).booleanValue());
		_groupField.setText((String) fieldValues.get(_groupFieldName));		
		_statusField.setSelectedOption((String) fieldValues.get(_statusFieldName));
		_parent1StatusField.setChecked(((Boolean) fieldValues.get(_parent1StatusFieldName)).booleanValue());
		_parent2StatusField.setChecked(((Boolean) fieldValues.get(_parent2StatusFieldName)).booleanValue());
		_parent3StatusField.setChecked(((Boolean) fieldValues.get(_parent3StatusFieldName)).booleanValue());
	}


	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFields()
	 */
	public void initializeFields() {
		_inactiveField = new CheckBox(_inactiveFieldName);
		
		_groupField = new Text("No selected group");//see initFieldContents
		
		_parent1StatusField = new CheckBox(_parent1StatusFieldName);
		_parent2StatusField = new CheckBox(_parent2StatusFieldName);
		_parent3StatusField = new CheckBox(_parent3StatusFieldName);
		_statusField = new SelectDropdown(_statusFieldName);
		
		Status[] status = null;
		try {
			status = (Status[]) com.idega.user.data.StatusBMPBean.getStaticInstance(Status.class).findAll();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		if (status != null) {
			if (status.length > 0) {
				IWContext iwc = IWContext.getInstance();
				IWResourceBundle iwrb = getResourceBundle(iwc);
				
				for (int i = 0; i < status.length; i++) {
					String n = status[i].getStatusKey();
					if (n != null) {
						String l = iwrb.getLocalizedString("usr_stat_" + n,n);
						_statusField.addOption(new SelectOption(l,((Integer)status[i].getPrimaryKey()).intValue()));
					} 					
				}
			}
		}
						
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		_inactiveText = new Text(iwrb.getLocalizedString(_inactiveFieldName, "In-active") + ":");
		_groupText = new Text(iwrb.getLocalizedString(_groupFieldName, "Group") + ":");
		_statusText = new Text(iwrb.getLocalizedString(_statusFieldName, "Status") + ":");
		_parent1StatusText = new Text(iwrb.getLocalizedString(_parent1StatusFieldName, "Parent status1") + ":");
		_parent2StatusText = new Text(iwrb.getLocalizedString(_parent2StatusFieldName, "Parent status2") + ":");
		_parent3StatusText = new Text(iwrb.getLocalizedString(_parent3StatusFieldName, "Parent status3") + ":");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#lineUpFields()
	 */
	public void lineUpFields() {
		empty();
		
		Table t = new Table(2, 6);
		t.add(_inactiveText, 1, 1);
		t.add(_inactiveField, 2, 1);
		t.add(_groupText, 1, 2);
		t.add(_groupField, 2, 2);
		t.add(_statusText, 1, 3);
		t.add(_statusField, 2, 3);
		t.add(_parent1StatusText, 1, 4);
		t.add(_parent1StatusField, 2, 4);
		t.add(_parent2StatusText, 1, 5);
		t.add(_parent2StatusField, 2, 5);
		t.add(_parent3StatusText, 1, 6);
		t.add(_parent3StatusField, 2, 6);

		add(t);		
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String inactive = iwc.getParameter(_inactiveFieldName);
			String status = iwc.getParameter(_statusFieldName);
			String parent1Status = iwc.getParameter(_parent1StatusFieldName);
			String parent2Status = iwc.getParameter(_parent2StatusFieldName);
			String parent3Status = iwc.getParameter(_parent3StatusFieldName);
			
			if (status != null) {
				fieldValues.put(_statusFieldName,status);
			}
			else {		
				fieldValues.put(_statusFieldName,"");
			}
			
			fieldValues.put(_parent1StatusFieldName,new Boolean(parent1Status != null));
			fieldValues.put(_parent2StatusFieldName,new Boolean(parent2Status != null));
			fieldValues.put(_parent3StatusFieldName,new Boolean(parent3Status != null));
			fieldValues.put(_inactiveFieldName, new Boolean(inactive != null));
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
	 * @see com.idega.user.presentation.UserTab#initFieldContents()
	 */
	public void initFieldContents() {
		if(this.getGroupID()>0){
			Group selectedGroup = this.getGroup();
			if(selectedGroup!=null){
				_groupField = new Text(selectedGroup.getName());
			}
		}
		else _groupField = new Text("No selected group");

	}
}