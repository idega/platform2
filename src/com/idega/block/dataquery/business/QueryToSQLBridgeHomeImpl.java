package com.idega.block.dataquery.business;


public class QueryToSQLBridgeHomeImpl extends com.idega.business.IBOHomeImpl implements QueryToSQLBridgeHome
{
 protected Class getBeanInterfaceClass(){
  return QueryToSQLBridge.class;
 }


 public QueryToSQLBridge create() throws javax.ejb.CreateException{
  return (QueryToSQLBridge) super.createIBO();
 }



}