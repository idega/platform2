package com.idega.block.timesheet.data;


public interface ProjectExtraHome extends com.idega.data.IDOHome
{
 public ProjectExtra create() throws javax.ejb.CreateException;
 public ProjectExtra createLegacy();
 public ProjectExtra findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ProjectExtra findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ProjectExtra findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}