//idega 2000 - Gimmi

package com.idega.projects.projectscene.entity;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class Group extends GenericEntity{

	public Group(){
		super();
	}

	public Group(int id)throws SQLException{
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

	public String getIDColumnName() {
		return "group__id";
	}

	public String getEntityName(){
		return "group_";
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
