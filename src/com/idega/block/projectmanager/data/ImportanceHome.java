package com.idega.block.projectmanager.data;


public interface ImportanceHome extends com.idega.data.IDOHome
{
 public Importance create() throws javax.ejb.CreateException;
 public Importance createLegacy();
 public Importance findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Importance findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Importance findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}