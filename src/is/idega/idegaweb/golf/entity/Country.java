//idega 2000 - Eiki

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.*;

public class Country extends GolfEntity{

	public Country(){
		super();	
	}
	
	public Country(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name", "Nafn", true, true, "java.lang.String");
		addAttribute("abbrevation", "Skammstöfun", true, true, "java.lang.String");
		addAttribute("internet_suffix", "internet skammstöfun", true, true, "java.lang.String");
		addAttribute("access_number", "Lykill", true, true, "java.lang.Integer");
	}
	
	public String getEntityName(){
		return "country";
	}
	
	public String getName(){
		return getStringColumnValue("name");		
	}
	
	public void setAbbrevation(String abbrevation){
		setColumn("abbrevation", abbrevation);
	}
	
	public String getAbbrevation(){
		return getStringColumnValue("abbrevation");		
	}
	
	public void setInternetSuffix(String internetSuffix){
		setColumn("internet_suffix", internetSuffix);
	}
	
	public String getInternetSuffix(){
		return getStringColumnValue("internet_suffix");		
	}
	
	public void setName(String name){
		setColumn("name", name);
	}
	
	public int getAccessNumber(){
		return getIntColumnValue("access_number");		
	}
	
	public void setAccessNumber(Integer access_number){
		setColumn("access_number", access_number);
	}
}
