package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportImportMemberHome extends com.idega.data.IDOHome
{
 public WorkReportImportMember create() throws javax.ejb.CreateException;
 public WorkReportImportMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportMembersByWorkReportId(int p0)throws javax.ejb.FinderException;
 public WorkReportImportMember findWorkReportMemberByUserIdAndWorkReportId(int p0,int p1)throws javax.ejb.FinderException;
 public java.lang.String getFemaleGenderString();
 public java.lang.String getMaleGenderString();

}