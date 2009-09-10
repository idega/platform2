package com.idega.block.staff.data;


public interface StaffInfoHome extends com.idega.data.IDOHome
{
 public StaffInfo create() throws javax.ejb.CreateException;
 public StaffInfo createLegacy();
 public StaffInfo findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public StaffInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public StaffInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}