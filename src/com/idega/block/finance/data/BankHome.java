package com.idega.block.finance.data;


public interface BankHome extends com.idega.data.IDOHome
{
 public Bank create() throws javax.ejb.CreateException;
 public Bank findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}