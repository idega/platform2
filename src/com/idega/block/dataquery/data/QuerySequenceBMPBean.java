package com.idega.block.dataquery.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOQuery;
import com.idega.data.TreeableEntity;
import com.idega.data.TreeableEntityBMPBean;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Feb 2, 2004
 */
public class QuerySequenceBMPBean extends TreeableEntityBMPBean implements QuerySequence, TreeableEntity  {
	
	private static final String ENTITY_NAME = "QUERY_SEQUENCE";
	private static final String COLUMN_NAME_NAME = "NAME";
	private static final String COLUMN_NAME_REAL_QUERY = "REAL_QUERY";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_NAME, "Name", true, true, String.class);
		addAttribute(COLUMN_NAME_REAL_QUERY, "Real query", true, true, Integer.class, "one-to-one", UserQuery.class);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME_NAME, name);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME_NAME);
	}
	
	public void setRealQuery(UserQuery realQuery) {
		setColumn(COLUMN_NAME_REAL_QUERY, realQuery);
	}
	
	public UserQuery getRealQuery() {
		return (UserQuery) getColumnValue(COLUMN_NAME_REAL_QUERY);
	}
	
	public String getIDColumnName() {
		return super.getIDColumnName();
	}
	
	public Integer ejbFindByName(String name) throws FinderException {
		IDOQuery sql =idoQuery();
		sql.appendSelectAllFrom(this.getEntityName());
		sql.appendWhereEqualsQuoted(COLUMN_NAME_NAME, name);
		return (Integer) idoFindOnePKByQuery(sql);
	}
		
	public Collection ejbFindAllByRealQuery(UserQuery userQuery) throws FinderException {
    IDOQuery query = idoQueryGetSelect();
    query.appendWhere();
    query.appendEquals(COLUMN_NAME_REAL_QUERY, userQuery);
    return idoFindPKsBySQL(query.toString());
	}
	
	
}
