package is.idega.idegaweb.member.isi.block.reports.business;

import is.idega.idegaweb.member.business.MemberUserBusiness;


public interface WorkReportBusiness extends com.idega.business.IBOService, MemberUserBusiness
{
 public boolean changeWorkReportGroupOfMember(is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember p2) throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember createWorkReportBoardMember(int p0,java.lang.String p1)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember createWorkReportMember(int p0,java.lang.String p1)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public java.util.Collection getAllLeagueWorkReportGroupsForYear(int p0) throws java.rmi.RemoteException;
 public java.util.Collection getAllWorkReportBoardMembersForWorkReportId(int p0) throws java.rmi.RemoteException;
 public java.util.Collection getAllWorkReportGroupsForYearAndType(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.util.Collection getAllWorkReportMembersForWorkReportId(int p0) throws java.rmi.RemoteException;
 public int getOrCreateWorkReportIdForClubIdByYear(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKeyHome getWorkReportAccountKeyHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMemberHome getWorkReportBoardMemberHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReport getWorkReportById(int p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubAccountRecordHome getWorkReportClubAccountRecordHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroupHome getWorkReportGroupHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportHome getWorkReportHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportMemberHome getWorkReportMemberHome() throws java.rmi.RemoteException;
 public boolean importAccountPart(int p0,int p1)throws is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportException, java.rmi.RemoteException;
 public boolean importBoardPart(int p0,int p1)throws is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportException, java.rmi.RemoteException;
 public boolean importMemberPart(int p0,int p1)throws is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportException, java.rmi.RemoteException;
}
