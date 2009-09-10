package com.idega.block.dataquery.business;


public class QueryServiceHomeImpl extends com.idega.business.IBOHomeImpl implements QueryServiceHome
{
 protected Class getBeanInterfaceClass(){
  return QueryService.class;
 }


 public QueryService create() throws javax.ejb.CreateException{
  return (QueryService) super.createIBO();
 }



}