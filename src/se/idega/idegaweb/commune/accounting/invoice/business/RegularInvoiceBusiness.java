package se.idega.idegaweb.commune.accounting.invoice.business;


public interface RegularInvoiceBusiness extends com.idega.business.IBOService
{
 public java.util.Collection findRegularInvoicesForPeriodeAndCategory(java.sql.Date p0,com.idega.block.school.data.SchoolCategory p1)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findRegularInvoicesForPeriodeUserAndCategory(java.sql.Date p0,java.sql.Date p1,int p2,java.lang.String p3)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findRegularLowIncomeInvoicesForPeriodeAndCategory(java.sql.Date p0,int p1,com.idega.block.school.data.SchoolCategory p2)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
}
