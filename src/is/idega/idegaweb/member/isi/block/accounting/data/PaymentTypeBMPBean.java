/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;

/**
 * @author palli
 *  
 */
public class PaymentTypeBMPBean extends GenericEntity implements PaymentType {

    protected final static String ENTITY_NAME = "isi_payment_type";

    protected final static String COLUMN_NAME = "name";

    protected final static String COLUMN_LOCALIZATION_KEY = "loc_key";

    protected final static String COLUMN_DELETED = "deleted";

    protected final static String LOC_KEY_CASH = "isi_payment_type_cash";

    protected final static String LOC_KEY_CHECK = "isi_payment_type_check";

    protected final static String LOC_KEY_DEBITCARD = "isi_payment_type_debitcard";

    protected final static String LOC_KEY_CREDITCARD = "isi_payment_type_creditcard";
    
    protected final static String LOC_KEY_BANK = "isi_payment_type_bank";

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

        addAttribute(COLUMN_NAME, "Name", true, true, String.class);
        addAttribute(COLUMN_LOCALIZATION_KEY, "localization key", true, true,
                String.class);
        addAttribute(COLUMN_DELETED, "Deleted", true, true, Boolean.class);
    }

    public void insertStartData() throws Exception {
        String names[] = { "Cash", "Check", "Debitcard", "Creditcard", "Bank"};
        String lockey[] = { LOC_KEY_CASH, LOC_KEY_CHECK, LOC_KEY_DEBITCARD,
                LOC_KEY_CREDITCARD, LOC_KEY_BANK};

        PaymentTypeHome typeHome = (PaymentTypeHome) IDOLookup
                .getHome(PaymentType.class);
        PaymentType type;

        for (int i = 0; i < lockey.length; i++) {
            type = typeHome.create();
            type.setName(names[i]);
            type.setLocalizationKey(lockey[i]);
            type.setDeleted(false);
            type.store();
        }
    }

    public void setName(String name) {
        setColumn(COLUMN_NAME, name);
    }

    public String getName() {
        return getStringColumnValue(COLUMN_NAME);
    }

    public void setLocalizationKey(String key) {
        setColumn(COLUMN_LOCALIZATION_KEY, key);
    }

    public String getLocalizationKey() {
        return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
    }

    public void setDeleted(boolean deleted) {
        setColumn(COLUMN_DELETED, deleted);
    }

    public boolean getDeleted() {
        return getBooleanColumnValue(COLUMN_DELETED, false);
    }

    public Collection ejbFindAllPaymentTypes() throws FinderException {
        IDOQuery sql = idoQuery();
        sql.appendSelectAllFrom(this);
        sql.appendWhereEquals(COLUMN_DELETED, false);

        return idoFindPKsByQuery(sql);
    }
}