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

public ImportFileClass findByClassName(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ImportFileClassBMPBean)entity).ejbFindByClassName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ImportFileClass findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ImportFileClass) super.findByPrimaryKeyIDO(pk);
 }



}