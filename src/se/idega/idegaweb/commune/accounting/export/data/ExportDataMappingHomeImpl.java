package se.idega.idegaweb.commune.accounting.export.data;


public class ExportDataMappingHomeImpl extends com.idega.data.IDOFactory implements ExportDataMappingHome
{
 protected Class getEntityInterfaceClass(){
  return ExportDataMapping.class;
 }


 public ExportDataMapping create() throws javax.ejb.CreateException{
  return (ExportDataMapping) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ExportDataMappingBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ExportDataMapping findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ExportDataMapping) super.findByPrimaryKeyIDO(pk);
 }



}