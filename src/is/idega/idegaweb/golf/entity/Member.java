//idega 2000 - idega team

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.*;
import java.util.Vector;

public class Member extends com.idega.data.genericentity.Member {

  public boolean debug = true;

  public Member(){
    super();
  }

  public Member(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    //super.initializeAttributes();
    //par1: column name, par2: visible column name, par3-par4: editable/showable, par5 ...
    addAttribute(getIDColumnName());
    addAttribute("first_name","Fornafn",true,true,"java.lang.String");
    addAttribute("middle_name","Miðnafn",true,true,"java.lang.String");
    addAttribute("last_name","Eftirnafn",true,true,"java.lang.String");
    addAttribute("date_of_birth","Fæðingardagur",true,true,"java.sql.Date");
    addAttribute("gender","Kyn",true,true,"java.lang.String");
    addAttribute(getSocialSecurityNumberColumnName(),"Kennitala",true,true,"java.lang.String");
    addAttribute("email","Tölvupóstur",true,true,"java.lang.String");
    addAttribute("image_id","Mynd",false,false,"java.lang.Integer","one-to-many","com.idega.jmodule.image.data.ImageEntity");
    addAttribute("workplace","Vinnustaður",true,true,"java.lang.String");
    addAttribute("job","Starfsheiti",true,true,"java.lang.String");
    addAttribute("full_name","Fullt nafn",true,true,"java.lang.String");

    addManyToManyRelationShip("is.idega.idegaweb.golf.entity.Address","member_address");
    addManyToManyRelationShip("is.idega.idegaweb.golf.entity.Card","member_card");
    addManyToManyRelationShip("is.idega.idegaweb.golf.entity.Phone","member_phone");
    addManyToManyRelationShip("is.idega.idegaweb.golf.entity.Group","group_member");


  }

  public String getEntityName(){
    return "member";
  }

  public void setDefaultValues(){
    setColumn("image_id",1);
  }


  public String getEmail() {
    return getStringColumnValue("email");
  }

  public void setEmail(String email) {
    setColumn("email",email);
  }

  public static String getSocialSecurityNumberColumnName(){
    return "social_security_number";
  }


  public String getName(){
    StringBuffer nameBuffer = new StringBuffer();
    if ((getFirstName() != null) && (getMiddleName() != null) && (getLastName() != null)){
             nameBuffer.append(getFirstName());
              nameBuffer.append(" ");
               nameBuffer.append(getMiddleName());
                nameBuffer.append(" ");
                 nameBuffer.append(getLastName());
    }
    else if ((getFirstName() != null) && (getLastName() != null)){
       nameBuffer.append(getFirstName());
        nameBuffer.append(" ");
         nameBuffer.append(getLastName());
    }
    else if(getLastName() != null){
      nameBuffer.append(getLastName());
    }
    else if (getFirstName() != null){
      nameBuffer.append(getFirstName());
    }
    return  nameBuffer.toString();
  }

  public String getFirstName(){
    return (String) getColumnValue("first_name");
  }

  public void setFirstName(String first_name){
    setColumn("first_name",first_name);
  }

  public String getMiddleName(){
    return (String) getColumnValue("middle_name");
  }

  public void setMiddleName(String middle_name){
    setColumn("middle_name",middle_name);
  }

  public String getLastName(){
    return (String) getColumnValue("last_name");
  }

  public void setLastName(String last_name){
    setColumn("last_name",last_name);
  }

  public void setFullName(){
     setColumn("full_name",getName());
  }

  public Date getDateOfBirth(){
    return (Date) getColumnValue("date_of_birth");
  }

  public void setDateOfBirth(Date dateOfBirth){
    setColumn("date_of_birth", dateOfBirth);
  }

  /*public char getGender(){
          return getCharColumnValue("gender");
  }

  public void setGender(Character gender){
          setColumn("gender",gender.toString());
  }*/

  public String getGender(){
    return getStringColumnValue("gender");
  }
  public void setGender(String gender){
    setColumn("gender",gender);
  }
  public String getSocialSecurityNumber(){
    return getStringColumnValue(getSocialSecurityNumberColumnName());
  }

  public String getSSN(){
    return getSocialSecurityNumber();
  }

