package com.idega.block.trade.stockroom.data;


public interface TimeframeHome extends com.idega.data.IDOHome
{
 public Timeframe create() throws javax.ejb.CreateException;
 public Timeframe createLegacy();
 public Timeframe findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Timeframe findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Timeframe findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}