//idega 2000 - Gimmi

package com.idega.jmodule.projectmanager.data;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class ProjectGroup extends GenericEntity{

	public ProjectGroup(){
		super();
	}

	public ProjectGroup(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","Nafn",true,true,"java.lang.String");
		addAttribute("visible","sýnilegt",true,true,"java.lang.Boolean",1);
	}

	public void setDefaultValues() {
		setVisible(true);
	}

        public String getIDColumnName(){
          return "pm_group_id";
        }


	public String getEntityName(){
		return "i_pm_group";
	}


	public boolean isVisible() {
		return ((Boolean) getColumnValue("visible")).booleanValue();
	}

	public void setVisible(boolean isVisible) {
		setColumn("visible",new Boolean(isVisible));
	}

	public String getName() {
		return (String) getColumnValue("name");
	}

	public void setName(String name) {
		setColumn("name",name);
	}

}
