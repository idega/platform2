package is.idega.idegaweb.member.isi.block.reports.business;


public interface WorkReportImportBusiness extends com.idega.business.IBOService,is.idega.idegaweb.member.business.MemberUserBusiness
{
 public int exportToExcel(int p0,int p1,int p2)throws is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileHome getWorkReportExportFileHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportImportBoardMemberHome getWorkReportImportBoardMemberHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportImportClubAccountRecordHome getWorkReportImportClubAccountRecordHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportImportDivisionBoardHome getWorkReportImportDivisionBoardHome() throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportImportMemberHome getWorkReportImportMemberHome() throws java.rmi.RemoteException;
 public boolean importAccountPart(int p0,int p1)throws is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean importBoardPart(int p0,int p1)throws is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportException,java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportReport importMemberPart(int p0,int p1,java.lang.String p2)throws is.idega.idegaweb.member.isi.block.reports.business.WorkReportImportException,java.rmi.RemoteException, java.rmi.RemoteException;
}
