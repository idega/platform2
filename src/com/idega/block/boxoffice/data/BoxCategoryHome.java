package com.idega.block.boxoffice.data;


public interface BoxCategoryHome extends com.idega.data.IDOHome
{
 public BoxCategory create() throws javax.ejb.CreateException;
 public BoxCategory createLegacy();
 public BoxCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public BoxCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public BoxCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}