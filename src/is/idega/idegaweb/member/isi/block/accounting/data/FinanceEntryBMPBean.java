/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.Batch;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.basket.data.BasketItem;
import com.idega.data.GenericEntity;
import com.idega.data.IDOPrimaryKey;
import com.idega.data.IDOQuery;
import com.idega.data.IDOUtil;
import com.idega.data.PrimaryKey;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserBMPBean;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class FinanceEntryBMPBean extends GenericEntity implements FinanceEntry,
        BasketItem {
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

    protected final static String COLUMN_TARIFF_ID = "tariff";

    protected final static String COLUMN_TARIFF_TYPE_ID = "tariff_type";

    protected final static String COLUMN_INSERTED_BY = "inserted_by";

    protected final static String COLUMN_AMOUNT_EQUALIZED = "eq_amount";

    protected final static String COLUMN_OPEN = "entry_open";

    protected final static String COLUMN_PAYMENT_TYPE_ID = "payment_type_id";

    protected final static String COLUMN_DISCOUNT_PERC = "discount_perc";

    protected final static String COLUMN_DISCOUNT_AMOUNT = "discount_amount";

    protected final static String COLUMN_DISCOUNT_INFO = "discount_info";

    protected final static String COLUMN_PAYMENT_DATE = "payment_date";

    protected final static String COLUMN_SENT = "sent";

    protected final static String COLUMN_PAYMENT_CONTRACT_ID = "pay_cont_id";
    
    protected final static String COLUMN_CREDIT_CARD_BATCH_ID = "cc_batch_id";

    protected final static String STATUS_CREATED = "C";

    protected final static String STATUS_READY = "R";

    protected final static String STATUS_SENT = "S";

    public final static String TYPE_ASSESSMENT = "A";

    public final static String TYPE_MANUAL = "M";

    public final static String TYPE_PAYMENT = "P";

    public final static String ENTRY_OPEN_YES = "Y";

    public final static String ENTRY_OPEN_NO = "N";

    protected static final String STRING_TYPE_MANUAL = "isi_acc_fin_entry_manual_type";

    protected static final String STRING_TYPE_AUTOMATIC = "isi_acc_fin_entry_auto_type";

    protected static final String STRING_TYPE_PAYMENT = "isi_acc_fin_entry_pay_type";

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

        addManyToOneRelationship(COLUMN_USER_ID, User.class);
        addManyToOneRelationship(COLUMN_ASSESSMENT_ROUND_ID,
                AssessmentRound.class);
        addManyToOneRelationship(COLUMN_CLUB_ID, Group.class);
        addManyToOneRelationship(COLUMN_DIVISION_ID, Group.class);
        addManyToOneRelationship(COLUMN_GROUP_ID, Group.class);
        addManyToOneRelationship(COLUMN_TARIFF_ID, ClubTariff.class);
        addManyToOneRelationship(COLUMN_TARIFF_TYPE_ID, ClubTariffType.class);
        addAttribute(COLUMN_AMOUNT, "Amount", true, true, Double.class);
        addAttribute(COLUMN_STATUS, "Status", true, true, String.class, 1);
        addAttribute(COLUMN_TYPE, "Type", true, true, String.class, 1);
        addAttribute(COLUMN_DATE_OF_ENTRY, "Timestamp", true, true,
                Timestamp.class);
        addAttribute(COLUMN_INFO, "Text info", true, true, String.class, 255);
        addManyToOneRelationship(COLUMN_INSERTED_BY, User.class);
        addAttribute(COLUMN_AMOUNT_EQUALIZED, "Amount equalized", true, true,
                Double.class);
        addAttribute(COLUMN_OPEN, "Open", true, true, Boolean.class);
        addManyToOneRelationship(COLUMN_PAYMENT_TYPE_ID, PaymentType.class);
        addAttribute(COLUMN_DISCOUNT_PERC, "Discount %", true, true,
                Double.class);
        addAttribute(COLUMN_DISCOUNT_AMOUNT, "Discount amount", true, true,
                Double.class);
        addAttribute(COLUMN_DISCOUNT_INFO, "Discount info", true, true,
                String.class, 255);
        addAttribute(COLUMN_PAYMENT_DATE, "Payment date", true, true,
                Timestamp.class);
        addAttribute(COLUMN_SENT, "Sent", true, true, Boolean.class);
        addManyToOneRelationship(COLUMN_PAYMENT_CONTRACT_ID,
                PaymentContract.class);
        addManyToOneRelationship(COLUMN_CREDIT_CARD_BATCH_ID, Batch.class);

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

    public String getType() {
        return getStringColumnValue(COLUMN_TYPE);
    }

    public void setPaymentTypeID(int payment_type_id) {
        setColumn(COLUMN_PAYMENT_TYPE_ID, payment_type_id);
    }

    public void setPaymentType(PaymentType type) {
        setColumn(COLUMN_PAYMENT_TYPE_ID, type);
    }

    public int getPaymentTypeID() {
        return getIntColumnValue(COLUMN_PAYMENT_TYPE_ID);
    }

    public PaymentType getPaymentType() {
        return (PaymentType) getColumnValue(COLUMN_PAYMENT_TYPE_ID);
    }

    public String getTypeLocalizationKey() {
        String type = getStringColumnValue(COLUMN_TYPE);
        if (type != null && !"".equals(type)) {
            if (type.equals(TYPE_ASSESSMENT)) {
                return STRING_TYPE_AUTOMATIC;
            } else if (type.endsWith(TYPE_MANUAL)) {
                return STRING_TYPE_MANUAL;
            } else if (type.equals(TYPE_PAYMENT)) {
                return STRING_TYPE_PAYMENT;
            } else {
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
        setColumn(COLUMN_DATE_OF_ENTRY, date);
    }

    public int getGroupID() {
        return getIntColumnValue(COLUMN_GROUP_ID);
    }

    public void setGroupID(int id) {
        setColumn(COLUMN_GROUP_ID, id);
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
        setColumn(COLUMN_USER_ID, id);
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

    public int getTariffID() {
        return getIntColumnValue(COLUMN_TARIFF_ID);
    }

    public void setTariffID(int id) {
        setColumn(COLUMN_TARIFF_ID, id);
    }

    public ClubTariff getTariff() {
        return (ClubTariff) getColumnValue(COLUMN_TARIFF_ID);
    }

    public void setTariff(ClubTariff clubTariff) {
        setColumn(COLUMN_TARIFF_ID, clubTariff);
    }

    public int getTariffTypeID() {
        return getIntColumnValue(COLUMN_TARIFF_TYPE_ID);
    }

    public void setTariffTypeID(int id) {
        setColumn(COLUMN_TARIFF_TYPE_ID, id);
    }

    public ClubTariffType getTariffType() {
        return (ClubTariffType) getColumnValue(COLUMN_TARIFF_TYPE_ID);
    }

    public void setTariffType(ClubTariffType clubTariffType) {
        setColumn(COLUMN_TARIFF_TYPE_ID, clubTariffType);
    }

    public int getInsertedByUserID() {
        return getIntColumnValue(COLUMN_INSERTED_BY);
    }

    public void setInsertedByUserID(int id) {
        setColumn(COLUMN_INSERTED_BY, id);
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

    public void setDiscountPerc(double perc) {
        setColumn(COLUMN_DISCOUNT_PERC, perc);
    }

    public double getDiscountPerc() {
        return getDoubleColumnValue(COLUMN_DISCOUNT_PERC, 0);
    }

    public void setDiscountAmount(double amount) {
        setColumn(COLUMN_DISCOUNT_AMOUNT, amount);
    }

    public double getDiscountAmount() {
        return getDoubleColumnValue(COLUMN_DISCOUNT_AMOUNT, 0);
    }

    public void setDiscountInfo(String info) {
        setColumn(COLUMN_DISCOUNT_INFO, info);
    }

    public String getDiscountInfo() {
        return getStringColumnValue(COLUMN_DISCOUNT_INFO);
    }

    public Timestamp getPaymentDate() {
        return (Timestamp) getColumnValue(COLUMN_PAYMENT_DATE);
    }

    public void setPaymentDate(Timestamp date) {
        setColumn(COLUMN_PAYMENT_DATE, date);
    }

    public boolean getSent() {
        return getBooleanColumnValue(COLUMN_SENT, false);
    }

    public void setSent(boolean sent) {
        setColumn(COLUMN_SENT, sent);
    }

    public void setSent(Boolean sent) {
        setColumn(COLUMN_SENT, sent);
    }

    public void setPaymentContract(PaymentContract contract) {
        setColumn(COLUMN_PAYMENT_CONTRACT_ID, contract);
    }

    public void setPaymentContractId(int id) {
        setColumn(COLUMN_PAYMENT_CONTRACT_ID, id);
    }

    public PaymentContract getPaymentContract() {
        return (PaymentContract) getColumnValue(COLUMN_PAYMENT_CONTRACT_ID);
    }

    public int getPaymentContractId() {
        return getIntColumnValue(COLUMN_PAYMENT_CONTRACT_ID);
    }
    
    public void setCreditCardBatchID(int id) {
        setColumn(COLUMN_CREDIT_CARD_BATCH_ID, id);
    }
    
    public void setCreditCardBatch(Batch batch) {
        setColumn(COLUMN_CREDIT_CARD_BATCH_ID, batch);
    }
    
    public int getCreditCardBatchID() {
        return getIntColumnValue(COLUMN_CREDIT_CARD_BATCH_ID);
    }
    
    public Batch getCreditCardBatch() {
        return (Batch) getColumnValue(COLUMN_CREDIT_CARD_BATCH_ID);
    }
    
    public Collection ejbFindAllByAssessmentRound(AssessmentRound round)
            throws FinderException {
        IDOQuery sql = idoQuery();
        sql.appendSelectAllFrom(this);
        sql.appendWhereEquals(COLUMN_ASSESSMENT_ROUND_ID, round);

        return idoFindPKsByQuery(sql);
    }

    public Collection ejbFindAllByUser(User user) throws FinderException {
        IDOQuery sql = idoQuery();
        sql.appendSelectAllFrom(this);
        sql.appendWhereEquals(COLUMN_USER_ID, user);
        //sql.appendOrderBy();
        //sql.append(getIDColumnName());
        //sql.appendDescending();

        return idoFindPKsByQuery(sql);
    }

    public Collection ejbFindAllByUser(Group club, Group division, User user)
            throws FinderException {
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

    public Collection ejbFindAllOpenAssessmentByUser(Group club,
            Group division, User user) throws FinderException {
        IDOQuery sql = idoQuery();
        sql.appendSelectAllFrom(this);
        sql.appendWhereEquals(COLUMN_CLUB_ID, club);
        if (division != null) {
            sql.appendAnd();
            sql.appendEquals(COLUMN_DIVISION_ID, division);
        }
        sql.appendAnd();
        sql.appendEquals(COLUMN_USER_ID, user);
        sql.appendAnd();
        sql.appendEquals(COLUMN_OPEN, true);
        sql.appendAnd();
        sql.append(COLUMN_TYPE);
        sql.append(" in ('");
        sql.append(TYPE_ASSESSMENT);
        sql.append("', '");
        sql.append(TYPE_MANUAL);
        sql.append("') ");
        sql.appendOrderBy();
        sql.append(getIDColumnName());
        sql.appendDescending();

        return idoFindPKsByQuery(sql);
    }

    public Collection ejbFindAllAssessmentByUser(Group club, Group division,
            User user) throws FinderException {
        return ejbFindAllAssessmentByUser(club, division, user, null);
    }

    public Collection ejbFindAllAssessmentByUser(Group club, Group division,
            User user, IWTimestamp entriesAfter) throws FinderException {
        IDOQuery sql = idoQuery();
        sql.appendSelectAllFrom(this);
        sql.appendWhereEquals(COLUMN_CLUB_ID, club);
        if (division != null) {
            sql.appendAnd();
            sql.appendEquals(COLUMN_DIVISION_ID, division);
        }
        sql.appendAnd();
        sql.appendEquals(COLUMN_USER_ID, user);
        sql.appendAnd();
        sql.append(COLUMN_TYPE);
        sql.append(" in ('");
        sql.append(TYPE_ASSESSMENT);
        sql.append("', '");
        sql.append(TYPE_MANUAL);
        sql.append("') ");
        if (entriesAfter != null) {
            sql.appendAnd();
            sql.append(COLUMN_DATE_OF_ENTRY);
            sql.appendGreaterThanOrEqualsSign();
            sql.append(entriesAfter.getDate());
        }
        sql.appendOrderBy();
        sql.append(getIDColumnName());
        sql.appendDescending();

        return idoFindPKsByQuery(sql);
    }

    public Collection ejbFindAllPaymentsByUser(Group club, Group division,
            User user) throws FinderException {
        IDOQuery sql = idoQuery();
        sql.appendSelectAllFrom(this);
        sql.appendWhereEquals(COLUMN_CLUB_ID, club);
        if (division != null) {
            sql.appendAnd();
            sql.appendEquals(COLUMN_DIVISION_ID, division);
        }
        sql.appendAnd();
        sql.appendEquals(COLUMN_USER_ID, user);
        sql.appendAnd();
        sql.appendEqualsQuoted(COLUMN_TYPE, TYPE_PAYMENT);
        sql.appendOrderBy();
        sql.append(getIDColumnName());
        sql.appendDescending();

        return idoFindPKsByQuery(sql);
    }

    /**
     * @param club
     * @param types
     * @param dateFrom
     * @param dateTo
     * @param divisions
     * @param groups
     * @return Collection
     * @throws FinderException
     */
    public Collection ejbFindAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(
            Group club, String[] types, Date dateFrom, Date dateTo,
            Collection divisions, Collection groups, String personalID)
            throws FinderException {
        IDOUtil util = IDOUtil.getInstance();
        IDOQuery sql = idoQuery();
        IWTimestamp stampFrom = new IWTimestamp(dateFrom);
        IWTimestamp stampTo = new IWTimestamp(dateTo);
        stampTo.addHours(23);
        stampTo.addMinutes(59);
        stampTo.addSeconds(59);
        final String F_ = "f."; // sql alias for finance records
        final String U_ = "u."; // sql alias for user
        final String[] tableNames = { ENTITY_NAME, UserBMPBean.SQL_TABLE_NAME };
        final String[] tableAliases = { "f", "u" };

        sql.appendSelect().append(F_).appendStar().appendFrom(tableNames,
                tableAliases);
        sql.appendWhereEquals(F_ + COLUMN_USER_ID, U_
                + UserBMPBean.FIELD_USER_ID);
        sql.appendAnd().appendWithinStamps(COLUMN_DATE_OF_ENTRY,
                stampFrom.getTimestamp(), stampTo.getTimestamp());
        if (types != null && !containsTypePayment(types))
            sql.appendAndEqualsQuoted(COLUMN_OPEN, ENTRY_OPEN_YES);
        if (types != null && types.length > 0)
            sql.appendAnd().append(COLUMN_TYPE).appendIn(
                    util.convertArrayToCommaseparatedString(types, true));
        if (club != null)
            sql.appendAndEquals(COLUMN_CLUB_ID, club.getPrimaryKey());
        if (divisions != null && divisions.size() > 0)
            sql.appendAnd().append(COLUMN_DIVISION_ID).appendIn(
                    util.convertListToCommaseparatedString(divisions));
        if (groups != null && groups.size() > 0)
            sql.appendAnd().append(COLUMN_GROUP_ID).appendIn(
                    util.convertListToCommaseparatedString(groups));
        if (personalID != null && !personalID.equals(""))
            sql.appendAnd().append(U_ + UserBMPBean.FIELD_PERSONAL_ID)
                    .appendLike().append("\'" + personalID + "%'");
        return idoFindIDsBySQL(sql.toString());
    }

    private boolean containsTypePayment(String[] types) {
        boolean containsTypePayment = false;
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(TYPE_PAYMENT))
                containsTypePayment = true;
        }
        return containsTypePayment;
    }

    /**
     * @param club
     * @param types
     * @param divisions
     * @param groups
     * @return Collection
     * @throws FinderException
     */
    public Collection ejbFindAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(
            Group club, String[] types, Collection divisions,
            Collection groups, String personalID) throws FinderException {
        IWTimestamp now = new IWTimestamp();
        now.addDays(1);
        IDOUtil util = IDOUtil.getInstance();
        IDOQuery sql = idoQuery();

        final String F_ = "f."; // sql alias for finance records
        final String A_ = "a."; // sql alias for assessment round
        final String U_ = "u."; // sql alias for user
        final String[] tableNames = { ENTITY_NAME,
                AssessmentRoundBMPBean.ENTITY_NAME, UserBMPBean.SQL_TABLE_NAME };
        final String[] tableAliases = { "f", "a", "u" };

        sql.appendSelect().append(F_).appendStar().appendFrom(tableNames,
                tableAliases);
        sql.appendWhereEquals(F_ + COLUMN_ASSESSMENT_ROUND_ID, A_
                + "ISI_ASS_ROUND_ID");
        sql
                .appendAndEquals(F_ + COLUMN_USER_ID, U_
                        + UserBMPBean.FIELD_USER_ID);
        sql.appendAnd().append(A_ + AssessmentRoundBMPBean.COLUMN_PAYMENT_DATE)
                .appendLessThanSign().append(now.getDate());
        sql.appendAndEqualsQuoted(COLUMN_OPEN, ENTRY_OPEN_YES);
        if (types != null && types.length > 0)
            sql.appendAnd().append(F_ + COLUMN_TYPE).appendIn(
                    util.convertArrayToCommaseparatedString(types, true));
        if (club != null)
            sql.appendAndEquals(F_ + COLUMN_CLUB_ID, club.getPrimaryKey());
        if (divisions != null && divisions.size() > 0)
            sql.appendAnd().append(F_ + COLUMN_DIVISION_ID).appendIn(
                    util.convertListToCommaseparatedString(divisions));
        if (groups != null && groups.size() > 0)
            sql.appendAnd().append(F_ + COLUMN_GROUP_ID).appendIn(
                    util.convertListToCommaseparatedString(groups));
        if (personalID != null && !personalID.equals(""))
            sql.appendAnd().append(U_ + UserBMPBean.FIELD_PERSONAL_ID)
                    .appendLike().append("\'" + personalID + "%'");
        return idoFindIDsBySQL(sql.toString());
    }
    
    public Collection ejbFindAllByGroupAndPaymentTypeNotInBatch(Group group, PaymentType type, IWTimestamp dateFrom, IWTimestamp dateTo) throws FinderException {
        IDOQuery sql = idoQuery();
        sql.appendSelectAllFrom(this);
        sql.appendWhereEquals(COLUMN_GROUP_ID, group);
        sql.appendAndEquals(COLUMN_PAYMENT_TYPE_ID, type);
        if (dateFrom != null) {
            sql.appendAnd();
//            sql.append
        }
        
        return idoFindPKsByQuery(sql);
    }

    //The methods needed to be implemented to be a BasketItem

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.block.basket.data.BasketItem#getDescription()
     */
    public String getItemDescription() {
        return getInfo();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.block.basket.data.BasketItem#getItemID()
     */
    public IDOPrimaryKey getItemID() {
        PrimaryKey key = new PrimaryKey();
        key.setPrimaryKeyValue(getIDColumnName(), getPrimaryKeyValue());

        return key;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.block.basket.data.BasketItem#getPrice()
     */
    public Double getItemPrice() {
        double remaining = getAmount() - getAmountEqualized()
                - getDiscountAmount();

        return new Double(remaining);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.block.basket.data.BasketItem#getItemName()
     */
    public String getItemName() {
        return getInfo();
    }
}