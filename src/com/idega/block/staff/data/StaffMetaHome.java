package com.idega.block.staff.data;


public interface StaffMetaHome extends com.idega.data.IDOHome
{
 public StaffMeta create() throws javax.ejb.CreateException;
 public StaffMeta createLegacy();
 public StaffMeta findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public StaffMeta findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public StaffMeta findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}