//idega 2000 - idega team

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;

public class Member extends com.idega.data.genericentity.Member {

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
		//addAttribute("active_member","Staða meðlims",true,true,"java.lang.String");
		addAttribute("social_security_number","Kennitala",true,true,"java.lang.String");
		//addAttribute("member_number","Númer meðlims",true,true,"java.lang.Integer");
		//addAttribute("family_id","Fjölskylda",false,false,"java.lang.Integer","one-to-many","com.idega.projects.golf.entity.Family");
                addAttribute("email","Tölvupóstur",true,true,"java.lang.String");
                addAttribute("image_id","MyndNúmer",false,false,"java.lang.Integer","one-to-many","com.idega.projects.golf.entity.ImageEntity");
                addAttribute("workplace","Vinnustaður",true,true,"java.lang.String");
                addAttribute("job","Starfsheiti",true,true,"java.lang.String");

		//addAttribute("family_id","Fjölskylda",false,true,"java.lang.Integer");
               /* addAttribute("payment_type_id","Greiðslumáti",true,true,"java.lang.Integer","one-to-one","com.idega.projects.golf.entity.PaymentType");
                addAttribute("preferred_installment_nr","Greiðslufjöldi",true,true,"java.lang.Integer");
		  addAttribute("comment","Upplýsingar",true,true,"java.lang.String");
                addAttribute("locker_number","Upplýsingar",true,true,"java.lang.String");
                addAttribute("visible","Sjáanlegur á vef",true,true,"java.lang.String");
                addAttribute("first_installment_date","Dagsetning fyrstu borgunar",true,true,"java.sql.Date");*/

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

	public String getMemberStatus(){
		return getStringColumnValue("active_member");	//breyta nafni i grunni
	}

	public void setMemberStatus(Character active_member){
		setColumn("active_member",active_member);
		//A: active , I:inactive, H:On hold
	}

	public String getSocialSecurityNumber(){
		return getStringColumnValue("social_security_number");
	}

	public void setSocialSecurityNumber(String social_security_number){
		setColumn("social_security_number",social_security_number);
	}

	public int getMemberNumber(){
		return getIntColumnValue("member_number");
	}

	public void setMemberNumber(Integer member_number){
		setColumn("member_number",member_number);
	}

	public void setMemberNumber(int member_number){
		setColumn("member_number",member_number);
	}

	public Family getFamily(){
		return (Family) getColumnValue("family_id");
	}

	public int getFamilyId(){
		return getIntColumnValue("family_id");
	}

	public void setFamilyId(int family_id){
		setColumn("family_id",family_id);
	}

	public void setFamilyId(Integer family_id){
		setColumn("family_id",family_id);
	}

