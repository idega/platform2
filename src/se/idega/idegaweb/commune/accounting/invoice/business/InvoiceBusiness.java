package se.idega.idegaweb.commune.accounting.invoice.business;


public interface InvoiceBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord createDetailedPaymentRecord(com.idega.user.data.User p0,se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord p1,com.idega.user.data.User p2)throws com.idega.data.IDOLookupException,javax.ejb.FinderException,java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader createInvoiceHeader(java.lang.String p0,com.idega.user.data.User p1,int p2,java.sql.Date p3)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord createInvoiceRecord(com.idega.user.data.User p0,se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.sql.Date p8,java.sql.Date p9,java.sql.Date p10,java.sql.Date p11,java.lang.Integer p12,java.lang.Integer p13,java.lang.Integer p14,java.lang.Integer p15,java.lang.Integer p16,java.lang.String p17,java.lang.String p18,java.lang.Integer p19,java.lang.String p20,java.lang.String p21,java.lang.Integer p22)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord createInvoiceRecord(se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord p0,com.idega.block.school.data.SchoolClassMember p1,se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail p2,se.idega.idegaweb.commune.accounting.invoice.business.PlacementTimes p3,java.sql.Date p4,java.sql.Date p5,java.lang.String p6)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord createOrUpdateVatPaymentRecord(se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord p0,com.idega.block.school.data.SchoolType p1,com.idega.block.school.data.SchoolYear p2,java.lang.String p3)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.core.file.data.ICFile exportToExcel(com.idega.idegaweb.IWResourceBundle p0,java.lang.String p1,se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p2,boolean p3)throws javax.ejb.FinderException,java.io.IOException,javax.ejb.CreateException, java.rmi.RemoteException;
 public java.util.Collection findInvoiceRecordsByContract(se.idega.idegaweb.commune.care.data.ChildCareContract p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public int generatePdf(java.lang.String p0,com.idega.io.MemoryFileBuffer p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType[] getAllRegulationSpecTypes()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getAllVATRuleRegulations()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.BatchRun getBatchRunByCategory(java.lang.String p0,boolean p1)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.user.data.User getChildByInvoiceRecord(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.care.data.ChildCareContractHome getChildCareContractHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome getInvoiceHeaderHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader[] getInvoiceHeadersByCustodianOrChild(java.lang.String p0,com.idega.user.data.User p1,com.idega.util.CalendarMonth p2,com.idega.util.CalendarMonth p3) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome getInvoiceRecordHome() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord[] getInvoiceRecordsByInvoiceHeader(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0) throws java.rmi.RemoteException;
 public int getNoPlacements(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
 public int getNoProviders(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
 public int getNumberOfHandledChildren(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
 public int getNumberOfInvoices(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome getPaymentHeaderHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome getPaymentRecordHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord[] getPaymentRecordsBySchoolCategoryAndProviderAndPeriod(java.lang.String p0,java.lang.Integer p1,java.sql.Date p2,java.sql.Date p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClassMember[] getSchoolClassMembers(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0) throws java.rmi.RemoteException;
 public int getTotAmountWithoutVAT(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
 public double getTotalAmountOfInvoices(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.Regulation getVATRuleRegulation(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean isChildCare(java.lang.String p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void removeInvoiceRecord(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord p0)throws java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
 public void removePaymentRecord(se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord p0)throws java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
 public void removePreliminaryInvoice(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0)throws javax.ejb.RemoveException, java.rmi.RemoteException;
 public void removePreliminaryInvoice(com.idega.util.CalendarMonth p0,java.lang.String p1)throws javax.ejb.FinderException,javax.ejb.RemoveException,se.idega.idegaweb.commune.accounting.invoice.business.BatchAlreadyRunningException,se.idega.idegaweb.commune.accounting.invoice.business.SchoolCategoryNotFoundException,com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public void removePreliminaryPayment(com.idega.util.CalendarMonth p0,java.lang.String p1)throws javax.ejb.RemoveException, java.rmi.RemoteException;
 public void saveInvoiceRecord(java.lang.Integer p0,com.idega.user.data.User p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.sql.Date p8,java.sql.Date p9,java.sql.Date p10,java.sql.Date p11,java.lang.String p12,java.lang.String p13,java.lang.Integer p14,java.lang.Integer p15,java.lang.Integer p16,java.lang.Integer p17)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void removeTestRecordsForProvider(com.idega.util.CalendarMonth month, String category, com.idega.block.school.data.School school) throws javax.ejb.RemoveException;
}
