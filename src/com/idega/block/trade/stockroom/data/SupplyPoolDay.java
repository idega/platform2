package com.idega.block.trade.stockroom.data;

import com.idega.data.IDOEntity;


/**
 * @author gimmi
 */
public interface SupplyPoolDay extends IDOEntity {

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolDayBMPBean#getPrimaryKeyClass
	 */
	public Class getPrimaryKeyClass();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolDayBMPBean#setMax
	 */
	public void setMax(int max);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolDayBMPBean#setMin
	 */
	public void setMin(int min);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolDayBMPBean#setEstimated
	 */
	public void setEstimated(int estimated);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolDayBMPBean#getMax
	 */
	public int getMax();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolDayBMPBean#getMin
	 */
	public int getMin();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolDayBMPBean#getEstimated
	 */
	public int getEstimated();
}
