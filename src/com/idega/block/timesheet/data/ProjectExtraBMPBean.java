// idega 2000 - Gimmi
package com.idega.block.timesheet.data;
//import java.util.*;
import java.sql.SQLException;
public class ProjectExtraBMPBean 	extends com.idega.data.GenericEntity
	implements com.idega.block.timesheet.data.ProjectExtra {
	
	private static final String TASKS = "TASKS";
	private static final String FINANCES = "FINANCES";
	private static final String GOALS = "GOALS";
	private static final String DESCRIPTION = "DESCRIPTION";
	public ProjectExtraBMPBean() {
		super();
	}
	
	public ProjectExtraBMPBean(int id) throws SQLException {
		super(id);
	}
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(DESCRIPTION, "lýsing", true, true, String.class);
		addAttribute(GOALS, "markmið", true, true, String.class);
		addAttribute(FINANCES, "fjálmál", true, true, String.class);
		addAttribute(TASKS, "verkliðir", true, true, String.class);
	}
	
	public String getIDColumnName() {
		return "TMS_PROJ_EXTRA_ID";
	}
	public String getEntityName() {
		return "TMS_PROJ_EXTRA";
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
