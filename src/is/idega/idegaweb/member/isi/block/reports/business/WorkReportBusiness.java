package is.idega.idegaweb.member.isi.block.reports.business;


public interface WorkReportBusiness extends com.idega.business.IBOService,is.idega.idegaweb.member.business.MemberUserBusiness
{
 public boolean addWorkReportGroupToEntity(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1,com.idega.data.IDOEntity p2) throws java.rmi.RemoteException;
 public boolean addWorkReportGroupToEntity(int p0,java.lang.String p1,int p2,com.idega.data.IDOEntity p3) throws java.rmi.RemoteException;
 public boolean changeWorkReportGroupOfEntity(int p0,java.lang.String p1,int p2,java.lang.String p3,int p4,com.idega.data.IDOEntity p5) throws java.rmi.RemoteException;
 public boolean changeWorkReportGroupOfEntity(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2,com.idega.data.IDOEntity p3) throws java.rmi.RemoteException;
 public boolean closeWorkReport(int p0) throws java.rmi.RemoteException;
 public void createOrUpdateLeagueWorkReportGroupsForYear(int p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember createWorkReportBoardMember(int p0,java.lang.String p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember createWorkReportBoardMember(int p0,com.idega.user.data.User p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public boolean createWorkReportData(int p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoard createWorkReportDivisionBoard(int p0,com.idega.user.data.Group p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember createWorkReportMember(int p0,java.lang.String p1)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember createWorkReportMember(int p0,com.idega.user.data.User p1)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember createWorkReportMember(int p0,java.lang.Integer p1)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public void deleteWorkReportAccountRecordsForReport(int p0) throws java.rmi.RemoteException;
 public void deleteWorkReportBoardMembersForReport(int p0) throws java.rmi.RemoteException;
 public void deleteWorkReportMembersForReport(int p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup findWorkReportGroupByNameAndYear(java.lang.String p0,int p1) throws java.rmi.RemoteException;
 public java.util.Collection getAllLeagueWorkReportGroupsForYear(int p0) throws java.rmi.RemoteException;
 public java.util.Collection getAllWorkReportBoardMembersForWorkReportId(int p0) throws java.rmi.RemoteException;
 public java.util.Collection getAllWorkReportDivisionBoardForWorkReportId(int p0) throws java.rmi.RemoteException;
 public java.util.Collection getAllWorkReportGroupsForYearAndType(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.util.List getAllWorkReportGroupsPrimaryKeysThatHaveMembers(int p0) throws java.rmi.RemoteException;
 public java.util.List getAllWorkReportGroupsPrimaryKeysWithNoMembers(int p0) throws java.rmi.RemoteException;
 public java.util.Collection getAllWorkReportMembersForWorkReportId(int p0) throws java.rmi.RemoteException;
 public java.util.Collection getAllWorkReportMembersForWorkReportIdAndWorkReportGroupId(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1) throws java.rmi.RemoteException;
 public java.util.Collection getAllWorkReportsForYear(int p0) throws java.rmi.RemoteException;
 public java.lang.String getFileName(int p0) throws java.rmi.RemoteException;
 public java.util.Collection getLeaguesOfWorkReportById(int p0)throws com.idega.data.IDOException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup getMainBoardWorkReportGroup(int p0) throws java.rmi.RemoteException;
 public int getOrCreateWorkReportIdForGroupIdByYear(int p0,int p1,boolean p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getTotalCountOfCompetitorsForWorkReportYear(int p0) throws java.rmi.RemoteException;
 public int getTotalCountOfMembersForWorkReportYear(int p0) throws java.rmi.RemoteException;
 public int getTotalCountOfPlayersForWorkReportYear(int p0) throws java.rmi.RemoteException;
 public int getTotalCountOfWorkReportsByStatusAndYear(java.lang.String p0,int p1) throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKeyHome getWorkReportAccountKeyHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMemberHome getWorkReportBoardMemberHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReport getWorkReportById(int p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubAccountRecordHome getWorkReportClubAccountRecordHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoardHome getWorkReportDivisionBoardHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroupHome getWorkReportGroupHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportHome getWorkReportHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportMemberHome getWorkReportMemberHome() throws java.rmi.RemoteException;
 public java.lang.String getWorkReportSentText(int p0) throws java.rmi.RemoteException;
 public boolean isBoardMissingForDivisionWithMembersOrYearlyAccount(int p0) throws java.rmi.RemoteException;
 public boolean isThereAYearlyAccountForAnEmptyDivision(int p0) throws java.rmi.RemoteException;
 public boolean isWorkReportReadOnly(int p0) throws java.rmi.RemoteException;
 public boolean isYearlyAccountMissingForADivisionWithMembers(int p0) throws java.rmi.RemoteException;
 public boolean removeWorkReportGroupFromEntity(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1,com.idega.data.IDOEntity p2) throws java.rmi.RemoteException;
 public boolean removeWorkReportGroupFromEntity(int p0,java.lang.String p1,int p2,com.idega.data.IDOEntity p3) throws java.rmi.RemoteException;
 public boolean sendWorkReport(int p0,java.lang.String p1,com.idega.idegaweb.IWResourceBundle p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean unSendWorkReport(int p0) throws java.rmi.RemoteException;
}
