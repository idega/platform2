package com.idega.block.finance.data;


public interface AccountKeyHome extends com.idega.data.IDOHome
{
 public AccountKey create() throws javax.ejb.CreateException;
 public AccountKey createLegacy();
 public AccountKey findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public AccountKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AccountKey findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}