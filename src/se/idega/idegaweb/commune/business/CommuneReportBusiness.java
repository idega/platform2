package se.idega.idegaweb.commune.business;


public interface CommuneReportBusiness extends com.idega.business.IBOSession
{
 public com.idega.block.datareport.util.ReportableCollection getChildAndItsParentsRegisteredInCommune(java.sql.Date p0,java.sql.Date p1,java.sql.Timestamp p2,java.sql.Timestamp p3)throws java.rmi.RemoteException,javax.ejb.CreateException,javax.ejb.FinderException, java.rmi.RemoteException;
}
