package se.idega.idegaweb.commune.accounting.invoice.data;


public interface InvoiceHeaderHome extends com.idega.data.IDOHome
{
 public InvoiceHeader create() throws javax.ejb.CreateException;
 public InvoiceHeader findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public InvoiceHeader findByCustodian(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public java.util.Collection findByMonth(java.sql.Date p0)throws javax.ejb.FinderException;
 public java.util.Collection findByMonthAndSchoolCategory(java.sql.Date p0,com.idega.block.school.data.SchoolCategory p1)throws javax.ejb.FinderException;
    java.util.Collection findInvoiceHeadersByCustodianOrChild (com.idega.user.data.User user) throws javax.ejb.FinderException;
}
