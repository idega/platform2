package com.idega.block.forum.data;


public interface ForumDataHome extends com.idega.data.IDOHome
{
 public ForumData create() throws javax.ejb.CreateException;
 public ForumData createLegacy();
 public ForumData findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ForumData findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ForumData findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findAllThreads(com.idega.block.category.data.ICCategory p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findThreadsInCategories(java.util.Collection p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findNewestThread(com.idega.block.category.data.ICCategory p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllThreads(com.idega.block.category.data.ICCategory p0)throws javax.ejb.FinderException;
 public int getNumberOfThreads(com.idega.block.category.data.ICCategory p0)throws javax.ejb.EJBException;

}