package se.idega.idegaweb.commune.accounting.invoice.business;


public interface InvoiceBusiness extends com.idega.business.IBOService {
	boolean isChildCare(java.lang.String p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
	com.idega.block.school.data.SchoolClassMember[] getSchoolClassMembers(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0) throws java.rmi.RemoteException;
	com.idega.user.data.User getChildByInvoiceRecord(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
	double getTotalAmountOfInvoices(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
	int generatePdf(java.lang.String p0,com.idega.io.MemoryFileBuffer p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
	int getNoPlacements(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
	int getNoProviders(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
	int getNumberOfHandledChildren(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
	int getNumberOfInvoices(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
	int getTotAmountWithoutVAT(se.idega.idegaweb.commune.accounting.invoice.data.BatchRun p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
	java.util.Collection findInvoiceRecordsByContract(se.idega.idegaweb.commune.childcare.data.ChildCareContract contract) throws java.rmi.RemoteException,javax.ejb.FinderException;
	java.util.Collection getAllVATRuleRegulations()throws java.rmi.RemoteException, java.rmi.RemoteException;
	se.idega.idegaweb.commune.accounting.invoice.data.BatchRun getBatchRunByCategory(java.lang.String p0, boolean p1)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
	se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader createInvoiceHeader(java.lang.String p0,com.idega.user.data.User p1,int p2,java.sql.Date p3)throws javax.ejb.CreateException, java.rmi.RemoteException;
	se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome getInvoiceHeaderHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
	se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader[] getInvoiceHeadersByCustodianOrChild(java.lang.String p0,com.idega.user.data.User p1,com.idega.util.CalendarMonth p2,com.idega.util.CalendarMonth p3) throws java.rmi.RemoteException;
	se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord createDetailedPaymentRecord(com.idega.user.data.User child,se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord paymentRecord,com.idega.user.data.User registrator)throws com.idega.data.IDOLookupException, javax.ejb.FinderException, java.rmi.RemoteException,javax.ejb.CreateException;
	se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord createInvoiceRecord	(se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord paymentRecord, com.idega.block.school.data.SchoolClassMember placement, se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail postingDetail, PlacementTimes checkPeriod, java.sql.Date startPlacementDate, java.sql.Date endPlacementDate, String createdBySignature)	throws java.rmi.RemoteException, javax.ejb.CreateException;
	se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord createInvoiceRecord(com.idega.user.data.User p0,se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.sql.Date p8,java.sql.Date p9,java.sql.Date p10,java.sql.Date p11,java.lang.Integer p12,java.lang.Integer p13,java.lang.Integer p14,java.lang.Integer p15,java.lang.Integer p16,java.lang.String p17,java.lang.String p18,java.lang.Integer p19,java.lang.String p20,java.lang.String p21,java.lang.Integer p22)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
	se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome getInvoiceRecordHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
	se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord[] getInvoiceRecordsByInvoiceHeader(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0) throws java.rmi.RemoteException;
	se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome getPaymentHeaderHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
	se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord createOrUpdateVatPaymentRecord(se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord previousPaymentRecord, com.idega.block.school.data.SchoolType sType,com.idega.block.school.data.SchoolYear sYear,String signature) throws java.rmi.RemoteException,javax.ejb.CreateException;
	se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome getPaymentRecordHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
	se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord[] getPaymentRecordsBySchoolCategoryAndProviderAndPeriod(java.lang.String p0,java.lang.Integer p1,java.sql.Date p2,java.sql.Date p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
	se.idega.idegaweb.commune.accounting.regulations.data.Regulation getVATRuleRegulation(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
	se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType[] getAllRegulationSpecTypes()throws java.rmi.RemoteException, java.rmi.RemoteException;
	void removeInvoiceRecord(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord p0)throws java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
	void removePaymentRecord (se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord paymentRecord) throws java.rmi.RemoteException, javax.ejb.RemoveException;
	void removePreliminaryInvoice(com.idega.util.CalendarMonth p0,java.lang.String p1)throws javax.ejb.RemoveException, java.rmi.RemoteException, javax.ejb.FinderException, BatchAlreadyRunningException, SchoolCategoryNotFoundException, com.idega.data.IDOLookupException;
	void removePreliminaryInvoice(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0)throws javax.ejb.RemoveException, java.rmi.RemoteException;
	void removePreliminaryPayment(com.idega.util.CalendarMonth month, java.lang.String category) throws javax.ejb.RemoveException;
	void saveInvoiceRecord(Integer recordId, com.idega.user.data.User p1, Integer placementId, Integer providerId, String invoiceText, String invoiceText2, String ruleText, String note, java.sql.Date checkEndPeriod, java.sql.Date checkStartPeriod, java.sql.Date placementStartPeriod, java.sql.Date placementEndPeriod, String ownPosting, String doublePosting, Integer amount, Integer vatAmount, Integer vatRule, Integer regSpecTypeId) throws java.rmi.RemoteException, javax.ejb.FinderException;
}
