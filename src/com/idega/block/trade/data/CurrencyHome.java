package com.idega.block.trade.data;


public interface CurrencyHome extends com.idega.data.IDOHome
{
 public Currency create() throws javax.ejb.CreateException;
 public Currency createLegacy();
 public Currency findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Currency findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Currency findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection getCurrenciesByAbbreviation(java.lang.String p0)throws javax.ejb.FinderException;
 public com.idega.block.trade.data.Currency getCurrencyByAbbreviation(java.lang.String p0)throws java.rmi.RemoteException,javax.ejb.FinderException;

}