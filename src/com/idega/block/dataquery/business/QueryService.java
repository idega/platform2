package com.idega.block.dataquery.business;


public interface QueryService extends com.idega.business.IBOService
{
 public com.idega.block.dataquery.data.xml.QueryFieldPart createQueryFieldPart(com.idega.idegaweb.IWResourceBundle p0,java.lang.String p1,java.lang.String p2,com.idega.data.EntityAttribute p3) throws java.rmi.RemoteException;
 public com.idega.block.dataquery.data.QueryResult generateQueryResult(java.lang.Integer p0,com.idega.presentation.IWContext p1)throws com.idega.block.dataquery.business.QueryGenerationException, java.rmi.RemoteException;
 public java.util.Collection getEntityAttributes(java.lang.Class p0) throws java.rmi.RemoteException;
 public java.util.Collection getEntityAttributes(com.idega.block.dataquery.data.xml.QueryEntityPart p0) throws java.rmi.RemoteException;
 public com.idega.block.dataquery.data.xml.QueryEntityPart getEntityTree(com.idega.block.dataquery.data.xml.QueryHelper p0,int p1) throws java.rmi.RemoteException;
 public java.util.Collection getInputHandlerNames() throws java.rmi.RemoteException;
 public java.util.Collection getListOfFieldParts(com.idega.idegaweb.IWResourceBundle p0,com.idega.block.dataquery.data.xml.QueryEntityPart p1,boolean p2) throws java.rmi.RemoteException;
 public java.util.Collection getManyToManyEntityDefinitions(com.idega.block.dataquery.data.xml.QueryEntityPart p0) throws java.rmi.RemoteException;
 public com.idega.block.dataquery.data.xml.QueryHelper getQueryHelper(com.idega.util.xml.XMLFile p0) throws java.rmi.RemoteException;
 public com.idega.block.dataquery.data.xml.QueryHelper getQueryHelper() throws java.rmi.RemoteException;
 public com.idega.block.dataquery.data.xml.QueryHelper getQueryHelper(int p0) throws java.rmi.RemoteException;
 public com.idega.block.dataquery.business.QueryToSQLBridge getQueryToSQLBridge()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getRelatedEntities(com.idega.block.dataquery.data.xml.QueryHelper p0,int p1) throws java.rmi.RemoteException;
 public java.util.Collection getRelatedQueryEntityParts(com.idega.block.dataquery.data.xml.QueryEntityPart p0,int p1)throws java.lang.ClassNotFoundException, java.rmi.RemoteException;
 public java.util.Collection getRelatedQueryEntityParts(java.lang.Class p0,int p1) throws java.rmi.RemoteException;
 public java.util.Collection getRelatedQueryEntityParts(java.lang.String p0,int p1)throws java.lang.ClassNotFoundException, java.rmi.RemoteException;
 public java.util.Collection getSourceQueryEntityParts() throws java.rmi.RemoteException;
}
