package com.idega.block.trade.stockroom.data;


public interface ProductCategoryHome extends com.idega.data.IDOHome
{
 public ProductCategory create() throws javax.ejb.CreateException;
 public ProductCategory createLegacy();
 public ProductCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ProductCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ProductCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}