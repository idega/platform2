package com.idega.block.staff.business;



import java.util.List;

import java.util.Vector;

import java.util.Iterator;

import java.sql.SQLException;

import com.idega.presentation.IWContext;

import com.idega.block.staff.data.*;

import com.idega.core.user.business.UserBusiness;

import com.idega.core.data.*;

import com.idega.core.user.data.*;

import com.idega.data.*;

import com.idega.util.idegaTimestamp;

import com.idega.core.business.UserGroupBusiness;

import com.idega.core.user.business.UserBusiness;

import com.idega.core.localisation.business.ICLocaleBusiness;



/**

 * Title:        User

 * Copyright:    Copyright (c) 2000 idega.is All Rights Reserved

 * Company:      idega margmiðlun

 * @author

 * @version 1.0

 */



public class StaffFinder {



  public static User getUser(int userID) {

    try {

      return ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(userID);

    }

    catch (SQLException e) {

      return null;

    }

  }



  public static StaffInfo getStaffInfo(int userId) {

    try {

      return ((com.idega.block.staff.data.StaffInfoHome)com.idega.data.IDOLookup.getHomeLegacy(StaffInfo.class)).findByPrimaryKeyLegacy(userId);

    }

    catch (SQLException e) {

      return null;

    }

  }



  public static StaffEntity getStaff(int userId) {

    try {

      return ((com.idega.block.staff.data.StaffEntityHome)com.idega.data.IDOLookup.getHomeLegacy(StaffEntity.class)).findByPrimaryKeyLegacy(userId);

    }

    catch (SQLException e) {

      return null;

    }

  }



  public static StaffMetaData[] getMetaData(int userId) {

    try {

      return (StaffMetaData[]) com.idega.block.staff.data.StaffMetaDataBMPBean.getStaticInstance().findAllByColumn(com.idega.block.staff.data.StaffMetaDataBMPBean.getColumnNameUserID(),Integer.toString(userId),"=");

    }

    catch (SQLException e) {

      return null;

    }

  }



  public static StaffMeta[] getMeta(int userID,int localeID) {

    try {

      return (StaffMeta[]) com.idega.block.staff.data.StaffMetaBMPBean.getStaticInstance(StaffMeta.class).findAllByColumn(com.idega.block.staff.data.StaffMetaBMPBean.getColumnNameUserID(),Integer.toString(userID),'=',com.idega.block.staff.data.StaffMetaBMPBean.getColumnNameLocaleId(),Integer.toString(localeID),'=');

    }

    catch (SQLException e) {

      return null;

    }

  }



  public static Email getUserEmail(User user) {

    return UserBusiness.getUserMail(user);

  }



  public static List getGroups(IWContext iwc) {

    try {

      List allGroups = UserGroupBusiness.getAllGroups(iwc);

      if ( allGroups != null ) {

        allGroups.remove(iwc.getAccessController().getPermissionGroupAdministrator());

      }

      return allGroups;

    }

    catch (Exception e) {

      return null;

    }

  }



  public static List getAllUsers(IWContext iwc) {

    try {

      List allUsers = EntityFinder.findAll(com.idega.core.user.data.UserBMPBean.getStaticInstance());

      if ( allUsers != null ) {

        allUsers.remove(iwc.getAccessController().getAdministratorUser());

      }

      return allUsers;

    }

    catch (Exception e) {

      return null;

    }

  }



  public static List getAllGroups(IWContext iwc) {

    List groups = UserGroupBusiness.getAllGroups(iwc);

    if ( groups != null ) {

      try {

        groups.remove(iwc.getAccessController().getPermissionGroupAdministrator());

      }

      catch (Exception e) {}

      return groups;

    }

    return null;

  }



  public static List getUsersInPrimaryGroup(GenericGroup group) {

    List users = UserBusiness.getUsersInPrimaryGroup(group);

    if ( users != null )

      return users;

    return null;

  }



  public static List getAllUsersByLetter(IWContext iwc,String letter) {

    try {

      List allUsers = EntityFinder.findAllByColumn(com.idega.core.user.data.UserBMPBean.getStaticInstance(),com.idega.core.user.data.UserBMPBean.getColumnNameFirstName(),letter+"%");

      if ( allUsers != null ) {

        allUsers.remove(iwc.getAccessController().getAdministratorUser());

      }

      return allUsers;

    }

    catch (Exception e) {

      return null;

    }

  }



  public static List getUsersInNoGroups(GenericGroup group) {

    try {

      return UserBusiness.getUsersInGroup(group);

    }

    catch (Exception e) {

      return null;

    }

  }



