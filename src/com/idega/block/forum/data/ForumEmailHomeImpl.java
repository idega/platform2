package com.idega.block.forum.data;


public class ForumEmailHomeImpl extends com.idega.data.IDOFactory implements ForumEmailHome
{
 protected Class getEntityInterfaceClass(){
  return ForumEmail.class;
 }

 public ForumEmail create() throws javax.ejb.CreateException{
  return (ForumEmail) super.idoCreate();
 }

 public ForumEmail createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ForumEmail findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ForumEmail) super.idoFindByPrimaryKey(id);
 }

 public ForumEmail findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ForumEmail) super.idoFindByPrimaryKey(pk);
 }

 public ForumEmail findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}