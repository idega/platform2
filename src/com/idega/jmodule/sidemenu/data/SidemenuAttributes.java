//idega 2000 - Gimmi

package com.idega.jmodule.sidemenu.data;


//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class SidemenuAttributes extends GenericEntity{

	public SidemenuAttributes(){
		super();
	}

	public SidemenuAttributes(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("sidemenu_id","númer auglýsingu",true,true, "java.lang.Integer");
                addAttribute("attribute_name","nafn einkennis",true,true, "java.lang.String");
                addAttribute("attribute_id","númer einkennis",true,true, "java.lang.Integer");
        }

	public String getIDColumnName(){
		return "sidemenu_attributes_id";
	}

	public String getEntityName(){
		return "sidemenu_attributes";
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

        public void setSidemenuId(int id) {
          setColumn("sidemenu_id",new Integer(id));
        }

        public int getSidemenuId() {
          return getIntColumnValue("sidemenu_id");
        }


}