  public void setSocialSecurityNumber(String social_security_number){
    setColumn(getSocialSecurityNumberColumnName(),social_security_number);
  }
  public int getImageId(){
    return getIntColumnValue("image_id");
  }
  public void setimage_id(int image_id){
    setColumn("image_id",image_id);
  }
  public void setimage_id(Integer image_id){
    setColumn("image_id",image_id);
  }
  public void setImageId(int image_id){
    setColumn("image_id",image_id);
  }
  public void setImageId(Integer image_id){
    setColumn("image_id",image_id);
  }
  public com.idega.jmodule.image.data.ImageEntity getImage(){
    return (com.idega.jmodule.image.data.ImageEntity)getColumnValue("image_id");
  }
  public String getJob() {
    return getStringColumnValue("job");
  }
  public void setJob(String job) {
    setColumn("job", job);
  }
  public String getWorkPlace() {
    return getStringColumnValue("workplace");
  }
  public void setWorkPlace(String workPlace) {
    setColumn("workplace", workPlace);
  }

  public MemberInfo getMemberInfo(){
    MemberInfo info=null;
    int id=this.getID();
    try{
      MemberInfo[] arr= null;
      MemberInfo mem = new MemberInfo();
      arr = ( MemberInfo[] ) mem.findAll("select * from member_info where member_id='"+id+"'");

      if( (arr!= null ) && (arr.length > 0) ) info = arr[0];
      //MemberInfo tempMem = new MemberInfo(this.getID());
    }
    catch(SQLException ex){
      System.err.println("is.idega.idegaweb.golf.entity.Member has no MemberInfo for id="+id);
      //info=null;
    }
    return info;
  }

  public UnionMemberInfo getUnionMemberInfo(String union_id)throws SQLException{
    UnionMemberInfo info = new UnionMemberInfo();
    UnionMemberInfo[] infos = null;
    infos = (UnionMemberInfo[]) (info.findAll("select * from union_member_info where member_id='"+this.getID()+"' and union_id='"+union_id+"' "));
    if( (infos!=null) && (infos.length>0)  ) info = (UnionMemberInfo) infos[0];
    else info = null;

    return info;
  }

  public UnionMemberInfo getUnionMemberInfo(String union_id, String member_id)throws SQLException{
    UnionMemberInfo info = new UnionMemberInfo();
    UnionMemberInfo[] infos = null;
    infos = (UnionMemberInfo[]) (info.findAll("select * from union_member_info where member_id='"+member_id+"' and union_id='"+union_id+"' "));
    if( (infos!=null) && (infos.length>0)  ) info = (UnionMemberInfo) infos[0];
    else info = null;

  return info;
  }

  public UnionMemberInfo getUnionMemberInfo(int unionId)throws SQLException{
    return getUnionMemberInfo(Integer.toString(unionId));
  }

  public int getMainUnionID() throws SQLException {
    int skilari=1;

    UnionMemberInfo[] union = (UnionMemberInfo[]) UnionMemberInfo.getStaticInstance("is.idega.idegaweb.golf.entity.UnionMemberInfo").findAll("select * from union_member_info where member_id = "+this.getID());

    for( int i=0; i < union.length ; i++ ){
      if( "main".equalsIgnoreCase(union[i].getMembershipType()) && "A".equalsIgnoreCase(union[i].getMemberStatus())) {
        skilari = union[i].getUnionID();
      }
    }

    try{
      if ( union.length == 0 ) {
        Union[] unionMember = (Union[]) this.findRelated(new Union());
        if ( unionMember.length > 0 ) {
          skilari = unionMember[0].getID();
        }
      }
    }
    catch(SQLException ex){
      ex.printStackTrace();
    }
    return skilari;
  }

  public void setMainUnion(Union union) throws SQLException {
    setMainUnion(union.getID());
  }

  public void setMainUnion(int iUnionId) throws SQLException {

    UnionMemberInfo[] unies = (UnionMemberInfo[]) (new UnionMemberInfo()).findAllByColumn("member_id",this.getID());

    for (int i = 0; i < unies.length; i++) {
      if( unies[i].getID()!= iUnionId )
        unies[i].setMembershipType("sub");
      else {
        unies[i].setMembershipType("main");
        unies[i].setMemberStatus("A");
      }
      unies[i].update();
    }
  }

  public Union getMainUnion()throws SQLException{
    return new Union(getMainUnionID());
  }

  //Many to many relations
  public void setAddress(Address type){
    setColumn("address_id",new Integer(type.getID()));
  }

  public Address[] getAddress()throws SQLException{
    return ((Address[]) findReverseRelated(new Address()));
  }

  public Card[] getCards()throws SQLException{
    return ((Card[]) findReverseRelated(new Card()));
  }

