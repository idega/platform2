 package is.idega.idegaweb.golf.service;



 import java.util.StringTokenizer;



 /**

  * Title:

  * Description:

  * Copyright:    Copyright (c) 2001

  * Company:      idega multimedia

  * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>

  * @version 1.0

  */



 public class ReportMember implements Comparable {



  private Name nName;

  private String sSocial;

  private String sEmail;

  private String sAddress;

  private int iBalance;

  private Float fHandicap;

  private String sPhone;

  private String sLocker;

  private String sStatus;



  public ReportMember() {

    this.nName = new Name();

    this.sEmail = "";

    this.sAddress = "";

    this.iBalance = -1;

    this.fHandicap = new Float( -1.0);

    this.sLocker = "";

    this.sPhone = "";

    this.sStatus = "";

  }

  public ReportMember(String first, String middle, String last, String Social,String Address, String Email, int Balance, float Handicap,String Phone,String Locker,String Status) {



      this.nName = new Name(first, middle, last);

      this.sSocial = Social!=null?Social:"";

      this.sEmail = Email!=null?Email:"";

      this.sAddress = Address!=null?Address:"";

      this.iBalance = Balance;

      this.fHandicap = new Float(Handicap);

      this.sLocker = Locker!=null?Locker:"";

      this.sPhone = Phone!=null?Phone:"";

      this.sStatus = Status!=null?Status:"";

  }





  public int compareTo(Object o) {



      // "convert" the objects to members... only members can be compared...

      ListedMember p = (ListedMember) o;

      // check on first name first...

      int result = nName.getFirstName().compareTo(p.getFirstName());



      // if equal, check middle name...

      if (result == 0)

          result = nName.getMiddleName().compareTo(p.getMiddleName());



      // if equal, check last name...

      if (result == 0)

          result = nName.getLastName().compareTo(p.getLastName());



      return result;

  }



  public String getFirstName() {

      return nName.getFirstName();

  }



  public String getMiddleName() {

      return nName.getMiddleName();

  }



  public String getLastName() {

      return nName.getLastName();

  }



  public void setMiddleName(String middle) {

      nName.setMiddleName(middle);

  }



  public void setFirstName(String firstName) {

      nName.setFirstName(firstName);

  }



  public void setLastName(String lastName) {

      nName.setLastName(lastName);

  }



  public String getName() {

      return nName.getName();

  }



  public void setName(String fullName) {

      nName.setName(fullName);

  }



  public String getSocial() {

      return this.sSocial ;

  }



  public String getAddress() {

      return this.sAddress ;

  }



  public String getEmail() {

      return this.sEmail;

  }



  public Float getHandicap() {

      return this.fHandicap;

  }



  public int getBalance() {

      return this.iBalance;

  }



  public String getPhone() {

      return this.sPhone;

  }



  public String getLocker() {

      return this.sLocker ;

  }

  public String getStatus() {

      return this.sStatus ;

  }

  public void setSocial(String social){

    this.sSocial = social;

  }



  public void setAddress(String address) {

      if(address != null)

          this.sAddress = address;

  }



  public void setEmail(String eMail) {

      if(eMail != null)

          this.sEmail = eMail;

  }



  public void setHandicap(float handicap) {

      this.fHandicap = new Float(handicap);

  }



  public void setHandicap(Float handicap) {

      this.fHandicap  = handicap;

  }



  public void setBalance(int Balance) {

      this.iBalance = Balance;

  }



  public void setPhone(String Phone) {

     this.sPhone = Phone;

  }



  public void setLocker(String Locker) {

     this.sLocker = Locker ;

  }



  public void setStatus(String Status) {

     this.sStatus = Status ;

  }



   class Name {

    private String firstName;

    private String middleName;

    private String lastName;



    public Name() {

        this.firstName = "";

        this.lastName = "";

        this.middleName = "";

    }



    public Name(String first, String middle, String last) {



        firstName = "";

        middleName = "";

        lastName = "";

        if(first != null)

            firstName = first;

        if(middle != null)

            middleName = middle;

        if(last != null)

            lastName = last;

    }



    public String getName() {

        if(this.middleName.equals(""))

            return this.firstName+" "+this.lastName;

        else return this.firstName+" "+this.middleName+" "+this.lastName;

    }



    public String getFirstName() {

        return this.firstName;

    }



    public String getMiddleName() {

        return this.middleName;

    }



    public String getLastName() {

        return this.lastName;

    }



    public void setMiddleName(String middle) {

        if(middle != null)

            this.middleName = middle;

    }



    public void setFirstName(String firstName) {

        if(firstName != null)

            this.firstName = firstName;

    }



    public void setLastName(String lastName) {

        if(lastName != null)

            this.lastName = lastName;

    }



    public void setName(String name) {

        StringTokenizer token = new StringTokenizer(name);

        setFirstName(((String)token.nextElement()));

        if(token.countTokens() > 1) {

            setMiddleName(((String)token.nextElement()));

            setLastName(((String)token.nextElement()));

        }

        else if(token.countTokens() > 0)

            setLastName(((String)token.nextElement()));

    }



  }

}
