//idega 2000 - Gimmi

package com.idega.jmodule.banner.data;


//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class BannerAttributes extends GenericEntity{

	public BannerAttributes(){
		super();
	}

	public BannerAttributes(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("ad_id","númer auglýsingu",true,true, "java.lang.Integer");
                addAttribute("attribute_name","nafn einkennis",true,true, "java.lang.String");
                addAttribute("attribute_id","númer einkennis",true,true, "java.lang.Integer");

        }

	public String getIDColumnName(){
		return "banner_attributes_id";
	}

	public String getEntityName(){
		return "banner_attributes";
	}

        public void setName(String name) {
          setAttributeName(name);
        }

        public String getName() {
          return getAttributeName();
        }

        public void setAttributeName(String name) {
          setColumn("attribute_name",name);
        }

        public String getAttributeName() {
          return (String) getStringColumnValue("attribute_name");
        }

        public void setAttributeId(int id) {
          setColumn("attribute_id",new Integer(id));
        }

        public int getAttributeId() {
          return getIntColumnValue("attribute_id");
        }

        public void setAdId(int id) {
          setColumn("ad_id",new Integer(id));
        }

        public int getAdId() {
          return getIntColumnValue("ad_id");
        }


}
