//idega 2000 - Gimmi

package com.idega.jmodule.sidemenu.data;


//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class SidemenuModule extends GenericEntity{

	public SidemenuModule(){
		super();
	}

	public SidemenuModule(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
                addAttribute("sidemenu_name","nafn einkennis",true,true, "java.lang.String");
                addAttribute("in_use","í notkun",true,true, "java.lang.Boolean");

        }

	public String getIDColumnName(){
		return "sidemenu_id";
	}

	public String getEntityName(){
		return "sidemenu";
	}


        public void setName(String name) {
          setColumn("sidemenu_name",name);
        }

        public String getName() {
          return getStringColumnValue("sidemenu_name");
        }

        public void setInUse(boolean in_use) {
          setColumn("in_use", new Boolean(in_use));
        }

        public boolean isInUse() {
          return getBooleanColumnValue("in_use");
        }


}
