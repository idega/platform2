package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportMemberHome extends com.idega.data.IDOHome
{
 public WorkReportMember create() throws javax.ejb.CreateException;
 public WorkReportMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportBoardMembersByWorkReportId(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportMembersByWorkReportIdOrderedByMemberName(int p0)throws javax.ejb.FinderException;
 public WorkReportMember findWorkReportMemberByUserIdAndWorkReportId(int p0,int p1)throws javax.ejb.FinderException;
 public java.lang.String getFemaleGenderString();
 public java.lang.String getMaleGenderString();

}