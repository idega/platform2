//idega 2000 - idega team

package com.idega.projects.gonguhrolfur.entity;

//import java.util.*;
import java.sql.*;

public class Subscription extends com.idega.data.GenericEntity {

	public Subscription(){
		super();
	}

	public Subscription(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","Nafn",true,true,"java.lang.String");
		addAttribute("home","Heimilisfang",true,true,"java.lang.String");
		addAttribute("zip","Póstnúmer",true,true,"java.lang.String");
		addAttribute("hphone","Heimasími",true,true,"java.lang.String");
		addAttribute("wphone","Vinnusími",true,true,"java.lang.String");
		addAttribute("gsm","GSM",true,true,"java.lang.String");
		addAttribute("email","Netfang",true,true,"java.lang.String");

       }

	public String getEntityName(){
		return "subscription";
	}

	public String getEmail() {
		return getStringColumnValue("email");
	}

	public void setEmail(String email) {
		setColumn("email",email);
	}

	public String getName(){
		return getStringColumnValue("name");
	}

	public void setName(String name){
		setColumn("name",name);
	}

	public String getHome(){
		return getStringColumnValue("home");
	}

	public void setHome(String home){
		setColumn("home",home);
	}

	public String getZip(){
		return getStringColumnValue("zip");
	}

	public void setZip(String zip){
		setColumn("zip",zip);
	}

	public String getHPhone(){
		return getStringColumnValue("hphone");
	}

	public void setHPhone(String hphone){
		setColumn("hphone",hphone);
	}

	public String getWPhone(){
		return getStringColumnValue("wphone");
	}

	public void setWPhone(String wphone){
		setColumn("wphone",wphone);
	}

	public String getGSM(){
		return getStringColumnValue("gsm");
	}

	public void setGSM(String gsm){
		setColumn("gsm",gsm);
	}

}
