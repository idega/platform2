package com.idega.block.trade.stockroom.data;


public interface ProductHome extends com.idega.data.IDOHome
{
 public Product create() throws javax.ejb.CreateException;
 public Product createLegacy();
 public Product findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Product findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Product findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}