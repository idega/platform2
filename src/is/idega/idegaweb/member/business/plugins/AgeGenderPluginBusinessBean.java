package is.idega.idegaweb.member.business.plugins;

import is.idega.idegaweb.member.presentation.GroupAgeGenderTab;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.business.IBOServiceBean;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 *@author     <a href="mailto:thomas@idega.is">Thomas Hilbig</a>
 *@version    1.0
 */
public class AgeGenderPluginBusinessBean extends IBOServiceBean implements  AgeGenderPluginBusiness, UserGroupPlugInBusiness {
  
  public static final String AGE_GENDER_PLUGIN_BUSINESS_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

  private static final String NULL = "null";
  
  private static final int LOWER_AGE_LIMIT_DEFAULT = 0;
  private static final int UPPER_AGE_LIMIT_DEFAULT = 110;
  private static final String DEFAULT_KEY_DATE = "12-31"; /// 31st of december <month>-<day>
  
  private static final String LOWER_AGE_LIMIT_META_DATA_KEY = "lowerAgeLimit";
  private static final String UPPER_AGE_LIMIT_META_DATA_KEY = "upperAgeLimit";
  private static final String GENDER_META_DATA_KEY = "gender";
  private static final String AGE_LIMIT_IS_STRINGENT_CONDITION_META_DATA_KEY = "ageLimitIsStringentCondition";
  private static final String KEY_DATE_FOR_AGE_META_DATA_KEY = "keyDateForAge";
  
  private static final int FEMALE = 0;
  private static final int MALE = 1;
  private static final int NEUTRAL = 2;
  
  private Object malePrimaryKeyFromDatastore = null;
  private Object femalePrimaryKeyFromDatastore = null;
  
  
  private void setGender(Group group, int gender){
    
    // choose NEUTRAL if either both are true or both values are false  
    if (NEUTRAL == gender) {
      // remove meta data does not work  
      // ((GenericEntity) group).removeMetaData(GENDER_META_DATA_KEY);
      group.setMetaData(GENDER_META_DATA_KEY, NULL);
      return;
    }
    try {
			GenderHome home = (GenderHome) this.getIDOHome(Gender.class);
      if (MALE == gender) {
        String maleId = ((Integer) home.getMaleGender().getPrimaryKey()).toString();
        group.setMetaData(GENDER_META_DATA_KEY, maleId);
      } 
      else  {
        String femaleId = ((Integer) home.getFemaleGender().getPrimaryKey()).toString();
        group.setMetaData(GENDER_META_DATA_KEY, femaleId);
      }
    }
		catch (RemoteException e) {
      System.err.println("[GeneralGroupInfoTab] remote error, GroupId : " + group.getPrimaryKey().toString());
      e.printStackTrace(System.err);
		}
    catch (FinderException e) {
      System.err.println("[GeneralGroupInfoTab] find error, GroupId : " + group.getPrimaryKey().toString());
      e.printStackTrace(System.err);
    }
  }  
  
  public void setMale(Group group)  {
    setGender(group, MALE);
  }
  
  public void setFemale(Group group)  {
    setGender(group, FEMALE);
  }
  
  public void setNeutral(Group group) {
    setGender(group, NEUTRAL);
  }
  
  private int getGender(Group group) throws RemoteException, FinderException {
    String genderIdString = (String) group.getMetaData(GENDER_META_DATA_KEY);
    if (genderIdString == null || NULL.equals(genderIdString))
      // meta data was not set
      // return NEUTRAL
      return NEUTRAL;
    Integer genderId = new Integer(genderIdString);
    return getMyGenderIdForGenderId(genderId);
  }
    
    
  private int getMyGenderIdForGenderId(Integer genderId) throws RemoteException, FinderException {  
    
  	if(malePrimaryKeyFromDatastore == null){
  		GenderHome home = (GenderHome) this.getIDOHome(Gender.class);
  	    malePrimaryKeyFromDatastore = ((Integer) home.getMaleGender().getPrimaryKey());
  	    femalePrimaryKeyFromDatastore = ((Integer) home.getFemaleGender().getPrimaryKey()); 
  	}
  	
    if (genderId.equals(malePrimaryKeyFromDatastore))
      return MALE;
    else if (genderId.equals(femalePrimaryKeyFromDatastore)){
      return FEMALE;
    }
    throw new FinderException("Id of gender was not found"); 
  }
  
  public boolean isFemale(Group group) throws RemoteException, FinderException  {
    return FEMALE == getGender(group);    
  }
  
  public boolean isNeutral(Group group) throws RemoteException, FinderException {
    return NEUTRAL == getGender(group);
  }
  
  public boolean isMale(Group group) throws RemoteException, FinderException {
    return MALE == getGender(group);
  }
  
