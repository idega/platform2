package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportClubMemberHome extends com.idega.data.IDOHome
{
 public WorkReportClubMember create() throws javax.ejb.CreateException;
 public WorkReportClubMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllClubMembersByWorkReportIdOrderedByMemberName(int p0)throws javax.ejb.FinderException;
 public WorkReportClubMember findClubMemberByUserIdAndWorkReportIdOrderedByMemberName(int p0,int p1)throws javax.ejb.FinderException;
 public java.lang.String getFemaleGenderString();
 public java.lang.String getMaleGenderString();

}