/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.presentation;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
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
import com.idega.presentation.ui.SelectDropdown;
import com.idega.presentation.ui.SelectOption;
import com.idega.user.business.UserStatusBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.Status;
import com.idega.user.data.StatusHome;
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
	
	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String HELP_TEXT_KEY = "user_status_tab";

//	private CheckBox _inactiveField; Eiki: removed this because it has no meaning
	private Text _groupField;
	private SelectDropdown _statusField;
//	private CheckBox _parent1StatusField;
//  private CheckBox _parent2StatusField;
	private CheckBox _parent3StatusField;

//	private Text _inactiveText;
	private Text _groupText;
	private Text _statusText;
//	private Text _parent1StatusText;
//	private Text _parent2StatusText;
	private Text _parent3StatusText;

//	private String _inactiveFieldName;
	private String _groupFieldName;
	private String _statusFieldName;
//	private String _parent1StatusFieldName;
//	private String _parent2StatusFieldName;
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
	//	_inactiveFieldName = "usr_stat_inactive";
		_groupFieldName = "usr_grp_status";
		_statusFieldName = "usr_stat_status";
//		_parent1StatusFieldName = "usr_stat_parent1_status";
//		_parent2StatusFieldName = "usr_stat_parent2_status";
		_parent3StatusFieldName = "usr_stat_parent3_status";
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
	//	fieldValues.put(_inactiveFieldName, Boolean.FALSE);
		
		fieldValues.put(_statusFieldName, "");
//		fieldValues.put(_parent1StatusFieldName, Boolean.FALSE);
//		fieldValues.put(_parent2StatusFieldName, Boolean.FALSE);
		fieldValues.put(_parent3StatusFieldName, Boolean.FALSE);
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
//		_inactiveField.setChecked(((Boolean) fieldValues.get(_inactiveFieldName)).booleanValue());
		if (getGroupID() > 0) {
			Group selectedGroup = getGroup();
			if (selectedGroup != null) {
				_groupField.setText(selectedGroup.getName());
			}
		}
		else {
			IWContext iwc = IWContext.getInstance();
			IWResourceBundle iwrb = getResourceBundle(iwc);
			_groupField.setText(iwrb.getLocalizedString("user_status_bar.no_group_selected","No group selected"));
		}
		_statusField.setSelectedOption((String) fieldValues.get(_statusFieldName));
