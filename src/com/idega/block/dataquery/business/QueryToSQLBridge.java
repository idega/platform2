package com.idega.block.dataquery.business;


public interface QueryToSQLBridge extends com.idega.business.IBOService
{
 public com.idega.block.dataquery.data.sql.QuerySQL createQuerySQL(com.idega.block.dataquery.data.xml.QueryHelper p0,com.idega.presentation.IWContext p1)throws com.idega.block.dataquery.business.QueryGenerationException, java.rmi.RemoteException;
 public com.idega.block.dataquery.data.QueryResult executeQueries(com.idega.block.dataquery.data.sql.QuerySQL p0) throws java.rmi.RemoteException;
 public com.idega.block.dataquery.data.QueryResult executeQueries(com.idega.block.dataquery.data.sql.QuerySQL p0,java.util.List p1) throws java.rmi.RemoteException;
}
