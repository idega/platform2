package se.idega.idegaweb.commune.accounting.invoice.data;


public interface InvoiceRecordHome extends com.idega.data.IDOHome
{
 public InvoiceRecord create() throws javax.ejb.CreateException;
 public InvoiceRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByInvoiceHeader(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0)throws javax.ejb.FinderException;
 public java.util.Collection findByPaymentRecord(se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord p0)throws javax.ejb.FinderException;
 public int getPlacementCountForSchoolCategoryAndPeriod(java.lang.String p0,java.sql.Date p1)throws com.idega.data.IDOException;

}