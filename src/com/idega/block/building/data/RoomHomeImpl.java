package com.idega.block.building.data;


public class RoomHomeImpl extends com.idega.data.IDOFactory implements RoomHome
{
 protected Class getEntityInterfaceClass(){
  return Room.class;
 }


 public Room create() throws javax.ejb.CreateException{
  return (Room) super.createIDO();
 }


 public Room findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Room) super.findByPrimaryKeyIDO(pk);
 }



}