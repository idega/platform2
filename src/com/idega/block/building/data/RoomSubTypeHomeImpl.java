package com.idega.block.building.data;


public class RoomSubTypeHomeImpl extends com.idega.data.IDOFactory implements RoomSubTypeHome
{
 protected Class getEntityInterfaceClass(){
  return RoomSubType.class;
 }

 public RoomSubType create() throws javax.ejb.CreateException{
  return (RoomSubType) super.idoCreate();
 }

 public RoomSubType createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public RoomSubType findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (RoomSubType) super.idoFindByPrimaryKey(id);
 }

 public RoomSubType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (RoomSubType) super.idoFindByPrimaryKey(pk);
 }

 public RoomSubType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}