package com.idega.block.dataquery.data;


public class QueryResultEntityHomeImpl extends com.idega.data.IDOFactory implements QueryResultEntityHome
{
 protected Class getEntityInterfaceClass(){
  return QueryResultEntity.class;
 }


 public QueryResultEntity create() throws javax.ejb.CreateException{
  return (QueryResultEntity) super.createIDO();
 }


 public QueryResultEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (QueryResultEntity) super.findByPrimaryKeyIDO(pk);
 }



}