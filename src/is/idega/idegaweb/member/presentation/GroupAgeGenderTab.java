package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusiness;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.ejb.FinderException;
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
	
	//EXTRA ISI STUFF
	private CheckBox nationalityDependentField;
	private CheckBox clubMemberExchangeDependentField;
	
	// text
	private Text femaleText;
	private Text maleText;
	private Text lowerAgeLimitText;
	private Text upperAgeLimitText;
	private Text ageLimitIsStringentConditionText;
	private Text keyDateForAgeText;
	
	//EXTRA ISI STUFF
	private Text nationalityDependentText;
	private Text clubMemberExchangeDependentText;
	
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
	//EXTRA ISI STUFF
	private String nationalityDependentFieldName;
	private String clubMemberExchangeDependentFieldName;

	public GroupAgeGenderTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
		// setName("Age/Gender");
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
		this.maleFieldName = "age_male";
		this.femaleFieldName = "age_female";
		this.lowerAgeLimitFieldName = "age_lowerAgeLimitField";
		this.upperAgeLimitFieldName = "age_upperAgeLimitField";
		this.ageLimitIsStringentConditionFieldName = "age_ageLimitIsStringentConditionFieldName";
		this.keyDateForAgeFieldName = "age_keyDateForAgeFieldName";
		this.lowerAgeTooSmallFieldName = "age_lowerAgeTooSmallField";
		this.upperAgeTooLargeFieldName = "age_upperAgeTooLargeField";
		this.lowerAgeGreaterThanUpperAgeFieldName = "age_lowerAgeGreaterThanUpperAgeField";
		//EXTRA ISI STUFF
		this.nationalityDependentFieldName = "nationalityDependent";
		this.clubMemberExchangeDependentFieldName = "clubMemberExchangeDependent";
	}

	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		this.fieldValues = new Hashtable();
		this.fieldValues.put(this.maleFieldName, new Boolean(false));
		this.fieldValues.put(this.femaleFieldName, new Boolean(false));
		this.fieldValues.put(this.lowerAgeLimitFieldName, new Integer(0));
		this.fieldValues.put(this.upperAgeLimitFieldName, new Integer(0));
		this.fieldValues.put(this.ageLimitIsStringentConditionFieldName, new Boolean(false));
		this.fieldValues.put(this.keyDateForAgeFieldName, "");
		// error fields
		this.fieldValues.put(this.lowerAgeTooSmallFieldName, "");
		this.fieldValues.put(this.upperAgeTooLargeFieldName, "");
		this.fieldValues.put(this.lowerAgeGreaterThanUpperAgeFieldName, "");
		
		//EXTRA ISI STUFF
		this.fieldValues.put(this.nationalityDependentFieldName, new Boolean(false));
		this.fieldValues.put(this.clubMemberExchangeDependentFieldName, new Boolean(false));
	}

	/**
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		this.femaleField.setChecked(((Boolean) this.fieldValues.get(this.femaleFieldName)).booleanValue());
		this.maleField.setChecked(((Boolean) this.fieldValues.get(this.maleFieldName)).booleanValue());
		this.lowerAgeLimitField.setContent(((Integer) this.fieldValues.get(this.lowerAgeLimitFieldName)).toString());
		this.upperAgeLimitField.setContent(((Integer) this.fieldValues.get(this.upperAgeLimitFieldName)).toString());
		this.ageLimitIsStringentConditionField.setChecked(((Boolean) this.fieldValues.get(this.ageLimitIsStringentConditionFieldName)).booleanValue());
		StringTokenizer keyDate = new StringTokenizer((String) this.fieldValues.get(this.keyDateForAgeFieldName), " -");
		if (keyDate.hasMoreTokens()) {
			this.keyDateForAgeField.setMonth(keyDate.nextToken());
		}
		else {
			this.keyDateForAgeField.setMonth(-1);
		}
		if (keyDate.hasMoreTokens()) {
			this.keyDateForAgeField.setDay(keyDate.nextToken());
		}
		else {
			this.keyDateForAgeField.setDay(-1);
		}
		// error fields
		this.lowerAgeTooSmallField.setText((String) this.fieldValues.get(this.lowerAgeTooSmallFieldName));
		this.upperAgeTooLargeField.setText((String) this.fieldValues.get(this.upperAgeTooLargeFieldName));
		this.lowerAgeGreaterThanUpperAgeField.setText((String) this.fieldValues.get(this.lowerAgeGreaterThanUpperAgeFieldName));
		
		//EXTRA ISI STUFF
		this.nationalityDependentField.setChecked(((Boolean) this.fieldValues.get(this.nationalityDependentFieldName)).booleanValue());
		this.clubMemberExchangeDependentField.setChecked(((Boolean) this.fieldValues.get(this.clubMemberExchangeDependentFieldName)).booleanValue());
	
	}

	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
		this.femaleField = new CheckBox(this.femaleFieldName);
		this.femaleField.setWidth("10");
		this.femaleField.setHeight("10");
		this.maleField = new CheckBox(this.maleFieldName);
		this.maleField.setWidth("10");
		this.maleField.setHeight("10");
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		String integerErrorWarning = iwrb.getLocalizedString("age_intError", "The input must be greater or equal zero");
		String notEmpty = iwrb.getLocalizedString("age_notEmpty", "Please fill in every field");
		this.lowerAgeLimitField = new IntegerInput(this.lowerAgeLimitFieldName, integerErrorWarning);
		this.upperAgeLimitField = new IntegerInput(this.upperAgeLimitFieldName, integerErrorWarning);
		this.lowerAgeLimitField.setSize(3);
		this.upperAgeLimitField.setSize(3);
		this.lowerAgeLimitField.setMaxlength(3);
		this.upperAgeLimitField.setMaxlength(3);
		this.lowerAgeLimitField.setAsNotEmpty(notEmpty);
		this.upperAgeLimitField.setAsNotEmpty(notEmpty);
		this.ageLimitIsStringentConditionField = new CheckBox(this.ageLimitIsStringentConditionFieldName);
		this.ageLimitIsStringentConditionField.setWidth("10");
		this.ageLimitIsStringentConditionField.setHeight("10");
		this.keyDateForAgeField = new DateInput(this.keyDateForAgeFieldName, false, false);
		// do not show the year
		this.keyDateForAgeField.setToShowYear(false);
		// error fields
		this.lowerAgeTooSmallField = new Text();
		this.lowerAgeTooSmallField.setFontColor("#FF0000");
		this.upperAgeTooLargeField = new Text();
		this.upperAgeTooLargeField.setFontColor("#FF0000");
		this.lowerAgeGreaterThanUpperAgeField = new Text();
		this.lowerAgeGreaterThanUpperAgeField.setFontColor("#FF0000");
		
		//EXTRA ISI STUFF
		this.nationalityDependentField = new CheckBox(this.nationalityDependentFieldName);
		this.nationalityDependentField.setWidth("10");
		this.nationalityDependentField.setHeight("10");
		
		this.clubMemberExchangeDependentField = new CheckBox(this.clubMemberExchangeDependentFieldName);
		this.clubMemberExchangeDependentField.setWidth("10");
		this.clubMemberExchangeDependentField.setHeight("10");
		
	}

	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle bundle = getResourceBundle(iwc);
		this.femaleText = new Text(bundle.getLocalizedString(this.femaleFieldName, "female members"));
		this.femaleText.setBold();
		this.maleText = new Text(bundle.getLocalizedString(this.maleFieldName, "male members"));
		this.maleText.setBold();
		this.lowerAgeLimitText = new Text(bundle.getLocalizedString(this.lowerAgeLimitFieldName, "Lower age limit"));
		this.lowerAgeLimitText.setBold();
		this.upperAgeLimitText = new Text(bundle.getLocalizedString(this.upperAgeLimitFieldName, "Upper age limit"));
		this.upperAgeLimitText.setBold();
		this.ageLimitIsStringentConditionText = new Text(bundle.getLocalizedString(this.ageLimitIsStringentConditionFieldName,
				"Age limits are stringent conditions"));
		this.ageLimitIsStringentConditionText.setBold();
		this.keyDateForAgeText = new Text(bundle.getLocalizedString(this.keyDateForAgeFieldName, "Key date for age"));
		this.keyDateForAgeText.setBold();
		this.lowerAgeTooSmallError = bundle.getLocalizedString(this.lowerAgeTooSmallFieldName, "Lower age limit is too small");
		this.upperAgeTooLargeError = bundle.getLocalizedString(this.upperAgeTooLargeFieldName, "Upper age limit is too large");
		this.lowerAgeGreaterThanUpperAgeError = bundle.getLocalizedString(this.lowerAgeGreaterThanUpperAgeFieldName,
				"Lower age is greater than upper age");
		
		//EXTRA ISI STUFF
		this.nationalityDependentText = new Text(bundle.getLocalizedString(this.nationalityDependentFieldName, "Nationality dependent"));
		this.nationalityDependentText.setBold();
		this.clubMemberExchangeDependentText = new Text(bundle.getLocalizedString(this.clubMemberExchangeDependentFieldName, "Club exchange dependent"));
		this.clubMemberExchangeDependentText.setBold();
	}

	/**
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		Table table = new Table(2, 7);
		table.setWidth(300);
		table.setCellpadding(5);
		table.setCellspacing(0);
		table.add(this.lowerAgeLimitText, 1, 1);
		table.add(Text.getBreak(), 1, 1);
		table.add(this.lowerAgeLimitField, 1, 1);
		table.add(this.upperAgeLimitText, 2, 1);
		table.add(Text.getBreak(), 2, 1);
		table.add(this.upperAgeLimitField, 2, 1);
		table.add(this.keyDateForAgeText, 1, 2);
		table.add(Text.getBreak(), 1, 2);
		table.add(this.keyDateForAgeField, 1, 2);
		
		//table.mergeCells(1, 3, 2, 3);
		table.add(this.femaleText, 1, 3);
		table.add(this.femaleField, 1, 3);
		//table.mergeCells(1, 4, 2, 4);
		table.add(this.maleText, 1, 4);
		table.add(this.maleField, 1, 4);
		//table.mergeCells(1, 5, 2, 5);
		table.add(this.ageLimitIsStringentConditionText, 1, 5);
		table.add(this.ageLimitIsStringentConditionField, 1, 5);
		// error fields
		table.add(this.lowerAgeTooSmallField, 1, 6);
		table.add(this.upperAgeTooLargeField, 1, 6);
		table.add(this.lowerAgeGreaterThanUpperAgeField, 1, 6);
		
		
		//EXTRA ISI STUFF
		table.add(this.nationalityDependentText, 2, 3);
		table.add(this.nationalityDependentField, 2, 3);
		
		table.add(this.clubMemberExchangeDependentText, 2, 4);
		table.add(this.clubMemberExchangeDependentField, 2, 4);
		
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
			Group group;
			try {
				group = (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(
						getGroupId())));
				// special case because the age and gender stuff should be
				// controlled by the club member template group
				// for other group types it is never read only
				boolean readOnly = IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(group.getGroupType());
				if (!readOnly) {
					String female = iwc.getParameter(this.femaleFieldName);
					String male = iwc.getParameter(this.maleFieldName);
					String lowerAgeLimit = iwc.getParameter(this.lowerAgeLimitFieldName);
					String upperAgeLimit = iwc.getParameter(this.upperAgeLimitFieldName);
					String ageLimitIsStringentCondition = iwc.getParameter(this.ageLimitIsStringentConditionFieldName);
					String keyDate = iwc.getParameter(this.keyDateForAgeFieldName);
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
						int i = keyDate.indexOf("-", 1); // 1 in order to
															// skip the year
						keyDate = keyDate.substring(++i);
					}
					else {
						keyDate = "";
					}
					this.fieldValues.put(this.keyDateForAgeFieldName, keyDate);
					this.fieldValues.put(this.ageLimitIsStringentConditionFieldName, new Boolean(
							ageLimitIsStringentCondition != null));
					this.fieldValues.put(this.femaleFieldName, new Boolean(female != null));
					this.fieldValues.put(this.maleFieldName, new Boolean(male != null));
					if (lowerAgeLimit != null) {
						this.fieldValues.put(this.lowerAgeLimitFieldName, new Integer(lowerAgeLimit));
					}
					if (upperAgeLimit != null) {
						this.fieldValues.put(this.upperAgeLimitFieldName, new Integer(upperAgeLimit));
					}
					// get corressponding service bean
					AgeGenderPluginBusiness ageGenderPluginBusiness = getAgeGenderPluginBusiness(iwc);
					// validate upper and lower age limit
					int lowerAge = ((Integer) this.fieldValues.get(this.lowerAgeLimitFieldName)).intValue();
					int upperAge = ((Integer) this.fieldValues.get(this.upperAgeLimitFieldName)).intValue();
					this.lowerAgeTooSmall = (lowerAge < ageGenderPluginBusiness.getLowerAgeLimitDefault());
					this.upperAgeTooLarge = (upperAge > ageGenderPluginBusiness.getUpperAgeLimitDefault());
					this.lowerAgeGreaterThanUpperAge = (lowerAge > upperAge);
					// set error text if necessary
					this.fieldValues.put(this.lowerAgeTooSmallFieldName, ((this.lowerAgeTooSmall) ? this.lowerAgeTooSmallError : ""));
					this.fieldValues.put(this.upperAgeTooLargeFieldName, ((this.upperAgeTooLarge) ? this.upperAgeTooLargeError : ""));
					this.fieldValues.put(this.lowerAgeGreaterThanUpperAgeFieldName,
							((this.lowerAgeGreaterThanUpperAge) ? this.lowerAgeGreaterThanUpperAgeError : ""));
					
					
					//EXTRA ISI STUFF
					String nationalityDep = iwc.getParameter(this.nationalityDependentFieldName);
					String clubExchangeDep = iwc.getParameter(this.clubMemberExchangeDependentFieldName);
					this.fieldValues.put(this.nationalityDependentFieldName, new Boolean(nationalityDep != null));
					this.fieldValues.put(this.clubMemberExchangeDependentFieldName, new Boolean(clubExchangeDep != null));
					
					this.updateFieldsDisplayStatus();
				}
				return true;
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
		try {
			Group group = (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(
					getGroupId())));
			// special case because the age and gender stuff should be
			// controlled by the club member template group
			// for other group types it is never read only
			boolean readOnly = IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(group.getGroupType());
			if (!readOnly) {
				// get corressponding service bean
				AgeGenderPluginBusiness ageGenderPluginBusiness = getAgeGenderPluginBusiness(iwc);
				// validate upper and lower age limit
				int lowerAge = ((Integer) this.fieldValues.get(this.lowerAgeLimitFieldName)).intValue();
				int upperAge = ((Integer) this.fieldValues.get(this.upperAgeLimitFieldName)).intValue();
				this.lowerAgeTooSmall = (lowerAge < ageGenderPluginBusiness.getLowerAgeLimitDefault());
				this.upperAgeTooLarge = (upperAge > ageGenderPluginBusiness.getUpperAgeLimitDefault());
				this.lowerAgeGreaterThanUpperAge = (lowerAge > upperAge);
				if (this.lowerAgeTooSmall || this.upperAgeTooLarge || this.lowerAgeGreaterThanUpperAge) {
					return false;
				}
				ageGenderPluginBusiness.setLowerAgeLimit(group, lowerAge);
				ageGenderPluginBusiness.setUpperAgeLimit(group, upperAge);
				// set gender
				boolean isFemale = ((Boolean) this.fieldValues.get(this.femaleFieldName)).booleanValue();
				boolean isMale = ((Boolean) this.fieldValues.get(this.maleFieldName)).booleanValue();
				if (isMale && !isFemale) {
					ageGenderPluginBusiness.setMale(group);
				}
				else if (isFemale && !isMale) {
					ageGenderPluginBusiness.setFemale(group);
				}
				else {
					// male and female are either both true or both false
					ageGenderPluginBusiness.setNeutral(group);
				}
				boolean ageLimitIsStringentCondition = ((Boolean) this.fieldValues.get(this.ageLimitIsStringentConditionFieldName)).booleanValue();
				ageGenderPluginBusiness.setAgeLimitIsStringentCondition(group, ageLimitIsStringentCondition);
				String keyDateForAge = (String) this.fieldValues.get(this.keyDateForAgeFieldName);
				ageGenderPluginBusiness.setKeyDateForAge(group, keyDateForAge);
				
				//EXTRA ISI STUFF 
				//TODO move to ksi plugin
				boolean isNationalityDependent = ((Boolean) this.fieldValues.get(this.nationalityDependentFieldName)).booleanValue();
				ageGenderPluginBusiness.setNationalityDependent(group,isNationalityDependent);
				boolean isClubExchangeDependent = ((Boolean) this.fieldValues.get(this.clubMemberExchangeDependentFieldName)).booleanValue();
				ageGenderPluginBusiness.setClubMemberExchangeDependent(group,isClubExchangeDependent);
				
				group.store();
			}
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
		int groupId = getGroupId();
		if (groupId > 0) {
			Group group;
			try {
				group = (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(
						getGroupId())));
				// special case because the age and gender stuff should be
				// controlled by the club member template group
				// for other group types it is never read only
				boolean readOnly = IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(group.getGroupType());
				if (readOnly) {
					this.femaleField.setDisabled(true);
					this.maleField.setDisabled(true);
					this.lowerAgeLimitField.setDisabled(true);
					this.upperAgeLimitField.setDisabled(true);
					this.ageLimitIsStringentConditionField.setDisabled(true);
					this.keyDateForAgeField.setDisabled(true);
				}
				// get corressponding service bean
				AgeGenderPluginBusiness ageGenderPluginBusiness = getAgeGenderPluginBusiness(this.getEventIWContext());
				// set gender radio buttons
				// isMale, isFemale throws RemoteException and FinderException
				boolean isFemale = ageGenderPluginBusiness.isFemale(group);
				boolean isMale = ageGenderPluginBusiness.isMale(group);
				// if isFemale and isMale are both false then the gender is
				// neuter
				// in this case show both checkboxes as checked
				if (!isFemale && !isMale) {
					isFemale = true;
					isMale = true;
				}
				this.fieldValues.put(this.femaleFieldName, new Boolean(isFemale));
				this.fieldValues.put(this.maleFieldName, new Boolean(isMale));
				// get lower age limit
				int lowerAgeLimit = ageGenderPluginBusiness.getLowerAgeLimit(group);
				this.fieldValues.put(this.lowerAgeLimitFieldName, new Integer(lowerAgeLimit));
				// get upper age limit
				int upperAgeLimit = ageGenderPluginBusiness.getUpperAgeLimit(group);
				this.fieldValues.put(this.upperAgeLimitFieldName, new Integer(upperAgeLimit));
				boolean ageLimitIsStringentCondition = ageGenderPluginBusiness.isAgeLimitStringentCondition(group);
				this.fieldValues.put(this.ageLimitIsStringentConditionFieldName, new Boolean(ageLimitIsStringentCondition));
				String keyDateForAge = ageGenderPluginBusiness.getKeyDateForAge(group);
				this.fieldValues.put(this.keyDateForAgeFieldName, keyDateForAge);
				
				//EXTRA ISI STUFF
				boolean isNationalityDep = ageGenderPluginBusiness.isNationalityDependent(group);
				boolean clubExchangeDep = ageGenderPluginBusiness.isClubMemberExchangeDependent(group);
				this.fieldValues.put(this.nationalityDependentFieldName, new Boolean(isNationalityDep));
				this.fieldValues.put(this.clubMemberExchangeDependentFieldName, new Boolean(clubExchangeDep));
			
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
	}

	public AgeGenderPluginBusiness getAgeGenderPluginBusiness(IWApplicationContext iwc) {
		AgeGenderPluginBusiness business = null;
		if (business == null) {
			try {
				business = (AgeGenderPluginBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc,
						AgeGenderPluginBusiness.class);
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