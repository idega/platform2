package com.idega.block.staff.presentation;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:laddi@idega.is">Thorhallur "Laddi" Helgason</a>
 * @version 1.2
 */

import java.sql.SQLException;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.textObject.Link;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.Image;
import com.idega.data.EntityFinder;
import com.idega.core.business.UserGroupBusiness;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.presentation.CreateUser;
import com.idega.core.user.presentation.CreateUserGroup;
import com.idega.core.data.GenericGroup;
import com.idega.core.data.PhoneType;
import com.idega.core.data.Phone;
import com.idega.core.data.Email;
import com.idega.core.user.data.User;
import com.idega.block.staff.data.StaffInfo;
import com.idega.block.staff.business.StaffBusiness;
import com.idega.core.accesscontrol.business.AccessControl;
import java.util.List;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;


public class StaffViewer extends JModuleObject{

private boolean isAdmin=false;
private IWBundle iwb;
private IWResourceBundle iwrb;
private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.staff";

public static final String PARAMETER_MODE = "mode";
public static final String PARAMETER_DELETE_USER = "user";
public static final String PARAMETER_DELETE_GROUP = "group";
public static final String PARAMETER_USER_ID = "user_id";
public static final String PARAMETER_GROUP_ID = "group_id";

private Table myTable;
private Table staffTable;
private Table groupsTable;
private int row = 1;
private int groupRow = 1;

private List allGroups;
private List usersInNoGroup;

  public StaffViewer(){
    initialize();
  }

  public void main(ModuleInfo modinfo) throws Exception {
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);
    isAdmin = AccessControl.hasEditPermission(this,modinfo);

    String mode = modinfo.getParameter(PARAMETER_MODE);
    if ( mode != null ) {
      if ( mode.equalsIgnoreCase(PARAMETER_DELETE_USER) )
        deleteUser(modinfo);
      if ( mode.equalsIgnoreCase(PARAMETER_DELETE_GROUP) )
        deleteGroup(modinfo);
    }

    getGroups();
    getUsersInNoGroups();

    if ( isAdmin )
      drawAdminButtons();

    drawGroupsTables();

    if ( isAdmin )
      drawUsersTables();

