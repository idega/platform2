//idega 2000 - Gimmi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.*;

public class Banner extends GolfEntity{

	public Banner(){
		super();
	}

	public Banner(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("ad_id","númer auglýsingar",true,true, "java.lang.Integer");
	}

	public String getIDColumnName(){
		return "banner_id";
	}

	public String getEntityName(){
		return "banner_ad";
	}


	public int getID() {
		return getIntColumnValue("banner_id");
	}

	public int getAdID() {
		return getIntColumnValue("ad_id");
	}

	public void setAdID(int ad_id) {
		setColumn("ad_id",ad_id);
	}



}
