package com.idega.block.building.data;


public interface ApartmentCategoryHome extends com.idega.data.IDOHome
{
 public ApartmentCategory create() throws javax.ejb.CreateException;
 public ApartmentCategory createLegacy();
 public ApartmentCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ApartmentCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ApartmentCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}