	public void setFamily(Family family){
		setColumn("family_id",family);
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

        public int getPaymentTypeID(){
		return getIntColumnValue("payment_type_id");
	}

	public void setPaymentTypeID(Integer payment_type_id){
		setColumn("payment_type_id",payment_type_id);
	}

        public int getPreferredInstallmentNr(){
		return getIntColumnValue("preferred_installment_nr");
	}

	public void setPreferredInstallmentNr(Integer preferred_installment_nr){
		setColumn("preferred_installment_nr",preferred_installment_nr);
	}


	public ImageEntity getImage(){
		return (ImageEntity)getColumnValue("image_id");
	}


        public void setComment(String comment){
          setColumn("comment",comment);
        }

        public String getComment(){
          return (String) this.getStringColumnValue("comment");
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

        public String getLockerNumber() {
            return getStringColumnValue("locker_number");
        }

        public void setLockerNumber(String lockerNumber) {
            setColumn("locker_number", lockerNumber);
        }

        public String getVisible(){
            return getStringColumnValue("visible");	//breyta nafni i grunni
	}

        public void setVisible(char visible){
            Character character = new Character(visible);
	    setColumn("visible", character.toString());
	}

        public void setVisible(boolean visible) {
          if (visible) {
              setColumn("visible","Y");
          }
          else{
              setColumn("visible","N");
          }
        }

        public void setVisible(String visible){
		setColumn("visible",visible);
	}

        public Date getFirstInstallmentDate() {
            return (Date) getColumnValue("first_installment_date");
        }

        public void setFirstInstallmentDate(Date date) {
            setColumn("first_installment_date", date);
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
            System.err.println("com.idega.projects.golf.entity.Member has no MemberInfo for id="+id);
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



          Union[] union = (Union[]) this.findRelated(new Union());

          for( int i=0; i < union.length ; i++ ){
            UnionMemberInfo uni = this.getUnionMemberInfo(union[i].getID());
            if(uni!=null){
              String type = uni.getMembershipType();
              if( "main".equalsIgnoreCase(type) ) skilari = union[i].getID();
            }
            //else if( (uni==null) && (union.length==1) ){
              else{
              //for club admins that do not have a unionMemberInfo
              skilari =  union[0].getID();
            }

          }

/*
      System.out.print("Byrja að leita...");
          Connection conn = getConnection();
          Statement Stmt = conn.createStatement();
          ResultSet RS = Stmt.executeQuery("select union_id from union_member_info where member_id = "+this.getID()+" and membership_type='main'");

          if (RS.next()){
                  skilari = RS.getInt("union_id");
          }
          RS.close();


          Stmt.close();

          if (conn != null){
            freeConnection(conn);
          }

          System.out.println("fann "+skilari);
*/
          return skilari;

	}

        public void setMainUnion(Union union) throws SQLException {
// crap!
            Connection conn = getConnection();
            Statement stmtInitialize = conn.createStatement();
            Statement stmtSetAsMain = conn.createStatement();


            stmtInitialize.executeUpdate("update union_member_info set membership_type = 'sub' where member_id = "+getID());

            if(isMemberIn(union))
                stmtSetAsMain.executeUpdate("update union_member set membership_type = 'main' where member_id = "+getID()+" and union_id = "+union.getID());
            else
                addTo(union, "membership_type", "main");

            stmtInitialize.close();
            stmtSetAsMain.close();

            if (conn != null){
              freeConnection(conn);
            }
            //debug temporary fix
            UnionMemberInfo uni = this.getUnionMemberInfo(union.getID());
            if ( uni!=null ) uni.setMemberStatus("A");

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

        public Card getCard(){
		return new Card();
	}

        public int getCardId() {
            return getIntColumnValue("card_id");
        }

        public void setCardId(int cardId) {
            setColumn("card_id", cardId);
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

	public Union[] getUnions()throws SQLException{
		Union union = new Union();
		return (Union[]) findRelated(union);
		//return (Union[])union.findAll("select * from "+union.getEntityName()+" where "+this.getIDColumnName()+"='"+this.getID()+"' ");
	}

	public boolean isMemberIn(Union union)throws SQLException{
            int numRecords = getNumberOfRecords("select count (member_id) from union_member where union_id = "+union.getID()+" and member_id = "+this.getID());

            return (numRecords > 0);
	}

        public boolean isMemberInUnion()throws SQLException{
            int numRecords = getNumberOfRecords("select count (member_id) from union_member where member_id = "+this.getID());
            return (numRecords > 0);
	}

	public Phone[] getPhone()throws SQLException {
		return (Phone[]) this.findReverseRelated(new Phone());
	}

//debug needs to be updated

	public void delete()throws SQLException{
		Connection conn= null;
		Statement Stmt= null;
		try{
			conn = getConnection();

			Address address = new Address();
			Address[] addresses = (Address[])findReverseRelated(address);

			Stmt = conn.createStatement();
			Stmt.executeUpdate("delete from "+getNameOfMiddleTable(this,address)+" where "+getIDColumnName()+"='"+getID()+"'");

			for(int i = 0; i < addresses.length; i++){
				addresses[i].delete();
			}


			Phone phone = new Phone();
			Phone[] phones = (Phone[])findReverseRelated(phone);
			Stmt = conn.createStatement();
			Stmt.executeUpdate("delete from "+getNameOfMiddleTable(this,phone)+" where "+getIDColumnName()+"='"+getID()+"'");

			for(int i = 0; i < phones.length; i++){
				phones[i].delete();
			}

			Card card = new Card();
			Card[] cards = (Card[])findReverseRelated(card);
			Stmt = conn.createStatement();
			Stmt.executeUpdate("delete from "+getNameOfMiddleTable(this,card)+" where "+getIDColumnName()+"='"+getID()+"'");

			for(int i = 0; i < cards.length; i++){
				cards[i].delete();
			}




			Tournament tournament = new Tournament();
			Stmt = conn.createStatement();
			Stmt.executeUpdate("delete from "+getNameOfMiddleTable(tournament,this)+" where "+getIDColumnName()+"='"+getID()+"'");


			Group group = new Group();
			Stmt = conn.createStatement();
			Stmt.executeUpdate("delete from "+getNameOfMiddleTable(group,this)+" where "+getIDColumnName()+"='"+getID()+"'");


			Union union = new Union();
			Stmt = conn.createStatement();
			Stmt.executeUpdate("delete from "+getNameOfMiddleTable(union,this)+" where "+getIDColumnName()+"='"+getID()+"'");


			try{
				MemberInfo info = new MemberInfo(this.getID());
				info.delete();
			}
			catch(SQLException ex){

			}

			//Warn against if there are others with the same family
			try{
				Family family = this.getFamily();
				family.delete();
			}
			catch(SQLException ex){

			}


			try{
				ImageEntity image = this.getImage();
				image.delete();
			}
			catch(SQLException ex){

			}


			Scorecard[] scores = (Scorecard[])(new Scorecard()).findAllByColumn("member_id",this.getID());
			for (int i = 0; i < scores.length;i++){

				Stroke	stroke = new Stroke();

				Stroke[] strokes = (Stroke[]) stroke.findAllByColumn("scorecard_id",scores[i].getID());

				for (int n = 0; n < strokes.length;n++){
					strokes[n].delete();
				}

				scores[i].delete();
			}


			Startingtime time = new Startingtime();
			Startingtime[] times = (Startingtime[])time.findAllByColumn("member_id",this.getID());

			for (int i = 0; i < times.length; i++){
				times[i].delete();
			}


			super.delete();
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

/*
	public void insert()throws SQLException{
		if (getFamily() == null){
			Family family = new Family();
			family.insert();
			this.setFamily(family);
		}

		super.insert();
	}*/




	public Group[] getGroups()throws SQLException{
		Group group = new Group();
		return (Group[]) findRelated(group);
		//return (Union[])union.findAll("select * from "+union.getEntityName()+" where "+this.getIDColumnName()+"='"+this.getID()+"' ");
	}


}
