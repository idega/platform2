package com.idega.block.boxoffice.data;


public interface BoxEntityHome extends com.idega.data.IDOHome
{
 public BoxEntity create() throws javax.ejb.CreateException;
 public BoxEntity createLegacy();
 public BoxEntity findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public BoxEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public BoxEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}