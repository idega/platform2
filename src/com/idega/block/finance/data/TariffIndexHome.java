package com.idega.block.finance.data;


public interface TariffIndexHome extends com.idega.data.IDOHome
{
 public TariffIndex create() throws javax.ejb.CreateException;
 public TariffIndex createLegacy();
 public TariffIndex findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TariffIndex findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TariffIndex findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}