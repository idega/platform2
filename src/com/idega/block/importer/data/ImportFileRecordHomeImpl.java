package com.idega.block.importer.data;


public class ImportFileRecordHomeImpl extends com.idega.data.IDOFactory implements ImportFileRecordHome
{
 protected Class getEntityInterfaceClass(){
  return ImportFileRecord.class;
 }


 public ImportFileRecord create() throws javax.ejb.CreateException{
  return (ImportFileRecord) super.createIDO();
 }


public ImportFileRecord findImportFileRecordFromNameAndSize(java.lang.String p0,java.lang.Integer p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ImportFileRecordBMPBean)entity).ejbFindImportFileRecordFromNameAndSize(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ImportFileRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ImportFileRecord) super.findByPrimaryKeyIDO(pk);
 }



}