package com.idega.block.staff.data;


public interface StaffLocalizedHome extends com.idega.data.IDOHome
{
 public StaffLocalized create() throws javax.ejb.CreateException;
 public StaffLocalized createLegacy();
 public StaffLocalized findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public StaffLocalized findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public StaffLocalized findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}