//idega 2000 - Ægir extends Tryggvi Larussson

package is.idega.idegaweb.golf.entity;
import com.idega.data.*;

//import java.util.*;
import java.sql.*;

public class MemberOverView extends GolfEntity{

	public MemberOverView(){
		super();	
	}
	
	public MemberOverView(int id)throws SQLException{
		super(id);
	}
	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("first_name", "", true, true, "java.lang.String");
		addAttribute("middle_name", "", true, true, "java.lang.String");
		addAttribute("last_name", "", true, true, "java.lang.String");
		addAttribute("social_security_number", "", true, true, "java.lang.String");
		addAttribute("union_name", "", true, true, "java.lang.String");
		addAttribute("handicap", "", true, true, "java.lang.Float");
		addAttribute("name", "", true, true, "java.lang.String");
		addAttribute("active_member","Staða meðlims",true,true,"java.lang.String");
		//addAttribute("Greiðaslustaða");
	}
	
	public String getEntityName(){
		return "member_list";
	}
	
	public String getIDColumnName(){
		return "member_list_id";
	}
	
	public GenericEntity[] findAll()throws SQLException{
		return findAll("SELECT * FROM MEMBER_LIST");
	}
	
	public String getUnionName(){
		return getStringColumnValue("union_name");
	}
	
	public float getHandicap(){
		return getFloatColumnValue("handicap");
	}
	
	public String getName(){
		String returnString = "";
		if ((getFirstName() != null) && (getMiddleName() != null) && (getLastName() != null)){
			returnString = getFirstName()+" "+getMiddleName()+" "+getLastName();
		}
		else if ((getFirstName() != null) && (getLastName() != null)){
			returnString = getFirstName()+" "+getLastName();
		}
		else if(getLastName() != null){
			returnString = getLastName();
		}
		else if (getFirstName() != null){
			returnString = getFirstName();
		}
		return returnString;
	}
	
	public String getFirstName(){
		return getStringColumnValue("first_name");	
	}
	
	public String getMiddleName(){
		return getStringColumnValue("middle_name");	
	}
	
	public String getLastName(){
		return getStringColumnValue("last_name");	
	}

	public String getMemberStatus(){
		return getStringColumnValue("active_member");	//breyta nafni i grunni
	}
	
	public String getSocialSecurityNumber(){
		return getStringColumnValue("social_security_number");		
	}
	
	public String getFamilyName(){
		return getStringColumnValue("name");	
	}

}
