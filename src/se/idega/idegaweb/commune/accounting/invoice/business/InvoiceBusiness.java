package se.idega.idegaweb.commune.accounting.invoice.business;


public interface InvoiceBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.accounting.invoice.data.BatchRun getBatchRunByCategory(java.lang.String p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public int getNoPlacements(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,javax.ejb.FinderException,com.idega.data.IDOException, java.rmi.RemoteException;
 public int getNoProviders(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,javax.ejb.FinderException,com.idega.data.IDOException, java.rmi.RemoteException;
 public int getTotAmountWithoutVAT(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,javax.ejb.FinderException,com.idega.data.IDOException, java.rmi.RemoteException;
 public boolean isHighShool(java.lang.String p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void removePreliminaryInvoice(java.sql.Date p0,java.lang.String p1)throws javax.ejb.RemoveException, java.rmi.RemoteException;
 void removePreliminaryInvoice (se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader header) throws javax.ejb.RemoveException;
 public void startPostingBatch(java.sql.Date p0,java.lang.String p1,com.idega.presentation.IWContext p2)throws com.idega.data.IDOLookupException,javax.ejb.FinderException,se.idega.idegaweb.commune.accounting.invoice.business.SchoolCategoryNotFoundException, java.rmi.RemoteException;
    se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome getInvoiceRecordHome () throws java.rmi.RemoteException;
    se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome getInvoiceHeaderHome () throws java.rmi.RemoteException;
 se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader [] getInvoiceHeadersByCustodianOrChild (com.idega.user.data.User user, java.util.Date fromPeriod, java.util.Date toPeriod);
 se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord [] getInvoiceRecordsByInvoiceHeader (se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader header);
 com.idega.user.data.User getChildByInvoiceRecord (se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord record) throws java.rmi.RemoteException;
}
