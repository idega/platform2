//idega 2001 - Thorhallur Helgason
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.projects.vf.entity;

import java.sql.*;
import com.idega.data.GenericEntity;

/**
*@author <a href="mailto:laddi@idega.is">Thorhallur Helgason</a>
*@version 1.0
*/
public class ProjectStatus extends GenericEntity{

	public ProjectStatus(){
		super();
	}

	public ProjectStatus(int id)throws SQLException{
		super(id);
	}

  public void initializeAttributes(){
  	addAttribute(getIDColumnName());
		addAttribute("name","Status name",true,true,"java.lang.String");
	}

	public String getEntityName(){
		return "vf_project_status";
	}

	public String getName(){
		return getStringColumnValue("name");
	}
  public void setName(String name) {
		setColumn("name",name);
	}

}
