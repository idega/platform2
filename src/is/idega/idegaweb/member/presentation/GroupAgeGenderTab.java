package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusiness;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.IntegerInput;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.presentation.UserGroupTab;

/**
 * @author <a href="mailto:thomas@idega.is">Thomas Hilbig </a>
 * @version 1.0
 */
public class GroupAgeGenderTab extends UserGroupTab {

	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "age_tab_name";

	private static final String DEFAULT_TAB_NAME = "Age/Gender";

	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	private static final String HELP_TEXT_KEY = "group_age_gender_tab";

	// field
	private CheckBox maleField;

	private CheckBox femaleField;

	private IntegerInput lowerAgeLimitField;

	private IntegerInput upperAgeLimitField;

	private CheckBox ageLimitIsStringentConditionField;

	private DateInput keyDateForAgeField;

	private Text lowerAgeTooSmallField;

	private Text upperAgeTooLargeField;

	private Text lowerAgeGreaterThanUpperAgeField;

	// text
	private Text femaleText;

	private Text maleText;

	private Text lowerAgeLimitText;

	private Text upperAgeLimitText;

	private Text ageLimitIsStringentConditionText;

	private Text keyDateForAgeText;

	// error text
	private String lowerAgeTooSmallError;

	private String upperAgeTooLargeError;

	private String lowerAgeGreaterThanUpperAgeError;

	// special error variables
	private boolean lowerAgeTooSmall = false;

	private boolean upperAgeTooLarge = false;

	private boolean lowerAgeGreaterThanUpperAge = false;

	// field name
	private String maleFieldName;

	private String femaleFieldName;

	private String lowerAgeLimitFieldName;

	private String upperAgeLimitFieldName;

	private String ageLimitIsStringentConditionFieldName;

	private String keyDateForAgeFieldName;

	private String lowerAgeTooSmallFieldName;

	private String upperAgeTooLargeFieldName;

	private String lowerAgeGreaterThanUpperAgeFieldName;