  public Scorecard[] getScorecards()throws SQLException{
    Scorecard scorecard = new Scorecard();
    return (Scorecard[]) scorecard.findAll("select * from "+scorecard.getEntityName()+" where member_id="+this.getID()+"");
  }

  public float getHandicap()throws SQLException{
    if (getMemberInfo() != null ){
            return getMemberInfo().getHandicap();
    }
    else
    {
     return -1;
    }
  }

  //public void addTo(Union union,String MemberShip type){
  //}


  /**
   * @todo needs to be updated and tested, using union_member_info instead of union_member
   */
  public Union[] getUnions()throws SQLException{

    Union union = (Union)Union.getStaticInstance();
    /**
     * New implementation should be:
     *

    UnionMemberInfo[] unioninfos = (UnionMemberInfo[]) UnionMemberInfo.getStaticInstance("is.idega.idegaweb.golf.entity.UnionMemberInfo").findAll("select * from union_member_info where member_id = "+this.getID());
    Vector vect = new Vector();
    for (int i = 0; i < unioninfos.length; i++) {
      vect.add(new Union(unioninfos[i].getUnionID()));
    }
    return (Union[])vect.toArray(new Union[0]);
    */
    return (Union[]) this.findRelated(union);
    //return (Union[])union.findAll("select * from "+union.getEntityName()+" where "+this.getIDColumnName()+"='"+this.getID()+"' ");
  }

  public boolean isMemberIn(Union union)throws SQLException{
    return isMemberInUnion(union);
  }

  public boolean isMemberInUnion(Union union)throws SQLException{
      int numRecords = getNumberOfRecords("select count (member_id) from union_member_info where union_id = "+union.getID()+" and member_id = "+this.getID());

      return (numRecords > 0);
  }

  public boolean isMemberInUnion()throws SQLException{
      int numRecords = getNumberOfRecords("select count (member_id) from union_member_info where member_id = "+this.getID());
      return (numRecords > 0);
  }

  public Phone[] getPhone()throws SQLException {
          return (Phone[]) this.findReverseRelated(new Phone());
  }

  public Member[] getFamilyMembers(int FamilyId)throws SQLException{
    String sql = "select member.* from member m,union_member_info u where m.member_id = u.member_id and u.family_id = "+FamilyId;
    Member[] mbs = (Member[])this.findAll(sql);
    return mbs;
  }

  /** @todo   needs to be updated */

