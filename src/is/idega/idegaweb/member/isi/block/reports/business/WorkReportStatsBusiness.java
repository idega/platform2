package is.idega.idegaweb.member.isi.block.reports.business;

import java.rmi.RemoteException;
import java.util.Collection;

import com.idega.block.datareport.util.ReportableCollection;


public interface WorkReportStatsBusiness extends com.idega.business.IBOSession
{
 public com.idega.block.datareport.util.ReportableCollection getClubMemberStatisticsForRegionalUnions(Integer p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public ReportableCollection getStatisticsForLeaguesByYearRegionalUnionsClubsAndLeaguesFiltering(final Integer year, Collection regionalUnionsFilter, Collection clubsFilter, Collection leaguesFilter) throws RemoteException;
}
