package is.idega.idegaweb.member.presentation;
import java.sql.SQLException;

import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneType;
import com.idega.data.GenericEntity;
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
		this.fieldValues.put(homePhoneFieldName, "");
		this.fieldValues.put(workPhoneFieldName, "");
		this.fieldValues.put(mobilePhoneFieldName, "");
		this.fieldValues.put(faxPhoneFieldName, "");
		this.fieldValues.put(homePhoneMenuName, "");
		this.fieldValues.put(workPhoneMenuName, "");
		this.fieldValues.put(mobilePhoneMenuName, "");
		this.fieldValues.put(faxPhoneMenuName, "");
		this.fieldValues.put(emailFieldName, "");
		this.fieldValues.put(homepageFieldName, "");
		updateFieldsDisplayStatus();
	}
	public void updateFieldsDisplayStatus() {
		this.homePhoneField.setContent((String) this.fieldValues.get(homePhoneFieldName));
		this.workPhoneField.setContent((String) this.fieldValues.get(workPhoneFieldName));
		this.mobilePhoneField.setContent((String) this.fieldValues.get(mobilePhoneFieldName));
		this.faxPhoneField.setContent((String) this.fieldValues.get(faxPhoneFieldName));
		this.homepageField.setContent((String) this.fieldValues.get(homepageFieldName));
		if ((String) this.fieldValues.get(homePhoneMenuName) != null && ((String) this.fieldValues.get(homePhoneMenuName)).length() > 0) {
			this.homePhoneMenu.setSelectedElement((String) this.fieldValues.get(homePhoneMenuName));
		}
		if ((String) this.fieldValues.get(workPhoneMenuName) != null && ((String) this.fieldValues.get(workPhoneMenuName)).length() > 0) {
			this.workPhoneMenu.setSelectedElement((String) this.fieldValues.get(workPhoneMenuName));
		}
		if ((String) this.fieldValues.get(mobilePhoneMenuName) != null && ((String) this.fieldValues.get(mobilePhoneMenuName)).length() > 0) {
			this.mobilePhoneMenu.setSelectedElement((String) this.fieldValues.get(mobilePhoneMenuName));
		}
		if ((String) this.fieldValues.get(faxPhoneMenuName) != null && ((String) this.fieldValues.get(faxPhoneMenuName)).length() > 0) {
			this.faxPhoneMenu.setSelectedElement((String) this.fieldValues.get(faxPhoneMenuName));
		}
		this.emailField.setContent((String) this.fieldValues.get(emailFieldName));
	}
	public void initializeFields() {
		PhoneType[] phoneTypes = null;
		try {
			phoneTypes = (PhoneType[]) GenericEntity.getStaticInstance(PhoneType.class).findAll();
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
		this.homePhoneField = new TextInput(homePhoneFieldName);
		this.homePhoneField.setLength(12);
		this.workPhoneField = new TextInput(workPhoneFieldName);
		this.workPhoneField.setLength(12);
		this.mobilePhoneField = new TextInput(mobilePhoneFieldName);
		this.mobilePhoneField.setLength(12);
		this.faxPhoneField = new TextInput(faxPhoneFieldName);
		this.faxPhoneField.setLength(12);
		this.homepageField = new TextInput(homepageFieldName);
		this.homepageField.setLength(24);
		this.homePhoneMenu = new DropdownMenu(phoneTypes, homePhoneMenuName);
		this.workPhoneMenu = new DropdownMenu(phoneTypes, workPhoneMenuName);
		this.mobilePhoneMenu = new DropdownMenu(phoneTypes, mobilePhoneMenuName);
		this.faxPhoneMenu = new DropdownMenu(phoneTypes, faxPhoneMenuName);
		for (int i = 0; i < phoneTypes.length; i++) {
			if (i == 0) {
				this.homePhoneMenu.setSelectedElement(phoneTypes[i].getPrimaryKey().toString());
			} else if (i == 1) {
				this.workPhoneMenu.setSelectedElement(phoneTypes[i].getPrimaryKey().toString());
			} else if (i == 2) {
				this.mobilePhoneMenu.setSelectedElement(phoneTypes[i].getPrimaryKey().toString());
			} else if (i == 3) {
				this.faxPhoneMenu.setSelectedElement(phoneTypes[i].getPrimaryKey().toString());
			}
		}
		this.emailField = new TextInput(emailFieldName);
		this.emailField.setLength(24);
	}
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		this.homePhoneText = new Text(iwrb.getLocalizedString(homePhoneFieldName, "Phone 1") + ":");
		this.homePhoneText.setBold();

		this.workPhoneText = new Text(iwrb.getLocalizedString(workPhoneFieldName, "Phone 2") + ":");
		this.workPhoneText.setBold();

		this.mobilePhoneText = new Text(iwrb.getLocalizedString(mobilePhoneFieldName, "Phone 3") + ":");
		this.mobilePhoneText.setBold();

		this.faxPhoneText = new Text(iwrb.getLocalizedString(faxPhoneFieldName, "Phone 4") + ":");
		this.faxPhoneText.setBold();

		this.emailText = new Text(iwrb.getLocalizedString(emailFieldName, "E-mail") + ":");
		this.emailText.setBold();

		this.homepageText = new Text(iwrb.getLocalizedString(homepageFieldName, "Homepage") + ":");
		this.homepageText.setBold();
	}
	public void lineUpFields() {
		resize(1, 1);
		
		Table table = new Table(2, 4);
		table.setWidth("100%");
		table.setCellpadding(5);
		table.setCellspacing(0);

		table.add(this.homePhoneText, 1, 1);
		table.add(Text.getBreak(), 1, 1);
		table.add(this.homePhoneField, 1, 1);
		table.add(this.homePhoneMenu, 1, 1);
		
		table.add(this.workPhoneText, 2, 1);
		table.add(Text.getBreak(), 2, 1);
		table.add(this.workPhoneField, 2, 1);
		table.add(this.workPhoneMenu, 2, 1);
		
		table.add(this.mobilePhoneText, 1, 2);
		table.add(Text.getBreak(), 1, 2);
		table.add(this.mobilePhoneField, 1, 2);
		table.add(this.mobilePhoneMenu, 1, 2);
		
		table.add(this.faxPhoneText, 2, 2);
		table.add(Text.getBreak(), 2, 2);
		table.add(this.faxPhoneField, 2, 2);
		table.add(this.faxPhoneMenu, 2, 2);
		
		table.add(this.emailText, 1, 4);
		table.add(Text.getBreak(), 1, 4);
		table.add(this.emailField, 1, 4);

		table.add(this.homepageText, 2, 4);
		table.add(Text.getBreak(), 2, 4);
		table.add(this.homepageField, 2, 4);
		
		add(table, 1, 1);
	}

	public void main(IWContext iwc) {
		getPanel().addHelpButton(getHelpButton());		
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
				this.fieldValues.put(homePhoneFieldName, homePhone);
			}
			if (workPhone != null) {
				this.fieldValues.put(workPhoneFieldName, workPhone);
			}
			if (mobilePhone != null) {
				this.fieldValues.put(mobilePhoneFieldName, mobilePhone);
			}
			if (faxPhone != null) {
				this.fieldValues.put(faxPhoneFieldName, faxPhone);
			}
			if (homePhoneType != null) {
				this.fieldValues.put(homePhoneMenuName, homePhoneType);
			}
			if (workPhoneType != null) {
				this.fieldValues.put(workPhoneMenuName, workPhoneType);
			}
			if (mobilePhoneType != null) {
				this.fieldValues.put(mobilePhoneMenuName, mobilePhoneType);
			}
			if (faxPhoneType != null) {
				this.fieldValues.put(faxPhoneMenuName, faxPhoneType);
			}
			if (email != null) {
				this.fieldValues.put(emailFieldName, email);
			}
			if (homepage != null) {
				this.fieldValues.put(homepageFieldName, homepage);
			}
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
				String[] phoneString = {(String) this.fieldValues.get(homePhoneFieldName), (String) this.fieldValues.get(workPhoneFieldName),
						(String) this.fieldValues.get(mobilePhoneFieldName), (String) this.fieldValues.get(faxPhoneFieldName)};
				String[] phoneTypeString = {(String) this.fieldValues.get(homePhoneMenuName), (String) this.fieldValues.get(workPhoneMenuName),
						(String) this.fieldValues.get(mobilePhoneMenuName), (String) this.fieldValues.get(faxPhoneMenuName)};
				for (int i = 0; i < phoneString.length; i++) {
					if (phoneString[i] != null && phoneString[i].length() > 0) {
						getGroupBusiness(iwc).updateGroupPhone(group, Integer.parseInt(phoneTypeString[i]), phoneString[i]);
					}
				}
				if ((String) this.fieldValues.get(emailFieldName) != null && ((String) this.fieldValues.get(emailFieldName)).length() > 0) {
					getGroupBusiness(iwc).updateGroupMail(group, (String) this.fieldValues.get(emailFieldName));
				}
			}
			if ((String) this.fieldValues.get(homepageFieldName) != null && ((String) this.fieldValues.get(homepageFieldName)).length() > 0) {
				group.setHomePageURL((String) this.fieldValues.get(homepageFieldName));
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
					this.fieldValues.put(homePhoneMenuName, (phones[a].getPhoneTypeId() != -1) ? Integer.toString(phones[a].getPhoneTypeId()) : "");
					this.fieldValues.put(homePhoneFieldName, (phones[a].getNumber() != null) ? phones[a].getNumber() : "");
				} else if (a == 1) {
					this.fieldValues.put(workPhoneMenuName, (phones[a].getPhoneTypeId() != -1) ? Integer.toString(phones[a].getPhoneTypeId()) : "");
					this.fieldValues.put(workPhoneFieldName, (phones[a].getNumber() != null) ? phones[a].getNumber() : "");
				} else if (a == 2) {
					this.fieldValues.put(mobilePhoneMenuName, (phones[a].getPhoneTypeId() != -1) ? Integer.toString(phones[a].getPhoneTypeId()) : "");
					this.fieldValues.put(mobilePhoneFieldName, (phones[a].getNumber() != null) ? phones[a].getNumber() : "");
				} else if (a == 3) {
					this.fieldValues.put(faxPhoneMenuName, (phones[a].getPhoneTypeId() != -1) ? Integer.toString(phones[a].getPhoneTypeId()) : "");
					this.fieldValues.put(faxPhoneFieldName, (phones[a].getNumber() != null) ? phones[a].getNumber() : "");
				}
			}
			if (mail != null) {
				this.fieldValues.put(emailFieldName, (mail.getEmailAddress() != null) ? mail.getEmailAddress() : "");
			}
			if (homepage != null) {
				this.fieldValues.put(homepageFieldName, homepage);
			}
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