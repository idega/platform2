package com.idega.block.trade.data;


public interface CurrencyHome extends com.idega.data.IDOHome
{
 public Currency create() throws javax.ejb.CreateException;
 public Currency createLegacy();
 public Currency findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Currency findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Currency findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}