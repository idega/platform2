package se.idega.idegaweb.commune.accounting.invoice.data;


public interface PaymentRecordHome extends com.idega.data.IDOHome
{
 public PaymentRecord create() throws javax.ejb.CreateException;
 public PaymentRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByMonth(com.idega.util.CalendarMonth p0)throws javax.ejb.FinderException;
 public java.util.Collection findByPaymentHeader(se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader p0)throws javax.ejb.FinderException;
 public java.util.Collection findByPaymentHeaders(java.util.Collection p0)throws javax.ejb.FinderException;
 public PaymentRecord findByPostingStrings(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException;
 public PaymentRecord findByPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,com.idega.util.CalendarMonth p4)throws javax.ejb.FinderException;
 public int getCountForMonthAndStatusLH(com.idega.util.CalendarMonth p0)throws javax.ejb.FinderException,com.idega.data.IDOException;
 public int getCountForMonthCategoryAndStatusLH(java.sql.Date p0,java.lang.String p1)throws javax.ejb.FinderException,com.idega.data.IDOException;
 public int getPlacementCountForSchoolCategoryAndPeriod(java.lang.String p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException;
 public int getTotAmountForProviderAndPeriod(int p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException;
 public int getTotAmountForSchoolCategoryAndPeriod(java.lang.String p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException;

}