  public static List getUsersInNoGroups() {

    try {

      return UserBusiness.getUsersInNoGroup();

    }

    catch (SQLException e) {

      return null;

    }

  }



  public static StaffLocalized getLocalizedStaff(StaffEntity entity, int iLocaleID){

    try {

      List list = null;

      try {

        list = EntityFinder.findRelated(entity,com.idega.block.staff.data.StaffLocalizedBMPBean.getStaticInstance(StaffLocalized.class));

      }

      catch (Exception e) {

        list = null;

      }



      if ( list != null ) {
        if(list.size() == 1){
          return (StaffLocalized) list.get(0);
        } else {
          Iterator iter = list.iterator();
          while (iter.hasNext()) {
            StaffLocalized item = (StaffLocalized) iter.next();
            if ( item.getLocaleId() == iLocaleID ) {
              return item;
            }
          }
        }
      }
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }



  public static List getStaffHolders(List users,IWContext iwc) {

    return getStaffHolders(users,ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale()));

  }



  public static List getStaffHolders(List users,int localeID) {

    Vector staffHolders = new Vector();



    if ( users != null ) {

      Iterator iter = users.iterator();

      while (iter.hasNext()) {

        staffHolders.add(getStaffHolder((User)iter.next(),localeID));

      }

    }

    return staffHolders;

  }



  public static StaffHolder getStaffHolder(int userID,IWContext iwc) {

    User user = StaffFinder.getUser(userID);

    return getStaffHolder(user,ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale()));

  }



  public static StaffHolder getStaffHolder(int userID,int localeID) {

    User user = StaffFinder.getUser(userID);

    return getStaffHolder(user,localeID);

  }



  public static StaffHolder getStaffHolder(User user,int localeID) {

    StaffEntity staff = getStaff(user.getID());

    StaffLocalized staffInfo = getLocalizedStaff(staff,localeID);

    StaffMeta[] staffMeta = getMeta(user.getID(),localeID);

    Phone workPhone = UserBusiness.getUserPhone(user.getID(),com.idega.core.data.PhoneTypeBMPBean.WORK_PHONE_ID);

    Phone mobilePhone = UserBusiness.getUserPhone(user.getID(),com.idega.core.data.PhoneTypeBMPBean.MOBILE_PHONE_ID);

    Email email = UserBusiness.getUserMail(user);



    StaffHolder holder = new StaffHolder(user);

    if ( user != null ) {

     idegaTimestamp stamp = null;

     if ( user.getDateOfBirth() != null )

        stamp = new idegaTimestamp(user.getDateOfBirth());

      idegaTimestamp dateToday = new idegaTimestamp();



      int userAge = 0;

      if ( stamp != null )

        userAge = (new idegaTimestamp().getDaysBetween(stamp,dateToday))/365;

/*

      holder.setFirstName(user.getFirstName());

      holder.setMiddleName(user.getMiddleName());

      holder.setLastName(user.getLastName());

*/

      holder.setAge(userAge);

//      holder.setUserID(user.getID());

    }

    if ( staff != null ) {

      if ( staff.getBeganWork() != null )

        holder.setBeganWork(new idegaTimestamp(staff.getBeganWork()));

      holder.setImageID(staff.getImageID());

    }

    if ( staffInfo != null ) {

      holder.setArea(staffInfo.getArea());

      holder.setEducation(staffInfo.getEducation());

      holder.setTitle(staffInfo.getTitle());

    } else {//Legacy
      try {

        StaffInfo info = getStaffInfo(user.getID());

        holder.setArea(info.getArea());

        holder.setEducation(info.getEducation());

        holder.setTitle(info.getTitle());

      }
      catch (Exception ex) {
        //
      }

    }

    if ( staffMeta != null ) {

      String[] attributes = new String[staffMeta.length];

      String[] values = new String[staffMeta.length];

      for ( int a = 0; a < staffMeta.length; a++ ) {

        attributes[a] = staffMeta[a].getAttribute();

        values[a] = staffMeta[a].getValue();

      }

      holder.setMetaAttributes(attributes);

      holder.setMetaValues(values);

    }

    if ( workPhone != null ) {

      holder.setWorkPhone(workPhone.getNumber());

    }

    if ( mobilePhone != null ) {

      holder.setArea(mobilePhone.getNumber());

    }

    if ( email != null ) {

      holder.setEmail(email.getEmailAddress());

    }

    return holder;

  }

} // Class StaffFinder
