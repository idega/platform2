package se.idega.idegaweb.commune.accounting.invoice.data;


public interface InvoiceRecordHome extends com.idega.data.IDOHome
{
 public InvoiceRecord create() throws javax.ejb.CreateException;
 public InvoiceRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}