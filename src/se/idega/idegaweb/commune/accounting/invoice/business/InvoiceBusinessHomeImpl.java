package se.idega.idegaweb.commune.accounting.invoice.business;


public class InvoiceBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements InvoiceBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return InvoiceBusiness.class;
 }


 public InvoiceBusiness create() throws javax.ejb.CreateException{
  return (InvoiceBusiness) super.createIBO();
 }



}