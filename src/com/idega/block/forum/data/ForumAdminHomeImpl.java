package com.idega.block.forum.data;


public class ForumAdminHomeImpl extends com.idega.data.IDOFactory implements ForumAdminHome
{
 protected Class getEntityInterfaceClass(){
  return ForumAdmin.class;
 }

 public ForumAdmin create() throws javax.ejb.CreateException{
  return (ForumAdmin) super.idoCreate();
 }

 public ForumAdmin createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ForumAdmin findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ForumAdmin) super.idoFindByPrimaryKey(id);
 }

 public ForumAdmin findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ForumAdmin) super.idoFindByPrimaryKey(pk);
 }

 public ForumAdmin findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}