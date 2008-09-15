package com.idega.block.finance.data;


import com.idega.block.category.data.CategoryEntity;
import com.idega.data.IDOEntity;

public interface AccountKey extends IDOEntity, CategoryEntity {
	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#getTariffKeyId
	 */
	public int getTariffKeyId();

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#getTariffKey
	 */
	public TariffKey getTariffKey();

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#setTariffKeyId
	 */
	public void setTariffKeyId(int id);

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#setInfo
	 */
	public void setInfo(String extra_info);

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#getOrdinal
	 */
	public Integer getOrdinal();

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#setOrdinal
	 */
	public void setOrdinal(Integer ordinal);

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#setOrdinal
	 */
	public void setOrdinal(int ordinal);

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#getDivision
	 */
	public String getDivision();

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#setDivision
	 */
	public void setDivision(String division);

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#getXMLParentKey
	 */
	public AccountKey getXMLParentKey();

	/**
	 * @see com.idega.block.finance.data.AccountKeyBMPBean#setXMLParentKey
	 */
	public void setXMLParentKey(AccountKey key);
}