    add(myTable);
  }

  private void drawGroupsTables() {
    if ( allGroups != null ) {
      for ( int a = 0; a < allGroups.size(); a++ ) {
        Table groupTable = (Table) groupsTable.clone();
        groupRow = 2;
        GenericGroup group = (GenericGroup)allGroups.get(a);

        Text groupName = new Text(group.getName());
          groupName.setFontSize(3);
          groupName.setBold();
        groupTable.add(groupName,1,groupRow);
        if ( isAdmin ) {
          Image deleteGroup = iwrb.getImage("delete.gif",iwrb.getLocalizedString("delete_division","Delete Division"));
            deleteGroup.setHorizontalSpacing(6);
          Link deleteGroupLink = new Link(deleteGroup);
            deleteGroupLink.addParameter(this.PARAMETER_MODE,this.PARAMETER_DELETE_GROUP);
            deleteGroupLink.addParameter(this.PARAMETER_GROUP_ID,group.getID());
          groupTable.add(deleteGroupLink,1,groupRow);
        }
        groupRow++;

        List usersInGroup = UserBusiness.getUsersInGroup(group);
        if ( usersInGroup != null ) {
          for ( int b = 0; b < usersInGroup.size(); b++ ) {
            Table userTable = (Table) staffTable.clone();
            User user = (User) usersInGroup.get(b);
            StaffInfo staff = StaffBusiness.getStaffInfo(user.getID());
            Phone userPhone = UserBusiness.getUserPhone(user.getID(),PhoneType.WORK_PHONE_ID);
            Email userMail = UserBusiness.getUserMail(user.getID());

            Text userText = new Text(user.getName());
            Link link = new Link(userText);
            link.addParameter(StaffPropertyWindow.PARAMETERSTRING_USER_ID,user.getID());
            link.setWindowToOpen(StaffPropertyWindow.class);
            if ( isAdmin )
              userTable.add(link,2,1);
            else
              userTable.add(userText,2,1);

            Image userImage = iwb.getImage("/shared/default.jpg");
              userImage.setBorder(1);

            if ( staff != null ) {
              Text title = new Text(" - "+staff.getTitle());
              userTable.add(title,2,1);
              Text education = new Text(staff.getEducation());
              userTable.add(education,2,2);

              if ( staff.getImageID() != -1 )
                try {
                  userImage = new Image(staff.getImageID());
                  userImage.setBorder(1);
                }
                catch (SQLException e) {
                }
            }
              userTable.add(userImage,1,1);
            if ( userPhone != null ) {
              Text phoneText = new Text(iwrb.getLocalizedString("work_phone","Work phone")+": ");
                phoneText.setBold();
              Text phone = new Text(userPhone.getNumber());
              userTable.add(phoneText,2,3);
              userTable.add(phone,2,3);
            }
            if ( userMail != null ) {
              Text mailText = new Text(iwrb.getLocalizedString("e_mail","e-mail")+": ");
                mailText.setBold();
              userTable.add(mailText,3,3);

              Link mail = new Link(userMail.getEmailAddress());
                mail.setURL("mailto:"+userMail.getEmailAddress());
              userTable.add(mail,3,3);
            }

            if ( isAdmin ) {
              Image deleteUser = iwrb.getImage("delete.gif",iwrb.getLocalizedString("delete_employee","Delete Employee"));
                deleteUser.setHorizontalSpacing(6);
              Link deleteUserLink = new Link(deleteUser);
                deleteUserLink.addParameter(this.PARAMETER_MODE,this.PARAMETER_DELETE_USER);
                deleteUserLink.addParameter(this.PARAMETER_USER_ID,user.getID());
              userTable.add(deleteUserLink,2,1);
            }

            groupTable.add(userTable,1,groupRow);

            groupRow++;
          }
          myTable.add(groupTable,1,row);
        }
        row++;
      }
    }
  }

  private void drawUsersTables() {
    Table noGroupTable = (Table) groupsTable.clone();
    if ( this.usersInNoGroup != null ) {
      groupRow = 1;
      for ( int a = 0; a < usersInNoGroup.size(); a++ ) {
        User user = (User) usersInNoGroup.get(a);

        Link link = new Link(user.getName());
        link.addParameter(StaffPropertyWindow.PARAMETERSTRING_USER_ID,user.getID());
        link.setWindowToOpen(StaffPropertyWindow.class);
        noGroupTable.add(link,1,groupRow);

        Image deleteUser = iwrb.getImage("delete.gif",iwrb.getLocalizedString("delete_employee","Delete Employee"));
          deleteUser.setHorizontalSpacing(6);
        Link deleteUserLink = new Link(deleteUser);
          deleteUserLink.addParameter(this.PARAMETER_MODE,this.PARAMETER_DELETE_USER);
          deleteUserLink.addParameter(this.PARAMETER_USER_ID,user.getID());
        noGroupTable.add(deleteUserLink,1,groupRow);

        groupRow++;
      }
      myTable.add(noGroupTable,1,row);
    }
  }

  private void drawAdminButtons() {
    Table adminTable = new Table(2,1);
      adminTable.setCellpadding(5);

    Image createUserImage = iwb.getImage("/shared/user.gif");
      createUserImage.setAlignment("absmiddle");
      createUserImage.setHorizontalSpacing(3);
    Image createGroupImage = iwb.getImage("/shared/group.gif");
      createGroupImage.setAlignment("absmiddle");
      createGroupImage.setHorizontalSpacing(3);

    Link createUser = new Link(iwrb.getLocalizedString("new_employee","New Employee"));
      createUser.setWindowToOpen(CreateUser.class);

    Link createGroup = new Link(iwrb.getLocalizedString("new_division","New Division"));
      createGroup.setWindowToOpen(CreateUserGroup.class);

    adminTable.add(createUserImage,1,1);
    adminTable.add(createUser,1,1);
    adminTable.add(createGroupImage,2,1);
    adminTable.add(createGroup,2,1);
    myTable.add(adminTable,1,row);
    row++;

  }

  private void initialize() {
    myTable = new Table();

    staffTable = new Table(3,3);
      staffTable.mergeCells(1,1,1,3);
      staffTable.mergeCells(2,1,3,1);
      staffTable.mergeCells(2,2,3,2);
      staffTable.setWidth(1,"100");
      staffTable.setAlignment(1,1,"center");
      staffTable.setVerticalAlignment(1,1,"top");
      staffTable.setWidth(2,3,"200");
      staffTable.setWidth(3,3,"200");
      staffTable.setWidth("100%");
      staffTable.setBorder(0);

    groupsTable = new Table();
      groupsTable.setWidth("100%");
  }

  private void getGroups() {
    try {
      allGroups = UserGroupBusiness.getAllGroups();
      if ( !isAdmin && allGroups != null ) {
        allGroups.remove(AccessControl.getAdministratorGroup());
      }
    }
    catch (Exception e) {
      System.out.println("AllGroups is null");
      allGroups = null;
    }
  }

  private void getUsersInNoGroups() {
    try {
      usersInNoGroup = UserBusiness.getUsersInNoGroup();
    }
    catch (Exception e) {
      allGroups = null;
    }
  }

  private void deleteUser(ModuleInfo modinfo) throws SQLException {
    String userId = modinfo.getParameter(PARAMETER_USER_ID);
    if ( userId != null ) {
      StaffBusiness.deleteStaff(Integer.parseInt(userId));
    }
  }

  private void deleteGroup(ModuleInfo modinfo) throws SQLException {
    String groupId = modinfo.getParameter(PARAMETER_GROUP_ID);
    if ( groupId != null ) {
      UserGroupBusiness.deleteGroup(Integer.parseInt(groupId));
    }
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public Object clone() {
    StaffViewer obj = null;
    try {
      obj = (StaffViewer)super.clone();

      obj.row = this.row;
      obj.groupRow = this.groupRow;

      if (this.myTable != null) {
        obj.myTable=(Table)this.myTable.clone();
      }
      if (this.staffTable != null) {
        obj.staffTable=(Table)this.staffTable.clone();
      }
      if (this.groupsTable != null) {
        obj.groupsTable=(Table)this.groupsTable.clone();
      }
    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }

}
