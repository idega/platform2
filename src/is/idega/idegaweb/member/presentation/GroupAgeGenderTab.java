package is.idega.idegaweb.member.presentation;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;

import javax.ejb.FinderException;

import is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusiness;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.RadioGroup;
import com.idega.user.data.GenderBMPBean;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.presentation.UserGroupTab;

/**
 *@author     <a href="mailto:thomas@idega.is">Thomas Hilbig</a>
 *@version    1.0
 */
public class GroupAgeGenderTab extends UserGroupTab {
  
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
  private String lowerAgeTooSmallError = "Lower age limit is too small";
  private String upperAgeTooLargeError = "Upper age limit is too large";
  private String lowerAgeGreaterThanUpperAgeError = "Lower age is greater than upper age";
  
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
  
  public GroupAgeGenderTab(){
    setName("Age/Gender");
  }
  
  public GroupAgeGenderTab(Group group) {
   this();
    // do not store the group because this tab instance will be also used by other groups
    // (see setGroupId() !)
    setGroupId(((Integer)group.getPrimaryKey()).intValue());

  }
    
	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldNames()
	 */
	public void initializeFieldNames() {
    maleFieldName = "male";
    femaleFieldName = "female";
    lowerAgeLimitFieldName = "lowerAgeLimitField";
    upperAgeLimitFieldName = "upperAgeLimitField";
    ageLimitIsStringentConditionFieldName = "ageLimitIsStringentConditionFieldName";
    keyDateForAgeFieldName = "keyDateForAgeFieldName";
    lowerAgeTooSmallFieldName = "lowerAgeTooSmallField";
    upperAgeTooLargeFieldName = "upperAgeTooLargeField";
    lowerAgeGreaterThanUpperAgeFieldName = "lowerAgeGreaterThanUpperAgeField";
	}
	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
    fieldValues = new Hashtable();
    fieldValues.put(maleFieldName,  new Boolean(false));
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
    femaleField.setChecked(((Boolean)fieldValues.get(femaleFieldName)).booleanValue());
    maleField.setChecked(((Boolean)fieldValues.get(maleFieldName)).booleanValue());
    lowerAgeLimitField.setContent(((Integer) fieldValues.get(lowerAgeLimitFieldName)).toString());
    upperAgeLimitField.setContent(((Integer) fieldValues.get(upperAgeLimitFieldName)).toString());

    ageLimitIsStringentConditionField.setChecked(((Boolean) fieldValues.get(ageLimitIsStringentConditionFieldName)).booleanValue());
    StringTokenizer keyDate = new StringTokenizer((String)fieldValues.get(keyDateForAgeFieldName)," -");  

    if(keyDate.hasMoreTokens()){
      keyDateForAgeField.setMonth(keyDate.nextToken());
    }
    if(keyDate.hasMoreTokens()){
      keyDateForAgeField.setDay(keyDate.nextToken());
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
    maleField = new CheckBox(maleFieldName);  
    
    String integerErrorWarning = "The input must be greater or equal zero";
    String notEmpty = "Please fill in every field";
    
    lowerAgeLimitField = new IntegerInput(lowerAgeLimitFieldName, integerErrorWarning);
    upperAgeLimitField = new IntegerInput(upperAgeLimitFieldName, integerErrorWarning);
    
    lowerAgeLimitField.setSize(3);
    upperAgeLimitField.setSize(3);
    
    lowerAgeLimitField.setMaxlength(3);
    upperAgeLimitField.setMaxlength(3);
    
    lowerAgeLimitField.setAsNotEmpty(notEmpty);
    upperAgeLimitField.setAsNotEmpty(notEmpty);
    
    ageLimitIsStringentConditionField = new CheckBox(ageLimitIsStringentConditionFieldName);
    
    keyDateForAgeField = new DateInput(keyDateForAgeFieldName);
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
    IWContext iwc = getEventIWContext();
    femaleText = new Text("female members");
    maleText = new Text("male members");
    lowerAgeLimitText = new Text("Lower age limit:");
    upperAgeLimitText = new Text("Upper age limit:");
    ageLimitIsStringentConditionText = new Text("Age limits are stringent conditions");
    keyDateForAgeText = new Text("Key date for age:");
	}
	/**
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
    Table table = new Table(2,9);
    table.add(femaleText,1, 1);
    table.add(femaleField,2,1);
    table.add(maleText,1,2);
    table.add(maleField, 2,2);
    table.add(lowerAgeLimitText,1,3);
    table.add(lowerAgeLimitField,2,3);
    table.add(upperAgeLimitText,1,4);
    table.add(upperAgeLimitField,2,4);
    table.add(keyDateForAgeText,1,5);
    table.add(keyDateForAgeField,2,5);
    table.add(ageLimitIsStringentConditionText,1,6);
    table.add(ageLimitIsStringentConditionField,2,6);
    // error fields
    table.add(lowerAgeTooSmallField,1,7);
    table.add(upperAgeTooLargeField,1,8);
    table.add(lowerAgeGreaterThanUpperAgeField,1,9);
    add(table);
	}
	/**
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
    
    if(iwc != null){
      String female = iwc.getParameter(femaleFieldName);
      String male = iwc.getParameter(maleFieldName);
      String lowerAgeLimit = iwc.getParameter(lowerAgeLimitFieldName);
      String upperAgeLimit = iwc.getParameter(upperAgeLimitFieldName);
      String ageLimitIsStringentCondition = iwc.getParameter(ageLimitIsStringentConditionFieldName);
      
      String keyDate = iwc.getParameter(keyDateForAgeFieldName);
      // only store key date if month and day is set by the user
      // that is e.g: "1 - 01 - 11"
      if ( (keyDate != null) && (keyDate.indexOf("-") != keyDate.lastIndexOf("-")) )   {
        int i = keyDate.indexOf("-");
        keyDate = keyDate.substring(++i);
        fieldValues.put(keyDateForAgeFieldName, keyDate);
      }
      
      fieldValues.put(ageLimitIsStringentConditionFieldName, new Boolean(ageLimitIsStringentCondition != null));
      
      fieldValues.put(femaleFieldName, new Boolean(female != null));
      fieldValues.put(maleFieldName, new Boolean(male != null));
 
      if(lowerAgeLimit != null){
        fieldValues.put(lowerAgeLimitFieldName, new Integer(lowerAgeLimit));
      }
      if(upperAgeLimit != null){
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
      catch (RemoteException ex)  {
        System.err.println("[GroupAgeGenderTab] Problem to get the AgeGenderPluginBusiness bean: "+ ex.getMessage());
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
                  
      ageGenderPluginBusiness.setLowerAgeLimit(group,lowerAge);
      ageGenderPluginBusiness.setUpperAgeLimit(group,upperAge);      
      // set gender
      boolean isFemale = ((Boolean) fieldValues.get(femaleFieldName)).booleanValue();
      boolean isMale = ((Boolean) fieldValues.get(maleFieldName)).booleanValue();
      if (isMale && !isFemale)
        ageGenderPluginBusiness.setMale(group);
      else if (isFemale && !isMale)
        ageGenderPluginBusiness.setFemale(group);
      else
        // male and female are either both true or both false 
        ageGenderPluginBusiness.setNeuter(group);

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
  
  public AgeGenderPluginBusiness getAgeGenderPluginBusiness(IWApplicationContext iwc){
    AgeGenderPluginBusiness business = null;
    if(business == null){
      try{
        business = (AgeGenderPluginBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,AgeGenderPluginBusiness.class);
      }
      catch(java.rmi.RemoteException rme){
        throw new RuntimeException(rme.getMessage());
      }
    }
    return business;
  }

}

