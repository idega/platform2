//idega 2000 - Gimmi

package com.idega.projects.projectscene.entity;

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
		addAttribute("issue_id","málaflokkur",true,true,"java.lang.Integer","one-to-one","com.idega.jmodule.boxoffice.data.Issues");
		addAttribute("forum_id","spjall",true,true,"java.lang.Integer");
		addAttribute("group_id","hópur",true,true,"java.lang.Integer","one-to-one","com.idega.projects.lv.entity.Group");
		addAttribute("importance_id","mikilvægi",true,true,"java.lang.Integer");
		addAttribute("valid","í lagi",true,true,"java.lang.Boolean",1);
		addAttribute("wheel_group_id","stýrishópur",true,true,"java.lang.Integer","one-to-one","com.idega.projects.lv.entity.Group");
		addAttribute("project_group_id","verkefnishópur",true,true,"java.lang.Integer","one-to-one","com.idega.projects.lv.entity.Group");
		addAttribute("project_manger_id","verkefnisstóri",true,true,"java.lang.Integer","one-to-one","com.idega.projects.lv.entity.Member");

	}

	public String getIDColumnName() {
		return "project_id";
	}

	public String getEntityName(){
		return "project";
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

	public Group getGroup(){
		return (Group) getColumnValue("group_id");
	}

	public void setGroupId(int group_id) {
		setColumn("group_id",group_id);
	}

	public Group getWheelGroup(){
		return (Group) getColumnValue("wheel_group_id");
	}

	public int getWheelGroupId() {
		return getIntColumnValue("wheel_group_id");
	}

	public void setWheelGroupId(int wheel_group_id) {
		setColumn("wheel_group_id",wheel_group_id);
	}

	public int getProjectGroupId() {
		return getIntColumnValue("project_group_id");
	}

	public Group getProjectGroup(){
		return (Group) getColumnValue("project_group_id");
	}

	public void setProjectGroupId(int project_group_id) {
		setColumn("project_group_id",project_group_id);
	}

	public Group getProjectManager() {
		return (Group) getColumnValue("project_manger_id");
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
