package com.idega.block.building.data;


public interface ComplexHome extends com.idega.data.IDOHome
{
 public Complex create() throws javax.ejb.CreateException;
 public Complex createLegacy();
 public Complex findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Complex findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Complex findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}