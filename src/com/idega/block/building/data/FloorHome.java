package com.idega.block.building.data;


public interface FloorHome extends com.idega.data.IDOHome
{
 public Floor create() throws javax.ejb.CreateException;
 public Floor createLegacy();
 public Floor findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Floor findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Floor findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}