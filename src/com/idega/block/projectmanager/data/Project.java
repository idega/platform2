//idega 2000 - Gimmi

package com.idega.block.projectmanager.data;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class Project extends GenericEntity{

	public Project(){
		super();
	}

	public Project(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","Nafn",true,true,"java.lang.String");
		addAttribute("issue_id","málaflokkur",true,true,"java.lang.Integer");
		addAttribute("forum_id","spjall",true,true,"java.lang.Integer");
		addAttribute("group_id","hópur",true,true,"java.lang.Integer");
		addAttribute("importance_id","mikilvægi",true,true,"java.lang.Integer");
		addAttribute("valid","í lagi",true,true,"java.lang.Boolean");
		addAttribute("wheel_group_id","stýrishópur",true,true,"java.lang.Integer");
		addAttribute("project_group_id","verkefnishópur",true,true,"java.lang.Integer");
		addAttribute("project_manger_id","verkefnisstóri",true,true,"java.lang.Integer");
		addAttribute("group_id_final","hópur",true,true,"java.lang.Boolean");
		addAttribute("wheel_group_id_final","stýrishópur",true,true,"java.lang.Boolean");
		addAttribute("project_group_id_final","verkefnishópur",true,true,"java.lang.Boolean");
		addAttribute("project_manager_id_final","verkefnisstóri",true,true,"java.lang.Boolean");
                // gimmi bætti við 29-01-2001
                addAttribute("project_number","verknúmer",true,true,"java.lang.String");
                addAttribute("pm_project_status_id","staða verks",true,true,"java.lang.Integer");
                addAttribute("parent_id","yfirverk",true,true,"java.lang.Integer");


	}


	public String getIDColumnName() {
		return "project_id";
	}

	public String getEntityName(){
		return "project";
	}


        public String getProjectNumber() {
            return getStringColumnValue("project_number");
        }
        public void setProjectNumber(String project_number) {
            setColumn("project_number",project_number);
        }

        public int getProjectStatusId() {
            return getIntColumnValue("pm_project_status_id");
        }
        public void setProjectStatusId(int status_id) {
            setColumn("pm_project_status_id",status_id);
        }

        public int getParentId() {
            return getIntColumnValue("parent_id");
        }
        public void setParentId(int parent_id) {
            setColumn("parent_id",parent_id);
        }

        public boolean isGroupIdFinal() {
          return ((Boolean) getColumnValue("group_id_final")).booleanValue();
        }
        public boolean isWheelGroupIdFinal() {
          return ((Boolean) getColumnValue("wheel_group_id_final")).booleanValue();
        }
        public boolean isProjectGroupIdFinal() {
          return ((Boolean) getColumnValue("project_group_id_final")).booleanValue();
        }
        public boolean isProjectManagerIdFinal() {
          return ((Boolean) getColumnValue("project_manager_id_final")).booleanValue();
        }

        public void setGroupIdFinal(boolean isFinal) {
          setColumn("group_id_final",isFinal);
        }
        public void setWheelGroupIdFinal(boolean isFinal) {
          setColumn("wheel_group_id_final",isFinal);
        }
        public void setProjectGroupIdFinal(boolean isFinal) {
          setColumn("project_group_id_final",isFinal);
        }
        public void setProjectManagerIdFinal(boolean isFinal) {
          setColumn("project_manager_id_final",isFinal);
        }


	public String getName() {
		return (String) getColumnValue("name");
	}

	public void setName(String name) {
		setColumn("name",name);
	}

	public int getIssueId() {
		return getIntColumnValue("issue_id");
	}

	public void setIssueId(int issue_id) {
		setColumn("issue_id",issue_id);
	}

	public int getForumId() {
		return getIntColumnValue("forum_id");
	}

	public void setForumId(int forum_id) {
		setColumn("forum_id",forum_id);
	}

	public int getGroupId() {
		return getIntColumnValue("group_id");
	}

/*	public Group getGroup(){
		return (Group) getColumnValue("group_id");
	}
*/
	public void setGroupId(int group_id) {
		setColumn("group_id",group_id);
	}
/*
	public Group getWheelGroup(){
		return (Group) getColumnValue("wheel_group_id");
	}
*/
	public int getWheelGroupId() {
		return getIntColumnValue("wheel_group_id");
	}

	public void setWheelGroupId(int wheel_group_id) {
		setColumn("wheel_group_id",wheel_group_id);
	}

	public int getProjectGroupId() {
		return getIntColumnValue("project_group_id");
	}

	public void setProjectGroupId(int project_group_id) {
		setColumn("project_group_id",project_group_id);
	}

	public int getProjectManagerId() {
		return getIntColumnValue("project_manger_id");
	}

	public void setProjectManagerId(int project_manager_id) {
		setColumn("project_manger_id",project_manager_id);
	}


	public void setImportanceId(int id) {
		setColumn("importance_id",id);
	}
	public int getImportanceId() {
		return getIntColumnValue("importance_id");
	}


	public boolean isValid() {
		return ((Boolean)getColumnValue("valid")).booleanValue();
	}

	public void setValid(boolean valid) {
		setColumn("valid",valid);
	}
}
