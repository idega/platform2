package com.idega.block.trade.stockroom.data;

import java.sql.Date;


public interface ProductDayInfoCountHome extends com.idega.data.IDOHome
{
 public ProductDayInfoCount create() throws javax.ejb.CreateException;
 public ProductDayInfoCount findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ProductDayInfoCount findByProductIdAndDate(int productId, Date date) throws javax.ejb.FinderException;

}