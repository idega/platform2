//idega 2000 - Gimmi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import com.idega.data.GenericEntity;

public class BannerBMPBean extends GenericEntity implements Banner{

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
