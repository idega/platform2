//idega 2000 - Gimmi



package com.idega.block.projectmanager.data;



//import java.util.*;

import java.sql.*;

import com.idega.data.*;



public class ProjectExtraBMPBean extends com.idega.data.GenericEntity implements com.idega.block.projectmanager.data.ProjectExtra {



	public ProjectExtraBMPBean(){

		super();

	}



	public ProjectExtraBMPBean(int id)throws SQLException{

		super(id);

	}





	public void initializeAttributes(){

		addAttribute(getIDColumnName());

		addAttribute("description","lýsing",true,true,"java.lang.String",4000);

		addAttribute("goals","markmið",true,true,"java.lang.String",4000);

		addAttribute("finances","fjármál",true,true,"java.lang.String",4000);

		addAttribute("tasks","verkliðir",true,true,"java.lang.String",4000);

          this.addManyToManyRelationShip(Project.class);

	}



        public String getIDColumnName(){

          return "pm_project_extra_id";

        }





	public String getEntityName(){

		return "i_pm_project_extra";

	}



	public String getDescription() {

		return getStringColumnValue("description");

	}



	public void setDescription(String description) {

		setColumn("description",description);

	}



	public String getGoals() {

		return getStringColumnValue("goals");

	}



	public void setGoals(String goals) {

		setColumn("goals",goals);

	}



	public String getFinances() {

		return getStringColumnValue("finances");

	}



	public void setFinances(String finances) {

		setColumn("finances",finances);

	}



	public String getTasks() {

		return getStringColumnValue("tasks");

	}



	public void setTasks(String tasks) {

		setColumn("tasks",tasks);

	}







}

