package com.idega.block.finance.data;


public interface AccountInfoHome extends com.idega.data.IDOHome
{
 public AccountInfo create() throws javax.ejb.CreateException;
 public AccountInfo createLegacy();
 public AccountInfo findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public AccountInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AccountInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}