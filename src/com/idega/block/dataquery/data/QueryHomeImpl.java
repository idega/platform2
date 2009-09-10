package com.idega.block.dataquery.data;


public class QueryHomeImpl extends com.idega.data.IDOFactory implements QueryHome
{
 protected Class getEntityInterfaceClass(){
  return Query.class;
 }


 public Query create() throws javax.ejb.CreateException{
  return (Query) super.createIDO();
 }


 public Query findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Query) super.findByPrimaryKeyIDO(pk);
 }



}