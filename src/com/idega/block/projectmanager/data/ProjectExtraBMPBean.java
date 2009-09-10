// idega 2000 - Gimmi
package com.idega.block.projectmanager.data;
//import java.util.*;
import java.sql.SQLException;
public class ProjectExtraBMPBean
	extends com.idega.data.GenericEntity
	implements com.idega.block.projectmanager.data.ProjectExtra {
	private static final String TASKS = "tasks";
	private static final String FINANCES = "finances";
	private static final String GOALS = "goals";
	private static final String DESCRIPTION = "description";
	public ProjectExtraBMPBean() {
		super();
	}
	public ProjectExtraBMPBean(int id) throws SQLException {
		super(id);
	}
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(DESCRIPTION, "lýsing", true, true, "java.lang.String", 4000);
		addAttribute(GOALS, "markmið", true, true, "java.lang.String", 4000);
		addAttribute(FINANCES, "fjármál", true, true, "java.lang.String", 4000);
		addAttribute(TASKS, "verkliðir", true, true, "java.lang.String", 4000);
		this.addManyToManyRelationShip(Project.class);
	}
	public String getIDColumnName() {
		return "PRMN_PROJECT_EXTRA_ID";
	}
	public String getEntityName() {
		return "PRMN_PROJECT_EXTRA";
	}
	public String getDescription() {
		return getStringColumnValue(DESCRIPTION);
	}
	public void setDescription(String description) {
		setColumn(DESCRIPTION, description);
	}
	public String getGoals() {
		return getStringColumnValue(GOALS);
	}
	public void setGoals(String goals) {
		setColumn(GOALS, goals);
	}
	public String getFinances() {
		return getStringColumnValue(FINANCES);
	}
	public void setFinances(String finances) {
		setColumn(FINANCES, finances);
	}
	public String getTasks() {
		return getStringColumnValue(TASKS);
	}
	public void setTasks(String tasks) {
		setColumn(TASKS, tasks);
	}
}
