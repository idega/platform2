package is.idega.idegaweb.campus.business;






public class HabitantsCollector {

  private int userID_ = -1;

  private String firstName_ = null;

  private String middleName_ = null;

  private String lastName_ = null;

  private String name_ = null;

  private String apartment_ = null;

  private String floor_ = null;

  private String phone_ = null;

  private String email_ = null;

  private String address_ = null;



  public HabitantsCollector() {

  }



  public void setUserID(int userID) {

    userID_ = userID;

  }



  public void setName(String name){

    name_ = name;

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



  public void setApartment(String apartment) {

    apartment_ = apartment;

  }



  public void setFloor(String floor) {

    floor_ = floor;

  }



  public void setPhone(String phone) {

    phone_ = phone;

  }



  public void setEmail(String email) {

    email_ = email;

  }



  public void setAddress(String address) {

    this.address_ = address;

  }



  public int getUserID() {

    return userID_;

  }



  public String getName() {

    /*

    StringBuffer name = new StringBuffer();

    if ( firstName_ != null )

      name.append(firstName_);

    if ( middleName_ != null && "" != middleName_ )

      name.append(" "+middleName_);

    if ( lastName_ != null )

      name.append(" "+lastName_);



    return name.toString();

    */

    return name_;

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



  public String getApartment() {

    return apartment_;

  }



  public String getFloor() {

    return floor_;

  }



  public String getPhone() {

    return phone_;

  }



  public String getEmail() {

    return email_;

  }

  public String getAddress() {

    return address_;

  }

}

