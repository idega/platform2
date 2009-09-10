package com.idega.block.boxoffice.data;


public class BoxCategoryHomeImpl extends com.idega.data.IDOFactory implements BoxCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return BoxCategory.class;
 }

 public BoxCategory create() throws javax.ejb.CreateException{
  return (BoxCategory) super.idoCreate();
 }

 public BoxCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public BoxCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (BoxCategory) super.idoFindByPrimaryKey(id);
 }

 public BoxCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BoxCategory) super.idoFindByPrimaryKey(pk);
 }

 public BoxCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}