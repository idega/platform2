//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.projects.golf.entity;

import java.sql.*;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class TeeColor extends GolfEntity{

	public TeeColor(){
		super();
	}
	
	public TeeColor(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("tee_color_name","Nafn teigar",true,true,"java.lang.String");
		addAttribute("gender","Kyn",true,true,"java.lang.String");
	}
	

	public void setName(String name){
		setColumn("tee_color_name",name);
	}

	public String getName(){
		return (String) getColumnValue("tee_color_name");
	}
	
	public String getEntityName(){
		return "tee_color";
	}
	
	/*
	*Sets the gender:
	*M=male
	*F=female
	*/
	public void setGender(String gender){
		setColumn("gender",gender);
	}

	/*
	*Returns the gender:
	*M=male
	*F=female
	*/
	public String getGender(){
		return (String) getColumnValue("gender");
	}
	
	
}
