package com.idega.block.forum.data;


public class ForumThreadHomeImpl extends com.idega.data.IDOFactory implements ForumThreadHome
{
 protected Class getEntityInterfaceClass(){
  return ForumThread.class;
 }

 public ForumThread create() throws javax.ejb.CreateException{
  return (ForumThread) super.idoCreate();
 }

 public ForumThread createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ForumThread findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ForumThread) super.idoFindByPrimaryKey(id);
 }

 public ForumThread findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ForumThread) super.idoFindByPrimaryKey(pk);
 }

 public ForumThread findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}