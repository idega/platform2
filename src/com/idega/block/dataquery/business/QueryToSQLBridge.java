package com.idega.block.dataquery.business;


public interface QueryToSQLBridge extends com.idega.business.IBOService
{
 public com.idega.block.dataquery.data.QuerySQL createQuerySQL(com.idega.block.dataquery.business.QueryHelper p0) throws java.rmi.RemoteException;
 public com.idega.block.dataquery.data.QueryResult executeStatement(java.lang.String p0,java.util.List p1)throws java.sql.SQLException, java.rmi.RemoteException;
}
