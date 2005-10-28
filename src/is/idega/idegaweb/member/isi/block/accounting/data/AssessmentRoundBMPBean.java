/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author palli
 */
public class AssessmentRoundBMPBean extends GenericEntity implements AssessmentRound {
	protected final static String ENTITY_NAME = "isi_ass_round";

	protected final static String COLUMN_NAME = "name";
	protected final static String COLUMN_EXECUTION_DATE = "executed_date";
	protected final static String COLUMN_EXECUTED_BY = "user_id";
	protected final static String COLUMN_CLUB = "club_id";
	protected final static String COLUMN_DIVISION = "division_id";
	protected final static String COLUMN_GROUP = "group_id";
	protected final static String COLUMN_START_TIME = "start_time";
	protected final static String COLUMN_END_TIME = "end_time";
	protected final static String COLUMN_INCLUDE_CHILDREN = "include_children";
	protected final static String COLUMN_DELETED = "deleted";
	protected final static String COLUMN_PAYMENT_DATE = "payment_date";
	protected final static String COLUMN_RUN_ON_DATE = "run_on_date";
	
//	protected final static String COLUMN_TARIFF_TYPE = "tariff_type_id";
	protected final static String COLUMN_AMOUNT = "amount";
	protected final static String COLUMN_PERIOD_FROM = "period_from";
	protected final static String COLUMN_PERIOD_TO = "period_to";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Assessment round name", true, true, String.class);
		addAttribute(COLUMN_EXECUTION_DATE, "The date of the assessment", true, true, Timestamp.class);
		addManyToOneRelationship(COLUMN_EXECUTED_BY, User.class);
		addManyToOneRelationship(COLUMN_CLUB, Group.class);
		addManyToOneRelationship(COLUMN_DIVISION, Group.class);
		addManyToOneRelationship(COLUMN_GROUP, Group.class);
		addAttribute(COLUMN_START_TIME, "The start time of the thread", true, true, Timestamp.class);
		addAttribute(COLUMN_END_TIME, "The end time of the thread", true, true, Timestamp.class);
		addAttribute(COLUMN_INCLUDE_CHILDREN, "Include children", true, true, Boolean.class);
		addAttribute(COLUMN_DELETED, "Deleted", true, true, Boolean.class);
		addManyToManyRelationShip(ClubTariffType.class);
		addAttribute(COLUMN_PAYMENT_DATE, "The payment date for the entries", true, true, Timestamp.class);
		addAttribute(COLUMN_RUN_ON_DATE, "The date the batch is run on", true, true, Timestamp.class);

//		addManyToOneRelationship(COLUMN_TARIFF_TYPE, ClubTariffType.class);
		addAttribute(COLUMN_AMOUNT, "Amount", true, true, Double.class);
		addAttribute(COLUMN_PERIOD_FROM, "Period from", true, true, Date.class);
		addAttribute(COLUMN_PERIOD_TO, "Period to", true, true, Date.class);
	}

	public void addTariffType(ClubTariffType tariffType) throws IDOAddRelationshipException {
		idoAddTo(tariffType);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setExecutionDate(Timestamp date) {
		setColumn(COLUMN_EXECUTION_DATE, date);
	}

	public void setExecutedById(int id) {
		setColumn(COLUMN_EXECUTED_BY, id);
	}

	public void setExecutedBy(User user) {
		setColumn(COLUMN_EXECUTED_BY, user);
	}

	public void setClubId(int id) {
		setColumn(COLUMN_CLUB, id);
	}

	public void setClub(Group club) {
		setColumn(COLUMN_CLUB, club);
	}

	public void setDivisionId(int id) {
		setColumn(COLUMN_DIVISION, id);
	}

	public void setDivision(Group club) {
		setColumn(COLUMN_DIVISION, club);
	}
	
	public void setGroupId(int id) {
		setColumn(COLUMN_GROUP, id);
	}

	public void setGroup(Group group) {
		setColumn(COLUMN_GROUP, group);
	}

	public void setStartTime(Timestamp time) {
		setColumn(COLUMN_START_TIME, time);
	}

	public void setEndTime(Timestamp time) {
		setColumn(COLUMN_END_TIME, time);
	}
	
	public void setIncludeChildren(boolean include) {
		setColumn(COLUMN_INCLUDE_CHILDREN, include);
	}
	
	public void setDeleted(boolean deleted) {
		setColumn(COLUMN_DELETED, deleted);
	}

	public void setPaymentDate(Timestamp time) {
		setColumn(COLUMN_PAYMENT_DATE, time);
	}
	
	public void setRunOnDate(Timestamp time) {
		setColumn(COLUMN_RUN_ON_DATE, time);
	}

/*	public void setTariffTypeId(int id) {
		setColumn(COLUMN_TARIFF_TYPE, id);
	}

	public void setTariffType(ClubTariffType type) {
		setColumn(COLUMN_TARIFF_TYPE, type);
	}*/

	public void setAmount(double amount) {
		setColumn(COLUMN_AMOUNT, amount);
	}

	public void setPeriodFrom(Date from) {
		setColumn(COLUMN_PERIOD_FROM, from);
	}

	public void setPeriodTo(Date to) {
		setColumn(COLUMN_PERIOD_TO, to);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public Timestamp getExecutionDate() {
		return getTimestampColumnValue(COLUMN_EXECUTION_DATE);
	}

	public int getExecutedById() {
		return getIntColumnValue(COLUMN_EXECUTED_BY);
	}

	public User getExecutedBy() {
		return (User) getColumnValue(COLUMN_EXECUTED_BY);
	}

	public int getClubId() {
		return getIntColumnValue(COLUMN_CLUB);
	}

	public Group getClub() {
		return (Group) getColumnValue(COLUMN_CLUB);
	}

	public int getDivisionId() {
		return getIntColumnValue(COLUMN_DIVISION);
	}

	public Group getDivision() {
		return (Group) getColumnValue(COLUMN_DIVISION);
	}
	
	public int getGroupId() {
		return getIntColumnValue(COLUMN_GROUP);
	}

	public Group getGroup() {
		return (Group) getColumnValue(COLUMN_GROUP);
	}

	public Timestamp getStartTime() {
		return (Timestamp) getColumnValue(COLUMN_START_TIME);
	}

	public Timestamp getEndTime() {
		return (Timestamp) getColumnValue(COLUMN_END_TIME);
	}
	
	public boolean getIncludeChildren() {
		return getBooleanColumnValue(COLUMN_INCLUDE_CHILDREN, false);
	}
	
	public boolean getDeleted() {
		return getBooleanColumnValue(COLUMN_DELETED, false);
	}

	public Timestamp getPaymentDate() {
		return (Timestamp) getColumnValue(COLUMN_PAYMENT_DATE);
	}
	
	public Timestamp getRunOnDate() {
		return (Timestamp) getColumnValue(COLUMN_RUN_ON_DATE);
	}
	
/*	public int getTariffTypeId() {
		return getIntColumnValue(COLUMN_TARIFF_TYPE);
	}

	public ClubTariffType getTariffType() {
		return (ClubTariffType) getColumnValue(COLUMN_TARIFF_TYPE);
	}*/

	public double getAmount() {
		return getDoubleColumnValue(COLUMN_AMOUNT);
	}

	public Date getPeriodFrom() {
		return getDateColumnValue(COLUMN_PERIOD_FROM);
	}

	public Date getPeriodTo() {
		return getDateColumnValue(COLUMN_PERIOD_TO);
	}
	
	public Collection ejbFindAllByClubAndDivision(Group club, Group div) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CLUB, club);
		if (div != null) {
			sql.appendAndEquals(COLUMN_DIVISION, div);
		}
		sql.appendAnd();
		sql.appendLeftParenthesis();
		sql.appendEquals(COLUMN_DELETED,false);
		sql.appendOr();
		sql.append(COLUMN_DELETED);
		sql.append(" is null");
		sql.appendRightParenthesis();
		sql.appendOrderByDescending(getIDColumnName());
		

		return idoFindPKsByQuery(sql);
	}
}