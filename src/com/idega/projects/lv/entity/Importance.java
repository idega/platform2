//idega 2000 - Gimmi

package com.idega.projects.lv.entity;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class Importance extends GenericEntity{

	public Importance(){
		super();
	}

	public Importance(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","Mikilvægi",true,true,"java.lang.String");

	}

	public String getIDColumnName() {
		return "importance_id";
	}


	public String getEntityName(){
		return "importance";
	}


	public String getName() {
		return (String) getColumnValue("name");
	}

	public void setName(String name) {
		setColumn("name",name);
	}

}
