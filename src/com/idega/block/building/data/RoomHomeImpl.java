package com.idega.block.building.data;


public class RoomHomeImpl extends com.idega.data.IDOFactory implements RoomHome
{
 protected Class getEntityInterfaceClass(){
  return Room.class;
 }

 public Room create() throws javax.ejb.CreateException{
  return (Room) super.idoCreate();
 }

 public Room createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Room findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Room) super.idoFindByPrimaryKey(id);
 }

 public Room findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Room) super.idoFindByPrimaryKey(pk);
 }

 public Room findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}