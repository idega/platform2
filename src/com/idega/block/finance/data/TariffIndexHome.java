package com.idega.block.finance.data;


public interface TariffIndexHome extends com.idega.data.IDOHome
{
 public TariffIndex create() throws javax.ejb.CreateException;
 public TariffIndex findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TariffIndex findLastByType(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findLastTypeGrouped()throws javax.ejb.FinderException;

}