package com.idega.block.staff.business;

import java.util.*;
import com.idega.util.idegaTimestamp;

public class StaffHolder {
  private int userID_ = -1;
  private String firstName_ = null;
  private String middleName_ = null;
  private String lastName_ = null;
  private String workPhone_ = null;
  private String mobilePhone_ = null;
  private String email_ = null;
  private idegaTimestamp beganWork_ = null;
  private String title_ = null;
  private String education_ = null;
  private String area_ = null;
  private String[] metaAttributes_ = null;
  private String[] metaValues_ = null;

  private int imageID_ = -1;
  private int age_ = -1;

  public StaffHolder() {
  }


  /* Setters */
  public void setUserID(int userID) {
    userID_ = userID;
  }

  public void setFirstName(String firstName) {
    firstName_ = firstName;
  }

  public void setMiddleName(String middleName) {
    middleName_ = middleName;
  }

  public void setLastName(String lastName) {
    lastName_ = lastName;
  }

  public void setWorkPhone(String phone) {
    workPhone_ = phone;
  }

  public void setMobilePhone(String phone) {
    mobilePhone_ = phone;
  }

  public void setEmail(String email) {
    email_ = email;
  }

  public void setAge(int age) {
    age_ = age;
  }

  public void setArea(String area) {
    area_ = area;
  }

  public void setBeganWork(idegaTimestamp beganWork) {
    beganWork_ = beganWork;
  }

  public void setEducation(String education) {
    education_ = education;
  }

  public void setImageID(int imageID) {
    imageID_ = imageID;
  }

  public void setTitle(String title) {
    title_ = title;
  }

  public void setMetaAttributes(String[] attributes) {
    metaAttributes_ = attributes;
  }

  public void setMetaValues(String[] values) {
    metaValues_ = values;
  }


  /* Getters */
  public int getUserID() {
    return userID_;
  }

  public String getName() {
    StringBuffer name = new StringBuffer();
    if ( firstName_ != null )
      name.append(firstName_);
    if ( middleName_ != null && "" != middleName_ )
      name.append(" "+middleName_);
    if ( lastName_ != null )
      name.append(" "+lastName_);

    return name.toString();
  }

  public String getFirstName() {
    return firstName_;
  }

  public String getMiddleName() {
    return middleName_;
  }

  public String getLastName() {
    return lastName_;
  }

  public String getWorkPhone() {
    return workPhone_;
  }

  public String getMobilePhone() {
    return mobilePhone_;
  }

  public String getEmail() {
    return email_;
  }

  public int getAge() {
    return age_;
  }

  public String getArea() {
    return area_;
  }

  public idegaTimestamp getBeganWork() {
    return beganWork_;
  }

  public String getEducation() {
    return education_;
  }

  public int getImageID() {
    return imageID_;
  }

  public String getTitle() {
    return title_;
  }

  public String[] getMetaAttributes() {
    return metaAttributes_;
  }

  public String[] getMetaValues() {
    return metaValues_;
  }
}
