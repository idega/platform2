//idega 2000 - Gimmi

package com.idega.jmodule.calendar.data;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class Faerslur extends GenericEntity{

	public Faerslur(){
		super();
	}

	public Faerslur(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("faerslur_date","date",true,true,"java.sql.Timestamp");
		addAttribute("owner_id","owner_id",true,true,"java.lang.String");
		addAttribute("forda_id","forda_id_string",true,true,"java.lang.String");
		addAttribute("verk_id","verk_nafn_stutt",true,true,"java.lang.String");
		addAttribute("timafjoldi","timafjoldi",true,true,"java.lang.Double");
		addAttribute("comment","komment",true,true,"java.lang.String");
		addAttribute("booked","bókað",true,true,"java.lang.Boolean");
		addAttribute("registered","skráð",true,true,"java.lang.Boolean");
	}

	public String getIDColumnName() {
		return "faerslu_id";
	}

	public String getEntityName(){
		return "faerslur";
	}

	public java.sql.Timestamp getDate(){		
		return (java.sql.Timestamp) getColumnValue("faerslur_date");		
	}

	public void setDate(java.sql.Timestamp date){
			setColumn("faerslur_date", date);	
	}

	public String getOwnerId() {
		return getStringColumnValue("owner_id");
	}

	public void setOwnerId(String owner_id) {
		setColumn("owner_id",owner_id);
	}

	public String getVerkNafnStutt() {
		return getStringColumnValue("verk_id");
	}
	
	public void setVerkNafnStutt(String verk_nafn_stutt) {
		setColumn("verk_id",verk_nafn_stutt);
	}

	public String getName() {
		return getFordiIdString();
	}

	public String getFordiIdString() {
		return getStringColumnValue("forda_id");
	}
	
	public void setFordiIdString(String forda_id_string) {
		setColumn("forda_id",forda_id_string);
	}

	public double getTimafjoldi() {
		return ((Double)getColumnValue("timafjoldi")).doubleValue();
	}

	public void setTimafjoldi(double timafjoldi) {
		setColumn("timafjoldi",new Double(timafjoldi));
	}

	public String getComment() {
		return getStringColumnValue("comment");
	}	
	
	public void setComment(String comment) {
		setColumn("comment",comment);
	}

	public boolean isBooked() {
		return ((Boolean)getColumnValue("booked")).booleanValue();
	}
	
	public void setBooked(boolean booked) {
		setColumn("booked",booked);
	}

	public boolean isRegistered() {
		return ((Boolean)getColumnValue("registered")).booleanValue();
	}
	
	public void setRegistered (boolean registered) {
		setColumn("registered",registered);
	}
	
}