  public void delete()throws SQLException{
    Connection conn= null;
    Statement Stmt= null;
    try{
      conn = getConnection();

      try{
        Address address = new Address();
        Address[] addresses = (Address[])findReverseRelated(address);
        Stmt = conn.createStatement();
        Stmt.executeUpdate("delete from "+getNameOfMiddleTable(this,address)+" where "+getIDColumnName()+"='"+getID()+"'");

        for(int i = 0; i < addresses.length; i++){
                addresses[i].delete();
        }
      }
      catch(SQLException ex){   if(debug) ex.printStackTrace();   }

      try{
        Phone phone = new Phone();
        Phone[] phones = (Phone[])findReverseRelated(phone);
        Stmt = conn.createStatement();
        Stmt.executeUpdate("delete from "+getNameOfMiddleTable(this,phone)+" where "+getIDColumnName()+"='"+getID()+"'");

        for(int i = 0; i < phones.length; i++){
                phones[i].delete();
        }
      }
      catch(SQLException ex){  if(debug) ex.printStackTrace();   }

      try{
        Card card = new Card();
        Card[] cards = (Card[])findReverseRelated(card);
        Stmt = conn.createStatement();
        Stmt.executeUpdate("delete from "+getNameOfMiddleTable(this,card)+" where "+getIDColumnName()+"='"+getID()+"'");
        for(int i = 0; i < cards.length; i++){
                cards[i].delete();
        }
      }
      catch(SQLException ex){   if(debug) ex.printStackTrace();   }

      try{
        Tournament tournament = new Tournament();
        Stmt = conn.createStatement();
        Stmt.executeUpdate("delete from "+getNameOfMiddleTable(tournament,this)+" where "+getIDColumnName()+"='"+getID()+"'");
      }
      catch(SQLException ex){    if(debug) ex.printStackTrace(); }

      try{
        Group group = new Group();
        Stmt = conn.createStatement();
        Stmt.executeUpdate("delete from "+getNameOfMiddleTable(group,this)+" where "+getIDColumnName()+"='"+getID()+"'");
      }
      catch(SQLException ex){   if(debug) ex.printStackTrace();  }

      try{
        Union union = new Union();
        Stmt = conn.createStatement();
        Stmt.executeUpdate("delete from "+getNameOfMiddleTable(union,this)+" where "+getIDColumnName()+"='"+getID()+"'");
      }
      catch(SQLException ex){    if(debug) ex.printStackTrace();  }

      try{
        MemberInfo info = getMemberInfo();
        //MemberInfo info = new MemberInfo(this.getID());
        if(info!= null)
          info.delete();
      }
      catch(SQLException ex){    if(debug) ex.printStackTrace();   }

    //Warn against if there are others with the same family
     /*
      try{
              Family family = this.getFamily();
              family.delete();
      }
      catch(SQLException ex){   if(debug) ex.printStackTrace();  }
      */
/*
      try{
              com.idega.jmodule.image.data.ImageEntity image = this.getImage();
              image.delete();
      }
      catch(SQLException ex){   if(debug) ex.printStackTrace(); }
*/
      try{
        Scorecard[] scores = (Scorecard[])(new Scorecard()).findAllByColumn("member_id",this.getID());
        for (int i = 0; i < scores.length;i++){
          Stroke	stroke = new Stroke();
          Stroke[] strokes = (Stroke[]) stroke.findAllByColumn("scorecard_id",scores[i].getID());
          for (int n = 0; n < strokes.length;n++){
            strokes[n].delete();
          }
          scores[i].delete();
        }
      }
      catch(SQLException ex){    if(debug) ex.printStackTrace();  }

      try{
        Startingtime time = new Startingtime();
        Startingtime[] times = (Startingtime[])time.findAllByColumn("member_id",this.getID());
        for (int i = 0; i < times.length; i++){
          int id = times[i].getID();
          Stmt = conn.createStatement();
          Stmt.executeUpdate("delete from tournament_startingtime where STARTINGTIME_ID ='"+id+"'");
          times[i].delete();
        }
      }
      catch(SQLException ex){   if(debug) ex.printStackTrace();   }

      try{
        Payment.getStaticInstance("is.idega.idegaweb.golf.entity.Payment").deleteMultiple("member_id",Integer.toString(this.getID()));
      }
      catch(SQLException ex){   if(debug) ex.printStackTrace();   }

      try{
        Account[] account = (Account[]) Account.getStaticInstance("is.idega.idegaweb.golf.entity.Account").findAllByColumn("member_id",this.getID());
        for ( int a = 0; a < account.length; a++ ) {
          AccountEntry.getStaticInstance("is.idega.idegaweb.golf.entity.AccountEntry").deleteMultiple("account_id",Integer.toString(account[a].getID()));
          account[a].delete();
        }
      }
      catch(SQLException ex){   if(debug) ex.printStackTrace();   }

      try{
        LoginTable.getStaticInstance("is.idega.idegaweb.golf.entity.LoginTable").deleteMultiple("member_id",Integer.toString(this.getID()));
      }
      catch(SQLException ex){   if(debug) ex.printStackTrace();   }

      try{
        super.delete();
      }
      catch(SQLException ex){    if(debug) ex.printStackTrace();  }
    }
    finally{
      if (Stmt != null){
              Stmt.close();
      }
      if (conn != null){
              freeConnection(conn);
      }
    }

  }

  public Group[] getGroups()throws SQLException{
          Group group = new Group();
          return (Group[]) findRelated(group);
          //return (Union[])union.findAll("select * from "+union.getEntityName()+" where "+this.getIDColumnName()+"='"+this.getID()+"' ");
  }


  public static com.idega.data.genericentity.Member getMember(String socialSecurityNumber) {
      com.idega.data.genericentity.Member returner = null;
      try {
          java.util.List members = com.idega.data.EntityFinder.findAllByColumn(new is.idega.idegaweb.golf.entity.Member(),"SOCIAL_SECURITY_NUMBER",socialSecurityNumber);
          if (members != null) {
              if (members.size()  > 0) {
                  returner = (com.idega.data.genericentity.Member) members.get( (members.size() -1));
              }
          }
      }
      catch (SQLException sq) {
          sq.printStackTrace(System.err);
      }

      return returner;
  }

  public static com.idega.data.genericentity.Member getStaticInstance(){
    return (com.idega.data.genericentity.Member)getStaticInstance("is.idega.idegaweb.golf.entity.Member");
  }

  public void insert() throws SQLException{
    setFullName();
    super.insert();
  };

    public void insertStartData(){
      //Administrator member created in LoginTable

    }

}
