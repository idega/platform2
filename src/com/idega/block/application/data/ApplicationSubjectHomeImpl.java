package com.idega.block.application.data;


public class ApplicationSubjectHomeImpl extends com.idega.data.IDOFactory implements ApplicationSubjectHome
{
 protected Class getEntityInterfaceClass(){
  return ApplicationSubject.class;
 }

 public ApplicationSubject create() throws javax.ejb.CreateException{
  return (ApplicationSubject) super.idoCreate();
 }

 public ApplicationSubject createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ApplicationSubject findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ApplicationSubject) super.idoFindByPrimaryKey(id);
 }

 public ApplicationSubject findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApplicationSubject) super.idoFindByPrimaryKey(pk);
 }

 public ApplicationSubject findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}