/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class ClubTariffBMPBean extends GenericEntity implements ClubTariff {
	protected final static String ENTITY_NAME = "isi_tariff";

	protected final static String COLUMN_CLUB = "club_id";
	protected final static String COLUMN_DIVISION = "division_id";
	protected final static String COLUMN_GROUP = "group_id";
	protected final static String COLUMN_TARIFF_TYPE = "tariff_type_id";
	protected final static String COLUMN_TEXT = "text";
	protected final static String COLUMN_AMOUNT = "amount";
	protected final static String COLUMN_PERIOD_FROM = "period_from";
	protected final static String COLUMN_PERIOD_TO = "period_to";
	protected final static String COLUMN_DELETED = "deleted";

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
		addManyToOneRelationship(COLUMN_CLUB, Group.class);
		addManyToOneRelationship(COLUMN_DIVISION, Group.class);
		addManyToOneRelationship(COLUMN_GROUP, Group.class);
		addManyToOneRelationship(COLUMN_TARIFF_TYPE, ClubTariffType.class);
		addAttribute(COLUMN_TEXT, "Text", true, true, String.class);
		addAttribute(COLUMN_AMOUNT, "Amount", true, true, Double.class);
		addAttribute(COLUMN_PERIOD_FROM, "Period from", true, true, Date.class);
		addAttribute(COLUMN_PERIOD_TO, "Period to", true, true, Date.class);
		addAttribute(COLUMN_DELETED, "Deleted", true, true, Boolean.class);
	}

	public void setClubID(int id) {
		setColumn(COLUMN_CLUB, id);
	}

	public void setClub(Group club) {
		setColumn(COLUMN_CLUB, club);
	}

	public void setDivisionID(int id) {
		setColumn(COLUMN_DIVISION, id);
	}

	public void setDivision(Group division) {
		setColumn(COLUMN_DIVISION, division);
	}

	public void setGroupId(int id) {
		setColumn(COLUMN_GROUP, id);
	}

	public void setGroup(Group group) {
		setColumn(COLUMN_GROUP, group);
	}

	public void setTariffTypeId(int id) {
		setColumn(COLUMN_TARIFF_TYPE, id);
	}

	public void setTariffType(ClubTariffType type) {
		setColumn(COLUMN_TARIFF_TYPE, type);
	}

	public void setText(String text) {
		setColumn(COLUMN_TEXT, text);
	}

	public void setAmount(double amount) {
		setColumn(COLUMN_AMOUNT, amount);
	}

	public void setPeriodFrom(Date from) {
		setColumn(COLUMN_PERIOD_FROM, from);
	}

	public void setPeriodTo(Date to) {
		setColumn(COLUMN_PERIOD_TO, to);
	}

	public void setDeleted(boolean deleted) {
		setColumn(COLUMN_DELETED, deleted);
	}

	public int getClubID() {
		return getIntColumnValue(COLUMN_CLUB);
	}

	public Group getClub() {
		return (Group) getColumnValue(COLUMN_CLUB);
	}

	public int getDivisionID() {
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

	public int getTariffTypeId() {
		return getIntColumnValue(COLUMN_TARIFF_TYPE);
	}

	public ClubTariffType getTariffType() {
		return (ClubTariffType) getColumnValue(COLUMN_TARIFF_TYPE);
	}

	public String getText() {
		return getStringColumnValue(COLUMN_TEXT);
	}

	public double getAmount() {
		return getDoubleColumnValue(COLUMN_AMOUNT);
	}

	public Date getPeriodFrom() {
		return getDateColumnValue(COLUMN_PERIOD_FROM);
	}

	public Date getPeriodTo() {
		return getDateColumnValue(COLUMN_PERIOD_TO);
	}

	public boolean getDeleted() {
		return getBooleanColumnValue(COLUMN_DELETED);
	}

	public Collection ejbFindAllByClub(Group club) throws FinderException {
		return ejbFindAllByClubAndDivision(club, null);
	}
	
	public Collection ejbFindAllByClubAndDivision(Group club, Group division) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CLUB, club);
		sql.appendAnd();
		if (division != null) {
			sql.appendEquals(COLUMN_DIVISION, division);
			sql.appendAnd();
		}
		sql.appendLeftParenthesis();
		sql.appendEquals(COLUMN_DELETED, false);
		sql.appendOr();
		sql.append(COLUMN_DELETED);
		sql.append(" is null");
		sql.appendRightParenthesis();
		sql.appendOrderBy();
		sql.append(COLUMN_DIVISION);
		sql.append(", ");
		sql.append(COLUMN_GROUP);
		sql.append(", ");
		sql.append(COLUMN_TARIFF_TYPE);
		
		return idoFindPKsByQuery(sql);
	}

	public Collection ejbFindAllValidByGroup(Group group) throws FinderException {
		IWTimestamp date = IWTimestamp.RightNow();
		date.setHour(0);
		date.setMinute(0);
		date.setSecond(0);
		date.setMilliSecond(0);
		
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_GROUP, group);
		sql.appendAnd();
		sql.appendLeftParenthesis();
		sql.appendEquals(COLUMN_DELETED, false);
		sql.appendOr();
		sql.append(COLUMN_DELETED);
		sql.append(" is null");
		sql.appendRightParenthesis();
		sql.appendAnd();
		sql.append(COLUMN_PERIOD_FROM);
		sql.appendGreaterThanOrEqualsSign();
		sql.append(date.getDate());
		sql.appendAnd();
		sql.append(date.getDate());
		sql.appendLessThanOrEqualsSign();
		sql.append(COLUMN_PERIOD_TO);
		
		return idoFindPKsByQuery(sql);
	}
	
	
	public Collection ejbFindByGroupAndTariffType(Group group, ClubTariffType type) throws FinderException {
		return ejbFindByGroupAndTariffType(group, type, null);
	}
	
	public Collection ejbFindByGroupAndTariffType(Group group, ClubTariffType type, IWTimestamp date) throws FinderException {
		if (date == null)
			date = IWTimestamp.RightNow();

		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_GROUP, group);
		sql.appendAnd();
		sql.appendEquals(COLUMN_TARIFF_TYPE, type);
		sql.appendAnd();
		sql.appendLeftParenthesis();
		sql.appendEquals(COLUMN_DELETED, false);
		sql.appendOr();
		sql.append(COLUMN_DELETED);
		sql.append(" is null");
		sql.appendRightParenthesis();
		sql.appendAnd();
		sql.append(COLUMN_PERIOD_FROM);
		sql.appendLessThanOrEqualsSign();
		sql.append(date.getDate());
		sql.appendAnd();
		sql.append(date.getDate());
		sql.appendLessThanOrEqualsSign();
		sql.append(COLUMN_PERIOD_TO);

		return idoFindPKsByQuery(sql);
	}	
}