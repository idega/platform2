package com.idega.block.projectmanager.data;


public interface ProjectHome extends com.idega.data.IDOHome
{
 public Project create() throws javax.ejb.CreateException;
 public Project createLegacy();
 public Project findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Project findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Project findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}