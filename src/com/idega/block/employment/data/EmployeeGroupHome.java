package com.idega.block.employment.data;


public interface EmployeeGroupHome extends com.idega.data.IDOHome
{
 public EmployeeGroup create() throws javax.ejb.CreateException;
 public EmployeeGroup createLegacy();
 public EmployeeGroup findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public EmployeeGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public EmployeeGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}