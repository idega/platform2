package com.idega.block.importer.data;


public class ImportHandlerHomeImpl extends com.idega.data.IDOFactory implements ImportHandlerHome
{
 protected Class getEntityInterfaceClass(){
  return ImportHandler.class;
 }


 public ImportHandler create() throws javax.ejb.CreateException{
  return (ImportHandler) super.createIDO();
 }


public java.util.Collection findAllAutomaticUpdates()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ImportHandlerBMPBean)entity).ejbFindAllAutomaticUpdates();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllImportHandlers()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ImportHandlerBMPBean)entity).ejbFindAllImportHandlers();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ImportHandler findByClassName(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ImportHandlerBMPBean)entity).ejbFindByClassName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ImportHandler findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ImportHandler) super.findByPrimaryKeyIDO(pk);
 }



}