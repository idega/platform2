package com.idega.block.staff.data;


public interface StaffMetaDataHome extends com.idega.data.IDOHome
{
 public StaffMetaData create() throws javax.ejb.CreateException;
 public StaffMetaData createLegacy();
 public StaffMetaData findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public StaffMetaData findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public StaffMetaData findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}