  public void setAgeLimitIsStringentCondition(Group group, boolean ageLimitIsStringentCondition) {
    if (ageLimitIsStringentCondition)
      group.setMetaData( AGE_LIMIT_IS_STRINGENT_CONDITION_META_DATA_KEY, new Boolean(true).toString());
    else
      // remove meta data does not work, set false
      group.setMetaData( AGE_LIMIT_IS_STRINGENT_CONDITION_META_DATA_KEY, new Boolean(false).toString());
  }
  
  public boolean isAgeLimitStringentCondition(Group group) {
    String ageLimitIsStringentConditionString = (String) group.getMetaData(AGE_LIMIT_IS_STRINGENT_CONDITION_META_DATA_KEY);
    return !(ageLimitIsStringentConditionString == null || 
              NULL.equals(ageLimitIsStringentConditionString) ||
              ! (new Boolean(ageLimitIsStringentConditionString).booleanValue()));
  }
    
  public void setLowerAgeLimit(Group group, int lowerAgeLimit)  {
    if (lowerAgeLimit == LOWER_AGE_LIMIT_DEFAULT)
      // remove meta data does not work
      // ((GenericEntity) group).removeMetaData(LOWER_AGE_LIMIT_META_DATA_KEY);
      group.setMetaData(LOWER_AGE_LIMIT_META_DATA_KEY, NULL);
    else
      group.setMetaData(LOWER_AGE_LIMIT_META_DATA_KEY, Integer.toString(lowerAgeLimit));   
  }
  
  public int getLowerAgeLimit(Group group)  {
    String lowerAgeLimitString = (String) group.getMetaData(LOWER_AGE_LIMIT_META_DATA_KEY);
    if (lowerAgeLimitString == null || NULL.equals(lowerAgeLimitString))
      return LOWER_AGE_LIMIT_DEFAULT;
    else
      return Integer.parseInt(lowerAgeLimitString); 
  }
  
  public void setUpperAgeLimit(Group group, int upperAgeLimit)  {
    if (upperAgeLimit == UPPER_AGE_LIMIT_DEFAULT)
      // remove meta data does not work
      group.setMetaData(UPPER_AGE_LIMIT_META_DATA_KEY, NULL);
    else
      group.setMetaData(UPPER_AGE_LIMIT_META_DATA_KEY, Integer.toString(upperAgeLimit));
  }
 
  public int getUpperAgeLimit(Group group)  {
    String upperAgeLimitString = (String) group.getMetaData(UPPER_AGE_LIMIT_META_DATA_KEY);
    if (upperAgeLimitString == null || NULL.equals(upperAgeLimitString))
      return UPPER_AGE_LIMIT_DEFAULT;
    else
      return Integer.parseInt(upperAgeLimitString); 
  }
  
  public int getLowerAgeLimitDefault()  {
    return LOWER_AGE_LIMIT_DEFAULT;
  }
  
  public int getUpperAgeLimitDefault()  {
    return UPPER_AGE_LIMIT_DEFAULT;
  }
  
  public void setKeyDateForAge(Group group, String keyDateForAge)  {
    if (keyDateForAge == null || keyDateForAge.length() == 0)
      // remove does not work
      group.setMetaData(KEY_DATE_FOR_AGE_META_DATA_KEY, NULL);
    else 
      // stored in this way <month>-<day> e.g. "04-02" (second of april)
      group.setMetaData(KEY_DATE_FOR_AGE_META_DATA_KEY, keyDateForAge);   
  }
  
