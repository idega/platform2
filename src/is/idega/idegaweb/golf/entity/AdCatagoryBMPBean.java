//idega 2000 - Gimmi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import com.idega.data.GenericEntity;

public class AdCatagoryBMPBean extends GenericEntity implements AdCatagory {

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("ad_catagory_name","banner_nafn",true,true,"java.lang.String");

                      addManyToManyRelationShip(is.idega.idegaweb.golf.entity.Ad.class,"ad_ad_catagory");
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

