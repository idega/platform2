// idega 2000 - Gimmi
package com.idega.block.timesheet.data;
//import java.util.*;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.projectmanager.data.Project;
public class ResourceBMPBean extends com.idega.data.GenericEntity implements com.idega.block.timesheet.data.Resource {
	private static final String SHORT_NAME = "short_name";
	private static final String IS_CLOSED = "is_closed";
	private static final String UNIT_NAME = "unit_name";
	private static final String PROJECT_ID = "project_id";
	private static final String RESOURCE_TYPE = "resource_type";
	private static final String RESOURCE_NAME = "resource_name";
	public ResourceBMPBean() {
		super();
	}
	public ResourceBMPBean(int id) throws SQLException {
		super(id);
	}
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(RESOURCE_NAME, "nafn", true, true, String.class);
		addAttribute(RESOURCE_TYPE, "týpa", true, true, String.class);
		addAttribute(PROJECT_ID, "númer verkefnis", true, true, Integer.class, "many-to-one", TimesheetProject.class);
		addAttribute("division_id", "númer deildar", true, true, Integer.class);
		//              addAttribute("division_id","númer deildar", true,
		// true,"java.lang.Integer","many-to-one","com.idega.jmodule.timesheet.data.Division");
		addAttribute(UNIT_NAME, "eining", true, true, String.class);
		addAttribute(IS_CLOSED, "lokaður", true, true, Boolean.class);
		addAttribute(SHORT_NAME, "Stutt nafn", true, true, String.class);
	}
	public String getIDColumnName() {
		return "TMS_RESOURCE_ID";
	}
	public String getEntityName() {
		return "TMS_RESOURCE";
	}
	public String getName() {
		return getResourceName();
	}
	public String getResourceName() {
		return getStringColumnValue(RESOURCE_NAME);
	}
	public void setResourceName(String name) {
		setColumn(RESOURCE_NAME, name);
	}
	public String getShortName() {
		return getStringColumnValue(SHORT_NAME);
	}
	public void setShortName(String short_name) {
		setColumn(SHORT_NAME, short_name);
	}
	public String getResourceType() {
		return getStringColumnValue(RESOURCE_TYPE);
	}
	public void setResourceType(String resource_type) {
		setColumn(RESOURCE_TYPE, resource_type);
	}
	public void setProjectId(int project_id) {
		setColumn(PROJECT_ID, (new Integer(project_id)));
	}
	public int getProjectId() {
		return getIntColumnValue(PROJECT_ID);
	}
	public Project getProject() {
		return (Project) getColumnValue(PROJECT_ID);
	}
	public int getDivisionId() {
		return getIntColumnValue("division_id");
	}
	public void setDivisionId(int division_id) {
		setColumn("division_id", (new Integer(division_id)));
	}
	/*
	 * public Division getDivision() {
	 * 
	 * Division division;
	 * 
	 * try {
	 * 
	 * if (getDivisionId() != -1 )
	 * 
	 * division = new Division(getDivisionId());
	 *  }
	 * 
	 * catch (SQLException s) {
	 *  }
	 *  }
	 *  
	 */
	public String getUnitName() {
		return getStringColumnValue(UNIT_NAME);
	}
	public void setUnitName(String unit_name) {
		setColumn(UNIT_NAME, unit_name);
	}
	public boolean isClosed() {
		return ((Boolean) getColumnValue(IS_CLOSED)).booleanValue();
	}
	public void setClosed(boolean closed) {
		setColumn(IS_CLOSED, closed);
	}
	
	public Collection ejbFindByClosure(boolean closed)throws FinderException{
		com.idega.data.IDOQuery query = super.idoQueryGetSelect().appendWhereEqualsWithSingleQuotes(IS_CLOSED,closed?"Y":"N").appendOrderBy(RESOURCE_NAME);
		System.out.println(query.toString());
		return super.idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindAllOpen()throws FinderException{
		return ejbFindByClosure(false);
	}
	
	public Collection ejbFindAllClosed()throws FinderException{
		return ejbFindByClosure(true);
	}
}
