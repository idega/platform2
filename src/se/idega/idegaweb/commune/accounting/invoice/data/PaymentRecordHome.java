package se.idega.idegaweb.commune.accounting.invoice.data;


public interface PaymentRecordHome extends com.idega.data.IDOHome
{
 public PaymentRecord create() throws javax.ejb.CreateException;
 public PaymentRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByMonth(java.sql.Date p0)throws javax.ejb.FinderException;
 public java.util.Collection findByPaymentHeader(se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader p0)throws javax.ejb.FinderException;
 public PaymentRecord findByPaymentHeaderAndRuleSpecType(se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader p0,java.lang.String p1)throws javax.ejb.FinderException;
 public int getPlacementCountForSchoolCategoryAndPeriod(int p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException;
 public int getTotAmountForProviderAndPeriod(int p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException;
 public int getTotAmountForSchoolCategoryAndPeriod(int p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException;

}