package com.idega.block.trade.stockroom.data;

import com.idega.data.PrimaryKey;


/**
 * @author gimmi
 */
public class SupplyPoolDayPK extends PrimaryKey {

	private static final String COLUMN_SUPPLY_POOL_ID = SupplyPoolDayBMPBean.COLUMN_POOL_ID;
	private static final String COLUMN_DAY_OF_WEEK = SupplyPoolDayBMPBean.COLUMN_DAY_OF_WEEK;
	
	public SupplyPoolDayPK (Object supplyPoolID, Object dayOfWeek) {
		this();
		setSupplyPoolID(supplyPoolID);
		setDayOfWeek(dayOfWeek);
	}
	
	public SupplyPoolDayPK() {
		super();
	}
	
	public void setSupplyPoolID(Object id) {
		setPrimaryKeyValue(COLUMN_SUPPLY_POOL_ID, id);
	}
	
	public Object getSupplyPoolID() {
		return getPrimaryKeyValue(COLUMN_SUPPLY_POOL_ID);
	}
	
	public void setDayOfWeek(Object day) {
		setPrimaryKeyValue(COLUMN_DAY_OF_WEEK, day);
	}
	
	public Object getDayOfWeek() {
		return getPrimaryKeyValue(COLUMN_DAY_OF_WEEK);
	}

}
