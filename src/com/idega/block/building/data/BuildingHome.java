package com.idega.block.building.data;


public interface BuildingHome extends com.idega.data.IDOHome
{
 public Building create() throws javax.ejb.CreateException;
 public Building createLegacy();
 public Building findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Building findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Building findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}