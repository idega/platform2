package se.idega.idegaweb.commune.accounting.invoice.data;


public interface PaymentRecordHome extends com.idega.data.IDOHome
{
 public PaymentRecord create() throws javax.ejb.CreateException;
 public PaymentRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByMonth(com.idega.util.CalendarMonth p0)throws javax.ejb.FinderException;
 public java.util.Collection findByMonthAndCategory(com.idega.util.CalendarMonth p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findByPaymentHeader(se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader p0)throws javax.ejb.FinderException;
 public PaymentRecord findByPaymentHeaderAndPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,com.idega.util.CalendarMonth p5)throws javax.ejb.FinderException;
 public PaymentRecord findByPaymentHeaderAndPostingStringsAndVATRuleRegulationAndPaymentTextAndMonth(se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader p0,java.lang.String p1,java.lang.String p2,se.idega.idegaweb.commune.accounting.regulations.data.Regulation p3,java.lang.String p4,com.idega.util.CalendarMonth p5)throws javax.ejb.FinderException;
 public java.util.Collection findByPaymentHeaders(java.util.Collection p0)throws javax.ejb.FinderException;
 public PaymentRecord findByPostingStrings(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException;
 public PaymentRecord findByPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,com.idega.util.CalendarMonth p4)throws javax.ejb.FinderException;
 public PaymentRecord findByPostingStringsAndVATRuleRegulationAndPaymentTextAndMonthAndStatus(java.lang.String p0,java.lang.String p1,se.idega.idegaweb.commune.accounting.regulations.data.Regulation p2,java.lang.String p3,com.idega.util.CalendarMonth p4,char p5)throws javax.ejb.FinderException;
 public int getCountForMonthAndStatusLH(com.idega.util.CalendarMonth p0)throws com.idega.data.IDOException;
 public int getCountForMonthCategoryAndStatusLH(com.idega.util.CalendarMonth p0,java.lang.String p1)throws com.idega.data.IDOException;
 public int getPlacementCountForSchoolCategoryAndMonth(java.lang.String p0,com.idega.util.CalendarMonth p1)throws com.idega.data.IDOException;
 public int getPlacementCountForSchoolIdAndDateAndSchoolCategory(int p0,java.sql.Date p1,java.lang.String p2)throws com.idega.data.IDOException;
 public int getTotAmountForProviderAndPeriod(int p0,java.sql.Date p1,java.lang.String p2)throws com.idega.data.IDOException;
 public int getTotAmountForSchoolCategoryAndPeriod(java.lang.String p0,java.sql.Date p1)throws com.idega.data.IDOException;
 public int getTotalVATAmountForPaymentHeaderAndMonthAndVATRuleRegulation(se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader p0,com.idega.util.CalendarMonth p1,se.idega.idegaweb.commune.accounting.regulations.data.Regulation p2)throws com.idega.data.IDOException;

}