package com.idega.block.albumcollection.data;


public class AuthorHomeImpl extends com.idega.data.IDOFactory implements AuthorHome
{
 protected Class getEntityInterfaceClass(){
  return Author.class;
 }

 public Author create() throws javax.ejb.CreateException{
  return (Author) super.idoCreate();
 }

 public Author createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Author findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Author) super.idoFindByPrimaryKey(id);
 }

 public Author findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Author) super.idoFindByPrimaryKey(pk);
 }

 public Author findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}