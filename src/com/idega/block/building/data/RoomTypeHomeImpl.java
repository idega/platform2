package com.idega.block.building.data;


public class RoomTypeHomeImpl extends com.idega.data.IDOFactory implements RoomTypeHome
{
 protected Class getEntityInterfaceClass(){
  return RoomType.class;
 }

 public RoomType create() throws javax.ejb.CreateException{
  return (RoomType) super.idoCreate();
 }

 public RoomType createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public RoomType findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (RoomType) super.idoFindByPrimaryKey(id);
 }

 public RoomType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (RoomType) super.idoFindByPrimaryKey(pk);
 }

 public RoomType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}