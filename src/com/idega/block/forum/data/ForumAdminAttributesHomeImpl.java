package com.idega.block.forum.data;


public class ForumAdminAttributesHomeImpl extends com.idega.data.IDOFactory implements ForumAdminAttributesHome
{
 protected Class getEntityInterfaceClass(){
  return ForumAdminAttributes.class;
 }

 public ForumAdminAttributes create() throws javax.ejb.CreateException{
  return (ForumAdminAttributes) super.idoCreate();
 }

 public ForumAdminAttributes createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ForumAdminAttributes findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ForumAdminAttributes) super.idoFindByPrimaryKey(id);
 }

 public ForumAdminAttributes findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ForumAdminAttributes) super.idoFindByPrimaryKey(pk);
 }

 public ForumAdminAttributes findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}