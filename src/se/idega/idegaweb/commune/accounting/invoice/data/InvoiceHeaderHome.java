package se.idega.idegaweb.commune.accounting.invoice.data;


public interface InvoiceHeaderHome extends com.idega.data.IDOHome
{
 public InvoiceHeader create() throws javax.ejb.CreateException;
 public InvoiceHeader findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public InvoiceHeader findByCustodian(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public InvoiceHeader findByCustodianAndMonth(com.idega.user.data.User p0,com.idega.util.CalendarMonth p1)throws javax.ejb.FinderException;
 public InvoiceHeader findByCustodianID(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByCustodianOrChild(java.lang.String p0,com.idega.user.data.User p1,java.util.Collection p2,java.util.Date p3,java.util.Date p4)throws javax.ejb.FinderException;
 public java.util.Collection findByMonth(com.idega.util.CalendarMonth p0)throws javax.ejb.FinderException;
 public java.util.Collection findByMonthAndSchoolCategory(com.idega.util.CalendarMonth p0,com.idega.block.school.data.SchoolCategory p1)throws javax.ejb.FinderException;
 public java.util.Collection findByStatusAndCategory(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException;
 public int getNumberOfInvoicesForSchoolCategoryAndMonth(java.lang.String p0,com.idega.util.CalendarMonth p1)throws com.idega.data.IDOException;

}