package com.idega.block.dataquery.data;


public interface QueryLogHome extends com.idega.data.IDOHome
{
 public QueryLog create() throws javax.ejb.CreateException;
 public QueryLog findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByTransactionID(java.lang.String p0)throws javax.ejb.FinderException;

}