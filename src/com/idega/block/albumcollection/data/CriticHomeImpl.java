package com.idega.block.albumcollection.data;


public class CriticHomeImpl extends com.idega.data.IDOFactory implements CriticHome
{
 protected Class getEntityInterfaceClass(){
  return Critic.class;
 }

 public Critic create() throws javax.ejb.CreateException{
  return (Critic) super.idoCreate();
 }

 public Critic createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Critic findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Critic) super.idoFindByPrimaryKey(id);
 }

 public Critic findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Critic) super.idoFindByPrimaryKey(pk);
 }

 public Critic findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}