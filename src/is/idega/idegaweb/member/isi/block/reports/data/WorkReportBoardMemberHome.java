package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportBoardMemberHome extends com.idega.data.IDOHome
{
 public WorkReportBoardMember create() throws javax.ejb.CreateException;
 public WorkReportBoardMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportBoardMembersByWorkReportIdOrderedByMemberName(int p0)throws javax.ejb.FinderException;
 public WorkReportBoardMember findWorkReportBoardMemberByUserIdAndWorkReportId(int p0,int p1)throws javax.ejb.FinderException;
 public java.lang.String getFemaleGenderString();
 public java.lang.String getMaleGenderString();

}