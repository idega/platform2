package is.idega.idegaweb.member.presentation;
import java.sql.SQLException;

import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneType;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.presentation.UserGroupTab;
/**
 *@author     <a href="mailto:thomas@idega.is">Thomas Hilbig</a>
 *@version    1.0
 */
public class GroupOfficeContactTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	private static final String TAB_NAME = "grp_ocon_tab_name";
	private static final String DEFAULT_TAB_NAME = "Contact";
	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String HELP_TEXT_KEY = "group_office_contact_tab";
	private TextInput homePhoneField;
	private TextInput workPhoneField;
	private TextInput mobilePhoneField;
	private TextInput faxPhoneField;
	private DropdownMenu homePhoneMenu;
	private DropdownMenu workPhoneMenu;
	private DropdownMenu mobilePhoneMenu;
	private DropdownMenu faxPhoneMenu;
	private TextInput emailField;
	private TextInput homepageField;
	public static String homePhoneFieldName = "homePhone";
	public static String workPhoneFieldName = "workPhone";
	public static String mobilePhoneFieldName = "mobilePhone";
	public static String faxPhoneFieldName = "faxPhone";
	public static String homePhoneMenuName = "homeChoice";
	public static String workPhoneMenuName = "workChoice";
	public static String mobilePhoneMenuName = "mobileChoice";
	public static String faxPhoneMenuName = "faxChoice";
	public static String emailFieldName = "email";
	public static String homepageFieldName = "homepage";
	private Text homePhoneText;
	private Text workPhoneText;
	private Text mobilePhoneText;
	private Text faxPhoneText;
	private Text emailText;
	private Text homepageText;
	public GroupOfficeContactTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}
	public GroupOfficeContactTab(Group group) {
		this();
		// do not store the group because this tab instance will also be used by other groups
		// (see setGroupId() !)
		setGroupId(((Integer) group.getPrimaryKey()).intValue());
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	public void initializeFieldNames() {
	}
	public void initializeFieldValues() {
		fieldValues.put(homePhoneFieldName, "");
		fieldValues.put(workPhoneFieldName, "");
		fieldValues.put(mobilePhoneFieldName, "");
		fieldValues.put(faxPhoneFieldName, "");
		fieldValues.put(homePhoneMenuName, "");
		fieldValues.put(workPhoneMenuName, "");
		fieldValues.put(mobilePhoneMenuName, "");
		fieldValues.put(faxPhoneMenuName, "");
		fieldValues.put(emailFieldName, "");
		fieldValues.put(homepageFieldName, "");
		updateFieldsDisplayStatus();
	}
	public void updateFieldsDisplayStatus() {
		homePhoneField.setContent((String) fieldValues.get(homePhoneFieldName));
		workPhoneField.setContent((String) fieldValues.get(workPhoneFieldName));
		mobilePhoneField.setContent((String) fieldValues.get(mobilePhoneFieldName));
		faxPhoneField.setContent((String) fieldValues.get(faxPhoneFieldName));
		homepageField.setContent((String) fieldValues.get(homepageFieldName));
		if ((String) fieldValues.get(homePhoneMenuName) != null && ((String) fieldValues.get(homePhoneMenuName)).length() > 0)
			homePhoneMenu.setSelectedElement((String) fieldValues.get(homePhoneMenuName));
		if ((String) fieldValues.get(workPhoneMenuName) != null && ((String) fieldValues.get(workPhoneMenuName)).length() > 0)
			workPhoneMenu.setSelectedElement((String) fieldValues.get(workPhoneMenuName));
		if ((String) fieldValues.get(mobilePhoneMenuName) != null && ((String) fieldValues.get(mobilePhoneMenuName)).length() > 0)
			mobilePhoneMenu.setSelectedElement((String) fieldValues.get(mobilePhoneMenuName));
		if ((String) fieldValues.get(faxPhoneMenuName) != null && ((String) fieldValues.get(faxPhoneMenuName)).length() > 0)
			faxPhoneMenu.setSelectedElement((String) fieldValues.get(faxPhoneMenuName));
		emailField.setContent((String) fieldValues.get(emailFieldName));
	}
	public void initializeFields() {
		PhoneType[] phoneTypes = null;
		try {
			phoneTypes = (PhoneType[]) com.idega.core.contact.data.PhoneTypeBMPBean.getStaticInstance(PhoneType.class).findAll();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		if (phoneTypes != null) {
			if (phoneTypes.length > 0) {
				IWContext iwc = IWContext.getInstance();
				IWResourceBundle iwrb = getResourceBundle(iwc);
				for (int i = 0; i < phoneTypes.length; i++) {
					String n = phoneTypes[i].getName();
					if (n != null) {
						String l = iwrb.getLocalizedString("grp_phone_" + n, n);
						phoneTypes[i].setName(l);
					}
				}
			}
		}
		homePhoneField = new TextInput(homePhoneFieldName);
		homePhoneField.setLength(24);
		workPhoneField = new TextInput(workPhoneFieldName);
		workPhoneField.setLength(24);
		mobilePhoneField = new TextInput(mobilePhoneFieldName);
		mobilePhoneField.setLength(24);
		faxPhoneField = new TextInput(faxPhoneFieldName);
		faxPhoneField.setLength(24);
		homepageField = new TextInput(homepageFieldName);
		homepageField.setLength(24);
		homePhoneMenu = new DropdownMenu(phoneTypes, homePhoneMenuName);
		workPhoneMenu = new DropdownMenu(phoneTypes, workPhoneMenuName);
		mobilePhoneMenu = new DropdownMenu(phoneTypes, mobilePhoneMenuName);
		faxPhoneMenu = new DropdownMenu(phoneTypes, faxPhoneMenuName);
		for (int i = 0; i < phoneTypes.length; i++) {
			if (i == 0) {
				homePhoneMenu.setSelectedElement(phoneTypes[i].getPrimaryKey().toString());
			} else if (i == 1) {
				workPhoneMenu.setSelectedElement(phoneTypes[i].getPrimaryKey().toString());
			} else if (i == 2) {
				mobilePhoneMenu.setSelectedElement(phoneTypes[i].getPrimaryKey().toString());
			} else if (i == 3) {
				faxPhoneMenu.setSelectedElement(phoneTypes[i].getPrimaryKey().toString());
			}
		}
		emailField = new TextInput(emailFieldName);
		emailField.setLength(24);
	}
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		homePhoneText = new Text(iwrb.getLocalizedString(homePhoneFieldName, "Phone 1") + ":");
		//		homePhoneText.setFontSize(fontSize);
		workPhoneText = new Text(iwrb.getLocalizedString(workPhoneFieldName, "Phone 2") + ":");
		//		workPhoneText.setFontSize(fontSize);
		mobilePhoneText = new Text(iwrb.getLocalizedString(mobilePhoneFieldName, "Phone 3") + ":");
		//		mobilePhoneText.setFontSize(fontSize);
		faxPhoneText = new Text(iwrb.getLocalizedString(faxPhoneFieldName, "Phone 4") + ":");
		//		faxPhoneText.setFontSize(fontSize);
		emailText = new Text(iwrb.getLocalizedString(emailFieldName, "E-mail") + ":");
		//		emailText.setFontSize(fontSize);
		homepageText = new Text(iwrb.getLocalizedString(homepageFieldName, "Homepage") + ":");
		//		homepageText.setFontSize(fontSize);
	}
	public void lineUpFields() {
		resize(1, 4);
		Table staffTable = new Table(3, 4);
		staffTable.setWidth("100%");
		staffTable.setCellpadding(0);
		staffTable.setCellspacing(0);
		staffTable.setHeight(1, rowHeight);
		staffTable.setHeight(2, rowHeight);
		staffTable.setHeight(3, rowHeight);
		staffTable.setHeight(4, rowHeight);
		staffTable.add(homePhoneText, 1, 1);
		staffTable.add(homePhoneMenu, 3, 1);
		staffTable.add(homePhoneField, 2, 1);
		staffTable.add(workPhoneText, 1, 2);
		staffTable.add(workPhoneMenu, 3, 2);
		staffTable.add(workPhoneField, 2, 2);
		staffTable.add(mobilePhoneText, 1, 3);
		staffTable.add(mobilePhoneMenu, 3, 3);
		staffTable.add(mobilePhoneField, 2, 3);
		staffTable.add(faxPhoneText, 1, 4);
		staffTable.add(faxPhoneMenu, 3, 4);
		staffTable.add(faxPhoneField, 2, 4);
		add(staffTable, 1, 1);
		Table mailTable = new Table(2, 2);
		mailTable.setWidth("100%");
		mailTable.setCellpadding(0);
		mailTable.setCellspacing(0);
		mailTable.setHeight(1, rowHeight);
		mailTable.add(emailText, 1, 1);
		mailTable.add(emailField, 2, 1);
		mailTable.add(homepageText, 1, 2);
		mailTable.add(homepageField, 2, 2);
		add(mailTable, 1, 3);
		Help help = getHelpButton();
		add(help, 1, 4);
	}
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String homePhone = iwc.getParameter(homePhoneFieldName);
			String workPhone = iwc.getParameter(workPhoneFieldName);
			String mobilePhone = iwc.getParameter(mobilePhoneFieldName);
			String faxPhone = iwc.getParameter(faxPhoneFieldName);
			String homePhoneType = iwc.getParameter(homePhoneMenuName);
			String workPhoneType = iwc.getParameter(workPhoneMenuName);
			String mobilePhoneType = iwc.getParameter(mobilePhoneMenuName);
			String faxPhoneType = iwc.getParameter(faxPhoneMenuName);
			String email = iwc.getParameter(emailFieldName);
			String homepage = iwc.getParameter(homepageFieldName);
			if (homePhone != null) {
				fieldValues.put(homePhoneFieldName, homePhone);
			}
			if (workPhone != null) {
				fieldValues.put(workPhoneFieldName, workPhone);
			}
			if (mobilePhone != null) {
				fieldValues.put(mobilePhoneFieldName, mobilePhone);
			}
			if (faxPhone != null) {
				fieldValues.put(faxPhoneFieldName, faxPhone);
			}
			if (homePhoneType != null) {
				fieldValues.put(homePhoneMenuName, homePhoneType);
			}
			if (workPhoneType != null) {
				fieldValues.put(workPhoneMenuName, workPhoneType);
			}
			if (mobilePhoneType != null) {
				fieldValues.put(mobilePhoneMenuName, mobilePhoneType);
			}
			if (faxPhoneType != null) {
				fieldValues.put(faxPhoneMenuName, faxPhoneType);
			}
			if (email != null) {
				fieldValues.put(emailFieldName, email);
			}
			if (homepage != null)
				fieldValues.put(homepageFieldName, homepage);
			updateFieldsDisplayStatus();
			return true;
		}
		return false;
	}
	public boolean store(IWContext iwc) {
		try {
			GroupBusiness groupBiz = getGroupBusiness(getEventIWContext());
			Group group = groupBiz.getGroupByGroupID(getGroupId());
			if (getGroupId() > -1) {
				String[] phoneString = {(String) fieldValues.get(homePhoneFieldName), (String) fieldValues.get(workPhoneFieldName),
						(String) fieldValues.get(mobilePhoneFieldName), (String) fieldValues.get(faxPhoneFieldName)};
				String[] phoneTypeString = {(String) fieldValues.get(homePhoneMenuName), (String) fieldValues.get(workPhoneMenuName),
						(String) fieldValues.get(mobilePhoneMenuName), (String) fieldValues.get(faxPhoneMenuName)};
				for (int i = 0; i < phoneString.length; i++) {
					if (phoneString[i] != null && phoneString[i].length() > 0) {
						getGroupBusiness(iwc).updateGroupPhone(group, Integer.parseInt(phoneTypeString[i]), phoneString[i]);
					}
				}
				if ((String) fieldValues.get(emailFieldName) != null && ((String) fieldValues.get(emailFieldName)).length() > 0) {
					getGroupBusiness(iwc).updateGroupMail(group, (String) fieldValues.get(emailFieldName));
				}
			}
			if ((String) fieldValues.get(homepageFieldName) != null && ((String) fieldValues.get(homepageFieldName)).length() > 0) {
				group.setHomePageURL((String) fieldValues.get(homepageFieldName));
			}
			
			
			group.store();
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new RuntimeException("update group exception");
		}
		return true;
	}
	public void initFieldContents() {
		try {
			GroupBusiness groupBiz = getGroupBusiness(getEventIWContext());
			Group group = groupBiz.getGroupByGroupID(getGroupId());
			Phone[] phones = groupBiz.getGroupPhones(group);
			Email mail = groupBiz.getGroupEmail(group);
			String homepage = group.getHomePageURL();
			for (int a = 0; a < phones.length; a++) {
				if (a == 0) {
					fieldValues.put(homePhoneMenuName, (phones[a].getPhoneTypeId() != -1) ? Integer.toString(phones[a].getPhoneTypeId()) : "");
					fieldValues.put(homePhoneFieldName, (phones[a].getNumber() != null) ? phones[a].getNumber() : "");
				} else if (a == 1) {
					fieldValues.put(workPhoneMenuName, (phones[a].getPhoneTypeId() != -1) ? Integer.toString(phones[a].getPhoneTypeId()) : "");
					fieldValues.put(workPhoneFieldName, (phones[a].getNumber() != null) ? phones[a].getNumber() : "");
				} else if (a == 2) {
					fieldValues.put(mobilePhoneMenuName, (phones[a].getPhoneTypeId() != -1) ? Integer.toString(phones[a].getPhoneTypeId()) : "");
					fieldValues.put(mobilePhoneFieldName, (phones[a].getNumber() != null) ? phones[a].getNumber() : "");
				} else if (a == 3) {
					fieldValues.put(faxPhoneMenuName, (phones[a].getPhoneTypeId() != -1) ? Integer.toString(phones[a].getPhoneTypeId()) : "");
					fieldValues.put(faxPhoneFieldName, (phones[a].getNumber() != null) ? phones[a].getNumber() : "");
				}
			}
			if (mail != null)
				fieldValues.put(emailFieldName, (mail.getEmailAddress() != null) ? mail.getEmailAddress() : "");
			if (homepage != null)
				fieldValues.put(homepageFieldName, homepage);
			updateFieldsDisplayStatus();
		} catch (Exception e) {
			System.err.println("GroupOfficeContactTab error initFieldContents, userId : " + getGroupId());
		}
	}
	public Help getHelpButton() {
		IWContext iwc = IWContext.getInstance();
		IWBundle iwb = getBundle(iwc);
		Help help = new Help();
		Image helpImage = iwb.getImage("help.gif");
		help.setHelpTextBundle(MEMBER_HELP_BUNDLE_IDENTIFIER);
		help.setHelpTextKey(HELP_TEXT_KEY);
		help.setImage(helpImage);
		return help;
	}
}