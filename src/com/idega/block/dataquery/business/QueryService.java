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
 public com.idega.block.dataquery.data.xml.QueryHelper getQueryHelper(com.idega.block.dataquery.data.UserQuery p0,com.idega.presentation.IWContext p1)throws java.lang.NumberFormatException,java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.dataquery.data.xml.QueryHelper getQueryHelper() throws java.rmi.RemoteException;
 public com.idega.block.dataquery.data.xml.QueryHelper getQueryHelper(int p0,com.idega.presentation.IWContext p1)throws java.lang.NumberFormatException,java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.dataquery.data.xml.QueryHelper getQueryHelperByNameAndPathToQuerySequence(java.lang.String p0,java.lang.String p1,com.idega.presentation.IWContext p2)throws java.lang.NumberFormatException,java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.dataquery.data.QuerySequenceHome getQuerySequenceHome() throws java.rmi.RemoteException;
 public com.idega.block.dataquery.business.QueryToSQLBridge getQueryToSQLBridge()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getRelatedEntities(com.idega.block.dataquery.data.xml.QueryHelper p0,int p1) throws java.rmi.RemoteException;
 public java.util.Collection getRelatedQueryEntityParts(com.idega.block.dataquery.data.xml.QueryEntityPart p0,int p1)throws java.lang.ClassNotFoundException, java.rmi.RemoteException;
 public java.util.Collection getRelatedQueryEntityParts(java.lang.Class p0,int p1) throws java.rmi.RemoteException;
 public java.util.Collection getRelatedQueryEntityParts(java.lang.String p0,int p1)throws java.lang.ClassNotFoundException, java.rmi.RemoteException;
 public java.util.Collection getSourceQueryEntityParts() throws java.rmi.RemoteException;
 public com.idega.user.business.UserBusiness getUserBusiness() throws java.rmi.RemoteException;
 public void removeUserQuery(java.lang.Integer p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public com.idega.block.dataquery.data.UserQuery storeOrUpdateQuery(java.lang.String p0,com.idega.block.dataquery.data.xml.QueryHelper p1,boolean p2,boolean p3,com.idega.idegaweb.IWUserContext p4)throws com.idega.data.IDOStoreException,java.io.IOException,javax.ejb.CreateException,java.sql.SQLException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.dataquery.data.UserQuery storeQuery(java.lang.String p0,com.idega.core.file.data.ICFile p1,boolean p2,com.idega.idegaweb.IWUserContext p3)throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
}
