//idega 2000 - Gimmi

package is.idega.idegaweb.golf.block.login.data;

//import java.util.*;
import com.idega.data.GenericEntity;

public class LoginTypeBMPBean extends GenericEntity implements LoginType{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","nafn",true,true,"java.lang.String");
		addAttribute("login_type","Týpa logins",true,true,"java.lang.String");
		addAttribute("description","Lýsing",true,true,"java.lang.String");
		addAttribute("extra_info","Aðrar upplýsingar",true,true,"java.lang.String");

	}

	public String getIDColumnName() {
		return "login_type_id";
	}

	public String getEntityName(){
		return "login_type";
	}

	public String getName() {
		return getStringColumnValue("name");
	}

	public String getLoginType() {
		return getStringColumnValue("login_type");
	}

	public String getDescription() {
		return getStringColumnValue("description");
	}

	public String getExtraInfo() {
		return getStringColumnValue("extra_info");
	}


}
