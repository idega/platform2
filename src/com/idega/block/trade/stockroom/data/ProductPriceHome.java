package com.idega.block.trade.stockroom.data;


public interface ProductPriceHome extends com.idega.data.IDOHome
{
 public ProductPrice create() throws javax.ejb.CreateException;
 public ProductPrice createLegacy();
 public ProductPrice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ProductPrice findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ProductPrice findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}