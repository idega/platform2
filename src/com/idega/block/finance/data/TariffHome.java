package com.idega.block.finance.data;


public interface TariffHome extends com.idega.data.IDOHome
{
 public Tariff create() throws javax.ejb.CreateException;
 public Tariff createLegacy();
 public Tariff findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Tariff findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Tariff findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}