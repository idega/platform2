package com.idega.block.projectmanager.data;


public interface ProjectGroupHome extends com.idega.data.IDOHome
{
 public ProjectGroup create() throws javax.ejb.CreateException;
 public ProjectGroup createLegacy();
 public ProjectGroup findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ProjectGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ProjectGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}