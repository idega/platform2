/*
 * $Id: SchoolClassMemberGradeBMPBean.java,v 1.2.2.1 2005/11/18 17:29:45 palli Exp $
 * Created on Jun 20, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.sql.Timestamp;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;


/**
 * Last modified: $Date: 2005/11/18 17:29:45 $ by $Author: palli $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2.2.1 $
 */
public class SchoolClassMemberGradeBMPBean extends GenericEntity  implements SchoolClassMemberGrade{

	private static final String ENTITY_NAME = "sch_class_member_grade";
	
	private static final String COLUMN_SCHOOL_CLASS_MEMBER = "sch_class_member_id";
	private static final String COLUMN_GRADE = "sch_grade_id";
	private static final String COLUMN_CREATED = "created";
	private static final String COLUMN_LOCKED = "locked";
	
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
		
		addManyToOneRelationship(COLUMN_SCHOOL_CLASS_MEMBER, "Student", SchoolClassMember.class);
		addManyToOneRelationship(COLUMN_GRADE, "Grade", Grade.class);
		
		addAttribute(COLUMN_CREATED, "Created", Timestamp.class);
		addAttribute(COLUMN_LOCKED, "Locked", Boolean.class);
	}
	
	//Getters
	public SchoolClassMember getStudent() {
		return (SchoolClassMember) getColumnValue(COLUMN_SCHOOL_CLASS_MEMBER);
	}
	
	public Object getStudentPK() {
		return getIntegerColumnValue(COLUMN_SCHOOL_CLASS_MEMBER);
	}
	
	public Grade getGrade() {
		return (Grade) getColumnValue(COLUMN_GRADE);
	}
	
	public Object getGradePK() {
		return getIntegerColumnValue(COLUMN_GRADE);
	}
	
	public Timestamp getCreated() {
		return getTimestampColumnValue(COLUMN_CREATED);
	}
	
	public boolean isLocked() {
		return getBooleanColumnValue(COLUMN_LOCKED, false);
	}
	
	//Setters
	public void setStudent(SchoolClassMember member) {
		setColumn(COLUMN_SCHOOL_CLASS_MEMBER, member);
	}
	
	public void setGrade(Grade grade) {
		setColumn(COLUMN_GRADE, grade);
	}
	
	public void setCreated(Timestamp created) {
		setColumn(COLUMN_CREATED, created);
	}
	
	public void setLocked(boolean locked) {
		setColumn(COLUMN_LOCKED, locked);
	}
	
	//Finders
	public Collection ejbFindAllByStudent(SchoolClassMember student) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName(), true);
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL_CLASS_MEMBER, MatchCriteria.EQUALS, student));
		query.addOrder(table, COLUMN_CREATED, false);
		
		return idoFindPKsByQuery(query);
	}

	public Object ejbFindByStudent(SchoolClassMember student) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL_CLASS_MEMBER, MatchCriteria.EQUALS, student));
		query.addOrder(table, COLUMN_CREATED, false);
		
		return idoFindOnePKByQuery(query);
	}
	
	public Object ejbFindFirstGradeSetForStudent(SchoolClassMember student) throws FinderException {
		//select * from sch_class_member_grade g
		//where g.sch_class_member_grade_id in (
		//select min(g2.sch_class_member_grade_id) from sch_class_member_grade g2 where g2.sch_class_member_id = m.sch_class_member_id))

		IDOQuery subquery = idoQuery();
		subquery.appendSelect();
		subquery.append("min(g2.sch_class_member_grade_id)");
		subquery.appendFrom("sch_class_member_grade g2");
		subquery.appendWhereEquals("g2.sch_class_member_id", student);
		
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).append(" g");
		query.appendWhere();
		query.append("g." + getIDColumnName());
		query.appendIn(subquery);

		System.out.println("sql = " + query.toString());
		
		return idoFindOnePKByQuery(query);

	}
}