package com.idega.block.finance.data;


public interface CashierHome extends com.idega.data.IDOHome
{
 public Cashier create() throws javax.ejb.CreateException;
 public Cashier createLegacy();
 public Cashier findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Cashier findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Cashier findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}