/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.sql.Timestamp;
import java.util.Collection;
import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.data.IDOUtil;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author palli
 */
public class FinanceEntryBMPBean extends GenericEntity implements FinanceEntry {
	protected final static String ENTITY_NAME = "isi_ass_entry"; //:)
	
	protected final static String COLUMN_USER_ID = "user_id";
	protected final static String COLUMN_ASSESSMENT_ROUND_ID = "assessment_id";
	protected final static String COLUMN_CLUB_ID = "club_id";
	protected final static String COLUMN_DIVISION_ID = "division_id";
	protected final static String COLUMN_GROUP_ID = "group_id";
	protected final static String COLUMN_AMOUNT = "amount";
	protected final static String COLUMN_STATUS = "status";
	protected final static String COLUMN_TYPE = "entry_type";
	protected final static String COLUMN_DATE_OF_ENTRY = "date_of_entry";
	protected final static String COLUMN_INFO = "text_info";
	protected final static String COLUMN_INSERTED_BY = "inserted_by";
	protected final static String COLUMN_AMOUNT_EQUALIZED = "eq_amount";
	protected final static String COLUMN_OPEN = "entry_open";
	
	protected final static String STATUS_CREATED = "C";
	protected final static String STATUS_READY = "R";
	protected final static String STATUS_SENT = "S";
	
	protected final static String TYPE_ASSESSMENT = "A";
	protected final static String TYPE_MANUAL = "M";
	protected final static String TYPE_PAYMENT = "P";
//	protected final static String TYPE_
	
	protected static final String STRING_TYPE_MANUAL = "isi_acc_fin_entry_manual_type";
	protected static final String STRING_TYPE_AUTOMATIC = "isi_acc_fin_entry_auto_type";
	protected static final String STRING_TYPE_PAYMENT = "isi_acc_fin_entry_pay_type";
	
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

		addManyToOneRelationship(COLUMN_USER_ID, User.class);
		addManyToOneRelationship(COLUMN_ASSESSMENT_ROUND_ID, AssessmentRound.class);
		addManyToOneRelationship(COLUMN_CLUB_ID, Group.class);
		addManyToOneRelationship(COLUMN_DIVISION_ID, Group.class);
		addManyToOneRelationship(COLUMN_GROUP_ID, Group.class);
		addAttribute(COLUMN_AMOUNT, "Amount", true, true, Double.class);
		addAttribute(COLUMN_STATUS, "Status", true, true, String.class, 1);
		addAttribute(COLUMN_TYPE, "Type", true, true, String.class, 1);
		addAttribute(COLUMN_DATE_OF_ENTRY, "Timestamp", true, true, Timestamp.class);
		addAttribute(COLUMN_INFO, "Text info", true, true, String.class, 255);
		addManyToOneRelationship(COLUMN_INSERTED_BY, User.class);
		addAttribute(COLUMN_AMOUNT_EQUALIZED, "Amount equalized", true, true, Double.class);
		addAttribute(COLUMN_OPEN, "Open", true, true, Boolean.class);
		
