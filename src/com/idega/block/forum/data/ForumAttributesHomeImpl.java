package com.idega.block.forum.data;


public class ForumAttributesHomeImpl extends com.idega.data.IDOFactory implements ForumAttributesHome
{
 protected Class getEntityInterfaceClass(){
  return ForumAttributes.class;
 }

 public ForumAttributes create() throws javax.ejb.CreateException{
  return (ForumAttributes) super.idoCreate();
 }

 public ForumAttributes createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ForumAttributes findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ForumAttributes) super.idoFindByPrimaryKey(id);
 }

 public ForumAttributes findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ForumAttributes) super.idoFindByPrimaryKey(pk);
 }

 public ForumAttributes findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}