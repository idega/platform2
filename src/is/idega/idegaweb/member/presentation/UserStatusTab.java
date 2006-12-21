/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.presentation;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
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
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserInfoColumnsBusiness;
import com.idega.user.business.UserStatusBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.Status;
import com.idega.user.data.StatusHome;
import com.idega.user.data.User;
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
	private static final String PARTICIPATING_STATUS_META_DATA_KEY = "participating_status";

//	private CheckBox _inactiveField; Eiki: removed this because it has no meaning
	private Text _groupField;
	private SelectDropdown _statusField;
//	private CheckBox _parent1StatusField;
//  private CheckBox _parent2StatusField;
	private CheckBox _parent3StatusField;
	private TextInput _userInfoField1;
	private TextInput _userInfoField2;
	private TextArea _userInfoField3;
	private Table t;

//	private Text _inactiveText;
	private Text _groupText;
	private Text _statusText;
//	private Text _parent1StatusText;
//	private Text _parent2StatusText;
	private Text _parent3StatusText;
	private Text _userInfoText;
	private Text _userInfoText1;
	private Text _userInfoText2;
	private Text _userInfoText3;

//	private String _inactiveFieldName;
	private String _groupFieldName;
	private String _statusFieldName;
//	private String _parent1StatusFieldName;
//	private String _parent2StatusFieldName;
	private String _parent3StatusFieldName;
	private String _userInfoFieldName;
	private String _userInfoFieldName1;
	private String _userInfoFieldName2;
	private String _userInfoFieldName3;

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
		this._groupFieldName = "usr_grp_status";
		this._statusFieldName = "usr_stat_status";
//		_parent1StatusFieldName = "usr_stat_parent1_status";
//		_parent2StatusFieldName = "usr_stat_parent2_status";
		this._parent3StatusFieldName = "usr_stat_parent3_status";
		this._userInfoFieldName = "usr_info_field";
		this._userInfoFieldName1 = "usr_info_field1";
		this._userInfoFieldName2 = "usr_info_field2";
		this._userInfoFieldName3 = "usr_info_field3";
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		this.fieldValues = new Hashtable();
	//	fieldValues.put(_inactiveFieldName, Boolean.FALSE);
		
		this.fieldValues.put(this._statusFieldName, "");
//		fieldValues.put(_parent1StatusFieldName, Boolean.FALSE);
//		fieldValues.put(_parent2StatusFieldName, Boolean.FALSE);
		this.fieldValues.put(this._parent3StatusFieldName, Boolean.FALSE);
		this.fieldValues.put(this._userInfoFieldName1, "");
		this.fieldValues.put(this._userInfoFieldName2, "");
		this.fieldValues.put(this._userInfoFieldName3, "");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
//		_inactiveField.setChecked(((Boolean) fieldValues.get(_inactiveFieldName)).booleanValue());
		if (getGroupID() > 0) {
			Group selectedGroup = getGroup();
			if (selectedGroup != null) {
				this._groupField.setText(selectedGroup.getName());
			}
		}
		
		else {
			IWContext iwc = IWContext.getInstance();
			IWResourceBundle iwrb = getResourceBundle(iwc);
			this._groupField.setText(iwrb.getLocalizedString("user_status_bar.no_group_selected","No group selected"));
		}
		
		this._statusField.setSelectedOption((String) this.fieldValues.get(this._statusFieldName));
