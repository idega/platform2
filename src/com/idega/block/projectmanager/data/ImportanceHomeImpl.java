package com.idega.block.projectmanager.data;


public class ImportanceHomeImpl extends com.idega.data.IDOFactory implements ImportanceHome
{
 protected Class getEntityInterfaceClass(){
  return Importance.class;
 }


 public Importance create() throws javax.ejb.CreateException{
  return (Importance) super.createIDO();
 }


 public Importance findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Importance) super.findByPrimaryKeyIDO(pk);
 }



}