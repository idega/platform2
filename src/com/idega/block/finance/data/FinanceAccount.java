package com.idega.block.finance.data;

/**
 *  Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 * @author     <br>
 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>
 *
 * @created    5. mars 2002
 * @version    1.0
 */

public interface FinanceAccount {

    /**
     *  Gets the balance of the FinanceAccount object
     *
     * @return    The balance value
     */
    public float getBalance();


    /**
     *  Gets the userId of the FinanceAccount object
     *
     * @return    The user id value
     */
    public int getUserId();


    /**
     *  Gets the accountId of the FinanceAccount object
     *
     * @return    The account id value
     */
    public int getAccountId();


    /**
     *  Gets the accountName of the FinanceAccount object
     *
     * @return    The account name value
     */
    public String getAccountName();


    /**
     *  Gets the accountType of the FinanceAccount object
     *
     * @return    The account type value
     */
    public String getAccountType();


    /**
     *  Gets the lastUpdated of the FinanceAccount object
     *
     * @return    The last updated value
     */
    public java.sql.Timestamp getLastUpdated();
}
