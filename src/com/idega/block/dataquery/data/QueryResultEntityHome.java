package com.idega.block.dataquery.data;


public interface QueryResultEntityHome extends com.idega.data.IDOHome
{
 public QueryResultEntity create() throws javax.ejb.CreateException;
 public QueryResultEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}