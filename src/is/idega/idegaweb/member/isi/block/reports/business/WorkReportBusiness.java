package is.idega.idegaweb.member.isi.block.reports.business;


public interface WorkReportBusiness extends com.idega.business.IBOService,is.idega.idegaweb.member.business.MemberUserBusiness
{
 public int getOrCreateWorkReportIdForClubIdByYear(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportHome getWorkReportHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubMemberHome getWorkReportClubMemberHome() throws java.rmi.RemoteException;
 public boolean createEntry(int reportID, String personalID) throws java.rmi.RemoteException;
}
