//idega 2000 - Gimmi

package com.idega.projects.projectscene.entity;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class Member extends GenericEntity{

	public Member(){
		super();
	}

	public Member(int id)throws SQLException{
		super(id);
	}


	public String getEntityName(){
		return "member";
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());

		addAttribute("first_name","Fornafn",true,true,"java.lang.String");
		addAttribute("middle_name","Miðnafn",true,true,"java.lang.String");
		addAttribute("last_name","Eftirnafn",true,true,"java.lang.String");
		addAttribute("date_of_birth","Fæðingardagur",true,true,"java.sql.Date");
		addAttribute("gender","Kyn",true,true,"java.lang.String");
		addAttribute("active_member","Staða meðlims",true,true,"java.lang.String");
		addAttribute("member_number","Númer meðlims",true,true,"java.lang.Integer");
		addAttribute("image_id","MyndNúmer",false,false,"java.lang.Integer");
//		addAttribute("image_id","MyndNúmer",false,false,"java.lang.Integer","one-to-many","com.idega.projects.lv.entity.ImageEntity");
		addAttribute("social_security_number","Kennitala",true,true,"java.lang.String");

		//addAttribute("family_id","Fjölskylda",false,true,"java.lang.Integer");

	}

	public void setDefaultValues(){
		setColumn("image_id",1);
	}


	public LoginType[] getLoginType()throws SQLException {
		LoginType logType = new LoginType();
		return (LoginType[]) findRelated(logType);
		//return (Union[])union.findAll("select * from "+union.getEntityName()+" where "+this.getIDColumnName()+"='"+this.getID()+"' ");
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


/*	public ImageEntity getImage(){
		return (ImageEntity)getColumnValue("image_id");
	}
*/

	public Date getDateOfBirth(){
		return (Date) getColumnValue("date_of_birth");
	}

	public void setDateOfBirth(Date dateOfBirth){
		setColumn("date_of_birth",dateOfBirth);
	}

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





	public String getName() {
		String first = getFirstName();
		String middle = getMiddleName();
		String last = getLastName();

		String name = "";
		if (first != null)
			name = name + first;
		if (middle != null)
			name = name + " " + middle;
		if (last != null)
			name = name + " " + last;

		return name;
	}

	public String getFirstName() {
		return (String) getColumnValue("first_name");
	}

	public String getMiddleName() {
		return (String) getColumnValue("middle_name");
	}

	public String getLastName() {
		return (String) getColumnValue("last_name");
	}

	public void setFirstName(String fName) {
		setColumn("first_name",fName);
	}

	public void setMiddleName(String mName) {
		setColumn("middle_name",mName);
	}

	public void setLastName(String lName) {
		setColumn("last_name",lName);
	}

}
