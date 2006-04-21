/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
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
public class CreditCardContractBMPBean extends GenericEntity implements
		CreditCardContract {

	protected final static String ENTITY_NAME = "isi_credit_contract";

	protected final static String COLUMN_CLUB = "club_id";

	protected final static String COLUMN_DIVISION = "div_id";

	protected final static String COLUMN_GROUP = "group_id";

	protected final static String COLUMN_CONTRACT_NR = "contract_number";

	protected final static String COLUMN_CARD_TYPE = "card_type_id";

	protected final static String COLUMN_DELETED = "deleted";

	protected final static String COLUMN_PERSONAL_ID = "personal_id";

	protected final static String COLUMN_COMPANY_NUMBER = "company_number";

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
		addAttribute(COLUMN_CONTRACT_NR, "Contract number", true, true,
				String.class);
		addManyToOneRelationship(COLUMN_CARD_TYPE, CreditCardType.class);
		addAttribute(COLUMN_DELETED, "Deleted", true, true, Boolean.class);
		addAttribute(COLUMN_PERSONAL_ID, "Personal id", String.class, 20);
		addAttribute(COLUMN_COMPANY_NUMBER, "Company number", String.class, 10);
	}

	public void setClubID(int id) {
		setColumn(COLUMN_CLUB, id);
	}

	public void setClub(Group club) {
		setColumn(COLUMN_CLUB, club);
	}

	public void setDivisionId(int id) {
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

	public void setContractNumber(String number) {
		setColumn(COLUMN_CONTRACT_NR, number);
	}

	public void setCardTypeId(int id) {
		setColumn(COLUMN_CARD_TYPE, id);
	}

	public void setCardType(CreditCardType type) {
		setColumn(COLUMN_CARD_TYPE, type);
	}

	public void setDeleted(boolean deleted) {
		setColumn(COLUMN_DELETED, deleted);
	}

	public void setPersonalId(String personalId) {
		setColumn(COLUMN_PERSONAL_ID, personalId);
	}

	public void setCompanyNumber(String companyNumber) {
		setColumn(COLUMN_COMPANY_NUMBER, companyNumber);
	}

	public int getClubID() {
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

	public String getContractNumber() {
		return getStringColumnValue(COLUMN_CONTRACT_NR);
	}

	public int getCardTypeId() {
		return getIntColumnValue(COLUMN_CARD_TYPE);
	}

	public CreditCardType getCardType() {
		return (CreditCardType) getColumnValue(COLUMN_CARD_TYPE);
	}

	public boolean getDeleted() {
		return getBooleanColumnValue(COLUMN_DELETED, false);
	}

	public String getPersonalId() {
		return getStringColumnValue(COLUMN_PERSONAL_ID);
	}

	public String getCompanyNumber() {
		return getStringColumnValue(COLUMN_COMPANY_NUMBER);
	}

	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere();
		sql.appendLeftParenthesis();
		sql.append(COLUMN_DELETED);
		sql.append(" is null ");
		sql.appendOr();
		sql.appendEquals(COLUMN_DELETED, false);
		sql.appendRightParenthesis();

		return idoFindPKsByQuery(sql);
	}

	public Collection ejbFindAllByClub(Group club) throws FinderException {
		return ejbFindAllByClubDivisionGroupAndType(club, null, null, null);
	}

	public Collection ejbFindAllByClubAndType(Group club, CreditCardType type)
			throws FinderException {
		return ejbFindAllByClubDivisionGroupAndType(club, null, null, type);
	}

	public Collection ejbFindAllByClubDivisionAndType(Group club,
			Group division, CreditCardType type) throws FinderException {
		return ejbFindAllByClubDivisionGroupAndType(club, division, null, type);
	}

	public Collection ejbFindAllByClubDivisionGroupAndType(Group club,
			Group division, Group group, CreditCardType type)
			throws FinderException {
		return ejbFindAllByClubDivisionGroupAndType(club, division, group, type, false);
	}

	public Collection ejbFindAllByClubDivisionGroupAndType(Group club,
			Group division, Group group, CreditCardType type, boolean getDeleted)
			throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CLUB, ((Integer) club.getPrimaryKey())
				.intValue());
		if (division != null) {
			sql.appendAndEquals(COLUMN_DIVISION, division);
		}
		if (group != null) {
			sql.appendAndEquals(COLUMN_GROUP, group);
		}
		if (type != null) {
			sql.appendAndEquals(COLUMN_CARD_TYPE, type);
		}
		if (!getDeleted) {
			sql.appendAnd();
			sql.appendLeftParenthesis();
			sql.appendEquals(COLUMN_DELETED, false);
			sql.appendOr();
			sql.append(COLUMN_DELETED);
			sql.append(" is null");
			sql.appendRightParenthesis();
		}
		sql.appendOrderBy();
		sql.append(COLUMN_DIVISION);
		sql.append(", ");
		sql.append(COLUMN_GROUP);
		
		System.out.println("sql = " + sql.toString());

		return idoFindPKsByQuery(sql);
	}

	public Collection ejbFindAllClubContracts() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere();
		sql.append(" not ");
		sql.append(COLUMN_CLUB);
		sql.append(" is null ");
		sql.appendAnd();
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

		return idoFindPKsByQuery(sql);
	}

	public Object ejbFindByGroupAndType(Group group, CreditCardType type)
			throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_GROUP, group);
		sql.appendAnd();
		sql.appendEquals(COLUMN_CARD_TYPE, type);
		sql.appendAnd();
		sql.appendLeftParenthesis();
		sql.appendEquals(COLUMN_DELETED, false);
		sql.appendOr();
		sql.append(COLUMN_DELETED);
		sql.append(" is null");
		sql.appendRightParenthesis();

		System.out.println("sql = " + sql.toString());

		return idoFindOnePKByQuery(sql);
	}

	public Object ejbFindByDivisionAndType(Group division, CreditCardType type)
			throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_DIVISION, division);
		sql.appendAnd();
		sql.append(COLUMN_GROUP);
		sql.append(" is null ");
		sql.appendAnd();
		sql.appendEquals(COLUMN_CARD_TYPE, type);
		sql.appendAnd();
		sql.appendLeftParenthesis();
		sql.appendEquals(COLUMN_DELETED, false);
		sql.appendOr();
		sql.append(COLUMN_DELETED);
		sql.append(" is null");
		sql.appendRightParenthesis();

		System.out.println("sql = " + sql.toString());

		return idoFindOnePKByQuery(sql);
	}

	public Object ejbFindByClubAndType(Group club, CreditCardType type)
			throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CLUB, club);
		sql.appendAnd();
		sql.append(COLUMN_DIVISION);
		sql.append(" is null ");
		sql.appendAnd();
		sql.append(COLUMN_GROUP);
		sql.append(" is null ");
		sql.appendAnd();
		sql.appendEquals(COLUMN_CARD_TYPE, type);
		sql.appendAnd();
		sql.appendLeftParenthesis();
		sql.appendEquals(COLUMN_DELETED, false);
		sql.appendOr();
		sql.append(COLUMN_DELETED);
		sql.append(" is null");
		sql.appendRightParenthesis();

		System.out.println("sql = " + sql.toString());

		return idoFindOnePKByQuery(sql);
	}
}