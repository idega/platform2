package se.idega.idegaweb.commune.accounting.invoice.data;


public interface InvoiceRecordHome extends com.idega.data.IDOHome
{
 public InvoiceRecord create() throws javax.ejb.CreateException;
 public InvoiceRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByInvoiceHeader(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0)throws javax.ejb.FinderException;
 public java.util.Collection findByMonth(java.sql.Date p0)throws javax.ejb.FinderException;

}