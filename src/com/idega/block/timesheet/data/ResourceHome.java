package com.idega.block.timesheet.data;


public interface ResourceHome extends com.idega.data.IDOHome
{
 public Resource create() throws javax.ejb.CreateException;
 public Resource createLegacy();
 public Resource findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Resource findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Resource findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}