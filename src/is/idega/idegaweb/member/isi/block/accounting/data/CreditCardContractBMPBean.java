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
    
    public Collection ejbFindAll() throws FinderException {
        IDOQuery sql = idoQuery();
        sql.appendSelectAllFrom(this);

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
    
    public Object ejbFindByGroupAndType(Group group, CreditCardType type) throws FinderException {
        IDOQuery sql = idoQuery();
        sql.appendSelectAllFrom(this);
        sql.appendWhereEquals(COLUMN_GROUP, group);
        sql.appendAnd();
        sql.appendEquals(COLUMN_CARD_TYPE, type);
        
        return idoFindOnePKByQuery(sql);
    }
}