//		_parent1StatusField.setChecked(((Boolean) fieldValues.get(_parent1StatusFieldName)).booleanValue());
//		_parent2StatusField.setChecked(((Boolean) fieldValues.get(_parent2StatusFieldName)).booleanValue());
		_parent3StatusField.setChecked(((Boolean) fieldValues.get(_parent3StatusFieldName)).booleanValue());
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFields()
	 */
	public void initializeFields() {
		System.out.println("initializeFields");
	//	_inactiveField = new CheckBox(_inactiveFieldName);
	//	_inactiveField.setWidth("10");
	//	_inactiveField.setHeight("10");

		_groupField = new Text(); //see initFieldContents

		_parent3StatusField = new CheckBox(_parent3StatusFieldName);
		_parent3StatusField.setWidth("10");
		_parent3StatusField.setHeight("10");

		_statusField = new SelectDropdown(_statusFieldName);

		IWContext iwc = IWContext.getInstance();
		Collection status = null;
		try {
			status = ((StatusHome)IDOLookup.getHome(Status.class)).findAll();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		if (status != null) {
			if (status.size() > 0) {
				IWResourceBundle iwrb = getResourceBundle(iwc);
				_statusField.addOption(new SelectOption(" "," "));
				
				Iterator it = status.iterator();
				while (it.hasNext()) {
					Status s = (Status)it.next();
					String n = s.getStatusKey();
					if (n != null) {
						String l = iwrb.getLocalizedString("usr_stat_" + n, n);
						_statusField.addOption(new SelectOption(l, ((Integer) s.getPrimaryKey()).intValue()));
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

		//_inactiveText = new Text(iwrb.getLocalizedString(_inactiveFieldName, "In-active"));
		//_inactiveText.setBold();
		
		_groupText = new Text(iwrb.getLocalizedString(_groupFieldName, "Group"));
		_groupText.setBold();
		
		_statusText = new Text(iwrb.getLocalizedString(_statusFieldName, "Status"));
		_statusText.setBold();
		
//		_parent1StatusText = new Text(iwrb.getLocalizedString(_parent1StatusFieldName, "Parent status1") + ":");
//		_parent2StatusText = new Text(iwrb.getLocalizedString(_parent2StatusFieldName, "Parent status2") + ":");
		_parent3StatusText = new Text(iwrb.getLocalizedString(_parent3StatusFieldName, "Parent status3"));
		_parent3StatusText.setBold();
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#lineUpFields()
	 */
	public void lineUpFields() {
		empty();

		Table t = new Table(2, 4);
		t.setCellpadding(5);
		t.setCellspacing(0);
		t.add(_groupText, 1, 1);
		t.add(_groupField, 2, 1);
		t.add(_statusText, 1, 2);
		t.add(_statusField, 2, 2);
//		t.add(_parent1StatusText, 1, 4);
//		t.add(_parent1StatusField, 2, 4);
//		t.add(_parent2StatusText, 1, 5);
//		t.add(_parent2StatusField, 2, 5);
		t.mergeCells(1, 4, 2, 4);
	//	t.add(_inactiveField, 1, 4);
	//	t.add(_inactiveText, 1, 4);
	//	t.mergeCells(1, 5, 2, 5);
		t.add(_parent3StatusField, 1, 4);
		t.add(_parent3StatusText, 1, 4);
		add(t);
	}

	public void main(IWContext iwc) {
		if (getPanel() != null) {
			getPanel().addHelpButton(getHelpButton());		
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
	//		String inactive = iwc.getParameter(_inactiveFieldName);
			String status = iwc.getParameter(_statusFieldName);
			
		System.out.println("Collect: status = " + status);
		
//			String parent1Status = iwc.getParameter(_parent1StatusFieldName);
//			String parent2Status = iwc.getParameter(_parent2StatusFieldName);
			String parent3Status = iwc.getParameter(_parent3StatusFieldName);

			if (status != null) {
				fieldValues.put(_statusFieldName, status);
			}
			else {
				fieldValues.put(_statusFieldName, " ");
			}

//			fieldValues.put(_parent1StatusFieldName, new Boolean(parent1Status != null));
//			fieldValues.put(_parent2StatusFieldName, new Boolean(parent2Status != null));
			fieldValues.put(_parent3StatusFieldName, new Boolean(parent3Status != null));
		//	fieldValues.put(_inactiveFieldName, new Boolean(inactive != null));

			updateFieldsDisplayStatus();
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
		try {
			String status = (String)fieldValues.get(_statusFieldName);
			System.out.println("Store: status = " + status);
			if (status != null && !status.equals(" ") && !status.equals("")) {
				int user_id = this.getUserId();
				int group_id = this.getGroupID();
				int status_id = Integer.parseInt(status);
				
				getUserStatusBusiness(iwc).setUserGroupStatus(user_id,group_id,status_id,iwc.getCurrentUserId()); 	
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initFieldContents()
	 */
	public void initFieldContents() {
		IWContext iwc = IWContext.getInstance();
		fieldValues = new Hashtable();
//		fieldValues.put(_inactiveFieldName, Boolean.FALSE);
		
		int status_id = -1;
		try {
			int user_id = getUserId();
			int group_id = getGroupID();
			status_id = getUserStatusBusiness(iwc).getUserGroupStatus(user_id,group_id);
		}
		catch(Exception e) {
			status_id = -1;
		}
		
		if (status_id > 0)
			fieldValues.put(_statusFieldName, Integer.toString(status_id));
		else
			fieldValues.put(_statusFieldName, "");
//		fieldValues.put(_parent1StatusFieldName, Boolean.FALSE);
//		fieldValues.put(_parent2StatusFieldName, Boolean.FALSE);
		fieldValues.put(_parent3StatusFieldName, Boolean.FALSE);

		updateFieldsDisplayStatus();
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
	
	public UserStatusBusiness getUserStatusBusiness(IWApplicationContext iwc){
		UserStatusBusiness business = null;
		if(business == null){
			try{
				business = (UserStatusBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,UserStatusBusiness.class);
			}
			catch(java.rmi.RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return business;
	}
	
}