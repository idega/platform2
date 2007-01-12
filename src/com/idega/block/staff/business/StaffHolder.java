package com.idega.block.staff.business;



import com.idega.core.user.data.User;
import com.idega.util.IWTimestamp;



public class StaffHolder {



  private User _user = null;

  //private int userID_ = -1;

  //private String firstName_ = null;

  //private String middleName_ = null;

  //private String lastName_ = null;

  private String workPhone_ = null;

  private String mobilePhone_ = null;

  private String email_ = null;

  private IWTimestamp beganWork_ = null;

  private String title_ = null;

  private String education_ = null;

  private String area_ = null;

  private String[] metaAttributes_ = null;

  private String[] metaValues_ = null;



  private int imageID_ = -1;

  private int age_ = -1;



  public StaffHolder( User user) {

    this._user = user;

  }





  /* Setters */

  /*

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

*/

  public void setWorkPhone(String phone) {

    this.workPhone_ = phone;

  }



  public void setMobilePhone(String phone) {

    this.mobilePhone_ = phone;

  }



  public void setEmail(String email) {

    this.email_ = email;

  }



  public void setAge(int age) {

    this.age_ = age;

  }



  public void setArea(String area) {

    this.area_ = area;

  }



  public void setBeganWork(IWTimestamp beganWork) {

    this.beganWork_ = beganWork;

  }



  public void setEducation(String education) {

    this.education_ = education;

  }



  public void setImageID(int imageID) {

    this.imageID_ = imageID;

  }



  public void setTitle(String title) {

    this.title_ = title;

  }



  public void setMetaAttributes(String[] attributes) {

    this.metaAttributes_ = attributes;

  }



  public void setMetaValues(String[] values) {

    this.metaValues_ = values;

  }





  /* Getters */

  public int getUserID() {

    //return userID_;

    return this._user.getID();

  }



  public String getName() {

 /*   StringBuffer name = new StringBuffer();

    if ( firstName_ != null )

      name.append(firstName_);

    if ( middleName_ != null && "" != middleName_ )

      name.append(" "+middleName_);

    if ( lastName_ != null )

      name.append(" "+lastName_);



    return name.toString();

    */



    return this._user.getName();

  }



  public String getFirstName() {

    return this._user.getFirstName();

    //return firstName_;

  }



  public String getMiddleName() {

    return this._user.getMiddleName();

    //return middleName_;

  }



  public String getLastName() {

    return this._user.getLastName();

    //return lastName_;

  }



  public String getWorkPhone() {

    return this.workPhone_;

  }



  public String getMobilePhone() {

    return this.mobilePhone_;

  }



  public String getEmail() {

    return this.email_;

  }



  public int getAge() {

    return this.age_;

  }



  public String getArea() {

    return this.area_;

  }



  public IWTimestamp getBeganWork() {

    return this.beganWork_;

  }



  public String getEducation() {

    return this.education_;

  }



  public int getImageID() {

    return this.imageID_;

  }



  public String getTitle() {

    return this.title_;

  }



  public String[] getMetaAttributes() {

    return this.metaAttributes_;

  }



  public String[] getMetaValues() {

    return this.metaValues_;

  }



  public int getGroupID(){

    return this._user.getGroupID();

  }



}

