/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
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
public class GroupBoardTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	
	private static final String TAB_NAME = "board_tab_name";
	private static final String DEFAULT_TAB_NAME = "Club Board";
	
	private TextInput _ssn1Field;
	private TextInput _ssn2Field;
	private TextInput _ssn3Field;

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
		_ssn1FieldName = "board_ssn1";
		_ssn2FieldName = "board_ssn2";
		_ssn3FieldName = "board_ssn3";
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
		fieldValues.put(_ssn1FieldName, "");
		fieldValues.put(_ssn2FieldName, "");
		fieldValues.put(_ssn3FieldName, "");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		_ssn1Field.setContent((String) fieldValues.get(_ssn1FieldName));
		_ssn2Field.setContent((String) fieldValues.get(_ssn2FieldName));
		_ssn3Field.setContent((String) fieldValues.get(_ssn3FieldName));
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
		_ssn1Field = new TextInput(_ssn1FieldName);
		_ssn2Field = new TextInput(_ssn2FieldName);
		_ssn3Field = new TextInput(_ssn3FieldName);
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
			IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		_ssn1Text = new Text(iwrb.getLocalizedString(_ssn1FieldName, "SSN1") + ":");
		_ssn2Text = new Text(iwrb.getLocalizedString(_ssn2FieldName, "SSN2") + ":");
		_ssn3Text = new Text(iwrb.getLocalizedString(_ssn3FieldName, "SSN3") + ":");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		Table t = new Table(2, 18);
		t.add(_ssn1Text, 1, 1);
		t.add(_ssn1Field, 2, 1);
		t.add("Nafn :",1,2);
		t.add("Aðsetur :",1,3);
		t.add("Sími :",1,4);
		t.add("Fax :",1,5);
		t.add("Tölvupóstfang :",1,6);
		t.add(_ssn2Text, 1, 7);
		t.add(_ssn2Field, 2, 7);
		t.add("Nafn :",1,8);
		t.add("Aðsetur :",1,9);
		t.add("Sími :",1,10);
		t.add("Fax :",1,11);
		t.add("Tölvupóstfang :",1,12);
		t.add(_ssn3Text, 1, 13);
		t.add(_ssn3Field, 2, 13);
		t.add("Nafn :",1,14);
		t.add("Aðsetur :",1,15);
		t.add("Sími :",1,16);
		t.add("Fax :",1,17);
		t.add("Tölvupóstfang :",1,18);

		add(t);
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String ssn1 = iwc.getParameter(_ssn1FieldName);
			String ssn2 = iwc.getParameter(_ssn2FieldName);
			String ssn3 = iwc.getParameter(_ssn3FieldName);

			if (ssn1 != null)
				fieldValues.put(_ssn1FieldName, ssn1);
			else
				fieldValues.put(_ssn1FieldName, "");
			if (ssn2 != null)
				fieldValues.put(_ssn2FieldName, ssn2);
			else
				fieldValues.put(_ssn2FieldName, "");
			if (ssn3 != null)
				fieldValues.put(_ssn2FieldName, ssn3);
			else
				fieldValues.put(_ssn3FieldName, "");

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

			String ssn1 = (String) fieldValues.get(_ssn1FieldName);
			String ssn2 = (String) fieldValues.get(_ssn2FieldName);
			String ssn3 = (String) fieldValues.get(_ssn3FieldName);

			group.setMetaData("BOARD_SSN1", ssn1);
			group.setMetaData("BOARD_SSN2", ssn2);
			group.setMetaData("BOARD_SSN3", ssn3);

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

			String ssn1 = group.getMetaData("BOARD_SSN1");
			String ssn2 = group.getMetaData("BOARD_SSN2");
			String ssn3 = group.getMetaData("BOARD_SSN2");

			if (ssn1 != null)
				fieldValues.put(_ssn1FieldName, ssn1);
			if (ssn2 != null)
				fieldValues.put(_ssn2FieldName, ssn2);
			if (ssn3 != null)
				fieldValues.put(_ssn3FieldName, ssn3);
			
			updateFieldsDisplayStatus();
		}
		catch (RemoteException e) {
			e.printStackTrace(System.err);
		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
		}
	}
}