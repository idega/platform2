//idega 2000 - Gimmi

package com.idega.jmodule.banner.data;


//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class BannerCollection extends GenericEntity{

	public BannerCollection(){
		super();
	}

	public BannerCollection(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("banner_collection_name","Nafn auglýsingasafns",true,true, "java.lang.String");
	}

	public String getIDColumnName(){
		return "banner_collection_id";
	}

	public String getEntityName(){
		return "banner_collection";
	}

        public void setName(String name) {
          setBannerCollectionName(name);
        }

        public String getName() {
          return getBannerCollectionName();
        }

        public void setBannerCollectionName(String name) {
          setColumn("banner_collection_name",name);
        }

        public String getBannerCollectionName() {
          return (String) getStringColumnValue("banner_collection_name");
        }




}
