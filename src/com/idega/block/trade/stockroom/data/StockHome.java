package com.idega.block.trade.stockroom.data;


public interface StockHome extends com.idega.data.IDOHome
{
 public Stock create() throws javax.ejb.CreateException;
 public Stock createLegacy();
 public Stock findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Stock findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Stock findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}