package is.idega.idegaweb.member.isi.block.reports.business;


public interface WorkReportStatsBusiness extends com.idega.business.IBOService
{
 public com.idega.block.datareport.util.ReportableCollection getAnnualChangeStatisticsForClubsByYearAndRegionalUnionsFilter(java.lang.Integer p0,java.util.Collection p1,java.lang.String p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getAnnualChangeStatisticsForClubsByYearAndRegionalUnionsFilter(java.lang.Integer p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getCostPerPlayerStatisticsForLeaguesByYearAgeGenderAndLeaguesFiltering(java.lang.Integer p0,java.lang.Integer p1,java.lang.String p2,java.util.Collection p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getGenderStatisticsForRegionalUnionsByYearAndRegionalUnionsFilter(java.lang.Integer p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getMemberStatisticsForClubsByYearAndRegionalUnionsFilter(java.lang.Integer p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getMemberStatisticsForClubsByYearClubsUMFIClubsAndClubTypesFilter(java.lang.Integer p0,java.util.Collection p1,java.util.Collection p2,java.lang.String p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getMemberStatisticsForRegionalUnionsByYearRegionalUnionsUMFIUnionsAndClubTypesFilter(java.lang.Integer p0,java.util.Collection p1,java.util.Collection p2,java.lang.String p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getPlayerStatisticsForClubsByYearAndRegionalUnionsFiltering(java.lang.Integer p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getPlayerStatisticsForClubsByYearClubsUMFIClubsAndClubTypesFilter(java.lang.Integer p0,java.util.Collection p1,java.util.Collection p2,java.lang.String p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getPlayerStatisticsForRegionalUnionsByYearRegionalUnionsUMFIUnionsAndClubTypesFilter(java.lang.Integer p0,java.util.Collection p1,java.util.Collection p2,java.lang.String p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getPlayersForRegionalUnionsByYearAndRegionalUnionsFilter(java.lang.Integer p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getRegionalUnionsAnnualComparisonByYearAndRegionalUnionsFilter(java.lang.Integer p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getStatisticsForLeaguesByYearAndLeaguesAndRegionalUnionsFiltering(java.lang.Integer p0,java.util.Collection p1,java.util.Collection p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getStatisticsForLeaguesByYearAndLeaguesFiltering(java.lang.Integer p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getStatisticsForLeaguesByYearAndLeaguesFilteringComparedWithLastYear(java.lang.Integer p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getStatisticsForLeaguesByYearRegionalUnionsAndLeaguesFiltering(java.lang.Integer p0,java.util.Collection p1,java.util.Collection p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getStatisticsForLeaguesByYearRegionalUnionsClubsAndLeaguesFiltering(java.lang.Integer p0,java.util.Collection p1,java.util.Collection p2,java.util.Collection p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getStatisticsForRegionalUnionsByYearAndRegionalUnionsFilter(java.lang.Integer p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getWorkReportStatusForClubsByYearRegionalUnionsAndClubs(java.lang.Integer p0,java.util.Collection p1,java.util.Collection p2,java.lang.String p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.datareport.util.ReportableCollection getWorkReportStatusForClubsByYearRegionalUnionsClubTypeAndStatus(java.lang.Integer p0,java.util.Collection p1,java.lang.String p2,java.lang.String p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
