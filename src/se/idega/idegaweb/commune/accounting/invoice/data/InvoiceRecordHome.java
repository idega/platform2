package se.idega.idegaweb.commune.accounting.invoice.data;


public interface InvoiceRecordHome extends com.idega.data.IDOHome
{
 public InvoiceRecord create() throws javax.ejb.CreateException;
 public InvoiceRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByContract(se.idega.idegaweb.commune.care.data.ChildCareContract p0)throws javax.ejb.FinderException;
 public java.util.Collection findByInvoiceHeader(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0)throws javax.ejb.FinderException;
 public java.util.Collection findByMonthAndCategory(com.idega.util.CalendarMonth p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findByPaymentRecord(se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord p0)throws javax.ejb.FinderException;
 public java.util.Collection findByPaymentRecordOrderedByStudentName(se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord p0)throws javax.ejb.FinderException;
 public java.util.Collection findByPaymentRecords(se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord[] p0)throws javax.ejb.FinderException;
 public int getIndividualCountByPaymentRecords(se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord[] p0)throws com.idega.data.IDOException;
 public int getNumberOfHandledChildrenForSchoolTypesAndMonth(java.util.Collection p0,com.idega.util.CalendarMonth p1)throws com.idega.data.IDOException;
 public int getPlacementCountForSchoolCategoryAndPeriod(java.lang.String p0,java.sql.Date p1)throws com.idega.data.IDOException;
 public double getTotalAmountForSchoolTypesAndMonth(java.util.Collection p0,com.idega.util.CalendarMonth p1)throws com.idega.data.IDOException;

}