package com.idega.block.finance.data;


public interface AccountTypeHome extends com.idega.data.IDOHome
{
 public AccountType create() throws javax.ejb.CreateException;
 public AccountType createLegacy();
 public AccountType findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public AccountType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AccountType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}