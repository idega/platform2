package is.idega.idegaweb.member.isi.block.accounting.business;


public interface AccountingStatsBusiness extends com.idega.business.IBOSession
{
 public com.idega.block.datareport.util.ReportableCollection getDebtOverviewByDivisionsGroupsAndDateIntervalFiltering(java.sql.Date p0,java.sql.Date p1,java.util.Collection p2,java.util.Collection p3,java.lang.String p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getEntryOverviewByDivisionsGroupsAndDateIntervalFiltering(java.sql.Date p0,java.sql.Date p1,java.util.Collection p2,java.util.Collection p3,java.lang.String p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getLatePaymentListByDivisionsGroupsAndDateIntervalFiltering(java.util.Collection p0,java.util.Collection p1,java.lang.String p2,java.lang.String p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getPaymentListByDivisionsGroupsAndDateIntervalFiltering(java.sql.Date p0,java.util.Collection p1,java.util.Collection p2,java.lang.String p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getPaymentOverviewByDivisionsGroupsAndDateIntervalFiltering(java.sql.Date p0,java.sql.Date p1,java.util.Collection p2,java.util.Collection p3,java.lang.String p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getPaymentStatusByDivisionsGroupsAndDateIntervalFiltering(java.sql.Date p0,java.sql.Date p1,java.util.Collection p2,java.util.Collection p3,java.lang.String p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
