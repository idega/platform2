package se.idega.idegaweb.commune.accounting.invoice.business;


public interface RegularPaymentBusiness extends com.idega.business.IBOService
{
 public java.util.Collection findOngoingRegularPaymentsForUserAndSchoolByDate(com.idega.user.data.User p0,com.idega.block.school.data.School p1,java.sql.Date p2)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findRegularPaymentsForPeriode(java.sql.Date p0,java.sql.Date p1) throws java.rmi.RemoteException;
 public java.util.Collection findRegularPaymentsForPeriodeAndCategory(java.sql.Date p0,com.idega.block.school.data.SchoolCategory p1)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findRegularPaymentsForPeriodeAndSchool(java.sql.Date p0,java.sql.Date p1,com.idega.block.school.data.School p2)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findRegularPaymentsForPeriodeAndUser(java.sql.Date p0,java.sql.Date p1,int p2)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
}
