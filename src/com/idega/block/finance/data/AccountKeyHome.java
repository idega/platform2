package com.idega.block.finance.data;


public interface AccountKeyHome extends com.idega.data.IDOHome
{
 public AccountKey create() throws javax.ejb.CreateException;
 public AccountKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByCategory(java.lang.Integer p0)throws javax.ejb.FinderException;

}