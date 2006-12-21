/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
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
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.user.presentation.UserChooserBrowser;
import com.idega.user.presentation.UserGroupTab;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GroupBoardTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	
	private static final String TAB_NAME = "board_tab_name";
	private static final String DEFAULT_TAB_NAME = "Club Board";
	
	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String HELP_TEXT_KEY = "group_board_tab";

	
	private UserChooserBrowser _ssn1Field;
	private UserChooserBrowser _ssn2Field;
	private UserChooserBrowser _ssn3Field;

	private Text _ssn1Text;
	private Text _ssn2Text;
	private Text _ssn3Text;

	private String _ssn1FieldName;
	private String _ssn2FieldName;
	private String _ssn3FieldName;

	public GroupBoardTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	public GroupBoardTab(Group group) {
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
		this._ssn1FieldName = "board_member1";
		this._ssn2FieldName = "board_member2";
		this._ssn3FieldName = "board_member3";
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		this.fieldValues = new Hashtable();
		this.fieldValues.put(this._ssn1FieldName, "");
		this.fieldValues.put(this._ssn2FieldName, "");
		this.fieldValues.put(this._ssn3FieldName, "");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		User user1 = null;
		User user2 = null;
		User user3 = null;
		String ssn1 = (String) this.fieldValues.get(this._ssn1FieldName);
		String ssn2 = (String) this.fieldValues.get(this._ssn2FieldName);
		String ssn3 = (String) this.fieldValues.get(this._ssn3FieldName);
		try {
			UserHome home = (UserHome) com.idega.data.IDOLookup.getHome(User.class);
			if (ssn1 != null && !ssn1.equals("")) {
				user1 = (home.findByPrimaryKey(new Integer(ssn1)));
			}
			if (ssn2 != null && !ssn2.equals("")) {
				user2 = (home.findByPrimaryKey(new Integer(ssn2)));
			}
			if (ssn3 != null && !ssn3.equals("")) {
				user3 = (home.findByPrimaryKey(new Integer(ssn3)));
			}

			if (ssn1 != null && !ssn1.equals("")) {
				this._ssn1Field.setSelectedUser(ssn1,user1.getName());
			}
			if (ssn2 != null && !ssn2.equals("")) {
				this._ssn2Field.setSelectedUser(ssn2,user2.getName());
			}
			if (ssn3 != null && !ssn3.equals("")) {
				this._ssn3Field.setSelectedUser(ssn3,user3.getName());
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
		this._ssn1Field = new UserChooserBrowser(this._ssn1FieldName);
		this._ssn2Field = new UserChooserBrowser(this._ssn2FieldName);
		this._ssn3Field = new UserChooserBrowser(this._ssn3FieldName);
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
			IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		this._ssn1Text = new Text(iwrb.getLocalizedString(this._ssn1FieldName, "Member 1"));
		this._ssn1Text.setBold();
		this._ssn2Text = new Text(iwrb.getLocalizedString(this._ssn2FieldName, "Member 2") + ":");
		this._ssn2Text.setBold();
		this._ssn3Text = new Text(iwrb.getLocalizedString(this._ssn3FieldName, "Member 3") + ":");
		this._ssn3Text.setBold();
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		Table t = new Table(2, 2);
		t.setWidth(Table.HUNDRED_PERCENT);
		t.setCellpadding(0);
		t.setCellspacing(0);
		
		t.add(this._ssn1Text, 1, 1);
		t.add(Text.getBreak(), 1, 1);
		t.add(this._ssn1Field, 1, 1);

		t.add(this._ssn2Text, 2, 1);
		t.add(Text.getBreak(), 2, 1);
		t.add(this._ssn2Field, 2, 1);
		
		t.add(this._ssn3Text, 1, 2);
		t.add(Text.getBreak(), 1, 2);
		t.add(this._ssn3Field, 1, 2);

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
			String ssn1 = iwc.getParameter(this._ssn1FieldName);
			String ssn2 = iwc.getParameter(this._ssn2FieldName);
			String ssn3 = iwc.getParameter(this._ssn3FieldName);

			if (ssn1 != null) {
				this.fieldValues.put(this._ssn1FieldName, ssn1);
			}
			else {
				this.fieldValues.put(this._ssn1FieldName, "");
			}
			if (ssn2 != null) {
				this.fieldValues.put(this._ssn2FieldName, ssn2);
			}
			else {
				this.fieldValues.put(this._ssn2FieldName, "");
			}
			if (ssn3 != null) {
				this.fieldValues.put(this._ssn2FieldName, ssn3);
			}
			else {
				this.fieldValues.put(this._ssn3FieldName, "");
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

			String ssn1 = (String) this.fieldValues.get(this._ssn1FieldName);
			String ssn2 = (String) this.fieldValues.get(this._ssn2FieldName);
			String ssn3 = (String) this.fieldValues.get(this._ssn3FieldName);

			group.setMetaData("BOARD_MEMBER1", ssn1);
			group.setMetaData("BOARD_MEMBER2", ssn2);
			group.setMetaData("BOARD_MEMBER3", ssn3);

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

			String ssn1 = group.getMetaData("BOARD_SSN1");
			String ssn2 = group.getMetaData("BOARD_SSN2");
			String ssn3 = group.getMetaData("BOARD_SSN2");

			if (ssn1 != null) {
				this.fieldValues.put(this._ssn1FieldName, ssn1);
			}
			if (ssn2 != null) {
				this.fieldValues.put(this._ssn2FieldName, ssn2);
			}
			if (ssn3 != null) {
				this.fieldValues.put(this._ssn3FieldName, ssn3);
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