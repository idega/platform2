package se.idega.idegaweb.commune.accounting.invoice.data;


public interface RegularInvoiceEntryHome extends com.idega.data.IDOHome
{
 public RegularInvoiceEntry create() throws javax.ejb.CreateException;
 public RegularInvoiceEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findRegularInvoicesForPeriodAndCategoryExceptType(java.sql.Date p0,java.lang.String p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findRegularInvoicesForPeriodAndChildAndCategory(java.sql.Date p0,java.sql.Date p1,int p2,java.lang.String p3)throws javax.ejb.FinderException;
 public java.util.Collection findRegularInvoicesForPeriodAndChildAndCategoryAndRegSpecType(java.sql.Date p0,int p1,java.lang.String p2,int p3)throws javax.ejb.FinderException;

}