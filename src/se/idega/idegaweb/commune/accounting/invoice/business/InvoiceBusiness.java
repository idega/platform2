package se.idega.idegaweb.commune.accounting.invoice.business;


public interface InvoiceBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader createInvoiceHeader(java.lang.String p0,com.idega.user.data.User p1,int p2,java.lang.String p3,java.lang.String p4,java.sql.Date p5)throws javax.ejb.CreateException, java.rmi.RemoteException;
se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord createInvoiceRecord(com.idega.user.data.User p1,Integer amount, java.sql.Date checkStartPeriod, java.sql.Date checkEndtPeriod, String doublePosting, Integer invoiceCompilation, String invoiceText, String note, Integer numberOfDays, String ownPosting, java.sql.Date placementStartPeriod, java.sql.Date placementEndPeriod, Integer provider, String regulationSpecType, Integer vatAmount, Integer vatRule) throws javax.ejb.CreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.BatchRun getBatchRunByCategory(java.lang.String p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.user.data.User getChildByInvoiceRecord(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome getInvoiceHeaderHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader[] getInvoiceHeadersByCustodianOrChild(com.idega.user.data.User p0,java.util.Date p1,java.util.Date p2) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome getInvoiceRecordHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord[] getInvoiceRecordsByInvoiceHeader(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0) throws java.rmi.RemoteException;
 public int getNoPlacements(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,javax.ejb.FinderException,com.idega.data.IDOException, java.rmi.RemoteException;
 public int getNoProviders(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,javax.ejb.FinderException,com.idega.data.IDOException, java.rmi.RemoteException;
 public java.util.Collection getPaymentRecordsByCategoryProviderAndPeriod(com.idega.block.school.data.SchoolCategory p0,com.idega.block.school.data.School p1,java.sql.Date p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getPaymentRecordsByCategoryProviderAndPeriod(java.lang.String p0,java.lang.String p1,java.sql.Date p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public int getTotAmountWithoutVAT(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,javax.ejb.FinderException,com.idega.data.IDOException, java.rmi.RemoteException;
 public boolean isHighShool(java.lang.String p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void removeInvoiceRecord(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord p0)throws java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
 public void removePreliminaryInvoice(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0)throws javax.ejb.RemoveException, java.rmi.RemoteException;
 public void removePreliminaryInvoice(java.sql.Date p0,java.lang.String p1)throws javax.ejb.RemoveException, java.rmi.RemoteException;
 public void startPostingBatch(java.sql.Date p0,java.lang.String p1,com.idega.presentation.IWContext p2)throws com.idega.data.IDOLookupException,javax.ejb.FinderException,se.idega.idegaweb.commune.accounting.invoice.business.SchoolCategoryNotFoundException, java.rmi.RemoteException;
    se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType [] getAllRegulationSpecTypes () throws java.rmi.RemoteException;
    se.idega.idegaweb.commune.accounting.regulations.data.VATRule [] getAllVatRules () throws java.rmi.RemoteException;
}
