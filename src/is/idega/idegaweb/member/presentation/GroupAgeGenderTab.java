package is.idega.idegaweb.member.presentation;

import java.rmi.RemoteException;
import java.util.Hashtable;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;

import javax.ejb.FinderException;

import sun.security.krb5.internal.crypto.g;

import is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusiness;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.IntegerInput;
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
  
  private RadioGroup genderRadioGroup;
  
  private IntegerInput lowerAgeLimitField;
  private IntegerInput upperAgeLimitField;
  
  
  private static final String GENDER_KEY = "gender_key";
  private static final String MALE_VALUE = "male";
  private static final String FEMALE_VALUE = "female";
  
  private Text genderText;
  private Text lowerAgeLimitText;
  private Text upperAgeLimitText;
  
  // field names
  private String genderFieldName;
  
  private String lowerAgeLimitFieldName;
  private String upperAgeLimitFieldName;
  
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
    genderFieldName = "gender";
    lowerAgeLimitFieldName = "lowerAgeLimitField";
    upperAgeLimitFieldName = "upperAgeLimitField";
	}
	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
    fieldValues = new Hashtable();
    fieldValues.put(genderFieldName,"");
    fieldValues.put(lowerAgeLimitFieldName, new Integer(0));
    fieldValues.put(upperAgeLimitFieldName, new Integer(0));
	}
  
	/**
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
    lowerAgeLimitField.setContent(((Integer) fieldValues.get(lowerAgeLimitFieldName)).toString());
    upperAgeLimitField.setContent(((Integer) fieldValues.get(upperAgeLimitFieldName)).toString());
	}
	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
    genderRadioGroup = new RadioGroup(GENDER_KEY);  
    genderRadioGroup.addRadioButton(MALE_VALUE, new Text("male"));
    genderRadioGroup.addRadioButton(FEMALE_VALUE, new Text("female"));  
    
    String integerErrorWarning = "The input must be an integer";
    lowerAgeLimitField = new IntegerInput(lowerAgeLimitFieldName, integerErrorWarning);
    upperAgeLimitField = new IntegerInput(upperAgeLimitFieldName, integerErrorWarning);
    lowerAgeLimitField.setSize(3);
    upperAgeLimitField.setSize(3);
    lowerAgeLimitField.setMaxlength(3);
    lowerAgeLimitField.setMaxlength(3);
    

	}
	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
    genderText = new Text("Gender:");
    lowerAgeLimitText = new Text("Lower age limit:");
    upperAgeLimitText = new Text("Upper age limit:");
	}
	/**
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
    Table table = new Table(2,3);
    table.add(genderRadioGroup, 1, 1);
    table.add(lowerAgeLimitText,1,2);
    table.add(lowerAgeLimitField,2,2);
    table.add(upperAgeLimitText,1,3);
    table.add(upperAgeLimitField,2,3);
    add(table);
	}
	/**
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
    
    if(iwc != null){
      String lowerAgeLimit = iwc.getParameter(lowerAgeLimitFieldName);
      String upperAgeLimit = iwc.getParameter(upperAgeLimitFieldName);

      if(lowerAgeLimit != null){
        fieldValues.put(lowerAgeLimitFieldName, new Integer(lowerAgeLimit));
      }
      if(upperAgeLimit != null){
        fieldValues.put(upperAgeLimitFieldName, new Integer(upperAgeLimit));
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
      // set upper and lower age limit  
      ageGenderPluginBusiness.setLowerAgeLimit(group, ((Integer) fieldValues.get(lowerAgeLimitFieldName)).intValue());
      ageGenderPluginBusiness.setUpperAgeLimit(group, ((Integer) fieldValues.get(upperAgeLimitFieldName)).intValue());
      
      IDOLegacyEntity ent = ((GenericEntity)group).getStaticInstance(group.getClass());
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
    
      // isMale throws RemoteException and FinderException
      boolean isMale = ageGenderPluginBusiness.isMale(group);  
      
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

