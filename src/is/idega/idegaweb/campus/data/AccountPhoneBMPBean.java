/*
 * $Id: AccountPhoneBMPBean.java,v 1.4 2004/06/04 17:32:19 aron Exp $
 * 
 * Copyright (C) 2001-2004 Idega hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */

package is.idega.idegaweb.campus.data;


import java.sql.Date;
import java.sql.SQLException;


/**
 * 
 * Title:
 * 
 * Description:
 * 
 * Copyright: Copyright (c) 2001
 * 
 * Company: idega.is
 * 
 * @author 2000 - idega team -<br>
 *         <a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * 
 * @version 1.0
 *  
 */

public class AccountPhoneBMPBean extends com.idega.data.GenericEntity implements
        is.idega.idegaweb.campus.data.AccountPhone {

    /*
     * create view V_PHONE_ACCOUNTS ( FIN_ACCOUNT_ID,CAM_PHONE_NUMBER
     * ,VALID_FROM,VALID_TO,DELIVER_DATE,RETURN_DATE) as select
     * ACC.fin_account_id, PHO.PHONE_NUMBER ,con.valid_from ,con.valid_to from
     * cam_phone pho,cam_contract con, fin_account acc where pho.bu_apartment_id =
     * con.bu_apartment_id and acc.ic_user_id = con.ic_user_id and
     * acc.account_type = 'PHONE' and con.status in ('S', 'E', 'U', 'T')
     */

    public static String getEntityTableName() {
        return "V_PHONE_ACCOUNTS";
    }

    public static String getColumnNameAccountId() {
        return "FIN_ACCOUNT_ID";
    }

    public static String getColumnNamePhoneNumber() {
        return "CAM_PHONE_NUMBER";
    }

    public static String getColumnNameValidTo() {
        return "VALID_TO";
    }

    public static String getColumnNameValidFrom() {
        return "VALID_FROM";
    }

    public static String getColumnDeliverDate() {
        return "DELIVER_DATE";
    }

    public static String getColumnReturnDate() {
        return "RETURN_DATE";
    }

    public AccountPhoneBMPBean() {
    }

    public AccountPhoneBMPBean(int id) throws SQLException {
    }

    public void initializeAttributes() {
        addAttribute(getColumnNameAccountId(), "Account Id", true, true,
                java.lang.Integer.class);
        addAttribute(getColumnNamePhoneNumber(), "Phone number", true, true,
                java.lang.String.class);
        addAttribute(getColumnNameValidFrom(), "Valid from", true, true,
                java.sql.Date.class);
        addAttribute(getColumnNameValidTo(), "Valid to", true, true,
                java.sql.Date.class);
    }

    public String getEntityName() {
        return (getEntityTableName());
    }

    public void setValidFrom(Date date) {
        setColumn(getColumnNameValidFrom(), date);
    }

    public Date getValidFrom() {
        return ((Date) getColumnValue(getColumnNameValidFrom()));
    }

    public void setValidTo(Date date) {
        setColumn(getColumnNameValidTo(), date);
    }

    public Date getValidTo() {
        return ((Date) getColumnValue(getColumnNameValidTo()));
    }

    public Integer getAccountId() {
        return getIntegerColumnValue(getColumnNameAccountId());
    }

    public String getPhoneNumber() {
        return getStringColumnValue(getColumnNamePhoneNumber());
    }

    public void insert() throws SQLException {
    }

    public void delete() throws SQLException {
    }
}