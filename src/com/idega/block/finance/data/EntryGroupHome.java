package com.idega.block.finance.data;


public interface EntryGroupHome extends com.idega.data.IDOHome
{
 public EntryGroup create() throws javax.ejb.CreateException;
 public EntryGroup createLegacy();
 public EntryGroup findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public EntryGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public EntryGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}