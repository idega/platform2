package com.idega.block.finance.data;


public interface CashierHome extends com.idega.data.IDOHome
{
 public Cashier create() throws javax.ejb.CreateException;
 public Cashier findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}