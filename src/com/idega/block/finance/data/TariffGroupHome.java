package com.idega.block.finance.data;


public interface TariffGroupHome extends com.idega.data.IDOHome
{
 public TariffGroup create() throws javax.ejb.CreateException;
 public TariffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByCategory(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByCategoryWithouthHandlers(java.lang.Integer p0)throws javax.ejb.FinderException;

}