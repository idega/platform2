package com.idega.block.building.data;


public class ApartmentCategoryHomeImpl extends com.idega.data.IDOFactory implements ApartmentCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return ApartmentCategory.class;
 }

 public ApartmentCategory create() throws javax.ejb.CreateException{
  return (ApartmentCategory) super.idoCreate();
 }

 public ApartmentCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ApartmentCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ApartmentCategory) super.idoFindByPrimaryKey(id);
 }

 public ApartmentCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApartmentCategory) super.idoFindByPrimaryKey(pk);
 }

 public ApartmentCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}