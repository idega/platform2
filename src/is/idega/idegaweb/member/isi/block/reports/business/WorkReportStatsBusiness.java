package is.idega.idegaweb.member.isi.block.reports.business;


public interface WorkReportStatsBusiness extends com.idega.business.IBOSession
{
 public com.idega.block.datareport.util.ReportableCollection getClubMemberStatisticsForRegionalUnions(java.lang.Integer p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getStatisticsForLeaguesByYearRegionalUnionsAndLeaguesFiltering(java.lang.Integer p0,java.util.Collection p1,java.util.Collection p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getStatisticsForLeaguesByYearRegionalUnionsClubsAndLeaguesFiltering(java.lang.Integer p0,java.util.Collection p1,java.util.Collection p2,java.util.Collection p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
