package se.idega.idegaweb.commune.accounting.invoice.data;


public class BatchRunErrorHomeImpl extends com.idega.data.IDOFactory implements BatchRunErrorHome
{
 protected Class getEntityInterfaceClass(){
  return BatchRunError.class;
 }


 public BatchRunError create() throws javax.ejb.CreateException{
  return (BatchRunError) super.createIDO();
 }


public java.util.Collection findAllOrdered()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BatchRunErrorBMPBean)entity).ejbFindAllOrdered();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public BatchRunError findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BatchRunError) super.findByPrimaryKeyIDO(pk);
 }



}