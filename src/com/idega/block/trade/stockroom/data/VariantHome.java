package com.idega.block.trade.stockroom.data;


public interface VariantHome extends com.idega.data.IDOHome
{
 public Variant create() throws javax.ejb.CreateException;
 public Variant createLegacy();
 public Variant findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Variant findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Variant findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}