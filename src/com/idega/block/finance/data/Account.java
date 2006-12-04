package com.idega.block.finance.data;


import java.sql.Timestamp;

import com.idega.user.data.User;

public interface Account extends com.idega.block.category.data.CategoryEntity, FinanceAccount {
	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getUserId
	 */
	public int getUserId();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setUserId
	 */
	public void setUserId(Integer user_id);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setUserId
	 */
	public void setUserId(int user_id);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getLastUpdated
	 */
	public Timestamp getLastUpdated();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setLastUpdated
	 */
	public void setLastUpdated(Timestamp last_updated);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getCashierId
	 */
	public int getCashierId();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setCashierId
	 */
	public void setCashierId(Integer cashier_id);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setCashierId
	 */
	public void setCashierId(int cashier_id);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getBalance
	 */
	public float getBalance();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setBalance
	 */
	public void setBalance(Float balance);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setBalance
	 */
	public void setBalance(float balance);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getCreationDate
	 */
	public Timestamp getCreationDate();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setCreationDate
	 */
	public void setCreationDate(Timestamp creation_date);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getExtraInfo
	 */
	public String getExtraInfo();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setExtraInfo
	 */
	public void setExtraInfo(String extra_info);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#addKredit
	 */
	public void addKredit(float amount);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#addKredit
	 */
	public void addKredit(Float amount);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#addDebet
	 */
	public void addDebet(float amount);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#addDebet
	 */
	public void addDebet(Float amount);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#addAmount
	 */
	public void addAmount(Float amount);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#addAmount
	 */
	public void addAmount(float amount);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setValid
	 */
	public void setValid(boolean valid);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getValid
	 */
	public boolean getValid();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setType
	 */
	public void setType(String type);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getType
	 */
	public String getType();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setTypeFinancial
	 */
	public void setTypeFinancial();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setTypePhone
	 */
	public void setTypePhone();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getAccountTypeId
	 */
	public int getAccountTypeId();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#setAccountTypeId
	 */
	public void setAccountTypeId(int typeId);

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getAccountType
	 */
	public String getAccountType();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getAccountName
	 */
	public String getAccountName();

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#getAccountId
	 */
	public Integer getAccountId();
}