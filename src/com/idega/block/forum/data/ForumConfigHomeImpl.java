package com.idega.block.forum.data;


public class ForumConfigHomeImpl extends com.idega.data.IDOFactory implements ForumConfigHome
{
 protected Class getEntityInterfaceClass(){
  return ForumConfig.class;
 }

 public ForumConfig create() throws javax.ejb.CreateException{
  return (ForumConfig) super.idoCreate();
 }

 public ForumConfig createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ForumConfig findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ForumConfig) super.idoFindByPrimaryKey(id);
 }

 public ForumConfig findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ForumConfig) super.idoFindByPrimaryKey(pk);
 }

 public ForumConfig findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}