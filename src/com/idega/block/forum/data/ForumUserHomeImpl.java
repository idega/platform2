package com.idega.block.forum.data;


public class ForumUserHomeImpl extends com.idega.data.IDOFactory implements ForumUserHome
{
 protected Class getEntityInterfaceClass(){
  return ForumUser.class;
 }

 public ForumUser create() throws javax.ejb.CreateException{
  return (ForumUser) super.idoCreate();
 }

 public ForumUser createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ForumUser findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ForumUser) super.idoFindByPrimaryKey(id);
 }

 public ForumUser findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ForumUser) super.idoFindByPrimaryKey(pk);
 }

 public ForumUser findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}