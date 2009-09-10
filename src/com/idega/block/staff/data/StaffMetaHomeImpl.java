package com.idega.block.staff.data;


public class StaffMetaHomeImpl extends com.idega.data.IDOFactory implements StaffMetaHome
{
 protected Class getEntityInterfaceClass(){
  return StaffMeta.class;
 }

 public StaffMeta create() throws javax.ejb.CreateException{
  return (StaffMeta) super.idoCreate();
 }

 public StaffMeta createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public StaffMeta findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (StaffMeta) super.idoFindByPrimaryKey(id);
 }

 public StaffMeta findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (StaffMeta) super.idoFindByPrimaryKey(pk);
 }

 public StaffMeta findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}