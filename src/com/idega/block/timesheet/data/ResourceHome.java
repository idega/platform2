package com.idega.block.timesheet.data;

public interface ResourceHome extends com.idega.data.IDOHome
{
 public Resource create() throws javax.ejb.CreateException;
 public Resource createLegacy();
 public Resource findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Resource findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Resource findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findAllClosed() throws javax.ejb.FinderException;
 public java.util.Collection findAllOpen() throws javax.ejb.FinderException;
 public java.util.Collection findByClosure(boolean closed) throws javax.ejb.FinderException;

}