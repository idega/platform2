 package is.idega.idegaweb.golf.service;



 import com.idega.presentation.Image;

 import java.util.*;

 import is.idega.idegaweb.golf.entity.*;

 import java.sql.SQLException;





 public class ListedMember implements Comparable {



  private int id;

  private Name name;

  private String eMail;

  private String address;

  private Character payment;

  private Float handicap;

  private Image img = new Image();



  public ListedMember() {

      id = 0;

      name = new Name();

      eMail = " - ";

      address = " - ";

      payment = new Character('o');

      handicap = new Float(-1);

      img = null;

  }



  public ListedMember(int memberId, String first, String middle, String last, String addr, String e_mail, Character pay, Float handi) {



      id = memberId;

      name = new Name(first, middle, last);

      eMail = "";

      address = "";

      payment = new Character('o');

      handicap = new Float(-1);

      if(addr != null)

          this.address = addr;

      if(e_mail != null)

          this.eMail = e_mail;

      if(pay != null)

          this.payment = pay;

      if(handi != null)

          this.handicap = handi;

  }



  public ListedMember(Member member) throws SQLException{

      if(member.getName() != null)

          setName(member.getName());

      if(member.getAddress()[0].getName() != null)

          setAddress(member.getAddress()[0].getName());

      if(member.getEmail() != null)

          setEmail(member.getEmail());

      setHandicap(member.getHandicap());

      setID(member.getID());

  }



  public ListedMember(int memberId, String first, String middle, String last, String addr, String e_mail, char pay, float handi) {



      id = memberId;

      name = new Name(first, middle, last);

      eMail = "";

      address = "";

      payment = new Character(pay);

      handicap = new Float(handi);

      if(addr != null)

          this.address = addr;

      if(e_mail != null)

          this.eMail = e_mail;

  }



  public ListedMember(int memberId, String fullName, String addr, String e_mail, char pay, float handi) {



      id = memberId;

      name = new Name();

      name.setName(fullName);

      eMail = "";

      address = "";

      payment = new Character(pay);

      handicap = new Float(handi);

      if(addr != null)

          this.address = addr;

      if(e_mail != null)

          this.eMail = e_mail;

  }



  public int compareTo(Object o) {



      // "convert" the objects to members... only members can be compared...

      ListedMember p = (ListedMember) o;

      // check on first name first...

      int result = name.getFirstName().compareTo(p.getFirstName());



      // if equal, check middle name...

      if (result == 0)

          result = name.getMiddleName().compareTo(p.getMiddleName());



      // if equal, check last name...

      if (result == 0)

          result = name.getLastName().compareTo(p.getLastName());



      return result;

  }



  public int getID() {

      return id;

  }



  public String getFirstName() {

      return name.getFirstName();

  }



  public String getMiddleName() {

      return name.getMiddleName();

  }



  public String getLastName() {

      return name.getLastName();

  }



  public void setMiddleName(String middle) {

      name.setMiddleName(middle);

  }



  public void setFirstName(String firstName) {

      name.setFirstName(firstName);

  }



  public void setLastName(String lastName) {

      name.setLastName(lastName);

  }



  public String getName() {

      return name.getName();

  }



  public void setName(String fullName) {

      name.setName(fullName);

  }



  public String getAddress() {

      return address;

  }



  public String getEmail() {

      return eMail;

  }



  public Float getHandicap() {

      return handicap;

  }



  public Character getPayment() {

      return this.payment;

  }



  public Image getImage() {

      return img;

  }



  public void setImage(Image img, char payment) {

      this.img = img;

      this.payment = new Character(payment);

  }



  public void setAddress(String address) {

      if(address != null)

          this.address = address;

  }



  public void setEmail(String eMail) {

      if(eMail != null)

          this.eMail = eMail;

  }



  public void setHandicap(float handicap) {

      this.handicap = new Float(handicap);

  }



  public void setHandicap(Float handicap) {

      this.handicap = handicap;

  }



  /*public void setPayment(Character payment) {

      this.payment = payment;

  }*/



  public void setPayment(char payment) {

      this.payment = new Character(payment);

  }



  public void setID(int memberId) {

      id = memberId;

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
