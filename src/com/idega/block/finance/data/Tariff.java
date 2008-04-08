package com.idega.block.finance.data;


import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface Tariff extends IDOEntity {
	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getTariffAttribute
	 */
	public String getTariffAttribute();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setTariffAttribute
	 */
	public void setTariffAttribute(String attribute);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getPrice
	 */
	public float getPrice();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setPrice
	 */
	public void setPrice(float price);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setPrice
	 */
	public void setPrice(Float price);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getAccountKeyId
	 */
	public int getAccountKeyId();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setAccountKeyId
	 */
	public void setAccountKeyId(Integer account_key_id);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setAccountKeyId
	 */
	public void setAccountKeyId(int account_key_id);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getTariffGroupId
	 */
	public int getTariffGroupId();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setTariffGroupId
	 */
	public void setTariffGroupId(Integer group_id);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setTariffGroupId
	 */
	public void setTariffGroupId(int group_id);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getUseFromDate
	 */
	public Timestamp getUseFromDate();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setUseFromDate
	 */
	public void setUseFromDate(Timestamp use_date);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getUseToDate
	 */
	public Timestamp getUseToDate();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setUseToDate
	 */
	public void setUseToDate(Timestamp use_date);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getIndexUpdated
	 */
	public Timestamp getIndexUpdated();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setIndexUpdated
	 */
	public void setIndexUpdated(Timestamp use_date);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setUseIndex
	 */
	public void setUseIndex(boolean useindex);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getUseIndex
	 */
	public boolean getUseIndex();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setInUse
	 */
	public void setInUse(boolean useindex);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getInUse
	 */
	public boolean getInUse();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getIndexType
	 */
	public String getIndexType();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setIndexType
	 */
	public void setIndexType(String type);

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#getUseDiscount
	 */
	public boolean getUseDiscount();

	/**
	 * @see com.idega.block.finance.data.TariffBMPBean#setUseDiscount
	 */
	public void setUseDiscount(boolean useDiscount);
}