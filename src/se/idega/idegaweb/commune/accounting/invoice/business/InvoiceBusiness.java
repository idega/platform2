package se.idega.idegaweb.commune.accounting.invoice.business;


public interface InvoiceBusiness extends com.idega.business.IBOService
{
 public void removePreliminaryInvoice(java.sql.Date p0)throws javax.ejb.RemoveException, java.rmi.RemoteException;
 public void startPostingBatch(java.sql.Date p0,com.idega.presentation.IWContext p1) throws java.rmi.RemoteException;
}
