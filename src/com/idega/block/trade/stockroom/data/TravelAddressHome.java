package com.idega.block.trade.stockroom.data;


public interface TravelAddressHome extends com.idega.data.IDOHome
{
 public TravelAddress create() throws javax.ejb.CreateException;
 public TravelAddress createLegacy();
 public TravelAddress findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TravelAddress findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TravelAddress findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}