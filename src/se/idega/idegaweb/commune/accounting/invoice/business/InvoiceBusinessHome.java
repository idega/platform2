package se.idega.idegaweb.commune.accounting.invoice.business;


public interface InvoiceBusinessHome extends com.idega.business.IBOHome
{
 public InvoiceBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}