package com.idega.block.mailinglist.data;


public interface AccountHome extends com.idega.data.IDOHome
{
 public Account create() throws javax.ejb.CreateException;
 public Account createLegacy();
 public Account findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Account findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Account findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}