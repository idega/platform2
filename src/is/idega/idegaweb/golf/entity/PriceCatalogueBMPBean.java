//idega 2000 - eiki

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import com.idega.data.GenericEntity;

public class PriceCatalogueBMPBean extends GenericEntity implements PriceCatalogue{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","Forði",true,true,"java.lang.String");
		addAttribute("union_id", "Samband/Klúbbur", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Union");
		addAttribute("extra_info","Athugasemd",true,true,"java.lang.String");
		addAttribute("price", "Verð", true, true, "java.lang.Integer");
                addAttribute("in_use","Gild",true,true,"java.lang.Boolean");
                addAttribute("is_independent","Óháð",true,true,"java.lang.Boolean");
	}

	public String getEntityName(){
		return "price_catalogue";

	}

	public String getName(){
		return getStringColumnValue("name");
	}

	public void setName(String name){
		setColumn("name", name);
	}


	public int getUnionId(){
		return getIntColumnValue("union_id");
	}

	public void setUnion_id(Integer union_id){
		setColumn("union_id", union_id);
	}

        public void setUnion_id(int union_id){
		setColumn("union_id", union_id);
	}

        public String getExtraInfo(){
		return getStringColumnValue("extra_info");
	}

	public void setExtraInfo(String extra_info){
		setColumn("extra_info", extra_info);
	}

	public int getPrice(){
		return  getIntColumnValue("price");
	}

	public void setPrice(Integer price){
		setColumn("price", price);
	}

        public void setPrice(int price){
		setColumn("price", price);
	}

        public void setInUse(boolean in_use) {
              setColumn("in_use", in_use);
        }

        public boolean isInUse() {
          return getBooleanColumnValue("in_use");
        }

        public void setIndependent(boolean is_independent) {
        setColumn("is_independent", is_independent);
        }

        public boolean isIndependent() {
          return getBooleanColumnValue("is_independent");
        }

}
