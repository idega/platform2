package com.idega.block.application.data;


public class ApplicationSubjectInfoHomeImpl extends com.idega.data.IDOFactory implements ApplicationSubjectInfoHome
{
 protected Class getEntityInterfaceClass(){
  return ApplicationSubjectInfo.class;
 }

 public ApplicationSubjectInfo create() throws javax.ejb.CreateException{
  return (ApplicationSubjectInfo) super.idoCreate();
 }

 public ApplicationSubjectInfo createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ApplicationSubjectInfo findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ApplicationSubjectInfo) super.idoFindByPrimaryKey(id);
 }

 public ApplicationSubjectInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApplicationSubjectInfo) super.idoFindByPrimaryKey(pk);
 }

 public ApplicationSubjectInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}