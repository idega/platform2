// idega 2000 - Gimmi
package com.idega.block.projectmanager.data;
//import java.util.*;
import java.sql.SQLException;
import com.idega.core.user.data.User;
public class ProjectGroupBMPBean
	extends com.idega.data.GenericEntity
	implements com.idega.block.projectmanager.data.ProjectGroup {
	private static final String VISIBLE = "VISIBLE";
	private static final String NAME = "NAME";
	public ProjectGroupBMPBean() {
		super();
	}
	public ProjectGroupBMPBean(int id) throws SQLException {
		super(id);
	}
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(NAME, "Nafn", true, true, "java.lang.String");
		addAttribute(VISIBLE, "sýnilegt", true, true, "java.lang.Boolean", 1);
		this.addManyToManyRelationShip(User.class);
	}
	public void setDefaultValues() {
		setVisible(true);
	}
	public String getIDColumnName() {
		return "PRMN_GROUP_ID";
	}
	public String getEntityName() {
		return "PRMN_GROUP";
	}
	public boolean isVisible() {
		return ((Boolean) getColumnValue(VISIBLE)).booleanValue();
	}
	public void setVisible(boolean isVisible) {
		setColumn(VISIBLE, new Boolean(isVisible));
	}
	public String getName() {
		return (String) getColumnValue(NAME);
	}
	public void setName(String name) {
		setColumn(NAME, name);
	}
}
