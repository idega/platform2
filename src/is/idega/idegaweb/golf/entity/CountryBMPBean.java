//idega 2000 - Eiki

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

public class CountryBMPBean extends GenericEntity implements Country{

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
	
	public Object ejbFindByAbbreviation(String abbreviation) throws FinderException {
		return idoFindOnePKByColumnBySQL("abbrevation", abbreviation);
	}
	
	public Collection ejbFindAll() throws FinderException {
		return idoFindAllIDsBySQL();
	}
}