  public String getKeyDateForAge(Group group)  {
    String keyDateForAgeString = (String) group.getMetaData(KEY_DATE_FOR_AGE_META_DATA_KEY);
    if (keyDateForAgeString == null || NULL.equals(keyDateForAgeString))
      return DEFAULT_KEY_DATE;
    else
      return keyDateForAgeString; 
  }  
  
	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreateOrUpdate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreateOrUpdate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
    List list = new ArrayList();
    list.add(new GroupAgeGenderTab(group));  
    return list;  
	}

	
	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		return new ArrayList();
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		return null;
	}
  
  
  /** Checks if the user is assignable from the specified source to the specified target.
   * 
   * @param user the user that should be moved.
   * @param sourceGroup source, the user should belong to the source
   * @param targetGroup target, where the user should be moved to.
   * @return a message that says what is wrong else null.
   */
  public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) {
 
    // check if the source and the target are the same
    // already done by caller

    // is the user already a member of the target group?
    // already done by caller
 
    return isUserSuitedForGroup(user, targetGroup);
  }
    
  public String isUserSuitedForGroup(User user, Group targetGroup)  {  
    // get my resource bundle for all the messages
    IWResourceBundle iwrb = getResourceBundle();
    // get date of birth
    Date date = user.getDateOfBirth();
    if (date == null) {
      return iwrb.getLocalizedString("age_gender_date_of_birth_of_user_not_set", "The date of birth is not set");
    }
    GregorianCalendar dateOfBirth = new GregorianCalendar();
    dateOfBirth.setTime(date);
    // get gender of user
    int genderId = user.getGenderID();
    if (genderId < 0) {
      return iwrb.getLocalizedString("age_gender_gender_unknown", "The gender is unknown");
    }
    int myGenderId = -1;
    boolean genderOkay = false;
    try {
      myGenderId = getMyGenderIdForGenderId(new Integer(genderId));
    // test gender of target group
    genderOkay = (
      (isNeutral(targetGroup)) ||
      (isFemale(targetGroup) && myGenderId == FEMALE) ||
      (isMale(targetGroup) && myGenderId == MALE));
    }
    catch (RemoteException rm)  {
      throw new RuntimeException(rm.getMessage());
    }
    catch (FinderException fex) {
      throw new EJBException(fex.getMessage());
    }
    // is gender okay?
    if (! genderOkay) {
      return iwrb.getLocalizedString("age_gender_wrong_gender_of_user", "The user's gender is not allowed for the group");
    }
    // gender is okay.....
    
    // do we have to check the age at all?
    if (isAgeLimitStringentCondition(targetGroup))  {
      // test age of target group
      GregorianCalendar keyDate = getKeyDateForYearZero(targetGroup);
      int keyDateDay = keyDate.get(Calendar.DAY_OF_MONTH);
      int keyDateMonth = keyDate.get(Calendar.MONTH);
      int yearOfBirth = dateOfBirth.get(Calendar.YEAR);
      int dateOfBirthDay = dateOfBirth.get(Calendar.DAY_OF_MONTH);
      int dateOfBirthMonth = dateOfBirth.get(Calendar.MONTH);
      boolean birthdayAfterKeyDate = ( keyDateMonth < dateOfBirthMonth ) || (keyDateMonth == dateOfBirthMonth && keyDateDay < dateOfBirthDay);
      // get age
      Calendar rightNow = Calendar.getInstance();
      int currentYear = rightNow.get(Calendar.YEAR);
      int userAge = currentYear - yearOfBirth;
      int lowerAgeLimit = getLowerAgeLimit(targetGroup);
      int upperAgeLimit = getUpperAgeLimit(targetGroup);
      // test lower age
      if (userAge < lowerAgeLimit || (userAge == lowerAgeLimit && birthdayAfterKeyDate) ) {
        return iwrb.getLocalizedString("age_gender_user_too_young", "The user is too young");
      }
      // test upper age
      if (userAge > upperAgeLimit + 1 || (userAge == upperAgeLimit + 1 && ! birthdayAfterKeyDate) ) {
        return iwrb.getLocalizedString("age_gender_user_too_old", "The user is too old");
      }
    }
    // everything is fine
    return null;
  }
  
  public GregorianCalendar getKeyDateForYearZero(Group group) {
    String keyDateForAge = getKeyDateForAge(group);
    StringTokenizer keyDate = new StringTokenizer(keyDateForAge," -");  
    int month = -1;
    int date = -1;
    try {
      if(keyDate.hasMoreTokens()){
        month = Integer.parseInt(keyDate.nextToken());
      }
      if(keyDate.hasMoreTokens()){
        date = Integer.parseInt(keyDate.nextToken());
      }
      if (month < 1 || month > 12 || date < 1 || date > 31) {
        throw new NumberFormatException("String does not represent a date");
      }
    }
    catch (NumberFormatException ex)  {
      System.err.println("[AgeGenderPLuginBusiness was not able to read the key date of group "+ group.getPrimaryKey() + " Message was " + ex.getMessage());
      ex.printStackTrace(System.err);
      return null;
    }
    // month is zero-based
    GregorianCalendar calendar = new GregorianCalendar(0, month - 1, date);
    return calendar;
  }   

  private IWResourceBundle getResourceBundle() {
    IWMainApplication mainApp = getIWApplicationContext().getIWMainApplication();
    Locale locale = mainApp.getSettings().getDefaultLocale();
    IWBundle bundle = mainApp.getBundle(getBundleIdentifier());
    return bundle.getResourceBundle(locale);
  }
    
  protected String getBundleIdentifier() {
    return AGE_GENDER_PLUGIN_BUSINESS_BUNDLE_IDENTIFIER;
  }

/* (non-Javadoc)
 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
 */
public List getMainToolbarElements() throws RemoteException {
	return null;
}

/* (non-Javadoc)
 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
 */
public List getGroupToolbarElements(Group group) throws RemoteException {
	return null;
}

/* (non-Javadoc)
 * @see com.idega.user.business.UserGroupPlugInBusiness#canCreateSubGroup(com.idega.user.data.Group)
 */
public String canCreateSubGroup(Group group) throws RemoteException {
	// TODO Auto-generated method stub
	return null;
} 
  
}
