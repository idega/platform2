package com.idega.block.boxoffice.data;


public interface BoxLinkHome extends com.idega.data.IDOHome
{
 public BoxLink create() throws javax.ejb.CreateException;
 public BoxLink createLegacy();
 public BoxLink findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public BoxLink findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public BoxLink findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}