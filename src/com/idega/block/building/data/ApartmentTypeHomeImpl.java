package com.idega.block.building.data;


public class ApartmentTypeHomeImpl extends com.idega.data.IDOFactory implements ApartmentTypeHome
{
 protected Class getEntityInterfaceClass(){
  return ApartmentType.class;
 }

 public ApartmentType create() throws javax.ejb.CreateException{
  return (ApartmentType) super.idoCreate();
 }

 public ApartmentType createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ApartmentType findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ApartmentType) super.idoFindByPrimaryKey(id);
 }

 public ApartmentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApartmentType) super.idoFindByPrimaryKey(pk);
 }

 public ApartmentType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}