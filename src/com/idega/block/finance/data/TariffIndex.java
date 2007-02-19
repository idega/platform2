package com.idega.block.finance.data;


import java.sql.Timestamp;

import com.idega.block.category.data.CategoryEntity;

public interface TariffIndex extends CategoryEntity {
	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#getIndex
	 */
	public double getIndex();

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#setIndex
	 */
	public void setIndex(double index);

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#setIndex
	 */
	public void setIndex(Double index);

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#getNewValue
	 */
	public double getNewValue();

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#setNewValue
	 */
	public void setNewValue(double index);

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#setNewValue
	 */
	public void setNewValue(Double index);

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#getOldValue
	 */
	public double getOldValue();

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#setOldValue
	 */
	public void setOldValue(double index);

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#setOldValue
	 */
	public void setOldValue(Double index);

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#getType
	 */
	public String getType();

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#setType
	 */
	public void setType(String type);

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#getDate
	 */
	public Timestamp getDate();

	/**
	 * @see com.idega.block.finance.data.TariffIndexBMPBean#setDate
	 */
	public void setDate(Timestamp use_date);
}