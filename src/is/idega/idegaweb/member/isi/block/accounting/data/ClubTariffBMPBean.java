/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;

/**
 * @author palli
 */
public class ClubTariffBMPBean extends GenericEntity implements ClubTariff {
	protected final static String ENTITY_NAME = "isi_club_tariff";
	
	protected final static String COLUMN_CLUB = "club_id";
	protected final static String COLUMN_GROUP = "group_id";
	protected final static String COLUMN_TARIFF_TYPE = "tariff_type_id";
	protected final static String COLUMN_TEXT = "text";
	protected final static String COLUMN_AMOUNT = "amount";
	protected final static String COLUMN_PERIOD_FROM = "period_from";
	protected final static String COLUMN_PERIOD_TO = "period_to";
	protected final static String COLUMN_DELETED = "deleted";

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
		addManyToOneRelationship(COLUMN_CLUB,Group.class);
		addManyToOneRelationship(COLUMN_GROUP,Group.class);
		addManyToOneRelationship(COLUMN_TARIFF_TYPE,ClubTariffType.class);
		addAttribute(COLUMN_TEXT,"Text",true,true,String.class);
		addAttribute(COLUMN_AMOUNT,"Amount",true,true,Float.class);
		addAttribute(COLUMN_PERIOD_FROM,"Period from",true,true,Date.class);
		addAttribute(COLUMN_PERIOD_TO,"Period to",true,true,Date.class);
		addAttribute(COLUMN_DELETED,"Deleted",true,true,Boolean.class);
	}

	public void setClubID(int id) {
		setColumn(COLUMN_CLUB,id);
	}
	
	public void setClub(Group club) {
		setColumn(COLUMN_CLUB,club);
	}

	public void setGroupId(int id) {
		setColumn(COLUMN_GROUP,id);
	}
	
	public void setGroup(Group group) {
		setColumn(COLUMN_GROUP,group);
	}
	
	public void setTariffTypeId(int id) {
		setColumn(COLUMN_TARIFF_TYPE,id);
	}
	
	public void setTariffType(ClubTariffType type) {
		setColumn(COLUMN_TARIFF_TYPE,type);
	}
	
	public void setText(String text) {
		setColumn(COLUMN_TEXT,text);
	}
	
	public void setAmount(float amount) {
		setColumn(COLUMN_AMOUNT,amount);
	}
	
	public void setPeriodFrom(Date from) {
		setColumn(COLUMN_PERIOD_FROM,from);
	}
	
	public void setPeriodTo(Date to) {
		setColumn(COLUMN_PERIOD_TO,to);
	}
	
	public int getClubID() {
		return getIntColumnValue(COLUMN_CLUB);
	}
	
	public Group getClub() {
		return (Group)getColumnValue(COLUMN_CLUB);
	}

	public int getGroupId() {
		return getIntColumnValue(COLUMN_GROUP);
	}
	
	public Group getGroup() {
		return (Group)getColumnValue(COLUMN_GROUP);
	}
	
	public int getTariffTypeId() {
		return getIntColumnValue(COLUMN_TARIFF_TYPE);
	}
	
	public ClubTariffType getTariffType() {
		return (ClubTariffType)getColumnValue(COLUMN_TARIFF_TYPE);
	}
	
	public String getText() {
		return getStringColumnValue(COLUMN_TEXT);
	}
	
	public float getAmount() {
		return getFloatColumnValue(COLUMN_AMOUNT);
	}
	
	public Date getPeriodFrom() {
		return getDateColumnValue(COLUMN_PERIOD_FROM);
	}
	
	public Date getPeriodTo() {
		return getDateColumnValue(COLUMN_PERIOD_TO);
	}
	
	public void setDeleted(boolean deleted) {
		setColumn(COLUMN_DELETED,deleted);
	}
	
	public boolean getDeleted() {
		return getBooleanColumnValue(COLUMN_DELETED);
	}
	
	public Collection ejbFindAllByClub(Group club) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CLUB, ((Integer)club.getPrimaryKey()).intValue());
		sql.appendAnd();
		sql.appendLeftParenthesis();
		sql.appendEquals(COLUMN_DELETED,false);
		sql.appendOr();
		sql.append(COLUMN_DELETED);
		sql.append(" is null");
		sql.appendRightParenthesis();
		
		System.out.println("sql =" + sql.toString());
		
		return idoFindPKsByQuery(sql);
	}
}