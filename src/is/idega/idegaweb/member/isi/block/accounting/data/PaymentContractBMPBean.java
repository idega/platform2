/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.data;

import java.sql.Date;

import com.idega.data.GenericEntity;
import com.idega.user.data.User;

/**
 * @author palli
 */
public class PaymentContractBMPBean extends GenericEntity  implements PaymentContract{

    protected final static String ENTITY_NAME = "isi_payment_contract";

    protected final static String COLUMN_USER = "user_id";

    protected final static String COLUMN_CARD_NUMBER = "number";

    protected final static String COLUMN_CARD_EXPIRES = "expires";

    protected final static String COLUMN_CARD_TYPE = "card_type_id";

    protected final static String COLUMN_FIRST_PAYMENT = "first_payment";

    protected final static String COLUMN_NUMBER_OF_PAYMENTS = "nop";


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
        addManyToOneRelationship(COLUMN_USER, User.class);
        addAttribute(COLUMN_CARD_NUMBER, "Card number", true, true,
                String.class);
        addAttribute(COLUMN_CARD_EXPIRES, "Card expires", true, true,
                Date.class);
        addManyToOneRelationship(COLUMN_CARD_TYPE, CreditCardType.class);
        addAttribute(COLUMN_FIRST_PAYMENT, "First payment", true, true,
                Date.class);
        addAttribute(COLUMN_NUMBER_OF_PAYMENTS, "Number of payments", true, true,
                Integer.class);
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
    
    public void setCardTypeId(int id) {
        setColumn(COLUMN_CARD_TYPE, id);
    }

    public void setCardType(CreditCardType type) {
        setColumn(COLUMN_CARD_TYPE, type);
    }

    public void setFirstPayment(Date date) {
        setColumn(COLUMN_FIRST_PAYMENT, date);
    }

    public void setNumberOfPayments(int nop) {
        setColumn(COLUMN_NUMBER_OF_PAYMENTS, nop);
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
    
    public int getCardTypeId() {
        return getIntColumnValue(COLUMN_CARD_TYPE);
    }

    public CreditCardType getCardType() {
        return (CreditCardType) getColumnValue(COLUMN_CARD_TYPE);
    }

    public Date getFirstPayment() {
        return getDateColumnValue(COLUMN_FIRST_PAYMENT);
    }
    
    public int getNumberOfPayments() {
        return getIntColumnValue(COLUMN_NUMBER_OF_PAYMENTS);
    }

}