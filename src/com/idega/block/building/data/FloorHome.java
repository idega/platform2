package com.idega.block.building.data;


public interface FloorHome extends com.idega.data.IDOHome
{
 public Floor create() throws javax.ejb.CreateException;
 public Floor findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByBuilding(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByBuilding(com.idega.block.building.data.Building p0)throws javax.ejb.FinderException;

}