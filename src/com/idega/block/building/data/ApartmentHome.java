package com.idega.block.building.data;


public interface ApartmentHome extends com.idega.data.IDOHome
{
 public Apartment create() throws javax.ejb.CreateException;
 public Apartment createLegacy();
 public Apartment findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Apartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Apartment findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}