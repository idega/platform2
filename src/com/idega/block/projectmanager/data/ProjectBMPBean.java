// idega 2000 - Gimmi
package com.idega.block.projectmanager.data;
//import java.util.*;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;
public class ProjectBMPBean
	extends com.idega.data.GenericEntity
	implements com.idega.block.projectmanager.data.Project {
	public static final String PROJECT = "PRMN_PROJECT";
	public static final String PROJECT_ID = "PRMN_PROJECT_ID";
	private static final String PARENT_ID = "PARENT_ID";
	private static final String PM_PROJECT_STATUS_ID = "PRM_PROJECT_STATUS_ID";
	private static final String PROJECT_NUMBER = "PROJECT_NUMBER";
	private static final String PROJECT_MANAGER_ID_FINAL = "PRMN_ID_FINAL";
	private static final String PROJECT_GROUP_ID_FINAL = "PRMN_GROUP_ID_FINAL";
	private static final String WHEEL_GROUP_ID_FINAL = "WHEEL_GROUP_ID_FINAL";
	private static final String GROUP_ID_FINAL = "GROUP_ID_FINAL";
	private static final String PROJECT_MANGER_ID = "PRMN_MANAGER_ID";
	private static final String PROJECT_GROUP_ID = "PRMN_GROUP_ID";
	private static final String WHEEL_GROUP_ID = "WHEEL_GROUP_ID";
	private static final String VALID = "VALID";
	private static final String IMPORTANCE_ID = "IMPORTANCE_ID";
	private static final String GROUP_ID = "GROUP_ID";
	private static final String FORUM_ID = "FORUM_ID";
	private static final String ISSUE_ID = "ISSUE_ID";
	private static final String NAME = "NAME";
	public ProjectBMPBean() {
		super();
	}
	public ProjectBMPBean(int id) throws SQLException {
		super(id);
	}
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(NAME, "Nafn", true, true, "java.lang.String");
		addAttribute(ISSUE_ID, "málaflokkur", true, true, "java.lang.Integer");
		addAttribute(FORUM_ID, "spjall", true, true, "java.lang.Integer");
		addAttribute(GROUP_ID, "hópur", true, true, "java.lang.Integer");
		addAttribute(IMPORTANCE_ID, "mikilvægi", true, true, "java.lang.Integer");
		addAttribute(VALID, "í lagi", true, true, "java.lang.Boolean");
		addAttribute(WHEEL_GROUP_ID, "stýrishópur", true, true, "java.lang.Integer");
		addAttribute(PROJECT_GROUP_ID, "verkefnishópur", true, true, "java.lang.Integer");
		addAttribute(PROJECT_MANGER_ID, "verkefnisstóri", true, true, "java.lang.Integer");
		addAttribute(GROUP_ID_FINAL, "hópur", true, true, "java.lang.Boolean");
		addAttribute(WHEEL_GROUP_ID_FINAL, "stýrishópur", true, true, "java.lang.Boolean");
		addAttribute(PROJECT_GROUP_ID_FINAL, "verkefnishópur", true, true, "java.lang.Boolean");
		addAttribute(PROJECT_MANAGER_ID_FINAL, "verkefnisstóri", true, true, "java.lang.Boolean");
		// gimmi bætti við 29-01-2001
		addAttribute(PROJECT_NUMBER, "verknúmer", true, true, "java.lang.String");
		addAttribute(PM_PROJECT_STATUS_ID, "staða verks", true, true, "java.lang.Integer");
		addAttribute(PARENT_ID, "yfirverk", true, true, "java.lang.Integer");
	}
	public String getIDColumnName() {
		return PROJECT_ID;
	}
	public String getEntityName() {
		return PROJECT;
	}
	public String getProjectNumber() {
		return getStringColumnValue(PROJECT_NUMBER);
	}
	public void setProjectNumber(String project_number) {
		setColumn(PROJECT_NUMBER, project_number);
	}
	public int getProjectStatusId() {
		return getIntColumnValue(PM_PROJECT_STATUS_ID);
	}
	public void setProjectStatusId(int status_id) {
		setColumn(PM_PROJECT_STATUS_ID, status_id);
	}
	public int getParentId() {
		return getIntColumnValue(PARENT_ID);
	}
	public void setParentId(int parent_id) {
		setColumn(PARENT_ID, parent_id);
	}
	public boolean isGroupIdFinal() {
		return ((Boolean) getColumnValue(GROUP_ID_FINAL)).booleanValue();
	}
	public boolean isWheelGroupIdFinal() {
		return ((Boolean) getColumnValue(WHEEL_GROUP_ID_FINAL)).booleanValue();
	}
	public boolean isProjectGroupIdFinal() {
		return ((Boolean) getColumnValue(PROJECT_GROUP_ID_FINAL)).booleanValue();
	}
	public boolean isProjectManagerIdFinal() {
		return ((Boolean) getColumnValue(PROJECT_MANAGER_ID_FINAL)).booleanValue();
	}
	public void setGroupIdFinal(boolean isFinal) {
		setColumn(GROUP_ID_FINAL, isFinal);
	}
	public void setWheelGroupIdFinal(boolean isFinal) {
		setColumn(WHEEL_GROUP_ID_FINAL, isFinal);
	}
	public void setProjectGroupIdFinal(boolean isFinal) {
		setColumn(PROJECT_GROUP_ID_FINAL, isFinal);
	}
	public void setProjectManagerIdFinal(boolean isFinal) {
		setColumn(PROJECT_MANAGER_ID_FINAL, isFinal);
	}
	public String getName() {
		return (String) getColumnValue(NAME);
	}
	public void setName(String name) {
		setColumn(NAME, name);
	}
	public int getIssueId() {
		return getIntColumnValue(ISSUE_ID);
	}
	public void setIssueId(int issue_id) {
		setColumn(ISSUE_ID, issue_id);
	}
	public int getForumId() {
		return getIntColumnValue(FORUM_ID);
	}
	public void setForumId(int forum_id) {
		setColumn(FORUM_ID, forum_id);
	}
	public int getGroupId() {
		return getIntColumnValue(GROUP_ID);
	}
	/*
	 * public Group getGroup(){
	 * 
	 * return (Group) getColumnValue("group_id"); }
	 *  
	 */
	public void setGroupId(int group_id) {
		setColumn(GROUP_ID, group_id);
	}
	/*
	 * 
	 * public Group getWheelGroup(){
	 * 
	 * return (Group) getColumnValue("wheel_group_id"); }
	 *  
	 */
	public int getWheelGroupId() {
		return getIntColumnValue(WHEEL_GROUP_ID);
	}
	public void setWheelGroupId(int wheel_group_id) {
		setColumn(WHEEL_GROUP_ID, wheel_group_id);
	}
	public int getProjectGroupId() {
		return getIntColumnValue(PROJECT_GROUP_ID);
	}
	public void setProjectGroupId(int project_group_id) {
		setColumn(PROJECT_GROUP_ID, project_group_id);
	}
	public int getProjectManagerId() {
		return getIntColumnValue(PROJECT_MANGER_ID);
	}
	public void setProjectManagerId(int project_manager_id) {
		setColumn(PROJECT_MANGER_ID, project_manager_id);
	}
	public void setImportanceId(int id) {
		setColumn(IMPORTANCE_ID, id);
	}
	public int getImportanceId() {
		return getIntColumnValue(IMPORTANCE_ID);
	}
	public boolean isValid() {
		return ((Boolean) getColumnValue(VALID)).booleanValue();
	}
	public void setValid(boolean valid) {
		setColumn(VALID, valid);
	}
	
	public Collection ejbFindAllOrderByNumber() throws FinderException{
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendOrderBy(PROJECT_NUMBER));
	}
	
}
