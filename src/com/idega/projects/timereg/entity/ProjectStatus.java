//idega 2000 - Gimmi

package com.idega.projects.timereg.entity;
//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class ProjectStatus extends GenericEntity{

	public ProjectStatus(){
		super();
	}

	public ProjectStatus(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		//addAttribute("first_name","Fornafn",true,true,"java.lang.String");
		addAttribute("project_id","Verkefnisnúmer", true, true, "java.lang.Integer");
		addAttribute("status","Staða", true, true, "java.lang.String");
	}

	public String getEntityName(){
		return "project_status";
	}


	public void setProjectId(int project_id) {
		setColumn("project_id",project_id);
	}

	public int getProjectId() {
		return getIntColumnValue("project_id");
	}

	public void setStatus(String status) {
		setColumn("status",status);
	}

	public String getStatus() {
		return getStringColumnValue("status");
	}

        public void setStatusForAllProjects(String status) {
        }

}
