package com.idega.block.building.data;


public class RoomTypeHomeImpl extends com.idega.data.IDOFactory implements RoomTypeHome
{
 protected Class getEntityInterfaceClass(){
  return RoomType.class;
 }


 public RoomType create() throws javax.ejb.CreateException{
  return (RoomType) super.createIDO();
 }


 public RoomType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (RoomType) super.findByPrimaryKeyIDO(pk);
 }



}