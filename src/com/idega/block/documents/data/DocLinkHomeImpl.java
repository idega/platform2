package com.idega.block.documents.data;


public class DocLinkHomeImpl extends com.idega.data.IDOFactory implements DocLinkHome
{
 protected Class getEntityInterfaceClass(){
  return DocLink.class;
 }

 public DocLink create() throws javax.ejb.CreateException{
  return (DocLink) super.idoCreate();
 }

 public DocLink createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public DocLink findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (DocLink) super.idoFindByPrimaryKey(id);
 }

 public DocLink findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (DocLink) super.idoFindByPrimaryKey(pk);
 }

 public DocLink findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}