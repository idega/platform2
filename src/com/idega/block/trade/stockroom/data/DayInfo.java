package com.idega.block.trade.stockroom.data;

import java.sql.Date;


public interface DayInfo extends com.idega.data.IDOEntity {
	public void setDate(Date date);
	public void setCount(int count);
	public void setSupplyPoolId(int id);
	public Date getDate();
	public int getCount();
	public int getSupplyPoolId();
}
