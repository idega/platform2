/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation.plugin;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardTypeHome;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.presentation.UserGroupTab;

/**
 * @author palli
 */
public class ClubCreditCardContractTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi.block.accounting";

	private static final String TAB_NAME = "club_creditcard_tab_name";
	private static final String DEFAULT_TAB_NAME = "Club Creditcard";

	private TextInput _contractNumberField;
	private TextInput _commentField;
	private DropdownMenu _cardTypeField;

	private Text _contractNumberText;
	private Text _commentText;
	private Text _cardTypeText;

	private String _contractNumberFieldName;
	private String _commentFieldName;
	private String _cardTypeFieldName;

	private IWResourceBundle iwrb;
  
	public ClubCreditCardContractTab() {
		super();
	}

	public ClubCreditCardContractTab(Group group) {
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
		_contractNumberFieldName = "cccct_number";
		_commentFieldName = "cccct_comment";
		_cardTypeFieldName = "cccct_card_type";
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
		fieldValues.put(_contractNumberFieldName, "");
		fieldValues.put(_commentFieldName, "");
		fieldValues.put(_cardTypeFieldName, "");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		lineUpFields();
		_contractNumberField.setContent((String) fieldValues.get(_contractNumberFieldName));
		_commentField.setContent((String) fieldValues.get(_commentFieldName));
		_cardTypeField.setContent((String) fieldValues.get(_cardTypeFieldName));
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
    
				IWContext iwc = IWContext.getInstance();
				iwrb = getResourceBundle(iwc);
				setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));

		_contractNumberField = new TextInput(_contractNumberFieldName);
		_commentField = new TextInput(_commentFieldName);
		_cardTypeField = new DropdownMenu(_cardTypeFieldName);
		
		Collection type = null;
		try {
			type = ((CreditCardTypeHome) com.idega.data.IDOLookup.getHome(CreditCardType.class)).findAll();

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		if (type != null) {
			Iterator it = type.iterator();
			while (it.hasNext()) {
				CreditCardType cType = (CreditCardType) it.next();
				_cardTypeField.addMenuElement(((Integer) cType.getPrimaryKey()).intValue(), cType.getName());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		_contractNumberText = new Text(iwrb.getLocalizedString(_contractNumberFieldName, "Contract number") + ":");
		_commentText = new Text(iwrb.getLocalizedString(_commentFieldName, "Comment") + ":");
		_cardTypeText = new Text(iwrb.getLocalizedString(_cardTypeFieldName, "Card type") + ":");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		empty();

		Table t = new Table(2, 3);
		t.add(_contractNumberText, 1, 1);
		t.add(_contractNumberField, 2, 1);
		t.add(_cardTypeText, 1, 2);
		t.add(_cardTypeField, 2, 2);
		t.add(_commentText, 1, 3);
		t.add(_commentField, 2, 3);

		add(t);
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String contractNumber = iwc.getParameter(_contractNumberFieldName);
			String comment = iwc.getParameter(_commentFieldName);
			String cardType = iwc.getParameter(_cardTypeFieldName);
			if (contractNumber != null)
				fieldValues.put(_contractNumberFieldName, contractNumber);
			else
				fieldValues.put(_contractNumberFieldName, "");
			if (comment != null)
				fieldValues.put(_commentFieldName, comment);
			else
				fieldValues.put(_commentFieldName, "");
			if (cardType != null)
				fieldValues.put(_cardTypeFieldName, cardType);
			else
				fieldValues.put(_cardTypeFieldName, "");
			updateFieldsDisplayStatus();
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
/*		Group group;
		try {
			group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));

			String number = (String) fieldValues.get(_contractNumberFieldName);
			String ssn = (String) fieldValues.get(_commentFieldName);
			String founded = (String) fieldValues.get(_cardTypeFieldName);

		}
		catch (RemoteException e) {
			e.printStackTrace(System.err);
			return false;
		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
			return false;
		}*/
		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initFieldContents()
	 */
	public void initFieldContents() {
/*		Group group;
		try {
			group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));

			List parents = group.getParentGroups();
			Iterator it = parents.iterator();

			String regional = null;

			if (it != null) {
				while (it.hasNext()) {
					Group parent = (Group) it.next();
					if (parent.getGroupType().equals(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION))
						regional = parent.getName();
				}
			}

			String contractNumber = group.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER);
			String comment = group.getMetaData(IWMemberConstants.META_DATA_CLUB_SSN);
			String cardType = group.getMetaData(IWMemberConstants.META_DATA_CLUB_FOUNDED);

			if (contractNumber != null)
				fieldValues.put(_contractNumberFieldName, contractNumber);
			if (comment != null)
				fieldValues.put(_commentFieldName, comment);
			if (cardType != null)
				fieldValues.put(_cardTypeFieldName, cardType);
				
			updateFieldsDisplayStatus();
		}
		catch (RemoteException e) {
			e.printStackTrace(System.err);
		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
		}*/
	}

/*	public ClubInformationPluginBusiness getClubInformationPluginBusiness(IWApplicationContext iwc) {
		ClubInformationPluginBusiness business = null;
		try {
			business = (ClubInformationPluginBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ClubInformationPluginBusiness.class);
		}
		catch (java.rmi.RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
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
*/
}