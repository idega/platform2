package com.idega.block.finance.data;


public interface TariffGroupHome extends com.idega.data.IDOHome
{
 public TariffGroup create() throws javax.ejb.CreateException;
 public TariffGroup createLegacy();
 public TariffGroup findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TariffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TariffGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}