package com.idega.block.building.data;


public class BuildingHomeImpl extends com.idega.data.IDOFactory implements BuildingHome
{
 protected Class getEntityInterfaceClass(){
  return Building.class;
 }

 public Building create() throws javax.ejb.CreateException{
  return (Building) super.idoCreate();
 }

 public Building createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Building findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Building) super.idoFindByPrimaryKey(id);
 }

 public Building findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Building) super.idoFindByPrimaryKey(pk);
 }

 public Building findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}