package com.idega.block.building.data;


public class ApartmentHomeImpl extends com.idega.data.IDOFactory implements ApartmentHome
{
 protected Class getEntityInterfaceClass(){
  return Apartment.class;
 }

 public Apartment create() throws javax.ejb.CreateException{
  return (Apartment) super.idoCreate();
 }

 public Apartment createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Apartment findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Apartment) super.idoFindByPrimaryKey(id);
 }

 public Apartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Apartment) super.idoFindByPrimaryKey(pk);
 }

 public Apartment findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}