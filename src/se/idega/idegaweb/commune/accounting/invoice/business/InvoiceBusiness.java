package se.idega.idegaweb.commune.accounting.invoice.business;

import com.idega.user.data.User;
import java.sql.Date;

public interface InvoiceBusiness extends com.idega.business.IBOService
{
    int generatePdf (String title, com.idega.io.MemoryFileBuffer buffer) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader createInvoiceHeader(java.lang.String p0,com.idega.user.data.User p1,int p2,java.sql.Date p5)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord createInvoiceRecord(User createdBy, Integer invoiceHeaderId, Integer placementId, Integer providerId, String ruleText, String invoiceText, String invoiceText2, String note, Date placementStartPeriod, Date placementEndPeriod, Date checkStartPeriod, Date checkEndPeriod, Integer amount, Integer vatAmount, Integer numberOfDays, Integer regSpecTypeId, Integer vatRule, String ownPosting, String doublePosting, Integer pieceAmount, String ownPaymentPosting, String doublePaymentPosting)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType[] getAllRegulationSpecTypes()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.VATRule[] getAllVatRules()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.VATRule getVatRule(int primaryKey)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.BatchRun getBatchRunByCategory(java.lang.String p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.user.data.User getChildByInvoiceRecord(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome getInvoiceHeaderHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader[] getInvoiceHeadersByCustodianOrChild(String schoolCategory,com.idega.user.data.User p0,java.util.Date p1,java.util.Date p2) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome getInvoiceRecordHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome getPaymentRecordHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome getPaymentHeaderHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord[] getInvoiceRecordsByInvoiceHeader(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0) throws java.rmi.RemoteException;
 public int getNoPlacements(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,javax.ejb.FinderException,com.idega.data.IDOException, java.rmi.RemoteException;
 public int getNoProviders(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,javax.ejb.FinderException,com.idega.data.IDOException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord [] getPaymentRecordsBySchoolCategoryAndProviderAndPeriod (String schoolCategory, Integer providerId, java.sql.Date startPeriod, java.sql.Date endPeriod) throws java.rmi.RemoteException;
 public java.util.Collection getPaymentRecordsByCategoryProviderAndPeriod(java.lang.String p0,java.lang.String p1,java.sql.Date p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getPaymentRecordsByCategoryProviderAndPeriod(com.idega.block.school.data.SchoolCategory p0,com.idega.block.school.data.School p1,java.sql.Date p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public int getTotAmountWithoutVAT(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,javax.ejb.FinderException,com.idega.data.IDOException, java.rmi.RemoteException;
 public boolean isHighShool(java.lang.String p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void removeInvoiceRecord(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord p0)throws java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
 public void removePreliminaryInvoice(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0)throws javax.ejb.RemoveException, java.rmi.RemoteException;
 public void removePreliminaryInvoice(java.sql.Date p0,java.lang.String p1)throws javax.ejb.RemoveException, java.rmi.RemoteException;
 public void startPostingBatch(java.sql.Date p0,java.sql.Date p1,java.lang.String p2,com.idega.presentation.IWContext p3)throws com.idega.data.IDOLookupException,javax.ejb.FinderException,se.idega.idegaweb.commune.accounting.invoice.business.SchoolCategoryNotFoundException, java.rmi.RemoteException;
    com.idega.block.school.data.SchoolClassMember [] getSchoolClassMembers (se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0)throws java.rmi.RemoteException;
}
