//idega 2000 - Thorhallur Helgason
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.projects.golf.entity;

import java.sql.*;


/**
*@author <a href="mailto:laddi@idega.is">Thorhallur Helgason</a>
*@version 1.2
*/
public class Dismissal extends GolfEntity{

	public Dismissal(){
		super();
	}

	public Dismissal(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","Frávísun",true,true,"java.lang.String");
	}


	public void setName(String name){
		setColumn("name",name);
	}

	public String getName(){
		return (String) getColumnValue("name");
	}

	public String getEntityName(){
		return "dismissal";
	}

}
