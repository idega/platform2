/*
 * $Id: SchoolClassMemberLogBMPBean.java,v 1.10 2005/02/17 11:43:56 laddi Exp $
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
import com.idega.data.query.Criteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/02/17 11:43:56 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.10 $
 */
public class SchoolClassMemberLogBMPBean extends GenericEntity  implements SchoolClassMemberLog{

	public final static String ENTITY_NAME = "sch_class_member_log";
	
	public final static String USER_PLACING = "placing_user";
	public final static String USER_TERMINATING = "terminating_user";
	public final static String SCHOOLCLASS = "sch_school_class_id";
	public final static String SCHOOLCLASSMEMBER = "sch_class_member_id";
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
		addManyToOneRelationship(USER_PLACING, User.class);
		addManyToOneRelationship(USER_TERMINATING, User.class);
		addManyToOneRelationship(SCHOOLCLASS, SchoolClass.class);
		addManyToOneRelationship(SCHOOLCLASSMEMBER, SchoolClassMember.class);
		addAttribute(START_DATE, "Starting date of placement", Date.class);
		addAttribute(END_DATE, "End date of placement", Date.class);
	}

	//Getters
	public User getUserPlacing() {
		return (User) getColumnValue(USER_PLACING);
	}
	
	public int getUserPlacingID() {
		return getIntColumnValue(USER_PLACING);
	}
	
	public User getUserTerminating() {
		return (User) getColumnValue(USER_TERMINATING);
	}
	
	public int getUserTerminatingID() {
		return getIntColumnValue(USER_TERMINATING);
	}
	
	public SchoolClass getSchoolClass() {
		return (SchoolClass) getColumnValue(SCHOOLCLASS);
	}
	
	public int getSchoolClassID() {
		return getIntColumnValue(SCHOOLCLASS);
	}
	
	public SchoolClassMember getSchoolClassMember() {
		return (SchoolClassMember) getColumnValue(SCHOOLCLASSMEMBER);
	}
	
	public int getSchoolClassMemberID() {
		return getIntColumnValue(SCHOOLCLASSMEMBER);
	}
	
	public Date getStartDate() {
		return getDateColumnValue(START_DATE);
	}
	
	public Date getEndDate() {
		return getDateColumnValue(END_DATE);
	}
	
	//Setters
	public void setUserPlacing(User user) {
		setColumn(USER_PLACING, user);
	}
	
	public void setUserPlacing(Object userPK) {
		setColumn(USER_PLACING, userPK);
	}
	
	public void setUserTerminating(User user) {
		setColumn(USER_TERMINATING, user);
	}
	
	public void setUserTerminating(Object userPK) {
		setColumn(USER_TERMINATING, userPK);
	}
	
	public void setSchoolClass(SchoolClass schoolClass) {
		setColumn(SCHOOLCLASS, schoolClass);
	}
	
	public void setSchoolClass(Object schoolClassPK) {
		setColumn(SCHOOLCLASS, schoolClassPK);
	}
	
	public void setSchoolClassMember(SchoolClassMember schoolClassMember) {
		setColumn(SCHOOLCLASSMEMBER, schoolClassMember);
	}
	
	public void setSchoolClassMember(Object schoolClassMemberPK) {
		setColumn(SCHOOLCLASSMEMBER, schoolClassMemberPK);
	}
	
	public void setStartDate(Date startDate) {
		setColumn(START_DATE, startDate);
	}
	
	public void setEndDate(Date endDate) {
		setColumn(END_DATE, endDate);
	}
	
	//Find methods
	public Collection ejbFindAllBySchoolClassMember(SchoolClassMember member) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOLCLASSMEMBER, MatchCriteria.EQUALS, member));
		query.addOrder(new Order(new Column(table, START_DATE), true));
		
		return idoFindPKsByQuery(query);
	}
	
	public Integer ejbFindOpenLogByUser(SchoolClassMember member) throws FinderException {
		return ejbFindOpenLogByUserAndSchoolClass(member, null);
	}
	
	public Integer ejbFindOpenLogByUserAndSchoolClass(SchoolClassMember member, SchoolClass schoolClass) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOLCLASSMEMBER, MatchCriteria.EQUALS, member));
		if (schoolClass != null) {
			query.addCriteria(new MatchCriteria(table, SCHOOLCLASS, MatchCriteria.EQUALS, schoolClass));
		}
		query.addCriteria(new MatchCriteria(new Column(table, END_DATE), false));
		
		return (Integer) idoFindOnePKByQuery(query);
	}
	
	public Integer ejbFindLatestLogByUser(SchoolClassMember member) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOLCLASSMEMBER, MatchCriteria.EQUALS, member));
		query.addOrder(new Order(new Column(table, START_DATE), false));
		
		return (Integer) idoFindOnePKByQuery(query);
	}

	public Integer ejbFindByPlacementAndDate(SchoolClassMember member, Date date) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOLCLASSMEMBER, MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(table, START_DATE, MatchCriteria.GREATEREQUAL, date));	
		Criteria a = new MatchCriteria(table, END_DATE, MatchCriteria.LESSEQUAL, date);
		Criteria b = new MatchCriteria(table, END_DATE, MatchCriteria.IS, MatchCriteria.NULL);
		query.addCriteria(new OR(a, b));
		query.addOrder(new Order(new Column(table, START_DATE), true));
		
		return (Integer) idoFindOnePKByQuery(query);
	}

	public Integer ejbFindFutureLogByPlacementAndDate(SchoolClassMember member, Date date) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOLCLASSMEMBER, MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(table, START_DATE, MatchCriteria.GREATEREQUAL, date));	
		query.addOrder(new Order(new Column(table, START_DATE), true));
		
		return (Integer) idoFindOnePKByQuery(query);
	}

	public Integer ejbFindByPlacementAndEndDate(SchoolClassMember member, Date date) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOLCLASSMEMBER, MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(table, END_DATE, MatchCriteria.EQUALS, date));	
		query.addOrder(new Order(new Column(table, START_DATE), true));
		
		return (Integer) idoFindOnePKByQuery(query);
	}

	public Integer ejbFindByPlacementAndDateBack(SchoolClassMember member, Date date) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(getIDColumnName()));
		query.addCriteria(new MatchCriteria(table, SCHOOLCLASSMEMBER, MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(table, START_DATE, MatchCriteria.LESSEQUAL, date));	
		Criteria a = new MatchCriteria(table, END_DATE, MatchCriteria.GREATEREQUAL, date);
		Criteria b = new MatchCriteria(table, END_DATE, MatchCriteria.IS, MatchCriteria.NULL);
		query.addCriteria(new OR(a, b));
		query.addOrder(new Order(new Column(table, START_DATE), true));
		
		return (Integer) idoFindOnePKByQuery(query);
	}

	
	public Collection ejbFindByPlacementAndDates(SchoolClassMember member, Date fromDate, Date toDate) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOLCLASSMEMBER, MatchCriteria.EQUALS, member));
		query.addCriteria(new MatchCriteria(table, START_DATE, MatchCriteria.GREATEREQUAL, fromDate));	
		if (toDate != null) {
			query.addCriteria(new MatchCriteria(table, END_DATE, MatchCriteria.LESSEQUAL, toDate));
		}
		query.addOrder(new Order(new Column(table, START_DATE), false));
		
		return idoFindPKsByQuery(query);
	}
}