package com.idega.block.importer.data;


public class ImportHandlerHomeImpl extends com.idega.data.IDOFactory implements ImportHandlerHome
{
 protected Class getEntityInterfaceClass(){
  return ImportHandler.class;
 }


 public ImportHandler create() throws javax.ejb.CreateException{
  return (ImportHandler) super.createIDO();
 }


public java.util.Collection findAllImportHandlers()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ImportHandlerBMPBean)entity).ejbFindAllImportHandlers();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ImportHandler findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ImportHandler) super.findByPrimaryKeyIDO(pk);
 }

/*
public com.idega.user.data.ImportHandler getFemaleImportHandler()throws java.rmi.RemoteException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	com.idega.user.data.ImportHandler theReturn = ((ImportHandlerBMPBean)entity).ejbHomeGetFemaleImportHandler();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}
*/


} 