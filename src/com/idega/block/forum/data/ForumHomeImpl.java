package com.idega.block.forum.data;


public class ForumHomeImpl extends com.idega.data.IDOFactory implements ForumHome
{
 protected Class getEntityInterfaceClass(){
  return Forum.class;
 }

 public Forum create() throws javax.ejb.CreateException{
  return (Forum) super.idoCreate();
 }

 public Forum createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Forum findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Forum) super.idoFindByPrimaryKey(id);
 }

 public Forum findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Forum) super.idoFindByPrimaryKey(pk);
 }

 public Forum findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}