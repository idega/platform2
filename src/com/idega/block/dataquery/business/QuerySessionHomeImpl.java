package com.idega.block.dataquery.business;


public class QuerySessionHomeImpl extends com.idega.business.IBOHomeImpl implements QuerySessionHome
{
 protected Class getBeanInterfaceClass(){
  return QuerySession.class;
 }


 public QuerySession create() throws javax.ejb.CreateException{
  return (QuerySession) super.createIBO();
 }



}