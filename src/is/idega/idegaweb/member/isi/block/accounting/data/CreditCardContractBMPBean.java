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
import com.idega.user.data.User;

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

    protected final static String COLUMN_USER = "user_id";

    protected final static String COLUMN_CARD_NUMBER = "number";

    protected final static String COLUMN_CARD_EXPIRES = "expires";

    protected final static String COLUMN_FIRST_PAYMENT = "first_payment";

    protected final static String COLUMN_NUMBER_OF_PAYMENTS = "nop";

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
        addManyToOneRelationship(COLUMN_USER, User.class);
        addAttribute(COLUMN_CARD_NUMBER, "Card number", true, true,
                String.class);
        addAttribute(COLUMN_CARD_EXPIRES, "Card expires", true, true,
                Date.class);
        addAttribute(COLUMN_FIRST_PAYMENT, "First payment", true, true,
                Date.class);
        addAttribute(COLUMN_NUMBER_OF_PAYMENTS, "Number of payments", true, true,
                Integer.class);
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
    
    public void setUser(User user) {
        setColumn(COLUMN_USER, user);
    }
    
    public void setUserId(int id) {
        setColumn(COLUMN_USER, id);
    }
    
    public void setCardNumber(String number) {
        setColumn(COLUMN_CARD_NUMBER, number);
    }
    
    public void setCardExpires(Date expires) {
        setColumn(COLUMN_CARD_EXPIRES, expires);
    }
    
    public void setFirstPayment(Date date) {
        setColumn(COLUMN_FIRST_PAYMENT, date);
    }

    public void setNumberOfPayments(int nop) {
        setColumn(COLUMN_NUMBER_OF_PAYMENTS, nop);
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
    
    public User getUser() {
        return (User) getColumnValue(COLUMN_USER);
    }
    
    public int getUserId() {
        return getIntColumnValue(COLUMN_USER);
    }
    
    public String getCardNumber() {
        return getStringColumnValue(COLUMN_CARD_NUMBER);
    }
    
    public Date getCardExpires() {
        return getDateColumnValue(COLUMN_CARD_EXPIRES);
    }
    
    public Date getFirstPayment() {
        return getDateColumnValue(COLUMN_FIRST_PAYMENT);
    }
    
    public int getNumberOfPayments() {
        return getIntColumnValue(COLUMN_NUMBER_OF_PAYMENTS);
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
}