package is.idega.idegaweb.member.presentation;

import java.sql.SQLException;

import com.idega.core.data.Email;
import com.idega.core.data.Phone;
import com.idega.core.data.PhoneType;
import com.idega.presentation.IWContext;
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
    this.setName("Contact");
  }

  public GroupOfficeContactTab(int groupId){
    this();
    this.setGroupId(groupId);
  }

  public void initializeFieldNames(){
  }

  public void initializeFieldValues(){
    fieldValues.put(this.homePhoneFieldName,"");
    fieldValues.put(this.workPhoneFieldName,"");
    fieldValues.put(this.mobilePhoneFieldName,"");
    fieldValues.put(this.faxPhoneFieldName,"");
    fieldValues.put(this.homePhoneMenuName,"");
    fieldValues.put(this.workPhoneMenuName,"");
    fieldValues.put(this.mobilePhoneMenuName,"");
    fieldValues.put(this.faxPhoneMenuName,"");
    fieldValues.put(this.emailFieldName,"");
    fieldValues.put(homepageFieldName,"");

    this.updateFieldsDisplayStatus();
  }

  public void updateFieldsDisplayStatus(){
    homePhoneField.setContent((String)fieldValues.get(this.homePhoneFieldName));
    workPhoneField.setContent((String)fieldValues.get(this.workPhoneFieldName));
    mobilePhoneField.setContent((String)fieldValues.get(this.mobilePhoneFieldName));
    faxPhoneField.setContent((String)fieldValues.get(this.faxPhoneFieldName));
    homepageField.setContent((String)fieldValues.get(homepageFieldName));

    if ( (String)fieldValues.get(this.homePhoneMenuName) != null  && ((String)fieldValues.get(this.homePhoneMenuName)).length() > 0)
      homePhoneMenu.setSelectedElement((String)fieldValues.get(this.homePhoneMenuName));
    if ( (String)fieldValues.get(this.workPhoneMenuName) != null  && ((String)fieldValues.get(this.workPhoneMenuName)).length() > 0)
      workPhoneMenu.setSelectedElement((String)fieldValues.get(this.workPhoneMenuName));
    if ( (String)fieldValues.get(this.mobilePhoneMenuName) != null  && ((String)fieldValues.get(this.mobilePhoneMenuName)).length() > 0)
      mobilePhoneMenu.setSelectedElement((String)fieldValues.get(this.mobilePhoneMenuName));
    if ( (String)fieldValues.get(this.faxPhoneMenuName) != null && ((String)fieldValues.get(this.faxPhoneMenuName)).length() > 0 )
      faxPhoneMenu.setSelectedElement((String)fieldValues.get(this.faxPhoneMenuName));

    emailField.setContent((String)fieldValues.get(this.emailFieldName));
  }


  public void initializeFields() {
    PhoneType[] phoneTypes = null;
    try {
      phoneTypes = (PhoneType[]) com.idega.core.data.PhoneTypeBMPBean.getStaticInstance(PhoneType.class).findAll();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
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

    homePhoneMenu = new DropdownMenu(phoneTypes,homePhoneMenuName);
    workPhoneMenu = new DropdownMenu(phoneTypes,workPhoneMenuName);
    mobilePhoneMenu = new DropdownMenu(phoneTypes,mobilePhoneMenuName);
    faxPhoneMenu = new DropdownMenu(phoneTypes,faxPhoneMenuName);

    for ( int a = 0; a < phoneTypes.length; a++ ) {
      if ( a == 0 ) {
        homePhoneMenu.setSelectedElement(Integer.toString(phoneTypes[a].getID()));
      }
      else if ( a == 1 ) {
        workPhoneMenu.setSelectedElement(Integer.toString(phoneTypes[a].getID()));
      }
      else if ( a == 2 ) {
        mobilePhoneMenu.setSelectedElement(Integer.toString(phoneTypes[a].getID()));
      }
      else if ( a == 3 ) {
        faxPhoneMenu.setSelectedElement(Integer.toString(phoneTypes[a].getID()));
      }
    }

    emailField = new TextInput(emailFieldName);
    emailField.setLength(24);
  }

  public void initializeTexts(){
    homePhoneText = new Text("Phone"+" 1:");
    homePhoneText.setFontSize(fontSize);

    workPhoneText = new Text("Phone"+" 2:");
    workPhoneText.setFontSize(fontSize);

    mobilePhoneText = new Text("Mobile"+" 3:");
    mobilePhoneText.setFontSize(fontSize);

    faxPhoneText = new Text("Fax"+" 4:");
    faxPhoneText.setFontSize(fontSize);

    emailText = new Text("E-mail"+":");
    emailText.setFontSize(fontSize);
    
    homepageText = new Text("Homepage:");
    homepageText.setFontSize(fontSize);
  }


  public void lineUpFields(){
    this.resize(1,3);

    Table staffTable = new Table(3,4);
    staffTable.setWidth("100%");
    staffTable.setCellpadding(0);
    staffTable.setCellspacing(0);
    staffTable.setHeight(1,rowHeight);
    staffTable.setHeight(2,rowHeight);
    staffTable.setHeight(3,rowHeight);
    staffTable.setHeight(4,rowHeight);

    staffTable.add(homePhoneText,1,1);
    staffTable.add(homePhoneMenu,3,1);
    staffTable.add(homePhoneField,2,1);
    staffTable.add(workPhoneText,1,2);
    staffTable.add(workPhoneMenu,3,2);
    staffTable.add(workPhoneField,2,2);
    staffTable.add(mobilePhoneText,1,3);
    staffTable.add(mobilePhoneMenu,3,3);
    staffTable.add(mobilePhoneField,2,3);
    staffTable.add(faxPhoneText,1,4);
    staffTable.add(faxPhoneMenu,3,4);
    staffTable.add(faxPhoneField,2,4);
    this.add(staffTable,1,1);

    Table mailTable = new Table(2,2);
    mailTable.setWidth("100%");
    mailTable.setCellpadding(0);
    mailTable.setCellspacing(0);
    mailTable.setHeight(1,rowHeight);

    mailTable.add(emailText,1,1);
    mailTable.add(emailField,2,1);
    mailTable.add(homepageText,1,2);
    mailTable.add(homepageField,2,2);
    this.add(mailTable,1,3);
  }


  public boolean collect(IWContext iwc){
    if(iwc != null){

      String homePhone = iwc.getParameter(this.homePhoneFieldName);
      String workPhone = iwc.getParameter(this.workPhoneFieldName);
      String mobilePhone = iwc.getParameter(this.mobilePhoneFieldName);
      String faxPhone = iwc.getParameter(this.faxPhoneFieldName);
      String homePhoneType = iwc.getParameter(this.homePhoneMenuName);
      String workPhoneType = iwc.getParameter(this.workPhoneMenuName);
      String mobilePhoneType = iwc.getParameter(this.mobilePhoneMenuName);
      String faxPhoneType = iwc.getParameter(this.faxPhoneMenuName);
      String email = iwc.getParameter(this.emailFieldName);
      String homepage = iwc.getParameter(this.homepageFieldName);

      if(homePhone != null){
        fieldValues.put(this.homePhoneFieldName,homePhone);
      }
      if(workPhone != null){
        fieldValues.put(this.workPhoneFieldName,workPhone);
      }
      if(mobilePhone != null){
        fieldValues.put(this.mobilePhoneFieldName,mobilePhone);
      }
      if(faxPhone != null){
        fieldValues.put(this.faxPhoneFieldName,faxPhone);
      }
      if(homePhoneType != null){
        fieldValues.put(this.homePhoneMenuName,homePhoneType);
      }
      if(workPhoneType != null){
        fieldValues.put(this.workPhoneMenuName,workPhoneType);
      }
      if(mobilePhoneType != null){
        fieldValues.put(this.mobilePhoneMenuName,mobilePhoneType);
      }
      if(faxPhoneType != null){
        fieldValues.put(this.faxPhoneMenuName,faxPhoneType);
      }
      if(email != null){
        fieldValues.put(this.emailFieldName,email);
      }
      if(homepage != null)
        fieldValues.put(homepageFieldName, homepage);

      this.updateFieldsDisplayStatus();

      return true;
    }
    return false;
  }

  public boolean store(IWContext iwc){

    try{
      GroupBusiness groupBiz = getGroupBusiness(this.getEventIWContext());
      Group group = groupBiz.getGroupByGroupID(getGroupId());
      if(getGroupId() > -1){
        String[] phoneString = { (String)fieldValues.get(this.homePhoneFieldName),(String)fieldValues.get(this.workPhoneFieldName),(String)fieldValues.get(this.mobilePhoneFieldName),(String)fieldValues.get(this.faxPhoneFieldName) };
        String[] phoneTypeString = { (String)fieldValues.get(this.homePhoneMenuName),(String)fieldValues.get(this.workPhoneMenuName),(String)fieldValues.get(this.mobilePhoneMenuName),(String)fieldValues.get(this.faxPhoneMenuName) };

        for ( int a = 0; a < phoneString.length; a++ ) {
          if ( phoneString[a] != null && phoneString[a].length() > 0 ) {
            //business.updateUserPhone(getUserId(),Integer.parseInt(phoneTypeString[a]),phoneString[a]);
            getGroupBusiness(iwc).updateGroupPhone(group,Integer.parseInt(phoneTypeString[a]),phoneString[a]);
          }
        }
        if ( (String)fieldValues.get(this.emailFieldName) != null && ((String)fieldValues.get(this.emailFieldName)).length() > 0 )
          //business.updateUserMail(getUserId(),(String)fieldValues.get(this.emailFieldName));
          getGroupBusiness(iwc).updateGroupMail(group,(String)fieldValues.get(this.emailFieldName));
        }
      if ( (String)fieldValues.get(homepageFieldName) != null && ((String)fieldValues.get(homepageFieldName)).length() > 0 )
        // to do: put this to group business or a plugin business!!        
        group.setMetaData("homepage", (String) fieldValues.get(homepageFieldName));
    }
    catch(Exception e){
      e.printStackTrace(System.err);
      throw new RuntimeException("update group exception");
    }
    return true;
  }


  public void initFieldContents(){

    try{
      GroupBusiness groupBiz = getGroupBusiness(this.getEventIWContext());
      Group group = groupBiz.getGroupByGroupID(getGroupId());
      Phone[] phones = groupBiz.getGroupPhones(group);
      Email mail = groupBiz.getGroupEmail(group); 
      // to do: put this to a business object
      String homepage = group.getMetaData("homepage");

      for ( int a = 0; a < phones.length; a++ ) {
        if ( a == 0 ) {
          fieldValues.put(this.homePhoneMenuName,(phones[a].getPhoneTypeId() != -1) ? Integer.toString(phones[a].getPhoneTypeId()):"" );
          fieldValues.put(this.homePhoneFieldName,(phones[a].getNumber() != null) ? phones[a].getNumber():"" );
        }
        else if ( a == 1 ) {
          fieldValues.put(this.workPhoneMenuName,(phones[a].getPhoneTypeId() != -1) ? Integer.toString(phones[a].getPhoneTypeId()):"" );
          fieldValues.put(this.workPhoneFieldName,(phones[a].getNumber() != null) ? phones[a].getNumber():"" );
        }
        else if ( a == 2 ) {
          fieldValues.put(this.mobilePhoneMenuName,(phones[a].getPhoneTypeId() != -1) ? Integer.toString(phones[a].getPhoneTypeId()):"" );
          fieldValues.put(this.mobilePhoneFieldName,(phones[a].getNumber() != null) ? phones[a].getNumber():"" );
        }
        else if ( a == 3 ) {
          fieldValues.put(this.faxPhoneMenuName,(phones[a].getPhoneTypeId() != -1) ? Integer.toString(phones[a].getPhoneTypeId()):"" );
          fieldValues.put(this.faxPhoneFieldName,(phones[a].getNumber() != null) ? phones[a].getNumber():"" );
        }
      }
      if ( mail != null )
        fieldValues.put(this.emailFieldName,(mail.getEmailAddress() != null) ? mail.getEmailAddress():"" );
      
      if ( homepage != null)
        fieldValues.put(homepageFieldName, homepage);
      this.updateFieldsDisplayStatus();

    }
    catch(Exception e){
      System.err.println("GroupOfficeContactTab error initFieldContents, userId : " + getGroupId());
    }


  }


}
