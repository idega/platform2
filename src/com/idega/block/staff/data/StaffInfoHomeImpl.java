package com.idega.block.staff.data;


public class StaffInfoHomeImpl extends com.idega.data.IDOFactory implements StaffInfoHome
{
 protected Class getEntityInterfaceClass(){
  return StaffInfo.class;
 }

 public StaffInfo create() throws javax.ejb.CreateException{
  return (StaffInfo) super.idoCreate();
 }

 public StaffInfo createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public StaffInfo findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (StaffInfo) super.idoFindByPrimaryKey(id);
 }

 public StaffInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (StaffInfo) super.idoFindByPrimaryKey(pk);
 }

 public StaffInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}