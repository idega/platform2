package com.idega.block.dataquery.data;


public interface QueryHome extends com.idega.data.IDOHome
{
 public Query create() throws javax.ejb.CreateException;
 public Query findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}