package se.idega.idegaweb.commune.accounting.invoice.data;


public class InvoiceHeaderHomeImpl extends com.idega.data.IDOFactory implements InvoiceHeaderHome
{
 protected Class getEntityInterfaceClass(){
  return InvoiceHeader.class;
 }


 public InvoiceHeader create() throws javax.ejb.CreateException{
  return (InvoiceHeader) super.createIDO();
 }


 public InvoiceHeader findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (InvoiceHeader) super.findByPrimaryKeyIDO(pk);
 }



}