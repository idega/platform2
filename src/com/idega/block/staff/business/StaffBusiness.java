package com.idega.block.staff.business;

import java.sql.SQLException;
import com.idega.block.staff.data.*;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.data.*;
import com.idega.util.idegaTimestamp;

/**
 * Title:        User
 * Copyright:    Copyright (c) 2000 idega.is All Rights Reserved
 * Company:      idega margmiðlun
 * @author
 * @version 1.0
 */

public class StaffBusiness extends UserBusiness{

  public StaffBusiness() {
  }

  public static StaffInfo getStaffInfo(int userId) {
    try {
      return new StaffInfo(userId);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static void updateStaff(int user_id, String title, String education, String school, String area, idegaTimestamp began_work) throws SQLException{
    StaffInfo staffToAdd = null;
    boolean update = false;

    try {
      staffToAdd = new StaffInfo(user_id);
      update = true;
    }
    catch (SQLException e) {
      staffToAdd = new StaffInfo();
      staffToAdd.setID(user_id);
      update = false;
    }

    if(title != null){
      staffToAdd.setTitle(title);
    }
    if(education != null){
      staffToAdd.setEducation(education);
    }
    if(school != null){
      staffToAdd.setSchool(school);
    }
    if(area != null){
      staffToAdd.setArea(area);
    }
    if(began_work != null){
      staffToAdd.setBeganWork(began_work.getSQLDate());
    }
    if(!update){
      staffToAdd.setImageID(-1);
    }

    if ( update )
      staffToAdd.update();
    else
      staffToAdd.insert();
  }

  public static void updateImage(int userId,String imageId) {
    StaffInfo staffToAdd = null;
    boolean update = false;

    try {
      staffToAdd = new StaffInfo(userId);
      update = true;
    }
    catch (SQLException e) {
      staffToAdd = new StaffInfo();
      staffToAdd.setID(userId);
      update = false;
    }

    if ( imageId != null ) {
      try {
        staffToAdd.setImageID(Integer.parseInt(imageId));
      }
      catch (NumberFormatException ex) {
        staffToAdd.setImageID(-1);
      }
    }

    try {
      if ( update ) {
        staffToAdd.update();
        System.out.println("Updated");
      }
      else {
        staffToAdd.insert();
        System.out.println("Inserted");
      }
    }
    catch (SQLException ex) {
      ex.printStackTrace(System.err);
    }
  }

  public static void deleteStaff(int userId) throws SQLException {
    StaffInfo delStaff = new StaffInfo(userId);
    delStaff.delete();

    deleteUser(userId);
  }
} // Class StaffBusiness