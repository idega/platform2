package com.idega.block.staff.data;


public interface StaffEntityHome extends com.idega.data.IDOHome
{
 public StaffEntity create() throws javax.ejb.CreateException;
 public StaffEntity createLegacy();
 public StaffEntity findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public StaffEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public StaffEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}