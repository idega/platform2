package com.idega.block.trade.stockroom.data;


public interface VariantValueHome extends com.idega.data.IDOHome
{
 public VariantValue create() throws javax.ejb.CreateException;
 public VariantValue createLegacy();
 public VariantValue findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public VariantValue findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public VariantValue findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}