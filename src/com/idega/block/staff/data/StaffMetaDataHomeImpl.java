package com.idega.block.staff.data;


public class StaffMetaDataHomeImpl extends com.idega.data.IDOFactory implements StaffMetaDataHome
{
 protected Class getEntityInterfaceClass(){
  return StaffMetaData.class;
 }

 public StaffMetaData create() throws javax.ejb.CreateException{
  return (StaffMetaData) super.idoCreate();
 }

 public StaffMetaData createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public StaffMetaData findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (StaffMetaData) super.idoFindByPrimaryKey(id);
 }

 public StaffMetaData findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (StaffMetaData) super.idoFindByPrimaryKey(pk);
 }

 public StaffMetaData findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}