package com.idega.block.boxoffice.data;


public class BoxEntityHomeImpl extends com.idega.data.IDOFactory implements BoxEntityHome
{
 protected Class getEntityInterfaceClass(){
  return BoxEntity.class;
 }

 public BoxEntity create() throws javax.ejb.CreateException{
  return (BoxEntity) super.idoCreate();
 }

 public BoxEntity createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public BoxEntity findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (BoxEntity) super.idoFindByPrimaryKey(id);
 }

 public BoxEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BoxEntity) super.idoFindByPrimaryKey(pk);
 }

 public BoxEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}