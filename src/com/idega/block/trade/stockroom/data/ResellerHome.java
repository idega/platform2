package com.idega.block.trade.stockroom.data;


public interface ResellerHome extends com.idega.data.IDOHome
{
 public Reseller create() throws javax.ejb.CreateException;
 public Reseller createLegacy();
 public Reseller findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Reseller findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Reseller findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}