package se.idega.idegaweb.commune.accounting.invoice.data;


public class InvoiceRecordHomeImpl extends com.idega.data.IDOFactory implements InvoiceRecordHome
{
 protected Class getEntityInterfaceClass(){
  return InvoiceRecord.class;
 }


 public InvoiceRecord create() throws javax.ejb.CreateException{
  return (InvoiceRecord) super.createIDO();
 }


 public InvoiceRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (InvoiceRecord) super.findByPrimaryKeyIDO(pk);
 }



}