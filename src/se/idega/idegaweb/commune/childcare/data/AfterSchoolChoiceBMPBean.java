/*
 * $Id: AfterSchoolChoiceBMPBean.java,v 1.4 2003/10/05 20:07:06 laddi Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.data;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.childcare.check.data.GrantedCheck;

import com.idega.block.contract.data.Contract;
import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

/**
 * This  does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class AfterSchoolChoiceBMPBean extends ChildCareApplicationBMPBean implements AfterSchoolChoice {

	private final static String CASE_CODE_KEY = "MBFRITV";
	private final static String CASE_CODE_KEY_DESC = "Application for after school centre";

	private final static String SCHOOL_SEASON = "school_season_id";

	/**
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeKey()
	 */
	public String getCaseCodeKey() {
		return CASE_CODE_KEY;
	}

	/**
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeDescription()
	 */
	public String getCaseCodeDescription() {
		return CASE_CODE_KEY_DESC;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	
	public void initializeAttributes() {
		super.initializeAttributes();
		this.addManyToOneRelationship(SCHOOL_SEASON, SchoolSeason.class);
	}

	public int getSchoolSeasonId() {
		return getIntColumnValue(SCHOOL_SEASON);
	}

	public void setSchoolSeasonId(int schoolSeasonID) {
		setColumn(SCHOOL_SEASON, schoolSeasonID);
	}

	public Collection ejbFindByChildAndSeason(Integer childID, Integer seasonID) throws javax.ejb.FinderException {
		IDOQuery query = super.idoQueryGetSelect().appendWhereEquals(CHILD_ID, childID.intValue());
		query.appendAndEquals(SCHOOL_SEASON, seasonID.intValue());
		query.appendOrderBy(CHOICE_NUMBER);

		return super.idoFindPKsByQuery(query);
	}
	
	public Object ejbFindByChildAndChoiceNumberAndSeason(Integer childID,Integer choiceNumber, Integer seasonID) throws javax.ejb.FinderException {
		IDOQuery query = super.idoQueryGetSelect().appendWhereEquals(CHILD_ID, childID.intValue());
		query.appendAndEquals(SCHOOL_SEASON, seasonID.intValue());
		query.appendAndEquals(CHOICE_NUMBER,choiceNumber.intValue());
		query.appendOrderBy(CHOICE_NUMBER);

		return super.idoFindOnePKByQuery(query);
	}

	public Object ejbFindByChildAndChoiceNumberAndSeason(Integer childID,Integer choiceNumber, Integer seasonID, String[] caseStatus) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append("c , proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+SCHOOL_SEASON, seasonID.intValue());
		sql.appendAndEquals("c."+CHOICE_NUMBER,choiceNumber.intValue());
		sql.appendAndEquals("c."+CHILD_ID,childID.intValue());
		sql.appendAnd().append("p.case_status").appendInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendOrderBy("c."+QUEUE_DATE+",c."+QUEUE_ORDER);

		return super.idoFindOnePKByQuery(sql);
	}
	
	public Collection ejbFindAllCasesByProviderAndStatus(int providerId, CaseStatus caseStatus) throws FinderException {
		return ejbFindAllCasesByProviderStatus(providerId, caseStatus.getStatus());
	}

	public Collection ejbFindAllCasesByProviderAndStatus(School provider, String caseStatus) throws FinderException {
		return ejbFindAllCasesByProviderStatus(((Integer)provider.getPrimaryKey()).intValue(), caseStatus);
	}
	
	public Collection ejbFindAllCasesByProviderAndStatus(School provider, CaseStatus caseStatus) throws FinderException {
		return ejbFindAllCasesByProviderStatus(((Integer)provider.getPrimaryKey()).intValue(), caseStatus.getStatus());
	}
	
	public Collection ejbFindAllCasesByProviderAndStatus(int providerId, String caseStatus) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append("c , proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		sql.appendAnd().appendEqualsQuoted("p.case_status",caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendOrderBy("c."+QUEUE_DATE+",c."+QUEUE_ORDER);

		return idoFindPKsBySQL(sql.toString());
	}	
	
}