/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;

/**
 * @author palli
 */
public class ClubTariffTypeBMPBean extends GenericEntity implements ClubTariffType {
	protected final static String ENTITY_NAME = "isi_tariff_type";

	protected final static String COLUMN_TARIFF_TYPE = "tariff_type";
	protected final static String COLUMN_NAME = "tariff_type_name";
	protected final static String COLUMN_LOCALIZED_KEY = "localized_key";
	protected final static String COLUMN_CLUB = "club_id";
	protected final static String COLUMN_DELETED = "deleted";

//	protected final static String TYPE_MEMBER_FEE = "MEMBER_FEE";
//	protected final static String TYPE_PRACTISE_FEE = "PRACTISE_FEE";
//	protected final static String TYPE_TOURNAMENT_FEE = "TOURNAMENT_FEE";

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
		addAttribute(COLUMN_TARIFF_TYPE, "Tariff type", true, true, java.lang.String.class, 255);
		addAttribute(COLUMN_NAME, "Tariff name", true, true, java.lang.String.class, 255);
		addAttribute(COLUMN_LOCALIZED_KEY, "Tariff localized key", true, true, java.lang.String.class, 255);
		addManyToOneRelationship(COLUMN_CLUB, Group.class);
		addAttribute(COLUMN_DELETED, "Deleted", true, true, Boolean.class);
		setNullable(COLUMN_CLUB, true);
	}

/*	public void insertStartData() throws Exception {

		String types[] = { TYPE_MEMBER_FEE, TYPE_PRACTISE_FEE, TYPE_TOURNAMENT_FEE };

		String names[] = { "Membership fee", "Practise fee", "Tournament fee" };

		ClubTariffTypeHome typeHome = (ClubTariffTypeHome) IDOLookup.getHome(ClubTariffType.class);
		ClubTariffType type;

		for (int i = 0; i < types.length; i++) {
			type = typeHome.create();
			type.setTariffType(types[i]);
			type.setName(names[i]);
			StringBuffer b = new StringBuffer(ENTITY_NAME);
			b.append(".");
			b.append(types[i]);
			type.setLocalizedKey(b.toString());
			type.store();
		}
	}*/

	public void setTariffType(String type) {
		setColumn(COLUMN_TARIFF_TYPE, type);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setLocalizedKey(String key) {
		setColumn(COLUMN_LOCALIZED_KEY, key);
	}

	public void setClub(Group club) {
		setColumn(COLUMN_CLUB, club);
	}

	public void setClubId(int clubId) {
		setColumn(COLUMN_CLUB, clubId);
	}

	public void setDeleted(boolean deleted) {
		setColumn(COLUMN_DELETED, deleted);
	}
	
	public String getTariffType() {
		return getStringColumnValue(COLUMN_TARIFF_TYPE);
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public String getLocalizedKey() {
		return getStringColumnValue(COLUMN_LOCALIZED_KEY);
	}

	public Group getClub() {
		return (Group) getColumnValue(COLUMN_CLUB);
	}

	public int getClubId() {
		return getIntColumnValue(COLUMN_CLUB);
	}
	
	public boolean getDeleted() {
		return getBooleanColumnValue(COLUMN_DELETED, false);
	}

	public Collection ejbFindAllByClub(Group club) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere();
		sql.appendEquals(COLUMN_CLUB, ((Integer)club.getPrimaryKey()).intValue());
		sql.appendAnd();
		sql.appendLeftParenthesis();
		sql.appendEquals(COLUMN_DELETED,false);
		sql.appendOr();
		sql.append(COLUMN_DELETED);
		sql.append(" is null");
		sql.appendRightParenthesis();
		
		return idoFindPKsByQuery(sql);
	}
}