package com.idega.block.documents.data;


public class DocVersionHomeImpl extends com.idega.data.IDOFactory implements DocVersionHome
{
 protected Class getEntityInterfaceClass(){
  return DocVersion.class;
 }

 public DocVersion create() throws javax.ejb.CreateException{
  return (DocVersion) super.idoCreate();
 }

 public DocVersion createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public DocVersion findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (DocVersion) super.idoFindByPrimaryKey(id);
 }

 public DocVersion findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (DocVersion) super.idoFindByPrimaryKey(pk);
 }

 public DocVersion findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}