//idega 2000 - Gimmi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.*;

public class AdCatagory extends GolfEntity{

	public AdCatagory(){
		super();
	}

	public AdCatagory(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("ad_catagory_name","banner_nafn",true,true,"java.lang.String");

                      addManyToManyRelationShip("is.idega.idegaweb.golf.entity.Ad","ad_ad_catagory");
	}
	public String getEntityName(){
		return "ad_catagory";
	}

	public String getAdCatagoryName() {
		return getStringColumnValue("ad_catagory_name");
	}

	public void setAdCatagoryName(String ad_catagory_name) {
		setColumn("ad_catagory_name",ad_catagory_name);
	}

}

