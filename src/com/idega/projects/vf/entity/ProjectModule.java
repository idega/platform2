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
public class ProjectModule extends GenericEntity{

	public ProjectModule(){
		super();
	}

	public ProjectModule(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","Project name",true,true,"java.lang.String");
		addAttribute("text_id","TextID",false,false,"java.lang.Integer","one-to-one","com.idega.jmodule.text.data.TextModule");
		addAttribute("issue_id","IssueID",false,false,"java.lang.Integer","one-to-one","com.idega.jmodule.boxoffice.data.Issues");
		addAttribute("vf_project_status_id","StatusID",false,false,"java.lang.Integer","many-to-one","com.idega.projects.vf.entity.ProjectStatus");
		addAttribute("vf_project_category_id","CategoryID",false,false,"java.lang.Integer","many-to-one","com.idega.projects.vf.entity.ProjectCategory");
	}

	public String getEntityName(){
		return "vf_project";
	}

	public String getName(){
		return getStringColumnValue("name");
	}
  public void setName(String name) {
		setColumn("name",name);
	}

	public int getTextID(){
		return getIntColumnValue("text_id");
	}
	public void setTextID(int text_id) {
		setColumn("text_id",text_id);
	}

	public int getIssueID(){
		return getIntColumnValue("issue_id");
	}
	public void setIssueID(int issue_id) {
		setColumn("issue_id",issue_id);
	}

	public int getStatusID(){
		return getIntColumnValue("vf_project_status_id");
	}
	public void setStatusID(int project_status_id) {
		setColumn("vf_project_status_id",project_status_id);
	}

	public int getCategoryID(){
		return getIntColumnValue("vf_project_category_id");
	}
	public void setCategoryID(int project_category_id) {
		setColumn("vf_project_category_id",project_category_id);
	}


}
