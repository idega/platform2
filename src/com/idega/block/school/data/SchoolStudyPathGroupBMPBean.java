/*
 * Created on 2005-apr-21
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolStudyPathGroupBMPBean extends GenericEntity implements SchoolStudyPathGroup{
	private static String TABLE_NAME           = "SCH_STUDY_PATH_GROUP";
	private static String COLUMN_GROUP_NAME    = "GROUP_NAME";
	private static String COLUMN_LOCALIZED_KEY = "LOCALIZED_KEY";

	
	
	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_GROUP_NAME, "group name", true, true, String.class);
		addAttribute(COLUMN_LOCALIZED_KEY, "localized key", String.class);
		
	}
	
	public Collection ejbFindAllStudyPathGroups() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendOrderBy(COLUMN_GROUP_NAME);
		return idoFindPKsByQuery(query);
	}
	
	public Object ejbFindByGroupName(String name) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEqualsQuoted(COLUMN_GROUP_NAME, name);
		query.appendOrderBy(COLUMN_GROUP_NAME);
		return idoFindOnePKByQuery(query);
	}
	
	public String getGroupName() {
		return getStringColumnValue(COLUMN_GROUP_NAME);
	}

	public void setGroupName(String groupname) {
		setColumn(COLUMN_GROUP_NAME, groupname);
	}
	
	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZED_KEY);
	}

	public void setLocalizationKey(String localizedkey) {
		setColumn(COLUMN_LOCALIZED_KEY, localizedkey);
	}
}
