package com.idega.block.finance.data;


public interface TariffKeyHome extends com.idega.data.IDOHome
{
 public TariffKey create() throws javax.ejb.CreateException;
 public TariffKey createLegacy();
 public TariffKey findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TariffKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TariffKey findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}