	public GroupAgeGenderTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));

		//    setName("Age/Gender");
	}

	public GroupAgeGenderTab(Group group) {
		this();
		// do not store the group because this tab instance will be also used by
		// other groups
		// (see setGroupId() !)
		setGroupId(((Integer) group.getPrimaryKey()).intValue());

	}

	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldNames()
	 */
	public void initializeFieldNames() {
		maleFieldName = "age_male";
		femaleFieldName = "age_female";
		lowerAgeLimitFieldName = "age_lowerAgeLimitField";
		upperAgeLimitFieldName = "age_upperAgeLimitField";
		ageLimitIsStringentConditionFieldName = "age_ageLimitIsStringentConditionFieldName";
		keyDateForAgeFieldName = "age_keyDateForAgeFieldName";
		lowerAgeTooSmallFieldName = "age_lowerAgeTooSmallField";
		upperAgeTooLargeFieldName = "age_upperAgeTooLargeField";
		lowerAgeGreaterThanUpperAgeFieldName = "age_lowerAgeGreaterThanUpperAgeField";
	}

	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
		fieldValues.put(maleFieldName, new Boolean(false));
		fieldValues.put(femaleFieldName, new Boolean(false));
		fieldValues.put(lowerAgeLimitFieldName, new Integer(0));
		fieldValues.put(upperAgeLimitFieldName, new Integer(0));
		fieldValues.put(ageLimitIsStringentConditionFieldName, new Boolean(false));
		fieldValues.put(keyDateForAgeFieldName, "");
		// error fields
		fieldValues.put(lowerAgeTooSmallFieldName, "");
		fieldValues.put(upperAgeTooLargeFieldName, "");
		fieldValues.put(lowerAgeGreaterThanUpperAgeFieldName, "");
	}

	/**
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		femaleField.setChecked(((Boolean) fieldValues.get(femaleFieldName)).booleanValue());
		maleField.setChecked(((Boolean) fieldValues.get(maleFieldName)).booleanValue());
		lowerAgeLimitField.setContent(((Integer) fieldValues.get(lowerAgeLimitFieldName)).toString());
		upperAgeLimitField.setContent(((Integer) fieldValues.get(upperAgeLimitFieldName)).toString());

		ageLimitIsStringentConditionField.setChecked(((Boolean) fieldValues.get(ageLimitIsStringentConditionFieldName)).booleanValue());
		StringTokenizer keyDate = new StringTokenizer((String) fieldValues.get(keyDateForAgeFieldName), " -");

		if (keyDate.hasMoreTokens()) {
			keyDateForAgeField.setMonth(keyDate.nextToken());
		}
		else {
			keyDateForAgeField.setMonth(-1);
		}
		if (keyDate.hasMoreTokens()) {
			keyDateForAgeField.setDay(keyDate.nextToken());
		}
		else {
			keyDateForAgeField.setDay(-1);
		}
		// error fields
		lowerAgeTooSmallField.setText((String) fieldValues.get(lowerAgeTooSmallFieldName));
		upperAgeTooLargeField.setText((String) fieldValues.get(upperAgeTooLargeFieldName));
		lowerAgeGreaterThanUpperAgeField.setText((String) fieldValues.get(lowerAgeGreaterThanUpperAgeFieldName));
	}

	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
		femaleField = new CheckBox(femaleFieldName);
		femaleField.setWidth("10");
		femaleField.setHeight("10");
		maleField = new CheckBox(maleFieldName);
		maleField.setWidth("10");
		maleField.setHeight("10");

		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		String integerErrorWarning = iwrb.getLocalizedString("age_intError", "The input must be greater or equal zero");
		String notEmpty = iwrb.getLocalizedString("age_notEmpty", "Please fill in every field");

		lowerAgeLimitField = new IntegerInput(lowerAgeLimitFieldName, integerErrorWarning);
		upperAgeLimitField = new IntegerInput(upperAgeLimitFieldName, integerErrorWarning);

		lowerAgeLimitField.setSize(3);
		upperAgeLimitField.setSize(3);

		lowerAgeLimitField.setMaxlength(3);
		upperAgeLimitField.setMaxlength(3);

		lowerAgeLimitField.setAsNotEmpty(notEmpty);
		upperAgeLimitField.setAsNotEmpty(notEmpty);

		ageLimitIsStringentConditionField = new CheckBox(ageLimitIsStringentConditionFieldName);
		ageLimitIsStringentConditionField.setWidth("10");
		ageLimitIsStringentConditionField.setHeight("10");

		keyDateForAgeField = new DateInput(keyDateForAgeFieldName, false, false);
		// do not show the year
		keyDateForAgeField.setToShowYear(false);

		// error fields
		lowerAgeTooSmallField = new Text();
		lowerAgeTooSmallField.setFontColor("#FF0000");

		upperAgeTooLargeField = new Text();
		upperAgeTooLargeField.setFontColor("#FF0000");

		lowerAgeGreaterThanUpperAgeField = new Text();
		lowerAgeGreaterThanUpperAgeField.setFontColor("#FF0000");

	}

	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle bundle = getResourceBundle(iwc);

		femaleText = new Text(bundle.getLocalizedString(femaleFieldName, "female members"));
		femaleText.setBold();
		
		maleText = new Text(bundle.getLocalizedString(maleFieldName, "male members"));
		maleText.setBold();
		
		lowerAgeLimitText = new Text(bundle.getLocalizedString(lowerAgeLimitFieldName, "Lower age limit"));
		lowerAgeLimitText.setBold();
		
		upperAgeLimitText = new Text(bundle.getLocalizedString(upperAgeLimitFieldName, "Upper age limit"));
		upperAgeLimitText.setBold();
		
		ageLimitIsStringentConditionText = new Text(bundle.getLocalizedString(ageLimitIsStringentConditionFieldName, "Age limits are stringent conditions"));
		ageLimitIsStringentConditionText.setBold();
		
		keyDateForAgeText = new Text(bundle.getLocalizedString(keyDateForAgeFieldName, "Key date for age"));
		keyDateForAgeText.setBold();
		
		lowerAgeTooSmallError = bundle.getLocalizedString(lowerAgeTooSmallFieldName, "Lower age limit is too small");
		upperAgeTooLargeError = bundle.getLocalizedString(upperAgeTooLargeFieldName, "Upper age limit is too large");
		lowerAgeGreaterThanUpperAgeError = bundle.getLocalizedString(lowerAgeGreaterThanUpperAgeFieldName, "Lower age is greater than upper age");
	}

	/**
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		Table table = new Table(2, 10);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(5);
		table.setCellspacing(0);
		
		table.add(lowerAgeLimitText, 1, 1);
		table.add(Text.getBreak(), 1, 1);
		table.add(lowerAgeLimitField, 1, 1);
		
		table.add(upperAgeLimitText, 2, 1);
		table.add(Text.getBreak(), 2, 1);
		table.add(upperAgeLimitField, 2, 1);
		
		table.add(keyDateForAgeText, 1, 2);
		table.add(Text.getBreak(), 1, 2);
		table.add(keyDateForAgeField, 1, 2);
		
		table.mergeCells(1, 3, 2, 3);
		table.add(femaleText, 1, 3);
		table.add(femaleField, 1, 3);
		
		table.mergeCells(1, 4, 2, 4);
		table.add(maleText, 1, 4);
		table.add(maleField, 1, 4);
		
		table.mergeCells(1, 5, 2, 5);
		table.add(ageLimitIsStringentConditionText, 1, 5);
		table.add(ageLimitIsStringentConditionField, 1, 5);
		
		// error fields
		table.add(lowerAgeTooSmallField, 1, 6);
		table.add(upperAgeTooLargeField, 1, 6);
		table.add(lowerAgeGreaterThanUpperAgeField, 1, 6);
		add(table);
	}

	public void main(IWContext iwc) {
		getPanel().addHelpButton(getHelpButton());		
	}

	/**
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {

		if (iwc != null) {
			String female = iwc.getParameter(femaleFieldName);
			String male = iwc.getParameter(maleFieldName);
			String lowerAgeLimit = iwc.getParameter(lowerAgeLimitFieldName);
			String upperAgeLimit = iwc.getParameter(upperAgeLimitFieldName);
			String ageLimitIsStringentCondition = iwc.getParameter(ageLimitIsStringentConditionFieldName);

			String keyDate = iwc.getParameter(keyDateForAgeFieldName);
			// only modify key date if month and day is set by the user.
			// not selected is indicated by -1.
			// key date = "year-month-day"
			// year is always not selected.
			// e.g:
			// "-1-03-11" changes to "03-11"
			// "-1--1-12 (month is not selected) changes to ""
			// "-1--09--23 changes to ""
			// "-1-07--30 changes to ""
			if ((keyDate != null) && (keyDate.length() != 0) && keyDate.indexOf("--") == -1) {
				// month and day are selected
				int i = keyDate.indexOf("-", 1); // 1 in order to skip the year
				keyDate = keyDate.substring(++i);
			}
			else {
				keyDate = "";
			}
			fieldValues.put(keyDateForAgeFieldName, keyDate);

			fieldValues.put(ageLimitIsStringentConditionFieldName, new Boolean(ageLimitIsStringentCondition != null));

			fieldValues.put(femaleFieldName, new Boolean(female != null));
			fieldValues.put(maleFieldName, new Boolean(male != null));

			if (lowerAgeLimit != null) {
				fieldValues.put(lowerAgeLimitFieldName, new Integer(lowerAgeLimit));
			}
			if (upperAgeLimit != null) {
				fieldValues.put(upperAgeLimitFieldName, new Integer(upperAgeLimit));
			}
			try {
				// get corressponding service bean
				AgeGenderPluginBusiness ageGenderPluginBusiness = getAgeGenderPluginBusiness(iwc);

				// validate upper and lower age limit
				int lowerAge = ((Integer) fieldValues.get(lowerAgeLimitFieldName)).intValue();
				int upperAge = ((Integer) fieldValues.get(upperAgeLimitFieldName)).intValue();
				lowerAgeTooSmall = (lowerAge < ageGenderPluginBusiness.getLowerAgeLimitDefault());
				upperAgeTooLarge = (upperAge > ageGenderPluginBusiness.getUpperAgeLimitDefault());
				lowerAgeGreaterThanUpperAge = (lowerAge > upperAge);
				// set error text if necessary
				fieldValues.put(lowerAgeTooSmallFieldName, ((lowerAgeTooSmall) ? lowerAgeTooSmallError : ""));
				fieldValues.put(upperAgeTooLargeFieldName, ((upperAgeTooLarge) ? upperAgeTooLargeError : ""));
				fieldValues.put(lowerAgeGreaterThanUpperAgeFieldName, ((lowerAgeGreaterThanUpperAge) ? lowerAgeGreaterThanUpperAgeError : ""));
			}
			catch (RemoteException ex) {
				System.err.println("[GroupAgeGenderTab] Problem to get the AgeGenderPluginBusiness bean: " + ex.getMessage());
				ex.printStackTrace(System.err);
			}
			this.updateFieldsDisplayStatus();

			return true;
		}
		return false;
	}

	/**
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {

		Group group;
		try {
			group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));
			// get corressponding service bean
			AgeGenderPluginBusiness ageGenderPluginBusiness = getAgeGenderPluginBusiness(iwc);

			// validate upper and lower age limit
			int lowerAge = ((Integer) fieldValues.get(lowerAgeLimitFieldName)).intValue();
			int upperAge = ((Integer) fieldValues.get(upperAgeLimitFieldName)).intValue();
			lowerAgeTooSmall = (lowerAge < ageGenderPluginBusiness.getLowerAgeLimitDefault());
			upperAgeTooLarge = (upperAge > ageGenderPluginBusiness.getUpperAgeLimitDefault());
			lowerAgeGreaterThanUpperAge = (lowerAge > upperAge);
			if (lowerAgeTooSmall || upperAgeTooLarge || lowerAgeGreaterThanUpperAge)
				return false;

			ageGenderPluginBusiness.setLowerAgeLimit(group, lowerAge);
			ageGenderPluginBusiness.setUpperAgeLimit(group, upperAge);
			// set gender
			boolean isFemale = ((Boolean) fieldValues.get(femaleFieldName)).booleanValue();
			boolean isMale = ((Boolean) fieldValues.get(maleFieldName)).booleanValue();
			if (isMale && !isFemale)
				ageGenderPluginBusiness.setMale(group);
			else if (isFemale && !isMale)
				ageGenderPluginBusiness.setFemale(group);
			else
				// male and female are either both true or both false
				ageGenderPluginBusiness.setNeutral(group);

			boolean ageLimitIsStringentCondition = ((Boolean) fieldValues.get(ageLimitIsStringentConditionFieldName)).booleanValue();
			ageGenderPluginBusiness.setAgeLimitIsStringentCondition(group, ageLimitIsStringentCondition);

			String keyDateForAge = (String) fieldValues.get(keyDateForAgeFieldName);
			ageGenderPluginBusiness.setKeyDateForAge(group, keyDateForAge);

			group.store();
		}
		catch (RemoteException e) {
			System.err.println("[GeneralGroupInfoTab] remote error store, GroupId : " + getGroupId());
			e.printStackTrace(System.err);
			return false;
		}
		catch (FinderException e) {
			System.err.println("[GeneralGroupInfoTab] find error store, GroupId : " + getGroupId());
			e.printStackTrace(System.err);
			return false;
		}
		return true;
	}

	/**
	 * @see com.idega.user.presentation.UserGroupTab#initFieldContents()
	 */
	public void initFieldContents() {
		// get group by group id
		Group group;
		try {
			group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));

			// get corressponding service bean
			AgeGenderPluginBusiness ageGenderPluginBusiness = getAgeGenderPluginBusiness(this.getEventIWContext());
			// set gender radio buttons

			// isMale, isFemale throws RemoteException and FinderException
			boolean isFemale = ageGenderPluginBusiness.isFemale(group);
			boolean isMale = ageGenderPluginBusiness.isMale(group);

			// if isFemale and isMale are both false then the gender is neuter
			// in this case show both checkboxes as checked
			if (!isFemale && !isMale) {
				isFemale = true;
				isMale = true;
			}
			fieldValues.put(femaleFieldName, new Boolean(isFemale));
			fieldValues.put(maleFieldName, new Boolean(isMale));

			// get lower age limit
			int lowerAgeLimit = ageGenderPluginBusiness.getLowerAgeLimit(group);
			fieldValues.put(lowerAgeLimitFieldName, new Integer(lowerAgeLimit));
			// get upper age limit
			int upperAgeLimit = ageGenderPluginBusiness.getUpperAgeLimit(group);
			fieldValues.put(upperAgeLimitFieldName, new Integer(upperAgeLimit));

			boolean ageLimitIsStringentCondition = ageGenderPluginBusiness.isAgeLimitStringentCondition(group);
			fieldValues.put(ageLimitIsStringentConditionFieldName, new Boolean(ageLimitIsStringentCondition));

			String keyDateForAge = ageGenderPluginBusiness.getKeyDateForAge(group);
			fieldValues.put(keyDateForAgeFieldName, keyDateForAge);

		}
		catch (RemoteException e) {
			System.err.println("[GeneralGroupInfoTab] remote error initFieldContents, GroupId : " + getGroupId());
			e.printStackTrace(System.err);
			return;
		}
		catch (FinderException e) {
			System.err.println("[GeneralGroupInfoTab] find error initFieldContents, GroupId : " + getGroupId());
			e.printStackTrace(System.err);
			return;
		}
		this.updateFieldsDisplayStatus();

	}

	public AgeGenderPluginBusiness getAgeGenderPluginBusiness(IWApplicationContext iwc) {
		AgeGenderPluginBusiness business = null;
		if (business == null) {
			try {
				business = (AgeGenderPluginBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, AgeGenderPluginBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return business;
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

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}