package com.idega.block.finance.data;


public interface TariffKeyHome extends com.idega.data.IDOHome
{
 public TariffKey create() throws javax.ejb.CreateException;
 public TariffKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByCategory(java.lang.Integer p0)throws javax.ejb.FinderException;

}