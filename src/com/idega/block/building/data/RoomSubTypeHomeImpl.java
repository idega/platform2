package com.idega.block.building.data;


public class RoomSubTypeHomeImpl extends com.idega.data.IDOFactory implements RoomSubTypeHome
{
 protected Class getEntityInterfaceClass(){
  return RoomSubType.class;
 }


 public RoomSubType create() throws javax.ejb.CreateException{
  return (RoomSubType) super.createIDO();
 }


 public RoomSubType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (RoomSubType) super.findByPrimaryKeyIDO(pk);
 }



}