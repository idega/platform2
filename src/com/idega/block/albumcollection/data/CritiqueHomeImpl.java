package com.idega.block.albumcollection.data;


public class CritiqueHomeImpl extends com.idega.data.IDOFactory implements CritiqueHome
{
 protected Class getEntityInterfaceClass(){
  return Critique.class;
 }

 public Critique create() throws javax.ejb.CreateException{
  return (Critique) super.idoCreate();
 }

 public Critique createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Critique findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Critique) super.idoFindByPrimaryKey(id);
 }

 public Critique findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Critique) super.idoFindByPrimaryKey(pk);
 }

 public Critique findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}