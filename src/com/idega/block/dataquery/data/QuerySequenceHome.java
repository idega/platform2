package com.idega.block.dataquery.data;


public interface QuerySequenceHome extends com.idega.data.IDOHome
{
 public QuerySequence create() throws javax.ejb.CreateException;
 public QuerySequence findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByRealQuery(com.idega.block.dataquery.data.UserQuery p0)throws javax.ejb.FinderException;
 public QuerySequence findByName(java.lang.String p0)throws javax.ejb.FinderException;

}