		setNullable(COLUMN_USER_ID, false);
		setNullable(COLUMN_ASSESSMENT_ROUND_ID, true);
		setNullable(COLUMN_CLUB_ID, false);
		setNullable(COLUMN_GROUP_ID, true);
	}

	public void setStatusCreated() {
		setColumn(COLUMN_STATUS, STATUS_CREATED);
	}

	public void setStatusReady() {
		setColumn(COLUMN_STATUS, STATUS_READY);
	}
	
	public void setStatusSent() {
		setColumn(COLUMN_STATUS, STATUS_SENT);
	}
	
	public void setTypeAssessment() {
		setColumn(COLUMN_TYPE, TYPE_ASSESSMENT);
	}

	public void setTypeManual() {
		setColumn(COLUMN_TYPE, TYPE_MANUAL);
	}
	
	public void setTypePayment() {
		setColumn(COLUMN_TYPE, TYPE_PAYMENT);
	}
	
	public String getTypeLocalizationKey() {
		String type = getStringColumnValue(COLUMN_TYPE);
		if (type != null && !"".equals(type)) {
			if (type.equals(TYPE_ASSESSMENT)) {
				return STRING_TYPE_AUTOMATIC;
			}
			else if (type.endsWith(TYPE_MANUAL)) {
				return STRING_TYPE_MANUAL;
			}
			else if (type.equals(TYPE_PAYMENT)) {
				return STRING_TYPE_PAYMENT;
			}
			else {
				return null;
			}
		}
		
		return null;
	}
	
	public double getAmount() {
		return getDoubleColumnValue(COLUMN_AMOUNT, 0);
	}

	public void setAmount(double amount) {
		setColumn(COLUMN_AMOUNT, amount);
	}

	public int getAssessmentRoundID() {
		return getIntColumnValue(COLUMN_ASSESSMENT_ROUND_ID);
	}

	public void setAssessmentRoundID(int id) {
		setColumn(COLUMN_ASSESSMENT_ROUND_ID, id);
	}
	
	public AssessmentRound getAssessmentRound() {
		return (AssessmentRound) getColumnValue(COLUMN_ASSESSMENT_ROUND_ID);
	}
	
	public void setAssessment(AssessmentRound assRound) {
		setColumn(COLUMN_ASSESSMENT_ROUND_ID, assRound);
	}

	public int getClubID() {
		return getIntColumnValue(COLUMN_CLUB_ID);
	}

	public void setClubID(int id) {
		setColumn(COLUMN_CLUB_ID, id);
	}
	
	public Group getClub() {
		return (Group) getColumnValue(COLUMN_CLUB_ID);
	}
	
	public void setClub(Group club) {
		setColumn(COLUMN_CLUB_ID, club);
	}

	public int getDivisionID() {
		return getIntColumnValue(COLUMN_DIVISION_ID);
	}

	public void setDivisionID(int id) {
		setColumn(COLUMN_DIVISION_ID, id);
	}
	
	public Group getDivision() {
		return (Group) getColumnValue(COLUMN_DIVISION_ID);
	}
	
	public void setDivision(Group club) {
		setColumn(COLUMN_DIVISION_ID, club);
	}
	
	public Timestamp getDateOfEntry() {
		return (Timestamp) getColumnValue(COLUMN_DATE_OF_ENTRY);
	}

	public void setDateOfEntry(Timestamp date) {
		setColumn(COLUMN_DATE_OF_ENTRY ,date);
	}

	public int getGroupID() {
		return getIntColumnValue(COLUMN_GROUP_ID);
	}

	public void setGroupID(int id) {
		setColumn(COLUMN_GROUP_ID ,id);
	}

	public Group getGroup() {
		return (Group) getColumnValue(COLUMN_GROUP_ID);
	}
	
	public void setGroup(Group group) {
		setColumn(COLUMN_GROUP_ID, group);
	}
	
	public int getUserID() {
		return getIntColumnValue(COLUMN_USER_ID);
	}

	public void setUserID(int id) {
		setColumn(COLUMN_USER_ID ,id);
	}
	
	public User getUser() {
		return (User) getColumnValue(COLUMN_USER_ID);
	}
	
	public void setUser(User user) {
		setColumn(COLUMN_USER_ID, user);
	}
	
	public String getInfo() {
		return getStringColumnValue(COLUMN_INFO);
	}
	
	public void setInfo(String info) {
		setColumn(COLUMN_INFO, info);
	}
	
	public int getInsertedByUserID() {
		return getIntColumnValue(COLUMN_INSERTED_BY);
	}

	public void setInsertedByUserID(int id) {
		setColumn(COLUMN_INSERTED_BY ,id);
	}
	
	public User getInsertedByUser() {
		return (User) getColumnValue(COLUMN_INSERTED_BY);
	}
	
	public void setInsertedByUser(User user) {
		setColumn(COLUMN_INSERTED_BY, user);
	}
	
	public double getAmountEqualized() {
		return getDoubleColumnValue(COLUMN_AMOUNT_EQUALIZED, 0);
	}

	public void setAmountEqualized(double amount) {
		setColumn(COLUMN_AMOUNT_EQUALIZED, amount);
	}
	
	public boolean getEntryOpen() {
		return getBooleanColumnValue(COLUMN_OPEN, true);
	}

	public void setEntryOpen(boolean open) {
		setColumn(COLUMN_OPEN, open);
	}
	
	public Collection ejbFindAllByAssessmentRound(AssessmentRound round) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_ASSESSMENT_ROUND_ID, round);
		
		return idoFindPKsByQuery(sql);
	}	
	
	public Collection ejbFindAllByUser(Group club, Group division, User user) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CLUB_ID, club);
		if (division != null) {
			sql.appendAnd();
			sql.appendEquals(COLUMN_DIVISION_ID, division);
		}
		sql.appendAnd();
		sql.appendEquals(COLUMN_USER_ID, user);
		sql.appendOrderBy();
		sql.append(getIDColumnName());
		sql.appendDescending();
		
		return idoFindPKsByQuery(sql);		
	}
	
	/**
	 * @param dateFrom
	 * @param dateTo
	 * @param divisions
	 * @param groups
	 * @return
	 * @throws FinderException
	 */
	public Collection ejbFindAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(
			Date dateFrom,
			Date dateTo,
			Collection divisions,
			Collection groups)
	throws FinderException {
		IDOUtil util = IDOUtil.getInstance();
		IDOQuery sql = idoQuery();
		String[] ordering = { COLUMN_DIVISION_ID, COLUMN_GROUP_ID , COLUMN_DATE_OF_ENTRY };
		String tableName = this.getEntityName();		
		sql.appendSelectAllFrom(tableName);
		sql.appendWhere().append(COLUMN_TYPE).appendIn("'A','M'");
		sql.appendAnd().appendWithinDates(COLUMN_DATE_OF_ENTRY, dateFrom, dateTo);
		if  (divisions != null && divisions.size()>0)
			sql.appendAnd().append(COLUMN_DIVISION_ID).appendIn(util.convertListToCommaseparatedString(divisions));
		if  (groups != null && groups.size()>0)
			sql.appendAnd().append(COLUMN_GROUP_ID).appendIn(util.convertListToCommaseparatedString(groups));
		sql.appendOrderBy(ordering);
		return idoFindIDsBySQL(sql.toString());
	}
}