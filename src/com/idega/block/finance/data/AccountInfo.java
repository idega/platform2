/**
 * 
 */
package com.idega.block.finance.data;

import java.sql.Timestamp;


import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * @author bluebottle
 *
 */
public interface AccountInfo extends IDOEntity, FinanceAccount {
	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#getAccountId
	 */
	public Integer getAccountId();

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#getCategoryId
	 */
	public int getCategoryId();

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#getUserId
	 */
	public int getUserId();

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#getCashierId
	 */
	public int getCashierId();

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#getAccountType
	 */
	public String getAccountType();

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#getLastUpdated
	 */
	public Timestamp getLastUpdated();

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#getBalance
	 */
	public float getBalance();

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#getAccountName
	 */
	public String getAccountName();

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#setCategoryId
	 */
	public void setCategoryId(int p0);

}
