package com.idega.block.importer.data;


public class ImportFileClassHomeImpl extends com.idega.data.IDOFactory implements ImportFileClassHome
{
 protected Class getEntityInterfaceClass(){
  return ImportFileClass.class;
 }


 public ImportFileClass create() throws javax.ejb.CreateException{
  return (ImportFileClass) super.createIDO();
 }


public java.util.Collection findAllImportFileClasses()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ImportFileClassBMPBean)entity).ejbFindAllImportFileClasses();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ImportFileClass findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ImportFileClass) super.findByPrimaryKeyIDO(pk);
 }

/*
public com.idega.user.data.ImportFileClass getFemaleImportFileClass()throws java.rmi.RemoteException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	com.idega.user.data.ImportFileClass theReturn = ((ImportFileClassBMPBean)entity).ejbHomeGetFemaleImportFileClass();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}
*/


}