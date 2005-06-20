/*
 * $Id: GradeBMPBean.java,v 1.2 2005/06/20 14:22:06 laddi Exp $
 * Created on Jun 20, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;


/**
 * Last modified: $Date: 2005/06/20 14:22:06 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class GradeBMPBean extends GenericEntity  implements Grade{

	private static final String ENTITY_NAME = "sch_grade";
	
	private static final String COLUMN_SCHOOL_TYPE = "sch_school_type_id";
	private static final String COLUMN_GRADE = "grade";
	private static final String COLUMN_LOCALIZED_KEY = "localized_key";

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
		
		addManyToOneRelationship(COLUMN_SCHOOL_TYPE, "School type", SchoolType.class);
		
		addAttribute(COLUMN_GRADE, "Grade", String.class, 20);
		addAttribute(COLUMN_LOCALIZED_KEY, "Localized key", String.class);
	}
	
	//Getters
	public SchoolType getSchoolType() {
		return (SchoolType) getColumnValue(COLUMN_SCHOOL_TYPE);
	}
	
	public Object getSchoolTypePK() {
		return getIntegerColumnValue(COLUMN_SCHOOL_TYPE);
	}
	
	public String getGrade() {
		return getStringColumnValue(COLUMN_GRADE);
	}
	
	public String getLocalizedKey() {
		return getStringColumnValue(COLUMN_LOCALIZED_KEY);
	}
	
	//Setters
	public void setSchoolType(SchoolType type) {
		setColumn(COLUMN_SCHOOL_TYPE, type);
	}
	
	public void setGrade(String grade) {
		setColumn(COLUMN_GRADE, grade);
	}
	
	public void setLocalizedKey(String localizedKey) {
		setColumn(COLUMN_LOCALIZED_KEY, localizedKey);
	}
	
	//Finders
	public Collection ejbFindAllBySchoolType(SchoolType type) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL_TYPE, MatchCriteria.EQUALS, type));
		
		return idoFindPKsByQuery(query);
	}
}