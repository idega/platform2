package com.idega.block.application.data;


public interface ApplicationHome extends com.idega.data.IDOHome
{
 public Application create() throws javax.ejb.CreateException;
 public Application createLegacy();
 public Application findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Application findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Application findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}