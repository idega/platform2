/*
 * $Id: GrantedCheckBMPBean.java,v 1.1 2004/10/07 14:19:51 thomas Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.check.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.user.data.User;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class GrantedCheckBMPBean extends GenericEntity implements GrantedCheck {
	private static final String ENTITY_NAME = "cc_granted_check";

	private static final String COLUMN_CHILD_ID = "CHILD_ID";
//	private static final String COLUMN_SCHOOL_ID = "school_id";
	private static final String COLUMN_GRANTED = "granted";
	private static final String COLUMN_LAST_USED = "last_used";
	private static final String COLUMN_CHECK_ID = "check_id";

	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_CHILD_ID, User.class);
		addAttribute(COLUMN_GRANTED, "", true, true, Timestamp.class);
		addAttribute(COLUMN_LAST_USED, "", true, true, Timestamp.class);
//		addManyToOneRelationship(COLUMN_SCHOOL_ID, School.class);
		addManyToOneRelationship(COLUMN_CHECK_ID, Check.class);
		setNullable(COLUMN_CHILD_ID, false);
		setUnique(COLUMN_CHILD_ID,true);
	}

	public void setChild(User child) {
		setColumn(COLUMN_CHILD_ID, child);
	}

	public void setChildId(int childId) {
		setColumn(COLUMN_CHILD_ID, childId);
	}

	public void setDateGranted(Timestamp date) {
		setColumn(COLUMN_GRANTED, date);
	}

	public void setDateLastUsed(Timestamp date) {
		setColumn(COLUMN_LAST_USED, date);
	}

//	public void setSchool(School school) {
//		setColumn(COLUMN_SCHOOL_ID, school);
//	}
//
//	public void setSchoolId(int schoolId) {
//		setColumn(COLUMN_SCHOOL_ID, schoolId);
//	}

	public void setCheck(Check check) {
		setColumn(COLUMN_CHECK_ID, check);
	}

	public void setCheckId(int checkId) {
		setColumn(COLUMN_CHECK_ID, checkId);
	}

	public User getChild() {
		return (User) getColumnValue(COLUMN_CHILD_ID);
	}

	public int getChildId() {
		return getIntColumnValue(COLUMN_CHILD_ID);
	}

	public Timestamp getDateGranted() {
		return (Timestamp) getColumnValue(COLUMN_GRANTED);
	}

	public Timestamp getDateLastUsed() {
		return (Timestamp) getColumnValue(COLUMN_LAST_USED);
	}

//	public School getSchool() {
//		return (School) getColumnValue(COLUMN_SCHOOL_ID);
//	}
//
//	public int getSchoolId() {
//		return getIntColumnValue(COLUMN_SCHOOL_ID);
//	}

	public Check getCheck() {
		return (Check) getColumnValue(COLUMN_CHECK_ID);
	}

	public int getCheckId() {
		return getIntColumnValue(COLUMN_CHECK_ID);
	}
	
	public Collection ejbFindChecks() throws FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityName());
		return idoFindPKsBySQL(sql.toString());
	}
	
	public Integer ejbFindChecksByUser(User user) throws FinderException {
		return ejbFindChecksByUser(((Integer)user.getPrimaryKey()).intValue());
	}
	
	public Integer ejbFindChecksByUser(int userID) throws FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(COLUMN_CHILD_ID);
		sql.append(" = ");
		sql.append(userID);
		return (Integer) idoFindOnePKBySQL(sql.toString());
	}	
}