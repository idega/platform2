/*
 * $Id: SchoolClassMemberLogBMPBean.java,v 1.1 2005/01/04 13:52:24 laddi Exp $
 * Created on 27.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.sql.Date;
import java.util.Collection;

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
 * Last modified: $Date: 2005/01/04 13:52:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class SchoolClassMemberLogBMPBean extends GenericEntity  implements SchoolClassMemberLog{

	public final static String ENTITY_NAME = "sch_class_member_log";
	
	public final static String USER = "ic_user_id";
	public final static String SCHOOLCLASS = "sch_school_class_id";
	public final static String START_DATE = "start_date";
	public final static String END_DATE = "end_date";

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
		addManyToOneRelationship(USER, User.class);
		addManyToOneRelationship(SCHOOLCLASS, SchoolClass.class);
		addAttribute(START_DATE, "Starting date of placement", Date.class);
		addAttribute(END_DATE, "End date of placement", Date.class);
	}

	//Getters
	public User getUser() {
		return (User) getColumnValue(USER);
	}
	
	public int getUserID() {
		return getIntColumnValue(USER);
	}
	
	public SchoolClass getSchoolClass() {
		return (SchoolClass) getColumnValue(SCHOOLCLASS);
	}
	
	public int getSchoolClassID() {
		return getIntColumnValue(SCHOOLCLASS);
	}
	
	public Date getStartDate() {
		return getDateColumnValue(START_DATE);
	}
	
	public Date getEndDate() {
		return getDateColumnValue(END_DATE);
	}
	
	//Setters
	public void setUser(User user) {
		setColumn(USER, user);
	}
	
	public void setUser(Object userPK) {
		setColumn(USER, userPK);
	}
	
	public void setSchoolClass(SchoolClass schoolClass) {
		setColumn(SCHOOLCLASS, schoolClass);
	}
	
	public void setSchoolClass(Object schoolClassPK) {
		setColumn(SCHOOLCLASS, schoolClassPK);
	}
	
	public void setStartDate(Date startDate) {
		setColumn(START_DATE, startDate);
	}
	
	public void setEndDate(Date endDate) {
		setColumn(END_DATE, endDate);
	}
	
	//Find methods
	public Collection ejbFindAllByUser(User user) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, USER, MatchCriteria.EQUALS, user));
		query.addOrder(new Order(new Column(table, START_DATE), true));
		
		return idoFindPKsByQuery(query);
	}
	
	public Integer ejbFindOpenLogByUser(User user) throws FinderException {
		return ejbFindOpenLogByUserAndSchoolClass(user, null);
	}
	
	public Integer ejbFindOpenLogByUserAndSchoolClass(User user, SchoolClass schoolClass) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, USER, MatchCriteria.EQUALS, user));
		if (schoolClass != null) {
			query.addCriteria(new MatchCriteria(table, SCHOOLCLASS, MatchCriteria.EQUALS, schoolClass));
		}
		query.addCriteria(new MatchCriteria(new Column(table, END_DATE), false));
		
		return (Integer) idoFindOnePKByQuery(query);
	}
	
	public Integer ejbFindLatestLogByUser(User user) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, USER, MatchCriteria.EQUALS, user));
		query.addOrder(new Order(new Column(table, START_DATE), false));
		
		return (Integer) idoFindOnePKByQuery(query);
	}
}