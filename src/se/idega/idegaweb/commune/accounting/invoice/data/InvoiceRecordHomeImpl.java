package se.idega.idegaweb.commune.accounting.invoice.data;


public class InvoiceRecordHomeImpl extends com.idega.data.IDOFactory implements InvoiceRecordHome
{
 protected Class getEntityInterfaceClass(){
  return InvoiceRecord.class;
 }


 public InvoiceRecord create() throws javax.ejb.CreateException{
  return (InvoiceRecord) super.createIDO();
 }


public java.util.Collection findByInvoiceHeader(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((InvoiceRecordBMPBean)entity).ejbFindByInvoiceHeader(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByMonth(java.sql.Date p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((InvoiceRecordBMPBean)entity).ejbFindByMonth(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public InvoiceRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (InvoiceRecord) super.findByPrimaryKeyIDO(pk);
 }



}