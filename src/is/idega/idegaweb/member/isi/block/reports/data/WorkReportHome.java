package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportHome extends com.idega.data.IDOHome
{
 public WorkReport create() throws javax.ejb.CreateException;
 public WorkReport findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public WorkReport findWorkReportByClubIdAndYearOfReport(int p0,int p1)throws javax.ejb.FinderException;

}