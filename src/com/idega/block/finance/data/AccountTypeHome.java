package com.idega.block.finance.data;


public interface AccountTypeHome extends com.idega.data.IDOHome
{
 public AccountType create() throws javax.ejb.CreateException;
 public AccountType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}