package com.idega.block.datareport.data;


public class EntityFieldHandlerMapHomeImpl extends com.idega.data.IDOFactory implements EntityFieldHandlerMapHome
{
 protected Class getEntityInterfaceClass(){
  return EntityFieldHandlerMap.class;
 }


 public EntityFieldHandlerMap create() throws javax.ejb.CreateException{
  return (EntityFieldHandlerMap) super.createIDO();
 }


 public EntityFieldHandlerMap findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (EntityFieldHandlerMap) super.findByPrimaryKeyIDO(pk);
 }



}