//idega 2001 -eiki
package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;


public class Family extends GolfEntity{

	public Family(){
		super();

	}

	public Family(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","Fjölskylda",true,true,"java.lang.String");
	}

	public String getEntityName(){
		return "family";
	}

	public String getName(){
		return getStringColumnValue("name");
	}

	public void setName(String name){
		setColumn("name", name);
	}


}
