package se.idega.idegaweb.commune.accounting.invoice.data;


public interface PaymentRecordHome extends com.idega.data.IDOHome
{
 public PaymentRecord create() throws javax.ejb.CreateException;
 public PaymentRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByMonth(java.sql.Date p0)throws javax.ejb.FinderException;
 public java.util.Collection findByPaymentHeader(se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader p0)throws javax.ejb.FinderException;
 public PaymentRecord findByPaymentHeaderAndRuleSpecType(se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader p0,java.lang.String p1)throws javax.ejb.FinderException;

}