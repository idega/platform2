/*
 * $Id: SchoolStudyPathBMPBean.java,v 1.1 2003/09/08 14:24:35 anders Exp $
 *
 * Copyright (C) 2003 Idega. All Rights Reserved.
 *
 * This software is the proprietary information of Idega.
 * Use is subject to license terms.
 *
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * Entity bean for study path entries.
 * <p>
 * Last modified: $Date: 2003/09/08 14:24:35 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */ 
public class SchoolStudyPathBMPBean extends GenericEntity implements SchoolStudyPath {

	private static final String ENTITY_NAME = "sch_study_path";

	private static final String COLUMN_STUDY_PATH_CODE = "study_path_code";
	private static final String COLUMN_DESCRIPTION = "description";

	/**
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.GenericEntity#getPrimaryKeyClass()
	 */
	public Class getPrimaryKeyClass() {
		return String.class;
	}
	
	/**
	 * @see com.idega.data.GenericEntity#getIdColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_STUDY_PATH_CODE;
	}
	
	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COLUMN_STUDY_PATH_CODE, "Study path code", String.class, 30);
		addAttribute(COLUMN_DESCRIPTION, "Description of study path", true, true, java.lang.String.class);

		setAsPrimaryKey(COLUMN_STUDY_PATH_CODE, true);
	}

	public String getCode() {
		return getStringColumnValue(COLUMN_STUDY_PATH_CODE);	
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);	
	}

	public void setCode(String code) { 
		setColumn(COLUMN_STUDY_PATH_CODE, code); 
	}

	public void setDescription(String description) { 
		setColumn(COLUMN_DESCRIPTION, description); 
	}

	/**
	 * Finds all study paths.
	 * @return collection of all study path objects
	 * @throws FinderException
	 */
	public Collection ejbFindAll() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendOrderBy();
		query.append(COLUMN_STUDY_PATH_CODE);
		return idoFindPKsByQuery(query);
	}
}
