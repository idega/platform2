package com.idega.block.trade.stockroom.data;


public interface PriceCategoryHome extends com.idega.data.IDOHome
{
 public PriceCategory create() throws javax.ejb.CreateException;
 public PriceCategory createLegacy();
 public PriceCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PriceCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PriceCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}