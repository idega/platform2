/*
 * $Id: ChildCareExportTimeBMPBean.java,v 1.1 2005/01/12 13:11:53 anders Exp $
 *
 * Copyright (C) 2005 Idega. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf & Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.export.data;

import java.sql.Timestamp;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.User;

/**
 * Entity bean for child care file export timestamp entries.
 * Each time a new file with childcare placements is created,
 * a record of this type is added.
 * <p>
 * Last modified: $Date: 2005/01/12 13:11:53 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class ChildCareExportTimeBMPBean extends GenericEntity implements ChildCareExportTime {

	private static final String ENTITY_NAME = "comm_cc_export_time";

	private static final String COLUMN_ID = "id";
	private static final String COLUMN_CREATED = "created";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_FILE_NAME = "file_name";
	private static final String COLUMN_FILE_TYPE = "file_type";
	
	/**
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	/**
	 * @see com.idega.data.GenericEntity#getIdColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_ID;
	}

	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		setAsPrimaryKey(getIDColumnName(), true);
		addAttribute(COLUMN_CREATED, "When the export file was created", Timestamp.class);
		addManyToOneRelationship(COLUMN_USER_ID, User.class);
		addAttribute(COLUMN_FILE_NAME, "Name of the export file", java.lang.String.class);
		addAttribute(COLUMN_FILE_TYPE, "Type of the export file (1 = placements, 2 = taxekat)", java.lang.Integer.class);
	}

	/**
	 * Returns the timestamp for when the export file was created.
	 */
	public Timestamp getCreated() {
		return getTimestampColumnValue(COLUMN_CREATED);	
	}

	/**
	 * Returns the id for the user who created the export file.
	 */
	public int getUserId() {
		return getIntColumnValue(COLUMN_USER_ID);	
	}

	/**
	 * Returns the user who created the export file.
	 */
	public User getUser() {
		return (User) getColumnValue(COLUMN_USER_ID);	
	}

	/**
	 * Returns the file name of the export file.
	 */
	public String getFileName() {
		return getStringColumnValue(COLUMN_FILE_NAME);	
	}

	/**
	 * Returns the type of the export file.
	 * 1 = child care placement export file for the IST Extens system.
	 * 2 = child care time 'taxekategori' change export file for the IST Extens system.
	 */
	public int getFileType() {
		return getIntColumnValue(COLUMN_FILE_TYPE);	
	}

	/**
	 * Sets the timestamp for when the export file was created.
	 * @param created the timestamp to set
	 */
	public void setCreated(Timestamp created) { 
		setColumn(COLUMN_CREATED, created); 
	}
	
	/**
	 * Sets the id for the user who created the export file.
	 * @param userId the user id to set
	 */
	public void setUserId(int userId) { 
		setColumn(COLUMN_USER_ID, userId); 
	}

	/**
	 * Sets the name of the export file.
	 * @param fileName the file name to set
	 */
	public void setFileName(String fileName) { 
		setColumn(COLUMN_FILE_NAME, fileName); 
	}

	/**
	 * Sets the type of the export file.
	 * 1 = child care placement export file for the IST Extens system.
	 * 2 = child care time 'taxekategori' change export file for the IST Extens system.
	 * @param fileType the integer type to set
	 */
	public void setFileType(int fileType) { 
		setColumn(COLUMN_FILE_TYPE, fileType); 
	}

	/**
	 * Finds the most recent export file entry for the specified file type.
	 * 1 = child care placement export file for the IST Extens system.
	 * 2 = child care time 'taxekategori' change export file for the IST Extens system.
	 * @param fileType the type of export file
	 * @return the primary key for the entry found
	 * @throws FinderException
	 */
	public Integer ejbFindLatest(int fileType) throws FinderException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, COLUMN_FILE_TYPE, MatchCriteria.EQUALS, fileType));
		query.addOrder(new Order(new Column(table, COLUMN_CREATED), false));
		return (Integer) idoFindOnePKByQuery(query);
	}
}
