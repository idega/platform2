package com.idega.block.application.data;


public class ApplicationHomeImpl extends com.idega.data.IDOFactory implements ApplicationHome
{
 protected Class getEntityInterfaceClass(){
  return Application.class;
 }

 public Application create() throws javax.ejb.CreateException{
  return (Application) super.idoCreate();
 }

 public Application createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Application findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Application) super.idoFindByPrimaryKey(id);
 }

 public Application findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Application) super.idoFindByPrimaryKey(pk);
 }

 public Application findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}