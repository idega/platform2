package com.idega.block.trade.stockroom.data;


public interface SuppliesHome extends com.idega.data.IDOHome
{
 public Supplies create() throws javax.ejb.CreateException;
 public Supplies createLegacy();
 public Supplies findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Supplies findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Supplies findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}