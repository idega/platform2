package com.idega.block.albumcollection.data;


public class CategoryHomeImpl extends com.idega.data.IDOFactory implements CategoryHome
{
 protected Class getEntityInterfaceClass(){
  return Category.class;
 }

 public Category create() throws javax.ejb.CreateException{
  return (Category) super.idoCreate();
 }

 public Category createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Category findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Category) super.idoFindByPrimaryKey(id);
 }

 public Category findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Category) super.idoFindByPrimaryKey(pk);
 }

 public Category findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}