package com.idega.block.building.data;


public interface ApartmentTypeHome extends com.idega.data.IDOHome
{
 public ApartmentType create() throws javax.ejb.CreateException;
 public ApartmentType createLegacy();
 public ApartmentType findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ApartmentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ApartmentType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}