package com.idega.block.dataquery.data;

import com.idega.data.EntityRepresentation;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jul 19, 2004
 */
public class QueryRepresentation implements EntityRepresentation {
	
	public static final String DESIGN_LAYOUT_KEY = "design_chooser_key";
	public static final String NAME_KEY = "name_key";
	public static final String GROUP_NAME_KEY = "group_name_key";
	public static final String IS_PRIVATE_KEY = "is_private_key";
	
	private int id;
	private String name;
	private String groupName;
	private boolean belongsToUser;
	private boolean isPrivate;
	
	public QueryRepresentation(int id, String name, String groupName, boolean isPrivate, boolean belongsToUser)	{
		this.id = id;
		this.name = name;
		this.groupName = groupName;
		this.belongsToUser = belongsToUser;
		this.isPrivate = isPrivate;
	}
	
	public Object getColumnValue(String columnName) {
		if (NAME_KEY.equals(columnName))	{
			return name;
		}
		else if (GROUP_NAME_KEY.equals(columnName))	{
			return groupName;
		} 
		else if (IS_PRIVATE_KEY.equals(columnName)) {
			return isPrivate ? "X" : "";
		}
		else if (DESIGN_LAYOUT_KEY.equals(columnName)) {
			//no preselection!
			return null;
		}
		return name;
	}

	public Object getPrimaryKey() {
		return new Integer(id);
	}
	
	public boolean belongsToUser() {
		return belongsToUser;
	}
}
