package com.idega.block.forum.data;


public class ForumDataHomeImpl extends com.idega.data.IDOFactory implements ForumDataHome
{
 protected Class getEntityInterfaceClass(){
  return ForumData.class;
 }

 public ForumData create() throws javax.ejb.CreateException{
  return (ForumData) super.idoCreate();
 }

 public ForumData createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ForumData findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ForumData) super.idoFindByPrimaryKey(id);
 }

 public ForumData findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ForumData) super.idoFindByPrimaryKey(pk);
 }

 public ForumData findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }

public java.util.Collection findAllThreads(com.idega.block.category.data.ICCategory p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ForumDataBMPBean)entity).ejbFindAllThreads(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findThreadsInCategories(java.util.Collection p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ForumDataBMPBean)entity).ejbFindThreadsInCategories(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findNewestThread(com.idega.block.category.data.ICCategory p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ForumDataBMPBean)entity).ejbFindNewestThread(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllThreads(com.idega.block.category.data.ICCategory p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ForumDataBMPBean)entity).ejbFindAllThreads(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public int getNumberOfThreads(com.idega.block.category.data.ICCategory p0)throws javax.ejb.EJBException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ForumDataBMPBean)entity).ejbHomeGetNumberOfThreads(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}