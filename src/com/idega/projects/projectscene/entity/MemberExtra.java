//idega 2000 - Gimmi

package com.idega.projects.projectscene.entity;
//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class MemberExtra extends GenericEntity{

	public MemberExtra(){
		super();
	}

	public MemberExtra(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		//addAttribute("first_name","Fornafn",true,true,"java.lang.String");
		addAttribute("title","Titill",true,true, "java.lang.String");
		addAttribute("education","Menntun",true,true, "java.lang.String",3500);
		addAttribute("school","Skóli",true,true, "java.lang.String",3500);
		addAttribute("cv","Starfsferill",true,true, "java.lang.String",3500);
		addAttribute("date_of_birth","Fæðingardagur",true,true, "java.lang.String");
		addAttribute("began_work","hóf störf", true, true, "java.lang.String");
	}

	public String getEntityName(){
		return "member_extra";
	}


	public void setBeganWork(String began_work) {
		setColumn("began_work",began_work);
	}

	public String getBeganWork() {
		return getStringColumnValue("began_work");
	}


	public void setDateOfBirth(String date_of_birth) {
		setColumn("date_of_birth",date_of_birth);
	}

	public String getDateOfBirth() {
		return getStringColumnValue("date_of_birth");
	}



	public String getName(){
		String returner;
		if (getID() == -1) {
			returner = "óskírð / ur";
		}
		else {
			returner = getMember().getName();
		}

		return returner;
	}

	public Member getMember() {
		return (new Member());
	}

	public String getTitle() {
		return getStringColumnValue("title");
	}

	public void setTitle(String title) {
		setColumn("title",title);
	}

	public String getEducation() {
		return getStringColumnValue("education");
	}

	public void setEducation(String education) {
		setColumn("education",education);
	}

	public String getSchool() {
		return getStringColumnValue("school");
	}

	public void setSchool(String school) {
		setColumn("school",school);
	}

	public String getCV() {
		return getStringColumnValue("cv");
	}

	public void setCV(String cv) {
		setColumn("cv",cv);
	}

}
