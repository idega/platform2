package is.idega.idegaweb.member.presentation;

import java.util.Hashtable;

import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CountryDropdownMenu;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.PostalCodeDropdownMenu;
import com.idega.presentation.ui.StyledButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.presentation.PostalCodeEditorWindow;
import com.idega.user.presentation.UserGroupTab;

/**
 *@author     <a href="mailto:thomas@idega.is">Thomas Hilbig</a>
 *@version    1.0
 */
public class GroupOfficeAddressTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "grp_oaddr_tab_name";
	private static final String DEFAULT_TAB_NAME = "Address";
	
	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String HELP_TEXT_KEY = "group_office_address_tab";

	private TextInput streetField;
	private TextInput cityField;
	private TextInput provinceField;
	private PostalCodeDropdownMenu postalCodeField;
	private CountryDropdownMenu countryField;
	private TextInput poBoxField;

	private static final String streetFieldName = "UMstreet";
	private static final String cityFieldName = "UMcity";
	private static final String provinceFieldName = "UMprovince";
	private static final String postalCodeFieldName = PostalCodeDropdownMenu.IW_POSTAL_CODE_MENU_PARAM_NAME;
	private static final String countryFieldName = "UMcountry";
	private static final String poBoxFieldName = "UMpoBox";

	private Text streetText;
	private Text cityText;
	private Text provinceText;
	private Text postalCodeText;
	private Text countryText;
	private Text poBoxText;

	public GroupOfficeAddressTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	public GroupOfficeAddressTab(Group group) {
		this();
		// do not store the group because this tab instance will be also used by other groups
		// (see setGroupId() !)
		setGroupId(((Integer) group.getPrimaryKey()).intValue());
	}

	public void initializeFieldNames() {

	}

	public void initializeFieldValues() {
		if (fieldValues == null)
			fieldValues = new Hashtable();
	}

	public void updateFieldsDisplayStatus() {
		String street = (String) fieldValues.get(streetFieldName);
		String city = (String) fieldValues.get(cityFieldName);
		String province = (String) fieldValues.get(provinceFieldName);
		String postalId = (String) fieldValues.get(postalCodeFieldName);
		String countryId = (String) fieldValues.get(countryFieldName);
		String poBox = (String) fieldValues.get(poBoxFieldName);

		if (street != null)
			streetField.setContent(street);
		if (city != null)
			cityField.setContent(city);
		if (province != null)
			provinceField.setContent(province);

		if (postalId != null && !postalId.equals(""))
			postalCodeField.setSelectedElement(Integer.parseInt(postalId));

		if(countryId!=null && !countryId.equals("") ){
			countryField.setSelectedElement(countryId);	
		}
			
			
			
		if (poBox != null)
			poBoxField.setContent(poBox);
	}

	public void initializeFields() {
		streetField = new TextInput(streetFieldName);
		streetField.setLength(20);

		cityField = new TextInput(cityFieldName);
		cityField.setLength(20);

		provinceField = new TextInput(provinceFieldName);
		provinceField.setLength(20);

		//only works for Iceland
		if (postalCodeField == null) {
			postalCodeField = new PostalCodeDropdownMenu();
			postalCodeField.setCountry("Iceland"); //hack
		}

		countryField = new CountryDropdownMenu(countryFieldName);
		countryField.setDisabled(true);
		countryField.setSelectedCountry("Iceland"); //TODO remove hack

		poBoxField = new TextInput(poBoxFieldName);
		poBoxField.setLength(10);
	}

	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		streetText = new Text(iwrb.getLocalizedString(streetFieldName,"Street"));
		streetText.setBold();

		cityText = new Text(iwrb.getLocalizedString(cityFieldName,"City"));
		cityText.setBold();

		provinceText = new Text(iwrb.getLocalizedString(provinceFieldName,"Province"));
		provinceText.setBold();

		postalCodeText = new Text(iwrb.getLocalizedString(postalCodeFieldName,"Postal"));
		postalCodeText.setBold();

		countryText = new Text(iwrb.getLocalizedString(countryFieldName,"Country"));
		countryText.setBold();

		poBoxText = new Text(iwrb.getLocalizedString(poBoxFieldName,"P.O.Box"));
		poBoxText.setBold();
	}

	public void lineUpFields() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		
		resize(1, 1);

		Table table = new Table();
		table.setWidth("100%");
		table.setCellpadding(5);
		table.setCellspacing(0);

		table.add(streetText, 1, 1);
		table.add(streetField, 1, 1);
		table.add(cityText, 2, 1);
		table.add(cityField, 2, 1);
		table.add(provinceText, 2, 2);
		table.add(provinceField, 2, 2);
		table.add(countryText, 1, 3);
		table.add(countryField, 1, 3);

		add(table);
		//    fpane.add(addressTable);
		table.add(postalCodeText, 2, 3);
		Table postalTable = new Table();
		postalTable.setCellpaddingAndCellspacing(0);
		postalTable.add(postalCodeField,1,1);
		table.add(postalTable, 2, 3);
		GenericButton addPostal = new GenericButton("add_postal", iwrb.getLocalizedString("GroupOfficeAddressTab.postalcodewindow.add","Add"));
		addPostal.setWindowToOpen(PostalCodeEditorWindow.class);
		StyledButton button = new StyledButton(addPostal);
		postalTable.setWidth(2, 3);
		postalTable.add(button, 3, 1);
				
		table.add(poBoxText, 1, 4);
		table.add(poBoxField, 1, 4);
	}

	public void main(IWContext iwc) {
		getPanel().addHelpButton(getHelpButton());		
	}

	public boolean collect(IWContext iwc) {

		if (iwc != null) {
			String street = iwc.getParameter(streetFieldName);
			String city = iwc.getParameter(cityFieldName);
			String province = iwc.getParameter(provinceFieldName);
			String postal = iwc.getParameter(postalCodeFieldName);
			String country = iwc.getParameter(countryFieldName);
			String poBox = iwc.getParameter(poBoxFieldName);

			if (street != null) {
				fieldValues.put(streetFieldName, street);
			}
			if (city != null) {
				fieldValues.put(cityFieldName, city);
			}
			if (province != null) {
				fieldValues.put(provinceFieldName, province);
			}
			if (postal != null) {
				fieldValues.put(postalCodeFieldName, postal);
			}
			if (country != null) {
				fieldValues.put(countryFieldName, country);
			}
			if (poBox != null) {
				fieldValues.put(poBoxFieldName, poBox);
			}

			updateFieldsDisplayStatus();

			return true;
		}
		return false;
	}

	public boolean store(IWContext iwc) {

		Integer groupId = new Integer(getGroupId());
		String street = iwc.getParameter(streetFieldName);

		if (street != null) {
			try {
				Integer postalCodeId = null;
				String postal = iwc.getParameter(postalCodeFieldName);
				if (postal != null)
					postalCodeId = new Integer(postal);
				String country = iwc.getParameter(countryFieldName);
				String city = iwc.getParameter(cityFieldName);
				String province = iwc.getParameter(provinceFieldName);
				String poBox = iwc.getParameter(poBoxFieldName);

				getGroupBusiness(iwc).updateGroupMainAddressOrCreateIfDoesNotExist(groupId, street, postalCodeId, country, city, province, poBox);

			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;

	}

	public void initFieldContents() {
		try {
			GroupBusiness groupBiz = getGroupBusiness(getEventIWContext());
			Group group = groupBiz.getGroupByGroupID(getGroupId());
			Address addr = groupBiz.getGroupMainAddress(group);

			boolean hasAddress = false;
			if (addr != null) {
				hasAddress = true;
			}

			if (hasAddress) {
				/** @todo remove this fieldValues bullshit!**/
				String street = addr.getStreetAddress();
				int code = addr.getPostalCodeID();
				Country country = addr.getCountry();
				String countryName = null;
				if (country != null)
					countryName = country.getName();
				String city = addr.getCity();
				String province = addr.getProvince();
				String poBox = addr.getPOBox();

				if (street != null)
					fieldValues.put(streetFieldName, street);
				if (city != null)
					fieldValues.put(cityFieldName, city);
				if (province != null)
					fieldValues.put(provinceFieldName, province);
				if (code != -1)
					fieldValues.put(postalCodeFieldName, String.valueOf(code));
				if (countryName != null)
					fieldValues.put(countryFieldName, countryName);
				if (poBox != null)
					fieldValues.put(poBoxFieldName, poBox);
			}

			updateFieldsDisplayStatus();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("AddressInfoTab error initFieldContents, groupId : " + getGroupId());
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
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}	
} // Class AddressInfoTab