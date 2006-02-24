/*
 * $Id: AfterSchoolChoiceBMPBean.java,v 1.3.2.3 2006/02/24 11:41:50 dainis Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.care.data;

import java.util.Collection;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.business.CareConstants;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.data.IDOQuery;

/**
 * This  does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class AfterSchoolChoiceBMPBean extends ChildCareApplicationBMPBean implements ChildCareApplication, AfterSchoolChoice {

	private final static String CASE_CODE_KEY_DESC = "Application for after school centre";

	private final static String SCHOOL_SEASON = "school_season_id";
	private final static String COLUMN_PAYER_NAME = "payer_name";
	private final static String COLUMN_PAYER_PERSONAL_ID = "payer_personal_id";
	private final static String COLUMN_CARD_TYPE = "card_type";
	private final static String COLUMN_CARD_NUMBER = "card_number";
	private final static String COLUMN_CARD_VALID_MONTH = "card_valid_month";
	private final static String COLUMN_CARD_VALID_YEAR = "card_valid_year";
	private final static String COLUMN_F_CLASS = "f_class";
	private final static String COLUMN_WANTS_REFRESHMENT = "wants_refreshment";

	/**
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeKey()
	 */
	public String getCaseCodeKey() {
		return CareConstants.AFTER_SCHOOL_CASE_CODE_KEY;
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
		
		addAttribute(COLUMN_PAYER_NAME, "Payer name", String.class);
		addAttribute(COLUMN_PAYER_PERSONAL_ID, "Payer personal ID", String.class);
		addAttribute(COLUMN_CARD_TYPE, "Card type", String.class);
		addAttribute(COLUMN_CARD_NUMBER, "Card number", String.class);
		addAttribute(COLUMN_CARD_VALID_MONTH, "Valid month", Integer.class);
		addAttribute(COLUMN_CARD_VALID_YEAR, "Valid year", Integer.class);
		
		addAttribute(COLUMN_F_CLASS, "F-class", Boolean.class);

		addAttribute(COLUMN_WANTS_REFRESHMENT, "Wants refreshments", Boolean.class);
	}

	public int getSchoolSeasonId() {
		return getIntColumnValue(SCHOOL_SEASON);
	}
	
	public String getPayerName() {
		return getStringColumnValue(COLUMN_PAYER_NAME);
	}
	
	public String getPayerPersonalID() {
		return getStringColumnValue(COLUMN_PAYER_PERSONAL_ID);
	}
	
	public String getCardType() {
		return getStringColumnValue(COLUMN_CARD_TYPE);
	}
	
	public String getCardNumber() {
		return getStringColumnValue(COLUMN_CARD_NUMBER);
	}
	
	public int getCardValidMonth() {
		return getIntColumnValue(COLUMN_CARD_VALID_MONTH);
	}
	
	public int getCardValidYear() {
		return getIntColumnValue(COLUMN_CARD_VALID_YEAR);
	}
	
	public boolean getFClass() {
		return getBooleanColumnValue(COLUMN_F_CLASS, false);
	}
	
	public boolean getWantsRefreshments() {
		return getBooleanColumnValue(COLUMN_WANTS_REFRESHMENT, false);
	}

	public void setSchoolSeasonId(int schoolSeasonID) {
		setColumn(SCHOOL_SEASON, schoolSeasonID);
	}
	
	public void setPayerName(String name) {
		setColumn(COLUMN_PAYER_NAME, name);
	}
	
	public void setPayerPersonalID(String personalID) {
		setColumn(COLUMN_PAYER_PERSONAL_ID, personalID);
	}
	
	public void setCardType(String type) {
		setColumn(COLUMN_CARD_TYPE, type);
	}
	
	public void setCardNumber(String number) {
		setColumn(COLUMN_CARD_NUMBER, number);
	}
	
	public void setCardValidMonth(int month) {
		setColumn(COLUMN_CARD_VALID_MONTH, month);
	}
	
	public void setCardValidYear(int year) {
		setColumn(COLUMN_CARD_VALID_YEAR, year);
	}
	
	public void setFClass(boolean fClass) {
		setColumn(COLUMN_F_CLASS, fClass);
	}	
	
	public void setWantsRefreshments(boolean wantsRefreshments) {
		setColumn(COLUMN_WANTS_REFRESHMENT, wantsRefreshments);
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
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+SCHOOL_SEASON, seasonID.intValue());
		sql.appendAndEquals("c."+CHOICE_NUMBER,choiceNumber.intValue());
		sql.appendAndEquals("c."+CHILD_ID,childID.intValue());
		sql.appendAnd().append("p.case_status").appendInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CareConstants.AFTER_SCHOOL_CASE_CODE_KEY);
		sql.appendOrderBy("c."+QUEUE_DATE+",c."+QUEUE_ORDER);

		return super.idoFindOnePKByQuery(sql);
	}
	
	public Object ejbFindByChildAndProviderAndSeason(int childID, int providerID, int seasonID, String[] caseStatus) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+SCHOOL_SEASON, seasonID);
		sql.appendAndEquals("c."+PROVIDER_ID,providerID);
		sql.appendAndEquals("c."+CHILD_ID,childID);
		sql.appendAnd().append("p.case_status").appendInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code", CareConstants.AFTER_SCHOOL_CASE_CODE_KEY);
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
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		sql.appendAnd().appendEqualsQuoted("p.case_status",caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CareConstants.AFTER_SCHOOL_CASE_CODE_KEY);
		sql.appendOrderBy("c."+QUEUE_DATE+",c."+QUEUE_ORDER);

		return idoFindPKsBySQL(sql.toString());
	}	
	
	public Collection ejbFindAllCasesByProviderAndNotInStatus(int providerId, String[] caseStatus) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		sql.appendAnd().append("p.case_status").appendNotInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CareConstants.AFTER_SCHOOL_CASE_CODE_KEY);
		sql.appendOrderBy("c."+QUEUE_DATE+",c."+QUEUE_ORDER);

		return idoFindPKsBySQL(sql.toString());
	}	
	
	//same as above but with a parameter for sorting and also selects from ic_user
	public Collection ejbFindAllCasesByProviderAndNotInStatus(int providerId, String[] caseStatus, String sorting) throws FinderException {
		IDOQuery sql = idoQuery();
		//sql.appendSelect().append("*.c").append(", *.p, iu.first_name, iu.last_name");
		sql.appendSelectAllFrom(this).append(" c, proc_case p, ic_user iu");
		//sql.append(", ic_user iu");
		//sql.appendFrom().append(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		sql.appendAnd().appendEquals("c.child_id","iu.ic_user_id");
		sql.appendAnd().append("p.case_status").appendNotInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code", CareConstants.AFTER_SCHOOL_CASE_CODE_KEY);
		sql.appendOrderBy(sorting+",c."+QUEUE_ORDER);

		return idoFindPKsBySQL(sql.toString());
	}	
	
}