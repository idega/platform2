package com.idega.block.staff.data;


public class StaffEntityHomeImpl extends com.idega.data.IDOFactory implements StaffEntityHome
{
 protected Class getEntityInterfaceClass(){
  return StaffEntity.class;
 }

 public StaffEntity create() throws javax.ejb.CreateException{
  return (StaffEntity) super.idoCreate();
 }

 public StaffEntity createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public StaffEntity findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (StaffEntity) super.idoFindByPrimaryKey(id);
 }

 public StaffEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (StaffEntity) super.idoFindByPrimaryKey(pk);
 }

 public StaffEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}