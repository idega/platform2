package com.idega.block.building.data;


public class FloorHomeImpl extends com.idega.data.IDOFactory implements FloorHome
{
 protected Class getEntityInterfaceClass(){
  return Floor.class;
 }

 public Floor create() throws javax.ejb.CreateException{
  return (Floor) super.idoCreate();
 }

 public Floor createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Floor findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Floor) super.idoFindByPrimaryKey(id);
 }

 public Floor findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Floor) super.idoFindByPrimaryKey(pk);
 }

 public Floor findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}