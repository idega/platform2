package com.idega.block.staff.data;


public class StaffLocalizedHomeImpl extends com.idega.data.IDOFactory implements StaffLocalizedHome
{
 protected Class getEntityInterfaceClass(){
  return StaffLocalized.class;
 }

 public StaffLocalized create() throws javax.ejb.CreateException{
  return (StaffLocalized) super.idoCreate();
 }

 public StaffLocalized createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public StaffLocalized findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (StaffLocalized) super.idoFindByPrimaryKey(id);
 }

 public StaffLocalized findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (StaffLocalized) super.idoFindByPrimaryKey(pk);
 }

 public StaffLocalized findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}