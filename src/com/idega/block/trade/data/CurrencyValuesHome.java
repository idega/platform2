package com.idega.block.trade.data;


public interface CurrencyValuesHome extends com.idega.data.IDOHome
{
 public CurrencyValues create() throws javax.ejb.CreateException;
 public CurrencyValues createLegacy();
 public CurrencyValues findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public CurrencyValues findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public CurrencyValues findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}