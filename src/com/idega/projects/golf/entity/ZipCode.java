//idega 2000 - Eiki

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;

public class ZipCode extends GolfEntity{

	public ZipCode(){
		super();	
	}
	
	public ZipCode(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("code", "Póstnúmer", true, true, "java.lang.String");
		addAttribute("city", "Borg", true, true, "java.lang.String");
		addAttribute("country_id", "Land", true, true, "java.lang.Integer","one-to-many","com.idega.projects.golf.entity.Country");
	}
	
	public String getEntityName(){
		return "zipcode";
	}
	
	public String getName(){
		return getCity();		
	}
	
	public void setCode(String code){
		setColumn("code", code);
	}
	
	public String getCode(){
		return getStringColumnValue("code");		
	}
	
	public void setCity(String city){
		setColumn("city", city);
	}
	
	public String getCity(){
		return getStringColumnValue("city");		
	}
	
	public void setCountry(Country country){
		setColumn("country_id",country);
	}
	
	public Country getCountry(){
		return (Country)getColumnValue("country_id");
	}
	
	public void setCountryID(int country_id){
		setColumn("country_id",country_id);
	}
	
	public int getCountryID(){
		return getIntColumnValue("country_id");
	}
	
}
