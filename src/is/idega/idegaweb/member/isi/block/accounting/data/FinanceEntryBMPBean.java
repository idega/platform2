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

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
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
	protected final static String COLUMN_TARIFF_TYPE = "tariff_type";
	protected final static String COLUMN_AMOUNT = "amount";
	protected final static String COLUMN_STATUS = "status";
	protected final static String COLUMN_TYPE = "entry_type";
	protected final static String COLUMN_DATE_OF_ENTRY = "date_of_entry";
	protected final static String COLUMN_INFO = "text_info";
	
	protected final static String STATUS_CREATED = "C";
	protected final static String STATUS_READY = "R";
	protected final static String STATUS_SENT = "S";
	
	protected final static String TYPE_ASSESSMENT = "A";
	protected final static String TYPE_MANUAL = "M";
	protected final static String TYPE_PAYMENT = "P";
	
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
		addManyToOneRelationship(COLUMN_TARIFF_TYPE, ClubTariffType.class);
		addAttribute(COLUMN_AMOUNT, "Amount", true, true, Double.class);
		addAttribute(COLUMN_STATUS, "Status", true, true, String.class, 1);
		addAttribute(COLUMN_TYPE, "Type", true, true, String.class, 1);
		addAttribute(COLUMN_DATE_OF_ENTRY, "Timestamp", true, true, Timestamp.class);
		addAttribute(COLUMN_INFO, "Text info", true, true, String.class, 255);
		
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
	
	public int getTariffTypeID() {
		return getIntColumnValue(COLUMN_TARIFF_TYPE);
	}

	public void setTariffTypeID(int id) {
		setColumn(COLUMN_TARIFF_TYPE ,id);
	}

	public ClubTariffType getTariffType() {
		return (ClubTariffType) getColumnValue(COLUMN_TARIFF_TYPE);
	}
	
	public void setGroup(ClubTariffType tariffType) {
		setColumn(COLUMN_TARIFF_TYPE, tariffType);
	}
	
	public Collection ejbFindAllByAssessmentRound(AssessmentRound round) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_ASSESSMENT_ROUND_ID, round);
		
		return idoFindPKsByQuery(sql);
	}	
}