//idega 2000 - Gimmi

package com.idega.jmodule.calendar.data;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class Fordi extends GenericEntity{

	public Fordi(){
		super();
	}

	public Fordi(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("forda_id_string","forda_id_string",true,true,"java.lang.String");
		addAttribute("member_id","númer notanda",true,true,"java.lang.Integer");
		addAttribute("tegund_nafn","Nafn tegundar",true,true,"java.lang.String");
		addAttribute("deild","deild",true,true,"java.lang.String");
		addAttribute("verkefni","verkefni",true,true,"java.lang.String");
		addAttribute("eining","eining",true,true,"java.lang.String");
		addAttribute("lokadur","lokadur",true,true,"java.lang.Boolean");
	}

	public String getIDColumnName() {
		return "fordi_id";
	}

	public String getEntityName(){
		return "fordi";
	}
	
	public String getName() {
		return getFordaIdString();
	}
	
	public String getFordaIdString() {
		return getStringColumnValue("forda_id_string");
	}
	
	public void setFordaIdString(String forda_id_string) {
		setColumn("forda_id_string",forda_id_string);
	}
	public int getMemberId() {
		return getIntColumnValue("member_id");
	}
	
	public void setMemberId(int member_id) {
		setColumn("member_id",member_id);
	}

/*	public Member getMember() {
		Member member;
		try {
			member = new member(getMemberId());
		}
		catch (Exception e) {
		}

		return member;		
	}
*/	
	public void setTegundNafn(String tegund_nafn) {
		setColumn("tegund_nafn",tegund_nafn);
	}
	
	public String getTegundNafn() {
		return getStringColumnValue("tegund_nafn");
	}
	
	public void setDeild(String deild) {
		setColumn("deild",deild);
	}
	
	public String getDeild() {
		return getStringColumnValue("deild");
	}
	
	public void setVerkefni(String verkefni) {
		setColumn("verkefni",verkefni);
	}
	
	public String getVerkefni() {
		return getStringColumnValue("verkefni");
	}

	public void setEining(String eining) {
		setColumn("eining",eining);
	}
	
	public String getEining() {
		return getStringColumnValue("eining");
	}

	public boolean isLokadur() {
		return ((Boolean)getColumnValue("lokadur")).booleanValue();
	}
	
	public void setLokadur(boolean lokadur) {
		setColumn("lokadur",lokadur);
	}
}
