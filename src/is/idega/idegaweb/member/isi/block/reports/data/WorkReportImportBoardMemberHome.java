package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportImportBoardMemberHome extends com.idega.data.IDOHome
{
 public WorkReportImportBoardMember create() throws javax.ejb.CreateException;
 public WorkReportImportBoardMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportBoardMembersByWorkReportId(int p0)throws javax.ejb.FinderException;
 public WorkReportImportBoardMember findWorkReportBoardMemberByUserIdAndWorkReportId(int p0,int p1)throws javax.ejb.FinderException;
 public java.lang.String getFemaleGenderString();
 public java.lang.String getMaleGenderString();

}