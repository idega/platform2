/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.presentation;

import java.util.Hashtable;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.SelectDropdown;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SelectPanel;
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
	
	private CheckBox _activeField;
	private SelectPanel _statusField;
	private SelectDropdown _parentStatusField;
	private SelectDropdown _boardStatusField;
	
	private Text _activeText;
	private Text _statusText;
	private Text _parentStatusText;
	private Text _boardStatusText;
	
	private String _activeFieldName;
	private String _statusFieldName;
	private String _parentStatusFieldName;
	private String _boardStatusFieldName;
	
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
		_activeFieldName = "usr_stat_active";
		_statusFieldName = "usr_stat_status";
		_parentStatusFieldName = "usr_stat_parent_status";
		_boardStatusFieldName = "usr_stat_board_status";
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
		fieldValues.put(_activeFieldName, new Boolean(false));
		fieldValues.put(_statusFieldName, new String[1]);
		fieldValues.put(_parentStatusFieldName, "");	
		fieldValues.put(_boardStatusFieldName, "");	
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		_activeField.setChecked(((Boolean) fieldValues.get(_activeFieldName)).booleanValue());		
		_statusField.setSelectedElements((String[]) fieldValues.get(_statusFieldName));
		_parentStatusField.setSelectedOption((String) fieldValues.get(_parentStatusFieldName));		
		_boardStatusField.setSelectedOption((String) fieldValues.get(_boardStatusFieldName));		
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFields()
	 */
	public void initializeFields() {
		_activeField = new CheckBox(_activeFieldName);
		_statusField = new SelectPanel(_statusFieldName);
		_parentStatusField = new SelectDropdown(_parentStatusFieldName);
		
		/**
		 * @todo Setja í töflu og sækja þaðan.
		 */		
		_statusField.addOption(new SelectOption("Félagsmaður","0"));
		_statusField.addOption(new SelectOption("Iðkandi","1"));
		_statusField.addOption(new SelectOption("Keppandi","2"));
		_statusField.addOption(new SelectOption("Stjórnarmaður","3"));
		_statusField.addOption(new SelectOption("Þjálfari","4"));
		_statusField.addOption(new SelectOption("Aðstoðarþjálfari","5"));
		_statusField.addOption(new SelectOption("Dómari","6"));
		_statusField.addOption(new SelectOption("Foreldri","7"));
		_statusField.addOption(new SelectOption("Forráðamaður","8"));
		_statusField.addOption(new SelectOption("Starfsmaður","9"));
		_statusField.addOption(new SelectOption("Styrktaraðili","10"));
		_statusField.addOption(new SelectOption("Aukaflokkur","11"));
		_statusField.addOption(new SelectOption("Iðk. á árinu","12"));
		_statusField.addOption(new SelectOption("Félagsmaður","13"));
		_statusField.addOption(new SelectOption("Félagsmaður","14"));
		
		_parentStatusField.addOption(new SelectOption("","0"));
		_parentStatusField.addOption(new SelectOption("Forráðamaður I","1"));
		_parentStatusField.addOption(new SelectOption("Forráðamaður II","2"));
		_parentStatusField.addOption(new SelectOption("Í foreldraráði","3"));
		_parentStatusField.addOption(new SelectOption("Í unglinganefnd","4"));
		_parentStatusField.addOption(new SelectOption("Vill taka þátt í félagsstarfi","5"));
		
		_boardStatusField.addOption(new SelectOption("","0"));
		_boardStatusField.addOption(new SelectOption("Formaður","0"));
		_boardStatusField.addOption(new SelectOption("Varaformaður","0"));
		_boardStatusField.addOption(new SelectOption("Gjaldkeri","0"));
		_boardStatusField.addOption(new SelectOption("Ritari","0"));
		_boardStatusField.addOption(new SelectOption("Meðstjórnandi","0"));
		_boardStatusField.addOption(new SelectOption("Framkvæmdastjóri","0"));
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		_activeText = new Text(iwrb.getLocalizedString(_activeFieldName, "Active") + ":");
		_statusText = new Text(iwrb.getLocalizedString(_statusFieldName, "Status") + ":");
		_parentStatusText = new Text(iwrb.getLocalizedString(_parentStatusFieldName, "Parent status") + ":");
		_boardStatusText = new Text(iwrb.getLocalizedString(_boardStatusFieldName, "Board status") + ":");		
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#lineUpFields()
	 */
	public void lineUpFields() {
		empty();
		
		Table t = new Table(2, 4);
		t.add(_activeText, 1, 1);
		t.add(_activeField, 2, 1);
		t.add(_statusText, 1, 2);
		t.add(_statusField, 2, 2);
		t.add(_parentStatusText, 1, 3);
		t.add(_parentStatusField, 2, 3);
		t.add(_boardStatusText, 1, 4);
		t.add(_boardStatusField, 2, 4);

		add(t);		
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String active = iwc.getParameter(_activeFieldName);
			String status[] = iwc.getParameterValues(_statusFieldName);
			String parentStatus = iwc.getParameter(_parentStatusFieldName);
			String boardStatus = iwc.getParameter(_boardStatusFieldName);
			
			if (status != null) {
				fieldValues.put(_statusFieldName,status);
			}
			else {		
				fieldValues.put(_statusFieldName,new String[1]);
			}
			
			if (parentStatus != null) {
				fieldValues.put(_parentStatusFieldName,parentStatus);
			}
			else {		
				fieldValues.put(_parentStatusFieldName,"");
			}

			if (boardStatus != null) {
				fieldValues.put(_boardStatusFieldName,boardStatus);
			}
			else {		
				fieldValues.put(_boardStatusFieldName,"");
			}
			fieldValues.put(_activeFieldName, new Boolean(active != null));
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initFieldContents()
	 */
	public void initFieldContents() {

	}
}