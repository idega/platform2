package is.idega.idegaweb.member.isi.block.accounting.business;


public interface AccountingStatsBusiness extends com.idega.business.IBOSession
{
 public com.idega.block.datareport.util.ReportableCollection getPaymentStatusByLeaguesGroupsAndDateIntervalFiltering(java.util.Collection p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
