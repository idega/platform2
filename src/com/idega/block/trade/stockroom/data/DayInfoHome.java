package com.idega.block.trade.stockroom.data;

import java.sql.Date;


public interface DayInfoHome extends com.idega.data.IDOHome
{
 public DayInfo create() throws javax.ejb.CreateException;
 public DayInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public DayInfo findBySupplyPoolIdAndDate(int supplyPoolId, Date date) throws javax.ejb.FinderException;

}