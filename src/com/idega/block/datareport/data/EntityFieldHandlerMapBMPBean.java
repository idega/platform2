package com.idega.block.datareport.data;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Dec 1, 2003
 */
public class EntityFieldHandlerMapBMPBean extends GenericEntity {
	
	protected final static String ENTITY_NAME = "ENTITY_FIELD_HANDLER_MAP";

	protected final static String COLUMN_NAME_ENTITY = "ENTITY";
	protected final static String COLUMN_NAME_FIELD = "FIELD";
	protected final static String COLUMN_NAME_HANDLER = "HANDLER";
	protected final static String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
	
	public EntityFieldHandlerMapBMPBean() {
		super();
	}


	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_ENTITY, "name of entity", true, true, String.class, 80);
		addAttribute(COLUMN_NAME_FIELD, "name of field", true, true, String.class, 40);
		addAttribute(COLUMN_NAME_DESCRIPTION, "description", true, true, String.class, 100);
		addAttribute(COLUMN_NAME_HANDLER, "handler", true, true, String.class, 80);
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getEntity() {
		return getStringColumnValue(COLUMN_NAME_ENTITY);
	}
	
	public String getField() {
		return getStringColumnValue(COLUMN_NAME_FIELD);
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_NAME_DESCRIPTION);
	}
	
	public String getHandler() {
		return getStringColumnValue(COLUMN_NAME_HANDLER);
	}
	
	public void setEntity(String entity)  {
		setColumn(COLUMN_NAME_ENTITY, entity);
	}
	
	public void setField(String field) {
		setColumn(COLUMN_NAME_FIELD, field);
	}
	
	public void setDescription(String description) {
		setColumn(COLUMN_NAME_DESCRIPTION, description);
	}
	
	public void setHandler(String handler) {
		setColumn(COLUMN_NAME_HANDLER, handler);
	}
	
	public Object findByEntityAndField(String entity, String field) throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(COLUMN_NAME_ENTITY, entity);
		query.appendAnd().appendEquals(COLUMN_NAME_FIELD, field);
		return idoFindOnePKBySQL(query.toString());
	}
		
		
}
