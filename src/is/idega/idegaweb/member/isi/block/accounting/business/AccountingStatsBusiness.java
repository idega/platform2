package is.idega.idegaweb.member.isi.block.accounting.business;


public interface AccountingStatsBusiness extends com.idega.business.IBOSession
{
 public com.idega.block.datareport.util.ReportableCollection getPaymentStatusByLeaguesGroupsAndDateIntervalFiltering(java.sql.Date p0,java.sql.Date p1,java.util.Collection p2,java.util.Collection p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