//		_parent1StatusField.setChecked(((Boolean) fieldValues.get(_parent1StatusFieldName)).booleanValue());
//		_parent2StatusField.setChecked(((Boolean) fieldValues.get(_parent2StatusFieldName)).booleanValue());
		this._parent3StatusField.setChecked(((Boolean) this.fieldValues.get(this._parent3StatusFieldName)).booleanValue());
		this._userInfoField1.setValue((String) this.fieldValues.get(this._userInfoFieldName1));
		this._userInfoField2.setValue((String) this.fieldValues.get(this._userInfoFieldName2));
		this._userInfoField3.setValue((String) this.fieldValues.get(this._userInfoFieldName3));
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFields()
	 */
	public void initializeFields() {
	//	_inactiveField = new CheckBox(_inactiveFieldName);
	//	_inactiveField.setWidth("10");
	//	_inactiveField.setHeight("10");

		this._groupField = new Text(); //see initFieldContents

		this._parent3StatusField = new CheckBox(this._parent3StatusFieldName);
		this._parent3StatusField.setWidth("10");
		this._parent3StatusField.setHeight("10");

		this._statusField = new SelectDropdown(this._statusFieldName);
		this._userInfoField1 = new TextInput(this._userInfoFieldName1);
		this._userInfoField1.setSize(50);
		this._userInfoField2 = new TextInput(this._userInfoFieldName2);
		this._userInfoField2.setSize(50);
		this._userInfoField3 = new TextArea(this._userInfoFieldName3, 50, 16);

		IWContext iwc = IWContext.getInstance();
		List status = null;
		try {
			status = (List) ((StatusHome)IDOLookup.getHome(Status.class)).findAll();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		if (status != null) {
			if (status.size() > 0) {
				final IWResourceBundle iwrb = getResourceBundle(iwc);
				this._statusField.addOption(new SelectOption(" ",-1));
				
				
				final Collator collator = Collator.getInstance(iwc.getLocale());
				Collections.sort(status,new Comparator() {
					public int compare(Object arg0, Object arg1) {
						return collator.compare(iwrb.getLocalizedString("usr_stat_" + ((Status) arg0).getStatusKey(), ((Status) arg0).getStatusKey()), iwrb.getLocalizedString("usr_stat_" + ((Status) arg1).getStatusKey(), ((Status) arg1).getStatusKey()));
					}				
				});
				
				Iterator it = status.iterator();
				while (it.hasNext()) {
					Status s = (Status)it.next();
					String n = s.getStatusKey();
					if (n != null) {
						String l = iwrb.getLocalizedString("usr_stat_" + n, n);
						this._statusField.addOption(new SelectOption(l, ((Integer) s.getPrimaryKey()).intValue()));
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
		
		this._groupText = new Text(iwrb.getLocalizedString(this._groupFieldName, "Group"));
		this._groupText.setBold();
		
		this._statusText = new Text(iwrb.getLocalizedString(this._statusFieldName, "Status"));
		this._statusText.setBold();
		
//		_parent1StatusText = new Text(iwrb.getLocalizedString(_parent1StatusFieldName, "Parent status1") + ":");
//		_parent2StatusText = new Text(iwrb.getLocalizedString(_parent2StatusFieldName, "Parent status2") + ":");
		this._parent3StatusText = new Text(iwrb.getLocalizedString(this._parent3StatusFieldName, "Parent status3"));
		this._parent3StatusText.setBold();
		this._userInfoText = new Text(iwrb.getLocalizedString(this._userInfoFieldName, "Info about group membership")+":");
		this._userInfoText.setBold();
		this._userInfoText1 = new Text(iwrb.getLocalizedString(this._userInfoFieldName1, "User info 1"));
		this._userInfoText1.setBold();
		this._userInfoText2 = new Text(iwrb.getLocalizedString(this._userInfoFieldName2, "User info 2"));
		this._userInfoText2.setBold();
		this._userInfoText3 = new Text(iwrb.getLocalizedString(this._userInfoFieldName3, "User info 3"));
		this._userInfoText3.setBold();
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#lineUpFields()
	 */
	public void lineUpFields() {
		empty();
		
		this.t = new Table(2, 8);
		this.t.setCellpadding(5);
		this.t.setCellspacing(0);
		this.t.add(this._groupText, 1, 1);
		this.t.add(this._groupField, 2, 1);
		this.t.add(this._statusText, 1, 2);
		this.t.add(this._statusField, 2, 2);
//		t.add(_parent1StatusText, 1, 4);
//		t.add(_parent1StatusField, 2, 4);
//		t.add(_parent2StatusText, 1, 5);
//		t.add(_parent2StatusField, 2, 5);
		this.t.mergeCells(1, 4, 2, 4);
	//	t.add(_inactiveField, 1, 4);
	//	t.add(_inactiveText, 1, 4);
	//	t.mergeCells(1, 5, 2, 5);
		this.t.add(this._parent3StatusField, 1, 4);
		this.t.add(this._parent3StatusText, 1, 4);
		add(this.t);
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
			String status = iwc.getParameter(this._statusFieldName);
			
//			String parent1Status = iwc.getParameter(_parent1StatusFieldName);
//			String parent2Status = iwc.getParameter(_parent2StatusFieldName);
			String parent3Status = iwc.getParameter(this._parent3StatusFieldName);

			if (status != null) {
				this.fieldValues.put(this._statusFieldName, status);
			}
			else {
				this.fieldValues.put(this._statusFieldName, " ");
			}

//			fieldValues.put(_parent1StatusFieldName, new Boolean(parent1Status != null));
//			fieldValues.put(_parent2StatusFieldName, new Boolean(parent2Status != null));
			this.fieldValues.put(this._parent3StatusFieldName, new Boolean(parent3Status != null));
			String userInfo1 = iwc.getParameter(this._userInfoFieldName1);
			if (userInfo1 != null) {
				this.fieldValues.put(this._userInfoFieldName1, userInfo1);
			}
			else {
				this.fieldValues.put(this._userInfoFieldName1, "");
			}
			String userInfo2 = iwc.getParameter(this._userInfoFieldName2);
			if (userInfo2 != null) {
				this.fieldValues.put(this._userInfoFieldName2, userInfo2);
			}
			else {
				this.fieldValues.put(this._userInfoFieldName2, "");
			}
			String userInfo3 = iwc.getParameter(this._userInfoFieldName3);
			if (userInfo3 != null) {
				this.fieldValues.put(this._userInfoFieldName3, userInfo3);
			}
			else {
				this.fieldValues.put(this._userInfoFieldName3, "");
			}
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
			String status = (String)this.fieldValues.get(this._statusFieldName);
			if (status != null && !"".equals(status)) {
				int user_id = this.getUserId();
				int group_id = this.getGroupID();
				int status_id = Integer.parseInt(status);
				
				getUserStatusBusiness(iwc).setUserGroupStatus(user_id,group_id,status_id,iwc.getCurrentUserId()); 	
			}
			Boolean participatingStatus = (Boolean)this.fieldValues.get(this._parent3StatusFieldName);
			User user = getUser();
			user.setMetaData(PARTICIPATING_STATUS_META_DATA_KEY, participatingStatus.toString());
			user.store();

			String userInfo1 = (String)this.fieldValues.get(this._userInfoFieldName1);
			String userInfo2 = (String)this.fieldValues.get(this._userInfoFieldName2);
			String userInfo3 = (String)this.fieldValues.get(this._userInfoFieldName3);
			if (this.getGroupID() != -1) {
				getUserInfoColumnsBusiness(iwc).setUserInfo(this.getUserId(),this.getGroupID(),userInfo1,userInfo2,userInfo3);
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
		this.fieldValues = new Hashtable();
//		fieldValues.put(_inactiveFieldName, Boolean.FALSE);
		
		if (getGroupID() != -1) {
			this.t.setCellpaddingTop(5,40);
			this.t.mergeCells(1, 5, 2, 5);
			this.t.add(this._userInfoText, 1, 5);
			this.t.setCellpaddingBottom(5,10);
			this.t.add(this._userInfoText1, 1, 6);
			this.t.add(this._userInfoField1, 2, 6);
			this.t.add(this._userInfoText2, 1, 7);
			this.t.add(this._userInfoField2, 2, 7);
			this.t.add(this._userInfoText3, 1, 8);
			this.t.add(this._userInfoField3, 2, 8);
			this.t.setCellpaddingBottom(8,10);
		}

		int status_id = -1;
		try {
			int user_id = getUserId();
			int group_id = getGroupID();
			status_id = getUserStatusBusiness(iwc).getUserGroupStatus(user_id,group_id);
		}
		catch(Exception e) {
			status_id = -1;
		}
		
		if (status_id > 0) {
			this.fieldValues.put(this._statusFieldName, Integer.toString(status_id));
		}
		else {
			this.fieldValues.put(this._statusFieldName, "");
//		fieldValues.put(_parent1StatusFieldName, Boolean.FALSE);
//		fieldValues.put(_parent2StatusFieldName, Boolean.FALSE);
		}
		
		User user = getUser();
		String participatingStatus = user.getMetaData(PARTICIPATING_STATUS_META_DATA_KEY);
		this.fieldValues.put(this._parent3StatusFieldName, Boolean.valueOf(participatingStatus));
		String userInfo1 = "";
		try {
			userInfo1 = getUserInfoColumnsBusiness(iwc).getUserInfo1(getUserId(),getGroupID());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		if (userInfo1 != null) {
			this.fieldValues.put(this._userInfoFieldName1, userInfo1);
		} else {
			this.fieldValues.put(this._userInfoFieldName1, "");
		}
		String userInfo2 = "";
		try {
			userInfo2 = getUserInfoColumnsBusiness(iwc).getUserInfo2(getUserId(),getGroupID());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		if (userInfo2 != null) {
			this.fieldValues.put(this._userInfoFieldName2, userInfo2);
		} else {
			this.fieldValues.put(this._userInfoFieldName2, "");
		}
		String userInfo3 = "";
		try {
			userInfo3 = getUserInfoColumnsBusiness(iwc).getUserInfo3(getUserId(),getGroupID());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		if (userInfo3 != null) {
			this.fieldValues.put(this._userInfoFieldName3, userInfo3);
		} else {
			this.fieldValues.put(this._userInfoFieldName3, "");
		}

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
	
	public UserInfoColumnsBusiness getUserInfoColumnsBusiness(IWApplicationContext iwc){
		UserInfoColumnsBusiness business = null;
		if(business == null){
			try{
				business = (UserInfoColumnsBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,UserInfoColumnsBusiness.class);
			}
			catch(java.rmi.RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return business;
	}
}