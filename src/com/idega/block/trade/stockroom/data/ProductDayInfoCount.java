package com.idega.block.trade.stockroom.data;

import java.sql.Date;


public interface ProductDayInfoCount extends com.idega.data.IDOEntity {
	public int getProductId();
	public Date getDate();
	public int getCount();
	public void setProductId(int productId);
	public void setDate(Date date);
	public void setCount(int count);
}
