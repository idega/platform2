package se.idega.idegaweb.commune.accounting.invoice.business;


public class RegularInvoiceBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements RegularInvoiceBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return RegularInvoiceBusiness.class;
 }


 public RegularInvoiceBusiness create() throws javax.ejb.CreateException{
  return (RegularInvoiceBusiness) super.createIBO();
 }



}